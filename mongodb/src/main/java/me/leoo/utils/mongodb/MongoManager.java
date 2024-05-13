package me.leoo.utils.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import lombok.Getter;
import me.leoo.utils.common.compatibility.SoftwareManager;
import me.leoo.utils.mongodb.data.PlayerDocument;

import java.util.UUID;
import java.util.function.BiConsumer;

@Data
public class MongoManager {

    private MongoClient client;
    private MongoDatabase database;

    @Getter
    private static MongoManager instance;

    private BiConsumer<UUID, PlayerDocument> addToPlayerData;
    private BiConsumer<UUID, PlayerDocument> removeFromPlayerData;

    public MongoManager(String host, int port, String database, String username, String password, BiConsumer<UUID, PlayerDocument> addToPlayerData, BiConsumer<UUID, PlayerDocument> removeFromPlayerData) {
        connect(host, port, database, username, password);

        this.addToPlayerData = addToPlayerData;
        this.removeFromPlayerData = removeFromPlayerData;
    }

    private void connect(String host, int port, String database, String username, String password) {
        if (username == null || username.isEmpty()) {
            this.client = MongoClients.create(String.format(
                    "mongodb://%s:%s/%s",
                    host, port, database
            ));
        } else {
            this.client = MongoClients.create(String.format(
                    "mongodb://%s:%s@%s:%d/%s",
                    username, password, host, port, database
            ));
        }

        this.database = this.client.getDatabase(database);

        SoftwareManager.info("Connected to the database.");

        instance = this;
    }

    public void close() {
        this.client.close();
    }
}
