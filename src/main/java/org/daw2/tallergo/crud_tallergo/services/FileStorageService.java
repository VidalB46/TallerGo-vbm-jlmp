package org.daw2.tallergo.crud_tallergo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${app.upload-root}")
    private String uploadRootPath;

    private static final String UPLOADS_SUBDIR = "uploads";

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFileName = UUID.randomUUID().toString();
            if (!fileExtension.isBlank()) uniqueFileName += "." + fileExtension;

            Path uploadsDir = Paths.get(uploadRootPath).resolve(UPLOADS_SUBDIR);
            Files.createDirectories(uploadsDir);
            Path filePath = uploadsDir.resolve(uniqueFileName);

            Files.write(filePath, file.getBytes());
            return "/uploads/" + uniqueFileName;
        } catch (IOException e) {
            logger.error("Error al guardar el archivo: {}", e.getMessage(), e);
            return null;
        }
    }

    public void deleteFile(String filePathOrWebPath) {
        if (filePathOrWebPath == null || filePathOrWebPath.isBlank()) return;
        try {
            String fileName = normalizeFileName(filePathOrWebPath);
            Path uploadsDir = Paths.get(uploadRootPath).resolve(UPLOADS_SUBDIR);
            Path filePath = uploadsDir.resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.error("Error al eliminar el archivo {}: {}", filePathOrWebPath, e.getMessage(), e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0 && lastDot < fileName.length() - 1) return fileName.substring(lastDot + 1);
        }
        return "";
    }

    private String normalizeFileName(String filePathOrWebPath) {
        String value = filePathOrWebPath.trim();
        if (value.startsWith("/uploads/")) value = value.substring("/uploads/".length());
        int lastSlash = value.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < value.length() - 1) value = value.substring(lastSlash + 1);
        return value;
    }
}