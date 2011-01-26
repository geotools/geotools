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
package org.geotools.caching.grid.featurecache;

import java.io.IOException;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.caching.featurecache.AbstractFeatureCache;
import org.geotools.caching.featurecache.FeatureCacheException;
import org.geotools.caching.grid.spatialindex.GridSpatialIndex;
import org.geotools.caching.grid.spatialindex.store.DiskStorage;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.Storage;

import com.vividsolutions.jts.geom.Envelope;


public class DiskGridFeatureCacheTest extends GridFeatureCacheTest {
    public static Test suite() {
        return new TestSuite(DiskGridFeatureCacheTest.class);
    }

    @Override
    protected AbstractFeatureCache createInstance(int capacity)
        throws FeatureCacheException, IOException {
        Storage storage = DiskStorage.createInstance();
        this.cache = new GridFeatureCache(ds.getFeatureSource(dataset.getSchema().getName()),
                100, capacity, storage);

        return this.cache;
    }
    
    
    public void testRegister() { 
        GridSpatialIndex index = (GridSpatialIndex)((GridFeatureCache)cache).getIndex();
        double tilesize = index.getRootNode().getTileSize();
        Region r = (Region)index.getRootNode().getShape();
        
        //matches all 
        Envelope e = new Envelope(r.getLow(0), r.getHigh(0), r.getLow(1), r.getHigh(1));
        List<Envelope> matches = cache.match(e);
        assertEquals(1, matches.size());

        //matches 4 tiles 
        e = new Envelope(r.getLow(0), r.getLow(0) + 2 * tilesize-0.00001, r.getLow(1), r.getLow(1) + 2 *tilesize - 0.0001);
        matches = cache.match(e);
        assertEquals(4, matches.size());

        cache.remove(e);
        
        matches = cache.match(e);
        assertEquals(4, matches.size());        
    }
}
