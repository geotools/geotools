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

import static org.geotools.data.shapefile.files.ShpFileType.DBF;
import static org.geotools.data.shapefile.files.ShpFileType.SHP;
import static org.geotools.data.shapefile.files.ShpFileType.SHX;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import org.geotools.data.shapefile.files.FileWriter;
import org.geotools.data.shapefile.files.Result;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.files.ShpFiles.State;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShpFilesLockingTest implements FileWriter {

    @Before
    public void setUp() throws Exception {
        getClass().getClassLoader().setDefaultAssertionStatus(true);
    }

    @After
    public void tearDown() throws Exception {
        Runtime.getRuntime().runFinalization();
    }

    @Test
    public void testAcquireReadFile() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        try {
            shpFiles.acquireReadFile(DBF, this);
            fail("Not a file should send exception");
        } catch (IllegalStateException e) {
            // good
        }

        String path = "somefile.shp";
        shpFiles = new ShpFiles(new File(path));

        File file = shpFiles.acquireReadFile(SHP, this);
        // under windows the two paths can be just different in terms of case..
        assertEquals(new File(path).getCanonicalPath().toLowerCase(), file.getPath().toLowerCase());
        assertEquals(1, shpFiles.numberOfLocks());

        shpFiles.unlockRead(file, this);
        shpFiles.dispose();
    }

    @Test
    public void testAcquireWriteFile() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        try {
            shpFiles.acquireWriteFile(DBF, this);
            fail("Not a file should send exception");
        } catch (IllegalStateException e) {
            // good
        }

        String path = "somefile.shp";
        shpFiles = new ShpFiles(new File(path));

        File file = shpFiles.acquireWriteFile(SHP, this);
        // under windows the two paths can be just different in terms of case..
        assertEquals(new File(path).getCanonicalPath().toLowerCase(), file.getPath().toLowerCase());
        assertEquals(1, shpFiles.numberOfLocks());

        shpFiles.unlockWrite(file, this);
        assertEquals(0, shpFiles.numberOfLocks());
        shpFiles.dispose();
    }

    @Test
    public void testAcquireRead1() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter =
                new FileWriter() {

                    public String id() {
                        return "Other";
                    }
                };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireRead(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value.toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        // same thread should work
        Result<URL, State> result2 = shpFiles.tryAcquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", result2.value.toExternalForm());
        assertEquals(3, shpFiles.numberOfLocks());

        shpFiles.unlockRead(result2.value, this);
        shpFiles.unlockRead(result1.value, testWriter);
        shpFiles.unlockRead(url, this);
        shpFiles.dispose();
    }

    @Test
    public void testUnlockReadAssertion() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter =
                new FileWriter() {

                    public String id() {
                        return "Other";
                    }
                };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireRead(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value.toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        try {
            shpFiles.unlockRead(result1.value, this);
            throw new RuntimeException("Unlock should fail because it is in the wrong reader");
        } catch (IllegalArgumentException e) {
            // good
        } catch (RuntimeException e) {
            fail(e.getMessage());
        }

        shpFiles.unlockRead(result1.value, testWriter);
        shpFiles.unlockRead(url, this);
        shpFiles.dispose();
    }

    @Test
    public void testUnlockWriteAssertion() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireWrite(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter =
                new FileWriter() {

                    public String id() {
                        return "Other";
                    }
                };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireWrite(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value.toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        try {
            shpFiles.unlockRead(result1.value, this);
            throw new RuntimeException("Unlock should fail because it is in the wrong reader");
        } catch (IllegalArgumentException e) {
            // good
        } catch (RuntimeException e) {
            fail(e.getMessage());
        }

        shpFiles.unlockWrite(url, this);
        shpFiles.unlockWrite(result1.value, testWriter);
        shpFiles.dispose();
    }

    public String id() {
        return getClass().getName();
    }
}
