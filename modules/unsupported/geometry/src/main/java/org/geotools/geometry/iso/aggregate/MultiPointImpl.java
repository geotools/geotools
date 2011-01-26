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

import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author roehrig
 * 
 *
 * @source $URL$
 */
public class MultiPointImpl extends MultiPrimitiveImpl implements MultiPoint {
    private static final long serialVersionUID = -1251341764107281100L;

    /**
	 * Creates a MultiPoint by a set of Points.
	 * @param crs
	 * @param points Set of Points which shall be contained by the MultiPoint
	 */
	public MultiPointImpl(CoordinateReferenceSystem crs, Set<Point> points) {
		super(crs, points);
	}

	
	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.aggregate.MultiPrimitiveImpl#getElements()
	 */
	@SuppressWarnings("unchecked")
    public Set<Point> getElements() {
		//return (Set<Point>) super.elements;
	    return Collections.checkedSet( (Set<Point>) super.elements, Point.class );
	}

}
