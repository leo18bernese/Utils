package me.leoo.utils.bungee.config;

import lombok.Getter;
import me.leoo.utils.bungee.chat.CC;
import me.leoo.utils.common.file.FileUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {

    private Configuration yml;
    private final File config;
    private final String name;

    public ConfigManager(Plugin plugin, String name, String dir) {
        FileUtil.generateFolder(dir);

        config = new File(dir, name + ".yml");

        try {
            if (!config.exists()) {
                InputStream inputStream = plugin.getResourceAsStream(config.getName());
                Files.copy(inputStream, config.toPath());
            }

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
            ProxyServer.getInstance().getLogger().info("String " + path + " not found in " + name + ".yml");
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