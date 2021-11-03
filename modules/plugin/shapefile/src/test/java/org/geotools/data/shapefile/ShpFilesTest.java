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
import static org.geotools.data.shapefile.files.ShpFileType.PRJ;
import static org.geotools.data.shapefile.files.ShpFileType.SHP;
import static org.geotools.data.shapefile.files.ShpFileType.SHP_XML;
import static org.geotools.data.shapefile.files.ShpFileType.SHX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.geotools.data.shapefile.files.BasicShpFileWriter;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.junit.Test;

public class ShpFilesTest {

    @Test
    public void testCaseURL() throws Exception {
        assertCorrectCase(true);
        assertCorrectCase(false);
    }

    private void assertCorrectCase(boolean uppercase) throws MalformedURLException {
        String base = "http://someURL.com/file.";

        String shp = SHP.extension.toLowerCase();
        String dbf = DBF.extension.toLowerCase();
        String shx = SHX.extension.toLowerCase();

        if (uppercase) {
            shp = shp.toUpperCase();
            dbf = dbf.toUpperCase();
            shx = shx.toUpperCase();
        }

        ShpFiles files = new ShpFiles(base + shp);

        BasicShpFileWriter requestor = new BasicShpFileWriter("testCaseURL");
        URL shpURL = files.acquireRead(SHP, requestor);
        URL dbfURL = files.acquireRead(DBF, requestor);
        URL shxURL = files.acquireRead(SHX, requestor);
        try {
            assertEquals(base + shp, shpURL.toExternalForm());
            assertEquals(base + dbf, dbfURL.toExternalForm());
            assertEquals(base + shx, shxURL.toExternalForm());
        } finally {
            files.unlockRead(shpURL, requestor);
            files.unlockRead(dbfURL, requestor);
            files.unlockRead(shxURL, requestor);
        }
    }

    @Test
    public void testCaseFile() throws Exception {
        Map<ShpFileType, File> files = createFiles("testCaseFile", ShpFileType.values(), true);

        String fileName = files.get(SHP).getPath();
        fileName = fileName.substring(0, fileName.length() - 4) + ".shp";
        ShpFiles shpFiles = new ShpFiles(fileName);

        BasicShpFileWriter requestor = new BasicShpFileWriter("testCaseFile");
        URL shpURL = shpFiles.acquireRead(SHP, requestor);
        URL dbfURL = shpFiles.acquireRead(DBF, requestor);
        URL shxURL = shpFiles.acquireRead(SHX, requestor);
        try {
            assertEquals(files.get(SHP).toURI().toURL().toExternalForm(), shpURL.toExternalForm());
            assertEquals(files.get(DBF).toURI().toURL().toExternalForm(), dbfURL.toExternalForm());
            assertEquals(files.get(SHX).toURI().toURL().toExternalForm(), shxURL.toExternalForm());
        } finally {
            shpFiles.unlockRead(shpURL, requestor);
            shpFiles.unlockRead(dbfURL, requestor);
            shpFiles.unlockRead(shxURL, requestor);
        }
    }

    @Test
    public void testCaseFileSkipScan() throws Exception {
        Map<ShpFileType, File> files = createFiles("TEST", ShpFileType.values(), true);

        String fileName = files.get(SHP).getPath();
        fileName = fileName.substring(0, fileName.length() - 4) + ".shp";
        File file = new File(fileName);

        String shpXml = files.get(SHP_XML).getPath();
        File shpxmlFile = new File(shpXml);
        shpxmlFile.delete();

        File shpXMLFile = new File(shpXml.replace(".shp.xml", ".shp.XML"));
        shpXMLFile.createNewFile();
        shpXMLFile.deleteOnExit();

        // skipScan isn't set. The Lookup will find .shp.XML and update the mapping
        // by associating it to the SHP_XML type.
        ShpFiles shpFiles = new ShpFiles(file);
        assertTrue(shpFiles.exists(SHP_XML));

        // skipScan is set. When testing on a Linux environment, the .shp.XML
        // won't be found since the mapping wasn't updated
        shpFiles = new ShpFiles(file, true);
        File caseSensitiveFile = new File("TEST.TXT");
        caseSensitiveFile.createNewFile();
        caseSensitiveFile.deleteOnExit();
        File checkCaseFile = new File("test.txt");
        boolean isCaseSensitive = !checkCaseFile.exists();
        assertEquals(shpFiles.exists(SHP_XML), !isCaseSensitive);
    }

    @Test
    public void testGetTypeName() throws Exception {
        assertEquals("shape", new ShpFiles("dir/shape.shp").getTypeName());
        assertEquals(".shape", new ShpFiles("dir/.shape.shp").getTypeName());
    }

    public static Map<ShpFileType, File> createFiles(
            String string, ShpFileType[] values, boolean uppercase) throws IOException {
        Map<ShpFileType, File> files = new HashMap<>();

        String extensionWithPeriod = values[0].extensionWithPeriod;
        File baseFile = File.createTempFile(string, extensionWithPeriod);
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

    @Test
    public void testShapefileFilesAll() throws Exception {
        Map<ShpFileType, File> expected =
                createFiles("testShapefileFilesAll", ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file);

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    @Test
    public void testURLStringConstructor() throws Exception {
        Map<ShpFileType, File> expected =
                createFiles("testURLStringConstructor", ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file.toURI().toURL().toExternalForm());

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    @Test
    public void testFileStringConstructor() throws Exception {
        Map<ShpFileType, File> expected =
                createFiles("testFileStringConstructor", ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file.getPath());

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    @Test
    public void testShapefileFilesSome() throws Exception {
        Map<ShpFileType, File> expected =
                createFiles(
                        "testShapefileFilesSome", new ShpFileType[] {SHP, DBF, SHX, PRJ}, false);

        File prj = expected.remove(PRJ);

        ShpFiles shapefiles = new ShpFiles(prj);

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    @Test
    public void testBadFormat() throws Exception {
        try {
            new ShpFiles("SomeName.woo");
            fail(
                    "The file is not one of the files types associated with a shapefile therefore the ShapefileFiles class should not be constructable");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    @Test
    public void testFileInNonExistingDirectory() throws Exception {
        try {
            new ShpFiles(new File("nowhere/test.shp"));
            // ok
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " should not be thrown");
        }
    }

    @Test
    public void testNonFileURLs() throws IOException {
        Map<ShpFileType, URL> expected = new HashMap<>();
        String base = "http://www.geotools.org/testFile";
        ShpFileType[] types = ShpFileType.values();
        for (ShpFileType type : types) {
            expected.put(type, new URL(base + type.extensionWithPeriod));
        }

        ShpFiles shapefiles = new ShpFiles(expected.get(SHP));

        Map<ShpFileType, String> files = shapefiles.getFileNames();

        Set<Entry<ShpFileType, URL>> expectedEntries = expected.entrySet();
        for (Entry<ShpFileType, URL> entry : expectedEntries) {
            assertEquals(entry.getValue().toExternalForm(), files.get(entry.getKey()));
        }
    }

    @Test
    public void testFileURLs() throws Exception {
        Map<ShpFileType, File> expected =
                createFiles("testShapefileFilesAll", ShpFileType.values(), false);

        File file = expected.values().iterator().next();
        ShpFiles shapefiles = new ShpFiles(file.toURI().toURL());

        assertEqualMaps(expected, shapefiles.getFileNames());
    }

    @Test
    public void testGetTypeNameSpecialCharacters() throws Exception {
        assertEquals(
                "Åéìòù",
                new ShpFiles("shapefile/test-data/special-characters/Åéìòù.shp").getTypeName());
    }

    private void assertEqualMaps(Map<ShpFileType, File> expected, Map<ShpFileType, String> files)
            throws MalformedURLException {

        Set<Entry<ShpFileType, File>> expectedEntries = expected.entrySet();
        for (Entry<ShpFileType, File> entry : expectedEntries) {
            assertEquals(
                    entry.getValue().toURI().toURL().toExternalForm(), files.get(entry.getKey()));
        }
    }
}
