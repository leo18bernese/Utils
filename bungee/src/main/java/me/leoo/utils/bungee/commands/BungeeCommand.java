package me.leoo.utils.bungee.commands;

import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bungee.chat.CC;
import me.leoo.utils.bungee.task.Tasks;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class BungeeCommand extends Command {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private String permission;
    private boolean forPlayersOnly;
    private boolean async;

    public BungeeCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer) && this.forPlayersOnly) {
            CC.sendMessage(sender, "This command is for players!");
            return;
        }

        if (!checkPermission(sender, this.permission)) {
            CC.sendMessage(sender, getNoPermissionMessage());
            return;
        }

        /*if (args.length > 0) {
            SubCommand subCommand = getSubCommandByName(args[0]);

            if(subCommand != null){
                if (subCommand.isForPlayersOnly() && !(sender instanceof Player)) {
                    CC.sendMessage(sender, "This command is for players!");
                    return false;
                }

                if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                    CC.sendMessage(sender, getNoPermissionMessage());
                    return false;
                }

                subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }*/

        if (this.async) {
            Tasks.runAsync(() -> this.executeCommand(sender, args));
        } else {
            this.executeCommand(sender, args);
        }
    }

    public abstract void executeCommand(CommandSender sender, String[] args);

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