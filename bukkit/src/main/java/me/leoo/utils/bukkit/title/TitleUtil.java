package me.leoo.utils.bukkit.title;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;

public class TitleUtil {

    public static void sendTitleFromConfig(Player player, ConfigManager config, String path, int fadeIn, int stay, int fadeOut) {
        sendTitleFromConfig(player, config, path, fadeIn, stay, fadeOut, string -> string);
    }

    public static void sendTitleFromConfig(Player player, ConfigManager config, String path, int fadeIn, int stay, int fadeOut, Function<String, String> replace) {
        Object messageObject = config.getYml().get(path + ".message");

        if (messageObject != null) {
            if (messageObject instanceof List) {
                config.getList(path + ".message").forEach(string -> player.sendMessage(replace.apply(string)));
            } else if (messageObject instanceof String) {
                String string = config.getString(path + ".message");

                if (!string.isEmpty()) player.sendMessage(replace.apply(string));
            }
        }

        String title = replace.apply(config.getString(path + ".title"));
        String subTitle = replace.apply(config.getString(path + ".sub-title"));

        new Titles(title, subTitle, fadeIn, stay, fadeOut).send(player);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        new Titles(title, subtitle, fadeIn, stay, fadeOut).send(player);
    }

    public static void sendActionBar(Player player, String text) {
        ActionBar.sendActionBar(player, text);
    }
}
