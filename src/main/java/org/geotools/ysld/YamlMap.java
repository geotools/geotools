package org.geotools.ysld;

import org.geotools.util.Converters;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Iterator;
import java.util.Map;

/**
 * Wrapper around a parsed Yaml mapping.
 */
public class YamlMap extends YamlObject<Map<String,Object>> implements Iterable<String> {

    public YamlMap(Object obj) {
        super(cast(obj));
    }

    static Map<String,Object> cast(Object obj) {
        if (!(obj instanceof Map)) {
            throw new IllegalArgumentException(obj + " is not a map");
        }
        return (Map<String,Object>) obj;
    }

    public String str(String key) {
        return strOr(key, null);
    }

    public String strOr(String key, String def) {
        return get(key, String.class, def);
    }

    public Integer integer(String key) {
        return intOr(key, null);
    }

    public Integer intOr(String key, Integer def) {
        return get(key, Integer.class, def);
    }

    public Double doub(String key) {
        return doubOr(key, null);
    }

    public Double doubOr(String key, Double def) {
        return get(key, Double.class, def);
    }

    public Boolean bool(String key) {
        return boolOr(key, null);
    }

    public Boolean boolOr(String key, Boolean def) {
        return get(key, Boolean.class, def);
    }

    public Object get(String key) {
        return raw.get(key);
    }

    public boolean has(String key) {
        return raw.containsKey(key);
    }

    public YamlObject obj(String key) {
        return YamlObject.create(raw.get(key));
    }

    public YamlMap map(String key) {
        Object obj = raw.get(key);
        if (obj == null) {
            return null;
        }

        return new YamlMap((Map)obj);
    }

    public YamlSeq seq(String key) {
        Object obj = raw.get(key);
        if (obj == null) {
            return null;
        }

        return new YamlSeq(obj);
    }

    @Override
    public Iterator<String> iterator() {
        return raw.keySet().iterator();
    }

    <T> T get(String key, Class<T> clazz, T def) {
        Object obj = raw.get(key);
        if (obj == null) {
            return def;
        }

        return convert(obj, clazz);
    }
}
