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
package org.geotools.geometry.iso.primitive;

import java.util.List;

import org.geotools.geometry.iso.complex.CompositeSurfaceImpl;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Shell;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * A Shell is used to represent a single connected component of a SolidBoundary.
 * It consists of a number of references to OrientableSurfaces connected in a
 * topological cycle (an object whose boundary is empty). Unlike a Ring, a
 * Shell's elements have no natural sort order. Like Rings, Shells are simple.
 * Shell: {isSimple() = TRUE}
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 * @source $URL$
 */
public class ShellImpl extends CompositeSurfaceImpl implements Shell {

	/**
	 * @param crs
	 * @param generator
	 */
	public ShellImpl(CoordinateReferenceSystem crs,
			List<OrientableSurface> generator) {
		super(generator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.complex.CompositeSurfaceImpl#isSimple()
	 */
	public boolean isSimple() {
		// Implementation ok
		// Shells are always simple
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.complex.CompositeSurfaceImpl#isCycle()
	 */
	public boolean isCycle() {
		// Implementation ok
		// Shells are always simple
		return true;
	}

}
