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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.MockHttpClient;
import org.geotools.ows.MockHttpResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.test.TestData;
import org.geotools.util.PreventLocalEntityResolver;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.XMLHandlerHints;
import org.junit.Test;
import org.xml.sax.EntityResolver;

/** Test validation and use of an {@link EntityResolver} during GetCapabilities parsing. */
public class WMSParserTest {

    // prepare the responses
    private static class CapsMockClient extends MockHttpClient {

        String capsResource;

        public CapsMockClient(String capsResource) {
            this.capsResource = capsResource;
        }

        public HTTPResponse get(URL url) throws IOException {
            if (url.getQuery().toLowerCase().contains("capabilities")) {
                URL caps = TestData.getResource(WMSParserTest.class, capsResource);
                return new MockHttpResponse(caps, "text/xml");
            } else {
                throw new IllegalArgumentException(
                        "Don't know how to handle a get request over " + url.toExternalForm());
            }
        }
    };

    @Test
    public void testWMS111EntityResolver() throws Exception {
        Map<String, Object> hints = new HashMap<>();
        hints.put(XMLHandlerHints.ENTITY_RESOLVER, PreventLocalEntityResolver.INSTANCE);

        WebMapServer wms =
                new WebMapServer(
                        new URL("http://test.org"),
                        new CapsMockClient("1.1.1Capabilities.xml"),
                        hints);
        assertEquals("1.1.1", wms.getCapabilities().getVersion());

        try {
            wms =
                    new WebMapServer(
                            new URL("http://test.org"),
                            new CapsMockClient("1.1.1Capabilities-xxe.xml"),
                            hints);
            fail("Should have failed with a XML parsing error");
        } catch (ServiceException e) {
            assertTrue(e.getMessage().contains("Error while parsing XML"));
        }
    }

    @Test
    public void testWMS130Validation() throws Exception {
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.TRUE);

        WebMapServer wms =
                new WebMapServer(
                        new URL("http://test.org"),
                        new CapsMockClient("1.3.0Capabilities.xml"),
                        hints);
        assertEquals("1.3.0", wms.getCapabilities().getVersion());
        assertEquals(Boolean.TRUE, wms.getHints().get(DocumentFactory.VALIDATION_HINT));

        hints = new HashMap<>();
        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);

        wms =
                new WebMapServer(
                        new URL("http://test.org"),
                        new CapsMockClient("1.3.0Capabilities.xml"),
                        hints);

        assertEquals("1.3.0", wms.getCapabilities().getVersion());
        assertEquals(Boolean.FALSE, wms.getHints().get(DocumentFactory.VALIDATION_HINT));
    }

    @Test
    public void testWMS130EntityResolver() throws Exception {
        Map<String, Object> hints = new HashMap<>();
        hints.put(XMLHandlerHints.ENTITY_RESOLVER, PreventLocalEntityResolver.INSTANCE);

        WebMapServer wms =
                new WebMapServer(
                        new URL("http://test.org"),
                        new CapsMockClient("1.3.0Capabilities.xml"),
                        hints);
        assertEquals("1.3.0", wms.getCapabilities().getVersion());
        try {
            wms =
                    new WebMapServer(
                            new URL("http://test.org"),
                            new CapsMockClient("1.3.0Capabilities-xxe.xml"),
                            hints);
            fail("Should have failed with a XML parsing error");
        } catch (ServiceException e) {
            assertTrue(e.getMessage().contains("Error while parsing XML"));
        }
    }
}
