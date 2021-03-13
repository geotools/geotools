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

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.Intersects;

public abstract class MongoDataStoreTest extends MongoTestSupport {

    protected MongoDataStoreTest(MongoTestSetup testSetup) {
        super(testSetup);
    }

    public void testGetTypeNames() throws Exception {
        String[] typeNames = dataStore.getTypeNames();
        assertEquals(2, typeNames.length);
        assertEquals("ft1", typeNames[0]);
        assertEquals("ft3", typeNames[1]);
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
        try (SimpleFeatureReader reader =
                (SimpleFeatureReader)
                        dataStore.getFeatureReader(new Query("ft1"), Transaction.AUTO_COMMIT)) {
            for (int i = 0; i < 3; i++) {
                assertTrue(reader.hasNext());
                SimpleFeature f = reader.next();

                assertFeature(f);
            }
            assertFalse(reader.hasNext());
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
        try (FeatureWriter w = dataStore.getFeatureWriterAppend("ft1", Transaction.AUTO_COMMIT)) {
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
        }
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

        try (FeatureWriter w = dataStore.getFeatureWriterAppend("ft2", Transaction.AUTO_COMMIT)) {
            SimpleFeature f = (SimpleFeature) w.next();
            f.setDefaultGeometry(new GeometryBuilder().point(1, 1));
            f.setAttribute("intProperty", 1);
            w.write();

            source = dataStore.getFeatureSource("ft2");
            assertEquals(1, source.getCount(new Query("ft2")));
        }
    }

    public void testRebuildSchemaWithId() throws Exception {
        try {
            dataStore.setSchemaInitParams(
                    MongoSchemaInitParams.builder().ids("58e5889ce4b02461ad5af082").build());
            clearSchemaStore(dataStore);
            dataStore.cleanEntries();
            SimpleFeatureType schema = dataStore.getSchema("ft1");
            assertNotNull(schema);

            assertNotNull(schema.getDescriptor("properties.optionalProperty"));
        } finally {
            dataStore.setSchemaInitParams(null);
            clearSchemaStore(dataStore);
            dataStore.cleanEntries();
        }
    }

    public void testRebuildSchemaWithMax() throws Exception {
        try {
            dataStore.setSchemaInitParams(MongoSchemaInitParams.builder().maxObjects(3).build());
            clearSchemaStore(dataStore);
            dataStore.cleanEntries();
            SimpleFeatureType schema = dataStore.getSchema("ft1");
            assertNotNull(schema);

            assertNotNull(schema.getDescriptor("properties.optionalProperty"));
        } finally {
            dataStore.setSchemaInitParams(null);
            clearSchemaStore(dataStore);
            dataStore.cleanEntries();
        }
    }

    public void testRebuildSchemaWithAllItems() throws Exception {
        try {
            dataStore.setSchemaInitParams(MongoSchemaInitParams.builder().maxObjects(-1).build());
            clearSchemaStore(dataStore);
            dataStore.cleanEntries();
            SimpleFeatureType schema = dataStore.getSchema("ft1");
            assertNotNull(schema);
            assertNotNull(schema.getDescriptor("properties.optionalProperty"));
            assertNotNull(schema.getDescriptor("properties.optionalProperty2"));
            assertNotNull(schema.getDescriptor("properties.optionalProperty3"));
            // check inside array value (not first element)
            assertNotNull(
                    "Inside array value check (not first element) failed.",
                    schema.getDescriptor("properties.listProperty.insideArrayValue"));
        } finally {
            dataStore.setSchemaInitParams(null);
            clearSchemaStore(dataStore);
            dataStore.cleanEntries();
        }
    }

    private void clearSchemaStore(MongoDataStore mongoStore) {
        List<String> typeNames = mongoStore.getSchemaStore().typeNames();
        for (String et : typeNames) {
            try {
                mongoStore.getSchemaStore().deleteSchema(new NameImpl(et));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        mongoStore.cleanEntries();
    }

    public void testSortBy() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        SortBy f = ff.sort("properties.dateProperty", SortOrder.ASCENDING);

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");

        Query q = new Query("ft1", Filter.INCLUDE);
        q.setSortBy(f);

        SimpleFeatureCollection features = source.getFeatures(q);
        try (SimpleFeatureIterator it = features.features()) {
            List<Date> dates = new ArrayList<>(3);
            while (it.hasNext()) {
                dates.add((Date) it.next().getAttribute("properties.dateProperty"));
            }
            assertEquals(dates.size(), 3);
            Date first = dates.get(0);
            Date second = dates.get(1);
            Date third = dates.get(2);
            assertTrue(first.before(second));
            assertTrue(second.before(third));
        }
    }

    public void testIsNullFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pn = ff.property("properties.nullableAttribute");
        PropertyIsNull isNull = ff.isNull(pn);
        Query q = new Query("ft1", isNull);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(features.size(), 2);
        try (SimpleFeatureIterator it = features.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNull(pn.evaluate(f));
            }
        }
    }

    public void testNotFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pn = ff.property("properties.stringProperty");
        PropertyIsLike like = ff.like(pn, "one");
        Not not = ff.not(like);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", not);
        SimpleFeatureCollection features = source.getFeatures(q);
        try (SimpleFeatureIterator it = features.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotEquals("one", pn.evaluate(f));
            }
        }
    }

    public void testNotNullFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pn = ff.property("properties.nullableAttribute");
        PropertyIsNull isNull = ff.isNull(pn);
        Not not = ff.not(isNull);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", not);
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeature f = features.features().next();
        assertNotNull(pn.evaluate(f));
    }

    public void testNotNotEqualFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pnS = ff.property("properties.stringProperty");
        PropertyIsNotEqualTo notEqualTo = ff.notEqual(pnS, ff.literal("one"));
        Not not = ff.not(notEqualTo);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", not);
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeature f = features.features().next();
        assertEquals(pnS.evaluate(f), "one");
    }

    public void testNotEqualBetweenPropertiesFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pnS = ff.property("properties.stringProperty");
        PropertyIsNotEqualTo notEqualTo = ff.notEqual(pnS, ff.literal("one"));
        Not not = ff.not(notEqualTo);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", not);
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeature f = features.features().next();
        assertEquals(pnS.evaluate(f), "one");
    }

    public void testAndNotFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pn = ff.property("properties.stringProperty");
        PropertyIsNull isNull = ff.isNull(ff.property("properties.nullableAttribute"));
        PropertyIsNotEqualTo equalTo = ff.notEqual(pn, ff.literal("zero"));
        Not notFirst = ff.not(isNull);
        Not notSecond = ff.not(equalTo);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", ff.and(notFirst, notSecond));
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeature f = features.features().next();
        assertEquals(pn.evaluate(f), "zero");
    }

    public void testOrNotFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pn = ff.property("properties.stringProperty");
        PropertyIsNull isNull = ff.isNull(ff.property("properties.nullableAttribute"));
        PropertyIsNotEqualTo equalTo = ff.notEqual(pn, ff.literal("zero"));
        Not notFirst = ff.not(isNull);
        Not notSecond = ff.not(equalTo);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", ff.or(notFirst, notSecond));
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeature f = features.features().next();
        assertEquals(pn.evaluate(f), "zero");
    }

    public void testEqualFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pn = ff.property("properties.stringProperty");
        PropertyIsEqualTo equalTo = ff.equals(pn, ff.literal("zero"));
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", equalTo);
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(1, features.size());
        SimpleFeature f = features.features().next();
        assertEquals(pn.evaluate(f), "zero");
    }

    public void testNotWithEqualFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName pn = ff.property("properties.stringProperty");
        PropertyIsEqualTo equalTo = ff.equals(pn, ff.literal("zero"));
        Not not = ff.not(equalTo);
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", not);
        SimpleFeatureCollection features = source.getFeatures(q);
        try (SimpleFeatureIterator it = features.features()) {
            assertEquals(2, features.size());
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotEquals("zero", pn.evaluate(f));
            }
        }
    }

    public void testIntersectsWithJsonSelectFunction() throws Exception {

        Intersects intersects = getIntersectsFilter("jsonSelect");

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", intersects);
        try (FeatureReader reader = dataStore.getFeatureReader(q, null)) {
            // check type if the filter isn't fully supported we would have got
            // a FilteringFeatureReader
            assertTrue(reader instanceof MongoFeatureReader);
        }
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(2, features.size());
    }

    public void testIntersectsWithJsonSelectAllFunction() throws Exception {

        Intersects intersects = getIntersectsFilter("jsonSelectAll");
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", intersects);
        try (FeatureReader reader = dataStore.getFeatureReader(q, null)) {
            // check type if the filter isn't fully supported we would have got
            // a FilteringFeatureReader
            assertTrue(reader instanceof MongoFeatureReader);
        }
        SimpleFeatureCollection features = source.getFeatures(q);
        assertEquals(2, features.size());
    }

    private Intersects getIntersectsFilter(String jsonSelectName) {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Coordinate[] coordinates = {
            new Coordinate(1.0, 1.0),
            new Coordinate(2.0, 1.0),
            new Coordinate(3.0, 3.0),
            new Coordinate(4.0, 4.0),
            new Coordinate(1.0, 1.0),
        };
        Polygon polygon = new GeometryFactory().createPolygon(coordinates);
        return ff.intersects(
                ff.function(jsonSelectName, ff.literal("geometry")), ff.literal(polygon));
    }
}
