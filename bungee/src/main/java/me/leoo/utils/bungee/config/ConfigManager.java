package me.leoo.utils.bungee.config;

import lombok.Getter;
import me.leoo.utils.bungee.Utils;
import me.leoo.utils.bungee.chat.CC;
import me.leoo.utils.common.file.FileUtil;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {

    private Configuration yml;
    private File config;
    private String name;

    public ConfigManager(String name, String dir) {
        config = FileUtil.generateFile(name + ".yml", dir);

        if (config == null) return;

        try {
            yml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        this.name = name;
    }

    public void reload() {
        try {
            yml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, config);
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

}