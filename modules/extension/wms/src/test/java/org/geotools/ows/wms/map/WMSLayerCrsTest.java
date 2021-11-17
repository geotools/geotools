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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSUtils;
import org.geotools.ows.wms.WebMapServer;
import org.junit.Before;
import org.junit.Test;

public class WMSLayerCrsTest {
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
                                caps =
                                        WMSCoverageReaderTest.class.getResource(
                                                "rootCRSscaps130.xml");
                            } else if ("1.1.0".equals(params.get("VERSION"))) {
                                caps =
                                        WMSCoverageReaderTest.class.getResource(
                                                "rootCRSscaps110.xml");
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

    /**
     * We test the layer declared native CRS is returned as first item on getSrs() and
     * getBoundinboxes() method. This makes the cascaded WMS layers use by default the declared
     * native CRS form the source WMS layer.
     */
    @Test
    public void testWMSLayerNativeCrsOrderPriority() {
        Layer[] wmsLayers = WMSUtils.getNamedLayers(server.getCapabilities());
        Set<String> srsSet = wmsLayers[0].getSrs();
        assertEquals(srsSet.iterator().next(), "EPSG:3395");
        Map<String, CRSEnvelope> boundingBoxes = wmsLayers[0].getBoundingBoxes();
        assertEquals(boundingBoxes.keySet().iterator().next(), "EPSG:3395");
    }
}
