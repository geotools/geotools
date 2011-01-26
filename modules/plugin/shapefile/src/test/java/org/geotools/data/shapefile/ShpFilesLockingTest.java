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

import static org.geotools.data.shapefile.ShpFileType.DBF;
import static org.geotools.data.shapefile.ShpFileType.SHP;
import static org.geotools.data.shapefile.ShpFileType.SHX;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.geotools.data.shapefile.ShpFiles.State;

public class ShpFilesLockingTest extends TestCase implements FileWriter {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getClass().getClassLoader().setDefaultAssertionStatus(true);
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        Runtime.getRuntime().runFinalization();
    }

    public void testAcquireReadFile() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        try{
            shpFiles.acquireReadFile(DBF, this);
            fail("Not a file should send exception");
        }catch(IllegalStateException e ){
            // good
        }
        

        String path = "somefile.shp";
        shpFiles = new ShpFiles( new File( path ));

        File file = shpFiles.acquireReadFile(SHP, this);
        assertEquals( new File(path).getCanonicalPath(), file.getPath());
        assertEquals(1, shpFiles.numberOfLocks());
        
        shpFiles.unlockRead(file, this);
        shpFiles.finalize();
    }
    public void testAcquireWriteFile() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        try {
            shpFiles.acquireWriteFile(DBF, this);
            fail("Not a file should send exception");
        } catch (IllegalStateException e) {
            // good
        }
        

        String path = "somefile.shp";
        shpFiles = new ShpFiles( new File( path ));

        File file = shpFiles.acquireWriteFile(SHP, this);
        assertEquals(new File( path ).getCanonicalPath(), file.getPath());
        assertEquals(1, shpFiles.numberOfLocks());
        
        shpFiles.unlockWrite(file, this);
        assertEquals(0, shpFiles.numberOfLocks());
        shpFiles.finalize();
    }

    public void testAcquireRead1() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter = new FileWriter() {

            public String id() {
                return "Other";
            }

        };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireRead(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value
                .toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        // same thread should work
        Result<URL, State> result2 = shpFiles.tryAcquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", result2.value
                .toExternalForm());
        assertEquals(3, shpFiles.numberOfLocks());

        shpFiles.unlockRead(result2.value, this);
        shpFiles.unlockRead(result1.value, testWriter);
        shpFiles.unlockRead(url, this);
        shpFiles.finalize();
    }

    public void testUnlockReadAssertion() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireRead(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter = new FileWriter() {

            public String id() {
                return "Other";
            }

        };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireRead(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value
                .toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        try {
            shpFiles.unlockRead(result1.value, this);
            throw new RuntimeException(
                    "Unlock should fail because it is in the wrong reader");
        } catch (IllegalArgumentException e) {
            // good
        } catch (RuntimeException e) {
            fail(e.getMessage());
        }

        shpFiles.unlockRead(result1.value, testWriter);
        shpFiles.unlockRead(url, this);
        shpFiles.finalize();
    }

    public void testUnlockWriteAssertion() throws Throwable {
        ShpFiles shpFiles = new ShpFiles("http://somefile.com/shp.shp");

        URL url = shpFiles.acquireWrite(DBF, this);
        assertEquals("http://somefile.com/shp.dbf", url.toExternalForm());
        assertEquals(1, shpFiles.numberOfLocks());
        FileWriter testWriter = new FileWriter() {

            public String id() {
                return "Other";
            }

        };

        // same thread should work
        Result<URL, State> result1 = shpFiles.tryAcquireWrite(SHX, testWriter);
        assertEquals("http://somefile.com/shp.shx", result1.value
                .toExternalForm());
        assertEquals(2, shpFiles.numberOfLocks());

        try {
            shpFiles.unlockRead(result1.value, this);
            throw new RuntimeException(
                    "Unlock should fail because it is in the wrong reader");
        } catch (IllegalArgumentException e) {
            // good
        } catch (RuntimeException e) {
            fail(e.getMessage());
        }

        shpFiles.unlockWrite(url, this);
        shpFiles.unlockWrite(result1.value, testWriter);
        shpFiles.finalize();
    }

    public String id() {
        return getClass().getName();
    }

}
