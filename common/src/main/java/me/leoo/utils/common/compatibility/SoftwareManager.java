package me.leoo.utils.common.compatibility;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SoftwareManager {

    @Getter
    private static SoftwareUtils utils;

    public void info(String text) {
        utils.info(text);
    }

    public void warning(String text) {
        utils.warning(text);
    }

    public void severe(String text) {
        utils.severe(text);
    }

    public String color(String text) {
        return utils.color(text);
    }

    public void init(SoftwareUtils utils) {
        SoftwareManager.utils = utils;
    }
}
