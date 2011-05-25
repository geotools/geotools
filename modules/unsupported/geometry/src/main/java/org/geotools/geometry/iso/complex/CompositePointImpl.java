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
package org.geotools.geometry.iso.complex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.io.GeometryToString;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A separate class for composite point, CompositePoint (Figure 27) is included
 * for completeness. It is a Complex containing one and only one Point.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 * @source $URL$
 */
public class CompositePointImpl extends CompositeImpl<PointImpl> implements CompositePoint {
    private static final long serialVersionUID = 3391515492110694489L;

    public CompositePointImpl(PointImpl generator) {
		this( generator.getCoordinateReferenceSystem(), generator );
	}
	/**
	 * The association role Composition::generator associates this Composite
	 * Point to the single primitive in this complex. CompositePoint::generator
	 * [1] : Point
	 * 
	 * The generator is realised by the element ArrayList of the super class
	 * Complex and will be passed through the super constructor
	 * 
	 * @param crs
	 * @param generator
	 */
	public CompositePointImpl(CoordinateReferenceSystem crs, PointImpl generator) {
		/* Call super constructor; elements will be set later */
		super(crs);
		List<PointImpl> list = new ArrayList<PointImpl>();
		list.add(generator);
		this.setElements(list);
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#clone()
	 */
	public CompositePointImpl clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.ComplexImpl#createBoundary()
	 */
	public Set<Complex> createBoundary() {
		// Return null, because a point doesn´t have a boundary
		return null;
	}


	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.CompositeImpl#getGeneratorClass()
	 */
	public Class getGeneratorClass() {
		return PointImpl.class;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.root.Geometry#isSimple()
	 */
	public boolean isSimple() {
		// a point is always simple
		return true;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.root.Geometry#isCycle()
	 */
	public boolean isCycle() {
		// A point is always a cicle
		return true;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.complex.Composite#getGenerators()
	 */
	public List getGenerators() {
		return this.elements;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getBoundary()
	 */
	public Boundary getBoundary() {
		// a point doesn´t have a boundary
		return null;
	}


	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getDimension(org.opengis.geometry.coordinate.DirectPosition)
	 */
	@Override
	public int getDimension(DirectPosition point) {
		// TODO Auto-generated method stub
		return this.getCoordinateDimension();
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getEnvelope()
	 */
	public Envelope getEnvelope() {
		// OK
		return this.elements.get(0).getEnvelope();
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getRepresentativePoint()
	 */
	public DirectPosition getRepresentativePoint() {
		// Return the point which defines this CompositePoint
		return ((PointImpl)this.elements.get(0)).getPosition();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return GeometryToString.getString(this);
	}

}
