/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sdmx;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** @source $URL$ */
public class SDMXDataStoreFactoryTest {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.SDMX");

    private SDMXDataStoreFactory dsf;
    private Map<String, Serializable> params;

    @Before
    public void setUp() throws Exception {
        dsf = new SDMXDataStoreFactory();
        params = new HashMap<String, Serializable>();
    }

    @After
    public void tearDown() throws Exception {
        dsf = null;
        params = null;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateNewDataStore() throws UnsupportedOperationException {
        (new SDMXDataStoreFactory()).createNewDataStore(params);
    }

    @Test
    public void testCanProcess() {
        // Nothing set
        assertFalse(dsf.canProcess(params));

        // Namespace set
        params.put(SDMXDataStoreFactory.NAMESPACE_PARAM.key, Helper.NAMESPACE);
        assertFalse(dsf.canProcess(params));

        // Provider set
        params.put(SDMXDataStoreFactory.PROVIDER_PARAM.key, Helper.PROVIDER_OLD);
        assertTrue(dsf.canProcess(params));

        // Username set
        params.put(SDMXDataStoreFactory.USER_PARAM.key, Helper.USER);
        assertTrue(dsf.canProcess(params));

        // Password set
        params.put(SDMXDataStoreFactory.PASSWORD_PARAM.key, Helper.PASSWORD);
        assertTrue(dsf.canProcess(params));
    }

    public void testCreateDataStore() throws IOException {
        SDMXDataStore ds = (SDMXDataStore) Helper.createSDMXTestDataStore();
        assertNotNull(ds);
    }

    public void testCreateDataStore2() throws IOException {
        SDMXDataStore ds = (SDMXDataStore) Helper.createSDMXTestDataStore2();
        assertNotNull(ds);
    }
}
