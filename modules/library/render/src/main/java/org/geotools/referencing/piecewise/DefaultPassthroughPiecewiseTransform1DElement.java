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
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.referencing.wkt.UnformattableObjectException;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.util.NumberRange;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * The {@link DefaultPassthroughPiecewiseTransform1DElement} identically maps
 * input values to the output ones.
 * 
 * Such DomainElement1D can be used in cases when only No-Data have been specified,
 * allowing us to create a convenience domain element for the other values.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
class DefaultPassthroughPiecewiseTransform1DElement extends DefaultPiecewiseTransform1DElement implements
		PiecewiseTransform1DElement {

	/**
	 * A generated Serial Version UID.
	 */
	private static final long serialVersionUID = -2420723761115130075L;

	/**
	 * Protected constructor for {@link DomainElement1D}s that want to build their
	 * transform later on.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}.
	 * @throws IllegalArgumentException
	 */
	DefaultPassthroughPiecewiseTransform1DElement(CharSequence name)
			throws IllegalArgumentException {
		super(name,  NumberRange.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
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
	DefaultPassthroughPiecewiseTransform1DElement(CharSequence name, final NumberRange<? extends Number> valueRange)
			throws IllegalArgumentException {
		super(name,  valueRange);
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
	 * Transforms the specified value.
	 * 
	 * @param value
	 *            The value to transform.
	 * @return the transformed value.
	 * @throws TransformException
	 *             if the value can't be transformed.
	 */
	public double transform(double value)
			throws TransformException {
		if(checkContainment(value))
			return value;
		throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1,"value"));
	}

	private boolean checkContainment(double value)
		throws TransformException{
		return contains(value);
		
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
	public double derivative(double value)
			throws TransformException {
		if(checkContainment(value))
			return 1;
		throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1,"value"));
	}

	/**
	 * Transforms the specified {@code ptSrc} and stores the result in
	 * {@code ptDst}.
	 */
	public DirectPosition transform(final DirectPosition ptSrc,
			DirectPosition ptDst) throws TransformException {
		PiecewiseUtilities.checkDimension(ptSrc);
		PiecewiseUtilities.ensureNonNull("DirectPosition", ptSrc);
		if (ptDst == null) {
			ptDst = new GeneralDirectPosition(1);
		} else {
			PiecewiseUtilities.checkDimension(ptDst);
		}
		final double value=ptSrc.getOrdinate(0);
		checkContainment(value);
		ptDst.setOrdinate(0, transform(value));
		return ptDst;
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
	 * @see org.opengis.referencing.operation.MathTransform#getSourceDimensions()
	 */
	public int getSourceDimensions() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#getTargetDimensions()
	 */
	public int getTargetDimensions() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#inverse()
	 */
	public MathTransform1D inverse()
			throws NoninvertibleTransformException {
		return LinearTransform1D.IDENTITY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#isIdentity()
	 */
	public boolean isIdentity() {
		return true;

	}

	/*
	 * (non-Javadoc)
	 * @see org.opengis.referencing.operation.MathTransform#derivative(org.opengis.geometry.DirectPosition)
	 */
	public Matrix derivative(DirectPosition dp)
			throws MismatchedDimensionException, TransformException {
		PiecewiseUtilities.ensureNonNull("DirectPosition", dp);
		PiecewiseUtilities.checkDimension(dp);
		if(checkContainment(dp.getOrdinate(0)))
			return new Matrix1(1);
		throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1,"DirectPosition"));

	}

}
