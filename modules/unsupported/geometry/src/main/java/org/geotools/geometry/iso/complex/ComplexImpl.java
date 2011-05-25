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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.geometry.iso.primitive.PrimitiveImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * A Complex is a collection of geometrically disjoint, simple Primitives. If a
 * Primitive (other than a Point) is in a particular Complex, then there exists
 * a set of primitives of lower dimension in the same complex that form the
 * boundary of this primitive.
 * 
 * NOTE A geometric complex can be thought of as a set in two distinct ways.
 * First, it is a finite set of objects (via delegation to its elements member)
 * and, second, it is an infinite set of point values as a subtype of geometric
 * object. The dual use of delegation and subtyping is to disambiguate the two
 * types of set interface. To determine if a Primitive P is an element of a
 * Complex C, call: C.element().contains(P).
 * 
 * The "element" attribute allows Complex to inherit the behavior of Set<Primitive>
 * without confusing the same sort of behavior inherited from TransfiniteSet<DirectPosition2D>
 * inherited through Object. Complexes shall be used in application schemas
 * where the sharing of geometry is important, such as in the use of
 * computational topology. In a complex, primitives may be aggregated
 * many-to-many into composites for use as attributes of features. Examples of
 * this are provided in the schemas in annex D.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 * @source $URL$
 */
public abstract class ComplexImpl extends GeometryImpl implements Complex {

	/**
	 * Contains association The association "Contains" instantiates the contains
	 * operation from Set<Primitive> as an association. Set<Complex> of
	 * Complex [0..n]
	 */
	private Set<Complex> subComplex = null;

	private Set<Complex> superComplex = null;

	/**
	 * The association "Complex" is defined by the "contains" operation in
	 * Object that is inherited from TransfiniteSet<DirectPosition2D>.
	 * Complex::element [1..n] : Primitive If a complex contains a Primitive,
	 * then it must also contain the elements of its boundary. Complex: --
	 * closed under the boundary operation
	 * self?forAll(self?includesAll(boundary()))
	 * 
	 * ArrayList of Primitive [1..n]
	 */
	// This cannot be a collection, but needs to be an ordered collecion, which
	// is a list
	// protected Collection<? extends Primitive> elements = null;
	protected List<? extends Primitive> elements = null;

	/**
	 * 
	 * @param elements
	 */
	public ComplexImpl(List<? extends Primitive> elements) {
		super(elements.get(0).getCoordinateReferenceSystem());
		if (elements.isEmpty())
			throw new IllegalArgumentException(
					"Array of Elements is empty. At least one element expected."); //$NON-NLS-1$
		this.elements = elements;
		this.subComplex = this.createBoundary();
		this.superComplex = null;

	}

	/**
	 * Constructs a Complex Elements have to be added after
	 * 
	 * @param crs
	 */
	public ComplexImpl(CoordinateReferenceSystem crs) {
		super(crs);
		this.elements = null;
		this.subComplex = null;
		this.superComplex = null;
	}

	/**
	 * Sets the elements contained by this Complex
	 * 
	 * @param element
	 */
	public void setElements(List<? extends Primitive> element) {
		this.elements = element;
	}

	/**
	 * @return Set<Complex>
	 */
	public abstract Set<Complex> createBoundary();

	/**
	 * Returns elements contained in this Complex
	 * 
	 * @return the primitives
	 */
	public Collection<? extends Primitive> getElements() {
		return this.elements;
	}

	/**
	 * @param primitive
	 * @return
	 */
	boolean hasElement(PrimitiveImpl primitive) {
		return this.elements.contains(primitive);
	}

	/**
	 * @param primitive
	 */
	void removeElement(PrimitiveImpl primitive) {
		this.elements.remove(primitive);
	}

	/**
	 * The Boolean valued operation "isMaximal" shall return TRUE if and only if
	 * this Complex is maximal. Complex::isMaximal() : Boolean
	 * 
	 * @return true if this complex is maximal
	 */
	public boolean isMaximal() {
		return ((superComplex == null) || superComplex.isEmpty());
	}

	/**
	 * Adds an associated Sub Complex
	 * 
	 * @param subComplex1
	 */
	protected void addSubComplex(Complex subComplex1) {
		this.subComplex.add(subComplex1);
	}

	/**
	 * Adds an associated Super Complex
	 * 
	 * @param superComplex1
	 */
	protected void addSuperComplex(ComplexImpl superComplex1) {
		this.superComplex.add(superComplex1);
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.complex.Complex#getSuperComplexes()
	 */
	public Complex[] getSuperComplexes() {
		return (this.superComplex == null) ? null : this.superComplex
				.toArray(new Complex[this.superComplex.size()]);
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.complex.Complex#getSubComplexes()
	 */
	public Complex[] getSubComplexes() {
		return (this.subComplex == null) ? null : this.subComplex
				.toArray(new Complex[this.subComplex.size()]);
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
	 * @return the set containing only this object if it is a maximal complex or
	 *         its maximum complexes
	 * 
	 */
	/* (non-Javadoc)
	 * @see org.opengis.geometry.coordinate.root.Geometry#getMaximalComplex()
	 */
	public Set<Complex> getMaximalComplex() {
		TreeSet<Complex> result = new TreeSet<Complex>();
		if (this.isMaximal()) {
			result.add(this);
		} else {
			for (Complex c : this.superComplex) {
				result.addAll(c.getMaximalComplex());
			}
		}
		return result;
	}

}
