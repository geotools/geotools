/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.WFSServiceInfo;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.internal.v1_x.CubeWerxStrategy;
import org.geotools.data.wfs.internal.v1_x.IonicStrategy;
import org.geotools.data.wfs.internal.v1_x.StrictWFS_1_x_Strategy;
import org.geotools.ows.ServiceException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WFSClientTest {

    WFSConfig config;

    @Before
    public void setUp() throws Exception {
        Loggers.RESPONSES.setLevel(Level.FINEST);
        config = new WFSConfig();
    }

    @After
    public void tearDown() throws Exception {}

    private WFSClient testInit(String resource, String expectedVersion)
            throws IOException, ServiceException {

        WFSClient client = newClient(resource);
        WFSGetCapabilities capabilities = client.getCapabilities();
        // GEOT-5113 (should not throw NPE)
        if (!client.getRemoteTypeNames().isEmpty()) {
            client.supportsTransaction(client.getRemoteTypeNames().iterator().next());
        }
        Assert.assertEquals(expectedVersion, capabilities.getVersion());
        return client;
    }

    private WFSClient newClient(String resource) throws IOException, ServiceException {
        URL capabilitiesURL = WFSTestData.url(resource);
        HTTPClient httpClient = new SimpleHttpClient();

        WFSClient client = new WFSClient(capabilitiesURL, httpClient, config);
        return client;
    }

    private WFSClient checkStrategy(
            String resource, String expectedVersion, Class<? extends WFSStrategy> expectedStrategy)
            throws IOException, ServiceException {

        WFSClient client = testInit(resource, expectedVersion);

        WFSStrategy strategy = client.getStrategy();

        assertEquals(expectedStrategy, strategy.getClass());

        return client;
    }

    @Test
    public void testInit_1_0() throws Exception {
        testInit("GeoServer_2.2.x/1.0.0/GetCapabilities.xml", "1.0.0");
        testInit("PCIGeoMatics_unknown/1.0.0/GetCapabilities.xml", "1.0.0");
        testInit("MapServer_5.6.5/1.0.0/GetCapabilities.xml", "1.0.0");
        testInit("Ionic_unknown/1.0.0/GetCapabilities.xml", "1.0.0");
        testInit("Galdos_unknown/1.0.0/GetCapabilities.xml", "1.0.0");
        testInit("CubeWerx_4.12.6/1.0.0/GetCapabilities.xml", "1.0.0");
    }

    @Test
    public void testInit_1_1() throws Exception {
        testInit("GeoServer_1.7.x/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("GeoServer_2.0/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("CubeWerx_4.12.6/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("CubeWerx_4.7.5/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("CubeWerx_5.6.3/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("Deegree_unknown/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("Ionic_unknown/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("MapServer_5.6.5/1.1.0/GetCapabilities.xml", "1.1.0");
        testInit("CubeWerx_nsdi/1.1.0/GetCapabilities.xml", "1.1.0");
    }

    @Test
    @Ignore
    public void testInit_2_0() throws Exception {
        testInit("GeoServer_2.2.x/2.0.0/GetCapabilities.xml", "2.0.0");
        testInit("Degree_3.0/2.0.0/GetCapabilities.xml", "2.0.0");
    }

    @Test
    public void testAutoDetermineStrategy() throws Exception {
        Class<StrictWFS_1_x_Strategy> strict10 = StrictWFS_1_x_Strategy.class;

        checkStrategy("GeoServer_2.2.x/1.0.0/GetCapabilities.xml", "1.0.0", strict10);
        checkStrategy("PCIGeoMatics_unknown/1.0.0/GetCapabilities.xml", "1.0.0", strict10);
        checkStrategy("MapServer_5.6.5/1.0.0/GetCapabilities.xml", "1.0.0", strict10);
        checkStrategy("Ionic_unknown/1.0.0/GetCapabilities.xml", "1.0.0", IonicStrategy.class);
        checkStrategy("Galdos_unknown/1.0.0/GetCapabilities.xml", "1.0.0", strict10);
        checkStrategy("CubeWerx_4.12.6/1.0.0/GetCapabilities.xml", "1.0.0", CubeWerxStrategy.class);

        Class<StrictWFS_1_x_Strategy> strict11 = StrictWFS_1_x_Strategy.class;
        checkStrategy("GeoServer_1.7.x/1.1.0/GetCapabilities.xml", "1.1.0", strict11);
        checkStrategy("GeoServer_2.0/1.1.0/GetCapabilities.xml", "1.1.0", strict11);
        checkStrategy("CubeWerx_4.12.6/1.1.0/GetCapabilities.xml", "1.1.0", CubeWerxStrategy.class);
        checkStrategy("CubeWerx_4.7.5/1.1.0/GetCapabilities.xml", "1.1.0", CubeWerxStrategy.class);
        checkStrategy("CubeWerx_5.6.3/1.1.0/GetCapabilities.xml", "1.1.0", CubeWerxStrategy.class);
        checkStrategy("Deegree_unknown/1.1.0/GetCapabilities.xml", "1.1.0", strict11);
        checkStrategy("Ionic_unknown/1.1.0/GetCapabilities.xml", "1.1.0", IonicStrategy.class);
        checkStrategy("MapServer_5.6.5/1.1.0/GetCapabilities.xml", "1.1.0", strict11);
        checkStrategy("CubeWerx_nsdi/1.1.0/GetCapabilities.xml", "1.1.0", CubeWerxStrategy.class);
    }

    @Test
    public void testGetRemoteTypeNames() throws Exception {
        testGetRemoteTypeNames("PCIGeoMatics_unknown/1.0.0/GetCapabilities.xml", 5);
        testGetRemoteTypeNames("MapServer_5.6.5/1.0.0/GetCapabilities.xml", 2);
        testGetRemoteTypeNames("Ionic_unknown/1.0.0/GetCapabilities.xml", 7);
        testGetRemoteTypeNames("Galdos_unknown/1.0.0/GetCapabilities.xml", 48);
        testGetRemoteTypeNames("CubeWerx_4.12.6/1.0.0/GetCapabilities.xml", 15);

        testGetRemoteTypeNames("GeoServer_1.7.x/1.1.0/GetCapabilities.xml", 3);
        testGetRemoteTypeNames("CubeWerx_4.12.6/1.1.0/GetCapabilities.xml", 15);
        testGetRemoteTypeNames("CubeWerx_4.7.5/1.1.0/GetCapabilities.xml", 1);
        testGetRemoteTypeNames("CubeWerx_5.6.3/1.1.0/GetCapabilities.xml", 5);
        testGetRemoteTypeNames("Deegree_unknown/1.1.0/GetCapabilities.xml", 15);
        testGetRemoteTypeNames("Ionic_unknown/1.1.0/GetCapabilities.xml", 1);
        testGetRemoteTypeNames("MapServer_5.6.5/1.1.0/GetCapabilities.xml", 2);
        testGetRemoteTypeNames("CubeWerx_nsdi/1.1.0/GetCapabilities.xml", 14);
    }

    @Test
    public void testGetInfo() throws Exception {
        WFSClient client = newClient("GeoServer_1.7.x/1.1.0/GetCapabilities.xml");
        WFSServiceInfo info = client.getInfo();
        assertEquals("My GeoServer WFS", info.getTitle());
        assertEquals("1.1.0", info.getVersion());
        assertEquals(3, info.getKeywords().size());
        assertTrue(info.getKeywords().contains("GEOSERVER"));
        assertTrue(info.getKeywords().contains("WFS"));
        assertTrue(info.getKeywords().contains("WMS"));
        assertEquals(
                "http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd",
                info.getSchema().toString());
        assertEquals("http://localhost:8080/geoserver/wfs?", info.getSource().toString());
        assertTrue(
                info.getDescription().contains("This is a description of your Web Feature Server.")
                        && info.getDescription()
                                .contains(
                                        "The GeoServer is a full transactional Web Feature Server, you may wish to limit GeoServer to a Basic service")
                        && info.getDescription()
                                .contains(
                                        "level to prevent modificaiton of your geographic data."));
    }

    @Test
    public void testGetInfoArcGIS() throws Exception {
        WFSClient client = newClient("ArcGIS/GetCapabilities.xml");
        WFSServiceInfo info = client.getInfo();
        assertEquals("SLIP_Public_Services_Environment_WFS", info.getTitle());
        assertEquals("1.1.0", info.getVersion());
    }

    private void testGetRemoteTypeNames(String capabilitiesLocation, int typeCount)
            throws Exception {

        WFSClient client = newClient(capabilitiesLocation);
        Set<QName> remoteTypeNames = client.getRemoteTypeNames();
        assertNotNull(remoteTypeNames);
        assertEquals(capabilitiesLocation, typeCount, remoteTypeNames.size());
    }
}
