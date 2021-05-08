/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.util.Map;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

public class PostgisDontPreserveTopologyOnlineTest extends JDBCTestSupport {

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(PostgisNGDataStoreFactory.SIMPLIFICATION_METHOD.key, SimplificationMethod.FAST);
        return params;
    }

    @Override
    protected JDBCDelegatingTestSetup createTestSetup() {
        return new PostgisPreserveTopologyTestSetup(new PostGISTestSetup());
    }

    /** Test PostGIS simplified geometries without topology preservation (GEOT-6663) */
    public void testSimplificationDontPreserveTopology() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("simplify_polygon_topology"));

        if (fs.getSupportedHints().contains(Hints.GEOMETRY_SIMPLIFICATION) == false) return;

        Query query = new Query();
        Hints hints = new Hints(Hints.GEOMETRY_SIMPLIFICATION, 1.5);
        query.setHints(hints);

        SimpleFeatureCollection fColl = fs.getFeatures(query);
        Geometry simplified = null;
        try (SimpleFeatureIterator iterator = fColl.features()) {
            if (iterator.hasNext()) simplified = (Geometry) iterator.next().getDefaultGeometry();
        }

        assertNotNull("Simplified geometry is null", simplified);
        assertTrue(simplified instanceof Polygon);
        assertEquals(0, ((Polygon) simplified).getNumInteriorRing());
    }
}
