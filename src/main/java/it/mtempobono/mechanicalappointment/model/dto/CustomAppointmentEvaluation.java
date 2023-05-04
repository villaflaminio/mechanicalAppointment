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
@AllArgsConstructor
@NoArgsConstructor
public class CustomAppointmentEvaluation implements Serializable {

    @Schema (description = "The id of the appointment" , example = "1")
    private  Long appointmentId;

    @Schema (description = "The internalTime selected by mecchanical")
    private  TimePeriod internalTime ;

    @Schema (description = "The externalTime selected by mecchanical")
    private  TimePeriod externalTime ;

    @Schema (description = "The price of the appointment" , example = "100.0")
    private  Double price;

    @Schema (description = "The custom appointment is approved by the mecchanic" , example = "true")
    private boolean isApproved;
}