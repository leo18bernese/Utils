package me.leoo.utils.bukkit.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Data
@RequiredArgsConstructor
public class CommandBuilder {

    private final String name;

    private String display;
    private String usage;
    private String[] aliases;

    private String permission;
    private String noPermissionMessage;

    private boolean playersOnly;
    private boolean async;

    private BiConsumer<CommandSender, String[]> executor;
    private BiConsumer<Player, String[]> playerExecutor;
    private BiFunction<CommandSender, String[], List<String>> tabComplete;

    public CommandBuilder setDisplay(String display) {
        this.display = display;
        return this;
    }

    public CommandBuilder setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public CommandBuilder setInfoFromConfig(String path, ConfigManager config) {
        setDisplay(config.getString(path + ".display"));
        setUsage(config.getString(path + ".usage"));

        return this;
    }

    public CommandBuilder setAliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder setNoPermissionMessage(String message) {
        this.noPermissionMessage = message;
        return this;
    }

    public CommandBuilder setPlayersOnly(boolean playersOnly) {
        this.playersOnly = playersOnly;
        return this;
    }

    public CommandBuilder setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public CommandBuilder setExecutor(BiConsumer<CommandSender, String[]> executor) {
        this.executor = executor;
        return this;
    }

    public CommandBuilder setPlayerExecutor(BiConsumer<Player, String[]> playerExecutor) {
        this.playerExecutor = playerExecutor;
        return this;
    }

    public CommandBuilder setTabComplete(BiFunction<CommandSender, String[], List<String>> tabComplete) {
        this.tabComplete = tabComplete;
        return this;
    }

    public boolean canSee(CommandSender sender) {
        if (permission == null) return true;

        return sender.hasPermission(permission);
    }

    public interface Callback<T> {
        boolean accept(T t);
    }
}
