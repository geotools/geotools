/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry;

import org.opengis.referencing.FactoryException;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.Surface;

/**
 * @author Jody Garnett
 * @author Joel Skelton
 *
 *
 * @source $URL$
 */
public class IntersectionTest extends AbstractGeometryTest {

    /**
     * Prepare the test environment
     */
    public void setUp() throws FactoryException {
        super.setUp();
    }

    /**
     * test the simple intersection of two polygons
     */
    public void testSimpleIntersection() {
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
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sA.getPerimeter(), 0.0);

        Surface sB = createSurface(pointsB);
        assertEquals(0.5, sB.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sB.getPerimeter(), 0.0);

        TransfiniteSet result = sA.intersection(sB);
        assertTrue(result instanceof Surface);
        Surface surfaceResult = (Surface)result;
        assertEquals(1.0 + Math.sqrt(2.0), surfaceResult.getPerimeter(), 0.0);
    }

    public void testEdgeIntersection() {
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
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sA.getPerimeter(), 0.0);

        Surface sB = createSurface(pointsB);
        assertEquals(0.5, sB.getArea(), 1.0e-8);
        assertEquals(1.0 + 1.0 + Math.sqrt(2.0), sB.getPerimeter(), 0.0);

        TransfiniteSet result = sA.intersection(sB);
        assertTrue(result instanceof Curve);
        Curve curveResult = (Curve)result;
        assertEquals(0.0, curveResult.getStartParam(), 1.0e-8);
        assertEquals(Math.sqrt(2.0), curveResult.getEndParam(), 1.0e-8);
    }

}
