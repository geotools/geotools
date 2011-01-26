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
package org.geotools.geometry.iso.aggregate;

import java.util.Collections;
import java.util.Set;

import org.opengis.geometry.Boundary;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MultiSurfaceImpl extends MultiPrimitiveImpl implements MultiSurface {
    private static final long serialVersionUID = -8409899769039201012L;

    /**
	 * Creates a MultiSurface by a set of Curves.
	 * @param crs
	 * @param surfaces Set of Surfaces which shall be contained by the MultiSurface
	 */
	public MultiSurfaceImpl(CoordinateReferenceSystem crs, Set<OrientableSurface> surfaces) {
		super(crs, surfaces);
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.aggregate.MultiSurface#getArea()
	 */
	public double getArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.aggregate.MultiPrimitiveImpl#getElements()
	 */
	@SuppressWarnings("unchecked")
	public Set<OrientableSurface> getElements() {
		//return (Set<OrientableSurface>) super.elements;
        return Collections.checkedSet( (Set<OrientableSurface>) super.elements, OrientableSurface.class );
	    	    
	}

}
