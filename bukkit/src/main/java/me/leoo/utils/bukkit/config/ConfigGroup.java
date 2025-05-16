package me.leoo.utils.bukkit.config;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ConfigGroup {

    private final ConfigManager config;

    public String getPath(String path, String subPath, String group) {
        path = path.isEmpty() ? "" : path + ".";
        subPath = subPath.isEmpty() ? "" : "." + subPath;

        return path + (config.contains(path + group + subPath) ? group : "Default") + subPath;
    }

    public <T> T get(String path, String subPath, String group) {
        return (T) config.getYml().get(getPath(path, subPath, group));
    }

    public <T> T getElse(String path, String subPath, String group, ConfigManager other) {
        return config.contains(getPath(path, subPath, group)) ? get(path, subPath, group) : (T) other.getYml().get(subPath);
    }

    public <T> T condition(String path, String subPath, String group, ConfigManager other, boolean condition) {
        return condition && config.contains(getPath(path, subPath, group)) ? get(path, subPath, group) : (T) other.getYml().get(subPath);
    }

    // Old method with checks
    public String getString(String path, String subPath, String group) {
        return config.getString(config.getGroupPath(path, subPath, group));
    }

    public List<String> getList(String path, String subPath, String group) {
        return config.getList(config.getGroupPath(path, subPath, group));
    }

    public Set<String> getKeys(String path, String subPath, String group) {
        return config.getKeys(config.getGroupPath(path, subPath, group));
    }

    public ConfigurationSection getSection(String path, String subPath, String group) {
        return config.getSection(config.getGroupPath(path, subPath, group));
    }

    public boolean getBoolean(String path, String subPath, String group) {
        return config.getBoolean(config.getGroupPath(path, subPath, group));
    }

    public int getInt(String path, String subPath, String group) {
        return config.getInt(config.getGroupPath(path, subPath, group));
    }

    public double getDouble(String path, String subPath, String group) {
        return config.getDouble(config.getGroupPath(path, subPath, group));
    }
}