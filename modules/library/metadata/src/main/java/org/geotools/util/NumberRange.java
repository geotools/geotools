/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import org.geotools.resources.XMath;
import org.geotools.resources.ClassChanger;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import static org.geotools.resources.ClassChanger.getFinestClass;
import static org.geotools.resources.ClassChanger.getWidestClass;


/**
 * A range of numbers. {@linkplain #union Union} and {@linkplain #intersect intersection}
 * are computed as usual, except that widening conversions will be applied as needed.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class NumberRange<T extends Number & Comparable<? super T>> extends Range<T> {
    //
    // IMPLEMENTATION NOTE: This class is full of @SuppressWarnings("unchecked") annotations.
    // Nevertheless we should never get ClassCastException - if we get some this is a bug in
    // this implementation. Users may get IllegalArgumentException however.
    //

    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -818167965963008231L;

    /**
     * Constructs an inclusive range of {@code byte} values.
     *
     * @param minimum  The minimum value, inclusive.
     * @param maximum  The maximum value, <strong>inclusive</strong>.
     *
     * @since 2.5
     */
    public static NumberRange<Byte> create(final byte minimum, final byte maximum) {
        return create(minimum, true, maximum, true);
    }

    /** @deprecated Use {@code create} method instead. */
    public NumberRange(final byte minimum, final byte maximum) {
        this(minimum, true, maximum, true);
    }

    /**
     * Constructs a range of {@code byte} values.
     *
     * @param minimum        The minimum value.
     * @param isMinIncluded  Defines whether the minimum value is included in the range.
     * @param maximum        The maximum value.
     * @param isMaxIncluded  Defines whether the maximum value is included in the range.
     *
     * @since 2.5
     */
    public static NumberRange<Byte> create(final byte minimum, final boolean isMinIncluded,
                                           final byte maximum, final boolean isMaxIncluded)
    {
        return new NumberRange<Byte>(Byte.class,
                Byte.valueOf(minimum), isMinIncluded,
                Byte.valueOf(maximum), isMaxIncluded);
    }

    /** @deprecated Use {@code create} method instead. */
    @Deprecated @SuppressWarnings("unchecked")
    public NumberRange(final byte minimum, final boolean isMinIncluded,
                       final byte maximum, final boolean isMaxIncluded)
    {
        super((Class) Byte.class,
                (T) (Object) Byte.valueOf(minimum), isMinIncluded,
                (T) (Object) Byte.valueOf(maximum), isMaxIncluded);
    }

    /**
     * Constructs an inclusive range of {@code short} values.
     *
     * @param minimum  The minimum value, inclusive.
     * @param maximum  The maximum value, <strong>inclusive</strong>.
     *
     * @since 2.5
     */
    public static NumberRange<Short> create(final short minimum, final short maximum) {
        return create(minimum, true, maximum, true);
    }

    /** @deprecated Use {@code create} method instead. */
    public NumberRange(final short minimum, final short maximum) {
        this(minimum, true, maximum, true);
    }

    /**
     * Constructs a range of {@code short} values.
     *
     * @param minimum        The minimum value.
     * @param isMinIncluded  Defines whether the minimum value is included in the range.
     * @param maximum        The maximum value.
     * @param isMaxIncluded  Defines whether the maximum value is included in the range.
     *
     * @since 2.5
     */
    public static NumberRange<Short> create(final short minimum, final boolean isMinIncluded,
                                            final short maximum, final boolean isMaxIncluded)
    {
        return new NumberRange<Short>(Short.class,
                Short.valueOf(minimum), isMinIncluded,
                Short.valueOf(maximum), isMaxIncluded);
    }

    /** @deprecated Use {@code create} method instead. */
    @Deprecated @SuppressWarnings("unchecked")
    public NumberRange(final short minimum, final boolean isMinIncluded,
                       final short maximum, final boolean isMaxIncluded)
    {
        super((Class) Short.class,
                (T) (Object) Short.valueOf(minimum), isMinIncluded,
                (T) (Object) Short.valueOf(maximum), isMaxIncluded);
    }

    /**
     * Constructs an inclusive range of {@code int} values.
     *
     * @param minimum  The minimum value, inclusive.
     * @param maximum  The maximum value, <strong>inclusive</strong>.
     *
     * @since 2.5
     */
    public static NumberRange<Integer> create(final int minimum, final int maximum) {
        return create(minimum, true, maximum, true);
    }

    /** @deprecated Use {@code create} method instead. */
    public NumberRange(final int minimum, final int maximum) {
        this(minimum, true, maximum, true);
    }

    /**
     * Constructs a range of {@code int} values.
     *
     * @param minimum        The minimum value.
     * @param isMinIncluded  Defines whether the minimum value is included in the range.
     * @param maximum        The maximum value.
     * @param isMaxIncluded  Defines whether the maximum value is included in the range.
     *
     * @since 2.5
     */
    public static NumberRange<Integer> create(final int minimum, final boolean isMinIncluded,
                                              final int maximum, final boolean isMaxIncluded)
    {
        return new NumberRange<Integer>(Integer.class,
                Integer.valueOf(minimum), isMinIncluded,
                Integer.valueOf(maximum), isMaxIncluded);
    }

    /** @deprecated Use {@code create} method instead. */
    @Deprecated @SuppressWarnings("unchecked")
    public NumberRange(final int minimum, final boolean isMinIncluded,
                       final int maximum, final boolean isMaxIncluded)
    {
        super((Class) Integer.class,
                (T) (Object) Integer.valueOf(minimum), isMinIncluded,
                (T) (Object) Integer.valueOf(maximum), isMaxIncluded);
    }

    /**
     * Constructs an inclusive range of {@code long} values.
     *
     * @param minimum  The minimum value, inclusive.
     * @param maximum  The maximum value, <strong>inclusive</strong>.
     *
     * @since 2.5
     */
    public static NumberRange<Long> create(final long minimum, final long maximum) {
        return create(minimum, true, maximum, true);
    }

    /** @deprecated Use {@code create} method instead. */
    public NumberRange(final long minimum, final long maximum) {
        this(minimum, true, maximum, true);
    }

    /**
     * Constructs a range of {@code long} values.
     *
     * @param minimum        The minimum value.
     * @param isMinIncluded  Defines whether the minimum value is included in the range.
     * @param maximum        The maximum value.
     * @param isMaxIncluded  Defines whether the maximum value is included in the range.
     *
     * @since 2.5
     */
    public static NumberRange<Long> create(final long minimum, final boolean isMinIncluded,
                                           final long maximum, final boolean isMaxIncluded)
    {
        return new NumberRange<Long>(Long.class,
                Long.valueOf(minimum), isMinIncluded,
                Long.valueOf(maximum), isMaxIncluded);
    }

    /** @deprecated Use {@code create} method instead. */
    @Deprecated @SuppressWarnings("unchecked")
    public NumberRange(final long minimum, final boolean isMinIncluded,
                       final long maximum, final boolean isMaxIncluded)
    {
        super((Class) Long.class,
                (T) (Object) Long.valueOf(minimum), isMinIncluded,
                (T) (Object) Long.valueOf(maximum), isMaxIncluded);
    }

    /**
     * Constructs an inclusive range of {@code float} values.
     *
     * @param minimum  The minimum value, inclusive.
     * @param maximum  The maximum value, <strong>inclusive</strong>.
     *
     * @since 2.5
     */
    public static NumberRange<Float> create(final float minimum, final float maximum) {
        return create(minimum, true, maximum, true);
    }

    /** @deprecated Use {@code create} method instead. */
    public NumberRange(final float minimum, final float maximum) {
        this(minimum, true, maximum, true);
    }

    /**
     * Constructs a range of {@code float} values.
     *
     * @param minimum        The minimum value.
     * @param isMinIncluded  Defines whether the minimum value is included in the range.
     * @param maximum        The maximum value.
     * @param isMaxIncluded  Defines whether the maximum value is included in the range.
     *
     * @since 2.5
     */
    public static NumberRange<Float> create(final float minimum, final boolean isMinIncluded,
                                            final float maximum, final boolean isMaxIncluded)
    {
        return new NumberRange<Float>(Float.class,
                Float.valueOf(minimum), isMinIncluded,
                Float.valueOf(maximum), isMaxIncluded);
    }

    /** @deprecated Use {@code create} method instead. */
    @Deprecated @SuppressWarnings("unchecked")
    public NumberRange(final float minimum, final boolean isMinIncluded,
                       final float maximum, final boolean isMaxIncluded)
    {
        super((Class) Float.class,
                (T) (Object) Float.valueOf(minimum), isMinIncluded,
                (T) (Object) Float.valueOf(maximum), isMaxIncluded);
    }

    /**
     * Constructs an inclusive range of {@code double} values.
     *
     * @param minimum  The minimum value, inclusive.
     * @param maximum  The maximum value, <strong>inclusive</strong>.
     *
     * @since 2.5
     */
    public static NumberRange<Double> create(final double minimum, final double maximum) {
        return create(minimum, true, maximum, true);
    }

    /** @deprecated Use {@code create} method instead. */
    public NumberRange(final double minimum, final double maximum) {
        this(minimum, true, maximum, true);
    }

    /**
     * Constructs a range of {@code double} values.
     *
     * @param minimum        The minimum value.
     * @param isMinIncluded  Defines whether the minimum value is included in the range.
     * @param maximum        The maximum value.
     * @param isMaxIncluded  Defines whether the maximum value is included in the range.
     *
     * @since 2.5
     */
    public static NumberRange<Double> create(final double minimum, final boolean isMinIncluded,
                                             final double maximum, final boolean isMaxIncluded)
    {
        return new NumberRange<Double>(Double.class,
                Double.valueOf(minimum), isMinIncluded,
                Double.valueOf(maximum), isMaxIncluded);
    }

    /** @deprecated Use {@code create} method instead. */
    @Deprecated @SuppressWarnings("unchecked")
    public NumberRange(final double minimum, final boolean isMinIncluded,
                       final double maximum, final boolean isMaxIncluded)
    {
        super((Class) Double.class,
                (T) (Object) Double.valueOf(minimum), isMinIncluded,
                (T) (Object) Double.valueOf(maximum), isMaxIncluded);
    }

    /**
     * Constructs an inclusive range of {@link Comparable} objects.
     * This constructor is used by {@link RangeSet#newRange} only.
     *
     * @param type     The element class, usually one of {@link Byte}, {@link Short},
     *                 {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @param minimum  The minimum value, inclusive.
     * @param maximum  The maximum value, <strong>inclusive</strong>.
     * @throws IllegalArgumentException if at least one argument is not of the expected type.
     */
    @SuppressWarnings("unchecked")
    NumberRange(Class<T> type, Comparable<T> minimum, Comparable<T> maximum)
            throws IllegalArgumentException
    {
        super(type, (T) minimum, (T) maximum);
    }

    /**
     * Constructs an inclusive range of {@link Number} objects.
     *
     * @param  type     The element class, usually one of {@link Byte}, {@link Short},
     *                  {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @param  minimum  The minimum value, inclusive.
     * @param  maximum  The maximum value, <strong>inclusive</strong>.
     */
    public NumberRange(final Class<T> type, final T minimum, final T maximum) {
        super(type, minimum, maximum);
    }

    /**
     * Constructs a range of {@link Number} objects.
     *
     * @param  type           The element class, usually one of {@link Byte}, {@link Short},
     *                        {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @param  minimum        The minimum value.
     * @param  isMinIncluded  Defines whether the minimum value is included in the range.
     * @param  maximum        The maximum value.
     * @param  isMaxIncluded  Defines whether the maximum value is included in the range.
     */
    public NumberRange(final Class<T> type,
                       final T minimum, final boolean isMinIncluded,
                       final T maximum, final boolean isMaxIncluded)
    {
        super(type, minimum, isMinIncluded, maximum, isMaxIncluded);
    }

    /**
     * Constructs a range with the same values than the specified range,
     * casted to the specified type.
     *
     * @param  type  The element class, usually one of {@link Byte}, {@link Short},
     *               {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @param  range The range to copy. The elements must be {@link Number} instances.
     * @throws IllegalArgumentException if the values are not convertible to the specified class.
     */
    NumberRange(final Class<T> type, final Range<? extends Number> range)
            throws IllegalArgumentException
    {
        // TODO: remove the (Number) casts when we will be allowed to compile for Java 6.
        this(type, ClassChanger.cast((Number) range.getMinValue(), type), range.isMinIncluded(),
                   ClassChanger.cast((Number) range.getMaxValue(), type), range.isMaxIncluded());
    }

    /**
     * Constructs a range with the same type and the same values than the
     * specified range. This is a copy constructor.
     *
     * @param range The range to copy. The elements must be {@link Number} instances.
     *
     * @since 2.4
     */
    public NumberRange(final Range<T> range) {
        super(range.getElementClass(),
              range.getMinValue(), range.isMinIncluded(),
              range.getMaxValue(), range.isMaxIncluded());
    }

    /**
     * Creates a new range using the same element class than this range. This method will
     * be overriden by subclasses in order to create a range of a more specific type.
     */
    @Override
    NumberRange<T> create(final T minValue, final boolean isMinIncluded,
                          final T maxValue, final boolean isMaxIncluded)
    {
        return new NumberRange<T>(elementClass, minValue, isMinIncluded, maxValue, isMaxIncluded);
    }

    /**
     * Ensures that {@link #elementClass} is compatible with the type expected by this range class.
     * Invoked for argument checking by the super-class constructor.
     */
    @Override
    void checkElementClass() throws IllegalArgumentException {
        ensureNumberClass(elementClass);
    }

    /**
     * Returns the type of minimum and maximum values.
     */
    private static Class<? extends Number> getElementClass(final Range<?> range)
            throws IllegalArgumentException
    {
        ensureNonNull("range", range);
        final Class<?> type = range.elementClass;
        ensureNumberClass(type);
        /*
         * Safe because we checked in the above line. We could have used Class.asSubclass(Class)
         * instead but we want an IllegalArgumentException in case of failure rather than a
         * ClassCastException.
         */
        @SuppressWarnings("unchecked")
        final Class<? extends Number> result = (Class<? extends Number>) type;
        return result;
    }

    /**
     * Ensures that the given class is {@link Number} or a subclass.
     */
    private static void ensureNumberClass(final Class<?> type) throws IllegalArgumentException {
        if (!Number.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.ILLEGAL_CLASS_$2, type, Number.class));
        }
    }

    /**
     * Wraps the specified {@link Range} in a {@code NumberRange} object. If the specified
     * range is already an instance of {@code NumberRange}, then it is returned unchanged.
     *
     * @param <N> The type of elements in the given range.
     * @param range The range to wrap.
     * @return The same range than {@code range} as a {@code NumberRange} object.
     */
    public static <N extends Number & Comparable<? super N>>
            NumberRange<N> wrap(final Range<N> range)
    {
        if (range instanceof NumberRange) {
            @SuppressWarnings("unchecked") // Method signature should have enforced the right type.
            final NumberRange<N> cast = (NumberRange<N>) range;
            return cast;
        }
        // The constructor will ensure that the range element class is a subclass of Number.
        return new NumberRange<N>(range);
    }

    /**
     * Casts the specified range to the specified type. If this class is associated to a unit of
     * measurement, then this method convert the {@code range} units to the same units than this
     * instance. This method is overriden by {@link MeasurementRange} only in the way described
     * above.
     *
     * @param  type The class to cast to. Must be one of {@link Byte}, {@link Short},
     *              {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @return The casted range, or {@code range} if no cast is needed.
     * @throws IllegalArgumentException if the values are not convertible to the specified class.
     */
    <N extends Number & Comparable<? super N>>
    NumberRange<N> convertAndCast(final Range<? extends Number> range, final Class<N> type)
            throws IllegalArgumentException
    {
        if (type.equals(range.getElementClass())) {
            @SuppressWarnings("unchecked") // Safe because we checked in the line just above.
            final NumberRange<N> cast = (NumberRange<N>) wrap((Range) range);
            return cast;
        }
        // TODO: Remove the (Range) cast when we will be allowed to compile for Java 6.
        return new NumberRange<N>(type, (Range) range);
    }

    /**
     * Work around a Java 5 compiler bug which is fixed in Java 6.
     *
     * @todo Delete when we will be allowed to target Java 6.
     */
    @Deprecated
    final NumberRange damnJava5(final Range range, final Class type) {
        return convertAndCast(range, type);
    }

    /**
     * Casts this range to the specified type.
     *
     * @param <N> The class to cast to.
     * @param type  The class to cast to. Must be one of {@link Byte}, {@link Short},
     *              {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @return The casted range, or {@code this} if this range already uses the specified type.
     * @throws IllegalArgumentException if the values are not convertible to the specified class.
     */
    public <N extends Number & Comparable<? super N>> NumberRange<N> castTo(final Class<N> type)
            throws IllegalArgumentException
    {
        return convertAndCast(this, type);
    }

    /**
     * Returns an initially empty array of the given length.
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array creation.
    NumberRange<T>[] newArray(final int length) {
        return new NumberRange[length];
    }

    /**
     * Returns {@code true} if the specified value is within this range.
     *
     * @param value The value to check for inclusion.
     * @return {@code true} if the given value is withing this range.
     * @throws IllegalArgumentException if the given value is not comparable.
     */
    public boolean contains(final Number value) throws IllegalArgumentException {
        if (value != null && !(value instanceof Comparable)) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.NOT_COMPARABLE_CLASS_$1, value.getClass()));
        }
        return contains((Comparable<?>) value);
    }

    /**
     * Returns {@code true} if the specified value is within this range.
     * The given value must be a subclass of {@link Number}.
     *
     * @throws IllegalArgumentException if the given value is not a subclass of {@link Number}.
     */
    @Override
    public boolean contains(Comparable<?> value) throws IllegalArgumentException {
        if (value == null) {
            return false;
        }
        ensureNumberClass(value.getClass());
        /*
         * Suppress warning because we checked the class in the line just above, so we are safe.
         * We could have used Class.cast(Object) but we want an IllegalArgumentException with a
         * localized message.
         */
        @SuppressWarnings("unchecked")
        Number number = (Number) value;
        final Class<? extends Number> type = getWidestClass(elementClass, number.getClass());
        number = ClassChanger.cast(number, type);
        /*
         * The 'type' bounds should actually be <? extends Number & Comparable> since the method
         * signature expect a Comparable and we have additionnaly casted to a Number.  However I
         * have not found a way to express that safely in a local variable with Java 6.
         */
        @SuppressWarnings("unchecked")
        final boolean contains = castTo((Class) type).containsNC((Comparable) number);
        return contains;
    }

    /**
     * Returns {@code true} if the supplied range is fully contained within this range.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Range<?> range) {
        final Class<? extends Number> type = getWidestClass(elementClass, getElementClass(range));
        /*
         * The type bounds is actually <? extends Number & Comparable> but I'm unable to express
         * it it local variable as of Java 6. So we have to bypass the compiler check, but those
         * casts are actually safes, including Range because getElementClass(range) would have
         * throw an exception otherwise.
         */
        range = convertAndCast((Range) range, (Class) type);
        return castTo((Class) type).containsNC(range);
    }

    /**
     * Returns {@code true} if this range intersects the given range.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean intersects(Range<?> range) {
        final Class<? extends Number> type = getWidestClass(elementClass, getElementClass(range));
        /*
         * The type bounds is actually <? extends Number & Comparable> but I'm unable to express
         * it it local variable as of Java 6. So we have to bypass the compiler check, but those
         * casts are actually safes, including Range because getElementClass(range) would have
         * throw an exception otherwise.
         */
        range = convertAndCast((Range) range, (Class) type);
        return castTo((Class) type).intersectsNC(range);
    }

    /**
     * Returns the union of this range with the given range.
     * Widening conversions will be applied as needed.
     */
    @Override
    @SuppressWarnings("unchecked")
    public NumberRange<?> union(Range<?> range) {
        final Class<? extends Number> type = getWidestClass(elementClass, getElementClass(range));
        range = convertAndCast((Range) range, (Class) type);
        return (NumberRange) castTo((Class) type).unionNC(range);
    }

    /**
     * Returns the intersection of this range with the given range. Widening
     * conversions will be applied as needed.
     */
    @Override
    @SuppressWarnings("unchecked")
    public NumberRange<?> intersect(Range<?> range) {
        final Class<? extends Number> rangeType = getElementClass(range);
        Class<? extends Number> type = getWidestClass(elementClass, rangeType);
        range = castTo((Class) type).intersectNC(convertAndCast((Range) range, (Class) type));
        /*
         * Use a finer type capable to holds the result (since the intersection
         * may have reduced the range), but not finer than the finest type of
         * the ranges used in the intersection calculation.
         */
        type = getFinestClass(elementClass, rangeType);
        if (range.minValue != null) {
            type = getWidestClass(type, getFinestClass(((Number) range.minValue).doubleValue()));
        }
        if (range.maxValue != null) {
            type = getWidestClass(type, getFinestClass(((Number) range.maxValue).doubleValue()));
        }
        return convertAndCast((Range) range, (Class) type);
    }

    /**
     * Returns the range of values that are in this range but not in the given range.
     */
    @Override
    @SuppressWarnings("unchecked")
    public NumberRange<?>[] subtract(Range<?> range) {
        Class<? extends Number> type = getWidestClass(elementClass, getElementClass(range));
        return (NumberRange[]) castTo((Class) type)
                .subtractNC(convertAndCast((Range) range, (Class) type));
    }

    /**
     * Returns the {@linkplain #getMinValue minimum value} as a {@code double}.
     * If this range is unbounded, then {@link Double#NEGATIVE_INFINITY} is returned.
     *
     * @return The minimum value.
     */
    @SuppressWarnings("unchecked")
    public double getMinimum() {
        final Number value = (Number) getMinValue();
        return (value != null) ? value.doubleValue() : Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the {@linkplain #getMinimum() minimum value} with the specified inclusive or
     * exclusive state. If this range is unbounded, then {@link Double#NEGATIVE_INFINITY} is
     * returned.
     *
     * @param inclusive
     *            {@code true} for the minimum value inclusive, or {@code false}
     *            for the minimum value exclusive.
     * @return The minimum value, inclusive or exclusive as requested.
     */
    public double getMinimum(final boolean inclusive) {
        double value = getMinimum();
        if (inclusive != isMinIncluded()) {
            value = XMath.rool(getElementClass(), value, inclusive ? +1 : -1);
        }
        return value;
    }

    /**
     * Returns the {@linkplain #getMaxValue maximum value} as a {@code double}.
     * If this range is unbounded, then {@link Double#POSITIVE_INFINITY} is returned.
     *
     * @return The maximum value.
     */
    @SuppressWarnings("unchecked")
    public double getMaximum() {
        final Number value = (Number) getMaxValue();
        return (value != null) ? value.doubleValue() : Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the {@linkplain #getMaximum() maximum value} with the specified inclusive or
     * exclusive state. If this range is unbounded, then {@link Double#POSITIVE_INFINITY} is
     * returned.
     *
     * @param inclusive
     *            {@code true} for the maximum value inclusive, or {@code false}
     *            for the maximum value exclusive.
     * @return The maximum value, inclusive or exclusive as requested.
     */
    public double getMaximum(final boolean inclusive) {
        double value = getMaximum();
        if (inclusive != isMaxIncluded()) {
            value = XMath.rool(getElementClass(), value, inclusive ? -1 : +1);
        }
        return value;
    }
}
