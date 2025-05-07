/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.jdbc.SequencedPrimaryKeyColumn;
import org.junit.Test;

public class OracleDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new OracleDataStoreAPITestSetup(new OracleTestSetup());
    }

    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // skip, does not work with Oracle
    }

    @Test
    public void testGetCommentsWithOracleOptionDefaultFalse() throws Exception {
        // Turning on comment retrieval in Oracle is set off by default in the
        // OracleNGDataStoreFactory, so this should work
        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("lake"));
        SimpleFeatureType simpleFeatureType = featureSource.getSchema();
        AttributeDescriptor attributeDescriptor = simpleFeatureType.getDescriptor("NAME");
        AttributeType attributeType = attributeDescriptor.getType();
        assertNull(attributeType.getDescription());
    }

    @Test
    public void testGetCommentsWithOracleOptionTrue() throws Exception {
        // explicitly turning on comment retrieval
        try (Connection conn = dataStore.getDataSource().getConnection()) {
            OracleDialect dialect = (OracleDialect) dataStore.getSQLDialect();
            dialect.setGetColumnRemarksEnabled(true);
            ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("lake"));
            SimpleFeatureType simpleFeatureType = featureSource.getSchema();
            AttributeDescriptor attributeDescriptor = simpleFeatureType.getDescriptor("NAME");
            AttributeType attributeType = attributeDescriptor.getType();
            assertEquals("This is a text column", attributeType.getDescription().toString());
            AttributeDescriptor attributeDescriptor2 = simpleFeatureType.getDescriptor("GEOM");
            AttributeType attributeType2 = attributeDescriptor2.getType();
            assertNull(attributeType2.getDescription()); // no comment on GEOM
            dialect.setRemarksReporting(conn, false);
        }
    }

    @Test
    public void testSequenceDetection() throws IOException {
        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("road"));
        assertNotNull(featureSource);
        assertTrue(featureSource instanceof JDBCFeatureStore);
        JDBCFeatureStore jdbc = (JDBCFeatureStore) featureSource;
        List<PrimaryKeyColumn> pks = jdbc.getPrimaryKey().getColumns();
        assertEquals(1, pks.size());
        PrimaryKeyColumn pk = pks.get(0);
        assertTrue(pk instanceof SequencedPrimaryKeyColumn);
        SequencedPrimaryKeyColumn seqPk = (SequencedPrimaryKeyColumn) pk;
        assertEquals("ROAD_FID_SEQ", seqPk.getSequenceName());
    }

    /**
     * Test if tables that should be hidden are absent in the list of type names.
     *
     * @throws IOException if any
     * @see org.geotools.data.oracle.OracleDialect#includeTable(String, String, java.sql.Connection)
     */
    @Test
    public void testHiddenTables() throws IOException {
        String[] typenames = dataStore.getTypeNames();
        for (String name : typenames) {
            assertFalse("Name should not end with 'MDXT_'", name.startsWith("MDXT_"));
            assertFalse("Name should not end with '$_BKTS'", name.endsWith("$_BKTS"));
            assertFalse("Name should not end with '$'", name.endsWith("$"));
        }
    }
}
