/*    GeoTools - The Open Source Java GIS Toolkit
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

import org.geotools.util.factory.Hints;
import org.junit.Test;

/** @author Roar Brænden */
public class DefaultHTTPClientFactoryTest {

    @Test
    public void testBasicUsage() throws Exception {

        HTTPClient client = new DefaultHTTPClientFactory().createClient();
        assertTrue(client != null);
        assertTrue(client instanceof SimpleHttpClient);
    }

    @Test
    public void testLoggingUsage() throws Exception {
        HTTPClientFactory factory = new DefaultHTTPClientFactory();
        HTTPClient client = factory.createClient(new Hints(Hints.HTTP_LOGGING, "TRUE"));
        assertTrue(client instanceof LoggingHTTPClient);
    }
}
