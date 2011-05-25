package org.geotools.coverage.io.domain;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;

/**
 * A class describing the desired layout of an <code>OpImage</code>.
 *
 * <p> The <code>RasterLayout</code> class encapsulates three types of information about
 * an image:
 *
 * <ul>
 * <li> The image bounds, comprising the min X and Y coordinates,
 *      image width, and image height;
 * <li> The tile grid layout, comprising the tile grid X and Y offsets,
 *      the tile width, and the tile height; and
 * <li> The <code>SampleModel</code> and <code>ColorModel</code> of the image.
 * </ul>
 *
 * <p> Each of these parameters may be set individually, or left unset.
 * An unset parameter will cause the corresponding value of a given
 * <code>RenderedImage</code> to be used.  For example, the code:
 *
 * <pre>
 * RasterLayout layout;
 * RenderedImage im;
 *
 * int width = layout.getTileWidth(im);
 * </pre>
 *
 * will return the tile width of the <code>RasterLayout</code> if it is set,
 * or the tile width of the image <code>im</code> if it is not.
 *
 * <p> <code>RasterLayout</code> objects are primarily intended to be passed as part
 * of the <code>renderingHints</code> argument of the <code>create()</code> method of
 * <code>RenderedImageFactory</code>.  The <code>create()</code> method may remove parameter
 * settings that it cannot deal with, prior to passing the
 * <code>RasterLayout</code> to any <code>OpImage</code> constructors.  New <code>OpImage</code> subclasses
 * are not required to accept an <code>RasterLayout</code> parameter, but most will
 * at least need to synthesize one to be passed up the constructor
 * chain.
 *
 * <p> Methods that modify the state of an <code>RasterLayout</code> return a reference
 * to 'this' following the change.  This allows multiple modifications to
 * be made in a single expression.  This provides a way of modifying an
 * <code>RasterLayout</code> within a superclass constructor call.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/domain/RasterLayout.java $
 */
public class RasterLayout extends Object implements Cloneable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1606060744626516740L;
	
	/** A bitmask to specify the validity of <code>minX</code>. */
    public static final int MIN_X_MASK = 0x1;
    /** A bitmask to specify the validity of <code>minY</code>. */
    public static final int MIN_Y_MASK = 0x2;
    /** A bitmask to specify the validity of <code>width</code>. */
    public static final int WIDTH_MASK = 0x4;
    /** A bitmask to specify the validity of <code>height</code>. */
    public static final int HEIGHT_MASK = 0x8;

    /** A bitmask to specify the validity of <code>tileGridXOffset</code>. */
    public static final int TILE_GRID_X_OFFSET_MASK = 0x10;
    /** A bitmask to specify the validity of <code>tileGridYOffset</code>. */
    public static final int TILE_GRID_Y_OFFSET_MASK = 0x20;
    /** A bitmask to specify the validity of <code>tileWidth</code>. */
    public static final int TILE_WIDTH_MASK = 0x40;
    /** A bitmask to specify the validity of <code>tileHeight</code>. */
    public static final int TILE_HEIGHT_MASK = 0x80;

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

    /** The 'or'-ed together valid bitmasks. */
    protected int validMask = 0;

    /** Constructs an <code>RasterLayout</code> with no parameters set. */
    public RasterLayout() {}

    /**
     * Constructs an <code>RasterLayout</code> with all its parameters set.
     * The <code>sampleModel</code> and <code>colorModel</code> parameters are ignored if null.
     *
     * @param minX the image's minimum X coordinate.
     * @param minY the image's minimum Y coordinate.
     * @param width the image's width.
     * @param height the image's height.
     * @param tileGridXOffset the X coordinate of tile (0, 0).
     * @param tileGridYOffset the Y coordinate of tile (0, 0).
     * @param tileWidth the width of a tile.
     * @param tileHeight the height of a tile.
     * @param sampleModel the image's <code>SampleModel</code>.
     * @param colorModel the image's <code>ColorModel</code>.
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
     * Constructs an <code>RasterLayout</code> with only the image dimension
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
     * Constructs an <code>RasterLayout</code> with all its parameters set
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

    public RasterLayout(Rectangle bounds) {
		if(bounds==null)
			throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "bounds"));
		this.height=bounds.height;
		this.width=bounds.width;
		this.minX=bounds.x;
		this.minY=bounds.y;
		
	}

	/**
     * Returns the 'or'-ed together bitmask indicating parameter validity.
     * To determine the validity of a particular parameter, say tile width,
     * test <code>getValidMask() & RasterLayout.TILE_WIDTH_MASK</code>
     * against <code>0</code>.
     *
     * <p> To test a single mask value or set of mask values, the
     * convenience method isValid() may be used.
     *
     * @return an int that is the logical 'or' of the valid mask values,
     *         with a '1' bit representing the setting of a value.
     */
    public int getValidMask() {
        return validMask;
    }

    /**
     * Returns <code>true</code> if all the parameters specified by the argument are set.
     *
     * @param mask a bitmask.
     * @return a boolean truth value.
     */
    public final boolean isValid(int mask) {
        return (validMask & mask) == mask;
    }

    /**
     * Sets selected bits of the valid bitmask.  The valid bitmask is
     * set to the logical 'or' of its prior value and a new value.
     *
     * @param mask the new mask value to be 'or'-ed with the prior value.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setValid(int mask) {
        validMask |= mask;
        return this;
    }

    /**
     * Clears selected bits of the valid bitmask.  The valid bitmask
     * is set to the logical 'and' of its prior value and the negation
     * of the new mask value.  This effectively subtracts from the set of
     * valid parameters.
     *
     * @param mask the new mask value to be negated and 'and'-ed with
     *        the prior value.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout unsetValid(int mask) {
        validMask &= ~mask;
        return this;
    }

    /**
     * Marks the parameters dealing with the image bounds
     * (minX, minY, width, and height) as being invalid.
     *
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout unsetImageBounds() {
        unsetValid(MIN_X_MASK |
                   MIN_Y_MASK |
                   WIDTH_MASK |
                   HEIGHT_MASK);
        return this;
    }

    /**
     * Marks the parameters dealing with the tile layout (tileGridXOffset,
     * tileGridYOffset, tileWidth, and tileHeight) as being invalid.
     *
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout unsetTileLayout() {
        unsetValid(TILE_GRID_X_OFFSET_MASK |
                   TILE_GRID_Y_OFFSET_MASK |
                   TILE_WIDTH_MASK |
                   TILE_HEIGHT_MASK);
        return this;
    }

    /**
     * Returns the value of <code>minX</code> if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If <code>minX</code> is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of minX.
     */
    public int getMinX(RenderedImage fallback) {
        if (isValid(MIN_X_MASK)) {
            return minX;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getMinX();
            }
        }
    }

    /**
     * Sets <code>minX</code> to the supplied value and marks it as valid.
     *
     * @param minX the minimum X coordinate of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setMinX(int minX) {
        this.minX = minX;
        setValid(MIN_X_MASK);
        return this;
    }

    /**
     * Returns the value of <code>minY</code> if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If <code>minY</code> is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of minY.
     */
    public int getMinY(RenderedImage fallback) {
        if (isValid(MIN_Y_MASK)) {
            return minY;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getMinY();
            }
        }
    }

    /**
     * Sets <code>minY</code> to the supplied value and marks it as valid.
     *
     * @param minY the minimum Y coordinate of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setMinY(int minY) {
        this.minY = minY;
        setValid(MIN_Y_MASK);
        return this;
    }

    /**
     * Returns the value of <code>width</code> if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If <code>width</code> is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of width.
     */
    public int getWidth(RenderedImage fallback) {
        if (isValid(WIDTH_MASK)) {
            return width;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getWidth();
            }
        }
    }

    /**
     * Sets <code>width</code> to the supplied value and marks it as valid.
     *
     * @param width the width of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>width</code> is non-positive.
     */
   public RasterLayout setWidth(int width) {
       if(width <= 0) {
           throw new IllegalArgumentException("ImageLayout0");
       }
       this.width = width;
       setValid(WIDTH_MASK);
       return this;
    }

    /**
     * Returns the value of height if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If height is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of height.
     */
    public int getHeight(RenderedImage fallback) {
        if (isValid(HEIGHT_MASK)) {
            return height;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getHeight();
            }
        }
    }

    /**
     * Sets height to the supplied value and marks it as valid.
     *
     * @param height the height of the image, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>height</code> is non-positive.
     */
    public RasterLayout setHeight(int height) {
       if(height <= 0) {
           throw new IllegalArgumentException("ImageLayout0");
       }
       this.height = height;
       setValid(HEIGHT_MASK);
       return this;
    }

    /**
     * Returns the value of <code>tileGridXOffset</code> if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If <code>tileGridXOffset</code> is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of tileGridXOffset.
     */
    public int getTileGridXOffset(RenderedImage fallback) {
        if (isValid(TILE_GRID_X_OFFSET_MASK)) {
            return tileGridXOffset;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getTileGridXOffset();
            }
        }
    }

    /**
     * Sets <code>tileGridXOffset</code> to the supplied value and marks it as valid.
     *
     * @param tileGridXOffset the X coordinate of tile (0, 0), as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setTileGridXOffset(int tileGridXOffset) {
        this.tileGridXOffset = tileGridXOffset;
        setValid(TILE_GRID_X_OFFSET_MASK);
        return this;
    }

    /**
     * Returns the value of <code>tileGridYOffset</code> if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If <code>tileGridYOffset</code> is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of tileGridYOffset.
     */
    public int getTileGridYOffset(RenderedImage fallback) {
        if (isValid(TILE_GRID_Y_OFFSET_MASK)) {
            return tileGridYOffset;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getTileGridYOffset();
            }
        }
    }

    /**
     * Sets <code>tileGridYOffset</code> to the supplied value and marks it as valid.
     *
     * @param tileGridYOffset the Y coordinate of tile (0, 0), as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     */
    public RasterLayout setTileGridYOffset(int tileGridYOffset) {
        this.tileGridYOffset = tileGridYOffset;
        setValid(TILE_GRID_Y_OFFSET_MASK);
        return this;
    }

    /**
     * Returns the value of <code>tileWidth</code> if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If <code>tileWidth</code> is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of tileWidth.
     */
    public int getTileWidth(RenderedImage fallback) {
        if (isValid(TILE_WIDTH_MASK)) {
            return tileWidth;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getTileWidth();
            }
        }
    }

    /**
     * Sets <code>tileWidth</code> to the supplied value and marks it as valid.
     *
     * @param tileWidth the width of a tile, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>tileWidth</code> is
     *                                  non-positive.
     */
    public RasterLayout setTileWidth(int tileWidth) {
       if(tileWidth <= 0) {
           throw new IllegalArgumentException("ImageLayout0");
       }
       this.tileWidth = tileWidth;
       setValid(TILE_WIDTH_MASK);
       return this;
    }

    /**
     * Returns the value of tileHeight if it is valid, and
     * otherwise returns the value from the supplied <code>RenderedImage</code>.
     * If tileHeight is not valid and fallback is null, 0 is returned.
     *
     * @param fallback the <code>RenderedImage</code> fallback.
     * @return the appropriate value of tileHeight.
     */
    public int getTileHeight(RenderedImage fallback) {
        if (isValid(TILE_HEIGHT_MASK)) {
            return tileHeight;
        } else {
            if (fallback == null) {
                return 0;
            } else {
                return fallback.getTileHeight();
            }
        }
    }

    /**
     * Sets tileHeight to the supplied value and marks it as valid.
     *
     * @param tileHeight the height of a tile, as an int.
     * @return a reference to this <code>RasterLayout</code> following the change.
     * @throws IllegalArgumentException if <code>tileHeight</code> is
     *                                  non-positive.
     */
    public RasterLayout setTileHeight(int tileHeight) {
       if(tileHeight <= 0) {
           throw new IllegalArgumentException("ImageLayout0");
       }
       this.tileHeight = tileHeight;
       setValid(TILE_HEIGHT_MASK);
       return this;
    }




    /** Returns a String containing the values of all valid fields. */
    public String toString() {
        String s = "RasterLayout[";
        boolean first = true;

        if (isValid(MIN_X_MASK)) {
            s += "MIN_X=" + minX;
            first = false;
        }

        if (isValid(MIN_Y_MASK)) {
            if (!first) {
                s += ", ";
            }
            s += "MIN_Y=" + minY;
            first = false;
        }

        if (isValid(WIDTH_MASK)) {
            if (!first) {
                s += ", ";
            }
            s += "WIDTH=" + width;
            first = false;
        }

        if (isValid(HEIGHT_MASK)) {
            if (!first) {
                s += ", ";
            }
            s += "HEIGHT=" + height;
            first = false;
        }

        if (isValid(TILE_GRID_X_OFFSET_MASK)) {
            if (!first) {
                s += ", ";
            }
            s += "TILE_GRID_X_OFFSET=" + tileGridXOffset;
            first = false;
        }

        if (isValid(TILE_GRID_Y_OFFSET_MASK)) {
            if (!first) {
                s += ", ";
            }
            s += "TILE_GRID_Y_OFFSET=" + tileGridYOffset;
            first = false;
        }

        if (isValid(TILE_WIDTH_MASK)) {
            if (!first) {
                s += ", ";
            }
            s += "TILE_WIDTH=" + tileWidth;
            first = false;
        }

        if (isValid(TILE_HEIGHT_MASK)) {
            if (!first) {
                s += ", ";
            }
            s += "TILE_HEIGHT=" + tileHeight;
            first = false;
        }


        s += "]";
        return s;
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
     *
     * @since JAI 1.1
     */
    public boolean equals(Object obj) {

	if (this == obj)
	    return true;

	if (!(obj instanceof RasterLayout))
	    return false;

	RasterLayout il = (RasterLayout)obj;

	return (validMask       == il.validMask      ) &&
	       (width           == il.width          ) &&
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
     *
     * @since JAI 1.1
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

	code ^= validMask;

	return code;
    }
}
