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
import static org.geotools.data.shapefile.ShpFileType.PRJ;
import static org.geotools.data.shapefile.ShpFileType.SHP;
import static org.geotools.data.shapefile.ShpFileType.SHX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

public class ShpFilesTest extends TestCase{

    public void testCaseURL() throws Exception {
        assertCorrectCase(true);
        assertCorrectCase(false);
    }


    private void assertCorrectCase(boolean uppercase)
            throws MalformedURLException {
        String base = "http://someURL.com/file.";
        
        String shp = SHP.extension.toLowerCase();
        String dbf = DBF.extension.toLowerCase();
        String shx = SHX.extension.toLowerCase();

        if( uppercase ){
            shp = shp.toUpperCase();
            dbf = dbf.toUpperCase();
            shx = shx.toUpperCase();
        }
        
        ShpFiles files = new ShpFiles(base+shp);
        
        BasicShpFileWriter requestor = new BasicShpFileWriter("testCaseURL");
        URL shpURL = files.acquireRead(SHP, requestor);
        URL dbfURL = files.acquireRead(DBF, requestor);
        URL shxURL = files.acquireRead(SHX, requestor);
        try{
            assertEquals(base+shp, shpURL.toExternalForm());
            assertEquals(base+dbf, dbfURL.toExternalForm());
            assertEquals(base+shx, shxURL.toExternalForm());
        }finally{
            files.unlockRead(shpURL, requestor);
            files.unlockRead(dbfURL, requestor);
            files.unlockRead(shxURL, requestor);
        }
    }
    

    public void testCaseFile() throws Exception {
        Map<ShpFileType, File> files = createFiles("testCaseFile", ShpFileType.values(), true);
        
        String fileName = files.get(SHP).getPath();
        fileName = fileName.substring(0, fileName.length()-4)+".shp";
        ShpFiles shpFiles = new ShpFiles(fileName);

        BasicShpFileWriter requestor = new BasicShpFileWriter("testCaseFile");
        URL shpURL = shpFiles.acquireRead(SHP, requestor);
        URL dbfURL = shpFiles.acquireRead(DBF, requestor);
        URL shxURL = shpFiles.acquireRead(SHX, requestor);
        try{
            assertEquals(files.get(SHP).toURI().toURL().toExternalForm(), shpURL.toExternalForm());
            assertEquals(files.get(DBF).toURI().toURL().toExternalForm(), dbfURL.toExternalForm());
            assertEquals(files.get(SHX).toURI().toURL().toExternalForm(), shxURL.toExternalForm());
        }finally{
            shpFiles.unlockRead(shpURL, requestor);
            shpFiles.unlockRead(dbfURL, requestor);
            shpFiles.unlockRead(shxURL, requestor);
        }
        
        
    }
    
    public static Map<ShpFileType, File> createFiles(String string,
            ShpFileType[] values, boolean uppercase) throws IOException {
        Map<ShpFileType, File> files = new HashMap<ShpFileType, File>();

        String extensionWithPeriod = values[0].extensionWithPeriod;
        File baseFile = File.createTempFile(string,
                extensionWithPeriod);
        baseFile.createNewFile();
        baseFile.deleteOnExit();

        files.put(values[0], baseFile);

        String baseFileName = values[0].toBase(baseFile);

        for (int i = 1; i < values.length; i++) {
            ShpFileType type = values[i];
            String extension = type.extensionWithPeriod;
            File file = new File(baseFileName + extension);
            file.createNewFile();
            file.deleteOnExit();
            files.put(type, file);
        }

        return files;
    }

    public void testShapefileFilesAll() throws Exception {
        Map<ShpFileType, File> expected = createFiles("testShapefileFilesAll",
                ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file);

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    public void testURLStringConstructor() throws Exception {
        Map<ShpFileType, File> expected = createFiles(
                "testURLStringConstructor", ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file.toURI().toURL()
                .toExternalForm());

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    public void testFileStringConstructor() throws Exception {
        Map<ShpFileType, File> expected = createFiles(
                "testFileStringConstructor", ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file.getPath());

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    public void testShapefileFilesSome() throws Exception {
        Map<ShpFileType, File> expected = createFiles("testShapefileFilesSome",
                new ShpFileType[] { SHP, DBF, SHX, PRJ }, false);

        File prj = expected.remove(PRJ);

        ShpFiles shapefiles = new ShpFiles(prj);

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    public void testBadFormat() throws Exception {
        try {
            new ShpFiles("SomeName.woo");
            fail("The file is not one of the files types associated with a shapefile therefore the ShapefileFiles class should not be constructable");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    public void testFileInNonExistingDirectory() throws Exception {
        try {
            new ShpFiles(new File("nowhere/test.shp"));
            // ok
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " should not be thrown");
        }
    }

    public void testNonFileURLs() throws IOException {
        Map<ShpFileType, URL> expected = new HashMap<ShpFileType, URL>();
        String base = "http://www.geotools.org/testFile";
        ShpFileType[] types = ShpFileType.values();
        for (ShpFileType type : types) {
            expected.put(type, new URL(base + type.extensionWithPeriod));
        }

        ShpFiles shapefiles = new ShpFiles(expected.get(SHP));

        Map<ShpFileType, String> files = shapefiles.getFileNames();

        Set<Entry<ShpFileType, URL>> expectedEntries = expected.entrySet();
        for (Entry<ShpFileType, URL> entry : expectedEntries) {
            assertEquals(entry.getValue().toExternalForm(), files.get(entry
                    .getKey()));
        }
    }

    public void testFileURLs() throws Exception {
        Map<ShpFileType, File> expected = createFiles("testShapefileFilesAll",
                ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file.toURI().toURL());

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    private void assertEqualMaps(Map<ShpFileType, File> expected,
            Map<ShpFileType, String> files) throws MalformedURLException {

        Set<Entry<ShpFileType, File>> expectedEntries = expected.entrySet();
        for (Entry<ShpFileType, File> entry : expectedEntries) {
            assertEquals(entry.getValue().toURI().toURL().toExternalForm(),
                    files.get(entry.getKey()));
        }
    }

}
