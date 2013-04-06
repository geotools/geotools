/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.projection;

import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.SEMI_MAJOR;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.SEMI_MINOR;
import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.junit.Test;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;


/**
 * Tests the {@link MapProjection} implementation.
 *
 * @source $URL$
 * @version $Id$
 * @author Frank Warmerdam
 */
public final class MapProjectionTest {
	
    /**
     * Make a simple spherical Mercator (Google Mercator) CRS.  We just use this as 
     * a simple example of a MapProjection since that class is abstract.
     */
	private static MapProjection createGoogleMercator() throws FactoryException {
        MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup parameters = mtFactory.getDefaultParameters("Mercator_1SP");
        parameters.parameter(SEMI_MAJOR.getName().getCode()).setValue(6378137.0);
        parameters.parameter(SEMI_MINOR.getName().getCode()).setValue(6378137.0);
        return (MapProjection) mtFactory.createParameterizedTransform(parameters);
	}
 	
    /**
     * Sets of geographic coordinates to project.
     */
    private static final double[] GEOGRAPHIC = {
    	47.0, -14.0,
    	48.38824840214492, -14.967538330290973,
    };

    /**
     * Set of projected coordinates.
     */
    private static final double[] PROJECTED = {
    	5232016.067283858, -1574216.548161465,
    	5386555.1725052055, -1685459.3322153771,
   };

    /**
     * Test we can round trip well behaved points, and that checkReciprocal() works properly.
     */
    @Test
    public void testCheckReciprocal() throws TransformException, FactoryException {
        final double[] dst = new double[PROJECTED.length];
        MapProjection mt = createGoogleMercator();
        mt.transform(GEOGRAPHIC, 0, dst, 0, PROJECTED.length/2);

        for (int i=0; i<PROJECTED.length; i++) {
           assertEquals(PROJECTED[i], dst[i], 0.1);   // 10 cm precision
        }
        
        for (int i=0; i < PROJECTED.length/2; i++) {
        	Point2D src = new Point2D.Double(GEOGRAPHIC[i*2+0], GEOGRAPHIC[i*2+1]);
        	Point2D target = new Point2D.Double(PROJECTED[i*2+0], PROJECTED[i*2+1]);
        	assertEquals(true, mt.checkReciprocal(src, target, false));
        	assertEquals(true, mt.checkReciprocal(target, src, true));
        }

        mt.inverse().transform(PROJECTED, 0, dst, 0, PROJECTED.length/2);
        for (int i=0; i<GEOGRAPHIC.length; i++) {
            assertEquals(GEOGRAPHIC[i], dst[i], 0.0001); // About 10 m precision
        }
    }

    /**
     * Test that orthodromicDistance() works well for small and large distances.
     */
    @Test
    public void testOrthodromicDistance() throws FactoryException {
        MapProjection mt = createGoogleMercator();
        
        // Test some large distances
		assertEquals(111319.49079,
				mt.orthodromicDistance(new Point2D.Double(0.0, 0.0),
				new Point2D.Double(0.0, 1.0)), 0.001);
		assertEquals(111319.49079,
				mt.orthodromicDistance(new Point2D.Double(0.0, 0.0),
				new Point2D.Double(1.0, 0.0)), 0.001);
		assertEquals(111319.49079,
				mt.orthodromicDistance(new Point2D.Double(0.0, 89.0),
				new Point2D.Double(0.0, 90.0)), 0.001);
		assertEquals(1942.76834,
				mt.orthodromicDistance(new Point2D.Double(0.0, 89.0),
				new Point2D.Double(1.0, 89.0)), 0.001);
		assertEquals(10018754.17139,
				mt.orthodromicDistance(new Point2D.Double(0.0, 0.0),
				new Point2D.Double(0.0, 90.0)), 0.001);
		
    	// Test some small distances.
    	Point2D src = new Point2D.Double(48.38824840214492, -14.967538330290973);
    	assertEquals(0.0, mt.orthodromicDistance(src, src), 0.000000001);

    	Point2D target = new Point2D.Double(src.getX(), src.getY()+0.0000001);
    	assertEquals(0.011131948840096939, mt.orthodromicDistance(src, target), 1E-12);
    	
    	Point2D target2 = new Point2D.Double(src.getX(), src.getY()+0.000000000001);
    	assertEquals(1.1117412E-7, mt.orthodromicDistance(src, target2), 1E-12);
    }
}
