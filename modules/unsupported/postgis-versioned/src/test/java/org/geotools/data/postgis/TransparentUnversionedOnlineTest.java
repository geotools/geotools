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
package org.geotools.data.postgis;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.feature.IllegalAttributeException;

public class TransparentUnversionedOnlineTest extends PostgisDataStoreAPIOnlineTest {

    public TransparentUnversionedOnlineTest(String test) {
        super(test);
    }

    protected void setupDbTables() throws Exception {
        super.setupDbTables();
        // make sure versioned metadata is not in the way
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_TABLESCHANGED, false);
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_VERSIONEDTABLES, false);
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_CHANGESETS, true);
    }

    public String getFixtureFile() {
        return "versioned.properties";
    }

    protected DataStore newDataStore() throws IOException {
        VersionedPostgisDataStore ds = new VersionedPostgisDataStore(pool, f.schema, getName(),
                PostgisDataStore.OPTIMIZE_SQL);
        ds.setWKBEnabled(true);
        return ds;
    }
    
    /**
     * Return true if the datastore is capable of computing the road bounds given a query
     * @return
     */
    protected boolean isEnvelopeComputingEnabled() {
        return true;
    }
    
    public void testOidFidMapper() throws IOException, IllegalAttributeException {
        // we have to override this one since versioned does not support oid mapper
    }
}
