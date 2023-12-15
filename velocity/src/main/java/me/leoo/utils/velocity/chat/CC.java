package me.leoo.utils.velocity.chat;

import com.velocitypowered.api.command.CommandSource;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;

@UtilityClass
public class CC {

    public String color(String s) {
        return s.replace("§", "&");
    }

    public void sendMessage(CommandSource sender, String message) {
        sender.sendMessage(getComponent(message));
    }

    public Component getComponent(String text) {
        return Component.text(text);
    }
}
