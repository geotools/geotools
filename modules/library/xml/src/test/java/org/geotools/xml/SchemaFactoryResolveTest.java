/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author "Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it" */
public class SchemaFactoryResolveTest {

    private static File tempFolder = null;

    @Before
    public void setUp() throws Exception {
        // reinitialize SchemaFactory singleton instance for every test, to clean caches
        SchemaFactory.is = new SchemaFactory();
        if (tempFolder == null) {
            tempFolder = File.createTempFile("schema", "cache");
            tempFolder.delete();
            tempFolder.deleteOnExit();
            System.setProperty("schema.factory.cache", tempFolder.getAbsolutePath());
        }
    }

    @After
    public void tearDown() throws Exception {
        if (tempFolder.exists()) {
            delete(tempFolder, true);
        }
    }

    @Test
    public void testLocalPathResolve() throws Exception {
        Assert.assertNotNull(
                SchemaFactory.getInstance(
                        URI.create("http://www.w3.org/XML/1998/namespace/test"),
                        URI.create("http://geotools.org/xml/test.xsd")));
    }

    @Test
    public void testCachedPathResolve() throws Exception {
        File folder =
                new File(
                        tempFolder
                                + File.separator
                                + "org"
                                + File.separator
                                + "geotools"
                                + File.separator
                                + "xml");
        Assert.assertTrue(folder.mkdirs());

        copy("/org/geotools/xml/test.xsd", "cached.xsd", folder);
        copy("/org/geotools/xml/XMLSchema.dtd", "XMLSchema.dtd", folder);
        copy("/org/geotools/xml/datatypes.dtd", "datatypes.dtd", folder);

        Assert.assertNotNull(
                SchemaFactory.getInstance(
                        URI.create("http://www.w3.org/XML/1998/namespace/cached"),
                        // URI.create("http://geotools.org/xml/cached.xsd")));
                        new File(folder, "cached.xsd").toURI()));
    }

    @Test
    public void testRemotePathResolve() throws Exception {
        try {
            Assert.assertNotNull(
                    SchemaFactory.getInstance(
                            URI.create("http://www.w3.org/XML/1998/namespace/remote"),
                            URI.create("http://www.w3.org/2001/03/xml.xsd")));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof UnknownHostException) {
                // fine, it just means the test is running offline
                return;
            } else if (e.getMessage().startsWith("Failed to resolve")) {
                // fine, it means the server is currently broken, not our problem (found a 503)
                return;
            }
            throw e;
        }
    }

    private void delete(File f, boolean onlyContent) {
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                delete(file, false);
            }
            if (!onlyContent) {
                f.delete();
            }
        } else {
            f.delete();
        }
    }

    private void copy(String sourceFile, String name, File outFolder) throws IOException {
        try (InputStream in = this.getClass().getResource(sourceFile).openStream();
                OutputStream out =
                        new FileOutputStream(outFolder.getAbsolutePath() + File.separator + name)) {
            final byte[] buffer = new byte[4096];
            int count;
            while ((count = in.read(buffer)) >= 0) {
                out.write(buffer, 0, count);
            }
        }
    }
}
