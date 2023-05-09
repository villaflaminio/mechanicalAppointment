package it.mtempobono.mechanicalappointment.service.impl;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.util.CalculatedTimeslots;

import java.time.LocalTime;
import java.util.*;

public class AppointmentCore {

    //singleton pattern
    private static AppointmentCore instance = null;

    private AppointmentCore() {
    }

    public static AppointmentCore getInstance() {
        if (instance == null) {
            instance = new AppointmentCore();
        }
        return instance;
    }

    /**
     * Finds the available timeslots for a given day.
     *
     * @param dayPlan the DayPlan to analyze
     * @return a list of TimePeriods representing the available timeslots
     */
    private static boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Finds the overlapping intervals in a list of TimePeriods.
     *
     * @param intervals the list of TimePeriods to analyze
     * @return a list of TimePeriods representing the overlapping intervals
     */
    private List<TimePeriod> findOverlappingIntervals(List<TimePeriod> intervals) {
        intervals.sort(TimePeriod::compareTo); // Sort the intervals by start date
        List<TimePeriod> overlappingIntervals = new ArrayList<>();

        for (int i = 0; i < intervals.size() - 1; i++) {
            for (int j = i + 1; j < intervals.size(); j++) {
                TimePeriod interval1 = intervals.get(i);
                TimePeriod interval2 = intervals.get(j);
                // Check if the intervals overlap
                if (interval1.getEnd().getLocalTime().isAfter(interval2.getStart().getLocalTime()) && interval2.getEnd().getLocalTime().isAfter(interval1.getStart().getLocalTime())) {
                    // Find the start and end dates of the overlapping interval
                    LocalTime overlapStart = interval1.getStart().getLocalTime().isBefore(interval2.getStart().getLocalTime()) ? interval2.getStart().getLocalTime() : interval1.getStart().getLocalTime();
                    LocalTime overlapEnd = interval1.getEnd().getLocalTime().isAfter(interval2.getEnd().getLocalTime()) ? interval2.getEnd().getLocalTime() : interval1.getEnd().getLocalTime();
                    TimePeriod overlapping = new TimePeriod(overlapStart, overlapEnd);
                    // Add the overlapping interval to the list only if it's not already present
                    overlappingIntervals.add(overlapping);
                }
            }
        }
        return overlappingIntervals;
    }


    public CalculatedTimeslots getAvailableAppointments(OpenDay openDay, MechanicalAction work) {

        DayPlan day = openDay.getWorkPlan();

        List<TimePeriod> scheduledAppointments = new ArrayList<TimePeriod>(openDay.getAppointments().stream().map(Appointment::getInternalTime).toList());

        List<TimePeriod> availablePeroids = day.timePeriodsWithBreaksExcluded();

        Integer maxEventInParallel = openDay.getMaxParallelAppointments();

        if (maxEventInParallel == 0) {
            availablePeroids = excludeAppointmentsFromTimePeriods(availablePeroids, scheduledAppointments, work);

        } else {
            List<TimePeriod> intervalFilled = findOverlappingIntervals(scheduledAppointments);
            List<TimePeriod> intervalToExclude = new ArrayList<>();

            List<TimePeriod> intervalFilledSplitEvrery30minutes = new ArrayList<>();

            for (TimePeriod timePeriod : intervalFilled) {
                timePeriod.getDuration().toMinutes();
                int numberOfSplit = (int) timePeriod.getDuration().toMinutes() / 30;
                for (int i = 0; i < numberOfSplit; i++) {
                    intervalFilledSplitEvrery30minutes.add(new TimePeriod(timePeriod.getStart().getLocalTime().plusMinutes(i * 30), timePeriod.getStart().getLocalTime().plusMinutes((i + 1) * 30)));
                }
            }


            Map<TimePeriod, Integer> frequencyMap = new HashMap<>();
            for (TimePeriod number : intervalFilledSplitEvrery30minutes) {
                frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
            }

            for (Map.Entry<TimePeriod, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() >= maxEventInParallel) {
                    intervalToExclude.add(entry.getKey());
                }
            }

            availablePeroids = excludeAppointmentsFromTimePeriods(availablePeroids, intervalToExclude, work);
        }


        return calculateAvailableHours(availablePeroids, work, openDay);
    }


    /**
     * Calculates the available hours for a MechanicalAction within a list of TimePeriods.
     *
     * @param availableTimePeriods the list of TimePeriods representing the available time
     * @param work                 the MechanicalAction to schedule
     * @return a list of TimePeriods representing the available hours for the MechanicalAction
     */
    private CalculatedTimeslots calculateAvailableHours(List<TimePeriod> availableTimePeriods, MechanicalAction work, OpenDay openDay) {
        List<TimePeriod> availableHoursOnInteralTime = new ArrayList<>(availableTimePeriods.size() * 2);
        List<TimePeriod> availableHoursOnExternalTime = new ArrayList<>(availableTimePeriods.size() * 2);

        if (work.getInternalDuration().toMinutes() <= 0 || work.getExternalDuration().toMinutes() <= 0)
            throw new IllegalArgumentException("Duration must be greater than 0");

        // On each available time period
        for (TimePeriod period : availableTimePeriods) {
            LocalTime startTime = period.getStart().getLocalTime();
            LocalTime endTime = period.getEnd().getLocalTime();
            // Iterate over each interval of the work time that can fit within the period
            while (startTime.plusMinutes(work.getInternalDuration().toMinutes()).isBefore(endTime)) {
                // Add the interval to the list of available hours
                availableHoursOnInteralTime.add(new TimePeriod(startTime, startTime.plusMinutes(work.getInternalDuration().toMinutes())));

                //if the external duration is not null and the external duration is less than the end time of the work plan
                // then add the external duration to the available hours
                if (startTime.plusMinutes(work.getExternalDuration().toMinutes()).isBefore(openDay.getWorkPlan().getWorkingHours().getEnd().getLocalTime())) {
                    availableHoursOnExternalTime.add(new TimePeriod(startTime, startTime.plusMinutes(work.getExternalDuration().toMinutes())));
                }


                startTime = startTime.plusMinutes(work.getInternalDuration().toMinutes());
            }
            // Add any remaining time as a separate interval
            if (startTime.isBefore(endTime)) {
                availableHoursOnInteralTime.add(new TimePeriod(startTime, endTime));

                if (startTime.plusMinutes(work.getExternalDuration().toMinutes()).isBefore(openDay.getWorkPlan().getWorkingHours().getEnd().getLocalTime())) {
                    availableHoursOnExternalTime.add(new TimePeriod(startTime, startTime.plusMinutes(work.getExternalDuration().toMinutes())));
                }
            }
        }

        return new CalculatedTimeslots(availableHoursOnInteralTime, availableHoursOnExternalTime);
    }

    /**
     * This method excludes appointments from the given time periods, returning the resulting time periods.
     *
     * @param periods      the list of time periods to be modified
     * @param appointments the list of time periods representing appointments to be excluded
     * @return a list of time periods that represent the resulting availability after excluding the appointments
     */
    private List<TimePeriod> excludeAppointmentsFromTimePeriods(List<TimePeriod> periods, List<TimePeriod> appointments, MechanicalAction work) {

        List<TimePeriod> toAdd = new ArrayList<>();

        // Sort the appointments by start time, in ascending order
        Collections.sort(appointments);

        // Loop through each appointment
        for (TimePeriod appointment : appointments) {

            // Create a copy of the appointment period to avoid modifying the original object
            TimePeriod appointmentPeriod = new TimePeriod(appointment.getStart().getLocalTime(), appointment.getEnd().getLocalTime());

            // Loop through each period
            for (TimePeriod period : periods) {

                // Case 1: appointment overlaps the start of the period
//                System.out.println("appointment start :  " + appointmentPeriod.getStart().getLocalTime() + " appointment end " + appointmentPeriod.getEnd().getLocalTime() + " period start : " + period.getStart().getLocalTime() + " period end : " + period.getEnd().getLocalTime());
                if ((appointment.getStart().getLocalTime().isBefore(period.getStart().getLocalTime()) || appointment.getStart().equals(period.getStart()))
                        && appointment.getEnd().getLocalTime().isAfter(period.getStart().getLocalTime()) &&
                        appointment.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime())) {
                    period.setStart(appointment.getEnd());
                }

                // Case 2: appointment overlaps the end of the period
                if (appointment.getStart().getLocalTime().isAfter(period.getStart().getLocalTime()) &&
                        appointment.getStart().getLocalTime().isBefore(period.getEnd().getLocalTime())
                        && (appointment.getEnd().getLocalTime().isAfter(period.getEnd().getLocalTime()) || appointment.getEnd().equals(period.getEnd()))) {
                    period.setEnd(appointment.getStart());
                }

                // Case 3: appointment is within the period
                if ((appointment.getStart().getLocalTime().isAfter(period.getStart().getLocalTime()) || (appointment.getStart().getLocalTime().equals(period.getStart().getLocalTime()))) &&
                        (appointment.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime()) || appointment.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime()))) {
                    TimePeriod time = new TimePeriod(period.getStart().getLocalTime(), appointment.getStart().getLocalTime());
//                    System.out.println("time start : " + time.getStart().getLocalTime() + " time end : " + time.getEnd().getLocalTime());
//                    System.out.println("work internal duration : " + work.getInternalDuration().toMinutes() + " time duration : " + time.getDuration().toMinutes());
                    if (work.getInternalDuration() != null && work.getInternalDuration().toMinutes() <= time.getDuration().toMinutes()) {
                        toAdd.add(time);
                    }
                    period.setStart(appointment.getEnd());
                    System.out.println("new time start : " + period.getStart().getLocalTime() + " time end : " + period.getEnd().getLocalTime());

                }
            }
        }

        // Add the new time periods created in case 3 to the list of periods
        periods.addAll(toAdd);

        // Sort the periods by start time, in ascending order
        Collections.sort(periods);

        return periods;
    }


}
