package me.leoo.utils.bukkit.commands.v2.parser;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.commands.v2.VCommandBuilder;
import me.leoo.utils.bukkit.commands.v2.VCommandManager;
import me.leoo.utils.bukkit.commands.v2.annotation.Optional;
import me.leoo.utils.bukkit.commands.v2.annotation.Sender;
import me.leoo.utils.bukkit.commands.v2.annotation.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class ArgumentParser {

    @Getter
    private final Map<Class<?>, Function<String, ?>> providers = new HashMap<>();

    static {
        providers.put(boolean.class, arg -> {
            arg = arg.toLowerCase();
            if (arg.equals("true") || arg.equals("false")) {
                return Boolean.parseBoolean(arg);
            }
            return null;
        });
        providers.put(int.class, arg -> {
            try {
                return Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                return null;
            }
        });
        providers.put(float.class, arg -> {
            try {
                return Float.parseFloat(arg);
            } catch (NumberFormatException e) {
                return null;
            }
        });
        providers.put(long.class, arg -> {
            try {
                return Long.parseLong(arg);
            } catch (NumberFormatException e) {
                return null;
            }
        });
        providers.put(String.class, arg -> arg);
        providers.put(Player.class, Bukkit::getPlayer);
    }

    public Object[] parseArguments(VCommandBuilder command, CommandArgs args) {
        Method method = command.getMethod();
        Object[] parameters = new Object[method.getParameterCount()];

        for (int i = 0; i < method.getParameterCount(); i++) {
            Parameter parameter = method.getParameters()[i];
            Class<?> type = parameter.getType();

            if (parameter.isAnnotationPresent(Sender.class)) {
                if (type == CommandSender.class) {
                    parameters[i] = args.getSender();
                } else if (type == Player.class) {
                    parameters[i] = args.getSender();
                }

                continue;
            }

            if (parameter.isAnnotationPresent(Text.class)) {
                StringBuilder builder = new StringBuilder();
                while (args.hasNext()) {
                    builder.append(args.next()).append(" ");
                }
                parameters[i] = builder.toString().trim();

                continue;
            }

            Function<String, ?> provider = providers.get(type);
            if (provider == null) {
                Bukkit.getLogger().severe(VCommandManager.getError().getInvalidArgumentMessage().replace("{class}", type.getName()));
                return null;
            }

            String arg = null;

            if (!args.hasNext()) {
                if (parameter.isAnnotationPresent(Optional.class)) {
                    arg = parameter.getAnnotation(Optional.class).value();

                    if (arg.isEmpty()) {
                        parameters[i] = null;
                        continue;
                    }
                } else {
                    command.sendUsage(args.getSender());
                    return null;
                }
            }

            Object value = provider.apply(arg != null ? arg : args.next());
            if (value == null) {
                command.sendUsage(args.getSender());
                return null;
            }

            parameters[i] = value;
        }

        return parameters;
    }

    public <T> void addProvider(Class<T> clazz, Function<String, T> provider) {
        providers.put(clazz, provider);
    }

}
