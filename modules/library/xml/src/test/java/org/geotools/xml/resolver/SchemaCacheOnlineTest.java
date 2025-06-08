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

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.geotools.test.OnlineTestSupport;
import org.geotools.util.URLs;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Online test for {@link SchemaCatalog}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class SchemaCacheOnlineTest extends OnlineTestSupport {

    /** System property used to set HTTPS protocols. */
    private static final String HTTPS_PROTOCOLS = "https.protocols";

    /**
     * Downloaded files are stored in this directory. We intentionally use a non-canonical cache directory to test that
     * resolved locations are canonical.
     */
    private static final File CACHE_DIRECTORY = new File("target/schema-cache/../schema-cache");

    /** Schema that is downloaded. */
    private static final String SCHEMA_LOCATION = "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd";

    /** Filename of the schema. */
    private static final String SCHEMA_FILENAME;

    static {
        String[] parts = SCHEMA_LOCATION.split("/");
        SCHEMA_FILENAME = parts[parts.length - 1];
    }

    /** @see org.geotools.test.OnlineTestSupport#getFixtureId() */
    @Override
    protected String getFixtureId() {
        return "schema-resolver";
    }

    /** @see org.geotools.test.OnlineTestSupport#before() */
    @Before
    @Override
    public void before() throws Exception {
        super.before();
        SchemaCache.delete(CACHE_DIRECTORY);
    }

    /** @see org.geotools.test.OnlineTestSupport#after() */
    @After
    @Override
    public void after() throws Exception {
        super.after();
        SchemaCache.delete(CACHE_DIRECTORY);
    }

    /** Test download of schema via http. */
    @Test
    public void downloadHttp() throws Exception {
        check(SchemaCache.download(new URI(SCHEMA_LOCATION)));
    }

    /** Test download of schema via http with smaller block size. */
    @Test
    public void downloadHttpWithSmallBlockSize() throws Exception {
        check(SchemaCache.download(new URI(SCHEMA_LOCATION), 32));
    }

    /** Test download of schema via http with larger block size. */
    @Test
    public void downloadHttpWithLargeBlockSize() throws Exception {
        check(SchemaCache.download(new URI(SCHEMA_LOCATION), 65536));
    }

    /** Test download of schema via https. */
    @Test
    public void downloadHttps() throws Exception {
        // save original system property
        String httpsProtocols = System.getProperty(HTTPS_PROTOCOLS);
        // force connection to use TLSv1.2 because OpenJDK 7 and Oracle JDK 7
        // fail when connecting to this test server with TLSv1; perhaps because
        // the test server renegotiates to TLSv1.2?
        System.setProperty(HTTPS_PROTOCOLS, "TLSv1.2");
        // test HTTPS download
        check(SchemaCache.download(new URI("https://geosciml.org/geosciml/2.0/xsd/geosciml.xsd")));
        // restore original system property
        if (httpsProtocols == null) {
            System.clearProperty(HTTPS_PROTOCOLS);
        } else {
            System.setProperty(HTTPS_PROTOCOLS, httpsProtocols);
        }
    }

    /** Basic sanity checks of schema and test store to disk. */
    private void check(byte[] bytes) {
        Assert.assertTrue(bytes.length > 0);
        String text = new String(bytes);
        Assert.assertTrue(text.contains("GeoSciML"));
        Assert.assertTrue(text.contains("<schema"));
        Assert.assertTrue(text.contains("</schema>"));
        File cachedFile = new File("target/test/test.xsd");
        SchemaCache.delete(cachedFile);
        Assert.assertFalse(cachedFile.exists());
        SchemaCache.store(cachedFile, bytes);
        Assert.assertTrue(cachedFile.exists());
        Assert.assertEquals(bytes.length, cachedFile.length());
    }

    @Test
    public void cache() throws Exception {
        // expect failure when downloading disabled
        {
            SchemaCache cache = new SchemaCache(CACHE_DIRECTORY, false);
            String location = cache.resolveLocation(SCHEMA_LOCATION);
            Assert.assertNull(location);
        }
        // should succeed if able to download
        {
            SchemaCache cache = new SchemaCache(CACHE_DIRECTORY, true);
            String location = cache.resolveLocation(SCHEMA_LOCATION);
            Assert.assertNotNull(location);
            Assert.assertTrue(location.startsWith("file:"));
            Assert.assertTrue(location.endsWith(SCHEMA_FILENAME));
            Assert.assertTrue(URLs.urlToFile(new URI(location).toURL()).exists());
        }
        // now that schema is is in the cache, should succeed even if downloading is disabled
        {
            SchemaCache cache = new SchemaCache(CACHE_DIRECTORY, false);
            String location = cache.resolveLocation(SCHEMA_LOCATION);
            Assert.assertNotNull(location);
            Assert.assertTrue(location.startsWith("file:"));
            Assert.assertTrue(location.endsWith(SCHEMA_FILENAME));
            Assert.assertTrue(URLs.urlToFile(new URI(location).toURL()).exists());
            // test that cache path is not canonical
            Assert.assertNotEquals(
                    CACHE_DIRECTORY.toString(),
                    CACHE_DIRECTORY.getCanonicalFile().toString());
            // test that resolved location is canonical, despite cache directory not being canonical
            Assert.assertEquals(
                    location,
                    URLs.urlToFile(new URI(location).toURL())
                            .getCanonicalFile()
                            .toURI()
                            .toString());
        }
    }

    /** Test that redirection is followed. */
    @Test
    public void downloadWithRedirect() throws IOException {
        URL url = new URL("http://wms.geo.admin.ch");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // this url has the header with a redirect
        Assert.assertNotNull(conn.getHeaderField("Location"));
        byte[] responseBody = SchemaCache.download("http://wms.geo.admin.ch");
        Assert.assertNotNull(responseBody);
        Assert.assertTrue(responseBody.length > 0);
    }

    /** Test that a failed download does not throw an exception. */
    @Test
    public void downloadFails() {
        byte[] responseBody = SchemaCache.download("https://www.google.com/404");
        Assert.assertNull(responseBody);
    }
}
