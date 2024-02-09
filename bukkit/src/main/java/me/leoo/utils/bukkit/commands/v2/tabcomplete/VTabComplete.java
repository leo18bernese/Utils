package me.leoo.utils.bukkit.commands.v2.tabcomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.common.reflection.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@AllArgsConstructor
public class VTabComplete {

    private String mainCommand;

    private String name;
    private String[] aliases;

    private Method method;

    public List<String> execute(CommandSender sender, String alias, String[] args) {
        Object instance = VCommandCache.getVInstances().get(mainCommand);
        if (instance == null) {
            Bukkit.getLogger().warning("No instance found for tab complete for main command " + mainCommand);
            return null;
        }

        return (List<String>) ReflectionUtil.invokeMethod(method, instance, sender, alias, args);
    }

}
