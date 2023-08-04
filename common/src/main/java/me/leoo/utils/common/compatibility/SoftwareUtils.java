package me.leoo.utils.common.compatibility;

import lombok.Getter;
import lombok.Setter;

public abstract class SoftwareUtils {

    @Getter
    @Setter
    private static SoftwareUtils instance;

    public abstract void info(String text);

    public abstract void warning(String text);

    public abstract void severe(String text);

    public abstract String color(String text);
}
