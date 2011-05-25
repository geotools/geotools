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
import java.io.Serializable;

// OpenGIS dependencies
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;


//geotools JTS wrappers dependency
import org.geotools.geometry.jts.GeometryUtils;

/**
 * Holds the coordinates for a one-dimensional position within some coordinate reference system.
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class DirectPosition1D implements DirectPosition, Serializable, Cloneable {
    
    /**
     * The coordinate reference system for this position;
     */
    private CoordinateReferenceSystem crs;

    /**
     * The ordinate value.
     */
    public double ordinate;
    
    /**
     * Construct a position initialized to (0) with a {@code null}
     * coordinate reference system.
     */
    public DirectPosition1D() {
    }
    
    /**
     * Construct a position with the specified coordinate reference system.
     */
    public DirectPosition1D(final CoordinateReferenceSystem crs) {
        setCoordinateReferenceSystem(crs);
    }
    
    /**
     * Construct a 1D position from the specified ordinate.
     */
    public DirectPosition1D(final double ordinate) {
        this.ordinate = ordinate;
    }
    
    /**
     * Construct a position initialized to the same values than the specified point.
     */
    public DirectPosition1D(final DirectPosition point) {
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
        GeometryUtils.checkDimension("dimension", crs, 1);
        this.crs = crs;
    }

    /**
     * The length of coordinate sequence (the number of entries).
     * This is always 1 for {@code DirectPosition1D} objects.
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
     * @return The coordinates
     */
    public double[] getCoordinate() {
        return new double[] {ordinate};
    }
    @Deprecated
    public double[] getCoordinates() {
        return getCoordinate();
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
        try {
        GeometryUtils.ensureDimensionMatch("position", position.getDimension(), 1);
        }
        catch (MismatchedDimensionException mde){}
        setCoordinateReferenceSystem(position.getCoordinateReferenceSystem());
        ordinate = position.getOrdinate(0);
    }
    
    /**
     * Returns a string representation of this coordinate. The returned string is
     * implementation dependent. It is usually provided for debugging purposes.
     */
    @Override
    public String toString() {
        return DirectPosition2D.toString(this, getCoordinates());
    }
    
    /**
     * Returns a hash value for this coordinate. This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        final long value = Double.doubleToLongBits(ordinate);
        int code = (int)value ^ (int)(value >>> 32);
        if (crs != null) {
            code ^= crs.hashCode();
        }
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
