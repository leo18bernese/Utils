package me.leoo.utils.bukkit.commands.v2.tabcomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.leoo.utils.bukkit.commands.CommandManager;
import me.leoo.utils.bukkit.commands.v2.cache.VCommandCache;
import me.leoo.utils.common.reflection.ReflectionUtil;
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
        return (List<String>) ReflectionUtil.invokeMethod(method, VCommandCache.getVInstances().get(mainCommand), sender, alias, args);
    }

}
