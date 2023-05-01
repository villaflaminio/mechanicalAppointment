package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.OpenDay} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "The OpenDay DTO")
public class OpenDayDto implements Serializable {

    @Schema(description = "The garage id", example = "1")
    private  Long garageId;

    @Schema(description = "The date of the open day", example = "2021-01-01")
    private  LocalDate date;

    @Schema(description = "The max number of parallel appointments", example = "2")
    private  Integer maxParallelAppointments;

    @Schema(description = "The linked day plan")
    private  DayPlan workPlan;
}