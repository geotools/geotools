/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.parameter;

import java.net.URI;
import javax.measure.unit.Unit;

import org.opengis.parameter.InvalidParameterTypeException;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A parameter value as a floating point (double precision) number.
 * This class provides the same functionalities than {@link Parameter}, except that:
 * <ul>
 *   <li>Values are always floating point numbers of type {@code double}.</li>
 *   <li>Units are the same than the {@linkplain ParameterDescriptor#getUnit default units}.</li>
 * </ul>
 * When those conditions are meet, {@code ParameterRealValue} is slightly more efficient
 * than {@code ParameterValue} since it avoid the creation of {@link Double} objects.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DefaultParameterDescriptor
 * @see ParameterGroup
 */
public class FloatParameter extends AbstractParameter implements ParameterValue<Double> {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 9027797654033417816L;

    /**
     * The value.
     */
    private double value;

    /**
     * Constructs a parameter from the specified descriptor. The descriptor
     * {@linkplain ParameterDescriptor#getValueClass() value class}
     * must be <code>{@linkplain Double}.class</code>.
     *
     * @param  descriptor The abstract definition of this parameter.
     * @throws IllegalArgumentException if the value class is not {@code Double.class}.
     */
    public FloatParameter(final ParameterDescriptor<Double> descriptor) {
        super(descriptor);
        final Class type = descriptor.getValueClass();
        final Class expected = Double.class;
        if (!expected.equals(type) && !Double.TYPE.equals(type)) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.ILLEGAL_CLASS_$2, type, expected));
        }
        final Number value = (Number) descriptor.getDefaultValue();
        this.value = (value!=null) ? value.doubleValue() : Double.NaN;
    }

    /**
     * Constructs a parameter from the specified descriptor and value. This convenience
     * constructor is equivalents to the one-argument constructor followed by a call to
     * {@link #setValue(double)}.
     *
     * @param  descriptor The abstract definition of this parameter.
     * @param  value The parameter value.
     * @throws IllegalArgumentException if the value class is not {@code Double.class}.
     */
    public FloatParameter(final ParameterDescriptor<Double> descriptor, final double value) {
        this(descriptor);
        setValue(value);
    }

    /**
     * Returns the abstract definition of this parameter.
     */
    @Override
    @SuppressWarnings("unchecked") // Type should has been checked by the constructor.
    public ParameterDescriptor<Double> getDescriptor() {
        return (ParameterDescriptor) super.getDescriptor();
    }

    /**
     * Returns the unit of measure of the {@linkplain #doubleValue() parameter value}. The default
     * implementation always delegates to {@link ParameterDescriptor#getUnit}.
     *
     * @return The unit of measure, or {@code null} if none.
     */
    public Unit<?> getUnit() {
        return ((ParameterDescriptor) descriptor).getUnit();
    }

    /**
     * Returns the numeric value of the coordinate operation parameter in the specified unit
     * of measure. This convenience method apply unit conversion on the fly as needed.
     *
     * @param  unit The unit of measure for the value to be returned.
     * @return The numeric value represented by this parameter after conversion to type
     *         {@code double} and conversion to {@code unit}.
     * @throws IllegalArgumentException if the specified unit is invalid for this parameter.
     */
    public double doubleValue(final Unit<?> unit) throws IllegalArgumentException {
        ensureNonNull("unit", unit);
        final Unit<?> thisUnit = getUnit();
        if (thisUnit == null) {
            throw unitlessParameter(descriptor);
        }
        final int expectedID = Parameter.getUnitMessageID(thisUnit);
        if (Parameter.getUnitMessageID(unit) != expectedID) {
            throw new IllegalArgumentException(Errors.format(expectedID, unit));
        }
        return thisUnit.getConverterTo(unit).convert(value);
    }

    /**
     * Returns the numeric value of the coordinate operation parameter with its
     * associated {@linkplain #getUnit unit of measure}.
     *
     * @return The numeric value represented by this parameter after conversion to type {@code double}.
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Returns the numeric value rounded to the nearest integer.
     *
     * @return The numeric value represented by this parameter after conversion to type {@code int}.
     */
    public int intValue() {
        return (int) Math.round(value);
    }

    /**
     * Returns {@code true} if the value is different from 0, {@code false} otherwise.
     *
     * @return The boolean value represented by this parameter.
     */
    public boolean booleanValue() {
        return value!=0 && !Double.isNaN(value);
    }

    /**
     * Returns the string representation of the value.
     *
     * @return The string value represented by this parameter.
     */
    public String stringValue() {
        return String.valueOf(value);
    }

    /**
     * Wraps the value in an array of length 1.
     *
     * @param  unit The unit of measure for the value to be returned.
     * @return The sequence of values represented by this parameter after conversion to type
     *         {@code double} and conversion to {@code unit}.
     * @throws IllegalArgumentException if the specified unit is invalid for this parameter.
     */
    public double[] doubleValueList(final Unit<?> unit) throws IllegalArgumentException {
        return new double[] {doubleValue(unit)};
    }

    /**
     * Wraps the value in an array of length 1.
     *
     * @return The sequence of values represented by this parameter.
     */
    public double[] doubleValueList() {
        return new double[] {doubleValue()};
    }

    /**
     * Wraps the value in an array of length 1.
     *
     * @return The sequence of values represented by this parameter.
     */
    public int[] intValueList() {
        return new int[] {intValue()};
    }

    /**
     * Always throws an exception, since this parameter is not an URI.
     *
     * @return Never return.
     * @throws InvalidParameterTypeException The value is not a reference to a file or an URI.
     */
    public URI valueFile() throws InvalidParameterTypeException {
        throw new InvalidParameterTypeException(getClassTypeError(), Parameter.getName(descriptor));
    }

    /**
     * Format an error message for illegal method call for the current value type.
     */
    private static String getClassTypeError() {
        return Errors.format(ErrorKeys.ILLEGAL_OPERATION_FOR_VALUE_CLASS_$1, Double.class);
    }

    /**
     * Returns the parameter value as {{@link Double},
     *
     * @return The parameter value as an object.
     */
    public Double getValue() {
        return Double.valueOf(value);
    }

    /**
     * Set the parameter value as a floating point and its associated unit.
     *
     * @param  value The parameter value.
     * @param  unit The unit for the specified value.
     * @throws InvalidParameterValueException if the value is illegal for some reason
     *         (for example a value out of range).
     */
    public void setValue(double value, final Unit<?> unit) throws InvalidParameterValueException {
        ensureNonNull("unit", unit);
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<Double> descriptor = (ParameterDescriptor) this.descriptor;
        final Unit<?> thisUnit = descriptor.getUnit();
        if (thisUnit == null) {
            throw unitlessParameter(descriptor);
        }
        final int expectedID = Parameter.getUnitMessageID(thisUnit);
        if (Parameter.getUnitMessageID(unit) != expectedID) {
            throw new IllegalArgumentException(Errors.format(expectedID, unit));
        }
        value = unit.getConverterTo(thisUnit).convert(value);
        this.value = Parameter.ensureValidValue(descriptor, Double.valueOf(value));
    }

    /**
     * Set the parameter value as a floating point.
     *
     * @param value The parameter value.
     * @throws InvalidParameterValueException if the value is illegal for some reason
     *         (for example a value out of range).
     */
    public void setValue(final double value) throws InvalidParameterValueException {
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<Double> descriptor = (ParameterDescriptor) this.descriptor;
        this.value = Parameter.ensureValidValue(descriptor, Double.valueOf(value));
    }

    /**
     * Set the parameter value as an integer.
     *
     * @param  value The parameter value.
     * @throws InvalidParameterValueException if the value is illegal for some reason
     *         (for example a value out of range).
     */
    public void setValue(final int value) throws InvalidParameterValueException {
        setValue((double) value);
    }

    /**
     * Set the parameter value as a boolean.
     *
     * @param  value The parameter value.
     * @throws InvalidParameterValueException if the boolean type is inappropriate for this parameter.
     */
    public void setValue(final boolean value) throws InvalidParameterValueException {
        setValue(value ? 1.0 : 0.0);
    }

    /**
     * Set the parameter value as a {@link Double} object.
     *
     * @param  value The parameter value.
     * @throws InvalidParameterValueException if the type of {@code value} is inappropriate
     *         for this parameter, or if the value is illegal for some other reason (for example
     *         the value is numeric and out of range).
     */
    public void setValue(final Object value) throws InvalidParameterValueException {
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<Double> descriptor = (ParameterDescriptor) this.descriptor;
        this.value = Parameter.ensureValidValue(descriptor, value);
    }

    /**
     * Always throws an exception, since this parameter is not an array.
     */
    public void setValue(double[] values, final Unit<?> unit)
            throws InvalidParameterValueException
    {
        throw new InvalidParameterTypeException(getClassTypeError(),
                  Parameter.getName(descriptor));
    }

    /**
     * Compares the specified object with this parameter for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            final FloatParameter that = (FloatParameter) object;
            return Double.doubleToLongBits(this.value) ==
                   Double.doubleToLongBits(that.value);
        }
        return false;
    }

    /**
     * Returns a hash value for this parameter.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(value);
        return (int)code ^ (int)(code >>> 32) + super.hashCode()*37;
    }

    /**
     * Returns a clone of this parameter.
     */
    @Override
    public FloatParameter clone() {
        return (FloatParameter) super.clone();
    }
}
