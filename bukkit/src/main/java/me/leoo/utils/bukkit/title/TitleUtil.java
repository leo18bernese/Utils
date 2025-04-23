package me.leoo.utils.bukkit.title;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;

@UtilityClass
public class TitleUtil {

    /**
     * @deprecated Use {@link TitleManager constructor} instead.
     */
    @Deprecated
    public void sendTitleFromConfig(List<Player> players, ConfigManager config, String path, int fadeIn, int stay, int fadeOut) {
        players.forEach(player -> sendTitleFromConfig(player, config, path, fadeIn, stay, fadeOut));
    }

    /**
     * @deprecated Use {@link TitleManager constructor} instead.
     */
    @Deprecated
    public void sendTitleFromConfig(Player player, ConfigManager config, String path, int fadeIn, int stay, int fadeOut) {
        sendTitleFromConfig(player, config, path, fadeIn, stay, fadeOut, string -> string);
    }

    /**
     * @deprecated Use {@link TitleManager constructor} instead.
     */
    @Deprecated
    public void sendTitleFromConfig(Player player, ConfigManager config, String path, int fadeIn, int stay, int fadeOut, Function<String, String> replace) {
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

    /**
     * @deprecated Use {@link TitleManager constructor} instead.
     */
    @Deprecated
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        new Titles(title, subtitle, fadeIn, stay, fadeOut).send(player);
    }

    public void sendActionBar(Player player, String text) {
        ActionBar.sendActionBar(player, text);
    }
}
