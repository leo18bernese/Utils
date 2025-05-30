package me.leoo.utils.common.number;

import lombok.experimental.UtilityClass;
import me.leoo.utils.common.compatibility.CommonUtils;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class NumberUtil {

    private static final TreeMap<Integer, String> ROMAN_NUMBERS = new TreeMap<>();

    static {
        ROMAN_NUMBERS.put(1000, "M");
        ROMAN_NUMBERS.put(900, "CM");
        ROMAN_NUMBERS.put(500, "D");
        ROMAN_NUMBERS.put(400, "CD");
        ROMAN_NUMBERS.put(100, "C");
        ROMAN_NUMBERS.put(90, "XC");
        ROMAN_NUMBERS.put(50, "L");
        ROMAN_NUMBERS.put(40, "XL");
        ROMAN_NUMBERS.put(10, "X");
        ROMAN_NUMBERS.put(9, "IX");
        ROMAN_NUMBERS.put(5, "V");
        ROMAN_NUMBERS.put(4, "IV");
        ROMAN_NUMBERS.put(1, "I");
    }

    public boolean isInt(Object object) {
        try {
            Integer.parseInt(object.toString());
            return true;
        } catch (NumberFormatException | NullPointerException ignored) {
            return false;
        }
    }

    public int toInt(Object object) {
        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            CommonUtils.severe("Cannot convert " + object + " to int");

            return 0;
        }
    }

    public float toFloat(Object object) {
        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            CommonUtils.severe("Cannot convert " + object + " to float");

            return Float.NaN;
        }
    }

    public double toDouble(Object object) {
        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            CommonUtils.severe("Cannot convert " + object + " to double");

            return Double.NaN;
        }
    }

    public long toLong(Object object) {
        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            CommonUtils.severe("Cannot convert " + object + " to long");

            return 0L;
        }
    }

    public short toShort(Object object) {
        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            CommonUtils.severe("Cannot convert " + object + " to short");

            return 0;
        }
    }

    public byte toByte(Object object) {
        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            CommonUtils.severe("Cannot convert " + object + " to byte");

            return 0;
        }
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

    public String toRoman(int number) {
        int l = ROMAN_NUMBERS.floorKey(number);
        if (number == l) {
            return ROMAN_NUMBERS.get(number);
        }

        return ROMAN_NUMBERS.get(l) + toRoman(number - l);
    }

    public int getPercentage(int value, int max) {
        if (max == 0) return 0;

        return (int) ((value / (double) max) * 100);
    }

    public int random(int max) {
        return (int) (Math.random() * max);
    }

    public boolean testChance(int chance) {
        if (chance <= 0) return false;
        if (chance >= 100) return true;
        return ThreadLocalRandom.current().nextInt(100) < chance;
    }

    public int getTotalPages(int allItems, int perPage) {
        return allItems == 0 ? 1 : (int) Math.ceil((double) allItems / (double) perPage);
    }

    public <T> List<T> getSubList(List<T> list, int page, int perPage) {
        int pages = NumberUtil.getTotalPages(list.size(), 10);

        if (page > pages) page = pages;
        if (page < 1) page = 1;

        return list.subList((page - 1) * perPage, Math.min(page * perPage, list.size()));
    }
}
