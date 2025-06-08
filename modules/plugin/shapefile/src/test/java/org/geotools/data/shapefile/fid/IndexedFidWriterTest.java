/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.fid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.data.shapefile.shp.IndexFile;
import org.junit.After;
import org.junit.Test;

public class IndexedFidWriterTest extends FIDTestCase {
    private IndexFile indexFile;
    private IndexedFidWriter writer;

    private void initWriter() throws IOException, MalformedURLException {
        close();
        indexFile = new IndexFile(shpFiles, false);
        writer = new IndexedFidWriter(shpFiles);
    }

    @After
    public void close() throws IOException {
        if (writer != null && !writer.isClosed()) {
            writer.close();
        }

        try {
            if (indexFile != null) {
                indexFile.close();
            }
        } catch (Exception e) {
            // fine if already closed
        }
    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidWriter.hasNext()'
     */
    @Test
    public void testHasNext() throws MalformedURLException, IOException {
        FidIndexer.generate(backshp.toURI().toURL());
        initWriter();

        for (int i = 1, j = indexFile.getRecordCount(); i < j; i++) {
            assertTrue(i + "th record", writer.hasNext());
            assertEquals(i, writer.next());
        }
    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidWriter.remove()'
     */
    @Test
    public void testRemove() throws MalformedURLException, IOException {
        FidIndexer.generate(backshp.toURI().toURL());
        initWriter();
        writer.next();
        writer.remove();

        for (int i = 2, j = indexFile.getRecordCount(); i < j; i++) {
            assertTrue(writer.hasNext());
            assertEquals(i, writer.next());
        }

        writer.write();
        close();

        initWriter();

        for (int i = 1, j = indexFile.getRecordCount() - 1; i < j; i++) {
            assertTrue(writer.hasNext());
            assertEquals((long) i + 1, writer.next());
        }
    }

    @Test
    public void testRemoveCounting() throws Exception {
        FidIndexer.generate(backshp.toURI().toURL());
        initWriter();
        writer.next();
        writer.remove();
        writer.next();
        writer.remove();
        writer.next();
        writer.remove();

        while (writer.hasNext()) {
            writer.next();
            writer.write();
        }

        close();
        try (IndexedFidReader reader = new IndexedFidReader(shpFiles)) {
            assertEquals(3, reader.getRemoves());
        }

        // remove some more features
        initWriter();
        writer.next();
        writer.next();
        writer.next();
        writer.remove();
        writer.next();
        writer.remove();
        writer.next();
        writer.next();
        writer.next();
        writer.remove();
        while (writer.hasNext()) {
            writer.next();
            writer.write();
        }

        close();

        try (IndexedFidReader reader = new IndexedFidReader(shpFiles)) {
            assertEquals(6, reader.getRemoves());
        }
    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidWriter.write()'
     */
    @Test
    public void testWrite() throws IOException {
        initWriter();

        for (int i = 0; i < 5; i++) {
            writer.next();
            writer.write();
        }

        initWriter();
        for (int i = 1; i <= 5; i++) {
            assertTrue(writer.hasNext());
            assertEquals(i, writer.next());
        }
    }
}
