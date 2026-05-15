/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.factory.Hints;
import org.junit.Assume;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

public class DuckDBPreserveTopologyOnlineTest extends JDBCTestSupport {

    @Override
    protected DuckDBPreserveTopologyTestSetup createTestSetup() {
        return new DuckDBPreserveTopologyTestSetup();
    }

    @Test
    public void testSimplificationPreserveTopology() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("simplify_polygon_topology"));

        Assume.assumeTrue(
                "Simplification hint not supported; skipping topology test",
                fs.getSupportedHints().contains(Hints.GEOMETRY_SIMPLIFICATION));

        Query query = new Query();
        query.setHints(new Hints(Hints.GEOMETRY_SIMPLIFICATION, 1.5));

        Geometry simplified = null;
        SimpleFeatureCollection collection = fs.getFeatures(query);
        try (SimpleFeatureIterator iterator = collection.features()) {
            if (iterator.hasNext()) {
                simplified = (Geometry) iterator.next().getDefaultGeometry();
            }
        }

        assertNotNull("Simplified geometry is null", simplified);
        assertTrue(simplified instanceof Polygon);
        assertEquals(1, ((Polygon) simplified).getNumInteriorRing());
    }
}
