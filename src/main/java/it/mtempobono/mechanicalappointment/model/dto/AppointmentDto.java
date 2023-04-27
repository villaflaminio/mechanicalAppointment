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
    
    private final Long id;
    @Schema (description = "The open day id", example = "1")
    private final Long openDay;

    @Schema (description = "The mechanical action id", example = "1")
    private final Long mechanicalAction;

    @Schema (description = "The comment", example = "I need to change the tires")
    private final String comment;

    @Schema (description = "The vehicle id", example = "1")
    private final Long vehicle;

    @Schema (description = "The status", example = "SCHEDULED")
    private final AppointmentStatus status;

    @Schema (description = "The external time")
    private final TimePeriod externalTime;

    @Schema (description = "The internal time")
    private final TimePeriod internalTime;

    @Schema (description = "The price", example = "100.0")
    private final Double price;

    @Schema (description = "The id calendar event", example = "ABC123")
    private final String idCalendarEvent;
}