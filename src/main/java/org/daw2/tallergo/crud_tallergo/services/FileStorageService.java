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

/**
 * Servicio encargado de la persistencia física de archivos en el servidor.
 * Gestiona el almacenamiento de imágenes de perfil, documentos de vehículos o facturas.
 */
@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${app.upload-root}")
    private String uploadRootPath;

    private static final String UPLOADS_SUBDIR = "uploads";

    /**
     * Guarda un archivo en el sistema de ficheros con un nombre único (UUID).
     * @param file El archivo recibido desde el controlador.
     * @return La ruta web relativa para acceder al archivo (ej: /uploads/uuid.png).
     */
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);

            // Generamos un nombre único para evitar colisiones y sobrescrituras
            String uniqueFileName = UUID.randomUUID().toString();
            if (!fileExtension.isBlank()) {
                uniqueFileName += "." + fileExtension;
            }

            // Aseguramos que el directorio de destino existe
            Path uploadsDir = Paths.get(uploadRootPath).resolve(UPLOADS_SUBDIR);
            Files.createDirectories(uploadsDir);

            Path filePath = uploadsDir.resolve(uniqueFileName);

            // Escribimos los bytes en el disco
            Files.write(filePath, file.getBytes());

            logger.info("Archivo guardado con éxito: {}", uniqueFileName);
            return "/uploads/" + uniqueFileName;

        } catch (IOException e) {
            logger.error("Error crítico al intentar guardar el archivo: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Elimina un archivo del disco basándose en su ruta web o nombre.
     * @param filePathOrWebPath Ruta completa o relativa almacenada en la DB.
     */
    public void deleteFile(String filePathOrWebPath) {
        if (filePathOrWebPath == null || filePathOrWebPath.isBlank()) return;

        try {
            String fileName = normalizeFileName(filePathOrWebPath);
            Path uploadsDir = Paths.get(uploadRootPath).resolve(UPLOADS_SUBDIR);
            Path filePath = uploadsDir.resolve(fileName);

            if (Files.deleteIfExists(filePath)) {
                logger.info("Archivo eliminado del servidor: {}", fileName);
            }
        } catch (IOException e) {
            logger.error("Error al intentar eliminar el archivo {}: {}", filePathOrWebPath, e.getMessage(), e);
        }
    }

    /**
     * Extrae la extensión de un nombre de archivo (ej: image.jpg -> jpg).
     */
    private String getFileExtension(String fileName) {
        if (fileName != null) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0 && lastDot < fileName.length() - 1) {
                return fileName.substring(lastDot + 1);
            }
        }
        return "";
    }

    /**
     * Limpia la ruta para obtener solo el nombre del fichero real.
     */
    private String normalizeFileName(String filePathOrWebPath) {
        String value = filePathOrWebPath.trim();
        if (value.startsWith("/uploads/")) {
            value = value.substring("/uploads/".length());
        }
        int lastSlash = value.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < value.length() - 1) {
            value = value.substring(lastSlash + 1);
        }
        return value;
    }
}