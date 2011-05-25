/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.mosaic;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.imageio.spi.ImageReaderSpi;
import java.io.IOException;


/**
 * A tile with larger capacity than the default {@link Tile} implementation. Instances of this
 * class usually don't need to be created, since the whole purpose of tile is to represent a
 * small portion of an image. However it is needed in some case, typically to represent an image
 * before a mosaic is built from it.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class LargeTile extends Tile {
    /**
     * For cross-version compatibility during serialization.
     */
    private static final long serialVersionUID = -390809968753673788L;

    /**
     * The size of the image to be read.
     */
    private int width, height;

    /**
     * Creates a tile for the given provider, input and location.
     * See {@linkplain Tile#Tile(ImageReaderSpi,Object,int,Rectangle,Dimension)
     * super-class constructor} for details.
     *
     * @param provider    The image reader provider to use, or {@code null}.
     * @param input       The input to be given to the image reader.
     * @param imageIndex  The image index to be given to the image reader for reading this tile.
     * @param location    The upper-left corner in the destination image.
     * @param subsampling The subsampling relative to the tile having the finest resolution,
     *                    or {@code null}.
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public LargeTile(ImageReaderSpi provider, Object input, int imageIndex,
                Point location, Dimension subsampling) throws IllegalArgumentException
    {
        super(provider, input, imageIndex, location, subsampling);
    }

    /**
     * Creates a tile for the given provider, input and region.
     * See {@linkplain Tile#Tile(ImageReaderSpi,Object,int,Rectangle,Dimension)
     * super-class constructor} for details.
     *
     * @param provider    The image reader provider to use, or {@code null}.
     * @param input       The input to be given to the image reader.
     * @param imageIndex  The image index to be given to the image reader for reading this tile.
     * @param region      The region in the destination image.
     * @param subsampling The subsampling relative to the tile having the finest resolution,
     *                    or {@code null}.
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public LargeTile(ImageReaderSpi provider, final Object input, final int imageIndex,
                final Rectangle region, final Dimension subsampling)
                throws IllegalArgumentException
    {
        super(provider, input, imageIndex, region, subsampling);
    }

    /**
     * Creates a tile for the given provider, input and "<cite>grid to real world</cite>" transform.
     * See {@linkplain Tile#Tile(ImageReaderSpi,Object,int,Rectangle,AffineTransform)
     * super-class constructor} for details.
     *
     * @param provider    The image reader provider to use, or {@code null}.
     * @param input       The input to be given to the image reader.
     * @param imageIndex  The image index to be given to the image reader for reading this tile.
     * @param region      The region in the destination image, or {@code null}.
     * @param gridToCRS   The "<cite>grid to real world</cite>" transform.
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public LargeTile(ImageReaderSpi provider, Object input, int imageIndex,
                     Rectangle region, AffineTransform gridToCRS) throws IllegalArgumentException
    {
        super(provider, input, imageIndex, region, gridToCRS);
        // (width, height) will be set indirectly through 'setSize' invocation.
        try {
            assert region == null || region.equals(getRegion());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Creates a tile for the given region with default subsampling.
     * See {@linkplain Tile#Tile(ImageReaderSpi,Object,int,Rectangle)
     * super-class constructor} for details.
     *
     * @param provider    The image reader provider to use, or {@code null}.
     * @param input       The input to be given to the image reader.
     * @param imageIndex  The image index to be given to the image reader for reading this tile.
     * @param region      The region in the destination image.
     * @throws IllegalArgumentException
     *          If a required argument is {@code null} or some argument has an invalid value.
     */
    public LargeTile(ImageReaderSpi provider, Object input, int imageIndex, Rectangle region)
                throws IllegalArgumentException
    {
        super(provider, input, imageIndex, region);
        // (width, height) will be set indirectly through 'setSize' invocation.
        try {
            assert region.equals(getRegion());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Sets the tile size to the given values.
     *
     * @param dx The tile width.
     * @param dy The tile height.
     * @throws IllegalArgumentException if the given size are negative.
     */
    @Override
    final void setSize(final int dx, final int dy) throws IllegalArgumentException {
        super.setSize(Math.min(dx, MASK), Math.min(dy, MASK));
        width  = dx;
        height = dy;
    }

    /**
     * Returns the upper-left corner in the destination image, with the image size.
     *
     * @return The region in the destination image.
     * @throws IOException if it was necessary to fetch the image dimension from the
     *         {@linkplain #getImageReader reader} and this operation failed.
     */
    @Override
    public Rectangle getRegion() throws IOException {
        final Rectangle region = super.getRegion();
        region.width  = width;
        region.height = height;
        return region;
    }
}
