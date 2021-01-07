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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;

/** @author Roar Brænden */
public class HTTPFactoryFinderTest {

    @Before
    public void resetFactoryRegistry() {
        HTTPFactoryFinder.reset();
    }

    @Test
    public void findingSimpleHttpClientAsDefault() throws Exception {
        HTTPClient client = HTTPFactoryFinder.getHttpClientFactory().getClient();

        assertTrue(client != null);
        assertTrue(client instanceof SimpleHttpClient);
    }

    @Test
    public void findingCustomHttpClientTestByHints() throws Exception {

        HTTPClient client =
                HTTPFactoryFinder.getHttpClientFactory()
                        .getClient(new Hints(Hints.HTTP_CLIENT, CustomHttpClient.class));

        assertTrue(client instanceof CustomHttpClient);
    }

    @Test
    public void usingSystemPropertiesToSetLogging() throws Exception {

        Hints.putSystemDefault(Hints.HTTP_LOGGING, "True");
        try {
            HTTPClient client = HTTPFactoryFinder.getHttpClientFactory().getClient();
            assertTrue(client instanceof LoggingHTTPClient);
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }

    @Test
    public void avoidLoggingInspiteSystemProperty() throws Exception {
        Hints.putSystemDefault(Hints.HTTP_LOGGING, "True");
        try {
            HTTPClientFactory factory = HTTPFactoryFinder.getHttpClientFactory();

            HTTPClient client = factory.getClient(new Hints(Hints.HTTP_LOGGING, "False"));
            assertTrue(client instanceof SimpleHttpClient);
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }

    @Test
    public void usingSystemPropertiesToSetLoggingWithCharset() throws Exception {

        Hints.putSystemDefault(Hints.HTTP_LOGGING, "utf-8");
        try {
            HTTPClient client = HTTPFactoryFinder.getHttpClientFactory().getClient();
            assertTrue(client instanceof LoggingHTTPClient);
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_LOGGING);
        }
    }

    static class CustomHttpClient extends AbstractHttpClient {

        @Override
        public HTTPResponse get(URL url) throws IOException {
            return null;
        }

        @Override
        public HTTPResponse post(URL url, InputStream content, String contentType)
                throws IOException {
            return null;
        }
    }
}
