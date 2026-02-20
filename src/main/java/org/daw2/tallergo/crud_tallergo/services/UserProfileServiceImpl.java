package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.UserProfileFormDTO;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.UserProfile;
import org.daw2.tallergo.crud_tallergo.exceptions.InvalidFileException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.UserProfileMapper;
import org.daw2.tallergo.crud_tallergo.repositories.UserProfileRepository;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);
    private static final long MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public UserProfileFormDTO getFormByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(user.getId());
        UserProfile profile = profileOpt.orElse(null);

        return UserProfileMapper.toFormDto(user, profile);
    }

    @Override
    public void updateProfile(String email, UserProfileFormDTO profileDto, MultipartFile profileImageFile) {
        logger.info("Actualizando perfil para email={}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        Long userId = user.getId();
        UserProfile profile = userProfileRepository.findByUserId(userId).orElse(null);
        boolean isNew = (profile == null);

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            validateProfileImage(profileImageFile);
            String oldImagePath = profileDto.getProfileImage();
            String newImageWebPath = fileStorageService.saveFile(profileImageFile);

            if (newImageWebPath == null || newImageWebPath.isBlank()) {
                throw new InvalidFileException("userProfile", "profileImageFile", profileImageFile.getOriginalFilename(), "No se pudo guardar la imagen.");
            }
            profileDto.setProfileImage(newImageWebPath);

            if (oldImagePath != null && !oldImagePath.isBlank()) {
                fileStorageService.deleteFile(oldImagePath);
            }
        }

        if (isNew) {
            profile = UserProfileMapper.toNewEntity(profileDto, user);
        } else {
            UserProfileMapper.copyToExistingEntity(profileDto, profile);
        }
        userProfileRepository.save(profile);
    }

    private void validateProfileImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("userProfile", "profileImageFile", contentType, "Tipo de archivo no permitido");
        }
        if (file.getSize() > MAX_IMAGE_SIZE_BYTES) {
            throw new InvalidFileException("userProfile", "profileImageFile", file.getSize(), "Archivo demasiado grande");
        }
    }
}