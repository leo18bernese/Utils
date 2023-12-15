package me.leoo.utils.velocity.commands;

import lombok.experimental.UtilityClass;
import me.leoo.utils.velocity.Utils;

@UtilityClass
public class CommandManager {

    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    public void registerCommand(Command command) {
        Utils.getInstance().getProxy().getCommandManager().register(command.getName(), command, command.getAliases());
    }
}
