package me.leoo.utils.common.config;

import lombok.Data;

import java.util.*;

@Data
public class Configuration {

    private final Map<String, Object> values = new LinkedHashMap<>();

    public Configuration() {
        this(new LinkedHashMap<>());
    }

    public Configuration(Map<String, Object> map) {
        if (map == null) {
            map = new LinkedHashMap<>();
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey() == null ? "null" : entry.getKey().toString();

            if (entry.getValue() instanceof Map) {
                values.put(key, new Configuration((Map<String, Object>) entry.getValue()));
            } else {
                values.put(key, entry.getValue());
            }

        }
    }

    private Configuration getSectionFor(String path) {
        int index = path.indexOf('.');
        if (index == -1) {
            return this;
        }

        String root = path.substring(0, index);
        Object section = values.get(root);
        if (section == null) {
            section = new Configuration();
            values.put(root, section);
        }

        return (Configuration) section;
    }

    private String getChild(String path) {
        int index = path.indexOf('.');
        return index == -1 ? path : path.substring(index + 1);
    }

    public void set(String path, Object value) {
        if (value instanceof Map) {
            value = new Configuration((Map<String, Object>) value);
        }

        Configuration section = getSectionFor(path);

        if (section == this) {
            if (value == null) {
                values.remove(path);
            } else {
                values.put(path, value);
            }
        } else {
            section.set(getChild(path), value);
        }
    }

    public <T> T get(String path) {
        Configuration section = getSectionFor(path);

        Object value;
        if (section == this) {
            value = values.get(path);
        } else {
            value = section.get(getChild(path));
        }

        return value != null ? (T) value : null;
    }

    public Configuration getSection(String path) {
        return get(path);
    }

    public Collection<String> getKeys() {
        return new LinkedHashSet<>(values.keySet());
    }

    public void addDefault(String path, Object value) {
        if (get(path) == null) {
            set(path, value);
        }
    }

    public byte getByte(String path) {
        Object val = get(path);
        return (val instanceof Number) ? ((Number) val).byteValue() : 0;
    }

    public List<Byte> getByteList(String path) {
        List<Byte> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Number) {
                result.add(((Number) object).byteValue());
            }
        }

        return result;
    }

    public short getShort(String path) {
        Object val = get(path);
        return (val instanceof Number) ? ((Number) val).shortValue() : 0;
    }

    public List<Short> getShortList(String path) {
        List<Short> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Number) {
                result.add(((Number) object).shortValue());
            }
        }

        return result;
    }

    public int getInt(String path) {
        Object val = get(path);
        return (val instanceof Number) ? ((Number) val).intValue() : 0;
    }

    public List<Integer> getIntList(String path) {
        List<Integer> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Number) {
                result.add(((Number) object).intValue());
            }
        }

        return result;
    }

    public long getLong(String path) {
        Object val = get(path);
        return (val instanceof Number) ? ((Number) val).longValue() : 0;
    }

    public List<Long> getLongList(String path) {
        List<Long> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Number) {
                result.add(((Number) object).longValue());
            }
        }

        return result;
    }

    public float getFloat(String path) {
        Object val = get(path);
        return (val instanceof Number) ? ((Number) val).floatValue() : 0;
    }

    public List<Float> getFloatList(String path) {
        List<Float> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Number) {
                result.add(((Number) object).floatValue());
            }
        }

        return result;
    }

    public double getDouble(String path) {
        Object val = get(path);
        return (val instanceof Number) ? ((Number) val).doubleValue() : 0;
    }

    public List<Double> getDoubleList(String path) {
        List<Double> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Number) {
                result.add(((Number) object).doubleValue());
            }
        }

        return result;
    }

    public boolean getBoolean(String path) {
        Object val = get(path);
        return (val instanceof Boolean) ? ((Boolean) val) : false;
    }

    public List<Boolean> getBooleanList(String path) {
        List<Boolean> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Boolean) {
                result.add((Boolean) object);
            }
        }

        return result;
    }

    public char getChar(String path) {
        Object val = get(path);
        return (val instanceof Character) ? (Character) val : '\u0000';
    }

    public List<Character> getCharList(String path) {
        List<Character> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof Character) {
                result.add((Character) object);
            }
        }

        return result;
    }

    public String getString(String path) {
        Object val = get(path);
        return (val instanceof String) ? (String) val : "";
    }

    public List<String> getStringList(String path) {
        List<String> result = new ArrayList<>();

        for (Object object : getList(path)) {
            if (object instanceof String) {
                result.add((String) object);
            }
        }

        return result;
    }

    public List<?> getList(String path) {
        Object val = get(path);
        return val instanceof List<?> ? (List<?>) val : Collections.emptyList();
    }
}
