/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.geotools.http.CustomHTTPClientFactory.CustomHttpClient;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author Roar Brænden */
public class HTTPClientFinderTest {

    @Before
    public void resetFactoryRegistry() {
        HTTPClientFinder.reset();
    }

    @Test
    public void findingSimpleHttpClientAsDefault() throws Exception {
        HTTPClient client = HTTPClientFinder.createClient();

        assertNotNull(client);
        assertTrue(client instanceof SimpleHttpClient);
    }

    @Test
    public void findingCustomHttpClientTestByHints() throws Exception {

        HTTPClient client = HTTPClientFinder.createClient(new Hints(Hints.HTTP_CLIENT, CustomHttpClient.class));

        assertTrue(client instanceof CustomHttpClient);
    }

    @Test
    public void usingSystemPropertiesToSetLogging() throws Exception {

        Hints.putSystemDefault(Hints.HTTP_LOGGING, "True");
        try {
            HTTPClient client = HTTPClientFinder.createClient();
            assertTrue(client instanceof LoggingHTTPClient);
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }

    @Test
    public void avoidLoggingInspiteSystemProperty() throws Exception {
        Hints.putSystemDefault(Hints.HTTP_LOGGING, "True");
        try {

            HTTPClient client = HTTPClientFinder.createClient(new Hints(Hints.HTTP_LOGGING, "False"));
            assertTrue(client instanceof SimpleHttpClient);
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }

    @Test
    public void usingSystemPropertiesToSetLoggingWithCharset() throws Exception {

        Hints.putSystemDefault(Hints.HTTP_LOGGING, "utf-8");
        try {
            try {
                HTTPClientFinder.createClient();
                Assert.fail("Wrong HTTP_LOGGING should end up with exception.");
            } catch (RuntimeException ex) {
                Assert.assertEquals(
                        "Exception when HTTP_LOGGING set to something else than true/false",
                        "HTTP_LOGGING value utf-8 is unknown.",
                        ex.getMessage());
            }

        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }

    @Test
    public void askingForNonExistingBehavior() throws Exception {
        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> HTTPClientFinder.createClient(HTTPConnectionPooling.class));
        assertEquals(
                "Exception message when asking for non existing behavior.",
                "Couldn't create HTTP client.\nBehaviors:HTTPConnectionPooling",
                ex.getMessage());
    }

    /** Test that a HTTPProxy behavior is added when http.proxyhost is set. In support of GEOT-6850. */
    @Test
    public void createClientWithSystemProxyHost() throws Exception {
        final boolean nullInitially = System.getProperty("http.proxyHost") == null;
        if (nullInitially) {
            System.setProperty("http.proxyHost", "http://proxy.dummy/");
        }
        try {
            HTTPClient client = HTTPClientFinder.createClient(HTTPProxy.class);
            assertTrue(client instanceof SimpleHttpClient);
        } finally {
            if (nullInitially) {
                System.clearProperty("http.proxyHost");
            }
        }
    }
}
