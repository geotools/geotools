/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.grid.spatialindex.store;

import java.util.Properties;

import org.geotools.caching.spatialindex.Storage;


public class StorageFactory {
    private static StorageFactory instance = null;

    public static StorageFactory getInstance() {
        if (instance == null) {
            instance = new StorageFactory();
        }

        return instance;
    }

    public Storage createStorage(Properties pset) {
        String type = pset.getProperty(Storage.STORAGE_TYPE_PROPERTY);

        if (type == null) {
            throw new IllegalArgumentException("Storage : Invalid property set.");
        }

        if (type.equals(MemoryStorage.class.getCanonicalName())) {
            return MemoryStorage.createInstance(pset);
        }

        if (type.equals(DiskStorage.class.getCanonicalName())) {
            return DiskStorage.createInstance(pset);
        }

        if (type.equals(BufferedDiskStorage.class.getCanonicalName())) {
            return BufferedDiskStorage.createInstance(pset);
        }

        return MemoryStorage.createInstance();
    }
}
