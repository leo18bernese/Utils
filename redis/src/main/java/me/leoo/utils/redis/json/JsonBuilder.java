package me.leoo.utils.redis.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class JsonBuilder {

    private final JsonObject jsonObject = new JsonObject();

    public JsonBuilder add(String key, String value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, Number value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, Boolean value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, UUID value) {
        jsonObject.addProperty(key, value == null ? null : value.toString());
        return this;
    }

    public JsonBuilder add(String key, JsonElement value) {
        jsonObject.add(key, value);
        return this;
    }

    // Lists
    public JsonBuilder addList(String key, List<String> values) {
        JsonArray array = new JsonArray();
        values.forEach(array::add);

        jsonObject.add(key, array);

        return this;
    }

    public JsonBuilder addIntList(String key, List<Integer> values) {
        JsonArray array = new JsonArray();
        values.forEach(array::add);

        jsonObject.add(key, array);

        return this;
    }

    // JSON Objects
    public JsonBuilder addObject(String key, List<JsonObject> values) {
        JsonArray array = new JsonArray();
        values.forEach(array::add);

        jsonObject.add(key, array);

        return this;
    }


    public String string() {
        return jsonObject.toString();
    }
}
