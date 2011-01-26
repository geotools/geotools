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
import java.util.TreeSet;

import org.geotools.geometry.iso.complex.ComplexImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.Composite;
import org.opengis.geometry.primitive.OrientablePrimitive;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.PrimitiveBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * Primitive is the abstract root class of the geometric primitives. Its main
 * purpose is to define the basic "boundary" operation that ties the primitives
 * in each dimension together. A geometric primitive is a geometric object that
 * is not decomposed further into other primitives in the system. This includes
 * curves and surfaces, even though they are composed of curve segments and
 * surface patches, respectively. This composition is a strong aggregation:
 * curve segments and surface patches cannot exist outside the context of a
 * primitive.
 * 
 * NOTE Most geometric primitives are decomposable infinitely many times. Adding
 * a centre point to a line may split that line into two separate lines. A new
 * curve drawn across a surface may divide that surface into two parts, each of
 * which is a surface. This is the reason that the normal definition of
 * primitive as "non-decomposable" is not plausible in a geometry model - the
 * only non-decomposable object in geometry is a point. Any geometric object
 * that is used to describe a feature is a collection of geometric primitives. A
 * collection of geometric primitives may or may not be a geometric complex.
 * Geometric complexes have additional properties such as closure by boundary
 * operations and mutually exclusive component parts.
 * 
 * Primitive and Complex share most semantics, in the meaning of operations,
 * attributes and associations. There is an exception in that a Primitive shall
 * not contain its boundary (except in the trivial case of Point where the
 * boundary is empty), while a Complex shall contain its boundary in all cases.
 * This means that if an instantiated object implements Object operations both
 * as Primitive and as a Complex, the semantics of each set theoretic operation
 * is determined by the its name resolution. Specifically, for a particular
 * object such as CompositeCurve, Primitive::contains (returns FALSE for end
 * points) is different from Complex::contains (returns TRUE for end points).
 * Further, if that object is cast as a Primitive value and as a Complex value,
 * then the two values need not be equal as Objects.
 * 
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/01-101.pdf">Abstract
 *          Specification V5</A>
 * @author Jackson Roehrig & Sanjay Jena
 * 
 */

public abstract class PrimitiveImpl extends GeometryImpl implements Primitive {
	
	/**
	 * The "Interior to" association associates Primitives which are by
	 * definition coincident with one another. This allows applications to
	 * override the Set<DirectPosition> interpretation and its associated
	 * computational geometry, and declare one Primitive to be "interior to"
	 * another. This association should normally be empty when the Primitives
	 * are within a Complex, since in that case the boundary information is
	 * sufficient for most cases.
	 * 
	 * Primitive::coincidentSubelement [0..n] : Reference<Primitive>
	 * Primitive::superElement [0..n] : Reference<Primitive>
	 * 
	 * This association is constrained by the set theory operators and dimension
	 * operators defined at Object.
	 * 
	 * Primitive: superElement=>includes(p: Primitive) = Object::contains (p)
	 * dimension() >= coincidentSubelement. dimension()
	 * 
	 * NOTE This association should not be used when the two Primitives are not
	 * close to one another. The intent is to allow applications to compensate
	 * for inherent and unavoidable round off, truncation, and other
	 * mathematical problems indigenous to computer calculations.
	 * 
	 * The fields containedPrimitive and containingPrimitive are unset per
	 * default
	 */
	protected Set<Primitive> containedPrimitive = null;

	/**
	 * See containedPrimitive
	 */
	protected Set<Primitive> containingPrimitive = null;

	/**
	 * A Primitive may be in several Complexes. This association may not be
	 * navigable in this direction (from primitive to complex), depending on the
	 * application schema.
	 * 
	 * Primitive::complex [0..n] : Reference<Complex>
	 * 
	 * The field complex is unset per default
	 */
	protected Set<Complex> complex = null;

	/**
	 * @param crs
	 * @param containedPrimitive
	 * @param containingPrimitive
	 * @param complex
	 */
	protected PrimitiveImpl(CoordinateReferenceSystem crs,
			Set<Primitive> containedPrimitive,
			Set<Primitive> containingPrimitive,
			Set<Complex> complex) {
		super(crs);
		// the parameters may be null
		this.containedPrimitive = containedPrimitive;
		this.containingPrimitive = containingPrimitive;
		this.complex = complex;
	}

	public PrimitiveImpl(CoordinateReferenceSystem coordinateReferenceSystem) {
		super( coordinateReferenceSystem );
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.Primitive#getContainingPrimitives()
	 */
	public Set<Primitive> getContainingPrimitives() {
		return this.containingPrimitive;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.Primitive#getContainedPrimitives()
	 */
	public Set<Primitive> getContainedPrimitives() {
		return this.containedPrimitive;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.Primitive#getComplexes()
	 */
	public Set<Complex> getComplexes() {
		return this.complex;
	}

        public Composite getComposite() {
            return null;
        }

	/**
	 * Adds a new subelement of same or lower dimension then this object. This
	 * object contains the given subelement
	 * 
	 * @param newSubelement
	 */
	public void addContainingPrimitive(PrimitiveImpl newSubelement) {
		if (this.containingPrimitive == null)
			this.containingPrimitive = new TreeSet<Primitive>();
		this.containingPrimitive.add(newSubelement);
	}

	/**
	 * Adds a the super element where this object in contained in
	 * 
	 * @param newSuperelement
	 */
	public void addContainedPrimitive(PrimitiveImpl newSuperelement) {
		if (this.containedPrimitive == null)
			this.containedPrimitive = new TreeSet<Primitive>();
		this.containedPrimitive.add(newSuperelement);
	}

	/**
	 * Adds a new complex to the set of complexes containing this primitive
	 * 
	 * @param newComplex
	 */
	public void addComplex(ComplexImpl newComplex) {
		if (this.complex == null)
			this.complex = new TreeSet<Complex>();
		this.complex.add(newComplex);
	}

	/**
	 * As a set of primitives, a Complex may be contained as a set in another
	 * larger Complex, referred to as a "super complex" of the original. A
	 * Complex is maximal if there is no such larger super complex. The
	 * operation "maximalComplex" shall return the set of maximal Complexes
	 * within which this Object is contained.
	 * 
	 * Object::maximalComplex() : Set<Complex>
	 * 
	 * If the application schema used does not include Complex, then this
	 * operation shall return a NULL value.
	 * 
	 * NOTE The usual semantics of maximal complexes does not allow any
	 * Primitive to be in more than one maximal complex, making it a strong
	 * aggregation. This is not an absolute, and depending on the semantics of
	 * the implementation, the association between Primitives and maximal
	 * Complexes could be many to many. From a programming point of view, this
	 * would be a difficult (but not impossible) dynamic structure to maintain,
	 * but as a static query-only structure, it could be quite useful in
	 * minimizing redundant data inherent in two representations of the same
	 * primitive geometric object.
	 * 
	 * @return the set of maximal complexes or null
	 * 
	 */
	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.root.Geometry#getMaximalComplex()
	 */
	public Set<Complex> getMaximalComplex() {
		if (this.complex == null)
			return null;
		TreeSet<Complex> result = new TreeSet<Complex>();
		for (Complex c : this.complex) {
			result.addAll(c.getMaximalComplex());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getBoundary()
	 */
	public abstract PrimitiveBoundary getBoundary();
	
	
    /* (non-Javadoc)
     * @see org.opengis.geometry.primitive.Primitive#getProxy()
     */
    public abstract OrientablePrimitive[] getProxy();

	
}
