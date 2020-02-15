/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ysld;

import java.util.Iterator;
import java.util.Map;
import org.geotools.util.KVP;

/** Wrapper around a parsed Yaml mapping. */
public class YamlMap extends YamlObject<Map<String, Object>> implements Iterable<String> {

    /**
     * Quick inline map creation.
     *
     * @param pairs Key value pairs
     * @return Map of provided key value pairs.
     */
    public static YamlMap from(Object... pairs) {
        KVP kvp = new KVP(pairs);
        return new YamlMap(kvp);
    }

    /**
     * Internal cast to map used by constructor.
     *
     * @return Map
     */
    static Map<String, Object> cast(Object obj) {
        if (!(obj instanceof Map)) {
            throw new IllegalArgumentException(obj + " is not a map");
        }
        return (Map<String, Object>) obj;
    }

    /**
     * Yaml mapping.
     *
     * @param obj Wrapped {@link Map}
     * @throws IllegalArgumentException {@link Map} is required
     */
    public YamlMap(Object obj) {
        super(cast(obj));
    }

    /**
     * String access.
     *
     * @return String access, converting if necessary.
     */
    public String str(String key) {
        return strOr(key, null);
    }

    /**
     * String access.
     *
     * @param def default if value not provided
     * @return String access, converting if necessary.
     */
    public String strOr(String key, String def) {
        return get(key, String.class, def);
    }

    /**
     * Integer access.
     *
     * @return Integer access, converting if necessary.
     */
    public Integer integer(String key) {
        return intOr(key, null);
    }

    /**
     * Integer access.
     *
     * @param def default if value not provided
     * @return Integer access, converting if necessary.
     */
    public Integer intOr(String key, Integer def) {
        return get(key, Integer.class, def);
    }

    /**
     * Double access.
     *
     * @return Integer access, converting if necessary.
     */
    public Double doub(String key) {
        return doubOr(key, null);
    }

    /**
     * Double access.
     *
     * @param def default if value not provided
     * @return Integer access, converting if necessary.
     */
    public Double doubOr(String key, Double def) {
        return get(key, Double.class, def);
    }

    /**
     * Boolean access.
     *
     * @return Boolean access, converting if necessary.
     */
    public Boolean bool(String key) {
        return boolOr(key, null);
    }

    /**
     * Boolean access.
     *
     * @param def default if value not provided
     * @return Boolean access, converting if necessary.
     */
    public Boolean boolOr(String key, Boolean def) {
        return get(key, Boolean.class, def);
    }

    /**
     * Check if mapping available for key
     *
     * @return true if mapping available for key
     */
    public boolean has(String key) {
        return raw.containsKey(key);
    }

    /**
     * Value access
     *
     * @return Value access
     */
    public Object get(String key) {
        return raw.get(key);
    }

    /**
     * Access value as a Yaml wrapper.
     *
     * <p>Wrappers are provided for Map and List
     *
     * @return Access as Yaml wrapper
     */
    public YamlObject<?> obj(String key) {
        return YamlObject.create(raw.get(key));
    }

    /**
     * Access value as a YamlMap
     *
     * @return Access as YamlMap
     */
    public YamlMap map(String key) {
        Object obj = raw.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof YamlMap) {
            return (YamlMap) obj;
        }
        return new YamlMap(obj);
    }

    /**
     * Access value as a YamlSeq
     *
     * @return Access as YamlSeq
     */
    public YamlSeq seq(String key) {
        Object obj = raw.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof YamlSeq) {
            return (YamlSeq) obj;
        }
        return new YamlSeq(obj);
    }

    /**
     * Access key by index, order provided by {@link #iterator()}.
     *
     * @param i index
     * @return key access by index
     */
    public String key(int i) {
        int index = 0;
        for (Iterator<String> iterator = iterator(); iterator.hasNext(); index++) {
            String key = iterator.next();
            if (index == i) {
                return key;
            }
        }
        throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + this.raw.size());
    }

    /** Iterate over keys. */
    @Override
    public Iterator<String> iterator() {
        return raw.keySet().iterator();
    }

    /**
     * Typed access
     *
     * @param def default if value not provided
     * @return Typed access, converting if necessary.
     */
    <T> T get(String key, Class<T> clazz, T def) {
        if (!raw.containsKey(key)) {
            return def;
        }
        Object obj = raw.get(key);
        if (obj == null) {
            return def; // consider returning null
        }
        return convert(obj, clazz);
    }
}
