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
package org.geotools.geometry.iso.coordinate;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.Triangle;
import org.opengis.geometry.coordinate.TriangulatedSurface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * A TriangulatedSurface (Figure 21) is a PolyhedralSurface that is composed
 * only of triangles (Triangle). There is no restriction on how the
 * triangulation is derived.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 * @source $URL$
 */
public class TriangulatedSurfaceImpl extends PolyhedralSurfaceImpl implements
		TriangulatedSurface {

	/**
	 * Constructor
	 * 
	 * @param crs
	 * @param triangles
	 */
	public TriangulatedSurfaceImpl(CoordinateReferenceSystem crs,
			List<Polygon> triangles) {
		super(crs, triangles);
	}
	
	/**
	 *
	 */
	public TriangulatedSurfaceImpl() {
		super((SurfaceBoundaryImpl) null);
	}

	/**
	 * @param boundary
	 */
	public TriangulatedSurfaceImpl(SurfaceBoundaryImpl boundary) {
		super(boundary);
	}

	/**
	 * Sets the Triangles for the Triangulated Surface
	 * 
	 * @param triangles
	 * @param surfaceBoundary
	 */
	public void setTriangles(ArrayList<TriangleImpl> triangles) {
		super.setPatches(triangles);
		// JR eingefügt und aus den TIN Konstruktoren entfernt
		for (TriangleImpl triangle : triangles) {
			triangle.setAssociatedSurface(this);
		}
	}
	
    /* (non-Javadoc)
     * @see org.geotools.geometry.iso.primitive.SurfaceImpl#getPatches()
     */
    public List<Triangle> getPatches() {
    	return (List<Triangle>) this.patch;
    }

	
	
}
