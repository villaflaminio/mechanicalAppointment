package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.dto.CreateAppointmentRequest;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.repository.AppointmentRepository;
import it.mtempobono.mechanicalappointment.repository.OpenDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AppointmentServiceImpl {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private OpenDayRepository openDayRepository;


    public List<TimePeriod> getAvailableHours(OpenDay openDay,MechanicalAction work ) {
//        OpenDay openDay = openDayRepository.findById(openDayId).orElseThrow(() -> new RuntimeException("OpenDay not found"));

        DayPlan day = openDay.getWorkPlan();

        List<Appointment> scheduledAppointments = openDay.getAppointments();
        List<TimePeriod> availablePeroids = day.timePeroidsWithBreaksExcluded();

        availablePeroids = excludeAppointmentsFromTimePeroids(availablePeroids, scheduledAppointments);

        return calculateAvailableHours(availablePeroids, work);
    }

    public List<TimePeriod> calculateAvailableHours(List<TimePeriod> availableTimePeroids, MechanicalAction work){
        ArrayList<TimePeriod> availableHours = new ArrayList();
        for (TimePeriod peroid : availableTimePeroids) {
            TimePeriod workPeroid = new TimePeriod(peroid.getStart(), peroid.getStart().plusMinutes(work.getInternalDuration().toMinutes()));
            while (workPeroid.getEnd().isBefore(peroid.getEnd()) || workPeroid.getEnd().equals(peroid.getEnd())) {
                availableHours.add(new TimePeriod(workPeroid.getStart(), workPeroid.getStart().plusMinutes(work.getInternalDuration().toMinutes())));
                workPeroid.setStart(workPeroid.getStart().plusMinutes(work.getInternalDuration().toMinutes()));
                workPeroid.setEnd(workPeroid.getEnd().plusMinutes(work.getInternalDuration().toMinutes()));
            }
        }
        return availableHours;
    }

    public List<TimePeriod> excludeAppointmentsFromTimePeroids(List<TimePeriod> peroids, List<Appointment> appointments) {

        List<TimePeriod> toAdd = new ArrayList();
        Collections.sort(appointments);
        for (Appointment appointment : appointments) {
            for (TimePeriod peroid : peroids) {
                if ((appointment.getInternalTime().getStart().isBefore(peroid.getStart()) || appointment.getInternalTime().getStart().equals(peroid.getStart()))
                        && appointment.getInternalTime().getEnd().isAfter(peroid.getStart()) && appointment.getInternalTime().getEnd().isBefore(peroid.getEnd())) {
                    peroid.setStart(appointment.getInternalTime().getEnd());
                }
                if (appointment.getInternalTime().getStart().isAfter(peroid.getStart()) && appointment.getInternalTime().getStart().isBefore(peroid.getEnd()) && appointment.getInternalTime().getEnd().isAfter(peroid.getEnd()) || appointment.getInternalTime().getEnd().equals(peroid.getEnd())) {
                    peroid.setEnd(appointment.getInternalTime().getStart());
                }
                if (appointment.getInternalTime().getStart().isAfter(peroid.getStart()) && appointment.getInternalTime().getEnd().isBefore(peroid.getEnd())) {
                    toAdd.add(new TimePeriod(peroid.getStart(), appointment.getInternalTime().getStart()));
                    peroid.setStart(appointment.getInternalTime().getEnd());
                }
            }
        }
        peroids.addAll(toAdd);
        Collections.sort(peroids);
        return peroids;
    }

    public void createAppointment(CreateAppointmentRequest request) {

        OpenDay openDay = openDayRepository.findById(request.getOpenDayId()).orElseThrow(() -> new RuntimeException("OpenDay not found"));

        List<Appointment> appointments = appointmentRepository.findByOpenDay(openDay);



    }
}
