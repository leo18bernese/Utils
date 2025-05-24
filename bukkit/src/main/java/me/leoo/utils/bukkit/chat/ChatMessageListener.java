package me.leoo.utils.bukkit.chat;

import me.leoo.utils.bukkit.task.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ChatMessageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        ChatMessage request = ChatMessage.getRequests().get(uuid);

        if (request != null) {
            event.setCancelled(true);

            ChatMessage.getRequests().remove(uuid);

            request.getConsumer().accept(event);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Tasks.async(() -> ChatMessage.getRequests().remove(event.getPlayer().getUniqueId()));
    }
}
