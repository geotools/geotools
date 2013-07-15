/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.BorderExtender;
import javax.media.jai.BorderExtenderCopy;
import javax.media.jai.BorderExtenderReflect;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.extent.GeographicBoundingBox;

import com.sun.media.jai.operator.ImageReadDescriptor;
import com.sun.media.jai.util.Rational;


/**
 * A set of static methods working on images. Some of those methods are useful, but not
 * really rigorous. This is why they do not appear in any "official" package, but instead
 * in this private one.
 *
 *                      <strong>Do not rely on this API!</strong>
 *
 * It may change in incompatible way in any future version.
 *
 * @since 2.0
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions
 */
public final class ImageUtilities {
	

	 /**
     * {@code true} if JAI media lib is available.
     */
    private static final boolean mediaLibAvailable;
    static {

    	// do we wrappers at hand?
        boolean mediaLib = false;
        Class<?> mediaLibImage = null;
        try {
            mediaLibImage = Class.forName("com.sun.medialib.mlib.Image");
        } catch (ClassNotFoundException e) {
        }
        mediaLib = (mediaLibImage != null);
        
        
        // npw check if we either wanted to disable explicitly and if we installed the native libs
        if(mediaLib){
        
	        try {
	        	// explicit disable
	            mediaLib =
	                !Boolean.getBoolean("com.sun.media.jai.disableMediaLib");
	            
	            //native libs installed
		        if(mediaLib)
		        {
		        	final Class<?> mImage=mediaLibImage;
	                mediaLib=AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
	                     public Boolean run() {
	                    	 try {
	                    		//get the method
	                    		final Class<?> params[] = {};
								Method method= mImage.getDeclaredMethod("isAvailable", params);

								//invoke
	                    		final Object paramsObj[] = {};

	        		        	final Object o=mImage.newInstance();
		                        return (Boolean) method.invoke(o, paramsObj);
							} catch (Throwable e) {
								return false;
							}
	                     }
	                });
		        }	            
	        } catch (Throwable e) {
	            // Because the property com.sun.media.jai.disableMediaLib isn't
	            // defined as public, the users shouldn't know it.  In most of
	            // the cases, it isn't defined, and thus no access permission
	            // is granted to it in the policy file.  When JAI is utilized in
	            // a security environment, AccessControlException will be thrown.
	            // In this case, we suppose that the users would like to use
	            // medialib accelaration.  So, the medialib won't be disabled.
	
	            // The fix of 4531501
	        	
	        	mediaLib=false;
	        }
	        

        }


        mediaLibAvailable=mediaLib;
    }
    
    /**
     * {@link RenderingHints} used to prevent {@link JAI} operations from expanding
     * {@link IndexColorModel}s.
     */
    public final static RenderingHints DONT_REPLACE_INDEX_COLOR_MODEL = new RenderingHints(
                    JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE);

    /**
     * {@link RenderingHints} used to force {@link JAI} operations to expand
     * {@link IndexColorModel}s.
     */
    public final static RenderingHints REPLACE_INDEX_COLOR_MODEL = new RenderingHints(
                    JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.TRUE);

    /**
     * {@link RenderingHints} for requesting Nearest Neighbor intepolation.
     */
    public static final RenderingHints NN_INTERPOLATION_HINT = new RenderingHints(
                    JAI.KEY_INTERPOLATION, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

    /**
     * {@link RenderingHints} for avoiding caching of {@link JAI} {@link RenderedOp}s.
     */
    public static final RenderingHints NOCACHE_HINT = new RenderingHints(
                    JAI.KEY_TILE_CACHE, null);

    /**
     * Cached instance of a {@link RenderingHints} for controlling border extension on
     * {@link JAI} operations. It contains an instance of a {@link BorderExtenderCopy}.
     */
    public final static RenderingHints EXTEND_BORDER_BY_COPYING = new RenderingHints(
                    JAI.KEY_BORDER_EXTENDER, BorderExtender
                    .createInstance(BorderExtender.BORDER_COPY));

    /**
     * Cached instance of a {@link RenderingHints} for controlling border extension on
     * {@link JAI} operations. It contains an instance of a {@link BorderExtenderReflect}.
     */
    public final static RenderingHints EXTEND_BORDER_BY_REFLECT = new RenderingHints(
                    JAI.KEY_BORDER_EXTENDER, BorderExtender
                    .createInstance(BorderExtender.BORDER_REFLECT));

    /**
     * The default tile size. This default tile size can be
     * overridden with a call to {@link JAI#setDefaultTileSize}.
     */
    private static final Dimension GEOTOOLS_DEFAULT_TILE_SIZE = new Dimension(512,512);

    /**
     * The minimum tile size.
     */
    private static final int GEOTOOLS_MIN_TILE_SIZE = 256;

    /**
     * Maximum tile width or height before to consider a tile as a stripe. It tile width or height
     * are smaller or equals than this size, then the image will be retiled. That is done because
     * there are many formats that use stripes as an alternative to tiles, an example is tiff. A
     * stripe can be a performance black hole, users can have stripes as large as 20000 columns x 8
     * rows. If we just want to see a chunk of 512x512, this is a lot of uneeded data to load.
     */
    private static final int STRIPE_SIZE = 64;

    /**
     * List of valid names. Note: the "Optimal" type is not
     * implemented because currently not provided by JAI.
     */
    public static final String[] INTERPOLATION_NAMES = {
        "Nearest",          // JAI name
        "NearestNeighbor",  // OpenGIS name
        "Bilinear",
        "Bicubic",
        "Bicubic2"          // Not in OpenGIS specification.
    };

    /**
     * Interpolation types (provided by Java Advanced Imaging) for {@link #INTERPOLATION_NAMES}.
     */
    public static final int[] INTERPOLATION_TYPES= {
        Interpolation.INTERP_NEAREST,
        Interpolation.INTERP_NEAREST,
        Interpolation.INTERP_BILINEAR,
        Interpolation.INTERP_BICUBIC,
        Interpolation.INTERP_BICUBIC_2
    };

    public final static BorderExtender DEFAULT_BORDER_EXTENDER = BorderExtender.createInstance(BorderExtender.BORDER_COPY);

    public final static RenderingHints BORDER_EXTENDER_HINTS = new RenderingHints(JAI.KEY_BORDER_EXTENDER, DEFAULT_BORDER_EXTENDER);

    public static final String DIRECT_KAKADU_PLUGIN = "it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReader";

    // FORMULAE FOR FORWARD MAP are derived as follows
    //     Nearest
    //        Minimum:
    //            srcMin = floor ((dstMin + 0.5 - trans) / scale)
    //            srcMin <= (dstMin + 0.5 - trans) / scale < srcMin + 1
    //            srcMin*scale <= dstMin + 0.5 - trans < (srcMin + 1)*scale
    //            srcMin*scale - 0.5 + trans
    //                       <= dstMin < (srcMin + 1)*scale - 0.5 + trans
    //            Let A = srcMin*scale - 0.5 + trans,
    //            Let B = (srcMin + 1)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            Note that srcMax is defined to be srcMin + dimension - 1
    //            srcMax = floor ((dstMax + 0.5 - trans) / scale)
    //            srcMax <= (dstMax + 0.5 - trans) / scale < srcMax + 1
    //            srcMax*scale <= dstMax + 0.5 - trans < (srcMax + 1)*scale
    //            srcMax*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1) * scale - 0.5 + trans
    //            Let float A = (srcMax + 1) * scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    //     Other interpolations
    //
    //        First the source should be shrunk by the padding that is
    //        required for the particular interpolation. Then the
    //        shrunk source should be forward mapped as follows:
    //
    //        Minimum:
    //            srcMin = floor (((dstMin + 0.5 - trans)/scale) - 0.5)
    //            srcMin <= ((dstMin + 0.5 - trans)/scale) - 0.5 < srcMin+1
    //            (srcMin+0.5)*scale <= dstMin+0.5-trans <
    //                                                  (srcMin+1.5)*scale
    //            (srcMin+0.5)*scale - 0.5 + trans
    //                       <= dstMin < (srcMin+1.5)*scale - 0.5 + trans
    //            Let A = (srcMin+0.5)*scale - 0.5 + trans,
    //            Let B = (srcMin+1.5)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            srcMax is defined as srcMin + dimension - 1
    //            srcMax = floor (((dstMax + 0.5 - trans) / scale) - 0.5)
    //            srcMax <= ((dstMax + 0.5 - trans)/scale) - 0.5 < srcMax+1
    //            (srcMax+0.5)*scale <= dstMax + 0.5 - trans <
    //                                                   (srcMax+1.5)*scale
    //            (srcMax+0.5)*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1.5)*scale - 0.5 + trans
    //            Let float A = (srcMax+1.5)*scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    public static final float RATIONAL_TOLERANCE = 0.000001F;

    /**
     * Do not allow creation of instances of this class.
     */
    private ImageUtilities() {
    }

    /**
     * Suggests an {@link ImageLayout} for the specified image. All parameters are initially set
     * equal to those of the given {@link RenderedImage}, and then the {@linkplain #toTileSize
     * tile size is updated according the image size}. This method never returns {@code null}.
     */
    public static ImageLayout getImageLayout(final RenderedImage image) {
        return getImageLayout(image, true);
    }

    /**
     * Returns an {@link ImageLayout} for the specified image. If {@code initToImage} is
     * {@code true}, then all parameters are initially set equal to those of the given
     * {@link RenderedImage} and the returned layout is never {@code null} (except if
     * the image is null).
     */
    private static ImageLayout getImageLayout(final RenderedImage image, final boolean initToImage) {
        if (image == null) {
            return null;
        }
        ImageLayout layout = initToImage ? new ImageLayout(image) : null;
        if ((image.getNumXTiles()==1 || image.getTileWidth () <= STRIPE_SIZE) &&
            (image.getNumYTiles()==1 || image.getTileHeight() <= STRIPE_SIZE))
        {
            // If the image was already tiled, reuse the same tile size.
            // Otherwise, compute default tile size.  If a default tile
            // size can't be computed, it will be left unset.
            if (layout != null) {
                layout = layout.unsetTileLayout();
            }
            Dimension defaultSize = JAI.getDefaultTileSize();
            if (defaultSize == null) {
                defaultSize = GEOTOOLS_DEFAULT_TILE_SIZE;
            }
            int s;
            if ((s=toTileSize(image.getWidth(), defaultSize.width)) != 0) {
                if (layout == null) {
                    layout = new ImageLayout();
                }
                layout = layout.setTileWidth(s);
                layout.setTileGridXOffset(image.getMinX());
            }
            if ((s=toTileSize(image.getHeight(), defaultSize.height)) != 0) {
                if (layout == null) {
                    layout = new ImageLayout();
                }
                layout = layout.setTileHeight(s);
                layout.setTileGridYOffset(image.getMinY());
            }
        }
        return layout;
    }

    /**
     * Suggests a set of {@link RenderingHints} for the specified image.
     * The rendering hints may include the following parameters:
     *
     * <ul>
     *   <li>{@link JAI#KEY_IMAGE_LAYOUT} with a proposed tile size.</li>
     * </ul>
     *
     * This method may returns {@code null} if no rendering hints is proposed.
     */
    public static RenderingHints getRenderingHints(final RenderedImage image) {
        final ImageLayout layout = getImageLayout(image, false);
        return (layout != null) ? new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout) : null;
    }

    /**
     * Suggests a tile size for the specified image size. On input, {@code size} is the image's
     * size. On output, it is the tile size. This method write the result directly in the supplied
     * object and returns {@code size} for convenience.
     * <p>
     * This method it aimed to computing a tile size such that the tile grid would have overlapped
     * the image bound in order to avoid having tiles crossing the image bounds and being therefore
     * partially empty. This method will never returns a tile size smaller than
     * {@value ImageUtilities#GEOTOOLS_MIN_TILE_SIZE}. If this method can't suggest a size,
     * then it left the corresponding {@code size} field ({@link Dimension#width width} or
     * {@link Dimension#height height}) unchanged.
     * <p>
     * The {@link Dimension#width width} and {@link Dimension#height height} fields are processed
     * independently in the same way. The following discussion use the {@code width} field as an
     * example.
     * <p>
     * This method inspects different tile sizes close to the {@linkplain JAI#getDefaultTileSize()
     * default tile size}. Lets {@code width} be the default tile width. Values are tried in the
     * following order: {@code width}, {@code width+1}, {@code width-1}, {@code width+2},
     * {@code width-2}, {@code width+3}, {@code width-3}, <cite>etc.</cite> until one of the
     * following happen:
     * <p>
     * <ul>
     *   <li>A suitable tile size is found. More specifically, a size is found which is a dividor
     *       of the specified image size, and is the closest one of the default tile size. The
     *       {@link Dimension} field ({@code width} or {@code height}) is set to this value.</li>
     *
     *   <li>An arbitrary limit (both a minimum and a maximum tile size) is reached. In this case,
     *       this method <strong>may</strong> set the {@link Dimension} field to a value that
     *       maximize the remainder of <var>image size</var> / <var>tile size</var> (in other
     *       words, the size that left as few empty pixels as possible).</li>
     * </ul>
     */
    public static Dimension toTileSize(final Dimension size) {
        Dimension defaultSize = JAI.getDefaultTileSize();
        if (defaultSize == null) {
            defaultSize = GEOTOOLS_DEFAULT_TILE_SIZE;
        }
        int s;
        if ((s=toTileSize(size.width,  defaultSize.width )) != 0) size.width  = s;
        if ((s=toTileSize(size.height, defaultSize.height)) != 0) size.height = s;
        return size;
    }

    /**
     * Suggests a tile size close to {@code tileSize} for the specified {@code imageSize}.
     * This method it aimed to computing a tile size such that the tile grid would have
     * overlapped the image bound in order to avoid having tiles crossing the image bounds
     * and being therefore partially empty. This method will never returns a tile size smaller
     * than {@value #GEOTOOLS_MIN_TILE_SIZE}. If this method can't suggest a size, then it returns 0.
     *
     * @param imageSize The image size.
     * @param tileSize  The preferred tile size, which is often {@value #GEOTOOLS_DEFAULT_TILE_SIZE}.
     */
    private static int toTileSize(final int imageSize, final int tileSize) {
        final int MAX_TILE_SIZE = Math.min(tileSize*2, imageSize);
        final int stop = Math.max(tileSize-GEOTOOLS_MIN_TILE_SIZE, MAX_TILE_SIZE-tileSize);
        int sopt = 0;  // An "optimal" tile size, to be used if no exact dividor is found.
        int rmax = 0;  // The remainder of 'imageSize / sopt'. We will try to maximize this value.
        /*
         * Inspects all tile sizes in the range [GEOTOOLS_MIN_TILE_SIZE .. MAX_TIME_SIZE]. We will begin
         * with a tile size equals to the specified 'tileSize'. Next we will try tile sizes of
         * 'tileSize+1', 'tileSize-1', 'tileSize+2', 'tileSize-2', 'tileSize+3', 'tileSize-3',
         * etc. until a tile size if found suitable.
         *
         * More generally, the loop below tests the 'tileSize+i' and 'tileSize-i' values. The
         * 'stop' constant was computed assuming that MIN_TIME_SIZE < tileSize < MAX_TILE_SIZE.
         * If a tile size is found which is a dividor of the image size, than that tile size (the
         * closest one to 'tileSize') is returned. Otherwise, the loop continue until all values
         * in the range [GEOTOOLS_MIN_TILE_SIZE .. MAX_TIME_SIZE] were tested. In this process, we remind
         * the tile size that gave the greatest reminder (rmax). In other words, this is the tile
         * size with the smallest amount of empty pixels.
         */
        for (int i=0; i<=stop; i++) {
            int s;
            if ((s = tileSize+i) <= MAX_TILE_SIZE) {
                final int r = imageSize % s;
                if (r == 0) {
                    // Found a size >= to 'tileSize' which is a dividor of image size.
                    return s;
                }
                if (r > rmax) {
                    rmax = r;
                    sopt = s;
                }
            }
            if ((s = tileSize-i) >= GEOTOOLS_MIN_TILE_SIZE) {
                final int r = imageSize % s;
                if (r == 0) {
                    // Found a size <= to 'tileSize' which is a dividor of image size.
                    return s;
                }
                if (r > rmax) {
                    rmax = r;
                    sopt = s;
                }
            }
        }
        /*
         * No dividor were found in the range [GEOTOOLS_MIN_TILE_SIZE .. MAX_TIME_SIZE]. At this point
         * 'sopt' is an "optimal" tile size (the one that left as few empty pixel as possible),
         * and 'rmax' is the amount of non-empty pixels using this tile size. We will use this
         * "optimal" tile size only if it fill at least 75% of the tile. Otherwise, we arbitrarily
         * consider that it doesn't worth to use a "non-standard" tile size. The purpose of this
         * arbitrary test is again to avoid too many small tiles (assuming that
         */
        return (rmax >= tileSize - tileSize/4) ? sopt : 0;
    }

    /**
     * Computes a new {@link ImageLayout} which is the intersection of the specified
     * {@code ImageLayout} and all {@code RenderedImage}s in the supplied list. If the
     * {@link ImageLayout#getMinX minX}, {@link ImageLayout#getMinY minY},
     * {@link ImageLayout#getWidth width} and {@link ImageLayout#getHeight height}
     * properties are not defined in the {@code layout}, then they will be inherited
     * from the <strong>first</strong> source for consistency with {@link OpImage} constructor.
     *
     * @param  layout The original layout. This object will not be modified.
     * @param  sources The list of sources {@link RenderedImage}.
     * @return A new {@code ImageLayout}, or the original {@code layout} if no change was needed.
     */
    public static ImageLayout createIntersection(final ImageLayout layout, final List<RenderedImage> sources) {
        ImageLayout result = layout;
        if (result == null) {
            result = new ImageLayout();
        }
        final int n = sources.size();
        if (n != 0) {
            // If layout is not set, OpImage uses the layout of the *first*
            // source image according OpImage constructor javadoc.
            RenderedImage source = (RenderedImage) sources.get(0);
            int minXL = result.getMinX  (source);
            int minYL = result.getMinY  (source);
            int maxXL = result.getWidth (source) + minXL;
            int maxYL = result.getHeight(source) + minYL;
            for (int i=0; i<n; i++) {
                source = (RenderedImage) sources.get(i);
                final int minX = source.getMinX  ();
                final int minY = source.getMinY  ();
                final int maxX = source.getWidth () + minX;
                final int maxY = source.getHeight() + minY;
                int mask = 0;
                if (minXL < minX) mask |= (1|4); // set minX and width
                if (minYL < minY) mask |= (2|8); // set minY and height
                if (maxXL > maxX) mask |= (4);   // Set width
                if (maxYL > maxY) mask |= (8);   // Set height
                if (mask != 0) {
                    if (layout == result) {
                        result = (ImageLayout) layout.clone();
                    }
                    if ((mask & 1) != 0) result.setMinX   (minXL=minX);
                    if ((mask & 2) != 0) result.setMinY   (minYL=minY);
                    if ((mask & 4) != 0) result.setWidth ((maxXL=maxX) - minXL);
                    if ((mask & 8) != 0) result.setHeight((maxYL=maxY) - minYL);
                }
            }
            // If the bounds changed, adjust the tile size.
            if (result != layout) {
                source = (RenderedImage) sources.get(0);
                if (result.isValid(ImageLayout.TILE_WIDTH_MASK)) {
                    final int oldSize = result.getTileWidth(source);
                    final int newSize = toTileSize(result.getWidth(source), oldSize);
                    if (oldSize != newSize) {
                        result.setTileWidth(newSize);
                    }
                }
                if (result.isValid(ImageLayout.TILE_HEIGHT_MASK)) {
                    final int oldSize = result.getTileHeight(source);
                    final int newSize = toTileSize(result.getHeight(source), oldSize);
                    if (oldSize != newSize) {
                        result.setTileHeight(newSize);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Casts the specified object to an {@link Interpolation object}.
     *
     * @param  type The interpolation type as an {@link Interpolation} or a {@link CharSequence}
     *         object.
     * @return The interpolation object for the specified type.
     * @throws IllegalArgumentException if the specified interpolation type is not a know one.
     */
    public static Interpolation toInterpolation(final Object type) throws IllegalArgumentException {
        if (type instanceof Interpolation) {
            return (Interpolation) type;
        } else if (type instanceof CharSequence) {
            final String name = type.toString();
            final int length= INTERPOLATION_NAMES.length;
            for (int i=0; i<length; i++) {
                if (INTERPOLATION_NAMES[i].equalsIgnoreCase(name)) {
                    return Interpolation.getInstance(INTERPOLATION_TYPES[i]);
                }
            }
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.UNKNOW_INTERPOLATION_$1, type));
    }

    /**
     * Returns the interpolation name for the specified interpolation object.
     * This method tries to infer the name from the object's class name.
     *
     * @param Interpolation The interpolation object, or {@code null} for "nearest"
     *        (which is an other way to say "no interpolation").
     */
    public static String getInterpolationName(Interpolation interp) {
        if (interp == null) {
            interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        }
        final String prefix = "Interpolation";
        for (Class<?> classe = interp.getClass(); classe!=null; classe=classe.getSuperclass()) {
            String name = Classes.getShortName(classe);
            int index = name.lastIndexOf(prefix);
            if (index >= 0) {
                return name.substring(index + prefix.length());
            }
        }
        return Classes.getShortClassName(interp);
    }

    /**
     * Tiles the specified image.
     *
     * @todo Usually, the tiling doesn't need to be performed as a separated operation. The
     * {@link ImageLayout} hint with tile information can be provided to most JAI operators.
     * The {@link #getRenderingHints} method provides such tiling information only if the
     * image was not already tiled, so it should not be a cause of tile size mismatch in an
     * operation chain. The mean usage for a separated "tile" operation is to tile an image
     * before to save it on disk in some format supporting tiling.
     *
     * @throws IOException If an I/O operation were required (in order to check if the image
     *         were tiled on disk) and failed.
     *
     * @since 2.3
     */
    public static RenderedOp tileImage(final RenderedOp image) throws IOException {
        // /////////////////////////////////////////////////////////////////////
        //
        // initialization
        //
        // /////////////////////////////////////////////////////////////////////
        final int width      = image.getWidth();
        final int height     = image.getHeight();
        final int tileHeight = image.getTileHeight();
        final int tileWidth  = image.getTileWidth();

        boolean needToTile = false;

        // /////////////////////////////////////////////////////////////////////
        //
        // checking if the image comes directly from an image read operation
        //
        // /////////////////////////////////////////////////////////////////////
        // getting the reader
        final Object o = image.getProperty(ImageReadDescriptor.PROPERTY_NAME_IMAGE_READER);
        if (o instanceof ImageReader) {
            final ImageReader reader = (ImageReader) o;
            if (!reader.isImageTiled(0)) {
                needToTile = true;
            }
        }
        // /////////////////////////////////////////////////////////////////////
        //
        // If the original image has tileW==W &&tileH==H it is untiled.
        //
        // /////////////////////////////////////////////////////////////////////
        if (!needToTile && tileWidth == width && tileHeight <= height) {
            needToTile = true;
        }
        // /////////////////////////////////////////////////////////////////////
        //
        // tiling central.
        //
        // /////////////////////////////////////////////////////////////////////
        if (needToTile) {

            // tiling the original image by providing a suitable layout
            final ImageLayout layout = getImageLayout(image);
            layout.unsetValid(ImageLayout.COLOR_MODEL_MASK | ImageLayout.SAMPLE_MODEL_MASK);

            // changing parameters related to the tiling
            final RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

            // reading the image
            final ParameterBlockJAI pbjFormat = new ParameterBlockJAI("Format");
            pbjFormat.addSource(image);
            pbjFormat.setParameter("dataType", image.getSampleModel().getDataType());

            return JAI.create("Format", pbjFormat, hints);
        }
        return image;
    }

    /**
     * Sets every samples in the given image to the given value. This method is typically used
     * for clearing an image content.
     *
     * @param image The image to fill.
     * @param value The value to to given to every samples.
     */
    public static void fill(final WritableRenderedImage image, final Number value) {
        int y = image.getMinTileY();
        for (int ny = image.getNumYTiles(); --ny >= 0;) {
            int x = image.getMinTileX();
            for (int nx = image.getNumXTiles(); --nx >= 0;) {
                final WritableRaster raster = image.getWritableTile(x, y);
                try {
                    fill(raster.getDataBuffer(), value);
                } finally {
                    image.releaseWritableTile(x, y);
                }
            }
        }
    }

    /**
     * Sets the content of all banks in the given data buffer to the specified value. We do not
     * allow setting of different value for invidivual bank because the data buffer "banks" do
     * not necessarly match the image "bands".
     *
     * @param buffer The data buffer to fill.
     * @param value  The values to be given to every elements in the data buffer.
     */
    private static void fill(final DataBuffer buffer, final Number value) {
        final int[] offsets = buffer.getOffsets();
        final int size = buffer.getSize();
        if (buffer instanceof DataBufferByte) {
            final DataBufferByte data = (DataBufferByte) buffer;
            final byte n = value.byteValue();
            for (int i=0; i<offsets.length; i++) {
                final int offset = offsets[i];
                Arrays.fill(data.getData(i), offset, offset + size, n);
            }
        } else if (buffer instanceof DataBufferShort) {
            final DataBufferShort data = (DataBufferShort) buffer;
            final short n = value.shortValue();
            for (int i=0; i<offsets.length; i++) {
                final int offset = offsets[i];
                Arrays.fill(data.getData(i), offset, offset + size, n);
            }
        } else if (buffer instanceof DataBufferUShort) {
            final DataBufferUShort data = (DataBufferUShort) buffer;
            final short n = value.shortValue();
            for (int i=0; i<offsets.length; i++) {
                final int offset = offsets[i];
                Arrays.fill(data.getData(i), offset, offset + size, n);
            }
        } else if (buffer instanceof DataBufferInt) {
            final DataBufferInt data = (DataBufferInt) buffer;
            final int n = value.intValue();
            for (int i=0; i<offsets.length; i++) {
                final int offset = offsets[i];
                Arrays.fill(data.getData(i), offset, offset + size, n);
            }
        } else if (buffer instanceof DataBufferFloat) {
            final DataBufferFloat data = (DataBufferFloat) buffer;
            final float n = value.floatValue();
            for (int i=0; i<offsets.length; i++) {
                final int offset = offsets[i];
                Arrays.fill(data.getData(i), offset, offset + size, n);
            }
        } else if (buffer instanceof DataBufferDouble) {
            final DataBufferDouble data = (DataBufferDouble) buffer;
            final double n = value.doubleValue();
            for (int i=0; i<offsets.length; i++) {
                final int offset = offsets[i];
                Arrays.fill(data.getData(i), offset, offset + size, n);
            }
        } else {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.UNSUPPORTED_DATA_TYPE));
        }
    }

    /**
     * Tells me whether or not the native libraries for JAI are active or not.
     * 
     * @return <code>false</code> in case the JAI native libs are not in the path, <code>true</code> otherwise.
     */
	public static boolean isMediaLibAvailable() {
		return mediaLibAvailable;
	}
	
    /**
     * Dispose an image with all its ancestors.
     * 
     * @param pi the {@link PlanarImage} to dispose.
     */
    public static void disposePlanarImageChain(PlanarImage pi) {
    	Utilities.ensureNonNull("PlanarImage", pi);
        disposePlanarImageChain(pi, new HashSet<PlanarImage>());
    }
    
	private static void disposePlanarImageChain(PlanarImage pi, Set<PlanarImage> visited) {
        List<?> sinks = pi.getSinks();
        // check all the sinks (the image might be in the middle of a chain)
        if(sinks != null) {
            for (Object sink: sinks) {
                if(sink instanceof PlanarImage && !visited.contains(sink))
                    disposePlanarImageChain((PlanarImage) sink, visited);
                else if(sink instanceof BufferedImage) {
                    ((BufferedImage) sink).flush();
                }
            }
        }
        // dispose the image itself
        pi.dispose();
        visited.add(pi);
        
        // check the image sources
        List<?> sources = pi.getSources();
        if(sources != null) {
            for (Object child : sources) {
                if(child instanceof PlanarImage && !visited.contains(child))
                    disposePlanarImageChain((PlanarImage) child, visited);
                else if(child instanceof BufferedImage) {
                    ((BufferedImage) child).flush();
                }
            }
        }
        
        // ImageRead might also hold onto a image input stream that we have to close
        if(pi instanceof RenderedOp) {
            RenderedOp op = (RenderedOp) pi;
            for(Object param : op.getParameterBlock().getParameters()) {
                if(param instanceof ImageInputStream) {
                    ImageInputStream iis = (ImageInputStream) param;
                    try {
                        iis.close();
                    } catch(IOException e) {
                        // fine, we tried
                    }
                }
            }
        }
    }

    /**
     * Relies on the {@link ImageWorker} to mask a certain color from an image.
     * 
     * @param transparentColor the {@link Color} to make transparent
     * @param image the {@link RenderedImage} to work on
     * @return a new {@link RenderedImage} where the provided {@link Color} has turned into transparent.
     * 
     * @throws IllegalStateException
     */
    public static RenderedImage maskColor(final Color transparentColor,
    		final RenderedImage image) throws IllegalStateException {
        Utilities.ensureNonNull("image", image);
        if(transparentColor==null){
            return image;
        }
        final ImageWorker w = new ImageWorker(image);
        if (image.getSampleModel() instanceof MultiPixelPackedSampleModel){
            w.forceComponentColorModel();
        }
        return w.makeColorTransparent(transparentColor).getRenderedImage();
    }

    static public ImageReadParam cloneImageReadParam(ImageReadParam param) {
    
    	// The ImageReadParam passed in is non-null. As the
    	// ImageReadParam class is not Cloneable, if the param
    	// class is simply ImageReadParam, then create a new
    	// ImageReadParam instance and set all its fields
    	// which were set in param. This will eliminate problems
    	// with concurrent modification of param for the cases
    	// in which there is not a special ImageReadparam used.
    
    	// Create a new ImageReadParam instance.
    	ImageReadParam newParam = new ImageReadParam();
    
    	// Set all fields which need to be set.
    
    	// IIOParamController field.
    	if (param.hasController()) {
    		newParam.setController(param.getController());
    	}
    
    	// Destination fields.
    	newParam.setDestination(param.getDestination());
    	if (param.getDestinationType() != null) {
    		// Set the destination type only if non-null as the
    		// setDestinationType() clears the destination field.
    		newParam.setDestinationType(param.getDestinationType());
    	}
    	newParam.setDestinationBands(param.getDestinationBands());
    	newParam.setDestinationOffset(param.getDestinationOffset());
    
    	// Source fields.
    	newParam.setSourceBands(param.getSourceBands());
    	newParam.setSourceRegion(param.getSourceRegion());
    	if (param.getSourceMaxProgressivePass() != Integer.MAX_VALUE) {
    		newParam.setSourceProgressivePasses(param
    				.getSourceMinProgressivePass(), param
    				.getSourceNumProgressivePasses());
    	}
    	if (param.canSetSourceRenderSize()) {
    		newParam.setSourceRenderSize(param.getSourceRenderSize());
    	}
    	newParam.setSourceSubsampling(param.getSourceXSubsampling(), param
    			.getSourceYSubsampling(), param.getSubsamplingXOffset(), param
    			.getSubsamplingYOffset());
    
    	// Replace the local variable with the new ImageReadParam.
    	return newParam;
    
    }

    public static Rectangle2D layoutHelper(RenderedImage source,
                                        float scaleX,
                                        float scaleY,
                                        float transX,
                                        float transY,
                                        Interpolation interp) {
    
    // Represent the scale factors as Rational numbers.
            // Since a value of 1.2 is represented as 1.200001 which
            // throws the forward/backward mapping in certain situations.
            // Convert the scale and translation factors to Rational numbers
            Rational scaleXRational = Rational.approximate(scaleX,RATIONAL_TOLERANCE);
            Rational scaleYRational = Rational.approximate(scaleY,RATIONAL_TOLERANCE);
    
            long scaleXRationalNum = (long) scaleXRational.num;
            long scaleXRationalDenom = (long) scaleXRational.denom;
            long scaleYRationalNum = (long) scaleYRational.num;
            long scaleYRationalDenom = (long) scaleYRational.denom;
    
            Rational transXRational = Rational.approximate(transX,RATIONAL_TOLERANCE);
            Rational transYRational = Rational.approximate(transY,RATIONAL_TOLERANCE);
    
            long transXRationalNum = (long) transXRational.num;
            long transXRationalDenom = (long) transXRational.denom;
            long transYRationalNum = (long) transYRational.num;
            long transYRationalDenom = (long) transYRational.denom;
    
            int x0 = source.getMinX();
            int y0 = source.getMinY();
            int w = source.getWidth();
            int h = source.getHeight();
    
            // Variables to store the calculated destination upper left coordinate
            long dx0Num, dx0Denom, dy0Num, dy0Denom;
    
            // Variables to store the calculated destination bottom right
            // coordinate
            long dx1Num, dx1Denom, dy1Num, dy1Denom;
    
            // Start calculations for destination
    
            dx0Num = x0;
            dx0Denom = 1;
    
            dy0Num = y0;
            dy0Denom = 1;
    
            // Formula requires srcMaxX + 1 = (x0 + w - 1) + 1 = x0 + w
            dx1Num = x0 + w;
            dx1Denom = 1;
    
            // Formula requires srcMaxY + 1 = (y0 + h - 1) + 1 = y0 + h
            dy1Num = y0 + h;
            dy1Denom = 1;
    
            dx0Num *= scaleXRationalNum;
            dx0Denom *= scaleXRationalDenom;
    
            dy0Num *= scaleYRationalNum;
            dy0Denom *= scaleYRationalDenom;
    
            dx1Num *= scaleXRationalNum;
            dx1Denom *= scaleXRationalDenom;
    
            dy1Num *= scaleYRationalNum;
            dy1Denom *= scaleYRationalDenom;
    
            // Equivalent to subtracting 0.5
            dx0Num = 2 * dx0Num - dx0Denom;
            dx0Denom *= 2;
    
            dy0Num = 2 * dy0Num - dy0Denom;
            dy0Denom *= 2;
    
            // Equivalent to subtracting 1.5
            dx1Num = 2 * dx1Num - 3 * dx1Denom;
            dx1Denom *= 2;
    
            dy1Num = 2 * dy1Num - 3 * dy1Denom;
            dy1Denom *= 2;
    
            // Adding translation factors
    
            // Equivalent to float dx0 += transX
            dx0Num = dx0Num * transXRationalDenom + transXRationalNum * dx0Denom;
            dx0Denom *= transXRationalDenom;
    
            // Equivalent to float dy0 += transY
            dy0Num = dy0Num * transYRationalDenom + transYRationalNum * dy0Denom;
            dy0Denom *= transYRationalDenom;
    
            // Equivalent to float dx1 += transX
            dx1Num = dx1Num * transXRationalDenom + transXRationalNum * dx1Denom;
            dx1Denom *= transXRationalDenom;
    
            // Equivalent to float dy1 += transY
            dy1Num = dy1Num * transYRationalDenom + transYRationalNum * dy1Denom;
            dy1Denom *= transYRationalDenom;
    
            // Get the integral coordinates
            int l_x0, l_y0, l_x1, l_y1;
    
            l_x0 = Rational.ceil(dx0Num, dx0Denom);
            l_y0 = Rational.ceil(dy0Num, dy0Denom);
    
            l_x1 = Rational.ceil(dx1Num, dx1Denom);
            l_y1 = Rational.ceil(dy1Num, dy1Denom);
    
            // Set the top left coordinate of the destination
            final Rectangle2D retValue= new Rectangle2D.Double();
            retValue.setFrame(l_x0, l_y0, l_x1 - l_x0 + 1, l_y1 - l_y0 + 1);
            return retValue;
    }

    /**
     * Builds a {@link ReferencedEnvelope} from a {@link GeographicBoundingBox}.
     * This is useful in order to have an implementation of {@link BoundingBox}
     * from a {@link GeographicBoundingBox} which strangely does implement
     * {@link GeographicBoundingBox}.
     * 
     * @param geographicBBox
     *            the {@link GeographicBoundingBox} to convert.
     * @return an instance of {@link ReferencedEnvelope}.
     */
    static public ReferencedEnvelope getReferencedEnvelopeFromGeographicBoundingBox(
    		final GeographicBoundingBox geographicBBox) {
    	Utilities.ensureNonNull("GeographicBoundingBox", geographicBBox);
    	return new ReferencedEnvelope(geographicBBox.getEastBoundLongitude(),
    			geographicBBox.getWestBoundLongitude(), geographicBBox
    					.getSouthBoundLatitude(), geographicBBox
    					.getNorthBoundLatitude(), DefaultGeographicCRS.WGS84);
    }

    /**
     * Builds a {@link ReferencedEnvelope} in WGS84 from a {@link GeneralEnvelope}.
     * 
     * @param coverageEnvelope
     *            the {@link GeneralEnvelope} to convert.
     * @return an instance of {@link ReferencedEnvelope} in WGS84 or <code>null</code> in case a problem during the conversion occurs.
     */
    static public ReferencedEnvelope getWGS84ReferencedEnvelope(
    		final GeneralEnvelope coverageEnvelope) {
    	Utilities.ensureNonNull("coverageEnvelope", coverageEnvelope);
    	final ReferencedEnvelope refEnv= new ReferencedEnvelope(coverageEnvelope);
    	try{
    	    return refEnv.transform(DefaultGeographicCRS.WGS84, true);
    	}catch (Exception e) {
                return null;
            }
    }

    /**
     * Retrieves the dimensions of the {@link RenderedImage} at index
     * <code>imageIndex</code> for the provided {@link ImageReader} and
     * {@link ImageInputStream}.
     * 
     * <p>
     * Notice that none of the input parameters can be <code>null</code> or a
     * {@link NullPointerException} will be thrown. Morevoer the
     * <code>imageIndex</code> cannot be negative or an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param imageIndex
     *            the index of the image to get the dimensions for.
     * @param inStream
     *            the {@link ImageInputStream} to use as an input
     * @param reader
     *            the {@link ImageReader} to decode the image dimensions.
     * @return a {@link Rectangle} that contains the dimensions for the image at
     *         index <code>imageIndex</code>
     * @throws IOException
     *             in case the {@link ImageReader} or the
     *             {@link ImageInputStream} fail.
     */
    public static Rectangle getDimension(final int imageIndex,
    		final ImageInputStream inStream, final ImageReader reader)
    		throws IOException {
    	Utilities.ensureNonNull("inStream", inStream);
    	Utilities.ensureNonNull("reader", reader);
    	if (imageIndex < 0)
    		throw new IllegalArgumentException(Errors.format(
    				ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, imageIndex));
    	inStream.reset();
    	reader.setInput(inStream);
    	return new Rectangle(0, 0, reader.getWidth(imageIndex), reader
    			.getHeight(imageIndex));
    }

    /**
     * Checks that the provided <code>dimensions</code> when intersected with
     * the source region used by the provided {@link ImageReadParam} instance
     * does not result in an empty {@link Rectangle}.
     * 
     * <p>
     * Input parameters cannot be null.
     * 
     * @param readParameters
     *            an instance of {@link ImageReadParam} for which we want to
     *            check the source region element.
     * @param dimensions
     *            an instance of {@link Rectangle} to use for the check.
     * @return <code>true</code> if the intersection is not empty,
     *         <code>false</code> otherwise.
     */
    public final static boolean checkEmptySourceRegion(final ImageReadParam readParameters,
    		final Rectangle dimensions) {
            Utilities.ensureNonNull("readDimension", dimensions);
            Utilities.ensureNonNull("readP", readParameters);
    	final Rectangle sourceRegion = readParameters.getSourceRegion();
    	Rectangle.intersect(sourceRegion, dimensions, sourceRegion);
    	if (sourceRegion.isEmpty())
    		return true;
    	readParameters.setSourceRegion(sourceRegion);
    	return false;
    }

    /**
     * Build a background values array using the same dataType of the input {@link SampleModel} (if
     * available) and the values provided in the input array.
     * 
     * @param sampleModel
     * @param backgroundValues
     * @return
     */
    public static Number[] getBackgroundValues(final SampleModel sampleModel, final double[] backgroundValues) {
        Number[] values = null;
        final int dataType = sampleModel != null ? sampleModel.getDataType() : DataBuffer.TYPE_DOUBLE;
        final int numBands=sampleModel != null? sampleModel.getNumBands() : 1;
        switch (dataType){
        
            case DataBuffer.TYPE_BYTE:
                values = new Byte[numBands];
                 if (backgroundValues == null){                          
                         Arrays.fill(values, Byte.valueOf((byte)0));
                 }
                 else{
                        //we have background values available
                     for (int i = 0; i < values.length; i++)
                         values[i] = i>=backgroundValues.length?Byte.valueOf((byte)backgroundValues[0]):Byte.valueOf((byte)backgroundValues[i]);
                 }
                 break;
            case DataBuffer.TYPE_SHORT:
            case DataBuffer.TYPE_USHORT:
                 values = new Short[numBands] ;
                 if (backgroundValues == null)
                         Arrays.fill(values, Short.valueOf((short)0));
                 else {
                        //we have background values available
                         for (int i = 0; i < values.length; i++)
                             values[i] = i>=backgroundValues.length?Short.valueOf((short)backgroundValues[0]):Short.valueOf((short)backgroundValues[i]);
                 }
                 break;
            case DataBuffer.TYPE_INT:
                values = new Integer[numBands] ;
                if (backgroundValues == null)
                    Arrays.fill(values, Integer.valueOf((int) 0));
                else {
                    // we have background values available
                    for (int i = 0; i < values.length; i++)
                        values[i] = i >= backgroundValues.length ? Integer.valueOf((int) backgroundValues[0]) : Integer.valueOf((int) backgroundValues[i]);
    
                }
                 break;
            case DataBuffer.TYPE_FLOAT:
                values = new Float[numBands] ;
                 if (backgroundValues == null)
                        Arrays.fill(values, Float.valueOf(0.f));
                 else{
                        //we have background values available
                     for (int i = 0; i < values.length; i++)
                         values[i] = i>=backgroundValues.length?Float.valueOf((float)backgroundValues[0]):Float.valueOf((float)backgroundValues[i]);
                 }
                 break;
            case DataBuffer.TYPE_DOUBLE:
                values = new Double[numBands] ;
                 if (backgroundValues == null)
                        Arrays.fill(values, Double.valueOf(0.d));
                 else {
                        //we have background values available
                     for (int i = 0; i < values.length; i++)
                         values[i] = i>=backgroundValues.length?Double.valueOf((Double)backgroundValues[0]):Double.valueOf((Double)backgroundValues[i]);
                 }
                 break;
            }
        return values;
    }
}
