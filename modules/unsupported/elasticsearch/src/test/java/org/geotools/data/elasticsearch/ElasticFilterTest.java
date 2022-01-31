/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

public class ElasticFilterTest {

    private FilterToElastic builder;

    private FilterFactory2 ff;

    private GeometryFactory gf;

    private SimpleFeatureType featureType;

    private Map<String, String> parameters;

    private Query query;

    private DateFormat dateFormat;

    @Before
    public void setUp() {
        ff = CommonFactoryFinder.getFilterFactory2();

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("test");
        typeBuilder.add("stringAttr", String.class);
        typeBuilder.add("integerAttr", Integer.class);
        typeBuilder.add("longAttr", Long.class);
        typeBuilder.add("booleanAttr", Boolean.class);
        typeBuilder.add("doubleAttr", Double.class);
        typeBuilder.add("floatAttr", Float.class);
        typeBuilder.add("dateAttr", Date.class);

        AttributeTypeBuilder geoPointAttBuilder = new AttributeTypeBuilder();
        geoPointAttBuilder.setName("geo_point");
        geoPointAttBuilder.setBinding(Point.class);
        AttributeDescriptor geoPointAtt =
                geoPointAttBuilder.buildDescriptor("geo_point", geoPointAttBuilder.buildType());
        geoPointAtt
                .getUserData()
                .put(
                        ElasticConstants.GEOMETRY_TYPE,
                        ElasticAttribute.ElasticGeometryType.GEO_POINT);
        typeBuilder.add(geoPointAtt);

        AttributeTypeBuilder geoShapeAttBuilder = new AttributeTypeBuilder();
        geoShapeAttBuilder.setName("geom");
        geoShapeAttBuilder.setBinding(Geometry.class);
        AttributeDescriptor geoShapeAtt =
                geoShapeAttBuilder.buildDescriptor("geom", geoShapeAttBuilder.buildType());
        geoShapeAtt
                .getUserData()
                .put(
                        ElasticConstants.GEOMETRY_TYPE,
                        ElasticAttribute.ElasticGeometryType.GEO_SHAPE);
        typeBuilder.add(geoShapeAtt);

        AttributeTypeBuilder analyzedAttBuilder = new AttributeTypeBuilder();
        analyzedAttBuilder.setName("analyzed");
        analyzedAttBuilder.setBinding(String.class);
        AttributeDescriptor analyzedAtt =
                analyzedAttBuilder.buildDescriptor("analyzed", analyzedAttBuilder.buildType());
        analyzedAtt.getUserData().put(ElasticConstants.ANALYZED, true);
        typeBuilder.add(analyzedAtt);

        AttributeTypeBuilder nestedAttBuilder = new AttributeTypeBuilder();
        nestedAttBuilder.setName("nested.hej");
        nestedAttBuilder.setBinding(String.class);
        AttributeDescriptor netsedAtt =
                nestedAttBuilder.buildDescriptor("nested.hej", nestedAttBuilder.buildType());
        netsedAtt.getUserData().put(ElasticConstants.NESTED, true);
        netsedAtt.getUserData().put(ElasticConstants.ANALYZED, true);
        typeBuilder.add(netsedAtt);

        AttributeTypeBuilder nestedDateAttBuilder = new AttributeTypeBuilder();
        nestedDateAttBuilder.setName("nested.datehej");
        nestedDateAttBuilder.setBinding(Date.class);
        AttributeDescriptor netsedDateAtt =
                nestedDateAttBuilder.buildDescriptor(
                        "nested.datehej", nestedDateAttBuilder.buildType());
        netsedDateAtt.getUserData().put(ElasticConstants.NESTED, true);
        typeBuilder.add(netsedDateAtt);

        featureType = typeBuilder.buildFeatureType();
        setFilterBuilder();

        parameters = new HashMap<>();
        final Hints hints = new Hints();
        hints.put(Hints.VIRTUAL_TABLE_PARAMETERS, parameters);
        query = new Query();
        query.setHints(hints);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        gf = new GeometryFactory();
    }

    private void setFilterBuilder() {
        builder = new FilterToElastic();
        builder.setFeatureType(featureType);
    }

    private void addDateWithFormatToFeatureType(String format) {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(featureType);

        AttributeTypeBuilder dateAttBuilder = new AttributeTypeBuilder();
        dateAttBuilder.setName("dateAttrWithFormat");
        dateAttBuilder.setBinding(Date.class);
        AttributeDescriptor dateAtt =
                dateAttBuilder.buildDescriptor("dateAttrWithFormat", dateAttBuilder.buildType());
        List<String> validFormats = new ArrayList<>();
        validFormats.add(format);
        dateAtt.getUserData().put(ElasticConstants.DATE_FORMAT, validFormats);
        typeBuilder.add(dateAtt);

        featureType = typeBuilder.buildFeatureType();
        setFilterBuilder();
    }

    @Test
    public void testEncodeQuery() {
        Query query = new Query();
        query.setFilter(Filter.INCLUDE);
        builder.encode(query);
        assertEquals(ElasticConstants.MATCH_ALL, builder.getQueryBuilder());
        assertEquals(ElasticConstants.MATCH_ALL, builder.getNativeQueryBuilder());
        assertNull(builder.getAggregations());
        assertTrue(builder.getFullySupported());
    }

    @Test
    public void testId() {
        final Id filter = ff.id(ff.featureId("id"));
        Map<String, Object> expected =
                ImmutableMap.of("ids", ImmutableMap.of("values", ImmutableList.of("id")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testAnd() {
        And filter = ff.and(ff.id(ff.featureId("id1")), ff.id(ff.featureId("id2")));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ImmutableList.of(
                                        ImmutableMap.of(
                                                "ids",
                                                ImmutableMap.of("values", ImmutableList.of("id1"))),
                                        ImmutableMap.of(
                                                "ids",
                                                ImmutableMap.of(
                                                        "values", ImmutableList.of("id2"))))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testOr() {
        final Or filter = ff.or(ff.id(ff.featureId("id1")), ff.id(ff.featureId("id2")));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "should",
                                ImmutableList.of(
                                        ImmutableMap.of(
                                                "ids",
                                                ImmutableMap.of("values", ImmutableList.of("id1"))),
                                        ImmutableMap.of(
                                                "ids",
                                                ImmutableMap.of(
                                                        "values", ImmutableList.of("id2"))))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testNot() {
        Not filter = ff.not(ff.id(ff.featureId("id")));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must_not",
                                ImmutableMap.of(
                                        "ids", ImmutableMap.of("values", ImmutableList.of("id")))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsNull() {
        PropertyIsNull filter = ff.isNull(ff.property("prop"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must_not",
                                ImmutableMap.of("exists", ImmutableMap.of("field", "prop"))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsNotNull() {
        Not filter = ff.not(ff.isNull(ff.property("prop")));
        Map<String, Object> expected = ImmutableMap.of("exists", ImmutableMap.of("field", "prop"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsEqualToString() {
        PropertyIsEqualTo filter = ff.equals(ff.property("stringAttr"), ff.literal("value"));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("stringAttr", "value"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testNestedPropertyIsEqualToString() {
        PropertyIsEqualTo filter = ff.equals(ff.property("nested.hej"), ff.literal("value"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "nested",
                        ImmutableMap.of(
                                "path",
                                "nested",
                                "query",
                                ImmutableMap.of("term", ImmutableMap.of("nested.hej", "value"))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testNestedStringIsEqualToProperty() {
        PropertyIsEqualTo filter = ff.equals(ff.literal("value"), ff.property("nested.hej"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "nested",
                        ImmutableMap.of(
                                "path",
                                "nested",
                                "query",
                                ImmutableMap.of("term", ImmutableMap.of("nested.hej", "value"))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsNotEqualToString() {
        PropertyIsNotEqualTo filter = ff.notEqual(ff.property("stringAttr"), ff.literal("value"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must_not",
                                ImmutableMap.of("term", ImmutableMap.of("stringAttr", "value"))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsEqualToDouble() {
        PropertyIsEqualTo filter = ff.equals(ff.property("doubleAttr"), ff.literal("4.5"));
        Map<String, Object> expected = ImmutableMap.of("term", ImmutableMap.of("doubleAttr", 4.5));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testDoubleIsEqualtoProperty() {
        PropertyIsEqualTo filter = ff.equals(ff.literal("4.5"), ff.property("doubleAttr"));
        Map<String, Object> expected = ImmutableMap.of("term", ImmutableMap.of("doubleAttr", 4.5));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsNotEqualToDouble() {
        PropertyIsNotEqualTo filter = ff.notEqual(ff.property("doubleAttr"), ff.literal("4.5"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must_not",
                                ImmutableMap.of("term", ImmutableMap.of("doubleAttr", 4.5))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsEqualToFloat() {
        PropertyIsEqualTo filter = ff.equals(ff.property("floatAttr"), ff.literal("4.5"));
        Map<String, Object> expected = ImmutableMap.of("term", ImmutableMap.of("floatAttr", 4.5f));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsEqualToInteger() {
        PropertyIsEqualTo filter = ff.equals(ff.property("integerAttr"), ff.literal("4"));
        Map<String, Object> expected = ImmutableMap.of("term", ImmutableMap.of("integerAttr", 4));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsEqualToBoolean() {
        PropertyIsEqualTo filter = ff.equals(ff.property("booleanAttr"), ff.literal("true"));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("booleanAttr", true));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsGreaterThan() {
        PropertyIsGreaterThan filter = ff.greater(ff.property("doubleAttr"), ff.literal("4.5"));
        Map<String, Object> expected =
                ImmutableMap.of("range", ImmutableMap.of("doubleAttr", ImmutableMap.of("gt", 4.5)));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsLessThan() {
        PropertyIsLessThan filter = ff.less(ff.property("doubleAttr"), ff.literal("4.5"));
        Map<String, Object> expected =
                ImmutableMap.of("range", ImmutableMap.of("doubleAttr", ImmutableMap.of("lt", 4.5)));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsGreaterThanOrEqualTo() {
        PropertyIsGreaterThanOrEqualTo filter =
                ff.greaterOrEqual(ff.property("doubleAttr"), ff.literal("4.5"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range", ImmutableMap.of("doubleAttr", ImmutableMap.of("gte", 4.5)));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsLessThanOrEqualTo() {
        PropertyIsLessThanOrEqualTo filter =
                ff.lessOrEqual(ff.property("doubleAttr"), ff.literal("4.5"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range", ImmutableMap.of("doubleAttr", ImmutableMap.of("lte", 4.5)));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testPropertyIsBetween() {
        PropertyIsBetween filter =
                ff.between(ff.property("doubleAttr"), ff.literal("4.5"), ff.literal("5.5"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of("doubleAttr", ImmutableMap.of("gte", 4.5, "lte", 5.5)));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testUnknownPropertyIsBetween() {
        PropertyIsBetween filter =
                ff.between(ff.property("unknownStr"), ff.literal("a"), ff.literal("c"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of("unknownStr", ImmutableMap.of("gte", "a", "lte", "c")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testIncludeFilter() {
        IncludeFilter filter = Filter.INCLUDE;

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(ElasticConstants.MATCH_ALL, builder.getQueryBuilder());
    }

    @Test
    public void testExcludeFilter() {
        ExcludeFilter filter = Filter.EXCLUDE;
        Map<String, Object> expected =
                ImmutableMap.of("bool", ImmutableMap.of("must_not", ElasticConstants.MATCH_ALL));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testNullFilter() {
        assertNull(builder.visitNullFilter(null));
    }

    @Test
    public void testNilFilter() {
        builder.field = "field";
        builder.visit((NilExpression) NilExpression.NIL, null);
        assertNull(builder.field);
    }

    @Test
    public void testNullBinarySpatialOperatorFilter() {
        boolean success = false;
        try {
            builder.visit((BBOX) null, null);
        } catch (NullPointerException e) {
            success = true;
        }
        assertTrue(success);
    }

    @Test
    public void testPropertyIsLike() {
        PropertyIsLike filter = ff.like(ff.property("analyzed"), "hello");
        Map<String, Object> expected =
                ImmutableMap.of(
                        "query_string",
                        ImmutableMap.of("query", "hello", "default_field", "analyzed"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testCaseSensitivePropertyIsLike() {
        PropertyIsLike filter = ff.like(ff.property("analyzed"), "hello", "\\", "*", ".", true);
        Map<String, Object> expected =
                ImmutableMap.of(
                        "query_string",
                        ImmutableMap.of("query", "hello", "default_field", "analyzed"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testNestedPropertyIsLike() {
        PropertyIsLike filter = ff.like(ff.property("nested.hej"), "hello");
        Map<String, Object> expectedFilter =
                ImmutableMap.of(
                        "query_string",
                        ImmutableMap.of("query", "hello", "default_field", "nested.hej"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "nested", ImmutableMap.of("path", "nested", "query", expectedFilter));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testConvertToRegex() {
        assertEquals("BroadWay.*", FilterToElastic.convertToRegex('!', '*', '.', "BroadWay*"));
        assertEquals("broad#ay", FilterToElastic.convertToRegex('!', '*', '.', "broad#ay"));
        assertEquals("broadway", FilterToElastic.convertToRegex('!', '*', '.', "broadway"));

        assertEquals("broad.ay", FilterToElastic.convertToRegex('!', '*', '.', "broad.ay"));
        assertEquals("broad\\.ay", FilterToElastic.convertToRegex('!', '*', '.', "broad!.ay"));

        assertEquals("broa'dway", FilterToElastic.convertToRegex('!', '*', '.', "broa'dway"));
        assertEquals("broa''dway", FilterToElastic.convertToRegex('!', '*', '.', "broa''dway"));

        assertEquals("broadway.", FilterToElastic.convertToRegex('!', '*', '.', "broadway."));
        assertEquals("broadway", FilterToElastic.convertToRegex('!', '*', '.', "broadway!"));
        assertEquals("broadway\\!", FilterToElastic.convertToRegex('!', '*', '.', "broadway!!"));
        assertEquals(
                "broadway\\\\", FilterToElastic.convertToRegex('\\', '*', '.', "broadway\\\\"));
        assertEquals("broadway\\", FilterToElastic.convertToRegex('!', '*', '.', "broadway\\"));
    }

    @Test
    public void testConvertToQueryString() {
        assertEquals("BroadWay*", FilterToElastic.convertToQueryString('!', '*', '.', "BroadWay*"));
        assertEquals("broad#ay", FilterToElastic.convertToQueryString('!', '*', '.', "broad#ay"));
        assertEquals("broadway", FilterToElastic.convertToQueryString('!', '*', '.', "broadway"));

        assertEquals("broad?ay", FilterToElastic.convertToQueryString('!', '*', '.', "broad.ay"));
        assertEquals(
                "broad\\.ay", FilterToElastic.convertToQueryString('!', '*', '.', "broad!.ay"));

        assertEquals("broa'dway", FilterToElastic.convertToQueryString('!', '*', '.', "broa'dway"));
        assertEquals(
                "broa''dway", FilterToElastic.convertToQueryString('!', '*', '.', "broa''dway"));

        assertEquals("broadway?", FilterToElastic.convertToQueryString('!', '*', '.', "broadway."));
        assertEquals("broadway", FilterToElastic.convertToQueryString('!', '*', '.', "broadway!"));
        assertEquals(
                "broadway\\!", FilterToElastic.convertToQueryString('!', '*', '.', "broadway!!"));
        assertEquals(
                "broadway\\\\",
                FilterToElastic.convertToQueryString('\\', '*', '.', "broadway\\\\"));
        assertEquals(
                "broadway\\", FilterToElastic.convertToQueryString('!', '*', '.', "broadway\\"));
    }

    @Test
    public void testGeoShapeBboxFilter() {
        BBOX filter = ff.bbox("geom", 0, 0, 1.1, 1.1, "EPSG:4326");
        List<List<Number>> coords = new ArrayList<>();
        coords.add(ImmutableList.of(0, 0));
        coords.add(ImmutableList.of(0, 1.1));
        coords.add(ImmutableList.of(1.1, 1.1));
        coords.add(ImmutableList.of(1.1, 0));
        coords.add(ImmutableList.of(0, 0));
        // vertices in reverse order
        final List<List<Number>> reverseCoords =
                ImmutableList.of(
                        coords.get(0), coords.get(3), coords.get(2), coords.get(1), coords.get(4));
        ImmutableMap<String, Serializable> geo =
                ImmutableMap.of("coordinates", ImmutableList.of(coords), "type", "Polygon");
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ElasticConstants.MATCH_ALL,
                                "filter",
                                ImmutableMap.of(
                                        "geo_shape",
                                        ImmutableMap.of(
                                                "geom",
                                                ImmutableMap.of(
                                                        "shape", geo, "relation", "INTERSECTS")))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertThat(
                builder.getQueryBuilder().toString(),
                isOneOf(
                        expected.toString(),
                        expected.toString().replace(coords.toString(), reverseCoords.toString())));
    }

    @Test
    public void testGeoShapeIntersectsFilter() throws CQLException {
        Intersects filter =
                (Intersects) ECQL.toFilter("INTERSECTS(\"geom\", LINESTRING(0 0,1.1 1.1))");
        List<List<Number>> coords = new ArrayList<>();
        coords.add(ImmutableList.of(0, 0));
        coords.add(ImmutableList.of(1.1, 1.1));
        ImmutableMap<String, Object> shape =
                ImmutableMap.of("coordinates", coords, "type", "LineString");
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ElasticConstants.MATCH_ALL,
                                "filter",
                                ImmutableMap.of(
                                        "geo_shape",
                                        ImmutableMap.of(
                                                "geom",
                                                ImmutableMap.of(
                                                        "shape",
                                                        shape,
                                                        "relation",
                                                        "INTERSECTS")))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        // TODO: Why doesn't equality check on objects work here
        assertEquals(expected.toString(), builder.getQueryBuilder().toString());
    }

    @Test
    public void testEmptyGeoShape() {
        LineString ls = gf.createLineString(new Coordinate[0]);
        Intersects filter = ff.intersects(ff.property("geom"), ff.literal(ls));

        Map<String, Object> expected =
                ImmutableMap.of("bool", ImmutableMap.of("must_not", ElasticConstants.MATCH_ALL));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testEmptyDisjointGeoShape() {
        LineString ls = gf.createLineString(new Coordinate[0]);
        Disjoint filter = ff.disjoint(ff.property("geom"), ff.literal(ls));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(ElasticConstants.MATCH_ALL, builder.getQueryBuilder());
    }

    @Test
    public void testGeoShapeIntersectsFilterReversed() throws CQLException {
        Intersects filter =
                (Intersects) ECQL.toFilter("INTERSECTS(LINESTRING(0 0,1.1 1.1), \"geom\")");
        List<List<Number>> coords = new ArrayList<>();
        coords.add(ImmutableList.of(0, 0));
        coords.add(ImmutableList.of(1.1, 1.1));
        ImmutableMap<String, Object> shape =
                ImmutableMap.of("coordinates", coords, "type", "LineString");
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ElasticConstants.MATCH_ALL,
                                "filter",
                                ImmutableMap.of(
                                        "geo_shape",
                                        ImmutableMap.of(
                                                "geom",
                                                ImmutableMap.of(
                                                        "shape",
                                                        shape,
                                                        "relation",
                                                        "INTERSECTS")))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected.toString(), builder.getQueryBuilder().toString());
    }

    @Test
    public void testAndWithBbox() {
        And filter =
                ff.and(ff.id(ff.featureId("id1")), ff.bbox("geom", 0, 0, 1.1, 1.1, "EPSG:4326"));
        List<List<Number>> coords = new ArrayList<>();
        coords.add(ImmutableList.of(0, 0));
        coords.add(ImmutableList.of(0, 1.1));
        coords.add(ImmutableList.of(1.1, 1.1));
        coords.add(ImmutableList.of(1.1, 0));
        coords.add(ImmutableList.of(0, 0));
        // vertices in reverse order
        List<List<Number>> reverseCoords =
                ImmutableList.of(
                        coords.get(0), coords.get(3), coords.get(2), coords.get(1), coords.get(4));
        Map<String, Object> geoShape =
                ImmutableMap.of(
                        "geo_shape",
                        ImmutableMap.of(
                                "geom",
                                ImmutableMap.of(
                                        "shape",
                                        ImmutableMap.of(
                                                "coordinates",
                                                ImmutableList.of(coords),
                                                "type",
                                                "Polygon"),
                                        "relation",
                                        "INTERSECTS")));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ImmutableList.of(
                                        ImmutableMap.of(
                                                "ids",
                                                ImmutableMap.of("values", ImmutableList.of("id1"))),
                                        ImmutableMap.of(
                                                "bool",
                                                ImmutableMap.of(
                                                        "must",
                                                        ElasticConstants.MATCH_ALL,
                                                        "filter",
                                                        geoShape)))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertThat(
                builder.getQueryBuilder().toString(),
                isOneOf(
                        expected.toString(),
                        expected.toString().replace(coords.toString(), reverseCoords.toString())));
    }

    @Test
    public void testGeoPointBboxFilter() {
        BBOX filter = ff.bbox("geo_point", 0., 0., 1., 1., "EPSG:4326");
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ElasticConstants.MATCH_ALL,
                                "filter",
                                ImmutableMap.of(
                                        "geo_bounding_box",
                                        ImmutableMap.of(
                                                "geo_point",
                                                ImmutableMap.of(
                                                        "top_left",
                                                        ImmutableList.of(0., 1.),
                                                        "bottom_right",
                                                        ImmutableList.of(1., 0.))))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testGeoPolygonFilter() throws CQLException {
        Intersects filter =
                (Intersects)
                        ECQL.toFilter(
                                "INTERSECTS(\"geo_point\", POLYGON((0 0, 0 1.1, 1.1 1.1, 1.1 0, 0 0)))");
        List<List<Double>> points =
                ImmutableList.of(
                        ImmutableList.of(0., 0.),
                        ImmutableList.of(0., 1.1),
                        ImmutableList.of(1.1, 1.1),
                        ImmutableList.of(1.1, 0.),
                        ImmutableList.of(0., 0.));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ElasticConstants.MATCH_ALL,
                                "filter",
                                ImmutableMap.of(
                                        "geo_polygon",
                                        ImmutableMap.of(
                                                "geo_point", ImmutableMap.of("points", points)))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testDWithinFilter() throws CQLException {
        DWithin filter =
                (DWithin) ECQL.toFilter("DWITHIN(\"geo_point\", POINT(0 1.1), 1.0, meters)");
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ElasticConstants.MATCH_ALL,
                                "filter",
                                ImmutableMap.of(
                                        "geo_distance",
                                        ImmutableMap.of(
                                                "distance",
                                                "1.0m",
                                                "geo_point",
                                                ImmutableList.of(0., 1.1)))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testDWithinPolygonFilter() throws CQLException {
        DWithin filter =
                (DWithin)
                        ECQL.toFilter(
                                "DWITHIN(\"geo_point\", POLYGON((0 0, 0 1, 1 1, 1 0, 0 0)), 1.0, meters)");
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ElasticConstants.MATCH_ALL,
                                "filter",
                                ImmutableMap.of(
                                        "geo_distance",
                                        ImmutableMap.of(
                                                "distance",
                                                "1.0m",
                                                "geo_point",
                                                ImmutableList.of(0.5, 0.5)))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testDBeyondFilter() throws CQLException {
        Beyond filter = (Beyond) ECQL.toFilter("BEYOND(\"geo_point\", POINT(0 1.1), 1.0, meters)");
        Map<String, Object> filterBit =
                ImmutableMap.of(
                        "geo_distance",
                        ImmutableMap.of(
                                "distance", "1.0m", "geo_point", ImmutableList.of(0., 1.1)));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must_not",
                                ImmutableMap.of(
                                        "bool",
                                        ImmutableMap.of(
                                                "must",
                                                ElasticConstants.MATCH_ALL,
                                                "filter",
                                                filterBit))));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testLinearRingVisit() {
        final Geometry ring =
                gf.createLinearRing(
                        new Coordinate[] {
                            new Coordinate(0, 0),
                            new Coordinate(1, 1),
                            new Coordinate(1, 0),
                            new Coordinate(0, 0)
                        });
        builder.visit(ff.literal(ring), null);

        assertEquals("LineString", builder.currentGeometry.getGeometryType());
    }

    @Test
    public void testCompoundFilter() throws CQLException {
        Filter filter =
                ECQL.toFilter(
                        "time > \"1970-01-01\" and INTERSECTS(\"geom\", LINESTRING(0 0,1.1 1.1))");
        List<List<Number>> coords = new ArrayList<>();
        coords.add(ImmutableList.of(0, 0));
        coords.add(ImmutableList.of(1.1, 1.1));
        Map<String, Object> geoShape =
                ImmutableMap.of(
                        "geo_shape",
                        ImmutableMap.of(
                                "geom",
                                ImmutableMap.of(
                                        "shape",
                                        ImmutableMap.of(
                                                "coordinates", coords, "type", "LineString"),
                                        "relation",
                                        "INTERSECTS")));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "bool",
                        ImmutableMap.of(
                                "must",
                                ImmutableList.of(
                                        ImmutableMap.of(
                                                "range",
                                                ImmutableMap.of(
                                                        "time",
                                                        ImmutableMap.of("gt", "1970-01-01"))),
                                        ImmutableMap.of(
                                                "bool",
                                                ImmutableMap.of(
                                                        "must",
                                                        ElasticConstants.MATCH_ALL,
                                                        "filter",
                                                        geoShape)))));

        builder.encode(filter);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected.toString(), builder.getQueryBuilder().toString());
    }

    @Test
    public void testCql() throws CQLException {
        Filter filter = ECQL.toFilter("\"object.field\"='value'");
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("object.field", "value"));

        builder.encode(filter);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testViewParamWithNullHints() {
        query.setHints(null);

        builder.addViewParams(query);
        assertEquals(ElasticConstants.MATCH_ALL, builder.getQueryBuilder());
        assertEquals(ElasticConstants.MATCH_ALL, builder.nativeQueryBuilder);
    }

    @Test
    public void testQueryViewParam() throws JsonProcessingException {
        Map<String, Object> idsQuery =
                ImmutableMap.of("ids", ImmutableMap.of("value", ImmutableList.of("type1")));
        parameters.put("q", new ObjectMapper().writeValueAsString(idsQuery));

        builder.addViewParams(query);
        assertEquals(idsQuery, builder.nativeQueryBuilder);
    }

    @Test
    public void testAggregationViewParam() {
        final String aggregation =
                "{\"ageohash_grid_agg\":{\"geohash_grid\": {\"field\":\"a_field\",\"precision\":1}}}";
        parameters.put("a", aggregation);
        builder.addViewParams(query);
        assertEquals(
                ImmutableMap.of(
                        "ageohash_grid_agg",
                        ImmutableMap.of(
                                "geohash_grid",
                                ImmutableMap.of("field", "a_field", "precision", 1))),
                builder.aggregations);
    }

    @Test
    public void testUrlEncodedAggregationViewParam() {
        final String aggregation =
                "%7B%22ageohash_grid_agg%22%3A%20%7B%22geohash_grid%22%3A%20%7B%22field%22%3A%20%22a_field%22%2C%20%22precision%22%3A%201%7D%7D%7D";
        parameters.put("a", aggregation);
        builder.addViewParams(query);
        assertEquals(
                ImmutableMap.of(
                        "ageohash_grid_agg",
                        ImmutableMap.of(
                                "geohash_grid",
                                ImmutableMap.of("field", "a_field", "precision", 1))),
                builder.aggregations);
    }

    @Test(expected = FilterToElasticException.class)
    public void testUrlEncodedAggregationViewParamWithParseError() {
        final String aggregation =
                "%7B%22ageohash_grid_agg%22%3A%20%7B%22geohash_grid%22%3A%20%7B%22field%22%3A%20%22a_field%22%2C%20%22precision%22%3A%201%7D%7D%";
        parameters.put("a", aggregation);
        builder.addViewParams(query);
    }

    @Test
    public void testAndQueryViewParam() throws JsonProcessingException {
        Map<String, Object> idsQuery =
                ImmutableMap.of("ids", ImmutableMap.of("value", ImmutableList.of("id")));
        builder.queryBuilder = idsQuery;
        parameters.put("q", new ObjectMapper().writeValueAsString(idsQuery));

        builder.addViewParams(query);
        assertNotNull(builder.getQueryBuilder());
    }

    @Test
    public void testNativeOnlyQueryViewParam() throws JsonProcessingException {
        parameters.put("native-only", "true");
        Map<String, Object> idsQuery =
                ImmutableMap.of("ids", ImmutableMap.of("value", ImmutableList.of("id")));
        builder.queryBuilder = idsQuery;
        parameters.put("q", new ObjectMapper().writeValueAsString(idsQuery));

        builder.addViewParams(query);
        assertEquals(builder.getQueryBuilder(), idsQuery);
    }

    @Test(expected = FilterToElasticException.class)
    public void testNativeQueryViewParamWithError() {
        parameters.put("native-only", "true");
        builder.queryBuilder =
                ImmutableMap.of("ids", ImmutableMap.of("value", ImmutableList.of("id")));
        parameters.put("q", "{\"x}");

        builder.addViewParams(query);
    }

    @Test
    public void testTemporalStringLiteral() {
        After filter = ff.after(ff.property("dateAttr"), ff.literal("1970-01-01T00:00:00.000Z"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of("dateAttr", ImmutableMap.of("gt", "1970-01-01T00:00:00Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testNestedTemporalStringLiteral() {
        After filter =
                ff.after(ff.property("nested.datehej"), ff.literal("1970-01-01T00:00:00.123Z"));
        Map<String, Object> expectedFilter =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "nested.datehej",
                                ImmutableMap.of("gt", "1970-01-01T00:00:00.123Z")));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "nested", ImmutableMap.of("path", "nested", "query", expectedFilter));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testTemporalInstantLiteralDefaultFormat() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttr"), ff.literal(temporalInstant));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of("dateAttr", ImmutableMap.of("gt", "1970-07-19T00:00:00Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testTemporalInstanceLiteralExplicitFormat() throws ParseException {
        addDateWithFormatToFeatureType("yyyy-MM-dd");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttrWithFormat"), ff.literal(temporalInstant));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of("dateAttrWithFormat", ImmutableMap.of("gt", "1970-07-19")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testTemporalInstanceLiteralBasicDateTimeFormat() throws ParseException {
        addDateWithFormatToFeatureType("basic_date_time");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttrWithFormat"), ff.literal(temporalInstant));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttrWithFormat",
                                ImmutableMap.of("gt", "19700719T020203.456Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testAfterFilter() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttr"), ff.literal(temporalInstant));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr", ImmutableMap.of("gt", "1970-07-19T02:02:03.456Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testAfterFilterSwapped() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.literal(temporalInstant), ff.property("dateAttr"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr", ImmutableMap.of("lt", "1970-07-19T02:02:03.456Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testAfterFilterPeriod() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);
        After filter = ff.after(ff.property("dateAttr"), ff.literal(period));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr", ImmutableMap.of("gt", "1970-07-19T07:08:09.101Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testAfterFilterPeriodSwapped() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);
        After filter = ff.after(ff.literal(period), ff.property("dateAttr"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr", ImmutableMap.of("lt", "1970-07-19T01:02:03.456Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testBeforeFilter() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        org.opengis.filter.temporal.Before filter =
                ff.before(ff.property("dateAttr"), ff.literal(temporalInstant));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr", ImmutableMap.of("lt", "1970-07-19T01:02:03.456Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testBeforeFilterPeriod() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        org.opengis.filter.temporal.Before filter =
                ff.before(ff.property("dateAttr"), ff.literal(period));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr", ImmutableMap.of("lt", "1970-07-19T01:02:03.456Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testBeforeFilterPeriodSwapped() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        org.opengis.filter.temporal.Before filter =
                ff.before(ff.literal(period), ff.property("dateAttr"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr", ImmutableMap.of("gt", "1970-07-19T07:08:09.101Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testBegins() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        Begins filter = ff.begins(ff.property("dateAttr"), ff.literal(period));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("dateAttr", "1970-07-19T01:02:03.456Z"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBeginsWithMissingPeriod() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Begins filter = ff.begins(ff.property("dateAttr"), ff.literal(date1));
        builder.visit(filter, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBeginsWithSwap() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        Begins filter = ff.begins(ff.literal(period), ff.property("dateAttr"));

        builder.visit(filter, null);
    }

    @Test
    public void testBegunBy() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        BegunBy filter = ff.begunBy(ff.literal(period), ff.property("dateAttr"));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("dateAttr", "1970-07-19T01:02:03.456Z"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testDuring() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        During filter = ff.during(ff.property("dateAttr"), ff.literal(period));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr",
                                ImmutableMap.of(
                                        "gt",
                                        "1970-07-19T01:02:03.456Z",
                                        "lt",
                                        "1970-07-19T07:08:09.101Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testEnds() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        Ends filter = ff.ends(ff.property("dateAttr"), ff.literal(period));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("dateAttr", "1970-07-19T07:08:09.101Z"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testEndedBy() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        EndedBy filter = ff.endedBy(ff.literal(period), ff.property("dateAttr"));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("dateAttr", "1970-07-19T07:08:09.101Z"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndedByWithoutSwap() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        EndedBy filter = ff.endedBy(ff.property("dateAttr"), ff.literal(period));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("dateAttr", "1970-07-19T07:08:09.101Z"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testTContains() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        TContains filter = ff.tcontains(ff.literal(period), ff.property("dateAttr"));
        Map<String, Object> expected =
                ImmutableMap.of(
                        "range",
                        ImmutableMap.of(
                                "dateAttr",
                                ImmutableMap.of(
                                        "gt",
                                        "1970-07-19T01:02:03.456Z",
                                        "lt",
                                        "1970-07-19T07:08:09.101Z")));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test
    public void testTEqualsFilter() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        TEquals filter = ff.tequals(ff.property("dateAttr"), ff.literal(temporalInstant));
        Map<String, Object> expected =
                ImmutableMap.of("term", ImmutableMap.of("dateAttr", "1970-07-19T01:02:03.456Z"));

        builder.visit(filter, null);
        assertTrue(builder.createCapabilities().fullySupports(filter));
        assertEquals(expected, builder.getQueryBuilder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTEqualsWithPeriod() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        TEquals filter = ff.tequals(ff.property("dateAttr"), ff.literal(period));

        builder.visit(filter, null);
    }

    @Test
    public void testPropertyNameWithExtraData() {
        builder.visit(ff.property("doubleAttr"), Double.class);
        assertEquals("doubleAttr", builder.field);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedBinaryExpression() {
        builder.visit(ff.subtract(ff.property("doubleAttr"), ff.literal(2.5)), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedPropertyIsNill() {
        builder.visit(ff.isNil(ff.property("stringAttr"), ff.literal(2.5)), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedBinaryComparisonOperatorWithBinaryExpression() {
        builder.visit(
                ff.equals(ff.subtract(ff.property("doubleAttr"), ff.literal(2.5)), ff.literal(0.0)),
                null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedBinaryTemporalOperator() {
        builder.visitBinaryTemporalOperator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedAdd() {
        builder.visit(ff.add(ff.property("p1"), ff.property("p2")), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedSubtract() {
        builder.visit(ff.subtract(ff.property("p1"), ff.property("p2")), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMult() {
        builder.visit(ff.multiply(ff.property("p1"), ff.property("p2")), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDivide() {
        builder.visit(ff.divide(ff.property("p1"), ff.property("p2")), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedFunction() {
        builder.visit(ff.function("sqrt", ff.property("doubleAttr")), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedLiteralTimePeriod() {
        builder.visitLiteralTimePeriod();
    }
}
