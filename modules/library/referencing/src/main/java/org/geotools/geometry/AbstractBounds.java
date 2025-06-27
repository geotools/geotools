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
 */
package org.geotools.geometry;

import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedReferenceSystemException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.util.Classes;
import org.geotools.util.Utilities;

/**
 * Base class for {@linkplain Bounds envelope} implementations. This base class provides default implementations for
 * {@link #toString}, {@link #equals} and {@link #hashCode} methods.
 *
 * <p>This class do not holds any state. The decision to implement {@link java.io.Serializable} or
 * {@link org.geotools.util.Cloneable} interfaces is left to implementors.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractBounds implements Bounds {
    /** Constructs an envelope. */
    protected AbstractBounds() {}

    /**
     * Returns the common CRS of specified points.
     *
     * @param minDP The first position.
     * @param maxDP The second position.
     * @return Their common CRS, or {@code null} if none.
     * @throws MismatchedReferenceSystemException if the two positions don't use the same CRS.
     */
    static CoordinateReferenceSystem getCoordinateReferenceSystem(final Position minDP, final Position maxDP)
            throws MismatchedReferenceSystemException {
        final CoordinateReferenceSystem crs1 = minDP.getCoordinateReferenceSystem();
        final CoordinateReferenceSystem crs2 = maxDP.getCoordinateReferenceSystem();
        if (crs1 == null) {
            return crs2;
        } else {
            if (crs2 != null && !crs1.equals(crs2)) {
                throw new MismatchedReferenceSystemException(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM);
            }
            return crs1;
        }
    }

    /**
     * A coordinate position consisting of all the {@linkplain #getMinimum minimal ordinates}. The default
     * implementation returns a direct position backed by this envelope, so changes in this envelope will be immediately
     * reflected in the direct position.
     *
     * @return The lower corner.
     */
    @Override
    public Position getLowerCorner() {
        return new LowerCorner();
    }

    /**
     * A coordinate position consisting of all the {@linkplain #getMaximum maximal ordinates}. The default
     * implementation returns a direct position backed by this envelope, so changes in this envelope will be immediately
     * reflected in the direct position.
     *
     * @return The upper corner.
     */
    @Override
    public Position getUpperCorner() {
        return new UpperCorner();
    }

    /**
     * Returns a string representation of this envelope. The default implementation returns a string containing
     * {@linkplain #getLowerCorner lower corner} coordinates first, followed by {@linkplain #getUpperCorner upper
     * corner} coordinates. Other informations like the CRS or class name may or may not be presents at implementor
     * choice.
     *
     * <p>This string is okay for occasional formatting (for example for debugging purpose). But if there is a lot of
     * envelopes to format, users will get more control by using their own instance of
     * {@link org.geotools.referencing.CoordinateFormat}.
     */
    @Override
    public String toString() {
        return toString(this);
    }

    /**
     * Formats the specified envelope. The returned string will contain the {@linkplain #getLowerCorner lower corner}
     * coordinates first, followed by {@linkplain #getUpperCorner upper corner} coordinates.
     */
    static String toString(final Bounds envelope) {
        final StringBuilder buffer = new StringBuilder(Classes.getShortClassName(envelope));
        final int dimension = envelope.getDimension();
        if (dimension != 0) {
            String separator = "[(";
            for (int i = 0; i < dimension; i++) {
                buffer.append(separator).append(envelope.getMinimum(i));
                separator = ", ";
            }
            separator = "), (";
            for (int i = 0; i < dimension; i++) {
                buffer.append(separator).append(envelope.getMaximum(i));
                separator = ", ";
            }
            buffer.append(")]");
        }
        return buffer.toString();
    }

    /** Returns a hash value for this envelope. */
    @Override
    public int hashCode() {
        final int dimension = getDimension();
        int code = 1;
        boolean p = true;
        do {
            for (int i = 0; i < dimension; i++) {
                final long bits = Double.doubleToLongBits(p ? getMinimum(i) : getMaximum(i));
                code = 31 * code + ((int) bits ^ (int) (bits >>> 32));
            }
        } while ((p = !p) == false);
        final CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
        if (crs != null) {
            code += crs.hashCode();
        }
        return code;
    }

    /**
     * Returns {@code true} if the specified object is also an {@linkplain Bounds envelope} with equals coordinates and
     * {@linkplain #getCoordinateReferenceSystem CRS}.
     *
     * @param object The object to compare with this envelope.
     * @return {@code true} if the given object is equals to this envelope.
     * @todo Current implementation requires that {@code object} is of the same class. We can not relax this rule before
     *     we ensure that every implementations in the Geotools code base follow the same contract.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final Bounds that = (Bounds) object;
            final int dimension = getDimension();
            if (dimension == that.getDimension()) {
                for (int i = 0; i < dimension; i++) {
                    if (!Utilities.equals(this.getMinimum(i), that.getMinimum(i))
                            || !Utilities.equals(this.getMaximum(i), that.getMaximum(i))) {
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

    /** Base class for direct position from an envelope. This class delegates its work to the enclosing envelope. */
    private abstract class Corner extends AbstractPosition {
        /** The coordinate reference system in which the coordinate is given. */
        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return AbstractBounds.this.getCoordinateReferenceSystem();
        }

        /** The length of coordinate sequence (the number of entries). */
        @Override
        public int getDimension() {
            return AbstractBounds.this.getDimension();
        }

        /** Sets the ordinate value along the specified dimension. */
        @Override
        public void setOrdinate(int dimension, double value) {
            throw new UnsupportedOperationException();
        }
    }

    /** The corner returned by {@link AbstractBounds#getLowerCorner}. */
    private final class LowerCorner extends Corner {
        @Override
        public double getOrdinate(final int dimension) throws IndexOutOfBoundsException {
            return getMinimum(dimension);
        }
    }

    /** The corner returned by {@link AbstractBounds#getUpperCorner}. */
    private final class UpperCorner extends Corner {
        @Override
        public double getOrdinate(final int dimension) throws IndexOutOfBoundsException {
            return getMaximum(dimension);
        }
    }
}
