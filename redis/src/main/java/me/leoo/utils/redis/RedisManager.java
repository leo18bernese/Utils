package me.leoo.utils.redis;

import com.google.gson.JsonElement;
import lombok.Getter;
import me.leoo.utils.common.compatibility.CommonUtils;
import me.leoo.utils.redis.json.JsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Nullable;
import java.util.*;

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

    private static final Map<String, List<String>> CHANNEL_SERVERS = new HashMap<>();

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

        // Setup listener
        listener = listener.initialize(serverId);

        setupListener(listener);

        CHANNEL_SERVERS.computeIfAbsent(listener.getServerChannel(), s -> new ArrayList<>()).add(clientName);
    }

    /*private Jedis getRedis() {
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
    }*/

    // Publishing messages
    public void publish(Enum<?> type, JsonBuilder builder, Object... channels) {
        publish(type, builder.getJsonObject(), null, false, Arrays.stream(channels).map(Object::toString).toArray(String[]::new));
    }

    public void publishSelf(Enum<?> type, JsonBuilder builder, Object... channels) {
        publish(type, builder.getJsonObject(), null, true, Arrays.stream(channels).map(Object::toString).toArray(String[]::new));
    }

    public void publishOnly(Enum<?> type, JsonBuilder builder, String target, Object... channels) {
        publish(type, builder.getJsonObject(), target, false, Arrays.stream(channels).map(Object::toString).toArray(String[]::new));
    }


    private void publish(Enum<?> type, JsonElement json, String target, boolean sendToSelf, String... channels) {
        String message = new JsonBuilder()
                .add("type", type.name())
                .add("id", (serverId == null || sendToSelf) ? "" : serverId.toString())
                .add("target", target)
                .add("data", json).string();

        String[] targetChannels = channels.length == 0 ? this.channels : channels;

        try (Jedis jedis = pool.getResource()) {
            for (String channel : targetChannels) {
                jedis.publish(channel.toUpperCase(), message);
            }
        }
    }

    public void publish(Enum<?> type, JsonElement json, String... channels) {
        publish(type, json, null, false, channels);
    }

    public void publishOnce(Enum<?> type, JsonBuilder builder, Object... channels) {
        for (Object object : channels) {
            String channel = object.toString();


            if (CHANNEL_SERVERS.containsKey(channel.toUpperCase())) {
                List<String> target = CHANNEL_SERVERS.get(channel.toUpperCase());

                if (target.isEmpty()) continue;

                publishOnly(type, builder, target.get(0), channel);
                return;
            }
        }
    }

    // Set, Get, Delete methods
    public void set(String key, String field, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(key, field, value);
        }
    }

    public Map<String, String> get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    public void delete(String key, String... fields) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hdel(key, fields);
        }
    }

    public boolean exists(String key, String field) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hexists(key, field);
        }
    }

    // Other methods
    public void close() {
        pool.close();
    }

    private JedisPool setupPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);

        if (user == null) {
            CommonUtils.info("Connecting to Redis with no authentication");
            return new JedisPool(poolConfig, host, port, 30_000, password, 0, clientName);
        }

        CommonUtils.info("Connecting to Redis with authentication");
        return new JedisPool(poolConfig, host, port, 30_000, user, password, 0, clientName);
    }

    private void setupListener(RedisListener<?> listener) {
        setupListener(listener, 0);
    }


    private void setupListener(RedisListener<?> listener, int retryCount) {
        Thread redisListenerThread = new Thread(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(listener, channels);
            } catch (Exception exception) {
                exception.printStackTrace();

                if (retryCount >= 5) {
                    CommonUtils.severe("Redis listener error, retry count exceeded, stopping...");
                    return;
                }

                CommonUtils.severe("Redis listener error, restarting...");

                setupListener(listener);
            }
        });

        redisListenerThread.setName(clientName + " redis listener");
        redisListenerThread.start();
        /*ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(listener/*.setServerId(serverId)*//*, channels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });*/
    }
}
