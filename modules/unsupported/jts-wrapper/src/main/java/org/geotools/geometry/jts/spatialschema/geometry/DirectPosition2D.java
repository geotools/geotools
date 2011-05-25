/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
/*
 * $ Id $
 * $ Source $
 * Created on May 5, 2005
 */
package org.geotools.geometry.jts.spatialschema.geometry;

//J2SE dependencies
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// OpenGIS dependencies
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;

//geotools JTS wrappers dependency
import org.geotools.geometry.jts.GeometryUtils;

/**
 * Holds the coordinates for a two-dimensional position within some coordinate reference system.
 * This class put no restriction on the axis order, i.e. it doesn't ensure that the value stored
 * in the {@linkplain #x x} field increases toward {@linkplain AxisDirection#EAST East} and the
 * value in the {@linkplain #y y} field toward {@linkplain AxisDirection#NORTH North}. The rational
 * is that other {@link DirectPosition} implementation do not have restriction on axis order, and
 * anyway such a restriction is hard to generalize (what to do with
 * {@linkplain AxisDirection#NORTH_EAST North-East} direction?).
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class DirectPosition2D extends Point2D.Double implements DirectPosition, Serializable {
    
    /**
     * The coordinate reference system for this position;
     */
    private CoordinateReferenceSystem crs;
    
    /**
     * Construct a position initialized to (0,0) with a {@code null}
     * coordinate reference system.
     */
    public DirectPosition2D() {
    }
    
    /**
     * Construct a position with the specified coordinate reference system.
     */
    public DirectPosition2D(final CoordinateReferenceSystem crs) {
        setCoordinateReferenceSystem(crs);
    }
    
    /**
     * Construct a 2D position from the specified ordinates.
     */
    public DirectPosition2D(final double x, final double y) {
        super(x,y);
    }
    
    /**
     * Construct a 2D position from the specified ordinates in the specified CRS.
     */
    public DirectPosition2D(final CoordinateReferenceSystem crs,
                            final double x, final double y)
    {
        super(x,y);
        setCoordinateReferenceSystem(crs);
    }
    
    /**
     * Construct a position from the specified {@link Point2D}.
     */
    public DirectPosition2D(final Point2D point) {
        super(point.getX(), point.getY());
        if (point instanceof DirectPosition) {
            setCoordinateReferenceSystem(((DirectPosition) point).getCoordinateReferenceSystem());
        }
    }
    
    /**
     * Construct a position initialized to the same values than the specified point.
     */
    public DirectPosition2D(final DirectPosition point) {
        setLocation(point);
    }

    /**
     * Returns always {@code this}, the direct position for this
     * {@linkplain org.opengis.geometry.coordinate.Position position}.
     */
    @Deprecated
    public DirectPosition getPosition() {
        return this;
    }

    /**
     * Returns always {@code this}, the direct position for this
     * {@linkplain org.opengis.geometry.coordinate.Position position}.
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
        GeometryUtils.checkDimension("dimension", crs, 2);
        this.crs = crs;
    }

    /**
     * The length of coordinate sequence (the number of entries).
     * This is always 2 for {@code DirectPosition2D} objects.
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
        GeometryUtils.ensureDimensionMatch("position", position.getDimension(), 2);
        setCoordinateReferenceSystem(position.getCoordinateReferenceSystem());
        x = position.getOrdinate(0);
        y = position.getOrdinate(1);
    }
    
    /**
     * Returns a {@link Point2D} with the same coordinate as this direct position.
     */
    public Point2D toPoint2D() {
        return new Point2D.Double(x,y);
    }
    
    /**
     * Returns a string representation of this coordinate. The returned string is
     * implementation dependent. It is usually provided for debugging purposes.
     */
    public String toString() {
        return toString(this, getCoordinates());
    }
    
    /**
     * Returns a hash value for this coordinate. This value need not remain consistent between
     * different implementations of the same class.
     */
    public int hashCode() {
        int code = super.hashCode();
        if (crs != null) {
            code ^= crs.hashCode();
        }
        return code;
    }

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
    
    /**
     * Returns a string representation of an object. The returned string is implementation
     * dependent. It is usually provided for debugging purposes.
     */
    static String toString(final Object owner, final double[] ordinates) {
        final StringBuffer buffer = new StringBuffer(owner.toString());//new StringBuffer(Utilities.getShortClassName(owner));
        buffer.append('[');
        for (int i=0; i<ordinates.length; i++) {
            if (i!=0) buffer.append(", ");
            buffer.append(ordinates[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }
}
