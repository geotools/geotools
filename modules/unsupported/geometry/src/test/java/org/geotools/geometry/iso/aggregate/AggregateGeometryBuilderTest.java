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
package org.geotools.geometry.iso.aggregate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class AggregateGeometryBuilderTest extends TestCase {

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
		System.out.println(primitiveFactory.getCoordinateReferenceSystem());
		Point point = primitiveFactory.createPoint(new double[] { 48.44, -123.37 });
		
		assertTrue(position.equals(point.getCentroid()));
		
		// change CRS and test
		builder.setCoordianteReferenceSystem(DefaultGeographicCRS.WGS84_3D);
		PrimitiveFactory primitiveFactory3D = builder.getPrimitiveFactory();
		Point point3D = primitiveFactory3D.createPoint(new double[] { 48.44, -123.37, 1.0 });
		
		assertFalse(point.getCoordinateReferenceSystem().equals(point3D.getCoordinateReferenceSystem()));
		assertFalse(point.equals(point3D));
		
		// back to 2D
		builder.setCoordianteReferenceSystem(DefaultGeographicCRS.WGS84);
		PositionFactory pf = builder.getPositionFactory();
		PrimitiveFactoryImpl primf = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		AggregateFactory agf = builder.getAggregateFactory();
		
		List<DirectPosition> directPositionList = new ArrayList<DirectPosition>();
		directPositionList.add(pf.createDirectPosition(new double[] {20, 10}));
		directPositionList.add(pf.createDirectPosition(new double[] {40, 10}));
		directPositionList.add(pf.createDirectPosition(new double[] {50, 40}));
		directPositionList.add(pf.createDirectPosition(new double[] {30, 50}));
		directPositionList.add(pf.createDirectPosition(new double[] {10, 30}));
		directPositionList.add(pf.createDirectPosition(new double[] {20, 10}));

		Ring exteriorRing = primf.createRingByDirectPositions(directPositionList);
		List<Ring> interiors = new ArrayList<Ring>();
		
		SurfaceBoundary surfaceBoundary1 = primf.createSurfaceBoundary(exteriorRing, interiors );
		Surface surface = primf.createSurface(surfaceBoundary1);
		
		Set<OrientableSurface> surfaces = new HashSet<OrientableSurface>();
		surfaces.add(surface);
		MultiSurface ms = agf.createMultiSurface(surfaces);
		//System.out.println(ms);
		//System.out.println(ms.getBoundary());
		//assertNotNull(ms.getBoundary());
		
		// test equals
		assertTrue(ms.equals(new MultiSurfaceImpl(ms.getCoordinateReferenceSystem(), surfaces)));
		
	}
}
