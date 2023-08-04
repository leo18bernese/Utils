package me.leoo.utils.velocity.software;

import me.leoo.utils.common.compatibility.SoftwareUtils;
import me.leoo.utils.velocity.Utils;
import me.leoo.utils.velocity.chat.CC;

public class Software extends SoftwareUtils {

    @Override
    public void info(String text) {

        Utils.get().getLogger().info(text);
    }

    @Override
    public void warning(String text) {
        Utils.get().getLogger().warn(text);
    }

    @Override
    public void severe(String text) {
        Utils.get().getLogger().error(text);
    }

    @Override
    public String color(String text) {
        return CC.color(text);
    }
}
