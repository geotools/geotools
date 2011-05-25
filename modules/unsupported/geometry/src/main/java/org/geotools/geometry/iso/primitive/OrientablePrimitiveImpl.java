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

import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.OrientablePrimitive;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * Orientable primitives (ISO 19107 v5 figure 10) are those that can be mirrored
 * into new geometric objects in terms of their internal local coordinate
 * systems (manifold charts). For curves, the orientation reflects the direction
 * in which the curve is traversed, that is, the sense of its parameterization.
 * When used as boundary curves, the surface being bounded is to the "left" of
 * the oriented curve. For surfaces, the orientation reflects from which
 * direction the local coordinate system can be viewed as right handed, the
 * "top" or the surface being the direction of a completing z-axis that would
 * form a right-handed system. When used as a boundary surface, the bounded
 * solid is "below" the surface. The orientation of points and solids has no
 * immediate geometric interpretation in 3-dimensional space.
 * OrientablePrimitive objects are essentially references to geometric
 * primitives that carry an "orientation" reversal flag (either "+" or "-") that
 * determines whether this primitive agrees or disagrees with the orientation of
 * the referenced object.
 * 
 * NOTE There are several reasons for subclassing the positive primitives
 * under the orientable primitives. First is a matter of the semantics of
 * subclassing. Subclassing is assumed to be a is type of hierarchy. In the
 * view used, the positive primitive is simply the orientable one with the
 * positive orientation. If the opposite view were taken, and orientable
 * primitives were subclassed under the positive primitive, then by
 * subclassing logic, the negative primitive would have to hold the same sort
 * of geometric description that the positive primitive does. The only viable
 * solution would be to separate negative primitives under the geometric root
 * as being some sort of reference to their opposite. This adds a great deal of
 * complexity to the subclassing tree. To minimize the number of objects and to
 * bypass this logical complexity, positively oriented primitives are
 * self-referential (are instances of the corresponding primitive subtype) while
 * negatively oriented primitives are not.
 * 
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/01-101.pdf">Abstract
 *          Specification V5</A>
 * @author Jackson Roehrig & Sanjay Jena
 * 
 */
public abstract class OrientablePrimitiveImpl extends PrimitiveImpl implements
		OrientablePrimitive {

	/**
	 * Oriented Association (from Specification of OrientablePrimitive) Each
	 * Primitive of dimension 1 or 2 is associated to two OrientablePrimitives,
	 * one for each possible orientation.
	 * 
	 * Primitive::proxy [2] : Reference<OrientablePrimitive>;
	 * 
	 * OrientablePrimitive::primitive [1] : Reference<Primitive>; For curves
	 * and surfaces, there are exactly two orientable primitives for each
	 * geometric object.
	 * 
	 * Primitive: (proxy ? notEmpty) = (dimension = 1 or dimension = 2);
	 * 
	 * OrientablePrimitive: a, b :OrientablePrimitive
	 * ((a.primitive=b.primitive)and(a.orientation=b.orientation)) implies a=b;
	 * 
	 * As the positive orientable primitive is self-referencing to the
	 * primitive, the field proxy has only one value, which is the opposite
	 * orientable primitive. See the method OrientablePrimitive[] getProxy()
	 */
	protected OrientablePrimitive proxy = null;

	/**
	 * Constructor for empty Orientable Primitive
	 * 
	 * @param crs
	 * 
	 */
	protected OrientablePrimitiveImpl(CoordinateReferenceSystem crs) {
		super(crs, null, null, null);
		this.proxy = this.createProxy();
	}

	/**
	 * Constructor 
	 * 
	 * @param crs
	 * @param containedPrimitive
	 * @param containingPrimitive
	 * @param complex
	 */
	protected OrientablePrimitiveImpl(CoordinateReferenceSystem crs,
			Set<Primitive> containedPrimitive,
			Set<Primitive> containingPrimitive, Set<Complex> complex) {
		super(crs, containedPrimitive, containingPrimitive, complex);
		this.proxy = this.createProxy();
	}

	/**
	 * @return a new orientable primitive with negative orientation
	 */
	protected abstract OrientablePrimitive createProxy();

	/**
	 * Returns an array with two orientable primitives, whereas the first one is
	 * "this" object and the second one the field proxy
	 * 
	 * @return an array OrientablePrimitive[2] with the positive and the
	 *         negative orientable primitive
	 */
	public OrientablePrimitive[] getProxy() {
		return new OrientablePrimitive[] { this, this.proxy };
	}

	/**
	 * Orientable primitives are often denoted by a sign (for the orientation)
	 * and a base geometry (curve or surface). The sign datatype is defined in
	 * ISO 19103. If "c" is a curve, then "<+, c>" is its positive orientable
	 * curve and "<-, c>" is its negative orientable curve. In most cases,
	 * leaving out the syntax for record "< , >" does not lead to confusion, so "<+,
	 * c>" may be written as "+c" or simply "c", and "<-, c>" as "-c". Curve
	 * space arithmetic can be performed if the curves align properly, so that:
	 * 
	 * For c, d : OrientableCurves such that c.endPoint = d.startPoint then
	 *  ( c + d ) ==: CompositeCurve = < c, d >
	 * 
	 * @return 1 if positive and -1 if negative
	 */
	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.OrientablePrimitive#getOrientation()
	 */
	public int getOrientation() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see org.opengis.geometry.primitive.OrientablePrimitive#getPrimitive()
	 */
	public Primitive getPrimitive() {
		return this;
	}

}
