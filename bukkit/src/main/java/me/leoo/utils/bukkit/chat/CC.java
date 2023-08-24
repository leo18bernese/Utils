package me.leoo.utils.bukkit.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class CC {

    public static final String BLUE = "§9";
    public static final String AQUA = "§b";
    public static final String YELLOW = "§e";
    public static final String RED = "§c";
    public static final String GRAY = "§7";
    public static final String GOLD = "§6";
    public static final String GREEN = "§a";
    public static final String WHITE = "§f";
    public static final String BLACK = "§0";
    public static final String BOLD = "§l";
    public static final String ITALIC = "§o";
    public static final String UNDER_LINE = "§n";
    public static final String STRIKE_THROUGH = "§m";
    public static final String RESET = "§r";
    public static final String DARK_BLUE = "§1";
    public static final String DARK_AQUA = "§3";
    public static final String DARK_GRAY = "§8";
    public static final String DARK_GREEN = "§2";
    public static final String DARK_PURPLE = "§5";
    public static final String DARK_RED = "§4";
    public static final String PINK = "§d";

    public static String color(String s) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            s = PlaceholderAPI.setPlaceholders(null, s);
        }

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static TextComponent getClickableMessage(String text, String hover, String command, ClickEvent.Action action) {
        TextComponent component = new TextComponent(text);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        component.setClickEvent(new ClickEvent(action, command));

        return component;
    }

    public static String translatePluginInfo(Plugin plugin, String s) {
        PluginDescriptionFile info = plugin.getDescription();

        return s.replace("{name}", info.getName())
                .replace("{version}", info.getVersion())
                .replace("{author}", info.getAuthors().toString().replace("[", "").replace("]", ""))
                .replace("{description}", info.getDescription());
    }
}
