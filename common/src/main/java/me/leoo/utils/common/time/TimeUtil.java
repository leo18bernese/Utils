package me.leoo.utils.common.time;

import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class TimeUtil {

    public int secondsByTimeString(String time) {
        Map<String, Integer> values = new HashMap<>();

        for (String s : new String[]{"s", "second", "seconds"}) {
            values.put(s, 1);
        }

        for (String s : new String[]{"m", "minute", "minutes"}) {
            values.put(s, 60);
        }

        for (String s : new String[]{"h", "hour", "hours"}) {
            values.put(s, 60 * 60);
        }

        for (String s : new String[]{"d", "day", "days"}) {
            values.put(s, 24 * 60 * 60);
        }

        for (String s : new String[]{"w", "week", "weeks"}) {
            values.put(s, 7 * 24 * 60 * 60);
        }

        for (String s : new String[]{"month", "months"}) {
            values.put(s, 30 * 7 * 24 * 60 * 60);
        }

        for (String unit : values.keySet()) {
            if (!time.endsWith(unit)) continue;

            return Integer.parseInt(time.replace(unit, "").trim()) * values.get(unit) * 1000;
        }

        return 0;
    }

    public boolean compareDate(long date1, int compareType) {
        return compareDate(date1, System.currentTimeMillis(), compareType);
    }

    public boolean compareDate(long date1, long date2, int compareType) {
        Calendar calendar1 = getCalendar(date1);
        Calendar calendar2 = getCalendar(date2);

        return calendar1.get(compareType) == calendar2.get(compareType);
    }

    public Calendar getCalendar(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return calendar;
    }
}
