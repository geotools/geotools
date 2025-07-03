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
package org.geotools.data.singlestore;

import static org.junit.Assert.assertEquals;

import org.geotools.api.data.Query;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCSpatialFiltersOnlineTest;
import org.junit.Test;

public class SingleStoreSpatialFiltersOnlineTest extends JDBCSpatialFiltersOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new SingleStoreDataStoreAPITestSetup();
    }

    @Test
    public void testBboxLargerThanWorld() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match all features (but without countermeasures against invalid coordinates, it matched nothing)
        BBOX bbox = ff.bbox(aname("geom"), -200, -100, 200, 100, "EPSG:4326");
        Query q = new Query(tname("road"), bbox);
        q.setFilter(bbox);
        assertEquals(3, dataStore.getFeatureSource(tname("road")).getCount(q));
    }

    @Test
    public void testBboxLargerThanEmisphere() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match all features (but without countermeasures against boxes larger than emispheres, it matched
        // nothing)
        BBOX bbox = ff.bbox(aname("geom"), -160, -40, 160, 40, "EPSG:4326");
        Query q = new Query(tname("road"), bbox);
        q.setFilter(bbox);
        assertEquals(3, dataStore.getFeatureSource(tname("road")).getCount(q));
    }

    @Test
    public void testBBoxDensification() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match all features (but without countermeasures against long lines, it matched nothing)
        BBOX bbox = ff.bbox(aname("geom"), -50, 0, 50, 50, "EPSG:4326");
        Query q = new Query(tname("road"), bbox);
        q.setFilter(bbox);
        assertEquals(3, dataStore.getFeatureSource(tname("road")).getCount(q));
    }
}
