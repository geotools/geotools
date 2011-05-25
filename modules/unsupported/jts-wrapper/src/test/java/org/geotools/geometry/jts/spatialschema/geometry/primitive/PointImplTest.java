/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import org.geotools.geometry.jts.spatialschema.PositionFactoryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.GeometryFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import junit.framework.TestCase;

/**
 * @author gdavis
 * 
 *
 *
 * @source $URL$
 */
public class PointImplTest extends TestCase {

	public void testNewEmptyPoint() {
		Point point = new PointImpl();
		assertNotNull(point.getCoordinateReferenceSystem());
		DirectPosition position = point.getPosition();
		assertNotNull(position);
	}

	public void testNewPointHere() {
		DirectPosition here = new DirectPositionImpl(DefaultGeographicCRS.WGS84);
		here.setOrdinate(0, 48.44);
		here.setOrdinate(1, -123.37); // 48.44,-123.37

		Point point = new PointImpl(here);
		assertNotNull(point.getCoordinateReferenceSystem());
		assertEquals(here.getCoordinateReferenceSystem(), point
				.getCoordinateReferenceSystem());
		assertEquals(here, point.getPosition());
		assertEquals(here.hashCode(), point.getPosition().hashCode());
	}

	public void testNewFactoryPointHere() {
		PositionFactory gFact = new PositionFactoryImpl(
				DefaultGeographicCRS.WGS84);
		double[] ords = { 48.44, -123.37 };
		DirectPosition here = gFact.createDirectPosition(ords);

		Point point = new PointImpl(here);
		assertNotNull(point.getCoordinateReferenceSystem());
		assertEquals(here.getCoordinateReferenceSystem(), point
				.getCoordinateReferenceSystem());
		assertEquals(here, point.getPosition());
		assertEquals(here.hashCode(), point.getPosition().hashCode());
	}

	public void testPicoStuff() {
		DefaultPicoContainer container = new DefaultPicoContainer(); // parent

		// Teach Container about Factory Implementations we want to use
		container.registerComponentImplementation(PositionFactoryImpl.class);
		container.registerComponentImplementation(PrimitiveFactoryImpl.class);
		container.registerComponentImplementation(GeometryFactoryImpl.class);
		
		// Confirm Container cannot create anything yet
		assertNull(container
				.getComponentInstanceOfType(CoordinateReferenceSystem.class));
		try {
			container.getComponentInstanceOfType(PositionFactory.class);
			//fail("We should not be able to make a position factory yet - we do not have a CRS");
			// we need to work with out a crs on the grounds that FactorySPI
			// has to be able to find our class :-(
		} catch (Exception expected) {
		}
		// let's provide a CRS now and confirm everything works
		container.registerComponentInstance(DefaultGeographicCRS.WGS84_3D);

		PositionFactory positionFactory =
			(PositionFactory) container.getComponentInstanceOfType(PositionFactory.class);
		
		assertSame(DefaultGeographicCRS.WGS84_3D, positionFactory.getCoordinateReferenceSystem());
	}
	
	/**
	 * Now that we understand containers let's start testing stuff ...
	 * @param crs
	 * @return container
	 */
	protected PicoContainer container( CoordinateReferenceSystem crs ){
		DefaultPicoContainer container = new DefaultPicoContainer(); // parent
		
		container.registerComponentImplementation(PositionFactoryImpl.class);
		container.registerComponentImplementation(PrimitiveFactoryImpl.class);
		container.registerComponentImplementation(GeometryFactoryImpl.class);
		container.registerComponentInstance( crs );
		
		return container;		
	}
	
	public void testWSG84Point(){
		PicoContainer c = container( DefaultGeographicCRS.WGS84 );
		
		// Do actually test stuff
		
		double[] ords = { 48.44, -123.37 };
		PositionFactory factory = (PositionFactory) c.getComponentInstanceOfType( PositionFactory.class );
		
		assertNotNull(factory);
		DirectPosition here = factory.createDirectPosition(ords);
		Point point = new PointImpl(here);
		assertNotNull(point.getCoordinateReferenceSystem());
		assertEquals(here.getCoordinateReferenceSystem(), point
				.getCoordinateReferenceSystem());
		assertEquals(here, point.getPosition());
		assertEquals(here.hashCode(), point.getPosition().hashCode());
	}
	
	public void testWSG843DPoint(){
		PicoContainer c = container( DefaultGeographicCRS.WGS84_3D );
		
		// Do actually test stuff
		
		double[] ords = { 48.44, -123.37, 0.0 };
		PositionFactory factory = (PositionFactory) c.getComponentInstanceOfType( PositionFactory.class );
		
		assertNotNull(factory);
		DirectPosition here = factory.createDirectPosition(ords);
		Point point = new PointImpl(here);
		assertNotNull(point.getCoordinateReferenceSystem());
		assertEquals(here.getCoordinateReferenceSystem(), point
				.getCoordinateReferenceSystem());
		assertEquals(here, point.getPosition());
		assertEquals(here.hashCode(), point.getPosition().hashCode());
	}
	
}
