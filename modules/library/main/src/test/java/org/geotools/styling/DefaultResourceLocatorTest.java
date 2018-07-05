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

import java.io.File;
import java.net.URL;
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
}
