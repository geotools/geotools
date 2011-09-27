/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Unit tests for the JTS utility class smooth method.
 * 
 * @author Michael Bedward
 *
 *
 * @source $URL$
 * @version $Id$
 * @since 2.8
 */
public class JTSSmoothTest extends JTSTestBase {
    private GeometryFactory factory;
    
    @Before
    public void setup() {
        factory = new GeometryFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void smoothWithNullGeometry() {
        JTS.smooth(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void smoothWithNullFactory() {
        LineString line = factory.createLineString(getLineCoords());
        JTS.smooth(line, 0, null);
    }

    @Test
    public void smoothPointReturnsSameObject() {
        Point point = factory.createPoint(new Coordinate());
        Geometry smoothed = JTS.smooth(point, 0);
        assertTrue(smoothed == point);
    }

    @Test
    public void smoothMultiPointReturnsSameObject() {
        Coordinate[] coords = getLineCoords();
        MultiPoint mpoint = factory.createMultiPoint(coords);
        Geometry smoothed = JTS.smooth(mpoint, 0);
        assertTrue(smoothed == mpoint);
    }

    @Test
    public void smoothLineString() {
        Coordinate[] coords = getLineCoords();
        LineString line = factory.createLineString(coords);
        Geometry smoothed = JTS.smooth(line, 0);
        assertTrue(smoothed instanceof LineString);
        
        CoordList list = new CoordList(smoothed.getCoordinates());
        assertTrue(list.containsAll(coords));
        
        Envelope lineEnv = line.getEnvelopeInternal();
        Envelope smoothEnv = smoothed.getEnvelopeInternal();
        assertTrue(smoothEnv.covers(lineEnv));
    }

    @Test
    public void smoothLinearRing() {
        Coordinate[] coords = getPolyCoords();
        LineString line = factory.createLinearRing(coords);
        Geometry smoothed = JTS.smooth(line, 0);
        assertTrue(smoothed instanceof LinearRing);
        
        CoordList list = new CoordList(smoothed.getCoordinates());
        assertTrue(list.containsAll(coords));
        
        Envelope lineEnv = line.getEnvelopeInternal();
        Envelope smoothEnv = smoothed.getEnvelopeInternal();
        assertTrue(smoothEnv.covers(lineEnv));
    }
    
    @Test
    public void smoothMultiLineString() {
        LineString[] lines = new LineString[3];
        lines[0] = factory.createLineString(getLineCoords(0));
        lines[1] = factory.createLineString(getLineCoords(10));
        lines[2] = factory.createLineString(getLineCoords(20));
        
        MultiLineString mls = factory.createMultiLineString(lines);
        Geometry smoothed = JTS.smooth(mls, 0);
        assertTrue(smoothed instanceof MultiLineString);
        assertEquals(3, smoothed.getNumGeometries());
        
        Envelope mlsEnv = mls.getEnvelopeInternal();
        Envelope smoothEnv = smoothed.getEnvelopeInternal();
        assertTrue(smoothEnv.covers(mlsEnv));
    }
    
    @Test
    public void smoothPolygon() {
        Coordinate[] coords = getPolyCoords();
        Polygon poly = factory.createPolygon( factory.createLinearRing(coords), null );
        Geometry smoothed = JTS.smooth(poly, 0);
        assertTrue(smoothed instanceof Polygon);
        
        CoordList list = new CoordList(smoothed.getCoordinates());
        assertTrue(list.containsAll(coords));
        
        Envelope polyEnv = poly.getEnvelopeInternal();
        Envelope smoothEnv = smoothed.getEnvelopeInternal();
        assertTrue(smoothEnv.covers(polyEnv));
    }
    
    @Test
    public void smoothMultiPolygon() {
        Polygon[] polys = new Polygon[3];
        polys[0] = factory.createPolygon(factory.createLinearRing(getPolyCoords(0)), null);
        polys[1] = factory.createPolygon(factory.createLinearRing(getPolyCoords(10)), null);
        polys[2] = factory.createPolygon(factory.createLinearRing(getPolyCoords(20)), null);
        
        MultiPolygon mp = factory.createMultiPolygon(polys);
        Geometry smoothed = JTS.smooth(mp, 0);
        assertTrue(smoothed instanceof MultiPolygon);
        assertEquals(3, smoothed.getNumGeometries());
        
        Envelope mpEnv = mp.getEnvelopeInternal();
        Envelope smoothEnv = smoothed.getEnvelopeInternal();
        assertTrue(smoothEnv.covers(mpEnv));
    }
    
    @Test
    public void smoothGeometryCollection() {
        Geometry[] geoms = new Geometry[3];
        geoms[0] = factory.createPoint(new Coordinate());
        geoms[1] = factory.createLineString(getLineCoords());
        geoms[2] = factory.createPolygon(factory.createLinearRing(getPolyCoords()), null);
        
        GeometryCollection gc = factory.createGeometryCollection(geoms);
        Geometry smoothed = JTS.smooth(gc, 0);
        assertTrue(smoothed instanceof GeometryCollection);
        assertEquals(3, smoothed.getNumGeometries());
        
        // Input point should have been returned
        assertTrue(geoms[0] == smoothed.getGeometryN(0));
        
        // Smoothed line
        Geometry g = smoothed.getGeometryN(1);
        assertTrue(g instanceof LineString);
        
        CoordList list = new CoordList(g.getCoordinates());
        assertTrue(list.containsAll(geoms[1].getCoordinates()));
        
        Envelope inEnv = geoms[1].getEnvelopeInternal();
        Envelope smoothEnv = g.getEnvelopeInternal();
        assertTrue(smoothEnv.covers(inEnv));
        
        // Smoothed polygon
        g = smoothed.getGeometryN(2);
        assertTrue(g instanceof Polygon);
        
        list = new CoordList(g.getCoordinates());
        assertTrue(list.containsAll(geoms[2].getCoordinates()));
        
        inEnv = geoms[2].getEnvelopeInternal();
        smoothEnv = g.getEnvelopeInternal();
        assertTrue(smoothEnv.covers(inEnv));
    }
    
}
