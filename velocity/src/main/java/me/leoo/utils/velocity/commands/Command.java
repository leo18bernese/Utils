package me.leoo.utils.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.velocity.chat.CC;

@Getter
@Setter
public abstract class Command implements SimpleCommand {

    private final String name;
    private String[] aliases;
    private String permission;
    private boolean forPlayersOnly;

    public Command(String name) {
        this.name = name;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player) && this.forPlayersOnly) {
            CC.sendMessage(sender, "This command is for players!");
            return;
        }

        if (!checkPermission(sender, this.permission)) {
            CC.sendMessage(sender, getNoPermissionMessage());
            return;
        }

        this.executeCommand(sender, invocation.arguments());
    }

    public abstract void executeCommand(CommandSource sender, String[] args);

    public static boolean checkPermission(CommandSource sender, String permission) {
        if (sender.hasPermission("*")) {
            return true;
        }

        if (permission != null) {
            return sender.hasPermission(permission);
        }

        return true;
    }

    public abstract String getNoPermissionMessage();
}