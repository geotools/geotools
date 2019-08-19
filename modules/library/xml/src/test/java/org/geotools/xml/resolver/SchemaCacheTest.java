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
import java.io.PrintWriter;
import java.net.URI;
import org.geotools.util.URLs;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SchemaCache}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class SchemaCacheTest {

    /**
     * Test the {@link SchemaCache#delete(File) method.
     */
    @Test
    public void delete() throws Exception {
        (new File("target/test/a/b/c")).mkdirs();
        (new File("target/test/a/b/d/e/f")).mkdirs();
        File f = new File("target/test/a/b/d/e/f/temp.txt");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(f);
            printWriter.println("Some text");
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        Assert.assertTrue((new File("target/test/a/b/d/e/f/temp.txt")).exists());
        SchemaCache.delete(new File("target/test/a"));
        Assert.assertFalse((new File("target/test/a")).exists());
    }

    /** Test resolution of a schema in an existing cache. */
    @Test
    public void resolve() throws Exception {
        // intentionally construct non-canonical cache directory
        File cacheDirectory =
                new File(
                        URLs.urlToFile(SchemaCacheTest.class.getResource("/test-data/cache")),
                        "../cache");
        SchemaResolver resolver = new SchemaResolver(new SchemaCache(cacheDirectory, false));
        String resolvedLocation =
                resolver.resolve("http://schemas.example.org/cache-test/cache-test.xsd");
        Assert.assertTrue(resolvedLocation.startsWith("file:"));
        Assert.assertTrue(resolvedLocation.endsWith("cache-test.xsd"));
        Assert.assertTrue(URLs.urlToFile((new URI(resolvedLocation)).toURL()).exists());
        // test that cache path is not canonical
        Assert.assertFalse(
                cacheDirectory.toString().equals(cacheDirectory.getCanonicalFile().toString()));
        // test that resolved location is canonical, despite cache directory not being canonical
        Assert.assertEquals(
                resolvedLocation,
                URLs.urlToFile((new URI(resolvedLocation)).toURL())
                        .getCanonicalFile()
                        .toURI()
                        .toString());
    }
}
