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

import static org.geotools.data.shapefile.files.ShpFileType.PRJ;
import static org.geotools.data.shapefile.files.ShpFileType.SHP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import org.geotools.data.shapefile.files.FileReader;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.files.StorageFile;
import org.junit.Before;
import org.junit.Test;

public class StorageFileTest implements FileReader {

    private ShpFiles shpFiles1;
    private ShpFiles shpFiles2;

    @Before
    public void setUp() throws Exception {
        Map<ShpFileType, File> files1 = ShpFilesTest.createFiles("Files1", ShpFileType.values(), false);
        Map<ShpFileType, File> files2 = ShpFilesTest.createFiles("Files2", ShpFileType.values(), false);

        shpFiles1 = new ShpFiles(files1.get(SHP));
        shpFiles2 = new ShpFiles(files2.get(SHP));
    }

    @Test
    public void testReplaceOriginal() throws Exception {
        ShpFiles files1 = shpFiles1;
        ShpFileType type = PRJ;
        StorageFile storagePRJ1 = files1.getStorageFile(type);
        File original = storagePRJ1.getFile();

        try {
            String writtenToStorageFile = "Copy";

            writeData(storagePRJ1, writtenToStorageFile);

            storagePRJ1.replaceOriginal();
            assertEquals(0, files1.numberOfLocks());

            assertCorrectData(files1, type, writtenToStorageFile);
        } catch (Exception e) {
            storagePRJ1.getFile().delete();
            original.delete();
        }
    }

    private void writeData(StorageFile storage, String writtenToStorageFile) throws IOException {
        File file = storage.getFile();
        file.deleteOnExit();

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(writtenToStorageFile);
        }
    }

    private void assertCorrectData(ShpFiles files1, ShpFileType type, String writtenToStorageFile) throws IOException {
        try (ReadableByteChannel channel = files1.getReadChannel(type, this)) {
            ByteBuffer buffer = ByteBuffer.allocate(20);
            channel.read(buffer);
            buffer.flip();
            String data = new String(buffer.array(), StandardCharsets.UTF_8).trim();
            assertEquals(writtenToStorageFile, data);
        }
    }

    @Test
    public void testReplaceOriginals() throws Exception {

        StorageFile storagePRJ1 = shpFiles1.getStorageFile(PRJ);
        StorageFile storageSHP1 = shpFiles1.getStorageFile(SHP);
        StorageFile storagePRJ2 = shpFiles2.getStorageFile(PRJ);
        StorageFile storageSHP2 = shpFiles2.getStorageFile(SHP);

        try {
            String sPRJ1 = "storagePRJ1";
            String sSHP1 = "storageSHP1";
            String sPRJ2 = "storagePRJ2";
            String sSHP2 = "storageSHP2";

            writeData(storagePRJ1, sPRJ1);
            writeData(storageSHP1, sSHP1);
            writeData(storagePRJ2, sPRJ2);
            writeData(storageSHP2, sSHP2);

            StorageFile.replaceOriginals(storagePRJ1, storagePRJ2, storageSHP1, storageSHP2, storageSHP2);

            this.assertCorrectData(shpFiles1, PRJ, sPRJ1);
            this.assertCorrectData(shpFiles1, SHP, sSHP1);
            this.assertCorrectData(shpFiles2, PRJ, sPRJ2);
            this.assertCorrectData(shpFiles2, SHP, sSHP2);

            assertEquals(0, shpFiles1.numberOfLocks());
            assertEquals(0, shpFiles2.numberOfLocks());
        } finally {
            storagePRJ1.getFile().delete();
            storagePRJ2.getFile().delete();
            storageSHP1.getFile().delete();
            storageSHP2.getFile().delete();
        }
    }

    @Test
    public void testReplaceOriginalsEmptyArgs() throws Exception {

        StorageFile.replaceOriginals(new StorageFile[0]);
    }

    @Test
    public void testCompareTo() throws IOException {
        StorageFile storagePRJ1 = shpFiles1.getStorageFile(PRJ);
        StorageFile storageSHP1 = shpFiles1.getStorageFile(SHP);
        StorageFile storagePRJ2 = shpFiles2.getStorageFile(PRJ);
        StorageFile storageSHP2 = shpFiles2.getStorageFile(SHP);

        try {
            assertNotEquals(0, storagePRJ1.compareTo(storageSHP1));
            assertNotEquals(0, storagePRJ1.compareTo(storagePRJ2));

            StorageFile[] array = {storagePRJ1, storagePRJ2, storageSHP1, storageSHP2};

            Arrays.sort(array);

            assertNotEquals(0, array[0].compareTo(array[1]));
            assertNotEquals(0, array[2].compareTo(array[3]));
            assertNotEquals(0, array[1].compareTo(array[2]));
        } finally {
            storagePRJ1.getFile().delete();
            storagePRJ2.getFile().delete();
            storageSHP1.getFile().delete();
            storageSHP2.getFile().delete();
        }
    }

    @Override
    public String id() {
        return getClass().getName();
    }
}
