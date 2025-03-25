package me.leoo.utils.redis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.leoo.utils.common.compatibility.CommonUtils;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

@Setter
@RequiredArgsConstructor
public abstract class RedisListener<T extends Enum<T>> extends JedisPubSub {

    @Getter
    private final String serverChannel;
    private final Class<T> t;

    private UUID serverId = null;

    public abstract void onMessage(T type, JsonElement id, JsonElement target, JsonObject data);

    public RedisListener<T> initialize(UUID serverId) {
        this.serverId = serverId;
        return this;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals(serverChannel)) return;

        JsonObject json = new JsonParser().parse(message).getAsJsonObject();
        JsonObject data = json.get("data").getAsJsonObject();

        T type = Enum.valueOf(t, json.get("type").getAsString());

        JsonElement jsonId = json.get("id");
        JsonElement jsonTarget = json.get("target");

        // avoid sender server messages
        if (parseServerId(jsonId, id -> serverId.equals(id))) return;

        // accept only messages if target is current server
        if (parseServerId(jsonTarget, id -> !serverId.equals(id))) return;

        onMessage(type, jsonId, jsonTarget, data);
    }

    public boolean isNull(JsonObject data, String key) {
        return data.has(key) && data.get(key).isJsonNull();
    }

    @Nullable
    public String getString(JsonObject data, String key) {
        JsonElement element = parseElementOrNull(data, key);
        if (element == null) return null;

        if (element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        }

        CommonUtils.severe("Failed to get string from json object with key: " + key);

        return null;
    }

    public int getInt(JsonObject data, String key) {
        JsonElement element = parseElementOrNull(data, key);
        if (element == null) return -1;

        if (element.getAsJsonPrimitive().isNumber()) {
            return element.getAsInt();
        }

        CommonUtils.severe("Failed to get int from json object with key: " + key);

        return -1;
    }

    public boolean getBoolean(JsonObject data, String key) {
        JsonElement element = parseElementOrNull(data, key);
        if (element == null) return false;

        if (element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsBoolean();
        }

        CommonUtils.severe("Failed to get boolean from json object with key: " + key);

        return false;
    }

    /**
     * @return parsed UUID or null if failed
     */
    @Nullable
    public UUID getUuid(JsonObject data, String key) {
        JsonElement element = parseElementOrNull(data, key);
        if (element == null) return null;

        if (element.getAsJsonPrimitive().isString()) {
            String value = element.getAsString();
            if (value.isEmpty()) return null;

            return UUID.fromString(value);
        }

        CommonUtils.severe("Failed to get UUID from json object with key: " + key);

        return null;
    }

    private boolean parseServerId(JsonElement element, Predicate<UUID> predicate) {
        if (element == null || element.isJsonNull()) return false;
        if (serverId == null) return false;

        String id = element.getAsString();
        UUID serverId = id.isEmpty() ? null : UUID.fromString(id);

        return predicate.test(serverId);

    }

    private JsonElement parseElementOrNull(JsonObject data, String key) {
        if (!data.has(key)) return null;

        JsonElement element = data.get(key);
        if (element.isJsonNull()) return null;

        return element;
    }
}
