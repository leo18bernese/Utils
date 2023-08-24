package me.leoo.utils.bukkit.title;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.entity.Player;

public class TitleUtil {

    public static void sendTitleFromConfig(Player player, ConfigManager config, String path, int fadeIn, int stay, int fadeOut) {
        String message = config.getString(path + ".message");
        if (message != null) {
            player.sendMessage(message);
        }

        new Titles(config.getString(path + ".title"), config.getString(path + ".sub-title"), fadeIn, stay, fadeOut).send(player);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        new Titles(title, subtitle, fadeIn, stay, fadeOut).send(player);
    }

    public static void sendActionBar(Player player, String text) {
        ActionBar.sendActionBar(player, text);
    }
}
