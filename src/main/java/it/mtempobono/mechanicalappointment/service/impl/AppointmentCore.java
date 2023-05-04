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
//                    if (!overlappingIntervals.contains(overlapping)) {
                    overlappingIntervals.add(overlapping);
//                    }
                }
            }
        }
        return overlappingIntervals;
    }

//    public List<TimePeriod> findOverlappingIntervalFromTimePeriod(List<TimePeriod> intervals) {
//        Collections.sort(intervals); //(n log n)
//        List<TimePeriod> overlappingInterval = new ArrayList<>();
//        List<int[]> overlappingIntervals = new ArrayList<>();
//
//        for (int i = 0; i < intervals.size(); i++) { //n
//
//            for (int j = i + 1; j < intervals.size(); j++) {
//                if (isOverlapping(intervals.get(i).getStart(), intervals.get(i).getEnd(),
//                        intervals.get(j).getStart(), intervals.get(j).getEnd())) {
//
//                    LocalTime prevStartInterval = intervals.get(i).getStart();
//                    LocalTime currStartInterval = intervals.get(j).getStart();
//
//                    LocalTime prevEndInterval = intervals.get(i).getEnd();
//                    LocalTime currEndInterval = intervals.get(j).getEnd();
//
//                    int[] prevInterval = {prevStartInterval.toSecondOfDay(), prevEndInterval.toSecondOfDay()};
//                    int[] currInterval = {currStartInterval.toSecondOfDay(), currEndInterval.toSecondOfDay()};
//
//                    int[] overlapping = new int[]{Math.max(prevInterval[0], currInterval[0]), Math.min(prevInterval[1], currInterval[1])};
//
//                    if (overlappingIntervals.stream()
//                            .noneMatch(x -> Arrays.equals(x, overlapping)))
//                        overlappingIntervals.add(overlapping);
//
//                }
//            }
//        }
//        for (int[] interval : overlappingIntervals) {
//
//            overlappingInterval.add(new TimePeriod(LocalTime.ofSecondOfDay(interval[0]), LocalTime.ofSecondOfDay(interval[1])));
//        }
//        return overlappingInterval;
//    }

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
//            for (int i = 1; i < maxEventInParallel; i++) {
//                intervalFilled = findOverlappingIntervals(intervalFilled);
//            }

            Map<TimePeriod, Integer> frequencyMap = new HashMap<>();
            for (TimePeriod number : intervalFilled) {
                frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
            }

            for (Map.Entry<TimePeriod, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() > maxEventInParallel) {
                    intervalToExclude.add(entry.getKey());
                }
            }

            availablePeroids = excludeAppointmentsFromTimePeriods(availablePeroids, intervalToExclude, work);
        }


        return calculateAvailableHours(availablePeroids, work, openDay);
    }

//    public List<TimePeriod> calculateAvailableHours(List<TimePeriod> availableTimePeroids, MechanicalAction work) {
//        ArrayList<TimePeriod> availableHours = new ArrayList();
//        for (TimePeriod peroid : availableTimePeroids) {
//            TimePeriod workPeroid = new TimePeriod(peroid.getStart(), peroid.getStart().plusMinutes(work.getInternalDuration().toMinutes()));
//            while (workPeroid.getEnd().isBefore(peroid.getEnd()) || workPeroid.getEnd().equals(peroid.getEnd())) {
//                availableHours.add(new TimePeriod(workPeroid.getStart(), workPeroid.getStart().plusMinutes(work.getInternalDuration().toMinutes())));
//                workPeroid.setStart(workPeroid.getStart().plusMinutes(work.getInternalDuration().toMinutes()));
//                workPeroid.setEnd(workPeroid.getEnd().plusMinutes(work.getInternalDuration().toMinutes()));
//            }
//        }
//        return availableHours;
//    }


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
                if (appointment.getStart().getLocalTime().isAfter(period.getStart().getLocalTime()) && appointment.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime())) {
                    TimePeriod time = new TimePeriod(period.getStart().getLocalTime(), appointment.getStart().getLocalTime());

                    if (work.getInternalDuration() != null && work.getInternalDuration().toMinutes() <= time.getDuration().toMinutes()) {
                        toAdd.add(time);
                    }
                    period.setStart(appointment.getEnd());

                }
            }
        }

        // Add the new time periods created in case 3 to the list of periods
        periods.addAll(toAdd);

        // Sort the periods by start time, in ascending order
        Collections.sort(periods);

        return periods;
    }


//    public List<TimePeriod> excludeAppointmentsFromTimePeriods(List<TimePeriod> peroids, List<TimePeriod> appointments) {
//
//        List<TimePeriod> toAdd = new ArrayList();
//        Collections.sort(appointments);
//        for (TimePeriod appointment : appointments) {
//            TimePeriod appointMentPeroid = new TimePeriod(appointment.getStart(), appointment.getEnd());
//            for (TimePeriod peroid : peroids) {
//                if ((appointment.getStart().isBefore(peroid.getStart()) || appointment.getStart().equals(peroid.getStart()))
//                        && appointment.getEnd().isAfter(peroid.getStart()) && appointment.getEnd().isBefore(peroid.getEnd())) {
//                    peroid.setStart(appointment.getEnd());
//                }
//                if (appointment.getStart().isAfter(peroid.getStart()) && appointment.getStart().isBefore(peroid.getEnd())
//                        && appointment.getEnd().isAfter(peroid.getEnd()) || appointment.getEnd().equals(peroid.getEnd())) {
//                    peroid.setEnd(appointment.getStart());
//                }
//                if (appointment.getStart().isAfter(peroid.getStart()) && appointment.getEnd().isBefore(peroid.getEnd())) {
//                    toAdd.add(new TimePeriod(peroid.getStart(), appointment.getStart()));
//                    peroid.setStart(appointment.getEnd());
//                }
//            }
//        }
//        peroids.addAll(toAdd);
//        Collections.sort(peroids);
//        return peroids;
//    }


}
