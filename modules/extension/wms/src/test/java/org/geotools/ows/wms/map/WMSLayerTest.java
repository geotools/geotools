/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wms.WMSUtils;
import org.geotools.ows.wms.WebMapServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** @author ian */
public class WMSLayerTest {
    private WebMapServer server;

    Map<String, String> parseParams(String query) {

        List<org.apache.http.NameValuePair> params =
                URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
        Map<String, String> result = new HashMap<>();
        for (Object param : params) {
            NameValuePair pair = (NameValuePair) param;
            result.put(pair.getName().toUpperCase(), pair.getValue());
        }
        return result;
    };
    /** @throws java.lang.Exception */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    /** @throws java.lang.Exception */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {
        HTTPClient client =
                new MockHttpClient() {

                    @Override
                    public HTTPResponse get(URL url) throws IOException {
                        if (url.getQuery().contains("GetCapabilities")) {
                            Map<String, String> params = parseParams(url.getQuery());
                            URL caps = null;
                            if ("1.3.0".equals(params.get("VERSION"))) {
                                caps = WMSCoverageReaderTest.class.getResource("caps130.xml");
                            } else if ("1.1.0".equals(params.get("VERSION"))) {
                                caps = WMSCoverageReaderTest.class.getResource("caps110.xml");
                            }
                            return new MockHttpResponse(caps, "text/xml");
                        } else {
                            throw new IllegalArgumentException(
                                    "Don't know how to handle a get request over "
                                            + url.toExternalForm());
                        }
                    }
                };
        // setup the reader
        server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
    }

    /** @throws java.lang.Exception */
    @After
    public void tearDown() throws Exception {}

    /** Test method for {@link WMSLayer#WMSLayer(WebMapServer, Layer)}. */
    @Test
    public void testWMSLayer() {
        Layer[] wmsLayers = WMSUtils.getNamedLayers(server.getCapabilities());

        WMSLayer l = new WMSLayer(server, wmsLayers[0]);
        assertNotNull(l);
        List<StyleImpl> styles = wmsLayers[0].getStyles();
        assertEquals(4, styles.size());
        l = new WMSLayer(server, wmsLayers[0], styles.get(1).getName());

        WMSLayer l2 = new WMSLayer(server, wmsLayers[0], styles.get(3).getName());
        assertNotNull(l2);
    }

    /** Test method for {@link WMSLayer#WMSLayer(WebMapServer, Layer)}. */
    @Test
    public void testPreferedFormatWMSLayer() {
        Layer[] wmsLayers = WMSUtils.getNamedLayers(server.getCapabilities());

        WMSLayer l = new WMSLayer(server, wmsLayers[0], "", "image/jpg");
        // verify reader is using correct
        assertTrue(l.getReader().format.equalsIgnoreCase("image/jpg"));

        WMSLayer l2 = new WMSLayer(server, wmsLayers[0], "", "image/unknown");
        // verify backward compatability
        assertTrue(l2.getReader().format.equalsIgnoreCase("image/png"));
    }

    /**
     * Test method for {@link WMSLayer#WMSLayer(WebMapServer, Layer)}. Test the sceanrio where
     * remote WMS server only supports one format which is not any of the PNG variants
     */
    @Test
    public void testPreferedFormatWMSLayerJPEGOnly() throws Exception {
        MockHttpClient client =
                new MockHttpClient() {

                    @Override
                    public HTTPResponse get(URL url) throws IOException {
                        if (url.getQuery().contains("GetCapabilities")) {
                            Map<String, String> params = parseParams(url.getQuery());
                            URL caps = null;
                            if ("1.3.0".equals(params.get("VERSION"))) {
                                caps =
                                        WMSCoverageReaderTest.class.getResource(
                                                "caps130_jpeg_only.xml");
                            }
                            return new MockHttpResponse(caps, "text/xml");
                        } else {
                            throw new IllegalArgumentException(
                                    "Don't know how to handle a get request over "
                                            + url.toExternalForm());
                        }
                    }
                };
        // setup the reader
        WebMapServer jpegOnlyWMSserver =
                new WebMapServer(new URL("http://jpeg.geoserver.org/geoserver/wms"), client);
        Layer[] wmsLayers = WMSUtils.getNamedLayers(jpegOnlyWMSserver.getCapabilities());

        WMSLayer l = new WMSLayer(jpegOnlyWMSserver, wmsLayers[0], "", "image/png");
        // verify reader is using correct
        assertTrue(l.getReader().format.equalsIgnoreCase("image/jpeg"));

        WMSLayer l2 = new WMSLayer(jpegOnlyWMSserver, wmsLayers[0], "", "image/unknown");
        // verify backward compatability
        assertTrue(l2.getReader().format.equalsIgnoreCase("image/jpeg"));
    }
}
