/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.data.DataSourceException;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.URLs;
import org.geotools.util.factory.GeoTools;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

public class ShapefileDescriptorLeakTest {

    @Rule public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testFileDescriptorLeakInvalidShp() throws IOException {
        // leak existed before GEOT-7088 fix for any shapefile with an invalid .shp file
        ShapefileDataStore store = createTestDataStore(ShpFileType.SHP);
        try {
            assertThrows(
                    BufferUnderflowException.class, () -> store.getFeatureSource().getFeatures());
            // verify that all opened file channels were closed
            assertEquals(0, store.shpFiles.numberOfLocks());
        } finally {
            store.dispose();
        }
        // file deletion will fail if there are open file descriptors
        FileUtils.deleteDirectory(folder.getRoot());
    }

    @Test
    public void testFileDescriptorLeakInvalidDbf() throws IOException {
        // leak existed before GEOT-7088 fix for any shapefile with an invalid .dbf file
        ShapefileDataStore store = createTestDataStore(ShpFileType.DBF);
        try {
            assertNotNull(store.getFeatureSource().getFeatures());
            // verify that all opened file channels were closed
            assertEquals(0, store.shpFiles.numberOfLocks());
        } finally {
            store.dispose();
        }
        // file deletion will fail if there are open file descriptors
        FileUtils.deleteDirectory(folder.getRoot());
    }

    @Test
    public void testFileDescriptorLeakInvalidShx() throws IOException {
        // leak did not exist
        ShapefileDataStore store = createTestDataStore(ShpFileType.SHX);
        try {
            assertNotNull(store.getFeatureSource().getFeatures());
            // verify that all opened file channels were closed
            assertEquals(0, store.shpFiles.numberOfLocks());
        } finally {
            store.dispose();
        }
        // file deletion will fail if there are open file descriptors
        FileUtils.deleteDirectory(folder.getRoot());
    }

    @Test
    public void testFileDescriptorLeakInvalidPrj() throws IOException {
        // leak did not exist
        ShapefileDataStore store = createTestDataStore(ShpFileType.PRJ);
        try {
            assertNotNull(store.getFeatureSource().getFeatures());
            // verify that all opened file channels were closed
            assertEquals(0, store.shpFiles.numberOfLocks());
        } finally {
            store.dispose();
        }
        // file deletion will fail if there are open file descriptors
        FileUtils.deleteDirectory(folder.getRoot());
    }

    @Test
    public void testFileDescriptorLeakInvalidQix() throws IOException {
        // leak did not exist
        ShapefileDataStore store = createTestDataStore(ShpFileType.QIX);
        try {
            // load the features with a spatial filter
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
            BBOX bbox = ff.bbox("", -180, -90, 180, 90, null);
            SimpleFeatureCollection features =
                    store.getFeatureSource().getFeatures().subCollection(bbox);
            RuntimeException exception =
                    assertThrows(RuntimeException.class, () -> features.features());
            assertThat(exception.getCause(), instanceOf(DataSourceException.class));
            // verify that all opened file channels were closed
            assertEquals(0, store.shpFiles.numberOfLocks());
        } finally {
            store.dispose();
        }
        // file deletion will fail if there are open file descriptors
        FileUtils.deleteDirectory(folder.getRoot());
    }

    @Test
    public void testFileDescriptorLeakInvalidFix() throws IOException {
        // leak existed before GEOT-7088 fix for any shapefile with an invalid .fix file
        ShapefileDataStore store = createTestDataStore(ShpFileType.FIX);
        try {
            SimpleFeatureCollection features = store.getFeatureSource().getFeatures();
            RuntimeException exception =
                    assertThrows(RuntimeException.class, () -> features.features());
            assertThat(exception.getCause(), instanceOf(IOException.class));
            // verify that all opened file channels were closed
            assertEquals(0, store.shpFiles.numberOfLocks());
        } finally {
            store.dispose();
        }
        // file deletion will fail if there are open file descriptors
        FileUtils.deleteDirectory(folder.getRoot());
    }

    @Test
    public void testFileDescriptorLeakInvalidCpg() throws IOException {
        // leak did not exist
        ShapefileDataStore store = createTestDataStore(ShpFileType.CPG);
        try {
            store.setTryCPGFile(true);
            assertNotNull(store.getFeatureSource().getFeatures());
            // verify that all opened file channels were closed
            assertEquals(0, store.shpFiles.numberOfLocks());
        } finally {
            store.dispose();
        }
        // file deletion will fail if there are open file descriptors
        FileUtils.deleteDirectory(folder.getRoot());
    }

    private ShapefileDataStore createTestDataStore(ShpFileType type) throws IOException {
        // copy the valid test shapefile to a temp directory
        String dstName = "invalid-" + type.extension;
        copyFile(dstName, ShpFileType.DBF);
        copyFile(dstName, ShpFileType.SHP);
        copyFile(dstName, ShpFileType.SHX);
        // put invalid contents into the file to test
        File testFile = new File(folder.getRoot(), dstName + type.extensionWithPeriod);
        FileUtils.write(testFile, "0", StandardCharsets.UTF_8);
        // create the data store
        File shapeFile = new File(folder.getRoot(), dstName + ShpFileType.SHP.extensionWithPeriod);
        return new ShapefileDataStore(URLs.fileToUrl(shapeFile));
    }

    private void copyFile(String dstName, ShpFileType type) throws IOException {
        File srcFile =
                TestData.file(this, "empty-shapefile/empty-shapefile" + type.extensionWithPeriod);
        File dstFile = new File(folder.getRoot(), dstName + type.extensionWithPeriod);
        FileUtils.copyFile(srcFile, dstFile);
    }
}
