package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Vehicle} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto implements Serializable {
    @Schema(description = "Unique identifier of the Vehicle.", example = "GJ123AA")
    private String plate;

    @Schema(description = "Model of the Vehicle.", example = "Urus")
    private String model;

    @Schema(description = "Brand of the Vehicle.", example = "Lamborghini")
    private String brand;

    @Schema(description = "Year of the Vehicle.", example = "2020")
    private Integer year;

    @Schema(description = "Fuel of the Vehicle.", example = "Gasoline")
    private String fuel;

    @Schema(description = "Is the Vehicle active.", example = "true")
    private Boolean isActive;

    @Schema(description = "User id of the Vehicle.", example = "1")
    private Long userId;
}