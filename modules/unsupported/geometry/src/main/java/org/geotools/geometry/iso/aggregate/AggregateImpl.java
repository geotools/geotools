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

import java.util.Set;

import org.geotools.geometry.iso.root.GeometryImpl;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.aggregate.Aggregate;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author roehrig
 * @param <T>
 * 
 *
 * @source $URL$
 */
public abstract class AggregateImpl extends GeometryImpl implements Aggregate {
	
	protected Set<? extends Geometry> elements = null;
	/**
	 * @param crs
	 */
	public AggregateImpl(CoordinateReferenceSystem crs, Set<? extends Geometry> elements) {
		super(crs);
		this.elements = elements;
	}

	public Set<? extends Geometry> getElements() {
	    return elements;
	}
	
	/* (non-Javadoc)
     * @see org.geotools.geometry.featgeom.root.GeometryImpl#clone()
     */
    public GeometryImpl clone() throws CloneNotSupportedException {
        return null;
    }
	/**
	 * Union the various elements together and return the result.
	 */
	public Boundary getBoundary() {
        Boundary bounds = null;
        for( Geometry geometry :  getElements() ){
            Boundary boundary = geometry.getBoundary();
            if (boundary != null) {
                if (bounds == null) {
                    bounds = boundary;
                }
                else {
                    bounds.union(boundary);
                }
            }
        }       
        return bounds;
    }
	
	/* (non-Javadoc)
     * @see org.geotools.geometry.featgeom.root.GeometryImpl#getDimension(org.opengis.geometry.coordinate.DirectPosition)
     */
    public int getDimension(DirectPosition point) {
        if (point != null) {
            return point.getDimension();
        }
        else {
            // return the largest dimension of all the contained elements in this collection
            int maxD = 0;
            for( Geometry geometry : getElements() ){
                maxD = Math.max( maxD, geometry.getDimension( null ));
            }
            return maxD;
        }
    }
}
