/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;



/**
 * A test for the MathTransformBuilders.
 *
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 * @author Adrian Custer
 */
public final class MathTransformBuilderTest {
    /**
     * Coordinates List generator.
     *
     * @param numberOfVertices count of generated points
     * @param seed for random generating.
     *
     * @return points
     */
    private List<MappedPosition> generateCoords(int numberOfVertices, long seed) {
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;
        return generateCoordsWithCRS(numberOfVertices, crs, seed, true);
    }

    /**
     * Coordinates List generator.
     *
     * @param numberOfVertices count of generated points
     * @param seed for random generating.
     * @param includeAccuracy set true to generate points with accuracy.
     */
    private List<MappedPosition> generateCoords(int numberOfVertices, long seed,
        boolean includeAccuracy) {
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;
        return generateCoordsWithCRS(numberOfVertices, crs, seed, includeAccuracy);
    }

    /**
     * Coordinates List generator.
     *
     * @param numberOfVertices count of generated points
     * @param crs Coordinate Reference System of generated points
     * @param seed seed for generate random numbers
     * @param includeAccuracy set true to generate points with accuracy.
     */
    private List <MappedPosition> generateCoordsWithCRS(int numberOfVertices,
        CoordinateReferenceSystem crs, long seed, boolean includeAccuracy) {
        List <MappedPosition>vert = new ArrayList<MappedPosition>();
        Random randomCoord = new Random(seed);
        for (int i = 0; i < numberOfVertices; i++) {
            double xs = randomCoord.nextDouble() * 1000;
            double ys = randomCoord.nextDouble() * 1000;
            double xd = randomCoord.nextDouble() * 1000;
            double yd = randomCoord.nextDouble() * 1000;
            MappedPosition p = new MappedPosition(new DirectPosition2D(crs, xs,
                        ys), new DirectPosition2D(crs, xd, yd));
            if (includeAccuracy) {
                p.setAccuracy(randomCoord.nextDouble());
            }
            vert.add(p);
        }
        return vert;
    }

    /**
     * Test expected values against transformed values.
     *
     * @param mt mathTransform that will be tested
     * @param pts MappedPositions of source and target values.
     */
    private void transformTest(MathTransform mt, List<MappedPosition> pts)
            throws FactoryException, TransformException
    {
        double[] points = new double[pts.size() * 2];
        double[] ptCalculated = new double[pts.size() * 2];
        for (int i = 0; i < pts.size(); i++) {
            points[(2 * i)    ] = pts.get(i).getSource().getCoordinates()[0];
            points[(2 * i) + 1] = pts.get(i).getSource().getCoordinates()[1];
        }
        mt.transform(points, 0, ptCalculated, 0, pts.size());
        for (int i = 0; i < pts.size(); i++) {
            assertEquals(pts.get(i).getTarget().getCoordinates()[0], ptCalculated[2 * i],       0.001);
            assertEquals(pts.get(i).getTarget().getCoordinates()[1], ptCalculated[(2 * i) + 1], 0.001);
        }
    }

  
    @Test    
    public void testNoSkewAffineBuilder() throws FactoryException, TransformException {
        List<MappedPosition> pts = generateCoords(6, 1245);    
        AdvancedAffineBuilder aab = new AdvancedAffineBuilder(pts);         
        aab.setConstrain(AdvancedAffineBuilder.SXY, 0);              
        AffineToGeometric a2g = new AffineToGeometric((AffineTransform2D)aab.getMathTransform());            
        Assert.assertEquals(a2g.getSkew(), 0, 0.0000000001);         
        }
}
