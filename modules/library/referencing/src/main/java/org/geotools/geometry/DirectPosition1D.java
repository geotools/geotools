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

import java.io.Serializable;
import org.opengis.util.Cloneable;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;


/**
 * Holds the coordinates for a one-dimensional position within some coordinate reference system.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DirectPosition2D
 * @see GeneralPosition
 */
public class DirectPosition1D extends AbstractDirectPosition implements Serializable, Cloneable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3235094562875693710L;

    /**
     * The coordinate reference system for this position;
     */
    private CoordinateReferenceSystem crs;

    /**
     * The ordinate value.
     */
    public double ordinate;

    /**
     * Constructs a position initialized to (0) with a {@code null}
     * coordinate reference system.
     */
    public DirectPosition1D() {
    }

    /**
     * Constructs a position with the specified coordinate reference system.
     *
     * @param crs The coordinate reference system.
     */
    public DirectPosition1D(final CoordinateReferenceSystem crs) {
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Constructs a 1D position from the specified ordinate.
     *
     * @param ordinate The ordinate value.
     */
    public DirectPosition1D(final double ordinate) {
        this.ordinate = ordinate;
    }

    /**
     * Constructs a position initialized to the same values than the specified point.
     *
     * @param point The position to copy.
     */
    public DirectPosition1D(final DirectPosition point) {
        setLocation(point);
    }

    /**
     * Returns the coordinate reference system in which the coordinate is given.
     * May be {@code null} if this particular {@code DirectPosition} is included
     * in a larger object with such a reference to a {@linkplain CoordinateReferenceSystem
     * coordinate reference system}.
     *
     * @return The coordinate reference system, or {@code null}.
     */
    public final CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Set the coordinate reference system in which the coordinate is given.
     *
     * @param crs The new coordinate reference system, or {@code null}.
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs) {
        checkCoordinateReferenceSystemDimension(crs, 1);
        this.crs = crs;
    }

    /**
     * The length of coordinate sequence (the number of entries).
     * This is always 1 for <code>DirectPosition1D</code> objects.
     *
     * @return The dimensionality of this position.
     */
    public final int getDimension() {
        return 1;
    }

    /**
     * Returns a sequence of numbers that hold the coordinate of this position in its
     * reference system.
     *
     * @return The coordinates.
     */
    @Override
    public double[] getCoordinate() {
        return new double[] {ordinate};
    }

    /**
     * Returns the ordinate at the specified dimension.
     *
     * @param  dimension The dimension, which must be 0.
     * @return The {@linkplain #ordinate}.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     *
     * @todo Provides a more detailled error message.
     */
    public final double getOrdinate(final int dimension) throws IndexOutOfBoundsException {
        if (dimension == 0) {
            return ordinate;
        } else {
            throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /**
     * Sets the ordinate value along the specified dimension.
     *
     * @param  dimension The dimension, which must be 0.
     * @param  value the ordinate value.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     *
     * @todo Provides a more detailled error message.
     */
    public final void setOrdinate(int dimension, double value) throws IndexOutOfBoundsException {
        if (dimension == 0) {
            ordinate = value;
        } else {
            throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /**
     * Set this coordinate to the specified direct position. If the specified position
     * contains a {@linkplain CoordinateReferenceSystem coordinate reference system},
     * then the CRS for this position will be set to the CRS of the specified position.
     *
     * @param  position The new position for this point.
     * @throws MismatchedDimensionException if this point doesn't have the expected dimension.
     */
    public void setLocation(final DirectPosition position) throws MismatchedDimensionException {
        AbstractDirectPosition.ensureDimensionMatch("position", position.getDimension(), 1);
        setCoordinateReferenceSystem(position.getCoordinateReferenceSystem());
        ordinate = position.getOrdinate(0);
    }

    /**
     * Returns a hash value for this coordinate.
     */
    @Override
    public int hashCode() {
        final long value = Double.doubleToLongBits(ordinate);
        int code = 31 + ((int)value ^ (int)(value >>> 32));
        if (crs != null) {
            code += crs.hashCode();
        }
        assert code == super.hashCode();
        return code;
    }

    /**
     * Returns a copy of this position.
     */
    @Override
    public DirectPosition1D clone() {
        try {
            return (DirectPosition1D) super.clone();
        } catch (CloneNotSupportedException exception) {
            // Should not happen, since we are cloneable.
            throw new AssertionError(exception);
        }
    }
}
