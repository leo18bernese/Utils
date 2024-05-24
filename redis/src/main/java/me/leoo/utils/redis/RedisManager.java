package me.leoo.utils.redis;

import com.google.gson.JsonElement;
import lombok.Getter;
import me.leoo.utils.redis.json.JsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@Getter
public class RedisManager {

    private final JedisPool pool;

    private final String clientName;

    private final String host;
    private final int port;

    @Nullable
    private final String user;

    @Nullable
    private final String password;

    public RedisManager(String clientName, String host, int port, @Nullable String user, @Nullable String password, JedisPubSub listener) {
        this.clientName = clientName;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;

        this.pool = setupPool();

        setupListener(listener);
    }

    public void publish(Enum<?> type, UUID serverId, JsonBuilder builder) {
        publish(type, serverId, builder.getJsonObject());
    }

    public void publish(Enum<?> type, UUID serverId, JsonElement json) {
        try (Jedis jedis = pool.getResource()) {


            if (password != null && !password.isEmpty() && user != null && !user.isEmpty()) {
                jedis.auth(user, password);
            }

            String message = new JsonBuilder()
                    .add("type", type.name().toLowerCase())
                    .add("id", serverId == null ? "" : serverId.toString())
                    .add("data", json).string();
            jedis.publish("guilds", message);
        }
    }

    public void close() {
        pool.close();
    }

    private JedisPool setupPool() {
        String redisPassword = (password == null || password.isEmpty()) ? null : password;
        String redisUser = (user == null || user.isEmpty()) ? null : user;

        if (redisUser == null) {
            return new JedisPool(
                    new JedisPoolConfig(),
                    host, port,
                    30_000, redisPassword, 0, "guilds"
            );
        } else {
            return new JedisPool(
                    new JedisPoolConfig(),
                    host, port,
                    30_000, redisUser, redisPassword, 0, "guilds"
            );
        }
    }

    private void setupListener(JedisPubSub listener) {
        ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(listener, "guilds");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
