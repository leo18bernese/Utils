package me.leoo.utils.bukkit.software;

import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.common.compatibility.SoftwareUtils;
import org.bukkit.Bukkit;

public class Software extends SoftwareUtils {

    @Override
    public void info(String text) {
        Bukkit.getLogger().info(text);
    }

    @Override
    public void warning(String text) {
        Bukkit.getLogger().warning(text);
    }

    @Override
    public void severe(String text) {
        Bukkit.getLogger().severe(text);
    }

    @Override
    public String color(String text) {
        return CC.color(text);
    }
}
