package me.leoo.utils.bukkit.config;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.commands.v2.VCommandBuilder;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.location.LocationUtil;
import me.leoo.utils.common.file.FileUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {

    private YamlConfiguration yml;
    private final File config;
    private final String name;

    private final List<String> excludeFirstTime = new ArrayList<>();
    private final boolean firstTime;

    private final ConfigGroup group = new ConfigGroup(this);

    private static final List<ConfigManager> configs = new ArrayList<>();

    public ConfigManager(String name, String dir) {
        this.name = name;
        this.firstTime = !FileUtil.exists(dir, name + ".yml");

        this.config = FileUtil.generateFile(dir, name + ".yml");
        if (config == null) return;

        this.yml = YamlConfiguration.loadConfiguration(config).options().copyDefaults(true).configuration();

        configs.add(this);
    }

    public ConfigManager(String name) {
        this(Utils.getInitializedFrom(), name);
    }

    public ConfigManager(Plugin plugin, String name) {
        this(name, plugin.getDataFolder().toString());
    }

    public void reload() {
        yml = YamlConfiguration.loadConfiguration(config);
    }

    public void addExcludeFirstTime(String path) {
        excludeFirstTime.add(path);
    }

    public boolean checkFirstTime(String path) {
        if (!excludeFirstTime.contains(path)) return true;

        return firstTime;
    }

    /**
     * @return return true if it's the FIRST TIME or the path DOES NOT exist
     * <br>
     * In practice, if TRUE you should save data, otherwise you should not <br>
     * It's useful when saving values that can be removed by the user, so to prevent the override of the user's preferences
     */
    public boolean toSave(String path) {
        return firstTime || !contains(path);
    }

    public void save() {
        yml.options().copyDefaults(true);

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
        if (!contains(path)) add(path, value);
    }

    public void add(String path, Object value) {
        yml.addDefault(path, value);
    }

    public void addList(String path, String... values) {
        add(path, values);
    }

    public void move(String from, String to) {
        if (!contains(from)) return;

        Object value = yml.get(from);

        yml.set(from, null);
        yml.set(to, value);
    }

    public void moveDefault(String from, String to, Object defaultValue) {
        if (!contains(from)) {
            yml.set(to, defaultValue);
        } else {
            move(from, to);
        }
    }

    public void replace(String path, String oldString, String newString) {
        if (!contains(path)) return;

        if (yml.isList(path)) {
            set(path, yml.getStringList(path).stream().map(s -> s.replace(oldString, newString)).collect(Collectors.toList()));
        } else {
            set(path, yml.getString(path).replace(oldString, newString));
        }
    }

    public boolean contains(String path) {
        return yml.get(path) != null;
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
            Bukkit.getLogger().severe("String " + path + " not found in " + name + ".yml");
            return "StringNotFound";
        }

        return CC.color(string);
    }

    public List<String> getList(String path) {
        if (!contains(path)) {
            Bukkit.getLogger().severe("List " + path + " not found in " + name + ".yml");
            return new ArrayList<>();
        }

        return yml.getStringList(path).stream().map(CC::color).collect(Collectors.toList());
    }

    public ConfigurationSection getSection(String path) {
        return yml.getConfigurationSection(path);
    }

    public Set<String> getKeys(String path) {
        ConfigurationSection section = yml.getConfigurationSection(path);

        return section == null ? new HashSet<>() : section.getKeys(false);
    }

    /**
     * @return A set of entries where the key is the key of the section and the value is the path to the key
     */
    public Set<Map.Entry<String, String>> getSectionContent(String path) {
        Map<String, String> map = new HashMap<>();

        getKeys(path).forEach(key -> map.put(key, path + "." + key));

        return map.entrySet();
    }

    public List<Integer> getIntegerSplit(String path) {
        return Arrays.stream(getString(path).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    //locations
    public Location getLocation(String path) {
        return LocationUtil.deserializeLocation(getString(path));
    }

    public List<Location> getLocations(String path) {
        return yml.getStringList(path).stream()
                .map(LocationUtil::deserializeLocation)
                .collect(Collectors.toList());
    }

    public void saveLocation(String path, Location location) {
        yml.set(path, LocationUtil.serializeLocation(location));
        save();
    }

    public void saveLocations(String path, List<Location> locations) {
        List<String> list = yml.getStringList(path);
        list.addAll(locations.stream().map(LocationUtil::serializeLocation).collect(Collectors.toList()));

        add(path, list);
        save();
    }

    //titles
    public void saveTitle(String path, String string, String title, String subTitle) {
        add(path + ".message", string);
        add(path + ".title", title);
        add(path + ".sub-title", subTitle);

        save();
    }

    public void saveTitle(String path, List<String> strings, String title, String subTitle) {
        add(path + ".message", strings);
        add(path + ".title", title);
        add(path + ".sub-title", subTitle);

        save();
    }

    //clickable messages
    public void saveClickableMessage(String path, String text, String hover, String command, ClickEvent.Action action) {
        add(path, text + ";" + hover + ";" + command + ";" + action.name());

        save();
    }

    //items
    public ItemBuilder getItem(String path) {
        return ItemBuilder.parse(path, this);
    }

    public void saveItem(String path, int slot, XMaterial material, String name, String... lore) {
        saveItem(path, Utils.getLanguage(this), slot, material, name, lore);
    }

    public void saveItem(String path, XMaterial material, String name, String... lore) {
        saveItem(path, -1, material, name, lore);
    }

    public void saveItem(String path, ConfigManager language, int slot, XMaterial material, String name, String... lore) {
        new ItemBuilder(material).data(material.getData()).name(name).lore(lore).slot(slot).save(path, this, language);

        save();
    }

    //action method from config
    public boolean executeAction(String path, Player player) {
        return PlayerAction.execute(player, this, path);
    }

    //group methods
    public String getGroupPath(String path, String subPath, String group) {
        path = path.isEmpty() ? "" : path + ".";
        subPath = subPath.isEmpty() ? "" : "." + subPath;

        return path + (contains(path + group + subPath) ? group : "Default") + subPath;
    }

    public String getGroupString(String path, String subPath, String group) {

        return getString(getGroupPath(path, subPath, group));
    }

    public List<String> getGroupList(String path, String subPath, String group) {
        return getList(getGroupPath(path, subPath, group));
    }

    public Set<String> getGroupKeys(String path, String subPath, String group) {
        return getKeys(getGroupPath(path, subPath, group));
    }

    public ConfigurationSection getGroupSection(String path, String subPath, String group) {
        return getSection(getGroupPath(path, subPath, group));
    }

    public boolean getGroupBoolean(String path, String subPath, String group) {
        return getBoolean(getGroupPath(path, subPath, group));
    }

    public int getGroupInt(String path, String subPath, String group) {
        return getInt(getGroupPath(path, subPath, group));
    }

    public double getGroupDouble(String path, String subPath, String group) {
        return getDouble(getGroupPath(path, subPath, group));
    }

    public void removeFromMemory() {
        configs.remove(this);
    }

    public static void reloadAll() {
        configs.forEach(ConfigManager::reload);

        VCommandCache.getCommands().values().forEach(VCommandBuilder::reload);
    }
}