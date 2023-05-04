package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Appointment} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto implements Serializable {

    @Schema (description = "The open day id", example = "1")
    private  Long openDayId;

    @Schema (description = "The mechanical action id", example = "1")
    private  Long mechanicalActionId;

    @Schema (description = "The comment", example = "I need to change the tires")
    private  String comment;

    @Schema (description = "The vehicle id", example = "1")
    private  Long vehicleId;

    @Schema (description = "The timeSlot selected by user")
    private  TimePeriod timeSlotSelected;

    @Schema (description = "The status of the appointment", example = "SCHEDULED")
    private  String status;

    @Schema (description = "Is mechanical action custom", example = "true")
    private  Boolean isMechanicalActionCustom;
}