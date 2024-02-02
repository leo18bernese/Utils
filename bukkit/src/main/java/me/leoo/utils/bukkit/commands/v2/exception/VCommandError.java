package me.leoo.utils.bukkit.commands.v2.exception;

public abstract class VCommandError {

    public abstract String getNoPermissionMessage();

    public abstract String getOnlyPlayersMessage();

    public abstract String getOnlyConsoleMessage();

    public abstract String getOnlyOperatorsMessage();


    public abstract String getInvalidArgumentMessage();

    public abstract String getMissingArgumentsMessage();
}
