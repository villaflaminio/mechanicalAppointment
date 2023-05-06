package it.mtempobono.mechanicalappointment.util.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.TimePeriod;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class LocalTimeWrapper implements Serializable {
    //region Fields
    @Schema(example = "10")
    private int hour;
    @Schema(example = "30")
    private int minute;
    @Schema(example = "0")
    private int second;
    @Schema(example = "0")
    private int nano;

    @JsonIgnore
    public LocalTime getLocalTime() {
        return LocalTime.of(hour, minute, second, nano);
    }
    //endregion

    //region Constructors
    public LocalTimeWrapper() {
    }

    public LocalTimeWrapper(int hour, int minute, int second, int nano) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.nano = nano;
    }
    //endregion

    //region Getters & Setters

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getNano() {
        return nano;
    }

    public void setNano(int nano) {
        this.nano = nano;
    }
    //endregion

    @Override
    public int hashCode() {
        return Objects.hash(hour, minute, second, nano);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalTimeWrapper)) return false;
        LocalTimeWrapper that = (LocalTimeWrapper) o;
        return hour == that.hour &&
                minute == that.minute &&
                second == that.second &&
                nano == that.nano;
    }
}
