/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

/**
 * Test {@link RegfuncPostgisDataStoreFactory}.
 * 
 * <p>
 * 
 * See {@link AbstractRegfuncPostgisOnlineTestCase} for a description of this test fixture.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegfuncPostgisDataStoreFactoryOnlineTest extends AbstractRegfuncPostgisOnlineTestCase {

    /**
     * Test that the factory can be used to build a {@link RegfuncPostgisDataStore}.
     * 
     * @throws Exception
     */
    public void testBuildDataStore() throws Exception {
        DataStore datastore = null;
        try {
            datastore = DataStoreFinder.getDataStore(getParams());
            assertEquals(RegfuncPostgisDataStore.class, datastore.getClass());
        } finally {
            if (datastore != null) {
                datastore.dispose();
            }
        }
    }

}
