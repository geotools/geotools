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
package org.geotools.data.shapefile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;

/**
 * 
 *
 * @source $URL$
 * @version $Id$
 * @author ian
 */
public class ServiceTest extends TestCaseSupport {

    final String TEST_FILE = "shapes/statepop.shp";

    public ServiceTest(String testName) throws IOException {
        super(testName);
    }

    /**
     * Make sure that the loading mechanism is working properly.
     */
    public void testIsAvailable() {
        Iterator list = DataStoreFinder.getAvailableDataStores();
        boolean found = false;
        while (list.hasNext()) {
            DataStoreFactorySpi fac = (DataStoreFactorySpi) list.next();
            if (fac instanceof ShapefileDataStoreFactory) {
                found = true;
                assertNotNull(fac.getDescription());
                break;
            }
        }
        assertTrue("ShapefileDataSourceFactory not registered", found);
    }

    /**
     * Ensure that we can create a DataStore using url OR string url.
     */
    public void testShapefileDataStore() throws Exception {
        HashMap params = new HashMap();
        params.put("url", TestData.url(TEST_FILE));
        DataStore ds = DataStoreFinder.getDataStore(params);
        assertNotNull(ds);
        params.put("url", TestData.url(TEST_FILE).toString());
        assertNotNull(ds);
        ds.dispose();
    }

    public void testBadURL() {
        HashMap params = new HashMap();
        params.put("url", "aaa://bbb.ccc");
        try {
            ShapefileDataStoreFactory f = new ShapefileDataStoreFactory();
            f.createDataStore(params);
            fail("did not throw error");
        } catch (java.io.IOException ioe) {
            // this is actually good
        }

    }

}
