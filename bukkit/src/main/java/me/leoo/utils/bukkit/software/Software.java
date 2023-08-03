package me.leoo.utils.bukkit.software;

import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.common.compatibility.SoftwareUtils;

public class Software extends SoftwareUtils {

    @Override
    public void info(String text) {
        Utils.get().getLogger().info(text);
    }

    @Override
    public void warning(String text) {
        Utils.get().getLogger().warning(text);
    }

    @Override
    public void severe(String text) {
        Utils.get().getLogger().severe(text);
    }

    @Override
    public String color(String text) {
        return CC.color(text);
    }
}
