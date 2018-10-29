/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DataTestCase;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class DecoratingDataStoreTest extends DataTestCase {
    public DecoratingDataStoreTest(String name) {
        super(name);
    }

    public static class MyDecoratingDataStore extends DecoratingDataStore {
        public MyDecoratingDataStore(DataStore delegate) {
            super(delegate);
        }
    }

    SimpleFeatureType riverType;
    SimpleFeature[] riverFeatures;
    ReferencedEnvelope riverBounds;
    Transaction defaultTransaction = new DefaultTransaction();

    MemoryDataStore data;
    MyDecoratingDataStore decorator;

    protected void setUp() throws Exception {
        super.setUp();
        data = new MemoryDataStore();
        data.addFeatures(roadFeatures);

        // Override river to use CRS
        riverType = SimpleFeatureTypeBuilder.retype(super.riverType, CRS.decode("EPSG:4326"));
        riverBounds = new ReferencedEnvelope(super.riverBounds, CRS.decode("EPSG:4326"));
        riverFeatures = new SimpleFeature[super.riverFeatures.length];
        for (int i = 0; i < riverFeatures.length; i++) {

            riverFeatures[i] = SimpleFeatureBuilder.retype(super.riverFeatures[i], riverType);
        }

        data.addFeatures(riverFeatures);
        decorator = new MyDecoratingDataStore(data);
    }

    protected void tearDown() throws Exception {
        defaultTransaction.close();
        data = null;
        super.tearDown();
    }

    @Test
    public void testUnwrap() {
        assertTrue(decorator.unwrap(DataStore.class) == data);
        assertTrue(decorator.unwrap(DataAccess.class) == data);
    }

    @Test
    public void testUnwrapWrongClass() {
        boolean error = false;
        try {
            decorator.unwrap(String.class);
        } catch (Exception e) {
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void testDataStore() throws IOException {
        assertEquals(2, decorator.getTypeNames().length);
        SimpleFeatureSource featureSource = decorator.getFeatureSource(riverType.getTypeName());
        assertNotNull(featureSource);
        assertEquals(riverFeatures.length, featureSource.getFeatures().size());
    }
}
