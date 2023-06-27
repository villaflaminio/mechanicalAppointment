package it.flaminiovilla.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class AppointmentVote implements java.io.Serializable {

    //region Fields
    private Long id;

    @Schema(example = "4")
    @Max(value = 5, message = "The rating must be between 0 and 5")
    @Min(value = 0, message = "The rating must be between 0 and 5")
    private Integer rating;
    @Schema(example = "Very good service")
    private String comment;
    @Schema(example = "1")
    private Long appointmentId;
    //endregion

    //region Constructors
    public AppointmentVote() {
    }

    public AppointmentVote(Long id, Integer rating, String comment, Long appointmentId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.appointmentId = appointmentId;
    }
    //endregion

    //region Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    //endregion
}
