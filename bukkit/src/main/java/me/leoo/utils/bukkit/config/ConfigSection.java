package me.leoo.utils.bukkit.config;

import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.location.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Base class for configuration handling that contains common getter methods
 * for accessing configuration data from a YAML configuration file.
 */
public abstract class ConfigSection {

    /**
     * Get the name of the configuration file
     *
     * @return the name of the configuration file
     */
    public abstract String getName();

    /**
     * Get the YamlConfiguration object
     *
     * @return the YamlConfiguration object
     */
    public abstract YamlConfiguration getYml();

    /**
     * Check if a path exists in the configuration file
     *
     * @param path the path to check
     * @return true if the path exists, false otherwise
     */
    public boolean contains(String path) {
        return getYml().get(path) != null;
    }

    public void addIfNotExists(String path, Object value) {
        if (!contains(path)) add(path, value);
    }

    public void add(String path, Object value) {
        getYml().addDefault(path, value);
    }

    public void addList(String path, String... values) {
        add(path, values);
    }

    /**
     * Get a boolean value from the configuration file
     *
     * @param path the path to get the boolean from
     * @return the boolean value
     */
    public boolean getBoolean(String path) {
        return getYml().getBoolean(path);
    }

    /**
     * Get an integer value from the configuration file
     *
     * @param path the path to get the integer from
     * @return the integer value
     */
    public int getInt(String path) {
        return getYml().getInt(path);
    }

    /**
     * Get a double value from the configuration file
     *
     * @param path the path to get the double from
     * @return the double value
     */
    public double getDouble(String path) {
        return getYml().getDouble(path);
    }

    /**
     * <p> Gets a string from the config file.
     * <p> The method checks for the existence of the path and logs an error if it does not exist.
     * <p> The string is colored using the CC.color method.
     *
     * @param path the path to get the string from
     * @return "StringNotFound" if the path does not exist
     */
    public String getString(String path) {
        String string = getYml().getString(path);

        if (string == null) {
            Bukkit.getLogger().severe("String " + path + " not found in " + getName() + ".yml");
            return "StringNotFound";
        }

        return CC.color(string);
    }

    /**
     * <p> Gets a list of strings from the config file.
     * <p> The method checks for the existence of the path and logs an error if it does not exist.
     * <p> The list is colored using the CC.color method.
     *
     * @param path the path to get the list from
     * @return an empty list if the path does not exist
     */
    public List<String> getList(String path) {
        if (!contains(path)) {
            Bukkit.getLogger().severe("List " + path + " not found in " + getName() + ".yml");
            return new ArrayList<>();
        }

        return getYml().getStringList(path).stream().map(CC::color).collect(Collectors.toList());
    }

    /**
     * <p> Gets a section from the config file.
     * <p> The method checks for the existence of the path and logs an error if it does not exist.
     *
     * @param path the path to get the section from
     * @return null if the path does not exist
     */
    public ConfigurationSection getSection(String path) {
        if (!contains(path)) {
            Bukkit.getLogger().severe("Section " + path + " not found in " + getName() + ".yml");
            return null;
        }

        return getYml().getConfigurationSection(path);
    }

    /**
     * Get the keys of a section in the configuration file
     *
     * @param path the path to get the keys from
     * @return a set of keys
     */
    public Set<String> getKeys(String path) {
        ConfigurationSection section = getSection(path);

        return section == null ? new HashSet<>() : section.getKeys(false);
    }

    /**
     * @param path the path to get the section content from
     * @return A set of entries where the key is the key of the section and the value is the path to the key
     */
    public Set<Map.Entry<String, String>> getSectionContent(String path) {
        Map<String, String> map = new HashMap<>();

        getKeys(path).forEach(key -> map.put(key, path + "." + key));

        return map.entrySet();
    }

    /**
     * Get a list of integers from a comma-separated string in the configuration file
     *
     * @param path the path to get the integers from
     * @return a list of integers
     */
    public List<Integer> getIntegerSplit(String path) {
        return Arrays.stream(getString(path).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Get a location from the configuration file
     *
     * @param path the path to get the location from
     * @return the location
     */
    public Location getLocation(String path) {
        return LocationUtil.deserializeLocation(getString(path));
    }

    /**
     * Get a list of locations from the configuration file
     *
     * @param path the path to get the locations from
     * @return a list of locations
     */
    public List<Location> getLocations(String path) {
        return getYml().getStringList(path).stream()
                .map(LocationUtil::deserializeLocation)
                .collect(Collectors.toList());
    }

    /**
     * Get an item builder from the configuration file
     *
     * @param path the path to get the item builder from
     * @return the item builder
     */
    public ItemBuilder getItem(String path) {
        return ItemBuilder.parse(path, this);
    }

    /**
     * Execute an action from the configuration file
     *
     * @param path   the path to get the action from
     * @param player the player to execute the action on
     * @return true if the action was executed successfully, false otherwise
     */
    public boolean executeAction(String path, Player player) {
        return PlayerAction.execute(player, this, path);
    }


    public String getGroupPath(String path, String subPath, String group) {
        path = path.isEmpty() ? "" : path + ".";
        subPath = subPath.isEmpty() ? "" : "." + subPath;

        return path + (contains(path + group + subPath) ? group : "Default") + subPath;
    }

    // Sub Config section
    public ConfigSection getConfigSection(String path) {
        if (!contains(path)) {
            Bukkit.getLogger().severe("Cannot create ConfigSection " + path + " in " + getName() + ".yml");
            return null;
        }

        return new ConfigWrapper(getSection(path), path);
    }

    /**
     * Gets a list of ConfigSection objects from a YAML list in the configuration.
     * Works with YAML lists using the dash (-) notation.
     *
     * @param path the path to the list in the configuration
     * @return a list of ConfigSection objects, each representing an item in the YAML list
     */
    public List<ConfigSection> getConfigSectionList(String path) {
        List<ConfigSection> result = new ArrayList<>();
        List<Map<?, ?>> mapList = getYml().getMapList(path);

        if (mapList.isEmpty() && contains(path)) {
            Bukkit.getLogger().warning("Path " + path + " exists but is not a list of maps in " + getName() + ".yml");
            return result;
        }

        int index = 0;
        for (Map<?, ?> map : mapList) {
            // Create a temporary section to hold this map's data
            YamlConfiguration tempConfig = new YamlConfiguration();

            // Convert the map to a configuration section
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                tempConfig.set(key, value);
            }

            // Create a wrapper around the temporary section
            result.add(new ConfigWrapper(tempConfig, getName() + " - " + path + "[" + index + "]"));
            index++;
        }

        return result;
    }
}