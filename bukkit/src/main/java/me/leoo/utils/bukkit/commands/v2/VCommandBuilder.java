package me.leoo.utils.bukkit.commands.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.commands.v2.annotation.*;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.bukkit.commands.v2.parser.ArgumentParser;
import me.leoo.utils.bukkit.commands.v2.tabcomplete.VTabComplete;
import me.leoo.utils.bukkit.config.ConfigManager;
import me.leoo.utils.common.reflection.ReflectionUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class VCommandBuilder {

    private String name;
    private String[] aliases;

    private CommandExecutor executorType;
    private String permission;

    private String usage;
    private String display;

    private Method method;

    private final List<VCommandBuilder> subCommands = new ArrayList<>();

    private static final VCommandManager manager = VCommandManager.get();

    public VCommandBuilder(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            parseCommand(method);
        }
    }

    private void parseCommand(Method method) {
        if (method.getAnnotation(Command.class) != null) {
            Command command = method.getAnnotation(Command.class);

            if (command.value().length == 0) throw new IllegalArgumentException("Command name cannot be empty");

            this.name = command.value()[0];
            this.aliases = Arrays.stream(command.value()).skip(1).toArray(String[]::new);

            this.executorType = command.executor();
            this.permission = parsePermission(method);

            this.usage = parseUsage(this.name, method);
            this.display = parseDisplay(this.name, method);

            this.method = method;

        } else if (method.getAnnotation(SubCommand.class) != null) {
            SubCommand subCommand = method.getAnnotation(SubCommand.class);

            if (subCommand.parent() != null && !subCommand.parent().isEmpty() && !subCommand.parent().equals(name))
                return;
            if (subCommand.value().length == 0) throw new IllegalArgumentException("Sub Command name cannot be empty");

            String subName = subCommand.value()[0];

            String[] subAliases = Arrays.stream(subCommand.value()).skip(1).toArray(String[]::new);

            CommandExecutor subExecutor = subCommand.executor();
            String subPermission = parsePermission(method);

            String subUsage = parseUsage(subName, method);
            String subDisplay = parseDisplay(subName, method);

            subCommands.add(new VCommandBuilder(subName, subAliases, subExecutor, subPermission, subUsage, subDisplay, method));
        } else if (method.getAnnotation(TabComplete.class) != null) {
            TabComplete tabComplete = method.getAnnotation(TabComplete.class);

            String main = tabComplete.value();
            String[] alias = tabComplete.aliases();

            VCommandCache.getTabComplete().put(main, new VTabComplete(this.name, main, alias, method));
        }
    }

    private String parsePermission(Method method) {
        if (method.getAnnotation(CommandPermission.class) != null) {
            CommandPermission commandPermission = method.getAnnotation(CommandPermission.class);

            return commandPermission.value();
        }

        return "";
    }

    //usage
    private String parseUsage(String name, Method method) {
        if (method.getAnnotation(CommandUsage.class) != null) {
            CommandUsage commandUsage = method.getAnnotation(CommandUsage.class);

            return commandUsage.usage();
        }

        return parseUsageFromSettings(name, method);
    }

    private String parseUsageFromSettings(String name, Method method) {
        ConfigManager config = manager.getConfigManager();
        String path = manager.getUsagePath();

        if (config != null && path != null) {
            String finalPath = path.replace("%name%", name);

            if (config.getYml().getString(finalPath) != null) {
                return config.getString(finalPath);
            }
        }

        if (manager.isBuildUsageMessage()) {
            return "&cUsage: /" + name + " " + ArgumentParser.parseArgumentsString(method);
        }

        return null;
    }

    //display
    private String parseDisplay(String name, Method method) {
        if (method.getAnnotation(CommandUsage.class) != null) {
            CommandUsage commandDisplay = method.getAnnotation(CommandUsage.class);

            return commandDisplay.display();
        }

        return parseDisplayFromSettings(name, method);
    }

    private String parseDisplayFromSettings(String name, Method method) {
        ConfigManager config = manager.getConfigManager();
        String path = manager.getDisplayPath();

        if (config != null && path != null) {
            String finalPath = path.replace("%name%", name);

            if (config.getYml().getString(finalPath) != null) {
                return config.getString(finalPath);
            }
        }

        return null;
    }

    //getter
    /*public VCommandBuilder getParent(String name) {
        //make it recursive
        for (VCommandBuilder subCommand : subCommands) {
            System.out.println("checking " + subCommand.getName() + " for " + name);

            if (subCommand.getName().equals(name)/* || getFromAlias(name) != null*//*) return subCommand;

            VCommandBuilder parent = subCommand.getParent(name);
            if (parent != null) return parent;
        }

        return null;
    }*/

    public VCommandBuilder getFromAlias(String alias) {
        return subCommands.stream().filter(subCommand -> Arrays.asList(subCommand.getAliases()).contains(alias)).findFirst().orElse(null);
    }

    //execute command
    public void execute(String main, CommandSender sender, Object[] args) {
        ReflectionUtil.invokeMethod(method, VCommandCache.getVInstances().get(main), args);
    }

    public void execute(String main, Player player, Object[] args) {
        ReflectionUtil.invokeMethod(method, VCommandCache.getVInstances().get(main), args);
    }

    public void execute(CommandSender sender, Object[] args) {
        if (sender instanceof Player) {
            execute(name, (Player) sender, args);
            return;
        }

        execute(name, sender, args);
    }


    public boolean checkExecutor(CommandSender sender) {
        switch (executorType) {
            case PLAYER:
            case OPERATORS:
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.color(manager.getError().getOnlyPlayersMessage()));
                    return false;
                }

                if (!sender.isOp() && executorType == CommandExecutor.OPERATORS) {
                    sender.sendMessage(CC.color(manager.getError().getOnlyOperatorsMessage()));
                    return false;
                }

                return true;
            case CONSOLE:
                if (sender instanceof Player) {
                    sender.sendMessage(CC.color(manager.getError().getOnlyConsoleMessage()));
                    return false;
                }

                return true;

        }

        return true;
    }

    public boolean checkPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    //send usage
    public void sendUsage(CommandSender sender) {
        if (usage == null || usage.isEmpty()) {
            sender.sendMessage(CC.color(manager.getError().getMissingArgumentsMessage()));
            return;
        }

        sender.sendMessage(CC.color(usage));
    }


    public static VCommandBuilder fromClass(Class<?> clazz) {
        VCommandBuilder commandBuilder = new VCommandBuilder(clazz);

        if (commandBuilder.getName() == null) return null;

        return commandBuilder;
    }
}
