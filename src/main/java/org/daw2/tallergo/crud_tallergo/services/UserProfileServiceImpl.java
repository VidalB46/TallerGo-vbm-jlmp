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

/**
 * Implementación del servicio de perfiles de usuario.
 * Orquestra la lógica de persistencia de datos personales y la gestión de archivos multimedia (avatar).
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    // Límite de 2MB para la imagen de perfil
    private static final long MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Recupera el perfil unificado (datos de cuenta + datos personales) para el formulario de edición.
     *
     * @param email Email del usuario autenticado.
     * @return DTO con los campos editables del perfil del usuario.
     * @throws ResourceNotFoundException si no existe ningún usuario con ese email.
     */
    @Override
    @Transactional(readOnly = true)
    public UserProfileFormDTO getFormByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);

        return UserProfileMapper.toFormDto(user, profile);
    }

    /**
     * Actualiza el perfil de un usuario: datos personales e imagen de avatar.
     * <p>Si se sube una nueva imagen, primero se valida, se almacena y finalmente
     * se elimina la imagen anterior del disco. Si no se sube imagen, se conserva la actual.</p>
     *
     * @param email            Email del usuario autenticado cuyo perfil se actualiza.
     * @param profileDto       DTO con los nuevos valores del perfil.
     * @param profileImageFile Nuevo archivo de imagen de perfil (puede ser {@code null} o vacío).
     * @throws ResourceNotFoundException si no existe ningún usuario con ese email.
     * @throws InvalidFileException      si el archivo no es una imagen o supera los 2 MB.
     */
    @Override
    public void updateProfile(String email, UserProfileFormDTO profileDto, MultipartFile profileImageFile) {
        logger.info("Iniciando actualización de perfil para: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);
        boolean isNew = (profile == null);

        // --- Gestión de la imagen de perfil ---
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            validateProfileImage(profileImageFile);

            // Guardamos la ruta de la imagen vieja para borrarla si la subida de la nueva tiene éxito
            String oldImagePath = (profile != null) ? profile.getProfileImage() : null;

            String newImageWebPath = fileStorageService.saveFile(profileImageFile);

            if (newImageWebPath == null || newImageWebPath.isBlank()) {
                throw new InvalidFileException("userProfile", "profileImageFile",
                        profileImageFile.getOriginalFilename(), "Error interno al procesar el almacenamiento del archivo.");
            }

            // Actualizamos el DTO con la nueva ruta para que el mapper la pase a la entidad
            profileDto.setProfileImage(newImageWebPath);

            // Cleanup: Borramos la imagen anterior del disco para no dejar basura
            if (oldImagePath != null && !oldImagePath.isBlank()) {
                fileStorageService.deleteFile(oldImagePath);
            }
        }

        // --- Persistencia de datos ---
        if (isNew) {
            profile = UserProfileMapper.toNewEntity(profileDto, user);
        } else {
            UserProfileMapper.copyToExistingEntity(profileDto, profile);
        }

        userProfileRepository.save(profile);
        logger.info("Perfil de usuario {} actualizado correctamente.", email);
    }

    /**
     * Valida el tipo MIME y el tamaño de la imagen de perfil subida.
     *
     * @param file Archivo a validar.
     * @throws InvalidFileException si el tipo MIME no es una imagen o el tamaño supera los 2 MB.
     */
    private void validateProfileImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("userProfile", "profileImageFile", contentType, "Solo se permiten archivos de imagen.");
        }
        if (file.getSize() > MAX_IMAGE_SIZE_BYTES) {
            throw new InvalidFileException("userProfile", "profileImageFile", file.getSize(), "La imagen excede el límite de 2MB.");
        }
    }
}