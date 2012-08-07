package org.geotools.data.wfs.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.namespace.QName;

import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.impl.WFSTestData;
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
    public void tearDown() throws Exception {
    }

    private WFSClient testInit(String resource, String expectedVersion) throws IOException,
            ServiceException {

        WFSClient client = newClient(resource);
        WFSGetCapabilities capabilities = client.getCapabilities();

        Assert.assertEquals(expectedVersion, capabilities.getVersion());
        return client;
    }

    private WFSClient newClient(String resource) throws IOException, ServiceException {
        URL capabilitiesURL = WFSTestData.url(resource);
        HTTPClient httpClient = new SimpleHttpClient();

        WFSClient client = new WFSClient(capabilitiesURL, httpClient, config);
        return client;
    }

    private WFSClient checkStrategy(String resource, String expectedVersion,
            Class<? extends WFSStrategy> expectedStrategy) throws IOException, ServiceException {

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

    private void testGetRemoteTypeNames(String capabilitiesLocation, int typeCount)
            throws Exception {

        WFSClient client = newClient(capabilitiesLocation);
        Set<QName> remoteTypeNames = client.getRemoteTypeNames();
        assertNotNull(remoteTypeNames);
        assertEquals(capabilitiesLocation, typeCount, remoteTypeNames.size());

    }
}
