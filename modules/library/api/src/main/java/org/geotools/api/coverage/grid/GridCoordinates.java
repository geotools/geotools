/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.coverage.grid;

import org.geotools.api.util.Cloneable;

/**
 * Holds the set of grid coordinates that specifies the location of the grid corners.
 *
 * @version ISO 19123:2004
 * @author Martin Schouwenburg
 * @author Wim Koolhoven
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 */
public interface GridCoordinates extends Cloneable {
    /**
     * Returns the number of dimensions. This method is equivalent to <code>
     * {@linkplain #getCoordinateValues()}.length</code>. It is provided for efficiency.
     *
     * @return The number of dimensions.
     */
    int getDimension();

    /**
     * Returns one integer value for each dimension of the grid. The value of a single coordinate shall be the number of
     * offsets from the origin of the grid in the direction of a specific axis.
     *
     * @return A copy of the coordinates. Changes in the returned array will not be reflected back in this
     *     {@code GridCoordinates} object.
     */
    int[] getCoordinateValues();

    /**
     * Returns the coordinate value at the specified dimension. This method is equivalent to <code>
     * {@linkplain #getCoordinateValues()}[<var>i</var>]</code>. It is provided for efficiency.
     *
     * @param dimension The dimension for which to obtain the coordinate value.
     * @return The coordinate value at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater than the
     *     {@linkplain #getDimension grid dimension}.
     */
    int getCoordinateValue(int dimension) throws IndexOutOfBoundsException;

    /**
     * Sets the coordinate value at the specified dimension (optional operation).
     *
     * @param dimension The dimension for which to set the coordinate value.
     * @param value The new value.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater than the
     *     {@linkplain #getDimension grid dimension}.
     * @throws UnsupportedOperationException if this grid coordinates is not modifiable.
     */
    void setCoordinateValue(int dimension, int value) throws IndexOutOfBoundsException, UnsupportedOperationException;
}
