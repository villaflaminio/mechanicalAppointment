package it.flaminiovilla.mechanicalappointment.util.wrappers;

import it.flaminiovilla.mechanicalappointment.model.TimePeriod;

import java.util.Comparator;
import java.util.Map;

class CompareTimePeriod implements Comparator<TimePeriod> {
    Map<TimePeriod,Integer> map;
    /**
     * Constructs our map
     * @param map map to be sorted.
     */
    public CompareTimePeriod(Map<TimePeriod,Integer> map){
        this.map = map;
    }
    /**
     * Performs the comparison between two entries.
     */
    public int compare(TimePeriod left, TimePeriod right){
        return map.get(left).compareTo(map.get(right));
    }
}