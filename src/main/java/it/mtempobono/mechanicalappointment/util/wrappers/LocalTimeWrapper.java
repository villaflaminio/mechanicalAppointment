package it.mtempobono.mechanicalappointment.util.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalTimeWrapper implements Serializable {
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
}
