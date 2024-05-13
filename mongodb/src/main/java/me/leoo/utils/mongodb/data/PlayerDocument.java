package me.leoo.utils.mongodb.data;

import lombok.Getter;
import me.leoo.utils.mongodb.MongoManager;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.function.Function;

@Getter
public abstract class PlayerDocument extends CommonDocument implements Listener {

    public final UUID uuid;
    private boolean firstLoad = false;

    private static final MongoManager MANAGER = MongoManager.getInstance();

    public PlayerDocument(UUID uuid, Enum<?> type) {
        super(type, "uuid");

        this.uuid = uuid;

        init();
        MANAGER.getAddToPlayerData().accept(uuid, this);
    }

    private void init() {
        Document document = getSavedDocument();

        if (document == null || onLoad().apply(document)) {
            firstLoad();
            save();

            init();

            firstLoad = true;

            return;
        }

        load(document);
    }

    public void save() {
        saveDocument(getDocument(), uuid.toString());
    }

    public void destroy() {
        save();

        MANAGER.getRemoveFromPlayerData().accept(uuid, this);
    }

    //Abstract methods
    public abstract void load(Document document);

    public abstract Document getDocument();

    //Methods
    public void firstLoad() {
    }

    public Function<Document, Boolean> onLoad() {
        return document -> false;
    }

    public void onJoin(Player player) {
    }

    public void onQuit(Player player) {
    }

    public Document getSavedDocument() {
        return getData(uuid.toString()).first();
    }
}

