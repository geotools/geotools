/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.piecewise;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.operation.matrix.Matrix1;
import org.geotools.referencing.wkt.UnformattableObjectException;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.util.NumberRange;
import org.geotools.util.Utilities;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Convenience implementation of the   {@link DefaultPiecewiseTransform1DElement}   .
 * @author   Simone Giannecchini, GeoSolutions
 *
 * @source $URL$
 */
public class DefaultPiecewiseTransform1DElement extends DefaultDomainElement1D implements
		PiecewiseTransform1DElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7422178060824402864L;
	/**
     * The math transform
     * @uml.property  name="transform"
     */
	private MathTransform1D transform;

	/**
	 * Inverse {@link MathTransform1D}
	 */
	private MathTransform1D inverse;
        
	private int hashCode=-1;

	
	/**
	 * Builds up a {@link DefaultPiecewiseTransform1DElement} which maps a range to a constant value.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name,
			final NumberRange<? extends Number> inRange,
			final double value){
		return new DefaultConstantPiecewiseTransformElement(name, inRange, value);
	}
	
	/**
	 * Builds up a DefaultPiecewiseTransform1DElement which maps a range to a constant value.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name,
			final NumberRange<? extends Number> inRange,
			final byte value){
		return new DefaultConstantPiecewiseTransformElement(name, inRange, value);
	}
	
	/**
	 * Builds up a DefaultPiecewiseTransform1DElement which maps a range to a constant value.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name,
			final NumberRange<? extends Number> inRange,
			final int value){
		return new DefaultConstantPiecewiseTransformElement(name, inRange, value);
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}.
	 * @param inRange
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}.
	 * @param outRange
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}.
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name, 
			final NumberRange<? extends Number> inRange,
			final NumberRange<? extends Number> outRange) {	
		return new DefaultLinearPiecewiseTransform1DElement(name,inRange,outRange);
	}
	
	
	/**
	 * Creates a pass-through DefaultPiecewiseTransform1DElement.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	public static DefaultPiecewiseTransform1DElement create(
			final CharSequence name)
			throws IllegalArgumentException {
		return new DefaultPassthroughPiecewiseTransform1DElement(name,  NumberRange.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
	}

	/**
	 * Creates a pass-through DefaultPiecewiseTransform1DElement.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @param valueRange
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	public  static DefaultPiecewiseTransform1DElement create(
			final CharSequence name, 
			final NumberRange<? extends Number> valueRange)
			throws IllegalArgumentException {
		return new DefaultPassthroughPiecewiseTransform1DElement(name,  valueRange);
	}	
	/**
	 * Protected constructor for {@link DomainElement1D}s that want to build their
	 * transform later on.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @param valueRange
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	protected DefaultPiecewiseTransform1DElement(CharSequence name, NumberRange<? extends Number> valueRange)
			throws IllegalArgumentException {
		super(name, valueRange);
	}

	/**
	 * Public constructor for building a {@link DomainElement1D} which applies the
	 * specified transformation on the values that fall into its definition
	 * range.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @param valueRange
	 *            for this {@link DomainElement1D}.
	 * @param transform
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	protected DefaultPiecewiseTransform1DElement(CharSequence name, NumberRange<? extends Number> valueRange,
			final MathTransform1D transform) throws IllegalArgumentException {
		super(name, valueRange);
		// /////////////////////////////////////////////////////////////////////
		//
		// Initial checks
		//
		// /////////////////////////////////////////////////////////////////////
		PiecewiseUtilities.ensureNonNull("transform", transform);
		this.transform = transform;
	}

	/**
	 * Returns a Well Known Text</cite> (WKT) for this object. This operation
	 * may fails if an object is too complex for the WKT format capability.
	 * 
	 * @return The Well Know Text for this object.
	 * @throws UnsupportedOperationException
	 *             If this object can't be formatted as WKT.
	 * 
	 * XXX Not yet implemented.
	 */
	public String toWKT() throws UnsupportedOperationException {
		throw new UnformattableObjectException("Not yet implemented.", this
				.getClass());
	}

	/**
     * Getter for the underlying  {@link MathTransform1D}  .
     * @return  the underlying  {@link MathTransform1D}  .
     * @uml.property  name="transform"
     */
	protected synchronized MathTransform1D getTransform() {
		return transform;
	}

	/**
	 * Transforms the specified value.
	 * 
	 * @param value
	 *            The value to transform.
	 * @return the transformed value.
	 * @throws TransformException
	 *             if the value can't be transformed.
	 */
	public synchronized double transform(double value)
			throws TransformException {

		if (transform == null)
			throw new IllegalStateException();

		if (contains(value))
			return transform.transform(value);

		throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, "Provided value is not contained in this domain"));
	}

	/**
	 * Gets the derivative of this function at a value.
	 * 
	 * @param value
	 *            The value where to evaluate the derivative.
	 * @return The derivative at the specified point.
	 * @throws TransformException
	 *             if the derivative can't be evaluated at the specified point.
	 */
	public synchronized double derivative(double value)
			throws TransformException {
		if (transform == null)
			//  @todo TODO
			throw new IllegalStateException();
		if (contains(value))
			return transform.derivative(value);
		throw new TransformException(Errors.format(ErrorKeys.TRANSFORM_EVALUATION_$1, new Double(value)));
	}

	/**
	 * Transforms the specified {@code ptSrc} and stores the result in
	 * {@code ptDst}.
	 */
	public DirectPosition transform(final DirectPosition ptSrc,
			DirectPosition ptDst) throws TransformException {
		PiecewiseUtilities.checkDimension(ptSrc);
		if (ptDst == null) {
			ptDst = new GeneralDirectPosition(1);
		} else {
			PiecewiseUtilities.checkDimension(ptDst);
		}
		ptDst.setOrdinate(0, transform(ptSrc.getOrdinate(0)));
		return ptDst;
	}

	/**
	 * Gets the derivative of this transform at a point.
	 */
	public Matrix derivative(final DirectPosition point)
			throws TransformException {
		PiecewiseUtilities.ensureNonNull("DirectPosition", point);
		PiecewiseUtilities.checkDimension(point);
		return new Matrix1(derivative(point.getOrdinate(0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(double[],
	 *      int, double[], int, int)
	 */
	public void transform(double[] arg0, int arg1, double[] arg2, int arg3,
			int arg4) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(float[],
	 *      int, float[], int, int)
	 */
	public void transform(float[] arg0, int arg1, float[] arg2, int arg3,
			int arg4) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(float[],
	 *      int, float[], int, int)
	 */
	public void transform(float[] arg0, int arg1, double[] arg2, int arg3,
			int arg4) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(float[],
	 *      int, float[], int, int)
	 */
	public void transform(double[] arg0, int arg1, float[] arg2, int arg3,
			int arg4) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#getSourceDimensions()
	 */
	public int getSourceDimensions() {
		return transform.getSourceDimensions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#getTargetDimensions()
	 */
	public int getTargetDimensions() {
		return transform.getTargetDimensions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#inverse()
	 */
	public synchronized MathTransform1D inverse()
			throws NoninvertibleTransformException {
		if (inverse != null)
			return inverse;
		if (transform == null)
			throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));
		inverse = (MathTransform1D) transform.inverse();
		return inverse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#isIdentity()
	 */
	public boolean isIdentity() {
		return transform.isIdentity();

	}

	/**
     * @param  mathTransform
     * @uml.property  name="inverse"
     */
	protected synchronized void setInverse(MathTransform1D mathTransform) {
		if (this.inverse == null)
			this.inverse = mathTransform;
		else
			throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));
	}

	/**
     * @param  transform
     * @uml.property  name="transform"
     */
	protected synchronized void setTransform(MathTransform1D transform) {
		PiecewiseUtilities.ensureNonNull("transform", transform);
		if (this.transform == null)
			this.transform = transform;
		else
			throw new IllegalStateException(Errors.format(ErrorKeys.ILLEGAL_STATE));
	}


	public boolean equals(Object obj) {
	    if(obj==this)
			return true;
            if(!(obj instanceof DefaultPiecewiseTransform1DElement))
                return false;
            final DefaultPiecewiseTransform1DElement that=(DefaultPiecewiseTransform1DElement) obj;
            if(getEquivalenceClass()!=(that.getEquivalenceClass()))
                return false;
            if (!Utilities.equals(transform, that.transform))
                return false;
            if (!Utilities.equals(inverse, that.inverse))
                return false;
            return super.equals(obj);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.referencing.piecewise.DefaultDomainElement1D#toString()
	 */
	public String toString() {
		final StringBuilder buffer= new StringBuilder(super.toString());
		buffer.append("\n").append("wkt transform=").append(this.transform.toWKT());
		return buffer.toString();
	}

    protected Class<?> getEquivalenceClass(){
        return DefaultPiecewiseTransform1DElement.class;
    }

    @Override
    public int hashCode() {
        if(hashCode>=0)
            return hashCode;
        hashCode=37;
        hashCode=Utilities.hash(transform,hashCode);
        hashCode=Utilities.hash( inverse,hashCode);
        hashCode=Utilities.hash( super.hashCode(),hashCode);
        return hashCode;
    }

    public static DefaultPiecewiseTransform1DElement create(String string,
            NumberRange<? extends Number> range,
            MathTransform1D mathTransform1D) {
        return new DefaultPiecewiseTransform1DElement(string, range, mathTransform1D);
    }

}
