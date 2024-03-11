package me.leoo.utils.common.time;

import lombok.experimental.UtilityClass;
import me.leoo.utils.common.number.NumberUtil;

import java.util.Calendar;
import java.util.TreeMap;

@UtilityClass
public class TimeUtil {

    public static final TreeMap<String, Integer> TIME_VALUES = new TreeMap<>();

    static {
        for (String s : new String[]{"s", "second", "seconds"}) {
            TIME_VALUES.put(s, 1);
        }

        for (String s : new String[]{"m", "minute", "minutes"}) {
            TIME_VALUES.put(s, 60);
        }

        for (String s : new String[]{"h", "hour", "hours"}) {
            TIME_VALUES.put(s, 60 * 60);
        }

        for (String s : new String[]{"d", "day", "days"}) {
            TIME_VALUES.put(s, 24 * 60 * 60);
        }

        for (String s : new String[]{"w", "week", "weeks"}) {
            TIME_VALUES.put(s, 7 * 24 * 60 * 60);
        }

        for (String s : new String[]{"month", "months"}) {
            TIME_VALUES.put(s, 30 * 7 * 24 * 60 * 60);
        }

        for (String s : new String[]{"y", "year", "years"}) {
            TIME_VALUES.put(s, 365 * 24 * 60 * 60);
        }
    }

    public long millisFromTimeString(String time) {
        if (time == null || time.isEmpty()) return -1;

        for (String unit : TIME_VALUES.keySet()) {
            if (!time.endsWith(unit)) continue;

            String replaced = time.replace(unit, "").trim();
            if (replaced.isEmpty()) continue;

            return NumberUtil.toInt(replaced) * TIME_VALUES.get(unit) * 1000L;
        }

        return -1;
    }

    public String timeStringFromMillis(long millis) {
        long seconds = millis / 1000L;

        if (seconds <= 0) {
            return "0 seconds";
        }

        long minutes = seconds / 60;
        seconds %= 60;

        long hours = minutes / 60;
        minutes %= 60;

        long days = hours / 24;
        hours %= 24;

        long years = days / 365;
        days %= 365;

        StringBuilder builder = new StringBuilder();
        if (years > 0) {
            builder.append(years).append(" year").append(years > 1 ? "s" : "").append(" ");
        }

        if (days > 0) {
            builder.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }

        if (hours > 0) {
            builder.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }

        if (minutes > 0) {
            builder.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        }

        if (seconds > 0) {
            builder.append(seconds).append(" second").append(seconds > 1 ? "s" : "").append(" ");
        }

        return builder.toString().trim();
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

    public int getDayOfYear() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * example: current year 2024 -> return 4
     */
    public int getYearLatestNumber() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        return year % 10;
    }
}
