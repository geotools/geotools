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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.util.NumberRange;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.NoninvertibleTransformException;

/**
 * Convenience class for linear transformations that maps an interval to another interval.
 * @author  Simone Giannecchini, GeoSolutions
 *
 *
 * @source $URL$
 */
public class DefaultLinearPiecewiseTransform1DElement extends DefaultPiecewiseTransform1DElement implements
		PiecewiseTransform1DElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4026834241134908025L;

	private final static Logger LOGGER = Logging
			.getLogger("org.geotools.referencing.piecewise.DefaultLinearPiecewiseTransform1DElement");

	/**
     * @uml.property  name="outputMaximum"
     */
	private double outputMaximum;
	/**
     * @uml.property  name="outputMinimum"
     */
	private double outputMinimum;
	/**
     * @uml.property  name="outputRange"
     */
	private NumberRange<? extends Number> outputRange;
	/**
     * @uml.property  name="outputMinimumNaN"
     */
	private boolean outputMinimumNaN;
	/**
     * @uml.property  name="outputMaximumNaN"
     */
	private boolean outputMaximumNaN;
	/**
     * @uml.property  name="outputMinimumInfinite"
     */
	private boolean outputMinimumInfinite;
	/**
     * @uml.property  name="outputMaximumInfinite"
     */
	private boolean outputMaximumInfinite;



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
	public DefaultLinearPiecewiseTransform1DElement(CharSequence name, NumberRange<? extends Number> inRange,
			NumberRange<? extends Number> outRange) {
		super(name, inRange);
		this.outputRange = outRange;
		// /////////////////////////////////////////////////////////////////////
		//
		// Checks
		//
		// /////////////////////////////////////////////////////////////////////
		// //
		//
		// the output class can only be integer
		//
		// //
		final Class<? extends Number> type = outRange.getElementClass();
		boolean minInc = outRange.isMinIncluded();
		boolean maxInc = outRange.isMaxIncluded();
		outputMinimum = PiecewiseUtilities.doubleValue(type, outRange
				.getMinValue(), minInc ? 0 : +1);
		outputMaximum = PiecewiseUtilities.doubleValue(type, outRange
				.getMaxValue(), maxInc ? 0 : -1);
		outputMinimumNaN = Double.isNaN(outputMinimum);
		outputMaximumNaN = Double.isNaN(outputMaximum);
		outputMinimumInfinite = Double.isInfinite(outputMinimum);
		outputMaximumInfinite = Double.isInfinite(outputMaximum);

		// //
		//
		// No open intervals for the output range
		//
		// //
		if (outputMinimumInfinite || outputMinimumInfinite) {
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.BAD_RANGE_$2, outputRange.getMinValue(),
					outputRange.getMaxValue()));
		}

		final int compareOutBounds = PiecewiseUtilities.compare(outputMinimum,
				outputMaximum);
		// //
		//
		// the output values are correctly ordered
		//
		// //
		if (compareOutBounds > 0) {
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.BAD_RANGE_$2, outputRange.getMinValue(),
					outputRange.getMaxValue()));
		}

		// //
		//
		// mapping NaN to a single value
		//
		// //
		if (isInputMaximumNaN() && isInputMinimumNaN())
			if (compareOutBounds == 0) {
				setTransform(LinearTransform1D.create(0, outputMinimum));
				setInverse(LinearTransform1D.create(outputMinimum, 0));
				return;
			} else
				throw new IllegalArgumentException(Errors.format(
						ErrorKeys.BAD_RANGE_$2, outputRange.getMinValue(),
						outputRange.getMaxValue()));

		// //
		//
		// Mapping an open interval to a single value, there is no way to map an
		// open interval to another interval!
		//
		// //
		if (isInputMaximumInfinite() || isInputMinimumInfinite())
			if (compareOutBounds == 0) {
				setTransform(PiecewiseUtilities.createLinearTransform1D(0,
						outputMinimum));
				setInverse(null);
				return;
			} else
				throw new IllegalArgumentException(Errors.format(
						ErrorKeys.BAD_RANGE_$2, outputRange.getMinValue(),
						outputRange.getMaxValue()));

		final MathTransform1D transform = PiecewiseUtilities.createLinearTransform1D(inRange,
				NumberRange.create(outputMinimum, outputMaximum));
		setTransform(transform);

		// //
		//
		// Checking the created transformation
		//
		// //
		assert transform instanceof LinearTransform1D;
		assert !Double.isNaN(((LinearTransform1D) transform).scale)
				&& !Double
						.isInfinite(((LinearTransform1D) transform).scale);

		// //
		//
		// Inverse
		//
		// //
		LinearTransform1D tempTransform = (LinearTransform1D) transform;
		final double scale = tempTransform.scale;
		if (Math.abs(scale) < 1E-6)
			if (PiecewiseUtilities.compare(getInputMaximum(), getInputMinimum()) == 0)
				setInverse(LinearTransform1D.create(0, getInputMinimum()));
			else
				setInverse(null);
		else
			try {
				setInverse((MathTransform1D) transform.inverse());
			} catch (NoninvertibleTransformException e) {
				if (LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
	}

	/**
     * Returns the maximum output values for this  {@link DefaultLinearPiecewiseTransform1DElement}  ;
     * @return  the maximum output values for this  {@link DefaultLinearPiecewiseTransform1DElement}  ;
     * @uml.property  name="outputMaximum"
     */
	public double getOutputMaximum() {
		return outputMaximum;
	}

	/**
     * Returns the minimum output values for this  {@link DefaultLinearPiecewiseTransform1DElement}  ;
     * @return  the minimum output values for this  {@link DefaultLinearPiecewiseTransform1DElement}  ;
     * @uml.property  name="outputMinimum"
     */
	public double getOutputMinimum() {
		return outputMinimum;
	}

	/**
     * Returns the range for the output values for this {@link DefaultLinearPiecewiseTransform1DElement}  ;
     * @return  the range for the output values for this {@link DefaultLinearPiecewiseTransform1DElement}  ;
     * @uml.property  name="outputRange"
     */
	public NumberRange<? extends Number> getOutputRange() {
		return outputRange;
	}

	/**
     * Tells me if the lower boundary of the output range is NaN
     * @return  <code>true</code> if the lower boundary of the output range is  NaN, <code>false</code> otherwise.
     * @uml.property  name="outputMinimumNaN"
     */
	public boolean isOutputMinimumNaN() {
		return outputMinimumNaN;
	}

	/**
     * Tells me if the upper boundary of the output range is NaN
     * @return  <code>true</code> if the upper boundary of the output range is  NaN, <code>false</code> otherwise.
     * @uml.property  name="outputMaximumNaN"
     */
	public boolean isOutputMaximumNaN() {
		return outputMaximumNaN;
	}

	/**
     * Tells me if the lower boundary of the output range is infinite
     * @return  <code>true</code> if the lower boundary of the output range is  infinite, <code>false</code> otherwise.
     * @uml.property  name="outputMinimumInfinite"
     */
	public boolean isOutputMinimumInfinite() {
		return outputMinimumInfinite;
	}

	/**
     * Tells me if the upper boundary of the output range is infinite
     * @return  <code>true</code> if the upper boundary of the output range is  infinite, <code>false</code> otherwise.
     * @uml.property  name="outputMaximumInfinite"
     */
	public boolean isOutputMaximumInfinite() {
		return outputMaximumInfinite;
	}
	
	/**
	 * Retrieves the scale factor for this linear {@link PiecewiseTransform1DElement}.
	 * 
	 * @return the scale factor for this linear {@link PiecewiseTransform1DElement}.
	 */
	public double getScale(){
		//get the transform at this point it is linear for sure
		final LinearTransform1D transform= (LinearTransform1D) getTransform();
		return transform.scale;
		
	}
	
	/**
	 * Retrieves the offset factor for this linear {@link PiecewiseTransform1DElement}.
	 * 
	 * @return the offset factor for this linear {@link PiecewiseTransform1DElement}.
	 */
	public double getOffset(){
		//get the transform at this point it is linear for sure
		final LinearTransform1D transform= (LinearTransform1D) getTransform();
		return transform.offset;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.geotools.referencing.piecewise.DefaultPiecewiseTransform1DElement#toString()
	 */
	public String toString() {
		final StringBuilder buffer= new StringBuilder(super.toString());
		buffer.append("\n").append("output range=").append(this.outputRange);
		return buffer.toString();
	}

    protected Class<?> getEquivalenceClass(){
        return DefaultLinearPiecewiseTransform1DElement.class;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(!(obj instanceof DefaultLinearPiecewiseTransform1DElement))
            return false;
        final DefaultLinearPiecewiseTransform1DElement that= (DefaultLinearPiecewiseTransform1DElement) obj;
        if(that.getEquivalenceClass()!=this.getEquivalenceClass())
            return false;
        if(!Utilities.equals(outputRange, that.outputRange))
            return false;
        if(!Utilities.equals(outputMaximum, that.outputMaximum))
            return false;
        if(!Utilities.equals(outputMinimum, that.outputMinimum))
            return false;
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hashCode=37;
        hashCode=Utilities.hash(outputRange,hashCode);
        hashCode=Utilities.hash( outputMaximum,hashCode);
        hashCode=Utilities.hash( outputMinimum,hashCode);
        hashCode=Utilities.hash( super.hashCode(),hashCode);
        return hashCode;
    }
}
