package org.geotools.gce.geotiff;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A class describing the layout of a Raster element of the {@link HorizontalDomain}.
 */
public class RasterLayout extends Object implements Cloneable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

    /** Constructs a <code>RasterLayout</code> with no parameters set. */
    public RasterLayout() {}

    /**
     * Constructs a <code>RasterLayout</code> with all its parameters set.
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
    public RasterLayout(int minX,
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
     * Constructs a <code>RasterLayout</code> with only the image dimension
     * parameters set.
     *
     * @param minX the image's minimum X coordinate.
     * @param minY the image's minimum Y coordinate.
     * @param width the image's width.
     * @param height the image's height.
     */
    public RasterLayout(int minX,
                       int minY,
                       int width,
                       int height) {
        setMinX(minX);
        setMinY(minY);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Constructs a <code>RasterLayout</code> with all its parameters set
     * to equal those of a given <code>RenderedImage</code>.
     *
     * @param im a <code>RenderedImage</code> whose layout will be copied.
     */
    public RasterLayout(RenderedImage im) {
        this(im.getMinX(),
             im.getMinY(),
             im.getWidth(),
             im.getHeight(),
             im.getTileGridXOffset(),
             im.getTileGridYOffset(),
             im.getTileWidth(),
             im.getTileHeight());
    }

    /**
     * Returns the value of <code>minX</code>.
     * @return the value of minX.
     */
    public int getMinX() {
            return minX;
    }

    /**
     * Sets <code>minX</code> to the supplied value and marks it as valid.
     *
     * @param minX the minimum X coordinate of the image, as an int.
     */
    public void setMinX(int minX) {
        this.minX = minX;
    }

    /**
     * Returns the value of <code>minY</code>.
     * @return the value of minY.
     */
    public int getMinY() {
            return minY;
    }

    /**
     * Sets <code>minY</code> to the supplied value and marks it as valid.
     *
     * @param minY the minimum Y coordinate of the image, as an int.
     */
    public void setMinY(int minY) {
        this.minY = minY;
    }

    /**
     * Returns the value of <code>width</code>.
     * @return the value of width.
     */
    public int getWidth() {
    	return width;
    }

    /**
     * Sets <code>width</code> to the supplied value.
     *
     * @param width the width of the image, as an int.
     * @throws IllegalArgumentException if <code>width</code> is non-positive.
     */
   public void setWidth(int width) {
       if(width <= 0) {
           throw new IllegalArgumentException("width shall be positive");
       }
       this.width = width;
    }

    /**
     * Returns the value of height.
     *
     * @return the value of height.
     */
    public int getHeight() {
            return height;
    }

    /**
     * Sets height to the supplied value.
     *
     * @param height the height of the image, as an int.
     * @throws IllegalArgumentException if <code>height</code> is non-positive.
     */
    public void setHeight(int height) {
       if(height <= 0) {
           throw new IllegalArgumentException("height shall be positive");
       }
       this.height = height;
    }

    /**
     * Returns the value of <code>tileGridXOffset</code>.
     * @return the value of tileGridXOffset.
     */
    public int getTileGridXOffset() {
            return tileGridXOffset;
    }

    /**
     * Sets <code>tileGridXOffset</code> to the supplied value.
     *
     * @param tileGridXOffset the X coordinate of tile (0, 0), as an int.
     */
    public void setTileGridXOffset(int tileGridXOffset) {
        this.tileGridXOffset = tileGridXOffset;
    }

    /**
     * Returns the value of <code>tileGridYOffset</code>.
     * @return the value of tileGridYOffset.
     */
    public int getTileGridYOffset() {
            return tileGridYOffset;
    }

    /**
     * Sets <code>tileGridYOffset</code> to the supplied value.
     *
     * @param tileGridYOffset the Y coordinate of tile (0, 0), as an int.
     */
    public void setTileGridYOffset(int tileGridYOffset) {
        this.tileGridYOffset = tileGridYOffset;
    }

    /**
     * Returns the value of <code>tileWidth</code>.
     * @return the value of tileWidth.
     */
    public int getTileWidth() {
            return tileWidth;
    }

    /**
     * Sets <code>tileWidth</code> to the supplied value.
     *
     * @param tileWidth the width of a tile, as an int.
     * @throws IllegalArgumentException if <code>tileWidth</code> is
     *                                  non-positive.
     */
    public void setTileWidth(int tileWidth) {
       if(tileWidth <= 0) {
           throw new IllegalArgumentException("tile width shall be positive");
       }
       this.tileWidth = tileWidth;
    }

    /**
     * Returns the value of tileHeight.
     * @return the appropriate value of tileHeight.
     */
    public int getTileHeight() {
            return tileHeight;
    }

    /**
     * Sets tileHeight to the supplied value.
     *
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>tileHeight</code> is
     *                                  non-positive.
     */
    public void setTileHeight(int tileHeight) {
       if(tileHeight <= 0) {
           throw new IllegalArgumentException("tile height shall be positive");
       }
       this.tileHeight = tileHeight;
    }




    /** Returns a String containing the values of all valid fields. */
    public String toString() {
        StringBuilder sb = new StringBuilder("RasterLayout[").
            append("MIN_X=").append(minX).
            append(", ").
            append("MIN_Y=").append(minY).
            append(", ").
            append("WIDTH=").append(width).
            append(", ").
            append("HEIGHT=").append(height).
            append(", ").
            append("TILE_GRID_X_OFFSET=").append(tileGridXOffset).
            append(", ").
            append("TILE_GRID_Y_OFFSET=").append(tileGridYOffset).
            append(", ").
            append("TILE_WIDTH=").append(tileWidth).
            append(", ").
            append("TILE_HEIGHT=").append(tileHeight).
            append("]");
        return sb.toString();
    }

    /**
     * Returns a clone of the <code>RasterLayout</code> as an Object.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Serialize the <code>RasterLayout</code>.
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        // Write the non-static and non-transient fields.
        out.defaultWriteObject();

    }

    /**
     * Deserialize the <code>RasterLayout</code>.
     * @throws IOException
     */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        // Read the non-static and non-transient fields.
        in.defaultReadObject();

      
    }

    /**
     * Tests if the specified <code>Object</code> equals this
     * <code>RasterLayout</code>.
     *
     * @param obj the <code>Object</code> to test for equality
     *
     * @return <code>true</code> if the specified <code>Object</code>
     * is an instance of <code>RasterLayout</code> and equals this
     * <code>RasterLayout</code>; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {

	if (this == obj)
	    return true;

	if (!(obj instanceof RasterLayout))
	    return false;

	RasterLayout il = (RasterLayout)obj;

	return (width           == il.width          ) &&
	       (height          == il.height         ) &&
	       (minX            == il.minX           ) &&
	       (minY            == il.minY           ) &&
	       (tileHeight      == il.tileHeight     ) &&
	       (tileWidth       == il.tileWidth      ) &&
	       (tileGridXOffset == il.tileGridXOffset) &&
	       (tileGridYOffset == il.tileGridYOffset) ;
    }

    /**
     * Returns the hash code for this <code>RasterLayout</code>.
     *
     * @return a hash code for this <code>RasterLayout</code>.
     */
    public int hashCode() {

	int code = 0, i = 1;

	// This implementation is quite arbitrary.
	// hashCode's NEED not be uniqe for two "different" objects
	code += (width           * i++);
	code += (height          * i++);
	code += (minX            * i++);
	code += (minY            * i++);
	code += (tileHeight      * i++);
	code += (tileWidth       * i++);
	code += (tileGridXOffset * i++);
	code += (tileGridYOffset * i++);

	return code;
    }
}