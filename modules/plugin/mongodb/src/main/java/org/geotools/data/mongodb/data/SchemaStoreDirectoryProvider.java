/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb.data;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author ImranR
 *     <p>Schema Directory Provider
 *     <p>Scans for implementations of
 *     <p>org.geotools.data.mongodb.data.StoreDirectory implementations
 */
public final class SchemaStoreDirectoryProvider {

    private static List<SchemaStoreDirectory> directoriies;

    static {
        directoriies = SchemaStoreDirectoryProvider.initDirectories(SchemaStoreDirectory.class);
    }

    private static <T> List<T> initDirectories(Class<T> type) {

        ServiceLoader<T> loader = ServiceLoader.load(type);
        loader.reload();
        List<T> directories = new ArrayList<>();
        for (T aLoader : loader) {
            directories.add(aLoader);
        }
        return directories;
    }

    public static List<SchemaStoreDirectory> getDirectories() {
        return directoriies;
    }

    public static SchemaStoreDirectory getHighestPriority() {
        return directoriies.stream()
                .sorted((o1, o2) -> o2.getPriority() - o1.getPriority())
                .findFirst()
                .get();
    }

    public static SchemaStoreDirectory getLowestPriority() {
        return directoriies.stream()
                .sorted((o1, o2) -> o1.getPriority() - o2.getPriority())
                .findFirst()
                .get();
    }

    public static void addStoreDirectory(SchemaStoreDirectory directory) {
        directoriies.add(directory);
    }
}
