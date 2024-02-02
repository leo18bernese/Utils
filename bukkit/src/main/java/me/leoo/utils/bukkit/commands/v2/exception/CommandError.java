package me.leoo.utils.bukkit.commands.v2.exception;

import me.leoo.utils.bukkit.chat.CC;

public class CommandError extends VCommandError {

    @Override
    public String getNoPermissionMessage() {
        return CC.RED + "You don't have permission to execute this command!";
    }

    @Override
    public String getOnlyPlayersMessage() {
        return CC.RED + "Only players can execute this command!";
    }

    @Override
    public String getOnlyConsoleMessage() {
        return CC.RED + "Only console can execute this command!";
    }

    @Override
    public String getOnlyOperatorsMessage() {
        return CC.RED + "Only operators can execute this command!";
    }


    @Override
    public String getInvalidArgumentMessage() {
        return CC.RED + "Unknown argument provided (class -> {class})!";
    }

    @Override
    public String getMissingArgumentsMessage() {
        return CC.RED + "Wrong or missing arguments!";
    }
}
