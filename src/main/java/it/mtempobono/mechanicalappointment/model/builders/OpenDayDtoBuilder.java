package it.mtempobono.mechanicalappointment.model.builders;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;

import java.time.LocalDate;
import java.util.List;

public final class OpenDayDtoBuilder {
    private Long id;
    private GarageDto garage;
    private LocalDate date;
    private Integer maxParallelAppointments;
    private List<AppointmentDto> appointments;
    private DayPlan workPlan;

    private OpenDayDtoBuilder() {
    }

    public static OpenDayDtoBuilder anOpenDayDto() {
        return new OpenDayDtoBuilder();
    }

    public OpenDayDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OpenDayDtoBuilder garage(GarageDto garage) {
        this.garage = garage;
        return this;
    }

    public OpenDayDtoBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public OpenDayDtoBuilder maxParallelAppointments(Integer maxParallelAppointments) {
        this.maxParallelAppointments = maxParallelAppointments;
        return this;
    }

    public OpenDayDtoBuilder appointments(List<AppointmentDto> appointments) {
        this.appointments = appointments;
        return this;
    }

    public OpenDayDtoBuilder workPlan(DayPlan workPlan) {
        this.workPlan = workPlan;
        return this;
    }

    public OpenDayDto build() {
        return new OpenDayDto(id, garage, date, maxParallelAppointments, appointments, workPlan);
    }
}
