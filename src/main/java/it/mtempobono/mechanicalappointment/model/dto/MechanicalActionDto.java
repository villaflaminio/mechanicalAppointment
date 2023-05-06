package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.util.wrappers.DurationWrapper;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.MechanicalAction} entity
 */
public class MechanicalActionDto implements Serializable {
    //region Fields
    @Schema(description = "The name of the action", example = "Change oil")
    private String name;

    @Schema(description = "The description of the action", example = "Change the oil of the car")
    private String description;

    @Schema(description = "The price of the action", example = "50.0")
    private Double price;

    @Schema(description = "If it is active or not", example = "true")
    private Boolean isActive;

    @Schema(description = "The internal duration of the action")
    private DurationWrapper internalDuration;

    @Schema(description = "The external duration of the action")
    private DurationWrapper externalDuration;
    //endregion

    //region Constructors
    public MechanicalActionDto() {
    }

    public MechanicalActionDto(String name, String description, Double price, Boolean isActive, DurationWrapper internalDuration, DurationWrapper externalDuration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.internalDuration = internalDuration;
        this.externalDuration = externalDuration;
    }
    //endregion

    //region Getters & Setters methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public DurationWrapper getInternalDuration() {
        return internalDuration;
    }

    public void setInternalDuration(DurationWrapper internalDuration) {
        this.internalDuration = internalDuration;
    }

    public DurationWrapper getExternalDuration() {
        return externalDuration;
    }

    public void setExternalDuration(DurationWrapper externalDuration) {
        this.externalDuration = externalDuration;
    }
    //endregion
}