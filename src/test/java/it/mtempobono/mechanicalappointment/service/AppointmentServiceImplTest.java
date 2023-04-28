package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.service.impl.AppointmentServiceImpl;
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

    OpenDay openDay = new OpenDay();

    @BeforeEach
    void setUp() {

        DayPlan dayPlan = new DayPlan();
        List<TimePeriod> breaks = new ArrayList<>();
        breaks.add(new TimePeriod(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        dayPlan.setBreaks(breaks);

        openDay.setWorkPlan(dayPlan);
    }

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


        List<TimePeriod> availableHours = appointmentService.getAvailableAppointments(openDay, work);

        assertEquals(availableHours.size(), 3);
        assertEquals(availableHours.get(0).getStart().getLocalTime(), LocalTime.of(11, 0));
        assertEquals(availableHours.get(0).getEnd().getLocalTime(), LocalTime.of(12, 0));

        assertEquals(availableHours.get(1).getStart().getLocalTime(), LocalTime.of(14, 0));
        assertEquals(availableHours.get(1).getEnd().getLocalTime(), LocalTime.of(15, 0));

        assertEquals(availableHours.get(2).getStart().getLocalTime(), LocalTime.of(16, 0));
        assertEquals(availableHours.get(2).getEnd().getLocalTime(), LocalTime.of(17, 0));
    }
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


        List<TimePeriod> availableHours = appointmentService.getAvailableAppointments(openDay, work);

        assertEquals(availableHours.size(), 9);
    }
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


        List<TimePeriod> availableHours = appointmentService.getAvailableAppointments(openDay, work);

        assertEquals(availableHours.size(), 8);

        assertThat(availableHours).doesNotContain(new TimePeriod(LocalTime.of(10, 0), LocalTime.of(11, 0)));
    }
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


        List<TimePeriod> availableHours = appointmentService.getAvailableAppointments(openDay, work);

        assertEquals(availableHours.size(), 16);

        assertThat(availableHours).doesNotContain(new TimePeriod(LocalTime.of(10, 0), LocalTime.of(10, 30)));
        assertThat(availableHours).doesNotContain(new TimePeriod(LocalTime.of(10, 30), LocalTime.of(11, 0)));
    }
}