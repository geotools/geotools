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
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.Classes;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.util.Cloneable;


/**
 * A simple grid geometry holding the grid range as a {@linkplain Rectangle rectangle} and the
 * <cite>grid to CRS</cite> relationship as an {@linkplain AffineTransform affine transform}.
 * This grid geometry does not hold any Coordinate Reference System information. Because of that,
 * it is not suitable to {@link GridCoverage2D} (the later rather use {@link GridGeometry2D}).
 * But it is sometime used with plain {@linkplain java.awt.image.RenderedImage rendered image}
 * instances.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see GridGeometry2D
 * @see GeneralGridGeometry
 */
public class ImageGeometry implements GridGeometry, Serializable, Cloneable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 1985363181119389264L;

    /**
     * The grid range.
     */
    private final GridEnvelope2D gridRange;

    /**
     * The <cite>grid to CRS</cite> affine transform.
     */
    private final AffineTransform2D gridToCRS;

    /**
     * Creates a grid geometry from the specified bounds and <cite>grid to CRS</cite>
     * affine transform.
     *
     * @param bounds The image bounds in pixel coordinates.
     * @param gridToCRS The affine transform from pixel coordinates to "real world" coordinates.
     */
    public ImageGeometry(final Rectangle bounds, final AffineTransform gridToCRS) {
        this.gridRange = new GridEnvelope2D(bounds);
        this.gridToCRS = new AffineTransform2D(gridToCRS);
    }

    /**
     * Returns the image bounds in pixel coordinates.
     */
    public GridEnvelope2D getGridRange() {
        return gridRange.clone();
    }

    /**
     * Returns the conversion from grid coordinates to real world earth coordinates.
     */
    public AffineTransform2D getGridToCRS() {
        return gridToCRS; // No need to clone since AffineTransform2D is immutable.
    }

    /**
     * @deprecated Renamed as {@link #getGridToCRS()}.
     */
    public AffineTransform2D getGridToCoordinateSystem() {
        return gridToCRS;
    }

    /**
     * Returns a string representation of this grid geometry. The returned string
     * is implementation dependent. It is usually provided for debugging purposes.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) + '[' + gridRange + ", " + gridToCRS + ']';
    }

    /**
     * Returns a hash code value for this grid geometry.
     */
    @Override
    public int hashCode() {
        return gridRange.hashCode() ^ gridToCRS.hashCode();
    }

    /**
     * Compares this grid geometry with the specified one for equality.
     *
     * @param object The object to compare with.
     * @return {@code true} if the given object is equals to this grid geometry.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object != null && object.getClass().equals(getClass())) {
            final ImageGeometry that = (ImageGeometry) object;
            return Utilities.equals(gridRange, that.gridRange) &&
                   Utilities.equals(gridToCRS, that.gridToCRS);
        }
        return false;
    }

    /**
     * Returns a clone of this image geometry.
     *
     * @return A clone of this grid geometry.
     */
    @Override
    public ImageGeometry clone() {
        try {
            return (ImageGeometry) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception); // Should never happen, since we are cloneable.
        }
    }
}
