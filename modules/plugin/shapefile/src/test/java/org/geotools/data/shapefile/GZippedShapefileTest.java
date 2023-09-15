/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2023, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.api.data.Transaction.AUTO_COMMIT;
import static org.geotools.data.shapefile.ShapefileDataStoreFactory.URLP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.locationtech.jts.util.Assert.shouldNeverReachHere;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Point;

/**
 * @version $Id$
 * @author Burkhard Strauss
 */
public final class GZippedShapefileTest {

    private static final String BASE = "gzipped/gzipped";
    private static final String[] NAMES = {
        BASE + ".dbf.gz", BASE + ".prj.gz", BASE + ".shp.gz", BASE + ".shx.gz"
    };

    private static final Map<String, Serializable> PARAMS = new HashMap<>();

    @Before
    public void beforeGZippedShapefileTest() throws IOException {

        for (final String name : NAMES) {
            assertTrue(TestData.copy(this, name).canRead());
        }
        final URL url = TestData.file(this, NAMES[2]).toURI().toURL();
        PARAMS.put(URLP.key, url);
    }

    @Test
    public void testShpFilesIsGz() throws IOException {

        final DataStore dataStore = new ShapefileDataStoreFactory().createDataStore(PARAMS);
        final ShapefileDataStore shapefileDataStore = (ShapefileDataStore) dataStore;
        assertTrue(shapefileDataStore.shpFiles.isGz());
    }

    @Test
    public void testShpFilesIsNotWritable() throws IOException {

        final DataStore dataStore = new ShapefileDataStoreFactory().createDataStore(PARAMS);
        final ShapefileDataStore shapefileDataStore = (ShapefileDataStore) dataStore;
        assertFalse(shapefileDataStore.shpFiles.isWritable());
    }

    @Test
    public void testCannotCreateSchema() throws IOException {

        final DataStore dataStore = new ShapefileDataStoreFactory().createDataStore(PARAMS);
        final SimpleFeatureType featureType = dataStore.getSchema("gzipped");
        try {
            dataStore.createSchema(featureType);
            shouldNeverReachHere();
        } catch (final IOException e) {
            assertTrue(e.getMessage().startsWith("Cannot create FeatureType"));
        }
    }

    @Test
    public void testCanReadTestFiles() throws IOException {

        assertTrue(new ShapefileDataStoreFactory().canProcess(PARAMS));
        final DataStore dataStore = new ShapefileDataStoreFactory().createDataStore(PARAMS);
        assertTrue(dataStore instanceof ShapefileDataStore);
        final String[] typeNames = dataStore.getTypeNames();
        assertEquals(1, typeNames.length);
        final String typeName = typeNames[0];
        assertEquals("gzipped", typeName);
        final Query query = new Query(typeName);
        int count = 0;
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(query, AUTO_COMMIT)) {
            while (reader.hasNext()) {
                final SimpleFeature feature = reader.next();
                final Object geometry = feature.getDefaultGeometry();
                assertTrue(geometry instanceof Point);
                for (final Object attribute : feature.getAttributes()) {
                    assertNotNull(attribute);
                }
                count++;
            }
        }
        assertEquals(63, count);
    }
}
