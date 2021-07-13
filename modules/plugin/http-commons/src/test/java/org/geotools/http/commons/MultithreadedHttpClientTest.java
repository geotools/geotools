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
package org.geotools.http.commons;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for {@link org.geotools.http.commons.MultithreadedHttpClient}.
 *
 * <p>Copied from gt-wms
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

    HttpResponse mockHttpResponse = mock(HttpResponse.class);
    StatusLine statusLine = mock(StatusLine.class);

    @Before
    public void setupMocks() throws ClientProtocolException, IOException {

        when(mockHttpClient.execute(any(HttpRequestBase.class))).thenReturn(mockHttpResponse);
        when(mockHttpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
    }

    /**
     * Verifies that the default configuration (with proxy settings) is used when a GET is executed,
     * matching no nonProxyHost.
     */
    @Test
    public void testGetWithNoMatchingNonProxyHost() throws MalformedURLException, IOException {
        System.setProperty(SYS_PROP_KEY_HOST, "myproxy");
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "*.geotools.org|localhost");
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            sut.setConnectTimeout(1);
            HttpClient client = sut.createHttpClient();
            try {
                client.execute(new HttpHost("www.google.com"), new HttpGet());
                fail("Proxy fail");
            } catch (UnknownHostException e) {
                // all is good
            }
            try {
                HttpResponse resp = client.execute(new HttpHost("localhost"), new HttpGet());
                assertNotNull("proxy non-host fail", resp);
            } catch (HttpHostConnectException e) {
                // all is good
            }
            try {
                HttpResponse resp = client.execute(new HttpHost("www.geotools.org"), new HttpGet());
                assertNotNull("proxy non-host fail", resp);
            } catch (HttpHostConnectException e) {
                // all is good
            }
        }
    }

    /** Verifies that method is executed without specifying nonProxyHosts. */
    @Test
    public void testGetWithoutNonProxyHost() throws MalformedURLException, IOException {
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            HttpClient client = sut.createHttpClient();
            HttpResponse resp = client.execute(new HttpHost("www.geotools.org"), new HttpGet());
            assertNotNull("network fail", resp);
        }

        System.setProperty(SYS_PROP_KEY_HOST, "myproxy");
        try (MultithreadedHttpClient sut = new MultithreadedHttpTestClient()) {
            // sut.get(new URL("http://www.geotools.org"));
            HttpClient client = sut.createHttpClient();
            HttpResponse resp = client.execute(new HttpHost("www.geotools.org"), new HttpGet());
            assertNull("Proxy look up failed", resp);
        }

        // HttpClient.executeMethod(HttpMethod) has to be called (w/o
        // HostConfig)

    }

    /** Verifies that the nonProxyConfig is used when a GET is executed, matching a nonProxyHost. */
    @Test
    public void testGetWithMatchingNonProxyHost() throws HttpException, IOException {
        System.setProperty(SYS_PROP_KEY_HOST, "myproxy");
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "localhost");
        try (MultithreadedHttpClient sut = new MultithreadedHttpTestClient()) {

            when(mockHttpClient.execute(isNull(), any(HttpRequestBase.class))).thenReturn(null);
            sut.get(new URL("http://localhost"));
        }

        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "\"localhost|www.geotools.org\"");
        try (MultithreadedHttpClient sut = new MultithreadedHttpTestClient()) {
            sut.get(new URL("http://localhost"));
            // HttpClient.executeMethod(HostConfig, HttpMethod) has to be called
            // (with HostConfig)
            verify(mockHttpClient, times(2)).execute(any(HttpRequestBase.class));
        }
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
    public void testBasicHeaderGET() throws IOException {
        String longPassword = String.join("", Collections.nCopies(10, "0123456789"));
        String userName = "user";
        stubFor(
                get(urlEqualTo("/test"))
                        .willReturn(
                                aResponse()
                                        .withStatus(401)
                                        .withHeader(
                                                "WWW-Authenticate",
                                                "Basic realm=\"User Visible Realm\"")));
        stubFor(
                get(urlEqualTo("/test"))
                        .withBasicAuth(userName, longPassword)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "text/xml")
                                        .withBody("<response>Some content</response>")));
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
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

    @Test
    public void testBasicHeaderPOST() throws IOException {
        String longPassword = String.join("", Collections.nCopies(10, "0123456789"));
        String userName = "user";
        stubFor(
                post(urlEqualTo("/test"))
                        .willReturn(
                                aResponse()
                                        .withStatus(401)
                                        .withHeader(
                                                "WWW-Authenticate",
                                                "Basic realm=\"User Visible Realm\"")));
        stubFor(
                post(urlEqualTo("/test"))
                        .withBasicAuth(userName, longPassword)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "text/xml")
                                        .withBody("<response>Some content</response>")));
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            client.setUser(userName);
            client.setPassword(longPassword);
            String body = "<data>A body string</data>";
            client.post(
                    new URL("http://localhost:" + wireMockRule.port() + "/test"),
                    new ByteArrayInputStream(body.getBytes()),
                    "text/xml");

            String encodedCredentials =
                    "dXNlcjowMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5";
            WireMock.verify(
                    postRequestedFor(urlEqualTo("/test"))
                            .withHeader("Authorization", equalTo("Basic " + encodedCredentials)));
        }
    }
}
