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

import org.opengis.geometry.primitive.PrimitiveBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * PrimitiveBoundary The abstract class Primitive boundary is the root for the
 * various return types of the boundary operator for subtypes of Primitive.
 * Since points have no boundary, no special subclass is needed for their
 * boundary.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 * @source $URL$
 */
public abstract class PrimitiveBoundaryImpl extends BoundaryImpl implements
		PrimitiveBoundary {

	/**
	 * @param crs
	 */
	public PrimitiveBoundaryImpl(CoordinateReferenceSystem crs) {
		super(crs);
	}

}
