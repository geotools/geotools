/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStoreFinder;

public class VersionedDataStoreStressTest extends
        AbstractVersionedPostgisDataTestCase {

    Map remote;

    private PostgisTests.Fixture f;

    public VersionedDataStoreStressTest(String name) {
        super(name);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        f = PostgisTests.newFixture("versioned.properties");
        remote = new HashMap();
        remote.put("dbtype", "postgis-versioned");
        remote.put("charset", "");
        remote.put("host", f.host);
        remote.put("port", f.port);
        remote.put("database", f.database);
        remote.put("user", f.user);
        remote.put("passwd", f.password);
        remote.put("namespace", f.namespace);

        super.setUp();
    }

    /**
     * Versioned data store used to leak connections pretty badly due to a buglet in JCBDUtils,
     * let's make sure this does not happen again
     * 
     * @throws IOException
     */
    public void testMetadataStress() throws IOException {
        for (int i = 0; i < 20; i++) {
            VersionedPostgisDataStore ds = (VersionedPostgisDataStore) DataStoreFinder
                    .getDataStore(remote);

            String[] typeNames = new String[] {"road", "river", "lake"};
            for (int j = 0; j < typeNames.length; j++) {
                String typeName = typeNames[j];
                ds.getSchema(typeName);
                ds.getFeatureSource(typeName).getBounds();
                boolean versioned = ds.isVersioned(typeName);
                try {
                    ds.setVersioned(typeName, !versioned, "mad cow",
                            "I like to change versioning status...");
                } catch (IOException e) {
                    // some feature types cannot be versioned, np
                }
            }
            ds.dispose();
        }
    }
    

}
