package me.leoo.utils.velocity.commands;

import me.leoo.utils.velocity.Utils;

public class CommandManager {

    public static void registerCommands(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    public static void registerCommand(Command command) {
        Utils.getInstance().getServer().getCommandManager().register(command.getName(), command, command.getAliases());
    }
}
