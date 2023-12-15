package me.leoo.utils.bungee.chat;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

@UtilityClass
public class CC {

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(getBaseComponent(message));
    }

    public BaseComponent[] getBaseComponent(String text) {
        return new ComponentBuilder(text).create();
    }
}
