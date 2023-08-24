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

    public void addIfNotExists(String path, Object value) {
        if (yml.get(path) == null) {
            set(path, value);
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
        return getList(path).stream()
                .map(LocationUtil::deserializeLocation)
                .collect(Collectors.toList());
    }

    public void saveLocation(String path, Location location) {
        yml.set(path, LocationUtil.serializeLocation(location));
        save();
    }

    public void saveLocations(String path, List<Location> locations) {
        List<String> list = getList(path);
        list.addAll(locations.stream().map(LocationUtil::serializeLocation).collect(Collectors.toList()));

        yml.set(path, list);
        save();
    }

    //action method from config
    public boolean executeAction(String path, Player player) {
        if (getYml().get(path + ".command") == null) return false;

        String string = getString(path + ".command");

        if (!string.contains("[") && !string.contains("]")) return false;

        String type = string.substring(string.indexOf('[') + 1, string.indexOf(']'));
        String value = string.substring(string.indexOf(']') + 1);

        if (type.equals("default")) {
            return false;
        } else {
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
                case "no-action":
                    break;
            }

            return true;
        }
    }

    //group methods
    private String getGroupPath(String prefix, String path, String group) {
        String prefixPath = prefix.isEmpty() ? "" : prefix + ".";
        String pathPath = path.isEmpty() ? "" : "." + path;

        return prefixPath + (yml.get(prefixPath + group + pathPath) == null ? "Default" : group) + path;
    }

    public String getGroupString(String prefix, String path, String group) {
        return getString(getGroupPath(prefix, path, group));
    }

    public List<String> getGroupList(String prefix, String path, String group) {
        return getList(getGroupPath(prefix, path, group));
    }

    public boolean getGroupBoolean(String prefix, String path, String group) {
        return getBoolean(getGroupPath(prefix, path, group));
    }

    public int getGroupInt(String prefix, String path, String group) {
        return getInt(getGroupPath(prefix, path, group));
    }

    public double getGroupDouble(String prefix, String path, String group) {
        return getDouble(getGroupPath(prefix, path, group));
    }
}