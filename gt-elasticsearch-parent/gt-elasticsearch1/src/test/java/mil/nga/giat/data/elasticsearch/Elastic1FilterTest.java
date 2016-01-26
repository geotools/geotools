/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import mil.nga.giat.data.elasticsearch.FilterToElastic;
import mil.nga.giat.data.elasticsearch.ElasticAttribute.ElasticGeometryType;
import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.ANALYZED;
import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.DATE_FORMAT;
import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.GEOMETRY_TYPE;
import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.NESTED;

import org.apache.commons.codec.binary.Base64;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.LineStringBuilder;
import org.elasticsearch.common.geo.builders.PolygonBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.junit.Before;
import org.junit.Test;
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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Elastic1FilterTest {

    private FilterToElastic1 builder;

    private FilterFactory2 ff;
    
    private GeometryFactory gf;

    private SimpleFeatureType featureType;

    private Map<String,String> parameters;

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

        AttributeDescriptor geoPointAtt = null;
        AttributeTypeBuilder geoPointAttBuilder = new AttributeTypeBuilder();
        geoPointAttBuilder.setName("geo_point");
        geoPointAttBuilder.setBinding(Point.class);
        geoPointAtt = geoPointAttBuilder.buildDescriptor("geo_point", geoPointAttBuilder.buildType());
        geoPointAtt.getUserData().put(GEOMETRY_TYPE, ElasticGeometryType.GEO_POINT);
        typeBuilder.add(geoPointAtt);

        AttributeDescriptor geoShapeAtt = null;
        AttributeTypeBuilder geoShapeAttBuilder = new AttributeTypeBuilder();
        geoShapeAttBuilder.setName("geom");
        geoShapeAttBuilder.setBinding(Geometry.class);
        geoShapeAtt = geoShapeAttBuilder.buildDescriptor("geom", geoShapeAttBuilder.buildType());
        geoShapeAtt.getUserData().put(GEOMETRY_TYPE, ElasticGeometryType.GEO_SHAPE);
        typeBuilder.add(geoShapeAtt);

        AttributeDescriptor analyzedAtt = null;
        AttributeTypeBuilder analyzedAttBuilder = new AttributeTypeBuilder();
        analyzedAttBuilder.setName("analyzed");
        analyzedAttBuilder.setBinding(String.class);
        analyzedAtt = analyzedAttBuilder.buildDescriptor("analyzed", analyzedAttBuilder.buildType());
        analyzedAtt.getUserData().put(ANALYZED, true);
        typeBuilder.add(analyzedAtt);

        AttributeDescriptor netsedAtt = null;
        AttributeTypeBuilder nestedAttBuilder = new AttributeTypeBuilder();
        nestedAttBuilder.setName("nested.hej");
        nestedAttBuilder.setBinding(String.class);
        netsedAtt = nestedAttBuilder.buildDescriptor("nested.hej", nestedAttBuilder.buildType());
        netsedAtt.getUserData().put(NESTED, true);
        netsedAtt.getUserData().put(ANALYZED, true);
        typeBuilder.add(netsedAtt);

        AttributeDescriptor netsedDateAtt = null;
        AttributeTypeBuilder nestedDateAttBuilder = new AttributeTypeBuilder();
        nestedDateAttBuilder.setName("nested.datehej");
        nestedDateAttBuilder.setBinding(Date.class);
        netsedDateAtt = nestedDateAttBuilder.buildDescriptor("nested.datehej", nestedDateAttBuilder.buildType());
        netsedDateAtt.getUserData().put(NESTED, true);
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
        builder = new FilterToElastic1();
        builder.setFeatureType(featureType);
    }

    private void addDateWithFormatToFeatureType(String format) {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(featureType);

        AttributeDescriptor dateAtt = null;
        AttributeTypeBuilder dateAttBuilder = new AttributeTypeBuilder();
        dateAttBuilder.setName("dateAttrWithFormat");
        dateAttBuilder.setBinding(Date.class);
        dateAtt = dateAttBuilder.buildDescriptor("dateAttrWithFormat", dateAttBuilder.buildType());
        dateAtt.getUserData().put(DATE_FORMAT, format);
        typeBuilder.add(dateAtt);

        featureType = typeBuilder.buildFeatureType();
        setFilterBuilder();
    }

    @Test
    public void testId() {
        final Id filter = ff.id(ff.featureId("id"));
        IdsFilterBuilder expected = FilterBuilders.idsFilter().addIds("id");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testAnd() {
        And filter = ff.and(ff.id(ff.featureId("id1")), ff.id(ff.featureId("id2")));
        AndFilterBuilder expected = FilterBuilders.andFilter(
                FilterBuilders.idsFilter().addIds("id1"), 
                FilterBuilders.idsFilter().addIds("id2"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testOr() {
        final Or filter = ff.or(ff.id(ff.featureId("id1")), ff.id(ff.featureId("id2")));
        OrFilterBuilder expected = FilterBuilders.orFilter(
                FilterBuilders.idsFilter().addIds("id1"), 
                FilterBuilders.idsFilter().addIds("id2"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testNot() {
        Not filter = ff.not(ff.id(ff.featureId("id")));
        NotFilterBuilder expected = FilterBuilders.notFilter(FilterBuilders.idsFilter().addIds("id"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsNull() {
        PropertyIsNull filter = ff.isNull(ff.property("prop"));
        MissingFilterBuilder expected = FilterBuilders.missingFilter("prop");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsNotNull() {
        Not filter = ff.not(ff.isNull(ff.property("prop")));
        ExistsFilterBuilder expected = FilterBuilders.existsFilter("prop");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsEqualToString() {
        PropertyIsEqualTo filter = ff.equals(ff.property("stringAttr"), ff.literal("value"));
        TermFilterBuilder expected = FilterBuilders.termFilter("stringAttr", "value");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testNestedPropertyIsEqualToString() {
        PropertyIsEqualTo filter = ff.equals(ff.property("nested.hej"), ff.literal("value"));
        NestedFilterBuilder expected = FilterBuilders.nestedFilter("nested", FilterBuilders.termFilter("nested.hej", "value"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertEquals(expected.toString(),builder.getFilterBuilder().toString());
    }

    @Test
    public void testNestedStringIsEqualToProperty() {
        PropertyIsEqualTo filter = ff.equals(ff.literal("value"), ff.property("nested.hej"));
        NestedFilterBuilder expected = FilterBuilders.nestedFilter("nested", FilterBuilders.termFilter("nested.hej", "value"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertEquals(expected.toString(),builder.getFilterBuilder().toString());
    }

    @Test
    public void testPropertyIsNotEqualToString() {
        PropertyIsNotEqualTo filter = ff.notEqual(ff.property("stringAttr"), ff.literal("value"));
        NotFilterBuilder expected = FilterBuilders.notFilter(FilterBuilders.termFilter("stringAttr", "value"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsEqualToDouble() {
        PropertyIsEqualTo filter = ff.equals(ff.property("doubleAttr"), ff.literal("4.5"));
        TermFilterBuilder expected = FilterBuilders.termFilter("doubleAttr", 4.5);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testDoubleIsEqualtoProperty() {
        PropertyIsEqualTo filter = ff.equals(ff.literal("4.5"), ff.property("doubleAttr"));
        TermFilterBuilder expected = FilterBuilders.termFilter("doubleAttr", 4.5);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsNotEqualToDouble() {
        PropertyIsNotEqualTo filter = ff.notEqual(ff.property("doubleAttr"), ff.literal("4.5"));
        NotFilterBuilder expected = FilterBuilders.notFilter(FilterBuilders.termFilter("doubleAttr", 4.5));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsEqualToFloat() {
        PropertyIsEqualTo filter = ff.equals(ff.property("floatAttr"), ff.literal("4.5"));
        TermFilterBuilder expected = FilterBuilders.termFilter("floatAttr", 4.5f);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsEqualToInteger() {
        PropertyIsEqualTo filter = ff.equals(ff.property("integerAttr"), ff.literal("4"));
        TermFilterBuilder expected = FilterBuilders.termFilter("integerAttr", 4);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsEqualToBoolean() {
        PropertyIsEqualTo filter = ff.equals(ff.property("booleanAttr"), ff.literal("true"));
        TermFilterBuilder expected = FilterBuilders.termFilter("booleanAttr", true);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsGreaterThan() {
        PropertyIsGreaterThan filter = ff.greater(ff.property("doubleAttr"), ff.literal("4.5"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("doubleAttr").gt(4.5);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsLessThan() {
        PropertyIsLessThan filter = ff.less(ff.property("doubleAttr"), ff.literal("4.5"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("doubleAttr").lt(4.5);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsGreaterThanOrEqualTo() {
        PropertyIsGreaterThanOrEqualTo filter = ff.greaterOrEqual(ff.property("doubleAttr"), ff.literal("4.5"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("doubleAttr").gte(4.5);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsLessThanOrEqualTo() {
        PropertyIsLessThanOrEqualTo filter = ff.lessOrEqual(ff.property("doubleAttr"), ff.literal("4.5"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("doubleAttr").lte(4.5);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testPropertyIsBetween() {
        PropertyIsBetween filter = ff.between(ff.property("doubleAttr"), ff.literal("4.5"), ff.literal("5.5"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("doubleAttr").gte(4.5).lte(5.5);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testUnknownPropertyIsBetween() {
        PropertyIsBetween filter = ff.between(ff.property("unknownStr"), ff.literal("a"), ff.literal("c"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("unknownStr").gte("a").lte("c");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testIncludeFilter() {
        IncludeFilter filter = Filter.INCLUDE;
        MatchAllFilterBuilder expected = FilterBuilders.matchAllFilter();

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testExcludeFilter() {
        ExcludeFilter filter = Filter.EXCLUDE;
        NotFilterBuilder expected = FilterBuilders.notFilter(FilterBuilders.matchAllFilter());

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }
    
    @Test
    public void testNullFilter() {
        assertTrue(builder.visitNullFilter(null)==null);
    }
    
    @Test
    public void testNilFilter() {
        builder.field = "field";
        builder.visit((NilExpression) NilExpression.NIL, null);
        assertTrue(builder.field == null);
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
        QueryFilterBuilder expected = FilterBuilders.queryFilter(QueryBuilders.queryString("hello").defaultField("analyzed"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testCaseSensitivePropertyIsLike() {
        PropertyIsLike filter = ff.like(ff.property("analyzed"), "hello", "\\", "*", ".", true);
        QueryFilterBuilder expected = FilterBuilders.queryFilter(QueryBuilders.queryString("hello").defaultField("analyzed"));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testNestedPropertyIsLike() {
        PropertyIsLike filter = ff.like(ff.property("nested.hej"), "hello");
        QueryFilterBuilder expectedFilter = FilterBuilders.queryFilter(QueryBuilders.queryString("hello").defaultField("nested.hej"));
        NestedFilterBuilder expected = FilterBuilders.nestedFilter("nested", expectedFilter);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertEquals(expected.toString(),builder.getFilterBuilder().toString());
    }

    @Test
    public void testConvertToRegex() {
        assertTrue("BroadWay.*".equals( FilterToElastic.convertToRegex('!','*','.',true,"BroadWay*")));
        assertTrue("broad#ay".equals(  FilterToElastic.convertToRegex('!','*','.',true,"broad#ay")));
        assertTrue("broadway".equals(  FilterToElastic.convertToRegex('!','*','.',true,"broadway")));

        assertTrue("broad.ay".equals(FilterToElastic.convertToRegex('!','*','.',true,"broad.ay")));
        assertTrue("broad\\.ay".equals(FilterToElastic.convertToRegex('!','*','.',true,"broad!.ay")));

        assertTrue("broa'dway".equals(FilterToElastic.convertToRegex('!','*','.',true,"broa'dway")));
        assertTrue("broa''dway".equals(FilterToElastic.convertToRegex('!','*','.',true,"broa''dway")));

        assertTrue("broadway.".equals(FilterToElastic.convertToRegex('!','*','.',true,"broadway.")));
        assertTrue("broadway".equals(FilterToElastic.convertToRegex('!','*','.',true,"broadway!")));
        assertTrue("broadway\\!".equals(FilterToElastic.convertToRegex('!','*','.',true,"broadway!!")));
        assertTrue("broadway\\\\".equals(FilterToElastic.convertToRegex('\\','*','.',true,"broadway\\\\")));        
        assertTrue("broadway\\".equals(FilterToElastic.convertToRegex('!','*','.',true,"broadway\\")));
    }

    @Test
    public void testConvertToQueryString() {
        assertTrue("BroadWay*".equals( FilterToElastic.convertToQueryString('!','*','.',true,"BroadWay*")));
        assertTrue("broad#ay".equals(  FilterToElastic.convertToQueryString('!','*','.',true,"broad#ay")));
        assertTrue("broadway".equals(  FilterToElastic.convertToQueryString('!','*','.',true,"broadway")));

        assertTrue("broad?ay".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broad.ay")));
        assertTrue("broad\\.ay".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broad!.ay")));

        assertTrue("broa'dway".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broa'dway")));
        assertTrue("broa''dway".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broa''dway")));

        assertTrue("broadway?".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broadway.")));
        assertTrue("broadway".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broadway!")));
        assertTrue("broadway\\!".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broadway!!")));
        assertTrue("broadway\\\\".equals(FilterToElastic.convertToQueryString('\\','*','.',true,"broadway\\\\")));        
        assertTrue("broadway\\".equals(FilterToElastic.convertToQueryString('!','*','.',true,"broadway\\")));
    }

    @Test
    public void testGeoShapeBboxFilter() throws ParseException, IOException {
        BBOX filter = ff.bbox("geom", 0., 0., 1., 1., "EPSG:4326");
        PolygonBuilder shape = ShapeBuilder.newPolygon().point(0, 0).point(0,1).point(1,1).point(1,0).point(0,0);
        GeoShapeFilterBuilder expected = FilterBuilders.geoShapeFilter("geom", shape, ShapeRelation.INTERSECTS);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testGeoShapeIntersectsFilter() throws CQLException {
        Intersects filter = (Intersects) ECQL.toFilter("INTERSECTS(\"geom\", LINESTRING(0 0,1 1))");
        LineStringBuilder shape = ShapeBuilder.newLineString().point(0, 0).point(1,1);
        GeoShapeFilterBuilder expected = FilterBuilders.geoShapeFilter("geom", shape, ShapeRelation.INTERSECTS);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testEmptyGeoShape() throws CQLException {
        LineString ls = gf.createLineString(new Coordinate[0]);
        Intersects filter = ff.intersects(ff.property("geom"), ff.literal(ls));
        
        NotFilterBuilder expected = FilterBuilders.notFilter(FilterBuilders.matchAllFilter());

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testEmptyDisjointGeoShape() throws CQLException {
        LineString ls = gf.createLineString(new Coordinate[0]);
        Disjoint filter = ff.disjoint(ff.property("geom"), ff.literal(ls));
        
        MatchAllFilterBuilder expected = FilterBuilders.matchAllFilter();

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testOutsideWorld() throws CQLException {
        Polygon poly = gf.createPolygon(new Coordinate[] {
                new Coordinate(-360,-180), new Coordinate(360,-180),
                new Coordinate(360,180), new Coordinate(-360,180), new Coordinate(-360,-180)});
        LineString ls = gf.createLineString(new Coordinate[] {new Coordinate(0,0), new Coordinate(1,1)});
        GeometryCollection gc = gf.createGeometryCollection(new Geometry[] {poly, ls});
        Disjoint filter = ff.disjoint(ff.property("geom"), ff.literal(gc));
        
        MatchAllFilterBuilder expected = FilterBuilders.matchAllFilter();

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertEquals(expected.toString(), builder.getFilterBuilder().toString());
    }

    @Test
    public void testGeoShapeIntersectsFilterReversed() throws CQLException {
        Intersects filter = (Intersects) ECQL.toFilter("INTERSECTS(LINESTRING(0 0,1 1), \"geom\")");
        LineStringBuilder shape = ShapeBuilder.newLineString().point(0, 0).point(1,1);
        GeoShapeFilterBuilder expected = FilterBuilders.geoShapeFilter("geom", shape, ShapeRelation.INTERSECTS);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testAndWithBbox() {
        And filter = ff.and(ff.id(ff.featureId("id1")), ff.bbox("geom", 0., 0., 1., 1., "EPSG:4326"));
        PolygonBuilder shape = ShapeBuilder.newPolygon().point(0, 0).point(0,1).point(1,1).point(1,0).point(0,0);
        AndFilterBuilder expected = FilterBuilders.andFilter(
                FilterBuilders.idsFilter().addIds("id1"), 
                FilterBuilders.geoShapeFilter("geom", shape, ShapeRelation.INTERSECTS));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testGeoPointBboxFilter() {
        BBOX filter = ff.bbox("geo_point", 0., 0., 1., 1., "EPSG:4326");
        GeoBoundingBoxFilterBuilder expected = FilterBuilders.geoBoundingBoxFilter("geo_point").topLeft(1,0).bottomRight(0,1);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));       
    }

    @Test
    public void testGeoPolygonFilter() throws CQLException {
        Intersects filter = (Intersects) ECQL.toFilter("INTERSECTS(\"geo_point\", POLYGON((0 0, 0 1, 1 1, 1 0, 0 0)))");
        GeoPolygonFilterBuilder expected = FilterBuilders.geoPolygonFilter("geo_point").addPoint(0,0).addPoint(1,0).addPoint(1,1).addPoint(0,1).addPoint(0,0);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));       
    }

    @Test
    public void testDWithinFilter() throws CQLException {
        DWithin filter = (DWithin) ECQL.toFilter("DWITHIN(\"geo_point\", POINT(0 1), 1.0, meters)");
        GeoDistanceFilterBuilder expected = FilterBuilders.geoDistanceFilter("geo_point").lat(1).lon(0).distance(1., DistanceUnit.METERS);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));       
    }

    @Test
    public void testDWithinPolygonFilter() throws CQLException {
        DWithin filter = (DWithin) ECQL.toFilter("DWITHIN(\"geo_point\", POLYGON((0 0, 0 1, 1 1, 1 0, 0 0)), 1.0, meters)");
        GeoDistanceFilterBuilder expected = FilterBuilders.geoDistanceFilter("geo_point").lat(0.5).lon(0.5).distance(1., DistanceUnit.METERS);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));       
    }

    @Test
    public void testDBeyondFilter() throws CQLException {
        Beyond filter = (Beyond) ECQL.toFilter("BEYOND(\"geo_point\", POINT(0 1), 1.0, meters)");
        NotFilterBuilder expected = FilterBuilders.notFilter(FilterBuilders.geoDistanceFilter("geo_point").lat(1).lon(0).distance(1., DistanceUnit.METERS));

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));       
    }

    @Test
    public void compoundFilter() throws CQLException {
        Filter filter = ECQL.toFilter("time > \"1970-01-01\" and INTERSECTS(\"geom\", LINESTRING(0 0,1 1))");
        RangeFilterBuilder expected1 = FilterBuilders.rangeFilter("time").gt("1970-01-01");
        LineStringBuilder shape = ShapeBuilder.newLineString().point(0, 0).point(1,1);
        GeoShapeFilterBuilder expected2 = FilterBuilders.geoShapeFilter("geom", shape, ShapeRelation.INTERSECTS);
        AndFilterBuilder expected = FilterBuilders.andFilter(expected1, expected2);

        builder.encode(filter);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testCql() throws CQLException {
        Filter filter = ECQL.toFilter("\"object.field\"='value'");
        TermFilterBuilder expected = FilterBuilders.termFilter("object.field", "value");

        builder.encode(filter);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testViewParamWithNullHints() {
        query.setHints(null);
        
        builder.addViewParams(query);
        assertTrue(builder.getFilterBuilder().toString().equals(FilterBuilders.matchAllFilter().toString()));
        assertEquals(QueryBuilders.matchAllQuery().toString(), builder.nativeQueryBuilder.toString());
    }

    @Test
    public void testQueryViewParam() {
        IdsQueryBuilder idsQuery = QueryBuilders.idsQuery("type1");
        parameters.put("q", idsQuery.toString());
        byte[] encoded = Base64.encodeBase64(idsQuery.toString().getBytes());
        final Pattern expected = Pattern.compile(".*\"wrapper\".*\"query\".*\"" + new String(encoded) + ".*", Pattern.MULTILINE|Pattern.DOTALL);

        builder.addViewParams(query);
        assertTrue(builder.getFilterBuilder().toString().equals(FilterBuilders.matchAllFilter().toString()));
        assertTrue(expected.matcher(builder.getQueryBuilder().toString()).matches());
    }

    @Test
    public void testFilterViewParam() {
        IdsFilterBuilder idsFilter = FilterBuilders.idsFilter().addIds("id");
        parameters.put("f", idsFilter.toString());
        byte[] encoded = Base64.encodeBase64(idsFilter.toString().getBytes());
        final Pattern expected = Pattern.compile(".*\"wrapper\".*\"filter\".*\"" + new String(encoded) + ".*", Pattern.MULTILINE|Pattern.DOTALL);

        builder.addViewParams(query);
        assertTrue(expected.matcher(builder.getFilterBuilder().toString()).matches());
        assertTrue(builder.nativeQueryBuilder.toString().equals(QueryBuilders.matchAllQuery().toString()));
    }

    @Test
    public void testAndFilterViewParam() {
        IdsFilterBuilder idsFilter = FilterBuilders.idsFilter().addIds("id");
        builder.filterBuilder = idsFilter;
        parameters.put("f", idsFilter.toString());

        builder.addViewParams(query);
        assertTrue(builder.getFilterBuilder() instanceof AndFilterBuilder);
    }
    
    @Test
    public void testNativeOnlyFilterViewParam() {
        parameters.put("native-only", "true");        
        IdsFilterBuilder idsFilter = FilterBuilders.idsFilter().addIds("id");
        builder.filterBuilder = idsFilter;
        parameters.put("f", idsFilter.toString());
        
        builder.addViewParams(query);
        assertTrue(!(builder.getFilterBuilder() instanceof AndFilterBuilder));
    }

    @Test
    public void testTemporalStringLiteral() {
        After filter = ff.after(ff.property("dateAttr"), ff.literal("1970-01-01 00:00:00"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").gt("1970-01-01 00:00:00");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testNestedTemporalStringLiteral() {
        After filter = ff.after(ff.property("nested.datehej"), ff.literal("1970-01-01 00:00:00"));
        RangeFilterBuilder expectedFilter = FilterBuilders.rangeFilter("nested.datehej").gt("1970-01-01 00:00:00");
        NestedFilterBuilder expected = FilterBuilders.nestedFilter("nested", expectedFilter);

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testTemporalInstantLiteralDefaultFormat() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttr"), ff.literal(temporalInstant));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").gt("1970-07-19T00:00:00.000Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testTemporalInstanceLiteralExplicitFormat() throws ParseException {
        addDateWithFormatToFeatureType("yyyy-MM-dd");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttrWithFormat"), ff.literal(temporalInstant));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttrWithFormat").gt("1970-07-19");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testTemporalInstanceLiteralBasicDateTimeFormat() throws ParseException {
        addDateWithFormatToFeatureType("basic_date_time");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttrWithFormat"), ff.literal(temporalInstant));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttrWithFormat").gt("19700719T020203.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testAfterFilter() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.property("dateAttr"), ff.literal(temporalInstant));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").gt("1970-07-19T02:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testAfterFilterSwapped() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456-0100");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        After filter = ff.after(ff.literal(temporalInstant), ff.property("dateAttr"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").lt("1970-07-19T02:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testAfterFilterPeriod() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);
        After filter = ff.after(ff.property("dateAttr"), ff.literal(period));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").gt("1970-07-19T07:08:09.101Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testAfterFilterPeriodSwapped() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);
        After filter = ff.after(ff.literal(period), ff.property("dateAttr"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").lt("1970-07-19T01:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testBeforeFilter() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        org.opengis.filter.temporal.Before filter = ff.before(ff.property("dateAttr"), ff.literal(temporalInstant));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").lt("1970-07-19T01:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testBeforeFilterPeriod() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        org.opengis.filter.temporal.Before filter = ff.before(ff.property("dateAttr"), ff.literal(period));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").lt("1970-07-19T01:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testBeforeFilterPeriodSwapped() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        org.opengis.filter.temporal.Before filter = ff.before(ff.literal(period), ff.property("dateAttr"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").gt("1970-07-19T07:08:09.101Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testBegins() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        Begins filter = ff.begins(ff.property("dateAttr"), ff.literal(period));
        TermFilterBuilder expected = FilterBuilders.termFilter("dateAttr","1970-07-19T01:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBeginsWithMissingPeriod() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Begins filter = ff.begins(ff.property("dateAttr"), ff.literal(date1));
        builder.visit(filter, null);
    }

    @Test(expected=IllegalArgumentException.class)
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
        TermFilterBuilder expected = FilterBuilders.termFilter("dateAttr","1970-07-19T01:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testDuring() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        During filter = ff.during(ff.property("dateAttr"), ff.literal(period));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").gt("1970-07-19T01:02:03.456Z").lt("1970-07-19T07:08:09.101Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testEnds() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        Ends filter = ff.ends(ff.property("dateAttr"), ff.literal(period));
        TermFilterBuilder expected = FilterBuilders.termFilter("dateAttr","1970-07-19T07:08:09.101Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testEndedBy() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        EndedBy filter = ff.endedBy(ff.literal(period), ff.property("dateAttr"));
        TermFilterBuilder expected = FilterBuilders.termFilter("dateAttr","1970-07-19T07:08:09.101Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEndedByWithoutSwap() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        EndedBy filter = ff.endedBy(ff.property("dateAttr"), ff.literal(period));
        TermFilterBuilder expected = FilterBuilders.termFilter("dateAttr","1970-07-19T07:08:09.101Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testTContains() throws ParseException {
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        Date date2 = dateFormat.parse("1970-07-19T07:08:09.101Z");
        Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
        Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

        TContains filter = ff.tcontains(ff.literal(period), ff.property("dateAttr"));
        RangeFilterBuilder expected = FilterBuilders.rangeFilter("dateAttr").gt("1970-07-19T01:02:03.456Z").lt("1970-07-19T07:08:09.101Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test
    public void testTEqualsFilter() throws ParseException {        
        Date date1 = dateFormat.parse("1970-07-19T01:02:03.456Z");
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));
        TEquals filter = ff.tequals(ff.property("dateAttr"), ff.literal(temporalInstant));
        TermFilterBuilder expected = FilterBuilders.termFilter("dateAttr","1970-07-19T01:02:03.456Z");

        builder.visit(filter, null);
        assertTrue(builder.createFilterCapabilities().fullySupports(filter));
        assertTrue(builder.getFilterBuilder().toString().equals(expected.toString()));
    }

    @Test(expected=IllegalArgumentException.class)
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
        assertTrue(builder.field.equals("doubleAttr"));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedBinaryExpression() {
        builder.visit(ff.subtract(ff.property("doubleAttr"), ff.literal(2.5)), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedPropertyIsNill() {
        builder.visit(ff.isNil(ff.property("stringAttr"), ff.literal(2.5)), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedBinaryComparisonOperatorWithBinaryExpression() {
        builder.visit(ff.equals(ff.subtract(ff.property("doubleAttr"), ff.literal(2.5)),ff.literal(0.0)), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedBinaryTemporalOperator() {
        builder.visitBinaryTemporalOperator(null,null,null,null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedAdd() {
        builder.visit(ff.add(ff.property("p1"), ff.property("p2")), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedSubtract() {
        builder.visit(ff.subtract(ff.property("p1"), ff.property("p2")), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedMult() {
        builder.visit(ff.multiply(ff.property("p1"), ff.property("p2")), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedDivide() {
        builder.visit(ff.divide(ff.property("p1"), ff.property("p2")), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedFunction() {
        builder.visit(ff.function("sqrt", ff.property("doubleAttr")), null);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUnsupportedLiteralTimePeriod() {
        builder.visitLiteralTimePeriod(null);
    }
        
}
