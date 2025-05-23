package me.leoo.utils.bukkit.commands.v2;

import lombok.Getter;
import lombok.Setter;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.chat.ChatMessage;
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

                if (subCommand.isConfirmation()) {
                    if (sender instanceof Player) {
                        ChatMessage.request(((Player) sender).getUniqueId(), event -> {
                            String runned = "/" + s + " " + String.join(" ", args);

                            if (event.getMessage().equals(runned)) {
                                ((Player) sender).performCommand(runned);
                            }
                        });
                    }
                }

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

            VTabComplete tabComplete = getTabComplete(mainCommand.getName());
            if (tabComplete != null) {
                completions.addAll(tabComplete.execute(sender, alias, args));
            }

            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }

        VTabComplete tabComplete = getTabComplete(args[0]);

        if (tabComplete != null) {
            return StringUtil.copyPartialMatches(args[args.length - 1], tabComplete.execute(sender, alias, args), new ArrayList<>());
        }

        return Collections.emptyList();
    }

    /**
     * <p> Requests confirmation from the player to execute a command.
     *
     * <p> Calls {@link #requestConfirmation(Player, List, String, Runnable)} with a single confirmation message.
     */
    public void requestConfirmation(Player player, String confirmMessage, String commandFormat, Runnable action) {
        requestConfirmation(player, Collections.singletonList(confirmMessage), commandFormat, action);
    }

    /**
     * Requests confirmation from the player to execute a command.
     *
     * @param player         The player to request confirmation from.
     * @param confirmMessage The message to send to the player for confirmation.
     * @param commandFormat  The command format to be executed upon confirmation.
     * @param action         The action to be executed upon confirmation.
     */
    public void requestConfirmation(Player player, List<String> confirmMessage, String commandFormat, Runnable action) {
        confirmMessage.forEach(s -> {
            s = s.replace("{confirmMessage}", commandFormat);

            player.sendMessage(CC.color(s));
        });

        ChatMessage.request(player.getUniqueId(), event -> {
            if (event.getMessage().equalsIgnoreCase(commandFormat)) {
                action.run();
            }
        });
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
        return VCommandCache.getTabComplete().values().stream()
                .filter(tabComplete -> tabComplete.getAliases().contains(alias))
                .findFirst().orElse(null);
    }
}
