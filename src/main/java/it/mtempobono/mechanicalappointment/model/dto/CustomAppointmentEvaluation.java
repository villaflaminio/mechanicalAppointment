package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.TimePeriod;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Appointment} entity
 */
public class CustomAppointmentEvaluation implements Serializable {

    //region Fields
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
    //endregion

    //region Constructors
    public CustomAppointmentEvaluation() {
    }

    public CustomAppointmentEvaluation(Long appointmentId, TimePeriod internalTime, TimePeriod externalTime, Double price, boolean isApproved) {
        this.appointmentId = appointmentId;
        this.internalTime = internalTime;
        this.externalTime = externalTime;
        this.price = price;
        this.isApproved = isApproved;
    }
    //endregion

    //region Getters & Setters

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public TimePeriod getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(TimePeriod internalTime) {
        this.internalTime = internalTime;
    }

    public TimePeriod getExternalTime() {
        return externalTime;
    }

    public void setExternalTime(TimePeriod externalTime) {
        this.externalTime = externalTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
    //endregion
}