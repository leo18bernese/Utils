package me.leoo.utils.bukkit.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public abstract class Command extends BukkitCommand {

    private CommandBuilder mainCommand;

    private final List<CommandBuilder> subCommands = new ArrayList<>();

    public Command(String name) {
        super(name);
    }

    public Command initialize() {
        setAliases(Arrays.asList(mainCommand.getAliases()));

        return this;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (mainCommand == null) return false;

        if (!(sender instanceof Player) && mainCommand.isPlayersOnly()) {
            sender.sendMessage("This command is for players!");
            return false;
        }

        if (!checkPermission(sender, mainCommand.getPermission())) {
            sender.sendMessage(mainCommand.getNoPermissionMessage() == null ? getNoPermissionMessage() : mainCommand.getNoPermissionMessage());
            return false;
        }

        if (args.length > 0) {
            CommandBuilder subCommand = getSubCommandByName(args[0]);

            if (subCommand != null) {
                if (!(sender instanceof Player) && subCommand.isPlayersOnly()) {
                    sender.sendMessage("This sub command is for players!");
                    return false;
                }

                if (!checkPermission(sender, subCommand.getPermission())) {
                    sender.sendMessage(subCommand.getNoPermissionMessage() == null ? getNoPermissionMessage() : subCommand.getNoPermissionMessage());
                    return false;
                }

                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

                if (sender instanceof Player && subCommand.isPlayersOnly()) {
                    subCommand.getPlayerExecutor().accept((Player) sender, subArgs);
                } else {
                    subCommand.getExecutor().accept(sender, subArgs);
                }

                return true;
            }
        }

        if (sender instanceof Player && mainCommand.isPlayersOnly()) {
            mainCommand.getPlayerExecutor().accept((Player) sender, args);
        } else {
            mainCommand.getExecutor().accept(sender, args);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            for (CommandBuilder subCommand : subCommands) {
                if (!subCommand.canSee(sender)) continue;

                tab.add(subCommand.getName());
            }

            return tab;
        }

        CommandBuilder subCommand = getSubCommandByName(args[0]);
        if (subCommand != null && subCommand.canSee(sender)) {
            return subCommand.getTabComplete() == null ? tab : subCommand.getTabComplete().apply(sender, args);

        }

        return tab;
    }

    public static boolean checkPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission("*") && permission != null) {
            return sender.hasPermission(permission);
        }

        return true;
    }

    public abstract String getNoPermissionMessage();

    public CommandBuilder getSubCommandByName(String name) {
        return subCommands.stream().filter(subCommand -> subCommand.getName().equals(name)).findAny().orElse(null);
    }
}
