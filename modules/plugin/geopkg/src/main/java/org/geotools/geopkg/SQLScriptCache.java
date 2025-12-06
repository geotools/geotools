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

package org.geotools.geopkg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/** Simple in-memory cache for GeoPackage own SQL setup/indexing scripts */
class SQLScriptCache {

    private ConcurrentHashMap<String, byte[]> cache = new ConcurrentHashMap<>();

    protected InputStream getScriptStream(String filename) {
        byte[] bytes = cache.get(filename);
        if (bytes == null) {
            try (InputStream is = GeoPackage.class.getResourceAsStream(filename)) {
                if (is == null) {
                    throw new IllegalArgumentException("Script not found: " + filename);
                }
                bytes = is.readAllBytes();
                byte[] existing = cache.putIfAbsent(filename, bytes);

                // Minor thread safety improvement, if existing is not null, it means another thread beat us.
                // Use the 'existing' value, as that is the one stored in the cache (the content will the same though
                // this is mostly to appease SpotBugs).
                if (existing != null) {
                    bytes = existing;
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to load script: " + filename, e);
            }
        }
        return new ByteArrayInputStream(bytes);
    }
}
