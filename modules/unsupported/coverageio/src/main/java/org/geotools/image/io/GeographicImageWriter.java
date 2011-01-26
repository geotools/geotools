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
package org.geotools.image.io;

import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import javax.imageio.IIOImage;
import javax.imageio.ImageWriter;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.event.IIOWriteWarningListener;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import org.geotools.image.ImageDimension;
import org.geotools.util.logging.Logging;
import org.geotools.resources.i18n.Locales;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.IndexedResourceBundle;


/**
 * Base class for writers of geographic images.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class GeographicImageWriter extends ImageWriter {
    /**
     * The logger to use for events related to this image writer.
     */
    static final Logger LOGGER = Logging.getLogger("org.geotools.image.io");

    /**
     * Index of the image in process of being written. This convenience index is reset to 0
     * by {@link #close} method.
     */
    private int imageIndex = 0;

    /**
     * Index of the ithumbnail in process of being written. This convenience index
     * is reset to 0 by {@link #close} method.
     */
    private int thumbnailIndex = 0;

    /**
     * Constructs a {@code GeographicImageWriter}.
     *
     * @param originatingProvider The {@code ImageWriterSpi} that
     *        is constructing this object, or {@code null}.
     */
    protected GeographicImageWriter(final ImageWriterSpi provider) {
        super(provider);
        availableLocales = Locales.getAvailableLocales();
    }

    /**
     * Sets the output.
     */
    @Override
    public void setOutput(final Object output) {
        imageIndex = 0;
        thumbnailIndex = 0;
        super.setOutput(output);
    }

    /**
     * Returns the resources for formatting error messages.
     */
    final IndexedResourceBundle getErrorResources() {
        return Errors.getResources(getLocale());
    }

    /**
     * Ensures that the specified parameter is non-null.
     *
     * @param  parameter The parameter name, for formatting an error message if needed.
     * @param  value The value to test.
     * @throws IllegalArgumentException if {@code value} is null.
     */
    private void ensureNonNull(final String parameter, final Object value)
            throws IllegalArgumentException
    {
        if (value == null) {
            throw new IllegalArgumentException(getErrorResources().
                        getString(ErrorKeys.NULL_ARGUMENT_$1, parameter));
        }
    }

    /**
     * Returns a metadata object containing default values for encoding a stream of images.
     * The default implementation returns {@code null}, which is appropriate for writer that
     * do not make use of stream meta-data.
     *
     * @param param Parameters that will be used to encode the image (in cases where
     *              it may affect the structure of the metadata), or {@code null}.
     * @return The metadata, or {@code null}.
     */
    public IIOMetadata getDefaultStreamMetadata(final ImageWriteParam param) {
        return null;
    }

    /**
     * Returns a metadata object containing default values for encoding an image of the given
     * type. The default implementation returns {@code null}, which is appropriate for writer
     * that do not make use of image meta-data.
     *
     * @param imageType The format of the image to be written later.
     * @param param Parameters that will be used to encode the image (in cases where
     *              it may affect the structure of the metadata), or {@code null}.
     * @return The metadata, or {@code null}.
     */
    public IIOMetadata getDefaultImageMetadata(final ImageTypeSpecifier imageType,
                                               final ImageWriteParam    param)
    {
        return null;
    }

    /**
     * Returns a metadata object initialized to the specified data for encoding a stream
     * of images. The default implementation copies the specified data into a
     * {@linkplain #getDefaultStreamMetadata default stream metadata}.
     *
     * @param inData Stream metadata used to initialize the state of the returned object.
     * @param param Parameters that will be used to encode the image (in cases where
     *              it may affect the structure of the metadata), or {@code null}.
     * @return The metadata, or {@code null}.
     */
    public IIOMetadata convertStreamMetadata(final IIOMetadata inData, final ImageWriteParam param) {
        ensureNonNull("inData", inData);
        final IIOMetadata outData = getDefaultStreamMetadata(param);
        final String format = commonMetadataFormatName(inData, outData);
        if (format != null) try {
            outData.mergeTree(format, inData.getAsTree(format));
            return outData;
        } catch (IIOInvalidTreeException ex) {
            warningOccurred("convertStreamMetadata", ex);
        }
        return null;
    }

    /**
     * Returns a metadata object initialized to the specified data for encoding an image
     * of the given type. The default implementation copies the specified data into a
     * {@linkplain #getDefaultImageMetadata default image metadata}.
     *
     * @param inData Image metadata used to initialize the state of the returned object.
     * @param imageType The format of the image to be written later.
     * @param param Parameters that will be used to encode the image (in cases where
     *              it may affect the structure of the metadata), or {@code null}.
     * @return The metadata, or {@code null}.
     */
    public IIOMetadata convertImageMetadata(final IIOMetadata        inData,
                                            final ImageTypeSpecifier imageType,
                                            final ImageWriteParam    param)
    {
        ensureNonNull("inData", inData);
        final IIOMetadata outData = getDefaultImageMetadata(imageType, param);
        final String format = commonMetadataFormatName(inData, outData);
        if (format != null) try {
            outData.mergeTree(format, inData.getAsTree(format));
            return outData;
        } catch (IIOInvalidTreeException ex) {
            warningOccurred("convertImageMetadata", ex);
        }
        return null;
    }

    /**
     * Finds a common metadata format name between the two specified metadata objects. This
     * method query for the {@code outData} {@linkplain #getNativeMetadataFormatName native
     * metadata format name} first.
     *
     * @param  inData  The input metadata.
     * @param  outData The output metadata.
     * @return A metadata format name common to {@code inData} and {@code outData},
     *         or {@code null} if none where found.
     */
    private static String commonMetadataFormatName(final IIOMetadata inData, final IIOMetadata outData) {
        String format = outData.getNativeMetadataFormatName();
        if (isSupportedFormat(inData, format)) {
            return format;
        }
        final String[] formats = outData.getExtraMetadataFormatNames();
        if (formats != null) {
            for (int i=0; i<formats.length; i++) {
                format = formats[i];
                if (isSupportedFormat(inData, format)) {
                    return format;
                }
            }
        }
        if (outData.isStandardMetadataFormatSupported() && inData.isStandardMetadataFormatSupported()) {
            return IIOMetadataFormatImpl.standardMetadataFormatName;
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified metadata supports the specified format.
     *
     * @param  metadata The metadata to test.
     * @param  format The format name to test, or {@code null}.
     * @return {@code true} if the specified metadata suports the specified format.
     */
    private static boolean isSupportedFormat(final IIOMetadata metadata, final String format) {
        if (format != null) {
            if (format.equalsIgnoreCase(metadata.getNativeMetadataFormatName())) {
                return true;
            }
            final String[] formats = metadata.getExtraMetadataFormatNames();
            if (formats != null) {
                for (int i=0; i<formats.length; i++) {
                    if (format.equalsIgnoreCase(formats[i])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the methods that take an {@link IIOImage} parameter are capable of dealing
     * with a {@link Raster}. The default implementation returns {@code true} since it is assumed
     * that subclasses will fetch pixels using the iterator returned by {@link #createRectIter
     * createRectIter}.
     */
    @Override
    public boolean canWriteRasters() {
        return true;
    }

    /**
     * Returns an iterator over the pixels of the specified image, taking subsampling in account.
     *
     * @param  image The image or raster to be written.
     * @param  parameters The write parameters, or {@code null} if the whole image will be written.
     * @return An iterator over the pixel values of the image to be written.
     */
    protected static RectIter createRectIter(final IIOImage image, final ImageWriteParam parameters) {
        /*
         * Examines the parameters for subsampling in lines, columns and bands. If a subsampling
         * is specified, the source region will be translated by the subsampling offset (if any).
         */
        Rectangle bounds;
        int[] sourceBands;
        final int sourceXSubsampling;
        final int sourceYSubsampling;
        if (parameters != null) {
            bounds             = parameters.getSourceRegion(); // Needs to be a clone.
            sourceXSubsampling = parameters.getSourceXSubsampling();
            sourceYSubsampling = parameters.getSourceYSubsampling();
            if (sourceXSubsampling != 1 || sourceYSubsampling != 1) {
                if (bounds == null) {
                    if (image.hasRaster()) {
                        bounds = image.getRaster().getBounds(); // Needs to be a clone.
                    } else {
                        final RenderedImage i = image.getRenderedImage();
                        bounds = new Rectangle(i.getMinX(), i.getMinY(), i.getWidth(), i.getHeight());
                    }
                }
                final int xOffset  = parameters.getSubsamplingXOffset();
                final int yOffset  = parameters.getSubsamplingYOffset();
                bounds.x      += xOffset;
                bounds.y      += yOffset;
                bounds.width  -= xOffset;
                bounds.height -= yOffset;
                // Fits to the smallest bounding box, which is
                // required by SubsampledRectIter implementation.
                bounds.width  -= (bounds.width  - 1) % sourceXSubsampling;
                bounds.height -= (bounds.height - 1) % sourceYSubsampling;
            }
            sourceBands = parameters.getSourceBands();
        } else {
            sourceBands        = null;
            bounds             = null;
            sourceXSubsampling = 1;
            sourceYSubsampling = 1;
        }
        /*
         * Creates the JAIiterator which will iterates over all pixels in the source region.
         * If no subsampling is specified and the source bands do not move and band, then the
         * JAI iterator is returned directly.
         */
        final int numBands;
        RectIter iterator;
        if (image.hasRaster()) {
            final Raster raster = image.getRaster();
            numBands = raster.getNumBands();
            iterator = RectIterFactory.create(raster, bounds);
        } else {
            final RenderedImage raster = image.getRenderedImage();
            numBands = raster.getSampleModel().getNumBands();
            iterator = RectIterFactory.create(raster, bounds);
        }
        if (sourceXSubsampling == 1 && sourceYSubsampling == 1) {
            if (sourceBands == null) {
                return iterator;
            }
            if (sourceBands.length == numBands) {
                boolean identity = true;
                for (int i=0; i<numBands; i++) {
                    if (sourceBands[i] != i) {
                        identity = false;
                        break;
                    }
                }
                if (identity) {
                    return iterator;
                }
            }
        }
        /*
         * A subsampling is required. Wraps the JAI iterator into a subsampler.
         */
        if (sourceBands == null) {
            sourceBands = new int[numBands];
            for (int i=0; i<numBands; i++) {
                sourceBands[i] = i;
            }
        }
        return new SubsampledRectIter(iterator, sourceXSubsampling, sourceYSubsampling, sourceBands);
    }

    /**
     * Computes the size of the region to be read, taking subsampling in account.
     *
     * @param  image The image or raster to be written.
     * @param  parameters The write parameters, or {@code null} if the whole image will be written.
     * @return dimension The dimension of the image to be written.
     */
    protected static ImageDimension computeSize(final IIOImage image, final ImageWriteParam parameters) {
        final ImageDimension dimension;
        if (image.hasRaster()) {
            dimension = new ImageDimension(image.getRaster());
        } else {
            dimension = new ImageDimension(image.getRenderedImage());
        }
        if (parameters != null) {
            final Rectangle bounds = parameters.getSourceRegion();
            if (bounds != null) {
                if (bounds.width < dimension.width) {
                    dimension.width = bounds.width;
                }
                if (bounds.height < dimension.height) {
                    dimension.height = bounds.height;
                }
            }
            dimension.width  -= parameters.getSubsamplingXOffset();
            dimension.height -= parameters.getSubsamplingYOffset();
        }
        return dimension;
    }

    /**
     * Broadcasts the start of an image write to all registered listeners. The default
     * implementation invokes the {@linkplain #processImageStarted(int) super-class method}
     * with an image index maintained by this writer.
     */
    protected void processImageStarted() {
        processImageStarted(imageIndex);
    }

    /**
     * Broadcasts the completion of an image write to all registered listeners.
     */
    @Override
    protected void processImageComplete() {
        super.processImageComplete();
        thumbnailIndex = 0;
        imageIndex++;
    }

    /**
     * Broadcasts the start of a thumbnail write to all registered listeners. The default
     * implementation invokes the {@linkplain #processThumbnailStarted(int,int) super-class
     * method} with an image and thumbnail index maintained by this writer.
     */
    protected void processThumbnailStarted() {
        processThumbnailStarted(imageIndex, thumbnailIndex);
    }

    /**
     * Broadcasts the completion of a thumbnail write to all registered listeners.
     */
    @Override
    protected void processThumbnailComplete() {
        super.processThumbnailComplete();
        thumbnailIndex++;
    }

    /**
     * Broadcasts a warning message to all registered listeners. The default implementation
     * invokes the {@linkplain #processWarningOccurred(int,String) super-class method} with
     * an image index maintained by this writer.
     */
    protected void processWarningOccurred(final String warning) {
        processWarningOccurred(imageIndex, warning);
    }

    /**
     * Broadcasts a warning message to all registered listeners. The default implementation
     * invokes the {@linkplain #processWarningOccurred(int,String,String) super-class method}
     * with an image index maintained by this writer.
     */
    protected void processWarningOccurred(final String baseName, final String keyword) {
        processWarningOccurred(imageIndex, baseName, keyword);
    }

    /**
     * Invoked when a warning occured. The default implementation make the following choice:
     * <p>
     * <ul>
     *   <li>If at least one {@linkplain IIOWriteWarningListener warning listener}
     *       has been {@linkplain #addIIOWriteWarningListener specified}, then the
     *       {@link IIOWriteWarningListener#warningOccurred warningOccurred} method is
     *       invoked for each of them and the log record is <strong>not</strong> logged.</li>
     *
     *   <li>Otherwise, the log record is sent to the {@code "org.geotools.image.io"} logger.</li>
     * </ul>
     *
     * Subclasses may override this method if more processing is wanted, or for
     * throwing exception if some warnings should be considered as fatal errors.
     */
    public void warningOccurred(final LogRecord record) {
        if (warningListeners == null) {
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        } else {
            processWarningOccurred(IndexedResourceBundle.format(record));
        }
    }

    /**
     * Convenience method for logging an exception from the given method.
     */
    private void warningOccurred(final String method, final Exception ex) {
        final LogRecord record = new LogRecord(Level.WARNING, ex.toString());
        record.setSourceClassName(GeographicImageWriter.class.getName());
        record.setSourceMethodName(method);
        record.setThrown(ex);
        warningOccurred(record);
    }

    /**
     * To be overriden and made {@code protected} by {@link StreamImageWriter} only.
     */
    void close() throws IOException {
        imageIndex = 0;
        thumbnailIndex = 0;
    }
}
