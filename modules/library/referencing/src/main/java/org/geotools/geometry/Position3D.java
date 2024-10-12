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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Utilities;

/**
 * Holds the coordinates for a three-dimensional position within some coordinate reference system.
 *
 * @version $Id$
 * @author Niels Charlier
 */
public class Position3D implements Position, Serializable, Cloneable {

    public double x, y, z;

    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 835130287438466996L;

    /** The coordinate reference system for this position; */
    private CoordinateReferenceSystem crs;

    /**
     * Constructs a position initialized to (0,0,0) with a {@code null} coordinate reference system.
     */
    public Position3D() {}

    /**
     * Constructs a position with the specified coordinate reference system.
     *
     * @param crs The coordinate reference system, or {@code null}.
     */
    public Position3D(final CoordinateReferenceSystem crs) {
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Constructs a 3D position from the specified ordinates.
     *
     * @param x The <var>x</var> value.
     * @param y The <var>y</var> value.
     * @param z The <var>z</var> value.
     */
    public Position3D(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a 2D position from the specified ordinates in the specified CRS.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @param x The <var>x</var> value.
     * @param y The <var>y</var> value.
     * @param z The <var>z</var> value.
     */
    public Position3D(final CoordinateReferenceSystem crs, final double x, final double y, final double z) {
        this(x, y, z);
        setCoordinateReferenceSystem(crs);
    }
    /**
     * Constructs a position initialized to the same values than the specified point.
     *
     * @param point The point to copy.
     */
    public Position3D(final Position point) {
        setLocation(point);
    }

    /** Returns always {@code this}, the direct position for this {@linkplain Position position}. */
    @Override
    public Position getDirectPosition() {
        return this;
    }

    /**
     * Returns the coordinate reference system in which the coordinate is given. May be {@code null}
     * if this particular {@code DirectPosition} is included in a larger object with such a
     * reference to a {@linkplain CoordinateReferenceSystem coordinate reference system}.
     *
     * @return The coordinate reference system, or {@code null}.
     */
    @Override
    public final CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Set the coordinate reference system in which the coordinate is given.
     *
     * @param crs The new coordinate reference system, or {@code null}.
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs) {
        AbstractPosition.checkCoordinateReferenceSystemDimension(crs, 3);
        this.crs = crs;
    }

    /**
     * The length of coordinate sequence (the number of entries). This is always 3 for <code>
     * DirectPosition3D</code> objects.
     *
     * @return The dimensionality of this position.
     */
    @Override
    public final int getDimension() {
        return 3;
    }

    /**
     * Returns a sequence of numbers that hold the coordinate of this position in its reference
     * system.
     *
     * @return The coordinates
     */
    @Override
    public double[] getCoordinate() {
        return new double[] {x, y, z};
    }

    /**
     * Returns the ordinate at the specified dimension.
     *
     * @param dimension The dimension in the range 0 to 2 inclusive.
     * @return The coordinate at the specified dimension.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     */
    @Override
    public final double getOrdinate(final int dimension) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /**
     * Sets the ordinate value along the specified dimension.
     *
     * @param dimension the dimension for the ordinate of interest.
     * @param value the ordinate value of interest.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     * @todo Provides a more detailed error message.
     */
    @Override
    public final void setOrdinate(int dimension, double value) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
            case 2:
                z = value;
                break;
            default:
                throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /**
     * Set this coordinate to the specified direct position. If the specified position contains a
     * {@linkplain CoordinateReferenceSystem coordinate reference system}, then the CRS for this
     * position will be set to the CRS of the specified position.
     *
     * @param position The new position for this point.
     * @throws MismatchedDimensionException if this point doesn't have the expected dimension.
     */
    public void setLocation(final Position position) throws MismatchedDimensionException {
        AbstractPosition.ensureDimensionMatch("position", position.getDimension(), 3);
        setCoordinateReferenceSystem(position.getCoordinateReferenceSystem());
        x = position.getOrdinate(0);
        y = position.getOrdinate(1);
        z = position.getOrdinate(2);
    }

    /** Returns a string representation of this coordinate. */
    @Override
    public String toString() {
        return AbstractPosition.toString(this);
    }

    /**
     * Returns a hash value for this coordinate. This method implements the {@link
     * Position#hashCode} contract.
     *
     * @return A hash code value for this position.
     */
    @Override
    public int hashCode() {
        return AbstractPosition.hashCode(this);
    }

    /**
     * Compares this point with the specified object for equality. If the given object implements
     * the {@link Position} interface, then the comparison is performed as specified in its {@link
     * Position#equals} contract.
     *
     * @param object The object to compare with this position.
     * @return {@code true} if the given object is equals to this position.
     */
    @Override
    public boolean equals(final Object object) {
        /*
         * If the other object implements the DirectPosition interface, performs
         * the comparaison as specified in DirectPosition.equals(Object) contract.
         */
        if (object instanceof Position) {
            final Position other = (Position) object;
            if (other.getDimension() == 3
                    && Utilities.equals(other.getOrdinate(0), x)
                    && Utilities.equals(other.getOrdinate(1), y)
                    && Utilities.equals(other.getOrdinate(2), z)
                    && Utilities.equals(other.getCoordinateReferenceSystem(), crs)) {
                assert hashCode() == other.hashCode() : this;
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Returns a clone of this point.
     *
     * @return A clone of this position.
     */
    @Override
    public Position3D clone() {
        return new Position3D(this);
    }

    /**
     * Write this object to the specified stream. This method is necessary because the super-class
     * is not serializable.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    /**
     * Read this object from the specified stream. This method is necessary because the super-class
     * is not serializable.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
