package org.daw2.tallergo.crud_tallergo.services;

/**
 * Interfaz para la gestión del flujo de recuperación de contraseñas.
 * Define las acciones necesarias para iniciar el proceso mediante un token
 * y finalizarlo estableciendo una nueva credencial.
 */
public interface PasswordResetService {

    /**
     * Inicia el proceso de recuperación.
     * Genera un token seguro, lo asocia al usuario y envía un correo con el enlace.
     * * @param email      Correo del usuario que solicita el reset.
     * @param requestIp  Dirección IP desde la que se realiza la petición (por seguridad/auditoría).
     * @param userAgent  Navegador/Dispositivo desde el que se solicita (por seguridad/auditoría).
     */
    void requestPasswordReset(String email, String requestIp, String userAgent);

    /**
     * Completa el proceso de recuperación utilizando el token recibido por email.
     * Valida que el token sea correcto, no haya expirado y no haya sido usado.
     * * @param rawToken    El token en texto plano recibido por el usuario.
     * @param newPassword La nueva contraseña (se debe hashear antes de guardar).
     */
    void resetPassword(String rawToken, String newPassword);
}