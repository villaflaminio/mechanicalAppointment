package it.mtempobono.mechanicalappointment.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.Duration;

@Data
public class DurationWrapper {

    private int hours;
    private int minutes;

    public DurationWrapper(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    @JsonIgnore
    public Duration getDuration (){
        return Duration.ofHours(hours).plusMinutes(minutes);
    }
}
