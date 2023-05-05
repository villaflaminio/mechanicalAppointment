package it.mtempobono.mechanicalappointment.model.builder;

import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.entity.*;

public final class AppointmentBuilder {
    private Long id;
    private OpenDay openDay;
    private MechanicalAction mechanicalAction;
    private String comment;
    private Vehicle vehicle;
    private AppointmentStatus status;
    private TimePeriod externalTime;
    private TimePeriod internalTime;
    private Double price;
    private String idCalendarEvent;

    private Boolean isMechanicalActionCustom;
    private AppointmentBuilder() {
    }

    public static AppointmentBuilder anAppointment() {
        return new AppointmentBuilder();
    }

    public AppointmentBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AppointmentBuilder openDay(OpenDay openDay) {
        this.openDay = openDay;
        return this;
    }

    public AppointmentBuilder mechanicalAction(MechanicalAction mechanicalAction) {
        this.mechanicalAction = mechanicalAction;
        return this;
    }

    public AppointmentBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public AppointmentBuilder vehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public AppointmentBuilder status(AppointmentStatus status) {
        this.status = status;
        return this;
    }

    public AppointmentBuilder externalTime(TimePeriod externalTime) {
        this.externalTime = externalTime;
        return this;
    }

    public AppointmentBuilder internalTime(TimePeriod internalTime) {
        this.internalTime = internalTime;
        return this;
    }

    public AppointmentBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public AppointmentBuilder idCalendarEvent(String idCalendarEvent) {
        this.idCalendarEvent = idCalendarEvent;
        return this;
    }
    public AppointmentBuilder mechanicalActionCustom( Boolean isMechanicalActionCustom) {
        this.isMechanicalActionCustom = isMechanicalActionCustom;
        return this;
    }


    public Appointment build() {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setOpenDay(openDay);
        appointment.setMechanicalAction(mechanicalAction);
        appointment.setComment(comment);
        appointment.setVehicle(vehicle);
        appointment.setStatus(status);
        appointment.setExternalTime(externalTime);
        appointment.setInternalTime(internalTime);
        appointment.setPrice(price);
        appointment.setIdCalendarEvent(idCalendarEvent);
        appointment.setIsMechanicalActionCustom(isMechanicalActionCustom);
        return appointment;
    }
}
