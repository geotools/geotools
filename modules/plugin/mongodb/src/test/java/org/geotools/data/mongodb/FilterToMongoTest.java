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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.math.BigInteger;
import java.util.Date;
import org.geotools.api.filter.And;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class FilterToMongoTest {

    static final String DATE_LITERAL = "2015-07-01T00:00:00.000+01:00";
    public static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    FilterFactory ff;
    FilterToMongo filterToMongo;
    FilterToMongo filterToMongo3857;
    MongoGeometryBuilder geometryBuilder;

    @Before
    public void setUp() throws FactoryException {
        ff = CommonFactoryFinder.getFilterFactory();
        filterToMongo = new FilterToMongo(new GeoJSONMapper());
        filterToMongo3857 = new FilterToMongo(new GeoJSONMapper());

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("ftTest");
        tb.setCRS(DefaultGeographicCRS.WGS84);
        tb.add("geometry", Point.class);
        tb.add("dateProperty", Date.class);
        filterToMongo.setFeatureType(tb.buildFeatureType());

        SimpleFeatureTypeBuilder tb3857 = new SimpleFeatureTypeBuilder();
        tb3857.setName("ftTest");
        tb3857.setCRS(CRS.decode("EPSG:3857"));
        tb3857.add("geometry", Point.class);
        tb3857.add("dateProperty", Date.class);
        filterToMongo3857.setFeatureType(tb3857.buildFeatureType());

        geometryBuilder = new MongoGeometryBuilder();
    }

    @Test
    public void testEqualTo() throws Exception {
        PropertyIsEqualTo equalTo = ff.equals(ff.property("foo"), ff.literal("bar"));
        BasicDBObject obj = (BasicDBObject) equalTo.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        Assert.assertEquals(1, obj.keySet().size());
        BasicDBObject operator = (BasicDBObject) obj.get("properties.foo");
        Assert.assertEquals("bar", operator.get("$eq"));
    }

    @Test
    public void testBBOX() {
        BBOX bbox = ff.bbox("loc", 10d, 10d, 20d, 20d, "epsg:4326");
        BasicDBObject obj = (BasicDBObject) bbox.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBObject filterGeometry = (BasicDBObject) obj.get("geometry");
        Assert.assertNotNull(filterGeometry);

        BasicDBObject filterIntersects = (BasicDBObject) filterGeometry.get("$geoIntersects");
        Assert.assertNotNull(filterIntersects);

        BasicDBObject filterIntersectsGeometry = (BasicDBObject) filterIntersects.get("$geometry");
        Assert.assertNotNull(filterIntersectsGeometry);

        Geometry geometry = geometryBuilder.toGeometry(filterIntersectsGeometry);
        Assert.assertTrue(Orientation.isCCW(geometry.getCoordinates()));

        BasicDBObject filterIntersectsCrs = (BasicDBObject) filterIntersectsGeometry.get("crs");
        Assert.assertNotNull(filterIntersectsCrs);

        BasicDBObject filterIntersectsCrsProperties =
                (BasicDBObject) filterIntersectsCrs.get("properties");
        Assert.assertNotNull(filterIntersectsCrsProperties);

        String filterIntersectsCrsPropertiesName =
                (String) filterIntersectsCrsProperties.get("name");
        Assert.assertNotNull(filterIntersectsCrsPropertiesName);
        Assert.assertEquals(
                "urn:x-mongodb:crs:strictwinding:EPSG:4326", filterIntersectsCrsPropertiesName);
    }

    @Test
    public void testBBOXOutsideOfWorld() {
        // Special case, verify we clip queries to the world bounds so mongodb doesnt have an error
        Envelope WORLD = new Envelope(-179.99, 179.99, -89.99, 89.99);
        BBOX bbox = ff.bbox("loc", 190d, 10d, 20d, 20d, "epsg:4326");

        // As is, the world does not contain the bbox
        Assert.assertFalse(WORLD.contains(bbox.getExpression2().evaluate(null, Envelope.class)));

        BasicDBObject obj = (BasicDBObject) bbox.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBObject filterGeometry = (BasicDBObject) obj.get("geometry");
        Assert.assertNotNull(filterGeometry);

        BasicDBObject filterIntersects = (BasicDBObject) filterGeometry.get("$geoIntersects");
        Assert.assertNotNull(filterIntersects);

        BasicDBObject filterIntersectsGeometry = (BasicDBObject) filterIntersects.get("$geometry");
        Assert.assertNotNull(filterIntersectsGeometry);

        Geometry geometry = geometryBuilder.toGeometry(filterIntersectsGeometry);

        // Verify the world now contains the clipped bbox
        Assert.assertTrue(WORLD.contains(geometry.getEnvelopeInternal()));
    }

    @Test
    public void testIntersects() {
        Intersects intersects = ff.intersects(ff.property("geom"), getGeometryParameter());
        BasicDBObject obj = (BasicDBObject) intersects.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBObject filterGeometry = (BasicDBObject) obj.get("geometry");
        Assert.assertNotNull(filterGeometry);

        BasicDBObject filterIntersects = (BasicDBObject) filterGeometry.get("$geoIntersects");
        Assert.assertNotNull(filterIntersects);

        BasicDBObject filterIntersectsGeometry = (BasicDBObject) filterIntersects.get("$geometry");
        Assert.assertNotNull(filterIntersectsGeometry);

        Geometry geometry = geometryBuilder.toGeometry(filterIntersectsGeometry);
        Assert.assertTrue(Orientation.isCCW(geometry.getCoordinates()));

        BasicDBObject filterIntersectsCrs = (BasicDBObject) filterIntersectsGeometry.get("crs");
        Assert.assertNotNull(filterIntersectsCrs);

        BasicDBObject filterIntersectsCrsProperties =
                (BasicDBObject) filterIntersectsCrs.get("properties");
        Assert.assertNotNull(filterIntersectsCrsProperties);

        String filterIntersectsCrsPropertiesName =
                (String) filterIntersectsCrsProperties.get("name");
        Assert.assertNotNull(filterIntersectsCrsPropertiesName);
        Assert.assertEquals(
                "urn:x-mongodb:crs:strictwinding:EPSG:4326", filterIntersectsCrsPropertiesName);
    }

    @Test
    public void testWithin() {

        Within within = ff.within(ff.property("geom"), getGeometryParameter());
        BasicDBObject obj = (BasicDBObject) within.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBObject filterGeometry = (BasicDBObject) obj.get("geometry");
        Assert.assertNotNull(filterGeometry);

        BasicDBObject filterWithin = (BasicDBObject) filterGeometry.get("$geoWithin");
        Assert.assertNotNull(filterWithin);

        BasicDBObject filterIntersectsGeometry = (BasicDBObject) filterWithin.get("$geometry");
        Assert.assertNotNull(filterIntersectsGeometry);

        Geometry geometry = geometryBuilder.toGeometry(filterIntersectsGeometry);
        Assert.assertTrue(Orientation.isCCW(geometry.getCoordinates()));

        BasicDBObject filterIntersectsCrs = (BasicDBObject) filterIntersectsGeometry.get("crs");
        Assert.assertNotNull(filterIntersectsCrs);

        BasicDBObject filterIntersectsCrsProperties =
                (BasicDBObject) filterIntersectsCrs.get("properties");
        Assert.assertNotNull(filterIntersectsCrsProperties);

        String filterIntersectsCrsPropertiesName =
                (String) filterIntersectsCrsProperties.get("name");
        Assert.assertNotNull(filterIntersectsCrsPropertiesName);
        Assert.assertEquals(
                "urn:x-mongodb:crs:strictwinding:EPSG:4326", filterIntersectsCrsPropertiesName);
    }

    @Test
    public void testDWithin() throws FactoryException, TransformException {
        Point point = GEOMETRY_FACTORY.createPoint(new Coordinate(10.0, 10.0));
        DWithin dwithin = ff.dwithin(ff.property("geom"), ff.literal(point), 1, "kilometers");
        BasicDBObject obj = (BasicDBObject) dwithin.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBObject filterDWithin = (BasicDBObject) obj.get("geometry");
        Assert.assertNotNull(filterDWithin);

        BasicDBObject near = (BasicDBObject) filterDWithin.get("$near");
        Assert.assertNotNull(near);

        Double maxDistance = (Double) near.get("$maxDistance");
        Assert.assertEquals(1000d, maxDistance, 00.1);

        BasicDBObject geometry = (BasicDBObject) near.get("$geometry");
        Assert.assertNotNull(geometry);

        BasicDBList coordinates = new BasicDBList();
        coordinates.add(10d);
        coordinates.add(10d);

        Assert.assertEquals("Point", geometry.get("type"));
        Assert.assertEquals(coordinates, geometry.get("coordinates"));
    }

    @Test
    public void testDWithinLinestring() {
        Coordinate[] coordinates = {
            new Coordinate(10.458984, 59.888937),
            new Coordinate(7.910156, 58.745407),
            new Coordinate(5.405273, 60.413852)
        };
        LineString line = GEOMETRY_FACTORY.createLineString(coordinates);

        Point[] pointsWithin5km = {
            GEOMETRY_FACTORY.createPoint(new Coordinate(10.2281, 59.729754)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(8.154602, 58.849279)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(5.899658, 60.159533))
        };

        Point[] pointsOutOf5km = {
            GEOMETRY_FACTORY.createPoint(new Coordinate(10.240803, 59.730014)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(8.265152, 58.801651)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(5.913563, 60.164999))
        };

        DWithin dwithin = ff.dwithin(ff.property("geom"), ff.literal(line), 5, "kilometers");
        BasicDBObject obj = (BasicDBObject) dwithin.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBObject filterDWithin = (BasicDBObject) obj.get("geometry");
        Assert.assertNotNull(filterDWithin);

        BasicDBObject near = (BasicDBObject) filterDWithin.get("$geoIntersects");
        Assert.assertNotNull(near);

        BasicDBObject geometry = (BasicDBObject) near.get("$geometry");
        Assert.assertNotNull(geometry);

        Assert.assertEquals("Polygon", geometry.get("type"));

        BasicDBList generatedCoordinates = (BasicDBList) geometry.get("coordinates");
        Polygon polygon = convertBasicDBListToPolygon((BasicDBList) generatedCoordinates.get(0));

        for (Point p : pointsWithin5km) {
            Assert.assertTrue(p.within(polygon));
        }
        for (Point p : pointsOutOf5km) {
            Assert.assertFalse(p.within(polygon));
        }
    }

    @Test
    public void testDWithinLinestring3857() {

        Coordinate[] coordinates = {
            new Coordinate(1164288.773095, 8375052.33805727),
            new Coordinate(880554.53801536, 8125561.8615686),
            new Coordinate(601712.23795863, 8492459.51157844)
        };
        LineString line = GEOMETRY_FACTORY.createLineString(coordinates);

        Point[] pointsWithin5km = {
            GEOMETRY_FACTORY.createPoint(new Coordinate(1116668.521323449, 8345509.311262323)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(861139.5282650845, 8164850.444119697)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(653340.7706351702, 8408627.216349771))
        };

        Point[] pointsOutOf5km = {
            GEOMETRY_FACTORY.createPoint(new Coordinate(1113305.3368676028, 8345776.956413542)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(863967.7112481785, 8166799.653481186)),
            GEOMETRY_FACTORY.createPoint(new Coordinate(652657.6029201719, 8407686.15925105))
        };

        DWithin dwithin = ff.dwithin(ff.property("geom"), ff.literal(line), 5, "kilometers");
        BasicDBObject obj = (BasicDBObject) dwithin.accept(filterToMongo3857, null);
        Assert.assertNotNull(obj);

        BasicDBObject filterDWithin = (BasicDBObject) obj.get("geometry");
        Assert.assertNotNull(filterDWithin);

        BasicDBObject near = (BasicDBObject) filterDWithin.get("$geoIntersects");
        Assert.assertNotNull(near);

        BasicDBObject geometry = (BasicDBObject) near.get("$geometry");
        Assert.assertNotNull(geometry);

        Assert.assertEquals("Polygon", geometry.get("type"));

        BasicDBList generatedCoordinates = (BasicDBList) geometry.get("coordinates");
        Polygon polygon = convertBasicDBListToPolygon((BasicDBList) generatedCoordinates.get(0));

        for (Point p : pointsWithin5km) {
            Assert.assertTrue(p.within(polygon));
        }
        for (Point p : pointsOutOf5km) {
            Assert.assertFalse(p.within(polygon));
        }
    }

    @Test
    public void testLike() throws Exception {
        PropertyIsLike like = ff.like(ff.property("stringProperty"), "on%", "%", "_", "\\");
        BasicDBObject obj = (BasicDBObject) like.accept(filterToMongo, null);

        Assert.assertNotNull(obj);
    }

    @Test
    public void testLikeUnsupported() throws Exception {
        PropertyIsLike likeLiteral = ff.like(ff.literal("once upon a time"), "on%", "%", "_", "\\");
        PropertyIsLike likeFunction =
                ff.like(
                        ff.function(
                                "Concatenate", ff.property("stringProperty"), ff.literal("test")),
                        "on%",
                        "%",
                        "_",
                        "\\");

        try {
            likeLiteral.accept(filterToMongo, null);
            Assert.fail("Expected UnsupportedOperationException not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnsupportedOperationException);
        }

        try {
            likeFunction.accept(filterToMongo, null);
            Assert.fail("Expected UnsupportedOperationException not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testDateGreaterComparison() {
        PropertyIsGreaterThan gt =
                ff.greater(ff.property("dateProperty"), ff.literal(DATE_LITERAL));
        BasicDBObject obj = (BasicDBObject) gt.accept(filterToMongo, null);

        Assert.assertNotNull(obj);
        BasicDBObject filter = (BasicDBObject) obj.get("properties.dateProperty");
        Assert.assertNotNull(filter);
        Assert.assertEquals(MongoTestSetup.parseDate(DATE_LITERAL), filter.get("$gt"));
    }

    @Test
    public void testDateLessComparison() {
        PropertyIsLessThan lt = ff.less(ff.property("dateProperty"), ff.literal(DATE_LITERAL));
        BasicDBObject obj = (BasicDBObject) lt.accept(filterToMongo, null);

        Assert.assertNotNull(obj);
        BasicDBObject filter = (BasicDBObject) obj.get("properties.dateProperty");
        Assert.assertNotNull(filter);
        Assert.assertEquals(MongoTestSetup.parseDate(DATE_LITERAL), filter.get("$lt"));
    }

    @Test
    public void testDateBetweenComparison() {
        final String LOWER_BOUND = DATE_LITERAL;
        final String UPPER_BOUND = "2015-07-31T00:00:00.000+01:00";

        PropertyIsBetween lt =
                ff.between(
                        ff.property("dateProperty"),
                        ff.literal(LOWER_BOUND),
                        ff.literal(UPPER_BOUND));
        BasicDBObject obj = (BasicDBObject) lt.accept(filterToMongo, null);

        Assert.assertNotNull(obj);

        BasicDBObject filter = (BasicDBObject) obj.get("properties.dateProperty");
        Assert.assertNotNull(filter);
        Assert.assertEquals(MongoTestSetup.parseDate(LOWER_BOUND), filter.get("$gte"));
        Assert.assertEquals(MongoTestSetup.parseDate(UPPER_BOUND), filter.get("$lte"));
    }

    @Test
    public void testAndComparison() {
        PropertyIsGreaterThan greaterThan = ff.greater(ff.property("property"), ff.literal(0));
        PropertyIsLessThan lessThan = ff.less(ff.property("property"), ff.literal(10));
        And and = ff.and(greaterThan, lessThan);
        BasicDBObject obj = (BasicDBObject) and.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBList andFilter = (BasicDBList) obj.get("$and");
        Assert.assertNotNull(andFilter);
        Assert.assertEquals(andFilter.size(), 2);
    }

    @Test
    public void testOrComparison() {
        PropertyIsGreaterThan greaterThan = ff.greater(ff.property("property"), ff.literal(0));
        PropertyIsLessThan lessThan = ff.less(ff.property("property"), ff.literal(10));
        Or or = ff.or(greaterThan, lessThan);
        BasicDBObject obj = (BasicDBObject) or.accept(filterToMongo, null);
        Assert.assertNotNull(obj);

        BasicDBList orFilter = (BasicDBList) obj.get("$or");
        Assert.assertNotNull(orFilter);
        Assert.assertEquals(orFilter.size(), 2);
    }

    @Test
    public void testEqualToInteger() throws Exception {
        PropertyIsEqualTo equalTo = ff.equals(ff.property("foo"), ff.literal(10));
        BasicDBObject obj = (BasicDBObject) equalTo.accept(filterToMongo, null);
        Assert.assertNotNull(obj);
        BasicDBObject operator = (BasicDBObject) obj.get("properties.foo");
        Assert.assertEquals(1, obj.keySet().size());
        Assert.assertEquals(10, operator.get("$eq"));
    }

    @Test
    public void testEqualToLong() throws Exception {
        PropertyIsEqualTo equalTo = ff.equals(ff.property("foo"), ff.literal(10L));
        BasicDBObject obj = (BasicDBObject) equalTo.accept(filterToMongo, null);
        Assert.assertNotNull(obj);
        BasicDBObject operator = (BasicDBObject) obj.get("properties.foo");
        Assert.assertEquals(1, obj.keySet().size());
        Assert.assertEquals(10L, operator.get("$eq"));
    }

    @Test
    public void testEqualToBigInteger() throws Exception {
        PropertyIsEqualTo equalTo =
                ff.equals(ff.property("foo"), ff.literal(BigInteger.valueOf(10L)));
        BasicDBObject obj = (BasicDBObject) equalTo.accept(filterToMongo, null);
        Assert.assertNotNull(obj);
        BasicDBObject operator = (BasicDBObject) obj.get("properties.foo");
        Assert.assertEquals(1, obj.keySet().size());
        Assert.assertEquals("10", operator.get("$eq"));
    }

    @Test
    public void testLiteralsHandling() throws Exception {
        // test NULL properties, supported primitives types should be preserved
        testLiteralEncoding(null, null, null);
        testLiteralEncoding(null, Object.class, null);
        testLiteralEncoding(false, null, false);
        testLiteralEncoding(10D, null, 10D);
        testLiteralEncoding(10, null, 10);
        testLiteralEncoding(10L, null, 10L);
        testLiteralEncoding("10", null, "10");
        // test primitives types conversions
        testLiteralEncoding("10", Double.class, 10D);
        testLiteralEncoding("10", Integer.class, 10);
        testLiteralEncoding("10", Long.class, 10L);
        testLiteralEncoding("10", String.class, "10");
        testLiteralEncoding(10f, Double.class, 10d);
        testLiteralEncoding("true", Boolean.class, true);
        testLiteralEncoding(10, String.class, "10");
        testLiteralEncoding(BigInteger.valueOf(10), Long.class, 10L);
        // test not supported and invalid types conversions
        testLiteralEncoding("10", Boolean.class, "10");
        testLiteralEncoding(BigInteger.valueOf(10), BigInteger.class, "10");
    }

    /** Helper method that test literal conversions. */
    private <T, U> void testLiteralEncoding(T literalValue, Class<?> typeHint, U expectedValue) {
        // construct the literal and visit it
        Literal literal = ff.literal(literalValue);
        Object value = literal.accept(filterToMongo, typeHint);
        // check the result againts the expected result
        if (expectedValue == null) {
            assertThat(value, nullValue());
        } else {
            assertThat(value, notNullValue());
            assertThat(value, is(expectedValue));
        }
    }

    @Test
    public void testIntersectsWithJsonSelect() {
        Intersects intersects =
                ff.intersects(
                        ff.function("jsonSelect", ff.literal("geom")), getGeometryParameter());
        BasicDBObject mongoQuery = (BasicDBObject) intersects.accept(filterToMongo, null);
        testIntersectMongoQuery(mongoQuery);
    }

    @Test
    public void testIntersectsWithJsonSelectAll() {

        Intersects intersects =
                ff.intersects(
                        ff.function("jsonSelectAll", ff.literal("geom")), getGeometryParameter());
        BasicDBObject mongoQuery = (BasicDBObject) intersects.accept(filterToMongo, null);
        testIntersectMongoQuery(mongoQuery);
    }

    @Test
    public void testNot() {
        Not not = ff.not(ff.isNull(ff.property("foo")));
        BasicDBObject obj = (BasicDBObject) not.accept(filterToMongo, null);
        Assert.assertNotNull(obj);
        Assert.assertEquals(1, obj.keySet().size());
        BasicDBObject operator = (BasicDBObject) obj.get("properties.foo");

        Assert.assertNull(operator.get("$eq"));
    }

    private Literal getGeometryParameter() {
        Coordinate[] coordinates = {
            new Coordinate(10.0, 10.0),
            new Coordinate(20.0, 10.0),
            new Coordinate(20.0, 20.0),
            new Coordinate(10.0, 20.0),
            new Coordinate(10.0, 10.0),
        };
        return ff.literal(new GeometryFactory().createPolygon(coordinates));
    }

    private void testIntersectMongoQuery(BasicDBObject mongoQuery) {
        Assert.assertNotNull(mongoQuery);

        BasicDBObject filterGeometry = (BasicDBObject) mongoQuery.get("geom");
        Assert.assertNotNull(filterGeometry);

        BasicDBObject filterIntersects = (BasicDBObject) filterGeometry.get("$geoIntersects");
        Assert.assertNotNull(filterIntersects);

        BasicDBObject filterIntersectsGeometry = (BasicDBObject) filterIntersects.get("$geometry");
        Assert.assertNotNull(filterIntersectsGeometry);

        Geometry geometry = geometryBuilder.toGeometry(filterIntersectsGeometry);
        Assert.assertTrue(Orientation.isCCW(geometry.getCoordinates()));

        BasicDBObject filterIntersectsCrs = (BasicDBObject) filterIntersectsGeometry.get("crs");
        Assert.assertNotNull(filterIntersectsCrs);

        BasicDBObject filterIntersectsCrsProperties =
                (BasicDBObject) filterIntersectsCrs.get("properties");
        Assert.assertNotNull(filterIntersectsCrsProperties);

        String filterIntersectsCrsPropertiesName =
                (String) filterIntersectsCrsProperties.get("name");
        Assert.assertNotNull(filterIntersectsCrsPropertiesName);
        Assert.assertEquals(
                "urn:x-mongodb:crs:strictwinding:EPSG:4326", filterIntersectsCrsPropertiesName);
    }

    public static Polygon convertBasicDBListToPolygon(BasicDBList basicDBList) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // Convert BasicDBList to an array of Coordinates
        Coordinate[] coordinates = new Coordinate[basicDBList.size()];
        for (int i = 0; i < basicDBList.size(); i++) {
            BasicDBList point = (BasicDBList) basicDBList.get(i);
            coordinates[i] = new Coordinate((double) point.get(0), (double) point.get(1));
        }

        // Create a LinearRing from the coordinates (needed to create a Polygon)
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);

        // Create the Polygon (no holes, hence null for the second argument)
        return geometryFactory.createPolygon(linearRing, null);
    }
}
