/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public abstract class MongoDataStoreTest extends MongoTestSupport {

    protected MongoDataStoreTest(MongoTestSetup testSetup) {
        super(testSetup);
    }

    public void testGetTypeNames() throws Exception {
        String[] typeNames = dataStore.getTypeNames();
        assertEquals(1, typeNames.length);
        assertEquals("ft1", typeNames[0]);
    }

    public void testGetSchema() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema("ft1");
        assertNotNull(schema);

        assertNotNull(schema.getDescriptor("geometry"));
        assertTrue(
                Geometry.class.isAssignableFrom(
                        schema.getDescriptor("geometry").getType().getBinding()));
    }

    public void testGetFeatureReader() throws Exception {
        SimpleFeatureReader reader =
                (SimpleFeatureReader)
                        dataStore.getFeatureReader(new Query("ft1"), Transaction.AUTO_COMMIT);
        try {
            for (int i = 0; i < 3; i++) {
                assertTrue(reader.hasNext());
                SimpleFeature f = reader.next();

                assertFeature(f);
            }
            assertFalse(reader.hasNext());
        } finally {
            reader.close();
        }
    }

    public void testGetFeatureSource() throws Exception {
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        assertEquals(3, source.getCount(Query.ALL));

        ReferencedEnvelope env = source.getBounds();
        assertEquals(0d, env.getMinX(), 0.1);
        assertEquals(0d, env.getMinY(), 0.1);
        assertEquals(2d, env.getMaxX(), 0.1);
        assertEquals(2d, env.getMaxY(), 0.1);
    }

    public void testGetAppendFeatureWriter() throws Exception {
        FeatureWriter w = dataStore.getFeatureWriterAppend("ft1", Transaction.AUTO_COMMIT);
        SimpleFeature f = (SimpleFeature) w.next();

        GeometryBuilder gb = new GeometryBuilder();
        f.setDefaultGeometry(gb.point(3, 3));
        f.setAttribute("properties.intProperty", 3);
        f.setAttribute("properties.doubleProperty", 3.3);
        f.setAttribute("properties.stringProperty", "three");
        f.setAttribute(
                "properties.dateProperty",
                MongoTestSetup.parseDate("2015-01-24T14:28:16.000+01:00"));
        w.write();
        w.close();
    }

    public void testCreateSchema() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("ft2");
        tb.setCRS(DefaultGeographicCRS.WGS84);
        tb.add("geometry", Point.class);
        tb.add("intProperty", Integer.class);
        tb.add("doubleProperty", Double.class);
        tb.add("stringProperty", String.class);
        tb.add("dateProperty", Date.class);

        List<String> typeNames = Arrays.asList(dataStore.getTypeNames());
        assertFalse(typeNames.contains("ft2"));

        dataStore.createSchema(tb.buildFeatureType());
        assertEquals(typeNames.size() + 1, dataStore.getTypeNames().length);
        typeNames = Arrays.asList(dataStore.getTypeNames());
        assertTrue(typeNames.contains("ft2"));

        SimpleFeatureSource source = dataStore.getFeatureSource("ft2");
        assertEquals(0, source.getCount(new Query("ft2")));

        FeatureWriter w = dataStore.getFeatureWriterAppend("ft2", Transaction.AUTO_COMMIT);
        SimpleFeature f = (SimpleFeature) w.next();
        f.setDefaultGeometry(new GeometryBuilder().point(1, 1));
        f.setAttribute("intProperty", 1);
        w.write();

        source = dataStore.getFeatureSource("ft2");
        assertEquals(1, source.getCount(new Query("ft2")));
    }
}
