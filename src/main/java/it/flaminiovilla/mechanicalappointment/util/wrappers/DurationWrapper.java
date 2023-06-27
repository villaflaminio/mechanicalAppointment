package it.flaminiovilla.mechanicalappointment.util.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;


public class DurationWrapper {

    //region Fields
    private int hours;
    private int minutes;
    //endregion

    //region Constructors
    public DurationWrapper() {
    }

    public DurationWrapper(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    ///
    /// This constructor is used to convert a string like "PT2H30M" or "PT1H" to a DurationWrapper
    ///
    public DurationWrapper(String duration) {

        Duration durationDate = Duration.parse(duration);

        this.hours = (int) durationDate.toHours();
        this.minutes = (int) durationDate.minusHours(this.hours).toMinutes();
    }

    @JsonIgnore
    public Duration getDuration() {
        return Duration.ofHours(hours).plusMinutes(minutes);
    }
    //endregion

    // region Getters and Setters

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    //endregion
}
