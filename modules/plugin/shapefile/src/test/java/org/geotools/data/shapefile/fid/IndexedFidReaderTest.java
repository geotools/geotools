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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

/**
 * 
 * 
 * @source $URL$
 */
public class IndexedFidReaderTest extends FIDTestCase {

    private IndexedFidReader reader;

    private IndexFile indexFile;

    private ShpFiles shpFiles;

    @Before
    public void setUpIndexedFidReaderTest() throws Exception {
        shpFiles = new ShpFiles(backshp.toURI().toURL());
        FidIndexer.generate(shpFiles);

        indexFile = new IndexFile(shpFiles, false);
        reader = new IndexedFidReader(shpFiles);
    }

    @After
    public void closeFiles() throws Exception {
        reader.close();
        indexFile.close();
        shpFiles.dispose();

        super.tearDown();
    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidReader.findFid(String)'
     */
    @Test
    public void testFindFid() throws Exception {
        long offset = reader.findFid(TYPE_NAME + ".4");
        assertEquals(3, offset);

        offset = reader.findFid(TYPE_NAME + ".1");
        assertEquals(0, offset);

        // test if the fid is too high
        offset = reader.findFid(TYPE_NAME + ".10000000");
        assertEquals(-1, offset);

        // test if the fid is negative
        offset = reader.findFid(TYPE_NAME + ".-1");
        assertEquals(-1, offset);

        // test if the fid does not match the <typeName>.<long> pattern
        offset = reader.findFid(TYPE_NAME + ".1ABC");
        assertEquals(-1, offset);

        offset = reader.findFid("prefix" + TYPE_NAME + ".1");
        assertEquals(-1, offset);
    }

    @Test
    public void testFindAllFids() throws Exception {
        int expectedCount = 0;
        Set<String> expectedFids = new LinkedHashSet<String>();
        {
            ShapefileDataStore ds = new ShapefileDataStore(backshp.toURI().toURL());
            SimpleFeatureSource featureSource = ds.getFeatureSource();
            SimpleFeatureIterator features = featureSource.getFeatures().features();
            while (features.hasNext()) {
                SimpleFeature next = features.next();
                expectedCount++;
                expectedFids.add(next.getID());
            }
            features.close();
            ds.dispose();
        }

        assertTrue(expectedCount > 0);
        assertEquals(expectedCount, reader.getCount());

        for (String fid : expectedFids) {
            long offset = reader.findFid(fid);
            assertFalse(-1 == offset);
        }
    }

    @Test
    public void testFindAllFidsReverseOrder() throws Exception {
        int expectedCount = 0;
        Set<String> expectedFids = new TreeSet<String>(Collections.reverseOrder());
        {
            ShapefileDataStore ds = new ShapefileDataStore(backshp.toURI().toURL());
            SimpleFeatureSource featureSource = ds.getFeatureSource();
            SimpleFeatureIterator features = featureSource.getFeatures().features();
            while (features.hasNext()) {
                SimpleFeature next = features.next();
                expectedCount++;
                expectedFids.add(next.getID());
            }
            features.close();
            ds.dispose();
        }

        assertTrue(expectedCount > 0);
        assertEquals(expectedCount, reader.getCount());

        assertFalse("findFid for archsites.5 returned -1", -1 == reader.findFid("archsites.5"));
        assertFalse("findFid for archsites.25 returned -1", -1 == reader.findFid("archsites.25"));

        for (String fid : expectedFids) {
            long offset = reader.findFid(fid);
            assertNotNull(offset);
            // System.out.print(fid + "=" + offset + ", ");
            assertFalse("findFid for " + fid + " returned -1", -1 == offset);
        }
    }

    // test if FID no longer exists.
    @Test
    public void testFindDeletedFID() throws Exception {
        reader.close();

        ShpFiles shpFiles = new ShpFiles(fixFile);
        IndexedFidWriter writer = new IndexedFidWriter(shpFiles);
        try {
            writer.next();
            writer.next();
            writer.next();
            writer.remove();
            while (writer.hasNext()) {
                writer.next();
            }
        } finally {
            writer.close();
            reader.close();
        }

        reader = new IndexedFidReader(shpFiles);

        long offset = reader.findFid(TYPE_NAME + ".11");
        assertEquals(9, offset);

        offset = reader.findFid(TYPE_NAME + ".4");
        assertEquals(2, offset);

        offset = reader.findFid(TYPE_NAME + ".3");
        assertEquals(-1, offset);

    }

    @Test
    public void testHardToFindFid() throws Exception {
        long offset = reader.search(5, 3, 7, 5);
        assertEquals(4, offset);
    }

    /*
     * Test method for 'org.geotools.index.fid.IndexedFidReader.goTo(int)'
     */
    @Test
    public void testGoTo() throws IOException {
        reader.goTo(10);
        assertEquals(shpFiles.getTypeName() + ".11", reader.next());
        assertTrue(reader.hasNext());

        reader.goTo(15);
        assertEquals(shpFiles.getTypeName() + ".16", reader.next());
        assertTrue(reader.hasNext());

        reader.goTo(0);
        assertEquals(shpFiles.getTypeName() + ".1", reader.next());
        assertTrue(reader.hasNext());

        reader.goTo(3);
        assertEquals(shpFiles.getTypeName() + ".4", reader.next());
        assertTrue(reader.hasNext());
    }
}
