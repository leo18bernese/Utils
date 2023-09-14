package me.leoo.utils.bukkit.bukkit;

import org.bukkit.Bukkit;

public class BukkitUtils {
    public static final String VERSION_STRING = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static final int VERSION = Integer.parseInt(VERSION_STRING.split("_")[1]);

    public static boolean supports(int version) {
        return VERSION >= version;
    }
}
