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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.caching.grid.spatialindex.store.MemoryStorage;
import org.geotools.caching.spatialindex.AbstractSpatialIndex;
import org.geotools.caching.spatialindex.AbstractSpatialIndexTest;
import org.geotools.caching.spatialindex.Region;


public class GridTest extends AbstractSpatialIndexTest {
    GridSpatialIndex index;

    public static Test suite() {
        return new TestSuite(GridTest.class);
    }

    protected AbstractSpatialIndex createIndex() {
        index = new GridSpatialIndex(new Region(universe), 100, MemoryStorage.createInstance(), 2000);

        return index;
    }

    public void testInsertion() {
        super.testInsertion();
        
        //************************************
        //This section tests that duplicate items are added to
        //the grid correctly.
        //********************************
        String data = "My Feature";
        Region r = new Region(universe);
        
        long datacount = index.getStatistics().getNumberOfData();
        index.insertData(data, r);
        assertEquals(datacount+1, index.getStatistics().getNumberOfData());
        
        //lets try to insert the same data again; this should not add anything
        index.insertData(data, r);	
        assertEquals(datacount+1, index.getStatistics().getNumberOfData());
  
        //different data, same region
        index.insertData("New Data", r);
        assertEquals(datacount+2, index.getStatistics().getNumberOfData());
        
    }
    
}
