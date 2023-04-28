package it.mtempobono.mechanicalappointment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class DayPlan implements Serializable {
    @Schema(description = "The working hours")
    private TimePeriod workingHours;

    @Schema(description = "The list of breaks")
    private List<TimePeriod> breaks;

    public DayPlan() {
        breaks = new ArrayList<>();
    }

    /*TODO  @deprecated
        public List<TimePeriod> timePeroidsWithBreaksExcluded() {
        ArrayList<TimePeriod> timePeroidsWithBreaksExcluded = new ArrayList<>();
        timePeroidsWithBreaksExcluded.add(new TimePeriod(getWorkingHours().getStart(), getWorkingHours().getEnd()));
        List<TimePeriod> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            ArrayList<TimePeriod> toAdd = new ArrayList();
            for (TimePeriod break1 : breaks) {
                if (break1.getStart().isBefore(workingHours.getStart())) {
                    break1.setStart(workingHours.getStart());
                }
                if (break1.getEnd().isAfter(workingHours.getEnd())) {
                    break1.setEnd(workingHours.getEnd());
                }
                for (TimePeriod peroid : timePeroidsWithBreaksExcluded) {
                    if (break1.getStart().equals(peroid.getStart()) && break1.getEnd().isAfter(peroid.getStart()) && break1.getEnd().isBefore(peroid.getEnd())) {
                        peroid.setStart(break1.getEnd());
                    }
                    if (break1.getStart().isAfter(peroid.getStart()) && break1.getStart().isBefore(peroid.getEnd()) && break1.getEnd().equals(peroid.getEnd())) {
                        peroid.setEnd(break1.getStart());
                    }
                    if (break1.getStart().isAfter(peroid.getStart()) && break1.getEnd().isBefore(peroid.getEnd())) {
                        toAdd.add(new TimePeriod(peroid.getStart(), break1.getStart()));
                        peroid.setStart(break1.getEnd());
                    }
                }
            }
            timePeroidsWithBreaksExcluded.addAll(toAdd);
            Collections.sort(timePeroidsWithBreaksExcluded);
        }


        return timePeroidsWithBreaksExcluded;
    }*/

    public List<TimePeriod> timePeriodsWithBreaksExcluded() {
        // Create a new list for time periods without breaks and add the working hours
        ArrayList<TimePeriod> timePeriodsWithBreaksExcluded = new ArrayList<>();
        timePeriodsWithBreaksExcluded.add(new TimePeriod(getWorkingHours().getStart().getLocalTime(), getWorkingHours().getEnd().getLocalTime()));
        // Get the list of breaks
        List<TimePeriod> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            // Create a new list to add any additional time periods that result from excluding breaks
            ArrayList<TimePeriod> toAdd = new ArrayList<>();
            // Loop through each break
            for (TimePeriod break1 : breaks) {
                // If the break starts before the working hours, adjust the start time
                if (break1.getStart().getLocalTime().isBefore(workingHours.getStart().getLocalTime())) {
                    break1.setStart(workingHours.getStart());
                }
                // If the break ends after the working hours, adjust the end time
                if (break1.getEnd().getLocalTime().isAfter(workingHours.getEnd().getLocalTime())) {
                    break1.setEnd(workingHours.getEnd());
                }
                // Loop through each time period without breaks
                for (TimePeriod period : timePeriodsWithBreaksExcluded) {
                    // If the break starts at the same time as the time period, adjust the start time
                    if (break1.getStart().equals(period.getStart()) && break1.getEnd().getLocalTime().isAfter(period.getStart().getLocalTime()) && break1.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime())) {
                        period.setStart(break1.getEnd());
                    }
                    // If the break ends at the same time as the time period, adjust the end time
                    if (break1.getStart().getLocalTime().isAfter(period.getStart().getLocalTime()) && break1.getStart().getLocalTime().isBefore(period.getEnd().getLocalTime()) && break1.getEnd().equals(period.getEnd())) {
                        period.setEnd(break1.getStart());
                    }
                    // If the break is within the time period, split the time period and add the new time period
                    if (break1.getStart().getLocalTime().isAfter(period.getStart().getLocalTime()) && break1.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime())) {
                        toAdd.add(new TimePeriod(period.getStart().getLocalTime(), break1.getStart().getLocalTime()));
                        period.setStart(break1.getEnd());
                    }
                }
            }
            // Add any additional time periods resulting from excluding breaks to the list
            timePeriodsWithBreaksExcluded.addAll(toAdd);
            // Sort the time periods by start time
            Collections.sort(timePeriodsWithBreaksExcluded);
        }

        return timePeriodsWithBreaksExcluded;
    }

}
