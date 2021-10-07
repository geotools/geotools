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

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import org.apache.http.HttpException;
import org.geotools.http.HTTPResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import wiremock.org.apache.commons.io.IOUtils;

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

    private static final String SYS_PROP_KEY_PORT = "http.proxyPort";

    private String[] sysPropOriginalValue = new String[3];

    /** Verifies that method is executed without specifying nonProxyHosts. */
    @Test
    public void testGetWithoutNonProxyHost() throws MalformedURLException, IOException {

        URL proxy = new URL("http://localhost:" + wireMockProxyRule.port());
        System.setProperty(SYS_PROP_KEY_HOST, proxy.getHost());
        System.setProperty(SYS_PROP_KEY_PORT, Integer.toString(proxy.getPort()));
        wireMockProxyRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/fred"))
                                .willReturn(
                                        aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "text/xml")
                                                .withBody("<response>Some content</response>"))));
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            sut.get(new URL("http://geotools.org/fred"));
        }
        // check we intercepted the request with our "proxy"
        wireMockProxyRule.verify(getRequestedFor(urlEqualTo("/fred")));
    }

    /** Verifies that the nonProxyConfig is used when a GET is executed, matching a nonProxyHost. */
    @Test
    public void testGetWithMatchingNonProxyHost() throws HttpException, IOException {
        URL proxy = new URL("http://localhost:" + wireMockProxyRule.port());

        System.setProperty(SYS_PROP_KEY_HOST, proxy.getHost());
        System.setProperty(SYS_PROP_KEY_PORT, Integer.toString(wireMockProxyRule.port()));
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "localhost");

        URL testURL = new URL("http://localhost:" + wireMockRule.port() + "/test");

        wireMockProxyRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/fred"))
                                .willReturn(
                                        aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "text/xml")
                                                .withBody("<response>Some content</response>"))));
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            sut.get(new URL("http://geotools.org/fred"));
        }
        // check we intercepted the request with our "proxy"
        wireMockProxyRule.verify(getRequestedFor(urlEqualTo("/fred")));

        wireMockRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/test"))
                                .willReturn(
                                        aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "text/xml")
                                                .withBody("<response>Some content</response>"))));
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "\"localhost|www.geotools.org\"");
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            sut.get(testURL);
        }
        // check we actually got the "page" we asked for
        wireMockRule.verify(getRequestedFor(urlEqualTo("/test")));
    }

    /** Save original system properties for later restore to avoid affecting other tests. */
    @Before
    public void setupSaveOriginalSysPropValue() {
        sysPropOriginalValue[0] = System.getProperty(SYS_PROP_KEY_NONPROXYHOSTS);
        sysPropOriginalValue[1] = System.getProperty(SYS_PROP_KEY_HOST);
        sysPropOriginalValue[2] = System.getProperty(SYS_PROP_KEY_PORT);
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
        if (sysPropOriginalValue[2] == null) {
            System.clearProperty(SYS_PROP_KEY_PORT);
        } else {
            System.setProperty(SYS_PROP_KEY_PORT, sysPropOriginalValue[2]);
        }
    }

    // use a dynamic http port to avoid conflicts
    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.options().dynamicPort());

    @Rule
    public WireMockRule wireMockProxyRule =
            new WireMockRule(WireMockConfiguration.options().dynamicPort());

    @Test
    public void testBasicHeaderGET() throws IOException {
        String longPassword = String.join("", Collections.nCopies(10, "0123456789"));
        String userName = "user";
        wireMockRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/test"))
                                .willReturn(
                                        aResponse()
                                                .withStatus(401)
                                                .withHeader(
                                                        "WWW-Authenticate",
                                                        "Basic realm=\"User Visible Realm\""))));
        wireMockRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/test"))
                                .withBasicAuth(userName, longPassword)
                                .willReturn(
                                        aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "text/xml")
                                                .withBody("<response>Some content</response>"))));
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            client.setUser(userName);
            client.setPassword(longPassword);
            client.get(new URL("http://localhost:" + wireMockRule.port() + "/test"));

            String encodedCredentials =
                    "dXNlcjowMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5";
            wireMockRule.verify(
                    getRequestedFor(urlEqualTo("/test"))
                            .withHeader("Authorization", equalTo("Basic " + encodedCredentials)));
        }
    }

    @Test
    public void testBasicHeaderPOST() throws IOException {
        String longPassword = String.join("", Collections.nCopies(10, "0123456789"));
        String userName = "user";
        wireMockRule.addStubMapping(
                stubFor(
                        post(urlEqualTo("/test"))
                                .willReturn(
                                        aResponse()
                                                .withStatus(401)
                                                .withHeader(
                                                        "WWW-Authenticate",
                                                        "Basic realm=\"User Visible Realm\""))));
        wireMockRule.addStubMapping(
                stubFor(
                        post(urlEqualTo("/test"))
                                .withBasicAuth(userName, longPassword)
                                .willReturn(
                                        aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "text/xml")
                                                .withBody("<response>Some content</response>"))));
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
            wireMockRule.verify(
                    postRequestedFor(urlEqualTo("/test"))
                            .withHeader("Authorization", equalTo("Basic " + encodedCredentials)));
        }
    }

    @Test
    public void testUserAgentInRequests() throws Exception {
        wireMockRule.addStubMapping(
                stubFor(
                        get(urlEqualTo("/agent"))
                                .withHeader("User-Agent", new ContainsPattern("GeoTools"))
                                .willReturn(
                                        aResponse()
                                                .withStatus(200)
                                                .withHeader("Content-Type", "plain/text")
                                                .withBody("OK"))));

        HTTPResponse response = null;
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            response = client.get(new URL("http://localhost:" + wireMockRule.port() + "/agent"));
            String result =
                    IOUtils.toString(response.getResponseStream(), response.getResponseCharset());
            Assert.assertEquals("OK", result);

            wireMockRule.verify(getRequestedFor(urlEqualTo("/agent")));
        } finally {
            if (response != null) {
                response.dispose();
            }
        }
    }
}
