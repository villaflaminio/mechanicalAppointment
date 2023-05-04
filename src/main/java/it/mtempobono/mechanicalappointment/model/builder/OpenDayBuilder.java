package it.mtempobono.mechanicalappointment.model.builder;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;

import java.time.LocalDate;
import java.util.List;

public final class OpenDayBuilder {
    private Long id;
    private Garage garage;
    private LocalDate date;
    private Integer maxParallelAppointments;
    private List<Appointment> appointments;
    private DayPlan workPlan;

    private OpenDayBuilder() {
    }

    public static OpenDayBuilder anOpenDay() {
        return new OpenDayBuilder();
    }

    public OpenDayBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OpenDayBuilder garage(Garage garage) {
        this.garage = garage;
        return this;
    }

    public OpenDayBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public OpenDayBuilder maxParallelAppointments(Integer maxParallelAppointments) {
        this.maxParallelAppointments = maxParallelAppointments;
        return this;
    }

    public OpenDayBuilder appointments(List<Appointment> appointments) {
        this.appointments = appointments;
        return this;
    }

    public OpenDayBuilder workPlan(DayPlan workPlan) {
        this.workPlan = workPlan;
        return this;
    }

    public OpenDay build() {
        OpenDay openDay = new OpenDay();
        openDay.setId(id);
        openDay.setGarage(garage);
        openDay.setDate(date);
        openDay.setMaxParallelAppointments(maxParallelAppointments);
        openDay.setAppointments(appointments);
        openDay.setWorkPlan(workPlan);
        return openDay;
    }
}
