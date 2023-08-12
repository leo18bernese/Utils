package me.leoo.utils.bukkit.config;

import lombok.Getter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.location.LocationUtil;
import me.leoo.utils.common.file.FileUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

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