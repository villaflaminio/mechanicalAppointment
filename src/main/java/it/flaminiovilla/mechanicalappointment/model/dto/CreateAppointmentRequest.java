package it.flaminiovilla.mechanicalappointment.model.dto;


import java.time.LocalDate;

public class CreateAppointmentRequest {

    //region Fields
    public Long openDayId;
    public Long mechanicalActionId;
    public Long vehicleId;
    public String comment;
    public LocalDate startTime;
    //endregion

    //region Constructors
    public CreateAppointmentRequest() {
    }

    public CreateAppointmentRequest(Long openDayId, Long mechanicalActionId, Long vehicleId, String comment, LocalDate startTime) {
        this.openDayId = openDayId;
        this.mechanicalActionId = mechanicalActionId;
        this.vehicleId = vehicleId;
        this.comment = comment;
        this.startTime = startTime;
    }
    //endregion

    //region Getters & Setters


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

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }
    // endregion
}
