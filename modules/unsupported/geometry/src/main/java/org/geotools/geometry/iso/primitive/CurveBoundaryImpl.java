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

import java.util.Set;

import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The boundary of Curves shall be represented as CurveBoundary.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 * @source $URL$
 */
public class CurveBoundaryImpl extends PrimitiveBoundaryImpl implements
		CurveBoundary {
    private static final long serialVersionUID = -3093563492516992268L;

    /**
	 * 
	 * startPoint, endPoint A CurveBoundary contains two Point references.
	 * 
	 * CurveBoundary::startPoint : Reference<Point>; CurveBoundary::endPoint :
	 * Reference<Point>;
	 * 
	 */
	private Point startPoint = null;

	private Point endPoint = null;

	/**
	 * Constructor
	 * 
	 * @param crs
	 * @param start
	 * @param end
	 */
	public CurveBoundaryImpl(CoordinateReferenceSystem crs, Point start, Point end) {
		super(crs);
		if (start.equals(end))
			throw new IllegalArgumentException("Start- and Endpoint of the CurveBoundary cannot be equal");
		this.startPoint = start;
		this.endPoint = end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#clone()
	 */
	public CurveBoundaryImpl clone() throws CloneNotSupportedException {
		// ok
		// Return new CurveBoundary with the cloned start and end point of this CurveBoundary
		return new CurveBoundaryImpl(this.getCoordinateReferenceSystem(), this.getStartPoint().clone(), this.getEndPoint().clone());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.primitive.CurveBoundary#getStartPoint()
	 */
	public PointImpl getStartPoint() {
		// TODO test
		// TODO documentation
		return (PointImpl) this.startPoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.primitive.CurveBoundary#getEndPoint()
	 */
	public PointImpl getEndPoint() {
		// TODO test
		// TODO documentation
		return (PointImpl) this.endPoint;
	}

	public String toString() {
		return "[CurveBoundary: StartPoint: " + this.startPoint + " EndPoint: " //$NON-NLS-1$//$NON-NLS-2$
				+ this.endPoint + "]"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getDimension(org.opengis.geometry.coordinate.DirectPosition)
	 */
	public int getDimension(final DirectPosition point) {
		// TODO semantic JR, SJ
		// TODO What is going to happen with the direct position?
		// The dimension of a CurveBoundary is the dimension of two Points. So we return the dimension value 0. 
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getEnvelope()
	 */
	public EnvelopeImpl getEnvelope() {
		// TODO Test
		/* Build Envelope with StartPoint */
		// EnvelopeImpl tmpEnv = new EnvelopeImpl(this.startPoint.getPosition(), this.startPoint.getPosition());
		
		EnvelopeImpl tmpEnv = new EnvelopeImpl(this.startPoint.getEnvelope());
		/* Extend Envelope with EndPoint */
		tmpEnv.expand(this.endPoint.getPosition().getCoordinates());
		return tmpEnv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.complex.ComplexImpl#createBoundary()
	 */
	public Set<Complex> createBoundary() {
		// TODO semantic JR, SJ
		// TODO implementation
		// TODO test
		// TODO documentation
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#isSimple()
	 */
	public boolean isSimple() {
		// A curveBoundary (start and end point) is always simple
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getRepresentativePoint()
	 */
	@Override
	public DirectPosition getRepresentativePoint() {
		// Use start point of Boundary as representative point
		return this.startPoint.getPosition();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((endPoint == null) ? 0 : endPoint.hashCode());
		result = PRIME * result + ((startPoint == null) ? 0 : startPoint.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CurveBoundaryImpl other = (CurveBoundaryImpl) obj;
		if (endPoint == null) {
			if (other.endPoint != null)
				return false;
		} else if (!endPoint.equals(other.endPoint))
			return false;
		if (startPoint == null) {
			if (other.startPoint != null)
				return false;
		} else if (!startPoint.equals(other.startPoint))
			return false;
		return true;
	}

}
