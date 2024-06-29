package me.leoo.utils.bukkit.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.events.Events;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ChatMessage implements Listener {

    private final Consumer<AsyncPlayerChatEvent> consumer;


    @Getter
    private static final Map<UUID, ChatMessage> requests = new HashMap<>();

    public static void request(UUID uuid, Consumer<AsyncPlayerChatEvent> consumer) {
        requests.put(uuid, new ChatMessage(consumer));
    }

    public static void register() {
        Events.register(new ChatMessageListener());
    }

    public void fehdi() {
    }
}
