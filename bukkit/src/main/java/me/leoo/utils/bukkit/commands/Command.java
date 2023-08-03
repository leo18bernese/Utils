package me.leoo.utils.bukkit.commands;

import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bukkit.task.Tasks;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Command extends BukkitCommand {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private String permission;
    private boolean forPlayersOnly;
    private boolean async;

    public Command(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player) && this.forPlayersOnly) {
            sender.sendMessage("This command is for players!");
            return false;
        }

        if (!checkPermission(sender, this.permission)) {
            sender.sendMessage(getNoPermissionMessage());
            return false;
        }

        /*if (args.length > 0) {
            SubCommand subCommand = getSubCommandByName(args[0]);

            if(subCommand != null){
                if (subCommand.isForPlayersOnly() && !(sender instanceof Player)) {
                    sender.sendMessage("This command is for players!");
                    return false;
                }

                if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                    sender.sendMessage(getNoPermissionMessage());
                    return false;
                }

                subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }*/

        if (this.async) {
            Tasks.run(() -> this.execute(sender, args));
        } else {
            this.execute(sender, args);
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public static boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission("*")) {
            return true;
        }

        if (permission != null) {
            return sender.hasPermission(permission);
        }

        return true;
    }

    public abstract String getNoPermissionMessage();

    public SubCommand getSubCommandByName(String name) {
        return null;
        //return subCommands.stream().filter(subCommand -> subCommand.getName().equals(name)).findAny().orElse(null);
    }
}