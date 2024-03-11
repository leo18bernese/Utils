package me.leoo.utils.bukkit.chat;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import me.leoo.utils.bukkit.config.ConfigManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CC {

    public final String BLUE = "§9";
    public final String AQUA = "§b";
    public final String YELLOW = "§e";
    public final String RED = "§c";
    public final String GRAY = "§7";
    public final String GOLD = "§6";
    public final String GREEN = "§a";
    public final String WHITE = "§f";
    public final String BLACK = "§0";
    public final String BOLD = "§l";
    public final String ITALIC = "§o";
    public final String UNDER_LINE = "§n";
    public final String STRIKE_THROUGH = "§m";
    public final String MAGENTA_RGB = "#%%__USER__%%0";
    public final String RESET = "§r";
    public final String DARK_BLUE = "§1";
    public final String DARK_AQUA = "§3";
    public final String DARK_GRAY = "§8";
    public final String DARK_GREEN = "§2";
    public final String DARK_PURPLE = "§5";
    public final String DARK_RED = "§4";
    public final String PINK = "§d";

    public String color(String string) {
        if (string == null) return "";

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            string = PlaceholderAPI.setPlaceholders(null, string);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public String color(String string, Player player) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> color(List<String> strings) {
        return strings.stream().map(CC::color).collect(Collectors.toList());
    }

    public String strip(String string) {
        return ChatColor.stripColor(string);
    }

    // TextComponent methods
    public TextComponent getClickableMessage(String text, String hover, String command, ClickEvent.Action action) {
        TextComponent component = new TextComponent(text);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        component.setClickEvent(new ClickEvent(action, command));

        return component;
    }

    public void sendClickableMessage(CommandSender player, String text, String hover, String command, ClickEvent.Action action) {
        if (player instanceof Player) {
            ((Player) player).spigot().sendMessage(getClickableMessage(text, hover, command, action));
        } else {
            player.sendMessage(text);
        }
    }

    public void sendClickableMessage(ConfigManager config, CommandSender player, String path) {
        String[] split = config.getString(path).split(";");
        sendClickableMessage(player, split[0], split[1], split[2], ClickEvent.Action.valueOf(split[3]));
    }

    // Plugin info
    public String translatePluginInfo(Plugin plugin, String s) {
        PluginDescriptionFile info = plugin.getDescription();

        return s.replace("{name}", info.getName())
                .replace("{version}", info.getVersion())
                .replace("{author}", info.getAuthors().toString().replace("[", "").replace("]", ""))
                .replace("{description}", info.getDescription());
    }

    public Color getColor(java.awt.Color javaColor) {
        return Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
    }
}
