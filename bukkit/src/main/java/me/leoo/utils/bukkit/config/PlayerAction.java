package me.leoo.utils.bukkit.config;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Data
@RequiredArgsConstructor
public class PlayerAction {

    private final String string;

    public boolean run(Player player) {
        if (!string.contains("[") && !string.contains("]")) return false;

        String type = string.substring(string.indexOf('[') + 1, string.indexOf(']'));
        String value = string.substring(string.indexOf(']') + 2)
                .replace("{player}", player.getName());

        if (type.equals("plugin")) {
            return false;
        } else {
            switch (type) {
                case "chat":
                    player.chat(value);
                    break;
                case "player":
                    player.performCommand(value);
                    break;
                case "console":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
                    break;
                case "server":
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                    out.writeUTF("Connect");
                    out.writeUTF(value);

                    player.sendPluginMessage(Utils.get(), "BungeeCord", out.toByteArray());
                    break;
                case "no-action":
                    break;
            }

            return true;
        }
    }

    public static PlayerAction fromConfig(ConfigManager config, String path) {
        return new PlayerAction(config.getYml().contains(path + ".command") ? config.getString(path + ".command") : "");
    }
}
