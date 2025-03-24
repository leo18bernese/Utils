package me.leoo.utils.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import lombok.Getter;
import me.leoo.utils.common.compatibility.CommonUtils;

@Data
public class MongoManager {

    private MongoClient client;
    private MongoDatabase database;

    @Getter
    private static MongoManager instance;

    public MongoManager(String host, int port, String database, String username, String password) {
        if (username == null || username.isEmpty()) {
            connect(String.format(
                    "mongodb://%s:%s/%s",
                    host, port, database), database);
        } else {
            connect(String.format(
                    "mongodb://%s:%s@%s:%d/%s",
                    username, password, host, port, database), database);
        }
    }

    public MongoManager(String uri, String database) {
        connect(uri, database);
    }

    private void connect(String uri, String database) {
        this.client = MongoClients.create(uri);

        this.database = this.client.getDatabase(database);

        CommonUtils.info("Connected to the database. (MongoDB)");

        instance = this;
    }

    public void close() {
        this.client.close();

        CommonUtils.info("Disconnected from the database. (MongoDB)");
    }
}
