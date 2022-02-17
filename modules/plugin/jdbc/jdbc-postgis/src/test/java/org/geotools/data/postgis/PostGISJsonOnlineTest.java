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
package org.geotools.data.postgis;

import java.io.IOException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBCFeatureReader;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class PostGISJsonOnlineTest extends JDBCTestSupport {

    PostGISJsonTestSetup pgJsonTestSetup;

    @Override
    protected JDBCTestSetup createTestSetup() {
        pgJsonTestSetup = new PostGISJsonTestSetup();
        return pgJsonTestSetup;
    }

    @Test
    public void testJsonAttributesMappedToString() throws Exception {
        String name = "numberEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        SimpleFeatureType featureType = feature.getFeatureType();
        AttributeDescriptor descriptor = featureType.getDescriptor("jsonColumn");
        assertEquals(String.class, descriptor.getType().getBinding());

        if (pgJsonTestSetup.supportJsonB) {
            descriptor = featureType.getDescriptor("jsonbColumn");
            assertEquals(String.class, descriptor.getType().getBinding());
        }
    }

    @Test
    public void testNumberEntry() throws Exception {
        String name = "numberEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");

        assertTrue(jsonColumnValue.contains("1e-3"));

        if (pgJsonTestSetup.supportJsonB) {
            String jsonbColumnValue = (String) feature.getAttribute("jsonbColumn");
            // in JSONB, numbers will be printed according to the behavior of the underlying numeric
            // type.
            // In practice this means that numbers entered with E notation will be printed without
            // it
            assertTrue(jsonbColumnValue.contains("0.001"));
        }
    }

    @Test
    public void testEntryWithSpaces() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("entryWithSpaces");
        assertEquals("{\"title\"    :    \"Title\" }", feature.getAttribute("jsonColumn"));
        // JSONB does not preserve white spaces
        if (pgJsonTestSetup.supportJsonB) {
            assertEquals("{\"title\": \"Title\"}", feature.getAttribute("jsonbColumn"));
        }
    }

    @Test
    public void testDuppedKeyEntry() throws Exception {
        String name = "duppedKeyEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");
        assertTrue(jsonColumnValue.contains("Title1"));
        assertTrue(jsonColumnValue.contains("Title2"));

        // JSONB does not keep duplicate object keys
        if (pgJsonTestSetup.supportJsonB) {
            String jsonbColumnValue = (String) feature.getAttribute("jsonbColumn");
            assertFalse(jsonbColumnValue.contains("Title1"));
            assertTrue(jsonbColumnValue.contains("Title2"));
        }
    }

    @Test
    public void testNullKeyEntry() throws Exception {
        String name = "nullKey";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");
        assertTrue(jsonColumnValue.contains("{\"title\":null}"));
    }

    @Test
    public void testNullEntry() throws Exception {
        String name = "nullEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");
        assertNull(jsonColumnValue);
        if (pgJsonTestSetup.supportJsonB) {
            String jsonbColumnValue = (String) feature.getAttribute("jsonbColumn");
            assertNull(jsonbColumnValue);
        }
    }

    @Test
    public void testJSONPointerFunction() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        pointerEquals(fs, ff, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerEquals(fs, ff, "jsonbColumn");
        }
    }

    private void pointerEquals(ContentFeatureSource fs, FilterFactory ff, String column)
            throws IOException {
        Function pointer = ff.function("jsonPointer", ff.property(column), ff.literal("/weight"));
        Literal literal = ff.literal(0.001);
        Filter filter = ff.equals(pointer, literal);
        Query q = new Query(tname("jsontest"), filter);
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertEquals(pointer.evaluate(f), 0.001f);
                size++;
            }
            assertEquals(1, size);
        }
    }

    @Test
    public void testJSONPointerFunctionWithArray() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        pointerGreater(fs, ff, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerGreater(fs, ff, "jsonbColumn");
        }
    }

    private void pointerGreater(ContentFeatureSource fs, FilterFactory ff, String column)
            throws IOException {
        Function pointer =
                ff.function("jsonPointer", ff.property(column), ff.literal("/arrayValues/1"));
        Literal literal = ff.literal(3);
        Filter filter = ff.greater(pointer, literal);
        Query q = new Query(tname("jsontest"), filter);
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertTrue((Integer) pointer.evaluate(f) > 3);
                size++;
            }
            assertEquals(2, size);
        }
    }

    @Test
    public void testJSONPointerFunctionIsNotNull() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        pointerNotNull(fs, ff, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerNotNull(fs, ff, "jsonbColumn");
        }
    }

    private void pointerNotNull(ContentFeatureSource fs, FilterFactory ff, String column)
            throws IOException {
        Function pointer = ff.function("jsonPointer", ff.property(column), ff.literal("/title"));
        Filter filter = ff.not(ff.isNull(pointer));
        Query q = new Query(tname("jsontest"), filter);
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertNotNull(pointer.evaluate(f));
                size++;
            }
            assertEquals(2, size);
        }
    }

    @Test
    public void testJSONPointerFunctionIgnoreCase() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        pointerEqualsIgnoreCase(fs, ff, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerEqualsIgnoreCase(fs, ff, "jsonbColumn");
        }
    }

    private void pointerEqualsIgnoreCase(ContentFeatureSource fs, FilterFactory ff, String column)
            throws IOException {
        Function pointer = ff.function("jsonPointer", ff.property(column), ff.literal("/strVal"));
        Filter filter = ff.equal(pointer, ff.literal("StrinGvalue"), false);

        Query q = new Query(tname("jsontest"), filter);
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertEquals(pointer.evaluate(f), "stringValue");
                size++;
            }
            assertEquals(1, size);
        }
    }

    @Test
    public void testJSONPointerFunctionNestedArray() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        pointerAndDouble(fs, ff, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerAndDouble(fs, ff, "jsonbColumn");
        }
    }

    private void pointerAndDouble(ContentFeatureSource fs, FilterFactory ff, String column)
            throws IOException {
        Function pointer =
                ff.function(
                        "jsonPointer", ff.property(column), ff.literal("/nestedObj/nestedProp"));
        Filter filter = ff.equals(pointer, ff.literal("stringValue"));
        Function pointer2 =
                ff.function(
                        "jsonPointer", ff.property(column), ff.literal("/nestedObj/nestedAr/1"));
        Filter filter2 = ff.greater(pointer2, ff.literal(5.1));
        And and = ff.and(filter, filter2);
        Query q = new Query(tname("jsontest"), and);
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertEquals(pointer.evaluate(f), "stringValue");
                assertTrue(((Integer) pointer2.evaluate(f)).doubleValue() > 5.1);
                size++;
            }
            assertEquals(1, size);
        }
    }

    @Test
    public void testJSONPointerFunctionNestedObject() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        pointerAndInt(fs, ff, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerAndInt(fs, ff, "jsonbColumn");
        }
    }

    private void pointerAndInt(ContentFeatureSource fs, FilterFactory ff, String column)
            throws IOException {
        Function pointer =
                ff.function(
                        "jsonPointer",
                        ff.property(column),
                        ff.literal("/nestedObj/nestedObj2/strProp"));
        Filter filter = ff.equals(pointer, ff.literal("stringValue2"));
        Function pointer2 =
                ff.function(
                        "jsonPointer",
                        ff.property(column),
                        ff.literal("/nestedObj/nestedObj2/numProp"));
        Filter filter2 = ff.greater(pointer2, ff.literal(3));
        And and = ff.and(filter, filter2);
        Query q = new Query(tname("jsontest"), and);
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertEquals(pointer.evaluate(f), "stringValue2");
                assertTrue(((Integer) pointer2.evaluate(f)).doubleValue() > 3);
                size++;
            }
            assertEquals(1, size);
        }
    }

    @Test
    public void testNonEncodableJSONPointerFunction() throws Exception {
        // tests that with a propertyName involved in the production of the
        // json pointer string, the filter is not encoded to PostGIS SQL dialect
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        Function concat1 =
                ff.function("strConcat", ff.property("name"), ff.literal("/nestedObj2/strProp"));
        Function concat2 = ff.function("strConcat", ff.literal("/"), concat1);
        pointerNonEncodable(fs, ff, concat2, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerNonEncodable(fs, ff, concat2, "jsonbColumn");
        }
    }

    private void pointerNonEncodable(
            ContentFeatureSource fs, FilterFactory ff, Function concat2, String column)
            throws IOException {

        Function pointer = ff.function("jsonPointer", ff.property(column), concat2);
        Filter filter = ff.equals(pointer, ff.literal("stringValue2"));
        Query q = new Query(tname("jsontest"), filter);
        try (FeatureReader reader = fs.getDataStore().getFeatureReader(q, null)) {
            // reader is instanceof FilteringFeatureReader so Filter has not been encoded
            assertTrue(reader instanceof FilteringFeatureReader);
        }
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertEquals(pointer.evaluate(f), "stringValue2");
                size++;
            }
            assertEquals(2, size);
        }
    }

    @Test
    public void testJSONPointerFunctionWithConstantParam() throws Exception {
        // test that with a constant expression as second param filter is encoded
        // to PostGIS SQL dialect
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        // json pointer string obtained by a string concatenation
        Function concat =
                ff.function(
                        "strConcat", ff.literal("/nestedObj"), ff.literal("/nestedObj2/strProp"));
        pointerConstant(fs, ff, concat, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerConstant(fs, ff, concat, "jsonColumn");
        }
    }

    private void pointerConstant(
            ContentFeatureSource fs, FilterFactory ff, Function concat, String column)
            throws IOException {
        Function pointer = ff.function("jsonPointer", ff.property(column), concat);
        Filter filter = ff.equals(pointer, ff.literal("stringValue2"));
        Query q = new Query(tname("jsontest"), filter);
        try (FeatureReader reader = fs.getDataStore().getFeatureReader(q, null)) {
            // reader is instanceof JDBCFeatureReader so Filter has been encoded
            assertTrue(reader instanceof JDBCFeatureReader);
        }
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertEquals(pointer.evaluate(f), "stringValue2");
                size++;
            }
            assertEquals(2, size);
        }
    }

    public void testJSONPointerFunctionLike() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        pointerLike(fs, ff, "jsonColumn");
        if (pgJsonTestSetup.supportJsonB) {
            pointerLike(fs, ff, "jsonbColumn");
        }
    }

    private void pointerLike(ContentFeatureSource fs, FilterFactory ff, String column)
            throws IOException {
        Function pointer =
                ff.function(
                        "jsonPointer", ff.property(column), ff.literal("/nestedObj/nestedProp"));
        Filter filter = ff.like(pointer, "%Value");
        Query q = new Query(tname("jsontest"), filter);
        FeatureCollection collection = fs.getFeatures(q);
        try (FeatureIterator it = collection.features()) {
            int size = 0;
            while (it.hasNext()) {
                Feature f = it.next();
                assertEquals(pointer.evaluate(f), "stringValue");
                size++;
            }
            assertEquals(2, size);
        }
    }

    private SimpleFeature getSingleFeatureByName(String name) throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")), ff.literal(name), true);
        Query q = new Query(tname("jsontest"), filter);
        return DataUtilities.first(fs.getFeatures(q));
    }
}
