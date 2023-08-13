package me.leoo.utils.bukkit.config;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.location.LocationUtil;
import me.leoo.utils.common.file.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {

    private YamlConfiguration yml;
    private final File config;
    private String name;

    public ConfigManager(String name, String dir) {
        config = FileUtil.generateFile(name + ".yml", dir);

        if (config == null) return;

        yml = YamlConfiguration.loadConfiguration(config);
        yml.options().copyDefaults(true);

        this.name = name;
    }

    public ConfigManager(Plugin plugin, String name) {
        this(name, String.valueOf(plugin.getDataFolder()));
    }

    public void reload() {
        yml = YamlConfiguration.loadConfiguration(config);
    }

    public void save() {
        try {
            yml.save(config);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        yml.set(path, value);
        save();
    }

    public boolean getBoolean(String path) {
        return yml.getBoolean(path);
    }

    public int getInt(String path) {
        return yml.getInt(path);
    }

    public double getDouble(String path) {
        return yml.getDouble(path);
    }

    public String getString(String path) {
        String string = yml.getString(path);

        if (string == null) {
            Utils.get().getLogger().info("String " + path + " not found in " + name + ".yml");
            return "StringNotFound";
        }

        return CC.color(string);
    }

    public List<String> getList(String path) {
        return yml.getStringList(path).stream().map(CC::color).collect(Collectors.toList());
    }

    public List<Integer> getIntegerSplitList(String path) {
        return Arrays.stream(getString(path).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    //locations
    public Location getLocation(String path) {
        return LocationUtil.deserializeLocation(yml.getString(path));
    }

    public List<Location> getLocations(String path) {
        List<Location> locations = new ArrayList<>();
        getList(path).forEach(s -> locations.add(LocationUtil.deserializeLocation(s)));

        return locations;
    }

    public void saveLocation(String path, Location location) {
        yml.set(path, LocationUtil.serializeLocation(location));
        save();
    }

    public void saveLocations(String path, List<Location> locations) {
        List<String> strings = new ArrayList<>(getList(path));
        locations.forEach(location -> strings.add(LocationUtil.serializeLocation(location)));

        yml.set(path, strings);
        save();
    }

    //action method from config
    public void executeAction(String path, Player player) {
        String string = getString(path);

        String type = string.substring(string.indexOf('[') + 1, string.indexOf(']'));
        String value = string.substring(string.indexOf(']') + 1);

        switch (type) {
            case "command":
                player.performCommand(value);
                break;
            case "console":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
                break;
            case "server":
                ByteArrayDataOutput out = ByteStreams.newDataOutput();

                out.writeUTF("Connect");
                out.writeUTF(value);

                player.sendPluginMessage(Utils.get(), "BungeeCord", out.toByteArray());
                break;
        }
    }

    //group methods
    public String getGroupString(String path, String group) {
        return getString((yml.get(group + "." + path) == null ? "Default" : group) + "." + path);
    }

    public List<String> getGroupList(String path, String group) {
        return getList((yml.get(group + "." + path) == null ? "Default" : group) + "." + path);
    }

    public boolean getGroupBoolean(String path, String group) {
        return getBoolean((yml.get(group + "." + path) == null ? "Default" : group) + "." + path);
    }

    public int getGroupInt(String path, String group) {
        return getInt((yml.get(group + "." + path) == null ? "Default" : group) + "." + path);
    }

    public double getGroupDouble(String path, String group) {
        return getDouble((yml.get(group + "." + path) == null ? "Default" : group) + "." + path);
    }
}