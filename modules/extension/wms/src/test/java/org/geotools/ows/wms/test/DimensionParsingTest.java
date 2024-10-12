/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.xml.Dimension;
import org.geotools.ows.wms.xml.Extent;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.junit.Test;

public class DimensionParsingTest {

    static final class CapabilitiesClient extends MockHttpClient {

        private String capabilitiesFileName;

        public CapabilitiesClient(String capabilitiesFileName) {
            super();
            this.capabilitiesFileName = capabilitiesFileName;
        }

        @Override
        public HTTPResponse get(URL url) throws IOException {
            if (url.getQuery().contains("GetCapabilities")) {
                File getCaps = TestData.file(this, capabilitiesFileName);
                URL caps = URLs.fileToUrl(getCaps);
                return new MockHttpResponse(caps, "text/xml");
            } else {
                throw new IllegalArgumentException(
                        "Don't know how to handle a get request over " + url.toExternalForm());
            }
        }
    }
    ;

    @Test
    public void testDimensionExtent130() throws Exception {
        MockHttpClient client = new CapabilitiesClient("dimensions1_3_0_Capabilities.xml");
        WebMapServer server = new WebMapServer(new URL("http://www.test.org"), client);
        // get the first layer
        Layer layer = server.getCapabilities().getLayer().getLayerChildren().get(0);
        assertEquals("Radar:kesalahti_etop_20", layer.getName());
        assertEquals(1, layer.getDimensions().size());
        Dimension time = layer.getDimension("time");
        assertNotNull(time);
        assertNotNull(time.getExtent());
        assertEquals("2016-01-08T12:45:00.000Z", time.getExtent().getValue());
    }

    @Test
    public void testDimensionExtent111() throws Exception {
        MockHttpClient client = new CapabilitiesClient("dimensions1_1_1_Capabilities.xml");
        WebMapServer server = new WebMapServer(new URL("http://www.test.org"), client);
        // get the first layer
        Layer layer = server.getCapabilities().getLayer().getLayerChildren().get(0);
        assertEquals("Radar:kesalahti_etop_20", layer.getName());
        assertEquals(1, layer.getDimensions().size());
        Dimension time = layer.getDimension("time");
        assertNotNull(time);
        assertNotNull(time.getExtent());
        assertEquals("2016-01-08T12:45:00.000Z", time.getExtent().getValue());
        // check also the stand alone extent
        Extent extent = layer.getExtent("time");
        assertEquals("2016-01-08T12:45:00.000Z", extent.getValue());
    }
}
