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
package org.geotools.geometry.iso.primitive;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;

public class PrimitiveGeometryBuilderTest extends TestCase {

	CoordinateReferenceSystem crs_WGS84;
	GeometryBuilder builder;
	
	public void setUp() {
		crs_WGS84 = DefaultGeographicCRS.WGS84;
		builder = new GeometryBuilder(crs_WGS84); 
	}
	
	public void testBuildPoint() {
		
		// test positionfactory
		PositionFactory posFactory = builder.getPositionFactory();
		DirectPosition position = posFactory.createDirectPosition(new double[] { 48.44, -123.37 });
		
		// test primitivefactory
		PrimitiveFactory primitiveFactory = builder.getPrimitiveFactory();
		Point point = primitiveFactory.createPoint(new double[] { 48.44, -123.37 });
		
		assertTrue(position.equals(point.getCentroid()));
		
		// change CRS and test
		builder.setCoordianteReferenceSystem(DefaultGeographicCRS.WGS84_3D);
		primitiveFactory = builder.getPrimitiveFactory();
		Point point3D = primitiveFactory.createPoint(new double[] { 48.44, -123.37, 1.0 });
		
		assertFalse(point.getCoordinateReferenceSystem().equals(point3D.getCoordinateReferenceSystem()));
		assertFalse(point.equals(point3D));
	}
}
