package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;


import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.HashMap;

public enum TimeUnit {
    SEC, MIN, HALFHOUR, HOUR, DAY, WEEK, MONTH, YEAR;

    private static final SparseArray<TimeUnit> idToTimeUnit = new SparseArray<>();
    private static final SparseIntArray timeUnitToSec = new SparseIntArray();
    private static final HashMap<TimeUnit, Integer> timeUnitToId = new HashMap<>();

    static {
        idToTimeUnit.put(10, TimeUnit.SEC);
        idToTimeUnit.put(20, TimeUnit.MIN);
        idToTimeUnit.put(25, TimeUnit.HALFHOUR);
        idToTimeUnit.put(30, TimeUnit.HOUR);
        idToTimeUnit.put(40, TimeUnit.DAY);
        idToTimeUnit.put(45, TimeUnit.WEEK); // Not in database but we might need it
        idToTimeUnit.put(50, TimeUnit.MONTH);
        idToTimeUnit.put(60, TimeUnit.YEAR);

        for (int i = 0; i < idToTimeUnit.size(); i += 1) {
            timeUnitToId.put(idToTimeUnit.valueAt(i), idToTimeUnit.keyAt(i));
        }

        timeUnitToSec.put(10, 1);
        timeUnitToSec.put(20, 60);
        timeUnitToSec.put(25, timeUnitToSec.get(20) * 30);
        timeUnitToSec.put(30, timeUnitToSec.get(20) * 60);
        timeUnitToSec.put(40, timeUnitToSec.get(30) * 24);
        timeUnitToSec.put(45, timeUnitToSec.get(40) * 7);
        timeUnitToSec.put(50, timeUnitToSec.get(40) * 31); // round up
        timeUnitToSec.put(60, timeUnitToSec.get(40) * 366); // round up
    }

    public static int toId(TimeUnit t) {
        return timeUnitToId.get(t);
    }

    public static TimeUnit fromId(int id) {
        return idToTimeUnit.get(id);
    }

    public static int toSec(int id) {
        return timeUnitToSec.get(id);
    }

    public static int toSec(TimeUnit t) {
        return toSec(toId(t));
    }

    public static int nowSec() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
