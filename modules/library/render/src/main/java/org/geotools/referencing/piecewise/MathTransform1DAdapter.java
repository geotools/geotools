/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Adapter class for {@link MathTransform1D}.
 * 
 * <p>
 * Simple adapter for {@link MathTransform1D} it provides some convenience methods for 
 * implementors.
 * 
 * <p>
 * Note that it throw an {@link UnsupportedOperationException} for the operations that must
 *  be implemented by implementors, namely:
 *  <ol>
 *  	<li>transform methods</li>
 *  	<li>inverse methods</li>
 *  	<li>derivative methods</li>
 *  </ol>
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 * @source $URL$
 */
public class MathTransform1DAdapter implements MathTransform1D {

	/**
	 * Ensure the specified point is one-dimensional.
	 */
	private static void checkDimension(final DirectPosition point) {
		final int dim = point.getDimension();
		if (dim != 1) {
			throw new MismatchedDimensionException(Errors.format(
					ErrorKeys.MISMATCHED_DIMENSION_$2, new Integer(1),
					new Integer(dim)));
		}
	}

	/**
	 * Makes sure that an argument is non-null.
	 * 
	 * @param name
	 *            Argument name.
	 * @param object
	 *            User argument.
	 * @throws IllegalArgumentException
	 *             if {@code object} is null.
	 */
	private static void ensureNonNull(final String name, final Object object)
			throws IllegalArgumentException {
		if (object == null) {
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.NULL_ARGUMENT_$1, name));
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform1D#derivative(double)
	 */
	public double derivative(double value) throws TransformException {

		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "inverse"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform1D#transform(double)
	 */
	public double transform(double value) throws TransformException {

		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#derivative(org.opengis.geometry.DirectPosition)
	 */
	public Matrix derivative(DirectPosition point)
			throws MismatchedDimensionException, TransformException {
		checkDimension(point);
		return new Matrix1(derivative(point.getOrdinate(0)));
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
	public MathTransform1D inverse() throws NoninvertibleTransformException {
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "inverse"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#isIdentity()
	 */
	public boolean isIdentity() {

		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "isIdentity"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#toWKT()
	 */
	public String toWKT() {
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "toWKT"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(org.opengis.geometry.DirectPosition,
	 *      org.opengis.geometry.DirectPosition)
	 */
	public DirectPosition transform(DirectPosition ptSrc, DirectPosition ptDst)
			throws MismatchedDimensionException, TransformException {
		// /////////////////////////////////////////////////////////////////////
		//
		// input checks
		//
		// /////////////////////////////////////////////////////////////////////
		ensureNonNull("ptSrc", ptSrc);
		checkDimension(ptSrc);
		if (ptDst == null) {
			ptDst = new GeneralDirectPosition(1);
		} else {
			checkDimension(ptDst);
		}
		ptDst.setOrdinate(0, transform(ptSrc.getOrdinate(0)));
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
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(float[],
	 *      int, float[], int, int)
	 */
	public void transform(float[] arg0, int arg1, float[] arg2, int arg3,
			int arg4) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(float[],
	 *      int, float[], int, int)
	 */
	public void transform(float[] arg0, int arg1, double[] arg2, int arg3,
			int arg4) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.referencing.operation.MathTransform#transform(float[],
	 *      int, float[], int, int)
	 */
	public void transform(double[] arg0, int arg1, float[] arg2, int arg3,
			int arg4) throws TransformException {
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "transform"));

	}

}
