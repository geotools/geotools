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

import junit.framework.TestCase;

import org.geotools.caching.grid.spatialindex.GridSpatialIndex;
import org.geotools.caching.grid.spatialindex.store.MemoryStorage;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;
import org.geotools.caching.spatialindex.Storage;


public abstract class AbstractStorageTest extends TestCase {
    Storage store;
    TestNode n;
    RegionNodeIdentifier id;
    GridSpatialIndex grid;

    protected void setUp() {
        grid = new GridSpatialIndex(new Region(new double[] { 0, 0 }, new double[] { 1, 1 }), 10,
                MemoryStorage.createInstance(), 200);
        n = new TestNode(grid, new Region(new double[] { 0, 0 }, new double[] { 1, 1 }));
        id = new RegionNodeIdentifier(n);
        store = createStorage();
    }

    abstract Storage createStorage();

    public void testPut() {
        store.put(n);
        store.put(n);
    }

    public void testGet() {
        store.put(n);

        Node g = store.get(id);
        assertEquals(n.getIdentifier(), g.getIdentifier());
    }

    public void testRemove() {
        store.put(n);

        Node g = store.get(id);
        assertEquals(n.getIdentifier(), g.getIdentifier());
        store.remove(id);
        assertNull(store.get(id));
        store.put(n);
        g = store.get(id);
        assertEquals(n.getIdentifier(), g.getIdentifier());
    }
}
