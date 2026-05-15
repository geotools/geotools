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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Assume;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class DuckDBMetadataOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new DuckDBDataStoreAPITestSetup();
    }

    @Test
    public void testBaseTableDiscoveryReturnsExpectedTypeNames() throws Exception {
        String tableName = tname("duckdb_metadata_table");
        try (Connection cx = setup.getDataSource().getConnection();
                Statement st = cx.createStatement()) {
            st.execute("DROP TABLE IF EXISTS \"" + tableName + "\"");
            st.execute("CREATE TABLE \"" + tableName + "\" (\"id\" INTEGER)");
        }

        try {
            assertTrue(Arrays.asList(dataStore.getTypeNames()).contains(tableName));
        } finally {
            try (Connection cx = setup.getDataSource().getConnection();
                    Statement st = cx.createStatement()) {
                st.execute("DROP TABLE IF EXISTS \"" + tableName + "\"");
            }
        }
    }

    @Test
    public void testNativeGeometryMetadataUsesGeometryBinding() throws Exception {
        SimpleFeatureType road = dataStore.getSchema(tname("road"));

        assertNotNull(road);
        assertNotNull(road.getGeometryDescriptor());
        assertEquals(aname("geom"), road.getGeometryDescriptor().getLocalName());
        assertEquals(Geometry.class, road.getGeometryDescriptor().getType().getBinding());
        assertNull(road.getGeometryDescriptor().getCoordinateReferenceSystem());
    }

    @Test
    public void testBoundsMetadataReflectsCurrentNullCrsBehavior() throws Exception {
        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("road")).getBounds();

        assertNotNull(bounds);
        assertEquals(1d, bounds.getMinX(), 0d);
        assertEquals(0d, bounds.getMinY(), 0d);
        assertEquals(5d, bounds.getMaxX(), 0d);
        assertEquals(4d, bounds.getMaxY(), 0d);
        // Current DuckDB limitation: JDBC metadata does not expose SRID/CRS for native GEOMETRY.
        // Update this assertion once SRID support is available through dialect metadata lookup.
        assertNull(bounds.getCoordinateReferenceSystem());
    }

    @Test
    public void testGeometrySridLookupReturnsNullWhenStSridFunctionUnavailable() throws Exception {
        boolean stSridAvailable;
        try (Connection cx = setup.getDataSource().getConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery(
                        "SELECT 1 FROM duckdb_functions() WHERE lower(function_name) = 'st_srid' LIMIT 1")) {
            if (!rs.next()) {
                stSridAvailable = false;
            } else {
                stSridAvailable = true;
            }
        }

        Assume.assumeFalse(
                "DuckDB now exposes ST_SRID; this test targets the current missing-function behavior", stSridAvailable);

        DuckDBDialect dialect = (DuckDBDialect) dataStore.getSQLDialect();
        try (Connection cx = setup.getDataSource().getConnection()) {
            Integer first = dialect.getGeometrySRID(null, tname("road"), aname("geom"), cx);
            Integer second = dialect.getGeometrySRID(null, tname("road"), aname("geom"), cx);

            assertNull(first);
            assertNull(second);
        }
    }
}
