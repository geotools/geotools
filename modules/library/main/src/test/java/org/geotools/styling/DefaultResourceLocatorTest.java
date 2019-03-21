/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import junit.framework.TestCase;
import org.geotools.util.URLs;

public class DefaultResourceLocatorTest extends TestCase {
    public void testRelativeFileURL() throws Exception {
        DefaultResourceLocator locator = new DefaultResourceLocator();
        locator.setSourceUrl(getClass().getResource("test-data/blob.gif"));

        checkURL(locator.locateResource("blob.gif"));
        checkURL(locator.locateResource("file:blob.gif"));
        checkURL(locator.locateResource("file://blob.gif"));
        checkURL(locator.locateResource("file://./blob.gif"));
    }

    public void testPreserveURLQuery() throws Exception {
        DefaultResourceLocator locator = new DefaultResourceLocator();
        locator.setSourceUrl(getClass().getResource("test-data/blob.gif"));

        // Confirm still able to resolve to a File
        URL url = locator.locateResource("blob.gif?query=parameter");
        assertEquals("query=parameter", url.getQuery());
        File file = URLs.urlToFile(url);
        assertTrue(file.exists());
    }

    public void testPreserveURLQueryWithColors() throws Exception {
        DefaultResourceLocator locator = new DefaultResourceLocator();
        locator.setSourceUrl(getClass().getResource("test-data/blob.gif"));

        // Confirm still able to resolve to a File
        URL url = locator.locateResource("blob.gif?fill=#ff0000&stroke=#000000");
        final String external = url.toExternalForm();
        assertTrue(external.indexOf('?') > 0);
        assertEquals("fill=#ff0000&stroke=#000000", external.split("\\?")[1]);
        File file = URLs.urlToFile(url);
        assertTrue(file.exists());
    }

    void checkURL(URL url) {
        File f = URLs.urlToFile(url);
        assertTrue(f.exists());
    }

    public void testInvalidPath() throws Exception {
        DefaultResourceLocator locator = new DefaultResourceLocator();
        URL testURL =
                new URL(
                        "resource",
                        null,
                        -1,
                        "target",
                        new URLStreamHandler() {

                            @Override
                            protected URLConnection openConnection(URL u) throws IOException {
                                return new URLConnection(u) {

                                    @Override
                                    public void connect() throws IOException {}

                                    @Override
                                    public long getLastModified() {
                                        return 0;
                                    }

                                    @Override
                                    public InputStream getInputStream() throws IOException {
                                        return new ByteArrayInputStream(new byte[0]);
                                    }

                                    @Override
                                    public OutputStream getOutputStream() throws IOException {
                                        return new ByteArrayOutputStream();
                                    }
                                };
                            }
                        });
        locator.setSourceUrl(testURL);
        // used to go NPE here
        assertEquals(new URL("file://test"), locator.locateResource("file://test"));
    }
}
