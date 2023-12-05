package me.leoo.utils.common.java;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class JavaUtil {

    public <T extends Enum<T>> T getNextEnum(Class<T> clazz, int ordinal) {
        T[] values = clazz.getEnumConstants();

        return values[(ordinal + 1) % values.length];
    }

    public <T extends Enum<T>> T getPreviousEnum(Class<T> clazz, int ordinal) {
        T[] values = clazz.getEnumConstants();

        return values[(ordinal - 1 + values.length) % values.length];
    }


    public <T extends Enum<T>> T getFiltered(Class<T> clazz, Predicate<? super T> predicate) {
        return Arrays.stream(clazz.getEnumConstants()).filter(predicate).findAny().orElse(null);
    }

    public <T extends Enum<T>> List<T> getFilteredList(Class<T> clazz, Predicate<? super T> predicate) {
        return Arrays.stream(clazz.getEnumConstants()).filter(predicate).collect(Collectors.toList());
    }
}
