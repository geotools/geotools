/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.csv.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;

public class CSVLatLonStrategyTest {

    @Test
    public void testBuildFeatureType() {
        String input =
                CSVTestStrategySupport.buildInputString("lat,lon,quux,morx\n1.0,2.3,dkdkd,dkskd");
        CSVFileState fileState = new CSVFileState(input, "foo");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();

        assertEquals("Invalid attribute count", 3, featureType.getAttributeCount());
        assertEquals("Invalid featuretype name", "foo", featureType.getName().getLocalPart());
        assertEquals("Invalid name", "foo", featureType.getTypeName());

        List<AttributeDescriptor> attrs = featureType.getAttributeDescriptors();
        assertEquals("Invalid number of attributes", 3, attrs.size());
        List<String> attrNames = new ArrayList<>(2);
        for (AttributeDescriptor attr : attrs) {
            if (!(attr instanceof GeometryDescriptor)) {
                attrNames.add(attr.getName().getLocalPart());
            }
        }
        Collections.sort(attrNames);
        assertEquals("Invalid property descriptor", "morx", attrNames.get(0));
        assertEquals("Invalid property descriptor", "quux", attrNames.get(1));

        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        GeometryType geometryType = geometryDescriptor.getType();
        assertEquals("Invalid geometry name", "location", geometryType.getName().getLocalPart());
    }

    @Test
    public void testBuildFeature() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString(
                        "lat,lon,fleem,zoo", "3,4,car,cdr", "8,9,blub,frob");
        CSVFileState fileState = new CSVFileState(input, "bar");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);

        try (CSVIterator iterator = strategy.iterator()) {

            assertTrue("next value not read", iterator.hasNext());
            SimpleFeature feature = iterator.next();
            Point geometry = (Point) feature.getDefaultGeometry();
            Coordinate coordinate = geometry.getCoordinate();
            assertEquals("Invalid point", 3, coordinate.y, 0.1);
            assertEquals("Invalid point", 4, coordinate.x, 0.1);
            assertEquals(
                    "Invalid feature property", "car", feature.getAttribute("fleem").toString());
            assertEquals("Invalid feature property", "cdr", feature.getAttribute("zoo").toString());

            assertTrue("next value not read", iterator.hasNext());
            feature = iterator.next();
            geometry = (Point) feature.getDefaultGeometry();
            coordinate = geometry.getCoordinate();
            assertEquals("Invalid point", 8, coordinate.y, 0.1);
            assertEquals("Invalid point", 9, coordinate.x, 0.1);
            assertEquals(
                    "Invalid feature property", "blub", feature.getAttribute("fleem").toString());
            assertEquals(
                    "Invalid feature property", "frob", feature.getAttribute("zoo").toString());
            assertFalse("extra next value", iterator.hasNext());

            try {
                iterator.next();
                fail("NoSuchElementException should have been thrown");
            } catch (NoSuchElementException e) {
                assertTrue(true);
            }
        }
    }

    @Test
    /* check for GEOT-6387 */
    public void testAxisOrder() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString(
                        "lat,lon,fleem,zoo", "-90,-180,car,cdr", "90,180,blub,frob");
        checkAxisOrder(input);
        input =
                CSVTestStrategySupport.buildInputString(
                        "lon,lat,fleem,zoo", "-180,-90,car,cdr", "180,90,blub,frob");
        checkAxisOrder(input);
    }

    /** */
    private void checkAxisOrder(String input) throws IOException {
        CSVFileState fileState = new CSVFileState(input, "bar");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);

        try (CSVIterator iterator = strategy.iterator()) {

            assertTrue("next value not read", iterator.hasNext());
            SimpleFeature feature = iterator.next();
            Point geometry = (Point) feature.getDefaultGeometry();
            Coordinate coordinate = geometry.getCoordinate();

            assertTrue("Swapped axis", -90 <= coordinate.y && coordinate.y <= 90);
        }
    }

    @Test
    public void testBuildFeatureDifferentTypes() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString(
                        "doubleval,intval,lat,stringval,lon",
                        "3.8,7,73.28,foo,-14.39",
                        "9.12,-38,0,bar,29",
                        "-37,0,49,baz,0");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        try (CSVIterator iterator = strategy.iterator()) {

            SimpleFeatureType featureType = strategy.getFeatureType();
            assertEquals("invalid attribute count", 4, featureType.getAttributeCount());

            GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
            String localName = geometryDescriptor.getLocalName();
            assertEquals("Invalid geometry name", "location", localName);

            assertEquals(
                    "Invalid attribute type",
                    "java.lang.Double",
                    getBindingName(featureType, "doubleval"));
            assertEquals(
                    "Invalid attribute type",
                    "java.lang.Integer",
                    getBindingName(featureType, "intval"));
            assertEquals(
                    "Invalid attribute type",
                    "java.lang.String",
                    getBindingName(featureType, "stringval"));

            // iterate through values and verify
            Object[][] expValues =
                    new Object[][] {
                        new Object[] {3.8, 7, "foo", 73.28, -14.39},
                        new Object[] {9.12, -38, "bar", 0, 29},
                        new Object[] {-37.0, 0, "baz", 49, 0}
                    };
            Object[] expTypes = new Object[] {Double.class, Integer.class, String.class};
            List<SimpleFeature> features = new ArrayList<>(3);
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            assertEquals("Invalid number of features", 3, features.size());

            String[] attrNames = new String[] {"doubleval", "intval", "stringval"};
            int i = 0;
            for (SimpleFeature feature : features) {
                Object[] expVals = expValues[i];
                for (int j = 0; j < 3; j++) {
                    String attr = attrNames[j];
                    Object value = feature.getAttribute(attr);
                    Class<?> type = value.getClass();
                    assertEquals("Invalid attribute type", expTypes[j], type);
                    assertEquals("Invalid value", expVals[j], value);
                }
                i++;
            }
        }
    }

    @Test
    public void testNoGeometry() throws IOException {
        String input = CSVTestStrategySupport.buildInputString("a,b", "foo,bar");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();

        assertEquals("Invalid number of attributes", 2, featureType.getAttributeCount());
        assertNull("Expected no geometry in feature type", featureType.getGeometryDescriptor());
        assertEquals(
                "Invalid attribute type", "java.lang.String", getBindingName(featureType, "a"));
        assertEquals(
                "Invalid attribute type", "java.lang.String", getBindingName(featureType, "b"));

        try (CSVIterator iterator = strategy.iterator()) {
            SimpleFeature feature = iterator.next();
            assertEquals("Invalid feature attribute count", 2, feature.getAttributeCount());
            assertEquals("Invalid attribute", "foo", feature.getAttribute("a"));
            assertEquals("Invalid attribute", "bar", feature.getAttribute("b"));
        }
    }

    @Test
    public void testOnlyLat() throws IOException {
        String input = CSVTestStrategySupport.buildInputString("lat,quux", "foo,morx");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid number of attributes", 2, featureType.getAttributeCount());
        assertNull("Unexpected geometry", featureType.getGeometryDescriptor());
        assertEquals(
                "Invalid attribute type", "java.lang.String", getBindingName(featureType, "lat"));
        assertEquals(
                "Invalid attribute type", "java.lang.String", getBindingName(featureType, "quux"));
        try (CSVIterator iterator = strategy.iterator()) {
            SimpleFeature feature = iterator.next();
            assertEquals("Invalid lat value", "foo", feature.getAttribute("lat"));
            assertEquals("Invalid lat value", "morx", feature.getAttribute("quux"));
        }
    }

    @Test
    public void testDataDoesNotContainAllFields() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString("lat,lon,foo,bar", "-72.3829,42.29,quux");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 3, featureType.getAttributeCount());
        try (CSVIterator iterator = strategy.iterator()) {
            SimpleFeature feature = iterator.next();
            Point point = (Point) feature.getAttribute("location");
            Coordinate coordinate = point.getCoordinate();
            assertEquals("Invalid longitude coordinate", 42.29, coordinate.x, 0.1);
            assertEquals("Invalid latitude coordinate", -72.3829, coordinate.y, 0.1);
            assertEquals("Invalid attribute value", "quux", feature.getAttribute("foo"));
            assertNull("Expected null", feature.getAttribute("bar"));
        }
    }

    @Test
    public void testDataContainsMoreFields() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString("lat,lon,foo", "-72.3829,42.29,quux,morx");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        try (CSVIterator iterator = strategy.iterator()) {
            SimpleFeature feature = iterator.next();
            assertEquals("Invalid attribute count", 2, feature.getAttributeCount());
            assertNotNull("No location", feature.getAttribute("location"));
            assertEquals("Invalid attribute value", "quux", feature.getAttribute("foo"));
        }
    }

    @Test
    public void testDataDifferentTypes() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString(
                        "lat,lon,foo", "-72.3829,42.29,38", "12,-13.21,9", "foo,2.5,7.8");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 3, featureType.getAttributeCount());
        assertEquals(
                "Invalid attribute type", "java.lang.String", getBindingName(featureType, "lat"));
        assertEquals(
                "Invalid attribute type", "java.lang.Double", getBindingName(featureType, "lon"));
        assertEquals(
                "Invalid attribute type", "java.lang.Double", getBindingName(featureType, "foo"));
        assertNull("Unexpected geometry", featureType.getGeometryDescriptor());
        try (CSVIterator iterator = strategy.iterator()) {
            String[] expLats = new String[] {"-72.3829", "12", "foo"};
            Double[] expLons = new Double[] {42.29, -13.21, 2.5};
            Double[] expFoos = new Double[] {38.0, 9.0, 7.8};
            int i = 0;
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertEquals("Invalid attribute count", 3, feature.getAttributeCount());
                assertEquals("Invalid lat value", expLats[i], feature.getAttribute("lat"));
                assertEquals(
                        "Invalid lat value", expLons[i], (Double) feature.getAttribute("lon"), 0.1);
                assertEquals(
                        "Invalid foo value", expFoos[i], (Double) feature.getAttribute("foo"), 0.1);
                i++;
            }
        }
    }

    @Test
    public void testDataFewerRowsDifferentType() throws IOException {
        String input = CSVTestStrategySupport.buildInputString("a,b", "foo");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        assertEquals(
                "Invalid attribute type", "java.lang.String", getBindingName(featureType, "a"));
        assertEquals(
                "Invalid attribute type", "java.lang.Integer", getBindingName(featureType, "b"));
    }

    @Test
    public void testLngColumnSpelling() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString("lat,lng,fleem", "73.239,-42.389,morx");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertNotNull("No geometry found", featureType.getGeometryDescriptor());
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        try (CSVIterator iterator = strategy.iterator()) {
            SimpleFeature feature = iterator.next();
            Point geometry = (Point) feature.getDefaultGeometry();
            Coordinate coordinate = geometry.getCoordinate();
            assertEquals("Invalid lon", -42.389, coordinate.x, 0.1);
            assertEquals("Invalid lat", 73.239, coordinate.y, 0.1);
            assertEquals("Invalid attribute value", "morx", feature.getAttribute("fleem"));
        }
    }

    @Test
    public void testLongColumnSpelling() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString("lat,long,fleem", "73.239,-42.389,morx");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertNotNull("No geometry found", featureType.getGeometryDescriptor());
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        try (CSVIterator iterator = strategy.iterator()) {
            SimpleFeature feature = iterator.next();
            Point geometry = (Point) feature.getDefaultGeometry();
            Coordinate coordinate = geometry.getCoordinate();
            assertEquals("Invalid lon", -42.389, coordinate.x, 0.1);
            assertEquals("Invalid lat", 73.239, coordinate.y, 0.1);
            assertEquals("Invalid attribute value", "morx", feature.getAttribute("fleem"));
        }
    }

    @Test
    public void testLatLngColumnsSpelledOut() throws Exception {
        String input =
                CSVTestStrategySupport.buildInputString(
                        "latitude,longitude,fleem", "73.239,-42.389,morx");
        CSVFileState fileState = new CSVFileState(input, "typename");
        CSVLatLonStrategy strategy = new CSVLatLonStrategy(fileState);
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertNotNull("No geometry found", featureType.getGeometryDescriptor());
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        try (CSVIterator iterator = strategy.iterator()) {
            SimpleFeature feature = iterator.next();
            Point geometry = (Point) feature.getDefaultGeometry();
            Coordinate coordinate = geometry.getCoordinate();
            assertEquals("Invalid lon", -42.389, coordinate.x, 0.1);
            assertEquals("Invalid lat", 73.239, coordinate.y, 0.1);
            assertEquals("Invalid attribute value", "morx", feature.getAttribute("fleem"));
        }
    }

    private String getBindingName(SimpleFeatureType featureType, String col) {
        AttributeDescriptor descriptor = featureType.getDescriptor(col);
        AttributeType attributeType = descriptor.getType();
        Class<?> binding = attributeType.getBinding();
        String bindingName = binding.getName();
        return bindingName;
    }

    @Test
    public void testSpecifiedBuildFeatureType() {
        String input = CSVTestStrategySupport.buildInputString("quux,morx\n1,2");
        CSVFileState fileState = new CSVFileState(input, "foo");
        CSVStrategy strategy = new CSVLatLonStrategy(fileState, "quux", "morx");
        SimpleFeatureType featureType = strategy.getFeatureType();

        assertEquals("Invalid attribute count", 1, featureType.getAttributeCount());
        assertEquals("Invalid featuretype name", "foo", featureType.getName().getLocalPart());
        assertEquals("Invalid name", "foo", featureType.getTypeName());

        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        assertEquals(
                "Invalid geometry attribute name", "location", geometryDescriptor.getLocalName());
    }

    @Test
    public void testCreateFeature() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString("fleem,zoo,morx", "3,4,car", "8,9.9,cdr");
        CSVFileState fileState = new CSVFileState(input, "bar");
        CSVStrategy strategy = new CSVLatLonStrategy(fileState, "fleem", "zoo");

        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        List<AttributeDescriptor> attrs = featureType.getAttributeDescriptors();
        CSVTestStrategySupport.verifyType(attrs.get(0), Point.class);
        CSVTestStrategySupport.verifyType(attrs.get(1), String.class);

        try (CSVIterator iterator = strategy.iterator()) {

            assertTrue("next value not read", iterator.hasNext());
            SimpleFeature feature = iterator.next();
            assertNull("Unexpected property", feature.getAttribute("fleem"));
            assertNull("Unexpected property", feature.getAttribute("zoo"));
            assertEquals("Invalid feature property", "car", feature.getAttribute("morx"));
            assertNotNull("Missing geometry", feature.getDefaultGeometry());

            assertTrue("next value not read", iterator.hasNext());
            feature = iterator.next();
            assertNull("Unexpected property", feature.getAttribute("fleem"));
            assertNull("Unexpected property", feature.getAttribute("zoo"));
            assertEquals("Invalid feature property", "cdr", feature.getAttribute("morx"));
            assertNotNull("Missing geometry", feature.getDefaultGeometry());
            assertFalse("extra next value", iterator.hasNext());

            try {
                iterator.next();
                fail("NoSuchElementException should have been thrown");
            } catch (NoSuchElementException e) {
                assertTrue(true);
            }
        }
    }

    @Test
    public void testCreateSpecifiedLatLngColumnsDontExist() throws IOException {
        String input = CSVTestStrategySupport.buildInputString("blub", "fubar");
        CSVFileState fileState = new CSVFileState(input, "zul");
        CSVStrategy strategy = new CSVLatLonStrategy(fileState, "non", "existing");
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 1, featureType.getAttributeCount());
        try (CSVIterator iterator = strategy.iterator()) {
            assertTrue("next value not read", iterator.hasNext());
            SimpleFeature feature = iterator.next();
            assertNull("Unexpected geometry", feature.getDefaultGeometry());
        }
    }

    @Test
    public void testCreateSpecifiedLatLngColumnsNotNumeric() throws IOException {
        String input = CSVTestStrategySupport.buildInputString("foo,bar", "3.8,quux");
        CSVFileState fileState = new CSVFileState(input, "zul");
        CSVStrategy strategy = new CSVLatLonStrategy(fileState, "foo", "bar");
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        try (CSVIterator iterator = strategy.iterator()) {
            assertTrue("next value not read", iterator.hasNext());
            SimpleFeature feature = iterator.next();
            assertNull("Unexpected geometry", feature.getDefaultGeometry());
        }
    }

    @Test
    public void testCreateSchema() throws IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.setName("testCreateSchema");
        builder.add("the_geom", Point.class);
        builder.add("id", Integer.class);
        builder.add("int_field", Integer.class);
        builder.add("string_field", String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        File csvFile = File.createTempFile("testCreateSchema", ".csv");
        CSVFileState csvFileState = new CSVFileState(csvFile);
        CSVStrategy strategy = new CSVLatLonStrategy(csvFileState, "lat_field", "long_field");
        strategy.createSchema(featureType);

        assertEquals(
                "Stragegy does not have provided feature type",
                featureType,
                strategy.getFeatureType());
        List<String> content = Files.readAllLines(csvFile.toPath());
        assertEquals("long_field,lat_field,id,int_field,string_field", content.get(0));
        csvFile.delete();
    }
}
