package it.mtempobono.mechanicalappointment.model.dto;

import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.entity.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Appointment} entity
 */
@Data
@Builder
public class AppointmentDto implements Serializable {
    private final Long id;
    private final OpenDayDto openDay;
    private final MechanicalActionDto mechanicalAction;
    private final String comment;
    private final VehicleDto vehicle;
    private final AppointmentStatus status;
    private final TimePeriod externalTime;
    private final TimePeriod internalTime;
    private final Double price;
}