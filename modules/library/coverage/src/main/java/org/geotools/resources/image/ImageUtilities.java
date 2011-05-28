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
import java.awt.RenderingHints;
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
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
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

import org.geotools.image.ImageWorker;
import org.geotools.image.io.ImageIOExt;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;

import com.sun.media.imageioimpl.common.PackageUtil;
import com.sun.media.jai.operator.ImageReadDescriptor;


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
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions
 */
@SuppressWarnings("unchecked")
public final class ImageUtilities {
	

	 /**
     * {@code true} if JAI media lib is available.
     */
    private static final boolean mediaLibAvailable;
    static {

    	// do we wrappers at hand?
        boolean mediaLib = false;
        Class mediaLibImage = null;
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
		        	final Class mImage=mediaLibImage;
	                mediaLib=AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
	                     public Boolean run() {
	                    	 try {
	                    		//get the method
	                    		final Class params[] = {};
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
     * Allows or disallows native acceleration for the specified image format. By default, the
     * image I/O extension for JAI provides native acceleration for PNG and JPEG. Unfortunatly,
     * those native codec has bug in their 1.0 version. Invoking this method will force the use
     * of standard codec provided in J2SE 1.4.
     * <p>
     * <strong>Implementation note:</strong> the current implementation assume that JAI codec
     * class name start with "CLib". It work for Sun's 1.0 implementation, but may change in
     * future versions. If this method doesn't recognize the class name, it does nothing.
     *
     * @param format The format name (e.g. "png").
     * @param category {@code ImageReaderSpi.class} to set the reader, or
     *        {@code ImageWriterSpi.class} to set the writer.
     * @param allowed {@code false} to disallow native acceleration.
     * @deprecated Use {@link ImageIOExt#allowNativeCodec(String, Class, boolean)} instead
     */
    public static synchronized <T extends ImageReaderWriterSpi> void allowNativeCodec(
            final String format, final Class<T> category, final boolean allowed)
    {
        ImageIOExt.allowNativeCodec(format, category, allowed);
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
     * Tells me whether or not the native libraries for JAI/ImageIO are active or not.
     * 
     * @return <code>false</code> in case the JAI/ImageIO native libs are not in the path, <code>true</code> otherwise.
     * @deprecated Use {@link ImageIOExt#isCLibAvailable()} instead
     */
	public static boolean isCLibAvailable() {
		return PackageUtil.isCodecLibAvailable();
	}

    /**
     * Get a proper {@link ImageInputStreamSpi} instance for the provided {@link Object} input without
     * trying to create an {@link ImageInputStream}.
     *  
     * @see #getImageInputStreamSPI(Object, boolean) 
     * @deprecated Use {@link ImageIOExt#getImageInputStreamSPI(Object, boolean)} instead
     */
    public final static ImageInputStreamSpi getImageInputStreamSPI(final Object input) {
        return ImageIOExt.getImageInputStreamSPI(input);
    }

    /**
     * Get a proper {@link ImageInputStreamSpi} instance for the provided {@link Object} input.
     *   
     * @param input the input object for which we need to find a proper {@link ImageInputStreamSpi} instance
     * @param streamCreationCheck if <code>true</code>, when a proper {@link ImageInputStreamSpi} have been found 
     * for the provided input, use it to try creating an {@link ImageInputStream} on top of the input.  
     * 
     * @return an {@link ImageInputStreamSpi} instance.
     * @deprecated Use {@link ImageIOExt#getImageInputStreamSPI(Object, boolean)} instead
     */
    public final static ImageInputStreamSpi getImageInputStreamSPI(final Object input, final boolean streamCreationCheck) {
        return ImageIOExt.getImageInputStreamSPI(input, streamCreationCheck);
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
    
	private static void disposePlanarImageChain(PlanarImage pi, HashSet<PlanarImage> visited) {
        Vector sinks = pi.getSinks();
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
        Vector sources = pi.getSources();
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
}
