/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.kml.bindings;

import javax.xml.namespace.QName;

import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
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

public class MultiGeometryTypeBindingTest extends KMLTestSupport {

    public void testType() {
        assertEquals(GeometryCollection.class, binding(KML.MultiGeometryType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.MultiGeometryType).getExecutionMode());
    }

    public void testParseMultiPoint() throws Exception {
        String xml = "<MultiGeometry>" +  
            "<Point><coordinates>0,0</coordinates></Point>" + 
            "<Point><coordinates>1,1</coordinates></Point>" +
            "</MultiGeometry>";

        buildDocument(xml);

        MultiPoint mp = (MultiPoint) parse();
        assertEquals( 2, mp.getNumPoints() );
    }
    
    public void testEncodeMultiPoint() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        MultiPoint mp = gf.createMultiPoint(
            new Coordinate[]{ new Coordinate( 0, 0 ), new Coordinate(1,1) }
        );
        Document dom = encode( mp, KML.MultiGeometry );
        assertEquals( 2, getElementsByQName(dom, KML.Point ).getLength() );
    }
    
    public void testParseMultiLineString() throws Exception {
        String xml = "<MultiGeometry>" +  
            "<LineString><coordinates>0,0 1,1</coordinates></LineString>" + 
            "<LineString><coordinates>2,2 3,3</coordinates></LineString>" +
            "</MultiGeometry>";

        buildDocument(xml);

        MultiLineString ml = (MultiLineString) parse();
        assertEquals( 2, ml.getNumGeometries() );
    }
    
    public void testEncodeMultiLineString() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        MultiLineString ml = gf.createMultiLineString(
            new LineString[]{
                gf.createLineString( new Coordinate[] { new Coordinate( 0, 0), new Coordinate( 1, 1) } ),
                gf.createLineString( new Coordinate[] { new Coordinate( 2, 2), new Coordinate( 3, 3) } )
            }
        );
        Document dom = encode( ml, KML.MultiGeometry );
        assertEquals( 2, getElementsByQName(dom, KML.LineString ).getLength() );
    }
    
    public void testParseMultiPolygon() throws Exception {
        String xml = "<MultiGeometry>" +  
            "<Polygon>" +
            "<outerBoundaryIs><LinearRing><coordinates>0,0 1,1 2,2 0,0</coordinates></LinearRing></outerBoundaryIs></Polygon>" +
            "<Polygon>" +
            "<outerBoundaryIs><LinearRing><coordinates>0,0 1,1 2,2 0,0</coordinates></LinearRing></outerBoundaryIs></Polygon>" +
            "</MultiGeometry>";

        buildDocument(xml);

        MultiPolygon ml = (MultiPolygon) parse();
        assertEquals( 2, ml.getNumGeometries() );
    }
    
    public void testEncodeMultiPolygon() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Polygon p = gf.createPolygon(
            gf.createLinearRing( new Coordinate[] {
                new Coordinate(0,0), new Coordinate(1,1), new Coordinate(2,2), 
                new Coordinate(0,0)
            }), null
        );
        
        MultiPolygon mp = gf.createMultiPolygon(new Polygon[]{p,p});
        
        Document dom = encode( mp, KML.MultiGeometry );
        assertEquals( 2, getElementsByQName(dom, KML.Polygon ).getLength() );
    }
    
    public void testParseMultiGeometry() throws Exception {
        String xml = "<MultiGeometry>" +  
        "<Point><coordinates>0,0</coordinates></Point>" + 
        "<LineString><coordinates>0,0 1,1</coordinates></LineString>" +
        "<Polygon>" +
        "<outerBoundaryIs><LinearRing><coordinates>0,0 1,1 2,2 0,0</coordinates></LinearRing></outerBoundaryIs></Polygon>" +
        "</MultiGeometry>";
        buildDocument(xml);
        
        GeometryCollection gc = (GeometryCollection) parse();
        assertEquals( 3, gc.getNumGeometries() );
        assertTrue( gc.getGeometryN(0) instanceof Point );
        assertTrue( gc.getGeometryN(1) instanceof LineString );
        assertTrue( gc.getGeometryN(2) instanceof Polygon );
    }
    
    public void testEncodeMultiGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        GeometryCollection gc = gf.createGeometryCollection( 
            new Geometry[]{
                gf.createPoint( new Coordinate(0,0)), 
                gf.createLineString( new Coordinate[]{new Coordinate(0,0), new Coordinate(1,1)}),
                gf.createPolygon(
                    gf.createLinearRing( new Coordinate[] {
                        new Coordinate(0,0), new Coordinate(1,1), new Coordinate(2,2), 
                        new Coordinate(0,0)
                    }), null
                )    
            }
        );
        
        Document dom = encode(gc, KML.MultiGeometry );
        assertNotNull( getElementByQName(dom, KML.Point ) );
        assertNotNull( getElementByQName(dom, KML.LineString ) );
        assertNotNull( getElementByQName(dom, KML.Polygon ) );
         
    }
}
