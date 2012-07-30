/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.impl;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.wfs.internal.Versions;
import org.geotools.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @source $URL$
 */
public class WFSDataStoreFactoryTest {

    private WFSDataStoreFactory dsf;

    private Map<String, Serializable> params;

    @Before
    public void setUp() throws Exception {
        dsf = new WFSDataStoreFactory();
        params = new HashMap<String, Serializable>();
    }

    @After
    public void tearDown() throws Exception {
        dsf = null;
        params = null;
    }

    @Test
    public void testCanProcess() {
        // URL not set
        assertFalse(dsf.canProcess(params));

        params.put(WFSDataStoreFactory.URL.key,
                "http://someserver.example.org/wfs?request=GetCapabilities");
        assertTrue(dsf.canProcess(params));

        params.put(WFSDataStoreFactory.USERNAME.key, "groldan");
        assertFalse(dsf.canProcess(params));

        params.put(WFSDataStoreFactory.PASSWORD.key, "secret");
        assertTrue(dsf.canProcess(params));
    }

    @Test
    public void testCreateDataStore() throws IOException {
        testCreateDataStore("CubeWerx_4.12.6/1.0.0/GetCapabilities.xml", Versions.v1_0_0);
        testCreateDataStore("CubeWerx_4.12.6/1.1.0/GetCapabilities.xml", Versions.v1_1_0);
        testCreateDataStore("CubeWerx_4.7.5/1.1.0/GetCapabilities.xml", Versions.v1_1_0);
        testCreateDataStore("CubeWerx_5.6.3/1.1.0/GetCapabilities.xml", Versions.v1_1_0);
        testCreateDataStore("CubeWerx_nsdi/1.1.0/GetCapabilities.xml", Versions.v1_1_0);
        testCreateDataStore("Deegree_unknown/1.1.0/GetCapabilities.xml", Versions.v1_1_0);

        // not yet testCreateDataStore("Deegree_3.0/2.0.0/GetCapabilities.xml", Versions.v2_0_0);
        testCreateDataStore("Galdos_unknown/1.0.0/GetCapabilities.xml", Versions.v1_0_0);

        testCreateDataStore("GeoServer_1.7.x/1.1.0/GetCapabilities.xml", Versions.v1_1_0);
        testCreateDataStore("GeoServer_2.0/1.1.0/GetCapabilities.xml", Versions.v1_1_0);
        testCreateDataStore("GeoServer_2.2.x/1.0.0/GetCapabilities.xml", Versions.v1_0_0);
        // not yet testCreateDataStore("GeoServer_2.2.x/2.0.0/GetCapabilities.xml",
        // Versions.v2_0_0);

        testCreateDataStore("Ionic_unknown/1.0.0/GetCapabilities.xml", Versions.v1_0_0);
        testCreateDataStore("Ionic_unknown/1.1.0/GetCapabilities.xml", Versions.v1_1_0);

        testCreateDataStore("MapServer_4.2-beta1/1.0.0/GetCapabilities.xml", Versions.v1_0_0);
        testCreateDataStore("MapServer_5.6.5/1.0.0/GetCapabilities.xml", Versions.v1_0_0);
        testCreateDataStore("MapServer_5.6.5/1.1.0/GetCapabilities.xml", Versions.v1_1_0);

        testCreateDataStore("PCIGeoMatics_unknown/1.0.0/GetCapabilities.xml", Versions.v1_0_0);
    }

    /**
     * @param capabilitiesFile
     *            the name of the GetCapabilities document under
     *            {@code /org/geotools/data/wfs/impl/test-data}
     */
    private WFSContentDataStore testCreateDataStore(final String capabilitiesFile,
            final Version expectedVersion) throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("TESTING", Boolean.TRUE);

        final URL capabilitiesUrl = getClass().getResource(
                "/org/geotools/data/wfs/impl/test-data/" + capabilitiesFile);
        if (capabilitiesUrl == null) {
            throw new IllegalArgumentException(capabilitiesFile + " not found");
        }
        params.put(WFSDataStoreFactory.URL.key, capabilitiesUrl);

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        assertNotNull(dataStore);
        assertTrue(dataStore instanceof WFSContentDataStore);

        WFSContentDataStore wfsDs = (WFSContentDataStore) dataStore;

        assertEquals(expectedVersion.toString(), wfsDs.getInfo().getVersion());

        return wfsDs;
    }

    @Test
    public void testCreateNewDataStore() throws IOException {
        try {
            dsf.createNewDataStore(params);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
}
