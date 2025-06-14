/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.text.MessageFormat;
import java.util.Objects;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * A class describing the desired layout of an <code>OpImage</code>.
 *
 * <p>The <code>RasterLayout</code> class encapsulates three types of information about an image:
 *
 * <ul>
 *   <li>The image bounds, comprising the min X and Y coordinates, image width, and image height;
 *   <li>The tile grid layout, comprising the tile grid X and Y offsets, the tile width, and the tile height; and
 * </ul>
 *
 * <p>Methods that modify the state of an <code>RasterLayout</code> return a reference to 'this' following the change.
 * This allows multiple modifications to be made in a single expression. This provides a way of modifying an <code>
 * RasterLayout</code> within a superclass constructor call.
 */
public class RasterLayout implements Cloneable {

    /** The image's minimum X coordinate. */
    int minX = 0;

    /** The image's minimum Y coordinate. */
    int minY = 0;

    /** The image's <code>width</code>. */
    int width = 0;

    /** The image's height. */
    int height = 0;

    /** The X coordinate of tile (0, 0). */
    int tileGridXOffset = 0;

    /** The Y coordinate of tile (0, 0). */
    int tileGridYOffset = 0;

    /** The width of a tile. */
    int tileWidth = 0;

    /** The height of a tile. */
    int tileHeight = 0;

    /** Constructs an <code>RasterLayout</code> with no parameters set. */
    public RasterLayout() {}

    /**
     * Constructs an <code>RasterLayout</code> with all its parameters set. The <code>sampleModel
     * </code> and <code>colorModel</code> parameters are ignored if null.
     *
     * @param minX the image's minimum X coordinate.
     * @param minY the image's minimum Y coordinate.
     * @param width the image's width.
     * @param height the image's height.
     * @param tileGridXOffset the X coordinate of tile (0, 0).
     * @param tileGridYOffset the Y coordinate of tile (0, 0).
     * @param tileWidth the width of a tile.
     * @param tileHeight the height of a tile.
     */
    public RasterLayout(
            int minX,
            int minY,
            int width,
            int height,
            int tileGridXOffset,
            int tileGridYOffset,
            int tileWidth,
            int tileHeight) {
        setMinX(minX);
        setMinY(minY);
        setWidth(width);
        setHeight(height);
        setTileGridXOffset(tileGridXOffset);
        setTileGridYOffset(tileGridYOffset);
        setTileWidth(tileWidth);
        setTileHeight(tileHeight);
    }

    /**
     * Constructs an <code>RasterLayout</code> with only the image dimension parameters set.
     *
     * @param minX the image's minimum X coordinate.
     * @param minY the image's minimum Y coordinate.
     * @param width the image's width.
     * @param height the image's height.
     */
    public RasterLayout(int minX, int minY, int width, int height) {
        setMinX(minX);
        setMinY(minY);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Constructs an <code>RasterLayout</code> with all its parameters set to equal those of a given <code>RenderedImage
     * </code>.
     *
     * @param im a <code>RenderedImage</code> whose layout will be copied.
     */
    public RasterLayout(RenderedImage im) {
        this(
                im.getMinX(),
                im.getMinY(),
                im.getWidth(),
                im.getHeight(),
                im.getTileGridXOffset(),
                im.getTileGridYOffset(),
                im.getTileWidth(),
                im.getTileHeight());
    }

    public RasterLayout(Rectangle bounds) {
        if (bounds == null) throw new NullPointerException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "bounds"));
        this.height = bounds.height;
        this.width = bounds.width;
        this.minX = bounds.x;
        this.minY = bounds.y;
    }

    public Rectangle toRectangle() {
        return new Rectangle(minX, minY, width, height);
    }

    /**
     * Returns the value of <code>minX</code> if it is valid, and otherwise returns the value from the supplied <code>
     * RenderedImage</code>. If <code>minX</code> is not valid and fallback is null, 0 is returned.
     *
     * @return the appropriate value of minX.
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Sets <code>minX</code> to the supplied value and marks it as valid.
     *
     * @param minX the minimum X coordinate of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setMinX(int minX) {
        this.minX = minX;
        return this;
    }

    /**
     * Returns the value of <code>minY</code> if it is valid, and otherwise returns the value from the supplied <code>
     * RenderedImage</code>. If <code>minY</code> is not valid and fallback is null, 0 is returned.
     *
     * @return the appropriate value of minY.
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Sets <code>minY</code> to the supplied value and marks it as valid.
     *
     * @param minY the minimum Y coordinate of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setMinY(int minY) {
        this.minY = minY;
        return this;
    }

    /**
     * Returns the value of <code>width</code> if it is valid, and otherwise returns the value from the supplied <code>
     * RenderedImage</code>. If <code>width</code> is not valid and fallback is null, 0 is returned.
     *
     * @return the appropriate value of width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets <code>width</code> to the supplied value and marks it as valid.
     *
     * @param width the width of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>width</code> is non-positive.
     */
    public RasterLayout setWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("ImageLayout0");
        }
        this.width = width;
        return this;
    }

    /**
     * Returns the value of height if it is valid, and otherwise returns the value from the supplied <code>RenderedImage
     * </code>. If height is not valid and fallback is null, 0 is returned.
     *
     * @return the appropriate value of height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height to the supplied value and marks it as valid.
     *
     * @param height the height of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>height</code> is non-positive.
     */
    public RasterLayout setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("ImageLayout0");
        }
        this.height = height;
        return this;
    }

    /**
     * Returns the value of <code>tileGridXOffset</code> if it is valid, and otherwise returns the value from the
     * supplied <code>RenderedImage</code>. If <code>tileGridXOffset</code> is not valid and fallback is null, 0 is
     * returned.
     *
     * @return the appropriate value of tileGridXOffset.
     */
    public int getTileGridXOffset() {
        return tileGridXOffset;
    }

    /**
     * Sets <code>tileGridXOffset</code> to the supplied value and marks it as valid.
     *
     * @param tileGridXOffset the X coordinate of tile (0, 0), as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setTileGridXOffset(int tileGridXOffset) {
        this.tileGridXOffset = tileGridXOffset;
        return this;
    }

    /**
     * Returns the value of <code>tileGridYOffset</code> if it is valid, and otherwise returns the value from the
     * supplied <code>RenderedImage</code>. If <code>tileGridYOffset</code> is not valid and fallback is null, 0 is
     * returned.
     *
     * @return the appropriate value of tileGridYOffset.
     */
    public int getTileGridYOffset() {
        return tileGridYOffset;
    }

    /**
     * Sets <code>tileGridYOffset</code> to the supplied value and marks it as valid.
     *
     * @param tileGridYOffset the Y coordinate of tile (0, 0), as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setTileGridYOffset(int tileGridYOffset) {
        this.tileGridYOffset = tileGridYOffset;
        return this;
    }

    /**
     * Returns the value of <code>tileWidth</code> if it is valid, and otherwise returns the value from the supplied
     * <code>RenderedImage</code>. If <code>tileWidth</code> is not valid and fallback is null, 0 is returned.
     *
     * @return the appropriate value of tileWidth.
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * Sets <code>tileWidth</code> to the supplied value and marks it as valid.
     *
     * @param tileWidth the width of a tile, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>tileWidth</code> is non-positive.
     */
    public RasterLayout setTileWidth(int tileWidth) {
        if (tileWidth <= 0) {
            throw new IllegalArgumentException("ImageLayout0");
        }
        this.tileWidth = tileWidth;
        return this;
    }

    /**
     * Returns the value of tileHeight if it is valid, and otherwise returns the value from the supplied <code>
     * RenderedImage</code>. If tileHeight is not valid and fallback is null, 0 is returned.
     *
     * @return the appropriate value of tileHeight.
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * Sets tileHeight to the supplied value and marks it as valid.
     *
     * @param tileHeight the height of a tile, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>tileHeight</code> is non-positive.
     */
    public RasterLayout setTileHeight(int tileHeight) {
        if (tileHeight <= 0) {
            throw new IllegalArgumentException("ImageLayout0");
        }
        this.tileHeight = tileHeight;
        return this;
    }

    /** Returns a String containing the values of all valid fields. */
    @Override
    public String toString() {
        String s = "RasterLayout[";

        s += "MIN_X=" + minX;

        s += ", ";
        s += "MIN_Y=" + minY;

        s += ", ";
        s += "WIDTH=" + width;

        s += ", ";
        s += "HEIGHT=" + height;

        s += ", ";
        s += "TILE_GRID_X_OFFSET=" + tileGridXOffset;

        s += ", ";
        s += "TILE_GRID_Y_OFFSET=" + tileGridYOffset;

        s += ", ";
        s += "TILE_WIDTH=" + tileWidth;

        s += ", ";
        s += "TILE_HEIGHT=" + tileHeight;

        s += "]";
        return s;
    }

    /** Returns a clone of the <code>RasterLayout</code> as an Object. */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Tests if the specified <code>Object</code> equals this <code>RasterLayout</code>.
     *
     * @param obj the <code>Object</code> to test for equality
     * @return <code>true</code> if the specified <code>Object</code> is an instance of <code>
     *     RasterLayout</code> and equals this <code>RasterLayout</code>; <code>false</code> otherwise.
     * @since JAI 1.1
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof RasterLayout)) return false;

        RasterLayout il = (RasterLayout) obj;

        return width == il.width
                && height == il.height
                && minX == il.minX
                && minY == il.minY
                && tileHeight == il.tileHeight
                && tileWidth == il.tileWidth
                && tileGridXOffset == il.tileGridXOffset
                && tileGridYOffset == il.tileGridYOffset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, width, height, tileGridXOffset, tileGridYOffset, tileWidth, tileHeight);
    }
}
