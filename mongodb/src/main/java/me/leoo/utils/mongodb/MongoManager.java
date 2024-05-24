package me.leoo.utils.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import lombok.Getter;
import me.leoo.utils.common.compatibility.SoftwareManager;

@Data
public class MongoManager {

    private MongoClient client;
    private MongoDatabase database;

    @Getter
    private static MongoManager instance;

    public MongoManager(String host, int port, String database, String username, String password) {
        connect(host, port, database, username, password);
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
