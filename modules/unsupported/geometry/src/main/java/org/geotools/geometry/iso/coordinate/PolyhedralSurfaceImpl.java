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

import java.util.List;

import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * A PolyhedralSurface (Figure 21) is a Surface composed of polygon surfaces
 * (Polygon) connected along their common boundary curves. This differs from
 * Surface only in the restriction on the types of surface patches acceptable.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 * @source $URL$
 */
public class PolyhedralSurfaceImpl extends SurfaceImpl implements
		PolyhedralSurface {

	/**
	 * The constructor for a PolyhedralSurface takes the facet Polygons and
	 * creates the necessary aggregate surface.
	 * 
	 * PolyhedralSurface::PolyhedralSurface(tiles[1..n]: Polygon ) :
	 * PolyhedralSurface
	 * 
	 * @param crs
	 * @param tiles
	 */
	public PolyhedralSurfaceImpl(CoordinateReferenceSystem crs,
			List<Polygon> tiles) {
		super(crs, tiles);

	}

	/**
	 * @param factory
	 * @param boundary
	 */
	public PolyhedralSurfaceImpl(SurfaceBoundaryImpl boundary) {
		super(boundary);
	}
	
    /* (non-Javadoc)
     * @see org.geotools.geometry.iso.primitive.SurfaceImpl#getPatches()
     */
    public List<? extends Polygon> getPatches() {
    	return (List<? extends Polygon>) this.patch;
    }


	// /**
	// * Constructor without arguments Surface Polygons has to be setted later
	// * @param factory
	// */
	// public PolyhedralSurfaceImpl(GeometryFactoryImpl factory) {
	// super(factory);
	// }


}
