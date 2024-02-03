package me.leoo.utils.bukkit.commands.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.leoo.utils.bukkit.chat.CC;
import me.leoo.utils.bukkit.commands.CommandManager;
import me.leoo.utils.bukkit.commands.v2.annotation.*;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.bukkit.commands.v2.tabcomplete.VTabComplete;
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

    private Method method;

    private final List<VCommandBuilder> subCommands = new ArrayList<>();

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

            this.usage = parseUsage(method);

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

            String subUsage = parseUsage(method);

            subCommands.add(new VCommandBuilder(subName, subAliases, subExecutor, subPermission, subUsage, method));
        } else if (method.getAnnotation(TabComplete.class) != null) {
            TabComplete tabComplete = method.getAnnotation(TabComplete.class);

            String main = tabComplete.value();
            String[] alias = tabComplete.aliases();

            VCommandCache.getTabComplete().put(main, new VTabComplete(name, alias, method));
        }
    }

    private String parsePermission(Method method) {
        if (method.getAnnotation(CommandPermission.class) != null) {
            CommandPermission commandPermission = method.getAnnotation(CommandPermission.class);

            return commandPermission.value();
        }

        return "";
    }

    private String parseUsage(Method method) {
        if (method.getAnnotation(CommandUsage.class) != null) {
            CommandUsage commandUsage = method.getAnnotation(CommandUsage.class);

            return commandUsage.value();
        }

        return null;
    }

    //getter
    public VCommandBuilder getParent(String name) {
        //make it recursive
        for (VCommandBuilder subCommand : subCommands) {
            System.out.println("checking " + subCommand.getName() + " for " + name);

            if (subCommand.getName().equals(name)/* || getFromAlias(name) != null*/) return subCommand;

            VCommandBuilder parent = subCommand.getParent(name);
            if (parent != null) return parent;
        }

        return null;
    }

    public VCommandBuilder getFromAlias(String alias) {
        return subCommands.stream().filter(subCommand -> Arrays.asList(subCommand.getAliases()).contains(alias)).findFirst().orElse(null);
    }

    //execute command
    public void execute(String main, CommandSender sender, Object[] args) {
        ReflectionUtil.invokeMethod(method, CommandManager.getVInstances().get(main), sender, args);
    }

    public void execute(String main, Player player, Object[] args) {
        ReflectionUtil.invokeMethod(method, CommandManager.getVInstances().get(main), args);
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
                    sender.sendMessage(CC.color(VCommandManager.getError().getOnlyPlayersMessage()));
                    return false;
                }

                if (!sender.isOp() && executorType == CommandExecutor.OPERATORS) {
                    sender.sendMessage(CC.color(VCommandManager.getError().getOnlyOperatorsMessage()));
                    return false;
                }

                return true;
            case CONSOLE:
                if (sender instanceof Player) {
                    sender.sendMessage(CC.color(VCommandManager.getError().getOnlyConsoleMessage()));
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
        if (usage == null) {
            sender.sendMessage(CC.color(VCommandManager.getError().getMissingArgumentsMessage()));
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
