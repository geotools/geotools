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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/** @author Roar Brænden */
public class DefaultHTTPClientFactoryTest {

    private URL url;
    private ByteArrayInputStream postBody = new ByteArrayInputStream("GeoTools".getBytes(StandardCharsets.UTF_8));
    private HTTPResponse mockResponse = Mockito.mock(HTTPResponse.class);
    private HTTPClient mockClient = Mockito.mock(HTTPClient.class);

    @Before
    public void setUp() throws MalformedURLException {
        url = new URL("http://dummy");
    }

    @Test
    public void testBasicUsage() throws Exception {

        HTTPClient client = new DefaultHTTPClientFactory().createClient(new LinkedList<>());
        assertNotNull(client);
        assertTrue(client instanceof SimpleHttpClient);
    }

    /**
     * Tests if 1) hint for logging produces expected client and 2) client calls expected API of delegate, to pass on
     * headers. Also covers {@link DelegateHTTPClient}.
     *
     * @throws Exception
     */
    @Test
    public void testLoggingUsage() throws Exception {
        HTTPClientFactory factory = new DefaultHTTPClientFactory();
        HTTPClient client = factory.createClient(new Hints(Hints.HTTP_LOGGING, "TRUE"), new LinkedList<>());
        assertTrue(client instanceof LoggingHTTPClient);

        LoggingHTTPClient loggingClient = (LoggingHTTPClient) client;

        when(mockClient.get(any())).thenReturn(mockResponse);
        when(mockClient.get(any(), any())).thenReturn(mockResponse);
        when(mockClient.post(any(), any(), any())).thenReturn(mockResponse);
        when(mockClient.post(any(), any(), any(), any())).thenReturn(mockResponse);

        loggingClient.delegate = mockClient;

        loggingClient.get(url);
        loggingClient.get(url, Collections.emptyMap());
        loggingClient.post(url, postBody, "text/plain");
        loggingClient.post(url, postBody, "text/plain", Collections.emptyMap());

        verify(mockClient, times(1)).get(any());
        verify(mockClient, times(1)).get(any(), any());
        verify(mockClient, times(1)).post(any(), any(), any());
        verify(mockClient, times(1)).post(any(), any(), any(), any());
    }
}
