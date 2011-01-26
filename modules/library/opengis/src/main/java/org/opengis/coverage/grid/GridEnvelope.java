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

import java.awt.Rectangle;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Provides the {@linkplain GridCoordinates grid coordinate} values for the diametrically opposed
 * corners of the {@linkplain Grid grid}.
 * <p>
 * Remark that both corners are inclusive. Thus the number of elements in the direction of the first
 * axis is <code>{@linkplain #getHigh(int) getHigh}(0) - {@linkplain #getLow(int) getLow}(0) + 1</code>.
 * This is the opposite of Java2D usage where maximal values in {@link Rectangle} (as computed by
 * {@linkplain Rectangle#getMaxX getMaxX()} and {@linkplain Rectangle#getMinY getMaxY()}) are
 * exclusive.
 *
 * @version ISO 19123:2004
 * @author  Wim Koolhoven
 * @author  Martin Schouwenburg
 * @since   GeoAPI 2.1
 *
 * @see org.opengis.geometry.Envelope
 */
@UML(identifier="CV_GridEnvelope", specification=ISO_19123)
public interface GridEnvelope {
    /**
     * Returns the number of dimensions. It must be equals to the number of dimensions
     * of {@linkplain #getLow low} and {@linkplain #getHigh high} grid coordinates.
     *
     * @return The number of dimensions.
     *
     * @since GeoAPI 2.2
     */
    @Extension
    int getDimension();

    /**
     * Returns the minimal coordinate values for all grid points within the {@linkplain Grid grid}.
     *
     * @return The minimal coordinate values for all grid points, inclusive.
     */
    @UML(identifier="low", obligation=MANDATORY, specification=ISO_19123)
    GridCoordinates getLow();

    /**
     * Returns the maximal coordinate values for all grid points within the {@linkplain Grid grid}.
     *
     * @return The maximal coordinate values for all grid points, <strong>inclusive</strong>.
     */
    @UML(identifier="high", obligation=MANDATORY, specification=ISO_19123)
    GridCoordinates getHigh();

    /**
     * Returns the valid minimum inclusive grid coordinate along the specified dimension. This is a
     * shortcut for the following without the cost of creating a temporary {@link GridCoordinates} object:
     *
     * <blockquote><code>
     * {@linkplain #getLow}.{@linkplain GridCoordinates#getCoordinateValue getCoordinateValue}(dimension)
     * </code></blockquote>
     *
     * @param  dimension The dimension for which to obtain the coordinate value.
     * @return The coordinate value at the given dimension, inclusive.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension grid dimension}.
     *
     * @see Rectangle#x
     * @see Rectangle#y
     *
     * @since GeoAPI 2.2
     */
    @Extension
    int getLow(int dimension) throws IndexOutOfBoundsException;

    /**
     * Returns the valid maximum inclusive grid coordinate along the specified dimension. This is a
     * shortcut for the following without the cost of creating a temporary {@link GridCoordinates} object:
     *
     * <blockquote><code>
     * {@linkplain #getHigh}.{@linkplain GridCoordinates#getCoordinateValue getCoordinateValue}(dimension)
     * </code></blockquote>
     *
     * @param  dimension The dimension for which to obtain the coordinate value.
     * @return The coordinate value at the given dimension, <strong>inclusive</strong>.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension grid dimension}.
     *
     * @since GeoAPI 2.2
     */
    @Extension
    int getHigh(int dimension) throws IndexOutOfBoundsException;

    /**
     * Returns the number of integer grid coordinates along the specified dimension.
     * This is equals to:
     *
     * <blockquote><code>
     * {@linkplain #getHigh getHigh}(dimension) - {@linkplain #getLow getLow}(dimension) + 1
     * </code></blockquote>
     *
     * @param  dimension The dimension for which to obtain the coordinate value.
     * @return The coordinate value at the given dimension.
     * @throws IndexOutOfBoundsException If the given index is negative or is equals or greater
     *         than the {@linkplain #getDimension grid dimension}.
     *
     * @see Rectangle#width
     * @see Rectangle#height
     *
     * @since GeoAPI 2.2
     */
    @Extension
    int getSpan(int dimension) throws IndexOutOfBoundsException;
}
