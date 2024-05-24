package me.leoo.utils.redis;

import com.google.gson.JsonElement;
import lombok.Getter;
import me.leoo.utils.redis.json.JsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

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
                        JedisPubSub listener, boolean generateServerId) {
        this.clientName = clientName;
        this.channels = channels;

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
    public void publish(Enum<?> type, JsonBuilder json, Enum<?>... channels) {
        publish(type, json, Arrays.stream(channels).map(Enum::name).toArray(String[]::new));
    }

    public void publish(Enum<?> type, JsonBuilder builder, String... channels) {
        publish(type, builder.getJsonObject(), channels);
    }

    public void publish(Enum<?> type, JsonElement json, String... channels) {
        String message = new JsonBuilder()
                .add("type", type.name().toLowerCase())
                .add("id", serverId == null ? "" : serverId.toString())
                .add("data", json).string();

        String[] targetChannels = channels.length == 0 ? this.channels : channels;

        for (String channel : targetChannels) {
            getRedis().publish(channel, message);
        }
    }

    // Set, Get, Delete methods
    public void set(String key, Map<String, String> value) {
        getRedis().hset(key, value);
    }

    public Map<String, String> get(String key) {
        return getRedis().hgetAll(key);
    }

    public void delete(String key, String... fields) {
        getRedis().hdel(key, fields);
    }

    // Other methods
    public void close() {
        pool.close();
    }

    private JedisPool setupPool() {
        if (user == null) {
            return new JedisPool(
                    new JedisPoolConfig(),
                    host, port,
                    30_000, password, 0, clientName
            );
        } else {
            return new JedisPool(
                    new JedisPoolConfig(),
                    host, port,
                    30_000, user, password, 0, clientName
            );
        }
    }

    private void setupListener(JedisPubSub listener) {
        ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(listener, channels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
