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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.ScreenMap;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.factory.Hints;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

public class DuckDBDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {
    private static final String TRANSACTION_ISOLATION_DIFFERS =
            "DuckDB detects write-write conflicts and does not match the generic JDBC transaction isolation expectation";
    private static final String WRITER_CONCURRENCY_DIFFERS =
            "DuckDB reports transaction conflicts for concurrent writers instead of FeatureLockException";
    private static final String DISTANCE_HINT_NOT_SUPPORTED =
            "DuckDB does not currently support Hints.GEOMETRY_DISTANCE in SQL generalization";

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new DuckDBDataStoreAPITestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(AbstractDuckDBDataStoreFactory.READ_ONLY.key, Boolean.FALSE);
        return params;
    }

    @Override
    @Test
    public void testCreateSchema() throws Exception {
        super.testCreateSchema();
    }

    @Override
    @Test
    public void testGetFeatureSourceRoad() throws Exception {
        SimpleFeatureSource road = dataStore.getFeatureSource(tname("road"));

        assertFeatureTypesEqual(td.roadType, road.getSchema());
        assertSame(dataStore, road.getDataStore());

        int count = road.getCount(Query.ALL);
        assertTrue(count == 3 || count == -1);

        assertBounds(road.getBounds(Query.ALL), 1, 0, 5, 4);

        SimpleFeatureCollection all = road.getFeatures();
        assertEquals(3, all.size());
        assertBounds(all.getBounds(), 1, 0, 5, 4);
    }

    @Override
    @Test
    public void testGetFeatureSourceRiver() throws IOException, IllegalAttributeException {
        SimpleFeatureSource river = dataStore.getFeatureSource(tname("river"));

        assertFeatureTypesEqual(td.riverType, river.getSchema());
        assertSame(dataStore, river.getDataStore());

        SimpleFeatureCollection all = river.getFeatures();
        assertEquals(2, all.size());
        assertBounds(all.getBounds(), 4, 3, 13, 10);
    }

    @Override
    @Test
    @Ignore(TRANSACTION_ISOLATION_DIFFERS)
    public void testTransactionIsolation() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(WRITER_CONCURRENCY_DIFFERS)
    public void testGetFeatureWriterConcurrency() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Test
    @Ignore(DISTANCE_HINT_NOT_SUPPORTED)
    public void testDistanceSimplification() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Test
    public void testScreenMap() throws Exception {
        SimpleFeatureSource road = dataStore.getFeatureSource(tname("road"));
        assertTrue(road.getSupportedHints().contains(Hints.SCREENMAP));

        FilterFactory factory = dataStore.getFilterFactory();
        Query q = new Query(tname("road"), factory.id(Collections.singleton(factory.featureId("road.0"))));
        ScreenMap screenMap =
                new ScreenMap(0, 0, 10, 10, new AffineTransform2D(AffineTransform.getScaleInstance(0.1, 0.1)));
        screenMap.setSpans(10, 10);
        q.setHints(new Hints(Hints.SCREENMAP, screenMap));

        try (SimpleFeatureIterator it = road.getFeatures(q).features()) {
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            LineString ls = (LineString) f.getDefaultGeometry();
            assertEquals(2, ls.getNumPoints());
            assertEquals(new Coordinate(-2, -3.5), ls.getStartPoint().getCoordinate());
            assertEquals(new Coordinate(8, 6.5), ls.getEndPoint().getCoordinate());
        }

        assertTrue(screenMap.get(0, 0));
    }

    private void assertBounds(ReferencedEnvelope bounds, double minX, double minY, double maxX, double maxY) {
        assertNotNull(bounds);
        assertEquals(minX, bounds.getMinX(), 0d);
        assertEquals(minY, bounds.getMinY(), 0d);
        assertEquals(maxX, bounds.getMaxX(), 0d);
        assertEquals(maxY, bounds.getMaxY(), 0d);
    }
}
