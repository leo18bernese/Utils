package me.leoo.utils.bukkit.config;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Data
@RequiredArgsConstructor
public class PlayerAction {

    private final String action;

    private static final Map<String, BiConsumer<Player, String>> ACTIONS = new HashMap<>();

    static {
        ACTIONS.put("message", Player::sendMessage);
        ACTIONS.put("chat", Player::chat);
        ACTIONS.put("player", Player::performCommand);
        ACTIONS.put("console", (player, value) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value));
        ACTIONS.put("server", (player, value) -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("Connect");
            out.writeUTF(value);

            player.sendPluginMessage(Utils.get(), "BungeeCord", out.toByteArray());
        });

        ACTIONS.put("no-action", (player, value) -> {
        });
    }

    public boolean run(Player player) {
        if (!action.contains("[") && !action.contains("]")) return false;

        String type = action.substring(action.indexOf('[') + 1, action.indexOf(']'));
        String value = action.substring(action.indexOf(']') + 2)
                .replace("{player}", player.getName());

        if (type.equals("plugin")) {
            return false;
        } else {
            ACTIONS.get(type).accept(player, value);

            return true;
        }
    }

    public static boolean run(Player player, String string) {
        return new PlayerAction(string).run(player);
    }

    public static boolean execute(Player player, ConfigSection config, String path) {
        path = path + ".command";

        if (!config.contains(path)) return false;

        if (config.getYml().isList(path)) {
            boolean executed = false;

            for (String s : config.getList(path)) {
                executed = PlayerAction.run(player, s) && !executed;
            }

            return executed;
        }

        return PlayerAction.run(player, config.getString(path));
    }
}
