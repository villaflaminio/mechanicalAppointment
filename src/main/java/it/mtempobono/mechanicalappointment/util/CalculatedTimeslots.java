package it.mtempobono.mechanicalappointment.util;

import it.mtempobono.mechanicalappointment.model.TimePeriod;
import lombok.Data;

import java.util.List;

@Data
public class CalculatedTimeslots {
    List<TimePeriod> availableHoursOnInteralTime;
    List<TimePeriod> availableHoursOnExternalTime;

    public CalculatedTimeslots(List<TimePeriod> availableHoursOnInteralTime, List<TimePeriod> availableHoursOnExternalTime) {
        this.availableHoursOnInteralTime = availableHoursOnInteralTime;
        this.availableHoursOnExternalTime = availableHoursOnExternalTime;
    }

}