/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.imageio;

import java.awt.Dimension;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;

/**
 * This class is the base class for building adapters/extensions to the ImageIO {@link ImageReadParam} class for
 * controlling the processing of reading a coverage from an {@link ImageReader}.
 *
 * @author Simone Giannecchini
 * @since 2.3.x
 */
public abstract class GeoToolsReadParams extends ImageReadParam {
    /** The {@link ImageReadParam} we are adapting/exteding. */
    protected ImageReadParam adaptee;

    /**
     * An array of preferred tile size range pairs. The default value is <code>null</code>, which indicates that there
     * are no preferred sizes. If the value is non-<code>null</code>, it must have an even length of at least two.
     *
     * <p>Subclasses that do not support reading tiles may ignore this value.
     *
     * @see #getPreferredTileSizes
     */
    protected Dimension[] preferredTileSizes = null;

    /**
     * The width of each tile if tiling has been set, or 0 otherwise.
     *
     * <p>Subclasses that do not support tiling may ignore this value.
     */
    protected int tileWidth = -1;

    /**
     * The height of each tile if tiling has been set, or 0 otherwise. The initial value is <code>0
     * </code>.
     *
     * <p>Subclasses that do not support tiling may ignore this value.
     */
    protected int tileHeight = -1;

    /**
     * A <code>boolean</code> that is <code>true</code> if tiling parameters have been specified.
     *
     * <p>Subclasses that do not support reading tiles may ignore this value.
     */
    protected boolean tilingSet = false;

    /**
     * Returns the width of each tile in an raster as it will be reader If tiling parameters have not been set, an
     * <code>IllegalStateException</code> is thrown.
     *
     * @return the tile width to be used for decoding.
     * @exception IllegalStateException if the tiling parameters have not been set.
     * @see #setTiling(int, int, int, int)
     * @see #getTileHeight()
     */
    public int getTileWidth() {

        if (!tilingSet) {
            throw new IllegalStateException("Tiling parameters not set!");
        }
        return tileWidth;
    }

    /**
     * Returns the width of each tile in an raster as it will be reader If tiling parameters have not been set, an
     * <code>IllegalStateException</code> is thrown.
     *
     * @return the tile height to be used for decoding.
     * @exception IllegalStateException if the tiling parameters have not been set.
     * @see #setTiling(int, int, int, int)
     * @see #getTileWidth()
     */
    public int getTileHeight() {

        if (!tilingSet) {
            throw new IllegalStateException("Tiling parameters not set!");
        }
        return tileHeight;
    }

    /**
     * Specifies that the image should be tiled. The <code>tileWidth</code> and <code>tileHeight
     * </code> parameters specify the width and height of the tiles in memory. If the tile width or height is greater
     * than the width or height of the image, the image is not tiled in that dimension.
     *
     * @param tileWidth the width of each tile.
     * @param tileHeight the height of each tile.
     * @exception IllegalArgumentException if the tile size is not within one of the allowable ranges returned by <code>
     *     getPreferredTileSizes</code>.
     * @exception IllegalArgumentException if <code>tileWidth</code> or <code>tileHeight</code> is less than or equal to
     *     0.
     * @see #canWriteTiles
     * @see #canOffsetTiles
     * @see #getTileWidth()
     * @see #getTileHeight()
     * @see #getTileGridXOffset()
     * @see #getTileGridYOffset()
     */
    public void setTiling(int tileWidth, int tileHeight) {

        if (tileWidth <= 0 || tileHeight <= 0) {
            throw new IllegalArgumentException("tile dimensions are non-positive!");
        }

        if (preferredTileSizes != null) {
            boolean ok = true;
            final int length = preferredTileSizes.length;
            for (int i = 0; i < length; i += 2) {
                Dimension min = preferredTileSizes[i];
                Dimension max = preferredTileSizes[i + 1];
                if (tileWidth < min.width
                        || tileWidth > max.width
                        || tileHeight < min.height
                        || tileHeight > max.height) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                throw new IllegalArgumentException("Illegal tile size!");
            }
        }

        this.tilingSet = true;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    /** Default contructor. */
    public GeoToolsReadParams(final ImageReadParam params) {
        this.adaptee = params;
    }

    // Return a deep copy of the array
    private static Dimension[] clonePreferredTileSizes(Dimension[] sizes) {
        if (sizes == null) {
            return null;
        }
        final int length = sizes.length;
        Dimension[] temp = new Dimension[length];
        for (int i = 0; i < length; i++) {
            temp[i] = new Dimension(sizes[i]);
        }
        return temp;
    }

    /**
     * Returns an array of <code>Dimension</code>s indicating the legal size ranges for tiles. The returned array is a
     * copy.
     *
     * <p>The information is returned as a set of pairs; the first element of a pair contains an (inclusive) minimum
     * width and height, and the second element contains an (inclusive) maximum width and height. Together, each pair
     * defines a valid range of sizes. To specify a fixed size, use the same width and height for both elements. To
     * specify an arbitrary range, a value of <code>null</code> is used in place of an actual array of <code>Dimension
     * </code>s.
     *
     * <p>If no array is specified on the constructor, but tiling is allowed, then this method returns <code>null</code>
     * .
     *
     * @exception UnsupportedOperationException if the plug-in does not support tiling.
     * @return an array of <code>Dimension</code>s with an even length of at least two, or <code>
     *     null</code>.
     */
    public Dimension[] getPreferredTileSizes() {
        return clonePreferredTileSizes(preferredTileSizes);
    }
}
