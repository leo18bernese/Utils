package me.leoo.utils.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

@Setter
@RequiredArgsConstructor
public abstract class RedisListener<T extends Enum<T>> extends JedisPubSub {

    private final String serverChannel;
    private final Class<T> t;
    private UUID serverId;

    public abstract void onMessage(T type, JsonObject data);

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals(serverChannel)) return;

        JsonObject json = new JsonParser().parse(message).getAsJsonObject();
        JsonObject data = json.get("data").getAsJsonObject();

        T type = Enum.valueOf(t, json.get("type").getAsString());

        String id = json.get("id").getAsString();
        UUID serverId = id.isEmpty() ? null : UUID.fromString(id);

        if (this.serverId != null && this.serverId.equals(serverId)) return;

        onMessage(type, data);
    }

    public RedisListener<T> setServerId(UUID serverId) {
        this.serverId = serverId;
        return this;
    }
}
