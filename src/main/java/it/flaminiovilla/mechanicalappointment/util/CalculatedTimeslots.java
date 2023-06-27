package it.flaminiovilla.mechanicalappointment.util;

import it.flaminiovilla.mechanicalappointment.model.TimePeriod;

import java.util.List;

public class CalculatedTimeslots {
    //region Fields
    List<TimePeriod> availableHoursOnInteralTime;
    List<TimePeriod> availableHoursOnExternalTime;
    //endregion

    //region Constructors
    public CalculatedTimeslots() {
    }

    public CalculatedTimeslots(List<TimePeriod> availableHoursOnInteralTime, List<TimePeriod> availableHoursOnExternalTime) {
        this.availableHoursOnInteralTime = availableHoursOnInteralTime;
        this.availableHoursOnExternalTime = availableHoursOnExternalTime;
    }
    //endregion

    //region Getters and Setters


    public List<TimePeriod> getAvailableHoursOnInteralTime() {
        return availableHoursOnInteralTime;
    }

    public void setAvailableHoursOnInteralTime(List<TimePeriod> availableHoursOnInteralTime) {
        this.availableHoursOnInteralTime = availableHoursOnInteralTime;
    }

    public List<TimePeriod> getAvailableHoursOnExternalTime() {
        return availableHoursOnExternalTime;
    }

    public void setAvailableHoursOnExternalTime(List<TimePeriod> availableHoursOnExternalTime) {
        this.availableHoursOnExternalTime = availableHoursOnExternalTime;
    }
    //endregion
}