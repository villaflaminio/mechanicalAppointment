package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Vehicle} entity
 */
@Data
public class VehicleDto implements Serializable {
    @Schema (description = "The user id", example = "1")
    private final Long userId;
    @Schema (description = "The plate", example = "AA123BB")
    private final String plate;
    @Schema (description = "The model", example = "Panda")
    private final String model;
    @Schema (description = "The brand", example = "Fiat")
    private final String brand;
    @Schema (description = "The year", example = "2010")
    private final Integer year;
    @Schema (description = "The fuel", example = "Diesel")
    private final String fuel;
    @Schema (description = "Is active", example = "true")
    private final Boolean isActive;

}