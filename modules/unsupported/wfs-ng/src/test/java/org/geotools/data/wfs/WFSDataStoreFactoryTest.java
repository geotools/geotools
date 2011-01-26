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
package org.geotools.data.wfs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.wfs.protocol.http.HTTPProtocol;
import org.geotools.data.wfs.v1_1_0.CubeWerxStrategy;
import org.geotools.data.wfs.v1_1_0.GeoServerStrategy;
import org.geotools.data.wfs.v1_1_0.IonicStrategy;
import org.geotools.data.wfs.v1_1_0.WFSStrategy;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

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

    @SuppressWarnings("nls")
    @Test
    public void testDetermineWFS1_1_0_Strategy() throws IOException {
        URL url;
        InputStream in;
        Document capabilitiesDoc;
        WFSStrategy strategy;

        url = TestData.url(this, "geoserver_capabilities_1_1_0.xml");
        in = url.openStream();
        capabilitiesDoc = WFSDataStoreFactory.parseCapabilities(in);
        strategy = WFSDataStoreFactory.determineCorrectStrategy(url, capabilitiesDoc);
        assertNotNull(strategy);
        assertEquals(GeoServerStrategy.class, strategy.getClass());

        url = TestData.url(this, "cubewerx_capabilities_1_1_0.xml");
        in = url.openStream();
        capabilitiesDoc = WFSDataStoreFactory.parseCapabilities(in);
        strategy = WFSDataStoreFactory.determineCorrectStrategy(url, capabilitiesDoc);
        assertNotNull(strategy);
        assertEquals(CubeWerxStrategy.class, strategy.getClass());

        url = TestData.url(this, "ionic_capabilities_1_1_0.xml");
        in = url.openStream();
        capabilitiesDoc = WFSDataStoreFactory.parseCapabilities(in);
        strategy = WFSDataStoreFactory.determineCorrectStrategy(url, capabilitiesDoc);
        assertNotNull(strategy);
        assertEquals(IonicStrategy.class, strategy.getClass());
    }

    @Test
    public void testCreateDataStoreWFS_1_1_0() throws IOException {
        String capabilitiesFile;
        capabilitiesFile = "geoserver_capabilities_1_1_0.xml";
        testCreateDataStore_WFS_1_1_0(capabilitiesFile);

        capabilitiesFile = "deegree_capabilities_1_1_0.xml";
        testCreateDataStore_WFS_1_1_0(capabilitiesFile);
    }

    private void testCreateDataStore_WFS_1_1_0(final String capabilitiesFile) throws IOException {
        // override caps loading not to set up an http connection at all but to
        // load the test file
        final WFSDataStoreFactory dsf = new WFSDataStoreFactory() {
            @Override
            byte[] loadCapabilities(final URL capabilitiesUrl, HTTPProtocol htp) throws IOException {
                InputStream in = capabilitiesUrl.openStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int aByte;
                while ((aByte = in.read()) != -1) {
                    out.write(aByte);
                }
                return out.toByteArray();
            }
        };
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        final URL capabilitiesUrl = TestData.getResource(this, capabilitiesFile);
        if (capabilitiesUrl == null) {
            throw new IllegalArgumentException(capabilitiesFile + " not found");
        }
        params.put(WFSDataStoreFactory.URL.key, capabilitiesUrl);

        WFSDataStore dataStore = dsf.createDataStore(params);
        assertTrue(dataStore instanceof WFS_1_1_0_DataStore);
    }

    @SuppressWarnings("nls")
    @Test
    public void testCreateCapabilities() throws MalformedURLException, UnsupportedEncodingException {
        final String parametrizedUrl = "https://excise.pyr.ec.gc.ca:8081/cgi-bin/mapserv.exe?map=/LocalApps/Mapsurfer/PYRWQMP.map&service=WFS&version=1.0.0&request=GetCapabilities";
        URL url = WFSDataStoreFactory.createGetCapabilitiesRequest(new URL(parametrizedUrl));
        assertNotNull(url);
        assertEquals("https", url.getProtocol());
        assertEquals("excise.pyr.ec.gc.ca", url.getHost());
        assertEquals(8081, url.getPort());
        assertEquals("/cgi-bin/mapserv.exe", url.getPath());

        String query = url.getQuery();
        assertNotNull(query);

        Map<String, String> kvpMap = new HashMap<String, String>();
        String[] kvpPairs = query.split("&");
        for (String kvp : kvpPairs) {
            assertTrue(kvp.indexOf('=') > 0);
            String[] split = kvp.split("=");
            String param = split[0];
            String value = split[1];
            value = URLDecoder.decode(value, "UTF-8");
            assertFalse(kvpMap.containsKey(param));
            kvpMap.put(param.toUpperCase(), value);
        }

        assertEquals("/LocalApps/Mapsurfer/PYRWQMP.map", kvpMap.get("MAP"));
        assertEquals("GetCapabilities", kvpMap.get("REQUEST"));
        assertEquals("WFS", kvpMap.get("SERVICE"));
        assertEquals("1.0.0", kvpMap.get("VERSION"));
    }
}
