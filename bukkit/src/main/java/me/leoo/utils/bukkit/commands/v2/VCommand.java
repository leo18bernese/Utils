package me.leoo.utils.bukkit.commands.v2;

import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.bukkit.commands.v2.parser.ArgumentParser;
import me.leoo.utils.bukkit.commands.v2.parser.CommandArgs;
import me.leoo.utils.bukkit.commands.v2.tabcomplete.VTabComplete;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public abstract class VCommand extends BukkitCommand {

    public VCommand() {
        super("");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        VCommandBuilder mainCommand = getMainCommand();

        if (mainCommand == null) return false;

        if (!mainCommand.testExecutor(sender)) {
            return false;
        }

        if (!mainCommand.testPermission(sender)) {
            sender.sendMessage(CC.color(VCommandManager.get().getError().getNoPermissionMessage()));
            return false;
        }

        if (args.length > 0) {

            VCommandBuilder subCommand = getSubCommand(args[0]);

            if (subCommand != null) {
                if (!subCommand.testExecutor(sender)) {
                    return false;
                }

                if (!subCommand.testPermission(sender)) {
                    sender.sendMessage(CC.color(VCommandManager.get().getError().getNoPermissionMessage()));
                    return false;
                }

                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

                Object[] subParameters = ArgumentParser.parseArguments(subCommand, new CommandArgs(sender, subArgs));
                if (subParameters == null) return false;

                if (sender instanceof Player) {
                    subCommand.execute(mainCommand.getName(), (Player) sender, subParameters);
                } else {
                    subCommand.execute(mainCommand.getName(), sender, subParameters);
                }

                return true;
            }
        }

        Object[] parameters = ArgumentParser.parseArguments(mainCommand, new CommandArgs(sender, args));
        if (parameters == null) return false;

        mainCommand.execute(sender, parameters);

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        VCommandBuilder mainCommand = getMainCommand();

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            for (VCommandBuilder subCommand : mainCommand.getSubCommands()) {
                if (!subCommand.testPermission(sender)) continue;

                completions.add(subCommand.getName());
                completions.addAll(Arrays.asList(subCommand.getAliases()));
            }

            if (getTabComplete(args[0]) != null) {
                completions.addAll(getTabComplete(args[0]).execute(sender, alias, args));
            }

            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }

        VTabComplete tabComplete = getTabComplete(args[0]);

        if (tabComplete != null) {
            return StringUtil.copyPartialMatches(args[1], tabComplete.execute(sender, alias, args), new ArrayList<>());
        }

        return Collections.emptyList();
    }

    public VCommandBuilder getMainCommand() {
        return VCommandCache.getCommand(getClass().getName());
    }

    public List<VCommandBuilder> getSubCommands() {
        return getMainCommand().getSubCommands();
    }

    private VCommandBuilder getSubCommand(String name) {
        return getSubCommands().stream().filter(subCommand -> subCommand.getName().equals(name)).findFirst().orElse(getSubCommandFromAlias(name));
    }

    private VCommandBuilder getSubCommandFromAlias(String alias) {
        return getSubCommands().stream().filter(subCommand -> Arrays.asList(subCommand.getAliases()).contains(alias)).findFirst().orElse(null);
    }

    private VTabComplete getTabComplete(String name) {
        return VCommandCache.getTabComplete(name) == null ? getTabCompleteFromAlias(name) : VCommandCache.getTabComplete(name);
    }

    private VTabComplete getTabCompleteFromAlias(String alias) {
        return VCommandCache.getTabComplete().values().stream().filter(tabComplete -> Arrays.asList(tabComplete.getAliases()).contains(alias)).findFirst().orElse(null);
    }
}
