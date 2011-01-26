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

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.caching.grid.spatialindex.store.DiskStorage;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Storage;


public class DiskStorageTest extends AbstractStorageTest {
    public static Test suite() {
        return new TestSuite(DiskStorageTest.class);
    }

    @Override
    Storage createStorage() {
        Storage storage = DiskStorage.createInstance();

        return storage;
    }

    public void testRecreation() {
        testPut();
        testRemove();

        Properties pset = this.store.getPropertySet();
        this.store.flush();
        this.store = DiskStorage.createInstance(pset);

        Node g = store.get(id);
        assertEquals(n.getIdentifier(), g.getIdentifier());
        store.remove(id);
        g = store.get(id);
        assertNull(g);
    }
}
