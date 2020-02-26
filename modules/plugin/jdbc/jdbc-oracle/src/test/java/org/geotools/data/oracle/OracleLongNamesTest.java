/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import static org.geotools.filter.text.cql2.CQL.toFilter;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;

/** This is an Oracle specific test for Oracle 12c longer names support */
public class OracleLongNamesTest extends JDBCTestSupport {

    static final Logger LOGGER = Logging.getLogger(OracleLongNamesTest.class);

    public static final String TABLE_NAME = "THIS_IS_A_TABLE_WITH_A_VERY_LONG_NAME";
    public static final String GEOM_NAME = "GEOMETRY_ATTRIBUTE_WITH_A_VERY_LONG_NAME";
    public static final String INT_NAME = "INT_ATTRIBUTE_WITH_A_VERY_LONG_NAME";
    private boolean atLeast12;

    @Override
    protected OracleLongNameTestSetup createTestSetup() {
        return new OracleLongNameTestSetup(new OracleTestSetup());
    }

    /** This test makes sne */
    @Override
    protected boolean isOnline() throws Exception {
        if (!super.isOnline()) {
            return false;
        }

        // We need to see if we have the citext extension and if we are authorized to
        // create it, to make things easy, we just directly try to do so, if this fails,
        // the test will be skipped
        JDBCTestSetup setup = createTestSetup();
        setup.setFixture(fixture);

        try (Connection cx = setup.getDataSource().getConnection()) {
            atLeast12 = cx.getMetaData().getDatabaseMajorVersion() >= 12;
            if (!atLeast12) {
                LOGGER.warning("Skipping long names test as database version is not at least 12");
            }
            return atLeast12;
        } catch (Throwable t) {
            return false;
        } finally {
            try {
                setup.tearDown();
            } catch (Exception e) {
                // System.out.println("Error occurred tearing down the test setup");
            }
        }
    }

    public void testSchema() throws IOException {
        if (!atLeast12) {
            return;
        }

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname(TABLE_NAME));
        assertEquals(GEOM_NAME, fs.getSchema().getGeometryDescriptor().getLocalName());
        assertNotNull(fs.getSchema().getDescriptor(INT_NAME));
    }

    public void testAlphanumericQuery() throws IOException, CQLException {
        if (!atLeast12) {
            return;
        }

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname(TABLE_NAME));
        Query q = new Query(TABLE_NAME);
        q.setFilter(toFilter(INT_NAME + " = 123"));
        assertEquals(1, fs.getCount(q));
    }

    public void testSpatialQuery() throws IOException, CQLException {
        if (!atLeast12) {
            return;
        }

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname(TABLE_NAME));
        Query q = new Query(TABLE_NAME);
        q.setFilter(toFilter("BBOX(" + GEOM_NAME + ",-10,-10,10,10)"));
        assertEquals(1, fs.getCount(q));
        q.setFilter(toFilter("BBOX(" + GEOM_NAME + ",10,10,20,20)"));
        assertEquals(0, fs.getCount(q));
    }

    public void testModify() throws IOException, CQLException {
        if (!atLeast12) {
            return;
        }

        SimpleFeatureStore fs = (SimpleFeatureStore) dataStore.getFeatureSource(tname(TABLE_NAME));
        fs.modifyFeatures(INT_NAME, 345, toFilter(INT_NAME + " = 123"));
        assertEquals(1, fs.getCount(new Query(TABLE_NAME, toFilter(INT_NAME + " = 345"))));
    }

    public void testInsert() throws IOException, CQLException, ParseException {
        if (!atLeast12) {
            return;
        }

        SimpleFeatureStore fs = (SimpleFeatureStore) dataStore.getFeatureSource(tname(TABLE_NAME));
        final Geometry point = new WKTReader().read("POINT( 2 2)");
        final SimpleFeature feature =
                SimpleFeatureBuilder.build(fs.getSchema(), new Object[] {point, 789}, null);
        fs.addFeatures(DataUtilities.collection(feature));
        assertEquals(
                1,
                fs.getCount(new Query(TABLE_NAME, toFilter("BBOX(" + GEOM_NAME + ",1,1,10,10)"))));
    }
}
