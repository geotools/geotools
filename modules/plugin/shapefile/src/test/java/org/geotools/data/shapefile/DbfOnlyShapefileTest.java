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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
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

/**
 * @version $Id$
 * @author Burkhard Strauss
 */
public final class DbfOnlyShapefileTest {

    private static final String FILE_NAME = "dbf-only/dbf-only.dbf.gz";

    private static final Map<String, Serializable> PARAMS = new HashMap<>();

    @Before
    public void beforeGZippedShapefileTest() throws IOException {

        final File file = TestData.copy(this, FILE_NAME);
        assertTrue(file.canRead());
        final URL url = file.toURI().toURL();
        PARAMS.put(URLP.key, url);
    }

    @Test
    public void testCanProcess() throws IOException {

        final Map<String, Serializable> params = new HashMap<>();
        params.put(URLP.key, new File("a_file_name.dbf").toURI().toURL());
        assertTrue(new ShapefileDataStoreFactory().canProcess(params));
        params.put(URLP.key, new File("a_file_name.dbf.gz").toURI().toURL());
        assertTrue(new ShapefileDataStoreFactory().canProcess(params));
        params.put(URLP.key, new File("a_file_name.shp").toURI().toURL());
        assertTrue(new ShapefileDataStoreFactory().canProcess(params));
        params.put(URLP.key, new File("a_file_name.shp.gz").toURI().toURL());
        assertTrue(new ShapefileDataStoreFactory().canProcess(params));
    }

    @Test
    public void testCanReadTestFile() throws IOException {

        assertTrue(new ShapefileDataStoreFactory().canProcess(PARAMS));
        final DataStore dataStore = new ShapefileDataStoreFactory().createDataStore(PARAMS);
        final String[] typeNames = dataStore.getTypeNames();
        assertEquals(1, typeNames.length);
        final String typeName = typeNames[0];
        assertEquals("dbf-only", typeName);

        final Query query = new Query(typeName);
        int count = 0;
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, AUTO_COMMIT)) {
            assertNull(reader.getFeatureType().getGeometryDescriptor());
            while (reader.hasNext()) {
                final SimpleFeature feature = reader.next();
                assertNull(feature.getDefaultGeometry());
                for (final Object attribute : feature.getAttributes()) {
                    assertNotNull(attribute);
                }
                count++;
            }
        }
        assertEquals(63, count);
    }
}
