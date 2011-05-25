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
import java.awt.image.RenderedImage;

import org.geotools.geometry.Envelope2D;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.util.Cloneable;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;


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
 *
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
     * Casts the specified envelope into a grid envelope. This is sometime useful after an
     * envelope has been transformed from "real world" coordinates to grid coordinates using the
     * {@linkplain org.opengis.coverage.grid.GridGeometry#getGridToCRS grid to CRS} transform.
     * The floating point values are rounded toward the nearest integers.
     * <p>
     * <strong>Notice that highest values are interpreted as non-inclusive</strong>
     * 
     * <p>
     * <b>Anchor</b><br>
     * According OpenGIS specification, {@linkplain org.opengis.coverage.grid.GridGeometry grid
     * geometry} maps pixel's center. But envelopes typically encompass all pixels. This means
     * that grid coordinates (0,0) has an envelope starting at (-0.5, -0.5). In order to revert
     * back such envelope to a grid envelope, it is necessary to add 0.5 to every coordinates
     * (including the maximum value since it is exclusive in a grid envelope). This offset is
     * applied only if {@code anchor} is {@link PixelInCell#CELL_CENTER}. Users who don't want
     * such offset should specify {@link PixelInCell#CELL_CORNER}.
     * <p>
     * The convention is specified as a {@link PixelInCell} code instead than the more detailed
     * {@link org.opengis.metadata.spatial.PixelOrientation} because the latter is restricted to
     * the two-dimensional case while the former can be used for any number of dimensions.
     *
     * @param envelope
     *          The envelope to use for initializing this grid envelope.
     * @param anchor
     *          Whatever envelope coordinates map to pixel center or pixel corner. Should be
     *          {@link PixelInCell#CELL_CENTER} for OGC convention (an offset of 0.5 will be
     *          added to every envelope coordinate values), or {@link PixelInCell#CELL_CORNER}
     *          for Java2D/JAI convention (no offset will be added).
     * @throws IllegalArgumentException
     *          If {@code anchor} is not valid.
     *
     */
    public GridEnvelope2D(final Envelope2D envelope, final PixelInCell anchor)
            throws IllegalArgumentException
    {
        this(envelope,anchor,false);
    }
    
    /**
     * Casts the specified envelope into a grid envelope. This is sometime useful after an
     * envelope has been transformed from "real world" coordinates to grid coordinates using the
     * {@linkplain org.opengis.coverage.grid.GridGeometry#getGridToCRS grid to CRS} transform.
     * The floating point values are rounded toward the nearest integers.
     * <p>
     * <b>Note about rounding mode</b><br>
     * It would have been possible to round the {@linkplain Envelope#getMinimum minimal value}
     * toward {@linkplain Math#floor floor} and the {@linkplain Envelope#getMaximum maximal value}
     * toward {@linkplain Math#ceil ceil} in order to make sure that the grid envelope encompass
     * fully the envelope - like what Java2D does when converting {@link java.awt.geom.Rectangle2D}
     * to {@link Rectangle}). But this approach may increase by 1 or 2 units the image
     * {@linkplain RenderedImage#getWidth width} or {@linkplain RenderedImage#getHeight height}. For
     * example the range {@code [-0.25 ... 99.75]} (which is exactly 101 units wide) would be casted
     * to {@code [-1 ... 100]}, which is 102 units wide. This leads to unexpected results when using
     * grid envelope with image operations like "{@link javax.media.jai.operator.AffineDescriptor
     * Affine}". For avoiding such changes in size, it is necessary to use the same rounding mode
     * for both minimal and maximal values. The selected rounding mode is {@linkplain Math#round
     * nearest integer} in this implementation.
     * <p>
     * <b>Anchor</b><br>
     * According OpenGIS specification, {@linkplain org.opengis.coverage.grid.GridGeometry grid
     * geometry} maps pixel's center. But envelopes typically encompass all pixels. This means
     * that grid coordinates (0,0) has an envelope starting at (-0.5, -0.5). In order to revert
     * back such envelope to a grid envelope, it is necessary to add 0.5 to every coordinates
     * (including the maximum value since it is exclusive in a grid envelope). This offset is
     * applied only if {@code anchor} is {@link PixelInCell#CELL_CENTER}. Users who don't want
     * such offset should specify {@link PixelInCell#CELL_CORNER}.
     * <p>
     * The convention is specified as a {@link PixelInCell} code instead than the more detailed
     * {@link org.opengis.metadata.spatial.PixelOrientation} because the latter is restricted to
     * the two-dimensional case while the former can be used for any number of dimensions.
     *
     * @param envelope
     *          The envelope to use for initializing this grid envelope.
     * @param anchor
     *          Whatever envelope coordinates map to pixel center or pixel corner. Should be
     *          {@link PixelInCell#CELL_CENTER} for OGC convention (an offset of 0.5 will be
     *          added to every envelope coordinate values), or {@link PixelInCell#CELL_CORNER}
     *          for Java2D/JAI convention (no offset will be added).
     * @param isHighIncluded
     *          {@code true} if the envelope maximal values are inclusive, or {@code false} if
     *          they are exclusive. This argument does not apply to minimal values, which are
     *          always inclusive.
     * @throws IllegalArgumentException
     *          If {@code anchor} is not valid.
     *
     */
    public GridEnvelope2D(final Envelope2D envelope, final PixelInCell anchor,
                               final boolean isHighIncluded)
            throws IllegalArgumentException
    {
        final double offset = PixelTranslation.getPixelTranslation(anchor) + 0.5;
        final int dimension = envelope.getDimension();
        assert dimension==2;
        final int[] index = new int[dimension * 2];
        for (int i=0; i<dimension; i++) {
            // See "note about conversion of floating point values to integers" in the JavaDoc.
            index[i            ] = (int) Math.round(envelope.getMinimum(i) + offset);
            index[i + dimension] = (int) Math.round(envelope.getMaximum(i) + offset);
        }
        if (isHighIncluded) {
            for (int i=index.length/2; i<index.length; i++) {
                index[i]++;
            }
        }
        
        setLocation(index[0], index[1]);
        setSize(index[0+dimension]-index[0], index[1+dimension]-index[1]);
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
