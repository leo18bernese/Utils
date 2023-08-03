package me.leoo.utils.bungee.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class CC {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(getBaseComponent(message));
    }

    public static BaseComponent[] getBaseComponent(String text) {
        return new ComponentBuilder(text).create();
    }
}
