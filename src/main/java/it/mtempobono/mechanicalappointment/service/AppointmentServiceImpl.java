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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private OpenDayRepository openDayRepository;

    public static boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    //    public List<TimePeriod> findOverlappingInterval(List<Appointment> intervals) {
//        Collections.sort(intervals); //(n log n)
//        List<TimePeriod> overlappingInterval = new ArrayList<>();
//
//        for (int i = 0; i < intervals.size() - 1; i++) { //n
//            if (isOverlapping(intervals.get(i).getInternalTime().getStart(), intervals.get(i).getInternalTime().getEnd(),
//                    intervals.get(i + 1).getInternalTime().getStart(), intervals.get(i + 1).getInternalTime().getEnd())) {
//
//                LocalTime prevStartInterval = intervals.get(i).getInternalTime().getStart();
//                LocalTime currStartInterval = intervals.get(i + 1).getInternalTime().getStart();
//
//                LocalTime prevEndInterval = intervals.get(i).getInternalTime().getEnd();
//                LocalTime currEndInterval = intervals.get(i + 1).getInternalTime().getEnd();
//
//                int[] prevInterval = {prevStartInterval.toSecondOfDay(), prevEndInterval.toSecondOfDay()};
//                int[] currInterval = {currStartInterval.toSecondOfDay(), currEndInterval.toSecondOfDay()};
//
//
//                List<int[]> overlappingIntervals = new ArrayList<>();
//                overlappingIntervals.add(new int[]{Math.max(prevInterval[0], currInterval[0]), Math.min(prevInterval[1], currInterval[1])});
//
//
//                for (int[] interval : overlappingIntervals) {
//
//                    overlappingInterval.add(new TimePeriod(LocalTime.ofSecondOfDay(interval[0]), LocalTime.ofSecondOfDay(interval[1])));
//                }
//            }
//
//        }
//        return overlappingInterval;
//    }

    public List<TimePeriod> findOverlappingIntervalFromTimePeriod(List<TimePeriod> intervals) {
        Collections.sort(intervals); //(n log n)
        List<TimePeriod> overlappingInterval = new ArrayList<>();
        List<int[]> overlappingIntervals = new ArrayList<>();

        for (int i = 0; i < intervals.size(); i++) { //n

            for (int j = i + 1; j < intervals.size(); j++) {
                if (isOverlapping(intervals.get(i).getStart(), intervals.get(i).getEnd(),
                        intervals.get(j).getStart(), intervals.get(j).getEnd())) {

                    LocalTime prevStartInterval = intervals.get(i).getStart();
                    LocalTime currStartInterval = intervals.get(j).getStart();

                    LocalTime prevEndInterval = intervals.get(i).getEnd();
                    LocalTime currEndInterval = intervals.get(j).getEnd();

                    int[] prevInterval = {prevStartInterval.toSecondOfDay(), prevEndInterval.toSecondOfDay()};
                    int[] currInterval = {currStartInterval.toSecondOfDay(), currEndInterval.toSecondOfDay()};

                    int[] overlapping = new int[]{Math.max(prevInterval[0], currInterval[0]), Math.min(prevInterval[1], currInterval[1])};

                    if (overlappingIntervals.stream()
                            .noneMatch(x -> Arrays.equals(x, overlapping)))
                        overlappingIntervals.add(overlapping);

                }
            }
        }
        for (int[] interval : overlappingIntervals) {

            overlappingInterval.add(new TimePeriod(LocalTime.ofSecondOfDay(interval[0]), LocalTime.ofSecondOfDay(interval[1])));
        }
        return overlappingInterval;
    }

    public List<TimePeriod> findOverlappingInterval(List<TimePeriod> intervals) {
        Collections.sort(intervals); //(n log n)
        List<TimePeriod> overlappingInterval = new ArrayList<>();
        List<int[]> overlappingIntervals = new ArrayList<>();

        for (int i = 0; i < intervals.size(); i++) { //n

            for (int j = i + 1; j < intervals.size(); j++) {
                if (isOverlapping(intervals.get(i).getStart(), intervals.get(i).getEnd(),
                        intervals.get(j).getStart(), intervals.get(j).getEnd())) {

                    LocalTime prevStartInterval = intervals.get(i).getStart();
                    LocalTime currStartInterval = intervals.get(j).getStart();

                    LocalTime prevEndInterval = intervals.get(i).getEnd();
                    LocalTime currEndInterval = intervals.get(j).getEnd();

                    int[] prevInterval = {prevStartInterval.toSecondOfDay(), prevEndInterval.toSecondOfDay()};
                    int[] currInterval = {currStartInterval.toSecondOfDay(), currEndInterval.toSecondOfDay()};
                    overlappingIntervals.add(new int[]{Math.max(prevInterval[0], currInterval[0]), Math.min(prevInterval[1], currInterval[1])});

                }
            }
        }
        for (int[] interval : overlappingIntervals) {

            overlappingInterval.add(new TimePeriod(LocalTime.ofSecondOfDay(interval[0]), LocalTime.ofSecondOfDay(interval[1])));
        }
        return overlappingInterval;
    }

    public List<TimePeriod> getAvailableHours(OpenDay openDay, MechanicalAction work) {
//        OpenDay openDay = openDayRepository.findById(openDayId).orElseThrow(() -> new RuntimeException("OpenDay not found"));

        DayPlan day = openDay.getWorkPlan();


        List<TimePeriod> scheduledAppointments = openDay.getAppointments().stream().map(Appointment::getInternalTime).toList();

        List<TimePeriod> availablePeroids = day.timePeroidsWithBreaksExcluded();

        int maxEventInParallel = 2;

       if(maxEventInParallel == 0){
           availablePeroids = excludeAppointmentsFromTimePeriods(availablePeroids, scheduledAppointments);

       }else{
           List<TimePeriod> intervalFilled  = findOverlappingInterval(scheduledAppointments);

           for(int i= 1 ; i< maxEventInParallel ; i++){
               intervalFilled = findOverlappingIntervalFromTimePeriod(intervalFilled);
           }
           availablePeroids = excludeAppointmentsFromTimePeriods(availablePeroids, intervalFilled);
       }

//        availablePeroids = excludeAppointmentsFromTimePeroids(availablePeroids, scheduledAppointments);

        return calculateAvailableHours(availablePeroids, work);
    }

    public List<TimePeriod> calculateAvailableHours(List<TimePeriod> availableTimePeroids, MechanicalAction work) {
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

//    public List<TimePeriod> excludeAppointmentsFromTimePeroids(List<TimePeriod> peroids, List<Appointment> appointments) {
//
//        List<TimePeriod> toAdd = new ArrayList();
//        Collections.sort(appointments);
//        for (Appointment appointment : appointments) {
//            TimePeriod appointMentPeroid = new TimePeriod(appointment.getInternalTime().getStart(), appointment.getInternalTime().getEnd());
//            System.out.println("Appointment: " + appointMentPeroid.getStart() + " - " + appointMentPeroid.getEnd());
//            for (TimePeriod peroid : peroids) {
//                if ((appointment.getInternalTime().getStart().isBefore(peroid.getStart()) || appointment.getInternalTime().getStart().equals(peroid.getStart()))
//                        && appointment.getInternalTime().getEnd().isAfter(peroid.getStart()) && appointment.getInternalTime().getEnd().isBefore(peroid.getEnd())) {
//                    peroid.setStart(appointment.getInternalTime().getEnd());
//                }
//                if (appointment.getInternalTime().getStart().isAfter(peroid.getStart()) && appointment.getInternalTime().getStart().isBefore(peroid.getEnd())
//                        && appointment.getInternalTime().getEnd().isAfter(peroid.getEnd()) || appointment.getInternalTime().getEnd().equals(peroid.getEnd())) {
//                    peroid.setEnd(appointment.getInternalTime().getStart());
//                }
//                if (appointment.getInternalTime().getStart().isAfter(peroid.getStart()) && appointment.getInternalTime().getEnd().isBefore(peroid.getEnd())) {
//                    toAdd.add(new TimePeriod(peroid.getStart(), appointment.getInternalTime().getStart()));
//                    peroid.setStart(appointment.getInternalTime().getEnd());
//                }
//            }
//        }
//        peroids.addAll(toAdd);
//        Collections.sort(peroids);
//        return peroids;
//    }
public List<TimePeriod> excludeAppointmentsFromTimePeriods(List<TimePeriod> peroids, List<TimePeriod> appointments) {

    List<TimePeriod> toAdd = new ArrayList();
    Collections.sort(appointments);
    for (TimePeriod appointment : appointments) {
        TimePeriod appointMentPeroid = new TimePeriod(appointment.getStart(), appointment.getEnd());
        System.out.println("Appointment: " + appointMentPeroid.getStart() + " - " + appointMentPeroid.getEnd());
        for (TimePeriod peroid : peroids) {
            if ((appointment.getStart().isBefore(peroid.getStart()) || appointment.getStart().equals(peroid.getStart()))
                    && appointment.getEnd().isAfter(peroid.getStart()) && appointment.getEnd().isBefore(peroid.getEnd())) {
                peroid.setStart(appointment.getEnd());
            }
            if (appointment.getStart().isAfter(peroid.getStart()) && appointment.getStart().isBefore(peroid.getEnd())
                    && appointment.getEnd().isAfter(peroid.getEnd()) || appointment.getEnd().equals(peroid.getEnd())) {
                peroid.setEnd(appointment.getStart());
            }
            if (appointment.getStart().isAfter(peroid.getStart()) && appointment.getEnd().isBefore(peroid.getEnd())) {
                toAdd.add(new TimePeriod(peroid.getStart(), appointment.getStart()));
                peroid.setStart(appointment.getEnd());
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
