package me.leoo.utils.bukkit.commands.v2;

import lombok.Data;
import me.leoo.utils.bukkit.commands.v2.exception.CommandError;
import me.leoo.utils.bukkit.commands.v2.exception.VCommandError;
import me.leoo.utils.bukkit.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Data
public class VCommandManager {

    private final VCommandError error;

    private String usagePath;
    private String displayPath;

    private Map<String, String> usagePaths = new HashMap<>();
    private Map<String, String> displayPaths = new HashMap<>();

    private Supplier<ConfigManager> language;

    private String path;
    private Supplier<ConfigManager> config;

    private boolean buildUsageMessage = true;

    private static VCommandManager instance;

    public VCommandManager(VCommandError commandError) {
        instance = this;

        error = commandError;
    }

    public VCommandManager() {
        this(new CommandError());
    }

    public void setCommandUsage(Supplier<ConfigManager> configManager, String usagePath, String displayPath) {
        this.language = configManager;
        this.usagePath = usagePath;
        this.displayPath = displayPath;
    }

    public void setCommandUsage(Supplier<ConfigManager> configManager, String id, String usagePath, String displayPath) {
        this.language = configManager;
        this.usagePaths.put(id, usagePath);
        this.displayPaths.put(id, displayPath);
    }

    public String getUsagePath(String id, String parent) {
        String name = parent == null ? id : parent;

        return getPath(id, usagePaths.getOrDefault(name, usagePath), null, language);
    }

    public String getDisplayPath(String id, String parent) {
        String name = parent == null ? id : parent;

        return getPath(id, displayPaths.getOrDefault(name, displayPath), null, language);
    }

    public void setGeneralConfig(Supplier<ConfigManager> configManager, String path) {
        this.config = configManager;
        this.path = path;
    }

    public static String getPath(String name, String path, String subPath, Supplier<ConfigManager> configManager) {
        if (path == null || configManager == null) return null;
        ConfigManager config = configManager.get();
        if (config == null) return null;
        String finalPath = path.replace("%name%", name);
        if (subPath != null) {
            finalPath += "." + subPath;
        }

        if (!config.contains(finalPath)) {
            return null;
        }

        return finalPath;
    }

    public static VCommandManager get() {
        return instance;
    }

    /*public static void register(VCommandManager manager, VCommandError vCommandError) {
        if (instance == null) {
            instance = manager;
        }

        error = vCommandError == null ? new CommandError() : vCommandError;
    }

    public static void register(VCommandManager manager) {
        register(manager, null);
    }*/
}
