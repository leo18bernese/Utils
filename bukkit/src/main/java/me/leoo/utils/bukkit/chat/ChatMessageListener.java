package me.leoo.utils.bukkit.chat;

import me.leoo.utils.bukkit.task.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatMessageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        ChatMessage request = ChatMessage.getRequests().get(event.getPlayer().getUniqueId());

        if (request != null) {
            event.setCancelled(true);

            ChatMessage.getRequests().remove(event.getPlayer().getUniqueId());

            request.getConsumer().accept(event);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Tasks.get().async(() -> ChatMessage.getRequests().remove(event.getPlayer().getUniqueId()));
    }
}
