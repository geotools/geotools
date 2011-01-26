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

import java.awt.Rectangle;
import org.opengis.util.Cloneable;
import org.opengis.coverage.grid.GridEnvelope;


/**
 * Defines a range of two-dimensional grid coverage coordinates. This implementation extends
 * {@link Rectangle} for interoperability with Java2D. Note that at the opposite of
 * {@link GeneralGridEnvelope}, this class is mutable.
 * <p>
 * <b>CAUTION:</b>
 * ISO 19123 defines {@linkplain #getHigh high} coordinates as <strong>inclusive</strong>.
 * We follow this specification for all getters methods, but keep in mind that this is the
 * opposite of Java2D usage where {@link Rectangle} maximal values are exclusive.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see GeneralGridEnvelope
 */
public class GridEnvelope2D extends Rectangle implements GridEnvelope, Cloneable {
    /**
     * For cross-version interoperability.
     */
    private static final long serialVersionUID = -3370515914148690059L;

    /**
     * Creates an initially empty grid envelope.
     */
    public GridEnvelope2D() {
    }

    /**
     * Creates a grid envelope initialized to the specified rectangle.
     *
     * @param rectangle The rectangle to use for initializing this grid envelope.
     */
    public GridEnvelope2D(final Rectangle rectangle) {
        super(rectangle);
    }

    /**
     * Creates a grid envelope initialized to the specified rectangle.
     *
     * @param x The minimal <var>x</var> ordinate.
     * @param y The minimal <var>y</var> ordinate.
     * @param width  The number of valid ordinates along the <var>x</var> axis.
     * @param height The number of valid ordinates along the <var>y</var> axis.
     */
    public GridEnvelope2D(final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
    }

    /**
     * Returns the number of dimensions, which is always 2.
     */
    public final int getDimension() {
        return 2;
    }

    /**
     * Returns the valid minimum inclusive grid coordinates.
     * The sequence contains a minimum value for each dimension of the grid coverage.
     */
    public GridCoordinates2D getLow() {
        return new GridCoordinates2D(x, y);
    }

    /**
     * Returns the valid maximum <strong>inclusive</strong> grid coordinates.
     * The sequence contains a maximum value for each dimension of the grid coverage.
     */
    public GridCoordinates2D getHigh() {
        return new GridCoordinates2D(x + width - 1, y + height - 1);
    }

    /**
     * Returns the valid minimum inclusive grid coordinate along the specified dimension.
     *
     * @see #getLow()
     */
    public int getLow(final int dimension) {
        switch (dimension) {
            case 0:  return x;
            case 1:  return y;
            default: throw new IndexOutOfBoundsException(GridCoordinates2D.indexOutOfBounds(dimension));
        }
    }

    /**
     * Returns the valid maximum <strong>inclusive</strong>
     * grid coordinate along the specified dimension.
     *
     * @see #getHigh()
     */
    public int getHigh(final int dimension) {
        switch (dimension) {
            case 0:  return x + width  - 1;
            case 1:  return y + height - 1;
            default: throw new IndexOutOfBoundsException(GridCoordinates2D.indexOutOfBounds(dimension));
        }
    }

    /**
     * Returns the number of integer grid coordinates along the specified dimension.
     * This is equals to {@code getHigh(dimension) - getLow(dimension)}.
     */
    public int getSpan(final int dimension) {
        switch (dimension) {
            case 0:  return width;
            case 1:  return height;
            default: throw new IndexOutOfBoundsException(GridCoordinates2D.indexOutOfBounds(dimension));
        }
    }

    // Inherit 'hashCode()' and 'equals' from Rectangle2D, which provides an implementation
    // aimed to be common for every Rectangle2D subclasses (not just the Java2D ones) -  we
    // don't want to change this behavior in order to stay consistent with Java2D.

    /**
     * Returns a string représentation of this grid envelope. The returned string is
     * implementation dependent. It is usually provided for debugging purposes.
     */
    @Override
    public String toString() {
        return GeneralGridEnvelope.toString(this);
    }

    /**
     * Returns a clone of this grid envelope.
     *
     * @return A clone of this grid envelope.
     */
    @Override
    public GridEnvelope2D clone() {
        return (GridEnvelope2D) super.clone();
    }
}
