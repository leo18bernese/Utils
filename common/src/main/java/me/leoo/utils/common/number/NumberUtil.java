package me.leoo.utils.common.number;

import lombok.experimental.UtilityClass;

import java.util.TreeMap;

@UtilityClass
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

    public int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }

        return 0;
    }

    public float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }

        return 0;
    }

    public double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public String toAlphabet(int number) {
        StringBuilder builder = new StringBuilder();

        while (number-- != 0) {
            int remainder = number % 26;
            builder.insert(0, (char) (65 + remainder));

            number /= 26;
        }

        return builder.toString();
    }

    public String convertToRomanNumeral(int number) {
        int l = romanNumbers.floorKey(number);
        if (number == l) {
            return romanNumbers.get(number);
        }
        return romanNumbers.get(l) + convertToRomanNumeral(number - l);
    }
}
