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

import org.geotools.geometry.iso.coordinate.PolygonImpl;
import org.geotools.geometry.iso.io.wkt.ParseException;
import org.geotools.geometry.iso.io.wkt.WKTReader;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;

public class UnionPolygonTest extends TestCase {

	private SurfaceImpl createPolygonFromWKT(CoordinateReferenceSystem crs, String aWKTpolygon) {
		SurfaceImpl surface = null;
		WKTReader wktReader = new WKTReader(crs);
		try {
			surface = (SurfaceImpl) wktReader.read(aWKTpolygon);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return surface;
	}
	
	private GeometryImpl _testUnion(GeometryImpl g1, GeometryImpl g2) {
		return (GeometryImpl) g1.union(g2);
	}
	
	public void testMain() {
		
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		String wktPoly1 = "POLYGON((0 0, 110 0, 110 60, 40 60, 180 140, 40 220, 110 260, 0 260, 0 0))";
		String wktPoly2 = "POLYGON((220 0, 110 0, 110 60, 180 60, 40 140, 180 220, 110 260, 220 260, 220 0))";
		String wktExpectedResult = "POLYGON((110 0, 0 0, 0 260, 110 260, 220 260, 220 0, 110 0), (110 260, 40 220, 110 180, 180 220, 110 260), (110 100, 40 60, 110 60, 180 60, 110 100))";
		SurfaceImpl poly1 = this.createPolygonFromWKT(crs, wktPoly1);
		SurfaceImpl poly2 = this.createPolygonFromWKT(crs, wktPoly2);
		SurfaceImpl expectedResult = this.createPolygonFromWKT(crs, wktExpectedResult);
		
		//GeometryImpl result = this._testUnion(poly1, poly2);
		//assertEquals(result, expectedResult);
	}
}
