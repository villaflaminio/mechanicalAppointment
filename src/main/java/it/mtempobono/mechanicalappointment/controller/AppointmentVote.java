package it.mtempobono.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentVote implements java.io.Serializable {
    @Schema(example = "4")
    @Max(value = 5, message = "The rating must be between 0 and 5")
    @Min(value = 0, message = "The rating must be between 0 and 5")
    private Integer rating;
    @Schema(example = "Very good service")
    private String comment;
    @Schema(example = "1")
    private Long appointmentId;
}
