package me.leoo.utils.bukkit.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Data
@RequiredArgsConstructor
@Accessors (chain = true)
public class CommandBuilder {

    private final String name;

    private String display;
    private String usage;
    private String[] aliases;

    private String permission;
    private String noPermissionMessage;

    private boolean playersOnly;
    private boolean operatorsOnly;
    private boolean async;

    private BiConsumer<CommandSender, String[]> executor;
    private BiConsumer<Player, String[]> playerExecutor;
    private BiFunction<CommandSender, String[], List<String>> tabComplete;

    public CommandBuilder setInfoFromConfig(String path, ConfigManager config) {
        setDisplay(config.getString(path + ".display"));
        setUsage(config.getString(path + ".usage"));

        return this;
    }

    public CommandBuilder setInfoFromConfig(ConfigManager config) {
        setDisplay(config.getString("commands." + name + ".display"));
        setUsage(config.getString("commands." + name + ".usage"));

        return this;
    }

    public boolean canSee(CommandSender sender) {
        if (permission == null) return true;

        return sender.hasPermission(permission);
    }
}
