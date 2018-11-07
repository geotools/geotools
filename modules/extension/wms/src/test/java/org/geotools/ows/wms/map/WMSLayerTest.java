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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.ParameterParser;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.MockHttpClient;
import org.geotools.ows.MockHttpResponse;
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
        ParameterParser pp = new ParameterParser();
        List params = pp.parse(query, '&');
        Map<String, String> result = new HashMap<String, String>();
        for (Iterator it = params.iterator(); it.hasNext(); ) {
            NameValuePair pair = (NameValuePair) it.next();
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
        MockHttpClient client =
                new MockHttpClient() {

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
}
