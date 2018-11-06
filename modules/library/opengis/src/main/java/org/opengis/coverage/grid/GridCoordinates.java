/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage.grid;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.Extension;
import org.opengis.annotation.UML;
import org.opengis.util.Cloneable;

/**
 * Holds the set of grid coordinates that specifies the location of the {@linkplain GridPoint grid
 * point} within the {@linkplain Grid grid}.
 *
 * @version ISO 19123:2004
 * @author Martin Schouwenburg
 * @author Wim Koolhoven
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 */
@UML(identifier = "CV_GridCoordinates", specification = ISO_19123)
public interface GridCoordinates extends Cloneable {
    /**
     * Returns the number of dimensions. This method is equivalent to <code>
     * {@linkplain #getCoordinateValues()}.length</code>. It is provided for efficienty.
     *
     * @return The number of dimensions.
     */
    @Extension
    int getDimension();

    /**
     * Returns one integer value for each dimension of the grid. The ordering of these coordinate
     * values shall be the same as that of the elements of {@link Grid#getAxisNames}. The value of a
     * single coordinate shall be the number of offsets from the origin of the grid in the direction
     * of a specific axis.
     *
     * @return A copy of the coordinates. Changes in the returned array will not be reflected back
     *     in this {@code GridCoordinates} object.
     */
    @UML(identifier = "coordValues", obligation = MANDATORY, specification = ISO_19123)
    int[] getCoordinateValues();

    /**
     * Returns the coordinate value at the specified dimension. This method is equivalent to <code>
     * {@linkplain #getCoordinateValues()}[<var>i</var>]</code>. It is provided for efficienty.
     *
     * @param dimension The dimension for which to obtain the coordinate value.
     * @return The coordinate value at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater than
     *     the {@linkplain #getDimension grid dimension}.
     */
    @Extension
    int getCoordinateValue(int dimension) throws IndexOutOfBoundsException;

    /**
     * Sets the coordinate value at the specified dimension (optional operation).
     *
     * @param dimension The dimension for which to set the coordinate value.
     * @param value The new value.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater than
     *     the {@linkplain #getDimension grid dimension}.
     * @throws UnsupportedOperationException if this grid coordinates is not modifiable.
     */
    @Extension
    void setCoordinateValue(int dimension, int value)
            throws IndexOutOfBoundsException, UnsupportedOperationException;
}
