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

import static org.junit.Assert.assertTrue;

import org.geotools.http.CustomHTTPClientFactory.CustomHttpClient;
import org.geotools.util.factory.Hints;
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

        assertTrue(client != null);
        assertTrue(client instanceof SimpleHttpClient);
    }

    @Test
    public void findingCustomHttpClientTestByHints() throws Exception {

        HTTPClient client =
                HTTPClientFinder.createClient(new Hints(Hints.HTTP_CLIENT, CustomHttpClient.class));

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

            HTTPClient client =
                    HTTPClientFinder.createClient(new Hints(Hints.HTTP_LOGGING, "False"));
            assertTrue(client instanceof SimpleHttpClient);
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }

    @Test
    public void usingSystemPropertiesToSetLoggingWithCharset() throws Exception {

        Hints.putSystemDefault(Hints.HTTP_LOGGING, "utf-8");
        try {
            HTTPClient client = HTTPClientFinder.createClient();
            assertTrue(client instanceof LoggingHTTPClient);
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }
}
