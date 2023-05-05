package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.TimePeriod;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Appointment} entity
 */
public class AppointmentDto implements Serializable {

    //region Fields
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
    //endregion

    //region Constructors
    public AppointmentDto() {
    }

    public AppointmentDto(Long openDayId, Long mechanicalActionId, String comment, Long vehicleId, TimePeriod timeSlotSelected, String status, Boolean isMechanicalActionCustom) {
        this.openDayId = openDayId;
        this.mechanicalActionId = mechanicalActionId;
        this.comment = comment;
        this.vehicleId = vehicleId;
        this.timeSlotSelected = timeSlotSelected;
        this.status = status;
        this.isMechanicalActionCustom = isMechanicalActionCustom;
    }
    //endregion

    //region Getters and Setters

    public Long getOpenDayId() {
        return openDayId;
    }

    public void setOpenDayId(Long openDayId) {
        this.openDayId = openDayId;
    }

    public Long getMechanicalActionId() {
        return mechanicalActionId;
    }

    public void setMechanicalActionId(Long mechanicalActionId) {
        this.mechanicalActionId = mechanicalActionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public TimePeriod getTimeSlotSelected() {
        return timeSlotSelected;
    }

    public void setTimeSlotSelected(TimePeriod timeSlotSelected) {
        this.timeSlotSelected = timeSlotSelected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsMechanicalActionCustom() {
        return isMechanicalActionCustom;
    }

    public void setIsMechanicalActionCustom(Boolean mechanicalActionCustom) {
        isMechanicalActionCustom = mechanicalActionCustom;
    }

    //endregion
}