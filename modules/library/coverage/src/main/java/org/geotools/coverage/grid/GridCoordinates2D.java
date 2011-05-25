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
package org.geotools.coverage.grid;

import java.awt.Point;

import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.coverage.grid.GridCoordinates;


/**
 * Holds the set of two-dimensional grid coordinates that specifies the location of the
 * {@linkplain GridPoint grid point} within the {@linkplain Grid grid}. This class extends
 * {@link Point} for interoperability with Java2D.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see GeneralGridCoordinates
 */
public class GridCoordinates2D extends Point implements GridCoordinates {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -4583333545268906740L;

    /**
     * Creates an initially empty grid coordinates.
     */
    public GridCoordinates2D() {
        super();
    }

    /**
     * Creates a grid coordinates initialized to the specified values.
     *
     * @param x The <var>x</var> coordinate value.
     * @param y The <var>y</var> coordinate value.
     */
    public GridCoordinates2D(final int x, final int y) {
        super(x,y);
    }

    /**
     * Creates a grid coordinates initialized to the specified point.
     *
     * @param coordinates The coordinate values to copy.
     */
    public GridCoordinates2D(final Point coordinates) {
        super(coordinates);
    }

    /**
     * Returns the number of dimensions, which is always 2.
     */
    public final int getDimension() {
        return 2;
    }

    /**
     * Returns one integer value for each dimension of the grid. This method returns
     * ({@linkplain #x x},{@linkplain #y y}) in an array of length 2.
     */
    public int[] getCoordinateValues() {
        return new int[] {x,y};
    }

    /**
     * Returns the coordinate value at the specified dimension. This method is equivalent to
     * <code>{@linkplain #getCoordinateValues()}[<var>i</var>]</code>. It is provided for
     * efficienty.
     *
     * @param  dimension The dimension from 0 inclusive to {@link #getDimension} exclusive.
     * @return The value at the requested dimension.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     */
    public int getCoordinateValue(final int dimension) throws IndexOutOfBoundsException {
        switch (dimension) {
            case 0:  return x;
            case 1:  return y;
            default: throw new IndexOutOfBoundsException(indexOutOfBounds(dimension));
        }
    }

    /**
     * Sets the coordinate value at the specified dimension.
     *
     * @param  dimension The index of the value to set.
     * @param  value The new value.
     * @throws IndexOutOfBoundsException if the specified dimension is out of bounds.
     * @throws UnsupportedOperationException if this grid coordinates is not modifiable.
     */
    public void setCoordinateValue(final int dimension, final int value)
            throws IndexOutOfBoundsException, UnsupportedOperationException
    {
        switch (dimension) {
            case 0:  x = value; break;
            case 1:  y = value; break;
            default: throw new IndexOutOfBoundsException(indexOutOfBounds(dimension));
        }
    }

    /**
     * Formats a message for an index out of 2D bounds.
     */
    static String indexOutOfBounds(final int dimension) {
        return Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, dimension);
    }

    /**
     * Returns a string representation of this grid coordinates.
     */
    @Override
    public String toString() {
        return GeneralGridCoordinates.toString(this);
    }

    // Inherit 'hashCode()' and 'equals' from Point2D, which provides an implementation
    // aimed to be common for every Point2D subclasses (not just the Java2D ones) -  we
    // don't want to change this behavior in order to stay consistent with Java2D.

    /**
     * Returns a clone of this coordinates.
     *
     * @return A clone of this coordinates.
     */
    @Override
    public GridCoordinates2D clone() {
        return (GridCoordinates2D) super.clone();
    }

	/**
	 * Makes sure that an argument is non-null.
	 *
	 * @param  name   Argument name.
	 * @param  object User argument.
	 * @throws IllegalArgumentException if {@code object} is null.
	 */
	static void ensureNonNull(final String name, final Object object)
	    throws IllegalArgumentException
	{
	    if (object == null) {
	        throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
	    }
	}
}
