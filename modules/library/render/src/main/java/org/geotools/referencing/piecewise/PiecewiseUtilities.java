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

import java.util.Arrays;

import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.resources.XMath;
import org.geotools.util.NumberRange;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform1D;

/**
 * Convenience class to group utilities methods for {@link DomainElement1D} and
 * {@link Domain1D} implmentations.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * 
 */
class PiecewiseUtilities {

	/**
	 * 
	 */
	private PiecewiseUtilities() {
	}

	/**
	 * Checks whether or not two DomainElement1Ds input range overlaps
	 * 
	 * @param domainElements
	 *            to be checked
	 * @param idx
	 *            index to start with
	 */
	 static void domainElementsOverlap(DomainElement1D[] domainElements, int idx) {
		// Two domain elements have overlapping ranges;
		// Format an error message...............
		final NumberRange<? extends Number> range1 = domainElements[idx - 1]
				.getRange();
		final NumberRange<? extends Number> range2 =  domainElements[idx].getRange();
		final Comparable[] args = new Comparable[] { range1.getMinValue(),
				range1.getMaxValue(), range2.getMinValue(),
				range2.getMaxValue() };
		for (int j = 0; j < args.length; j++) {
			if (args[j] instanceof Number) {
				final double value = ((Number) args[j]).doubleValue();
				if (Double.isNaN(value)) {
					String hex = Long.toHexString(Double
							.doubleToRawLongBits(value));
					args[j] = "NaN(" + hex + ')';
				}
			}
		}
		throw new IllegalArgumentException(Errors.format(
				ErrorKeys.RANGE_OVERLAP_$4, args));
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
	static void ensureNonNull(final String name,
			final Object object) throws IllegalArgumentException {
		if (object == null) {
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.NULL_ARGUMENT_$1, name));
		}
	}

	/**
	 * Effectue une recherche bi-lin�aire de la valeur sp�cifi�e. Cette m�thode
	 * est semblable � {@link Arrays#binarySearch(double[],double)}, except�
	 * qu'elle peut distinguer diff�rentes valeurs de NaN.
	 * 
	 * Note: This method is not private in order to allows testing by {@link }.
	 */
	static int binarySearch(final double[] array, final double val) {
		int low = 0;
		int high = array.length - 1;
		final boolean keyIsNaN = Double.isNaN(val);
		while (low <= high) {
			final int mid = (low + high) >> 1;
			final double midVal = array[mid];
			if (midVal < val) { // Neither val is NaN, midVal is smaller
				low = mid + 1;
				continue;
			}
			if (midVal > val) { // Neither val is NaN, midVal is larger
				high = mid - 1;
				continue;
			}
			/*
			 * The following is an adaptation of evaluator's comments for bug
			 * #4471414
			 * (http://developer.java.sun.com/developer/bugParade/bugs/4471414.html).
			 * Extract from evaluator's comment:
			 * 
			 * [This] code is not guaranteed to give the desired results because
			 * of laxity in IEEE 754 regarding NaN values. There are actually
			 * two types of NaNs, signaling NaNs and quiet NaNs. Java doesn't
			 * support the features necessary to reliably distinguish the two.
			 * However, the relevant point is that copying a signaling NaN may
			 * (or may not, at the implementors discretion) yield a quiet NaN --
			 * a NaN with a different bit pattern (IEEE 754 6.2). Therefore, on
			 * IEEE 754 compliant platforms it may be impossible to find a
			 * signaling NaN stored in an array since a signaling NaN passed as
			 * an argument to binarySearch may get replaced by a quiet NaN.
			 */
			final long midRawBits = Double.doubleToRawLongBits(midVal);
			final long keyRawBits = Double.doubleToRawLongBits(val);
			if (midRawBits == keyRawBits) {
				return mid; // key found
			}
			final boolean midIsNaN = Double.isNaN(midVal);
			final boolean adjustLow;
			if (keyIsNaN) {
				// If (mid,key)==(!NaN, NaN): mid is lower.
				// If two NaN arguments, compare NaN bits.
				adjustLow = (!midIsNaN || midRawBits < keyRawBits);
			} else {
				// If (mid,key)==(NaN, !NaN): mid is greater.
				// Otherwise, case for (-0.0, 0.0) and (0.0, -0.0).
				adjustLow = (!midIsNaN && midRawBits < keyRawBits);
			}
			if (adjustLow)
				low = mid + 1;
			else
				high = mid - 1;
		}
		return -(low + 1); // key not found.
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

	/**
	 * V�rifie si le tableau de cat�gories sp�cifi� est bien en ordre croissant.
	 * La comparaison ne tient pas compte des valeurs {@code NaN}. Cette
	 * m�thode n'est utilis�e que pour les {@code assert}.
	 */
	static boolean isSorted(final DefaultDomainElement1D[] domains) {
		if(domains==null)
			return true;
		for (int i = 1; i < domains.length; i++) {
			final DefaultDomainElement1D d1 = domains[i ];
			assert !(d1.getInputMinimum() > d1
					.getInputMaximum()) : d1;
			final DefaultDomainElement1D d0 = domains[i - 1];
			assert !(d0.getInputMinimum() >d0
					.getInputMaximum()) : d0;
			if (compare(d0.getInputMaximum(), d1
					.getInputMinimum()) > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Ensure the specified point is one-dimensional.
	 */
	static void checkDimension(final DirectPosition point) {
		final int dim = point.getDimension();
		if (dim != 1) {
			throw new MismatchedDimensionException(Errors.format(
					ErrorKeys.MISMATCHED_DIMENSION_$2, new Integer(1),
					new Integer(dim)));
		}
	}

	/**
	 * Returns a {@code double} value for the specified number. If
	 * {@code direction} is non-zero, then this method will returns the closest
	 * representable number of type {@code type} before or after the double
	 * value.
	 * 
	 * @param type
	 *            The range element class. {@code number} must be an instance of
	 *            this class (this will not be checked).
	 * @param number
	 *            The number to transform to a {@code double} value.
	 * @param direction
	 *            -1 to return the previous representable number, +1 to return
	 *            the next representable number, or 0 to return the number with
	 *            no change.
	 */
	static double doubleValue(final Class<? extends Number> type,
			final Number number, final int direction) {
		assert (direction >= -1) && (direction <= +1) : direction;
		return XMath.rool(type, number.doubleValue(), direction);
	}

	/**
	 * Returns a linear transform with the supplied scale and offset values.
	 * 
	 * @param scale
	 *            The scale factor. May be 0 for a constant transform.
	 * @param offset
	 *            The offset value. May be NaN.
	 */
	static MathTransform1D createLinearTransform1D(
			final double scale, final double offset) {
		return LinearTransform1D.create(scale, offset);
	}

	/**
	 * Create a linear transform mapping values from {@code sampleValueRange} to
	 * {@code geophysicsValueRange}.
	 */
	static MathTransform1D createLinearTransform1D(
			final NumberRange<? extends Number> sourceRange, final NumberRange<? extends Number> destinationRange) {
		final Class<? extends Number> sType = sourceRange.getElementClass();
		final Class<? extends Number> dType = destinationRange.getElementClass();
		/*
		 * First, find the direction of the adjustment to apply to the ranges if
		 * we wanted all values to be inclusives. Then, check if the adjustment
		 * is really needed: if the values of both ranges are inclusive or
		 * exclusive, then there is no need for an adjustment before computing
		 * the coefficient of a linear relation.
		 */
		int sMinInc = sourceRange.isMinIncluded() ? 0 : +1;
		int sMaxInc = sourceRange.isMaxIncluded() ? 0 : -1;
		int dMinInc = destinationRange.isMinIncluded() ? 0 : +1;
		int dMaxInc = destinationRange.isMaxIncluded() ? 0 : -1;

		/*
		 * Now, extracts the minimal and maximal values and computes the linear
		 * coefficients.
		 */
		final double minSource = doubleValue(sType, sourceRange.getMinValue(),
				sMinInc);
		final double maxSource = doubleValue(sType, sourceRange.getMaxValue(),
				sMaxInc);
		final double minDestination = doubleValue(dType, destinationRange
				.getMinValue(), dMinInc);
		final double maxDestination = doubleValue(dType, destinationRange
				.getMaxValue(), dMaxInc);

		// /////////////////////////////////////////////////////////////////
		//
		// optimizations
		//
		// /////////////////////////////////////////////////////////////////
		// //
		//
		// If the output range is a single value let's create a constant
		// transform
		//
		// //
		if (PiecewiseUtilities.compare(minDestination, maxDestination) == 0)
			return LinearTransform1D.create(0, minDestination);

		// //
		//
		// If the input range is a single value this transform ca be created
		// only if we map to another single value
		//
		// //
		if (PiecewiseUtilities.compare(minSource, maxSource) == 0)
			throw new IllegalArgumentException("Impossible to map a single value to a range.");

		double scale = (maxDestination - minDestination)
				/ (maxSource - minSource);
		// /////////////////////////////////////////////////////////////////
		//
		// Take into account the fact that the maxSample and the minSample can
		// be
		// similar hence we have a constant transformation.
		//
		// /////////////////////////////////////////////////////////////////
		if (Double.isNaN(scale))
			scale = 0;
		final double offset = minDestination - scale * minSource;
		return createLinearTransform1D(scale, offset);
	}

	
}
