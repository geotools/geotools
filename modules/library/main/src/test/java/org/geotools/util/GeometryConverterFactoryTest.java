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
package org.geotools.util;

import junit.framework.TestCase;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GeometryConverterFactoryTest extends TestCase {

	GeometryConverterFactory factory;
	
	protected void setUp() throws Exception {
		factory = new GeometryConverterFactory();
	}
	
	public void testEnvelopeToGeometry() throws Exception {
		Geometry geometry = (Geometry) factory.createConverter( Envelope.class, Geometry.class, null )
			.convert( new Envelope( new Coordinate( 0,0 ), new Coordinate( 1,1 ) ), Geometry.class );
		assertNotNull( geometry );
		assertTrue(
			new GeometryFactory().createPolygon(
				new GeometryFactory().createLinearRing( 
					new Coordinate[] {
						new Coordinate(0,0), new Coordinate(1,0),new Coordinate(1,1), 
						new Coordinate(0,1), new Coordinate(0,0)
					}
				), null
			).equals( geometry )	
		);
		
	}
	
	public void testGeometryToEnvelope() throws Exception {
		Envelope envelope = (Envelope) factory.createConverter( Geometry.class, Envelope.class, null ).convert( 
			new GeometryFactory().createPolygon(
				new GeometryFactory().createLinearRing( 
					new Coordinate[] {
						new Coordinate(0,0), new Coordinate(1,0),new Coordinate(1,1), 
						new Coordinate(0,1), new Coordinate(0,0)
					}
				), null
			), Envelope.class
		);
		
		assertEquals( new Envelope( new Coordinate( 0,0 ), new Coordinate( 1,1 ) ), envelope );
	}
	
	public void testStringToGeometry()  throws Exception {
		Geometry geometry = (Geometry) factory.createConverter( String.class, Geometry.class, null )
			.convert( "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", Geometry.class );
		assertNotNull( geometry );
		assertTrue(
			new GeometryFactory().createPolygon(
				new GeometryFactory().createLinearRing( 
					new Coordinate[] {
						new Coordinate(0,0), new Coordinate(1,0),new Coordinate(1,1), 
						new Coordinate(0,1), new Coordinate(0,0)
					}
				), null
			).equals( geometry )	
		);
	}
	
	public void testGeometryToString() throws Exception {
		String wkt = (String) factory.createConverter( Geometry.class, String.class, null ).convert( 
			new GeometryFactory().createPolygon(
				new GeometryFactory().createLinearRing( 
					new Coordinate[] {
						new Coordinate(0,0), new Coordinate(1,0),new Coordinate(1,1), 
						new Coordinate(0,1), new Coordinate(0,0)
					}
				), null
			), String.class
		);
		
		assertEquals( "POLYGON ((0 0, 1 0, 1 1, 0 1, 0 0))", wkt );
	}
}
