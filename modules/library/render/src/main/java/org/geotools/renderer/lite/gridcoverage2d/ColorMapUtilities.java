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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Color;

import org.geotools.referencing.piecewise.DefaultPiecewiseTransform1DElement;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;

class ColorMapUtilities {

	/**
	 * Codes ARGB par d�faut. On utilise un exemplaire unique pour toutes les
	 * cr�ation d'objets {@link LinearColorMapElement}.
	 */
	static final int[] DEFAULT_ARGB = { 0xFF000000, 0xFFFFFFFF };

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
	static void ensureNonNull(final String name,
			final Object object) throws IllegalArgumentException {
		if (object == null) {
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.NULL_ARGUMENT_$1, name));
		}
	}

	/**
	 * Compare deux valeurs de type {@code double}. Cette m�thode est similaire
	 * � {@link Double#compare(double,double)}, except� qu'elle ordonne aussi
	 * les diff�rentes valeurs NaN.
	 */
	static int compare(final double v1, final double v2) {
		if (Double.isNaN(v1) && Double.isNaN(v2)) {
			final long bits1 = Double.doubleToRawLongBits(v1);
			final long bits2 = Double.doubleToRawLongBits(v2);
			if (bits1 < bits2)
				return -1;
			if (bits1 > bits2)
				return +1;
		}
		return Double.compare(v1, v2);
	}

//	/**
//	 * Returns a linear transform with the supplied scale and offset values.
//	 * 
//	 * @param scale
//	 *            The scale factor. May be 0 for a constant transform.
//	 * @param offset
//	 *            The offset value. May be NaN if this method is invoked from a
//	 *            constructor for initializing {@link #transform} for a
//	 *            qualitative category.
//	 */
//	static MathTransform1D createLinearTransform(
//			final double scale, final double offset) {
//		return LinearTransform1D.create(scale, offset);
//	}
//
//	/**
//	 * Create a linear transform mapping values from {@code sampleValueRange} to
//	 * {@code geophysicsValueRange}.
//	 */
//	static MathTransform1D createLinearTransform(
//			final NumberRange sourceRange, final NumberRange destinationRange) {
//		final Class sType = sourceRange.getElementClass();
//		final Class dType = destinationRange.getElementClass();
//		/*
//		 * First, find the direction of the adjustment to apply to the ranges if
//		 * we wanted all values to be inclusives. Then, check if the adjustment
//		 * is really needed: if the values of both ranges are inclusive or
//		 * exclusive, then there is no need for an adjustment before computing
//		 * the coefficient of a linear relation.
//		 */
//		int sMinInc = sourceRange.isMinIncluded() ? 0 : +1;
//		int sMaxInc = sourceRange.isMaxIncluded() ? 0 : -1;
//		int dMinInc = destinationRange.isMinIncluded() ? 0 : +1;
//		int dMaxInc = destinationRange.isMaxIncluded() ? 0 : -1;
//	
//		/*
//		 * Now, extracts the minimal and maximal values and computes the linear
//		 * coefficients.
//		 */
//		final double minSource = doubleValue(sType, sourceRange.getMinValue(),
//				sMinInc);
//		final double maxSource = doubleValue(sType, sourceRange.getMaxValue(),
//				sMaxInc);
//		final double minDestination = doubleValue(dType, destinationRange
//				.getMinValue(), dMinInc);
//		final double maxDestination = doubleValue(dType, destinationRange
//				.getMaxValue(), dMaxInc);
//	
//		// /////////////////////////////////////////////////////////////////
//		//
//		// optimizations
//		//
//		// /////////////////////////////////////////////////////////////////
//		// //
//		//
//		// If the output range is a single value let's create a constant
//		// transform
//		//
//		// //
//		if (ColorMapUtilities.compare(minDestination, maxDestination) == 0)
//			return LinearTransform1D.create(0, minDestination);
//	
//		// //
//		//
//		// If the input range is a single value this transform ca be created
//		// only if we map to another single value
//		//
//		// //
//		if (ColorMapUtilities.compare(minSource, maxSource) == 0)
//			throw new IllegalArgumentException("Impossible to map a single value to a range.");
//	
//		double scale = (maxDestination - minDestination)
//				/ (maxSource - minSource);
//		// /////////////////////////////////////////////////////////////////
//		//
//		// Take into account the fact that the maxSample and the minSample can
//		// be
//		// similar hence we have a constant transformation.
//		//
//		// /////////////////////////////////////////////////////////////////
//		if (Double.isNaN(scale))
//			scale = 0;
//		final double offset = minDestination - scale * minSource;
//		return createLinearTransform(scale, offset);
//	}
//
//	/**
//	 * Returns a {@code double} value for the specified number. If
//	 * {@code direction} is non-zero, then this method will returns the closest
//	 * representable number of type {@code type} before or after the double
//	 * value.
//	 * 
//	 * @param type
//	 *            The range element class. {@code number} must be an instance of
//	 *            this class (this will not be checked).
//	 * @param number
//	 *            The number to transform to a {@code double} value.
//	 * @param direction
//	 *            -1 to return the previous representable number, +1 to return
//	 *            the next representable number, or 0 to return the number with
//	 *            no change.
//	 */
//	static double doubleValue(final Class type,
//			final Comparable number, final int direction) {
//		assert (direction >= -1) && (direction <= +1) : direction;
//		return XMath.rool(type, ((Number) number).doubleValue(), direction);
//	}

	/**
	 * Ensure the specified point is one-dimensional.
	 */
	static void checkDimension(final DirectPosition point) {
		final int dim = point.getDimension();
		if (dim != 1) {
			throw new MismatchedDimensionException(Errors.format(
					ErrorKeys.MISMATCHED_DIMENSION_$2, Integer.valueOf(1),
					Integer.valueOf(dim)));
		}
	}
	/**
	 * Check that all the output values for the various
	 * {@link DefaultConstantPiecewiseTransformElement} are equal.
	 * 
	 * @param preservingElements
	 *            array of {@link DefaultConstantPiecewiseTransformElement}s.
	 * @return the array of {@link DefaultConstantPiecewiseTransformElement}s if the check is
	 *         successful.
	 * @throws IllegalArgumentException
	 *             in case the check is unsuccessful.
	 */
	static DefaultPiecewiseTransform1DElement[] checkPreservingElements(
			LinearColorMapElement[] preservingElements) {
		if (preservingElements != null) {
			double outval = Double.NaN;
			Color color=null;
			for (int i = 0; i < preservingElements.length; i++) {
				//the no data element must be a linear transform mapping to a single value
				if(!(preservingElements[i] instanceof ConstantColorMapElement))
					throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, preservingElements));
				final ConstantColorMapElement nc = (ConstantColorMapElement) preservingElements[i];
				if(nc.getColors().length!=1)
					throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, nc.getColors()));
				if (i == 0)
				{
					outval = nc.getOutputMaximum();
					color=nc.getColors()[0];
				}
				else 
				{	
					if (compare(outval, nc.getOutputMaximum()) != 0)
						throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, nc.getColors()));
					if (!color.equals(nc.getColors()[0]))
						throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, nc.getColors()));
				}
	
			}
		}
		return preservingElements;
	}
}
