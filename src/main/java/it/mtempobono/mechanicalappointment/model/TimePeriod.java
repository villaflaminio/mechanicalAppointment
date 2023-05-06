package it.mtempobono.mechanicalappointment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.util.wrappers.LocalTimeWrapper;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

/**
 * Class to manage time periods in a working day
 */
public class TimePeriod implements Comparable<TimePeriod>, Serializable{
    // region Fields
    @Schema(description = "The start time")
    private LocalTimeWrapper start;

    @Schema(description = "The end time")
    private LocalTimeWrapper end;
    // endregion Fields

    // region Constructors
    public TimePeriod() {
    }

    public TimePeriod(LocalTime start, LocalTime end) {
        LocalTimeWrapper localTimeWrapperStart = new LocalTimeWrapper(
                start.getHour(),
                start.getMinute(),
                start.getSecond(),
                start.getNano()
        );

        LocalTimeWrapper localTimeWrapperEnd = new LocalTimeWrapper(
                end.getHour(),
                end.getMinute(),
                end.getSecond(),
                end.getNano()
        );

        this.start = localTimeWrapperStart;
        this.end = localTimeWrapperEnd;
    }

    @JsonIgnore
    public Duration getDuration() {
        return Duration.between(this.start.getLocalTime(), this.end.getLocalTime());
    }
    // endregion Constructors

    // region Methods
    public LocalTimeWrapper getStart() {
        return start;
    }

    public void setStart(LocalTimeWrapper start) {
        this.start = start;
    }

    public LocalTimeWrapper getEnd() {
        return end;
    }

    public void setEnd(LocalTimeWrapper end) {
        this.end = end;
    }

    // endregion Methods

    // region Overrides
    @Override
    public int compareTo(TimePeriod o) {
        return this.getStart().getLocalTime().compareTo(o.getStart().getLocalTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod peroid = (TimePeriod) o;
        return this.start.equals(peroid.getStart()) &&
                this.end.equals(peroid.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    // endregion Overrides
}
