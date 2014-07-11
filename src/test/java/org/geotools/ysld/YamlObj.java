package org.geotools.ysld;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * Helper for playing with Yaml objects.
 */
public class YamlObj {

    Object obj;

    public YamlObj(Object obj) {
        this.obj = obj;
    }

    public YamlObj o(String key) {
        return new YamlObj(map().get(key));
    }

    public String s(String key) {
        return (String) map().get(key);
    }

    public Integer i(String key) {
        return (Integer) map().get(key);
    }

    public Double d(String key) {
        return (Double) map().get(key);
    }

    public Boolean b(String key) {
        return (Boolean) map().get(key);
    }

    public YamlObj o(String key, int index) {
        return new YamlObj(o(key).list().get(index));
    }

    public String s(String key, int index) {
        return (String) o(key).list().get(index);
    }

    Map<String,Object> map() {
        if (!(obj instanceof Map)) {
            throw new IllegalStateException(obj + " is not a map");
        }
        return (Map) obj;
    }

    List<Object> list() {
        if (!(obj instanceof List)) {
            throw new IllegalStateException(obj + " is not a list");
        }
        return (List) obj;
    }

    @Override
    public String toString() {
        DumperOptions dumpOpts = new DumperOptions();
        dumpOpts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(dumpOpts).dump(obj);
    }
}
