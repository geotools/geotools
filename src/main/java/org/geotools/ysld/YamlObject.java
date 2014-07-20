package org.geotools.ysld;

import org.geotools.util.Converters;

import java.util.List;
import java.util.Map;

/**
 * Base class for Yaml object wrappers.
 */
public class YamlObject<T> {

    public static YamlObject create(Object raw) {
        if (raw == null) {
            return null;
        }

        if (raw instanceof YamlObject) {
            return (YamlObject) raw;
        }

        if (raw instanceof Map) {
            return new YamlMap(raw);
        }

        if (raw instanceof List) {
            return new YamlSeq(raw);
        }

        throw new IllegalArgumentException("Unable to create yaml object from: " + raw);
    }

    /**
     * underlying raw object
     */
    protected T raw;

    protected YamlObject(T raw) {
        this.raw = raw;
    }

    /**
     * Casts this object to a {@link YamlMap}.
     */
    public YamlMap map() {
        if (this instanceof YamlMap) {
            return (YamlMap) this;
        }
        throw new IllegalArgumentException("Object " + this + " is not a mapping");
    }

    /**
     * Casts this object to a {@link YamlSeq}.
     */
    public YamlSeq seq() {
        if (this instanceof YamlSeq) {
            return (YamlSeq) this;
        }
        throw new IllegalArgumentException("Object " + this + " is not a sequence");
    }

    /**
     * Returns the raw object.
     */
    public T raw() {
        return raw;
    }

    /**
     * Converts an object to the specified class.
     */
    protected <T> T convert(Object obj, Class<T> clazz) {
        if (!clazz.isInstance(obj)) {
            T converted = Converters.convert(obj, clazz);
            if (converted != null) {
                obj = converted;
            }
        }

        try {
            return clazz.cast(obj);
        }
        catch(ClassCastException e) {
            throw new IllegalStateException(String.format(
                    "Unable to retrieve %s as %s", obj, clazz.getSimpleName()), e);
        }
    }
}
