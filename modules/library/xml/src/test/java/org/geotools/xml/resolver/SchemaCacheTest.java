/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml.resolver;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.geotools.http.commons.MultithreadedHttpClientFactory;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link SchemaCache}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class SchemaCacheTest {

    public static final String MOCK_SCHEMA_LOCATION = "http://schemacache";

    public static final String MOCK_HTTP_RESPONSE_BODY = "<schemaCacheMockHttpClientResponse>";

    @ClassRule
    public static WireMockClassRule classRule =
            new WireMockClassRule(WireMockConfiguration.options().dynamicPort());

    @Rule
    public WireMockClassRule service = classRule;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Test the {@link SchemaCache#delete(File) method.
     */
    @Test
    public void delete() throws Exception {
        new File("target/test/a/b/c").mkdirs();
        new File("target/test/a/b/d/e/f").mkdirs();
        File f = new File("target/test/a/b/d/e/f/temp.txt");
        try (PrintWriter printWriter = new PrintWriter(f, StandardCharsets.UTF_8)) {
            printWriter.println("Some text");
        }
        Assert.assertTrue(new File("target/test/a/b/d/e/f/temp.txt").exists());
        SchemaCache.delete(new File("target/test/a"));
        Assert.assertFalse(new File("target/test/a").exists());
    }

    /** Test resolution of a schema in an existing cache. */
    @Test
    public void resolve() throws Exception {
        // intentionally construct non-canonical cache directory
        File cacheDirectory =
                new File(URLs.urlToFile(SchemaCacheTest.class.getResource("/test-data/cache")), "../cache");
        SchemaResolver resolver = new SchemaResolver(new SchemaCache(cacheDirectory, false));
        String resolvedLocation = resolver.resolve("http://schemas.example.org/cache-test/cache-test.xsd");
        Assert.assertTrue(resolvedLocation.startsWith("file:"));
        Assert.assertTrue(resolvedLocation.endsWith("cache-test.xsd"));
        Assert.assertTrue(URLs.urlToFile(new URI(resolvedLocation).toURL()).exists());
        // test that cache path is not canonical
        Assert.assertNotEquals(
                cacheDirectory.toString(), cacheDirectory.getCanonicalFile().toString());
        // test that resolved location is canonical, despite cache directory not being canonical
        Assert.assertEquals(
                resolvedLocation,
                URLs.urlToFile(new URI(resolvedLocation).toURL())
                        .getCanonicalFile()
                        .toURI()
                        .toString());
    }

    /**
     * Tests if current data directory have workspace and styles directories and workspace directory has default.xml
     * file inside.
     */
    @Test
    public void testIsSuitableDirectoryToContainCache() throws Exception {
        File dataFolder = folder.newFolder("data");
        File workspaceFolder = folder.newFolder("data", "workspaces");
        folder.newFolder("data", "styles");
        File defaultXmlFile = new File(workspaceFolder, "default.xml");
        defaultXmlFile.createNewFile();
        assertTrue(SchemaCache.isSuitableDirectoryToContainCache(dataFolder));
    }

    /** Test download with the HTTP client specified in the GeoTools hints */
    @Test
    public void downloadWithHttpClient() {
        Hints.putSystemDefault(Hints.HTTP_CLIENT_FACTORY, SchemaCacheMockHttpClientFactory.class);
        byte[] responseBody = SchemaCache.download(MOCK_SCHEMA_LOCATION);
        Assert.assertArrayEquals(MOCK_HTTP_RESPONSE_BODY.getBytes(StandardCharsets.UTF_8), responseBody);
        Hints.removeSystemDefault(Hints.HTTP_CLIENT_FACTORY);
    }

    /** Test that a circular redirect is not followed indefinitely when using the multithreaded HTTP client */
    @Test
    public void circularRedirectMultithreadedHttpClient() {
        Hints.putSystemDefault(Hints.HTTP_CLIENT_FACTORY, MultithreadedHttpClientFactory.class);
        String redirectUrl = "http://localhost:" + service.port() + "/test";
        service.stubFor(
                get(urlEqualTo("/test")).willReturn(aResponse().withStatus(301).withHeader("Location", redirectUrl)));
        byte[] responseBody = SchemaCache.download(redirectUrl);
        Assert.assertNull(responseBody);
        Hints.removeSystemDefault(Hints.HTTP_CLIENT_FACTORY);
    }
}
