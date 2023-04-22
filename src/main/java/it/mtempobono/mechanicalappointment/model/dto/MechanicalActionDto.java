package it.mtempobono.mechanicalappointment.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.MechanicalAction} entity
 */
@Data
@Builder
public class MechanicalActionDto implements Serializable {
    private final Long id;
    private final String name;
    private final String description;
    private final Double price;
    private final Boolean isActive;
    private final Duration internalDuration;
    private final Duration externalDuration;
}