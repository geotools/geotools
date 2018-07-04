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
package org.geotools.caching.spatialindex.store;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.geotools.caching.grid.spatialindex.store.BufferedDiskStorage;
import org.geotools.caching.spatialindex.Storage;

/** @source $URL$ */
public class BufferedDiskStorageTest extends AbstractStorageTest {
    public static Test suite() {
        return new TestSuite(BufferedDiskStorageTest.class);
    }

    @Override
    Storage createStorage() {
        Storage storage = BufferedDiskStorage.createInstance();
        return storage;
    }
}
