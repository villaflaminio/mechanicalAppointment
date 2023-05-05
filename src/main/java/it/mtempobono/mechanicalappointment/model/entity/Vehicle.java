package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate")
    private String plate;

    @Column(name = "model")
    private String model;

    @Column(name = "brand")
    private String brand;

    @Column(name = "year_car")
    private Integer year;

    @Column(name = "fuel")
    private String fuel;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonBackReference(value = "user-vehicle")
    private User user;

    @OneToMany(mappedBy = "vehicle")
    @JsonBackReference(value = "vehicle-appointment")
    private List<Appointment> reservation;
    //endregion

    //region Constructors
    public Vehicle() {
    }

    public Vehicle(Long id, String plate, String model, String brand, Integer year, String fuel, Boolean isActive, User user, List<Appointment> reservation) {
        this.id = id;
        this.plate = plate;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.fuel = fuel;
        this.isActive = isActive;
        this.user = user;
        this.reservation = reservation;
    }

    // endregion

    //region Getters & Setters methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Appointment> getReservation() {
        return reservation;
    }

    public void setReservation(List<Appointment> reservation) {
        this.reservation = reservation;
    }

    //endregion
}
