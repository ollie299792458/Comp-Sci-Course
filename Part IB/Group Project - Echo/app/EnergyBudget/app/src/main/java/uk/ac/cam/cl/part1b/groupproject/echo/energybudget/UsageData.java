package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class UsageData {
    public static final String TAG = "UsageData";

    private HashMap<TimeUnit, TreeMap<Integer, Double>> data;
    private HashMap<TimeUnit, TreeMap<Integer, Integer>> ranges;

    public UsageData() {
        data = new HashMap<>();
        ranges = new HashMap<>();

        for (TimeUnit t : TimeUnit.values()) {
            data.put(t, new TreeMap<>());
            ranges.put(t, new TreeMap<>());
        }
    }

    /**
     *
     * @param timeUnit
     * @param start inclusive start time
     * @param end inclusive end time
     * @return true if the range is already included
     */
    public boolean hasRange(TimeUnit timeUnit, int start, int end) {
        TreeMap<Integer, Integer> unitRange = ranges.get(timeUnit);
        if (unitRange.isEmpty()) return false;

        Integer prevStart = unitRange.floorKey(start);
        if (prevStart != null) {
            int prevEnd = unitRange.get(prevStart);
            if (end - prevEnd < TimeUnit.toSec(timeUnit)) return true;
        }

        return false;
    }

    public void putRange(TimeUnit timeUnit, TreeMap<Integer, Double> range) {
        if (range.isEmpty()) return;

        // put the new values anyway
        data.get(timeUnit).putAll(range);

        int unitSecs = TimeUnit.toSec(timeUnit);
        TreeMap<Integer, Integer> unitRange = ranges.get(timeUnit);

        int startTime = range.firstKey();
        int endTime = range.lastKey();

        Integer prevStart = unitRange.floorKey(startTime);

        if (prevStart != null) {
            int prevEnd = unitRange.get(prevStart);
            if (startTime - prevEnd <= unitSecs) {
                // append to previous range
                if (prevEnd >= endTime) {
                    // range was already included
                    return;
                }
                startTime = prevStart;
            }
        }
        //Necessary?
        Integer nextStart = unitRange.higherKey(startTime);
        if (nextStart != null) {
            int nextEnd = unitRange.get(nextStart);
            if (nextStart - endTime <= unitSecs) {
                // merge with next range
                unitRange.remove(nextStart);
                if (endTime < nextEnd) {
                    endTime = nextEnd;
                }
            }
        }
        unitRange.put(startTime, endTime);

        // Use days to build up weeks
        if (timeUnit == TimeUnit.DAY) {
            putWeeks(range);
        }
    }

    private void putWeeks(TreeMap<Integer, Double> days) {
        TreeMap<Integer, Double> weekRange = new TreeMap<>();
        Iterator<Map.Entry<Integer, Double>> iter = days.entrySet().iterator();
        Calendar c = Calendar.getInstance();
        Map.Entry<Integer, Double> e, prevE = null;

        while (prevE != null || iter.hasNext()) {
            if (prevE != null) {
                // Make sure we don't skip over values
                e = prevE;
                prevE = null;
            } else {
                e = iter.next();
            }

            int startT = e.getKey();
            double avg = e.getValue();
            c.setTime(new Date((long)startT * 1000L));
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != 1) continue;

            int numDays = 1;
            while (numDays < 7 && iter.hasNext()) {
                e = iter.next();

                int t = e.getKey();
                if (t > startT + TimeUnit.toSec(TimeUnit.WEEK)) {
                    prevE = e;
                    break;
                }

                avg += e.getValue();
                numDays += 1;
            }
            avg /= (double)numDays;
            weekRange.put(startT, avg);
        }
        putRange(TimeUnit.WEEK, weekRange);
    }

    /**
     * NOTE: treat as read-only
     * NOTE: use check hasRange first to see if range is filled
     * @param timeUnit
     * @return all existing items in the range
     */
    public TreeMap<Integer, Double> getRange(TimeUnit timeUnit, int startTime, int endTime) {
        TreeMap<Integer, Double> unitData = data.get(timeUnit);

        return new TreeMap<>(unitData.subMap(startTime, endTime + 1));
    }
}
