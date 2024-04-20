package me.leoo.utils.bukkit.commands;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.commands.v2.VCommand;
import me.leoo.utils.bukkit.commands.v2.VCommandBuilder;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.common.reflection.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.util.Arrays;

@UtilityClass
public class CommandManager {

    public void register(me.leoo.utils.bukkit.commands.Command... commands) {
        for (me.leoo.utils.bukkit.commands.Command command : commands) {
            registerCommand(command.initialize());
        }
    }

    public void register(VCommand... commands) {
        for (VCommand command : commands) {
            VCommandBuilder builder = new VCommandBuilder(command.getClass());
            VCommandCache.getCommands().put(command.getClass().getName(), builder);

            command.setName(builder.getName());
            command.setAliases(Arrays.asList(builder.getAliases()));

            VCommandCache.getInstances().put(builder.getName(), command);

            registerCommand(command);
        }
    }

    public void register(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    private void registerCommand(Command command) {
        CommandMap commandMap = (CommandMap) ReflectionUtil.getFieldValue("commandMap", Bukkit.getServer());
        if (commandMap == null) return;

        commandMap.register(command.getName(), command);
    }
}
