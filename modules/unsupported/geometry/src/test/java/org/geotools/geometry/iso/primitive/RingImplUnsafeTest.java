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

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.referencing.operation.TransformException;

import junit.framework.TestCase;

public class RingImplUnsafeTest extends TestCase {
	
		public void testRingImpls() {
			
			Hints hints1 = GeoTools.getDefaultHints();
	        hints1.put(Hints.CRS, DefaultGeographicCRS.WGS84 );
	        hints1.put(Hints.GEOMETRY_VALIDATE, true);
			GeometryBuilder builder_validate = new GeometryBuilder(hints1);
			
			Hints hints2 = GeoTools.getDefaultHints();
	        hints2.put(Hints.CRS, DefaultGeographicCRS.WGS84 );
	        hints2.put(Hints.GEOMETRY_VALIDATE, false);
			GeometryBuilder builder_novalid = new GeometryBuilder(hints2);
			
			Ring validated = createRing(builder_validate);
			Ring not_validated = createRing(builder_novalid);
			
			//System.out.println(validated);
			//System.out.println(not_validated);
			
			assertTrue(validated instanceof RingImpl);
			assertTrue(not_validated instanceof RingImplUnsafe);
		}
		
		public void testSurfaceImpl() {
			
			Hints hints1 = GeoTools.getDefaultHints();
	        hints1.put(Hints.CRS, DefaultGeographicCRS.WGS84 );
	        hints1.put(Hints.GEOMETRY_VALIDATE, true);
			GeometryBuilder builder_validate = new GeometryBuilder(hints1);
			
			Hints hints2 = GeoTools.getDefaultHints();
	        hints2.put(Hints.CRS, DefaultGeographicCRS.WGS84 );
	        hints2.put(Hints.GEOMETRY_VALIDATE, false);
			GeometryBuilder builder_novalid = new GeometryBuilder(hints2);
			
			Surface validated = createSurface(builder_validate);
			Surface not_validated = createSurface(builder_novalid);
			
			//System.out.println(validated);
			//System.out.println(not_validated);
			
			assertTrue(validated instanceof SurfaceImpl);
			assertTrue(not_validated instanceof SurfaceImpl);
			
			// test transform
			try {
				Surface v2 = (Surface) validated.transform(DefaultGeographicCRS.WGS84);
			} catch (TransformException e) {
				assertTrue("Validated Surface transform failed", false);
			}
			
			// test transform
			try {
				Surface nv2 = (Surface) not_validated.transform(DefaultGeographicCRS.WGS84);
			} catch (TransformException e) {
				assertTrue("Unvalidated Surface transform failed", false);
			}
		}
		
		public Surface createSurface(GeometryBuilder builder) {
			
			GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
			PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();

			List<DirectPosition> directPositionList = new ArrayList<DirectPosition>();
			directPositionList.add(tCoordFactory.createDirectPosition(new double[] {20, 10}));
			directPositionList.add(tCoordFactory.createDirectPosition(new double[] {40, 10}));
			directPositionList.add(tCoordFactory.createDirectPosition(new double[] {50, 40}));
			directPositionList.add(tCoordFactory.createDirectPosition(new double[] {30, 50}));
			directPositionList.add(tCoordFactory.createDirectPosition(new double[] {10, 30}));
			directPositionList.add(tCoordFactory.createDirectPosition(new double[] {20, 10}));

			Ring exteriorRing = (Ring) tPrimFactory.createRingByDirectPositions(directPositionList);
			List<Ring> interiors = new ArrayList<Ring>();
			
			SurfaceBoundaryImpl surfaceBoundary1 = tPrimFactory.createSurfaceBoundary(exteriorRing, interiors );
			
			Surface surface2 = tPrimFactory.createSurface(surfaceBoundary1);
			
			return surface2;
		}

		public Ring createRing(GeometryBuilder builder) {
			
			GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
			PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
			
			/* Defining Positions for LineStrings */
			ArrayList<Position> line1 = new ArrayList<Position>();
			line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50, 20})));
			line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30, 30})));
			line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 50})));
			line1.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 70})));

			ArrayList<Position> line2 = new ArrayList<Position>();
			line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{20, 70})));
			line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{40, 80})));
			line2.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{70, 80})));

			ArrayList<Position> line3 = new ArrayList<Position>();
			line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{70, 80})));
			line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{90, 70})));
			line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 60})));
			line3.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 40})));

			ArrayList<Position> line4 = new ArrayList<Position>();
			line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{100, 40})));
			line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{80, 30})));
			line4.add(new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50, 20})));
			
			/* Setting up Array of these LineStrings */
			ArrayList<CurveSegment> tLineList1 = new ArrayList<CurveSegment>();
			tLineList1.add(tCoordFactory.createLineString(line1));
			tLineList1.add(tCoordFactory.createLineString(line2)); 

			ArrayList<CurveSegment> tLineList2 = new ArrayList<CurveSegment>();
			tLineList2.add(tCoordFactory.createLineString(line3)); 
			tLineList2.add(tCoordFactory.createLineString(line4)); 

			/* Build Curve */
			CurveImpl curve1 = tPrimFactory.createCurve(tLineList1);
			CurveImpl curve2 = tPrimFactory.createCurve(tLineList2);

			
			/* Build Ring */
			ArrayList<OrientableCurve> curveList = new ArrayList<OrientableCurve>();
			curveList.add(curve1);
			curveList.add(curve2);
			
			Ring ring1 = tPrimFactory.createRing(curveList);

			return ring1;
		}
}
