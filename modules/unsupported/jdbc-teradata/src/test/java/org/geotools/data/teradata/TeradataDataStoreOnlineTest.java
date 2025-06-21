/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class TeradataDataStoreOnlineTest extends JDBCDataStoreOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new TeradataTestSetup();
    }

    @Test
    public void testConcurrentWriters() throws Exception {
        final boolean[] errors = {false};
        Thread[] t = new Thread[8];

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("stringProperty"), String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);
        for (int i = 0; i < t.length; i++) {
            final int id = i + 1;
            t[i] = new Thread(() -> {
                for (int j = 0; j < 50; j++) {
                    try (FeatureWriter w = dataStore.getFeatureWriter(tname("ft2"), Transaction.AUTO_COMMIT)) {

                        while (w.hasNext()) {
                            w.next();
                        }
                        SimpleFeature f = (SimpleFeature) w.next();
                        f.setAttribute(1, Integer.valueOf(id * 100 + j));
                        f.setAttribute(2, "one");
                        w.write();
                    } catch (Exception ex) {
                        java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex);
                        errors[0] = true;
                    }
                }
            });
            t[i].start();
        }
        for (Thread thread : t) {
            thread.join();
        }
        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("ft2"));
        featureSource.getFeatures().size();
        if (errors[0]) fail();
    }

    @Test
    public void testCreateSchemaWithCaseSensitivity() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("stringProperty"), String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        try (FeatureWriter w = dataStore.getFeatureWriter(tname("ft2"), Transaction.AUTO_COMMIT)) {
            w.hasNext();

            SimpleFeature f = (SimpleFeature) w.next();
            f.setAttribute(1, Integer.valueOf(0));
            f.setAttribute(2, "one");
            w.write();
        }

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo correct = ff.equal(ff.property(aname("stringProperty")), ff.literal("one"), true);
        PropertyIsEqualTo incorrect = ff.equal(ff.property(aname("stringProperty")), ff.literal("OnE"), true);

        assertEquals(1, dataStore.getFeatureSource("ft2").getCount(new Query(tname("ft2"), correct)));
        assertEquals(0, dataStore.getFeatureSource("ft2").getCount(new Query(tname("ft2"), incorrect)));
    }
}
