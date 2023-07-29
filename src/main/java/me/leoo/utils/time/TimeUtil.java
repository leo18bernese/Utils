package me.leoo.utils.time;

public class TimeUtil {

    public static int secondsByTimeString(String time) {
        int converted = 0;

        if (time.endsWith("s") || time.endsWith("second") || time.endsWith("seconds")) {
            converted = Integer.parseInt(time.replace("seconds", "").replace("second", "").replace("s", ""));
        } else if (time.endsWith("m") || time.endsWith("minute") || time.endsWith("minutes")) {
            converted = Integer.parseInt(time.replace("minutes", "").replace("minute", "").replace("m", "")) * 60;
        } else if (time.endsWith("h") || time.endsWith("hour") || time.endsWith("hours")) {
            converted = Integer.parseInt(time.replace("hours", "").replace("hour", "").replace("h", "")) * 60 * 60;
        } else if (time.endsWith("d") || time.endsWith("day") || time.endsWith("days")) {
            converted = Integer.parseInt(time.replace("days", "").replace("day", "").replace("d", "")) * 24 * 60 * 60;
        } else if (time.endsWith("w") || time.endsWith("week") || time.endsWith("weeks")) {
            converted = Integer.parseInt(time.replace("weeks", "").replace("week", "").replace("w", "")) * 7 * 24 * 60 * 60;
        } else if (time.endsWith("month") || time.endsWith("months")) {
            converted = Integer.parseInt(time.replace("months", "").replace("month", "")) * 30 * 7 * 24 * 60 * 60;
        } else if (time.endsWith("y") || time.endsWith("year") || time.endsWith("years")) {
            converted = Integer.parseInt(time.replace("years", "").replace("year", "").replace("y", "")) * 365 * 30 * 7 * 24 * 60 * 60;
        }

        return converted * 1000;
    }
}
