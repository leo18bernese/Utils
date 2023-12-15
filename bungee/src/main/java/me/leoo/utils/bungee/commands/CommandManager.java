package me.leoo.utils.bungee.commands;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bungee.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

@UtilityClass
public class CommandManager {

    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    public void registerCommand(Command command) {
        ProxyServer.getInstance().getPluginManager().registerCommand(Utils.get(), command);
    }
}
