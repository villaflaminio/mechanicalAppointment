package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Appointment} entity
 */
@Data
public class CustomAppointmentEvaluation implements Serializable {

    private final Long appointmentId;

    @Schema (description = "The internalTime selected by mecchanical")
    private final TimePeriod internalTime ;

    @Schema (description = "The externalTime selected by mecchanical")
    private final TimePeriod externalTime ;

    @Schema (description = "The price of the appointment" , example = "100.0")
    private final Double price;

    @Schema (description = "The custom appointment is approved by the mecchanic" , example = "true")
    private boolean isApproved;
}