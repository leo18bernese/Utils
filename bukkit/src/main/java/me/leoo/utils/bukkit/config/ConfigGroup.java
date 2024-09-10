package me.leoo.utils.bukkit.config;

import lombok.RequiredArgsConstructor;

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
}