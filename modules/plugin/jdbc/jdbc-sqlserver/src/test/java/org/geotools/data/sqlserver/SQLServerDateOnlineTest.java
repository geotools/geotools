/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import microsoft.sql.DateTimeOffset;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.junit.Test;

public class SQLServerDateOnlineTest extends JDBCDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new SQLServerDateTestSetup();
    }

    @Test
    public void testInsert() throws Exception {
        dataStore.setExposePrimaryKeyColumns(true);
        SimpleFeatureType ft = dataStore.getSchema(tname("dates"));
        SimpleFeature datesFeat = DataUtilities.createFeature(
                ft, "id=4|2009-09-29|2009-09-29T17:54:23|2009-09-29T17:54:23|2009-09-29T17:54:23+02:00|17:54:23");
        FeatureStore<SimpleFeatureType, SimpleFeature> fs =
                (FeatureStore<SimpleFeatureType, SimpleFeature>) dataStore.getFeatureSource("dates");
        List<FeatureId> featIds = fs.addFeatures(DataUtilities.collection(datesFeat));
        assertEquals(1, featIds.size());
    }

    @Test
    public void testQuery() throws IOException {
        ContentFeatureCollection fc = dataStore.getFeatureSource("dates").getFeatures();
        try (SimpleFeatureIterator it = fc.features()) {
            SimpleFeature f1 = it.next();
            Object a1 = f1.getAttribute(0);
            assertEquals(Date.class, a1.getClass());
            assertEquals("2009-06-28", ((Date) a1).toString());
            Object a2 = f1.getAttribute(1);
            assertEquals(Timestamp.class, a2.getClass());
            assertEquals("2009-06-28 15:12:41.0", ((Timestamp) a2).toString());
            Object a3 = f1.getAttribute(2);
            assertEquals(Timestamp.class, a3.getClass());
            assertEquals("2009-06-28 15:12:41.0", ((Timestamp) a3).toString());
            Object a4 = f1.getAttribute(3);
            assertEquals(DateTimeOffset.class, a4.getClass());
            assertEquals("2009-06-28 15:12:41 +00:00", ((DateTimeOffset) a4).toString());
            Object a5 = f1.getAttribute(4);
            assertEquals(Time.class, a5.getClass());
            assertEquals("15:12:41", ((Time) a5).toString());
            SimpleFeature f2 = it.next();
            a4 = f2.getAttribute(3);
            assertEquals(DateTimeOffset.class, a4.getClass());
            assertEquals("2009-01-15 13:10:12 +00:00", ((DateTimeOffset) a4).toString());
            SimpleFeature f3 = it.next();
            a4 = f3.getAttribute(3);
            assertEquals(DateTimeOffset.class, a4.getClass());
            assertEquals("2009-09-29 17:54:23 +02:00", ((DateTimeOffset) a4).toString());
            SimpleFeature f4 = it.next();
            a4 = f4.getAttribute(3);
            assertEquals(null, a4);
            assertEquals(false, it.hasNext());
        }
        assertEquals(4, fc.size());
    }
}
