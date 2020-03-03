/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FakeTypes;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests cases for DataUtilities.
 *
 * @author Jody Garnett, Refractions Research
 */
public class DataUtilitiesTest extends DataTestCase {
    /** Constructor for DataUtilitiesTest. */
    public DataUtilitiesTest(String arg0) {
        super(arg0);
    }

    public void testSimpleCollection() {
        FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection =
                DataUtilities.collection(roadFeatures);
        SimpleFeatureCollection simple = DataUtilities.simple(featureCollection);
        assertSame(simple, featureCollection); // we expect a straight cast
        assertEquals(roadFeatures.length, featureCollection.size());
    }

    public void testSimpleCollectionList() {
        SimpleFeatureCollection featureCollection =
                DataUtilities.collection(Arrays.asList(roadFeatures));
        assertEquals(roadFeatures.length, featureCollection.size());
    }

    public void testSimpleSource() {
        SimpleFeatureCollection collection = DataUtilities.collection(roadFeatures);
        FeatureSource<SimpleFeatureType, SimpleFeature> source = DataUtilities.source(collection);
        SimpleFeatureSource simple = DataUtilities.simple(source);
        assertSame(simple, source);
    }

    public void testSimpleType() throws DataSourceException {
        SimpleFeatureType simpleFeatureType = DataUtilities.simple(FakeTypes.Mine.MINETYPE_TYPE);
        assertEquals(null, simpleFeatureType.getGeometryDescriptor());
    }

    public void testDataStore() throws IOException {
        SimpleFeatureSource features = DataUtilities.source(roadFeatures);
        Name name = features.getName();
        String typeName = name.getLocalPart();

        DataStore store = DataUtilities.dataStore(features);

        assertSame(features.getSchema(), store.getSchema(name));
        assertSame(features.getSchema(), store.getSchema(typeName));
        assertSame(features, store.getFeatureSource(name));
        assertSame(features, store.getFeatureSource(typeName));
        assertEquals(name, store.getNames().get(0));
        assertEquals(typeName, store.getTypeNames()[0]);
    }

    public void testFirst() {
        SimpleFeatureCollection collection = DataUtilities.collection(roadFeatures);
        SimpleFeature first = DataUtilities.first(collection);
        assertNotNull(first);
        assertEquals("road.rd1", first.getID());
    }

    public void testCheckDirectory() {
        File home = new File(System.getProperty("user.home"));
        File file = DataUtilities.checkDirectory(home);
        assertNotNull(file);

        File missing = new File(home, ".missing");
        try {
            File found = DataUtilities.checkDirectory(missing);
            assertNull(found);
            fail("Missing should not be found");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testReadable() {
        File home = new File(System.getProperty("user.home"));
        assertFalse("Home is not a readable file", DataUtilities.checkFileReadable(home, null));

        FilenameFilter findFilter =
                new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        File target = new File(dir, name);
                        return target.isFile() && target.canRead();
                    }
                };
        File readable[] = home.listFiles(findFilter);
        if (readable.length > 0) {
            File test = readable[0];
            assertTrue(test.toString(), DataUtilities.checkFileReadable(test, null));
        }
    }

    public void testFilters() {
        File home = new File(System.getProperty("user.home"));
        FilenameFilter directoryFilter =
                new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        File target = new File(dir, name);
                        return target.isDirectory();
                    }
                };
        FilenameFilter hiddenFilter =
                new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        File target = new File(dir, name);
                        return target.isHidden();
                    }
                };
        List<String> dir = Arrays.asList(home.list(directoryFilter));
        List<String> hidden = Arrays.asList(home.list(hiddenFilter));

        FilenameFilter includeFilter = DataUtilities.includeFilters(directoryFilter, hiddenFilter);
        List<String> include = Arrays.asList(home.list(includeFilter));

        Set<String> both = new HashSet<String>();
        both.addAll(dir);
        both.addAll(hidden);
        assertEquals(both.size(), include.size());

        FilenameFilter excludeFilter = DataUtilities.excludeFilters(directoryFilter, hiddenFilter);
        List<String> exclude = Arrays.asList(home.list(excludeFilter));

        Set<String> subtract = new HashSet<String>(dir);
        subtract.removeAll(hidden);
        assertEquals(subtract.size(), exclude.size());
    }

    /** Test for {@link DataUtilities#attributeNames(FeatureType)} */
    public void testAttributeNamesFeatureType() {
        String[] names;

        names = DataUtilities.attributeNames(roadType);
        assertEquals(4, names.length);
        assertEquals("id", names[0]);
        assertEquals("geom", names[1]);
        assertEquals("name", names[2]);
        assertEquals("uuid", names[3]);

        names = DataUtilities.attributeNames(subRoadType);
        assertEquals(2, names.length);
        assertEquals("id", names[0]);
        assertEquals("geom", names[1]);
    }

    /*
     * Test for String[] attributeNames(Filter)
     */
    public void testAttributeNamesFilter() throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        String[] names;

        Filter filter = null;

        // check null
        names = DataUtilities.attributeNames(filter);
        assertEquals(names.length, 0);

        Id fidFilter = factory.id(Collections.singleton(factory.featureId("fid")));

        // check fidFilter
        names = DataUtilities.attributeNames(fidFilter);
        assertEquals(0, names.length);

        PropertyName id = factory.property("id");
        PropertyName name = factory.property("name");
        PropertyName geom = factory.property("geom");

        // check nullFilter
        PropertyIsNull nullFilter = factory.isNull(id);
        names = DataUtilities.attributeNames(nullFilter);
        assertEquals(1, names.length);
        assertEquals("id", names[0]);

        PropertyIsEqualTo equal = factory.equals(name, id);
        names = DataUtilities.attributeNames(equal);
        assertEquals(2, names.length);
        List list = Arrays.asList(names);
        assertTrue(list.contains("name"));
        assertTrue(list.contains("id"));

        Function fnCall = factory.function("Max", new Expression[] {id, name});

        PropertyIsLike fn = factory.like(fnCall, "does-not-matter");
        names = DataUtilities.attributeNames(fn);
        list = Arrays.asList(names);
        assertTrue(list.contains("name"));
        assertTrue(list.contains("id"));

        PropertyIsBetween between = factory.between(name, id, geom);
        names = DataUtilities.attributeNames(between);
        assertEquals(3, names.length);
        list = Arrays.asList(names);
        assertTrue(list.contains("name"));
        assertTrue(list.contains("id"));
        assertTrue(list.contains("geom"));

        // check logic filter
        PropertyIsNull geomNull = factory.isNull(geom);
        names = DataUtilities.attributeNames(factory.and(geomNull, equal));
        assertEquals(3, names.length);
        list = Arrays.asList(names);
        assertTrue(list.contains("name"));
        assertTrue(list.contains("id"));
        assertTrue(list.contains("geom"));

        // check not filter
        names = DataUtilities.attributeNames(factory.not(geomNull));
        assertEquals(1, names.length);
        assertEquals("geom", names[0]);

        // check with namespace qualified name
        PropertyIsEqualTo equalToWithPrefix = factory.equals(factory.property("gml:name"), id);
        names = DataUtilities.attributeNames(equalToWithPrefix, roadType);
        assertEquals(2, names.length);
        list = Arrays.asList(names);
        assertTrue(list.contains("name"));
        assertTrue(list.contains("id"));
    }

    /** Test for {@link DataUtilities#attributeNames(Filter, FeatureType)} */
    public void testAttributeNamesFilterFeatureType() {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        Filter filter =
                factory.equals(
                        factory.property("id"),
                        factory.add(factory.property("geom"), factory.property("gml:name")));

        String[] names;

        names = DataUtilities.attributeNames(filter, roadType);
        assertEquals(3, names.length);
        List namesList = Arrays.asList(names);
        assertTrue(namesList.contains("id"));
        assertTrue(namesList.contains("geom"));
        assertTrue(namesList.contains("name"));
    }

    /** Test for {@link DataUtilities#attributeNames(Expression, FeatureType)} */
    public void testAttributeExpressionFilterFeatureType() {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        Expression expression = factory.add(factory.property("geom"), factory.property("gml:name"));

        String[] names;

        names = DataUtilities.attributeNames(expression, roadType);
        assertEquals(2, names.length);
        List namesList = Arrays.asList(names);
        assertTrue(namesList.contains("geom"));
        assertTrue(namesList.contains("name"));
    }

    public void testCompare() throws SchemaException {
        assertEquals(0, DataUtilities.compare(null, null));
        assertEquals(-1, DataUtilities.compare(roadType, null));
        assertEquals(-1, DataUtilities.compare(null, roadType));
        assertEquals(-1, DataUtilities.compare(riverType, roadType));
        assertEquals(-1, DataUtilities.compare(roadType, riverType));
        assertEquals(0, DataUtilities.compare(roadType, roadType));
        assertEquals(1, DataUtilities.compare(subRoadType, roadType));

        // different order
        SimpleFeatureType road2 =
                DataUtilities.createType("namespace.road", "geom:LineString,name:String,id:0");
        assertEquals(1, DataUtilities.compare(road2, roadType));

        // different namespace
        SimpleFeatureType road3 =
                DataUtilities.createType("test.road", "id:0,geom:LineString,name:String,uuid:UUID");
        assertEquals(0, DataUtilities.compare(road3, roadType));

        // different attribute bindings
        SimpleFeatureType road4 =
                DataUtilities.createType("road", "id:0,geom:LineString,name:String,uuid:String");
        assertEquals(-1, DataUtilities.compare(road4, roadType));
    }

    public void testCompareNames() throws SchemaException {
        assertEquals(0, DataUtilities.compareNames(null, null));
        assertEquals(-1, DataUtilities.compareNames(roadType, null));
        assertEquals(-1, DataUtilities.compareNames(null, roadType));
        assertEquals(-1, DataUtilities.compareNames(riverType, roadType));
        assertEquals(-1, DataUtilities.compareNames(roadType, riverType));
        assertEquals(0, DataUtilities.compareNames(roadType, roadType));
        assertEquals(1, DataUtilities.compareNames(subRoadType, roadType));

        // different order
        SimpleFeatureType road2 =
                DataUtilities.createType("namespace.road", "geom:LineString,name:String,id:0");
        assertEquals(1, DataUtilities.compareNames(road2, roadType));

        // different namespace
        SimpleFeatureType road3 =
                DataUtilities.createType("test.road", "id:0,geom:LineString,name:String,uuid:UUID");
        assertEquals(0, DataUtilities.compareNames(road3, roadType));

        // same name, different attribute bindings
        SimpleFeatureType road4 =
                DataUtilities.createType("road", "id:0,geom:LineString,name:String,uuid:String");
        assertEquals(0, DataUtilities.compareNames(road4, roadType));

        // different order & attribute bindings
        SimpleFeatureType road5 =
                DataUtilities.createType("road", "id:0,uuid:String,geom:LineString,name:String");
        assertEquals(1, DataUtilities.compareNames(road5, roadType));
    }

    public void testIsMatch() throws SchemaException {
        SimpleFeatureType roadType1 =
                DataUtilities.createType("road", "id:0,geom:LineString,name:String,uuid:String");

        // different binding mismatch when strict flg set
        assertEquals(
                false,
                DataUtilities.isMatch(
                        roadType.getDescriptor("uuid"), roadType1.getDescriptor("uuid")));

        // different binding match when strict flg not set
        assertEquals(
                true,
                DataUtilities.isMatch(
                        roadType.getDescriptor("uuid"), roadType1.getDescriptor("uuid"), false));

        // different names mismatch in both cases
        SimpleFeatureType roadType2 =
                DataUtilities.createType("road", "id:0,the_geom:LineString,name:String,uuid:UUID");
        assertEquals(
                false,
                DataUtilities.isMatch(
                        roadType.getDescriptor("geom"), roadType2.getDescriptor("the_geom")));
        assertEquals(
                false,
                DataUtilities.isMatch(
                        roadType.getDescriptor("geom"),
                        roadType2.getDescriptor("the_geom"),
                        false));

        SimpleFeatureType roadType3 =
                DataUtilities.createType(
                        "road", "id:0,the_geom:LineString,geom:String,name:String,uuid:UUID");
        // same names different descriptors mismatch when strict flg set
        assertEquals(
                false,
                DataUtilities.isMatch(
                        roadType.getDescriptor("geom"), roadType3.getDescriptor("geom")));

        // same names different descriptors match when strict flg not set
        assertEquals(
                true,
                DataUtilities.isMatch(
                        roadType.getDescriptor("geom"), roadType3.getDescriptor("geom"), false));
    }

    public void testReType() throws Exception {
        SimpleFeature rd1 = roadFeatures[0];
        assertEquals(rd1, rd1);

        SimpleFeature rdDuplicate = SimpleFeatureBuilder.copy(rd1);

        assertEquals(rd1, rdDuplicate);
        assertNotSame(rd1, rdDuplicate);

        SimpleFeature rd2 = DataUtilities.reType(roadType, rd1);

        assertEquals(rd1, rd2);
        assertNotSame(rd1, rd2);

        SimpleFeature rd3 = DataUtilities.reType(subRoadType, rd1);

        assertFalse(rd1.equals(rd3));
        assertEquals(2, rd3.getAttributeCount());
        assertEquals(rd1.getID(), rd3.getID());
        assertEquals(rd1.getAttribute("id"), rd3.getAttribute("id"));
        assertEquals((Geometry) rd1.getAttribute("geom"), (Geometry) rd3.getAttribute("geom"));
        assertNotNull(rd3.getDefaultGeometry());

        SimpleFeature rv1 = riverFeatures[0];
        assertEquals(rv1, rv1);

        SimpleFeature rvDuplicate = SimpleFeatureBuilder.copy(rv1);

        assertEquals(rv1, rvDuplicate);
        assertNotSame(rv1, rvDuplicate);

        SimpleFeature rv2 = DataUtilities.reType(riverType, rv1);

        assertEquals(rv1, rv2);
        assertNotSame(rv1, rv2);

        SimpleFeature rv3 = DataUtilities.reType(subRiverType, rv1);

        assertFalse(rv1.equals(rv3));
        assertEquals(2, rv3.getAttributeCount());
        assertEquals(rv1.getID(), rv3.getID());
        assertEquals(rv1.getAttribute("name"), rv3.getAttribute("name"));
        assertEquals(rv1.getAttribute("flow"), rv3.getAttribute("flow"));
        assertNull(rv3.getDefaultGeometry());
    }
    /** Test createType and createFeature methods as per GEOT-4150 */
    public void testCreate() throws Exception {
        SimpleFeatureType featureType =
                DataUtilities.createType(
                        "Contact", "id:Integer,party:String,geom:Geometry:srid=4326");
        SimpleFeature feature1 =
                DataUtilities.createFeature(
                        featureType, "fid1=1|Jody Garnett\\nSteering Committee|POINT(1 2)");
        SimpleFeature feature2 =
                DataUtilities.createFeature(
                        featureType, "2|John Hudson\\|Hapless Victim|POINT(6 2)");

        assertNotNull(featureType.getCoordinateReferenceSystem());

        Geometry geometry = (Geometry) feature1.getAttribute("geom");
        assertEquals("geom", 2.0, geometry.getCoordinate().y);
        assertEquals("fid preservation", "fid1", feature1.getID());

        // test escape handling
        assertEquals(
                "newline decode check",
                "Jody Garnett\nSteering Committee",
                feature1.getAttribute("party"));
        assertEquals("escape check", "John Hudson|Hapless Victim", feature2.getAttribute("party"));

        // test feature id handling
        assertEquals("fid1", feature1.getID());
        assertNotNull(feature2.getID());
        assertEquals(feature2.getID(), feature2.getIdentifier().getID());

        // test geometry handling
        GeometryBuilder geomBuilder = new GeometryBuilder();
        assertEquals(geomBuilder.point(6, 2), feature2.getDefaultGeometry());
        assertEquals(geomBuilder.point(6, 2), feature2.getAttribute("geom"));
    }
    /** Test createType and createFeature methods as per GEOT-4150 */
    public void testEncode() throws Exception {
        SimpleFeatureType featureType =
                DataUtilities.createType(
                        "Contact", "id:Integer,party:String,geom:Geometry:srid=4326");
        SimpleFeature feature1 =
                DataUtilities.createFeature(
                        featureType, "fid1=1|Jody Garnett\\nSteering Committee|POINT (1 2)");
        SimpleFeature feature2 =
                DataUtilities.createFeature(
                        featureType, "2|John Hudson\\|Hapless Victim|POINT (6 2)");

        String spec = DataUtilities.encodeType(featureType);
        assertEquals("id:Integer,party:String,geom:Geometry:srid=4326", spec);

        String text = DataUtilities.encodeFeature(feature1);
        assertEquals("fid1=1|Jody Garnett\\nSteering Committee|POINT (1 2)", text);
    }

    /**
     * Test createType and encode can handle com.vividsolutions.jts and org.locationtech.jts
     * bindings.
     */
    public void testDecodeGeometrySpec() throws Exception {
        SimpleFeatureType featureType1, featureType2;

        featureType1 = DataUtilities.createType("Contact", "id:Integer,party:String,geom:Geometry");
        featureType2 =
                DataUtilities.createType(
                        "Contact",
                        "id:Integer,party:String,geom:org.locationtech.jts.geom.Geometry");

        assertEquals(featureType1, featureType2);
        featureType2 =
                DataUtilities.createType(
                        "Contact",
                        "id:Integer,party:String,geom:com.vividsolutions.jts.geom.Geometry");
        assertEquals(featureType1, featureType2);

        assertEquals(
                DataUtilities.createAttribute("point:Point"),
                DataUtilities.createAttribute("point:com.vividsolutions.jts.geom.Point"));
        assertEquals(
                DataUtilities.createAttribute("point:Point"),
                DataUtilities.createAttribute("point:org.locationtech.jts.geom.Point"));

        assertEquals(
                DataUtilities.createAttribute("area:Polygon"),
                DataUtilities.createAttribute("area:com.vividsolutions.jts.geom.Polygon"));
    }
    /*
     * Test for Feature template(FeatureType)
     */
    public void testTemplateFeatureType() throws IllegalAttributeException {
        SimpleFeature feature = DataUtilities.template(roadType);
        assertNotNull(feature);
        assertEquals(roadType.getAttributeCount(), feature.getAttributeCount());
    }

    /*
     * Test for Feature template(FeatureType, String)
     */
    public void testTemplateFeatureTypeString() throws IllegalAttributeException {
        SimpleFeature feature = DataUtilities.template(roadType, "Foo");
        assertNotNull(feature);
        assertEquals(roadType.getAttributeCount(), feature.getAttributeCount());
        assertEquals("Foo", feature.getID());
        assertNull(feature.getAttribute("name"));
        assertNull(feature.getAttribute("id"));
        assertNull(feature.getAttribute("geom"));
    }

    public void testDefaultValues() throws IllegalAttributeException {
        Object[] values = DataUtilities.defaultValues(roadType);
        assertNotNull(values);
        assertEquals(values.length, roadType.getAttributeCount());
    }

    public void testDefaultValue() throws IllegalAttributeException {
        assertNull(roadType.getDescriptor("name").getDefaultValue());
        assertNull(roadType.getDescriptor("id").getDefaultValue());
        assertNull(roadType.getDescriptor("geom").getDefaultValue());

        GeometryFactory fac = new GeometryFactory();
        Coordinate coordinate = new Coordinate(0, 0);
        Point point = fac.createPoint(coordinate);

        Geometry geometry = fac.createGeometry(point);
        assertEquals(geometry, DataUtilities.defaultValue(Geometry.class));
    }

    public void testDefaultValueArray() throws Exception {
        assertArrayEquals(new byte[] {}, (byte[]) DataUtilities.defaultValue(byte[].class));
        assertArrayEquals(new String[] {}, (String[]) DataUtilities.defaultValue(String[].class));
    }

    public void testCollection() {
        SimpleFeatureCollection collection = DataUtilities.collection(roadFeatures);
        assertEquals(roadFeatures.length, collection.size());
    }

    public void testBounds() {
        SimpleFeatureCollection collection = DataUtilities.collection(roadFeatures);

        ReferencedEnvelope expected = collection.getBounds();
        ReferencedEnvelope actual = DataUtilities.bounds(collection);
        assertEquals(expected, actual);
    }

    public void testCollectionList() {
        SimpleFeatureCollection collection = DataUtilities.collection(Arrays.asList(roadFeatures));
        assertEquals(roadFeatures.length, collection.size());
    }

    public void testReaderFeatureArray() throws Exception {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(roadFeatures);
        assertEquals(roadFeatures.length, count(reader));
    }

    public void testReaderCollection() throws Exception {
        SimpleFeatureCollection collection = DataUtilities.collection(roadFeatures);
        assertEquals(roadFeatures.length, collection.size());

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(collection);
        assertEquals(roadFeatures.length, count(reader));
    }

    public void testCreateSubType() throws Exception {
        SimpleFeatureType before =
                DataUtilities.createType("cities", "the_geom:Point:srid=4326,name:String");
        SimpleFeatureType after = DataUtilities.createSubType(before, new String[] {"the_geom"});
        assertEquals(1, after.getAttributeCount());

        before =
                DataUtilities.createType(
                        "cities", "the_geom:Point:srid=4326,name:String,population:Integer");
        URI here = new URI("http://localhost/");
        after =
                DataUtilities.createSubType(
                        before, new String[] {"the_geom"}, DefaultGeographicCRS.WGS84, "now", here);
        assertEquals(here.toString(), after.getName().getNamespaceURI());
        assertEquals("now", after.getName().getLocalPart());
        assertEquals(DefaultGeographicCRS.WGS84, after.getCoordinateReferenceSystem());
        assertEquals(1, after.getAttributeCount());
        assertEquals("the_geom", after.getDescriptor(0).getLocalName());
        assertNotNull(after.getGeometryDescriptor());

        // check that subtyping does not cause the geometry attribute structure to change
        before =
                DataUtilities.createType(
                        "cities", "the_geom:Point:srid=4326,name:String,population:Integer");
        after = DataUtilities.createSubType(before, new String[] {"the_geom"});
        assertEquals(before.getGeometryDescriptor(), after.getGeometryDescriptor());

        // check that subtyping does not cause the geometry attribute structure to change
        before =
                DataUtilities.createType(
                        "cities", "the_geom:Point:srid=4326,name:String,population:Integer");
        after = DataUtilities.createSubType(before, new String[] {"the_geom"});
        assertEquals(before.getGeometryDescriptor(), after.getGeometryDescriptor());
    }

    public void testCreateSubTypePreservesDefaultGeometryProperty() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("name", String.class);
        tb.add("the_geom1", Point.class, 4326);
        tb.add("the_geom2", Point.class, 4326);
        tb.add("the_geom3", Point.class, 4326);
        tb.setDefaultGeometry("the_geom2");
        SimpleFeatureType before = tb.buildFeatureType();
        SimpleFeatureType after =
                DataUtilities.createSubType(
                        before, new String[] {"name", "the_geom1", "the_geom3"});
        assertEquals(3, after.getAttributeCount());
        assertEquals("the_geom1", after.getGeometryDescriptor().getLocalName());

        after =
                DataUtilities.createSubType(
                        before,
                        new String[] {"name", "the_geom1", "the_geom3"},
                        DefaultGeographicCRS.WGS84);
        assertEquals(3, after.getAttributeCount());
        assertEquals("the_geom1", after.getGeometryDescriptor().getLocalName());

        after =
                DataUtilities.createSubType(
                        before, new String[] {"name", "the_geom1", "the_geom2"});
        assertEquals(3, after.getAttributeCount());
        assertEquals("the_geom2", after.getGeometryDescriptor().getLocalName());

        after =
                DataUtilities.createSubType(
                        before,
                        new String[] {"name", "the_geom1", "the_geom2"},
                        DefaultGeographicCRS.WGS84);
        assertEquals(3, after.getAttributeCount());
        assertEquals("the_geom2", after.getGeometryDescriptor().getLocalName());
    }

    public void testCreateSubTypeWithPropertyNotMatchingAnAttributeDescriptor() throws Exception {
        // creating a sub type with a property that doesn't map to an attribute descriptor
        SimpleFeatureType before =
                DataUtilities.createType("cities", "the_geom:Point:srid=4326,name:String");
        SimpleFeatureType after =
                DataUtilities.createSubType(
                        before, new String[] {"the_geom", "name", "not_existing"});
        // the not_existing property should have been ignored
        assertEquals(2, after.getAttributeCount());
        assertNotNull(after.getDescriptor("the_geom"));
        assertNotNull(after.getDescriptor("name"));
    }

    public void testSource() throws Exception {
        SimpleFeatureSource s = DataUtilities.source(roadFeatures);
        assertEquals(3, s.getCount(Query.ALL));
        assertEquals(3, s.getFeatures().size());
        assertEquals(3, s.getFeatures(Query.ALL).size());
        assertEquals(3, s.getFeatures(Filter.INCLUDE).size());
        assertEquals(0, s.getFeatures(Filter.EXCLUDE).size());
        assertEquals(1, s.getFeatures(rd1Filter).size());
        assertEquals(2, s.getFeatures(rd12Filter).size());
    }

    /** tests the policy of DataUtilities.mixQueries */
    public void testMixQueries() throws Exception {
        Query firstQuery;
        Query secondQuery;

        firstQuery =
                new Query(
                        "typeName",
                        Filter.EXCLUDE,
                        100,
                        new String[] {"att1", "att2", "att3"},
                        "handle");
        secondQuery =
                new Query(
                        "typeName",
                        Filter.EXCLUDE,
                        20,
                        new String[] {"att1", "att2", "att4"},
                        "handle2");
        secondQuery.setStartIndex(4);

        Query mixed = DataUtilities.mixQueries(firstQuery, secondQuery, "newhandle");

        // the passed handle
        assertEquals("newhandle", mixed.getHandle());
        // the lower of both
        assertEquals(20, mixed.getMaxFeatures());
        // att1, 2, 3 and 4
        assertEquals(4, mixed.getPropertyNames().length);
        assertEquals(4, (int) mixed.getStartIndex());

        // now use some filters
        Filter filter1 = null;
        Filter filter2 = null;
        FilterFactory ffac = CommonFactoryFinder.getFilterFactory(null);

        String typeSpec = "geom:Point,att1:String,att2:String,att3:String,att4:String";
        SimpleFeatureType testType = DataUtilities.createType("testType", typeSpec);
        // System.err.println("created test type: " + testType);

        filter1 = ffac.equals(ffac.property("att1"), ffac.literal("val1"));
        filter2 = ffac.equals(ffac.property("att2"), ffac.literal("val2"));

        firstQuery = new Query("typeName", filter1, 100, (String[]) null, "handle");
        secondQuery =
                new Query(
                        "typeName", filter2, 20, new String[] {"att1", "att2", "att4"}, "handle2");

        mixed = DataUtilities.mixQueries(firstQuery, secondQuery, "newhandle");

        // the passed handle
        assertEquals("newhandle", mixed.getHandle());
        // the lower of both
        assertEquals(20, mixed.getMaxFeatures());
        // att1, 2 and 4
        assertEquals(3, mixed.getPropertyNames().length);

        Filter mixedFilter = mixed.getFilter();
        assertNotNull(mixedFilter);
        assertTrue(mixedFilter instanceof BinaryLogicOperator);
        BinaryLogicOperator f = (BinaryLogicOperator) mixedFilter;

        assertTrue(f instanceof And);
        for (Iterator fit = f.getChildren().iterator(); fit.hasNext(); ) {
            Filter subFilter = (Filter) fit.next();
            assertTrue(filter1.equals(subFilter) || filter2.equals(subFilter));
        }

        // check mixing hints too
        firstQuery.setHints(new Hints(Hints.USE_PROVIDED_FID, Boolean.TRUE));
        secondQuery.setHints(new Hints(Hints.FEATURE_2D, Boolean.TRUE));
        mixed = DataUtilities.mixQueries(firstQuery, secondQuery, "newhandle");

        assertEquals(2, mixed.getHints().size());
        assertTrue((Boolean) mixed.getHints().get(Hints.USE_PROVIDED_FID));
        assertTrue((Boolean) mixed.getHints().get(Hints.FEATURE_2D));
    }

    public void testMixQueryAll() {
        // mixing Query.ALL equivalents with extra hints did not work
        Query firstQuery = new Query(Query.ALL);
        Query secondQuery = new Query(Query.ALL);
        firstQuery.setHints(new Hints(Hints.USE_PROVIDED_FID, Boolean.TRUE));
        secondQuery.setHints(new Hints(Hints.FEATURE_2D, Boolean.TRUE));

        Query mixed = DataUtilities.mixQueries(firstQuery, secondQuery, "mixer");
        assertEquals(2, mixed.getHints().size());
        assertTrue((Boolean) mixed.getHints().get(Hints.USE_PROVIDED_FID));
        assertTrue((Boolean) mixed.getHints().get(Hints.FEATURE_2D));
    }

    public void testSimplifyFilter() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter filter = ff.and(Filter.INCLUDE, Filter.INCLUDE);
        Query query = new Query(Query.ALL);
        query.setFilter(filter);
        final Query result = DataUtilities.simplifyFilter(query);
        assertEquals(Filter.INCLUDE, result.getFilter());
    }

    public void testSpecNoCRS() throws Exception {
        String spec = "id:String,polygonProperty:Polygon";
        SimpleFeatureType ft = DataUtilities.createType("testType", spec);
        String spec2 = DataUtilities.encodeType(ft);
        // System.out.println("BEFORE:"+spec);
        // System.out.println(" AFTER:"+spec2);
        assertEquals(spec, spec2);
    }

    public void testSpecCRS() throws Exception {
        String spec = "id:String,polygonProperty:Polygon:srid=32615";
        SimpleFeatureType ft = DataUtilities.createType("testType", spec);
        String spec2 = DataUtilities.encodeType(ft);
        // System.out.println("BEFORE:"+spec);
        // System.out.println(" AFTER:"+spec2);
        assertEquals(spec, spec2);
    }

    public void testAllGeometryTypes() throws Exception {
        List<Class<?>> bindings =
                Arrays.asList(
                        Geometry.class,
                        Point.class,
                        LineString.class,
                        Polygon.class,
                        MultiPoint.class,
                        MultiLineString.class,
                        MultiPolygon.class,
                        GeometryCollection.class);

        StringBuilder specBuilder = new StringBuilder();
        bindings.forEach(
                b ->
                        specBuilder
                                .append(b.getSimpleName())
                                .append("_type:")
                                .append(b.getName())
                                .append(','));

        String spec = specBuilder.toString();
        SimpleFeatureType ft = DataUtilities.createType("testType", spec);
        bindings.forEach(
                b -> {
                    AttributeDescriptor descriptor = ft.getDescriptor(b.getSimpleName() + "_type");
                    assertNotNull(descriptor);
                    assertEquals(b, descriptor.getType().getBinding());
                });
    }

    public void testSpecNotIdentifiable() throws Exception {
        String spec = "id:String,polygonProperty:Polygon:srid=32615";
        SimpleFeatureType ft = DataUtilities.createType("testType", spec);
        CoordinateReferenceSystem crsNoId =
                CRS.parseWKT(
                        "PROJCS[\"Geoscience Australia Standard National Scale Lambert Projection\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_1978\",6378135,298.26],TOWGS84[0,0,0]],PRIMEM[\"Greenwich\",0],UNIT[\"Decimal_Degree\",0.0174532925199433]],PROJECTION[\"Lambert_Conformal_Conic_2SP\"],PARAMETER[\"central_meridian\",134.0],PARAMETER[\"latitude_of_origin\",0.0],PARAMETER[\"standard_parallel_1\",-18.0],PARAMETER[\"standard_parallel_2\",-36.0],UNIT[\"Meter\",1]]");
        SimpleFeatureType transformedFt = FeatureTypes.transform(ft, crsNoId);

        // since we cannot go back to a code with do a best effort encoding
        String expected = "id:String,polygonProperty:Polygon";
        String spec2 = DataUtilities.encodeType(transformedFt);
        // System.out.println("  BEFORE:"+spec);
        // System.out.println("EXPECTED:"+expected);
        // System.out.println("  AFTER:"+spec2);
        assertEquals(expected, spec2);
    }

    public void testCreateView() throws Exception {
        String[] propNames = {"id", "geom"};
        Query query = new Query(roadType.getTypeName(), Filter.INCLUDE, 100, propNames, null);
        SimpleFeatureSource source = DataUtilities.source(roadFeatures);
        SimpleFeatureSource view = DataUtilities.createView(source, query);

        assertNotNull(view);
        List<AttributeDescriptor> desc = view.getSchema().getAttributeDescriptors();
        assertTrue(desc.size() == propNames.length);
        assertTrue(desc.get(0).getLocalName().equals(propNames[0]));
        assertTrue(desc.get(1).getLocalName().equals(propNames[1]));
    }

    public void testAddMandatoryProperties() {
        AttributeType at =
                new AttributeTypeImpl(
                        new NameImpl("String"),
                        String.class,
                        false,
                        false,
                        Collections.EMPTY_LIST,
                        null,
                        null);

        AttributeDescriptor descr1 =
                new AttributeDescriptorImpl(at, new NameImpl("att1"), 0, 1, false, null);
        AttributeDescriptor descr2 =
                new AttributeDescriptorImpl(at, new NameImpl("att2"), 0, 1, false, null);
        AttributeDescriptor descr3 =
                new AttributeDescriptorImpl(at, new NameImpl("att3"), 1, 1, false, null);
        AttributeDescriptor descr4 =
                new AttributeDescriptorImpl(at, new NameImpl("att4"), 1, 1, false, null);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("type");
        tb.add(descr1);
        tb.add(descr2);
        tb.add(descr3);
        tb.add(descr4);

        SimpleFeatureType type = tb.buildFeatureType();

        PropertyName propName1 = ff.property("att1");
        PropertyName propName2 = ff.property("att2");
        PropertyName propName3 = ff.property("att3");
        PropertyName propName4 = ff.property("att4");

        List<PropertyName> list = new ArrayList<PropertyName>();
        list.add(propName1);
        list.add(propName4);

        List<PropertyName> list2 = DataUtilities.addMandatoryProperties(type, list);

        assertTrue(list2.contains(propName1)); // in original list, not mandatory
        assertTrue(!list2.contains(propName2)); // not in original list and not mandatory
        assertTrue(list2.contains(propName3)); // mandatory
        assertTrue(list2.contains(propName4)); // mandatory and in list
        assertEquals(3, list2.size());
    }

    public void testMixQueriesSort() {
        // simple merge, no conflict
        Query q1 = new Query();
        Query q2 = new Query();
        q2.setSortBy(new SortBy[] {SortBy.NATURAL_ORDER});
        assertThat(
                DataUtilities.mixQueries(q1, q2, null).getSortBy(),
                arrayContaining(SortBy.NATURAL_ORDER));
        assertThat(
                DataUtilities.mixQueries(q2, q1, null).getSortBy(),
                arrayContaining(SortBy.NATURAL_ORDER));

        // more complex, override (the second wins)
        Query q3 = new Query();
        q3.setSortBy(new SortBy[] {SortBy.REVERSE_ORDER});
        assertThat(
                DataUtilities.mixQueries(q2, q3, null).getSortBy(),
                arrayContaining(SortBy.REVERSE_ORDER));
        assertThat(
                DataUtilities.mixQueries(q3, q2, null).getSortBy(),
                arrayContaining(SortBy.NATURAL_ORDER));
    }
}
