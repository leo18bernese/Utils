package me.leoo.utils.common.time;

import lombok.experimental.UtilityClass;
import me.leoo.utils.common.java.JavaUtil;
import me.leoo.utils.common.number.NumberUtil;

import java.util.*;

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

            if (!NumberUtil.isInt(replaced)) continue;

            return NumberUtil.toInt(replaced) * TIME_VALUES.get(unit) * 1000L;
        }

        return -1;
    }

    public String timeStringFromMillis(long millis) {
        List<String> list = new ArrayList<>();

        long next = 0;

        for (TimeUnit unit : TimeUnit.values()) {

            TimeUnit nextUnit = JavaUtil.getNextEnum(TimeUnit.class, unit.ordinal());
            if (nextUnit == null || nextUnit.ordinal() == 0) break;

            long current;

            if (unit == TimeUnit.SECOND) {
                current = millis / unit.getPartOf();

                if (current <= 0) return "0 " + unit.getNames()[1];
            } else {
                current = next;
            }

            next = current / nextUnit.getPartOf();
            current %= nextUnit.getPartOf();

            if (current <= 0) continue;

            list.add(unit.format(current) + " ");
        }

        //reverse list
        Collections.reverse(list);
        return String.join("", list);
    }

    /*public String timeStringFromMillis(long millis,
                                       String yearName, String yearsName, String dayName, String daysName,
                                       String hourName, String hoursName, String minuteName, String minutesName,
                                       String secondName, String secondsName) {
        int seconds = (int) (millis / 1000L);

        if (seconds <= 0) {
            return "0 " + seconds;
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
            builder.append(years).append(" ").append(years > 1 ? yearsName : yearName).append(" ");
        }

        if (days > 0) {
            builder.append(days).append(" ").append(days > 1 ? daysName : dayName).append(" ");
        }

        if (hours > 0) {
            builder.append(hours).append(" ").append(hours > 1 ? hoursName : hourName).append(" ");
        }

        if (minutes > 0) {
            builder.append(minutes).append(" ").append(minutes > 1 ? minutesName : minuteName).append(" ");
        }

        if (seconds > 0) {
            builder.append(seconds).append(" ").append(seconds > 1 ? secondsName : secondName);
        }


        return builder.toString().trim();
    }*/

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
