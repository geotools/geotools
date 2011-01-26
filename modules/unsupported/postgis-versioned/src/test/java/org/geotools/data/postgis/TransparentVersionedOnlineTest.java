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

public class TransparentVersionedOnlineTest extends
        PostgisDataStoreAPIOnlineTest {

    public TransparentVersionedOnlineTest(String test) {
        super(test);
    }

    protected void setupDbTables() throws Exception {
        super.setupDbTables();
        // make sure versioned metadata is not in the way
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_TABLESCHANGED,
                false);
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_VERSIONEDTABLES,
                false);
        SqlTestUtils
                .dropTable(pool, VersionedPostgisDataStore.TBL_CHANGESETS, true);
    }

    public String getFixtureFile() {
        return "versioned.properties";
    }

    protected DataStore newDataStore() throws IOException {
        VersionedPostgisDataStore ds = new VersionedPostgisDataStore(pool,
                f.schema, getName(), PostgisDataStore.OPTIMIZE_SQL);
        ds.setWKBEnabled(true);
        ds.setVersioned("road", true, "gimbo", "hallabaloola");
        ds.setVersioned("river", true, "gimbo", "hallabaloola");
        return ds;
    }

    /**
     * Return true if the datastore is capable of computing the road bounds
     * given a query
     * 
     * @return
     */
    protected boolean isEnvelopeComputingEnabled() {
        return true;
    }

    public void testOidFidMapper() throws IOException,
            IllegalAttributeException {
        // we have to override this one since versioned does not support oid
        // mapper
    }

    public void testLockFeatures() throws IOException {
        // disable this test, we don't support locking at the moment
    }

    public void testUnLockFeatures() throws IOException {
        // disable this test, we don't support locking at the moment
    }

    public void testLockFeatureInteraction() throws IOException {
        // disable this test, we don't support locking at the moment
    }

    public void testGetFeatureLockingExpire() throws Exception {
        // disable this test, we don't support locking at the moment
    }
    
    public void testGetFeatureWriterTransaction() throws Exception {
        // disable this test, the datastore does not support concurrent transactions
        // from the same thread, one will lock the changesets and the second will
        // wait indefinitely. Mind, this does not prevent really concurrent transactions 
        // from executing, it prevents the single thread to mix two transactions
    }
    
    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // see above
    }
}
