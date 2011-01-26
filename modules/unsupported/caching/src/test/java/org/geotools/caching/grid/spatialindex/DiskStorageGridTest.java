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
package org.geotools.caching.grid.spatialindex;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.caching.grid.spatialindex.store.BufferedDiskStorage;
import org.geotools.caching.spatialindex.AbstractSpatialIndex;
import org.geotools.caching.spatialindex.AbstractSpatialIndexTest;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.Storage;


//import org.geotools.caching.spatialindex.store.DiskStorage;
public class DiskStorageGridTest extends AbstractSpatialIndexTest {
    GridSpatialIndex index;

    public static Test suite() {
        return new TestSuite(DiskStorageGridTest.class);
    }

    @Override
    protected AbstractSpatialIndex createIndex() {
        Storage storage = BufferedDiskStorage.createInstance();
        //        Storage storage = DiskStorage.createInstance();
        index = new GridSpatialIndex(new Region(universe), 100, storage, 2000);

        return index;
    }

    public void testInsertion() {
        super.testInsertion();
    }

    public void testWarmStart() throws IOException {
        Properties pset = index.getIndexProperties();
        pset.store(System.out, "Grid property set");
        index.flush();
        
        index = (GridSpatialIndex) GridSpatialIndex.createInstance(pset);
        super.index = index;
        //testIntersectionQuery();
        
        //check the number of nodes
        HarvestingVisitor v = new HarvestingVisitor();
        Region query = new Region(new double[] { 0, 0 }, new double[] { 1, 1 });
        index.intersectionQuery(query, v);
        assertEquals(index.getStatistics().getNumberOfNodes(), v.visited_nodes);
        
        //the actual data count will be 0
        assertEquals(0,v.harvest.size());
    }
}
