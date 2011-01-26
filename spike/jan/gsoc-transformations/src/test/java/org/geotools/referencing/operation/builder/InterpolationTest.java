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

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.Hints;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.operation.builder.algorithm.TPSInterpolation;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class InterpolationTest extends TestCase {

	public InterpolationTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
    public void testInterpoaltion(){
    	
    	 BufferedImage bimage = new BufferedImage(100, 100, BufferedImage.TYPE_USHORT_GRAY);
    	 bimage.setRGB(4, 3, 4);
    	 GridCoverageFactory p = new GridCoverageFactory() ;
    	 
    	
    	float[][] g = new float[10][10];
    	g[0][0]=2;
    	CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;

    	// Define the Envelope for our work; this will be the bounds of the final interpolation
    	GeneralDirectPosition min = new GeneralDirectPosition( 0.0,   0.0 );
    	GeneralDirectPosition max = new GeneralDirectPosition( 100.0, 100.0 );
    	GeneralEnvelope env = new GeneralEnvelope(min, max);    
    	env.setCoordinateReferenceSystem(crs);
    	
    	Hints k;
    	GridCoverageFactory fac = new GridCoverageFactory();
    	 GridCoverage2D cov = fac.create("sd", g, env);
    	 
    	
    	// Generate some known points to root the interpolation
    	DirectPosition a = new DirectPosition2D(crs,10,10);
    	DirectPosition b = new DirectPosition2D(crs,80,80);
    	DirectPosition c = new DirectPosition2D(crs,10,90);
    	DirectPosition d = new DirectPosition2D(crs,80,10);

    	
    	// Define at each point the values to be interpolated; we do this in a HashMap
    	Map<DirectPosition, Float> pointsAndValues = new HashMap();
    	pointsAndValues.put(a,  new Float(6.5456));
    	pointsAndValues.put(b,  new Float(1.541906));
    	pointsAndValues.put(c,  new Float(-9.54456));
    	pointsAndValues.put(d,  new Float(7.2345));


    	//now we can construct the Interpolation Object
    	TPSInterpolation interp = new TPSInterpolation(pointsAndValues, 100, 100, env);


    	 // We can create and show a coverage image of the interpolation within the Envelope
    	GridCoverageFactory gcf = new GridCoverageFactory();
    	GridCoverage2D coverage = gcf.create("Intepolated Coverage",  interp.get2DGrid(), env);//.show();
    	
    	
    	//Assert.assertEquals( Double.parseDouble((coverage.evaluate(a, (Set)(new HashSet())).iterator().next()).toString()), pointsAndValues.get(a).floatValue(), 0.01);
    	Assert.assertEquals( coverage.evaluate(a, new float[1])[0], pointsAndValues.get(a).floatValue(), 0.01);
    	
    	
    	// We can also get interpolated value at any DirectPosition
    	float myValue = interp.getValue(a);
    	Assert.assertEquals( interp.getValue(a), pointsAndValues.get(a).floatValue(), 0.0);        
    	Assert.assertEquals( interp.getValue(b), pointsAndValues.get(b).floatValue(), 0.0);
    	Assert.assertEquals( interp.getValue(c), pointsAndValues.get(c).floatValue(), 0.0);
    	Assert.assertEquals( interp.getValue(d), pointsAndValues.get(d).floatValue(), 0.0);
       
    	
    }
    
    public static Test suite() {
        return new TestSuite(InterpolationTest.class);
    }
    
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
