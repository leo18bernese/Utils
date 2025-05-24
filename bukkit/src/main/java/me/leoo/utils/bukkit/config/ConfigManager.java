package me.leoo.utils.bukkit.config;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.commands.v2.VCommandBuilder;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.location.LocationUtil;
import me.leoo.utils.common.file.FileUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager extends ConfigSection {

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

    // locations
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
    public void saveItem(String path, int slot, XMaterial material, String name, String... lore) {
        saveItem(path, Utils.getLanguage(this), slot, material, name, lore);
    }

    public void saveItem(String path, XMaterial material, String name, String... lore) {
        saveItem(path, -1, material, name, lore);
    }

    public void saveItem(String path, ConfigSection language, int slot, XMaterial material, String name, String... lore) {
        new ItemBuilder(material).data(material.getData()).name(name).lore(lore).slot(slot).save(path, this, language);

        save();
    }

    public void removeFromMemory() {
        configs.remove(this);
    }

    public static void reloadAll() {
        configs.forEach(ConfigManager::reload);

        VCommandCache.getCommands().values().forEach(VCommandBuilder::reload);
    }
}