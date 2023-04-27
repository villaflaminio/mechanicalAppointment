package it.mtempobono.mechanicalappointment.model.dto;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.OpenDay} entity
 */
@Data
public class OpenDayDto implements Serializable {
    private final Long id;
    private final GarageDto garage;
    private final LocalDate date;
    private final Integer maxParallelAppointments;
    private final List<AppointmentDto> appointments;
    private final DayPlan workPlan;
}