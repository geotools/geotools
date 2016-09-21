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
package org.geotools.data.wms.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.MockHttpClient;
import org.geotools.data.ows.MockHttpResponse;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.xml.WMSSchema;
import org.geotools.ows.ServiceException;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xml.handlers.DocumentHandler;
import org.junit.Test;

/**
 *
 *
 *
 * @source $URL$
 */
public class EntityResolverTest {

    // prepare the responses
    private class CapsMockClient extends MockHttpClient {

        String capsResource;

        public CapsMockClient(String capsResource) {
            this.capsResource = capsResource;
        }

        public HTTPResponse get(URL url) throws IOException {
            if (url.getQuery().toLowerCase().contains("capabilities")) {
                URL caps = TestData.getResource(EntityResolverTest.this, capsResource);
                return new MockHttpResponse(caps, "text/xml");
            } else {
                throw new IllegalArgumentException(
                        "Don't know how to handle a get request over " + url.toExternalForm());
            }
        }

    };

    @Test
    public void testWMS111() throws Exception {
        testWMSParsingFailure("1.1.1Capabilities-xxe.xml");
    }
    
    @Test
    public void testWMS130() throws Exception {
        testWMSParsingFailure("1.3.0Capabilities-xxe.xml");
    }


    private void testWMSParsingFailure(String capsResource) throws IOException, MalformedURLException {
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);
        hints.put(XMLHandlerHints.ENTITY_RESOLVER, new NoExternalEntityResolver());

        try {
            new WebMapServer(new URL("http://test.org"),
                    new CapsMockClient(capsResource), hints);
            fail("Should have failed with a XML parsing error");
        } catch (ServiceException e) {
            // hamcrest is not up to date enough to use the containsString matcher
            assertTrue(e.getMessage().contains("Error while parsing XML"));
        }
    }
}
