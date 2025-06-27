/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.expression;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/** Tests the MapBox Feature Data expressions. */
public class MBFeatureDataTest extends AbstractMBExpressionTest {

    private SimpleFeature[] lineStringFeatures;
    private SimpleFeature[] polygonFeatures;
    private SimpleFeature[] multiPolygonFeatures;
    private SimpleFeature[] multiLineStringFeatures;
    private SimpleFeature[] multiPointFeatures;

    @Override
    protected String getTestResourceName() {
        return "expressionMBFeatureDataTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBFeatureData.class;
    }

    private MultiPoint createMultiPoint(int index) throws Exception {
        // create 5 coordinates based off the supplied index
        double d1 = 5 * index + 10;
        double d2 = 5 * index + 11;
        double d3 = 5 * index + 12;
        double d4 = 5 * index + 13;
        double d5 = 5 * index + 14;
        Coordinate c1 = new Coordinate(d1, d1);
        Coordinate c2 = new Coordinate(d2, d2);
        Coordinate c3 = new Coordinate(d3, d3);
        Coordinate c4 = new Coordinate(d4, d4);
        Coordinate c5 = new Coordinate(d5, d5);
        return geometryFactory.createMultiPoint(new CoordinateArraySequence(new Coordinate[] {c1, c2, c3, c4, c5}));
    }

    private LineString createLineString(int index) throws Exception {
        // create 5 coordinates based off the supplied index
        double d1 = 5 * index + 10;
        double d2 = 5 * index + 11;
        double d3 = 5 * index + 12;
        double d4 = 5 * index + 13;
        double d5 = 5 * index + 14;
        Coordinate c1 = new Coordinate(d1, d1);
        Coordinate c2 = new Coordinate(d2, d2);
        Coordinate c3 = new Coordinate(d3, d3);
        Coordinate c4 = new Coordinate(d4, d4);
        Coordinate c5 = new Coordinate(d5, d5);
        return geometryFactory.createLineString(new Coordinate[] {c1, c2, c3, c4, c5});
    }

    private Polygon createPolygon(int index) throws Exception {
        // create 2 coordinates based off the supplied index
        double d1 = 5 * index + 10;
        double d2 = 5 * index + 11;
        Coordinate c1 = new Coordinate(d1, d1);
        Coordinate c2 = new Coordinate(d1, d2);
        Coordinate c3 = new Coordinate(d2, d2);
        Coordinate c4 = new Coordinate(d2, d1);
        return geometryFactory.createPolygon(new Coordinate[] {c1, c2, c3, c4, c1});
    }

    private MultiLineString createMultiLineString(int index) throws Exception {
        // create linestrings
        LineString l1 = createLineString(index);
        LineString l2 = createLineString(index + 10);
        LineString l3 = createLineString(index + 20);
        return geometryFactory.createMultiLineString(new LineString[] {l1, l2, l3});
    }

    private MultiPolygon createMultiPolygon(int index) throws Exception {
        // create polygons
        Polygon p1 = createPolygon(index);
        Polygon p2 = createPolygon(index + 10);
        Polygon p3 = createPolygon(index + 20);
        return geometryFactory.createMultiPolygon(new Polygon[] {p1, p2, p3});
    }

    @Override
    protected void setupInternal() throws Exception {
        // add some extra features as test structures
        final SimpleFeatureType lineStringType = DataUtilities.createType(
                "mbfeaturedata.line",
                "lineInt:int,anotherLineInt:int,lineDoubleField:double,lineGeom:LineString,lineName:String");
        final SimpleFeatureType polygonType = DataUtilities.createType(
                "mbfeaturedata.polygon",
                "polygonInt:int,anotherPolygonInt:int,polygonDouble:double,polygonGeom:Polygon,polygonName:String");
        final SimpleFeatureType multiLineStringType = DataUtilities.createType(
                "mbfeaturedata.multiline",
                "multiLineInt:int,anotherMultiLineInt:int,multiLineDoubleField:double,multiLineGeom:MultiLineString,multiLineName:String");
        final SimpleFeatureType multiPolygonType = DataUtilities.createType(
                "mbfeaturedata.multipolygon",
                "multiPolygonInt:int,anotherMultiPolygonInt:int,multiPolygonDouble:double,multiPolygonGeom:MultiPolygon,multiPolygonName:String");
        final SimpleFeatureType multiPointType = DataUtilities.createType(
                "mbfeaturedata.multipoint",
                "multiPointInt:int,anotherMultiPointInt:int,multiPointDoubleField:double,multiPointGeom:MultiPoint,multiPointName:String");
        // create the feature arrays
        lineStringFeatures = new SimpleFeature[intVals.length];
        polygonFeatures = new SimpleFeature[intVals.length];
        multiPointFeatures = new SimpleFeature[intVals.length];
        multiLineStringFeatures = new SimpleFeature[intVals.length];
        multiPolygonFeatures = new SimpleFeature[intVals.length];
        // build some features
        for (int i = 0; i < intVals.length; ++i) {
            // get the feature values
            final Integer anInt = i;
            final Integer anotherInt = intVals[i];
            final Double aDouble = doubleVals[i];
            final String aName = "name_" + intVals[i];
            // build the features
            final SimpleFeature multiPoint = SimpleFeatureBuilder.build(
                    multiPointType,
                    new Object[] {anInt, anotherInt, aDouble, createMultiPoint(i), aName},
                    "mbmultipoint." + (i + 1));
            final SimpleFeature line = SimpleFeatureBuilder.build(
                    lineStringType,
                    new Object[] {anInt, anotherInt, aDouble, createLineString(i), aName},
                    "mbline." + (i + 1));
            final SimpleFeature multiLine = SimpleFeatureBuilder.build(
                    multiLineStringType,
                    new Object[] {anInt, anotherInt, aDouble, createMultiLineString(i), aName},
                    "mbmultiline." + (i + 1));
            final SimpleFeature polygon = SimpleFeatureBuilder.build(
                    polygonType,
                    new Object[] {anInt, anotherInt, aDouble, createPolygon(i), aName},
                    "mbpolygon." + (i + 1));
            final SimpleFeature multiPolygon = SimpleFeatureBuilder.build(
                    multiPolygonType,
                    new Object[] {anInt, anotherInt, aDouble, createMultiPolygon(i), aName},
                    "mbmultipolygon." + (i + 1));
            // add the features
            lineStringFeatures[i] = line;
            polygonFeatures[i] = polygon;
            multiPointFeatures[i] = multiPoint;
            multiLineStringFeatures[i] = multiLine;
            multiPolygonFeatures[i] = multiPolygon;
        }
    }

    @Test
    public void testFeatureDataId() throws Exception {
        final JSONObject j = getObjectByLayerId("featureDataId", "layout");
        // validate the feature ids
        for (int i = 0; i < intVals.length; ++i) {
            assertEquals("mbexpression." + (i + 1), getExpressionEvaluation(j, "featureId", testFeatures[i]));
            assertEquals("mbmultipoint." + (i + 1), getExpressionEvaluation(j, "featureId", multiPointFeatures[i]));
            assertEquals("mbline." + (i + 1), getExpressionEvaluation(j, "featureId", lineStringFeatures[i]));
            assertEquals("mbmultiline." + (i + 1), getExpressionEvaluation(j, "featureId", multiLineStringFeatures[i]));
            assertEquals("mbpolygon." + (i + 1), getExpressionEvaluation(j, "featureId", polygonFeatures[i]));
            assertEquals("mbmultipolygon." + (i + 1), getExpressionEvaluation(j, "featureId", multiPolygonFeatures[i]));
        }
    }

    @Test
    public void testGeometryType() throws Exception {
        final JSONObject j = getObjectByLayerId("featureDataId", "layout");
        // validate the geometry types
        for (int i = 0; i < intVals.length; ++i) {
            assertEquals("Point", getExpressionEvaluation(j, "geometryType", testFeatures[i]));
            assertEquals("LineString", getExpressionEvaluation(j, "geometryType", lineStringFeatures[i]));
            assertEquals("Polygon", getExpressionEvaluation(j, "geometryType", polygonFeatures[i]));
            assertEquals("MultiPoint", getExpressionEvaluation(j, "geometryType", multiPointFeatures[i]));
            assertEquals("MultiLineString", getExpressionEvaluation(j, "geometryType", multiLineStringFeatures[i]));
            assertEquals("MultiPolygon", getExpressionEvaluation(j, "geometryType", multiPolygonFeatures[i]));
        }
    }

    // Currently, "properties" is not implemented. When it is, the expected Exception should be
    // removed.
    @Test(expected = UnsupportedOperationException.class)
    public void testProperties() throws Exception {
        final JSONObject j = getObjectByLayerId("featureDataId", "layout");
        // validate the properties
        final Collection<String> pointProps =
                Arrays.asList("anIntField", "anotherIntField", "doubleField", "geom", "name");
        final Collection<String> lineProps =
                Arrays.asList("lineInt", "anotherLineInt", "lineDoubleField", "lineGeom", "lineName");
        final Collection<String> polygonProps =
                Arrays.asList("polygonInt", "anotherPolygonInt", "polygonDouble", "polygonGeom", "polygonName");
        final Collection<String> multiPointProps = Arrays.asList(
                "multiPointInt", "anotherMultiPointInt", "multiPointDoubleField", "multiPointGeom", "multiPointName");
        final Collection<String> multiLineProps = Arrays.asList(
                "multiLineInt", "anotherMultiLineInt", "multiLineDoubleField", "multiLineGeom", "multiLineName");
        final Collection<String> multiPolygonProps = Arrays.asList(
                "multiPolygonInt",
                "anotherMultiPolygonInt",
                "multiPolygonDouble",
                "multiPolygonGeom",
                "multiPolygonName");
        for (int i = 0; i < intVals.length; ++i) {
            assertEquals(pointProps, getExpressionEvaluation(j, "properties", testFeatures[i]));
            assertEquals(lineProps, getExpressionEvaluation(j, "properties", lineStringFeatures[i]));
            assertEquals(polygonProps, getExpressionEvaluation(j, "properties", polygonFeatures[i]));
            assertEquals(multiPointProps, getExpressionEvaluation(j, "properties", multiPointFeatures[i]));
            assertEquals(multiLineProps, getExpressionEvaluation(j, "properties", multiLineStringFeatures[i]));
            assertEquals(multiPolygonProps, getExpressionEvaluation(j, "properties", multiPolygonFeatures[i]));
        }
    }
}
