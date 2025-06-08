/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2021, Open Source Geospatial Foundation (OSGeo)
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

import com.sun.media.imageioimpl.common.BogusColorSpace;
import com.sun.media.imageioimpl.common.PackageUtil;
import com.sun.media.imageioimpl.plugins.gif.GIFImageWriter;
import com.sun.media.jai.util.ImageUtil;
import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.algebra.AlgebraDescriptor;
import it.geosolutions.jaiext.algebra.AlgebraDescriptor.Operator;
import it.geosolutions.jaiext.classifier.ColorMapTransform;
import it.geosolutions.jaiext.colorconvert.IHSColorSpaceJAIExt;
import it.geosolutions.jaiext.colorindexer.ColorIndexer;
import it.geosolutions.jaiext.lookup.LookupTable;
import it.geosolutions.jaiext.lookup.LookupTableFactory;
import it.geosolutions.jaiext.piecewise.PiecewiseTransform1D;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.rlookup.RangeLookupTable;
import it.geosolutions.jaiext.stats.HistogramWrapper;
import it.geosolutions.jaiext.stats.Statistics;
import it.geosolutions.jaiext.stats.Statistics.StatsType;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
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
import java.text.MessageFormat;
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
import javax.media.jai.BorderExtender;
import javax.media.jai.ColorCube;
import javax.media.jai.Histogram;
import javax.media.jai.IHSColorSpace;
import javax.media.jai.ImageFunction;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.OperationRegistry;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.ParameterListDescriptor;
import javax.media.jai.PlanarImage;
import javax.media.jai.PropertyGenerator;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.Warp;
import javax.media.jai.WarpAffine;
import javax.media.jai.WarpGrid;
import javax.media.jai.operator.AddDescriptor;
import javax.media.jai.operator.BandCombineDescriptor;
import javax.media.jai.operator.BandSelectDescriptor;
import javax.media.jai.operator.BinarizeDescriptor;
import javax.media.jai.operator.ColorConvertDescriptor;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.ErrorDiffusionDescriptor;
import javax.media.jai.operator.ExtremaDescriptor;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.operator.HistogramDescriptor;
import javax.media.jai.operator.InvertDescriptor;
import javax.media.jai.operator.LookupDescriptor;
import javax.media.jai.operator.MeanDescriptor;
import javax.media.jai.operator.MosaicDescriptor;
import javax.media.jai.operator.MosaicType;
import javax.media.jai.operator.MultiplyConstDescriptor;
import javax.media.jai.operator.MultiplyDescriptor;
import javax.media.jai.operator.OrderedDitherDescriptor;
import javax.media.jai.operator.RescaleDescriptor;
import javax.media.jai.operator.SubtractDescriptor;
import javax.media.jai.operator.XorConstDescriptor;
import javax.media.jai.registry.RenderedRegistryMode;
import org.geotools.api.coverage.processing.OperationNotFoundException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.image.io.ImageIOExt;
import org.geotools.image.util.ColorUtilities;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.transform.WarpBuilder;
import org.geotools.util.Arguments;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Helper methods for applying JAI operations on an image. The image is specified at
 * {@linkplain #ImageWorker(RenderedImage) creation time}. Successive operations can be applied by invoking the methods
 * defined in this class, and the final image can be obtained by invoking {@link #getRenderedImage} at the end of the
 * process.
 *
 * <p>If an exception is thrown during a method invocation, then this {@code ImageWorker} is left in an undetermined
 * state and should not be used anymore.
 *
 * @since 2.3
 * @version $Id$
 * @author Simone Giannecchini
 * @author Bryce Nordgren
 * @author Martin Desruisseaux
 */
public class ImageWorker {

    static final Integer ONE = 1;

    /** Byte identity lookup table for indexed color models */
    private static final byte[][] IDENTITY_BYTE;
    /** Short identity lookup table for indexed color models */
    private static final short[][] IDENTITY_SHORT;

    static {
        final byte[][] data = new byte[1][256];
        for (int i = 0; i < 256; i++) {
            data[0][i] = (byte) (0xFF & i);
        }

        IDENTITY_BYTE = data;
    }

    static {
        final short[][] data = new short[1][65535];
        for (int i = 0; i < 65535; i++) {
            data[0][i] = (short) (0xFFFF & i);
        }
        IDENTITY_SHORT = data;
    }

    private static final double[] ROI_BACKGROUND = {0};

    private static final double[][] ROI_THRESHOLDS = {{1.0}};

    private static final String OPERATION_CONST_OP_NAME = "operationConst";

    private static final String ALGEBRIC_OP_NAME = "algebric";

    public static final String SCALE_NAME = "Scale";

    public static final String SCALE2_NAME = "Scale2";

    public static final String SCALE_OP_NAME;

    public static final String JAIEXT_ENABLED_KEY = "org.geotools.coverage.jaiext.enabled";

    public static final String USE_JAI_SCALE2_KEY = "it.geosolutions.jaiext.scale2";

    public static final boolean JAIEXT_ENABLED;

    public static final boolean USE_JAI_SCALE2;

    public static boolean isJaiExtEnabled() {
        return JAIEXT_ENABLED;
    }

    /** The logger to use for this class. */
    private static final Logger LOGGER = Logging.getLogger(ImageWorker.class);

    /** CODEC_LIB_AVAILABLE */
    private static final boolean CODEC_LIB_AVAILABLE = PackageUtil.isCodecLibAvailable();

    /** Registration of the JAI-EXT operations */
    static {
        JAIEXT_ENABLED = Boolean.valueOf(System.getProperty(JAIEXT_ENABLED_KEY, "true"));
        JAIExt.initJAIEXT(JAIEXT_ENABLED);
        USE_JAI_SCALE2 = Boolean.getBoolean(USE_JAI_SCALE2_KEY) && JAIEXT_ENABLED;
        SCALE_OP_NAME = USE_JAI_SCALE2 ? SCALE2_NAME : SCALE_NAME;
    }

    /** JDK_JPEG_IMAGE_WRITER_SPI */
    private static final ImageWriterSpi JDK_JPEG_IMAGE_WRITER_SPI;

    static {
        ImageWriterSpi temp = null;
        try {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/jpeg");
            while (writers.hasNext()) {
                ImageWriter temp2 = writers.next();
                if (temp2.getOriginatingProvider().getClass().getName()
                        == "com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi") {
                    temp = temp2.getOriginatingProvider();
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            temp = null;
        }
        // assign
        JDK_JPEG_IMAGE_WRITER_SPI = temp;
    }

    /** IMAGEIO_GIF_IMAGE_WRITER_SPI */
    private static final ImageWriterSpi IMAGEIO_GIF_IMAGE_WRITER_SPI;

    static {
        ImageWriterSpi temp = null;
        try {
            temp = getImageWriterSpi("com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi");
        } catch (Exception e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            temp = null;
        }

        // assign
        IMAGEIO_GIF_IMAGE_WRITER_SPI = temp;
    }

    /** IMAGEIO_JPEG_IMAGE_WRITER_SPI */
    private static final ImageWriterSpi IMAGEIO_JPEG_IMAGE_WRITER_SPI;

    static {
        ImageWriterSpi temp = null;
        try {
            if (PackageUtil.isCodecLibAvailable()) {
                temp = getImageWriterSpi("com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriterSpi");
            } else {
                temp = null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            temp = null;
        }

        // assign
        IMAGEIO_JPEG_IMAGE_WRITER_SPI = temp;
    }

    /** IMAGEIO_EXT_TIFF_IMAGE_WRITER_SPI */
    private static final ImageWriterSpi IMAGEIO_EXT_TIFF_IMAGE_WRITER_SPI;

    static {
        ImageWriterSpi temp = null;
        try {
            temp = getImageWriterSpi("it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi");
        } catch (Exception e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            temp = null;
        }

        // assign
        IMAGEIO_EXT_TIFF_IMAGE_WRITER_SPI = temp;
    }

    /** IMAGEIO_PNG_IMAGE_WRITER_SPI */
    private static final ImageWriterSpi CLIB_PNG_IMAGE_WRITER_SPI;

    static {
        ImageWriterSpi temp = null;
        try {
            if (PackageUtil.isCodecLibAvailable()) {
                temp = getImageWriterSpi("com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriterSpi");
            } else {
                temp = null;
            }

        } catch (Exception e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            temp = null;
        }

        // assign
        CLIB_PNG_IMAGE_WRITER_SPI = temp;
    }

    static ImageWriterSpi getImageWriterSpi(String className) throws Exception {
        Iterator<ImageWriterSpi> serviceProviders = IIORegistry.lookupProviders(ImageWriterSpi.class);
        while (serviceProviders.hasNext()) {
            ImageWriterSpi serviceProvider = serviceProviders.next();
            if (serviceProvider.getClass().getName() == className) {
                return serviceProvider;
            }
        }
        throw new Exception("Class " + className + " not found");
    }

    /** Raster space epsilon */
    static final float RS_EPS = 1E-02f;

    /** Controls the warp-affine reduction */
    public static final String WARP_REDUCTION_ENABLED_KEY = "org.geotools.image.reduceWarpAffine";

    static boolean WARP_REDUCTION_ENABLED =
            Boolean.parseBoolean(System.getProperty(WARP_REDUCTION_ENABLED_KEY, "TRUE"));

    /**
     * Workaround class for compressing PNG using the default PNGImageEncoder shipped with the JDK.
     *
     * <p>{@link PNGImageWriter} does not support {@link ImageWriteParam#setCompressionMode(int)} set to
     * {@link ImageWriteParam#MODE_EXPLICIT}, it only allows {@link ImageWriteParam#MODE_DEFAULT}.
     *
     * @author Simone Giannecchini
     * @todo Consider moving to {@link org.geotools.image.io} package.
     */
    public static final class PNGImageWriteParam extends ImageWriteParam {
        /** Default constructor. */
        public PNGImageWriteParam() {
            super();
            this.canWriteProgressive = true;
            this.canWriteCompressed = true;
            this.locale = Locale.getDefault();
        }
    }

    /** CS_PYCC */
    static final ColorSpace CS_PYCC;

    static {
        ColorSpace cs = null;
        try {
            cs = ColorSpace.getInstance(ColorSpace.CS_PYCC);
        } catch (Throwable t) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);
            }
        }

        // assign, either null or the real CS
        CS_PYCC = cs;
    }

    private static final byte[] ALPHA_LUT;

    static {
        ALPHA_LUT = new byte[256];
        for (int i = 1; i < 256; i++) {
            ALPHA_LUT[i] = (byte) 255;
        }
    }

    protected static OperationDescriptor getOperationDescriptor(final String name) throws OperationNotFoundException {
        final OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
        OperationDescriptor operation =
                (OperationDescriptor) registry.getDescriptor(RenderedRegistryMode.MODE_NAME, name);
        if (operation != null) {
            return operation;
        }

        throw new OperationNotFoundException(MessageFormat.format(ErrorKeys.OPERATION_NOT_FOUND_$1, name));
    }

    /**
     * If {@link Boolean#FALSE FALSE}, image operators are not allowed to produce tiled images. The default is
     * {@link Boolean#TRUE TRUE}. The {@code FALSE} value is sometime useful for exporting images to some formats that
     * doesn't support tiling (e.g. GIF).
     *
     * @see #setRenderingHint
     */
    public static final Hints.Key TILING_ALLOWED = new Hints.Key(Boolean.class);

    /** Specify whether to set a ROI property when doing a mosaic, even when background is specified */
    public static final Hints.Key FORCE_MOSAIC_ROI_PROPERTY = new Hints.Key(Boolean.class);

    /** Specify whether to preserve chained affines instead of collapsing them */
    public static final Hints.Key PRESERVE_CHAINED_AFFINES = new Hints.Key(Boolean.class);

    /** The image property name generated by {@link ExtremaDescriptor}. */
    private static final String EXTREMA = "extrema";

    /** The image property name generated by {@link HistogramDescriptor}. */
    private static final String HISTOGRAM = "histogram";

    /** The image property name generated by {@link MeanDescriptor}. */
    private static final String MEAN = "mean";

    /** Register manually the GTCrop operation, in web containers JAI registration may fails */
    static {
        if (WARP_REDUCTION_ENABLED) {
            GTWarpPropertyGenerator.register(false);
        }
        LOGGER.log(Level.INFO, "Warp/affine reduction enabled: " + WARP_REDUCTION_ENABLED);
        GTAffinePropertyGenerator.register(false);
    }

    /**
     * The image specified by the user at construction time, or last time {@link #invalidateStatistics} were invoked.
     * The {@link #getComputedProperty} method will not search a property pass this point.
     */
    private RenderedImage inheritanceStopPoint;

    /** The image being built. */
    protected RenderedImage image;

    /** The region of interest, or {@code null} if none. */
    private ROI roi;

    /** The NoData range to check nodata, or {@code null} if none. */
    private Range nodata;

    /** Array of values used for indicating the background values */
    private double[] background;

    /**
     * The rendering hints to provides to all image operators. Additional hints may be set (in a separated
     * {@link RenderingHints} object) for particular images.
     */
    private RenderingHints commonHints;

    /**
     * 0 if tile cache is enabled, any other value otherwise. This counter is incremented everytime
     * {@code tileCacheEnabled(false)} is invoked, and decremented every time {@code tileCacheEnabled(true)} is invoked.
     */
    private int tileCacheDisabled = 0;

    /** Subsampling factor for the x axis, used in statistical operations (extrema and histogram) */
    private int xPeriod = ONE;

    /** Subsampling factor for the y axis, used in statistical operations (extrema and histogram) */
    private int yPeriod = ONE;

    /**
     * Creates a new uninitialized builder for an {@linkplain #load image read} or a {@linkplain #mosaic mosaic
     * operation}
     *
     * @see #load(String, int, boolean)
     * @see #mosaic(RenderedImage[], MosaicType, PlanarImage[], ROI[], double[][], Range[])
     */
    public ImageWorker() {
        inheritanceStopPoint = this.image = null;
    }

    /**
     * Creates a new uninitialized worker with RenderingHints for a {@linkplain #mosaic mosaic operation}
     *
     * @see #mosaic(RenderedImage[], MosaicType, PlanarImage[], ROI[], double[][], Range[])
     */
    public ImageWorker(RenderingHints hints) {
        this();
        setRenderingHints(hints);
    }

    /**
     * Creates a new builder for an image read from the specified file.
     *
     * @param input The file to read.
     * @throws IOException if the file can't be read.
     */
    public ImageWorker(final File input) throws IOException {
        this(ImageIOExt.read(input));
    }

    /**
     * Creates a new builder for the specified image. The images to be computed (if any) will save their tiles in the
     * default {@linkplain TileCache tile cache}.
     *
     * @param image The source image.
     */
    public ImageWorker(final RenderedImage image) {
        setImage(image);
    }

    /** The x-period used by statistical operations (e.g. extrema, mean, histogram). Defaults to 1. */
    public int getXPeriod() {
        return xPeriod;
    }

    /**
     * Sets the x-period used by statistical operations (e.g. extrema, mean, histogram). E.g., setting it to 2 wil make
     * the statistical operations read one pixel and skip one pixel, and so on.
     */
    public ImageWorker setXPeriod(int xPeriod) {
        this.xPeriod = xPeriod;
        return this;
    }

    /** The y-period used by statistical operations (e.g. extrema, mean, histogram). Defaults to 1. */
    public int getYPeriod() {
        return yPeriod;
    }

    /**
     * Sets the x-period used by statistical operations (e.g. extrema, mean, histogram). E.g., setting it to 2 wil make
     * the statistical operations read one pixel and skip one pixel, and so on.
     */
    public ImageWorker setYPeriod(int yPeriod) {
        this.yPeriod = yPeriod;
        return this;
    }

    public Range extractNoDataProperty(final RenderedImage image) {
        Object property = image.getProperty(NoDataContainer.GC_NODATA);
        if (property != null) {
            if (property instanceof NoDataContainer) {
                return ((NoDataContainer) property).getAsRange();
            } else if (property instanceof Double) {
                return RangeFactory.create((Double) property, (Double) property);
            }
        }
        return null;
    }

    /**
     * Prepare this builder for the specified image. The images to be computed (if any) will save their tiles in the
     * default {@linkplain TileCache tile cache}.
     *
     * @param image The source image.
     */
    public final ImageWorker setImage(final RenderedImage image) {
        inheritanceStopPoint = this.image = image;
        setNoData(extractNoDataProperty(image));
        return this;
    }

    /** Creates a new image worker with the same hints but a different image. */
    private ImageWorker fork(final RenderedImage image) {
        final ImageWorker worker = new ImageWorker(image);
        if (commonHints != null && !commonHints.isEmpty()) {
            RenderingHints hints = new RenderingHints(null);
            hints.add(worker.commonHints);
            worker.commonHints = hints;
        }
        return worker;
    }

    /**
     * Loads an image using the provided file name and the {@linkplain #getRenderingHints current hints}, which are used
     * to control caching and layout.
     *
     * @param source Filename of the source image to read.
     * @param imageChoice Image index in multipage images.
     * @param readMetadata If {@code true}, metadata will be read.
     */
    public final void load(final String source, final int imageChoice, final boolean readMetadata) {
        final ParameterBlockJAI pbj = new ParameterBlockJAI("ImageRead");
        pbj.setParameter("Input", source)
                .setParameter("ImageChoice", Integer.valueOf(imageChoice))
                .setParameter("ReadMetadata", Boolean.valueOf(readMetadata))
                .setParameter("VerifyInput", Boolean.TRUE);
        image = JAI.create("ImageRead", pbj, getRenderingHints());
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////// ////////
    // ////// IMAGE, PROPERTIES AND RENDERING HINTS ACCESSORS ////////
    // ////// ////////
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the current image.
     *
     * @return The rendered image.
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
     * @see #getRenderedImage
     * @see #getPlanarImage
     * @see #getRenderedOperation
     * @see #getImageAsROI
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
     * @see #getRenderedImage
     * @see #getPlanarImage
     * @see #getImageAsROI
     */
    public final RenderedOp getRenderedOperation() {
        final RenderedImage image = getRenderedImage();
        if (image instanceof RenderedOp) {
            return (RenderedOp) image;
        }
        // Creating a parameter block
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        // Executing the operation
        return JAI.create("Null", pb, getRenderingHints());
    }

    /**
     * Returns the {@linkplain #getRenderedImage rendered image} after null operation. This operation may be used for
     * setting new ImageProperties or for applying new RenderingHints.
     *
     * @return The rendered operation.
     * @see #getRenderedImage
     * @see #getPlanarImage
     * @see #getImageAsROI
     */
    public ImageWorker nullOp() {
        // Creating a parameter block
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        // Executing the operation
        image = JAI.create("Null", pb, getRenderingHints());
        return this;
    }

    /**
     * Returns a {@linkplain ROI Region Of Interest} built from the current {@linkplain #getRenderedImage image}. If the
     * image is multi-bands, then this method first computes an estimation of its {@linkplain #intensity intensity}.
     * Next, this method {@linkplain #binarize() binarize} the image and constructs a {@link ROI} from the result.
     *
     * @return The image as a region of interest.
     * @see #getRenderedImage
     * @see #getPlanarImage
     * @see #getRenderedOperation
     */
    public final ROI getImageAsROI() {
        binarize();
        return new ROI(getRenderedImage());
    }

    /**
     * Returns the <cite>region of interest</cite> currently set, or {@code null} if none. The default value is
     * {@code null}.
     *
     * @return The current region of interest.
     * @see #getMinimums
     * @see #getMaximums
     */
    public final ROI getROI() {
        return roi;
    }

    /**
     * Returns the <cite>NoData Range</cite> currently set, or {@code null} if none. The default value is {@code null}.
     *
     * @return The current NoData Range.
     */
    public final Range getNoData() {
        return nodata;
    }

    /**
     * Returns the <cite>NoData Range</cite> currently set, or {@code null} if none. The default value is {@code null}.
     *
     * @return The current NoData Range.
     */
    public final double[] getDestinationNoData() {
        return background;
    }

    /** Returns true if destination NoData values must be set and they must be used in computation */
    public boolean isNoDataNeeded() {
        return roi != null || nodata != null;
    }

    /**
     * Set the <cite>region of interest</cite> (ROI). A {@code null} set the ROI to the whole {@linkplain #image}. The
     * ROI is used by statistical methods like {@link #getMinimums} and {@link #getMaximums}.
     *
     * @param roi The new region of interest.
     * @return This ImageWorker
     * @see #getMinimums
     * @see #getMaximums
     */
    public final ImageWorker setROI(final ROI roi) {
        this.roi = roi;
        // If ROI == null remove it also from the image properties
        PlanarImage pl = getPlanarImage();
        if (roi == null) {
            pl.removeProperty("ROI");
            // get it back, in some ops like mosaic setting it to null has no effect,
            // will just make it pick from the first source...
            // Computing the ROI from sources might fail, so a fallback is needed for that case too
            boolean overwriteROI = false;
            try {
                Object property = pl.getProperty("ROI");
                overwriteROI = property != null && property != Image.UndefinedProperty;
            } catch (Exception e) {
                // evidently getting the ROI by computation will cause issues, overwrite with a
                // solid one
                overwriteROI = true;
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                            Level.FINE,
                            "Failure while checking source image ROI during a ROI reset, normally it's safely ignorable",
                            e);
                }
            }
            if (overwriteROI) {
                // a ROIGeometry from a rectangle is a good substitute in this case
                pl.setProperty(
                        "ROI",
                        new ROIGeometry(JTS.toPolygon(
                                new Rectangle(image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight()))));
            }
        } else {
            pl.setProperty("ROI", roi);
        }
        image = pl;

        invalidateStatistics();
        return this;
    }

    /**
     * Set the <cite>NoData Range</cite> for checking NoData during computation.
     *
     * @param nodata The new NoData Range.
     * @return This ImageWorker
     */
    public final ImageWorker setNoData(final Range nodata) {
        this.nodata = nodata;
        if (nodata != null && image != null) {
            PlanarImage img = getPlanarImage();
            img.setProperty(NoDataContainer.GC_NODATA, new NoDataContainer(nodata));
            image = img;
        } else if (image != null) {
            PlanarImage img = getPlanarImage();
            Object property = img.getProperty(NoDataContainer.GC_NODATA);
            if (property != null && property != Image.UndefinedProperty) {
                // this kind of property cannot be unset, but it's possible to set it to
                // an invalid value
                img.setProperty(NoDataContainer.GC_NODATA, new Object());
                image = img;
            }
        }
        invalidateStatistics();
        return this;
    }

    /**
     * Set the image background value
     *
     * @param background The image background.
     * @return This ImageWorker
     */
    public final ImageWorker setBackground(final double[] background) {
        this.background = background;
        invalidateStatistics();
        return this;
    }

    /** Returns the rendering hint for the specified key, or {@code null} if none. */
    public final Object getRenderingHint(final RenderingHints.Key key) {
        return commonHints != null ? commonHints.get(key) : null;
    }

    /**
     * Sets a rendering hint tile to use for all images to be computed by this class. This method applies only to the
     * next images to be computed; images already computed before this method call (if any) will not be affected.
     *
     * <p>Some common examples:
     *
     * <p>
     *
     * <ul>
     *   <li><code>setRenderingHint({@linkplain JAI#KEY_TILE_CACHE}, null)</code> disables completly the tile cache.
     *   <li><code>setRenderingHint({@linkplain #TILING_ALLOWED}, Boolean.FALSE)</code> forces all operators to produce
     *       untiled images.
     * </ul>
     *
     * @return This ImageWorker
     */
    public final ImageWorker setRenderingHint(final RenderingHints.Key key, final Object value) {
        if (commonHints == null) {
            commonHints = new RenderingHints(null);
        }
        commonHints.add(new RenderingHints(key, value));
        return this;
    }

    /**
     * Set a map of rendering hints to use for all images to be computed by this class. This method applies only to the
     * next images to be computed; images already computed before this method call (if any) will not be affected.
     *
     * <p>If <code>hints</code> is null we won't modify this list.
     *
     * @return This ImageWorker
     * @see #setRenderingHint(RenderingHint)
     */
    public final ImageWorker setRenderingHints(final RenderingHints hints) {
        if (commonHints == null) {
            commonHints = new RenderingHints(null);
        }
        if (hints != null) {
            commonHints.add(hints);
        }
        return this;
    }

    public final ImageWorker removeRenderingHints() {
        if (commonHints != null) {
            commonHints = null;
        }
        return this;
    }

    /**
     * Removes a rendering hint. Note that invoking this method is <strong>not</strong> the same than invoking <code>
     * {@linkplain #setRenderingHint setRenderingHint}(key, null)</code>. This is especially true for the
     * {@linkplain javax.media.jai.TileCache tile cache} hint:
     *
     * <p>
     *
     * <ul>
     *   <li><code>{@linkplain #setRenderingHint setRenderingHint}({@linkplain JAI#KEY_TILE_CACHE},
     *       null)</code> disables the use of any tile cache. In other words, this method call do request a tile cache,
     *       which happen to be the "null" cache.
     *   <li><code>removeRenderingHint({@linkplain JAI#KEY_TILE_CACHE})</code> unsets any tile cache specified by a
     *       previous rendering hint. All images to be computed after this method call will save their tiles in the
     *       {@linkplain JAI#getTileCache JAI default tile cache}.
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
     * Returns the rendering hints for an image to be computed by this class. The default implementation returns the
     * following hints:
     *
     * <p>
     *
     * <ul>
     *   <li>An {@linkplain ImageLayout image layout} with tiles size computed automatically from the current
     *       {@linkplain #image} size.
     *   <li>Any additional hints specified through the {@link #setRenderingHint} method. If the user provided
     *       explicitly a {@link JAI#KEY_IMAGE_LAYOUT} , then the user layout has precedence over the automatic layout
     *       computed in previous step.
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
            if (commonHints == null || layout != commonHints.get(JAI.KEY_IMAGE_LAYOUT)) {
                // Set the layout only if it is not a user-supplied object.
                layout.setTileWidth(image.getWidth());
                layout.setTileHeight(image.getHeight());
                layout.setTileGridXOffset(image.getMinX());
                layout.setTileGridYOffset(image.getMinY());
                hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
            }
        }
        if (tileCacheDisabled != 0 && commonHints != null && !commonHints.containsKey(JAI.KEY_TILE_CACHE)) {
            hints.add(new RenderingHints(JAI.KEY_TILE_CACHE, null));
        }
        return hints;
    }

    /**
     * Returns the {@linkplain #getRenderingHints rendering hints}, but with a {@linkplain ComponentColorModel component
     * color model} of the specified data type. The data type is changed only if no color model was explicitly specified
     * by the user through {@link #getRenderingHints()}.
     *
     * @param type The data type (typically {@link DataBuffer#TYPE_BYTE}).
     */
    private final RenderingHints getRenderingHints(final int type) {
        /*
         * Gets the default hints, which usually contains only informations about tiling. If the user overridden the rendering hints with an explict
         * color model, keep the user's choice.
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
        if (oldCm != null) {
            final ColorModel newCm = new ComponentColorModel(
                    oldCm.getColorSpace(),
                    oldCm.hasAlpha(), // If true, supports transparency.
                    oldCm.isAlphaPremultiplied(), // If true, alpha is premultiplied.
                    oldCm.getTransparency(), // What alpha values can be represented.
                    type); // Type of primitive array used to represent pixel.
            /*
             * Creating the final image layout which should allow us to change color model.
             */
            layout.setColorModel(newCm);
            layout.setSampleModel(newCm.createCompatibleSampleModel(image.getWidth(), image.getHeight()));
        } else {
            final int numBands = image.getSampleModel().getNumBands();
            final ColorModel newCm = new ComponentColorModel(
                    new BogusColorSpace(numBands),
                    false, // If true, supports transparency.
                    false, // If true, alpha is premultiplied.
                    Transparency.OPAQUE, // What alpha values can be represented.
                    type); // Type of primitive array used to represent pixel.
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
     * Gets the image layout from the specified rendering hints, creating a new one if needed. This method do not modify
     * the specified hints. If the caller modifies the image layout, it should invoke
     * {@code hints.put(JAI.KEY_IMAGE_LAYOUT, layout)} explicitly.
     */
    private static ImageLayout getImageLayout(final RenderingHints hints) {
        final Object candidate = hints.get(JAI.KEY_IMAGE_LAYOUT);
        if (candidate instanceof ImageLayout) {
            return (ImageLayout) candidate;
        }
        return new ImageLayout();
    }

    /**
     * If {@code false}, disables the tile cache. Invoking this method with value {@code true} cancel the last
     * invocation with value {@code false}. If this method was invoking many time with value {@code false}, then this
     * method must be invoked the same amount of time with the value {@code true} for reenabling the cache.
     *
     * <p><strong>Note:</strong> This method name doesn't contain the usual {@code set} prefix because it doesn't really
     * set a flag. Instead it increments or decrements a counter.
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

    /** Returns the transparent pixel value, or -1 if none. */
    public final int getTransparentPixel() {
        final ColorModel cm = image.getColorModel();
        return cm instanceof IndexColorModel ? ((IndexColorModel) cm).getTransparentPixel() : -1;
    }

    /**
     * Gets a property from the property set of the {@linkplain #image}. If the property name is not recognized, then
     * {@link Image#UndefinedProperty} will be returned. This method do <strong>not</strong> inherits properties from
     * the image specified at {@linkplain #ImageWorker(RenderedImage) construction time} - only properties generated by
     * this class are returned.
     */
    private Object getComputedProperty(final String name) {
        final Object value = image.getProperty(name);
        return value == inheritanceStopPoint.getProperty(name) ? Image.UndefinedProperty : value;
    }

    /**
     * Returns the minimums and maximums values found in the image. Those extremas are returned as an array of the form
     * {@code double[2][#bands]}.
     */
    private double[][] getExtremas() {
        Object extrema = getComputedProperty(EXTREMA);
        if (!(extrema instanceof double[][])) {
            // Create the parameterBlock
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            if (JAIExt.isJAIExtOperation("Stats")) {
                StatsType[] stats = {StatsType.EXTREMA};
                // Band definition
                int numBands = getNumBands();
                int[] bands = new int[numBands];
                for (int i = 0; i < numBands; i++) {
                    bands[i] = i;
                }

                // Image parameters
                pb.set(xPeriod, 0); // xPeriod
                pb.set(yPeriod, 1); // yPeriod
                pb.set(roi, 2); // ROI
                pb.set(nodata, 3); // NoData
                pb.set(bands, 5); // band indexes
                pb.set(stats, 6); // statistic operation
                image = JAI.create("Stats", pb, getRenderingHints());
                // Retrieving the statistics
                Statistics[][] results = (Statistics[][]) getComputedProperty(Statistics.STATS_PROPERTY);
                double[][] ext = new double[2][numBands];
                for (int i = 0; i < numBands; i++) {
                    double[] extBand = (double[]) results[i][0].getResult();
                    ext[0][i] = extBand[0];
                    ext[1][i] = extBand[1];
                }
                // Setting the property
                if (image instanceof PlanarImage) {
                    ((PlanarImage) image).setProperty(EXTREMA, ext);
                } else {
                    PlanarImage p = getPlanarImage();
                    p.setProperty(EXTREMA, ext);
                    image = p;
                }
            } else {
                pb.set(roi, 0); // The region of the image to scan. Default to all.
                pb.set(xPeriod, 1); // The horizontal sampling rate. Default to 1.
                pb.set(yPeriod, 2); // The vertical sampling rate. Default to 1.
                pb.set(ONE, 4); // Maximum number of run length codes to store. Default to 1.
                image = JAI.create("Extrema", pb, getRenderingHints());
            }
            extrema = getComputedProperty(EXTREMA);
        }
        return (double[][]) extrema;
    }

    /** Returns the histogram of the image. */
    public Histogram getHistogram(int[] numBins, double[] lowValues, double[] highValues) {
        Object histogram = getComputedProperty(HISTOGRAM);
        // can reuse cached histogram only if the bucket definitions are the same
        if (histogram instanceof HistogramWrapper) {
            HistogramWrapper wrapper = (HistogramWrapper) histogram;
            double[] prevHighs = wrapper.getHighValue();
            double[] prevLows = wrapper.getLowValue();
            int[] prevNumBins = wrapper.getNumBins();
            if (Arrays.equals(prevHighs, highValues)
                    && Arrays.equals(prevLows, lowValues)
                    && Arrays.equals(prevNumBins, numBins)) {
                return wrapper;
            }
        }
        // Create the parameterBlock
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        if (JAIExt.isJAIExtOperation("Stats")) {
            StatsType[] stats = {StatsType.HISTOGRAM};
            // Band definition
            int numBands = getNumBands();
            int[] bands = new int[numBands];
            for (int i = 0; i < numBands; i++) {
                bands[i] = i;
            }

            // Image parameters
            pb.set(xPeriod, 0); // xPeriod
            pb.set(yPeriod, 1); // yPeriod
            pb.set(roi, 2); // ROI
            pb.set(nodata, 3); // NoData
            pb.set(bands, 5); // band indexes
            pb.set(stats, 6); // statistic operation
            pb.set(numBins, 9); // Bin number.
            pb.set(lowValues, 7); // Lower values per band.
            pb.set(highValues, 8); // Higher values per band.
            image = JAI.create("Stats", pb, getRenderingHints());
            // Retrieving the statistics
            Statistics[][] results = (Statistics[][]) getComputedProperty(Statistics.STATS_PROPERTY);
            int[][] bins = new int[numBands][];

            // Cycle on the bands
            for (int i = 0; i < results.length; i++) {
                Statistics stat = results[i][0];
                double[] binsDouble = (double[]) stat.getResult();
                bins[i] = new int[binsDouble.length];
                for (int j = 0; j < binsDouble.length; j++) {
                    bins[i][j] = (int) binsDouble[j];
                }
            }
            ParameterBlock parameterBlock = getRenderedOperation().getParameterBlock();
            if (numBins == null) {
                numBins = (int[]) parameterBlock.getObjectParameter(9);
            }
            if (lowValues == null) {
                lowValues = (double[]) parameterBlock.getObjectParameter(7);
            }
            if (highValues == null) {
                highValues = (double[]) parameterBlock.getObjectParameter(8);
            }
            HistogramWrapper wrapper = new HistogramWrapper(numBins, lowValues, highValues, bins);
            // Setting the property
            if (image instanceof PlanarImage) {
                ((PlanarImage) image).setProperty(HISTOGRAM, wrapper);
            } else {
                PlanarImage p = getPlanarImage();
                p.setProperty(HISTOGRAM, wrapper);
                image = p;
            }
        } else {
            pb.set(roi, 0); // The region of the image to scan. Default to all.
            pb.set(xPeriod, 1); // The horizontal sampling rate. Default to 1.
            pb.set(yPeriod, 2); // The vertical sampling rate. Default to 1.
            pb.set(numBins, 3); // Bin number.
            pb.set(lowValues, 4); // Lower values per band.
            pb.set(highValues, 5); // Higher values per band.
            image = JAI.create("Histogram", pb, getRenderingHints());
        }
        histogram = getComputedProperty(HISTOGRAM);
        return (Histogram) histogram;
    }

    /**
     * Returns the minimums and maximums values found in the image. Those extremas are returned as an array of the form
     * {@code double[2][#bands]}.
     */
    public double[] getMean() {
        Object mean = getComputedProperty(MEAN);
        if (!(mean instanceof double[])) {
            // Create the parameterBlock
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            if (JAIExt.isJAIExtOperation("Stats")) {
                StatsType[] stats = {StatsType.MEAN};
                // Band definition
                int numBands = getNumBands();
                int[] bands = new int[numBands];
                for (int i = 0; i < numBands; i++) {
                    bands[i] = i;
                }

                // Image parameters
                pb.set(xPeriod, 0); // xPeriod
                pb.set(yPeriod, 1); // yPeriod
                pb.set(roi, 2); // ROI
                pb.set(nodata, 3); // NoData
                pb.set(bands, 5); // band indexes
                pb.set(stats, 6); // statistic operation
                image = JAI.create("Stats", pb, getRenderingHints());
                // Retrieving the statistics
                Statistics[][] results = (Statistics[][]) getComputedProperty(Statistics.STATS_PROPERTY);
                double[] meanBands = new double[numBands];
                for (int i = 0; i < numBands; i++) {
                    meanBands[i] = (double) results[i][0].getResult();
                }
                // Setting the property
                if (image instanceof PlanarImage) {
                    ((PlanarImage) image).setProperty(MEAN, meanBands);
                } else {
                    PlanarImage p = getPlanarImage();
                    p.setProperty(MEAN, meanBands);
                    image = p;
                }
            } else {
                pb.set(roi, 0); // The region of the image to scan. Default to all.
                pb.set(xPeriod, 1); // The horizontal sampling rate. Default to 1.
                pb.set(yPeriod, 2); // The vertical sampling rate. Default to 1.
                image = JAI.create("Mean", pb, getRenderingHints());
            }
            mean = getComputedProperty(MEAN);
        }
        return (double[]) mean;
    }

    /**
     * Tells this builder that all statistics on pixel values (e.g. the "extrema" property in the {@linkplain #image})
     * should not be inherited from the source images (if any). This method should be invoked every time an operation
     * changed the pixel values.
     *
     * @return This ImageWorker
     */
    private ImageWorker invalidateStatistics() {
        inheritanceStopPoint = image;
        return this;
    }

    /**
     * Returns the minimal values found in every {@linkplain #image} bands. If a {@linkplain #getROI region of interest}
     * is defined, then the statistics will be computed only over that region.
     *
     * @see #getMaximums
     * @see #setROI
     */
    public final double[] getMinimums() {
        return getExtremas()[0];
    }

    /**
     * Returns the maximal values found in every {@linkplain #image} bands. If a {@linkplain #getROI region of interest}
     * is defined, then the statistics will be computed only over that region.
     *
     * @see #getMinimums
     * @see #setROI
     */
    public final double[] getMaximums() {
        return getExtremas()[1];
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////// ////////
    // ////// KIND OF IMAGE (BYTES, BINARY, INDEXED, RGB...) ////////
    // ////// ////////
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns {@code true} if the {@linkplain #image} stores its pixel values in 8 bits.
     *
     * @see #rescaleToBytes
     */
    public final boolean isBytes() {
        final SampleModel sm = image.getSampleModel();
        final int[] sampleSize = sm.getSampleSize();
        for (int j : sampleSize) if (j != 8) return false;
        return true;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} is binary. Such image usually contains only two values: 0 and 1.
     *
     * @see #binarize()
     * @see #binarize(double)
     * @see #binarize(int,int)
     */
    public final boolean isBinary() {
        return ImageUtil.isBinary(image.getSampleModel());
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses an {@linkplain IndexColorModel index color model}.
     *
     * @see #forceIndexColorModel
     * @see #forceBitmaskIndexColorModel
     * @see #forceIndexColorModelForGIF
     */
    public final boolean isIndexed() {
        return image.getColorModel() instanceof IndexColorModel;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses a RGB {@linkplain ColorSpace color space}. Note that a RGB
     * color space doesn't mean that pixel values are directly stored as RGB components. The image may be
     * {@linkplain #isIndexed indexed} as well.
     *
     * @see #forceColorSpaceRGB
     */
    public final boolean isColorSpaceRGB() {
        final ColorModel cm = image.getColorModel();
        if (cm == null) {
            return false;
        }
        return cm.getColorSpace().getType() == ColorSpace.TYPE_RGB;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses a YCbCr {@linkplain ColorSpace color space}.
     *
     * @see #forceColorSpaceYCbCr()
     */
    public final boolean isColorSpaceYCbCr() {
        // check the presence of the PYCC.pf file that contains the profile for the YCbCr color
        // space
        if (ImageWorker.CS_PYCC == null) {
            throw new IllegalStateException(
                    "Unable to create an YCbCr profile most like since we are unable to locate the YCbCr color profile. Check the Java installation.");
        }
        final ColorModel cm = image.getColorModel();
        if (cm == null) {
            return false;
        }
        return cm.getColorSpace().getType() == ColorSpace.TYPE_YCbCr
                || cm.getColorSpace().equals(CS_PYCC);
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses a IHA {@linkplain ColorSpace color space}.
     *
     * @see #forceColorSpaceIHS()
     */
    public final boolean isColorSpaceIHS() {
        final ColorModel cm = image.getColorModel();
        if (cm == null) {
            return false;
        }
        return cm.getColorSpace() instanceof IHSColorSpace || cm.getColorSpace() instanceof IHSColorSpaceJAIExt;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} uses a GrayScale {@linkplain ColorSpace color space}. Note that a
     * GrayScale color space doesn't mean that pixel values are directly stored as GrayScale component. The image may be
     * {@linkplain #isIndexed indexed} as well.
     *
     * @see #forceColorSpaceGRAYScale
     */
    public final boolean isColorSpaceGRAYScale() {
        final ColorModel cm = image.getColorModel();
        if (cm == null) return false;
        return cm.getColorSpace().getType() == ColorSpace.TYPE_GRAY;
    }

    /**
     * Returns {@code true} if the {@linkplain #image} is {@linkplain Transparency#TRANSLUCENT translucent}.
     *
     * @see #forceBitmaskIndexColorModel
     */
    public final boolean isTranslucent() {
        return image.getColorModel().getTransparency() == Transparency.TRANSLUCENT;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////// ////////
    // ////// IMAGE OPERATORS ////////
    // ////// ////////
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Rescales the {@linkplain #image} such that it uses 8 bits. If the image already uses 8 bits, then this method
     * does nothing. Otherwise this method computes the minimum and maximum values for each band,
     * {@linkplain RescaleDescriptor rescale} them in the range {@code [0 .. 255]} and force the resulting image to
     * {@link DataBuffer#TYPE_BYTE TYPE_BYTE}.
     *
     * @return This ImageWorker
     * @see #isBytes
     * @see RescaleDescriptor
     */
    public final ImageWorker rescaleToBytes() {

        if (isBytes()) {
            // Already using bytes - nothing to do.
            return this;
        }

        // this is to support 16 bits IndexColorModel
        forceComponentColorModel(true, true);
        if (isBytes()) {
            // there might be the case that we entered rescaleToBytes with a
            // 16bits color-palette but the forceComponentColorModel call above
            // already transformed it to RGB bytes, so no need to proceed with
            // the rescale anymore
            return this;
        }
        final double[][] extrema = getExtremas();
        final int length = extrema[0].length;
        final double[] scale = new double[length];
        final double[] offset = new double[length];
        final double destNodata = background != null && background.length > 0
                ? background[0]
                : nodata != null && !nodata.contains(0) ? 0d : Double.NaN;

        // If setting noData to zero, make sure the rescale doesn't map good values to zero.
        double offsetAdjustment = nodata != null || Math.abs(destNodata - 0) < 1E-6 ? 1 : 0;

        boolean computeRescale = false;
        for (int i = 0; i < length; i++) {
            final double delta = extrema[1][i] - extrema[0][i];
            if (Math.abs(delta) > 1E-6 // maximum and minimum does not coincide
                            && (extrema[1][i] - 255 > 1E-6 // the maximum is greater than 255
                                    || extrema[0][i] < -1E-6) // the minimum is smaller than 0
                    || offsetAdjustment > 0) // noData has been remapped to byte
            {
                // we need to rescale
                computeRescale = true;

                // rescale factors
                scale[i] = (255 - offsetAdjustment) / delta;
                offset[i] = -scale[i] * extrema[0][i] + offsetAdjustment;
            } else {
                // we do not rescale explicitly bu in case we have to, we relay on the clamping
                // capabilities of the format operator
                scale[i] = 1;
                offset[i] = 0;
            }
        }
        final RenderingHints hints = getRenderingHints(DataBuffer.TYPE_BYTE);
        if (computeRescale) {
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0); // The source image.
            pb.set(scale, 0); // The per-band constants to multiply by.
            pb.set(offset, 1); // The per-band offsets to be added.
            pb.set(roi, 2); // ROI
            pb.set(nodata, 3); // NoData range
            if (isNoDataNeeded() && !Double.isNaN(destNodata)) {
                pb.set(destNodata, 5);
            }

            image = JAI.create("Rescale", pb, hints);
            if (!Double.isNaN(destNodata)) {
                setNoData(RangeFactory.create((byte) destNodata, DataBuffer.TYPE_BYTE));
            }
        } else {
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0); // The source image.
            pb.set(DataBuffer.TYPE_BYTE, 0); // The destination image data type (BYTE)
            image = JAI.create("Format", pb, hints);
            setNoData(RangeFactory.convert(nodata, DataBuffer.TYPE_BYTE));
        }
        invalidateStatistics(); // Extremas are no longer valid.

        // All post conditions for this method contract.
        assert isBytes();
        return this;
    }

    /**
     * Reduces the color model to {@linkplain IndexColorModel index color model}. If the current {@linkplain #image}
     * already uses an {@linkplain IndexColorModel index color model}, then this method do nothing. Otherwise, the
     * current implementation performs a ditering on the original color model. Note that this operation loose the alpha
     * channel.
     *
     * <p>This for the moment should work only with opaque images, with non opaque images we just remove the alpha band
     * in order to build an {@link IndexColorModel}. This is one because in general it could be very difficult to decide
     * the final transparency for each pixel given the complexity if the algorithms for obtaining an
     * {@link IndexColorModel}.
     *
     * <p>If an {@link IndexColorModel} with a single transparency index is enough for you, we advise you to take a look
     * at {@link #forceIndexColorModelForGIF(boolean)} methdo.
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
        if (getNumBands() % 2 == 0) retainBands(getNumBands() - 1);
        forceColorSpaceRGB();
        final RenderingHints hints = getRenderingHints();
        if (error) {
            // error diffusion
            final KernelJAI ditherMask = KernelJAI.ERROR_FILTER_FLOYD_STEINBERG;
            final LookupTableJAI colorMap = ColorCube.BYTE_496;

            // Creation of the ParameterBlock
            ParameterBlock pb = new ParameterBlock();
            // Setting source
            pb.setSource(image, 0);
            // Setting parameters
            pb.set(colorMap, 0);
            pb.set(ditherMask, 1);
            pb.set(roi, 2);
            pb.set(nodata, 3);
            if (isNoDataNeeded()) {
                if (background != null && background.length > 0) {
                    int dest = (int) background[0];
                    pb.set(dest, 4);
                }
            }

            image = JAI.create("ErrorDiffusion", pb, hints);
        } else {
            // ordered dither
            final KernelJAI[] ditherMask = KernelJAI.DITHER_MASK_443;
            final ColorCube colorMap = ColorCube.BYTE_496;

            // Creation of the ParameterBlock
            ParameterBlock pb = new ParameterBlock();
            // Setting source
            pb.setSource(image, 0);
            // Setting parameters
            pb.set(colorMap, 0);
            pb.set(ditherMask, 1);
            pb.set(roi, 2);
            pb.set(nodata, 3);
            if (isNoDataNeeded()) {
                if (background != null && background.length > 0) {
                    int dest = (int) background[0];
                    pb.set(dest, 4);
                }
            }

            image = JAI.create("OrderedDither", pb, hints);
        }
        tileCacheEnabled(true);
        invalidateStatistics();

        // All post conditions for this method contract.
        assert isIndexed();
        return this;
    }

    /**
     * Reduces the color model to {@linkplain IndexColorModel index color model} with {@linkplain Transparency#OPAQUE
     * opaque} or {@linkplain Transparency#BITMASK bitmask} transparency. If the current {@linkplain #image} already
     * uses a suitable color model, then this method do nothing.
     *
     * @return this {@link ImageWorker}.
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
     * Reduces the color model to {@linkplain IndexColorModel index color model} with {@linkplain Transparency#OPAQUE
     * opaque} or {@linkplain Transparency#BITMASK bitmask} transparency. If the current {@linkplain #image} already
     * uses a suitable color model, then this method do nothing.
     *
     * @param suggestedTransparent A suggested pixel index to define as the transparent pixel. *
     * @param errorDiffusion Tells if I should use {@link ErrorDiffusionDescriptor} or {@link OrderedDitherDescriptor}
     *     JAi operations. errorDiffusion
     * @return this {@link ImageWorker}.
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
             * The index color model need to be replaced. Creates a lookup table mapping from the old pixel values to new pixels values, with
             * transparent colors mapped to the new transparent pixel value. The lookup table uses TYPE_BYTE or TYPE_USHORT, which are the two only
             * types supported by IndexColorModel.
             */
            final int mapSize = oldCM.getMapSize();
            if (transparentPixel < 0)
                suggestedTransparent = suggestedTransparent <= mapSize ? mapSize + 1 : suggestedTransparent;
            else suggestedTransparent = transparentPixel;
            final int newSize = Math.max(mapSize, suggestedTransparent);
            final int newPixelSize = ColorUtilities.getBitCount(newSize);
            if (newPixelSize > 16)
                throw new IllegalArgumentException("Unable to create index color model with more than 65536 elements");
            final LookupTable lookupTable;
            if (newPixelSize <= 8) {
                final byte[] table = new byte[mapSize];
                for (int i = 0; i < mapSize; i++) {
                    table[i] = (byte) (oldCM.getAlpha(i) == 0 ? suggestedTransparent : i);
                }
                lookupTable =
                        LookupTableFactory.create(table, image.getSampleModel().getDataType());
            } else {
                final short[] table = new short[mapSize];
                for (int i = 0; i < mapSize; i++) {
                    table[i] = (short) (oldCM.getAlpha(i) == 0 ? suggestedTransparent : i);
                }
                lookupTable = LookupTableFactory.create(table, true);
            }
            /*
             * Now we need to perform the look up transformation. First of all we create the new color model with a bitmask transparency using the
             * transparency index specified to this method. Then we perform the lookup operation in order to prepare for the gif image.
             */
            final byte[][] rgb = new byte[3][newSize];
            oldCM.getReds(rgb[0]);
            oldCM.getGreens(rgb[1]);
            oldCM.getBlues(rgb[2]);
            final IndexColorModel newCM =
                    new IndexColorModel(newPixelSize, newSize, rgb[0], rgb[1], rgb[2], suggestedTransparent);
            final RenderingHints hints = getRenderingHints();
            final ImageLayout layout = getImageLayout(hints);
            layout.setColorModel(newCM);
            // we should not transform on color map here
            hints.put(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.FALSE);
            hints.put(JAI.KEY_IMAGE_LAYOUT, layout);

            // ParameterBlock definition
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            pb.set(lookupTable, 0);
            pb.set(roi, 2);
            pb.set(nodata, 3);
            if (isNoDataNeeded()) {
                if (background != null && background.length > 0) {
                    pb.set(background[0], 1);
                }
            }

            image = JAI.create("Lookup", pb, hints);
            // New parameterblock for format operation
            pb = new ParameterBlock();
            pb.setSource(image, 0);
            int dataType = image.getSampleModel().getDataType();
            pb.set(dataType, 0);
            image = JAI.create("Format", pb, hints);
            // Converting NoData Range
            setNoData(RangeFactory.convert(nodata, dataType));
        } else {
            // force component color model first
            forceComponentColorModel(true);
            /*
             * The image is not indexed.
             */
            if (cm.hasAlpha()) {
                // Getting the alpha channel.
                tileCacheEnabled(false);
                int numBands = getNumBands();
                final RenderingHints hints = getRenderingHints();

                // ParameterBlock creation
                ParameterBlock pb = new ParameterBlock();
                pb.setSource(image, 0);
                pb.set(new int[] {--numBands}, 0);
                final RenderedOp alphaChannel = JAI.create("BandSelect", pb, hints);
                retainBands(numBands);
                forceIndexColorModel(errorDiffusion);
                tileCacheEnabled(true);

                /*
                 * Adding transparency if needed, which means using the alpha channel to build a new color model. The method call below implies
                 * 'forceColorSpaceRGB()' and 'forceIndexColorModel()' method calls.
                 */
                addTransparencyToIndexColorModel(alphaChannel, false, suggestedTransparent, errorDiffusion);
            } else forceIndexColorModel(errorDiffusion);
        }
        // All post conditions for this method contract.
        assert isIndexed();
        assert !isTranslucent();
        return this;
    }

    /**
     * Converts the image to a GIF-compliant image. This method has been created in order to convert the input image to
     * a form that is compatible with the GIF model. It first remove the information about transparency since the error
     * diffusion and the error dither operations are unable to process images with more than 3 bands. Afterwards the
     * image is processed with an error diffusion operator in order to reduce the number of bands from 3 to 1 and the
     * number of color to 216. A suitable layout is used for the final image via the {@linkplain #getRenderingHints
     * rendering hints} in order to take into account the different layout model for the final image.
     *
     * <p><strong>Tip:</strong> For optimizing writing GIF, we need to create the image untiled. This can be done by
     * invoking <code>
     * {@linkplain #setRenderingHint setRenderingHint}({@linkplain
     * #TILING_ALLOWED}, Boolean.FALSE)</code> first.
     *
     * @param errorDiffusion Tells if I should use {@link ErrorDiffusionDescriptor} or {@link OrderedDitherDescriptor}
     *     JAi operations.
     * @return this {@link ImageWorker}.
     * @see #isIndexed
     * @see #forceIndexColorModel
     * @see #forceBitmaskIndexColorModel
     */
    public final ImageWorker forceIndexColorModelForGIF(final boolean errorDiffusion) {
        /*
         * Checking the color model to see if we need to convert it back to color model. We might also need to reformat the image in order to get it
         * to 8 bits samples.
         */
        ColorModel cm = image.getColorModel();
        if (cm instanceof PackedColorModel) {
            forceComponentColorModel();
            cm = image.getColorModel();
        }
        if (!(cm instanceof IndexColorModel) || cm.getPixelSize() > 8) rescaleToBytes();
        /*
         * Getting the alpha channel and separating from the others bands. If the initial image had no alpha channel (more specifically, if it is
         * either opaque or a bitmask) we proceed without doing anything since it seems that GIF encoder in such a case works fine. If we need to
         * create a bitmask, we will use the last index value allowed (255) as the transparent pixel value.
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
     * Reformats the {@linkplain ColorModel color model} to a {@linkplain ComponentColorModel component color model}
     * preserving transparency. This is used especially in order to go from {@link PackedColorModel} to
     * {@link ComponentColorModel}, which seems to be well accepted from {@code PNGEncoder} and {@code TIFFEncoder}.
     *
     * <p>This code is adapted from jai-interests mailing list archive.
     *
     * @return this {@link ImageWorker}.
     * @see FormatDescriptor
     */
    public final ImageWorker forceComponentColorModel() {
        return forceComponentColorModel(false);
    }

    /**
     * Reformats the {@linkplain ColorModel color model} to a {@linkplain ComponentColorModel component color model}
     * preserving transparency. This is used especially in order to go from {@link PackedColorModel} to
     * {@link ComponentColorModel}, which seems to be well accepted from {@code PNGEncoder} and {@code TIFFEncoder}. The
     * omitAlphaOnExpand param allows to explicity avoid producing an alpha band when expanding an
     * {@link IndexColorModel} to {@link ComponentColorModel}. This may be useful when preparing an Image for JPEG
     * encoding which do not support alpha band, avoiding the need of a BandSelect right after the color expansion. Note
     * that this flag has only effect when expanding from {@link IndexColorModel} so alpha is preserved if input
     * colorModel is already a ComponentColorModel.
     *
     * <p>This code is adapted from jai-interests mailing list archive.
     *
     * @return this {@link ImageWorker}.
     * @see FormatDescriptor
     */
    public final ImageWorker forceComponentColorModel(
            boolean checkTransparent, boolean optimizeGray, boolean omitAlphaOnExpand) {
        final ColorModel cm = image.getColorModel();
        if (cm instanceof ComponentColorModel) {
            // Already a component color model - nothing to do.
            return this;
        }
        // shortcut for index color model
        if (cm instanceof IndexColorModel) {
            final IndexColorModel icm = (IndexColorModel) cm;
            final SampleModel sm = this.image.getSampleModel();
            final int datatype = sm.getDataType();
            Range noData = getNoData();
            final boolean gray = ColorUtilities.isGrayPalette(icm, checkTransparent) && optimizeGray && noData == null;
            final boolean alpha = (icm.hasAlpha() || noData != null) && !omitAlphaOnExpand;
            /*
             * If the image is grayscale, retain only the needed bands.
             */
            final int numDestinationBands = gray ? alpha ? 2 : 1 : alpha ? 4 : 3;
            LookupTable lut = null;

            switch (datatype) {
                case DataBuffer.TYPE_BYTE:
                    {
                        final byte[][] data = new byte[numDestinationBands][256];
                        icm.getReds(data[0]);
                        if (numDestinationBands >= 2)
                            // remember to optimize for grayscale images
                            if (!gray) icm.getGreens(data[1]);
                            else icm.getAlphas(data[1]);
                        if (numDestinationBands >= 3) icm.getBlues(data[2]);
                        if (numDestinationBands == 4) {
                            icm.getAlphas(data[3]);
                        }
                        if (icm.getMapSize() < 256) {
                            Color bgColor = getBackgroundColor();
                            if (bgColor == null) {
                                bgColor = Color.BLACK;
                            }
                            byte r = (byte) (bgColor.getRed() & 0xFF);
                            byte g = (byte) (bgColor.getRed() & 0xFF);
                            byte b = (byte) (bgColor.getBlue() & 0xFF);
                            byte a = (byte) (bgColor.getAlpha() & 0xFF);
                            for (int i = icm.getMapSize(); i < 256; i++) {
                                data[0][i] = r;
                                if (numDestinationBands >= 2) {
                                    // remember to optimize for grayscale images
                                    if (!gray) {
                                        data[1][i] = g;
                                    } else {
                                        data[1][i] = a;
                                    }
                                }
                                if (numDestinationBands >= 3) {
                                    data[2][i] = b;
                                }
                                if (numDestinationBands == 4) {
                                    data[3][i] = a;
                                }
                            }
                        }
                        if (noData != null && (numDestinationBands == 2 || numDestinationBands == 4)) {
                            int noDataValue = noData.getMin().intValue();
                            for (int i = 0; i < 256; i++) {
                                if (i == noDataValue) {
                                    if (numDestinationBands == 2 && gray) {
                                        data[1][i] = 0;
                                    } else if (numDestinationBands == 4) {
                                        data[3][i] = 0;
                                    }
                                }
                            }
                        }
                        lut = LookupTableFactory.create(data, datatype);
                    }
                    break;

                case DataBuffer.TYPE_USHORT:
                    lut = gray
                            ? createGrayLookupTable(icm, numDestinationBands)
                            : createRGBLookupTable(icm, numDestinationBands);
                    break;

                default:
                    throw new IllegalArgumentException(
                            MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "datatype", datatype));
            }

            // did we initialized the LUT?
            if (lut == null) throw new IllegalStateException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "lut"));
            /*
             * Get the default hints, which usually contains only information about tiling.
             * If the user override the rendering hints with an explicit color model,
             * keep the user's choice.
             */
            final RenderingHints hints = getRenderingHints();
            final ImageLayout layout;
            final Object candidate = hints.get(JAI.KEY_IMAGE_LAYOUT);
            if (candidate instanceof ImageLayout) {
                layout = (ImageLayout) candidate;
            } else {
                layout = new ImageLayout(image);
                hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            }

            int[] bits = new int[numDestinationBands];
            // bits per component
            for (int i = 0; i < numDestinationBands; i++) {
                // When RGB(A), go to 8 bits, otherwise copy the sample size
                // (might be 16 bits)
                bits[i] = numDestinationBands >= 3 ? 8 : sm.getSampleSize(i);
            }
            final ComponentColorModel destinationColorModel = new ComponentColorModel(
                    numDestinationBands >= 3
                            ? ColorSpace.getInstance(ColorSpace.CS_sRGB)
                            : ColorSpace.getInstance(ColorSpace.CS_GRAY),
                    bits,
                    alpha,
                    cm.isAlphaPremultiplied(),
                    alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE,
                    datatype);
            final SampleModel destinationSampleModel =
                    destinationColorModel.createCompatibleSampleModel(image.getWidth(), image.getHeight());
            layout.setColorModel(destinationColorModel);
            layout.setSampleModel(destinationSampleModel);

            // ParameterBlock definition
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            pb.set(lut, 0);
            pb.set(roi, 2);
            pb.set(nodata, 3);
            if (isNoDataNeeded()) {
                if (background != null && background.length > 0) {
                    pb.set(background[0], 1);
                }
            }

            image = JAI.create("Lookup", pb, hints);

        } else {
            // Most of the code adapted from jai-interests is in 'getRenderingHints(int)'.
            final int type = cm instanceof DirectColorModel
                    ? DataBuffer.TYPE_BYTE
                    : image.getSampleModel().getTransferType();
            final RenderingHints hints = getRenderingHints(type);
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0); // The source image.
            pb.set(type, 0);

            image = JAI.create("Format", pb, hints);
            setNoData(RangeFactory.convert(nodata, type));
        }
        invalidateStatistics();

        // All post conditions for this method contract.
        assert image.getColorModel() instanceof ComponentColorModel;
        return this;
    }

    private LookupTable createGrayLookupTable(IndexColorModel icm, int numDestinationBands) {
        final int mapSize = icm.getMapSize();
        final short[][] data = new short[numDestinationBands][mapSize];
        for (int i = 0; i < mapSize; i++) {
            data[0][i] = (short) (icm.getRed(i) & 0xFF);
            if (numDestinationBands == 2) data[1][i] = (short) (icm.getAlpha(i) & 0xFF);
        }
        return LookupTableFactory.create(data, true);
    }

    private LookupTable createRGBLookupTable(IndexColorModel icm, int numDestinationBands) {
        final int mapSize = icm.getMapSize();
        // Even if starting from a 16bits paletted image, RGB will go to bytes
        final byte[][] data = new byte[numDestinationBands][mapSize];
        for (int i = 0; i < mapSize; i++) {
            data[0][i] = (byte) (icm.getRed(i) & 0xFF);
            data[1][i] = (byte) (icm.getGreen(i) & 0xFF);
            data[2][i] = (byte) (icm.getBlue(i) & 0xFF);
            if (numDestinationBands == 4) {
                data[3][i] = (byte) (icm.getAlpha(i) & 0xFF);
            }
        }
        return LookupTableFactory.create(data);
    }

    /**
     * If the image has an indexed color model, removes it, and replaces it with a component color model. can be useful
     * before a band-merge if the image in question is not meant to be color expanded.
     */
    public final ImageWorker removeIndexColorModel() {
        if (image.getColorModel() instanceof IndexColorModel) {
            LookupTable lut = null;
            SampleModel sampleModel = image.getSampleModel();
            int dataType = sampleModel.getDataType();
            switch (dataType) {
                case DataBuffer.TYPE_BYTE:
                    lut = LookupTableFactory.create(IDENTITY_BYTE);
                    break;

                case DataBuffer.TYPE_USHORT:
                    boolean unsigned = dataType == DataBuffer.TYPE_SHORT;
                    lut = LookupTableFactory.create(IDENTITY_SHORT, unsigned);
                    break;

                default:
                    throw new IllegalArgumentException(
                            MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "datatype", dataType));
            }

            // prepare color model and sample model
            final ComponentColorModel destinationColorModel = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, dataType);
            final SampleModel destinationSampleModel =
                    destinationColorModel.createCompatibleSampleModel(sampleModel.getWidth(), sampleModel.getHeight());
            ImageLayout layout = new ImageLayout(image);
            layout.setColorModel(destinationColorModel);
            layout.setSampleModel(destinationSampleModel);

            // perform the lookup
            final RenderingHints oldRi = this.getRenderingHints();
            final RenderingHints newRi = (RenderingHints) oldRi.clone();
            newRi.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            try {
                setRenderingHints(newRi);
                lookup(lut);
            } finally {
                // restore RI
                this.setRenderingHints(oldRi);
            }
        }

        return this;
    }

    /**
     * Reformats the {@linkplain ColorModel color model} to a {@linkplain ComponentColorModel component color model}
     * preserving transparency. This is used especially in order to go from {@link PackedColorModel} to
     * {@link ComponentColorModel}, which seems to be well accepted from {@code PNGEncoder} and {@code TIFFEncoder}.
     *
     * <p>This code is adapted from jai-interests mailing list archive.
     *
     * @param checkTransparent tells this method to not consider fully transparent pixels when optimizing grayscale
     *     palettes.
     * @return this {@link ImageWorker}.
     * @see FormatDescriptor
     */
    public final ImageWorker forceComponentColorModel(boolean checkTransparent) {
        return forceComponentColorModel(checkTransparent, true);
    }

    /**
     * Reformats the {@linkplain ColorModel color model} to a {@linkplain ComponentColorModel component color model}
     * preserving transparency. This is used especially in order to go from {@link PackedColorModel} to
     * {@link ComponentColorModel}, which seems to be well accepted from {@code PNGEncoder} and {@code TIFFEncoder}.
     *
     * <p>This code is adapted from jai-interests mailing list archive.
     *
     * @param checkTransparent tells this method to not consider fully transparent pixels when optimizing grayscale
     *     palettes.
     * @return this {@link ImageWorker}.
     * @see FormatDescriptor
     */
    public final ImageWorker forceComponentColorModel(boolean checkTransparent, boolean optimizeGray) {
        return forceComponentColorModel(checkTransparent, optimizeGray, false);
    }

    /**
     * Forces the {@linkplain #image} color model to the {@linkplain ColorSpace#CS_sRGB RGB color space}. If the current
     * color space is already of {@linkplain ColorSpace#TYPE_RGB RGB type}, then this method does nothing. This
     * operation may loose the alpha channel.
     *
     * @return this {@link ImageWorker}.
     * @see #isColorSpaceRGB
     * @see ColorConvertDescriptor
     */
    public final ImageWorker forceColorSpaceRGB() {
        if (!isColorSpaceRGB()) {
            final ColorModel cm = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_sRGB),
                    false,
                    false,
                    Transparency.OPAQUE,
                    image.getSampleModel().getDataType());

            // force computation of the new colormodel
            forceColorModel(cm);
        }
        // All post conditions for this method contract.
        assert isColorSpaceRGB();
        return this;
    }

    /**
     * Forces the {@linkplain #image} color model to the {@linkplain ColorSpace#CS_PYCC YCbCr color space}. If the
     * current color space is already of {@linkplain ColorSpace#CS_PYCC YCbCr}, then this method does nothing.
     *
     * @return this {@link ImageWorker}.
     * @see #isColorSpaceRGB
     * @see ColorConvertDescriptor
     */
    public final ImageWorker forceColorSpaceYCbCr() {
        if (!isColorSpaceYCbCr()) {
            // go to component model
            forceComponentColorModel();

            // Create a ColorModel to convert the image to YCbCr.
            final ColorModel cm = new ComponentColorModel(
                    CS_PYCC,
                    false,
                    false,
                    Transparency.OPAQUE,
                    this.image.getSampleModel().getDataType());

            // force computation of the new colormodel
            forceColorModel(cm);
        }
        // All post conditions for this method contract.
        assert isColorSpaceYCbCr();
        return this;
    }

    /**
     * Forces the {@linkplain #image} color model to the IHS color space. If the current color space is already of IHS
     * type, then this method does nothing. This operation may loose the alpha channel.
     *
     * @return this {@link ImageWorker}.
     * @see ColorConvertDescriptor
     */
    public final ImageWorker forceColorSpaceIHS() {
        if (!isColorSpaceIHS()) {
            forceComponentColorModel();

            // Create a ColorModel to convert the image to IHS.
            final ColorSpace ihs = isJaiExtEnabled() ? IHSColorSpaceJAIExt.getInstance() : IHSColorSpace.getInstance();
            final int numBits = image.getColorModel().getComponentSize(0);
            final ColorModel ihsColorModel = new ComponentColorModel(
                    ihs,
                    new int[] {numBits, numBits, numBits},
                    false,
                    false,
                    Transparency.OPAQUE,
                    image.getSampleModel().getDataType());

            // compute
            forceColorModel(ihsColorModel);
        }

        // All post conditions for this method contract.
        assert isColorSpaceIHS();
        return this;
    }

    /** Forces the provided {@link ColorModel} via the JAI ColorConvert operation. */
    private void forceColorModel(final ColorModel cm) {

        final ImageLayout2 il = new ImageLayout2(image);
        il.setColorModel(cm);
        il.setSampleModel(cm.createCompatibleSampleModel(image.getWidth(), image.getHeight()));
        final RenderingHints oldRi = this.getRenderingHints();
        final RenderingHints newRi = (RenderingHints) oldRi.clone();
        newRi.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il));
        setRenderingHints(newRi);

        // Setting the parameter blocks
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(cm, 0);
        pb.set(roi, 1);
        pb.set(nodata, 2);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                // Elaborating the final NoData value
                if (background.length != cm.getNumColorComponents()) {
                    throw new IllegalArgumentException("Wrong DestinationNoData value defined");
                }
                pb.set(background, 3);
            }
        }

        image = JAI.create("ColorConvert", pb, getRenderingHints());

        // restore RI
        this.setRenderingHints(oldRi);

        // invalidate stats
        invalidateStatistics();
    }

    /**
     * Add the bands to the Component Color Model
     *
     * @param writeband number of bands after the bandmerge.
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker bandMerge(int writeband) {
        ParameterBlock pb = new ParameterBlock();

        PlanarImage sourceImage = PlanarImage.wrapRenderedImage(getRenderedImage());

        int numBands = sourceImage.getSampleModel().getNumBands();

        // getting first band
        final RenderedImage firstBand = JAI.create("bandSelect", sourceImage, new int[] {0});

        // adding to the image
        final int length = writeband - numBands;
        pb.addSource(sourceImage);
        for (int i = 0; i < length; i++) {
            pb.addSource(firstBand);
        }
        pb.set(new Range[] {nodata}, 0);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                // Elaborating the final NoData value
                pb.set(background[0], 1);
            } else if (nodata != null) {
                // default background value may screw up things, let's preserve nodata
                pb.set(nodata.getMin().doubleValue(), 1);
            }
        }
        pb.set(roi, 3);
        sourceImage = JAI.create("bandmerge", pb);
        image = sourceImage;
        invalidateStatistics();

        // All post conditions for this method contract.
        assert image.getSampleModel().getNumBands() == writeband;
        return this;
    }

    /**
     * Perform a BandMerge operation between the underlying image and the provided one.
     *
     * @param image to merge with the underlying one.
     * @param before <code>true</code> if we want to use first the provided image, <code>false
     *     </code> otherwise.
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker addBand(RenderedImage image, boolean before) {
        return addBand(image, before, false, null);
    }

    /**
     * Perform a BandMerge operation between the underlying image and the provided one.
     *
     * @param image to merge with the underlying one.
     * @param before <code>true</code> if we want to use first the provided image, <code>false
     *     </code> otherwise.
     * @param addAlpha <code>true</code> if we want to set the last image as alpha, <code>false
     *     </code> otherwise.
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker addBand(RenderedImage image, boolean before, boolean addAlpha, Range nodata2) {
        ParameterBlock pb = new ParameterBlock();
        if (before) {
            pb.setSource(image, 0);
            pb.setSource(this.image, 1);
        } else {
            pb.setSource(this.image, 0);
            pb.setSource(image, 1);
        }
        pb.set(new Range[] {nodata, nodata2}, 0);
        if (isNoDataNeeded() || nodata2 != null) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 1);
            } else if (nodata != null) {
                // default background value may screw up things, let's preserve nodata
                pb.set(nodata.getMin().doubleValue(), 1);
            }
        }
        pb.set(roi, 3);
        pb.set(addAlpha, 4);
        this.image = JAI.create("BandMerge", pb, this.getRenderingHints());
        invalidateStatistics();

        return this;
    }

    /**
     * Perform a BandMerge operation between the underlying image and the provided one.
     *
     * @param bands to merge with the underlying one.
     * @param addAlpha <code>true</code> if we want to set the last image as alpha, <code>false
     *     </code> otherwise.
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker addBands(RenderedImage[] bands, boolean addAlpha, Range[] nodata2) {
        return addBands(bands, addAlpha, nodata2, null);
    }

    /**
     * Perform a BandMerge operation between the underlying image and the provided one.
     *
     * @param bands images to merge with the underlying one.
     * @param addAlpha <code>true</code> if we want to set the last image as alpha, <code>false
     *     </code> otherwise.
     * @param transformationList List of AffineTransformation that can be applied to the input rasters in order to
     *     repoject them to the same CRS.
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker addBands(
            RenderedImage[] bands, boolean addAlpha, Range[] nodata2, List<AffineTransform> transformationList) {
        ParameterBlock pb = new ParameterBlock();
        for (RenderedImage band : bands) {
            pb.addSource(band);
        }
        Range[] newRange = new Range[bands.length + 1];
        newRange[0] = nodata;
        if (nodata2 != null) {
            System.arraycopy(nodata2, 0, newRange, 1, nodata2.length);
        }
        pb.set(newRange, 0);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 1);
            } else if (nodata != null) {
                // default background value may screw up things, let's preserve nodata
                pb.set(nodata.getMin().doubleValue(), 1);
            }
        }
        pb.set(transformationList, 3);
        pb.set(roi, 3);
        pb.set(addAlpha, 4);
        this.image = JAI.create("BandMerge", pb, this.getRenderingHints());
        invalidateStatistics();

        return this;
    }

    /**
     * Forces the {@linkplain #image} color model to the {@linkplain ColorSpace#CS_GRAY GRAYScale color space}. If the
     * current color space is already of {@linkplain ColorSpace#TYPE_GRAY type}, then this method does nothing.
     *
     * @return this {@link ImageWorker}.
     * @see #isColorSpaceGRAYScale
     * @see ColorConvertDescriptor
     */
    public final ImageWorker forceColorSpaceGRAYScale() {
        if (!isColorSpaceRGB()) {
            final ColorModel cm = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_GRAY),
                    false,
                    false,
                    Transparency.OPAQUE,
                    DataBuffer.TYPE_BYTE);
            forceColorModel(cm);
            invalidateStatistics();
        }
        // All post conditions for this method contract.
        assert isColorSpaceGRAYScale();
        return this;
    }

    /**
     * Creates an image which represents approximatively the intensity of {@linkplain #image}. The result is always a
     * single-banded image. If the image uses an {@linkplain IHSColorSpace IHS color space}, then this method just
     * {@linkplain #retainFirstBand retain the first band} without any further processing. Otherwise, this method
     * performs a simple {@linkplain BandCombineDescriptor band combine} operation on the {@linkplain #image} in order
     * to come up with a simple estimation of the intensity of the image based on the average value of the color
     * components. It is worthwhile to note that the alpha band is stripped from the image.
     *
     * @return this {@link ImageWorker}.
     * @see BandCombineDescriptor
     */
    public final ImageWorker intensity() {
        /*
         * If the color model already uses a IHS color space or a Gray color space, keep only the intensity band. Otherwise, we need a component color
         * model to be sure to understand what we are doing.
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
        // remove the alpha band
        if (numColorBands != numBands) {
            this.retainBands(numBands);
        }
        /*
         * We have more than one band. Note that there is no need to remove the alpha band before to apply the "bandCombine" operation - it is
         * suffisient to let the coefficient for the alpha band to the 0 value.
         */
        final double[][] coeff = new double[1][numBands + 1];
        Arrays.fill(coeff[0], 0, numColorBands, 1.0 / numColorBands);
        // ParameterBlock definition
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(coeff, 0);
        pb.set(roi, 1);
        pb.set(nodata, 2);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 3);
            }
        }

        image = JAI.create("BandCombine", pb, getRenderingHints());

        invalidateStatistics();

        // All post conditions for this method contract.
        assert getNumBands() == 1;
        return this;
    }

    /**
     * Retains inconditionnaly the first band of {@linkplain #image}. All other bands (if any) are discarted without any
     * further processing.
     *
     * @return this {@link ImageWorker}.
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
     * Retains unconditionally the last band of {@linkplain #image}. All other bands (if any) are discarded without any
     * further processing.
     *
     * <p>It is worth to point out that we use the true number of bands rather than the number of color components. This
     * means that if we apply this method on a colormapped image we get back the image itself untouched since it
     * originally contains 1 band although the color components are 3 or 4 as per the attached colormap.
     *
     * @return this {@link ImageWorker}.
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
     * Retains inconditionnaly the first {@code numBands} of {@linkplain #image}. All other bands (if any) are discarted
     * without any further processing. This method does nothing if the current {@linkplain #image} does not have a
     * greater amount of bands than {@code numBands}.
     *
     * @param numBands the number of bands to retain.
     * @return this {@link ImageWorker}.
     * @see #getNumBands
     * @see #retainFirstBand
     * @see BandSelectDescriptor
     */
    public final ImageWorker retainBands(final int numBands) {
        if (numBands <= 0) {
            throw new IndexOutOfBoundsException(
                    MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "numBands", numBands));
        }
        if (getNumBands() > numBands) {
            final int[] bands = new int[numBands];
            for (int i = 0; i < bands.length; i++) {
                bands[i] = i;
            }
            // ParameterBlock creation
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            pb.set(bands, 0);
            image = JAI.create("BandSelect", pb, getRenderingHints());
        }

        // All post conditions for this method contract.
        assert getNumBands() <= numBands;
        return this;
    }

    /**
     * Retains inconditionnaly certain bands of {@linkplain #image}. All other bands (if any) are discarded without any
     * further processing.
     *
     * @param bands the bands to retain.
     * @return this {@link ImageWorker}.
     * @see #getNumBands
     * @see #retainFirstBand
     * @see BandSelectDescriptor
     */
    public final ImageWorker retainBands(final int[] bands) {
        // ParameterBlock creation
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(bands, 0);
        image = JAI.create("BandSelect", pb, getRenderingHints());
        return this;
    }

    /**
     * Formats the underlying image to the provided data type.
     *
     * @param dataType to be used for this {@link FormatDescriptor} operation.
     * @return this {@link ImageWorker}
     */
    public final ImageWorker format(final int dataType) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        pb.set(dataType, 0);

        image = JAI.create("Format", pb, getRenderingHints());
        setNoData(RangeFactory.convert(nodata, dataType));

        // All post conditions for this method contract.
        assert image.getSampleModel().getDataType() == dataType;
        return this;
    }

    /**
     * Binarizes the {@linkplain #image}. If the image is multi-bands, then this method first computes an estimation of
     * its {@linkplain #intensity intensity}. Then, the threshold value is set halfway between the minimal and maximal
     * values found in the image.
     *
     * @return this {@link ImageWorker}.
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
     * Binarizes the {@linkplain #image}. If the image is already binarized, then this method does nothing.
     *
     * @param threshold The threshold value.
     * @return this {@link ImageWorker}.
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
            // ParameterBlock definition
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            pb.set(threshold, 0);
            pb.set(roi, 1);
            pb.set(nodata, 2);

            image = JAI.create("Binarize", pb, hints);

            setNoData(null);
            invalidateStatistics();
        }
        // All post conditions for this method contract.
        assert isBinary();
        return this;
    }

    /**
     * Binarizes the {@linkplain #image} (if not already done) and replace all 0 values by {@code value0} and all 1
     * values by {@code value1}. If the image should be binarized using a custom threshold value (instead of the
     * automatic one), invoke {@link #binarize(double)} explicitly before this method.
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
        final LookupTable table;
        final int min = Math.min(value0, value1);
        if (min >= 0) {
            final int max = Math.max(value0, value1);
            if (max < 256) {
                table = LookupTableFactory.create(new byte[] {(byte) value0, (byte) value1}, DataBuffer.TYPE_BYTE);
            } else if (max < 65536) {
                table = LookupTableFactory.create(new short[] {(short) value0, (short) value1}, true);
            } else {
                table = LookupTableFactory.create(new int[] {value0, value1});
            }
        } else {
            table = LookupTableFactory.create(new int[] {value0, value1}, DataBuffer.TYPE_BYTE);
        }
        // ParameterBlock definition
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(table, 0);
        pb.set(roi, 2);
        pb.set(nodata, 3);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 1);
            }
        }

        image = JAI.create("Lookup", pb, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Replaces all occurences of the given color (usually opaque) by a fully transparent color. Currents implementation
     * supports image backed by any {@link IndexColorModel}, or by {@link ComponentColorModel} with
     * {@link DataBuffer#TYPE_BYTE TYPE_BYTE}. More types may be added in future GeoTools versions.
     *
     * @param transparentColor The color to make transparent.
     * @return this image worker.
     * @throws IllegalStateException if the current {@linkplain #image} has an unsupported color model.
     */
    public final ImageWorker makeColorTransparent(final Color transparentColor) throws IllegalStateException {
        if (transparentColor == null) {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "transparentColor"));
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
        throw new IllegalStateException(ErrorKeys.UNSUPPORTED_DATA_TYPE);
    }

    /**
     * For an image backed by an {@link IndexColorModel}, replaces all occurences of the given color (usually opaque) by
     * a fully transparent color.
     *
     * @param transparentColor The color to make transparent.
     * @return this image worker.
     */
    private final ImageWorker maskIndexColorModel(final Color transparentColor) {
        assert image.getColorModel() instanceof IndexColorModel;

        // Gets informations about the provided images.
        IndexColorModel cm = (IndexColorModel) image.getColorModel();
        final int numComponents = cm.getNumComponents();
        int transparency = cm.getTransparency();
        int transparencyIndex = cm.getTransparentPixel();
        final int mapSize = cm.getMapSize();
        final int transparentRGB = transparentColor.getRGB() & 0x00FFFFFF;
        /*
         * Optimization in case of Transparency.BITMASK. If the color we want to use as the fully transparent one is the same that is actually used as
         * the transparent color, we leave doing nothing.
         */
        if (transparency == Transparency.BITMASK && transparencyIndex != -1) {
            int transpColor = cm.getRGB(transparencyIndex) & 0x00FFFFFF;
            if (transpColor == transparentRGB) {
                return this;
            }
        }
        /*
         * Find the index of the specified color. Most of the time, the color should appears only once, which will leads us to a BITMASK image.
         * However we allows more occurences, which will leads us to a TRANSLUCENT image.
         */
        final List<Integer> transparentPixelsIndexes = new ArrayList<>();
        for (int i = 0; i < mapSize; i++) {
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
        final byte[][] rgb = new byte[4][mapSize];
        cm.getReds(rgb[0]);
        cm.getGreens(rgb[1]);
        cm.getBlues(rgb[2]);
        if (numComponents == 4) {
            cm.getAlphas(rgb[3]);
        } else {
            Arrays.fill(rgb[3], (byte) 255);
        }
        if (transparency != Transparency.TRANSLUCENT) {
            cm = new IndexColorModel(cm.getPixelSize(), mapSize, rgb[0], rgb[1], rgb[2], transparencyIndex);
        } else {
            for (Integer transparentPixelsIndex : transparentPixelsIndexes) {
                rgb[3][transparentPixelsIndex] = (byte) 0;
            }
            cm = new IndexColorModel(cm.getPixelSize(), mapSize, rgb[0], rgb[1], rgb[2], rgb[3]);
        }

        // Format the input image.
        final ImageLayout layout = new ImageLayout(image);
        layout.setColorModel(cm);
        final RenderingHints hints = getRenderingHints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
        // ParameterBlock definition
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        pb.set(image.getSampleModel().getDataType(), 0);

        image = JAI.create("Format", pb, hints);
        setNoData(RangeFactory.convert(nodata, image.getSampleModel().getDataType()));
        invalidateStatistics();
        return this;
    }

    /**
     * For an image backed by an {@link ComponentColorModel}, replaces all occurences of the given color (usually
     * opaque) by a fully transparent color.
     *
     * @param transparentColor The color to make transparent.
     * @return this image worker.
     *     <p>Current implementation invokes a lot of JAI operations:
     *     <p>"BandSelect" --> "Lookup" --> "BandCombine" --> "Extrema" --> "Binarize" --> "Format" --> "BandSelect"
     *     (one more time) --> "Multiply" --> "BandMerge".
     *     <p>I would expect more speed and memory efficiency by writing our own JAI operation (PointOp subclass) doing
     *     that in one step. It would also be more deterministic (our "binarize" method depends on statistics on pixel
     *     values) and avoid unwanted side-effect like turning black color (RGB = 0,0,0) to transparent one. It would
     *     also be easier to maintain I believe.
     */
    private final ImageWorker maskComponentColorModelByte(final Color transparentColor) {
        assert image.getColorModel() instanceof ComponentColorModel;
        assert image.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE;
        /*
         * Prepares the look up table for the source image. Remember what follows which is taken from the JAI programming guide.
         *
         * "The lookup operation performs a general table lookup on a rendered or renderable image. The destination image is obtained by passing the
         * source image through the lookup table. The source image may be single- or multi-banded of data types byte, ushort, short, or int. The
         * lookup table may be single- or multi-banded of any JAI- supported data types.
         *
         * The destination image must have the same data type as the lookup table, and its number of bands is determined based on the number of bands
         * of the source and the table. If the source is single-banded, the destination has the same number of bands as the lookup table; otherwise,
         * the destination has the same number of bands as the source.
         *
         * If either the source or the table is single-banded and the other one is multibanded, the single band is applied to every band of the
         * multi-banded object. If both are multi-banded, their corresponding bands are matched up."
         *
         * A final annotation, if we have an input image with transparency we just DROP it since we want to re-add it using the supplied color as the
         * mask for transparency.
         */

        /*
         * In case of a gray color model we can do everything in one step by expanding the color model to get one more band directly which is the
         * alpha band itself.
         *
         * For a multiband image the lookup is applied to each band separately. This means that we cannot control directly the image as a whole but we
         * need first to interact with the single bands then to combine the result into a single band that will provide us with the alpha band.
         */
        int numBands = image.getSampleModel().getNumBands();
        final int numColorBands = image.getColorModel().getNumColorComponents();
        final RenderingHints hints = getRenderingHints();
        if (numColorBands != numBands) {
            // Typically, numColorBands will be equals to numBands-1.
            final int[] opaqueBands = new int[numColorBands];
            for (int i = 0; i < opaqueBands.length; i++) {
                opaqueBands[i] = i;
            }
            // ParameterBlock creation
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            pb.set(opaqueBands, 0);
            image = JAI.create("BandSelect", pb, hints);
            numBands = numColorBands;
        }

        // now prepare the lookups
        final byte[][] tableData = new byte[numColorBands][256];
        final boolean singleStep = numColorBands == 1;
        if (singleStep) {
            final byte[] data = tableData[0];
            Arrays.fill(data, (byte) 255);
            data[transparentColor.getRed()] = 0;
        } else {
            switch (numColorBands) {
                case 3:
                    Arrays.fill(tableData[2], (byte) 255);
                    tableData[2][transparentColor.getBlue()] = 0; // fall through

                case 2:
                    Arrays.fill(tableData[1], (byte) 255);
                    tableData[1][transparentColor.getGreen()] = 0; // fall through

                case 1:
                    Arrays.fill(tableData[0], (byte) 255);
                    tableData[0][transparentColor.getRed()] = 0; // fall through

                case 0:
                    break;
            }
        }
        // Create a LookupTableJAI object to be used with the "lookup" operator.
        LookupTable table =
                LookupTableFactory.create(tableData, image.getSampleModel().getDataType());
        // Do the lookup operation.
        // we should not transform on color map here
        hints.put(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.FALSE);
        // ParameterBlock definition
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(table, 0);
        pb.set(roi, 2);
        pb.set(nodata, 3);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 1);
            }
        }

        PlanarImage luImage = JAI.create("Lookup", pb, hints);
        // PlanarImage luImage = LookupDescriptor.create(image, table, hints);

        /*
         * Now that we have performed the lookup operation we have to remember what we stated here above.
         *
         * If the input image is multiband we will get a multiband image as the output of the lookup operation hence we need to perform some form of
         * band combination to get the alpha band out of the lookup image.
         *
         * The way we wanted things to be done is by exploiting the clamping behavior that kicks in when we do sums and the like on pixels and we
         * overcome the maximum value allowed by the DataBufer DataType.
         */
        if (!singleStep) {
            // We simply add the three generated bands together in order to get the right.
            final double[][] matrix = new double[1][4];
            // Values at index 0,1,2 are set to 1.0, value at index 3 is left to 0.
            Arrays.fill(matrix[0], 0, 3, 1.0);
            // ParameterBlock definition
            pb = new ParameterBlock();
            pb.setSource(luImage, 0);
            pb.set(matrix, 0);
            pb.set(roi, 1);
            pb.set(nodata, 2);
            if (background != null && background.length > 0) {
                pb.set(background[0], 3);
            }

            luImage = JAI.create("BandCombine", pb, getRenderingHints());
            // luImage = BandCombineDescriptor.create(luImage, matrix, hints);
        }
        pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.setSource(luImage, 1);
        pb.set(new Range[] {nodata}, 0);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                double dest = background[0];
                pb.set(dest, 1);
            }
        }
        pb.set(roi, 3);
        pb.set(true, 4);
        image = JAI.create("BandMerge", pb, hints);
        // image = BandMergeDescriptor.create(image, luImage, hints);

        invalidateStatistics();
        return this;
    }

    /**
     * Inverts the pixel values of the {@linkplain #image}.
     *
     * @see InvertDescriptor
     */
    public final ImageWorker invert() {
        // ParameterBlock creation
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        if (JAIExt.isJAIExtOperation(ALGEBRIC_OP_NAME)) {
            pb.set(AlgebraDescriptor.Operator.INVERT, 0);
            pb.set(roi, 1);
            pb.set(nodata, 2);
            if (isNoDataNeeded()) {
                if (background != null && background.length > 0) {
                    double dest = background[0];
                    pb.set(dest, 3);
                }
            }
            image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
        } else {
            image = JAI.create("Invert", pb, getRenderingHints());
        }
        invalidateStatistics();
        return this;
    }

    /**
     * Applies the specified mask over the current {@linkplain #image}. The mask should be {@linkplain #binarize()
     * binarized} - if it is not, this method will do it itself. Then, for every pixels in the mask with value equals to
     * {@code maskValue}, the corresponding pixel in the {@linkplain #image} will be set to the specified
     * {@code newValue}.
     *
     * <p><strong>Note:</strong> current implementation force the color model to an {@linkplain IndexColorModel indexed}
     * one. Future versions may avoid this change.
     *
     * @param mask The mask to apply, as a {@linkplain #binarize() binarized} image.
     * @param maskValue The mask value to search for ({@code false} for 0 or {@code true} for 1).
     * @param newValue The new value for every pixels in {@linkplain #image} corresponding to {@code maskValue} in the
     *     mask.
     * @return this {@link ImageWorker}.
     * @todo This now should work only if {@code newValue} is 255 and {@code maskValue} is {@code false}.
     */
    public final ImageWorker mask(RenderedImage mask, final boolean maskValue, int newValue) {

        /*
         * Make sure that the underlying image is indexed.
         */
        tileCacheEnabled(false);
        forceIndexColorModel(true);
        final RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, null);

        /*
         * special case for newValue == 255 && !maskValue.
         */
        if (newValue == 255 && !maskValue) {
            /*
             * Build a lookup table in order to make the transparent pixels equal to 255 and all the others equal to 0.
             */
            final byte[] lutData = new byte[256];
            // mapping all the non-transparent pixels to opaque
            Arrays.fill(lutData, (byte) 0);
            // for transparent pixels
            lutData[0] = (byte) 255;
            final LookupTable lut =
                    LookupTableFactory.create(lutData, mask.getSampleModel().getDataType());

            // ParameterBlock definition
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(mask, 0);
            pb.set(lut, 0);
            if (background != null && background.length > 0) {
                pb.set(background[0], 1);
            }
            pb.set(roi, 2);

            mask = JAI.create("Lookup", pb, hints);

            // mask = LookupDescriptor.create(mask, lut, hints);

            /*
             * Adding to the other image exploiting the implict clamping
             */
            pb = new ParameterBlock();
            pb.setSource(image, 0);
            pb.setSource(mask, 1);
            if (JAIExt.isJAIExtOperation(ALGEBRIC_OP_NAME)) {
                prepareAlgebricOperation(Operator.SUM, pb, roi, nodata, true);
                image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
            } else {
                image = JAI.create("Add", pb, getRenderingHints());
            }
            // image = AddDescriptor.create(image, mask, getRenderingHints());
            tileCacheEnabled(true);
            invalidateStatistics();
            return this;
        } else {
            // general case

            // it has to be binary
            if (!isBinary()) binarize();

            // Split between JAI and JAI-EXT operations
            boolean algebricJAIExt = JAIExt.isJAIExtOperation(ALGEBRIC_OP_NAME);
            boolean opConstJAIExt = JAIExt.isJAIExtOperation(OPERATION_CONST_OP_NAME);

            ParameterBlock pb;
            // now if we mask with 1 we have to invert the mask
            RenderingHints renderingHints = new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE);
            if (maskValue) {
                pb = new ParameterBlock();
                pb.setSource(mask, 0);
                if (algebricJAIExt) {
                    prepareAlgebricOperation(Operator.NOT, pb, roi, null, false);
                    mask = JAI.create(ALGEBRIC_OP_NAME, pb, renderingHints);
                } else {
                    mask = JAI.create("Not", pb, renderingHints);
                }
            }
            // and with the image to zero the interested pixels
            tileCacheEnabled(false);
            pb = new ParameterBlock();
            pb.setSource(mask, 0);
            pb.setSource(image, 1);
            if (algebricJAIExt) {
                prepareAlgebricOperation(Operator.AND, pb, roi, nodata, true);
                image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
            } else {
                image = JAI.create("And", pb, getRenderingHints());
            }
            // image = AndDescriptor.create(mask, image, getRenderingHints());

            // add the new value to the mask
            pb = new ParameterBlock();
            pb.setSource(mask, 0);
            if (opConstJAIExt) {
                prepareOpConstOperation(Operator.SUM, new double[] {newValue}, pb, roi, null, false);
                image = JAI.create(OPERATION_CONST_OP_NAME, pb, renderingHints);
            } else {
                image = JAI.create("AddConst", pb, renderingHints);
            }
            // mask = AddConstDescriptor.create(mask, new double[] { newValue }, renderingHints);

            // add the mask to the image to mask with the new value
            pb = new ParameterBlock();
            pb.setSource(mask, 0);
            pb.setSource(image, 1);
            if (algebricJAIExt) {
                prepareAlgebricOperation(Operator.SUM, pb, roi, nodata, true);
                image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
            } else {
                image = JAI.create("Add", pb, getRenderingHints());
            }
            // image = AddDescriptor.create(mask, image, getRenderingHints());
            tileCacheEnabled(true);
            invalidateStatistics();
            return this;
        }
    }

    /**
     * Copmputes a pixel-by-pixel maximum value from all the provided sources. The images must share the same raster
     * space.
     */
    public ImageWorker max(RenderedImage[] sources) {
        ParameterBlock pb = new ParameterBlock();
        for (int i = 0; i < sources.length; i++) {
            pb.setSource(sources[i], i);
        }
        prepareAlgebricOperation(Operator.MAX, pb, roi, nodata, true);
        image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
        return this;
    }

    /**
     * Copmputes a pixel-by-pixel minimum value from all the provided sources. The images must share the same raster
     * space.
     */
    public ImageWorker min(RenderedImage[] sources) {
        ParameterBlock pb = new ParameterBlock();
        for (int i = 0; i < sources.length; i++) {
            pb.setSource(sources[i], i);
        }
        prepareAlgebricOperation(Operator.MIN, pb, roi, nodata, true);
        image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
        return this;
    }

    private void prepareAlgebricOperation(
            Operator op, ParameterBlock pb, ROI roi, Range nodata, boolean setDestNoData) {
        pb.set(op, 0);
        pb.set(roi, 1);
        pb.set(nodata, 2);
        if (background != null && background.length > 0) {
            pb.set(background[0], 3);
            // We must set the new NoData value
            if (setDestNoData && roi != null && nodata != null) {
                setNoData(RangeFactory.create(background[0], background[0]));
            }
        }
    }

    private void prepareOpConstOperation(
            Operator op, double[] values, ParameterBlock pb, ROI roi, Range nodata, boolean setDestNoData) {
        pb.set(op, 1);
        pb.set(values, 0);
        pb.set(roi, 2);
        pb.set(nodata, 3);
        if (background != null && background.length > 0) {
            pb.set(background[0], 4);
            // We must set the new NoData value
            if (setDestNoData && roi != null && nodata != null) {
                setNoData(RangeFactory.create(background[0], background[0]));
            }
        }
    }

    /**
     * Takes two rendered or renderable source images, and adds every pair of pixels, one from each source image of the
     * corresponding position and band. See JAI {@link AddDescriptor} for details.
     *
     * @param renderedImage the {@link RenderedImage} to be added to this {@link ImageWorker}.
     * @return this {@link ImageWorker}.
     * @see AddDescriptor
     */
    public final ImageWorker addImage(final RenderedImage renderedImage) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.setSource(renderedImage, 1);
        if (JAIExt.isJAIExtOperation(ALGEBRIC_OP_NAME)) {
            prepareAlgebricOperation(Operator.SUM, pb, roi, nodata, true);
            image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
        } else {
            image = JAI.create("Add", pb, getRenderingHints());
        }
        // image = AddDescriptor.create(image, renderedImage, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Takes one rendered or renderable image and an array of double constants, and multiplies every pixel of the same
     * band of the source by the constant from the corresponding array entry. See JAI {@link MultiplyConstDescriptor}
     * for details.
     *
     * @param inValues The constants to be multiplied.
     * @return this {@link ImageWorker}.
     * @see MultiplyConstDescriptor
     */
    public final ImageWorker multiplyConst(double[] inValues) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        if (JAIExt.isJAIExtOperation(OPERATION_CONST_OP_NAME)) {
            prepareOpConstOperation(Operator.MULTIPLY, inValues, pb, roi, nodata, true);
            image = JAI.create(OPERATION_CONST_OP_NAME, pb, getRenderingHints());
        } else {
            image = JAI.create("MultiplyConst", pb, getRenderingHints());
        }
        // image = MultiplyConstDescriptor.create(image, inValues, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Takes two rendered or renderable source images, and myltiply form each pixel the related value of the second
     * image, each one from each source image of the corresponding position and band. See JAI {@link MultiplyDescriptor}
     * for details.
     *
     * @param renderedImage the {@link RenderedImage} to be multiplied to this {@link ImageWorker}.
     * @return this {@link ImageWorker}.
     * @see MultiplyDescriptor
     */
    public final ImageWorker multiply(RenderedImage renderedImage) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.setSource(renderedImage, 1);
        if (JAIExt.isJAIExtOperation(ALGEBRIC_OP_NAME)) {
            prepareAlgebricOperation(Operator.MULTIPLY, pb, roi, nodata, true);
            image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
        } else {
            image = JAI.create("Multiply", pb, getRenderingHints());
        }
        invalidateStatistics();
        return this;
    }

    /**
     * Takes one rendered or renderable image and an array of integer constants, and performs a bit-wise logical "xor"
     * between every pixel in the same band of the source and the constant from the corresponding array entry. See JAI
     * {@link XorConstDescriptor} for details.
     *
     * @see XorConstDescriptor
     */
    public final ImageWorker xorConst(int[] values) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        if (JAIExt.isJAIExtOperation(OPERATION_CONST_OP_NAME)) {
            double[] valuesD = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                valuesD[i] = values[i];
            }
            prepareOpConstOperation(Operator.XOR, valuesD, pb, roi, nodata, true);
            image = JAI.create(OPERATION_CONST_OP_NAME, pb, getRenderingHints());
        } else {
            image = JAI.create("XorConst", pb, getRenderingHints());
        }
        // image = XorConstDescriptor.create(image, values, getRenderingHints());
        invalidateStatistics();
        return this;
    }

    /**
     * Takes two rendered or renderable source images, and subtract form each pixel the related value of the second
     * image, each one from each source image of the corresponding position and band. See JAI {@link AddDescriptor} for
     * details.
     *
     * @param renderedImage the {@link RenderedImage} to be subtracted to this {@link ImageWorker}.
     * @return this {@link ImageWorker}.
     * @see SubtractDescriptor
     */
    public final ImageWorker subtract(final RenderedImage renderedImage) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.setSource(renderedImage, 1);
        if (JAIExt.isJAIExtOperation(ALGEBRIC_OP_NAME)) {
            prepareAlgebricOperation(Operator.SUBTRACT, pb, roi, nodata, true);
            image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
        } else {
            image = JAI.create("Subtract", pb, getRenderingHints());
        }
        invalidateStatistics();
        return this;
    }

    /**
     * Takes two rendered or renderable source images, and do an OR for each pixel images, each one from each source
     * image of the corresponding position and band. See JAI {@link AddDescriptor} for details.
     *
     * @param renderedImage the {@link RenderedImage} to be subtracted to this {@link ImageWorker}.
     * @return this {@link ImageWorker}.
     * @see SubtractDescriptor
     */
    public final ImageWorker or(final RenderedImage renderedImage) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.setSource(renderedImage, 1);
        if (JAIExt.isJAIExtOperation(ALGEBRIC_OP_NAME)) {
            prepareAlgebricOperation(Operator.OR, pb, roi, nodata, true);
            image = JAI.create(ALGEBRIC_OP_NAME, pb, getRenderingHints());
        } else {
            image = JAI.create("Or", pb, getRenderingHints());
        }
        invalidateStatistics();
        return this;
    }

    public final ImageWorker artifactsFilter(int threshold, int filterSize) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(roi, 0);
        pb.set(background, 1);
        pb.set(threshold, 2);
        pb.set(filterSize, 3);
        pb.set(nodata, 4);
        invalidateStatistics();
        return this;
    }

    /**
     * Adds transparency to a preexisting image whose color model is {@linkplain IndexColorModel index color model}. For
     * all pixels with the value {@code false} in the specified transparency mask, the corresponding pixel in the
     * {@linkplain #image} is set to the transparent pixel value. All other pixels are left unchanged.
     *
     * @param alphaChannel The mask to apply as a {@linkplain #binarize() binarized} image.
     * @param errorDiffusion Tells if I should use {@link ErrorDiffusionDescriptor} or {@link OrderedDitherDescriptor}
     *     JAi operations.
     * @return this {@link ImageWorker}.
     * @see #isTranslucent
     * @see #forceBitmaskIndexColorModel
     */
    public ImageWorker addTransparencyToIndexColorModel(
            final RenderedImage alphaChannel, final boolean errorDiffusion) {
        addTransparencyToIndexColorModel(alphaChannel, true, getTransparentPixel(), errorDiffusion);
        return this;
    }

    /**
     * Adds transparency to a preexisting image whose color model is {@linkplain IndexColorModel index color model}.
     * First, this method creates a new index color model with the specified {@code transparent} pixel, if needed (this
     * method may skip this step if the specified pixel is already transparent. Then for all pixels with the value
     * {@code false} in the specified transparency mask, the corresponding pixel in the {@linkplain #image} is set to
     * that transparent value. All other pixels are left unchanged.
     *
     * @param alphaChannel The mask to apply as a {@linkplain #binarize() binarized} image.
     * @param translucent {@code true} if {@linkplain Transparency#TRANSLUCENT translucent} images are allowed, or
     *     {@code false} if the resulting images must be a {@linkplain Transparency#BITMASK bitmask}.
     * @param transparent The value for transparent pixels, to be given to every pixels in the {@linkplain #image}
     *     corresponding to {@code false} in the mask. The special value {@code -1} maps to the last pixel value allowed
     *     for the {@linkplain IndexedColorModel indexed color model}.
     * @param errorDiffusion Tells if I should use {@link ErrorDiffusionDescriptor} or {@link OrderedDitherDescriptor}
     *     JAi operations.
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker addTransparencyToIndexColorModel(
            final RenderedImage alphaChannel,
            final boolean translucent,
            int transparent,
            final boolean errorDiffusion) {
        tileCacheEnabled(false);
        forceIndexColorModel(errorDiffusion);
        tileCacheEnabled(true);
        /*
         * Prepares hints and layout to use for mask operations. A color model hint will be set only if the block below is executed.
         */
        final ImageWorker worker = fork(image);
        final RenderingHints hints = worker.getRenderingHints();
        /*
         * Gets the index color model. If the specified 'transparent' value is not fully transparent, replaces the color model by a new one with the
         * transparent pixel defined. NOTE: the "transparent &= (1 << pixelSize) - 1" instruction below is a safety for making sure that the
         * transparent index value can hold in the amount of bits allowed for this color model (the mapSize value may not use all bits). It works as
         * expected with the -1 special value. It also make sure that "transparent + 1" do not exeed the maximum map size allowed.
         */
        final IndexColorModel oldCM = (IndexColorModel) image.getColorModel();
        final int pixelSize = oldCM.getPixelSize();
        transparent &= (1 << pixelSize) - 1;
        final boolean forceBitmask = !translucent && oldCM.getTransparency() == Transparency.TRANSLUCENT;
        if (forceBitmask || oldCM.getTransparentPixel() != transparent) {
            final int mapSize = Math.max(oldCM.getMapSize(), transparent + 1);
            final byte[][] RGBA = new byte[translucent ? 4 : 3][mapSize];
            // Note: we might use less that 256 values.
            oldCM.getReds(RGBA[0]);
            oldCM.getGreens(RGBA[1]);
            oldCM.getBlues(RGBA[2]);
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
        assert image.getColorModel().getAlpha(transparent) == 0;
        return this;
    }

    /**
     * If the was not already tiled, tile it. Note that no tiling will be done if 'getRenderingHints()' failed to
     * suggest a tile size. This method is for internal use by {@link #write} methods only.
     *
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker tile() {
        final RenderingHints hints = getRenderingHints();
        final ImageLayout layout = getImageLayout(hints);
        if (layout.isValid(ImageLayout.TILE_WIDTH_MASK) || layout.isValid(ImageLayout.TILE_HEIGHT_MASK)) {
            final int type = image.getSampleModel().getDataType();
            // ParameterBlock definition
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0); // The source image.
            pb.set(type, 0);

            image = JAI.create("Format", pb, hints);
            setNoData(RangeFactory.convert(nodata, type));
        }
        return this;
    }

    /**
     * Applies the specified opacity to the image by either adding an alpha band, or modifying the existing one by
     * multiplication
     *
     * @param opacity The opacity to be applied, between 0 and 1
     * @return this {@link ImageWorker}.
     */
    public ImageWorker applyOpacity(float opacity) {
        RenderedImage result;
        ColorModel colorModel = image.getColorModel();

        // if it's an index color model we can just recompute the palette
        // and replace it
        if (colorModel instanceof IndexColorModel) {
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
                if (i == transparentPixel) {
                    alphas[i] = 0;
                }
            }

            // build a new palette
            IndexColorModel newColorModel =
                    new IndexColorModel(index.getPixelSize(), index.getMapSize(), reds, greens, blues, alphas);
            LookupTable table =
                    buildOpacityLookupTable(0, 1, -1, image.getSampleModel().getDataType());
            ImageLayout layout = new ImageLayout(image);
            layout.setColorModel(newColorModel);
            RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);

            // ParameterBlock definition
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(image, 0);
            pb.set(table, 0);
            pb.set(roi, 2);
            pb.set(nodata, 3);
            if (isNoDataNeeded()) {
                if (background != null && background.length > 0) {
                    pb.set(background[0], 1);
                }
            }

            result = JAI.create("Lookup", pb, hints);
            // result = LookupDescriptor.create(image, table, hints);
        } else {
            // not indexed, then make sure it's some sort of component color model or turn it into
            // one
            RenderedImage expanded;
            if (!(colorModel instanceof ComponentColorModel)) {
                expanded = new ImageWorker(image).forceComponentColorModel().getRenderedImage();
            } else {
                expanded = image;
            }

            // do we have to add the alpha band or it's there and we need to change it?

            if (!expanded.getColorModel().hasAlpha()) {
                // we just need to add it, so first build a constant image with the same structure
                // as the original image
                byte alpha = (byte) Math.round(255 * opacity);
                ImageLayout layout =
                        new ImageLayout(image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
                RenderedOp alphaBand = ConstantDescriptor.create(
                        (float) image.getWidth(),
                        (float) image.getHeight(),
                        new Byte[] {Byte.valueOf(alpha)},
                        new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));

                ParameterBlock pb = new ParameterBlock();
                pb.setSource(expanded, 0);
                pb.setSource(alphaBand, 1);
                pb.set(new Range[] {nodata, nodata == null ? null : RangeFactory.create(alpha - 1, alpha - 1)}, 0);
                if (isNoDataNeeded()) {
                    if (background != null && background.length > 0) {
                        double dest = background[0];
                        pb.set(dest, 1);
                    }
                }
                pb.set(roi, 3);
                pb.set(true, 4);
                result = JAI.create("BandMerge", pb, null);
                // result = BandMergeDescriptor.create(expanded, alphaBand, null);
            } else {
                // we need to transform the existing, we'll use a lookup
                final int bands = expanded.getSampleModel().getNumBands();
                int alphaBand = bands - 1;
                // ParameterBlock definition
                ParameterBlock pb = new ParameterBlock();
                pb.setSource(expanded, 0);
                LookupTable table = buildOpacityLookupTable(
                        opacity, bands, alphaBand, expanded.getSampleModel().getDataType());
                pb.set(table, 0);
                pb.set(roi, 2);
                pb.set(nodata, 3);
                if (isNoDataNeeded()) {
                    if (background != null && background.length > 0) {
                        pb.set(background[0], 1);
                    }
                }

                result = JAI.create("Lookup", pb, null);
                // LookupTableJAI table = buildOpacityLookupTable(opacity, bands, alphaBand);
                // result = LookupDescriptor.create(expanded, table, null);
            }
        }

        image = result;
        return this;
    }

    /** Builds a lookup table that is the identity on all bands but the alpha one, where the opacity is applied */
    LookupTable buildOpacityLookupTable(float opacity, final int bands, int alphaBand, int dataType) {
        byte[][] matrix = new byte[bands][256];
        for (int band = 0; band < matrix.length; band++) {
            if (band == alphaBand) {
                for (int i = 0; i < 256; i++) {
                    matrix[band][i] = (byte) Math.round(i * opacity);
                }
            } else {
                for (int i = 0; i < 256; i++) {
                    matrix[band][i] = (byte) i;
                }
            }
        }
        LookupTable table = LookupTableFactory.create(matrix, dataType);
        return table;
    }

    /**
     * Writes the {@linkplain #image} to the specified file. This method differs from {@link ImageIO#write(String,File)}
     * in a number of ways:
     *
     * <p>
     *
     * <ul>
     *   <li>The {@linkplain ImageWriter image writer} to use is inferred from the file extension.
     *   <li>If the image writer accepts {@link File} objects as input, then the {@code file} argument is given directly
     *       without creating an {@link ImageOutputStream} object. This is important for some formats like HDF, which
     *       work <em>only</em> with files.
     *   <li>If the {@linkplain #image} is not tiled, then it is tiled prior to be written.
     *   <li>If some special processing is needed for a given format, then the corresponding method is invoked. Example:
     *       {@link #forceIndexColorModelForGIF}.
     * </ul>
     *
     * @return this {@link ImageWorker}.
     */
    public final ImageWorker write(final File output) throws IOException {
        final String filename = output.getName();
        final int dot = filename.lastIndexOf('.');
        if (dot < 0) {
            throw new IIOException(ErrorKeys.NO_IMAGE_WRITER);
        }
        final String extension = filename.substring(dot + 1).trim();
        write(output, ImageIO.getImageWritersBySuffix(extension));
        return this;
    }

    /**
     * Writes outs the image contained into this {@link ImageWorker} as a PNG using the provided destination,
     * compression and compression rate.
     *
     * <p>The destination object can be anything providing that we have an {@link ImageOutputStreamSpi} that recognizes
     * it.
     *
     * @param destination where to write the internal {@link #image} as a PNG.
     * @param compression algorithm.
     * @param compressionRate percentage of compression.
     * @param nativeAcc should we use native acceleration.
     * @param paletted should we write the png as 8 bits?
     * @throws IOException In case an error occurs during the search for an {@link ImageOutputStream} or during the
     *     eoncding process.
     */
    // Current code doesn't check if the writer already accepts the provided destination. It
    // wraps it in a ImageOutputStream inconditionnaly.
    public final void writePNG(
            final Object destination,
            final String compression,
            final float compressionRate,
            final boolean nativeAcc,
            final boolean paletted)
            throws IOException {
        // Reformatting this image for PNG.
        final boolean hasPalette = image.getColorModel() instanceof IndexColorModel;
        final boolean hasColorModel = hasPalette ? false : image.getColorModel() instanceof ComponentColorModel;
        if (paletted && !hasPalette) {
            // we have to reduce colors
            forceIndexColorModelForGIF(true);
        } else {
            if (!hasColorModel && !hasPalette) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.fine("Forcing input image to be compatible with PNG: No palette, no component color model");
                }
                // png supports gray, rgb, rgba and paletted 8 bit, but not, for example, double and
                // float values, or 16 bits palettes
                forceComponentColorModel();
            }
        }

        // PNG does not support all kinds of index color models
        if (hasPalette) {
            IndexColorModel icm = (IndexColorModel) image.getColorModel();
            // PNG supports palettes with up to 256 colors, beyond that we have to expand to RGB
            if (icm.getMapSize() > 256) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.fine(
                            "Forcing input image to be compatible with PNG: Palette with > 256 color is not supported.");
                }
                rescaleToBytes();
                if (paletted) {
                    forceIndexColorModelForGIF(true);
                }
            }
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Encoded input image for png writer");
        }

        // Getting a writer.
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Getting a writer");
        }
        ImageWriter writer = null;
        ImageWriterSpi originatingProvider = null;
        // ImageIO
        if (nativeAcc) {
            if (CLIB_PNG_IMAGE_WRITER_SPI != null) {
                // let me check if the native writer can encode this image
                if (CLIB_PNG_IMAGE_WRITER_SPI.canEncodeImage(new ImageTypeSpecifier(image))) {
                    writer = CLIB_PNG_IMAGE_WRITER_SPI.createWriterInstance();
                    originatingProvider = CLIB_PNG_IMAGE_WRITER_SPI;

                } else {
                    LOGGER.fine("The ImageIO PNG native encode cannot encode this image!");
                    writer = null;
                    originatingProvider = null;
                }
            } else {
                LOGGER.fine("Unable to use Native ImageIO PNG writer.");
            }
        }

        // move on with the writer quest
        if (!nativeAcc || writer == null) {

            final Iterator<ImageWriter> it = ImageIO.getImageWriters(new ImageTypeSpecifier(image), "PNG");
            if (!it.hasNext()) {
                throw new IllegalStateException(ErrorKeys.NO_IMAGE_WRITER);
            }
            while (it.hasNext()) {
                writer = it.next();
                originatingProvider = writer.getOriginatingProvider();
                // check that this is not the native one
                if (CLIB_PNG_IMAGE_WRITER_SPI != null
                        && originatingProvider.getClass().equals(CLIB_PNG_IMAGE_WRITER_SPI.getClass())) {
                    if (it.hasNext()) {
                        writer = it.next();
                        originatingProvider = writer.getOriginatingProvider();
                    } else {
                        LOGGER.fine("Unable to use PNG writer different than ImageIO CLib one");
                    }
                }

                // let me check if the native writer can encode this image (paranoiac checks this
                // was already performed by the ImageIO search
                if (originatingProvider.canEncodeImage(new ImageTypeSpecifier(image))) {
                    break; // leave loop
                }

                // clean
                writer = null;
                originatingProvider = null;
            }
        }

        // ok, last resort use the JDK one and reformat the image
        if (writer == null) {
            List providers = com.sun.media.imageioimpl.common.ImageUtil.getJDKImageReaderWriterSPI(
                    IIORegistry.getDefaultInstance(), "PNG", false);
            if (providers == null || providers.isEmpty()) {
                throw new IllegalStateException("Unable to find JDK Png encoder!");
            }
            originatingProvider = (ImageWriterSpi) providers.get(0);
            writer = originatingProvider.createWriterInstance();

            // kk, last resort reformat the image
            forceComponentColorModel(true, true);
            rescaleToBytes();
            if (!originatingProvider.canEncodeImage(image)) {
                throw new IllegalArgumentException(
                        "Unable to find a valid PNG Encoder! And believe me, we tried hard!");
            }
        }

        LOGGER.fine("Using ImageIO Writer with SPI: "
                + originatingProvider.getClass().getCanonicalName());

        // Getting a stream.
        LOGGER.fine("Setting write parameters for this writer");

        ImageWriteParam iwp = null;
        try (ImageOutputStream memOutStream = ImageIOExt.createImageOutputStream(image, destination)) {
            if (memOutStream == null) {
                throw new IIOException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "stream"));
            }
            if (CLIB_PNG_IMAGE_WRITER_SPI != null
                    && originatingProvider.getClass().equals(CLIB_PNG_IMAGE_WRITER_SPI.getClass())) {
                // Compressing with native.
                LOGGER.fine("Writer is native");
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
                LOGGER.fine("Writer is NOT native");

                // Instantiating PNGImageWriteParam
                iwp = new PNGImageWriteParam();
                // Define compression mode
                iwp.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
            }
            LOGGER.fine("About to write png image");
            try {
                writer.setOutput(memOutStream);
                writer.write(null, new IIOImage(image, null, null), iwp);
            } finally {
                try {
                    writer.dispose();
                } catch (Throwable e) {
                    if (LOGGER.isLoggable(Level.FINEST)) LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /**
     * Writes outs the image contained into this {@link ImageWorker} as a GIF using the provided destination,
     * compression and compression rate.
     *
     * <p>It is worth to point out that the only compressions algorithm available with the jdk {@link GIFImageWriter} is
     * "LZW" while the compression rates have to be confined between 0 and 1. AN acceptable values is usally 0.75f.
     *
     * <p>The destination object can be anything providing that we have an {@link ImageOutputStreamSpi} that recognizes
     * it.
     *
     * @param destination where to write the internal {@link #image} as a gif.
     * @param compression The name of compression algorithm.
     * @param compressionRate percentage of compression, as a number between 0 and 1.
     * @return this {@link ImageWorker}.
     * @throws IOException In case an error occurs during the search for an {@link ImageOutputStream} or during the
     *     eoncding process.
     * @see #forceIndexColorModelForGIF(boolean)
     */
    public final ImageWorker writeGIF(final Object destination, final String compression, final float compressionRate)
            throws IOException {
        forceIndexColorModelForGIF(true);

        if (IMAGEIO_GIF_IMAGE_WRITER_SPI == null) {
            throw new IIOException(ErrorKeys.NO_IMAGE_WRITER);
        }
        try (ImageOutputStream stream = ImageIOExt.createImageOutputStream(image, destination)) {
            if (stream == null) throw new IIOException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "stream"));
            final ImageWriter writer = IMAGEIO_GIF_IMAGE_WRITER_SPI.createWriterInstance();
            final ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionType(compression);
            param.setCompressionQuality(compressionRate);

            writer.setOutput(stream);
            writer.write(null, new IIOImage(image, null, null), param);
        }
        return this;
    }

    /**
     * Writes outs the image contained into this {@link ImageWorker} as a JPEG using the provided destination ,
     * compression and compression rate.
     *
     * <p>The destination object can be anything providing that we have an {@link ImageOutputStreamSpi} that recognizes
     * it.
     *
     * @param destination where to write the internal {@link #image} as a JPEG.
     * @param compression algorithm.
     * @param compressionRate percentage of compression.
     * @param nativeAcc should we use native acceleration.
     * @throws IOException In case an error occurs during the search for an {@link ImageOutputStream} or during the
     *     eoncding process.
     */
    public final void writeJPEG(
            final Object destination, final String compression, final float compressionRate, final boolean nativeAcc)
            throws IOException {
        // Reformatting this image for jpeg.
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Encoding input image to write out as JPEG.");
        }

        // go to component color model if needed
        ColorModel cm = image.getColorModel();
        final boolean hasAlpha = cm.hasAlpha();
        forceComponentColorModel(false, true, true);
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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Getting a JPEG writer and configuring it.");
        }
        ImageWriter writer = null;
        if (nativeAcc && CODEC_LIB_AVAILABLE && IMAGEIO_JPEG_IMAGE_WRITER_SPI != null) {
            try {
                writer = IMAGEIO_JPEG_IMAGE_WRITER_SPI.createWriterInstance();
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Unable to instantiate CLIB JPEG ImageWriter", e);
                }
                writer = null;
            }
        }
        // in case we want the JDK one or in case the native one is not at hand we use the JDK one
        if (writer == null) {
            if (JDK_JPEG_IMAGE_WRITER_SPI == null) {
                throw new IllegalStateException(
                        MessageFormat.format(ErrorKeys.ILLEGAL_CLASS_$2, "Unable to find JDK JPEG Writer"));
            }
            writer = JDK_JPEG_IMAGE_WRITER_SPI.createWriterInstance();
        }

        // Compression is available on both lib
        final ImageWriteParam iwp = writer.getDefaultWriteParam();

        try (ImageOutputStream outStream = ImageIOExt.createImageOutputStream(image, destination)) {
            if (outStream == null) {
                throw new IIOException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "stream"));
            }

            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionType(compression); // Lossy compression.
            iwp.setCompressionQuality(compressionRate); // We can control quality here.
            if (iwp instanceof JPEGImageWriteParam) {
                final JPEGImageWriteParam param = (JPEGImageWriteParam) iwp;
                param.setOptimizeHuffmanTables(true);
                try {
                    param.setProgressiveMode(JPEGImageWriteParam.MODE_DEFAULT);
                } catch (UnsupportedOperationException e) {
                    throw new IOException(e);
                }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Writing out...");
            }

            try {

                writer.setOutput(outStream);
                // the JDK writer has problems with images that do not start at minx==miny==0
                // while the clib writer has issues with tiled images
                if (!nativeAcc && (image.getMinX() != 0 || image.getMinY() != 0)
                        || nativeAcc && (image.getNumXTiles() > 1 || image.getNumYTiles() > 1)) {
                    final BufferedImage finalImage = new BufferedImage(
                            image.getColorModel(),
                            ((WritableRaster) image.getData()).createWritableTranslatedChild(0, 0),
                            image.getColorModel().isAlphaPremultiplied(),
                            null);

                    writer.write(null, new IIOImage(finalImage, null, null), iwp);
                } else {
                    writer.write(null, new IIOImage(image, null, null), iwp);
                }
            } finally {
                try {
                    writer.dispose();
                } catch (Throwable e) {
                    if (LOGGER.isLoggable(Level.FINEST)) LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
                }
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Writing out... Done!");
                }
            }
        }
    }

    /**
     * Writes outs the image contained into this {@link ImageWorker} as a TIFF using the provided destination,
     * compression and compression rate and basic tiling information
     *
     * <p>The destination object can be anything providing that we have an {@link ImageOutputStreamSpi} that recognizes
     * it.
     *
     * @param destination where to write the internal {@link #image} as a TIFF.
     * @param compression algorithm.
     * @param compressionRate percentage of compression.
     * @param tileSizeX tile size x direction (or -1 if tiling is not desired)
     * @param tileSizeY tile size y direction (or -1 if tiling is not desired)
     * @throws IOException In case an error occurs during the search for an {@link ImageOutputStream} or during the
     *     eoncding process.
     */
    public final void writeTIFF(
            final Object destination,
            final String compression,
            final float compressionRate,
            final int tileSizeX,
            final int tileSizeY)
            throws IOException {
        // Reformatting this image for jpeg.
        if (LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Encoding input image to write out as TIFF.");

        // Getting a writer.
        if (LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Getting a TIFF writer and configuring it.");
        ImageWriter writer = null;
        if (IMAGEIO_EXT_TIFF_IMAGE_WRITER_SPI == null) {
            // our own is not there, strange... this should not happen
            LOGGER.finer("Unable to find ImageIO-Ext Tiff Writer, looking for another one");
            final Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("TIFF");
            if (!it.hasNext()) {
                throw new IllegalStateException(ErrorKeys.NO_IMAGE_WRITER);
            }
            writer = it.next();
        } else {
            writer = IMAGEIO_EXT_TIFF_IMAGE_WRITER_SPI.createWriterInstance();
        }

        // checks
        if (writer == null) {
            throw new IllegalStateException("Unable to find Tiff ImageWriter!");
        }

        final ImageWriteParam iwp = writer.getDefaultWriteParam();
        try (ImageOutputStream outStream = ImageIOExt.createImageOutputStream(image, destination)) {
            if (outStream == null) {
                throw new IIOException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "stream"));
            }

            if (compression != null) {
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionType(compression);
                iwp.setCompressionQuality(compressionRate); // We can control quality here.
            } else {
                iwp.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
            }
            if (tileSizeX > 0 && tileSizeY > 0) {
                iwp.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setTiling(tileSizeX, tileSizeY, 0, 0);
            }

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("Writing out...");
            }

            try {

                writer.setOutput(outStream);
                writer.write(null, new IIOImage(image, null, null), iwp);
            } finally {
                try {
                    writer.dispose();
                } catch (Throwable e) {
                    if (LOGGER.isLoggable(Level.FINEST)) LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /**
     * Performs an affine transform on the image, applying optimization such as affine removal in case the affine is an
     * identity, affine merging if the affine is applied on top of another affine, and using optimized operations for
     * integer translates
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
        if (!hasScaleX && !hasScaleY && !hasShearX && !hasShearY && !hasTranslateX && !hasTranslateY) {
            return this;
        }

        // apply defaults to allow for comparisong
        ParameterListDescriptor pld =
                getOperationDescriptor("affine").getParameterListDescriptor(RenderedRegistryMode.MODE_NAME);
        if (interpolation == null) {
            interpolation = (Interpolation) pld.getParamDefaultValue("interpolation");
        }
        if (bgValues == null) {
            if (background == null || background.length <= 0) {
                bgValues = (double[]) pld.getParamDefaultValue("backgroundValues");
            } else {
                bgValues = background;
            }
        }
        // Setting the new backgroung values
        background = bgValues;

        // affine over affine/scale?
        RenderedImage source = image;
        if (image instanceof RenderedOp) {
            RenderedOp op = (RenderedOp) image;

            Object mtProperty = op.getProperty("MathTransform");
            Object sourceBoundsProperty = op.getProperty("SourceBoundingBox");
            String opName = op.getOperationName();

            // check if we can do a warp-affine reduction
            final ParameterBlock sourceParamBlock = op.getParameterBlock();

            boolean preserveChainedAffines = false;
            Object preserveHints = getRenderingHint(PRESERVE_CHAINED_AFFINES);
            if (preserveHints != null && preserveHints instanceof Boolean) {
                preserveChainedAffines = (Boolean) preserveHints;
            }
            if (WARP_REDUCTION_ENABLED
                    && "Warp".equals(opName)
                    && mtProperty instanceof MathTransform2D
                    && sourceBoundsProperty instanceof Rectangle
                    && !preserveChainedAffines) {
                try {
                    WarpAffineReducer warpAffineReducer =
                            new WarpAffineReducer(tx, interpolation, bgValues, op).invoke();
                    if (warpAffineReducer.reduced()) return this;
                    bgValues = warpAffineReducer.getBgValues();
                } catch (Exception e) {
                    LOGGER.log(
                            Level.WARNING,
                            "Failed to squash warp and affine into a single operation, chaining them instead",
                            e);
                    // move on
                }
            }

            // see if we can merge affine with other affine types then
            if ("Affine".equals(opName) && !preserveChainedAffines) {
                ParameterBlock paramBlock = sourceParamBlock;
                RenderedImage sSource = paramBlock.getRenderedSource(0);

                AffineTransform sTx = (AffineTransform) paramBlock.getObjectParameter(0);
                Interpolation sInterp = (Interpolation) paramBlock.getObjectParameter(1);
                double[] sBgValues = (double[]) paramBlock.getObjectParameter(2);

                Range nodata = null;
                ROI r = null;
                boolean similarROI = true;
                boolean hasSameNodata = true;
                // Minor checks on ROI and NoData
                if (paramBlock.getNumParameters() > 3) {
                    nodata = (Range) paramBlock.getObjectParameter(6);
                    r = (ROI) paramBlock.getObjectParameter(3);
                    if (r != null) {
                        try {
                            AffineTransform inverse = sTx.createInverse();
                            ROI newROI = this.roi != null ? this.roi.transform(inverse) : null;
                            similarROI = newROI != null && newROI.intersects(r.getBounds());
                        } catch (NoninvertibleTransformException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                        hasSameNodata = nodata == null
                                || sBgValues != null
                                        && this.nodata != null
                                        && sBgValues.length > 0
                                        && sBgValues[0] == this.nodata.getMin().doubleValue();
                    }
                }

                if (sInterp == interpolation
                        && Arrays.equals(sBgValues, bgValues)
                        && (nodata == null || hasSameNodata)
                        && (r == null || similarROI)) {
                    // we can replace it
                    AffineTransform concat = new AffineTransform(tx);
                    concat.concatenate(sTx);
                    tx = concat;
                    source = sSource;
                    if (similarROI && r != null) {
                        try {
                            AffineTransform inverse = sTx.createInverse();
                            ROI newROI = this.roi != null ? this.roi.transform(inverse) : null;
                            this.roi = newROI != null ? newROI.intersect(r) : null;
                        } catch (NoninvertibleTransformException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                    if (hasSameNodata && nodata != null) {
                        setNoData(nodata);
                    }
                }
            } else if ((SCALE_NAME.equals(opName) || SCALE2_NAME.equals(opName)) && !preserveChainedAffines) {
                boolean isScale2 = SCALE2_NAME.equals(opName);
                ParameterBlock paramBlock = sourceParamBlock;
                RenderedImage sSource = paramBlock.getRenderedSource(0);

                double[] scalingParams = getScalingParams(paramBlock, isScale2);
                Interpolation sInterp = (Interpolation) paramBlock.getObjectParameter(4);

                Range nodata = null;
                ROI r = null;
                boolean similarROI = true;
                boolean hasSameNodata = true;
                // Minor checks on ROI and NoData
                final int numParameters = paramBlock.getNumParameters();
                if (numParameters > 5) {
                    r = (ROI) paramBlock.getObjectParameter(5);
                    nodata = numParameters > 7 ? (Range) paramBlock.getObjectParameter(7) : null;
                    // The background may haven't been set
                    double[] sBgValues = numParameters > 8 ? (double[]) paramBlock.getObjectParameter(8) : null;
                    if (r != null) {
                        try {
                            ROI newROI = computeScaledROI(scalingParams);
                            similarROI = newROI != null && newROI.intersects(r.getBounds());
                        } catch (NoninvertibleTransformException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                    hasSameNodata = nodata == null
                            || sBgValues != null
                                    && this.nodata != null
                                    && sBgValues.length > 0
                                    && sBgValues[0] == this.nodata.getMin().doubleValue();
                }

                if (sInterp == interpolation && (nodata == null || hasSameNodata) && (r == null || similarROI)) {
                    // we can replace it
                    AffineTransform concat = new AffineTransform(tx);
                    concat.concatenate(new AffineTransform(
                            scalingParams[0], 0, 0, scalingParams[1], scalingParams[2], scalingParams[3]));
                    tx = concat;
                    source = sSource;
                    if (similarROI && r != null) {
                        try {
                            ROI newROI = computeScaledROI(scalingParams);
                            this.roi = newROI.intersect(r);
                        } catch (NoninvertibleTransformException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                    if (hasSameNodata && nodata != null) {
                        setNoData(nodata);
                    }
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
        boolean intTranslateX = Math.abs(tx.getTranslateX() - Math.round(tx.getTranslateX())) < RS_EPS;
        boolean intTranslateY = Math.abs(tx.getTranslateY() - Math.round(tx.getTranslateY())) < RS_EPS;
        boolean nonNegativeScaleX = tx.getScaleX() >= 0;
        boolean nonNegativeScaleY = tx.getScaleY() >= 0;

        // did it become a identity after the combination?
        if (!hasScaleX && !hasScaleY && !hasShearX && !hasShearY && !hasTranslateX && !hasTranslateY) {
            this.image = source;
            return this;
        }
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(source, 0);
        if (!hasShearX && !hasShearY && nonNegativeScaleX && nonNegativeScaleY) {
            if (!hasScaleX && !hasScaleY && intTranslateX && intTranslateY) {
                // this will do an integer translate, but to get there we need to remove the image
                // layout
                Hints localHints = new Hints(getRenderingHints());
                localHints.remove(JAI.KEY_IMAGE_LAYOUT);
                double[] scalingParams = {1.0, 1.0, Math.round(tx.getTranslateX()), Math.round(tx.getTranslateY())};
                scale(pb, scalingParams, interpolation, localHints);
                updateNoData(background, image);
                updateROI(false, SCALE_OP_NAME);
            } else {
                // generic scale
                double[] scalingParams = {tx.getScaleX(), tx.getScaleY(), tx.getTranslateX(), tx.getTranslateY()};
                scale(pb, scalingParams, interpolation, getRenderingHints());
                updateNoData(background, image);
                updateROI(false, SCALE_OP_NAME);
            }
        } else {
            pb.set(tx, 0);
            pb.set(interpolation, 1);
            pb.set(bgValues, 2);
            pb.set(roi, 3);
            pb.set(true, 5);
            pb.set(nodata, 6);
            image = JAI.create("Affine", pb, getRenderingHints());
            updateNoData(bgValues, image);
            updateROI(false, "Affine");
        }
        return this;
    }

    /** Perform scaling */
    private void scale(ParameterBlock pb, double[] scalingParams, Interpolation interpolation, RenderingHints hints) {
        for (int i = 0; i < 4; i++) {
            if (USE_JAI_SCALE2) {
                pb.set(scalingParams[i], i);
            } else {
                pb.set((float) scalingParams[i], i);
            }
        }
        RenderedImage sourceImage = pb.getRenderedSource(0);
        ColorModel cm = sourceImage.getColorModel();
        SampleModel sm = sourceImage.getSampleModel();
        final int numBands = sm.getNumBands();
        RenderedImage alphaChannel = null;

        // When interpolation is not nearest, Alpha should not be interpolated the same way
        // as normal bands to avoid partial transparencies
        if (cm.hasAlpha()
                && (numBands == 2 || numBands == 4)
                && interpolation != null
                && (interpolation.getWidth() > 1 || interpolation.getHeight() > 1)) {

            // Extract Alpha for future re-attach
            ImageWorker noAlpha =
                    new ImageWorker(sourceImage).setRenderingHints(hints).retainBands(numBands - 1);
            alphaChannel = new ImageWorker(sourceImage)
                    .setRenderingHints(hints)
                    .retainLastBand()
                    .getRenderedImage();
            pb.setSource(noAlpha.getRenderedImage(), 0);
        }

        pb.set(interpolation, 4);
        pb.set(roi, 5);
        pb.set(nodata, 7);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background, 8);
            }
        }
        RenderedImage scaledImage = JAI.create(SCALE_OP_NAME, pb, hints);
        image = scaledImage;
        if (alphaChannel != null) {
            // Need to scale alpha separately (with nearest interpolation)
            // to avoid partial transparency
            ParameterBlock pb2 = new ParameterBlock();
            pb2.setSource(alphaChannel, 0);
            for (int i = 0; i < 4; i++) {
                if (USE_JAI_SCALE2) {
                    pb2.set(scalingParams[i], i);
                } else {
                    pb2.set((float) scalingParams[i], i);
                }
            }
            pb2.set(Interpolation.getInstance(Interpolation.INTERP_NEAREST), 4);
            pb2.set(roi, 5);
            pb2.set(nodata, 7);
            alphaChannel = JAI.create(SCALE_OP_NAME, pb2, hints);
            // Now, re-attach the scaled alpha to the scaled image
            ImageWorker merged = prepareForScaledAlphaChannel(scaledImage, hints, cm, sm);
            image = merged.addBand(alphaChannel, false, true, null).getRenderedImage();
        }
    }

    /** Reattach scaled alpha channel with separate layout. */
    ImageWorker prepareForScaledAlphaChannel(
            RenderedImage scaledImage, RenderingHints hints, ColorModel cm, SampleModel sm) {
        ImageWorker merged = new ImageWorker(scaledImage);
        Object candidate = hints.get(JAI.KEY_IMAGE_LAYOUT);
        if (candidate instanceof ImageLayout) {
            ImageLayout layout = (ImageLayout) candidate;
            if (layout.getTileWidth(null) > 0 && layout.getTileHeight(null) > 0) {
                ImageLayout layout2 = new ImageLayout2(
                        layout.getTileGridXOffset(null),
                        layout.getTileGridYOffset(null),
                        layout.getTileWidth(null),
                        layout.getTileHeight(null),
                        sm,
                        cm);
                merged.setRenderingHints(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout2));
            }
        }
        return merged;
    }

    /**
     * Update the ROI by extracting it from the current image using the underlying property generator (when specified)
     *
     * @param forceUpdate update the ROI no matter if the original roi was is null.
     * @param opName if not null, get the property from the underlying property generator
     */
    private void updateROI(boolean forceUpdate, String opName) {
        // getting the new ROI property
        if (forceUpdate || roi != null) {
            Object prop = null;
            RenderedImage localImage = image;
            // there might be the case that the image comes from a bandMerge
            // so let's try to extract the ROI from the source image
            if (localImage instanceof RenderedOp) {
                String operationName = ((RenderedOp) localImage).getOperationName();
                if ("BandMerge".equalsIgnoreCase(operationName)) {
                    List<RenderedImage> sources = localImage.getSources();
                    if (!sources.isEmpty()) {
                        localImage = sources.get(0);
                    }
                }
            }
            if (opName != null) {
                // Extract the roi using a property generator
                PropertyGenerator gen =
                        getOperationDescriptor(opName).getPropertyGenerators(RenderedRegistryMode.MODE_NAME)[0];
                prop = gen.getProperty("roi", localImage);
            } else {
                // extract the roi from the image
                prop = localImage.getProperty("roi");
            }
            // Actual ROI update
            if (prop != null && prop instanceof ROI) {
                setROI((ROI) prop);
            } else {
                setROI(null);
            }
        }
    }

    /** Apply scaling parameter to the ROI to return the scaled version */
    private ROI computeScaledROI(double[] scalingParams) throws NoninvertibleTransformException {
        ROI newRoi = null;
        if (roi != null) {
            AffineTransform sTx = AffineTransform.getScaleInstance(scalingParams[0], scalingParams[1]);
            sTx.concatenate(AffineTransform.getTranslateInstance(scalingParams[2], scalingParams[3]));
            newRoi = roi.transform(sTx.createInverse());
        }
        return newRoi;
    }

    private double[] getScalingParams(ParameterBlock paramBlock, boolean isScale2) {
        // Shearing is not taken into account.
        // params are ordered like this: scaleX, scaleY, transX, transY
        double[] scalingParams = new double[4];
        for (int i = 0; i < 4; i++) {
            // Use proper datatype parameter getter depending on the type of operation
            scalingParams[i] = isScale2 ? paramBlock.getDoubleParameter(i) : paramBlock.getFloatParameter(i);
        }
        return scalingParams;
    }

    private void updateNoData(double[] bgValues, RenderedImage image) {
        if (bgValues != null && bgValues.length > 0) {
            Range newNoData = extractNoDataProperty(image);
            if (newNoData != null) {
                setNoData(newNoData);
            }
        }
    }

    /**
     * Crops the image to the specified bounds. Will use an internal operation that ensures the tile cache and tile
     * scheduler hints are used, and will perform operation elimination in case the crop is doing nothing, or in case
     * the crop is performed over another crop
     */
    public ImageWorker crop(float x, float y, float width, float height) {
        // no op elimination
        if (image.getMinX() == x && image.getMinY() == y && image.getWidth() == width && image.getHeight() == height) {
            return this;
        }

        // crop over crop
        RenderedImage source = image;
        if (image instanceof RenderedOp) {
            RenderedOp op = (RenderedOp) image;
            if ("Crop".equals(op.getOperationName()) || "GTCrop".equals(op.getOperationName())) {
                ParameterBlock paramBlock = op.getParameterBlock();
                source = paramBlock.getRenderedSource(0);

                float sx = paramBlock.getFloatParameter(0);
                float sy = paramBlock.getFloatParameter(1);
                float sWidth = paramBlock.getFloatParameter(2);
                float sHeight = paramBlock.getFloatParameter(3);

                Rectangle2D.Float sourceBounds = new Rectangle2D.Float(sx, sy, sWidth, sHeight);
                Rectangle2D.Float bounds = new Rectangle.Float(x, y, width, height);
                Rectangle2D intersection = bounds.createIntersection(sourceBounds);

                x = (float) intersection.getMinX();
                y = (float) intersection.getMinY();
                width = (float) intersection.getWidth();
                height = (float) intersection.getHeight();
            }
        }

        ParameterBlock pb = new ParameterBlock();
        pb.setSource(source, 0);
        pb.set(x, 0);
        pb.set(y, 1);
        pb.set(width, 2);
        pb.set(height, 3);
        pb.set(roi, 4);
        pb.set(nodata, 5);
        if (isNoDataNeeded()) {
            double destinationNoData = nodata != null
                    ? nodata.getMin().doubleValue()
                    : background != null && background.length > 0 ? background[0] : Double.NaN;
            pb.set(new double[] {destinationNoData}, 6);
        }

        image = JAI.create("Crop", pb, commonHints);
        invalidateStatistics();
        return this;
    }

    public ImageWorker function(
            ImageFunction function, int w, int h, float xScale, float yScale, float xTrans, float yTrans) {
        if (image != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Input image already present but will be replaced by ImageFunction");
            }
        }
        // Create a new parameter block
        ParameterBlock pb = new ParameterBlock();
        pb.add(function)
                .add(w)
                .add(h)
                .add(xScale)
                .add(yScale)
                .add(xTrans)
                .add(yTrans)
                .add(roi)
                .add(nodata);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.add((float) background[0]);
            }
        }

        RenderedImage result = JAI.create("ImageFunction", pb, getRenderingHints());
        setImage(result);
        return this;
    }

    /** Returns the background colors as a value, if at all possible (3 or 4 values in the right range) */
    private Color getBackgroundColor() {
        if (background == null || background.length < 3 || background.length > 4) {
            return null;
        }

        for (double component : background) {
            if (component < 0 || component > 255) {
                return null;
            }
        }

        if (background.length == 3) {
            return new Color((int) background[0], (int) background[1], (int) background[2]);
        } else {
            // as per initial exit condition, here we can only have background.length == 4
            return new Color((int) background[0], (int) background[1], (int) background[2], (int) background[3]);
        }
    }

    public ImageWorker mosaic(
            RenderedImage[] images,
            MosaicType type,
            PlanarImage[] alphas,
            ROI[] rois,
            double[][] thresholds,
            Range[] nodata) {
        // check if we might be applying a background value that's not in palettes, still assuming
        // the input images have uniform palettes
        double[] background = this.background;
        if (images != null && images.length > 0) {
            ColorModel cmref = images[0].getColorModel();
            Color backgroundColor = getBackgroundColor();
            if (cmref instanceof IndexColorModel
                    && (cmref.getTransparency() != IndexColorModel.OPAQUE
                            || images[0].getProperty("ROI") instanceof ROI)
                    && backgroundColor != null) {
                IndexColorModel icm = (IndexColorModel) cmref;
                int index = ColorUtilities.getColorIndex(icm, backgroundColor, -1);
                Color color;
                if (icm.hasAlpha()) {
                    color = new Color(icm.getRed(index), icm.getGreen(index), icm.getBlue(index));
                } else {
                    color = new Color(icm.getRed(index), icm.getGreen(index), icm.getBlue(index), icm.getAlpha(index));
                }
                if (color.equals(backgroundColor)) {
                    background = new double[] {index};
                } else {
                    // we have to expand to RGB to apply that value
                    for (int i = 0; i < images.length; i++) {
                        images[i] = new ImageWorker(images[i])
                                .forceComponentColorModel()
                                .getRenderedImage();
                    }
                }
            }
        }

        // ParameterBlock creation
        ParameterBlock pb = new ParameterBlock();
        int srcNum = 0;
        // pb.addSource(image);
        if (images != null && images.length > 0) {
            for (RenderedImage renderedImage : images) {
                if (renderedImage != null) {
                    pb.addSource(renderedImage);
                    srcNum++;
                }
            }
        }
        // Setting ROIs
        ROI[] roisNew = null;
        if (rois != null && srcNum > 0) {
            roisNew = new ROI[srcNum];
            System.arraycopy(rois, 0, roisNew, 0, rois.length);
        }
        // Setting Alphas
        PlanarImage[] alphasNew = null;
        if (alphas != null && srcNum > 0) {
            alphasNew = new PlanarImage[srcNum];
            System.arraycopy(alphas, 0, alphasNew, 0, alphas.length);
        }
        // Setting NoData
        Range[] nodataNew = null;
        double[] nodataBackground = null;
        boolean noInternalNoData = true;
        if (nodata != null && srcNum > 0) {
            nodataNew = new Range[srcNum];
            System.arraycopy(nodata, 0, nodataNew, 0, nodata.length);
        } else {
            nodataNew = new Range[srcNum];
            for (int i = 0; i < srcNum; i++) {
                RenderedImage img = pb.getRenderedSource(i);
                Range nodProp = extractNoDataProperty(img);
                if (background == null && nodProp != null && nodataBackground == null) {
                    nodataBackground = new double[1];
                    nodataBackground[0] = nodProp.getMax().doubleValue();
                }
                noInternalNoData &= nodProp == null;
                nodataNew[i] = nodProp;
            }
        }

        if (noInternalNoData && thresholds != null) {
            nodataNew = handleMosaicThresholds(thresholds, srcNum);
        }
        // Setting the parameters
        pb.add(type);
        pb.add(alphasNew);
        pb.add(roisNew);
        pb.add(thresholds);
        pb.add(background != null ? background : nodataBackground);
        pb.add(nodataNew);
        RenderingHints hints = getRenderingHints();
        image = JAI.create("Mosaic", pb, hints);
        boolean setRoiProperty = false;
        if (hints != null && !hints.isEmpty() && hints.containsKey(FORCE_MOSAIC_ROI_PROPERTY)) {
            setRoiProperty = (boolean) hints.get(FORCE_MOSAIC_ROI_PROPERTY);
        }
        // Setting the final ROI as union of the older ROIs, assuming
        // we did not apply a background color, in that case, there is no more ROI to
        // care for (However, we still set the ROI if explicitly required via Hints)
        if (background == null || setRoiProperty) {
            if (roisNew != null) {
                ROI finalROI = mosaicROIs(pb.getSources(), roisNew);
                setROI(finalROI);
            }
        } else {
            setROI(null);
        }
        return this;
    }

    private ROI mosaicROIs(List sources, ROI... roiArray) {
        if (roiArray == null) {
            return null;
        }

        // collect all ROIs
        List<ROI> rois = new ArrayList<>(Arrays.asList(roiArray));
        int numSources = sources.size();
        if (roiArray.length < numSources) {
            for (int i = roiArray.length; i < numSources; i++) {
                RenderedImage img = (RenderedImage) sources.get(i);
                ROI r = getImageBoundsROI(img);
                rois.add(r);
            }
        }
        // handle possible null inputs
        for (int i = 0; i < roiArray.length; i++) {
            ROI roi = roiArray[i];
            if (roi == null) {
                RenderedImage img = (RenderedImage) sources.get(i);
                ROI r = getImageBoundsROI(img);
                rois.set(i, r);
            }
        }

        // bail out for the simple case without creating new objects
        if (rois.size() == 1) {
            return rois.get(0);
        }

        // prepare the vector union, take aside a ROIGeometry if possible
        // as it can add both ROIShape and ROIGeometry
        List<ROI> rasterROIs = new ArrayList<>();
        List<ROI> vectorROIs = new ArrayList<>();
        ROI vectorReference = null;
        for (ROI roi : rois) {
            if (roi instanceof ROIShape || roi instanceof ROIGeometry) {
                if (vectorReference == null && roi instanceof ROIGeometry) {
                    vectorReference = roi;
                } else {
                    vectorROIs.add(roi);
                }
            } else {
                rasterROIs.add(roi);
            }
        }
        if (vectorReference == null && !vectorROIs.isEmpty()) {
            vectorReference = vectorROIs.remove(0);
        }
        // accumulate the vector ROIs, if any
        for (ROI roi : vectorROIs) {
            vectorReference = vectorReference.add(roi);
        }

        // optimization in case we end up with just one ROI, no need to mosaic
        if (rasterROIs.isEmpty()) {
            return vectorReference;
        } else if (rasterROIs.size() == 1 && vectorReference == null) {
            return rasterROIs.get(0);
        }

        // ok, rasterize the vector one if any and mosaic
        ParameterBlock pb = new ParameterBlock();
        if (vectorReference != null) {
            pb.addSource(vectorReference.getAsImage());
        }
        for (ROI rasterROI : rasterROIs) {
            pb.addSource(rasterROI.getAsImage());
        }
        pb.add(javax.media.jai.operator.MosaicDescriptor.MOSAIC_TYPE_OVERLAY);
        pb.add(null); // alphas
        pb.add(null); // ROI (null to avoid double bit -> byte expansion of the ROI data
        pb.add(ROI_THRESHOLDS);
        pb.add(ROI_BACKGROUND);
        pb.add(handleMosaicThresholds(ROI_THRESHOLDS, rasterROIs.size() + (vectorReference != null ? 1 : 0)));
        RenderedImage roiMosaic = JAI.create("Mosaic", pb, getRenderingHints());
        return new ROI(roiMosaic);
    }

    private ROI getImageBoundsROI(RenderedImage image) {
        final int minX = image.getMinX();
        final int minY = image.getMinY();
        final int maxX = minX + image.getWidth();
        final int maxY = minY + image.getHeight();
        LiteCoordinateSequence cs =
                new LiteCoordinateSequence(minX, minY, maxX, minY, maxX, maxY, minX, maxY, minX, minY);
        Geometry footprint = new GeometryFactory().createPolygon(cs);
        return new ROIGeometry(footprint);
    }

    private Range[] handleMosaicThresholds(double[][] thresholds, int srcNum) {
        Range[] nodata = new Range[srcNum];
        int minSrcNum = Math.min(srcNum, thresholds.length);
        for (int i = 0; i < minSrcNum; i++) {

            double maxValue = Double.NEGATIVE_INFINITY;
            int numBands = thresholds[i].length;
            for (int b = 0; b < numBands; b++) {
                double bandValue = thresholds[i][b];
                if (bandValue > maxValue) {
                    maxValue = bandValue;
                }
            }
            nodata[i] = RangeFactory.create(Double.NEGATIVE_INFINITY, true, maxValue, false);
        }
        if (minSrcNum < srcNum) {
            for (int i = minSrcNum; i < srcNum; i++) {
                nodata[i] = nodata[0];
            }
        }

        return nodata;
    }

    public ImageWorker border(int leftPad, int rightPad, int topPad, int bottomPad, BorderExtender ext) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(leftPad);
        pb.add(rightPad);
        pb.add(topPad);
        pb.add(bottomPad);
        pb.add(ext);
        pb.add(nodata);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.add(background[0]);
            }
        }
        image = JAI.create("Border", pb, getRenderingHints());
        return this;
    }

    public ImageWorker translate(float xTrans, float yTrans, Interpolation interp) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(xTrans);
        pb.add(yTrans);
        pb.add(interp);
        // do not use getRenderingHints() with translate, as it cannot deal with layout hints
        image = JAI.create("Translate", pb, commonHints);
        return this;
    }

    /** Warps the underlying raster using the provided Warp object. */
    public ImageWorker warp(Warp warp, Interpolation interp) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        pb.set(warp, 0);
        pb.set(interp, 1);
        pb.set(roi, 3);
        pb.set(nodata, 4);
        pb.set(background, 2);
        image = JAI.create("Warp", pb, getRenderingHints());
        updateROI(true, null);
        return this;
    }

    /** Scales the underlying raster using the provided parameters. */
    public ImageWorker scale(double xScale, double yScale, double xTrans, double yTrans, Interpolation interp) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        double[] scalingParams = {xScale, yScale, xTrans, yTrans};
        scale(pb, scalingParams, interp, getRenderingHints());

        // getting the new ROI property
        updateROI(true, SCALE_OP_NAME);
        return this;
    }

    /** Performs Lookup on the underlying image */
    public ImageWorker lookup(LookupTable table) {
        // ParameterBlock definition
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(table, 0);
        pb.set(roi, 2);
        // Convert the NoData
        if (nodata != null) {
            nodata = RangeFactory.convert(nodata, image.getSampleModel().getDataType());
        }
        pb.set(nodata, 3);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 1);
            }
        }

        image = JAI.create("Lookup", pb, getRenderingHints());
        return this;
    }

    /** Warps the underlying using the provided Warp object. */
    public ImageWorker colorIndex(ColorIndexer indexer) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        pb.set(indexer, 0);
        pb.set(roi, 1);
        pb.set(nodata, 2);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background, 3);
            }
        }
        image = JAI.create("ColorIndexer", pb, getRenderingHints());
        return this;
    }

    /** Apply a Raster classification on the underlying image. */
    public ImageWorker classify(ColorMapTransform domain1D, Integer bandIndex) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        pb.set(domain1D, 0);
        pb.set(bandIndex, 1);
        pb.set(roi, 2);
        pb.set(nodata, 3);
        if (isNoDataNeeded()) {
            if (domain1D.hasGaps()) {
                // We must set the new NoData value
                setNoData(RangeFactory.create(domain1D.getDefaultValue(), domain1D.getDefaultValue()));
            }
        }
        image = JAI.create("RasterClassifier", pb, getRenderingHints());

        return this;
    }

    /** Apply a Generic Piecewise operation on the underlying image. */
    public ImageWorker piecewise(PiecewiseTransform1D transform, Integer bandIndex) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        pb.set(transform, 0);
        pb.set(bandIndex, 1);
        pb.set(roi, 2);
        pb.set(nodata, 3);
        if (isNoDataNeeded()) {
            if (transform.hasGaps()) {
                // We must set the new NoData value
                setNoData(RangeFactory.create(transform.getDefaultValue(), transform.getDefaultValue()));
            }
        }
        image = JAI.create("GenericPiecewise", pb, getRenderingHints());

        return this;
    }

    /** Apply a rescale operation on the underlying image. */
    public ImageWorker rescale(double[] scale, double[] offset) {
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0); // The source image.
        pb.set(scale, 0); // The per-band constants to multiply by.
        pb.set(offset, 1); // The per-band offsets to be added.
        pb.set(roi, 2); // ROI
        pb.set(nodata, 3); // NoData range

        double destNodata = Double.NaN;
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                destNodata = background[0];
            } else if (nodata != null) {
                // preserve nodata to avoid the destination nodata fall
                // in the range of valid values
                destNodata = nodata.getMin().doubleValue();
            }
        }

        pb.set(destNodata, 5);
        image = JAI.create("Rescale", pb, getRenderingHints());
        if (!Double.isNaN(destNodata)) {
            setNoData(RangeFactory.create((byte) destNodata, (byte) destNodata));
        }
        return this;
    }

    /** Apply a rescale operation on the underlying image. */
    public ImageWorker bandCombine(double[][] coeff) {
        // ParameterBlock definition
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(coeff, 0);
        pb.set(roi, 1);
        pb.set(nodata, 2);
        if (isNoDataNeeded()) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 3);
            }
        }

        image = JAI.create("BandCombine", pb, getRenderingHints());

        return this;
    }

    /** Apply a rangeLookup operation on the underlying image. */
    public ImageWorker rangeLookup(Object rangeLookup) {
        // ParameterBlock definition
        ParameterBlock pb = new ParameterBlock();
        pb.setSource(image, 0);
        pb.set(rangeLookup, 0);
        pb.set(roi, 2);
        if (roi != null) {
            if (background != null && background.length > 0) {
                pb.set(background[0], 1);
                // We must set the new NoData value
                setNoData(RangeFactory.create(background[0], background[0]));
            }
        }
        if (JAIExt.isJAIExtOperation("RLookup")) {
            image = JAI.create("RLookup", pb, getRenderingHints());
        } else {
            image = JAI.create("RangeLookup", pb, getRenderingHints());
        }

        return this;
    }

    /**
     * Forces all NODATA pixels, as well as those outside of the ROI, to be transparent (expanding the color model as
     * needed in order to make it so). In case the image has no ROI or no nodata, the method won't perform any change
     */
    public ImageWorker prepareForRendering() {
        // anything to do?
        ROI roi = getROI();
        if (roi == null) {
            Object roiCandidate = image.getProperty("roi");
            if (roiCandidate instanceof ROI) {
                roi = (ROI) roiCandidate;
            }
        }
        if (nodata == null && roi == null) {
            return this;
        }

        RenderedImage image = getRenderedImage();

        // figure out the suitable background value
        ColorModel cm = image.getColorModel();
        double[] bgValues = null;
        PlanarImage[] alphaChannels = null;
        final int transparencyType = cm.getTransparency();

        // in case of index color model we try to preserve it, so that output
        // formats that can work with it can enjoy its extra compactness
        if (cm instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) cm;
            // try to find the index that matches the requested background color
            final int bgColorIndex = icm.getTransparentPixel();

            // we did not find the background color, well we have to expand to RGB and then tell
            // Mosaic to use the RGB(A) color as the
            // background
            if (bgColorIndex == -1) {
                // we need to expand the image to RGB
                bgValues = new double[] {0, 0, 0, 0};
                if (getNoData() != null) {
                    ImageWorker delegate = new ImageWorker(getRenderedImage());
                    delegate.setBackground(bgValues);
                    delegate.forceComponentColorModel();
                    setImage(delegate.getRenderedImage());
                    // nodata has been replaced by transparency
                    setNoData(null);
                } else {
                    forceComponentColorModel();
                }
                if (!getRenderedImage().getColorModel().hasAlpha()) {
                    addAlphaChannel();
                }
                image = getRenderedImage();
            } else {
                // we found the background color in the original image palette therefore we set its
                // index as the bkg value.
                // The final Mosaic will use the IndexColorModel of this image anywa, therefore all
                // we need to do is to force
                // the background to point to the right color in the palette
                bgValues = new double[] {bgColorIndex};
            }

            // collect alpha channels if we have them in order to reuse them later on for mosaic
            // operation
            if (cm.hasAlpha() && bgColorIndex == -1) {
                forceComponentColorModel();
                final RenderedImage alpha =
                        new ImageWorker(getRenderedImage()).retainLastBand().getRenderedImage();
                alphaChannels = new PlanarImage[] {PlanarImage.wrapRenderedImage(alpha)};
            }
        } else if (cm instanceof ComponentColorModel) {
            // convert to RGB if necessary
            ComponentColorModel ccm = (ComponentColorModel) cm;
            boolean hasAlpha = cm.hasAlpha();

            // if we have a grayscale image see if we have to expand to RGB
            if (ccm.getNumColorComponents() == 1) {
                if (ccm.getTransferType() == DataBuffer.TYPE_DOUBLE
                        || ccm.getTransferType() == DataBuffer.TYPE_FLOAT
                        || ccm.getTransferType() == DataBuffer.TYPE_UNDEFINED
                        || !hasAlpha) {
                    // expand to RGB, this is not a case we can optimize
                    final ImageWorker iw = new ImageWorker(image);
                    if (hasAlpha) {
                        final RenderedImage alpha = iw.retainLastBand().getRenderedImage();
                        // get first band
                        final RenderedImage gray =
                                new ImageWorker(image).retainFirstBand().getRenderedImage();
                        image = new ImageWorker(gray)
                                .bandMerge(3)
                                .addBand(alpha, false)
                                .forceComponentColorModel()
                                .forceColorSpaceRGB()
                                .getRenderedImage();
                    } else {
                        image = iw.bandMerge(3)
                                .forceComponentColorModel()
                                .forceColorSpaceRGB()
                                .getRenderedImage();
                    }
                } else {
                    // has alpha channel, extract it
                    final ImageWorker iw = new ImageWorker(image);
                    final RenderedImage alpha = iw.retainLastBand().getRenderedImage();
                    alphaChannels = new PlanarImage[] {PlanarImage.wrapRenderedImage(alpha)};

                    bgValues = new double[] {0, 0, 0, 0};
                }

                // get back the ColorModel
                cm = image.getColorModel();
                ccm = (ComponentColorModel) cm;
                hasAlpha = cm.hasAlpha();
            }

            if (bgValues == null) {
                if (hasAlpha) {
                    // get alpha
                    final ImageWorker iw = new ImageWorker(image);
                    final RenderedImage alpha = iw.retainLastBand().getRenderedImage();
                    alphaChannels = new PlanarImage[] {PlanarImage.wrapRenderedImage(alpha)};
                    bgValues = new double[] {0, 0, 0, 0};
                } else {
                    if (nodata != null) {
                        // must map nodata to alpha
                        RangeLookupTable.Builder<Byte, Byte> builder = new RangeLookupTable.Builder<>();
                        if (nodata.getMin().doubleValue() != Double.NEGATIVE_INFINITY) {
                            builder.add(
                                    RangeFactory.create(
                                            Double.NEGATIVE_INFINITY,
                                            true,
                                            nodata.getMin().doubleValue(),
                                            !nodata.isMinIncluded()),
                                    (byte) 255);
                        }
                        builder.add(nodata, (byte) 0);
                        if (nodata.getMax().doubleValue() != Double.POSITIVE_INFINITY) {
                            builder.add(
                                    RangeFactory.create(
                                            nodata.getMax().doubleValue(),
                                            !nodata.isMaxIncluded(),
                                            Double.POSITIVE_INFINITY,
                                            false),
                                    (byte) 255);
                        }
                        RangeLookupTable lookupTable = builder.build();
                        final RenderedImage alpha;
                        final int numBands = this.image.getSampleModel().getNumBands();
                        if (numBands == 1) {
                            alpha = new ImageWorker(this.image)
                                    .rangeLookup(lookupTable)
                                    .getRenderedImage();
                        } else {
                            // need to combine all bands to extract alpha based on nodata.
                            // we may want to setup a custom jai operation for that.
                            final double weight = 1.0 / numBands;
                            final double[][] matrix = new double[1][numBands + 1];

                            // Assign a weight to each band to combine results of RangeLookup
                            for (int i = 0; i < numBands; i++) {
                                matrix[0][i] = weight;
                            }

                            // Final lookup table to assign fully transparent (0) to all zero pixels
                            alpha = new ImageWorker(this.image)
                                    .setROI(roi)
                                    .rangeLookup(lookupTable)
                                    .bandCombine(matrix)
                                    .lookup(LookupTableFactory.create(ALPHA_LUT))
                                    .getRenderedImage();
                        }
                        image = new ImageWorker(image)
                                .addBand(alpha, false, true, null)
                                .getRenderedImage();
                    } else {
                        image = new ImageWorker(image).addAlphaChannel().getRenderedImage();
                    }
                    // this will work fine for all situation where the color components are <= 3
                    // e.g., one band rasters with no colormap will have only one usually
                    bgValues = new double[] {0, 0, 0, 0};
                }
            }
        }

        // If we need to add a collar use mosaic or if we need to blend/apply a bkg color
        ImageWorker iw = new ImageWorker(image);
        ROI[] rois = {roi};

        // build the transparency thresholds
        double[][] thresholds = {
            {ColorUtilities.getThreshold(image.getSampleModel().getDataType())}
        };
        // apply the mosaic
        iw.setBackground(bgValues);
        iw.mosaic(
                new RenderedImage[] {image},
                alphaChannels != null && transparencyType == Transparency.TRANSLUCENT
                        ? MosaicDescriptor.MOSAIC_TYPE_BLEND
                        : MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                alphaChannels,
                rois,
                thresholds,
                null);
        this.image = iw.getRenderedImage();

        return this;
    }

    /**
     * Adds an extra channel to the image, with a value of 255 (not public yet because it won't work with all image
     * types)
     */
    private ImageWorker addAlphaChannel() {
        final ImageLayout tempLayout = new ImageLayout(image);
        tempLayout.unsetValid(ImageLayout.COLOR_MODEL_MASK).unsetValid(ImageLayout.SAMPLE_MODEL_MASK);
        RenderedImage alpha = ConstantDescriptor.create(
                Float.valueOf(image.getWidth()),
                Float.valueOf(image.getHeight()),
                new Byte[] {Byte.valueOf((byte) 255)},
                new RenderingHints(JAI.KEY_IMAGE_LAYOUT, tempLayout));
        addBand(alpha, false, true, null);
        return this;
    }

    /**
     * Writes the {@linkplain #image} to the specified output, trying all encoders in the specified iterator in the
     * iteration order.
     *
     * @return this {@link ImageWorker}.
     */
    @SuppressWarnings("PMD.UseTryWithResources") // resource might be null
    private ImageWorker write(final Object output, final Iterator<? extends ImageWriter> encoders) throws IOException {
        if (encoders != null) {
            while (encoders.hasNext()) {
                final ImageWriter writer = encoders.next();
                final ImageWriterSpi spi = writer.getOriginatingProvider();
                final Class<?>[] outputTypes;
                if (spi == null) {
                    outputTypes = new Class[] {ImageOutputStream.class};
                } else {
                    /*
                     * If the encoder is for some format handled in a special way (e.g. GIF), apply the required operation. Note that invoking the
                     * same method many time (e.g. "forceIndexColorModelForGIF", which could occurs if there is more than one GIF encoder registered)
                     * should not hurt - all method invocation after the first one should be no-op.
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
                 * Now try to set the output directly (if possible), or as an ImageOutputStream if the encoder doesn't accept directly the specified
                 * output. Note that some formats like HDF may not support ImageOutputStream.
                 */
                ImageOutputStream stream = null;
                try {
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
                } finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
                return this;
            }
        }
        throw new IIOException(ErrorKeys.NO_IMAGE_WRITER);
    }

    /** Returns {@code true} if the specified array contains the specified type. */
    private static boolean acceptInputType(final Class<?>[] types, final Class<?> searchFor) {
        for (int i = types.length; --i >= 0; ) {
            if (searchFor.isAssignableFrom(types[i])) {
                return true;
            }
        }
        return false;
    }

    /** Returns {@code true} if the specified array contains the specified string. */
    private static boolean containsFormatName(final String[] formats, final String searchFor) {
        for (int i = formats.length; --i >= 0; ) {
            if (searchFor.equalsIgnoreCase(formats[i])) {
                return true;
            }
        }
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////// ////////
    // ////// DEBUGING HELP ////////
    // ////// ////////
    // /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Shows the current {@linkplain #image} in a window together with the operation chain as a
     * {@linkplain javax.swing.JTree tree}. This method is provided mostly for debugging purpose. This method requires
     * the {@code gt2-widgets-swing.jar} file in the classpath.
     *
     * @throws HeadlessException if {@code gt2-widgets-swing.jar} is not on the classpath, or if AWT can't create the
     *     window components.
     * @return this {@link ImageWorker}.
     * @see org.geotools.gui.swing.image.OperationTreeBrowser#show(RenderedImage)
     */
    public final ImageWorker show() throws HeadlessException {
        /*
         * Uses reflection because the "gt2-widgets-swing.jar" dependency is optional and may not be available in the classpath. All the complicated
         * stuff below is simply doing this call:
         *
         * OperationTreeBrowser.show(image);
         *
         * Tip: The @see tag in the above javadoc can be used as a check for the existence of class and method referenced below. Check for the javadoc
         * warnings.
         */
        final Class<?> c;
        try {
            c = Class.forName("org.geotools.gui.swing.image.OperationTreeBrowser");
        } catch (ClassNotFoundException cause) {
            final HeadlessException e = new HeadlessException("The \"gt2-widgets-swing.jar\" file" + " is required.");
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
             * ClassNotFoundException may be expected, but all other kinds of checked exceptions (and they are numerous...) are errors.
             */
            throw new AssertionError(e);
        }
        return this;
    }

    /**
     * Provides a hint that this {@link ImageWorker} will no longer be accessed from a reference in user space. The
     * results are equivalent to those that occur when the program loses its last reference to this image, the garbage
     * collector discovers this, and finalize is called. This can be used as a hint in situations where waiting for
     * garbage collection would be overly conservative.
     *
     * <p>Mind, this also results in disposing the JAI Image chain attached to the image the worker is applied to, so
     * don't call this method on image changes (full/partial) that you want to use.
     *
     * <p>{@link ImageWorker} defines this method to remove the image being disposed from the list of sinks in all of
     * its source images. The results of referencing an {@link ImageWorker} after a call to dispose() are undefined.
     */
    public final void dispose() {
        if (commonHints != null) {
            this.commonHints.clear();
        }
        this.commonHints = null;
        this.roi = null;
        if (this.image instanceof PlanarImage) {
            ImageUtilities.disposePlanarImageChain(PlanarImage.wrapRenderedImage(image));
        } else if (this.image instanceof BufferedImage) {
            ((BufferedImage) this.image).flush();
            this.image = null;
        }
    }

    /**
     * Loads the image from the specified file, and {@linkplain #show display} it in a window. This method is mostly as
     * a convenient way to test operation chains. This method can be invoked from the command line. If an optional
     * {@code -operation} argument is provided, the Java method (one of the image operations provided in this class)
     * immediately following it is executed. Example:
     *
     * <blockquote>
     *
     * <pre>
     * java org.geotools.image.ImageWorker -operation binarize &lt;var&gt;&lt;filename&gt;&lt;/var&gt;
     * </pre>
     *
     * </blockquote>
     */
    public static void main(String[] args) {
        final Arguments arguments = new Arguments(args);
        final String operation = arguments.getOptionalString("-operation");
        args = arguments.getRemainingArguments(1);
        if (args.length != 0)
            try {
                final ImageWorker worker = new ImageWorker(new File(args[0]));
                // Force usage of tile cache for every operations, including intermediate steps.
                worker.setRenderingHint(
                        JAI.KEY_TILE_CACHE, JAI.getDefaultInstance().getTileCache());
                if (operation != null) {
                    worker.getClass().getMethod(operation, (Class[]) null).invoke(worker, (Object[]) null);
                }
                /*
                 * TIP: Tests operations here (before the call to 'show()'), if wanted.
                 */
                worker.show();
            } catch (FileNotFoundException | NoSuchMethodException e) {
                arguments.printSummary(e);
            } catch (Exception e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }
    }

    private class WarpAffineReducer {
        private boolean reduced;
        private AffineTransform tx;
        private Interpolation interpolation;
        private double[] bgValues;
        private RenderedOp op;

        public WarpAffineReducer(AffineTransform tx, Interpolation interpolation, double[] bgValues, RenderedOp op) {
            this.tx = tx;
            this.interpolation = interpolation;
            this.bgValues = bgValues;
            this.op = op;
        }

        boolean reduced() {
            return reduced;
        }

        public double[] getBgValues() {
            return bgValues;
        }

        public WarpAffineReducer invoke() throws FactoryException, TransformException {
            // we can merge the affine into the warp
            MathTransform2D originalTransform = (MathTransform2D) op.getProperty("MathTransform");
            ParameterBlock sourceParamBlock = op.getParameterBlock();
            MathTransformFactory factory = ReferencingFactoryFinder.getMathTransformFactory(null);
            MathTransform affineMT =
                    factory.createAffineTransform(new org.geotools.referencing.operation.matrix.AffineTransform2D(tx));
            MathTransform2D chained =
                    (MathTransform2D) factory.createConcatenatedTransform(affineMT.inverse(), originalTransform);

            // setup the warp builder
            Double tolerance = (Double) getRenderingHint(Hints.RESAMPLE_TOLERANCE);
            if (tolerance == null) {
                tolerance = (Double) Hints.getSystemDefault(Hints.RESAMPLE_TOLERANCE);
            }
            if (tolerance == null) {
                tolerance = Hints.DEFAULT_RESAMPLE_TOLERANCE;
            }

            // in case of oversampling, reduce the tolerance by the oversampling factor
            // as the oversampling magnifies errors that would not be otherwise visible
            if (tx.getScaleX() > 1 || tx.getScaleY() > 1) {
                double factor = Math.max(tx.getScaleX(), tx.getScaleY());
                tolerance = tolerance / factor;
            }

            // setup a warp builder that is not gong to use too much memory
            WarpBuilder wb = new WarpBuilder(tolerance);
            wb.setMaxPositions(4 * 1024 * 1024);

            // compute the target bbox the same way the affine would have to have a 1-1
            // match
            ParameterBlock pb = new ParameterBlock();
            pb.setSource(op, 0);
            pb.set(tx, 0);
            pb.set(interpolation, 1);
            pb.set(bgValues, 2);
            pb.set(roi, 3);
            pb.set(true, 5);
            pb.set(nodata, 6);
            RenderedOp at = JAI.create("Affine", pb, getRenderingHints());
            updateNoData(bgValues, image);

            // commonHints);
            Rectangle targetBB = at.getBounds();
            int tileWidth = at.getTileWidth();
            int tileHeight = at.getTileHeight();
            ImageUtilities.disposeSinglePlanarImage(at);

            // warp
            Rectangle sourceBB = (Rectangle) op.getProperty("SourceBoundingBox");
            Rectangle mappingBB;
            if (op.getProperty("ROI") instanceof ROI) {
                // Due to a limitation in JAI we need to make sure the
                // mapping bounding box covers both source and target bounding box
                // otherwise the warped roi image layout won't be computed properly
                mappingBB = sourceBB.union(targetBB);
            } else {
                mappingBB = targetBB;
            }
            Warp warp = wb.buildWarp(chained, mappingBB);

            // do the switch only if we get a warp that is as fast as the original one,
            // of if we are upsampling, in which case the merge is required to preserve
            // good image quality (warp on NN produces pixels that are aligned to the axis
            // and then scaled, while the pixels should appear rotated instead)
            Warp sourceWarp = (Warp) sourceParamBlock.getObjectParameter(0);
            if (warp instanceof WarpGrid
                    || warp instanceof WarpAffine
                    || !(sourceWarp instanceof WarpGrid || sourceWarp instanceof WarpAffine)
                    || tx.getScaleX() > 1
                    || tx.getScaleY() > 1) {
                // and then the JAI Operation
                PlanarImage sourceImage = op.getSourceImage(0);
                final ParameterBlock paramBlk = new ParameterBlock().addSource(sourceImage);
                Object property = sourceImage.getProperty("ROI");
                // Boolean indicating if optional ROI may be reprojected back to the initial
                // image
                boolean canProcessROI = true;
                // Boolean indicating if NoData are the same as for the source operation or
                // are not present
                Range oldNoData = (Range)
                        (sourceParamBlock.getNumParameters() > 3 ? sourceParamBlock.getObjectParameter(4) : null);
                boolean hasSameNodata =
                        oldNoData == null && nodata == null || oldNoData != null && oldNoData.equals(nodata);
                if (property == null || property.equals(Image.UndefinedProperty) || !(property instanceof ROI)) {
                    paramBlk.add(warp).add(interpolation).add(bgValues);
                    if (oldNoData != null) {
                        paramBlk.set(oldNoData, 4);
                    }
                    // Try to reproject ROI after Warp
                    ROI newROI = null;
                    if (roi != null) {
                        ROI reprojectedROI = roi;
                        try {
                            MathTransform inverse = originalTransform.inverse();
                            if (inverse instanceof AffineTransform) {
                                AffineTransform inv = (AffineTransform) inverse;
                                newROI = reprojectedROI.transform(inv);
                            }
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to compute the inverse of the new ROI provided", e);
                            }
                            // Skip Warp Affine reduction
                            canProcessROI = false;
                        }
                    }

                    if (newROI != null) {
                        setROI(newROI);
                        paramBlk.set(newROI, 3);
                    }
                } else {
                    // Intersect ROIs
                    ROI newROI = null;
                    if (roi != null) {
                        // Try to reproject ROI after Warp
                        ROI reprojectedROI = roi;
                        try {
                            MathTransform inverse = originalTransform.inverse();
                            if (inverse instanceof AffineTransform) {
                                AffineTransform inv = (AffineTransform) inverse;
                                reprojectedROI = reprojectedROI.transform(inv);
                                newROI = reprojectedROI.intersect((ROI) property);
                            }
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to compute the inverse of the new ROI provided", e);
                            }
                            // Skip Warp Affine reduction
                            canProcessROI = false;
                        }
                    } else {
                        newROI = (ROI) property;
                    }
                    setROI(newROI);
                    paramBlk.add(warp).add(interpolation).add(newROI);
                    if (oldNoData != null) {
                        paramBlk.set(oldNoData, 4);
                    }
                }

                // handle background values
                if (bgValues == null && sourceParamBlock.getNumParameters() > 2) {
                    bgValues = (double[]) sourceParamBlock.getObjectParameter(2);
                }
                if (bgValues != null) {
                    paramBlk.set(bgValues, 2);
                }

                // Checks if ROI can be processed
                if (canProcessROI && hasSameNodata) {
                    // force in the image layout, this way we get exactly the same
                    // as the affine we're eliminating
                    Hints localHints = new Hints(getRenderingHints());
                    localHints.remove(JAI.KEY_IMAGE_LAYOUT);
                    ImageLayout il = new ImageLayout();
                    il.setMinX(targetBB.x);
                    il.setMinY(targetBB.y);
                    il.setWidth(targetBB.width);
                    il.setHeight(targetBB.height);

                    il.setTileHeight(tileWidth);
                    il.setTileWidth(tileHeight);
                    il.setTileGridXOffset(0);
                    il.setTileGridYOffset(0);
                    localHints.put(JAI.KEY_IMAGE_LAYOUT, il);

                    RenderedOp result = JAI.create("Warp", paramBlk, localHints);
                    result.setProperty("MathTransform", chained);
                    image = result;
                    // getting the new ROI property
                    Object prop = result.getProperty("roi");
                    if (prop != null && prop instanceof ROI) {
                        setROI((ROI) prop);
                    } else {
                        setROI(null);
                    }
                    reduced = true;
                    return this;
                }
            }
            reduced = false;
            return this;
        }
    }
}
