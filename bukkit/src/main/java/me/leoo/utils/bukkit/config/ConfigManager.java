package me.leoo.utils.bukkit.config;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.location.LocationUtil;
import me.leoo.utils.common.file.FileUtil;
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
    private String name;

    private final boolean firstTime;

    public ConfigManager(String name, String dir) {
        firstTime = !new File(dir, name).exists();
        config = FileUtil.generateFile(name + ".yml", dir);

        if (config == null) return;

        yml = YamlConfiguration.loadConfiguration(config).options().copyDefaults(true).configuration();

        this.name = name;
    }

    public ConfigManager(Plugin plugin, String name) {
        this(name, plugin.getDataFolder().toString());
    }

    public ConfigManager(String name) {
        this(Utils.getInitializedFrom(), name);
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

    public void add(String path, Object value) {
        yml.addDefault(path, value);
    }

    public void addList(String path, String... values) {
        add(path, Arrays.asList(values));
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
            Utils.get().getLogger().severe("String " + path + " not found in " + name + ".yml");
            return "StringNotFound";
        }

        return CC.color(string);
    }

    public List<String> getList(String path) {
        if (yml.get(path) == null) {
            Utils.get().getLogger().severe("List " + path + " not found in " + name + ".yml");
            return Collections.emptyList();
        }

        return yml.getStringList(path).stream().map(CC::color).collect(Collectors.toList());
    }

    public Set<String> getSection(String path) {
        ConfigurationSection section = yml.getConfigurationSection(path);

        return section == null ? new HashSet<>() : section.getKeys(false);
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

    //items
    public void addItem(String path, XMaterial material, String name, String... lore) {
        new ItemBuilder(material).setName(name).setLore(lore).saveIntoConfig(path, this);

        save();
    }

    //action method from config
    public boolean executeAction(String path, Player player) {
        return PlayerAction.fromConfig(this, path).run(player);
    }

    //group methods
    public String getGroupPath(String prefix, String path, String group) {
        String prefixPath = prefix.isEmpty() ? "" : prefix + ".";
        String pathPath = path.isEmpty() ? "" : "." + path;

        return prefixPath + (yml.get(prefixPath + group + pathPath) == null ? "Default" : group) + pathPath;
    }

    public String getGroupString(String prefix, String path, String group) {
        return getString(getGroupPath(prefix, path, group));
    }

    public List<String> getGroupList(String prefix, String path, String group) {
        return getList(getGroupPath(prefix, path, group));
    }

    public Set<String> getGroupSection(String prefix, String path, String group) {
        return getSection(getGroupPath(prefix, path, group));
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