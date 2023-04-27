package it.mtempobono.mechanicalappointment.model.builders;

import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;

public final class VehicleDtoBuilder {
    private Long id;
    private String plate;
    private String model;
    private String brand;
    private Integer year;
    private String fuel;
    private Boolean isActive;

    private VehicleDtoBuilder() {
    }

    public static VehicleDtoBuilder aVehicleDto() {
        return new VehicleDtoBuilder();
    }

    public VehicleDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public VehicleDtoBuilder plate(String plate) {
        this.plate = plate;
        return this;
    }

    public VehicleDtoBuilder model(String model) {
        this.model = model;
        return this;
    }

    public VehicleDtoBuilder brand(String brand) {
        this.brand = brand;
        return this;
    }

    public VehicleDtoBuilder year(Integer year) {
        this.year = year;
        return this;
    }

    public VehicleDtoBuilder fuel(String fuel) {
        this.fuel = fuel;
        return this;
    }

    public VehicleDtoBuilder isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public VehicleDto build() {
        return new VehicleDto(id, plate, model, brand, year, fuel, isActive);
    }
}
