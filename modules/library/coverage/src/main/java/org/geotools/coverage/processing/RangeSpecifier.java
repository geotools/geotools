/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import javax.measure.Unit;
import org.geotools.api.referencing.operation.MathTransform1D;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Classes;
import org.geotools.util.NumberRange;
import org.geotools.util.Utilities;

/**
 * Argument type for {@link DefaultProcessor} operations for specifying the range, colors and units of a computation
 * result. {@code RangeSpecifier} are used for tuning the {@link org.geotools.coverage.Category} object to be
 * constructed. By default, most {@linkplain OperationJAI operations} try to guess a raisonable range for output values.
 * This default behavior can be overridden with an explicit {@code RangeSpecifier} argument.
 *
 * <p>All {@code RangeSpecifier}'s properties are optional; it is up to processor's {@linkplain OperationJAI operation}
 * to replace {@code null} values by a default one.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @todo Move into the {@code org.geotools.coverage.grid} package as a {@code ImageLayout} subclass. Rename as
 *     {@code GridCoverageLayout}.
 */
public class RangeSpecifier implements Serializable, Cloneable {
    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = 8436500582161136302L;

    /** The target range, or {@code null} if none. */
    private NumberRange<?> range;

    /** The target "sample to geophysics" transform, or {@code null} if none. */
    private MathTransform1D transform;

    /** The target range units, or {@code null} if none. */
    private Unit<?> unit;

    /** The target colors, or {@code null} if none. */
    private Color[] colors;

    /** Constructs a default {@code RangeSpecifier} with no value set. */
    public RangeSpecifier() {}

    /**
     * Constructs a {@code RangeSpecifier} initialised to the spécified range.
     *
     * @param range The range
     */
    public RangeSpecifier(final NumberRange<?> range) {
        this.range = range;
    }

    /**
     * Constructs a {@code RangeSpecifier} initialised to the specified "sample to geophysics" transform.
     *
     * @param transform The sample to geophysics transform.
     */
    public RangeSpecifier(final MathTransform1D transform) {
        this.transform = transform;
    }

    /**
     * Returns the target range, or {@code null} if none.
     *
     * @return The range
     */
    public NumberRange<?> getRange() {
        return range;
    }

    /**
     * Set the target range to the specified values. Setting this property will clear the
     * {@linkplain #getSampleToGeophysics sample to geophysics transform}, since those properties are mutually
     * exclusive.
     *
     * @param range The target range.
     */
    public void setRange(final NumberRange<?> range) {
        this.range = range;
        transform = null;
    }

    /**
     * Returns the target "sample to geophysics" transform, or {@code null} if none.
     *
     * @return The current sample to geophysics transform.
     */
    public MathTransform1D getSampleToGeophysics() {
        return transform;
    }

    /**
     * Set the target "sample to geophysics" transform to the specified value. Setting this property will clear the
     * {@linkplain #getRange range} property, since those properties are mutually exclusive.
     *
     * @param transform The new sample to geophysics transform.
     */
    public void setSampleToGeophysics(final MathTransform1D transform) {
        this.transform = transform;
        range = null;
    }

    /**
     * Returns the target range units, or {@code null} if none.
     *
     * @return The current units.
     */
    public Unit<?> getUnit() {
        return unit;
    }

    /**
     * Set the target range units to the specified value.
     *
     * @param unit The new units.
     */
    public void setUnit(final Unit<?> unit) {
        this.unit = unit;
    }

    /**
     * Returns the target colors, or {@code null} if none.
     *
     * @return The current colors.
     */
    public Color[] getColors() {
        return colors != null ? colors.clone() : null;
    }

    /**
     * Set the target colors to the specified value.
     *
     * @param colors The new colors.
     */
    public void setColors(final Color[] colors) {
        this.colors = colors != null ? colors.clone() : null;
    }

    /**
     * Returns a clone of this object.
     *
     * @return A clone of this object.
     */
    @Override
    public RangeSpecifier clone() {
        try {
            return (RangeSpecifier) super.clone();
        } catch (CloneNotSupportedException exception) {
            // Should not happen, since we are cloneable.
            throw new AssertionError(exception);
        }
    }

    /** Returns a hash code value for this range specifier. */
    @Override
    public int hashCode() {
        int code = (int) serialVersionUID;
        if (range != null) {
            code += range.hashCode();
        }
        if (transform != null) {
            code += transform.hashCode();
        }
        return code;
    }

    /**
     * Compares this range specifier with the specified object for equality.
     *
     * @param object The object to compare with.
     * @return {@code true} if the given object is equals to this range specifier.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final RangeSpecifier that = (RangeSpecifier) object;
            return Utilities.equals(this.range, that.range)
                    && Utilities.equals(this.transform, that.transform)
                    && Utilities.equals(this.unit, that.unit)
                    && Arrays.equals(this.colors, that.colors);
        }
        return false;
    }

    /** Returns a string representation of this range specifier. */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(this));
        buffer.append('[');
        if (range != null) {
            buffer.append(range);
        } else if (transform != null) {
            buffer.append(transform);
        }
        return buffer.append(']').toString();
    }
}
