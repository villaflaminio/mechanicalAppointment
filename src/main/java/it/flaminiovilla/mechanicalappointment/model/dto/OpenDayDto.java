package it.flaminiovilla.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.flaminiovilla.mechanicalappointment.model.entity.OpenDay;
import it.flaminiovilla.mechanicalappointment.model.DayPlan;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link OpenDay} entity
 */
public class OpenDayDto implements Serializable {

    //region Fields
    @Schema(description = "The garage id", example = "1")
    private  Long garageId;

    @Schema(description = "The date of the open day", example = "2021-01-01")
    private  LocalDate date;

    @Schema(description = "The max number of parallel appointments", example = "2")
    private  Integer maxParallelAppointments;

    @Schema(description = "The linked day plan")
    private  DayPlan workPlan;
    //endregion

    //region Constructors
    public OpenDayDto() {
    }

    public OpenDayDto(Long garageId, LocalDate date, Integer maxParallelAppointments, DayPlan workPlan) {
        this.garageId = garageId;
        this.date = date;
        this.maxParallelAppointments = maxParallelAppointments;
        this.workPlan = workPlan;
    }
    //endregion

    //region Getters & Setters methods

    public Long getGarageId() {
        return garageId;
    }

    public void setGarageId(Long garageId) {
        this.garageId = garageId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getMaxParallelAppointments() {
        return maxParallelAppointments;
    }

    public void setMaxParallelAppointments(Integer maxParallelAppointments) {
        this.maxParallelAppointments = maxParallelAppointments;
    }

    public DayPlan getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(DayPlan workPlan) {
        this.workPlan = workPlan;
    }
    //endregion
}