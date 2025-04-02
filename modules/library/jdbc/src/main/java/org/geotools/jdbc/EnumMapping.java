/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Helper class for enums. */
public class EnumMapping {

    Map<String, String> keyToValue = new HashMap<>();
    Map<String, String> valueToKey = new HashMap<>();

    /** Adds a key/value mapping */
    public void addMapping(String key, String value) {
        keyToValue.put(key, value);
        valueToKey.put(value, key);
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key the key to look up
     * @return the value mapped to the given key, or {@code null} if no mapping exists
     */
    public String fromKey(String key) {
        return keyToValue.get(key);
    }

    /**
     * Retrieves the key associated with the given value.
     *
     * @param value the value to look up
     * @return the key mapped to the given value, or {@code null} if no mapping exists
     */
    public String fromValue(String value) {
        return valueToKey.get(value);
    }

    /**
     * Retrieves the key associated with the given value.
     *
     * @param value the value to look up
     * @param matchCase {@code true} to perform a case-sensitive lookup, {@code false} otherwise
     * @return the key mapped to the given value, or {@code null} if no mapping exists
     */
    public String fromValue(String value, boolean matchCase) {
        if (matchCase) {
            return valueToKey.get(value);
        }

        return valueToKey.entrySet().stream()
                .filter(entry -> value.equalsIgnoreCase(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    /**
     * Returns an unmodifiable view of the key-to-value mappings.
     *
     * @return an unmodifiable {@code Map} containing the key-to-value mappings
     */
    public Map<String, String> keyToValueMap() {
        return Collections.unmodifiableMap(keyToValue);
    }

    /**
     * Returns an unmodifiable view of the value-to-key mappings.
     *
     * @return an unmodifiable {@code Map} containing the value-to-key mappings
     */
    public Map<String, String> valueToKeyMap() {
        return Collections.unmodifiableMap(valueToKey);
    }
}
