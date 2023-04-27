package it.mtempobono.mechanicalappointment.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Vehicle} entity
 */
@Data
public class VehicleDto implements Serializable {
    private final Long id;
    private final String plate;
    private final String model;
    private final String brand;
    private final Integer year;
    private final String fuel;
    private final Boolean isActive;
}