package me.leoo.utils.bukkit.title;

import com.cryptomorin.xseries.messages.Titles;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.entity.Player;

public class TitleUtil {

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        new Titles(title, subtitle, fadeIn, stay, fadeOut).send(player);
    }

    public static void sendActionBar(Player player, String text){
        ActionBar.sendActionBar(player, text);
    }
}
