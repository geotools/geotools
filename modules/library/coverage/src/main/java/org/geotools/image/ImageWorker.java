/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PackedColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageOutputStreamSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.ColorCube;
import javax.media.jai.IHSColorSpace;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.ParameterListDescriptor;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.operator.AddConstDescriptor;
import javax.media.jai.operator.AddDescriptor;
import javax.media.jai.operator.AffineDescriptor;
import javax.media.jai.operator.AndDescriptor;
import javax.media.jai.operator.BandCombineDescriptor;
import javax.media.jai.operator.BandMergeDescriptor;
import javax.media.jai.operator.BandSelectDescriptor;
import javax.media.jai.operator.BinarizeDescriptor;
import javax.media.jai.operator.ColorConvertDescriptor;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.ErrorDiffusionDescriptor;
import javax.media.jai.operator.ExtremaDescriptor;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.operator.InvertDescriptor;
import javax.media.jai.operator.LookupDescriptor;
import javax.media.jai.operator.MultiplyConstDescriptor;
import javax.media.jai.operator.NotDescriptor;
import javax.media.jai.operator.NullDescriptor;
import javax.media.jai.operator.OrderedDitherDescriptor;
import javax.media.jai.operator.RescaleDescriptor;
import javax.media.jai.operator.ScaleDescriptor;
import javax.media.jai.operator.XorConstDescriptor;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.factory.Hints;
import org.geotools.image.crop.GTCropDescriptor;
import org.geotools.image.io.ImageIOExt;
import org.geotools.resources.Arguments;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.image.ColorUtilities;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.logging.Logging;

import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.media.imageioimpl.common.BogusColorSpace;
import com.sun.media.imageioimpl.common.PackageUtil;
import com.sun.media.imageioimpl.plugins.gif.GIFImageWriter;
import com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriterSpi;
import com.sun.media.jai.util.ImageUtil;


/**
 * Helper methods for applying JAI operations on an image. The image is specified at
 * {@linkplain #ImageWorker(RenderedImage) creation time}. Sucessive operations can
 * be applied by invoking the methods defined in this class, and the final image can
 * be obtained by invoking {@link #getRenderedImage} at the end of the process.
 * <p>
 * If an exception is thrown during a method invocation, then this {@code ImageWorker}
 * is left in an undetermined state and should not be used anymore.
 *
 * @since 2.3
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 * @author Bryce Nordgren
 * @author Martin Desruisseaux
 */
public class ImageWorker {
    
    /**
     * Raster space epsilon
     */
    static final float RS_EPS = 1E-02f;
    
    /**
     * Workaround class for compressing PNG using the default
     * PNGImageEncoder shipped with the JDK.
     * <p>
     * {@link PNGImageWriter} does not support
     * {@link ImageWriteParam#setCompressionMode(int)} set to
     * {@link ImageWriteParam#MODE_EXPLICIT}, it only allows
     * {@link ImageWriteParam#MODE_DEFAULT}.
     *
     * @author Simone Giannecchini
     *
     * @todo Consider moving to {@link org.geotools.image.io} package.
     */
    public final static class PNGImageWriteParam extends ImageWriteParam {
        /**
         * Default constructor.
         */
        public PNGImageWriteParam() {
            super();
            this.canWriteProgressive = true;
            this.canWriteCompressed = true;
            this.locale = Locale.getDefault();
        }
    }
    

    /**
     * The logger to use for this class.
     */
    private final static Logger LOGGER = Logging.getLogger("org.geotools.image");

    /**
     * If {@link Boolean#FALSE FALSE}, image operators are not allowed to
     * produce tiled images. The default is {@link Boolean#TRUE TRUE}. The
     * {@code FALSE} value is sometime useful for exporting images to some
     * formats that doesn't support tiling (e.g. GIF).
     *
     * @see #setRenderingHint
     */
    public static final Hints.Key TILING_ALLOWED = new Hints.Key(Boolean.class);

    /**
     * The image property name generated by {@link ExtremaDescriptor}.
     */
    private static final String EXTREMA = "extrema";
    
    /**
     * Register manually the GTCrop operation, in web containers JAI registration may fails
     */
    static {
        GTCropDescriptor.register();
    }

    /**
     * The image specified by the user at construction time, or last time
     * {@link #invalidateStatistics} were invoked. The {@link #getComputedProperty}
     * method will not search a property pass this point.
     */
    private RenderedImage inheritanceStopPoint;

    /**
     * The image being built.
     */
    protected RenderedImage image;

    /**
     * The region of interest, or {@code null} if none.
     */
    private ROI roi;

    /**
     * The rendering hints to provides to all image operators. Additional hints may
     * be set (in a separated {@link RenderingHints} object) for particular images.
     */
    private RenderingHints commonHints;

    /**
     * 0 if tile cache is enabled, any other value otherwise. This counter is
     * incremented everytime {@code tileCacheEnabled(false)} is invoked, and
     * decremented every time {@code tileCacheEnabled(true)} is invoked.
     */
    private int tileCacheDisabled = 0;

    /**
     * Creates a new uninitialized builder for an {@linkplain #load image read}.
     *
     * @see #load
     */
    public ImageWorker()  {
    	inheritanceStopPoint = this.image = null;
    }

    /**
     * Creates a new builder for an image read from the specified file.
     *
     * @param  input The file to read.
     * @throws IOException if the file can't be read.
     */
    public ImageWorker(final File input) throws IOException {
        this(ImageIO.read(input));
    }

    /**
     * Creates a new builder for the specified image. The images to be computed (if any)
     * will save their tiles in the default {@linkplain TileCache tile cache}.
     *
     * @param image The source image.
     */
    public ImageWorker(final RenderedImage image) {
        inheritanceStopPoint = this.image = image;
    }

    /**
     * Prepare this builder for the specified image. The images to be computed (if any)
     * will save their tiles in the default {@linkplain TileCache tile cache}.
     *
     * @param image The source image.
     */
    public final ImageWorker setImage(final RenderedImage image) {
        inheritanceStopPoint = this.image = image;
        return this;
    }

    /**
     * Creates a new image worker with the same hints but a different image.
     */
    private ImageWorker fork(final RenderedImage image) {
        final ImageWorker worker = new ImageWorker(image);
        if (commonHints!=null && !commonHints.isEmpty()) {
        	RenderingHints hints = new RenderingHints(null);
        	hints.add(worker.commonHints);
        	worker.commonHints = hints;
        }
        return worker;
    }

    /**
     * Loads an image using the provided file name and the
     * {@linkplain #getRenderingHints current hints}, which are used to control caching and layout.
     *
     * @param source       Filename of the source image to read.
     * @param imageChoice  Image index in multipage images.
     * @param readMatadata If {@code true}, metadata will be read.
     */
    public final void load(final String source, final int imageChoice, final boolean readMetadata) {
        final ParameterBlockJAI pbj = new ParameterBlockJAI("ImageRead");
        pbj.setParameter("Input",        source)
           .setParameter("ImageChoice",  Integer.valueOf(imageChoice))
           .setParameter("ReadMetadata", Boolean.valueOf(readMetadata))
           .setParameter("VerifyInput",  Boolean.TRUE);
        image = JAI.create("ImageRead", pbj, getRenderingHints());
    }



    ///////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                       ////////
    ////////            IMAGE, PROPERTIES AND RENDERING HINTS ACCESSORS            ////////
    ////////                                                                       ////////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the current image.
     *
     * @return The rendered image.
     *
     * @see #getBufferedImage
     * @see #getPlanarImage
     * @see #getRenderedOperation
     * @see #getImageAsROI
     */
    public final RenderedImage getRenderedImage() {
        return image;
    }

    /**
     * Returns the current image as a buffered image.
     *
     * @return The buffered image.
     *
     * @see #getRenderedImage
     * @see #getPlanarImage
     * @see #getRenderedOperation
     * @see #getImageAsROI
     *
     * @since 2.5
     */
    public final BufferedImage getBufferedImage() {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        } else {
            return getPlanarImage().getAsBufferedImage();
        }
    }

    /**
     * Returns the {@linkplain #getRenderedImage rendered image} as a planar image.
     *
     * @return The planar image.
     *
     * @see #getRenderedImage
     * @see #getRenderedOperation
     * @see #getImageAsROI
     */
    public final PlanarImage getPlanarImage() {
        return PlanarImage.wrapRenderedImage(getRenderedImage());
    }

    /**
     * Returns the {@linkplain #getRenderedImage rendered image} as a rendered operation.
     *
     * @return The rendered operation.
     *
     * @see #getRenderedImage
     * @see #getPlanarImage
     * @see #getImageAsROI
     */
    public final RenderedOp getRenderedOperation() {
        final RenderedImage image = getRenderedImage();
        if (image instanceof RenderedOp) {
            return (RenderedOp) image;
        }
        return NullDescriptor.create(image, getRenderingHints());
    }

    /**
     * Returns a {@linkplain ROI Region Of Interest} built from the current
     * {@linkplain #getRenderedImage image}. If the image is multi-bands, then this method first
     * computes an estimation of its {@linkplain #intensity intensity}. Next, this method
     * {@linkplain #binarize() binarize} the image and constructs a {@link ROI} from the result.
     *
     * @return The image as a region of interest.
     *
     * @see #getRenderedImage
     * @see #getPlanarImage
     * @see #getRenderedOperation
     */
    public final ROI getImageAsROI() {
        binarize();
        return new ROI(getRenderedImage());
    }

    /**
     * Returns the <cite>region of interest</cite> currently set, or {@code null} if none.
     * The default value is {@code null}.
     *
     * @return The current region of interest.
     *
     * @see #getMinimums
     * @see #getMaximums
     */
    public final ROI getROI() {
        return roi;
    }

    /**
     * Set the <cite>region of interest</cite> (ROI). A {@code null} set the ROI to the whole
     * {@linkplain #image}. The ROI is used by statistical methods like {@link #getMinimums}
     * and {@link #getMaximums}.
     *
     * @param roi The new region of interest.
     * @return This ImageWorker
     *
     * @see #getMinimums
     * @see #getMaximums
     */
    public final ImageWorker setROI(final ROI roi) {
        this.roi = roi;
        invalidateStatistics();
        return this;
    }

    /**
     * Returns the rendering hint for the specified key, or {@code null} if none.
     */
    public final Object getRenderingHint(final RenderingHints.Key key) {
        return (commonHints != null) ? commonHints.get(key) : null;
    }

    /**
     * Sets a rendering hint tile to use for all images to be computed by this class. This method
     * applies only to the next images to be computed; images already computed before this method
     * call (if any) will not be affected.
     * <p>
     * Some common examples:
     * <p>
     * <ul>
     *   <li><code>setRenderingHint({@linkplain JAI#KEY_TILE_CACHE}, null)</code>
     *       disables completly the tile cache.</li>
     *   <li><code>setRenderingHint({@linkplain #TILING_ALLOWED}, Boolean.FALSE)</code>
     *       forces all operators to produce untiled images.</li>
     * </ul>
     *
     * @return This ImageWorker
     */
    public final ImageWorker setRenderingHint(final RenderingHints.Key key, final Object value) {
        if (commonHints == null) {
            commonHints = new RenderingHints(null);
        }
        commonHints.add(new RenderingHints(key,value));
        return this;
    }

    /**
     * Set a map of rendering hints  to use for all images to be computed by this class. This method
     * applies only to the next images to be computed; images already computed before this method
     * call (if any) will not be affected.
     *
     * <p>
     * If <code>hints</code> is null we won't modify this list.
     * @return This ImageWorker
     * @see #setRenderingHint(RenderingHints)
     */
	public final ImageWorker setRenderingHints(final RenderingHints
			hints) {
        if (commonHints == null) {
            commonHints = new RenderingHints(null);
        }
        if(hints!=null)
        	commonHints.add(hints);
		return this;
    }

    /**
     * Removes a rendering hint. Note that invoking this method is <strong>not</strong> the same
     * than invoking <code>{@linkplain #setRenderingHint setRenderingHint}(key, null)</code>.
     * This is especially true for the {@linkplain javax.media.jai.TileCache tile cache} hint:
     * <p>
     * <ul>
     *   <li><code>{@linkplain #setRenderingHint setRenderingHint}({@linkplain JAI#KEY_TILE_CACHE},
     *       null)</code> disables the use of any tile cache. In other words, this method call do
     *       request a tile cache, which happen to be the "null" cache.</li>
     *
     *   <li><code>removeRenderingHint({@linkplain JAI#KEY_TILE_CACHE})</code> unsets any tile cache
     *       specified by a previous rendering hint. All images to be computed after this method
     *       call will save their tiles in the {@linkplain JAI#getTileCache JAI default tile
     *       cache}.</li>
     * </ul>
     *
     * @return This ImageWorker
     */
    public final ImageWorker removeRenderingHint(final RenderingHints.Key key) {
        if (commonHints != null) {
            commonHints.remove(key);
        }
        return this;
    }

    /**
     * Returns the rendering hints for an image to be computed by this class.
     * The default implementation returns the following hints:
     * <p>
     * <ul>
     *   <li>An {@linkplain ImageLayout image layout} with tiles size computed automatically
     *       from the current {@linkplain #image} size.</li>
     *   <li>Any additional hints specified through the {@link #setRenderingHint} method. If the
     *       user provided explicitly a {@link JAI#KEY_IMAGE_LAYOUT}, then the user layout has
     *       precedence over the automatic layout computed in previous step.</li>
     * </ul>
     *
     * @return The rendering hints to use for image computation (never {@code null}).
     */
    public final RenderingHints getRenderingHints() {
        RenderingHints hints = ImageUtilities.getRenderingHints(image);
        if (hints == null) {
            hints = new RenderingHints(null);
            if (commonHints != null) {
            	hints.add(commonHints);
            }
        } else if (commonHints != null) {
            hints.putAll(commonHints);
        }
        if (Boolean.FALSE.equals(hints.get(TILING_ALLOWED))) {
            final ImageLayout layout = getImageLayout(hints);
            if (commonHints==null || layout != commonHints.get(JAI.KEY_IMAGE_LAYOUT)) {
                // Set the layout only if it is not a user-supplied object.
                layout.setTileWidth      (image.getWidth());
                layout.setTileHeight     (image.getHeight());
                layout.setTileGridXOffset(image.getMinX());
                layout.setTileGridYOffset(image.getMinY());
                hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
            }
        }
        if (tileCacheDisabled!=0 && (commonHints!=null &&!commonHints.containsKey(JAI.KEY_TILE_CACHE))) {
            hints.add(new RenderingHints(JAI.KEY_TILE_CACHE, null));
        }
        return hints;
    }

    /**
     * Returns the {@linkplain #getRenderingHints rendering hints}, but with a
     * {@linkplain ComponentColorModel component color model} of the specified
     * data type. The data type is changed only if no color model was explicitly
     * specified by the user through {@link #getRenderingHints()}.
     *
     * @param type The data type (typically {@link DataBuffer#TYPE_BYTE}).
     */
    private final RenderingHints getRenderingHints(final int type) {
        /*
         * Gets the default hints, which usually contains only informations about tiling.
         * If the user overridden the rendering hints with an explict color model, keep
         * the user's choice.
         */
        final RenderingHints hints = getRenderingHints();
        final ImageLayout layout = getImageLayout(hints);
        if (layout.isValid(ImageLayout.COLOR_MODEL_MASK)) {
            return hints;
        }
        /*
         * Creates the new color model.
         */
        final ColorModel oldCm = image.getColorModel();
        if(oldCm!=null){
	        final ColorModel newCm= new ComponentColorModel(
	                oldCm.getColorSpace(),
	                oldCm.hasAlpha(),               // If true, supports transparency.
	                oldCm.isAlphaPremultiplied(),   // If true, alpha is premultiplied.
	                oldCm.getTransparency(),        // What alpha values can be represented.
	                type);                          // Type of primitive array used to represent pixel.
	        /*
	         * Creating the final image layout which should allow us to change color model.
	         */
	        layout.setColorModel(newCm);
	        layout.setSampleModel(newCm.createCompatibleSampleModel(image.getWidth(), image.getHeight()));
        }else{
        	final int numBands=image.getSampleModel().getNumBands();
        	final ColorModel newCm= new ComponentColorModel(
	                new BogusColorSpace(numBands),
	                false,               		// If true, supports transparency.
	                false,   					// If true, alpha is premultiplied.
	                Transparency.OPAQUE,        // What alpha values can be represented.
	                type);                      // Type of primitive array used to represent pixel.
	        /*
	         * Creating the final image layout which should allow us to change color model.
	         */
	        layout.setColorModel(newCm);
	        layout.setSampleModel(newCm.createCompatibleSampleModel(image.getWidth(), image.getHeight()));
        }
        hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
        return hints;
    }

    /**
     * Gets the image layout from the specified rendering hints, creating a new one if needed.
     * This method do not modify the specified hints. If the caller modifies the image layout,
     * it should invoke {@code hints.put(JAI.KEY_IMAGE_LAYOUT, layout)} explicitly.
     */
    private static ImageLayout getImageLayout(final RenderingHints hints) {
        final Object candidate = hints.get(JAI.KEY_IMAGE_LAYOUT);
        if (candidate instanceof ImageLayout) {
            return (ImageLayout) candidate;
        }
        return new ImageLayout();
    }

    /**
     * If {@code false}, disables the tile cache. Invoking this method with value {@code true}
     * cancel the last invocation with value {@code false}. If this method was invoking many
     * time with value {@code false}, then this method must be invoked the same amount of time
     * with the value {@code true} for reenabling the cache.
     * <p>
     * <strong>Note:</strong> This method name doesn't contain the usual {@code set} prefix
     * because it doesn't really set a flag. Instead it increments or decrements a counter.
     *
     * @return This ImageWorker
     */
    public final ImageWorker tileCacheEnabled(final boolean status) {
        if (status) {
            if (tileCacheDisabled != 0) {
                tileCacheDisabled--;
            } else {
                throw new IllegalStateException();
            }
        } else {
            tileCacheDisabled++;
        }
        return this;
    }

    /**
     * Returns the number of bands in the {@linkplain #image}.
     *
     * @see #retainBands
     * @see #retainFirstBand
     * @see SampleModel#getNumBands
     */
    public final int getNumBands() {
        return image.getSampleModel().getNumBands();
    }

    /**
     * Returns the transparent pixel value, or -1 if none.
     */
    public final int getTransparentPixel() {
        final ColorModel cm = image.getColorModel();
        return (cm instanceof IndexColorModel) ? ((IndexColorModel) cm).getTransparentPixel() : -1;
    }

    /**
     * Gets a property from the property set of the {@linkplain #image}. If the property name
     * is not recognized, then {@link Image#UndefinedProperty} will be returned. This method
     * do <strong>not</strong> inherits properties from the image specified at
     * {@linkplain #ImageWorker(RenderedImage) construction time} - only properties generated
     * by this class are returned.
     */
    private Object getComputedProperty(final String name) {
        final Object value = image.getProperty(name);
        return (value == inheritanceStopPoint.getProperty(name)) ? Image.UndefinedProperty : value;
    }

    /**
     * Returns the minimums and maximums values found in the image. Those extremas are
     * returned as an array of the form {@code double[2][#bands]}.
     */
    private double[][] getExtremas() {
        Object extrema = getComputedProperty(EXTREMA);
        if (!(extrema instanceof double[][])) {
            final Integer ONE = 1;
            image = ExtremaDescriptor.create(
                    image,  // The source image.
                    roi,    // The region of the image to scan. Default to all.
                    ONE,    // The horizontal sampling rate. Default to 1.
                    ONE,    // The vertical sampling rate. Default to 1.
                    null,   // Whether to store extrema locations. Default to false.
                    ONE,    // Maximum number of run length codes to store. Default to 1.
                    getRenderingHints());
            extrema = getComputedProperty(EXTREMA);
        }
        return (double[][]) extrema;
    }

    /**
     * Tells this builder that all statistics on pixel values (e.g. the "extrema" property
     * in the {@linkplain #image}) should not be inherited from the source images (if any).
     * This method should be invoked every time an operation changed the pixel values.
     *
     * @return This ImageWorker
     */
    private ImageWorker invalidateStatistics() {
        inheritanceStopPoint = image;
        return this;
    }

    /**
     * Returns the minimal values found in every {@linkplain #image} bands. If a
     * {@linkplain #getROI region of interest} is defined, then the statistics
     * will be computed only over that region.
     *
     * @see #getMaximums
     * @see #setROI
     */
    public final double[] getMinimums() {
        return getExtremas()[0];
    }

    /**
     * Returns the maximal values found in every {@linkplain #image} bands. If a
     * {@linkplain #getROI region of interest} is defined, then the statistics
     * will be computed only over that region.
     *
     * @see #getMinimums
     * @see #setROI
     */
    public final double[] getMaximums() {
        return getExtremas()[1];
    }



    ///////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                       ////////
    ////////            KIND OF IMAGE (BYTES, BINARY, INDEXED, RGB...)             ////////
    ////////                                                                       ////////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns {@code true} if the {@linkplain #image} stores its pixel values in 8 bits.
     *
     * @see #rescaleToBytes
     */
    public final boolean isBytes() {
        final SampleModel sm    = image.getSampleModel();
        final int[] sampleSize=sm.getSampleSize();
        for(int i=0;i<sampleSize.length;i++)
            if(sampleSize[i]!=8)
                return false;
        return true;
        
    }

    /**
     * Returns {@code true} if the {@linkplain #image} is binary. Such image usually contains
     * only two values: 0 and 1.
     *
     * @see #binarize()
     * @see #binarize(double)
     * @see #binarize(int,int)
     */
    public final boolean isBinary() {
        return ImageUtil.isBinary(image.getSampleModel());
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses an {@linkplain IndexColorModel
     * index color model}.
     *
     * @see #forceIndexColorModel
     * @see #forceBitmaskIndexColorModel
     * @see #forceIndexColorModelForGIF
     */
    public final boolean isIndexed() {
        return image.getColorModel() instanceof IndexColorModel;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses a RGB {@linkplain ColorSpace color
     * space}. Note that a RGB color space doesn't mean that pixel values are directly stored
     * as RGB components. The image may be {@linkplain #isIndexed indexed} as well.
     *
     * @see #forceColorSpaceRGB
     */
    public final boolean isColorSpaceRGB() {
    	final ColorModel cm = image.getColorModel();
    	if(cm==null)
    		return false;
        return cm.getColorSpace().getType() == ColorSpace.TYPE_RGB;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses a GrayScale
     * {@linkplain ColorSpace color space}. Note that a GrayScale color space
     * doesn't mean that pixel values are directly stored as GrayScale
     * component. The image may be {@linkplain #isIndexed indexed} as well.
     *
     * @see #forceColorSpaceGRAYScale
     */
    public final boolean isColorSpaceGRAYScale() {
    	final ColorModel cm = image.getColorModel();
    	if(cm==null)
    		return false;
        return cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} is
     * {@linkplain Transparency#TRANSLUCENT translucent}.
     *
     * @see #forceBitmaskIndexColorModel
     */
    public final boolean isTranslucent() {
        return image.getColorModel().getTransparency() == Transparency.TRANSLUCENT;
    }



    ///////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                       ////////
    ////////                            IMAGE OPERATORS                            ////////
    ////////                                                                       ////////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Rescales the {@linkplain #image} such that it uses 8 bits. If the image already uses 8 bits,
     * then this method does nothing. Otherwise this method computes the minimum and maximum values
     * for each band, {@linkplain RescaleDescriptor rescale} them in the range {@code [0 .. 255]}
     * and force the resulting image to {@link DataBuffer#TYPE_BYTE TYPE_BYTE}.
     *
     * @return This ImageWorker
     *
     * @see #isBytes
     * @see RescaleDescriptor
     */
    public final ImageWorker rescaleToBytes() {

        if (isIndexed()) {
            throw new UnsupportedOperationException(
                    "Rescaling not yet implemented for IndexColorModel.");
        }
        
        if (isBytes()) {
            // Already using bytes - nothing to do.
            return this;
        }
        final double[][] extrema = getExtremas();
        final int length = extrema[0].length;
        final double[] scale  = new double[length];
        final double[] offset = new double[length];
        boolean computeRescale=false;
        for (int i=0; i<length; i++) {
            final double delta = extrema[1][i] - extrema[0][i];
            if(Math.abs(delta)>1E-6			//maximum and minimum does not coincide
            		&&
              ((extrema[1][i]-255>1E-6)		//the maximum is greater than 255
              		|| 
              (extrema[0][i]<-1E-6)))		//the minimum is smaller than 0
            {
            	// we need to rescale
            	computeRescale=true;
            	
            	// rescale factors
                scale [i] = 255 / delta;
                offset[i] = -scale[i] * extrema[0][i];            	
            }
            else
            {
            	// we do not rescale explicitly bu in case we have to, we relay on the clamping capabilities of the format operator
                scale [i] = 1;
                offset[i] = 0; 
            }
        }
        final RenderingHints hints = getRenderingHints(DataBuffer.TYPE_BYTE);
        if(computeRescale)
	        image = RescaleDescriptor.create(
	                image,      // The source image.
	                scale,      // The per-band constants to multiply by.
	                offset,     // The per-band offsets to be added.
	                hints);     // The rendering hints.
        else
        	image= FormatDescriptor.create(
        			image,      			// The source image.
        			DataBuffer.TYPE_BYTE,	// The destination image data type (BYTE)
        			hints);					// The rendering hints.
        invalidateStatistics(); // Extremas are no longer valid.

        // All post conditions for this method contract.
        assert isBytes();
        return this;
    }

    /**
     * Reduces the color model to {@linkplain IndexColorModel index color model}.
     * If the current {@linkplain #image} already uses an
     * {@linkplain IndexColorModel index color model}, then this method do
     * nothing. Otherwise, the current implementation performs a ditering on the
     * original color model. Note that this operation loose the alpha channel.
     * <p>
     * This for the moment should work only with opaque images, with non opaque
     * images we just remove the alpha band in order to build an
     * {@link IndexColorModel}. This is one because in general it could be very
     * difficult to decide the final transparency for each pixel given the
     * complexity if the algorithms for obtaining an {@link IndexColorModel}.
     * <p>
     * If an {@link IndexColorModel} with a single transparency index is enough
     * for you, we advise you to take a look at
     * {@link #forceIndexColorModelForGIF(boolean)} methdo.
     *
     * @see #isIndexed
     * @see #forceBitmaskIndexColorModel
     * @see #forceIndexColorModelForGIF
     * @see OrderedDitherDescriptor
     */
    public final ImageWorker forceIndexColorModel(final boolean error) {
        final ColorModel cm = image.getColorModel();
        if (cm instanceof IndexColorModel) {
            // Already an index color model - nothing to do.
            return this;
        }
        tileCacheEnabled(false);
		if (getNumBands() % 2 == 0)
			retainBands(getNumBands() - 1);
		forceColorSpaceRGB();
		final RenderingHints hints = getRenderingHints();
		if (error) {
			// color quantization
			// final RenderedOp temp = ColorQuantizerDescriptor.create(image,
			// ColorQuantizerDescriptor.MEDIANCUT, new Integer(254),
			// new Integer(200), null, new Integer(1), new Integer(1),
			// getRenderingHints());
			// final ImageLayout layout= new ImageLayout();
			// layout.setColorModel(temp.getColorModel());
			// hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));

			// error diffusion
			final KernelJAI ditherMask = KernelJAI.ERROR_FILTER_FLOYD_STEINBERG;
			final LookupTableJAI colorMap = ColorCube.BYTE_496;
			// (LookupTableJAI) temp.getProperty("JAI.LookupTable");
			image = ErrorDiffusionDescriptor.create(image, colorMap,
					ditherMask, hints);
		} else {
			// ordered dither
			final KernelJAI[] ditherMask = KernelJAI.DITHER_MASK_443;
			final ColorCube colorMap = ColorCube.BYTE_496;
			image = OrderedDitherDescriptor.create(image, colorMap, ditherMask,
					hints);
		}
        tileCacheEnabled(true);
        invalidateStatistics();

        // All post conditions for this method contract.
        assert isIndexed();
        return this;
    }

    /**
     * Reduces the color model to {@linkplain IndexColorModel index color model}
     * with {@linkplain Transparency#OPAQUE opaque} or
     * {@linkplain Transparency#BITMASK bitmask} transparency. If the current
     * {@linkplain #image} already uses a suitable color model, then this method
     * do nothing.
     *
     * @return this {@link ImageWorker}.
     *
     * @see #isIndexed
     * @see #isTranslucent
     * @see #forceIndexColorModel
     * @see #forceIndexColorModelForGIF
     */
    public final ImageWorker forceBitmaskIndexColorModel() {
        forceBitmaskIndexColorModel(getTransparentPixel(), true);
        return this;
    }

    /**
     * Reduces the color model to {@linkplain IndexColorModel index color model}
     * with {@linkplain Transparency#OPAQUE opaque} or
     * {@linkplain Transparency#BITMASK bitmask} transparency. If the current
     * {@linkplain #image} already uses a suitable color model, then this method
     * do nothing.
     *
     * @param suggestedTransparent
     *            A suggested pixel index to define as the transparent pixel. *
     * @param errorDiffusion
     *            Tells if I should use {@link ErrorDiffusionDescriptor} or
     *            {@link OrderedDitherDescriptor} JAi operations. errorDiffusion
     * @return this {@link ImageWorker}.
     *
     * @see #isIndexed
     * @see #isTranslucent
     * @see #forceIndexColorModel
     * @see #forceIndexColorModelForGIF
     */
    public final ImageWorker forceBitmaskIndexColorModel(int suggestedTransparent, final boolean errorDiffusion) {
        final ColorModel cm = image.getColorModel();
        if (cm instanceof IndexColorModel) {
            final IndexColorModel oldCM = (IndexColorModel) cm;
            switch (oldCM.getTransparency()) {
                case Transparency.OPAQUE: {
                    // Suitable color model. There is nothing to do.
                    return this;
                }
                case Transparency.BITMASK: {
                    if (oldCM.getTransparentPixel() == suggestedTransparent) {
                        // Suitable color model. There is nothing to do.
                        return this;
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            
            // check if we already have a pixel fully transparent
            final int transparentPixel = ColorUtilities.getTransparentPixel(oldCM);
            /*
             * The index color model need to be replaced. Creates a lookup table
             * mapping from the old pixel values to new pixels values, with
             * transparent colors mapped to the new transparent pixel value. The
             * lookup table uses TYPE_BYTE or TYPE_USHORT, which are the two
             * only types supported by IndexColorModel.
             */
            final int mapSize = oldCM.getMapSize();
            if(transparentPixel<0)
            	suggestedTransparent=suggestedTransparent<=mapSize?mapSize+1:suggestedTransparent;
            else
            	suggestedTransparent=transparentPixel;
            final int newSize = Math.max(mapSize, suggestedTransparent);
            final int newPixelSize=ColorUtilities.getBitCount(newSize);
            if(newPixelSize>16)
            	throw new IllegalArgumentException("Unable to create index color model with more than 65536 elements");
            final LookupTableJAI lookupTable;
            if (newPixelSize <= 8) {
                final byte[] table = new byte[mapSize];
                for (int i=0; i<mapSize; i++) {
                    table[i] = (byte) ((oldCM.getAlpha(i) == 0) ? suggestedTransparent : i);
                }
                lookupTable = new LookupTableJAI(table);
            } else {
                final short[] table = new short[mapSize];
                for (int i=0; i<mapSize; i++) {
                    table[i] = (short) ((oldCM.getAlpha(i) == 0) ? suggestedTransparent : i);
                }
                lookupTable = new LookupTableJAI(table, true);
            }
            /*
             * Now we need to perform the look up transformation. First of all
             * we create the new color model with a bitmask transparency using
             * the transparency index specified to this method. Then we perform
             * the lookup operation in order to prepare for the gif image.
             */
            final byte[][] rgb = new byte[3][newSize];
            oldCM.getReds  (rgb[0]);
            oldCM.getGreens(rgb[1]);
            oldCM.getBlues (rgb[2]);
            final IndexColorModel newCM = new IndexColorModel(newPixelSize,newSize, rgb[0], rgb[1], rgb[2], suggestedTransparent);
            final RenderingHints hints = getRenderingHints();
            final ImageLayout layout = getImageLayout(hints);
            layout.setColorModel(newCM);
            // we should not transform on color map here
            hints.put(JAI.KEY_TRANSFORM_ON_COLORMAP,Boolean.FALSE);            
            hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
            image = LookupDescriptor.create(image, lookupTable, hints);
            
            //workaround bug in Lookup since it looks like it is switching 255 and 254
            image=FormatDescriptor.create(image, image.getSampleModel().getDataType(), hints);
        } else {
        	// force component color model first
        	forceComponentColorModel(true);
			/*
             * The image is not indexed. 
             */
            if (cm.hasAlpha()) {
            	//Getting the alpha channel.
                tileCacheEnabled(false);
                int numBands = getNumBands();
                final RenderingHints hints = getRenderingHints();

                final RenderedOp alphaChannel = BandSelectDescriptor.create(image,
                                new int[] { --numBands }, hints);
                retainBands(numBands);
                forceIndexColorModel(errorDiffusion);
                tileCacheEnabled(true);
                

                /*
                 * Adding transparency if needed, which means using the alpha
                 * channel to build a new color model. The method call below implies
                 * 'forceColorSpaceRGB()' and 'forceIndexColorModel()' method calls.
                 */
                addTransparencyToIndexColorModel(alphaChannel, false, suggestedTransparent, errorDiffusion);
            }
            else
                forceIndexColorModel(errorDiffusion);
        }
        // All post conditions for this method contract.
        assert isIndexed();
        assert !isTranslucent();
        return this;
    }

    /**
     * Converts the image to a GIF-compliant image. This method has been created
     * in order to convert the input image to a form that is compatible with the
     * GIF model. It first remove the information about transparency since the
     * error diffusion and the error dither operations are unable to process
     * images with more than 3 bands. Afterwards the image is processed with an
     * error diffusion operator in order to reduce the number of bands from 3 to
     * 1 and the number of color to 216. A suitable layout is used for the final
     * image via the {@linkplain #getRenderingHints rendering hints} in order to
     * take into account the different layout model for the final image.
     * <p>
     * <strong>Tip:</strong> For optimizing writing GIF, we need to create the
     * image untiled. This can be done by invoking
     * <code>{@linkplain #setRenderingHint setRenderingHint}({@linkplain
     * #TILING_ALLOWED}, Boolean.FALSE)</code>
     * first.
     *
     * @param errorDiffusion
     *            Tells if I should use {@link ErrorDiffusionDescriptor} or
     *            {@link OrderedDitherDescriptor} JAi operations.
     *
     * @return this {@link ImageWorker}.
     *
     * @see #isIndexed
     * @see #forceIndexColorModel
     * @see #forceBitmaskIndexColorModel
     */
    public final ImageWorker forceIndexColorModelForGIF(final boolean errorDiffusion) {
        /*
         * Checking the color model to see if we need to convert it back to
         * color model. We might also need to reformat the image in order to get
         * it to 8 bits samples.
         */
        ColorModel cm= image.getColorModel();
        if (cm instanceof PackedColorModel) {
            forceComponentColorModel();
            cm= image.getColorModel();
        }
        if(!(cm instanceof IndexColorModel))
            rescaleToBytes();
        /*
         * Getting the alpha channel and separating from the others bands. If
         * the initial image had no alpha channel (more specifically, if it is
         * either opaque or a bitmask) we proceed without doing anything since
         * it seems that GIF encoder in such a case works fine. If we need to
         * create a bitmask, we will use the last index value allowed (255) as
         * the transparent pixel value.
         */
        if (isTranslucent()) {
            forceBitmaskIndexColorModel(255, errorDiffusion);
        } else {
            forceIndexColorModel(errorDiffusion);
        }
        // All post conditions for this method contract.
        // assert isBytes(); // could be less, like binary, 4 bits
        assert isIndexed();
        assert !isTranslucent();
        return this;
    }

    /**
     * Reformats the {@linkplain ColorModel color model} to a
     * {@linkplain ComponentColorModel component color model} preserving
     * transparency. This is used especially in order to go from
     * {@link PackedColorModel} to {@link ComponentColorModel}, which seems to
     * be well accepted from {@code PNGEncoder} and {@code TIFFEncoder}.
     * <p>
     * This code is adapted from jai-interests mailing list archive.
     *
     * @return this {@link ImageWorker}.
     *
     * @see FormatDescriptor
     */
    public final ImageWorker forceComponentColorModel() {
        return forceComponentColorModel(false);
    }
    
    /**
     * Reformats the {@linkplain ColorModel color model} to a
     * {@linkplain ComponentColorModel component color model} preserving
     * transparency. This is used especially in order to go from
     * {@link PackedColorModel} to {@link ComponentColorModel}, which seems to
     * be well accepted from {@code PNGEncoder} and {@code TIFFEncoder}.
     * <p>
     * This code is adapted from jai-interests mailing list archive.
     *
     * @param checkTransparent
     * @param optimizeGray
     * 
     * @return this {@link ImageWorker}.
     *
     * @see FormatDescriptor
     */
    public final ImageWorker forceComponentColorModel(boolean checkTransparent,boolean optimizeGray) {
    	final ColorModel cm = image.getColorModel();
        if (cm instanceof ComponentColorModel) {
            // Already an component color model - nothing to do.
            return this;
        }
        // shortcut for index color model
        if (cm instanceof IndexColorModel) {
            final IndexColorModel icm = (IndexColorModel) cm;
            final SampleModel sm   = this.image.getSampleModel();
            final int datatype	   = sm.getDataType();
            final boolean gray     = ColorUtilities.isGrayPalette(icm, checkTransparent)&optimizeGray;
            final boolean alpha    = icm.hasAlpha();
            /*
             * If the image is grayscale, retain only the needed bands.
             *
             */            
            final int     numDestinationBands = gray?(alpha?2:1):(alpha?4:3);
            LookupTableJAI lut = null;

            switch (datatype) {
			case DataBuffer.TYPE_BYTE:
				{
		            final byte    data[][] = new byte[numDestinationBands][icm.getMapSize()];
		            icm.getReds  (data[0]);
		            if(numDestinationBands>=2)
		            	// remember to optimize for grayscale images
		            	if(!gray)
		            		icm.getGreens(data[1]);
		            	else
		            		icm.getAlphas(data[1]);
		            if(numDestinationBands>=3)
		            	icm.getBlues (data[2]);
		            if (numDestinationBands == 4) {
		                icm.getAlphas(data[3]);
		            }		
		            lut = new LookupTableJAI(data);

				}
				break;
				
			case DataBuffer.TYPE_USHORT:
			{
				final int mapSize=icm.getMapSize();
				final short    data[][] = new short[numDestinationBands][mapSize];
				for(int i=0;i<mapSize;i++)
				{
					data[0][i]=(short) icm.getRed(i);
					if(numDestinationBands>=2)
		            	// remember to optimize for grayscale images
		            	if(!gray)
		            		data[1][i]=(short)icm.getGreen(i);
		            	else
		            		data[1][i]=(short)icm.getAlpha(i);
		            if(numDestinationBands>=3)
		            	data[2][i]=(short)icm.getBlue(i);
		            if (numDestinationBands == 4) {
		            	data[3][i]=(short)icm.getAlpha(i);
		            }
				}
				lut = new LookupTableJAI(data,datatype==DataBuffer.TYPE_USHORT);	
	            
	            				
			}				
				break;				

			default:
				throw new IllegalArgumentException(
								Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"datatype", datatype));
			}

            //did we initialized the LUT?
            if(lut==null)
            	throw new IllegalStateException(
						Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"lut"));
            /*
             * Get the default hints, which usually contains only informations
             * about tiling. If the user override the rendering hints with an
             * explicit color model, keep the user's choice.
             */
            final RenderingHints hints = (RenderingHints) getRenderingHints();
            final ImageLayout layout;
            final Object candidate = hints.get(JAI.KEY_IMAGE_LAYOUT);
            if (candidate instanceof ImageLayout) {
            	layout= (ImageLayout) candidate;
            }
            else
            {
            	layout= new ImageLayout(image);
            	hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));
            }
            
            int[] bits= new int[numDestinationBands];
            //bits per component
            for(int i=0;i<numDestinationBands;i++)
            	bits[i]=sm.getSampleSize(i);            
            final ComponentColorModel destinationColorModel=new ComponentColorModel(
            		numDestinationBands>=3?ColorSpace.getInstance(ColorSpace.CS_sRGB):ColorSpace.getInstance(ColorSpace.CS_GRAY),
            				bits,
                    		alpha,
                    		cm.isAlphaPremultiplied(),
                    		alpha?Transparency.TRANSLUCENT:Transparency.OPAQUE,
                    		datatype);
            final SampleModel destinationSampleModel=destinationColorModel.createCompatibleSampleModel(image.getWidth(), image.getHeight());
            layout.setColorModel(destinationColorModel);
            layout.setSampleModel(destinationSampleModel);
            image = LookupDescriptor.create(image, lut, hints);

        } else {
            // Most of the code adapted from jai-interests is in 'getRenderingHints(int)'.
            final int type = (cm instanceof DirectColorModel) ?DataBuffer.TYPE_BYTE : image.getSampleModel().getTransferType();
            final RenderingHints hints = getRenderingHints(type);
//            image=ColorConvertDescriptor.create(image, RIFUtil.getImageLayoutHint(hints).getColorModel(null), hints);
            image = FormatDescriptor.create(image, type, hints);;
        } 
        invalidateStatistics();

        // All post conditions for this method contract.
        assert image.getColorModel() instanceof ComponentColorModel;
        return this;
    }

    /**
     * Reformats the {@linkplain ColorModel color model} to a
     * {@linkplain ComponentColorModel component color model} preserving
     * transparency. This is used especially in order to go from
     * {@link PackedColorModel} to {@link ComponentColorModel}, which seems to
     * be well accepted from {@code PNGEncoder} and {@code TIFFEncoder}.
     * <p>
     * This code is adapted from jai-interests mailing list archive.
     *
     * @param checkTransparent
     *            tells this method to not consider fully transparent pixels
     *            when optimizing grayscale palettes.
     *
     * @return this {@link ImageWorker}.
     *
     * @see FormatDescriptor
     */
    public final ImageWorker forceComponentColorModel(boolean checkTransparent) {
        return forceComponentColorModel(checkTransparent,true);
    }

    /**
     * Forces the {@linkplain #image} color model to the
     * {@linkplain ColorSpace#CS_sRGB RGB color space}. If the current color
     * space is already of {@linkplain ColorSpace#TYPE_RGB RGB type}, then this
     * method does nothing. This operation may loose the alpha channel.
     *
     * @return this {@link ImageWorker}.
     *
     * @see #isColorSpaceRGB
     * @see ColorConvertDescriptor
     */
    public final ImageWorker forceColorSpaceRGB() {
        if (!isColorSpaceRGB()) {
            final ColorModel cm = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false,
                    Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
            image = ColorConvertDescriptor.create(image, cm, getRenderingHints());
            invalidateStatistics();
        }
        // All post conditions for this method contract.
        assert isColorSpaceRGB();
        return this;
    }

	/**
	 * Forces the {@linkplain #image} color model to the
	 *  IHS color space. If the current color
	 * space is already of IHS type, then this
	 * method does nothing. This operation may loose the alpha channel.
	 *
	 * @return this {@link ImageWorker}.
	 *
	 * @see ColorConvertDescriptor
	 */
	public final ImageWorker forceColorSpaceIHS() {
		if (!(image.getColorModel().getColorSpace() instanceof IHSColorSpace)) {
			forceComponentColorModel();
			 // Create a ColorModel to convert the image to IHS.
			final IHSColorSpace ihs = IHSColorSpace.getInstance();
			final int numBits=image.getColorModel().getComponentSize(0);
			final ColorModel ihsColorModel = new ComponentColorModel(ihs, new int[] {
					numBits, numBits, numBits }, false, false, Transparency.OPAQUE,
					image.getSampleModel().getDataType());
			// Create a ParameterBlock for the conversion.
			final ParameterBlock pb = new ParameterBlock();
			pb.addSource(image);
			pb.add(ihsColorModel);
			// Do the conversion.
			image = JAI.create("colorconvert", pb);
			invalidateStatistics();
		}

		// All post conditions for this method contract.
		assert image.getColorModel().getColorSpace() instanceof IHSColorSpace;
		return this;
	}

	/**
	 * Add the bands to the Component Color Model
	 *
	 * @param writeband
	 *            number of bands after the bandmerge.
	 *
	 * @return this {@link ImageWorker}.
	 *
	 */
	public final ImageWorker bandMerge(int writeband) {
		ParameterBlock pb = new ParameterBlock();

		PlanarImage sourceImage = PlanarImage
				.wrapRenderedImage(getRenderedImage());

		int numBands = sourceImage.getSampleModel().getNumBands();

		// getting first band
		final RenderedImage firstBand = JAI.create("bandSelect", sourceImage,
				new int[] { 0 });

		// adding to the image
		final int length=writeband-numBands;
		for (int i = 0; i < length; i++) {
			pb.removeParameters();
			pb.removeSources();

			pb.addSource(sourceImage);
			pb.addSource(firstBand);
			sourceImage = JAI.create("bandmerge", pb);

			pb.removeParameters();
			pb.removeSources();
		}

		image = (RenderedImage) sourceImage;
		invalidateStatistics();

		// All post conditions for this method contract.
		assert image.getSampleModel().getNumBands() == writeband;
		return this;
	}

	/**
	 * Perform a BandMerge operation between the underlying image and the provided one.
	 *
	 * @param image
	 *            to merge with the underlying one.
	 * @param before <code>true</code> if we want to use first the provided image, <code>false</code>
	 *  otherwise.
	 *
	 * @return this {@link ImageWorker}.
	 *
	 */
	public final ImageWorker addBand(RenderedImage image, boolean before) {

		this.image = before?
						BandMergeDescriptor.create(image, this.image, this.getRenderingHints()):
						BandMergeDescriptor.create(this.image, image, this.getRenderingHints());
		invalidateStatistics();

		return this;
	}

	/**
     * Forces the {@linkplain #image} color model to the
     * {@linkplain ColorSpace#CS_GRAY GRAYScale color space}. If the current
     * color space is already of {@linkplain ColorSpace#TYPE_GRAY  type}, then
     * this method does nothing.
     *
     * @return this {@link ImageWorker}.
     *
     * @see #isColorSpaceGRAYScale
     * @see ColorConvertDescriptor
     */
    public final ImageWorker forceColorSpaceGRAYScale() {
        if (!isColorSpaceRGB()) {
            final ColorModel cm = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false,
                    Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
            image = ColorConvertDescriptor.create(image, cm, getRenderingHints());
            invalidateStatistics();
        }
        // All post conditions for this method contract.
        assert isColorSpaceGRAYScale();
        return this;
    }

    /**
     * Creates an image which represents approximatively the intensity of
     * {@linkplain #image}. The result is always a single-banded image. If the
     * image uses an {@linkplain IHSColorSpace IHS color space}, then this
     * method just {@linkplain #retainFirstBand retain the first band} without
     * any further processing. Otherwise, this method performs a simple
     * {@linkplain BandCombineDescriptor band combine} operation on the
     * {@linkplain #image} in order to come up with a simple estimation of the
     * intensity of the image based on the average value of the color
     * components. It is worthwhile to note that the alpha band is stripped from
     * the image.
     *
     * @return this {@link ImageWorker}.
     *
     * @see BandCombineDescriptor
     */
    public final ImageWorker intensity() {
        /*
         * If the color model already uses a IHS color space or a Gray color
         * space, keep only the intensity band. Otherwise, we need a component
         * color model to be sure to understand what we are doing.
         */
        ColorModel cm = image.getColorModel();
        final ColorSpace cs = cm.getColorSpace();
        if (cs.getType() == ColorSpace.TYPE_GRAY || cs instanceof IHSColorSpace) {
            retainFirstBand();
            return this;
        }
        if (cm instanceof IndexColorModel) {
            forceComponentColorModel();
            cm = image.getColorModel();
        }

        // Number of color componenents
        final int numBands = cm.getNumComponents();
        final int numColorBands = cm.getNumColorComponents();
        final boolean hasAlpha = cm.hasAlpha();

        // One band, nothing to combine.
        if (numBands == 1) {
            return this;
        }
        // One band plus alpha, let's remove alpha.
        if (numColorBands == 1 && hasAlpha) {
            retainFirstBand();
            return this;
		}
		//remove the alpha band
		if (numColorBands != numBands) {
			this.retainBands(numBands);
		}
		/*
         * We have more than one band. Note that there is no need to remove the
         * alpha band before to apply the "bandCombine" operation - it is
         * suffisient to let the coefficient for the alpha band to the 0 value.
         */
        final double[][] coeff = new double[1][numBands + 1];
        Arrays.fill(coeff[0], 0, numColorBands, 1.0 / numColorBands);
        image = BandCombineDescriptor.create(image, coeff, getRenderingHints());
        invalidateStatistics();

        // All post conditions for this method contract.
        assert getNumBands() == 1;
        return this;
    }

    /**
     * Retains inconditionnaly the first band of {@linkplain #image}. All other
     * bands (if any) are discarted without any further processing.
     *
     * @return this {@link ImageWorker}.
     *
     * @see #getNumBands
     * @see #retainBands
     * @see BandSelectDescriptor
     */
    public final ImageWorker retainFirstBand() {
        retainBands(1);

        // All post conditions for this method contract.
        assert getNumBands() == 1;
        return this;
    }

    /**
     * Retains unconditionally the last band of {@linkplain #image}. All other
     * bands (if any) are discarded without any further processing.
     *
     * <p>
     * It is worth to point out that we use the true number of bands rather than
     * the number of color components. This means that if we apply this method on 
     * a colormapped image we get back the image itself untouched since it originally
     * contains 1 band although the color components are 3 or 4 as per the attached colormap.
     * 
     * @return this {@link ImageWorker}.
     *
     * @see #getNumBands
     * @see #retainBands
     * @see BandSelectDescriptor
     */
    public final ImageWorker retainLastBand() {
        final int band = getNumBands() - 1;
        if (band != 0) {
            retainBands(new int[] {band});
        }
        // All post conditions for this method contract.
        assert getNumBands() == 1;
        return this;
    }

    /**
     * Retains inconditionnaly the first {@code numBands} of {@linkplain #image}.
     * All other bands (if any) are discarted without any further processing.
     * This method does nothing if the current {@linkplain #image} does not have
     * a greater amount of bands than {@code numBands}.
     *
     * @param numBands
     *            the number of bands to retain.
     * @return this {@link ImageWorker}.
     *
     * @see #getNumBands
     * @see #retainFirstBand
     * @see BandSelectDescriptor
     */
    public final ImageWorker retainBands(final int numBands) {
        if (numBands <= 0) {
            throw new IndexOutOfBoundsException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                    "numBands", numBands));
        }
        if (getNumBands() > numBands) {
            final int[] bands = new int[numBands];
            for (int i=0; i<bands.length; i++) {
                bands[i] = i;
            }
            image = BandSelectDescriptor.create(image, bands, getRenderingHints());
        }

        // All post conditions for this method contract.
        assert getNumBands() <= numBands;
        return this;
    }

    /**
     * Retains inconditionnaly certain bands of {@linkplain #image}. All other
     * bands (if any) are discarded without any further processing.
     *
     * @param bands
     *            the bands to retain.
     * @return this {@link ImageWorker}.
     *
     * @see #getNumBands
     * @see #retainFirstBand
     * @see BandSelectDescriptor
     */
    public final ImageWorker retainBands(final int[] bands) {
        image = BandSelectDescriptor.create(image, bands, getRenderingHints());
        return this;
    }

    /**
     * Formats the underlying image to the provided data type.
     *
     * @param dataType
     *            to be used for this {@link FormatDescriptor} operation.
     * @return this {@link ImageWorker}
     */
    public final ImageWorker format(final int dataType) {
        image = FormatDescriptor.create(image, dataType, getRenderingHints());

        // All post conditions for this method contract.
        assert image.getSampleModel().getDataType() == dataType;
        return this;
    }

    /**
     * Binarizes the {@linkplain #image}. If the image is multi-bands, then
     * this method first computes an estimation of its
     * {@linkplain #intensity intensity}. Then, the threshold value is set
     * halfway between the minimal and maximal values found in the image.
     *
     * @return this {@link ImageWorker}.
     *
     * @see #isBinary
     * @see #binarize(double)
     * @see #binarize(int,int)
     * @see BinarizeDescriptor
     */
    public final ImageWorker binarize() {
        binarize(Double.NaN);

        // All post conditions for this method contract.
        assert isBinary();
        return this;
    }

    /**
     * Binarizes the {@linkplain #image}. If the image is already binarized,
     * then this method does nothing.
     *
     * @param threshold
     *            The threshold value.
     * @return this {@link ImageWorker}.
     *
     * @see #isBinary
     * @see #binarize()
     * @see #binarize(int,int)
     * @see BinarizeDescriptor
     */
    public final ImageWorker binarize(double threshold) {
        // If the image is already binary and the threshold is >=1 then there is no work to do.
        if (!isBinary()) {
            if (Double.isNaN(threshold)) {
                if (getNumBands() != 1) {
                    tileCacheEnabled(false);
                    intensity();
                    tileCacheEnabled(true);
                }
                final double[][] extremas = getExtremas();
                threshold = 0.5 * (extremas[0][0] + extremas[1][0]);
            }
            final RenderingHints hints = getRenderingHints();
            image = BinarizeDescriptor.create(image, threshold, hints);
            invalidateStatistics();
        }
        // All post conditions for this method contract.
        assert isBinary();
        return this;
    }

    /**
     * Binarizes the {@linkplain #image} (if not already done) and replace all 0
     * values by {@code value0} and all 1 values by {@code value1}. If the
     * image should be binarized using a custom threshold value (instead of the
     * automatic one), invoke {@link #binarize(double)} explicitly before this
     * method.
     *
     * @return this {@link ImageWorker}.
     * @see #isBinary
     * @see #binarize()
     * @see #binarize(double)
     * @see BinarizeDescriptor
     * @see LookupDescriptor
     */
    public final ImageWorker binarize(final int value0, final int value1) {
        tileCacheEnabled(false);
        binarize();
        tileCacheEnabled(true);
        final LookupTableJAI table;
        final int min = Math.min(value0, value1);
        if (min >= 0) {
            final int max = Math.max(value0, value1);
            if (max < 256) {
                table = new LookupTableJAI(new byte[] {(byte) value0, (byte) value1});
            } else if (max < 65536) {
                table = new LookupTableJAI(new short[] {(short) value0, (short) value1}, true);
            } else {
                table = new LookupTableJAI(new int[] {value0, value1});
            }
        } else {
            table = new LookupTableJAI(new int[] { value0, value1 });
        }
        image = LookupDescriptor.create(image, table, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Replaces all occurences of the given color (usually opaque) by a fully transparent color.
     * Currents implementation supports image backed by any {@link IndexColorModel}, or by
     * {@link ComponentColorModel} with {@link DataBuffer#TYPE_BYTE TYPE_BYTE}. More types
     * may be added in future GeoTools versions.
     *
     * @param transparentColor The color to make transparent.
     * @return this image worker.
     *
     * @throws IllegalStateException if the current {@linkplain #image} has an unsupported color
     *         model.
     */
    public final ImageWorker makeColorTransparent(final Color transparentColor)
            throws IllegalStateException
    {
        if (transparentColor == null) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "transparentColor"));
        }
        final ColorModel cm = image.getColorModel();
        if (cm instanceof IndexColorModel) {
            return maskIndexColorModel(transparentColor);
        } else if (cm instanceof ComponentColorModel) {
            switch (image.getSampleModel().getDataType()) {
                case DataBuffer.TYPE_BYTE: {
                    return maskComponentColorModelByte(transparentColor);
                }
                // Add other types here if we support them...
            }
        }
        throw new IllegalStateException(Errors.format(ErrorKeys.UNSUPPORTED_DATA_TYPE));
    }

    /**
     * For an image backed by an {@link IndexColorModel}, replaces all occurences of the given
     * color (usually opaque) by a fully transparent color.
     *
     * @param transparentColor The color to make transparent.
     * @return this image worker.
     *
     */
    private final ImageWorker maskIndexColorModel(final Color transparentColor) {
        assert image.getColorModel() instanceof IndexColorModel;

        // Gets informations about the provided images.
        IndexColorModel cm = (IndexColorModel) image.getColorModel();
        final int numComponents     = cm.getNumComponents();
        int       transparency      = cm.getTransparency();
        int       transparencyIndex = cm.getTransparentPixel();
        final int mapSize           = cm.getMapSize();
        final int transparentRGB    = transparentColor.getRGB() & 0x00FFFFFF;
        /*
         * Optimization in case of Transparency.BITMASK.
         * If the color we want to use as the fully transparent one is the same
         * that is actually used as the transparent color, we leave doing nothing.
         */
        if (transparency == Transparency.BITMASK && transparencyIndex != -1) {
            int transpColor = cm.getRGB(transparencyIndex) & 0x00FFFFFF;
            if (transpColor == transparentRGB) {
                return this;
            }
        }
        /*
         * Find the index of the specified color. Most of the time, the color should appears only
         * once, which will leads us to a BITMASK image. However we allows more occurences, which
         * will leads us to a TRANSLUCENT image.
         */
        final List<Integer> transparentPixelsIndexes = new ArrayList<Integer>();
        for (int i=0; i<mapSize; i++) {
            // Gets the color for this pixel removing the alpha information.
            final int color = cm.getRGB(i) & 0xFFFFFF;
            if (transparentRGB == color) {
                transparentPixelsIndexes.add(i);
                if (Transparency.BITMASK == transparency) {
                    break;
                }
            }
        }
        final int found = transparentPixelsIndexes.size();
        if (found == 1) {
            // Transparent color found.
            transparencyIndex = transparentPixelsIndexes.get(0);
            transparency = Transparency.BITMASK;
        } else if (found == 0) {
            return this;
        } else {
            transparencyIndex = -1;
            transparency = Transparency.TRANSLUCENT;
        }

        // Prepare the new ColorModel.
        // Get the old map and update it as needed.
        final byte rgb[][] = new byte[4][mapSize];
        cm.getReds  (rgb[0]);
        cm.getGreens(rgb[1]);
        cm.getBlues (rgb[2]);
        if (numComponents == 4) {
            cm.getAlphas(rgb[3]);
        } else {
            Arrays.fill(rgb[3], (byte) 255);
        }
        if (transparency != Transparency.TRANSLUCENT) {
            cm = new IndexColorModel(cm.getPixelSize(), mapSize,
                    rgb[0], rgb[1], rgb[2], transparencyIndex);
        } else {
            for (int k=0; k<found; k++) {
                rgb[3][transparentPixelsIndexes.get(k)] = (byte) 0;
            }
            cm = new IndexColorModel(cm.getPixelSize(), mapSize,
                    rgb[0], rgb[1], rgb[2], rgb[3]);
        }

        // Format the input image.
        final ImageLayout layout = new ImageLayout(image);
        layout.setColorModel(cm);
        final RenderingHints hints = getRenderingHints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
        image = FormatDescriptor.create(image, image.getSampleModel().getDataType(), hints);
        invalidateStatistics();
        return this;
    }

    /**
     * For an image backed by an {@link ComponentColorModel}, replaces all occurences
     * of the given color (usually opaque) by a fully transparent color.
     *
     * @param transparentColor The color to make transparent.
     * @return this image worker.
     *
     *
     * Current implementation invokes a lot of JAI operations:
     *
     *     "BandSelect" --> "Lookup" --> "BandCombine" --> "Extrema" --> "Binarize" -->
     *     "Format" --> "BandSelect" (one more time) --> "Multiply" --> "BandMerge".
     *
     * I would expect more speed and memory efficiency by writing our own JAI operation (PointOp
     * subclass) doing that in one step. It would also be more deterministic (our "binarize" method
     * depends on statistics on pixel values) and avoid unwanted side-effect like turning black
     * color (RGB = 0,0,0) to transparent one. It would also be easier to maintain I believe.
     */
    private final ImageWorker maskComponentColorModelByte(final Color transparentColor) {
        assert image.getColorModel() instanceof ComponentColorModel;
        assert image.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE;
        /*
         * Prepares the look up table for the source image.
         * Remember what follows which is taken from the JAI programming guide.
         *
         *     "The lookup operation performs a general table lookup on a rendered or renderable
         *      image. The destination image is obtained by passing the source image through the
         *      lookup table. The source image may be single- or multi-banded of data types byte,
         *      ushort, short, or int. The lookup table may be single- or multi-banded of any JAI-
         *      supported data types.
         *
         *      The destination image must have the same data type as the lookup table, and its
         *      number of bands is determined based on the number of bands of the source and the
         *      table. If the source is single-banded, the destination has the same number of bands
         *      as the lookup table; otherwise, the destination has the same number of bands as the
         *      source.
         *
         *      If either the source or the table is single-banded and the other one is multibanded,
         *      the single band is applied to every band of the multi-banded object. If both are
         *      multi-banded, their corresponding bands are matched up."
         *
         * A final annotation, if we have an input image with transparency we just DROP it since
         * we want to re-add it using the supplied color as the mask for transparency.
         */

        /*
         * In case of a gray color model we can do everything in one step by expanding
         * the color model to get one more band directly which is the alpha band itself.
         *
         * For a multiband image the lookup is applied to each band separately.
         * This means that we cannot control directly the image as a whole but
         * we need first to interact with the single bands then to combine the
         * result into a single band that will provide us with the alpha band.
         */
        int numBands = image.getSampleModel().getNumBands();
        final int numColorBands = image.getColorModel().getNumColorComponents();
        final RenderingHints hints = getRenderingHints();
        if (numColorBands != numBands) {
            // Typically, numColorBands will be equals to numBands-1.
            final int[] opaqueBands = new int[numColorBands];
            for (int i=0; i<opaqueBands.length; i++) {
                opaqueBands[i] = i;
            }
            image = BandSelectDescriptor.create(image, opaqueBands, hints);
            numBands=numColorBands;
        }
        
        // now prepare the lookups
        final byte[][] tableData = new byte[numColorBands][256];
        final boolean singleStep = (numColorBands == 1);
        if (singleStep) {
            final byte[] data = tableData[0];
            Arrays.fill(data, (byte) 255);
            data[transparentColor.getRed()] = 0;
        } else {
            switch (numColorBands) {
                case 3: 
                	Arrays.fill(tableData[2], (byte) 255);
                	tableData[2][transparentColor.getBlue() ] = 0; // fall through
                	
                case 2: 
                	Arrays.fill(tableData[1], (byte) 255);
                	tableData[1][transparentColor.getGreen()] = 0; // fall through
                	
                case 1: 
                	Arrays.fill(tableData[0], (byte)   255);
                	tableData[0][transparentColor.getRed()  ] = 0; // fall through
                	
                case 0: break;
            }
        }
        // Create a LookupTableJAI object to be used with the "lookup" operator.
        LookupTableJAI table = new LookupTableJAI(tableData);
        // Do the lookup operation.
        // we should not transform on color map here
        hints.put(JAI.KEY_TRANSFORM_ON_COLORMAP,Boolean.FALSE);
        PlanarImage luImage = LookupDescriptor.create(image, table, hints);
        
        /*
         * Now that we have performed the lookup operation we have to remember
         * what we stated here above.
         *
         * If the input image is multiband we will get a multiband image as
         * the output of the lookup operation hence we need to perform some form
         * of band combination to get the alpha band out of the lookup image.
         *
         * The way we wanted things to be done is by exploiting the clamping
         * behavior that kicks in when we do sums and the like on pixels and
         * we overcome the maximum value allowed by the DataBufer DataType.
         */
        if (!singleStep) {
            // We simply add the three generated bands together in order to get the right.
            final double[][] matrix = new double[1][4];
            // Values at index 0,1,2 are set to 1.0, value at index 3 is left to 0.
            Arrays.fill(matrix[0], 0, 3, 1.0);
            luImage = BandCombineDescriptor.create(luImage, matrix, hints);
        }
        image = BandMergeDescriptor.create(image, luImage, hints);
        
        invalidateStatistics();
        return this;
    }

    /**
     * Inverts the pixel values of the {@linkplain #image}.
     *
     * @see InvertDescriptor
     */
    public final ImageWorker invert() {
        image = InvertDescriptor.create(image, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Applies the specified mask over the current {@linkplain #image}. The mask should be
     * {@linkplain #binarize() binarized} - if it is not, this method will do it itself.
     * Then, for every pixels in the mask with value equals to {@code maskValue}, the
     * corresponding pixel in the {@linkplain #image} will be set to the specified
     * {@code newValue}.
     * <p>
     * <strong>Note:</strong> current implementation force the color model to an
     * {@linkplain IndexColorModel indexed} one. Future versions may avoid this change.
     *
     * @param mask
     *            The mask to apply, as a {@linkplain #binarize() binarized} image.
     * @param maskValue
     *            The mask value to search for ({@code false} for 0 or {@code true} for 1).
     * @param newValue
     *            The new value for every pixels in {@linkplain #image}
     *            corresponding to {@code maskValue} in the mask.
     *
     * @return this {@link ImageWorker}.
     *
     * @todo This now should work only if {@code newValue} is 255
     *       and {@code maskValue} is {@code false}.
     */
	public final ImageWorker mask(RenderedImage mask, final boolean maskValue,
			int newValue) {

		/*
		 * Make sure that the underlying image is indexed.
		 */
		tileCacheEnabled(false);
		forceIndexColorModel(true);
		final RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE,
				null);

		/*
		 * special case for newValue == 255 && !maskValue.
		 */
		if (newValue == 255 && !maskValue) {
			/*
			 * Build a lookup table in order to make the transparent pixels
			 * equal to 255 and all the others equal to 0.
			 *
			 */
			final byte[] lutData = new byte[256];
			// mapping all the non-transparent pixels to opaque
			Arrays.fill(lutData, (byte) 0);
			// for transparent pixels
			lutData[0] = (byte) 255;
			final LookupTableJAI lut = new LookupTableJAI(lutData);
			mask = LookupDescriptor.create(mask, lut, hints);

			/*
			 * Adding to the other image exploiting the implict clamping
			 *
			 */
			image = AddDescriptor.create(image, mask, getRenderingHints());
			tileCacheEnabled(true);
			invalidateStatistics();
			return this;
		} else {
			// general case

			// it has to be binary
			if (!isBinary())
				binarize();

			// now if we mask with 1 we have to invert the mask
			if (maskValue)
				mask = NotDescriptor.create(mask, new RenderingHints(
						JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));

			// and with the image to zero the interested pixels
			tileCacheEnabled(false);
			image = AndDescriptor.create(mask, image, getRenderingHints());

			// add the new value to the mask
			mask = AddConstDescriptor.create(mask, new double[] { newValue },
					new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL,
							Boolean.FALSE));

			// add the mask to the image to mask with the new value
			image = AddDescriptor.create(mask, image, getRenderingHints());
			tileCacheEnabled(true);
			invalidateStatistics();
			return this;
		}

	}

    /**
     * Takes two rendered or renderable source images, and adds every pair of pixels, one from
     * each source image of the corresponding position and band. See JAI {@link AddDescriptor}
     * for details.
     *
     * @param renderedImage
     *            the {@link RenderedImage} to be added to this {@link ImageWorker}.
     * @return this {@link ImageWorker}.
     *
     * @see AddDescriptor
     */
    public final ImageWorker addImage(final RenderedImage renderedImage) {
        image = AddDescriptor.create(image, renderedImage, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Takes one rendered or renderable image and an array of double constants, and multiplies
     * every pixel of the same band of the source by the constant from the corresponding array
     * entry. See JAI {@link MultiplyConstDescriptor} for details.
     *
     * @param inValues
     *            The constants to be multiplied.
     * @return this {@link ImageWorker}.
     *
     * @see MultiplyConstDescriptor
     */
    public final ImageWorker multiplyConst(double[] inValues) {
        image = MultiplyConstDescriptor.create(image, inValues, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Takes one rendered or renderable image and an array of integer constants, and performs a
     * bit-wise logical "xor" between every pixel in the same band of the source and the constant
     * from the corresponding array entry. See JAI {@link XorConstDescriptor} for details.
     *
     * @see XorConstDescriptor
     */
    public final ImageWorker xorConst(int[] values) {
        image = XorConstDescriptor.create(image, values, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Adds transparency to a preexisting image whose color model is
     * {@linkplain IndexColorModel index color model}. For all pixels with the
     * value {@code false} in the specified transparency mask, the corresponding
     * pixel in the {@linkplain #image} is set to the transparent pixel value.
     * All other pixels are left unchanged.
     *
     * @param alphaChannel
     *            The mask to apply as a {@linkplain #binarize() binarized} image.
     * @param errorDiffusion
     *            Tells if I should use {@link ErrorDiffusionDescriptor} or
     *            {@link OrderedDitherDescriptor} JAi operations.
     * @return this {@link ImageWorker}.
     *
     * @see #isTranslucent
     * @see #forceBitmaskIndexColorModel
     */
    public ImageWorker addTransparencyToIndexColorModel(final RenderedImage alphaChannel,
                                                        final boolean errorDiffusion)
    {
        addTransparencyToIndexColorModel(alphaChannel, true, getTransparentPixel(), errorDiffusion);
        return this;
    }

    /**
     * Adds transparency to a preexisting image whose color model is {@linkplain IndexColorModel
     * index color model}. First, this method creates a new index color model with the specified
     * {@code transparent} pixel, if needed (this method may skip this step if the specified pixel
     * is already transparent. Then for all pixels with the value {@code false} in the specified
     * transparency mask, the corresponding pixel in the {@linkplain #image} is set to that
     * transparent value. All other pixels are left unchanged.
     *
     * @param alphaChannel
     *            The mask to apply as a {@linkplain #binarize() binarized} image.
     * @param translucent
     *            {@code true} if {@linkplain Transparency#TRANSLUCENT translucent} images are
     *            allowed, or {@code false} if the resulting images must be a
     *            {@linkplain Transparency#BITMASK bitmask}.
     * @param transparent
     *            The value for transparent pixels, to be given to every pixels in the
     *            {@linkplain #image} corresponding to {@code false} in the mask. The
     *            special value {@code -1} maps to the last pixel value allowed for the
     *            {@linkplain IndexedColorModel indexed color model}.
     * @param errorDiffusion
     *            Tells if I should use {@link ErrorDiffusionDescriptor} or
     *            {@link OrderedDitherDescriptor} JAi operations.
     *
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker addTransparencyToIndexColorModel(final RenderedImage alphaChannel,
            final boolean translucent, int transparent, final boolean errorDiffusion)
    {
        tileCacheEnabled(false);
        forceIndexColorModel(errorDiffusion);
        tileCacheEnabled(true);
        /*
         * Prepares hints and layout to use for mask operations. A color model
         * hint will be set only if the block below is executed.
         */
        final ImageWorker worker = fork(image);
        final RenderingHints hints = worker.getRenderingHints();
        /*
         * Gets the index color model. If the specified 'transparent' value is not fully
         * transparent, replaces the color model by a new one with the transparent pixel
         * defined. NOTE: the "transparent &= (1 << pixelSize) - 1" instruction below is
         * a safety for making sure that the transparent index value can hold in the amount
         * of bits allowed for this color model (the mapSize value may not use all bits).
         * It works as expected with the -1 special value. It also make sure that
         * "transparent + 1" do not exeed the maximum map size allowed.
         */
        final boolean forceBitmask;
        final IndexColorModel oldCM = (IndexColorModel) image.getColorModel();
        final int pixelSize = oldCM.getPixelSize();
        transparent &= (1 << pixelSize) - 1;
        forceBitmask = !translucent && oldCM.getTransparency() == Transparency.TRANSLUCENT;
        if (forceBitmask || oldCM.getTransparentPixel() != transparent) {
            final int mapSize = Math.max(oldCM.getMapSize(), transparent + 1);
            final byte[][] RGBA = new byte[translucent ? 4 : 3][mapSize];
            // Note: we might use less that 256 values.
            oldCM.getReds  (RGBA[0]);
            oldCM.getGreens(RGBA[1]);
            oldCM.getBlues (RGBA[2]);
            final IndexColorModel newCM;
            if (translucent) {
                oldCM.getAlphas(RGBA[3]);
                RGBA[3][transparent] = 0;
                newCM = new IndexColorModel(pixelSize, mapSize, RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
            } else {
                newCM = new IndexColorModel(pixelSize, mapSize, RGBA[0], RGBA[1], RGBA[2], transparent);
            }
            /*
             * Set the color model hint.
             */
            final ImageLayout layout = getImageLayout(hints);
            layout.setColorModel(newCM);
            worker.setRenderingHint(JAI.KEY_IMAGE_LAYOUT, layout);
        }
        /*
         * Applies the mask, maybe with a color model change.
         */
        worker.setRenderingHint(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE);
        worker.mask(alphaChannel, false, transparent);
        image = worker.image;
        invalidateStatistics();

        // All post conditions for this method contract.
        assert isIndexed();
        assert translucent || !isTranslucent() : translucent;
        assert ((IndexColorModel) image.getColorModel()).getAlpha(transparent) == 0;
        return this;
    }

    /**
     * If the was not already tiled, tile it. Note that no tiling will be done
     * if 'getRenderingHints()' failed to suggest a tile size. This method is
     * for internal use by {@link #write} methods only.
     *
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker tile() {
        final RenderingHints hints = getRenderingHints();
        final ImageLayout layout = getImageLayout(hints);
        if (layout.isValid(ImageLayout.TILE_WIDTH_MASK) ||
            layout.isValid(ImageLayout.TILE_HEIGHT_MASK))
        {
            final int type = image.getSampleModel().getDataType();
            image = FormatDescriptor.create(image, type, hints);
        }
        return this;
    }
    
    /**
     * Applies the specified opacity to the image by either adding an alpha band, or modifying
     * the existing one by multiplication
     * 
     * @param opacity The opacity to be applied, between 0 and 1 
     * 
     * @return this {@link ImageWorker}.
     */
    public ImageWorker applyOpacity(float opacity) {
        RenderedImage result;
        ColorModel colorModel = image.getColorModel();
        
        // if it's an index color model we can just recompute the palette
        // and replace it
        if(colorModel instanceof IndexColorModel) {
            // grab the original palette
            IndexColorModel index = (IndexColorModel) colorModel;
            byte[] reds = new byte[index.getMapSize()]; 
            byte[] greens = new byte[index.getMapSize()];
            byte[] blues = new byte[index.getMapSize()];
            byte[] alphas = new byte[index.getMapSize()];
            index.getReds(reds);
            index.getGreens(greens);
            index.getBlues(blues);
            index.getAlphas(alphas);

            // multiply the alphas by opacity
            final int transparentPixel = index.getTransparentPixel();
            for (int i = 0; i < alphas.length; i++) {
                alphas[i] = (byte) Math.round((0xFF & alphas[i]) * opacity);
                if(i == transparentPixel) {
                    alphas[i] = 0;
                }
            }
            
            // build a new palette
            IndexColorModel newColorModel = new IndexColorModel(index.getPixelSize(), index.getMapSize(), 
                    reds, greens, blues, alphas);
            LookupTableJAI table = buildOpacityLookupTable(0, 1, -1);
            ImageLayout layout = new ImageLayout(image);
            layout.setColorModel(newColorModel);
            RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
            result = LookupDescriptor.create(image, table, hints);
        } else {
            // not indexed, then make sure it's some sort of component color model or turn it into one
            RenderedImage expanded;
            if(!(colorModel instanceof ComponentColorModel)) {
                expanded = new ImageWorker(image).forceComponentColorModel().getRenderedImage();
            } else {
                expanded = image;
            }
            
            // do we have to add the alpha band or it's there and we need to change it?
            
            if(!expanded.getColorModel().hasAlpha()) {
                // we just need to add it, so first build a constant image with the same structure
                // as the original image
                byte alpha = (byte) Math.round(255 * opacity);
                ImageLayout layout = new ImageLayout(image.getMinX(), image.getMinY(), 
                        image.getWidth(), image.getHeight());
                RenderedOp alphaBand = ConstantDescriptor.create(
                        (float) image.getWidth(), (float) image.getHeight(), 
                        new Byte[] {new Byte(alpha)}, new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
                
                result = BandMergeDescriptor.create(expanded, alphaBand, null);
            } else {
                // we need to transform the existing, we'll use a lookup
                final int bands = expanded.getSampleModel().getNumBands();
                int alphaBand = bands - 1;
                LookupTableJAI table = buildOpacityLookupTable(opacity, bands, alphaBand);
                result = LookupDescriptor.create(expanded, table, null);
            }
        }
        
        image = result;
        return this;
    }


    /**
     * Builds a lookup table that is the identity on all bands but the alpha one, where
     * the opacity is applied
     * @param opacity
     * @param bands
     * @param alphaBand
     * @return
     */
    LookupTableJAI buildOpacityLookupTable(float opacity, final int bands, int alphaBand) {
        byte[][] matrix = new byte[bands][256];
        for (int band = 0; band < matrix.length; band++) {
            if(band == alphaBand) {
                for (int i = 0; i < 256; i++) {
                    matrix[band][i] = (byte) Math.round(i * opacity);
                }
            } else {
                for (int i = 0; i < 256; i++) {
                    matrix[band][i] = (byte) i;
                }
            }
        }
        LookupTableJAI table = new LookupTableJAI(matrix);
        return table;
    }

    /**
     * Writes the {@linkplain #image} to the specified file. This method differs
     * from {@link ImageIO#write(String,File)} in a number of ways:
     * <p>
     * <ul>
     *   <li>The {@linkplain ImageWriter image writer} to use is inferred from the file
     *       extension.</li>
     *   <li>If the image writer accepts {@link File} objects as input, then the {@code file}
     *       argument is given directly without creating an {@link ImageOutputStream} object.
     *       This is important for some formats like HDF, which work <em>only</em> with files.</li>
     *   <li>If the {@linkplain #image} is not tiled, then it is tiled prior to be written.</li>
     *   <li>If some special processing is needed for a given format, then the corresponding method
     *       is invoked. Example: {@link #forceIndexColorModelForGIF}.</li>
     * </ul>
     *
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker write(final File output) throws IOException {
        final String filename = output.getName();
        final int dot = filename.lastIndexOf('.');
        if (dot < 0) {
            throw new IIOException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
        }
        final String extension = filename.substring(dot + 1).trim();
        write(output, ImageIO.getImageWritersBySuffix(extension));
        return this;
    }

    /**
     * Writes outs the image contained into this {@link ImageWorker} as a PNG
     * using the provided destination, compression and compression rate.
     * <p>
     * The destination object can be anything providing that we have an
     * {@link ImageOutputStreamSpi} that recognizes it.
     *
     * @param destination
     *            where to write the internal {@link #image} as a PNG.
     * @param compression
     *            algorithm.
     * @param compressionRate
     *            percentage of compression.
     * @param nativeAcc
     *            should we use native acceleration.
     * @param paletted
     *            should we write the png as 8 bits?
     * @return this {@link ImageWorker}.
     * @throws IOException
     *             In case an error occurs during the search for an
     *             {@link ImageOutputStream} or during the eoncding process.
     *
     * @todo Current code doesn't check if the writer already accepts the provided destination.
     *       It wraps it in a {@link ImageOutputStream} inconditionnaly.
     */
    public final void writePNG(final Object destination, final String compression,
                               final float compressionRate, final boolean nativeAcc,
                               final boolean paletted)
            throws IOException
    {
        // Reformatting this image for PNG.
        if (!paletted) {
            forceComponentColorModel();
        }else{
        	forceIndexColorModelForGIF(true);
        }
        if(LOGGER.isLoggable(Level.FINER))
			LOGGER.finer("Encoded input image for png writer");

        // Getting a writer.
        if(LOGGER.isLoggable(Level.FINER))
			LOGGER.finer("Getting a writer");
        final Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("PNG");
        if (!it.hasNext()) {
            throw new IllegalStateException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
        }
        ImageWriter writer = it.next();

        // Getting a stream.
        if(LOGGER.isLoggable(Level.FINER))
			LOGGER.finer("Setting write parameters for this writer");
        ImageWriteParam iwp = null;
        final ImageOutputStream memOutStream = ImageIOExt.createImageOutputStream(image, destination);
        if(memOutStream==null)
        	throw new IIOException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"stream"));        
        if (nativeAcc && writer.getClass().getName().equals(
                "com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter"))
        {
            // Compressing with native.
        	if(LOGGER.isLoggable(Level.FINER))
    			LOGGER.finer("Writer is native");
            iwp = writer.getDefaultWriteParam();
            // Define compression mode
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            // best compression
            iwp.setCompressionType(compression);
            // we can control quality here
            iwp.setCompressionQuality(compressionRate);
            // destination image type
            iwp.setDestinationType(new ImageTypeSpecifier(image.getColorModel(), image.getSampleModel()));
        } else {
            // Compressing with pure Java.
            // pure java from native
            if (!nativeAcc && it.hasNext() && writer.getClass().getName().equals(
                    "com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter"))
            {
                writer = it.next();
            }
            if(LOGGER.isLoggable(Level.FINER))
    			LOGGER.finer("Writer is NOT native");
            // Instantiating PNGImageWriteParam
            iwp = new PNGImageWriteParam();
            // Define compression mode
            iwp.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
        }
        if(LOGGER.isLoggable(Level.FINER))
			LOGGER.finer("About to write png image");
        try{
	        writer.setOutput(memOutStream);
	        writer.write(null, new IIOImage(image, null, null), iwp);
        }
        finally{
        	try{
        		writer.dispose();
        	}catch (Throwable e) {
        		if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
			}
        	try{
        		memOutStream.close();
        	}catch (Throwable e) {
        		if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
			}        	
            
            
        }
    }

    /**
     * Writes outs the image contained into this {@link ImageWorker} as a GIF
     * using the provided destination, compression and compression rate.
     * <p>
     * It is worth to point out that the only compressions algorithm availaible
     * with the jdk {@link GIFImageWriter} is "LZW" while the compression rates
     * have to be confined between 0 and 1. AN acceptable values is usally 0.75f.
     * <p>
     * The destination object can be anything providing that we have an
     * {@link ImageOutputStreamSpi} that recognizes it.
     *
     * @param destination
     *            where to write the internal {@link #image} as a gif.
     * @param compression
     *            The name of compression algorithm.
     * @param compressionRate
     *            percentage of compression, as a number between 0 and 1.
     * @return this {@link ImageWorker}.
     * @throws IOException
     *             In case an error occurs during the search for an
     *             {@link ImageOutputStream} or during the eoncding process.
     *
     * @see #forceIndexColorModelForGIF(boolean)
     */
    public final ImageWorker writeGIF(final Object destination,
                                      final String compression,
                                      final float  compressionRate)
            throws IOException
    {
        forceIndexColorModelForGIF(true);
        final IIORegistry registry = IIORegistry.getDefaultInstance();
        Iterator<ImageWriterSpi> it = registry.getServiceProviders(ImageWriterSpi.class, true);
        ImageWriterSpi spi = null;
        while (it.hasNext()) {
            final ImageWriterSpi candidate = it.next();
            if (containsFormatName(candidate.getFormatNames(), "gif")) {
                if (spi == null) {
                    spi = candidate;
                } else {
                    final String name = candidate.getClass().getName();
                    if (name.equals("com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi")) {
                        spi = candidate;
                        break;
                    }
                }
            }
        }
        if (spi == null) {
            throw new IIOException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
        }
        final ImageOutputStream stream = ImageIOExt.createImageOutputStream(image, destination);
        if(stream==null)
        	throw new IIOException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"stream"));
        final ImageWriter       writer = spi.createWriterInstance();
        final ImageWriteParam   param  = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionType(compression);
        param.setCompressionQuality(compressionRate);

        try{
	        writer.setOutput(stream);
	        writer.write(null, new IIOImage(image, null, null), param);
        }finally{
        	try{
        		stream.close();
        	}catch (Throwable e) {
				if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
			}
        	try{
    	        writer.dispose();
        	}catch (Throwable e) {
				if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
			}        	
        }
        return this;
    }

    /**
     * Writes outs the image contained into this {@link ImageWorker} as a JPEG
     * using the provided destination , compression and compression rate.
     * <p>
     * The destination object can be anything providing that we have an
     * {@link ImageOutputStreamSpi} that recognizes it.
     *
     * @param destination
     *            where to write the internal {@link #image} as a JPEG.
     * @param compression
     *            algorithm.
     * @param compressionRate
     *            percentage of compression.
     * @param nativeAcc
     *            should we use native acceleration.
     * @return this {@link ImageWorker}.
     * @throws IOException
     *             In case an error occurs during the search for an
     *             {@link ImageOutputStream} or during the eoncding process.
     */
    public final void writeJPEG(final Object destination, final String compression,
                                final float compressionRate, final boolean nativeAcc)
            throws IOException
    {
        // Reformatting this image for jpeg.
        if(LOGGER.isLoggable(Level.FINER))
        	LOGGER.finer("Encoding input image to write out as JPEG.");

        // go to component color model if needed
        ColorModel cm = image.getColorModel();        
        final boolean hasAlpha = cm.hasAlpha();
        forceComponentColorModel();  
        cm = image.getColorModel();
        
        // rescale to 8 bit
        rescaleToBytes();
        cm = image.getColorModel();
        
        // remove transparent band
        final int numBands = image.getSampleModel().getNumBands();
        if (hasAlpha) {
            retainBands(numBands - 1);
        }
        

        // Getting a writer.
        if(LOGGER.isLoggable(Level.FINER))
        	LOGGER.finer("Getting a JPEG writer and configuring it.");
        final Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("JPEG");
        if (!it.hasNext()) {
            throw new IllegalStateException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
        }
        ImageWriter writer = it.next();
        if (!nativeAcc && writer.getClass().getName().equals(
                "com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter"))
        {
            writer = it.next();
        }     
        if((!PackageUtil.isCodecLibAvailable()||!(writer.getOriginatingProvider() instanceof CLibJPEGImageWriterSpi))
        		&&
        		compression.equals("JPEG-LS")
        	)
        		throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"compression","JPEG-LS"));
        

        // Compression is available on both lib
        final ImageWriteParam iwp = writer.getDefaultWriteParam();
        final ImageOutputStream outStream = ImageIOExt.createImageOutputStream(image, destination);
        if(outStream==null)
        	throw new IIOException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"stream"));
         
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionType(compression);        // Lossy compression.
        iwp.setCompressionQuality(compressionRate); // We can control quality here.
        if (iwp instanceof JPEGImageWriteParam) {
            final JPEGImageWriteParam param = (JPEGImageWriteParam) iwp;
            param.setOptimizeHuffmanTables(true);
            try {
                param.setProgressiveMode(JPEGImageWriteParam.MODE_DEFAULT);
            } catch (UnsupportedOperationException e) {
                throw (IOException) new IOException().initCause(e);
                // TODO: inline cause when we will be allowed to target Java 6.
            }
        }

        if(LOGGER.isLoggable(Level.FINER))
        	LOGGER.finer("Writing out...");
        
        try{

            writer.setOutput(outStream);
        	// the JDK writer has problems with images that do not  start at minx==miny==0
            // while the clib writer has issues with tiled images
            if ((!nativeAcc && (image.getMinX() != 0 || image.getMinY() != 0)) ||
                    (nativeAcc && (image.getNumXTiles() > 1 || image.getNumYTiles() > 1))) {
                 	final BufferedImage finalImage= new BufferedImage(
                 			image.getColorModel(),
                			((WritableRaster)image.getData()).createWritableTranslatedChild(0,0),
                 			image.getColorModel().isAlphaPremultiplied(),null);
                 	
                    writer.write(null, new IIOImage(finalImage, null, null), iwp);
             } else {
            	writer.write(null, new IIOImage(image, null, null), iwp);
             }
        } finally{
        	try{
        		writer.dispose();
        	}catch (Throwable e) {
        		if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
			}
        	try{
        		outStream.close();
        	}catch (Throwable e) {
        		if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
			}        	
            
            
        }
       

    }

    
    /**
     * Writes outs the image contained into this {@link ImageWorker} as a TIFF
     * using the provided destination, compression and compression rate and basic tiling information
     * <p>
     * The destination object can be anything providing that we have an
     * {@link ImageOutputStreamSpi} that recognizes it.
     *
     * @param destination
     *            where to write the internal {@link #image} as a TIFF.
     * @param compression
     *            algorithm.
     * @param compressionRate
     *            percentage of compression.
     * @param nativeAcc
     *            should we use native acceleration.
     * @param tileSizeX
     *            tile size x direction (or -1 if tiling is not desired)
     * @param tileSizeY
     *            tile size y direction (or -1 if tiling is not desired)
     * @return this {@link ImageWorker}.
     * @throws IOException
     *             In case an error occurs during the search for an
     *             {@link ImageOutputStream} or during the eoncding process.
     */
    public final void writeTIFF(final Object destination, final String compression,
                                final float compressionRate, final int tileSizeX, final int tileSizeY)
            throws IOException
    {
        // Reformatting this image for jpeg.
        if(LOGGER.isLoggable(Level.FINER))
            LOGGER.finer("Encoding input image to write out as TIFF.");

        // Getting a writer.
        if(LOGGER.isLoggable(Level.FINER))
            LOGGER.finer("Getting a TIFF writer and configuring it.");
        final Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("TIFF");
        if (!it.hasNext()) {
            throw new IllegalStateException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
        }
        ImageWriter writer = it.next();
        
        final ImageWriteParam iwp = writer.getDefaultWriteParam();
        final ImageOutputStream outStream = ImageIOExt.createImageOutputStream(image, destination);
        if(outStream==null)
            throw new IIOException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"stream"));
        
        if(compression != null) {
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionType(compression);        
            iwp.setCompressionQuality(compressionRate); // We can control quality here.
        } else {
            iwp.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
        }
        if(tileSizeX > 0 && tileSizeY > 0) {
            iwp.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setTiling(tileSizeX, tileSizeY, 0, 0);
        }

        if(LOGGER.isLoggable(Level.FINER))
            LOGGER.finer("Writing out...");
        
        try{

            writer.setOutput(outStream);
            writer.write(null, new IIOImage(image, null, null), iwp);
        } finally{
            try{
                writer.dispose();
            }catch (Throwable e) {
                if(LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
            }
            try{
                outStream.close();
            }catch (Throwable e) {
                if(LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(Level.FINEST,e.getLocalizedMessage(),e);
            }           
            
            
        }
       

    }
    
    /**
     * Performs an affine transform on the image, applying optimization such as affine removal in
     * case the affine is an identity, affine merging if the affine is applied on top of another
     * affine, and using optimized operations for integer translates
     * 
     * @param tx
     * @param interpolation
     * @param bgValues
     * @return
     */
    public ImageWorker affine(AffineTransform tx, Interpolation interpolation, double[] bgValues) {
        // identity elimination -> check the tx params against the image size to see if
        // any if likely to actually move the image by at least one pixel
        int size = Math.max(image.getWidth(), image.getHeight());
        boolean hasScaleX = Math.abs(tx.getScaleX() - 1) * size > RS_EPS;
        boolean hasScaleY = Math.abs(tx.getScaleY() - 1) * size > RS_EPS;
        boolean hasShearX = Math.abs(tx.getShearX()) * size > RS_EPS;
        boolean hasShearY = Math.abs(tx.getShearY()) * size > RS_EPS;
        boolean hasTranslateX = Math.abs(tx.getTranslateX()) > RS_EPS; 
        boolean hasTranslateY = Math.abs(tx.getTranslateY()) > RS_EPS;
        if(!hasScaleX && !hasScaleY && !hasShearX && !hasShearY && !hasTranslateX && !hasTranslateY) {
            return this;
        }
        
        // apply defaults to allow for comparisong
        ParameterListDescriptor pld = new AffineDescriptor().getParameterListDescriptor(RenderedRegistryMode.MODE_NAME);
        if(interpolation == null) {
            interpolation = (Interpolation) pld.getParamDefaultValue("interpolation");
        }
        if(bgValues == null) {
            bgValues = (double[]) pld.getParamDefaultValue("backgroundValues");
        }
        
        // affine over affine/scale?
        RenderedImage source = image;
        if(image instanceof RenderedOp) {
            RenderedOp op = (RenderedOp) image;
            if("Affine".equals(op.getOperationName())) {
                ParameterBlock paramBlock = op.getParameterBlock();
                RenderedImage sSource = paramBlock.getRenderedSource(0);

                AffineTransform sTx = (AffineTransform) paramBlock.getObjectParameter(0);
                Interpolation sInterp = (Interpolation) paramBlock.getObjectParameter(1);
                double[] sBgValues = (double[]) paramBlock.getObjectParameter(2);
                
                if((sInterp == interpolation  && Arrays.equals(sBgValues, bgValues))) {
                    // we can replace it
                    AffineTransform concat = new AffineTransform(tx);
                    concat.concatenate(sTx);
                    tx = concat;
                    source = sSource;
                }
            } else if("Scale".equals(op.getOperationName())) {
                ParameterBlock paramBlock = op.getParameterBlock();
                RenderedImage sSource = paramBlock.getRenderedSource(0);

                float xScale = paramBlock.getFloatParameter(0);
                float yScale = paramBlock.getFloatParameter(1);
                float xTrans = paramBlock.getFloatParameter(2);
                float yTrans = paramBlock.getFloatParameter(3);
                Interpolation sInterp = (Interpolation)paramBlock.getObjectParameter(4);
                
                if(sInterp == interpolation) {
                    // we can replace it
                    AffineTransform concat = new AffineTransform(tx);
                    concat.concatenate(new AffineTransform(xScale, 0, 0, yScale, xTrans, yTrans));
                    tx = concat;
                    source = sSource;
                }
            }
        }

        // check again params, we might have combined two transformations sets
        hasScaleX = Math.abs(tx.getScaleX() - 1) * size > RS_EPS;
        hasScaleY = Math.abs(tx.getScaleY() - 1) * size > RS_EPS;
        hasShearX = Math.abs(tx.getShearX()) * size > RS_EPS;
        hasShearY = Math.abs(tx.getShearY()) * size > RS_EPS;
        hasTranslateX = Math.abs(tx.getTranslateX()) > RS_EPS; 
        hasTranslateY = Math.abs(tx.getTranslateY()) > RS_EPS;
        boolean intTranslateX = Math.abs((tx.getTranslateX() - Math.round(tx.getTranslateX()))) < RS_EPS;
        boolean intTranslateY = Math.abs((tx.getTranslateY() - Math.round(tx.getTranslateY()))) < RS_EPS;
        
        // did it become a identity after the combination?
        if(!hasScaleX && !hasScaleY && !hasShearX && !hasShearY && !hasTranslateX && !hasTranslateY) {
            return this;
        }

        if (!hasShearX && !hasShearY) {
            if(!hasScaleX && !hasScaleY && intTranslateX && intTranslateY) {
                // this will do an integer translate, but to get there we need to remove the image layout
                Hints localHints = new Hints(commonHints);
                localHints.remove(JAI.KEY_IMAGE_LAYOUT);
                image = ScaleDescriptor.create(source, 1.0f, 1.0f,
                        (float) Math.round(tx.getTranslateX()), (float) Math.round(tx.getTranslateY()), interpolation,
                        localHints);
            } else {
                // generic scale
                image = ScaleDescriptor.create(source, (float) tx.getScaleX(), (float) tx.getScaleY(),
                        (float) tx.getTranslateX(), (float) tx.getTranslateY(), interpolation,
                        commonHints);
            }
        } else {
            image = AffineDescriptor.create(source, tx, interpolation, bgValues, commonHints);
        }
        return this;
    }
    
    /**
     * Crops the image to the specified bounds. Will use an internal operation that ensures the tile
     * cache and tile scheduler hints are used, and will perform operation elimination in case the
     * crop is doing nothing, or in case the crop is performed over another crop
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public ImageWorker crop(float x, float y, float width, float height) {
        // no op elimination
        if(image.getMinX() == x && image.getMinY() == y 
                && image.getWidth() == width && image.getHeight() == height) {
            return this;
        }
        
        // crop over crop
        RenderedImage source = image;
        if(image instanceof RenderedOp) {
            RenderedOp op = (RenderedOp) image;
            if("Crop".equals(op.getOperationName()) || "GTCrop".equals(op.getOperationName())) {
                ParameterBlock paramBlock = op.getParameterBlock();
                source = paramBlock.getRenderedSource(0);

                float sx = (float) paramBlock.getFloatParameter(0);
                float sy = (float) paramBlock.getFloatParameter(1);
                float sWidth = (float) paramBlock.getFloatParameter(2);
                float sHeight = (float) paramBlock.getFloatParameter(3);
                
                // merge the two (just need to sum the two origins)
                if(sx > 0) {
                    x = sx + x;
                }
                if(sy > 0) {
                    y = sy + y;
                }
                
            }
        }
        
        image = GTCropDescriptor.create(source, x, y, width, height, commonHints);

        return this;
    }


    /**
     * Writes the {@linkplain #image} to the specified output, trying all
     * encoders in the specified iterator in the iteration order.
     *
     * @return this {@link ImageWorker}.
     */
    private ImageWorker write(final Object output, final Iterator<? extends ImageWriter> encoders)
            throws IOException
    {
        if (encoders != null) {
            while (encoders.hasNext()) {
                final ImageWriter writer = encoders.next();
                final ImageWriterSpi spi = writer.getOriginatingProvider();
                final Class<?>[] outputTypes;
                if (spi == null) {
                    outputTypes = ImageWriterSpi.STANDARD_OUTPUT_TYPE;
                } else {
                    /*
                     * If the encoder is for some format handled in a special way (e.g. GIF), apply
                     * the required operation. Note that invoking the same method many time (e.g.
                     * "forceIndexColorModelForGIF", which could occurs if there is more than one
                     * GIF encoder registered) should not hurt - all method invocation after the
                     * first one should be no-op.
                     */
                    final String[] formats = spi.getFormatNames();
                    if (containsFormatName(formats, "gif")) {
                        forceIndexColorModelForGIF(true);
                    } else {
                        tile();
                    }
                    if (!spi.canEncodeImage(image)) {
                        continue;
                    }
                    outputTypes = spi.getOutputTypes();
                }
                /*
                 * Now try to set the output directly (if possible), or as an ImageOutputStream if
                 * the encoder doesn't accept directly the specified output. Note that some formats
                 * like HDF may not support ImageOutputStream.
                 */
                final ImageOutputStream stream;
                if (acceptInputType(outputTypes, output.getClass())) {
                    writer.setOutput(output);
                    stream = null;
                } else if (acceptInputType(outputTypes, ImageOutputStream.class)) {
                    stream = ImageIOExt.createImageOutputStream(image, output);
                    writer.setOutput(stream);
                } else {
                    continue;
                }
                /*
                 * Now saves the image.
                 */
                writer.write(image);
                writer.dispose();
                if (stream != null) {
                    stream.close();
                }
                return this;
            }
        }
        throw new IIOException(Errors.format(ErrorKeys.NO_IMAGE_WRITER));
    }

    /**
     * Returns {@code true} if the specified array contains the specified type.
     */
    private static boolean acceptInputType(final Class<?>[] types, final Class<?> searchFor) {
        for (int i=types.length; --i >= 0;) {
            if (searchFor.isAssignableFrom(types[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if the specified array contains the specified string.
     */
    private static boolean containsFormatName(final String[] formats, final String searchFor) {
        for (int i=formats.length; --i >= 0;) {
            if (searchFor.equalsIgnoreCase(formats[i])) {
                return true;
            }
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                       ////////
    ////////                             DEBUGING HELP                             ////////
    ////////                                                                       ////////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Shows the current {@linkplain #image} in a window together with the operation chain as a
     * {@linkplain javax.swing.JTree tree}. This method is provided mostly for debugging purpose.
     * This method requires the {@code gt2-widgets-swing.jar} file in the classpath.
     *
     * @throws HeadlessException
     *             if {@code gt2-widgets-swing.jar} is not on the classpath, or
     *             if AWT can't create the window components.
     * @return this {@link ImageWorker}.
     *
     * @see org.geotools.gui.swing.image.OperationTreeBrowser#show(RenderedImage)
     */
    public final ImageWorker show() throws HeadlessException {
        /*
         * Uses reflection because the "gt2-widgets-swing.jar" dependency is optional and may not
         * be available in the classpath. All the complicated stuff below is simply doing this call:
         *
         *     OperationTreeBrowser.show(image);
         *
         * Tip: The @see tag in the above javadoc can be used as a check for the existence
         *      of class and method referenced below. Check for the javadoc warnings.
         */
        final Class<?> c;
        try {
            c = Class.forName("org.geotools.gui.swing.image.OperationTreeBrowser");
        } catch (ClassNotFoundException cause) {
            final HeadlessException e;
            e = new HeadlessException("The \"gt2-widgets-swing.jar\" file is required.");
            e.initCause(cause);
            throw e;
        }
        try {
            c.getMethod("show", new Class[] {RenderedImage.class}).invoke(null, new Object[] {image});
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new AssertionError(e);
        } catch (Exception e) {
            /*
             * ClassNotFoundException may be expected, but all other kinds of
             * checked exceptions (and they are numerous...) are errors.
             */
            throw new AssertionError(e);
        }
        return this;
    }

    /**
     * Loads the image from the specified file, and {@linkplain #show display}
     * it in a window. This method is mostly as a convenient way to test
     * operation chains. This method can be invoked from the command line. If an
     * optional {@code -operation} argument is provided, the Java method (one of
     * the image operations provided in this class) immediately following it is
     * executed. Example:
     *
     * <blockquote><pre>
     * java org.geotools.image.ImageWorker -operation binarize &lt;var&gt;&lt;filename&gt;&lt;/var&gt;
     * </pre></blockquote>
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        final String operation = arguments.getOptionalString("-operation");
        args = arguments.getRemainingArguments(1);
        if (args.length != 0) try {
            final ImageWorker worker = new ImageWorker(new File(args[0]));
            // Force usage of tile cache for every operations, including intermediate steps.
            worker.setRenderingHint(JAI.KEY_TILE_CACHE, JAI.getDefaultInstance().getTileCache());
            if (operation != null) {
                worker.getClass().getMethod(operation, (Class[]) null).invoke(worker, (Object[]) null);
            }
            /*
             * TIP: Tests operations here (before the call to 'show()'), if wanted.
             */
            worker.show();
        } catch (FileNotFoundException e) {
            arguments.printSummary(e);
        } catch (NoSuchMethodException e) {
            arguments.printSummary(e);
        } catch (Exception e) {
            e.printStackTrace(arguments.err);
        }
    }
}
