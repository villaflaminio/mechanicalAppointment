package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.entity.AppointmentStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Appointment} entity
 */
@Data
public class AppointmentDto implements Serializable {

    @Schema (description = "The open day id", example = "1")
    private final Long openDayId;//

    @Schema (description = "The mechanical action id", example = "1")
    private final Long mechanicalActionId;//

    @Schema (description = "The comment", example = "I need to change the tires")
    private final String comment;//

    @Schema (description = "The vehicle id", example = "1")
    private final Long vehicleId;//

    @Schema (description = "The timeSlot selected by user")
    private final TimePeriod timeSlotSelected;

    @Schema (description = "The status of the appointment", example = "SCHEDULED")
    private final String status;//

    @Schema (description = "Is mechanical action custom", example = "true")
    private final Boolean isMechanicalActionCustom;
}