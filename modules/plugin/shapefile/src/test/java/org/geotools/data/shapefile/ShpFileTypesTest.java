/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

public class ShpFileTypesTest extends TestCase {

    public void testToFileBase() {

        ShpFileType[] values = ShpFileType.values();
        for (ShpFileType value : values) {
            assertToFileBase(value);
        }

    }

    public void testToURLBase() throws MalformedURLException {

        ShpFileType[] values = ShpFileType.values();
        for (ShpFileType value : values) {
            assertToURLBase(value);
        }

    }

    private void assertToURLBase(ShpFileType type) throws MalformedURLException {
        String urlString = "file://c:/shapefiles/file1." + type.extension;
        URL url = new URL(urlString);

        ShpFileType[] values = ShpFileType.values();
        for (ShpFileType value : values) {
            if (value != type) {
                assertNull(value.toBase(url));
            } else {
                assertEquals("file://c:/shapefiles/file1", value.toBase(url));
            }
        }
    }

    private void assertToFileBase(ShpFileType type) {
        File file = new File("c:\\shapefiles\\file1." + type.extension);

        ShpFileType[] values = ShpFileType.values();
        for (ShpFileType value : values) {
            if (value != type) {
                assertNull(value.toBase(file));
            } else {
                assertEquals("c:\\shapefiles\\file1", value.toBase(file));
            }
        }
    }

    public void testNoExtension() throws Exception {
        File noExtension = new File("name.");
        assertNull(ShpFileType.DBF.toBase(noExtension));
    }

    public void testNoBaseName() throws Exception {
        File noBase = new File(".dbf");
        assertNull(ShpFileType.DBF.toBase(noBase));
    }

    public void testNoBaseNameMixedCase() throws Exception {
        File noBase = new File(".dbF");
        assertNull(ShpFileType.DBF.toBase(noBase));
    }

    public void testUppercase() throws Exception {
        File file = new File("BLOOB.DBF");
        assertEquals("BLOOB", ShpFileType.DBF.toBase(file));
    }

    public void testMixedcase() throws Exception {
        File file = new File("Beebop.dBf");
        assertEquals("Beebop", ShpFileType.DBF.toBase(file));
    }

}
