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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.io.GeometryToString;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The boundary of Surfaces shall be represented as SurfaceBoundary.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 * @source $URL$
 */
public class SurfaceBoundaryImpl extends PrimitiveBoundaryImpl implements
		SurfaceBoundary {
    private static final long serialVersionUID = -1357747117053853492L;

    /**
	 * A SurfaceBoundary consists of some number of Rings, corresponding to the
	 * various components of its boundary. In the normal 2D case, one of these
	 * rings is distinguished as being the exterior boundary. In a general
	 * manifold this is not always possible, in which case all boundaries shall
	 * be listed as interior boundaries, and the exterior will be empty.
	 * 
	 * SurfaceBoundary::exterior[0,1] : Ring;
	 * 
	 * SurfaceBoundary::interior[0..n] : Ring;
	 * 
	 * NOTE The use of exterior and interior here is not intended to invoke the
	 * definitions of "interior" and "exterior" of geometric objects. The terms
	 * are in common usage, and reflect a linguistic metaphor that uses the same
	 * linguistic constructs for the concept of being inside an object to being
	 * inside a container. In normal mathematical terms, the exterior boundary
	 * is the one that appears in the Jordan Separation Theorem (Jordan Curve
	 * Theorem extended beyond 2D). The exterior boundary is the one that
	 * separates the surface (or solid in 3D) from infinite space. The interior
	 * boundaries separate the object at hand from other bounded objects. The
	 * uniqueness of the exterior comes from the uniqueness of unbounded space.
	 * Essentially, the Jordan Separation Theorem shows that normal 2D or 3D
	 * space separates into bounded and unbounded pieces by the insertion of a
	 * ring or shell, respectively. It goes beyond that, but this standard is
	 * restricted to at most 3 dimensions.
	 * 
	 * EXAMPLE 1 If the underlying manifold is an infinite cylinder, then two
	 * transverse cuts of the cylinder define a compact surface between the
	 * cuts, and two separate unbounded portions of the cylinders. In this case,
	 * either cut could reasonably be called exterior. In cases of such
	 * ambiguity, the standard chooses to list all boundaries in the "interior"
	 * set. The only guarantee of an exterior boundary being unique is in the
	 * 2-dimensional plane, E2.
	 * 
	 * EXAMPLE 2 Taking the equator of a sphere, and generating a 1 meter
	 * buffer, we have a surface with two isomorphic boundary components. There
	 * is no unbiased manner to distinguish one of these as an exterior.
	 * 
	 */
	private Ring exterior = null;

	private List<Ring> interior = null;

	/**
	 * 
	 * @param crs
	 * @param exterior
	 * @param interior
	 */
	public SurfaceBoundaryImpl(CoordinateReferenceSystem crs, Ring exterior,
			List<Ring> interior) {
		super(crs);
		// TODO The consisty need to checked: Interior rings cannot cross each other or the exterior ring
		this.exterior = exterior;
		this.interior = interior;
	}

//	/**
//	 * @param factory
//	 * @param patch
//	 */
//	public SurfaceBoundaryImpl(FeatGeomFactoryImpl factory,
//			ArrayList<? extends SurfacePatchImpl> patch) {
//		super(factory);
//		if (patch == null || patch.isEmpty()) {
//			assert (false);
//			throw new IllegalArgumentException("Constructor not implemented"); //$NON-NLS-1$
//		}
//		// The Exterior Ring of the Surfaceboundary should be calculated considering ALL patches!
//		this.exterior = patch.get(0).getBoundary().getExterior();
//		// this.interior = patch[0].boundary().getInterior();
//
//	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#clone()
	 */
	public SurfaceBoundaryImpl clone() throws CloneNotSupportedException {
		// Test OK		
		// Clone exterior ring and interior rings
		Ring newExterior = (Ring) this.getExterior().clone();
		List<Ring> newInteriors = new ArrayList<Ring>();
		Iterator<Ring> interiors = this.getInteriors().iterator();
		while (interiors.hasNext()) {
			newInteriors.add((Ring) interiors.next().clone());
		}
		// Use the cloned rings to create a new SurfaceBoundary
		return new SurfaceBoundaryImpl(getCoordinateReferenceSystem(),
				newExterior, newInteriors);
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.SurfaceBoundary#getExterior()
	 */
	public Ring getExterior() {
		// Return exterior ring of this boundary
		return this.exterior;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.SurfaceBoundary#getInteriors()
	 */
	public List<Ring> getInteriors() {
		// Return interior rings of this boundary
		return this.interior;
	}


	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getEnvelope()
	 */
	public Envelope getEnvelope() {
		/* Return Envelope of the exterior Ring */
		return this.exterior.getEnvelope();
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.ComplexImpl#createBoundary()
	 */
	public Set<Complex> createBoundary() {
		// Return NULL, because a Boundary does not have a boundary
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#isSimple()
	 */
	public boolean isSimple() {
		// TODO semantic JR, SJ
		// the boundary of a surface does not have too be simple in all cases
		// for example, when an interior ring touches the exterior ring
		// question is, whether this is allowed or not
		// TODO implementation
		// TODO test
		// TODO documentation
		return false;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getDimension(org.opengis.geometry.coordinate.DirectPosition)
	 */
	public int getDimension(DirectPosition point) {
		// TODO What is going to happen with the point parameter?!
		// The Dimension of a SurfaceBoundary is 1, because a SurfaceBoundary consists of Rings.
		return 1;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getRepresentativePoint()
	 */
	public DirectPosition getRepresentativePoint() {
		// ok
		// Return first point of the exterior ring of the surface boundary
		return ((CurveImpl)this.getExterior().getGenerators().iterator().next()).getStartPoint();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return GeometryToString.getString(this);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((exterior == null) ? 0 : exterior.hashCode());
		result = PRIME * result + ((interior == null) ? 0 : interior.hashCode());
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
		final SurfaceBoundaryImpl other = (SurfaceBoundaryImpl) obj;
		if (exterior == null) {
			if (other.exterior != null)
				return false;
		} else if (!exterior.equals(other.exterior))
			return false;
		if (interior == null) {
			if (other.interior != null)
				return false;
		} else if (!interior.equals(other.interior))
			return false;
		return true;
	}
	
	
	
//	/**
//	 * @return the length of the ring
//	 */
//	public double getLength() {
//		// not tested
//		double len = 0.0;
//		if (this.exterior != null)
//			len = this.getExterior().getLength();
//		if (this.interior != null) {
//			for (int i = 0; i < this.interior.size(); ++i) {
//				RingImpl ring = (RingImpl) this.interior.get(i);
//				len = len + ring.getLength();
//			}
//		}
//		return len;
//	}

//Not used!	
//	/**
//	 * @param distance
//	 */
//	public void split(double distance) {
//		((RingImpl) this.exterior).split(distance);
//		if (this.interior != null) {
//			for (Ring ring : this.interior) {
//				((RingImpl) ring).split(distance);
//			}
//		}
//	}

}
