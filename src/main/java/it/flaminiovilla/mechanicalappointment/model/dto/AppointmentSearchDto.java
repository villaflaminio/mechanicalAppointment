package it.flaminiovilla.mechanicalappointment.model.dto;

import java.io.Serializable;

public class AppointmentSearchDto implements Serializable {
    //region Fields
    private Long opendayId;
    private Long mechanicalActionId;
    private boolean externalTimeslot;
    //endregion

    //region Constructors
    public AppointmentSearchDto() {
    }

    public AppointmentSearchDto(Long opendayId, Long mechanicalActionId, boolean externalTimeslot) {
        this.opendayId = opendayId;
        this.mechanicalActionId = mechanicalActionId;
        this.externalTimeslot = externalTimeslot;
    }
    //endregion

    //region Getters & Setters


    public Long getOpendayId() {
        return opendayId;
    }

    public void setOpendayId(Long opendayId) {
        this.opendayId = opendayId;
    }

    public Long getMechanicalActionId() {
        return mechanicalActionId;
    }

    public void setMechanicalActionId(Long mechanicalActionId) {
        this.mechanicalActionId = mechanicalActionId;
    }

    public boolean isExternalTimeslot() {
        return externalTimeslot;
    }

    public void setExternalTimeslot(boolean externalTimeslot) {
        this.externalTimeslot = externalTimeslot;
    }
    //endregion
}
