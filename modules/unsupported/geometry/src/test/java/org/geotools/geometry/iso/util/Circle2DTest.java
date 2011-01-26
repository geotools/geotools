/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util;

import java.awt.geom.Point2D;

import org.geotools.geometry.iso.util.elem2D.Circle2D;
import org.geotools.geometry.iso.util.elem2D.Node2D;

import junit.framework.TestCase;

public class Circle2DTest extends TestCase {

    public void testInstance() {
        Circle2D circle = new Circle2D(0.0, 0.0, 1.5);
        assertNotNull(circle);
    }
    
    public void testGetSet() {
        Circle2D circle = new Circle2D(10.0, 8.0, 5.0);
        assertNotNull(circle);
        assertEquals(10.0, circle.getX());
        assertEquals(8.0, circle.getY());
        assertEquals(5.0, circle.getRadius());

        circle.setX(9.0);
        assertEquals(9.0, circle.getX());
        assertEquals(8.0, circle.getY());
        assertEquals(5.0, circle.getRadius());

        circle.setY(7.0);
        assertEquals(9.0, circle.getX());
        assertEquals(7.0, circle.getY());
        assertEquals(5.0, circle.getRadius());
        
        circle.setRadius(4.0);
        assertEquals(9.0, circle.getX());
        assertEquals(7.0, circle.getY());
        assertEquals(4.0, circle.getRadius());
        
        circle.setValues(8.0, -6.0, 9.0);
        assertEquals(8.0, circle.getX());
        assertEquals(-6.0, circle.getY());
        assertEquals(9.0, circle.getRadius());
        
        assertEquals(8.0, circle.getCenter().getX());
        assertEquals(-6.0, circle.getCenter().getY());
        
        
    }
    
    public void testContains() {
        Circle2D circle = new Circle2D(100.0, 50.0, 25.0);
        assertNotNull(circle);
        
        assertFalse(circle.contains(new Node2D(0.0, 0.0)));
        assertFalse(circle.contains(new Node2D(-10.0, 90.0)));
        assertTrue(circle.contains(new Node2D(100.0, 50.0)));
        assertTrue(circle.contains(new Node2D(124.9, 49.9)));
        assertTrue(circle.contains(new Node2D(90.0, 60.0)));
        assertFalse(circle.contains(new Node2D(-1000.0, -900.0)));
    }
    
    public void test3PointConstructor() {
        Point2D pt1 = new Node2D(0.0, 0.0);
        Point2D pt2 = new Node2D(5.0, 0.0);
        Point2D pt3 = new Node2D(2.5, -2.0);
        Circle2D circle = new Circle2D(pt1, pt2, pt3);
        assertNotNull(circle);
        
        assertEquals("Circle2D: X:2.5 Y:0.5625 r:2.5625", circle.toString());
        assertEquals(5.125, circle.getRectangle().getWidth());
        assertEquals(5.125, circle.getRectangle().getHeight());
        assertEquals(2.5, circle.getCenter().getX());
        
        //3 vertical points (bad!)
        pt1 = new Node2D(0.0, 5.0);
        pt2 = new Node2D(0.0, 10.0);
        pt3 = new Node2D(0.0, 15.0);
        
        try {
            circle = new Circle2D(pt1, pt2, pt3);
            fail("should have thrown illegal argument exception");
        } catch (IllegalArgumentException e) {
        }
        
        pt1 = new Node2D(0.0, 5.0);
        pt2 = new Node2D(0.0, 10.0);
        pt3 = new Node2D(1.0, 15.0);

        circle = new Circle2D(pt1, pt2, pt3);
        assertNotNull(circle);
        assertEquals("Circle2D: X:25.5 Y:7.5 r:25.622255950637914", circle.toString());
    }
}
