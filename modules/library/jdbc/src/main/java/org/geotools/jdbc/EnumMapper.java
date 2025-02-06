/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

/**
 * Helper class for enums mapped between integers and strings.
 *
 * <p><b>Deprecated in favor of {@link EnumMapping} due to <a
 * href="https://osgeo-org.atlassian.net/browse/GEOT-7715">GEOT-7715</a></b>.
 */
@Deprecated(forRemoval = true)
public class EnumMapper {

    Map<Integer, String> integerToString = new HashMap<>();
    Map<String, Integer> stringToInteger = new HashMap<>();

    /** Adds a key/value mapping */
    public void addMapping(Integer key, String value) {
        integerToString.put(key, value);
        stringToInteger.put(value, key);
    }

    /**
     * @param key
     * @return
     */
    public String fromInteger(Integer key) {
        return integerToString.get(key);
    }

    public Integer fromString(String key) {
        return stringToInteger.get(key);
    }

    public Map<Integer, String> getIntegerToString() {
        return Collections.unmodifiableMap(integerToString);
    }

    public Map<String, Integer> getStringToInteger() {
        return Collections.unmodifiableMap(stringToInteger);
    }

    public Integer fromString(String value, boolean matchCase) {
        if (!matchCase) {
            return stringToInteger.get(value);
        }

        for (Map.Entry<String, Integer> entry : stringToInteger.entrySet()) {
            if (value.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
