/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.test.imagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

/** Test helper for parsing ImageN {@code META-INF/registryFile.imagen} resources. */
public final class RegistryFileTestSupport {

    private RegistryFileTestSupport() {}

    /**
     * Parses registry file entries and returns the class names in declaration order.
     *
     * @param stream registry file input stream
     * @return parsed class names
     * @throws IOException if reading fails
     */
    public static Set<String> parseRegistryClasses(InputStream stream) throws IOException {
        LinkedHashSet<String> classes = new LinkedHashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                String[] parts = trimmed.split("\\s+");
                if (parts.length >= 2 && isRegistryKeyword(parts[0])) {
                    classes.add(parts[1]);
                }
            }
        }
        return classes;
    }

    /** Returns {@code true} when the token is a recognized ImageN registry directive. */
    public static boolean isRegistryKeyword(String keyword) {
        return "descriptor".equals(keyword)
                || "odesc".equals(keyword)
                || "rendered".equals(keyword)
                || "renderable".equals(keyword)
                || "collection".equals(keyword)
                || "tileDecoder".equals(keyword)
                || "tileEncoder".equals(keyword)
                || "registryMode".equals(keyword);
    }
}
