package me.leoo.utils.common.time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum TimeUnit {

    SECOND(1000),
    MINUTE( 60),
    HOUR(60),
    DAY(24),
    YEAR(365);

    private final long partOf;
    //private final Function<Long, Long> fromMillis;

    private static final Map<TimeUnit, String[]> NAMES = new HashMap<>();

    public String format(long value){
        String[] names = NAMES.get(this);

        return value + " " + (value == 1 ? names[0] : names[1]);
    }

    /*public String format(long millis) {
        String[] names = NAMES.get(this);

        long value = get(millis);

        return value + " " + (value == 1 ? names[0] : names[1]);
    }*/

    public long get(long millis) {
        return millis / partOf;
    }

    public String[] getNames() {
        return NAMES.get(this);
    }

    public static void setNames(String yearName, String yearsName, String dayName, String daysName,
                                String hourName, String hoursName, String minuteName, String minutesName,
                                String secondName, String secondsName) {
        NAMES.clear();

        setNames(YEAR, yearName, yearsName);
        setNames(DAY, dayName, daysName);
        setNames(HOUR, hourName, hoursName);
        setNames(MINUTE, minuteName, minutesName);
        setNames(SECOND, secondName, secondsName);
    }

    public static void setNames(TimeUnit unit, String singular, String plural) {
        NAMES.put(unit, new String[]{singular, plural});
    }

    static {
        setNames("year", "years",
                "day", "days",
                "hour", "hours",
                "minute", "minutes",
                "second", "seconds");
    }
}
