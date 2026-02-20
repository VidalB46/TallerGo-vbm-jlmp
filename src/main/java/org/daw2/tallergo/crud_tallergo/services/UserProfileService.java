package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.UserProfileFormDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    UserProfileFormDTO getFormByEmail(String email);
    void updateProfile(String email, UserProfileFormDTO profileDto, MultipartFile profileImageFile);
}