package it.mtempobono.mechanicalappointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.util.converters.DayPlanConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "open_days")
public class OpenDay {

    //region Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Garage garage;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "max_parallel_appointments")
    private Integer maxParallelAppointments;

    @OneToMany(mappedBy = "openDay")
    private List<Appointment> appointments;

    @Convert(converter = DayPlanConverter.class)
    private DayPlan workPlan;
    //endregion

    //region Constructors
    public OpenDay() {
    }
    public OpenDay(Long id, Garage garage, LocalDate date, Integer maxParallelAppointments, List<Appointment> appointments, DayPlan workPlan) {
        this.id = id;
        this.garage = garage;
        this.date = date;
        this.maxParallelAppointments = maxParallelAppointments;
        this.appointments = appointments;
        this.workPlan = workPlan;
    }
    //endregion

    //region Getters & Setters methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public DayPlan getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(DayPlan workPlan) {
        this.workPlan = workPlan;
    }

    //endregion
}
