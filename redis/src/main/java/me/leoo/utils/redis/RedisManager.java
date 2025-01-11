package me.leoo.utils.redis;

import com.google.gson.JsonElement;
import lombok.Getter;
import me.leoo.utils.common.compatibility.SoftwareManager;
import me.leoo.utils.redis.json.JsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@Getter
public class RedisManager {

    private final JedisPool pool;
    private final UUID serverId;

    private final String clientName;
    private final String[] channels;

    private final String host;
    private final int port;

    @Nullable
    private final String user;

    @Nullable
    private final String password;

    public RedisManager(String clientName, String[] channels,
                        String host, int port, @Nullable String user, @Nullable String password,
                        RedisListener<?> listener, boolean generateServerId) {
        this.clientName = clientName;
        this.channels = Arrays.stream(channels).map(String::toUpperCase).toArray(String[]::new);

        this.host = host;
        this.port = port;
        this.user = (user == null || user.isEmpty()) ? null : user;
        this.password = (password == null || password.isEmpty()) ? null : password;

        this.serverId = generateServerId ? UUID.randomUUID() : null;

        this.pool = setupPool();

        setupListener(listener);
    }

    private Jedis getRedis() {
        try (Jedis jedis = pool.getResource()) {

            if (password != null) {
                if (user != null) {
                    jedis.auth(user, password);
                } else {
                    jedis.auth(password);
                }
            }

            return jedis;
        }
    }

    // Publishing messages
    public void publish(Enum<?> type, JsonBuilder builder, Object... channels) {
        publish(type, builder.getJsonObject(), Arrays.stream(channels).map(Object::toString).toArray(String[]::new));
    }

    public void publish(Enum<?> type, JsonElement json, String... channels) {
        String message = new JsonBuilder()
                .add("type", type.name())
                .add("id", serverId == null ? "" : serverId.toString())
                .add("data", json).string();

        String[] targetChannels = channels.length == 0 ? this.channels : channels;

        for (String channel : targetChannels) {
            getRedis().publish(channel.toUpperCase(), message);
        }
    }

    // Set, Get, Delete methods
    public void set(String key, String field, String value) {
        getRedis().hset(key, field, value);
    }

    public Map<String, String> get(String key) {
        return getRedis().hgetAll(key);
    }

    public void delete(String key, String... fields) {
        getRedis().hdel(key, fields);
    }

    public boolean exists(String key, String field) {
        return getRedis().hexists(key, field);
    }

    // Other methods
    public void close() {
        pool.close();
    }

    private JedisPool setupPool() {
        if (user == null) {

            SoftwareManager.info("Connecting to Redis with no authentication");

            return new JedisPool(
                    new JedisPoolConfig(),
                    host, port,
                    30_000, password, 0, clientName
            );
        }

        SoftwareManager.info("Connecting to Redis with authentication");

        return new JedisPool(
                new JedisPoolConfig(),
                host, port,
                30_000, user, password, 0, clientName
        );
    }

    private void setupListener(RedisListener<?> listener) {
        ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(listener/*.setServerId(serverId)*/, channels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
