package me.leoo.utils.common.number;

import java.util.TreeMap;

public class NumberUtil {

    private static final TreeMap<Integer, String> romanNumbers = new TreeMap<>();

    static {
        romanNumbers.put(1000, "M");
        romanNumbers.put(900, "CM");
        romanNumbers.put(500, "D");
        romanNumbers.put(400, "CD");
        romanNumbers.put(100, "C");
        romanNumbers.put(90, "XC");
        romanNumbers.put(50, "L");
        romanNumbers.put(40, "XL");
        romanNumbers.put(10, "X");
        romanNumbers.put(9, "IX");
        romanNumbers.put(5, "V");
        romanNumbers.put(4, "IV");
        romanNumbers.put(1, "I");
    }

    public static int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }

        return 0;
    }

    public static float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }

        return 0;
    }

    public static double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static String convertToRomanNumeral(int number) {
        int l = romanNumbers.floorKey(number);
        if (number == l) {
            return romanNumbers.get(number);
        }
        return romanNumbers.get(l) + convertToRomanNumeral(number - l);
    }
}
