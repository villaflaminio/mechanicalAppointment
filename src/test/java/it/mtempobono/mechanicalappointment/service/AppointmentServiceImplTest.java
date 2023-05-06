package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.service.impl.AppointmentCore;
import it.mtempobono.mechanicalappointment.service.impl.AppointmentServiceImpl;
import it.mtempobono.mechanicalappointment.util.CalculatedTimeslots;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class AppointmentServiceImplTest {

    @InjectMocks
    AppointmentServiceImpl appointmentService = new AppointmentServiceImpl();
    private AppointmentCore appointmentCore = new AppointmentCore();

    OpenDay openDay = new OpenDay();

    @BeforeEach
    void setUp() {

        DayPlan dayPlan = new DayPlan();
        List<TimePeriod> breaks = new ArrayList<>();
        breaks.add(new TimePeriod(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        dayPlan.setBreaks(breaks);

        openDay.setWorkPlan(dayPlan);
    }

    /*
    Test with max 2 parallel appointments

    11:00 → 11:30
    11:30 → 12:00
    12:00 → 14:00	x	x	x	x
    14:00 → 14:30	x
    14:30 → 15:00	x			x
    15:00 → 15:30	x	x		x
    15:30 → 16:00	x	x	x
    16:00 → 16:30		x	x
    16:30 → 17:00			x
     */
    @Test
    void getAvailableHoursWithMaxTwoParallelAppointments() {

        openDay.getWorkPlan().setWorkingHours(new TimePeriod(LocalTime.of(11, 0), LocalTime.of(17, 0)));
        openDay.setMaxParallelAppointments(2);
        Appointment appointment = new Appointment();
        appointment.setInternalTime(new TimePeriod(LocalTime.of(14, 0), LocalTime.of(16, 0)));

        Appointment appointment2 = new Appointment();
        appointment2.setInternalTime(new TimePeriod(LocalTime.of(15, 0), LocalTime.of(16, 30)));

        Appointment appointment3 = new Appointment();
        appointment3.setInternalTime(new TimePeriod(LocalTime.of(15, 30), LocalTime.of(17, 0)));

        Appointment appointment4 = new Appointment();
        appointment4.setInternalTime(new TimePeriod(LocalTime.of(14, 30), LocalTime.of(15, 30)));

        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        appointments.add(appointment2);
        appointments.add(appointment3);
        appointments.add(appointment4);
        openDay.setAppointments(appointments);


        MechanicalAction work = new MechanicalAction();
        work.setInternalDuration(Duration.ofHours(1));
        work.setExternalDuration(Duration.ofHours(2));
        CalculatedTimeslots slots = appointmentCore.getAvailableAppointments(openDay, work);

        List<TimePeriod> availableHoursInternal = slots.getAvailableHoursOnInteralTime();
        List<TimePeriod> availableHoursExternal = slots.getAvailableHoursOnExternalTime();

        assertEquals(availableHoursInternal.size(), 3);
        assertEquals(availableHoursInternal.get(0).getStart().getLocalTime(), LocalTime.of(11, 0));
        assertEquals(availableHoursInternal.get(0).getEnd().getLocalTime(), LocalTime.of(12, 0));

        assertEquals(availableHoursInternal.get(1).getStart().getLocalTime(), LocalTime.of(14, 0));
        assertEquals(availableHoursInternal.get(1).getEnd().getLocalTime(), LocalTime.of(15, 0));

        assertEquals(availableHoursInternal.get(2).getStart().getLocalTime(), LocalTime.of(16, 0));
        assertEquals(availableHoursInternal.get(2).getEnd().getLocalTime(), LocalTime.of(17, 0));

        assertEquals(availableHoursExternal.size(), 2);
        assertEquals(availableHoursExternal.get(0).getStart().getLocalTime(), LocalTime.of(11, 0));
        assertEquals(availableHoursExternal.get(0).getEnd().getLocalTime(), LocalTime.of(13, 0));

        assertEquals(availableHoursExternal.get(1).getStart().getLocalTime(), LocalTime.of(14, 0));
        assertEquals(availableHoursExternal.get(1).getEnd().getLocalTime(), LocalTime.of(16, 0));


    }

    /*
    6:00
    7:00 → 7:30
    7:30 → 8:00
    8:00 → 8:30	    x
    8:30 → 9:00	    x
    9:00 →9:30	    x
    9:30 → 10:00	x
    10:00 → 10:30	x
    10:30 → 11:00	x
    11:00 → 11:30
    11:30 → 12:00
    12:00 → 14:00	x	x
    14:00 → 14:30
    14:30 → 15:00
    15:00 → 15:30	x
    15:30 → 16:00	x
    16:00 → 16:30	x
    16:30 → 17:00
     */
    @Test
    void getAvailableHoursWithMaxOneParallelAppointments1() {

        openDay.getWorkPlan().setWorkingHours(new TimePeriod(LocalTime.of(6, 0), LocalTime.of(17, 0)));
        openDay.setMaxParallelAppointments(1);
        Appointment appointment = new Appointment();
        appointment.setInternalTime(new TimePeriod(LocalTime.of(8, 0), LocalTime.of(11, 0)));

        Appointment appointment2 = new Appointment();
        appointment2.setInternalTime(new TimePeriod(LocalTime.of(15, 0), LocalTime.of(16, 30)));

        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        appointments.add(appointment2);
        openDay.setAppointments(appointments);


        MechanicalAction work = new MechanicalAction();
        work.setInternalDuration(Duration.ofHours(1));
        work.setExternalDuration(Duration.ofHours(6));

        CalculatedTimeslots slots = appointmentCore.getAvailableAppointments(openDay, work);

        List<TimePeriod> availableHoursInternal = slots.getAvailableHoursOnInteralTime();
        List<TimePeriod> availableHoursExternal = slots.getAvailableHoursOnExternalTime();
        assertEquals(availableHoursInternal.size(), 9);
        assertEquals(availableHoursExternal.size(), 5);

    }

    /*
    6:00
    7:00 → 7:30
    7:30 → 8:00
    8:00 → 8:30	    x
    8:30 → 9:00 	x
    9:00 →9:30  	x
    9:30 → 10:00	x
    10:00 → 10:30	x	x
    10:30 → 11:00	x	x
    11:00 → 11:30		x
    11:30 → 12:00		x
    12:00 → 14:00	x	x
    14:00 → 14:30
    14:30 → 15:00
    15:00 → 15:30	x
    15:30 → 16:00	x
    16:00 → 16:30	x
    16:30 → 17:00
     */
    @Test
    void getAvailableHoursWithMaxOneParallelAppointments2() {

        openDay.getWorkPlan().setWorkingHours(new TimePeriod(LocalTime.of(6, 0), LocalTime.of(17, 0)));
        openDay.setMaxParallelAppointments(1);
        Appointment appointment = new Appointment();
        appointment.setInternalTime(new TimePeriod(LocalTime.of(8, 0), LocalTime.of(11, 0)));

        Appointment appointment2 = new Appointment();
        appointment2.setInternalTime(new TimePeriod(LocalTime.of(15, 0), LocalTime.of(16, 30)));

        Appointment appointment3 = new Appointment();
        appointment3.setInternalTime(new TimePeriod(LocalTime.of(10, 0), LocalTime.of(12, 0)));


        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        appointments.add(appointment2);
        appointments.add(appointment3);
        openDay.setAppointments(appointments);


        MechanicalAction work = new MechanicalAction();
        work.setInternalDuration(Duration.ofHours(1));
        work.setExternalDuration(Duration.ofHours(6));


        CalculatedTimeslots slots = appointmentCore.getAvailableAppointments(openDay, work);

        List<TimePeriod> availableHoursInternal = slots.getAvailableHoursOnInteralTime();
        List<TimePeriod> availableHoursExternal = slots.getAvailableHoursOnExternalTime();

        assertEquals(availableHoursInternal.size(), 8);
        assertEquals(availableHoursExternal.size(), 4);

        assertThat(availableHoursInternal).doesNotContain(new TimePeriod(LocalTime.of(10, 0), LocalTime.of(11, 0)));

        assertThat(availableHoursExternal).contains(new TimePeriod(LocalTime.of(6, 0), LocalTime.of(12, 0)));
        assertThat(availableHoursExternal).contains(new TimePeriod(LocalTime.of(7, 0), LocalTime.of(13, 0)));
        assertThat(availableHoursExternal).contains(new TimePeriod(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        assertThat(availableHoursExternal).contains(new TimePeriod(LocalTime.of(9, 0), LocalTime.of(15, 0)));
    }

    /*
    6:00
    7:00 → 7:30
    7:30 → 8:00
    8:00 → 8:30 	x
    8:30 → 9:00	    x
    9:00 →9:30	    x
    9:30 → 10:00	x
    10:00 → 10:30	x	x
    10:30 → 11:00	x	x
    11:00 → 11:30		x
    11:30 → 12:00		x
    12:00 → 14:00	x	x
    14:00 → 14:30
    14:30 → 15:00
    15:00 → 15:30	x
    15:30 → 16:00	x
    16:00 → 16:30	x
    16:30 → 17:00
     */
    @Test
    void getAvailableHoursWithMaxOneParallelAppointments3() {

        openDay.getWorkPlan().setWorkingHours(new TimePeriod(LocalTime.of(6, 0), LocalTime.of(17, 0)));
        openDay.setMaxParallelAppointments(1);
        Appointment appointment = new Appointment();
        appointment.setInternalTime(new TimePeriod(LocalTime.of(8, 0), LocalTime.of(11, 0)));

        Appointment appointment2 = new Appointment();
        appointment2.setInternalTime(new TimePeriod(LocalTime.of(15, 0), LocalTime.of(16, 30)));

        Appointment appointment3 = new Appointment();
        appointment3.setInternalTime(new TimePeriod(LocalTime.of(10, 0), LocalTime.of(12, 0)));


        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        appointments.add(appointment2);
        appointments.add(appointment3);
        openDay.setAppointments(appointments);


        MechanicalAction work = new MechanicalAction();
        work.setInternalDuration(Duration.ofMinutes(30));
        work.setExternalDuration(Duration.ofHours(1));

        CalculatedTimeslots slots = appointmentCore.getAvailableAppointments(openDay, work);

        List<TimePeriod> availableHoursInternal = slots.getAvailableHoursOnInteralTime();
        List<TimePeriod> availableHoursExternal = slots.getAvailableHoursOnExternalTime();

        assertEquals(availableHoursInternal.size(), 16);
        assertEquals(availableHoursExternal.size(), 14);

        assertThat(availableHoursInternal).doesNotContain(new TimePeriod(LocalTime.of(10, 0), LocalTime.of(10, 30)));
        assertThat(availableHoursInternal).doesNotContain(new TimePeriod(LocalTime.of(10, 30), LocalTime.of(11, 0)));

        assertThat(availableHoursExternal).doesNotContain(new TimePeriod(LocalTime.of(10, 0), LocalTime.of(10, 30)));
        assertThat(availableHoursExternal).doesNotContain(new TimePeriod(LocalTime.of(10, 30), LocalTime.of(11, 0)));
        assertThat(availableHoursExternal).doesNotContain(new TimePeriod(LocalTime.of(16, 30), LocalTime.of(17, 0)));
    }


    @Test
    void test_appointment_with2Scheduled_appointment_atSameTime() {

        openDay.getWorkPlan().setWorkingHours(new TimePeriod(LocalTime.of(6, 0), LocalTime.of(17, 0)));
        openDay.setMaxParallelAppointments(2);
        Appointment appointment = new Appointment();
        appointment.setInternalTime(new TimePeriod(LocalTime.of(6, 30), LocalTime.of(10, 30)));

        Appointment appointment2 = new Appointment();
        appointment2.setInternalTime(new TimePeriod(LocalTime.of(6, 30), LocalTime.of(10, 30)));

        Appointment appointment3 = new Appointment();
        appointment3.setInternalTime(new TimePeriod(LocalTime.of(6, 30), LocalTime.of(10, 30)));

        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        appointments.add(appointment2);
        appointments.add(appointment3);
        openDay.setAppointments(appointments);


        MechanicalAction work = new MechanicalAction();
        work.setInternalDuration(Duration.ofHours(2));
        work.setExternalDuration(Duration.ofHours(4));

        CalculatedTimeslots slots = appointmentCore.getAvailableAppointments(openDay, work);

        List<TimePeriod> availableHoursInternal = slots.getAvailableHoursOnInteralTime();
        List<TimePeriod> availableHoursExternal = slots.getAvailableHoursOnExternalTime();

        assertEquals(availableHoursInternal.size(), 3);
        assertEquals(availableHoursExternal.size(), 1);

    }

}