package me.leoo.utils.velocity.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.leoo.utils.common.file.FileUtil;
import me.leoo.utils.velocity.Utils;
import me.leoo.utils.velocity.chat.CC;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {

    private JsonObject json;
    private final File config;
    private final String name;

    public ConfigManager(InputStream inputStream, String name, String dir) {
        FileUtil.generateFolder(dir);

        config = new File(dir, name + ".json");

        try {
            if (!config.exists()) {
                if (inputStream != null) Files.copy(inputStream, config.toPath());
            }

            json = JsonParser.parseString(Files.readString(config.toPath())).getAsJsonObject();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        this.name = name;
    }

    public boolean getBoolean(String path) {
        return get(path).getAsBoolean();
    }

    public int getInt(String path) {
        return get(path).getAsInt();
    }

    public double getDouble(String path) {
        return get(path).getAsDouble();
    }

    public String getString(String path) {
        String string = get(path).getAsString();

        if (string == null) {
            Utils.getInstance().getLogger().info("String " + path + " not found in " + name + ".yml");
            return "StringNotFound";
        }

        return CC.color(string);
    }

    public List<String> getList(String path) {
        return get(path).getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.toList());
    }

    public List<Integer> getIntegerSplitList(String path) {
        return Arrays.stream(getString(path).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    public JsonObject get(String path) {
        String[] split = path.split("\\.");
        JsonObject object = json;

        for (String string : split) {
            JsonElement jsonElement = object.get(string);

            if (jsonElement.isJsonObject()) {
                object = jsonElement.getAsJsonObject();
            } else {
                break;
            }
        }

        return object;
    }

}