package it.mtempobono.mechanicalappointment.model.builders;

import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.User;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;

import java.util.List;

public final class VehicleBuilder {
    private Long id;
    private String plate;
    private String model;
    private String brand;
    private Integer year;
    private String fuel;
    private Boolean isActive;
    private User user;
    private List<Appointment> reservation;

    private VehicleBuilder() {
    }

    public static VehicleBuilder aVehicle() {
        return new VehicleBuilder();
    }

    public VehicleBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public VehicleBuilder plate(String plate) {
        this.plate = plate;
        return this;
    }

    public VehicleBuilder model(String model) {
        this.model = model;
        return this;
    }

    public VehicleBuilder brand(String brand) {
        this.brand = brand;
        return this;
    }

    public VehicleBuilder year(Integer year) {
        this.year = year;
        return this;
    }

    public VehicleBuilder fuel(String fuel) {
        this.fuel = fuel;
        return this;
    }

    public VehicleBuilder isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public VehicleBuilder user(User user) {
        this.user = user;
        return this;
    }

    public VehicleBuilder reservation(List<Appointment> reservation) {
        this.reservation = reservation;
        return this;
    }

    public Vehicle build() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setPlate(plate);
        vehicle.setModel(model);
        vehicle.setBrand(brand);
        vehicle.setYear(year);
        vehicle.setFuel(fuel);
        vehicle.setIsActive(isActive);
        vehicle.setUser(user);
        vehicle.setReservation(reservation);
        return vehicle;
    }
}
