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

import org.geotools.geometry.iso.io.wkt.ParseException;
import org.geotools.geometry.iso.io.wkt.WKTReader;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;

import org.geotools.geometry.visualization.PaintGMObject;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class DisplayGeometry {
	
	public static void main(String[] args) {
		//FeatGeomFactoryImpl tGeomFactory = FeatGeomFactoryImpl.getDefault2D();
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		
		DisplayGeometry d = new DisplayGeometry();
		
		SurfaceImpl s1 = null;
		SurfaceImpl s2 = null;
		
		s1= d.createSurfaceAwithTwoHoles(crs);
		draw(s1);

		s2= d.createSurfaceBwithHole(crs);
		draw(s2);

		GeometryImpl g = null;
		g =	(GeometryImpl) s1.difference(s2);
		draw(g);

		g = (GeometryImpl) s1.symmetricDifference(s2);
		draw(g);
		
		g = (GeometryImpl) s1.union(s2);
		draw(g);
		

	}
	
	public static void draw(GeometryImpl g) {
		PaintGMObject.paint(g);
	}

	
	
	private SurfaceImpl createSurfaceAwithoutHole(CoordinateReferenceSystem crs) {
		SurfaceImpl rSurface = null;
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}
	
	private SurfaceImpl createSurfaceAwithHole(CoordinateReferenceSystem crs) {
		SurfaceImpl rSurface = null;
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}

	private SurfaceImpl createSurfaceAwithTwoHoles(CoordinateReferenceSystem crs) {
		SurfaceImpl rSurface = null;
		String wktSurface1 = "SURFACE ((10 90, 30 50, 70 30, 120 40, 150 70, 150 120, 100 150, 30 140, 10 90), (90 60, 110 100, 120 90, 100 60, 90 60), (30 100, 30 120, 50 120, 50 100, 30 100))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}

	private SurfaceImpl createSurfaceBwithoutHole(CoordinateReferenceSystem crs) {
		SurfaceImpl rSurface = null;
		// Clockwise oriented
		String wktSurface1 = "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}

	private SurfaceImpl createSurfaceBwithHole(CoordinateReferenceSystem crs) {
		SurfaceImpl rSurface = null;
		// Clockwise oriented
		String wktSurface1 = "SURFACE ((100 10, 70 50, 90 100, 160 140, 200 90, 170 20, 100 10), (120 30, 110 50, 120 80, 170 80, 160 40, 120 30))";
		return this.createSurfaceFromWKT(crs, wktSurface1);
	}
	
	private SurfaceImpl createSurfaceFromWKT(CoordinateReferenceSystem crs, String aWKTsurface) {
		SurfaceImpl rSurface = null;
		WKTReader wktReader = new WKTReader(crs);
		try {
			rSurface = (SurfaceImpl) wktReader.read(aWKTsurface);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rSurface;
	}
	
	
}
