package me.leoo.utils.bungee.commands;

import me.leoo.utils.bungee.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class CommandManager {

    public static void registerCommands(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    public static void registerCommand(Command command) {
        ProxyServer.getInstance().getPluginManager().registerCommand(Utils.get(), command);
    }
}
