package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;

/**
 * Clase utilitaria para mapear entidades de tipo {@link Appointment} (Citas)
 * a sus respectivos Data Transfer Objects (DTOs) y viceversa.
 * Facilita la transferencia segura de datos entre las capas de la aplicación.
 */
public class AppointmentMapper {

    /**
     * Convierte una entidad Appointment completa en un DTO ligero.
     * Ideal para listados y tablas donde no se requiere toda la información anidada.
     *
     * @param entity La entidad Appointment originada en la base de datos.
     * @return {@link AppointmentDTO} con los datos básicos y nombres concatenados, o null si la entidad es nula.
     */
    public static AppointmentDTO toDTO(Appointment entity) {
        if (entity == null) return null;

        return AppointmentDTO.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .status(entity.getStatus())
                .vehicleModel(entity.getVehicle() != null ? entity.getVehicle().getMatricula() + " - " + entity.getVehicle().getModel() : "Desconocido")
                .workshopName(entity.getWorkshop() != null ? entity.getWorkshop().getName() : "Desconocido")
                .userEmail(entity.getUser() != null ? entity.getUser().getEmail() : "Desconocido")
                .build();
    }

    /**
     * Convierte una entidad Appointment en un DTO detallado.
     * Utilizado principalmente para las vistas de "Detalle de Cita", ya que incluye objetos
     * anidados (Vehículo, Taller) y sincronización con el estado de las Reparaciones y Presupuestos.
     *
     * @param entity La entidad Appointment originada en la base de datos.
     * @return {@link AppointmentDetailDTO} con todos los datos expandidos, o null si la entidad es nula.
     */
    public static AppointmentDetailDTO toDetailDTO(Appointment entity) {
        if (entity == null) return null;

        AppointmentDetailDTO dto = new AppointmentDetailDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        dto.setMediaUrl(entity.getMediaUrl());

        dto.setUserEmail(entity.getUser() != null ? entity.getUser().getEmail() : "Desconocido");
        dto.setVehicle(VehicleMapper.toDTO(entity.getVehicle()));
        dto.setWorkshop(WorkshopMapper.toDTO(entity.getWorkshop()));

        // Sincronización con la Reparación y el Presupuesto
        if (entity.getRepair() != null) {
            dto.setRepairId(entity.getRepair().getId());
            dto.setHasBudget(entity.getRepair().getBudget() != null);

            // CORRECCIÓN APLICADA: Se usa getAccepted() y validación segura con Boolean.TRUE.equals()
            dto.setIsBudgetAccepted(
                    entity.getRepair().getBudget() != null &&
                            Boolean.TRUE.equals(entity.getRepair().getBudget().getAccepted())
            );
        } else {
            dto.setHasBudget(false);
            dto.setIsBudgetAccepted(false);
        }

        return dto;
    }

    /**
     * Convierte un DTO de creación de cita en una nueva Entidad Appointment.
     * Inyecta las entidades relacionadas previamente buscadas en la base de datos y asigna
     * el estado inicial por defecto.
     *
     * @param dto      Datos introducidos por el usuario en el formulario.
     * @param user     Entidad del cliente solicitante.
     * @param workshop Entidad del taller seleccionado.
     * @param vehicle  Entidad del vehículo a reparar.
     * @return Nueva entidad {@link Appointment} lista para ser persistida, o null si el dto es nulo.
     */
    public static Appointment toEntity(AppointmentCreateDTO dto, User user, Workshop workshop, Vehicle vehicle) {
        if (dto == null) return null;

        Appointment entity = new Appointment();
        entity.setStartDate(dto.getStartDate());
        entity.setNotes(dto.getNotes());

        // ¡MAGIA DE LAS IMÁGENES! Pasamos la ruta web a la entidad
        entity.setMediaUrl(dto.getMediaUrl());

        // Toda cita recién creada inicia en estado SOLICITADO
        entity.setStatus(AppointmentStatus.SOLICITADO);

        entity.setUser(user);
        entity.setWorkshop(workshop);
        entity.setVehicle(vehicle);

        return entity;
    }

    /**
     * Actualiza una entidad Appointment existente con los datos proporcionados desde un DTO de actualización.
     * Solo modifica los campos que no son nulos en el DTO, protegiendo así los datos existentes.
     *
     * @param dto    DTO con los nuevos datos a aplicar.
     * @param entity Entidad Appointment que será modificada (pasada por referencia).
     */
    public static void updateEntity(AppointmentUpdateDTO dto, Appointment entity) {
        if (dto == null || entity == null) return;

        if (dto.getStartDate() != null) {
            entity.setStartDate(dto.getStartDate());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
    }
}