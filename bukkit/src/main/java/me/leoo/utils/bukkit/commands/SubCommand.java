package me.leoo.utils.bukkit.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.List;

@Data
@RequiredArgsConstructor
public abstract class SubCommand {

    private final String name;

    private String permission;
    private boolean forPlayersOnly;

    public abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> getTabComplete(String[] args);
}
