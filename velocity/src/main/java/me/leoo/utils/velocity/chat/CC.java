package me.leoo.utils.velocity.chat;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;

public class CC {

    public static String color(String s) {
        return s.replace("§", "&");
    }

    public static void sendMessage(CommandSource sender, String message) {
        sender.sendMessage(getComponent(message));
    }

    public static Component getComponent(String text) {
        return Component.text(text);
    }
}
