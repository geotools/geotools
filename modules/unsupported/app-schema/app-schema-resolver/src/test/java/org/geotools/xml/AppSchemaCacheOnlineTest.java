/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import java.io.File;
import java.net.URI;

import org.geotools.data.DataUtilities;
import org.geotools.test.OnlineTestSupport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Online test for {@link AppSchemaCatalog}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 */
public class AppSchemaCacheOnlineTest extends OnlineTestSupport {

    /**
     * Downloaded files are stored in this directory. We intentionally use a non-canonical cache
     * directory to test that resolved locations are canonical.
     */
    private static final File CACHE_DIRECTORY = new File(
            "target/app-schema-cache/../app-schema-cache");

    /**
     * Schema that is downloaded.
     */
    private static final String SCHEMA_LOCATION = "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd";

    /**
     * Filename of the schema.
     */
    private static final String SCHEMA_FILENAME;

    static {
        String[] parts = SCHEMA_LOCATION.split("/");
        SCHEMA_FILENAME = parts[parts.length - 1];
    }

    /**
     * @see org.geotools.test.OnlineTestSupport#getFixtureId()
     */
    @Override
    protected String getFixtureId() {
        return "app-schema-resolver";
    }

    /**
     * @see org.geotools.test.OnlineTestSupport#before()
     */
    @Before
    @Override
    public void before() throws Exception {
        super.before();
        AppSchemaCache.delete(CACHE_DIRECTORY);
    }

    /**
     * @see org.geotools.test.OnlineTestSupport#after()
     */
    @After
    @Override
    public void after() throws Exception {
        super.after();
        AppSchemaCache.delete(CACHE_DIRECTORY);
    }

    /**
     * Test download of schema via http.
     */
    @Test
    public void downloadHttp() throws Exception {
        check(AppSchemaCache.download(new URI(SCHEMA_LOCATION)));
    }

    /**
     * Test download of schema via http with smaller block size.
     */
    @Test
    public void downloadHttpWithSmallBlockSize() throws Exception {
        check(AppSchemaCache.download(new URI(SCHEMA_LOCATION), 32));

    }

    /**
     * Test download of schema via http with larger block size.
     */
    @Test
    public void downloadHttpWithLargeBlockSize() throws Exception {
        check(AppSchemaCache.download(new URI(
                "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd"), 65536));
    }

    /**
     * Test download of schema via https.
     */
    @Test
    public void downloadHttps() throws Exception {
        check(AppSchemaCache.download(new URI("https://www.seegrid.csiro.au"
                + "/subversion/GeoSciML/tags/2.0.0/schema/GeoSciML/geosciml.xsd")));
    }

    /**
     * Basic sanity checks of schema and test store to disk.
     */
    private void check(byte[] bytes) {
        Assert.assertTrue(bytes.length > 0);
        String text = new String(bytes);
        Assert.assertTrue(text.contains("GeoSciML"));
        Assert.assertTrue(text.contains("<schema"));
        Assert.assertTrue(text.contains("</schema>"));
        File cachedFile = new File("target/test/test.xsd");
        AppSchemaCache.delete(cachedFile);
        Assert.assertFalse(cachedFile.exists());
        AppSchemaCache.store(cachedFile, bytes);
        Assert.assertTrue(cachedFile.exists());
        Assert.assertEquals(bytes.length, cachedFile.length());
    }

    @Test
    public void cache() throws Exception {
        // expect failure when downloading disabled
        {
            AppSchemaCache cache = new AppSchemaCache(CACHE_DIRECTORY, false);
            String location = cache.resolveLocation(SCHEMA_LOCATION);
            Assert.assertNull(location);
        }
        // should succeed if able to download
        {
            AppSchemaCache cache = new AppSchemaCache(CACHE_DIRECTORY, true);
            String location = cache.resolveLocation(SCHEMA_LOCATION);
            Assert.assertNotNull(location);
            Assert.assertTrue(location.startsWith("file:"));
            Assert.assertTrue(location.endsWith(SCHEMA_FILENAME));
            Assert.assertTrue(DataUtilities.urlToFile((new URI(location)).toURL()).exists());
        }
        // now that schema is is in the cache, should succeed even if downloading is disabled
        {
            AppSchemaCache cache = new AppSchemaCache(CACHE_DIRECTORY, false);
            String location = cache.resolveLocation(SCHEMA_LOCATION);
            Assert.assertNotNull(location);
            Assert.assertTrue(location.startsWith("file:"));
            Assert.assertTrue(location.endsWith(SCHEMA_FILENAME));
            Assert.assertTrue(DataUtilities.urlToFile((new URI(location)).toURL()).exists());
            // test that cache path is not canonical
            Assert.assertFalse(CACHE_DIRECTORY.toString().equals(
                    CACHE_DIRECTORY.getCanonicalFile().toString()));
            // test that resolved location is canonical, despite cache directory not being canonical
            Assert.assertEquals(location, DataUtilities.urlToFile((new URI(location)).toURL())
                    .getCanonicalFile().toURI().toString());
        }
    }

}
