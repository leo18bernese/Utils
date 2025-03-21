package me.leoo.utils.redis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.leoo.utils.common.compatibility.SoftwareManager;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;
import java.util.function.Predicate;

@Setter
@RequiredArgsConstructor
public abstract class RedisListener<T extends Enum<T>> extends JedisPubSub {

    private final String serverChannel;
    private final Class<T> t;

    public abstract void onMessage(T type, JsonObject data);

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals(serverChannel)) return;

        JsonObject json = new JsonParser().parse(message).getAsJsonObject();
        JsonObject data = json.get("data").getAsJsonObject();

        T type = Enum.valueOf(t, json.get("type").getAsString());


        // avoid sender server messages
        if (parseServerId(json.get("id"), id -> getServerId().equals(id))) return;

        // accept only messages if target is current server
        if (parseServerId(json.get("target"), id -> !getServerId().equals(id))) return;

        onMessage(type, data);
    }

    public abstract UUID getServerId();

    public boolean isNull(JsonObject data, String key) {
        return data.has(key) && data.get(key).isJsonNull();
    }

    public String getString(JsonObject data, String key) {
        if (data.has(key) && data.get(key).getAsJsonPrimitive().isString()) {
            return data.get(key).getAsString();
        }

        SoftwareManager.severe("Failed to get string from json object with key: " + key);

        return null;
    }

    public int getInt(JsonObject data, String key) {
        if (data.has(key) && data.get(key).getAsJsonPrimitive().isNumber()) {
            return data.get(key).getAsInt();
        }

        SoftwareManager.severe("Failed to get int from json object with key: " + key);

        return -1;
    }

    public boolean getBoolean(JsonObject data, String key) {
        if (data.has(key) && data.get(key).getAsJsonPrimitive().isBoolean()) {
            return data.get(key).getAsBoolean();
        }

        SoftwareManager.severe("Failed to get boolean from json object with key: " + key);

        return false;
    }

    private boolean parseServerId(JsonElement element, Predicate<UUID> predicate) {
        if (element == null || element.isJsonNull()) return false;

        String id = element.getAsString();
        UUID serverId = id.isEmpty() ? null : UUID.fromString(id);

        if (getServerId() == null) return false;

        return predicate.test(serverId);

    }
}
