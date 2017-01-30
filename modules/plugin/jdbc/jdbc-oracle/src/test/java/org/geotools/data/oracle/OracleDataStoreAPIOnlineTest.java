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

import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.jdbc.SequencedPrimaryKeyColumn;

import java.io.IOException;
import java.util.List;

/**
 * 
 *
 * @source $URL$
 */
public class OracleDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new OracleDataStoreAPITestSetup(new OracleTestSetup());
    }

    
    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // skip, does not work with Oracle
    }

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
}
