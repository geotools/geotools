/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.geotools.ows.wms.MultithreadedHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for {@link MultithreadedHttpClient}.
 *
 * @author awaterme
 */
public class MultithreadedHttpClientTest {

    private static final String SYS_PROP_KEY_NONPROXYHOSTS = "http.nonProxyHosts";
    private static final String SYS_PROP_KEY_HOST = "http.proxyHost";

    private String[] sysPropOriginalValue = new String[2];

    private final class MultithreadedHttpTestClient extends MultithreadedHttpClient {
        @Override
        public HttpClient createHttpClient() {
            return mockHttpClient;
        }
    };

    private HttpClient mockHttpClient = mock(HttpClient.class);
    private HostConfiguration mockHostConfiguration = mock(HostConfiguration.class);

    @Before
    public void setupMocks() {
        when(mockHttpClient.getHostConfiguration()).thenReturn(mockHostConfiguration);
    }

    /**
     * Verifies that the default configuration (with proxy settings) is used when a GET is executed,
     * matching no nonProxyHost.
     */
    @Test
    public void testGetWithNoMatchingNonProxyHost() throws MalformedURLException, IOException {
        System.setProperty(SYS_PROP_KEY_HOST, "myproxy");
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "localhost");
        MultithreadedHttpClient sut = new MultithreadedHttpTestClient();
        when(mockHttpClient.executeMethod(any(HttpMethod.class))).thenReturn(200);
        sut.get(new URL("http://www.geotools.org"));
        // HttpClient.executeMethod(HttpMethod) has to be called (w/o
        // HostConfig)
        verify(mockHttpClient, times(1)).executeMethod(any(HttpMethod.class));
    }

    /** Verifies that method is executed without specifying nonProxyHosts. */
    @Test
    public void testGetWithoutNonProxyHost() throws MalformedURLException, IOException {
        MultithreadedHttpClient sut = new MultithreadedHttpTestClient();
        when(mockHttpClient.executeMethod(any(HttpMethod.class))).thenReturn(200);
        sut.get(new URL("http://www.geotools.org"));

        System.setProperty(SYS_PROP_KEY_HOST, "myproxy");
        sut = new MultithreadedHttpTestClient();
        sut.get(new URL("http://www.geotools.org"));

        // HttpClient.executeMethod(HttpMethod) has to be called (w/o
        // HostConfig)
        verify(mockHttpClient, times(2)).executeMethod(any(HttpMethod.class));
    }

    /** Verifies that the nonProxyConfig is used when a GET is executed, matching a nonProxyHost. */
    @Test
    public void testGetWithMatchingNonProxyHost() throws HttpException, IOException {
        System.setProperty(SYS_PROP_KEY_HOST, "myproxy");
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "localhost");
        MultithreadedHttpClient sut = new MultithreadedHttpTestClient();
        when(mockHttpClient.executeMethod((HostConfiguration) isNull(), any(HttpMethod.class)))
                .thenReturn(200);
        sut.get(new URL("http://localhost"));

        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "\"localhost|www.geotools.org\"");
        sut = new MultithreadedHttpTestClient();
        sut.get(new URL("http://localhost"));
        // HttpClient.executeMethod(HostConfig, HttpMethod) has to be called
        // (with HostConfig)
        verify(mockHttpClient, times(2)).executeMethod(any(), any(HttpMethod.class));
    }

    /** Save original system properties for later restore to avoid affecting other tests. */
    @Before
    public void setupSaveOriginalSysPropValue() {
        sysPropOriginalValue[0] = System.getProperty(SYS_PROP_KEY_NONPROXYHOSTS);
        sysPropOriginalValue[1] = System.getProperty(SYS_PROP_KEY_HOST);
    }

    /** Restore original system properties to avoid affecting other tests. */
    @After
    public void setupRestoreOriginalSysPropValue() {
        if (sysPropOriginalValue[0] == null) {
            System.clearProperty(SYS_PROP_KEY_NONPROXYHOSTS);
        } else {
            System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, sysPropOriginalValue[0]);
        }
        if (sysPropOriginalValue[1] == null) {
            System.clearProperty(SYS_PROP_KEY_HOST);
        } else {
            System.setProperty(SYS_PROP_KEY_HOST, sysPropOriginalValue[1]);
        }
    }

    // use a dynamic http port to avoid conflicts
    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.options().dynamicPort());

    @Test
    public void testBasicHeader() throws IOException {
        stubFor(
                get(urlEqualTo("/test"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "text/xml")
                                        .withBody("<response>Some content</response>")));

        String longPassword = String.join("", Collections.nCopies(10, "0123456789"));
        String userName = "user";
        MultithreadedHttpClient client = new MultithreadedHttpClient();
        client.setUser(userName);
        client.setPassword(longPassword);
        client.get(new URL("http://localhost:" + wireMockRule.port() + "/test"));

        String encodedCredentials =
                "dXNlcjowMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5";
        WireMock.verify(
                getRequestedFor(urlEqualTo("/test"))
                        .withHeader("Authorization", equalTo("Basic " + encodedCredentials)));
    }
}
