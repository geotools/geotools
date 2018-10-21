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

import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.util.URLs;
import org.junit.Test;

public class FidIndexerTest extends FIDTestCase {

    /*
     * Test method for 'org.geotools.index.fid.FidIndexer.generate(URL)'
     */
    @Test
    public void testGenerate() throws Exception {
        ShpFiles shpFiles = new ShpFiles(backshp.toURI().toURL());
        FidIndexer.generate(shpFiles);

        ShapefileDataStore ds = new ShapefileDataStore(URLs.fileToUrl(backshp));

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
