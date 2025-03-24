package me.leoo.utils.common.compatibility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtils {

    // Instance
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
        CommonUtils.utils = utils;
    }
}
