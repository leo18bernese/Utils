package me.leoo.utils.common.file;

import lombok.Getter;
import me.leoo.utils.common.compatibility.SoftwareManager;
import me.leoo.utils.common.compatibility.SoftwareUtils;
import me.leoo.utils.common.config.Configuration;
import me.leoo.utils.common.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {

    private YamlConfiguration configuration;
    private Configuration yml;
    private final File config;
    private final String name;

    private static final SoftwareUtils utils = SoftwareManager.getUtils();

    public ConfigManager(String name, String directory) {
        this.name = name;

        config = FileUtil.generateFile(name + ".yml", directory);
        if (config == null) return;

        try {
            configuration = new YamlConfiguration(config);

            yml = configuration.load();
            configuration.save(yml);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            configuration.save(yml);
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
            SoftwareManager.getUtils().info("String " + path + " not found in " + name + ".yml");
            return "StringNotFound";
        }

        return SoftwareManager.getUtils().color(string);
    }

    public List<String> getList(String path) {
        return yml.getStringList(path).stream().map(SoftwareManager.getUtils()::color).collect(Collectors.toList());
    }

    public List<Integer> getIntegerSplitList(String path) {
        return Arrays.stream(getString(path).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }


}
