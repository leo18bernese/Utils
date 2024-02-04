package me.leoo.utils.bukkit.commands.v2;

import lombok.Data;
import lombok.Getter;
import me.leoo.utils.bukkit.commands.v2.exception.CommandError;
import me.leoo.utils.bukkit.commands.v2.exception.VCommandError;
import me.leoo.utils.bukkit.config.ConfigManager;

@Data
public class VCommandManager {

    private final VCommandError error;

    private String commandUsagePath;
    private ConfigManager configManager;

    private boolean buildUsageMessage = true;

    private static VCommandManager instance;

    public VCommandManager(VCommandError commandError) {
        instance = this;

        error = commandError;
    }

    public VCommandManager() {
        this(new CommandError());
    }

    public void setCommandUsage(ConfigManager configManager, String path) {
        this.configManager = configManager;
        this.commandUsagePath = path;
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
