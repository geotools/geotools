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

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.geotools.util.Converters;
import org.yaml.snakeyaml.DumperOptions;

/**
 * Base class for Yaml object wrappers.
 *
 * <p>Yaml is represented as atomic types (literals, expressions) and YamlObjects (for wrapping
 * lists and maps). The factory method {@link #create(Object)} will sort out the details. These
 * YamlObjects are used to stage data when parsing a Yaml document.
 */
public class YamlObject<T> {

    /**
     * Convert raw object to Yaml wrapper.
     *
     * <p>
     *
     * <ul>
     *   <li>Map wrapped as {@link YamlMap}
     *   <li>List wrapped as {@link YamlSeq}
     * </ul>
     *
     * Other data (Literals, Expressions) are considered atomic and do not provide a YamlObject
     * representation.
     *
     * @return Yaml object wrapper, or null if the provided raw object is null.
     */
    @SuppressWarnings("unchecked")
    public static <W> YamlObject<W> create(W raw) {
        if (raw == null) {
            return null;
        }

        if (raw instanceof YamlObject) {
            return (YamlObject<W>) raw;
        }

        if (raw instanceof Map) {
            return (YamlObject<W>) new YamlMap(raw);
        }

        if (raw instanceof List) {
            return (YamlObject<W>) new YamlSeq(raw);
        }
        throw new IllegalArgumentException("Unable to create yaml object from: " + raw);
    }

    /** underlying raw object */
    protected T raw;

    protected YamlObject(T raw) {
        this.raw = raw;
    }

    /** Casts this object to a {@link YamlMap}. */
    public YamlMap map() {
        if (this instanceof YamlMap) {
            return (YamlMap) this;
        }
        throw new IllegalArgumentException("Object " + this + " is not a mapping");
    }

    /** Casts this object to a {@link YamlSeq}. */
    public YamlSeq seq() {
        if (this instanceof YamlSeq) {
            return (YamlSeq) this;
        }
        throw new IllegalArgumentException("Object " + this + " is not a sequence");
    }

    static Object yamlize(Object o) {
        if (o instanceof Map) {
            o = new YamlMap(o);
        }
        if (o instanceof List) {
            o = new YamlSeq(o);
        }
        if (o.getClass().isArray()) {
            o = new YamlSeq(o);
        }
        return o;
    }

    /**
     * Raw value access via path.
     *
     * <p>Indended for quick value lookup during test cases.
     *
     * <ul>
     *   <li>raw("1"): first value in a sequence
     *   <li>raw("x"): value for "x" in a map
     *   <li>raw("rules/2/symbolizers/3"): third symbolizer in second rule
     *
     * @return raw value, or null if not available
     */
    public Object lookup(String path) {
        Object here = this;
        for (String key : path.split("/")) {
            // Step 1: cast to wrapper if required for further navigation
            here = yamlize(here);

            // Step 2: navigate into data structure
            int index;
            try {
                index = Integer.parseInt(key);
            } catch (NumberFormatException nan) {
                index = -1;
            }
            if (here instanceof YamlMap) {
                YamlMap map = (YamlMap) here;
                if (index != -1) {
                    String tempKey = map.key(index);
                    here = map.get(tempKey);
                } else if (map.has(key)) {
                    here = map.get(key);
                } else {
                    throw new NoSuchElementException("Key: " + key + ", Keys: " + map.raw.keySet());
                }
            } else if (here instanceof YamlSeq) {
                YamlSeq list = (YamlSeq) here;
                if (index != -1) {
                    here = list.get(index);
                } else {
                    throw new IndexOutOfBoundsException(
                            "Index: " + key + ", Size: " + list.raw.size());
                }
            } else {
                throw new NoSuchElementException(
                        "Key: " + key + ", For:" + here.getClass().getSimpleName());
            }
        }
        return here;
    }

    /** Returns the raw object. */
    public T raw() {
        return raw;
    }

    /**
     * See {@link #lookup}. Ensures that the result is wrapped as a YamlObject if it is a map, list,
     * or array.
     */
    public Object lookupY(String path) {
        return yamlize(lookup(path));
    }

    /** Converts an object to the specified class. */
    protected <C> C convert(Object obj, Class<C> clazz) {
        if (!clazz.isInstance(obj)) {
            C converted = Converters.convert(obj, clazz);
            if (converted != null) {
                obj = converted;
            }
        }

        try {
            return clazz.cast(obj);
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    String.format("Unable to retrieve %s as %s", obj, clazz.getSimpleName()), e);
        }
    }

    /**
     * Yaml representation.
     *
     * @return Yaml representation
     */
    @Override
    public String toString() {
        StringWriter w = new StringWriter();
        DumperOptions dumperOpts = new DumperOptions();
        dumperOpts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOpts.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        YamlUtil.getSafeYaml(dumperOpts).dump(raw, w);
        return w.toString();
    }
}
