package me.leoo.utils.velocity.chat;

import com.velocitypowered.api.command.CommandSource;
import lombok.experimental.UtilityClass;
import me.leoo.utils.velocity.utils.VelocityUtils;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class CC {

    public String color(String s) {
        return s.replace("§", "&");
    }

    public void sendMessage(CommandSource sender, String message) {
        sender.sendMessage(getComponent(message));
    }

    public void sendMessage(List<CommandSource> senders, String message) {
        senders.forEach(sender -> sender.sendMessage(getComponent(message)));
    }

    public void sendMessageUUID(List<UUID> senders, String message) {
        sendMessage(senders.stream()
                .map(VelocityUtils::getPlayerByUuid)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()), message);
    }

    public Component getComponent(String text) {
        return Component.text(text);
    }
}
