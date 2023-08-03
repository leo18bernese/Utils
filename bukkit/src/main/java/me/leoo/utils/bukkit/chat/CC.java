package me.leoo.utils.bukkit.chat;

import org.bukkit.ChatColor;

public class CC {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
