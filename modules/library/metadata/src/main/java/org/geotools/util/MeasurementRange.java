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
 */
package org.geotools.util;

import javax.measure.unit.Unit;
import javax.measure.converter.UnitConverter;
import javax.measure.converter.ConversionException;

import org.geotools.resources.ClassChanger;


/**
 * A range of numbers associated with a unit of measurement. Unit conversions are applied as
 * needed by {@linkplain #union union} and {@linkplain #intersect intersection} operations.
 *
 * @param <T> The type of range elements as a subclass of {@link Number}.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class MeasurementRange<T extends Number & Comparable<? super T>> extends NumberRange<T> {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3980319420337513745L;

    /**
     * The units of measurement, or {@code null} if unknown.
     */
    private final Unit<?> units;

    /**
     * Constructs an inclusive range of {@code float} values.
     *
     * @param minimum The minimum value, inclusive.
     * @param maximum The maximum value, <strong>inclusive</strong>.
     * @param units   The units of measurement, or {@code null} if unknown.
     * @return The measurement range.
     *
     * @since 2.5
     */
    public static MeasurementRange<Float> create(float minimum, float maximum, Unit<?> units) {
        return create(minimum, true, maximum, true, units);
    }

    /** @deprecated Use {@code create} methods instead. */
    public MeasurementRange(final float minimum, final float maximum, final Unit<?> units) {
        super(minimum, maximum);
        this.units = units;
    }

    /**
     * Constructs a range of {@code float} values.
     *
     * @param minimum The minimum value.
     * @param isMinIncluded Defines whether the minimum value is included in the Range.
     * @param maximum The maximum value.
     * @param isMaxIncluded Defines whether the maximum value is included in the Range.
     * @param units   The units of measurement, or {@code null} if unknown.
     * @return The measurement range.
     *
     * @since 2.5
     */
    public static MeasurementRange<Float> create(float minimum, boolean isMinIncluded,
                                                 float maximum, boolean isMaxIncluded, Unit<?> units)
    {
        return new MeasurementRange<Float>(Float.class, Float.valueOf(minimum), isMinIncluded,
                Float.valueOf(maximum), isMaxIncluded, units);
    }

    /** @deprecated Use {@code create} methods instead. */
    public MeasurementRange(final float minimum, final boolean isMinIncluded,
                            final float maximum, final boolean isMaxIncluded, final Unit<?> units)
    {
        super(minimum, isMinIncluded, maximum, isMaxIncluded);
        this.units = units;
    }

    /**
     * Constructs an inclusive range of {@code double} values.
     *
     * @param minimum The minimum value, inclusive.
     * @param maximum The maximum value, <strong>inclusive</strong>.
     * @param units   The units of measurement, or {@code null} if unknown.
     * @return The measurement range.
     */
    public static MeasurementRange<Double> create(double minimum, double maximum, Unit<?> units) {
        return create(minimum, true, maximum, true, units);
    }

    /** @deprecated Use {@code create} methods instead. */
    public MeasurementRange(final double minimum, final double maximum, final Unit<?> units) {
        super(minimum, maximum);
        this.units = units;
    }

    /**
     * Constructs a range of {@code double} values.
     *
     * @param minimum The minimum value.
     * @param isMinIncluded Defines whether the minimum value is included in the Range.
     * @param maximum The maximum value.
     * @param isMaxIncluded Defines whether the maximum value is included in the Range.
     * @param units   The units of measurement, or {@code null} if unknown.
     * @return The measurement range.
     */
    public static MeasurementRange<Double> create(double minimum, boolean isMinIncluded,
                                                  double maximum, boolean isMaxIncluded, Unit<?> units)
    {
        return new MeasurementRange<Double>(Double.class, Double.valueOf(minimum), isMinIncluded,
                Double.valueOf(maximum), isMaxIncluded, units);
    }

    /** @deprecated Use {@code create} methods instead. */
    public MeasurementRange(final double minimum, final boolean isMinIncluded,
                            final double maximum, final boolean isMaxIncluded, final Unit<?> units)
    {
        super(minimum, isMinIncluded, maximum, isMaxIncluded);
        this.units = units;
    }

    /**
     * Constructs a range of {@link Number} objects.
     *
     * @param type          The element class, usually one of {@link Byte}, {@link Short},
     *                      {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @param minimum       The minimum value.
     * @param isMinIncluded Defines whether the minimum value is included in the Range.
     * @param maximum       The maximum value.
     * @param isMaxIncluded Defines whether the maximum value is included in the Range.
     * @param units         The units of measurement, or {@code null} if unknown.
     */
    public MeasurementRange(final Class<T> type,
                            final T minimum, final boolean isMinIncluded,
                            final T maximum, final boolean isMaxIncluded,
                            final Unit<?> units)
    {
        super(type, minimum, isMinIncluded, maximum, isMaxIncluded);
        this.units = units;
    }

    /**
     * Constructs a range with the same values than the specified range.
     *
     * @param range The range to copy. The elements must be {@link Number} instances.
     * @param units The units of measurement, or {@code null} if unknown.
     */
    public MeasurementRange(final Range<T> range, final Unit<?> units) {
        super(range);
        this.units = units;
    }

    /**
     * Constructs a range with the same values than the specified range,
     * casted to the specified type.
     *
     * @param type The element class, usually one of {@link Byte}, {@link Short},
     *             {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @param range The range to copy. The elements must be {@link Number} instances.
     * @param units   The units of measurement, or {@code null} if unknown.
     */
    private MeasurementRange(Class<T> type, Range<? extends Number> range, final Unit<?> units) {
        // TODO: remove the (Range) cast when we will be allowed to compile for Java 6.
        super(type, (Range) range);
        this.units = units;
    }

    /**
     * Creates a new range using the same element class than this range.
     */
    @Override
    MeasurementRange<T> create(final T minValue, final boolean isMinIncluded,
                               final T maxValue, final boolean isMaxIncluded)
    {
        return new MeasurementRange<T>(elementClass, minValue, isMinIncluded, maxValue, isMaxIncluded, units);
    }

    /**
     * Returns the units of measurement, or {@code null} if unknown.
     *
     * @return The units of measurement, or {@code null}.
     */
    @Override
    public Unit<?> getUnits() {
        return units;
    }

    /**
     * Converts this range to the specified units. If this measurement range has null units,
     * then the specified target units are simply assigned to the returned range with no
     * other changes.
     *
     * @param  targetUnits the target units.
     * @return The converted range, or {@code this} if no conversion is needed.
     * @throws ConversionException if the target units are not compatible with
     *         this {@linkplain #getUnits range units}.
     */
    public MeasurementRange convertTo(final Unit<?> targetUnits) throws ConversionException {
        return convertAndCast(elementClass, targetUnits);
    }

    /**
     * Casts this range to the specified type.
     *
     * @param <N>   The class to cast to.
     * @param  type The class to cast to. Must be one of {@link Byte}, {@link Short},
     *              {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @return The casted range, or {@code this} if this range already uses the specified type.
     */
    @Override
    public <N extends Number & Comparable<? super N>> MeasurementRange<N> castTo(Class<N> type) {
        return (MeasurementRange) damnJava5(this, type);
    }

    /**
     * Casts the specified range to the specified type. If this class is associated to a unit of
     * measurement, then this method convert the {@code range} units to the same units than this
     * instance.
     *
     * @param type The class to cast to. Must be one of {@link Byte}, {@link Short},
     *             {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @return The casted range, or {@code range} if no cast is needed.
     */
    @Override
    <N extends Number & Comparable<? super N>>
    MeasurementRange<N> convertAndCast(final Range<? extends Number> range, final Class<N> type)
            throws IllegalArgumentException
    {
        if (range instanceof MeasurementRange) {
            final MeasurementRange<?> casted = (MeasurementRange) range;
            return casted.convertAndCast(type, units);
        }
        // TODO: Remove the (Range) cast when we will be allowed to compile for Java 6.
        return new MeasurementRange<N>(type, (Range) range, units);
    }

    /**
     * Casts this range to the specified type and converts to the specified units.
     *
     * @param  type The class to cast to. Must be one of {@link Byte}, {@link Short},
     *             {@link Integer}, {@link Long}, {@link Float} or {@link Double}.
     * @param  targetUnit the target units.
     * @return The casted range, or {@code this}.
     * @throws ConversionException if the target units are not compatible with
     *         this {@linkplain #getUnits range units}.
     */
    private <N extends Number & Comparable<? super N>> MeasurementRange<N>
            convertAndCast(final Class<N> type, final Unit<?> targetUnits) throws ConversionException
    {
        if (targetUnits == null || targetUnits.equals(units)) {
            if (type.equals(elementClass)) {
                @SuppressWarnings("unchecked")
                final MeasurementRange<N> result = (MeasurementRange) this;
                return result;
            } else {
                return new MeasurementRange<N>(type, this, units);
            }
        }
        if (units == null) {
            return new MeasurementRange<N>(type, this, targetUnits);
        }
        final UnitConverter converter = units.getConverterTo(targetUnits);
        if (converter.equals(UnitConverter.IDENTITY)) {
            return new MeasurementRange<N>(type, this, targetUnits);
        }
        boolean isMinIncluded = isMinIncluded();
        boolean isMaxIncluded = isMaxIncluded();
        Double minimum = converter.convert(getMinimum());
        Double maximum = converter.convert(getMaximum());
        if (minimum.compareTo(maximum) > 0) {
            final Double td = minimum;
            minimum = maximum;
            maximum = td;
            final boolean tb = isMinIncluded;
            isMinIncluded = isMaxIncluded;
            isMaxIncluded = tb;
        }
        return new MeasurementRange<N>(type,
                ClassChanger.cast(minimum, type), isMinIncluded,
                ClassChanger.cast(maximum, type), isMaxIncluded, targetUnits);
    }

    /**
     * Returns an initially empty array of the given length.
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array creation.
    MeasurementRange<T>[] newArray(final int length) {
        return new MeasurementRange[length];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeasurementRange union(final Range range) {
        return (MeasurementRange) super.union(range);
        // Should never throw ClassCastException because super.union(Range) invokes create(...),
        // which is overriden in this class with MeasurementRange return type.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeasurementRange intersect(final Range range) {
        return (MeasurementRange) super.intersect(range);
        // Should never throw ClassCastException because super.intersect(Range) invokes
        // convertAndCast(...),  which is overriden in this class with MeasurementRange
        // return type.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeasurementRange[] subtract(final Range range) {
        return (MeasurementRange[]) super.subtract(range);
        // Should never throw ClassCastException because super.subtract(Range) invokes newArray(int)
        // and create(...), which are overriden in this class with MeasurementRange return type.
    }

    /**
     * Compares this range with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            if (object instanceof MeasurementRange) {
                final MeasurementRange that = (MeasurementRange) object;
                return Utilities.equals(this.units, that.units);
            }
            return true;
        }
        return false;
    }
}
