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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.primitive.OrientableSurfaceImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.primitive.OrientablePrimitive;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;

/**
 * A composite surface, CompositeSurface (Figure 29) shall be a Complex with all
 * the geometric properties of a surface, and thus can be considered as a type
 * of orientable surface (OrientableSurface). Essentially, a composite surface
 * is a collection of oriented surfaces that join in pairs on common boundary
 * curves and which, when considered as a whole, form a single surface.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 * 
 *
 *
 * @source $URL$
 */
public class CompositeSurfaceImpl extends CompositeImpl<OrientableSurfaceImpl>
		implements CompositeSurface {
    private static final long serialVersionUID = 1764508224739439610L;


    /**
	 * Generator The association role Composition::generator associates this
	 * CompositeSurface to the primitive Surfaces and OrientableSurfaces in its
	 * generating set, a list of the Surfaces that form the core of this
	 * complex. CompositeSurface::generator : Set<OrientableSurface>
	 * 
	 * NOTE To get a full representation of the elements in the Complex, the
	 * Curves and Points on the boundary of the generator set of Surfaces would
	 * be added to the curves in the generator list.
	 * @param generator
	 */
	public CompositeSurfaceImpl(List<? extends OrientableSurface> generator) {
		super(generator);
	}

//	/**
//	 * The method <code>dimension</code> returns the inherent dimension of
//	 * this Object, which is less than or equal to the coordinate dimension. The
//	 * dimension of a collection of geometric objects is the largest dimension
//	 * of any of its pieces. Points are 0-dimensional, curves are 1-dimensional,
//	 * surfaces are 2-dimensional, and solids are 3-dimensional. Locally, the
//	 * dimension of a geometric object at a point is the dimension of a local
//	 * neighborhood of the point - that is the dimension of any coordinate
//	 * neighborhood of the point. Dimension is unambiguously defined only for
//	 * DirectPositions interior to this Object. If the passed DirectPosition2D
//	 * is NULL, then the method returns the largest possible dimension for any
//	 * DirectPosition2D in this Object.
//	 * 
//	 * @param point
//	 *            a <code>DirectPosition2D</code> value
//	 * @return an <code>int</code> value
//	 */
//	public int dimension(final DirectPositionImpl point) {
//		return 2;
//	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.ComplexImpl#getElements()
	 */
//	public List<Primitive> getElements() {
//		return super.getElements();
//	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getBoundary()
	 */
	public SurfaceBoundary getBoundary() {
		// 6.3.3 ComplexBoundary
		// The boundary operation for Complex objects shall return a
		// ComplexBoundary, which is a collection of primitives and a
		// Complex of dimension 1 less than the original object.
		
		// TODO SJ: Since the CompositeSurface only contains Surfaces:
		// Is the Boundary a collection of SurfaceBoundaries?
		// The result must be a Complex again, could it be a CompositeCurve which contains all (exterior and interior)Rings of the SurfaceBoundaries of the Surfaces which are contained by the CompositeSurface?
		return null;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.CompositeImpl#getGeneratorClass()
	 */
	public Class getGeneratorClass() {
		return org.geotools.geometry.iso.primitive.OrientableSurfaceImpl.class;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.complex.ComplexImpl#createBoundary()
	 */
	public Set<Complex> createBoundary() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#clone()
	 */
	public CompositeSurfaceImpl clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.complex.Composite#getGenerators()
	 */
	public Set<OrientableSurface> getGenerators() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.root.Geometry#isSimple()
	 */
	public boolean isSimple() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.OrientableSurface#getComposite()
	 */
	public CompositeSurface getComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.OrientablePrimitive#getOrientation()
	 */
	public int getOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.OrientablePrimitive#getPrimitive()
	 */
	public Surface getPrimitive() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.Primitive#getContainedPrimitives()
	 */
	public Set getContainedPrimitives() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.Primitive#getContainingPrimitives()
	 */
	public Set getContainingPrimitives() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.Primitive#getComplexes()
	 */
	public Set getComplexes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.Primitive#getProxy()
	 */
	public OrientablePrimitive[] getProxy() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getDimension(org.opengis.geometry.coordinate.DirectPosition)
	 */
	public int getDimension(DirectPosition point) {
		// TODO Auto-generated method stub
		return 2;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getEnvelope()
	 */
	public Envelope getEnvelope() {
		// Test ok
		Collection<Primitive> tmpSurfaces = (Collection<Primitive>) this.getElements();

		Iterator<Primitive> surfIter = tmpSurfaces.iterator();
		
		if (!surfIter.hasNext())
			return null;
		
		Surface actSurf = (Surface) surfIter.next();
		/* Use envelope of the first Curve as base for the new envelope */
		EnvelopeImpl rEnv = new EnvelopeImpl(actSurf.getEnvelope());
		//EnvelopeImpl rEnv = this.getFeatGeometryFactory().getGeometryFactoryImpl().createEnvelope(actSurf.getEnvelope());
		
		/* Add envelopes of the other Curves */
		while (surfIter.hasNext()) {
			actSurf = (Surface) surfIter.next();
			rEnv.expand(actSurf.getEnvelope());
		}

		return rEnv;
	}
	

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getRepresentativePoint()
	 */
	public DirectPosition getRepresentativePoint() {
		// Return the representative point of the first surface in this composite
		return this.elements.get(0).getRepresentativePoint();
	}

}
