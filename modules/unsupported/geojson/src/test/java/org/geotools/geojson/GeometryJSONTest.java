/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson;

import java.io.IOException;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class GeometryJSONTest extends GeoJSONTestSupport {

    GeometryFactory gf = new GeometryFactory();
    GeometryJSON gjson = new GeometryJSON();

    public void testPointWrite() throws Exception {
        assertEquals(pointText(), gjson.toString(point()));
        assertEquals(point3dText(), gjson.toString(point3d()));
    }

    String pointText() {
        return strip("{'type': 'Point','coordinates':[100.1,0.1]}");
    }

    String badPointText() {
        return strip("{'type': 'Point','coordinates':[100.1]}");
    }

    Point point() {
        Point p = gf.createPoint(new Coordinate(100.1, 0.1));
        return p;
    }

    String point3dText() {
        return strip("{'type': 'Point','coordinates':[100.1,0.1,10.2]}");
    }

    Point point3d() {
        Point p = gf.createPoint(new Coordinate(100.1, 0.1, 10.2));
        return p;
    }

    public void testPointRead() throws Exception {
        assertEquals(point(), gjson.readPoint(reader(pointText())));
        assertEquals(point3d(), gjson.readPoint(reader(point3dText())));
        try {
            gjson.read(reader(badPointText()));
            fail("Read in a bad point");
        } catch (Exception e) {
            // good
        }
    }

    public void testLineWrite() throws Exception {
        assertEquals(lineText(), gjson.toString(line()));
        assertEquals(line2Text(), gjson.toString(line2()));
        assertEquals(line3dText(), gjson.toString(line3d()));
    }

    String lineText() {
        return strip("{'type': 'LineString', 'coordinates': [[100.1,0.1],[101.1,1.1]]}");
    }

    String badLineText() {
        return strip("{'type': 'LineString', 'coordinates': [[100.1,0.1],[101.1]]}");
    }

    String line2Text() {
        return strip("null");
    }

    LineString line() {
        LineString l = gf.createLineString(array(new double[][] {{100.1, 0.1}, {101.1, 1.1}}));
        return l;
    }

    LineString line2() {
        LineString l = gf.createLineString(array(new double[][] {}));
        return l;
    }

    String line3dText() {
        return strip("{'type': 'LineString', 'coordinates': [[100.1,0.1,10.2],[101.1,1.1,10.2]]}");
    }

    LineString line3d() {
        LineString l =
                gf.createLineString(array(new double[][] {{100.1, 0.1, 10.2}, {101.1, 1.1, 10.2}}));
        return l;
    }

    public void testLineRead() throws Exception {
        assertEquals(line(), (gjson.readLine(reader(lineText()))));
        assertNull(gjson.readLine(reader(line2Text())));
        assertEquals(line3d(), (gjson.readLine(reader(line3dText()))));
        try {
            gjson.read(gjson.readLine(reader(badLineText())));
            fail("Read in bad line");
        } catch (Exception e) {
            // good
        }
    }

    public void testPolyWrite() throws Exception {
        assertEquals(polygonText1(), gjson.toString(polygon1()));
        assertEquals(polygonText2(), gjson.toString(polygon2()));
        assertEquals(polygonText3(), gjson.toString(polygon3()));
    }

    Polygon polygon2() {
        Polygon poly;
        poly =
                gf.createPolygon(
                        gf.createLinearRing(
                                array(
                                        new double[][] {
                                            {100.1, 0.1},
                                            {101.1, 0.1},
                                            {101.1, 1.1},
                                            {100.1, 1.1},
                                            {100.1, 0.1}
                                        })),
                        new LinearRing[] {
                            gf.createLinearRing(
                                    array(
                                            new double[][] {
                                                {100.2, 0.2},
                                                {100.8, 0.2},
                                                {100.8, 0.8},
                                                {100.2, 0.8},
                                                {100.2, 0.2}
                                            }))
                        });
        return poly;
    }

    Polygon polygon3() {
        Polygon poly;
        poly =
                gf.createPolygon(
                        gf.createLinearRing(
                                array(
                                        new double[][] {
                                            {100.1, 0.1, 10.2},
                                            {101.1, 0.1, 11.2},
                                            {101.1, 1.1, 11.2},
                                            {100.1, 1.1, 10.2},
                                            {100.1, 0.1, 10.2}
                                        })),
                        new LinearRing[] {
                            gf.createLinearRing(
                                    array(
                                            new double[][] {
                                                {100.2, 0.2, 10.2},
                                                {100.8, 0.2, 11.2},
                                                {100.8, 0.8, 11.2},
                                                {100.2, 0.8, 10.2},
                                                {100.2, 0.2, 10.2}
                                            }))
                        });
        return poly;
    }

    String polygonText3() {
        return strip(
                "{ 'type': 'Polygon',"
                        + "    'coordinates': ["
                        + "      [ [100.1, 0.1, 10.2], [101.1, 0.1, 11.2], [101.1, 1.1, 11.2], [100.1, 1.1, 10.2], [100.1, 0.1, 10.2] ],"
                        + "      [ [100.2, 0.2, 10.2], [100.8, 0.2, 11.2], [100.8, 0.8, 11.2], [100.2, 0.8, 10.2], [100.2, 0.2, 10.2] ]"
                        + "      ]"
                        + "   }");
    }

    String polygonText1() {
        return strip(
                "{ 'type': 'Polygon',"
                        + "'coordinates': ["
                        + "  [ [100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1] ]"
                        + "  ]"
                        + "}");
    }

    String badPolygonText1() {
        return strip(
                "{ 'type': 'Polygon',"
                        + "'coordinates': ["
                        + "  [ [100.1, 0.1], [101.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1] ]"
                        + "  ]"
                        + "}");
    }

    String polygonText2() {
        return strip(
                "{ 'type': 'Polygon',"
                        + "    'coordinates': ["
                        + "      [ [100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1] ],"
                        + "      [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]"
                        + "      ]"
                        + "   }");
    }

    Polygon polygon1() {
        Polygon poly =
                gf.createPolygon(
                        gf.createLinearRing(
                                array(
                                        new double[][] {
                                            {100.1, 0.1},
                                            {101.1, 0.1},
                                            {101.1, 1.1},
                                            {100.1, 1.1},
                                            {100.1, 0.1}
                                        })),
                        null);
        return poly;
    }

    public void testPolyRead() throws Exception {
        assertEquals(polygon1(), (gjson.readPolygon(reader(polygonText1()))));
        assertEquals(polygon2(), (gjson.readPolygon(reader(polygonText2()))));
        assertEquals(polygon3(), (gjson.readPolygon(reader(polygonText3()))));
        try {
            gjson.readPolygon(reader(badPolygonText1()));
            fail("Read bad polygon");
        } catch (Exception e) {
            // good
        }
    }

    public void testMultiPointWrite() throws Exception {
        assertEquals(multiPointText(), gjson.toString(multiPoint()));
        assertEquals(multiPoint3dText(), gjson.toString(multiPoint3d()));
    }

    String multiPointText() {
        return strip(
                "{ 'type': 'MultiPoint'," + "'coordinates': [ [100.1, 0.1], [101.1, 1.1] ]" + "}");
    }

    MultiPoint multiPoint() {
        MultiPoint mpoint = gf.createMultiPoint(array(new double[][] {{100.1, 0.1}, {101.1, 1.1}}));
        return mpoint;
    }

    String multiPoint3dText() {
        return strip(
                "{ 'type': 'MultiPoint',"
                        + "'coordinates': [ [100.1, 0.1, 10.2], [101.1, 1.1, 11.2] ]"
                        + "}");
    }

    MultiPoint multiPoint3d() {
        MultiPoint mpoint =
                gf.createMultiPoint(array(new double[][] {{100.1, 0.1, 10.2}, {101.1, 1.1, 11.2}}));
        return mpoint;
    }

    public void testMultiPointRead() throws Exception {
        assertTrue(multiPoint().equals(gjson.readMultiPoint(reader(multiPointText()))));
        assertTrue(multiPoint3d().equals(gjson.readMultiPoint(reader(multiPoint3dText()))));
    }

    public void testMultiLineWrite() throws Exception {
        assertEquals(multiLineText(), gjson.toString(multiLine()));
        assertEquals(multiLine3dText(), gjson.toString(multiLine3d()));
    }

    String multiLineText() {
        return strip(
                "{ 'type': 'MultiLineString',"
                        + "    'coordinates': ["
                        + "        [ [100.1, 0.1], [101.1, 1.1] ],"
                        + "        [ [102.1, 2.1], [103.1, 3.1] ]"
                        + "      ]"
                        + "    }");
    }

    MultiLineString multiLine() {
        MultiLineString mline =
                gf.createMultiLineString(
                        new LineString[] {
                            gf.createLineString(array(new double[][] {{100.1, 0.1}, {101.1, 1.1}})),
                            gf.createLineString(array(new double[][] {{102.1, 2.1}, {103.1, 3.1}}))
                        });
        return mline;
    }

    String multiLine3dText() {
        return strip(
                "{ 'type': 'MultiLineString',"
                        + "    'coordinates': ["
                        + "        [ [100.1, 0.1, 10.2], [101.1, 1.1, 10.2] ],"
                        + "        [ [102.1, 2.1, 11.2], [103.1, 3.1, 11.2] ]"
                        + "      ]"
                        + "    }");
    }

    MultiLineString multiLine3d() {
        MultiLineString mline =
                gf.createMultiLineString(
                        new LineString[] {
                            gf.createLineString(
                                    array(new double[][] {{100.1, 0.1, 10.2}, {101.1, 1.1, 10.2}})),
                            gf.createLineString(
                                    array(new double[][] {{102.1, 2.1, 11.2}, {103.1, 3.1, 11.2}}))
                        });
        return mline;
    }

    public void testMultiLineRead() throws Exception {
        assertTrue(multiLine().equals(gjson.readMultiLine(reader(multiLineText()))));
        assertTrue(multiLine3d().equals(gjson.readMultiLine(reader(multiLine3dText()))));
    }

    public void testMultiPolygonWrite() throws Exception {
        assertEquals(multiPolygonText(), gjson.toString(multiPolygon()));
        assertEquals(multiPolygon3dText(), gjson.toString(multiPolygon3d()));
    }

    String multiPolygonText() {
        return strip(
                "{ 'type': 'MultiPolygon',"
                        + "    'coordinates': ["
                        + "      [[[102.1, 2.1], [103.1, 2.1], [103.1, 3.1], [102.1, 3.1], [102.1, 2.1]]],"
                        + "      [[[100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1]],"
                        + "       [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]"
                        + "      ]"
                        + "    }");
    }

    MultiPolygon multiPolygon() {
        MultiPolygon mpoly =
                gf.createMultiPolygon(
                        new Polygon[] {
                            gf.createPolygon(
                                    gf.createLinearRing(
                                            array(
                                                    new double[][] {
                                                        {102.1, 2.1},
                                                        {103.1, 2.1},
                                                        {103.1, 3.1},
                                                        {102.1, 3.1},
                                                        {102.1, 2.1}
                                                    })),
                                    null),
                            gf.createPolygon(
                                    gf.createLinearRing(
                                            array(
                                                    new double[][] {
                                                        {100.1, 0.1},
                                                        {101.1, 0.1},
                                                        {101.1, 1.1},
                                                        {100.1, 1.1},
                                                        {100.1, 0.1}
                                                    })),
                                    new LinearRing[] {
                                        gf.createLinearRing(
                                                array(
                                                        new double[][] {
                                                            {100.2, 0.2},
                                                            {100.8, 0.2},
                                                            {100.8, 0.8},
                                                            {100.2, 0.8},
                                                            {100.2, 0.2}
                                                        }))
                                    })
                        });
        return mpoly;
    }

    String multiPolygon3dText() {
        return strip(
                "{ 'type': 'MultiPolygon',"
                        + "    'coordinates': ["
                        + "      [[[102.1, 2.1, 10.2], [103.1, 2.1, 10.2], [103.1, 3.1, 10.2], [102.1, 3.1, 10.2], [102.1, 2.1, 10.2]]],"
                        + "      [[[100.1, 0.1, 10.2], [101.1, 0.1, 10.2], [101.1, 1.1, 10.2], [100.1, 1.1, 10.2], [100.1, 0.1, 10.2]],"
                        + "       [[100.2, 0.2, 10.2], [100.8, 0.2, 10.2], [100.8, 0.8, 10.2], [100.2, 0.8, 10.2], [100.2, 0.2, 10.2]]]"
                        + "      ]"
                        + "    }");
    }

    MultiPolygon multiPolygon3d() {
        MultiPolygon mpoly =
                gf.createMultiPolygon(
                        new Polygon[] {
                            gf.createPolygon(
                                    gf.createLinearRing(
                                            array(
                                                    new double[][] {
                                                        {102.1, 2.1, 10.2},
                                                        {103.1, 2.1, 10.2},
                                                        {103.1, 3.1, 10.2},
                                                        {102.1, 3.1, 10.2},
                                                        {102.1, 2.1, 10.2}
                                                    })),
                                    null),
                            gf.createPolygon(
                                    gf.createLinearRing(
                                            array(
                                                    new double[][] {
                                                        {100.1, 0.1, 10.2},
                                                        {101.1, 0.1, 10.2},
                                                        {101.1, 1.1, 10.2},
                                                        {100.1, 1.1, 10.2},
                                                        {100.1, 0.1, 10.2}
                                                    })),
                                    new LinearRing[] {
                                        gf.createLinearRing(
                                                array(
                                                        new double[][] {
                                                            {100.2, 0.2, 10.2},
                                                            {100.8, 0.2, 10.2},
                                                            {100.8, 0.8, 10.2},
                                                            {100.2, 0.8, 10.2},
                                                            {100.2, 0.2, 10.2}
                                                        }))
                                    })
                        });
        return mpoly;
    }

    public void testMultiPolygonRead() throws IOException {
        assertTrue(multiPolygon().equals(gjson.readMultiPolygon(reader(multiPolygonText()))));
        assertTrue(multiPolygon3d().equals(gjson.readMultiPolygon(reader(multiPolygon3dText()))));
    }

    public void testGeometryCollectionWrite() throws Exception {
        assertEquals(collectionText(), gjson.toString(collection()));
        assertEquals(collection3dText(), gjson.toString(collection3d()));
    }

    private String collectionText() {
        return strip(
                "{ 'type': 'GeometryCollection',"
                        + "    'geometries': ["
                        + "      { 'type': 'Point',"
                        + "        'coordinates': [100.1, 0.1]"
                        + "        },"
                        + "      { 'type': 'LineString',"
                        + "        'coordinates': [ [101.1, 0.1], [102.1, 1.1] ]"
                        + "        }"
                        + "    ]"
                        + "  }");
    }

    private String collectionTypeLastText() {
        return strip(
                "{ "
                        + "    'geometries': ["
                        + "      { 'type': 'Point',"
                        + "        'coordinates': [100.1, 0.1]"
                        + "        },"
                        + "      { 'type': 'LineString',"
                        + "        'coordinates': [ [101.1, 0.1], [102.1, 1.1] ]"
                        + "        }"
                        + "    ], "
                        + "    'type': 'GeometryCollection'"
                        + "  }");
    }

    GeometryCollection collection() {
        GeometryCollection gcol =
                gf.createGeometryCollection(
                        new Geometry[] {
                            gf.createPoint(new Coordinate(100.1, 0.1)),
                            gf.createLineString(array(new double[][] {{101.1, 0.1}, {102.1, 1.1}}))
                        });
        return gcol;
    }

    private String collection3dText() {
        return strip(
                "{ 'type': 'GeometryCollection',"
                        + "    'geometries': ["
                        + "      { 'type': 'Point',"
                        + "        'coordinates': [100.1, 0.1, 10.2]"
                        + "        },"
                        + "      { 'type': 'LineString',"
                        + "        'coordinates': [ [101.1, 0.1, 10.2], [102.1, 1.1, 11.2] ]"
                        + "        }"
                        + "    ]"
                        + "  }");
    }

    GeometryCollection collection3d() {
        GeometryCollection gcol =
                gf.createGeometryCollection(
                        new Geometry[] {
                            gf.createPoint(new Coordinate(100.1, 0.1, 10.2)),
                            gf.createLineString(
                                    array(new double[][] {{101.1, 0.1, 10.2}, {102.1, 1.1, 11.2}}))
                        });
        return gcol;
    }

    public void testGeometryCollectionRead() throws Exception {
        assertEqual(
                collection(),
                (GeometryCollection) gjson.readGeometryCollection(reader(collectionText())));
        assertEqual(
                collection3d(),
                (GeometryCollection) gjson.readGeometryCollection(reader(collection3dText())));
    }

    public void testRead() throws Exception {
        assertTrue(point().equals(gjson.read(reader(pointText()))));
        assertTrue(point3d().equals(gjson.read(reader(point3dText()))));
        assertTrue(line().equals(gjson.read(reader(lineText()))));
        assertTrue(line3d().equals(gjson.read(reader(line3dText()))));
        assertTrue(polygon1().equals(gjson.read(reader(polygonText1()))));
        assertTrue(polygon2().equals(gjson.read(reader(polygonText2()))));
        assertTrue(polygon3().equals(gjson.read(reader(polygonText3()))));
        assertTrue(multiPoint().equals(gjson.read(reader(multiPointText()))));
        assertTrue(multiPoint3d().equals(gjson.read(reader(multiPoint3dText()))));
        assertTrue(multiLine().equals(gjson.read(reader(multiLineText()))));
        assertTrue(multiLine3d().equals(gjson.read(reader(multiLine3dText()))));
        assertTrue(multiPolygon().equals(gjson.read(reader(multiPolygonText()))));
        assertTrue(multiPolygon3d().equals(gjson.read(reader(multiPolygon3dText()))));

        assertEqual(collection(), (GeometryCollection) gjson.read(reader(collectionText())));
        assertEqual(collection3d(), (GeometryCollection) gjson.read(reader(collection3dText())));
    }

    public void testReadOrder() throws Exception {
        String json = strip("{'coordinates':[100.1,0.1], 'type': 'Point'}");
        assertTrue(point().equals(gjson.read(reader(json))));

        json = strip("{'coordinates': [[100.1,0.1],[101.1,1.1]], 'type': 'LineString'}");
        assertTrue(line().equals(gjson.read(reader(json))));

        json =
                strip(
                        "{ 'coordinates': ["
                                + "      [ [100.1, 0.1, 10.2], [101.1, 0.1, 11.2], [101.1, 1.1, 11.2], [100.1, 1.1, 10.2], [100.1, 0.1, 10.2] ],"
                                + "      [ [100.2, 0.2, 10.2], [100.8, 0.2, 11.2], [100.8, 0.8, 11.2], [100.2, 0.8, 10.2], [100.2, 0.2, 10.2] ]"
                                + "      ]"
                                + ", 'type': 'Polygon' }");
        assertTrue(polygon3().equals(gjson.read(reader(json))));

        json = strip("{ 'coordinates': [ [100.1, 0.1], [101.1, 1.1] ], 'type': 'MultiPoint'}");
        assertTrue(multiPoint().equals(gjson.read(reader(json))));

        json =
                strip(
                        "{ 'coordinates': ["
                                + "        [ [100.1, 0.1], [101.1, 1.1] ],"
                                + "        [ [102.1, 2.1], [103.1, 3.1] ]"
                                + "      ]"
                                + "    , 'type': 'MultiLineString'}");
        assertTrue(multiLine().equals(gjson.read(reader(json))));

        json =
                strip(
                        "{ 'coordinates': ["
                                + "      [[[102.1, 2.1], [103.1, 2.1], [103.1, 3.1], [102.1, 3.1], [102.1, 2.1]]],"
                                + "      [[[100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1]],"
                                + "       [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]"
                                + "      ]"
                                + "   , 'type': 'MultiPolygon' }");
        assertTrue(multiPolygon().equals(gjson.read(reader(json))));
    }

    void assertEqual(GeometryCollection col1, GeometryCollection col2) {
        assertEquals(col1.getNumGeometries(), col2.getNumGeometries());
        for (int i = 0; i < col1.getNumGeometries(); i++) {
            assertTrue(col1.getGeometryN(i).equals(col2.getGeometryN(i)));
        }
    }

    Coordinate[] array(double[][] coords) {
        Coordinate[] coordinates = new Coordinate[coords.length];
        for (int i = 0; i < coords.length; i++) {
            Coordinate c = new Coordinate(coords[i][0], coords[i][1]);
            if (coords[i].length > 2) {
                c.z = coords[i][2];
            }

            coordinates[i] = c;
        }
        return coordinates;
    }

    public void testGeometryCollectionReadTypeLast() throws IOException {
        Object obj = gjson.read(collectionTypeLastText());
        assertTrue(obj instanceof GeometryCollection);

        GeometryCollection gc = (GeometryCollection) obj;
        assertEquals(2, gc.getNumGeometries());

        assertTrue(gc.getGeometryN(0) instanceof Point);
        assertTrue(gc.getGeometryN(1) instanceof LineString);
    }

    public void testPointOrderParsing() throws Exception {
        String input1 = "{\n" + "  \"type\": \"Point\",\n" + "  \"coordinates\": [10, 10]\n" + "}";
        String input2 = "{\n" + "  \"coordinates\": [10, 10],\n" + "  \"type\": \"Point\"\n" + "}";
        org.geotools.geojson.geom.GeometryJSON geometryJSON =
                new org.geotools.geojson.geom.GeometryJSON();
        Point p1 = geometryJSON.readPoint(input1);
        assertEquals(10, p1.getX(), 0d);
        assertEquals(10, p1.getY(), 0d);
        Point p2 = geometryJSON.readPoint(input2);
        assertEquals(10, p2.getX(), 0d);
        assertEquals(10, p2.getY(), 0d);
    }

    public void testKeyOrderInGeometryCollectionParsing() throws Exception {
        /* Test parsing of two variations of the same GeoJSON object. */

        /* input1 tests parsing when "type" occurs at the top of each sub-object */
        String input1 =
                "{"
                        + " \"type\": \"GeometryCollection\","
                        + " \"geometries\": [{"
                        + "  \"type\": \"Polygon\","
                        + "  \"coordinates\": [[[100.0, 1.0],[101.0, 1.0],[100.5, 1.5],[100.0, 1.0]]]"
                        + "  }]"
                        + "}";

        /* input2 tests parsing when "type" in a geometry of the geom collection occurs after "coordinates" */
        String input2 =
                "{"
                        + " \"type\": \"GeometryCollection\","
                        + " \"geometries\": [{"
                        + "  \"coordinates\": [[[100.0, 1.0],[101.0, 1.0],[100.5, 1.5],[100.0, 1.0]]],"
                        + "  \"type\": \"Polygon\""
                        + " }]"
                        + "}";

        /* input3 tests parsing when  "type" of the geometry collection occurs after "geometries" */
        String input3 =
                "{"
                        + " \"geometries\": [{"
                        + "  \"coordinates\": [[[100.0, 1.0],[101.0, 1.0],[100.5, 1.5],[100.0, 1.0]]],"
                        + "  \"type\": \"Polygon\""
                        + " }],"
                        + " \"type\": \"GeometryCollection\""
                        + "}";

        Point expectedLastPoint = gf.createPoint(new Coordinate(100.0, 1.0));

        org.geotools.geojson.geom.GeometryJSON geometryJSON =
                new org.geotools.geojson.geom.GeometryJSON();

        /* test input 1 */
        GeometryCollection collection = geometryJSON.readGeometryCollection(input1);
        testKeyOrderInGeometryCollectionParsing_VerifyContents(collection, expectedLastPoint);

        /* test input 2 */
        collection = geometryJSON.readGeometryCollection(input2);
        testKeyOrderInGeometryCollectionParsing_VerifyContents(collection, expectedLastPoint);

        /* test input 3 */
        collection = geometryJSON.readGeometryCollection(input3);
        testKeyOrderInGeometryCollectionParsing_VerifyContents(collection, expectedLastPoint);
    }

    /*
     * Helper function that specifically supports test case testKeyOrderInGeometryCollectionParsing
     */
    private final void testKeyOrderInGeometryCollectionParsing_VerifyContents(
            GeometryCollection collection, Point expectedLastPoint) {
        assertNotNull(collection);
        assertNotNull(expectedLastPoint);
        assertEquals(1, collection.getNumGeometries());
        Object geomObj = collection.getGeometryN(0);
        assertTrue(geomObj instanceof Polygon);
        Polygon polygon = (Polygon) geomObj;
        assertEquals(1, polygon.getNumGeometries(), 0d);
        assertEquals(0, polygon.getNumInteriorRing(), 0d);
        assertEquals(4, polygon.getNumPoints(), 0);
        LineString outerBoundary = polygon.getExteriorRing();
        assertEquals(4, outerBoundary.getNumPoints());
        Point lastPoint = outerBoundary.getPointN(3);
        assertTrue(lastPoint.equalsExact(expectedLastPoint));
    }
}
