package me.leoo.utils.mongodb.data;

import org.bukkit.event.Listener;

public abstract class ServerDocument<T> extends CommonDocument implements Listener {

    public ServerDocument(Enum<?> type, String idString) {
        super(type, idString);
    }

    //Type methods
    public abstract void save(T object);
}

