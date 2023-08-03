package me.leoo.utils.bungee.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;

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
