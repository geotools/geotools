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

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.geotools.util.Utilities;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.Cloneable;


/**
 * Holds the coordinates for a two-dimensional position within some coordinate reference system.
 * <p>
 * <b>Note 1:</b><blockquote>
 * This class inherits {@linkplain #x x} and {@linkplain #y y} fields. But despite their names,
 * they don't need to be oriented toward {@linkplain org.opengis.referencing.cs.AxisDirection#EAST
 * East} and {@linkplain org.opengis.referencing.cs.AxisDirection#NORTH North}.
 * The (<var>x</var>,<var>y</var>) axis can have any orientation and should be understood as
 * "<cite>ordinate 0</cite>" and "<cite>ordinate 1</cite>" values instead. This is not specific
 * to this implementation; in Java2D too, the visual axis orientation depend on the
 * {@linkplain java.awt.Graphics2D#getTransform affine transform in the graphics context}.
 * <p>
 * The rational for avoiding axis orientation restriction is that other {@link DirectPosition}
 * implementation do not have such restriction, and it would be hard to generalize (what to do
 * with {@linkplain org.opengis.referencing.cs.AxisDirection#NORTH_EAST North-East} direction?).
 * </blockquote>
 * <p>
 * <b>Note 2:</b><blockquote>
 * <strong>Do not mix instances of this class with ordinary {@link Point2D} instances in a
 * {@link java.util.HashSet} or as {@link java.util.HashMap} keys.</strong> It is not possible to
 * meet both {@link Point2D#hashCode} and {@link DirectPosition#hashCode} contract, and this class
 * choose to implements the later. Concequently, <strong>{@link #hashCode} is inconsistent with
 * {@link Point2D#equals}</strong> (but is consistent with {@link DirectPosition#equals}).
 * <p>
 * In other words, it is safe to add instances of {@code DirectPosition2D} in a
 * {@code HashSet<DirectPosition>}, but it is unsafe to add them in a {@code HashSet<Point2D>}.
 * Collections that do not rely on {@link Object#hashCode}, like {@link java.util.ArrayList},
 * are safe in all cases.
 * </blockquote>
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DirectPosition1D
 * @see GeneralDirectPosition
 * @see java.awt.geom.Point2D
 */
public class DirectPosition2D extends Point2D.Double implements DirectPosition, Serializable, Cloneable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 835130287438466996L;

    /**
     * The coordinate reference system for this position;
     */
    private CoordinateReferenceSystem crs;

    /**
     * Constructs a position initialized to (0,0) with a {@code null}
     * coordinate reference system.
     */
    public DirectPosition2D() {
    }

    /**
     * Constructs a position with the specified coordinate reference system.
     *
     * @param crs The coordinate reference system, or {@code null}.
     */
    public DirectPosition2D(final CoordinateReferenceSystem crs) {
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Constructs a 2D position from the specified ordinates. Despite their name,
     * the (<var>x</var>,<var>y</var>) coordinates don't need to be oriented toward
     * ({@linkplain org.opengis.referencing.cs.AxisDirection#EAST  East},
     *  {@linkplain org.opengis.referencing.cs.AxisDirection#NORTH North}).
     * Those parameter names simply match the {@linkplain #x x} and {@linkplain #y y}
     * fields. See the {@linkplain DirectPosition2D class javadoc} for details.
     *
     * @param x The <var>x</var> value.
     * @param y The <var>y</var> value.
     */
    public DirectPosition2D(final double x, final double y) {
        super(x,y);
    }

    /**
     * Constructs a 2D position from the specified ordinates in the specified CRS. Despite
     * their name, the (<var>x</var>,<var>y</var>) coordinates don't need to be oriented toward
     * ({@linkplain org.opengis.referencing.cs.AxisDirection#EAST  East},
     *  {@linkplain org.opengis.referencing.cs.AxisDirection#NORTH North}).
     * Those parameter names simply match the {@linkplain #x x} and {@linkplain #y y}
     * fields. The actual axis orientations are determined by the specified CRS.
     * See the {@linkplain DirectPosition2D class javadoc} for details.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @param x The <var>x</var> value.
     * @param y The <var>y</var> value.
     */
    public DirectPosition2D(final CoordinateReferenceSystem crs,
                            final double x, final double y)
    {
        super(x, y);
        setCoordinateReferenceSystem(crs);
    }

    /**
     * Constructs a position from the specified {@link Point2D}.
     *
     * @param point The point to copy.
     */
    public DirectPosition2D(final Point2D point) {
        super(point.getX(), point.getY());
        if (point instanceof DirectPosition) {
            setCoordinateReferenceSystem(((DirectPosition) point).getCoordinateReferenceSystem());
        }
    }

    /**
     * Constructs a position initialized to the same values than the specified point.
     *
     * @param point The point to copy.
     */
    public DirectPosition2D(final DirectPosition point) {
        setLocation(point);
    }

    /**
     * @deprecated Renamed as {@link #getDirectPosition}.
     */
    @Deprecated
    public DirectPosition getPosition() {
        return this;
    }

    /**
     * Returns always {@code this}, the direct position for this
     * {@linkplain org.opengis.geometry.coordinate.Position position}.
     *
     * @since 2.5
     */
    public DirectPosition getDirectPosition() {
        return this;
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
        AbstractDirectPosition.checkCoordinateReferenceSystemDimension(crs, 2);
        this.crs = crs;
    }

    /**
     * The length of coordinate sequence (the number of entries).
     * This is always 2 for <code>DirectPosition2D</code> objects.
     *
     * @return The dimensionality of this position.
     */
    public final int getDimension() {
        return 2;
    }

    /**
     * Returns a sequence of numbers that hold the coordinate of this position in its
     * reference system.
     *
     * @return The coordinates
     */
    public double[] getCoordinate() {
        return new double[] {x,y};
    }

    /**
     * @deprecated Renamed as {@link #getCoordinate} for consistency with ISO 19107.
     *
     * @return A copy of the coordinates.
     */
    @Deprecated
    public double[] getCoordinates() {
        return getCoordinate();
    }

    /**
     * Returns the ordinate at the specified dimension.
     *
     * @param  dimension The dimension in the range 0 to 1 inclusive.
     * @return The coordinate at the specified dimension.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     *
     * @todo Provides a more detailled error message.
     */
    public final double getOrdinate(final int dimension) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:  return x;
            case 1:  return y;
            default: throw new IndexOutOfBoundsException(String.valueOf(dimension));
        }
    }

    /**
     * Sets the ordinate value along the specified dimension.
     *
     * @param  dimension the dimension for the ordinate of interest.
     * @param  value the ordinate value of interest.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     *
     * @todo Provides a more detailled error message.
     */
    public final void setOrdinate(int dimension, double value) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:  x=value; break;
            case 1:  y=value; break;
            default: throw new IndexOutOfBoundsException(String.valueOf(dimension));
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
        AbstractDirectPosition.ensureDimensionMatch("position", position.getDimension(), 2);
        setCoordinateReferenceSystem(position.getCoordinateReferenceSystem());
        x = position.getOrdinate(0);
        y = position.getOrdinate(1);
    }

    /**
     * Returns a {@link Point2D} with the same coordinate as this direct position.
     *
     * @return This position as a point.
     */
    public Point2D toPoint2D() {
        return new Point2D.Double(x,y);
    }

    /**
     * Returns a string representation of this coordinate. The default implementation formats
     * this coordinate using a shared instance of {@link org.geotools.measure.CoordinateFormat}.
     * This is okay for occasional formatting (for example for debugging purpose). But if there
     * is a lot of positions to format, users will get better performance and more control by
     * using their own instance of {@link org.geotools.measure.CoordinateFormat}.
     */
    @Override
    public String toString() {
        return AbstractDirectPosition.toString(this);
    }

    /**
     * Returns a hash value for this coordinate. This method implements the
     * {@link DirectPosition#hashCode} contract, not the {@link Point2D#hashCode} contract.
     *
     * @return A hash code value for this position.
     */
    @Override
    public int hashCode() {
        return AbstractDirectPosition.hashCode(this);
    }

    /**
     * Compares this point with the specified object for equality. If the given object implements
     * the {@link DirectPosition} interface, then the comparaison is performed as specified in its
     * {@link DirectPosition#equals} contract. Otherwise the comparaison is performed as specified
     * in {@link Point2D#equals}.
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
        if (object instanceof DirectPosition) {
            final DirectPosition other = (DirectPosition) object;
            if (other.getDimension() == 2 &&
                Utilities.equals(other.getOrdinate(0), x) &&
                Utilities.equals(other.getOrdinate(1), y) &&
                Utilities.equals(other.getCoordinateReferenceSystem(), crs))
            {
                assert hashCode() == other.hashCode() : this;
                return true;
            }
            return false;
        }
        /*
         * Otherwise performs the comparaison as in Point2D.equals(Object).
         * Do NOT check the CRS if the given object is an ordinary Point2D.
         * This is necessary in order to respect the contract defined in Point2D.
         */
        return super.equals(object);
    }

    /**
     * Returns a clone of this point.
     *
     * @return A clone of this position.
     */
    @Override
    public DirectPosition2D clone() {
        return (DirectPosition2D) super.clone();
    }

    /**
     * Write this object to the specified stream. This method is necessary
     * because the super-class is not serializable.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(x);
        out.writeDouble(y);
    }

    /**
     * Read this object from the specified stream. This method is necessary
     * because the super-class is not serializable.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        x = in.readDouble();
        y = in.readDouble();
    }
}
