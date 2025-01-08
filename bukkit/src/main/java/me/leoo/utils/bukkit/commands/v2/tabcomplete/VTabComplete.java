package me.leoo.utils.bukkit.commands.v2.tabcomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.leoo.utils.bukkit.commands.v2.VCommandBuilder;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.common.reflection.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class VTabComplete {

    private String mainCommand;

    private String name;
    private final List<String> aliases = new ArrayList<>();

    private Method method;

    /**
     * Add automatically the aliases of the sub command
     */
    public VTabComplete init(VCommandBuilder builder, List<String> alias) {
        VCommandBuilder subCommand = builder.getSubCommand(name);

        if (subCommand == null) {
            Bukkit.getLogger().warning("No sub command found for tab complete for main command " + mainCommand + " and sub command " + name);
            return this;
        }

        aliases.addAll(alias);
        aliases.addAll(Arrays.asList(subCommand.getAliases()));

        return this;
    }

    public List<String> execute(CommandSender sender, String alias, String[] args) {
        Object instance = VCommandCache.getInstance(mainCommand);

        if (instance == null) {
            Bukkit.getLogger().warning("No instance found for tab complete for main command " + mainCommand);
            return null;
        }

        return (List<String>) ReflectionUtil.invokeMethod(method, instance, sender, alias, args);
    }

}
