package me.leoo.utils.common.config;

import lombok.Data;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class YamlConfiguration {

    private final File file;
    private final Yaml yaml;

    public YamlConfiguration(File file) {
        this.file = file;

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer(options) {
            {
                representers.put(Configuration.class, data -> represent(((Configuration) data).getValues()));
            }
        };

        this.yaml = new Yaml(representer, options);
    }

    public Configuration load() throws IOException {
        InputStream inputStream = Files.newInputStream(file.toPath());
        Map<String, Object> map = yaml.loadAs(inputStream, LinkedHashMap.class);

        return new Configuration(map);
    }

    public void save(Configuration configuration) throws IOException {
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8.newEncoder())) {
            yaml.dump(configuration.getValues(), writer);
        }
    }
}
