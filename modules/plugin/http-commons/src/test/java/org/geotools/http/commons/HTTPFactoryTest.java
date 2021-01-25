/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http.commons;

import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPConnectionPooling;
import org.geotools.http.commons.MultithreadedHttpClientFactory.LoggingConnectionPoolingHTTPClient;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author Roar Brænden */
public class HTTPFactoryTest {

    @Before
    public void setup() throws Exception {
        Hints.putSystemDefault(Hints.HTTP_CLIENT_FACTORY, MultithreadedHttpClientFactory.class);
    }

    @After
    public void teardown() throws Exception {
        Hints.removeSystemDefault(Hints.HTTP_CLIENT_FACTORY);
    }

    @Test
    public void testConnectionPooling() throws Exception {
        final int CONN_POOL = 4;
        HTTPClient client = HTTPClientFinder.createClient();
        Assert.assertTrue(client instanceof HTTPConnectionPooling);
        ((HTTPConnectionPooling) client).setMaxConnections(CONN_POOL);
        Assert.assertEquals(CONN_POOL, ((HTTPConnectionPooling) client).getMaxConnections());
        Assert.assertTrue(client instanceof MultithreadedHttpClient);
    }

    @Test
    public void testLogging() throws Exception {
        final int CONN_POOL = 3;
        HTTPClient client = HTTPClientFinder.createClient(new Hints(Hints.HTTP_LOGGING, "True"));
        Assert.assertTrue(client instanceof HTTPConnectionPooling);
        ((HTTPConnectionPooling) client).setMaxConnections(CONN_POOL);
        Assert.assertEquals(CONN_POOL, ((HTTPConnectionPooling) client).getMaxConnections());
        Assert.assertTrue(client instanceof LoggingConnectionPoolingHTTPClient);
    }
}
