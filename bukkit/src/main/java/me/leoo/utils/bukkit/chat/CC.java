package me.leoo.utils.bukkit.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CC {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static TextComponent getClickableMessage(String text, String hover, String command, ClickEvent.Action action) {
        TextComponent component = new TextComponent(text);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        component.setClickEvent(new ClickEvent(action, command));

        return component;
    }
}
