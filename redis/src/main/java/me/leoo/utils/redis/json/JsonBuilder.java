package me.leoo.utils.redis.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public JsonBuilder add(String key, JsonElement value) {
        jsonObject.add(key, value);
        return this;
    }

    public String string() {
        return jsonObject.toString();
    }
}
