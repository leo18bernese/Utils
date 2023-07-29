package me.leoo.utils.commands;

import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.task.Tasks;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Command extends BukkitCommand {

    private final boolean forPlayersOnly;
    private final String permission;
    @Getter
    private String alias = null;
    @Setter
    private boolean async;

    public Command(String name) {
        this(name, new ArrayList<>());
    }

    public Command(String name, List<String> aliases) {
        this(name, aliases, null, false);
    }

    public Command(String name, String rank) {
        this(name, new ArrayList<>(), rank);
    }

    public Command(String name, List<String> aliases, String permission) {
        this(name, aliases, permission, false);
    }

    public Command(String name, boolean forPlayersOnly) {
        this(name, new ArrayList<>(), null, forPlayersOnly);
    }

    public Command(String name, List<String> aliases, boolean forPlayersOnly) {
        this(name, aliases, null, forPlayersOnly);
    }

    public Command(String name, String permission, boolean forPlayersOnly) {
        this(name, new ArrayList<>(), permission, forPlayersOnly);
    }

    public Command(String name, List<String> aliases, String permission, boolean forPlayersOnly) {
        super(name);

        this.setAliases(aliases);

        if (permission != null && permission.equalsIgnoreCase("SYSTEM")) {
            permission = "core.command." + name;
        }

        this.permission = permission;
        this.forPlayersOnly = forPlayersOnly;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player) && this.forPlayersOnly) {
            sender.sendMessage("This command is for players!");
            return false;
        }

        if (!testPermission(sender, this.permission)) {
            sender.sendMessage(getNoPermission());
            return false;
        }

        this.alias = alias;

        if (this.async) {
            Tasks.run(() -> this.execute(sender, args));
        } else {
            this.execute(sender, args);
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public static boolean testPermission(CommandSender sender, String requiredRank) {
        if (sender.hasPermission("*")) {
            return true;
        }

        if (requiredRank != null) {
            return sender.hasPermission(requiredRank);
        }

        return true;
    }

    public abstract String getNoPermission();
}