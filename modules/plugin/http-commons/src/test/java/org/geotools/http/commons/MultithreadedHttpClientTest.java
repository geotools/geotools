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
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.apache.hc.core5.http.HttpException;
import org.geotools.http.HTTPResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
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

    @ClassRule
    public static WireMockClassRule classRule =
            new WireMockClassRule(WireMockConfiguration.options().dynamicPort());

    @ClassRule
    public static WireMockClassRule classProxyRule =
            new WireMockClassRule(WireMockConfiguration.options().dynamicPort());

    @Rule
    public WireMockClassRule service = classRule;

    @Rule
    public WireMockClassRule proxyService = classProxyRule;

    /** Verifies that method is executed without specifying nonProxyHosts. */
    @Test
    public void testGetWithoutNonProxyHost() throws MalformedURLException, IOException {

        URL proxy = new URL("http://localhost:" + proxyService.port());
        System.setProperty(SYS_PROP_KEY_HOST, proxy.getHost());
        System.setProperty(SYS_PROP_KEY_PORT, Integer.toString(proxy.getPort()));
        proxyService.stubFor(get(urlEqualTo("/fred"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            sut.get(new URL("http://geotools.org/fred"));
        }
        // check we intercepted the request with our "proxy"
        proxyService.verify(getRequestedFor(urlEqualTo("/fred")));
    }

    @Test
    public void testWithBasicAuthProvided() throws Exception {
        service.stubFor(get(urlEqualTo("/testba"))
                .withBasicAuth("flup", "top")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<ok>authorized</ok>")));

        try (MultithreadedHttpClient toTest = new MultithreadedHttpClient()) {
            toTest.setUser("flup");
            toTest.setPassword("top");
            toTest.get(new URL("http://localhost:" + service.port() + "/testba"));
        }
    }

    /** Verifies that the nonProxyConfig is used when a GET is executed, matching a nonProxyHost. */
    @Test
    public void testGetWithMatchingNonProxyHost() throws HttpException, IOException {
        URL proxy = new URL("http://localhost:" + proxyService.port());

        System.setProperty(SYS_PROP_KEY_HOST, proxy.getHost());
        System.setProperty(SYS_PROP_KEY_PORT, Integer.toString(proxyService.port()));
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "localhost");

        URL testURL = new URL("http://localhost:" + service.port() + "/test");

        proxyService.stubFor(get(urlEqualTo("/fred"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            sut.get(new URL("http://geotools.org/fred"));
        }
        // check we intercepted the request with our "proxy"
        proxyService.verify(getRequestedFor(urlEqualTo("/fred")));

        service.stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));
        System.setProperty(SYS_PROP_KEY_NONPROXYHOSTS, "\"localhost|www.geotools.org\"");
        try (MultithreadedHttpClient sut = new MultithreadedHttpClient()) {
            sut.get(testURL);
        }
        // check we actually got the "page" we asked for
        service.verify(getRequestedFor(urlEqualTo("/test")));
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

    @Test
    public void testBasicHeaderGET() throws IOException {
        String longPassword = "0123456789".repeat(10);
        String userName = "user";
        service.stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("WWW-Authenticate", "Basic realm=\"User Visible Realm\"")));
        service.stubFor(get(urlEqualTo("/test"))
                .withBasicAuth(userName, longPassword)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            client.setUser(userName);
            client.setPassword(longPassword);
            client.get(new URL("http://localhost:" + service.port() + "/test"));

            String encodedCredentials =
                    "dXNlcjowMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5";
            service.verify(getRequestedFor(urlEqualTo("/test"))
                    .withHeader("Authorization", equalTo("Basic " + encodedCredentials)));
        }
    }

    @Test
    public void testBasicHeaderPOST() throws IOException {
        String longPassword = "0123456789".repeat(10);
        String userName = "user";
        service.stubFor(post(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("WWW-Authenticate", "Basic realm=\"User Visible Realm\"")));
        service.stubFor(post(urlEqualTo("/test"))
                .withBasicAuth(userName, longPassword)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            client.setUser(userName);
            client.setPassword(longPassword);
            String body = "<data>A body string</data>";
            client.post(
                    new URL("http://localhost:" + service.port() + "/test"),
                    new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)),
                    "text/xml");

            String encodedCredentials =
                    "dXNlcjowMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5";
            service.verify(postRequestedFor(urlEqualTo("/test"))
                    .withHeader("Authorization", equalTo("Basic " + encodedCredentials)));
        }
    }

    @Test
    public void testUserAgentInRequests() throws Exception {
        service.stubFor(get(urlEqualTo("/agent"))
                .withHeader("User-Agent", new ContainsPattern("GeoTools"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "plain/text")
                        .withBody("OK")));

        HTTPResponse response = null;
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            response = client.get(new URL("http://localhost:" + service.port() + "/agent"));
            String result = IOUtils.toString(response.getResponseStream(), response.getResponseCharset());
            Assert.assertEquals("OK", result);

            service.verify(getRequestedFor(urlEqualTo("/agent")));
        } finally {
            if (response != null) {
                response.dispose();
            }
        }
    }

    /**
     * Tests if additional headers are added to requests as expected
     *
     * @throws IOException
     */
    @Test
    public void testRequestsWithAdditionalHeaders() throws IOException {
        String headerValue;
        URL url = new URL("http://localhost:" + service.port() + "/test");
        UrlPattern urlPattern = urlEqualTo("/test");
        ResponseDefinitionBuilder responseBldr = aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/xml")
                .withBody("<response>Some content</response>");
        ByteArrayInputStream postBody = new ByteArrayInputStream("GeoTools".getBytes(StandardCharsets.UTF_8));
        HTTPResponse response = null;

        // GET
        service.stubFor(get(urlPattern).willReturn(responseBldr));
        headerValue = "Bearer " + System.currentTimeMillis();
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            response = client.get(url, Map.of("Authorization", headerValue));
            service.verify(getRequestedFor(urlEqualTo("/test")).withHeader("Authorization", equalTo(headerValue)));
        } finally {
            if (response != null) {
                response.dispose();
                response = null;
            }
        }

        // POST
        service.stubFor(post(urlPattern).willReturn(responseBldr));
        headerValue = "Bearer " + System.currentTimeMillis() + 1;
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            response = client.post(url, postBody, "text/plain", Map.of("Authorization", headerValue));
            service.verify(postRequestedFor(urlEqualTo("/test")).withHeader("Authorization", equalTo(headerValue)));
        } finally {
            if (response != null) {
                response.dispose();
            }
        }
    }

    @Test
    public void testGzipEncodedResponse() throws IOException {
        String testContent =
                """
        <?xml version="1.0" encoding="UTF-8"?>
        <response>
            <message>GZipped test content - this is some longer text to ensure proper compression</message>
        </response>""";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(testContent.getBytes(StandardCharsets.UTF_8));
        }
        byte[] gzipContent = byteArrayOutputStream.toByteArray();

        service.stubFor(get(urlEqualTo("/gzip"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withHeader("Content-Encoding", "gzip")
                        .withBody(gzipContent)));

        HTTPResponse response = null;
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            response = client.get(new URL("http://localhost:" + service.port() + "/gzip"));
            String result = IOUtils.toString(response.getResponseStream(), StandardCharsets.UTF_8);
            Assert.assertEquals(testContent, result);
        } finally {
            if (response != null) {
                response.dispose();
            }
        }
    }

    /** Verifies that a response arriving slower than the configured readTimeout fails fast instead of hanging. */
    @Test(timeout = 60000)
    public void testReadTimeout() {
        service.stubFor(get(urlEqualTo("/slow-response"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withFixedDelay(3000)
                        .withBody("too slow")));

        long start = System.currentTimeMillis();
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            client.setReadTimeout(1);
            try {
                client.get(new URL("http://localhost:" + service.port() + "/slow-response"));
            } catch (IOException e) {
                Assert.assertEquals("Read timed out", e.getMessage());
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        Assert.assertTrue(
                "Expected read timeout to fire around the configured 1s, but took " + elapsed + "ms", elapsed < 2000);
    }

    /**
     * Verifies that a connection attempt taking longer than the configured connectTimeout fails fast instead of
     * hanging. WireMock cannot delay the TCP handshake itself, so a non-routable address (RFC 5737 TEST-NET-1) is used
     * to simulate a connection that never completes.
     */
    // Guards against the test hanging the build if connectTimeout isn't honored in some environment.
    @Test(timeout = 60000)
    public void testConnectTimeout() {
        long start = System.currentTimeMillis();
        try (MultithreadedHttpClient client = new MultithreadedHttpClient()) {
            client.setConnectTimeout(1);
            Assert.assertThrows(IOException.class, () -> client.get(new URL("http://192.0.2.1:81/unreachable")));
        }
        long elapsed = System.currentTimeMillis() - start;
        Assert.assertTrue(
                "Expected connect timeout to fire around the configured 1s, but took " + elapsed + "ms",
                elapsed < 2000);
    }
}
