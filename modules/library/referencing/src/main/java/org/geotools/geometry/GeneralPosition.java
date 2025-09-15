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

import java.awt.geom.Point2D;
import java.io.Serial;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.Cloneable;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.util.SuppressFBWarnings;

/**
 * Holds the coordinates for a position within some coordinate reference system. Since {@code DirectPosition}s, as data
 * types, will often be included in larger objects (such as {@linkplain org.geotools.geometry.Geometry geometries}) that
 * have references to {@link CoordinateReferenceSystem}, the {@link #getCoordinateReferenceSystem} method may returns
 * {@code null} if this particular {@code DirectPosition} is included in a larger object with such a reference to a
 * {@linkplain CoordinateReferenceSystem coordinate reference system}. In this case, the cordinate reference system is
 * implicitly assumed to take on the value of the containing object's {@link CoordinateReferenceSystem}.
 *
 * <p>This particular implementation of {@code DirectPosition} is said "General" because it uses an
 * {@linkplain #ordinates array of ordinates} of an arbitrary length. If the direct position is know to be always
 * two-dimensional, then {@link Position2D} may provides a more efficient implementation.
 *
 * <p>Most methods in this implementation are final for performance reason.
 *
 * @since 2.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see Position1D
 * @see Position2D
 * @see java.awt.geom.Point2D
 */
public class GeneralPosition extends AbstractPosition implements Serializable, Cloneable {
    /** Serial number for interoperability with different versions. */
    @Serial
    private static final long serialVersionUID = 9071833698385715524L;

    /** The ordinates of the direct position. */
    public final double[] ordinates;

    /** The coordinate reference system for this position, or {@code null}. */
    private CoordinateReferenceSystem crs;

    /**
     * Constructs a position using the specified coordinate reference system. The number of dimensions is inferred from
     * the coordinate reference system.
     *
     * @param crs The coordinate reference system to be given to this position.
     * @since 2.2
     */
    public GeneralPosition(final CoordinateReferenceSystem crs) {
        this(crs.getCoordinateSystem().getDimension());
        this.crs = crs;
    }

    /**
     * Constructs a position with the specified number of dimensions.
     *
     * @param numDim Number of dimensions.
     * @throws NegativeArraySizeException if {@code numDim} is negative.
     */
    public GeneralPosition(final int numDim) throws NegativeArraySizeException {
        ordinates = new double[numDim];
    }

    /**
     * Constructs a position with the specified ordinates. The {@code ordinates} array will be copied.
     *
     * @param ordinates The ordinate values to copy.
     */
    public GeneralPosition(final double[] ordinates) {
        this.ordinates = ordinates.clone();
    }

    /**
     * Constructs a 2D position from the specified ordinates. Despite their name, the (<var>x</var>,<var>y</var>)
     * coordinates don't need to be oriented toward ({@linkplain org.geotools.api.referencing.cs.AxisDirection#EAST
     * East}, {@linkplain org.geotools.api.referencing.cs.AxisDirection#NORTH North}). See the {@link Position2D}
     * javadoc for details.
     *
     * @param x The first ordinate value.
     * @param y The second ordinate value.
     */
    public GeneralPosition(final double x, final double y) {
        ordinates = new double[] {x, y};
    }

    /**
     * Constructs a 3D position from the specified ordinates. Despite their name, the
     * (<var>x</var>,<var>y</var>,<var>z</var>) coordinates don't need to be oriented toward
     * ({@linkplain org.geotools.api.referencing.cs.AxisDirection#EAST East},
     * {@linkplain org.geotools.api.referencing.cs.AxisDirection#NORTH North},
     * {@linkplain org.geotools.api.referencing.cs.AxisDirection#UP Up}).
     *
     * @param x The first ordinate value.
     * @param y The second ordinate value.
     * @param z The third ordinate value.
     */
    public GeneralPosition(final double x, final double y, final double z) {
        ordinates = new double[] {x, y, z};
    }

    /**
     * Constructs a position from the specified {@link Point2D}.
     *
     * @param point The position to copy.
     */
    public GeneralPosition(final Point2D point) {
        this(point.getX(), point.getY());
    }

    /**
     * Constructs a position initialized to the same values than the specified point.
     *
     * @param point The position to copy.
     * @since 2.2
     */
    public GeneralPosition(final Position point) {
        ordinates = point.getCoordinate(); // Should already be cloned.
        crs = point.getCoordinateReferenceSystem();
    }

    /**
     * Returns the coordinate reference system in which the coordinate is given. May be {@code null} if this particular
     * {@code DirectPosition} is included in a larger object with such a reference to a
     * {@linkplain CoordinateReferenceSystem coordinate reference system}.
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
     * @throws MismatchedDimensionException if the specified CRS doesn't have the expected number of dimensions.
     */
    public void setCoordinateReferenceSystem(final CoordinateReferenceSystem crs) throws MismatchedDimensionException {
        checkCoordinateReferenceSystemDimension(crs, getDimension());
        this.crs = crs;
    }

    /**
     * The length of coordinate sequence (the number of entries). This may be less than or equal to the dimensionality
     * of the {@linkplain #getCoordinateReferenceSystem() coordinate reference system}.
     *
     * @return The dimensionality of this position.
     */
    @Override
    public final int getDimension() {
        return ordinates.length;
    }

    /**
     * Returns a sequence of numbers that hold the coordinate of this position in its reference system.
     *
     * @return A copy of the {@linkplain #ordinates coordinates}.
     */
    @Override
    public final double[] getCoordinate() {
        return ordinates.clone();
    }

    /**
     * Returns the ordinate at the specified dimension.
     *
     * @param dimension The dimension in the range 0 to {@linkplain #getDimension dimension}-1.
     * @return The coordinate at the specified dimension.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     */
    @Override
    public final double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        return ordinates[dimension];
    }

    /**
     * Sets the ordinate value along the specified dimension.
     *
     * @param dimension the dimension for the ordinate of interest.
     * @param value the ordinate value of interest.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     */
    @Override
    public final void setOrdinate(int dimension, double value) throws IndexOutOfBoundsException {
        ordinates[dimension] = value;
    }

    /**
     * Set this coordinate to the specified direct position. If the specified position contains a
     * {@linkplain CoordinateReferenceSystem coordinate reference system}, then the CRS for this position will be set to
     * the CRS of the specified position.
     *
     * @param position The new position for this point.
     * @throws MismatchedDimensionException if this point doesn't have the expected dimension.
     * @since 2.2
     */
    public final void setLocation(final Position position) throws MismatchedDimensionException {
        ensureDimensionMatch("position", position.getDimension(), ordinates.length);
        setCoordinateReferenceSystem(position.getCoordinateReferenceSystem());
        for (int i = 0; i < ordinates.length; i++) {
            ordinates[i] = position.getOrdinate(i);
        }
    }

    /**
     * Set this coordinate to the specified direct position. This method is identical to {@link #setLocation(Position)},
     * but is slightly faster in the special case of an {@code GeneralDirectPosition} implementation.
     *
     * @param position The new position for this point.
     * @throws MismatchedDimensionException if this point doesn't have the expected dimension.
     */
    public final void setLocation(final GeneralPosition position) throws MismatchedDimensionException {
        ensureDimensionMatch("position", position.ordinates.length, ordinates.length);
        setCoordinateReferenceSystem(position.crs);
        System.arraycopy(position.ordinates, 0, ordinates, 0, ordinates.length);
    }

    /**
     * Set this coordinate to the specified {@link Point2D}. This coordinate must be two-dimensional.
     *
     * @param point The new coordinate for this point.
     * @throws MismatchedDimensionException if this coordinate point is not two-dimensional.
     */
    public final void setLocation(final Point2D point) throws MismatchedDimensionException {
        if (ordinates.length != 2) {
            throw new MismatchedDimensionException(
                    MessageFormat.format(ErrorKeys.NOT_TWO_DIMENSIONAL_$1, ordinates.length));
        }
        ordinates[0] = point.getX();
        ordinates[1] = point.getY();
    }

    /**
     * Returns a {@link Point2D} with the same coordinate as this direct position. This is a convenience method for
     * interoperability with Java2D.
     *
     * @return This position as a two-dimensional point.
     * @throws IllegalStateException if this coordinate point is not two-dimensional.
     */
    public Point2D toPoint2D() throws IllegalStateException {
        if (ordinates.length != 2) {
            throw new IllegalStateException(MessageFormat.format(ErrorKeys.NOT_TWO_DIMENSIONAL_$1, ordinates.length));
        }
        return new Point2D.Double(ordinates[0], ordinates[1]);
    }

    /** Returns a hash value for this coordinate. */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        int code = Arrays.hashCode(ordinates);
        if (crs != null) {
            code += crs.hashCode();
        }
        assert code == super.hashCode();
        return code;
    }

    /** Returns a deep copy of this position. */
    @Override
    @SuppressFBWarnings("CN_IDIOM_NO_SUPER_CALL")
    public GeneralPosition clone() {
        return new GeneralPosition(ordinates);
    }
}
