package it.mtempobono.mechanicalappointment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class DayPlan {

    private TimePeriod workingHours;
    private List<TimePeriod> breaks;

    public DayPlan() {
        breaks = new ArrayList();
    }

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
    }

}
