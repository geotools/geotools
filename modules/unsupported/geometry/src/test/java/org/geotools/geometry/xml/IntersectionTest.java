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
package org.geotools.geometry.xml;

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.Surface;
import org.opengis.referencing.FactoryException;

public class IntersectionTest extends AbstractGeometryTest {

    /**
     * Prepare the test environment
     */
    public void setUp() throws FactoryException {
        super.setUp();
    }
    public void testNotYet(){        
    }
    
    /**
     * test the simple intersection of two polygons
     */
    public void XtestSimpleIntersection() {
        DirectPosition[] pointsA = new DirectPosition[4];
        pointsA[0] = createDirectPosition(0.0, 0.0);
        pointsA[1] = createDirectPosition(1.0, 0.0);
        pointsA[2] = createDirectPosition(0.0, 1.0);
        pointsA[3] = createDirectPosition(0.0, 0.0);

        DirectPosition[] pointsB = new DirectPosition[4];
        pointsB[0] = createDirectPosition(0.0, 0.0);
        pointsB[1] = createDirectPosition(1.0, 0.0);
        pointsB[2] = createDirectPosition(1.0, 1.0);
        pointsB[3] = createDirectPosition(0.0, 0.0);

        Surface sA = createSurface(pointsA);
        assertEquals(0.5, sA.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sA.getPerimeter());

        Surface sB = createSurface(pointsB);
        assertEquals(0.5, sB.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sB.getPerimeter());

        TransfiniteSet result = sA.intersection(sB);
        assertTrue(result instanceof Surface);
        Surface surfaceResult = (Surface)result;
        assertEquals(1.0 + Math.sqrt(2.0), surfaceResult.getPerimeter());
    }

    public void XtestEdgeIntersection() {
        DirectPosition[] pointsA = new DirectPosition[4];
        pointsA[0] = createDirectPosition(0.0, 0.0);
        pointsA[1] = createDirectPosition(1.0, 0.0);
        pointsA[2] = createDirectPosition(0.0, 1.0);
        pointsA[3] = createDirectPosition(0.0, 0.0);

        DirectPosition[] pointsB = new DirectPosition[4];
        pointsB[0] = createDirectPosition(1.0, 0.0);
        pointsB[1] = createDirectPosition(1.0, 1.0);
        pointsB[2] = createDirectPosition(0.0, 1.0);
        pointsB[3] = createDirectPosition(1.0, 0.0);

        Surface sA = createSurface(pointsA);
        assertEquals(0.5, sA.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sA.getPerimeter());

        Surface sB = createSurface(pointsB);
        assertEquals(0.5, sB.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sB.getPerimeter());

        TransfiniteSet result = sA.intersection(sB);
        assertTrue(result instanceof Curve);
        Curve curveResult = (Curve)result;
        assertEquals(0.0, curveResult.getStartParam(), 1.0e-8);
        assertEquals(Math.sqrt(2.0), curveResult.getEndParam(), 1.0e-8);
    }

}
