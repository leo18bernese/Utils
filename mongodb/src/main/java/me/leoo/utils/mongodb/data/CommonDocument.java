package me.leoo.utils.mongodb.data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.leoo.utils.common.string.StringUtil;
import me.leoo.utils.mongodb.MongoManager;
import org.bson.Document;

@Getter
public class CommonDocument {

    private final Enum<?> type;
    protected final String idString;

    public CommonDocument(Enum<?> type, String idString) {
        this.type = type;

        this.idString = idString;
    }

    public MongoCollection<Document> getCollection() {
        return MongoManager.getInstance().getDatabase().getCollection(StringUtil.translateLower(type.name()));
    }

    //Type methods

    //Data methods
    public FindIterable<Document> getData(String value) {
        return getCollection().find(Filters.eq(idString, value));
    }

    public FindIterable<Document> getData() {
        return getCollection().find();
    }

    //Document methods
    public void saveDocument(Document document, Object value) {
        getCollection().replaceOne(Filters.eq(idString, value), document, new ReplaceOptions().upsert(true));
    }

    public void removeDocument(Object value) {
        getCollection().deleteMany(Filters.eq(idString, value));
    }

    public Document getSubDocument(Document main, String name) {
        return (Document) main.getOrDefault(name, new Document());
    }

    //Utils methods
    public long getDocumentsCount(String value) {
        return getCollection().countDocuments(Filters.eq(idString, value));
    }

    public long getRegexDocumentsCount(String value) {
        return getCollection().countDocuments(Filters.regex(idString, value));
    }

    public boolean exists(String value) {
        return getDocumentsCount(value) > 0;
    }

    public boolean existsRegex(String value) {
        return getRegexDocumentsCount(value) > 0;
    }
}
