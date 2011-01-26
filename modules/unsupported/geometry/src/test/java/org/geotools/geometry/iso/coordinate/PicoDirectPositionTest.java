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
package org.geotools.geometry.iso.coordinate;

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.util.Cloneable;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Precision;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import junit.framework.TestCase;

public class PicoDirectPositionTest extends TestCase {

	PicoContainer c;
	
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        c = container( DefaultGeographicCRS.WGS84_3D );       
    }
    
	/**
	 * Creates a pico container that knows about all the geom factories
	 * @param crs
	 * @return container
	 */
	protected PicoContainer container( CoordinateReferenceSystem crs ){
		
		DefaultPicoContainer container = new DefaultPicoContainer(); // parent
		
		// Teach Container about Factory Implementations we want to use
		container.registerComponentImplementation(PositionFactoryImpl.class);
		container.registerComponentImplementation(AggregateFactoryImpl.class);
		container.registerComponentImplementation(ComplexFactoryImpl.class);
		container.registerComponentImplementation(GeometryFactoryImpl.class);
		container.registerComponentImplementation(CollectionFactoryMemoryImpl.class);
		container.registerComponentImplementation(PrimitiveFactoryImpl.class);
		container.registerComponentImplementation(Geo2DFactory.class);
		
		// Teach Container about other dependacies needed
		container.registerComponentInstance( crs );
		Precision pr = new PrecisionModel();
		container.registerComponentInstance( pr );
		
		return container;		
	}

	public void testDirectPosition() {
		double x1 = 10000.00;
		double y1 = 10015.50;
		double z1 = 10031.00;
		
		double coords1[] = new double[]{x1, y1, z1};
		double coords2[] = new double[]{x1, y1};
		double resultCoords[];
		
		// Creating a DP
		GeometryFactoryImpl gf = (GeometryFactoryImpl) c.getComponentInstanceOfType(GeometryFactory.class);
		DirectPosition dp1 = gf.createDirectPosition(coords1);
		
		// getCoordinates()
		resultCoords = dp1.getCoordinates();
		assertTrue(coords1[0] == resultCoords[0]);
		assertTrue(coords1[1] == resultCoords[1]);
		assertTrue(coords1[2] == resultCoords[2]);
		
		// getOrdinate(dim)
		assertTrue(coords1[0] == dp1.getOrdinate(0));
		assertTrue(coords1[1] == dp1.getOrdinate(1));
		assertTrue(coords1[2] == dp1.getOrdinate(2));
		
		// Cloning a DP
		DirectPosition dp2 = (DirectPosition) ((Cloneable) dp1).clone();
		
		// setOrdinate(dim, value)
		dp1.setOrdinate(0, 10.5);
		dp1.setOrdinate(1, 20.7);
		dp1.setOrdinate(2, -30.666);
		resultCoords = dp1.getCoordinates();
		assertTrue(resultCoords[0] == 10.5);
		assertTrue(resultCoords[1] == 20.7);
		assertTrue(resultCoords[2] == -30.666);
		
		// Test if clone() returned a copy, and not a reference
		// The values of dp2 should not be modified by the previous setOrdinate call in dp1
		resultCoords = dp2.getCoordinates();
		assertTrue(x1 == resultCoords[0]);
		assertTrue(y1 == resultCoords[1]);
		assertTrue(z1 == resultCoords[2]);

		//DirectPosition dp3 = aCoordFactory.createDirectPosition(coords2);
		
		assertTrue(dp1.getDimension() == 3);
		
	}
}
