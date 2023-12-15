package me.leoo.utils.bukkit.commands;

import lombok.experimental.UtilityClass;
import me.leoo.utils.common.reflection.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

@UtilityClass
public class CommandManager {

    public void registerCommands(me.leoo.utils.bukkit.commands.Command... commands) {
        for (me.leoo.utils.bukkit.commands.Command command : commands) {
            registerCommand(command.initialize());
        }
    }

    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    public void registerCommand(Command command) {
        CommandMap commandMap = (CommandMap) ReflectionUtil.getFieldValue("commandMap", Bukkit.getServer());
        if (commandMap == null) return;

        commandMap.register(command.getName(), command);
    }
}
