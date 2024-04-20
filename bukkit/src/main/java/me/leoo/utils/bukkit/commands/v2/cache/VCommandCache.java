package me.leoo.utils.bukkit.commands.v2.cache;

import lombok.Getter;
import me.leoo.utils.bukkit.commands.v2.VCommandBuilder;
import me.leoo.utils.bukkit.commands.v2.tabcomplete.VTabComplete;

import java.util.HashMap;
import java.util.Map;

public class VCommandCache {

    @Getter
    private static final Map<String, VCommandBuilder> commands = new HashMap<>();

    @Getter
    private static final Map<String, VTabComplete> tabComplete = new HashMap<>();

    @Getter
    private static final Map<String, Object> instances = new HashMap<>();

    public static VCommandBuilder getCommand(String name) {
        return commands.get(name);
    }

    public static VTabComplete getTabComplete(String name) {
        return tabComplete.get(name);
    }

    public static Object getInstance(String name) {
        return instances.get(name);
    }
}
