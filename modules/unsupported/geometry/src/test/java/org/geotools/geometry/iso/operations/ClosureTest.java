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
package org.geotools.geometry.iso.operations;

import junit.framework.TestCase;

import org.geotools.geometry.iso.io.wkt.ParseException;
import org.geotools.geometry.iso.io.wkt.WKTReader;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ClosureTest extends TestCase {

	//private FeatGeomFactoryImpl factory = null;
	private CoordinateReferenceSystem crs = null;

	public void testMain() {
		
		//this.factory = FeatGeomFactoryImpl.getDefault2D();
		this.crs = DefaultGeographicCRS.WGS84;
		
		// Test Curves
		this._testAll();
	}
	
	private void _testAll() {
		
		
		// Point
		CompositePoint cp = (CompositePoint) this.createPoint().getClosure();
		//System.out.println(cp);
		
		// Curve
		CompositeCurve cc = (CompositeCurve) this.createCurve().getClosure();
		//System.out.println(cc);
		
		// Surface
		CompositeSurface cs = (CompositeSurface) this.createSurface().getClosure();
		//System.out.println(cs);

		
		// Complexes
		CompositePoint ncp = (CompositePoint) cp.getClosure();
		assertTrue(ncp == cp);
		//System.out.println(ncp);

		CompositeCurve ncc = (CompositeCurve) cc.getClosure();
		assertTrue(ncc == cc);
		//System.out.println(ncc);
		
		CompositeSurface ncs = (CompositeSurface) cs.getClosure();
		assertTrue(ncs == cs);
		//System.out.println(ncs);
		
		// Boundaries
		
		Complex c = null;
		Boundary b = null;

		b = this.createCurve().getBoundary();
		c = b.getClosure();
		assertTrue(b == c);

		b = this.createSurface().getBoundary();
		c = b.getClosure();
		assertTrue(b == c);

	}
	


	
	private PointImpl createPointFromWKT(String aWKTpoint) {
		PointImpl rPoint = null;
		WKTReader wktReader = new WKTReader(this.crs);
		try {
			rPoint = (PointImpl) wktReader.read(aWKTpoint);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rPoint;
	}

	
	private CurveImpl createCurveFromWKT(String aWKTcurve) {
		CurveImpl rCurve = null;
		WKTReader wktReader = new WKTReader(this.crs);
		try {
			rCurve = (CurveImpl) wktReader.read(aWKTcurve);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rCurve;
	}
	
	private SurfaceImpl createSurfaceFromWKT(String aWKTsurface) {
		SurfaceImpl rSurface = null;
		WKTReader wktReader = new WKTReader(this.crs);
		try {
			rSurface = (SurfaceImpl) wktReader.read(aWKTsurface);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rSurface;
	}
	
	
	private PointImpl createPoint() {
		String wktPoint = "POINT(30 50)";
		return this.createPointFromWKT(wktPoint);
	}
	
	
	private CurveImpl createCurve() {
		String wktCurve1 = "CURVE(150.0 100.0, 160.0 140.0, 180.0 100.0, 170.0 120.0)";
		return this.createCurveFromWKT(wktCurve1);
	}
	
	private SurfaceImpl createSurface() {
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90))";
		return this.createSurfaceFromWKT(wktSurface1);
	}
	
	

}
