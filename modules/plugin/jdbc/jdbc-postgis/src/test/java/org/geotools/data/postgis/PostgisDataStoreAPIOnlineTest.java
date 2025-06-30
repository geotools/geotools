/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.util.Version;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class PostgisDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {

    public PostgisDataStoreAPIOnlineTest() {
        this.forceLongitudeFirst = true;
    }

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new PostgisDataStoreAPITestSetup(new PostGISTestSetup());
    }

    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // postgis will lock indefinitely, won't throw an exception
    }

    /** Test PostGIS specific collapsed simplified geometries (GEOT-4737) */
    @Test
    public void testSimplificationPreserveCollapsed() throws Exception {
        Version version;
        try (Connection cx = dataStore.getDataSource().getConnection()) {
            PostGISDialect dialect = (PostGISDialect) dataStore.getSQLDialect();
            version = dialect.getVersion(cx);
        }

        // Would use Assume.assumeTrue here, but this class extends TestCase
        // and thus is run as a JUnit 3 test, which reports false assumptions
        // as failures
        if (version.compareTo(PostGISDialect.V_2_2_0) < 0) {
            return;
        }

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("simplify_polygon"));

        if (fs.getSupportedHints().contains(Hints.GEOMETRY_SIMPLIFICATION) == false) return;

        SimpleFeatureCollection fColl = fs.getFeatures();
        Geometry original = null;
        try (SimpleFeatureIterator iterator = fColl.features()) {
            if (iterator.hasNext()) {
                original = (Geometry) iterator.next().getDefaultGeometry();
            }
        }
        double width = original.getEnvelope().getEnvelopeInternal().getWidth();

        Query query = new Query();
        Hints hints = new Hints(Hints.GEOMETRY_SIMPLIFICATION, width * 2);
        query.setHints(hints);

        Geometry simplified = null;
        fColl = fs.getFeatures(query);
        try (SimpleFeatureIterator iterator = fColl.features()) {
            if (iterator.hasNext()) simplified = (Geometry) iterator.next().getDefaultGeometry();
        }

        // PostGIS 2.2+ should use ST_Simplify's preserveCollapsed flag
        assertNotNull("Simplified geometry is null", simplified);
        assertTrue(original.getNumPoints() >= simplified.getNumPoints());
    }

    @Test
    public void testGetComments() throws Exception {
        // PostgreSQL comment retrieval is always on, so this should work
        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("lake"));
        SimpleFeatureType simpleFeatureType = featureSource.getSchema();
        AttributeDescriptor attributeDescriptor = simpleFeatureType.getDescriptor("name");
        AttributeType attributeType = attributeDescriptor.getType();
        assertEquals("This is a text column", attributeType.getDescription().toString());
        AttributeDescriptor attributeDescriptor2 = simpleFeatureType.getDescriptor("geom");
        AttributeType attributeType2 = attributeDescriptor2.getType();
        assertNull(attributeType2.getDescription()); // no comment on GEOM
    }
}
