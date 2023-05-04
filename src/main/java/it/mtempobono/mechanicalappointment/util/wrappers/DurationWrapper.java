package it.mtempobono.mechanicalappointment.util.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@Builder
public class DurationWrapper {

    private int hours;
    private int minutes;

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
}
