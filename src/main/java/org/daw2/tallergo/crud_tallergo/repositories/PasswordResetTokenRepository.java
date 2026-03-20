package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de tokens de recuperación de contraseña.
 * Incluye lógica de seguridad para la invalidación y limpieza de credenciales temporales.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Busca un token específico por su valor hasheado.
     * Es fundamental buscar por el hash y no por el token en texto plano por seguridad.
     */
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    /**
     * Invalida todos los tokens activos de un usuario marcándolos como usados.
     * Se utiliza normalmente cuando un usuario solicita un nuevo token o acaba de cambiar su clave,
     * garantizando que los tokens antiguos dejen de ser válidos inmediatamente.
     * * @param userId ID del usuario.
     * @param now Fecha y hora actual para el registro.
     * @return Número de tokens invalidados.
     */
    @Modifying
    @Query("update PasswordResetToken t set t.usedAt = :now where t.user.id = :userId and t.usedAt is null")
    int invalidateAllActiveTokensForUser(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    /**
     * Tarea de limpieza para eliminar tokens obsoletos (expirados o ya utilizados).
     * Ayuda a mantener la tabla con un tamaño controlado y elimina datos sensibles innecesarios.
     * * @param now Fecha de corte para considerar la expiración.
     * @return Número de registros eliminados.
     */
    @Modifying
    @Query("delete from PasswordResetToken t where t.expiresAt < :now or t.usedAt is not null")
    int deleteOldTokens(@Param("now") LocalDateTime now);
}