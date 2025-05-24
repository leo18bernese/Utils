package me.leoo.utils.redis.json;

import com.google.gson.JsonElement;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class JsonUtils {

    public <T> List<T> asArray(JsonElement json, Function<JsonElement, T> function) {
        return json.getAsJsonArray().asList().stream()
                .map(function)
                .collect(Collectors.toList());
    }
}
