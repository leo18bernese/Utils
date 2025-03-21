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
import org.bukkit.Bukkit;
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

    private String parent;

    private CommandExecutor executorType;
    private String permission;

    private String[] usage;
    private String display;

    private Method method;

    private boolean confirmation;

    private final List<VCommandBuilder> subCommands = new ArrayList<>();

    private static final VCommandManager MANAGER = VCommandManager.get();

    public VCommandBuilder(Class<?> clazz) {
        boolean mainParsed = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Command.class))
                .findFirst()
                .map(this::parseMain)
                .orElse(false);

        if (!mainParsed) {
            throw new IllegalArgumentException("Error during main command parse or command disabled by config");
        }

        if (this.name == null) {
            throw new IllegalArgumentException("Main Command not initialized for class " + clazz.getName() + " with @Command annotation");
        }

        for (Method method : clazz.getMethods()) {
            parseSubCommand(method);
        }

        for (Method method : clazz.getMethods()) {
            parseTabComplete(method);
        }
    }

    private boolean parseMain(Method method) {
        if (method.isAnnotationPresent(Command.class)) {
            Command mainCommand = method.getAnnotation(Command.class);

            if (mainCommand.value().length == 0)
                throw new IllegalArgumentException("Main Command name cannot be empty");

            this.name = mainCommand.value()[0];

            if (isDisabledByConfig(this.name)) return false;

            this.aliases = Arrays.stream(mainCommand.value()).skip(1).toArray(String[]::new);

            this.executorType = mainCommand.executor();
            this.permission = parsePermission(method);

            this.usage = parseUsage(this.name, null, method);
            this.display = parseDisplay(this.name, null, method);

            this.method = method;

            return true;
        }

        return false;
    }

    private void parseSubCommand(Method method) {
        if (method.isAnnotationPresent(SubCommand.class)) {
            SubCommand subCommand = method.getAnnotation(SubCommand.class);

            if (subCommand.parent() != null && !subCommand.parent().isEmpty() && !subCommand.parent().equals(this.name))
                return;
            if (subCommand.value().length == 0) throw new IllegalArgumentException("Sub Command name cannot be empty");

            String subName = subCommand.value()[0];
            String parent = this.name;

            if (isDisabledByConfig(parent + "." + subName)) return;

            String[] subAliases = Arrays.stream(subCommand.value()).skip(1).toArray(String[]::new);

            CommandExecutor subExecutor = subCommand.executor();
            String subPermission = parsePermission(method);

            String[] subUsage = parseUsage(subName, parent, method);
            String subDisplay = parseDisplay(subName, parent, method);

            List<String> subAliasesByConfig = getAliasesByConfig(parent + "." + subName);
            if (subAliasesByConfig != null) {
                subAliases = subAliasesByConfig.toArray(new String[0]);
            }


            boolean confirmation = subCommand.confirmation();

            subCommands.add(new VCommandBuilder(subName, subAliases, parent, subExecutor, subPermission, subUsage, subDisplay, method, confirmation));
        }
    }

    private void parseTabComplete(Method method) {
        if (method.isAnnotationPresent(TabComplete.class)) {
            TabComplete tabComplete = method.getAnnotation(TabComplete.class);

            String main = tabComplete.value();

            List<String> alias = tabComplete.aliases() == null ? new ArrayList<>() : Arrays.asList(tabComplete.aliases());

            VCommandCache.getTabComplete().put(main, new VTabComplete(this.name, main, method).init(this, alias));
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
    private String[] parseUsage(String name, String parent, Method method) {
        if (method.getAnnotation(CommandUsage.class) != null) {
            CommandUsage commandUsage = method.getAnnotation(CommandUsage.class);

            return commandUsage.usage();
        }

        return parseUsageFromSettings(name, parent, method);
    }

    private String[] parseUsageFromSettings(String name, String parent, Method method) {
        ConfigManager config = MANAGER.getLanguage().get();
        String path = MANAGER.getUsagePath(name, parent);

        if (config != null && path != null) {
            String finalPath = path.replace("%name%", name);

            Object object = config.getYml().get(finalPath);
            if (object == null) return null;

            if (object instanceof List) {
                return config.getList(finalPath).toArray(new String[0]);
            }

            return new String[]{config.getString(finalPath)};
        }

        if (MANAGER.isBuildUsageMessage()) {
            return new String[]{"&cUsage: /" + name + " " + ArgumentParser.parseArgumentsString(method)};
        }

        return null;
    }

    //display
    private String parseDisplay(String name, String parent, Method method) {
        if (method.getAnnotation(CommandUsage.class) != null) {
            CommandUsage commandDisplay = method.getAnnotation(CommandUsage.class);

            return commandDisplay.display();
        }

        return parseDisplayFromSettings(name, parent, method);
    }

    private String parseDisplayFromSettings(String name, String parent, Method method) {
        ConfigManager config = MANAGER.getLanguage().get();
        String path = MANAGER.getDisplayPath(name, parent);

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

    public VCommandBuilder getSubCommand(String name) {
        return subCommands.stream().filter(subCommand -> subCommand.getName().equals(name)).findFirst().orElse(null);
    }

    public VCommandBuilder getFromAlias(String alias) {
        return subCommands.stream().filter(subCommand -> Arrays.asList(subCommand.getAliases()).contains(alias)).findFirst().orElse(null);
    }

    //execute command
    public void execute(String main, CommandSender sender, Object[] args) {
        ReflectionUtil.invokeMethod(method, VCommandCache.getInstances().get(main), args);
    }

    public void execute(String main, Player player, Object[] args) {
        Object instance = VCommandCache.getInstance(main);

        if (instance == null) {
            Bukkit.getLogger().warning("No instance found for command " + main + ", please report this to the developer.");
            return;
        }

        ReflectionUtil.invokeMethod(method, VCommandCache.getInstance(main), args);
    }

    public void execute(CommandSender sender, Object[] args) {
        if (sender instanceof Player) {
            execute(name, (Player) sender, args);
            return;
        }

        execute(name, sender, args);
    }


    public boolean testExecutor(CommandSender sender) {
        switch (executorType) {
            case PLAYER:
            case OPERATORS:
                if (!(sender instanceof Player)) {
                    sender.sendMessage(CC.color(MANAGER.getError().getOnlyPlayersMessage()));
                    return false;
                }

                if (!sender.isOp() && executorType == CommandExecutor.OPERATORS) {
                    sender.sendMessage(CC.color(MANAGER.getError().getOnlyOperatorsMessage()));
                    return false;
                }

                return true;
            case CONSOLE:
                if (sender instanceof Player) {
                    sender.sendMessage(CC.color(MANAGER.getError().getOnlyConsoleMessage()));
                    return false;
                }

                return true;

        }

        return true;
    }

    public boolean testPermission(CommandSender sender) {
        if (permission == null || permission.isEmpty()) return true;

        return sender.hasPermission(permission);
    }

    //check if command is disable by config
    public boolean isDisabledByConfig(String commandPath) {
        String path = VCommandManager.getPath(commandPath, MANAGER.getPath(), "disabled", MANAGER.getConfig());
        if (path == null) return false;

        return MANAGER.getConfig().get().getBoolean(path);
    }

    //alies by config
    public List<String> getAliasesByConfig(String commandPath) {
        String path = VCommandManager.getPath(commandPath, MANAGER.getPath(), "aliases", MANAGER.getConfig());
        if (path == null) return null;

        return MANAGER.getConfig().get().getList(path);
    }

    //send usage
    public void sendUsage(CommandSender sender) {
        if (usage == null || usage.length == 0) {
            sender.sendMessage(CC.color(MANAGER.getError().getMissingArgumentsMessage()));
            return;
        }

        for (String s : usage) {
            sender.sendMessage(CC.color(s));
        }
    }

    //reload
    public void reload() {
        subCommands.forEach(VCommandBuilder::reload);

        this.usage = parseUsage(name, parent, method);
        this.display = parseDisplay(name, parent, method);
    }

    public static VCommandBuilder fromClass(Class<?> clazz) {
        VCommandBuilder commandBuilder = new VCommandBuilder(clazz);

        if (commandBuilder.getName() == null) return null;

        return commandBuilder;
    }
}
