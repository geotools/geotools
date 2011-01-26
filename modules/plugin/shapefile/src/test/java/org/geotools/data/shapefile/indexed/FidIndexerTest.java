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
package org.geotools.data.shapefile.indexed;

import java.io.IOException;

import org.geotools.data.Query;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.simple.SimpleFeatureSource;

public class FidIndexerTest extends FIDTestCase {
    public  FidIndexerTest( ) throws IOException {
        super("FidIndexerTest");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'org.geotools.index.fid.FidIndexer.generate(URL)'
     */
    public void testGenerate() throws Exception {
        ShpFiles shpFiles = new ShpFiles(backshp.toURI().toURL());
        FidIndexer.generate(shpFiles);

        IndexedShapefileDataStore ds = new IndexedShapefileDataStore(backshp
                .toURI().toURL(), null, false, false, IndexType.NONE);

        SimpleFeatureSource fs = ds.getFeatureSource();
        int features = fs.getCount(Query.ALL);

        IndexedFidReader reader = new IndexedFidReader(shpFiles);

        try {
            assertEquals(features, reader.getCount());

            int i = 1;

            while (reader.hasNext()) {
                assertEquals(shpFiles.getTypeName() + "." + i, reader.next());
                assertEquals(shpFiles.getTypeName() + "." + i, i - 1, reader.currentSHXIndex());
                i++;
            }

            assertEquals(features, i - 1);
        } finally {
            reader.close();
            ds.dispose();
        }
    }
}
