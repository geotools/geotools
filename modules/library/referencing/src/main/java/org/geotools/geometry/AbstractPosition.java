/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry;

import java.text.MessageFormat;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.util.Classes;
import org.geotools.util.Utilities;

/**
 * Base class for {@linkplain Position direct position} implementations. This base class provides default
 * implementations for {@link #toString}, {@link #equals} and {@link #hashCode} methods.
 *
 * <p>This class do not holds any state. The decision to implement {@link java.io.Serializable} or
 * {@link org.geotools.util.Cloneable} interfaces is left to implementors.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractPosition implements Position {
    /** Constructs a direct position. */
    protected AbstractPosition() {}

    /**
     * Returns always {@code this}, the direct position for this {@linkplain Position position}.
     *
     * @since 2.5
     */
    @Override
    public Position getDirectPosition() {
        return this;
    }

    /**
     * Sets this direct position to the given position. If the given position is {@code null}, then all ordinate values
     * are set to {@linkplain Double#NaN NaN}.
     *
     * @param position The new position.
     * @since 2.5
     */
    public void setPosition(final Position position) {
        final int dimension = getDimension();
        if (position != null) {
            ensureDimensionMatch("position", position.getDimension(), dimension);
            for (int i = 0; i < dimension; i++) {
                setOrdinate(i, position.getOrdinate(i));
            }
        } else {
            for (int i = 0; i < dimension; i++) {
                setOrdinate(i, Double.NaN);
            }
        }
    }

    /**
     * Returns a sequence of numbers that hold the coordinate of this position in its reference system.
     *
     * @return The coordinates.
     */
    @Override
    public double[] getCoordinate() {
        final double[] ordinates = new double[getDimension()];
        for (int i = 0; i < ordinates.length; i++) {
            ordinates[i] = getOrdinate(i);
        }
        return ordinates;
    }

    /**
     * Convenience method for checking coordinate reference system validity.
     *
     * @param crs The coordinate reference system to check.
     * @param expected the dimension expected.
     * @throws MismatchedDimensionException if the CRS dimension is not valid.
     */
    public static void checkCoordinateReferenceSystemDimension(final CoordinateReferenceSystem crs, final int expected)
            throws MismatchedDimensionException {
        if (crs != null) {
            final int dimension = crs.getCoordinateSystem().getDimension();
            if (dimension != expected) {
                final Object arg0 = crs.getName().getCode();
                throw new MismatchedDimensionException(
                        MessageFormat.format(ErrorKeys.MISMATCHED_DIMENSION_$3, arg0, dimension, expected));
            }
        }
    }

    /**
     * Convenience method for checking object dimension validity. This method is usually invoked for argument checking.
     *
     * @param name The name of the argument to check.
     * @param dimension The object dimension.
     * @param expectedDimension The Expected dimension for the object.
     * @throws MismatchedDimensionException if the object doesn't have the expected dimension.
     */
    static void ensureDimensionMatch(final String name, final int dimension, final int expectedDimension)
            throws MismatchedDimensionException {
        if (dimension != expectedDimension) {
            throw new MismatchedDimensionException(
                    MessageFormat.format(ErrorKeys.MISMATCHED_DIMENSION_$3, name, dimension, expectedDimension));
        }
    }

    /**
     * Returns a string representation of this coordinate. The default implementation is okay for occasional formatting
     * (for example for debugging purpose). But if there is a lot of positions to format, users will get more control by
     * using their own instance of {@link org.geotools.referencing.CoordinateFormat}.
     */
    @Override
    public String toString() {
        return toString(this);
    }

    /** Formats the specified position. */
    static String toString(final Position position) {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(position)).append('[');
        final int dimension = position.getDimension();
        for (int i = 0; i < dimension; i++) {
            if (i != 0) {
                buffer.append(", ");
            }
            buffer.append(position.getOrdinate(i));
        }
        return buffer.append(']').toString();
    }

    /**
     * Returns a hash value for this coordinate.
     *
     * @return A hash code value for this position.
     */
    @Override
    public int hashCode() {
        return hashCode(this);
    }

    /** Returns a hash value for the given coordinate. */
    static int hashCode(final Position position) {
        final int dimension = position.getDimension();
        int code = 1;
        for (int i = 0; i < dimension; i++) {
            final long bits = Double.doubleToLongBits(position.getOrdinate(i));
            code = 31 * code + ((int) bits ^ (int) (bits >>> 32));
        }
        final CoordinateReferenceSystem crs = position.getCoordinateReferenceSystem();
        if (crs != null) {
            code += crs.hashCode();
        }
        return code;
    }

    /**
     * Returns {@code true} if the specified object is also a {@linkplain Position direct position} with equals
     * {@linkplain #getCoordinate coordinate} and {@linkplain #getCoordinateReferenceSystem CRS}.
     *
     * @param object The object to compare with this position.
     * @return {@code true} if the given object is equals to this position.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Position) {
            final Position that = (Position) object;
            final int dimension = getDimension();
            if (dimension == that.getDimension()) {
                for (int i = 0; i < dimension; i++) {
                    if (!Utilities.equals(this.getOrdinate(i), that.getOrdinate(i))) {
                        return false;
                    }
                }
                if (Utilities.equals(this.getCoordinateReferenceSystem(), that.getCoordinateReferenceSystem())) {
                    assert hashCode() == that.hashCode() : this;
                    return true;
                }
            }
        }
        return false;
    }
}
