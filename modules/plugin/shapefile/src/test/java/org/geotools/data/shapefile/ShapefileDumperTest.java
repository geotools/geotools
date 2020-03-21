/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.URLs;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class ShapefileDumperTest {

    static final String BASIC_POLYGONS = "BasicPolygons";

    static final String ALL_TYPES = "AllTypes";

    static final String ALL_TYPES_WITH_NULL = "AllTypesWithNull";

    static final String LONGNAMES = "longnames";

    static final String NULLGEOM = "nullgeom";

    File properties = new File("./src/test/resources/org/geotools/data/shapefile/test-data/dumper");

    File dumperFolder = new File("./target/dumperFolder");

    PropertyDataStore propertyStore = null;

    List<ShapefileDataStore> shapefileStores = new ArrayList<>();

    @Before
    public void setup() throws IOException {
        if (dumperFolder.exists()) {
            FileUtils.deleteQuietly(dumperFolder);
        }
        dumperFolder.mkdirs();

        propertyStore = new PropertyDataStore(properties);
    }

    @After
    public void teardown() throws IOException {
        if (propertyStore != null) {
            propertyStore.dispose();
        }
    }

    @Test
    public void testBasicPolygons() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(BASIC_POLYGONS);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.dump(fc);

        testBasicPolygonCollection(3, BASIC_POLYGONS);
    }

    @Test
    public void testBasicPolygonsOtherName() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(BASIC_POLYGONS);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.dump("DaBasicPolygons", fc);

        testBasicPolygonCollection(3, "DaBasicPolygons");
    }

    @Test
    public void testLongNames() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(LONGNAMES);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.dump(fc);

        SimpleFeatureCollection actual = getFeaturesFromShapefile(LONGNAMES);
        assertEquals(2, actual.size());
        assertFieldsNotEmpty(actual);
        checkTypeStructure(
                actual.getSchema(), MultiPolygon.class, "FID", "VERYLONGNA", "VERYLONGN0");
        assertCst(LONGNAMES, "ISO-8859-1");
    }

    @Test
    public void testNullGeometry() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(NULLGEOM);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.dump(fc);

        SimpleFeatureCollection actual = getFeaturesFromShapefile(NULLGEOM);
        assertEquals(2, actual.size());
        checkTypeStructure(actual.getSchema(), MultiPolygon.class, "FID", "NAME");
        assertCst(NULLGEOM, "ISO-8859-1");

        // check features attributes
        List<SimpleFeature> features = getFeaturesSortedById(fc);
        // check the first feature with a geometry
        assertThat(features.get(0).getAttribute("FID"), notNullValue());
        assertThat(features.get(0).getAttribute("FID"), is("117"));
        assertThat(features.get(0).getAttribute("NAME"), is("Ashton"));
        assertThat(features.get(0).getAttribute("the_geom"), notNullValue());
        // check the second feature with no geometry
        assertThat(features.get(1).getAttribute("FID"), notNullValue());
        assertThat(features.get(1).getAttribute("FID"), is("118"));
        assertThat(features.get(1).getAttribute("NAME"), is("Goose Island"));
        assertThat(features.get(1).getAttribute("the_geom"), nullValue());
    }

    @Test
    public void testCharset() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(BASIC_POLYGONS);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.setCharset(Charset.forName("ISO-8859-15"));
        dumper.dump(fc);

        SimpleFeatureCollection actual = getFeaturesFromShapefile(BASIC_POLYGONS);
        assertEquals(3, actual.size());
        assertFieldsNotEmpty(actual);
        checkTypeStructure(actual.getSchema(), MultiPolygon.class, "ID");
        assertCst(BASIC_POLYGONS, "ISO-8859-15");
    }

    private SimpleFeatureCollection testBasicPolygonCollection(int expectedSize, String typeName)
            throws IOException {
        SimpleFeatureCollection fc = getFeaturesFromShapefile(typeName);
        assertEquals(expectedSize, fc.size());
        assertFieldsNotEmpty(fc);
        checkTypeStructure(fc.getSchema(), MultiPolygon.class, "ID");
        assertCst(typeName, "ISO-8859-1");
        return fc;
    }

    @Test
    public void testMultipleTypes() throws Exception {
        testMultipleTypes(ALL_TYPES, ALL_TYPES);
    }

    @Test
    public void testMultipleTypesDot() throws Exception {
        testMultipleTypes("All.Types.Dots", "All.Types.Dots");
    }

    private void testMultipleTypes(String inputTypeName, String baseTypeName) throws IOException {
        SimpleFeatureCollection fc = getFeaturesFromProperties(inputTypeName);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.dump(fc);

        // points
        SimpleFeatureCollection point = getFeaturesFromShapefile(baseTypeName + "Point");
        assertEquals(1, point.size());
        assertFieldsNotEmpty(point);
        checkTypeStructure(point.getSchema(), Point.class, "name");
        assertCst(baseTypeName + "Point", "ISO-8859-1");

        // multipoints
        SimpleFeatureCollection mpoint = getFeaturesFromShapefile(baseTypeName + "MPoint");
        assertEquals(1, mpoint.size());
        assertFieldsNotEmpty(mpoint);
        checkTypeStructure(mpoint.getSchema(), MultiPoint.class, "name");
        assertCst(baseTypeName + "MPoint", "ISO-8859-1");

        // polygon (and multipolygon)
        SimpleFeatureCollection polygon = getFeaturesFromShapefile(baseTypeName + "Polygon");
        assertEquals(2, polygon.size());
        assertFieldsNotEmpty(polygon);
        checkTypeStructure(polygon.getSchema(), MultiPolygon.class, "name");
        assertCst(baseTypeName + "Polygon", "ISO-8859-1");

        // line (and multiline)
        SimpleFeatureCollection line = getFeaturesFromShapefile(baseTypeName + "Line");
        assertEquals(2, line.size());
        assertFieldsNotEmpty(line);
        checkTypeStructure(line.getSchema(), MultiLineString.class, "name");
        assertCst(baseTypeName + "Line", "ISO-8859-1");
    }

    @Test
    public void testMultipleTypesWithNullGeometries() throws Exception {
        // features with null geometries  will be wrote to AllTypesWithNull_NULL file
        testMultipleTypes(ALL_TYPES_WITH_NULL, ALL_TYPES_WITH_NULL);
        // check that NULL geometries where wrote to the correct file
        SimpleFeatureCollection nullGeometries =
                getFeaturesFromShapefile(ALL_TYPES_WITH_NULL + "_NULL");
        assertEquals(2, nullGeometries.size());
        checkTypeStructure(nullGeometries.getSchema(), Point.class, "name");
        assertCst(ALL_TYPES_WITH_NULL + "_NULL", "ISO-8859-1");
        // check that name attribute was correctly handled
        getFeaturesSortedById(nullGeometries)
                .forEach(
                        feature -> {
                            assertThat(feature.getAttribute("name"), notNullValue());
                            assertThat(
                                    feature.getAttribute("name"),
                                    CoreMatchers.anyOf(is("f007"), is("f008")));
                            assertThat(feature.getAttribute("geom"), nullValue());
                        });
    }

    @Test
    public void testEmptyCollectionAllowNoDump() throws Exception {
        SimpleFeatureCollection fc =
                getFeaturesFromProperties(BASIC_POLYGONS).subCollection(Filter.EXCLUDE);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.setEmptyShapefileAllowed(false);
        assertFalse(dumper.dump(fc));
        assertEquals(0, dumperFolder.list().length);
    }

    @Test
    public void testEmptyCollection() throws Exception {
        SimpleFeatureCollection fc =
                getFeaturesFromProperties(BASIC_POLYGONS).subCollection(Filter.EXCLUDE);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        assertFalse(dumper.dump(fc));

        SimpleFeatureCollection actual = getFeaturesFromShapefile(BASIC_POLYGONS);
        assertEquals(0, actual.size());
        assertFieldsNotEmpty(actual);
        checkTypeStructure(actual.getSchema(), MultiPolygon.class, "ID");
    }

    @Test
    public void testEmptyMultipleTypes() throws Exception {
        SimpleFeatureCollection fc =
                getFeaturesFromProperties(ALL_TYPES).subCollection(Filter.EXCLUDE);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        assertFalse(dumper.dump(fc));

        SimpleFeatureCollection allTypes = getFeaturesFromShapefile("AllTypes");
        assertEquals(0, allTypes.size());
        checkTypeStructure(allTypes.getSchema(), Point.class, "name");
    }

    @Test
    public void testEmptyMultipleTypesAllowNoDump() throws Exception {
        SimpleFeatureCollection fc =
                getFeaturesFromProperties(ALL_TYPES).subCollection(Filter.EXCLUDE);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.setEmptyShapefileAllowed(false);
        assertFalse(dumper.dump(fc));

        assertEquals(0, dumperFolder.list().length);
    }

    @Test(expected = ShapefileSizeException.class)
    public void testImpossibleMaxShpSize() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(BASIC_POLYGONS);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.setMaxShpSize(1);
        dumper.dump(fc);
    }

    @Test(expected = ShapefileSizeException.class)
    public void testImpossibleMaxDbfSize() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(BASIC_POLYGONS);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        dumper.setMaxDbfSize(1);
        dumper.dump(fc);
    }

    @Test
    public void testSplitOverThree() throws Exception {
        SimpleFeatureCollection fc = getFeaturesFromProperties(BASIC_POLYGONS);
        ShapefileDumper dumper = new ShapefileDumper(dumperFolder);
        // set a size small enough that only a single feature will fit
        dumper.setMaxDbfSize(500);
        dumper.dump(fc);

        testBasicPolygonCollection(1, BASIC_POLYGONS);
        testBasicPolygonCollection(1, BASIC_POLYGONS + "1");
        testBasicPolygonCollection(1, BASIC_POLYGONS + "2");
    }

    /** Verifies the contents of the CST file are the expected ones */
    private void assertCst(String typeName, String expectedCharset) throws IOException {
        File cst = new File(dumperFolder, typeName + ".cst");
        String actualCharset = FileUtils.readFileToString(cst, "UTF-8");
        assertEquals(expectedCharset, actualCharset);
    }

    /**
     * Returns a collection from one of the property sample data
     *
     * @param typeName The name of the property file (without .properties)
     */
    private SimpleFeatureCollection getFeaturesFromProperties(String typeName) throws IOException {
        return propertyStore.getFeatureSource(typeName).getFeatures();
    }

    /**
     * Returns a collection from the dumper folder given a type name. The support shapefile data
     * store will be closed automatically by the test machinery during tear down
     */
    private SimpleFeatureCollection getFeaturesFromShapefile(String typeName) throws IOException {
        File shp = new File(dumperFolder, typeName + ".shp");
        if (!shp.exists()) {
            fail(
                    "Could not find expected shapefile "
                            + shp.getPath()
                            + ", available files are: "
                            + Arrays.asList(dumperFolder.listFiles()));
        }
        // check all the sidecar files are there
        final String[] extensions = new String[] {".shx", ".dbf", ".prj", ".cst"};
        for (String extension : extensions) {
            File f = new File(dumperFolder, typeName + extension);
            if (!shp.exists()) {
                fail(
                        "Could not find expected shapefile sidecar "
                                + shp.getPath()
                                + ", available files are: "
                                + Arrays.asList(dumperFolder.listFiles()));
            }
        }

        // extract the features
        ShapefileDataStore ds = new ShapefileDataStore(URLs.fileToUrl(shp));
        shapefileStores.add(ds);
        return ds.getFeatureSource().getFeatures();
    }

    /**
     * Verifies the specified type has the right geometry type, and the specified list of attributes
     */
    private void checkTypeStructure(
            SimpleFeatureType type, Class geometryType, String... attributes) {
        assertEquals(geometryType, type.getGeometryDescriptor().getType().getBinding());
        if (attributes == null) {
            assertEquals(1, type.getDescriptors().size());
        } else {
            assertEquals(1 + attributes.length, type.getDescriptors().size());
            for (String attribute : attributes) {
                assertNotNull(
                        "Could not find attribute "
                                + attribute
                                + ", avaiable ones are "
                                + type.getAttributeDescriptors(),
                        type.getDescriptor(attribute));
            }
        }
    }

    /**
     * Helper method that extract the features from a feature collection and sort them by their ID.
     */
    private List<SimpleFeature> getFeaturesSortedById(SimpleFeatureCollection featureCollection) {
        // extract the features
        List<SimpleFeature> features = new ArrayList<>();
        try (SimpleFeatureIterator iterator = featureCollection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                features.add(feature);
            }
        }
        // sort the features by their ID
        Collections.sort(
                features,
                (feature1, feature2) -> {
                    assertThat(feature1.getID(), notNullValue());
                    assertThat(feature2.getID(), notNullValue());
                    return feature1.getID().compareTo(feature2.getID());
                });
        return features;
    }

    private void assertFieldsNotEmpty(SimpleFeatureCollection fc) {
        try (SimpleFeatureIterator fi = fc.features()) {
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                for (Object attrValue : f.getAttributes()) {
                    assertNotNull(attrValue);
                    if (Geometry.class.isAssignableFrom(attrValue.getClass()))
                        assertFalse("Empty geometry", ((Geometry) attrValue).isEmpty());
                    else
                        assertFalse(
                                "Empty value for attribute",
                                attrValue.toString().trim().equals(""));
                }
            }
        }
    }
}
