package it.mtempobono.mechanicalappointment.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link it.mtempobono.mechanicalappointment.model.entity.Vehicle} entity
 */
@Data
public class VehicleDto implements Serializable {
    //region Fields
    @Schema(description = "Unique identifier of the Vehicle.", example = "GJ123AA")
    private String plate;

    @Schema(description = "Model of the Vehicle.", example = "Urus")
    private String model;

    @Schema(description = "Brand of the Vehicle.", example = "Lamborghini")
    private String brand;

    @Schema(description = "Year of the Vehicle.", example = "2020")
    private Integer year;

    @Schema(description = "Fuel of the Vehicle.", example = "Gasoline")
    private String fuel;

    @Schema(description = "Is the Vehicle active.", example = "true")
    private Boolean isActive;

    @Schema(description = "User id of the Vehicle.", example = "1")
    private Long userId;
    //endregion

    //region Constructors
    public VehicleDto() {
    }

    public VehicleDto(String plate, String model, String brand, Integer year, String fuel, Boolean isActive, Long userId) {
        this.plate = plate;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.fuel = fuel;
        this.isActive = isActive;
        this.userId = userId;
    }
    //endregion

    //region Getters & Setters methods

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    //endregion
}