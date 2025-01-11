package me.leoo.utils.mongodb.data;

import lombok.Getter;
import me.leoo.utils.mongodb.MongoManager;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public abstract class PlayerDocument<T> extends CommonDocument implements Listener {

    public final UUID uuid;
    private boolean firstLoad = false;

    public final T data;

    private final Consumer<PlayerDocument<T>> addConsumer;
    private final Consumer<PlayerDocument<T>> removeConsumer;

    private static final MongoManager MANAGER = MongoManager.getInstance();

    public PlayerDocument(UUID uuid, T data, Enum<?> type, String idString, Consumer<PlayerDocument<T>> addConsumer, Consumer<PlayerDocument<T>> removeConsumer) {
        super(type, idString);

        this.uuid = uuid;
        this.data = data;

        this.addConsumer = addConsumer;
        this.removeConsumer = removeConsumer;

        init();

        addConsumer.accept(this);
    }

    private void init() {
        Document document = getSavedDocument();

        if ((document == null || onLoad().apply(document)) && !firstLoad) {
            firstLoad = true;

            firstLoad();
            save();

            init();

            return;
        }

        load(document);
        afterLoad();
    }

    public void save() {
        Document document = getDocument();
        if (document == null) return;

        saveDocument(document, uuid.toString());
    }

    public void destroy() {
        save();

        removeConsumer.accept(this);
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

    public void afterLoad() {
    }

    public void onJoin(Player player) {
    }

    public void onQuit(Player player) {
    }

    public Document getSavedDocument() {
        return getDocuments(uuid.toString()).first();
    }
}

