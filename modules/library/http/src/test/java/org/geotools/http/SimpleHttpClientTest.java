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
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.util.Collections.singletonMap;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import org.junit.Rule;
import org.junit.Test;

public class SimpleHttpClientTest {

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
     * Tests if authKey is added to requests as expected
     *
     * @throws IOException
     */
    @Test
    public void testRequestsWithAuthKey() throws IOException {
        SimpleHttpClient client = new SimpleHttpClient();

        String testAuthKey = "some_api_key=123";

        URL urlWithoutAuthKey = new URL("http://localhost:" + wireMockRule.port() + "/test");

        // Mock the expected request and response
        UrlPattern urlPattern = urlEqualTo("/test?" + testAuthKey);
        ResponseDefinitionBuilder response =
                aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>");
        stubFor(get(urlPattern).willReturn(response));
        stubFor(post(urlPattern).willReturn(response));

        client.setAuthKey(testAuthKey);

        // GET
        client.get(urlWithoutAuthKey);
        verify(
                getRequestedFor(urlEqualTo("/test?" + testAuthKey))
                        .withQueryParam("some_api_key", equalTo("123")));

        // POST
        ByteArrayInputStream postBody = new ByteArrayInputStream("GeoTools".getBytes());
        client.post(urlWithoutAuthKey, postBody, "text/plain");
        verify(
                postRequestedFor(urlEqualTo("/test?" + testAuthKey))
                        .withQueryParam("some_api_key", equalTo("123")));
    }
}
