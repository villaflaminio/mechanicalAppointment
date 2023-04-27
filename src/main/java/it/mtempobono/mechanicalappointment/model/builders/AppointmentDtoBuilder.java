package it.mtempobono.mechanicalappointment.model.builders;

import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.dto.MechanicalActionDto;
import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.AppointmentStatus;

public final class AppointmentDtoBuilder {
    private Long id;
    private OpenDayDto openDay;
    private MechanicalActionDto mechanicalAction;
    private String comment;
    private VehicleDto vehicle;
    private AppointmentStatus status;
    private TimePeriod externalTime;
    private TimePeriod internalTime;
    private Double price;

    private AppointmentDtoBuilder() {
    }

    public static AppointmentDtoBuilder anAppointmentDto() {
        return new AppointmentDtoBuilder();
    }

    public AppointmentDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AppointmentDtoBuilder openDay(OpenDayDto openDay) {
        this.openDay = openDay;
        return this;
    }

    public AppointmentDtoBuilder mechanicalAction(MechanicalActionDto mechanicalAction) {
        this.mechanicalAction = mechanicalAction;
        return this;
    }

    public AppointmentDtoBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public AppointmentDtoBuilder vehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public AppointmentDtoBuilder status(AppointmentStatus status) {
        this.status = status;
        return this;
    }

    public AppointmentDtoBuilder externalTime(TimePeriod externalTime) {
        this.externalTime = externalTime;
        return this;
    }

    public AppointmentDtoBuilder internalTime(TimePeriod internalTime) {
        this.internalTime = internalTime;
        return this;
    }

    public AppointmentDtoBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public AppointmentDto build() {
        return new AppointmentDto(id, openDay, mechanicalAction, comment, vehicle, status, externalTime, internalTime, price);
    }
}
