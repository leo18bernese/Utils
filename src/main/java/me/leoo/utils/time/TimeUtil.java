package me.leoo.utils.time;

import java.util.HashMap;

public class TimeUtil {

    public static int secondsByTimeString(String time) {
        HashMap<String, Integer> values = new HashMap<>();

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
}
