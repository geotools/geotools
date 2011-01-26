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

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;

import org.geotools.geometry.visualization.PaintGMObject;
import org.geotools.referencing.crs.DefaultGeographicCRS;


public class DisplayCurve {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84_3D);
		
		//this._testTriangle1(tGeomFactory);
		_testRing1(builder);
		
	}

	private static void _testRing1(GeometryBuilder builder) {

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

		System.out.println(ring1);

		System.out.println(ring1.getEnvelope());
		
		PaintGMObject.paint(curve1);
	}

}
