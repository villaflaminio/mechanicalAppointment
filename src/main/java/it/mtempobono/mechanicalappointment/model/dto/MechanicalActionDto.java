package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.util.wrappers.DurationWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.MechanicalAction} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MechanicalActionDto implements Serializable {
    @Schema(description = "The name of the action", example = "Change oil")
    private String name;

    @Schema(description = "The description of the action", example = "Change the oil of the car")
    private String description;

    @Schema(description = "The price of the action", example = "50.0")
    private Double price;

    @Schema(description = "If it is active or not", example = "true")
    private Boolean isActive;

    @Schema(description = "The internal duration of the action")
    private DurationWrapper internalDuration;

    @Schema(description = "The external duration of the action")
    private DurationWrapper externalDuration;
}