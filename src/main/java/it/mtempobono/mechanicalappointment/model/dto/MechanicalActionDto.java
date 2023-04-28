package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.util.DurationWrapper;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.MechanicalAction} entity
 */
@Data
public class MechanicalActionDto implements Serializable {
    @Schema (description = "The name of the action", example = "Change oil")
    private final String name;

    @Schema (description = "The description of the action", example = "Change the oil of the car")
    private final String description;

    @Schema (description = "The price of the action", example = "50.0")
    private final Double price;

    @Schema (description = "If it is active or not", example = "true")
    private final Boolean isActive;

    @Schema (description = "The internal duration of the action")
    private final DurationWrapper internalDuration;

    @Schema (description = "The external duration of the action")
    private final DurationWrapper externalDuration;
}