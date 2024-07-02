/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.util.Collections.singletonMap;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.ssl.SSLContextBuilder;
import com.github.tomakehurst.wiremock.http.ssl.TrustSelfSignedStrategy;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import wiremock.org.apache.commons.io.IOUtils;

public class SimpleHttpClientTest {

    // use a dynamic http port to avoid conflicts
    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(WireMockConfiguration.options().dynamicPort().dynamicHttpsPort());

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
        SimpleHttpClient client = new SimpleHttpClient();
        client.setUser(userName);
        client.setPassword(longPassword);
        client.get(new URL("http://localhost:" + wireMockRule.port() + "/test"));

        String encodedCredentials =
                "dXNlcjowMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5";
        verify(
                getRequestedFor(urlEqualTo("/test"))
                        .withHeader("Authorization", equalTo("Basic " + encodedCredentials)));
    }

    /**
     * Tests if additional headers are added to requests as expected
     *
     * @throws IOException
     */
    @Test
    public void testRequestsWithAdditionalHeaders() throws IOException {
        String headerValue;
        URL url = new URL("http://localhost:" + wireMockRule.port() + "/test");
        UrlPattern urlPattern = urlEqualTo("/test");
        ResponseDefinitionBuilder response =
                aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>");
        ByteArrayInputStream postBody = new ByteArrayInputStream("GeoTools".getBytes());
        SimpleHttpClient client = new SimpleHttpClient();

        // GET
        stubFor(get(urlPattern).willReturn(response));
        headerValue = "Bearer " + System.currentTimeMillis();
        client.get(url, singletonMap("Authorization", headerValue));
        verify(
                getRequestedFor(urlEqualTo("/test"))
                        .withHeader("Authorization", equalTo(headerValue)));

        // POST
        stubFor(post(urlPattern).willReturn(response));
        headerValue = "Bearer " + System.currentTimeMillis() + 1;
        client.post(url, postBody, "text/plain", singletonMap("Authorization", headerValue));
        verify(
                postRequestedFor(urlEqualTo("/test"))
                        .withHeader("Authorization", equalTo(headerValue)));
    }

    /**
     * Tests if extraParams are added to requests as expected
     *
     * @throws IOException
     */
    @Test
    public void testRequestsWithExtraParams() throws IOException {
        SimpleHttpClient client = new SimpleHttpClient();

        Map<String, String> testExtraParams =
                Map.of("key1", "123", "key2", "value2", "key%3", "value/3");

        URL urlWithoutExtraParams =
                new URL("http://localhost:" + wireMockRule.port() + "/test?key2=duplicate");

        // Mock the expected request and response
        UrlPattern urlPattern = urlMatching("/test[\\w?&=%]*"); // \w or any of ?&=%
        ResponseDefinitionBuilder response =
                aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>");
        stubFor(get(urlPattern).willReturn(response));
        stubFor(post(urlPattern).willReturn(response));

        client.setExtraParams(testExtraParams);

        // GET
        client.get(urlWithoutExtraParams);
        verify(
                getRequestedFor(urlMatching("/test[\\w?&=%]*"))
                        .withQueryParam("key1", equalTo("123"))
                        .withQueryParam("key2", equalTo("value2"))
                        .withQueryParam("key2", equalTo("duplicate"))
                        .withQueryParam(
                                "key%3",
                                equalTo("value/3"))); // % and / are URL-encoded and then decoded
        // again

        // POST
        ByteArrayInputStream postBody = new ByteArrayInputStream("GeoTools".getBytes());
        client.post(urlWithoutExtraParams, postBody, "text/plain");
        verify(
                postRequestedFor(urlMatching("/test[\\w?&=%]*"))
                        .withQueryParam("key1", equalTo("123"))
                        .withQueryParam("key2", equalTo("value2"))
                        .withQueryParam("key2", equalTo("duplicate"))
                        .withQueryParam("key%3", equalTo("value/3")));
    }

    /**
     * Tests if redirection (HTTP -> HTTPS) is followed
     *
     * @throws IOException
     */
    @Test
    public void testRedirect() throws IOException {
        trustSelfSignedCertificate();
        String expectedContent = "<response>Redirected content</response>";

        String redirectURL =
                String.format("https://localhost:" + wireMockRule.httpsPort() + "/test-redirected");
        stubFor(
                get(urlEqualTo("/test-redirect"))
                        .willReturn(
                                aResponse().withStatus(301).withHeader("Location", redirectURL)));
        stubFor(
                get(urlEqualTo("/test-redirected"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "text/xml")
                                        .withBody(expectedContent)));

        SimpleHttpClient client = new SimpleHttpClient();

        HTTPResponse response =
                client.get(new URL("http://localhost:" + wireMockRule.port() + "/test-redirect"));
        String actualContent =
                IOUtils.toString(response.getResponseStream(), StandardCharsets.UTF_8);

        Assert.assertEquals(actualContent, expectedContent);
        verify(getRequestedFor(urlEqualTo("/test-redirected")));
    }

    /**
     * Tests that the maximum number of redirections is respected in case of a redirection loop
     *
     * @throws IOException
     */
    @Test
    public void testMaxRedirectionLimit() throws IOException {
        trustSelfSignedCertificate();
        String httpRedirectURL =
                String.format(
                        "http://localhost:" + wireMockRule.port() + "/test-redirect-loop-http");
        String httpsRedirectURL =
                String.format(
                        "https://localhost:"
                                + wireMockRule.httpsPort()
                                + "/test-redirect-loop-https");
        stubFor(
                get(urlEqualTo("/test-redirect-loop-http"))
                        .willReturn(
                                aResponse()
                                        .withStatus(301)
                                        .withHeader("Location", httpsRedirectURL)));
        stubFor(
                get(urlEqualTo("/test-redirect-loop-https"))
                        .willReturn(
                                aResponse()
                                        .withStatus(301)
                                        .withHeader("Location", httpRedirectURL)));

        SimpleHttpClient client = new SimpleHttpClient();
        client.get(new URL(httpRedirectURL));

        verify(9, getRequestedFor(urlEqualTo("/test-redirect-loop-http")));
        verify(8, getRequestedFor(urlEqualTo("/test-redirect-loop-https")));
    }

    /** Trust wiremock self-signed certificate in order to perform test HTTPS requests */
    private void trustSelfSignedCertificate() {
        SSLContext ctx;
        try {
            SSLContextBuilder sslContextBuilder =
                    SSLContextBuilder.create()
                            .loadTrustMaterial(null, new TrustSelfSignedStrategy());
            ctx = sslContextBuilder.build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        if (ctx != null) {
            SSLContext.setDefault(ctx);
        }
    }
}
