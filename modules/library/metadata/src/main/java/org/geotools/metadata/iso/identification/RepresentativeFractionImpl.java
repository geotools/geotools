/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.identification;

import org.opengis.metadata.identification.RepresentativeFraction;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * A scale where {@linkplain #getDenominator denominator} = {@code 1 / scale}.
 * This implementation is set up as a {@linkplain Number number} - because it is.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 *
 * @since 2.4
 */
public class RepresentativeFractionImpl extends Number implements RepresentativeFraction {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 7228422109144637537L;

    /**
     * The number below the line in a vulgar fraction.
     */
    private long denominator;

    /**
     * Default empty constructor.
     */
    public RepresentativeFractionImpl() {
    }

    /**
     * Creates a new representative fraction from the specified denominator.
     *
     * @param denominator The denominator.
     */
    public RepresentativeFractionImpl(final long denominator) {
        this.denominator = denominator;
    }

    /**
     * Creates a representative fraction from a scale as a {@code double} value.
     * The {@linkplain #getDenominator denominator} will be set to {@code 1/scale}.
     *
     * @param  scale The scale as a number between 0 and 1.
     * @return The representative fraction created from the given scale.
     * @throws IllegalArgumentException if the condition {@code abs(scale) <= 1} is not meet.
     */
    public static RepresentativeFraction fromScale(final double scale)
            throws IllegalArgumentException
    {
        if (Math.abs(scale) <= 1 || scale == Double.POSITIVE_INFINITY) {
            // Note: we accept positive infinity, but not negative infinity because
            //       we can't represent a negative zero using 'long' primitive type.
            return new RepresentativeFractionImpl(Math.round(1.0 / scale)); // flip!
        } else {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "scale", scale));
        }
    }

    /**
     * @deprecated This is equivalent to {@link #doubleValue}.
     */
    @Deprecated
    public double toScale(){
        return doubleValue();
    }

    /**
     * Returns the scale in a form usable for computation.
     *
     * @return <code>1.0 / {@linkplain #getDenominator() denominator}</code>
     */
    public double doubleValue() {
        return 1.0 / (double) denominator;
    }

    /**
     * Returns the scale as a {@code float} type.
     */
    public float floatValue() {
        return 1.0f / (float) denominator;
    }

    /**
     * Returns the scale as an integer. This method returns 0, 1 or throws an exception
     * as specified in {@link #intValue}.
     *
     * @throws ArithmeticException if the {@linkplain #getDenominator denominator} is 0.
     */
    public long longValue() throws ArithmeticException {
        return intValue();
    }

    /**
     * Returns the scale as an integer. If the denominator is 0, then this method throws an
     * {@link ArithmeticException} since infinities can not be represented by an integer.
     * Otherwise if the denominator is 1, then this method returns 1. Otherwise returns 0
     * 0 since the scale is a fraction between 0 and 1, and such value can not be represented
     * as an integer.
     *
     * @throws ArithmeticException if the {@linkplain #getDenominator denominator} is 0.
     */
    public int intValue() throws ArithmeticException {
        if (denominator == 1) {
            return 1;
        } else if (denominator != 0) {
            return 0;
        } else {
            throw new ArithmeticException();
        }
    }

    /**
     * Returns the number below the line in a vulgar fraction.
     */
    public long getDenominator() {
        return denominator;
    }

    /**
     * Sets the denominator value.
     *
     * @param denominator The new denominator value.
     */
    public void setDenominator(final long denominator) {
        this.denominator = denominator;
    }

    /**
     * Compares this object with the specified value for equality.
     *
     * @param object The object to compare with.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final Object object) {
        /*
         * Note: 'equals(Object)' and 'hashCode()' implementations are defined in the interface,
         * in order to ensure that the following requirements hold:
         *
         * - a.equals(b) == b.equals(a)   (reflexivity)
         * - a.equals(b) implies (a.hashCode() == b.hashCode())
         */
        if (object instanceof RepresentativeFraction) {
            final RepresentativeFraction that = (RepresentativeFraction) object;
            return denominator == that.getDenominator();
        }
        return false;
    }

    /**
     * Returns a hash value for this representative fraction.
     */
    @Override
    public int hashCode() {
        return (int) denominator;
    }
}
