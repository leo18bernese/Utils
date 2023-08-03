package me.leoo.utils.common.compatibility;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class SoftwareUtils {

    @Setter
    private static SoftwareUtils instance;

    public abstract void info(String text);

    public abstract void warning(String text);

    public abstract void severe(String text);

    public abstract String color(String text);
}
