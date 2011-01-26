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

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.opengis.parameter.InvalidParameterTypeException;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.util.CodeList;

import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.Classes;
import org.geotools.measure.Units;
import org.geotools.util.Utilities;


/**
 * A parameter value used by an operation method.
 * Most CRS parameter values are numeric, but other types of parameter values are possible.
 * The parameter type can be fetch with the
 * <code>{@linkplain #getValue()}.{@linkplain Object#getClass() getClass()}</code> idiom.
 * The {@link #getValue()} and {@link #setValue(Object)} methods can be invoked at any time.
 * Others getters and setters are parameter-type dependents.
 *
 * @param <T> The value type.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Jody Garnett (Refractions Research)
 *
 * @see DefaultParameterDescriptor
 * @see ParameterGroup
 */
public class Parameter<T> extends AbstractParameter implements ParameterValue<T> {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5837826787089486776L;

    /**
     * The value.
     */
    private T value;

    /**
     * The unit of measure for the value, or {@code null} if it doesn't apply.
     */
    private Unit<?> unit;

//    /**
//     * Constructs a parameter from the specified name and value. This convenience
//     * constructor creates a {@link DefaultParameterDescriptor} object. But if such
//     * an object was available, then the preferred way to get a {@code ParameterValue}
//     * is to invokes {@link ParameterDescriptor#createValue}.
//     *
//     * @param name  The parameter name.
//     * @param value The parameter value.
//     *
//     * @deprecated This constructor can not ensure type safety with parameterized types.
//     *             Use the static {@code create} methods instead.
//     */
//    @Deprecated
//    public Parameter(final String name, final int value) {
//        this(DefaultParameterDescriptor.create(name, 0, Integer.MIN_VALUE, Integer.MAX_VALUE));
//        this.value = (T) (Object) value;
//    }
//
//    /**
//     * Constructs a parameter from the specified name and value. This convenience
//     * constructor creates a {@link DefaultParameterDescriptor} object. But if such
//     * an object was available, then the preferred way to get a {@code ParameterValue} is
//     * to invokes {@link ParameterDescriptor#createValue}.
//     *
//     * @param name  The parameter name.
//     * @param value The parameter value.
//     * @param unit  The unit for the parameter value.
//     *
//     * @deprecated This constructor can not ensure type safety with parameterized types.
//     *             Use the static {@code create} methods instead.
//     */
//    @Deprecated
//    public Parameter(final String name, final double value, final Unit<?> unit) {
//        this(DefaultParameterDescriptor.create(name, Double.NaN, Double.NEGATIVE_INFINITY,
//                                            Double.POSITIVE_INFINITY, normalize(unit)));
//        this.value = (T) (Object) value;
//        this.unit  = unit;
//    }
//
//    /**
//     * Constructs a parameter from the specified enumeration. This convenience
//     * constructor creates a {@link DefaultParameterDescriptor} object. But if
//     * such an object was available, then the preferred way to get a {@code ParameterValue}
//     * is to invokes {@link ParameterDescriptor#createValue}.
//     *
//     * @param name  The parameter name.
//     * @param value The parameter value.
//     *
//     * @deprecated This constructor can not ensure type safety with parameterized types.
//     *             Use the static {@code create} methods instead.
//     */
//    @Deprecated
//    public Parameter(final String name, final CodeList value) {
//        this(new DefaultParameterDescriptor(name, value.getClass(), null,(CodeList)null));
//        this.value = (T) (Object) value;
//    }

    /**
     * Constructs a parameter value from the specified descriptor.
     * The value will be initialized to the default value, if any.
     *
     * @param descriptor The abstract definition of this parameter.
     */
    public Parameter(final ParameterDescriptor<T> descriptor) {
        super(descriptor);
        value = descriptor.getDefaultValue();
        unit  = descriptor.getUnit();
    }

    /**
     * Constructs a parameter value from the specified descriptor and value.
     *
     * @param  descriptor The abstract definition of this parameter.
     * @param  value The parameter value.
     * @throws InvalidParameterValueException if the type of {@code value} is inappropriate
     *         for this parameter, or if the value is illegal for some other reason (for example
     *         the value is numeric and out of range).
     */
    public Parameter(final ParameterDescriptor<T> descriptor, final T value)
            throws InvalidParameterValueException
    {
        super(descriptor);
        unit = descriptor.getUnit();
        setValue(value);
    }

    /**
     * Constructs a parameter from the specified name and value. This convenience
     * constructor creates a {@link DefaultParameterDescriptor} object. But if such
     * an object is available, then the preferred way to get a {@code ParameterValue}
     * is to invoke {@link ParameterDescriptor#createValue}.
     *
     * @param  name  The parameter name.
     * @param  value The parameter value.
     * @return A new parameter instance for the given name and value.
     *
     * @since 2.5
     */
    public static Parameter<Integer> create(final String name, final int value) {
        final ParameterDescriptor<Integer> descriptor = DefaultParameterDescriptor.create(
                name, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        final Parameter<Integer> parameter = new Parameter<Integer>(descriptor);
        parameter.value = value;
        return parameter;
    }

    /**
     * Constructs a parameter from the specified name and value. This convenience
     * constructor creates a {@link DefaultParameterDescriptor} object. But if such
     * an object is available, then the preferred way to get a {@code ParameterValue}
     * is to invoke {@link ParameterDescriptor#createValue}.
     *
     * @param name  The parameter name.
     * @param value The parameter value.
     * @param unit  The unit for the parameter value.
     * @return A new parameter instance for the given name and value.
     *
     * @since 2.5
     */
    public static Parameter<Double> create(final String name, final double value, Unit<?> unit) {
        // Normalizes the specified unit into one of "standard" units used in projections.
        if (unit != null) {
                 if (SI.METER          .isCompatible(unit)) unit = SI.METER;
            else if (NonSI.DAY         .isCompatible(unit)) unit = NonSI.DAY;
            else if (NonSI.DEGREE_ANGLE.isCompatible(unit)) unit = NonSI.DEGREE_ANGLE;
        }
        final ParameterDescriptor<Double> descriptor = DefaultParameterDescriptor.create(
                name, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, unit);
        final Parameter<Double> parameter = new Parameter<Double>(descriptor);
        parameter.value = value;
        parameter.unit  = unit;
        return parameter;
    }

    /**
     * Constructs a parameter from the specified code list. This convenience
     * constructor creates a {@link DefaultParameterDescriptor} object. But if
     * such an object is available, then the preferred way to get a {@code ParameterValue}
     * is to invoke {@link ParameterDescriptor#createValue}.
     *
     * @param  <T>   The parameter type.
     * @param  name  The parameter name.
     * @param  type  The parameter type.
     * @param  value The parameter value.
     * @return A new parameter instance for the given name and value.
     *
     * @since 2.5
     */
    public static <T extends CodeList> Parameter<T> create(
            final String name, final Class<T> type, final T value)
    {
        final ParameterDescriptor<T> descriptor =
                DefaultParameterDescriptor.create(name, null, type, null, true);
        final Parameter<T> parameter = new Parameter<T>(descriptor);
        parameter.value = value;
        return parameter;
    }

    /**
     * Ensures that the given value is valid according the specified parameter descriptor.
     * This convenience method ensures that {@code value} is assignable to the
     * {@linkplain ParameterDescriptor#getValueClass expected class}, is between the
     * {@linkplain ParameterDescriptor#getMinimumValue minimum} and
     * {@linkplain ParameterDescriptor#getMaximumValue maximum} values and is one of the
     * {@linkplain ParameterDescriptor#getValidValues set of valid values}.
     * If the value fails any of those tests, then an exception is thrown.
     * <p>
     * This method is similar to <code>{@linkplain Parameters#isValid
     * Parameters#isValid}(descriptor, value)</code> except that the exception contains an
     * error message formatted with a description of the failure reason.
     *
     * @param  <T> The type of parameter value. The given {@code value} should typically be an
     *         instance of this class. This is not required by this method signature but is
     *         checked by this method implementation.
     * @param  descriptor The parameter descriptor to check against.
     * @param  value The value to check, or {@code null}.
     * @return The value casted to the descriptor parameterized type.
     * @throws InvalidParameterValueException if the parameter value is invalid.
     */
    public static <T> T ensureValidValue(final ParameterDescriptor<T> descriptor, final Object value)
            throws InvalidParameterValueException
    {
        if (value == null) {
            return null;
        }
        final String error;
        final Class<T> type = descriptor.getValueClass();
        if (!type.isInstance(value)) {
            error = Errors.format(ErrorKeys.ILLEGAL_OPERATION_FOR_VALUE_CLASS_$1, Classes.getClass(value));
        } else {
            @SuppressWarnings("unchecked") // Type checked in the above test case.
            final Comparable<Object> minimum = (Comparable) descriptor.getMinimumValue();
            @SuppressWarnings("unchecked")
            final Comparable<Object> maximum = (Comparable) descriptor.getMaximumValue();
            if ((minimum != null && minimum.compareTo(value) > 0) ||
                (maximum != null && maximum.compareTo(value) < 0))
            {
                error = Errors.format(ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, value, minimum, maximum);
            } else {
                final Set<?> validValues = descriptor.getValidValues();
                if (validValues!=null && !validValues.contains(value)) {
                    error = Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, getName(descriptor), value);
                } else {
                    return type.cast(value);
                }
            }
        }
        throw new InvalidParameterValueException(error, getName(descriptor), value);
    }

    /**
     * Format an error message for illegal method call for the current value type.
     */
    private String getClassTypeError() {
        return Errors.format(ErrorKeys.ILLEGAL_OPERATION_FOR_VALUE_CLASS_$1,
               ((ParameterDescriptor) descriptor).getValueClass());
    }

    /**
     * Returns the abstract definition of this parameter.
     */
    @Override
    @SuppressWarnings("unchecked") // Type checked by the constructor.
    public ParameterDescriptor<T> getDescriptor() {
        return (ParameterDescriptor) super.getDescriptor();
    }

    /**
     * Returns the unit of measure of the {@linkplain #doubleValue() parameter value}.
     * If the parameter value has no unit (for example because it is a {@link String} type),
     * then this method returns {@code null}. Note that "no unit" doesn't means
     * "dimensionless".
     *
     * @return The unit of measure, or {@code null} if none.
     *
     * @see #doubleValue()
     * @see #doubleValueList()
     * @see #getValue
     */
    public Unit<?> getUnit() {
        return unit;
    }

    /**
     * Returns the unit type as one of error message code. Used for checking unit with a better
     * error message formatting if needed.
     * <p>
     * Note: It is difficult to differentiate scale and angular units, since both of them are
     *       dimensionless. However, in EPSG database version 6.7, there is only 3 scale units
     *       and all of them maps to {@link Unit#ONE} or {@link Units#PPM}. Consequently, they
     *       are hard-coded and treated especially by this method.
     *
     * @todo Provides a better way to differentiate scale units (currently Unit.ONE)
     *       and angular units. Both are dimensionless...
     */
    static int getUnitMessageID(final Unit<?> unit) {
        // Note: ONE must be tested before RADIAN.
        if (Unit.ONE .equals      (unit) ||
            Units.PPM.equals      (unit)) return ErrorKeys.NON_SCALE_UNIT_$1;
        if (SI.METER .isCompatible(unit)) return ErrorKeys.NON_LINEAR_UNIT_$1;
        if (SI.SECOND.isCompatible(unit)) return ErrorKeys.NON_TEMPORAL_UNIT_$1;
        if (SI.RADIAN.isCompatible(unit)) return ErrorKeys.NON_ANGULAR_UNIT_$1;
        return ErrorKeys.INCOMPATIBLE_UNIT_$1;
    }

    /**
     * Returns the numeric value of the coordinate operation parameter in the specified unit
     * of measure. This convenience method apply unit conversion on the fly as needed.
     *
     * @param  unit The unit of measure for the value to be returned.
     * @return The numeric value represented by this parameter after conversion to type
     *         {@code double} and conversion to {@code unit}.
     * @throws InvalidParameterTypeException if the value is not a numeric type.
     * @throws IllegalArgumentException if the specified unit is invalid for this parameter.
     *
     * @see #getUnit
     * @see #setValue(double,Unit)
     * @see #doubleValueList(Unit)
     */
    public double doubleValue(final Unit<?> unit) throws InvalidParameterTypeException {
        if (this.unit == null) {
            throw unitlessParameter(descriptor);
        }
        ensureNonNull("unit", unit);
        final int expectedID = getUnitMessageID(this.unit);
        if (getUnitMessageID(unit) != expectedID) {
            throw new IllegalArgumentException(Errors.format(expectedID, unit));
        }
        return this.unit.getConverterTo(unit).convert(doubleValue());
    }

    /**
     * Returns the numeric value of the coordinate operation parameter with its
     * associated {@linkplain #getUnit unit of measure}.
     *
     * @return The numeric value represented by this parameter after conversion to type {@code double}.
     * @throws InvalidParameterTypeException if the value is not a numeric type.
     *
     * @see #getUnit
     * @see #setValue(double)
     * @see #doubleValueList()
     */
    public double doubleValue() throws InvalidParameterTypeException {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        final String name = getName(descriptor);
        if (value == null) {
            // This is the kind of exception expected by org.geotools.referencing.wkt.Formatter.
            throw new IllegalStateException(Errors.format(ErrorKeys.MISSING_PARAMETER_$1, name));
        }
        // Reminder: the following is a specialization of IllegalStateException.
        throw new InvalidParameterTypeException(getClassTypeError(), name);
    }

    /**
     * Returns the positive integer value of an operation parameter, usually used
     * for a count. An integer value does not have an associated unit of measure.
     *
     * @return The numeric value represented by this parameter after conversion to type {@code int}.
     * @throws InvalidParameterTypeException if the value is not an integer type.
     *
     * @see #setValue(int)
     * @see #intValueList
     */
    public int intValue() throws InvalidParameterTypeException {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        final String name = getName(descriptor);
        if (value == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.MISSING_PARAMETER_$1, name));
        }
        throw new InvalidParameterTypeException(getClassTypeError(), name);
    }

    /**
     * Returns the boolean value of an operation parameter.
     * A boolean value does not have an associated unit of measure.
     *
     * @return The boolean value represented by this parameter.
     * @throws InvalidParameterTypeException if the value is not a boolean type.
     *
     * @see #setValue(boolean)
     */
    public boolean booleanValue() throws InvalidParameterTypeException {
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        final String name = getName(descriptor);
        if (value == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.MISSING_PARAMETER_$1, name));
        }
        throw new InvalidParameterTypeException(getClassTypeError(), name);
    }

    /**
     * Returns the string value of an operation parameter.
     * A string value does not have an associated unit of measure.
     *
     * @return The string value represented by this parameter.
     * @throws InvalidParameterTypeException if the value is not a string.
     *
     * @see #getValue
     * @see #setValue(Object)
     */
    public String stringValue() throws InvalidParameterTypeException {
        if (value instanceof CharSequence) {
            return value.toString();
        }
        final String name = getName(descriptor);
        if (value == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.MISSING_PARAMETER_$1, name));
        }
        throw new InvalidParameterTypeException(getClassTypeError(), name);
    }

    /**
     * Returns an ordered sequence of numeric values in the specified unit of measure.
     * This convenience method apply unit conversion on the fly as needed.
     *
     * @param  unit The unit of measure for the value to be returned.
     * @return The sequence of values represented by this parameter after conversion to type
     *         {@code double} and conversion to {@code unit}.
     * @throws InvalidParameterTypeException if the value is not an array of {@code double}s.
     * @throws IllegalArgumentException if the specified unit is invalid for this parameter.
     *
     * @see #getUnit
     * @see #setValue(double[],Unit)
     * @see #doubleValue(Unit)
     */
    public double[] doubleValueList(final Unit<?> unit) throws InvalidParameterTypeException {
        if (this.unit == null) {
            throw unitlessParameter(descriptor);
        }
        ensureNonNull("unit", unit);
        final int expectedID = getUnitMessageID(this.unit);
        if (getUnitMessageID(unit) != expectedID) {
            throw new IllegalArgumentException(Errors.format(expectedID, unit));
        }
        final UnitConverter converter = this.unit.getConverterTo(unit);
        final double[] values = doubleValueList().clone();
        for (int i=0; i<values.length; i++) {
            values[i] = converter.convert(values[i]);
        }
        return values;
    }

    /**
     * Returns an ordered sequence of two or more numeric values of an operation parameter
     * list, where each value has the same associated {@linkplain Unit unit of measure}.
     *
     * @return The sequence of values represented by this parameter.
     * @throws InvalidParameterTypeException if the value is not an array of {@code double}s.
     *
     * @see #getUnit
     * @see #setValue(Object)
     * @see #doubleValue()
     */
    public double[] doubleValueList() throws InvalidParameterTypeException {
        if (value instanceof double[]) {
            return (double[]) value;
        }
        final String name = getName(descriptor);
        if (value == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.MISSING_PARAMETER_$1, name));
        }
        throw new InvalidParameterTypeException(getClassTypeError(), name);
    }

    /**
     * Returns an ordered sequence of two or more integer values of an operation parameter list,
     * usually used for counts. These integer values do not have an associated unit of measure.
     *
     * @return The sequence of values represented by this parameter.
     * @throws InvalidParameterTypeException if the value is not an array of {@code int}s.
     *
     * @see #setValue(Object)
     * @see #intValue
     */
    public int[] intValueList() throws InvalidParameterTypeException {
        if (value instanceof int[]) {
            return (int[]) value;
        }
        final String name = getName(descriptor);
        if (value == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.MISSING_PARAMETER_$1, name));
        }
        throw new InvalidParameterTypeException(getClassTypeError(), name);
    }

    /**
     * Returns a reference to a file or a part of a file containing one or more parameter
     * values. When referencing a part of a file, that file must contain multiple identified
     * parts, such as an XML encoded document. Furthermore, the referenced file or part of a
     * file can reference another part of the same or different files, as allowed in XML documents.
     *
     * @return The reference to a file containing parameter values.
     * @throws InvalidParameterTypeException if the value is not a reference to a file or an URI.
     *
     * @see #getValue
     * @see #setValue(Object)
     */
    public URI valueFile() throws InvalidParameterTypeException {
        if (value instanceof URI) {
            return (URI) value;
        }
        if (value instanceof File) {
            return ((File) value).toURI();
        }
        Exception cause = null;
        try {
            if (value instanceof URL) {
                return ((URL) value).toURI();
            }
            if (value instanceof String) {
                return new URI((String) value);
            }
        } catch (URISyntaxException exception) {
            cause = exception;
        }
        /*
         * Value can't be converted.
         */
        final String name = getName(descriptor);
        if (value == null) {
            throw new IllegalStateException(Errors.format(ErrorKeys.MISSING_PARAMETER_$1, name));
        }
        final InvalidParameterTypeException exception =
                new InvalidParameterTypeException(getClassTypeError(), name);
        if (cause != null) {
            exception.initCause(cause);
        }
        throw exception;
    }

    /**
     * Returns the parameter value as an object. The object type is typically a {@link Double},
     * {@link Integer}, {@link Boolean}, {@link String}, {@link URI}, {@code double[]} or
     * {@code int[]}.
     *
     * @return The parameter value as an object.
     *
     * @see #setValue(Object)
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the parameter value as a floating point and its associated unit.
     *
     * @param  value The parameter value.
     * @param  unit The unit for the specified value.
     * @throws InvalidParameterValueException if the floating point type is inappropriate for this
     *         parameter, or if the value is illegal for some other reason (for example a value out
     *         of range).
     *
     * @see #setValue(double)
     * @see #doubleValue(Unit)
     */
    public void setValue(final double value, final Unit<?> unit)
            throws InvalidParameterValueException
    {
        ensureNonNull("unit", unit);
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<T> descriptor = (ParameterDescriptor) this.descriptor;
        final Unit<?> targetUnit = descriptor.getUnit();
        if (targetUnit == null) {
            throw unitlessParameter(descriptor);
        }
        final int expectedID = getUnitMessageID(targetUnit);
        if (getUnitMessageID(unit) != expectedID) {
            throw new InvalidParameterValueException(Errors.format(expectedID, unit),
                      descriptor.getName().getCode(), value);
        }
        final Double converted = unit.getConverterTo(targetUnit).convert(value);
        ensureValidValue(descriptor, converted);
        // Really store the original value, not the converted one,
        // because we store the unit as well.
        this.value = descriptor.getValueClass().cast(value);
        this.unit  = unit;
    }

    /**
     * Sets the parameter value as a floating point.
     * The unit, if any, stay unchanged.
     *
     * @param value The parameter value.
     * @throws InvalidParameterValueException if the floating point type is inappropriate for this
     *         parameter, or if the value is illegal for some other reason (for example a value out
     *         of range).
     *
     * @see #setValue(double,Unit)
     * @see #doubleValue()
     */
    public void setValue(final double value) throws InvalidParameterValueException {
        final Double check = value;
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<T> descriptor = (ParameterDescriptor) this.descriptor;
        this.value = ensureValidValue(descriptor, check);
    }

    /**
     * Sets the parameter value as an integer.
     *
     * @param  value The parameter value.
     * @throws InvalidParameterValueException if the integer type is inappropriate for this parameter,
     *         or if the value is illegal for some other reason (for example a value out of range).
     *
     * @see #intValue
     */
    public void setValue(final int value) throws InvalidParameterValueException {
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<T> descriptor = (ParameterDescriptor) this.descriptor;
        final Class<T> type = descriptor.getValueClass();
        if (Double.class.equals(type) || Double.TYPE.equals(type)) {
            setValue((double) value);
            return;
        }
        final Integer check = value;
        this.value = ensureValidValue(descriptor, check);
    }

    /**
     * Sets the parameter value as a boolean.
     *
     * @param  value The parameter value.
     * @throws InvalidParameterValueException if the boolean type is inappropriate for this parameter.
     *
     * @see #booleanValue
     */
    public void setValue(final boolean value) throws InvalidParameterValueException {
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<T> descriptor = (ParameterDescriptor) this.descriptor;
        final Boolean check = Boolean.valueOf(value);
        this.value = ensureValidValue(descriptor, check);
    }

    /**
     * Set the parameter value as an object. The object type is typically a {@link Double},
     * {@link Integer}, {@link Boolean}, {@link String}, {@link URI}, {@code double[]}
     * or {@code int[]}.
     *
     * @param  value The parameter value.
     * @throws InvalidParameterValueException if the type of {@code value} is inappropriate
     *         for this parameter, or if the value is illegal for some other reason (for example
     *         the value is numeric and out of range).
     *
     * @see #getValue
     */
    public void setValue(final Object value) throws InvalidParameterValueException {
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<T> descriptor = (ParameterDescriptor) this.descriptor;
        this.value = ensureValidValue(descriptor, value);
    }

    /**
     * Set the parameter value as an array of floating point and their associated unit.
     *
     * @param  values The parameter values.
     * @param  unit The unit for the specified value.
     * @throws InvalidParameterValueException if the floating point type is inappropriate for this
     *         parameter, or if the value is illegal for some other reason (for example a value out
     *         of range).
     */
    public void setValue(double[] values, final Unit<?> unit)
            throws InvalidParameterValueException
    {
        ensureNonNull("unit", unit);
        @SuppressWarnings("unchecked") // Checked by constructor.
        final ParameterDescriptor<T> descriptor = (ParameterDescriptor) this.descriptor;
        final Unit<?> targetUnit = descriptor.getUnit();
        if (targetUnit == null) {
            throw unitlessParameter(descriptor);
        }
        final int expectedID = getUnitMessageID(targetUnit);
        if (getUnitMessageID(unit) != expectedID) {
            throw new IllegalArgumentException(Errors.format(expectedID, unit));
        }
        final double[] converted = values.clone();
        final UnitConverter converter = unit.getConverterTo(targetUnit);
        for (int i=0; i<converted.length; i++) {
            converted[i] = converter.convert(converted[i]);
        }
        this.value = ensureValidValue(descriptor, converted);
        this.unit  = unit;
    }

    /**
     * Compares the specified object with this parameter for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final Parameter that = (Parameter) object;
            return Utilities.equals(this.value, that.value) &&
                   Utilities.equals(this.unit,  that.unit);
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
        int code = super.hashCode()*37;
        if (value != null) code +=   value.hashCode();
        if (unit  != null) code += 37*unit.hashCode();
        return code ^ (int)serialVersionUID;
    }

    /**
     * Returns a clone of this parameter.
     */
    @Override
    public Parameter clone() {
        return (Parameter) super.clone();
    }
}
