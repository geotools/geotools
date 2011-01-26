/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeometryJSONTest extends GeoJSONTestSupport {

    GeometryFactory gf = new GeometryFactory();
    GeometryJSON gjson = new GeometryJSON();
    
    public void testPointWrite() throws Exception {
        assertEquals(pointText(), gjson.toString(point()));
    }

    String pointText() {
        return strip("{'type': 'Point','coordinates':[100.1,0.1]}");
    }

    Point point() {
        Point p = gf.createPoint(new Coordinate(100.1, 0.1));
        return p;
    }
    
    public void testPointRead() throws Exception {
        assertTrue(point().equals(gjson.readPoint(reader(pointText()))));
    }
    
    public void testLineWrite() throws Exception {
        assertEquals(lineText(), gjson.toString(line()));
    }

    String lineText() {
        return strip(
            "{'type': 'LineString', 'coordinates': [[100.1,0.1],[101.1,1.1]]}");
    }

    LineString line() {
        LineString l = gf.createLineString(array(new double[][]{{100.1, 0.1},{101.1,1.1}}));
        return l;
    }
    
    public void testLineRead() throws Exception {
        assertTrue(line().equals(gjson.readLine(reader(lineText()))));
    }
    
    public void testPolyWrite() throws Exception {
        assertEquals(polygonText1(), gjson.toString(polygon1()));
        assertEquals(polygonText2(), gjson.toString(polygon2()));
    }

    Polygon polygon2() {
        Polygon poly;
        poly = gf.createPolygon(gf.createLinearRing(
            array(new double[][]{ 
                {100.1, 0.1}, {101.1, 0.1}, {101.1, 1.1}, {100.1, 1.1}, {100.1, 0.1}})),
            new LinearRing[]{ gf.createLinearRing(array(new double[][]{
                {100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}))});
        return poly;
    }

    String polygonText1() {
        return strip("{ 'type': 'Polygon',"+
        "'coordinates': ["+
        "  [ [100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1] ]"+
        "  ]"+
         "}");
    }
    
    String polygonText2() {
        return strip("{ 'type': 'Polygon',"+
        "    'coordinates': ["+
        "      [ [100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1] ],"+
        "      [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]"+
        "      ]"+
        "   }");
    }

    Polygon polygon1() {
        Polygon poly = 
            gf.createPolygon(gf.createLinearRing(array(new double[][]{ 
                {100.1, 0.1}, {101.1, 0.1}, {101.1, 1.1}, {100.1, 1.1}, {100.1, 0.1}})), null);
        return poly;
    }
    
    public void testPolyRead() throws Exception {
        assertTrue(polygon1().equals(gjson.readPolygon(reader(polygonText1()))));
        assertTrue(polygon2().equals(gjson.readPolygon(reader(polygonText2()))));
    }
    
    public void testMultiPointWrite() throws Exception {
        assertEquals(multiPointText(), gjson.toString(multiPoint()));
    }

    String multiPointText() {
        return strip(
            "{ 'type': 'MultiPoint',"+
                "'coordinates': [ [100.1, 0.1], [101.1, 1.1] ]"+
            "}");
    }

    MultiPoint multiPoint() {
        MultiPoint mpoint = gf.createMultiPoint(array(new double[][]{{100.1, 0.1}, {101.1, 1.1}}));
        return mpoint;
    }
    
    public void testMultiPointRead() throws Exception {
        assertTrue(multiPoint().equals(gjson.readMultiPoint(reader(multiPointText()))));
    }
    
    public void testMultiLineWrite() throws Exception {
        assertEquals(multiLineText(), gjson.toString(multiLine()));
    }

    String multiLineText() {
        return strip(
            "{ 'type': 'MultiLineString',"+
            "    'coordinates': ["+
            "        [ [100.1, 0.1], [101.1, 1.1] ],"+
            "        [ [102.1, 2.1], [103.1, 3.1] ]"+
            "      ]"+
            "    }");
    }

    MultiLineString multiLine() {
        MultiLineString mline = gf.createMultiLineString(new LineString[]{
            gf.createLineString(array(new double[][]{{100.1, 0.1}, {101.1, 1.1}})), 
            gf.createLineString(array(new double[][]{{102.1, 2.1}, {103.1, 3.1}}))
        });
        return mline;
    }
    
    public void testMultiLineRead() throws Exception {
        assertTrue(multiLine().equals(gjson.readMultiLine(reader(multiLineText()))));
    }
    
    public void testMultiPolygonWrite() throws Exception {
        assertEquals(multiPolygonText(), gjson.toString(multiPolygon()));
    }

    String multiPolygonText() {
        return strip(
        "{ 'type': 'MultiPolygon',"+
        "    'coordinates': ["+
        "      [[[102.1, 2.1], [103.1, 2.1], [103.1, 3.1], [102.1, 3.1], [102.1, 2.1]]],"+
        "      [[[100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1]],"+
        "       [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]"+
        "      ]"+
        "    }");
    }

    MultiPolygon multiPolygon() {
        MultiPolygon mpoly = gf.createMultiPolygon(new Polygon[]{
            gf.createPolygon(gf.createLinearRing(
                array(new double[][]{{102.1, 2.1}, {103.1, 2.1}, {103.1, 3.1}, {102.1, 3.1}, {102.1, 2.1}})),null),
            gf.createPolygon(gf.createLinearRing(
                array(new double[][]{{100.1, 0.1}, {101.1, 0.1}, {101.1, 1.1}, {100.1, 1.1}, {100.1, 0.1}})), 
                new LinearRing[]{gf.createLinearRing(
                    array(new double[][]{{100.2, 0.2}, {100.8, 0.2}, {100.8, 0.8}, {100.2, 0.8}, {100.2, 0.2}}))})
        });
        return mpoly;
    }
    
    public void testMultiPolygonRead() throws IOException {
        assertTrue(multiPolygon().equals(gjson.readMultiPolygon(reader(multiPolygonText()))));
    }
    
    public void testGeometryCollectionWrite() throws Exception {
        assertEquals(collectionText(), gjson.toString(collection()));
    }

    private String collectionText() {
        return strip(
            "{ 'type': 'GeometryCollection',"+
            "    'geometries': ["+
            "      { 'type': 'Point',"+
            "        'coordinates': [100.1, 0.1]"+
            "        },"+
            "      { 'type': 'LineString',"+
            "        'coordinates': [ [101.1, 0.1], [102.1, 1.1] ]"+
            "        }"+
            "    ]"+
            "  }");
    }

    GeometryCollection collection() {
        GeometryCollection gcol = gf.createGeometryCollection(new Geometry[]{
           gf.createPoint(new Coordinate(100.1,0.1)), 
           gf.createLineString(array(new double[][]{{101.1, 0.1}, {102.1, 1.1}}))
        });
        return gcol;
    }
    
    public void testGeometryCollectionRead() throws Exception {
        assertEqual(collection(), 
            (GeometryCollection)gjson.readGeometryCollection(reader(collectionText())));
    }
    
    public void testRead() throws Exception {
        assertTrue(point().equals(gjson.read(reader(pointText()))));
        assertTrue(line().equals(gjson.read(reader(lineText()))));
        assertTrue(polygon1().equals(gjson.read(reader(polygonText1()))));
        assertTrue(polygon2().equals(gjson.read(reader(polygonText2()))));
        assertTrue(multiPoint().equals(gjson.read(reader(multiPointText()))));
        assertTrue(multiLine().equals(gjson.read(reader(multiLineText()))));
        assertTrue(multiPolygon().equals(gjson.read(reader(multiPolygonText()))));
        
        assertEqual(collection(), (GeometryCollection) gjson.read(reader(collectionText())));
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
}
