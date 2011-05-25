/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;

import java.awt.Rectangle;
import java.awt.image.ColorModel;      // For javadoc
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;     // For javadoc
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel; // For javadoc

import java.io.IOException;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.event.IIOReadWarningListener; // For javadoc

import org.geotools.util.NumberRange;
import org.geotools.util.logging.Logging;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Locales;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.IndexedResourceBundle;
import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.image.io.metadata.Band;


/**
 * Base class for readers of geographic images. The default implementation assumes that only one
 * {@linkplain ImageTypeSpecifier image type} is supported (as opposed to the arbitrary number
 * allowed by the standard {@link ImageReader}). It also provides a default image type built
 * automatically from a color palette and a range of valid values.
 * <p>
 * More specifically, this class provides the following conveniences to implementors:
 *
 * <ul>
 *   <li><p>Provides default {@link #getNumImages} and {@link #getNumBands} implementations,
 *       which return 1. This default behavior matches simple image formats like flat binary
 *       files or ASCII files. Those methods need to be overrided for more complex image
 *       formats.</p></li>
 *
 *   <li><p>Provides {@link #checkImageIndex} and {@link #checkBandIndex} convenience methods.
 *       Those methods are invoked by most implementation of public methods. They perform their
 *       checks based on the informations provided by the above-cited {@link #getNumImages} and
 *       {@link #getNumBands} methods.</p></li>
 *
 *   <li><p>Provides default implementations of {@link #getImageTypes} and {@link #getRawImageType},
 *       which assume that only one {@linkplain ImageTypeSpecifier image type} is supported. The
 *       default image type is created from the informations provided by {@link #getRawDataType}
 *       and {@link #getImageMetadata}.</p></li>
 *
 *   <li><p>Provides {@link #getStreamMetadata} and {@link #getImageMetadata} default
 *       implementations, which return {@code null} as authorized by the specification.
 *       Note that subclasses should consider returning {@link GeographicMetadata} instances.</p></li>
 * </ul>
 *
 * Images may be flat binary or ASCII files with no meta-data and no color information.
 * Their pixel values may be floating point values instead of integers. The default
 * implementation assumes floating point values and uses a grayscale color space scaled
 * to fit the range of values. Displaying such an image may be very slow. Consequently,
 * users who want to display image are encouraged to change data type and color space with
 * <a href="http://java.sun.com/products/java-media/jai/">Java Advanced Imaging</a>
 * operators after reading.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public abstract class GeographicImageReader extends ImageReader {
    /**
     * The logger to use for events related to this image reader.
     */
    static final Logger LOGGER = Logging.getLogger("org.geotools.image.io");

    /**
     * Metadata for each images, or {@code null} if not yet created.
     */
    private transient GeographicMetadata[] metadata;

    /**
     * Constructs a new image reader.
     *
     * @param provider The {@link ImageReaderSpi} that is invoking this constructor,
     *        or {@code null} if none.
     */
    protected GeographicImageReader(final ImageReaderSpi provider) {
        super(provider);
        availableLocales = Locales.getAvailableLocales();
    }

    /**
     * Sets the input source to use.
     *
     * @param input           The input object to use for future decoding.
     * @param seekForwardOnly If {@code true}, images and metadata may only be read
     *                        in ascending order from this input source.
     * @param ignoreMetadata  If {@code true}, metadata may be ignored during reads.
     */
    @Override
    public void setInput(final Object  input,
                         final boolean seekForwardOnly,
                         final boolean ignoreMetadata)
    {
        metadata = null; // Clears the cache
        super.setInput(input, seekForwardOnly, ignoreMetadata);
    }

    /**
     * Returns the resources for formatting error messages.
     */
    final IndexedResourceBundle getErrorResources() {
        return Errors.getResources(getLocale());
    }

    /**
     * Ensures that the specified image index is inside the expected range.
     * The expected range is {@link #minIndex minIndex} inclusive (initially 0)
     * to <code>{@link #getNumImages getNumImages}(false)</code> exclusive.
     *
     * @param  imageIndex Index to check for validity.
     * @throws IndexOutOfBoundsException if the specified index is outside the expected range.
     * @throws IOException If the operation failed because of an I/O error.
     */
    protected void checkImageIndex(final int imageIndex)
            throws IOException, IndexOutOfBoundsException
    {
        final int numImages = getNumImages(false);
        if (imageIndex < minIndex || (imageIndex >= numImages && numImages >= 0)) {
            throw new IndexOutOfBoundsException(indexOutOfBounds(imageIndex, minIndex, numImages));
        }
    }

    /**
     * Ensures that the specified band index is inside the expected range. The expected
     * range is 0 inclusive to <code>{@link #getNumBands getNumBands}(imageIndex)</code>
     * exclusive.
     *
     * @param  imageIndex The image index.
     * @param  bandIndex Index to check for validity.
     * @throws IndexOutOfBoundsException if the specified index is outside the expected range.
     * @throws IOException If the operation failed because of an I/O error.
     */
    protected void checkBandIndex(final int imageIndex, final int bandIndex)
            throws IOException, IndexOutOfBoundsException
    {
        // Call 'getNumBands' first in order to call 'checkImageIndex'.
        final int numBands = getNumBands(imageIndex);
        if (bandIndex >= numBands || bandIndex < 0) {
            throw new IndexOutOfBoundsException(indexOutOfBounds(bandIndex, 0, numBands));
        }
    }

    /**
     * Formats an error message for an index out of bounds.
     *
     * @param index The index out of bounds.
     * @param lower The lower legal value, inclusive.
     * @param upper The upper legal value, exclusive.
     */
    private String indexOutOfBounds(final int index, final int lower, final int upper) {
        return getErrorResources().getString(ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, index, lower, upper-1);
    }

    /**
     * Returns the number of images available from the current input source.
     * The default implementation returns 1.
     *
     * @param  allowSearch If true, the number of images will be returned
     *         even if a search is required.
     * @return The number of images, or -1 if {@code allowSearch}
     *         is false and a search would be required.
     *
     * @throws IllegalStateException if the input source has not been set.
     * @throws IOException if an error occurs reading the information from the input source.
     */
    public int getNumImages(final boolean allowSearch) throws IllegalStateException, IOException {
        if (input != null) {
            return 1;
        }
        throw new IllegalStateException(getErrorResources().getString(ErrorKeys.NO_IMAGE_INPUT));
    }

    /**
     * Returns the number of bands available for the specified image.
     * The default implementation returns 1.
     *
     * @param  imageIndex  The image index.
     * @throws IOException if an error occurs reading the information from the input source.
     */
    public int getNumBands(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        return 1;
    }

    /**
     * Returns the number of dimension of the image at the given index.
     * The default implementation always returns 2.
     *
     * @param  imageIndex The image index.
     * @return The number of dimension for the image at the given index.
     * @throws IOException if an error occurs reading the information from the input source.
     *
     * @since 2.5
     */
    public int getDimension(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        return 2;
    }

    /**
     * Returns metadata associated with the input source as a whole. Since many raw images
     * can't store metadata, the default implementation returns {@code null}.
     *
     * @throws IOException if an error occurs during reading.
     */
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    /**
     * Returns metadata associated with the given image. Since many raw images
     * can't store metadata, the default implementation returns {@code null}.
     *
     * @param  imageIndex The image index.
     * @return The metadata, or {@code null} if none.
     * @throws IOException if an error occurs during reading.
     */
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        return null;
    }

    /**
     * Returns a helper parser for metadata associated with the given image. This implementation
     * invokes  <code>{@linkplain #getImageMetadata getImageMetadata}(imageIndex)</code>,  wraps
     * the result in a {@link GeographicMetadata} object if non-null and caches the result.
     * <p>
     * Note that this method forces {@link #ignoreMetadata} to {@code false} for the time of
     * <code>{@linkplain #getImageMetadata getImageMetadata}(imageIndex)</code> execution,
     * because some image reader implementations need geographic metadata in order to infer
     * a valid {@linkplain ColorModel color model}.
     *
     * @param  imageIndex The image index.
     * @return The geographic metadata, or {@code null} if none.
     * @throws IOException if an error occurs during reading.
     */
    public GeographicMetadata getGeographicMetadata(final int imageIndex) throws IOException {
        // Checks if a cached instance is available.
        if (metadata != null && imageIndex >= 0 && imageIndex < metadata.length) {
            final GeographicMetadata parser = metadata[imageIndex];
            if (parser != null) {
                return parser;
            }
        }
        // Checks if metadata are availables. If the user set 'ignoreMetadata' to 'true',
        // we override his setting since we really need metadata for creating a ColorModel.
        final IIOMetadata candidate;
        final boolean oldIgnore = ignoreMetadata;
        try {
            ignoreMetadata = false;
            candidate = getImageMetadata(imageIndex);
        } finally {
            ignoreMetadata = oldIgnore;
        }
        if (candidate == null) {
            return null;
        }
        // Wraps the IIOMetadata into a GeographicMetadata object,
        // if it was not already of the appropriate type.
        final GeographicMetadata parser;
        if (candidate instanceof GeographicMetadata) {
            parser = (GeographicMetadata) candidate;
        } else {
            parser = new GeographicMetadata(this);
            parser.mergeTree(candidate);
        }
        if (metadata == null) {
            metadata = new GeographicMetadata[Math.max(imageIndex+1, 4)];
        }
        if (imageIndex >= metadata.length) {
            metadata = XArray.resize(metadata, Math.max(imageIndex+1, metadata.length*2));
        }
        metadata[imageIndex] = parser;
        return parser;
    }

    /**
     * Returns a collection of {@link ImageTypeSpecifier} containing possible image types to which
     * the given image may be decoded. The default implementation returns a singleton containing
     * <code>{@link #getRawImageType(int) getRawImageType}(imageIndex)</code>.
     *
     * @param  imageIndex The index of the image to be retrieved.
     * @return A set of suggested image types for decoding the current given image.
     * @throws IOException If an error occurs reading the format information from the input source.
     */
    public Iterator<ImageTypeSpecifier> getImageTypes(final int imageIndex) throws IOException {
        return Collections.singleton(getRawImageType(imageIndex)).iterator();
    }

    /**
     * Returns an image type specifier indicating the {@link SampleModel} and {@link ColorModel}
     * which most closely represents the "raw" internal format of the image. The default
     * implementation delegates to the following:
     *
     * <blockquote><code>{@linkplain #getRawImageType(int,ImageReadParam,SampleConverter[])
     * getRawImageType}(imageIndex, {@linkplain #getDefaultReadParam()}, null);</code></blockquote>
     *
     * If this method needs to be overriden, consider overriding the later instead.
     *
     * @param  imageIndex The index of the image to be queried.
     * @return The image type (never {@code null}).
     * @throws IOException If an error occurs reading the format information from the input source.
     */
    @Override
    public ImageTypeSpecifier getRawImageType(final int imageIndex) throws IOException {
        return getRawImageType(imageIndex, getDefaultReadParam(), null);
    }

    /**
     * Returns an image type specifier indicating the {@link SampleModel} and {@link ColorModel}
     * which most closely represents the "raw" internal format of the image. The default
     * implementation applies the following rules:
     *
     * <ol>
     *   <li><p>The {@linkplain Band#getValidRange range of expected values} and the
     *       {@linkplain Band#getNoDataValues no-data values} are extracted from the
     *       {@linkplain #getGeographicMetadata geographic metadata}, if any.</p></li>
     *
     *   <li><p>If the given {@code parameters} argument is an instance of {@link GeographicImageReadParam},
     *       then the user-supplied {@linkplain GeographicImageReadParam#getPaletteName palette name}
     *       is fetched. Otherwise or if no palette name was explicitly set, then this method default
     *       to {@value org.geotools.image.io.GeographicImageReadParam#DEFAULT_PALETTE_NAME}. The
     *       palette name will be used in order to {@linkplain PaletteFactory#getColors read a
     *       predefined set of colors} (as RGB values) to be given to the
     *       {@linkplain IndexColorModel index color model}.</p></li>
     *
     *   <li><p>If the {@linkplain #getRawDataType raw data type} is {@link DataBuffer#TYPE_FLOAT
     *       TYPE_FLOAT} or {@link DataBuffer#TYPE_DOUBLE TYPE_DOUBLE}, then this method builds
     *       a {@linkplain PaletteFactory#getContinuousPalette continuous palette} suitable for
     *       the range fetched at step 1. The data are assumed <cite>geophysics</cite> values
     *       rather than some packed values. Consequently, the {@linkplain SampleConverter sample
     *       converters} will replace no-data values by {@linkplain Float#NaN NaN} with no other
     *       changes.</p></li>
     *
     *   <li><p>Otherwise, if the {@linkplain #getRawDataType raw data type} is a unsigned integer type
     *       like {@link DataBuffer#TYPE_BYTE TYPE_BYTE} or {@link DataBuffer#TYPE_USHORT TYPE_USHORT},
     *       then this method builds an {@linkplain PaletteFactory#getPalette indexed palette} (i.e. a
     *       palette backed by an {@linkplain IndexColorModel index color model}) with just the minimal
     *       {@linkplain IndexColorModel#getMapSize size} needed for containing fully the range and the
     *       no-data values fetched at step 1. The data are assumed <cite>packed</cite> values rather
     *       than geophysics values. Consequently, the {@linkplain SampleConverter sample converters}
     *       will be the {@linkplain SampleConverter#IDENTITY identity converter} except in the
     *       following cases:
     *       <ul>
     *         <li>The {@linkplain Band#getValidRange range of valid values} is outside the range
     *             allowed by the {@linkplain #getRawDataType raw data type} (e.g. the range of
     *             valid values contains negative integers). In this case, the sample converter
     *             will shift the values to a strictly positive range and replace no-data values
     *             by 0.</li>
     *         <li>At least one {@linkplain Band#getNoDataValues no-data value} is outside the range
     *             of values allowed by the {@linkplain #getRawDataType raw data type}. In this case,
     *             this method will try to only replace the no-data values by 0, without shifting
     *             the valid values if this shift can be avoided.</li>
     *         <li>At least one {@linkplain Band#getNoDataValues no-data value} is far away from the
     *             {@linkplain Band#getValidRange range of valid values} (for example 9999 while
     *             the range of valid values is [0..255]). The meaning of "far away" is determined
     *             by the {@link #collapseNoDataValues collapseNoDataValues} method.</li>
     *       </ul>
     *       </p></li>
     *
     *   <li><p>Otherwise, if the {@linkplain #getRawDataType raw data type} is a signed integer
     *       type like {@link DataBuffer#TYPE_SHORT TYPE_SHORT}, then this method builds an
     *       {@linkplain PaletteFactory#getPalette indexed palette} with the maximal {@linkplain
     *       IndexColorModel#getMapSize size} supported by the raw data type (note that this is
     *       memory expensive - typically 256 kilobytes). Negative values will be stored in their
     *       two's complement binary form in order to fit in the range of positive integers
     *       supported by the {@linkplain IndexColorModel index color model}.</p></li>
     * </ol>
     *
     * <h3>Overriding this method</h3>
     * Subclasses may override this method when a constant color {@linkplain Palette palette} is
     * wanted for all images in a series, for example for all <cite>Sea Surface Temperature</cite>
     * (SST) from the same provider. A constant color palette facilitates the visual comparaison
     * of different images at different time. The example below creates hard-coded objects:
     *
     * <blockquote><code>
     * int minimum    = -2000; // </code>minimal expected value<code><br>
     * int maximum    = +2300; // </code>maximal expected value<code><br>
     * int fillValue  = -9999; // </code>Value for missing data<code><br>
     * String palette = "SST-Nasa";// </code>Named set of RGB colors<code><br>
     * converters[0] = {@linkplain SampleConverter#createOffset(double,double)
     * SampleConverter.createOffset}(1 - minimum, fillValue);<br>
     * return {@linkplain PaletteFactory#getDefault()}.{@linkplain PaletteFactory#getPalettePadValueFirst
     * getPalettePadValueFirst}(paletteName, maximum - minimum).{@linkplain Palette#getImageTypeSpecifier
     * getImageTypeSpecifier}();
     * </code></blockquote>
     *
     * @param imageIndex
     *              The index of the image to be queried.
     * @param parameters
     *              The user-supplied parameters, or {@code null}. Note: we recommand to supply
     *              {@link #getDefaultReadParam} instead of {@code null} since subclasses may
     *              override the later with default values suitable to a particular format.
     * @param  converters
     *              If non-null, an array where to store the converters created by this method.
     *              Those converters should be used by <code>{@linkplain #read(int,ImageReadParam)
     *              read}(imageIndex, parameters)</code> implementations for converting the values
     *              read in the datafile to values acceptable for the underling {@linkplain
     *              ColorModel color model}.
     * @return
     *              The image type (never {@code null}).
     * @throws IOException
     *              If an error occurs while reading the format information from the input source.
     *
     * @see #getRawDataType
     * @see #collapseNoDataValues
     * @see #getDestination(int, ImageReadParam, int, int, SampleConverter[])
     */
    protected ImageTypeSpecifier getRawImageType(final int               imageIndex,
                                                 final ImageReadParam    parameters,
                                                 final SampleConverter[] converters)
            throws IOException
    {
        /*
         * Gets the minimal and maximal values allowed for the target image type.
         * Note that this is meanless for floating point types, so the values in
         * that case are arbitrary.
         *
         * The only integer types that are signed are SHORT (not to be confused with
         * USHORT) and INT. Other types like BYTE and USHORT are treated as unsigned.
         */
        final boolean isFloat;
        final long floor, ceil;
        final int dataType = getRawDataType(imageIndex);
        switch (dataType) {
            case DataBuffer.TYPE_UNDEFINED: // Actually we don't really know what to do for this case...
            case DataBuffer.TYPE_DOUBLE:    // Fall through since we can treat this case as float.
            case DataBuffer.TYPE_FLOAT: {
                isFloat = true;
                floor   = Long.MIN_VALUE;
                ceil    = Long.MAX_VALUE;
                break;
            }
            case DataBuffer.TYPE_INT: {
                isFloat = false;
                floor   = Integer.MIN_VALUE;
                ceil    = Integer.MAX_VALUE;
                break;
            }
            case DataBuffer.TYPE_SHORT: {
                isFloat = false;
                floor   = Short.MIN_VALUE;
                ceil    = Short.MAX_VALUE;
                break;
            }
            default: {
                isFloat = false;
                floor   = 0;
                ceil    = (1L << DataBuffer.getDataTypeSize(dataType)) - 1;
                break;
            }
        }
        /*
         * Extracts all informations we will need from the user-supplied parameters, if any.
         */
        final String paletteName;
        final int[]  sourceBands;
        final int[]  targetBands;
        final int    visibleBand;
        if (parameters != null) {
            sourceBands = parameters.getSourceBands();
            targetBands = parameters.getDestinationBands();
        } else {
            sourceBands = null;
            targetBands = null;
        }
        if (parameters instanceof GeographicImageReadParam) {
            final GeographicImageReadParam geoparam = (GeographicImageReadParam) parameters;
            paletteName = geoparam.getNonNullPaletteName();
            visibleBand = geoparam.getVisibleBand();
        } else {
            paletteName = GeographicImageReadParam.DEFAULT_PALETTE_NAME;
            visibleBand = 0;
        }
        final int numBands;
        if (sourceBands != null) {
            numBands = sourceBands.length;
        } else if (targetBands != null) {
            numBands = targetBands.length;
        } else {
            numBands = getNumBands(imageIndex);
        }
        /*
         * Computes a range of values for all bands, as the union in order to make sure that
         * we can stores every sample values. Also creates SampleConverters in the process.
         * The later is an opportunist action since we gather most of the needed information
         * during the loop.
         */
        NumberRange     allRanges        = null;
        NumberRange     visibleRange     = null;
        SampleConverter visibleConverter = SampleConverter.IDENTITY;
        double          maximumFillValue = 0; // Only in the visible band, and must be positive.
        final GeographicMetadata metadata = getGeographicMetadata(imageIndex);
        if (metadata != null) {
            final int numMetadataBands = metadata.getNumBands();
            for (int i=0; i<numBands; i++) {
                final int sourceBand = (sourceBands != null) ? sourceBands[i] : i;
                if (sourceBand < 0 || sourceBand >= numMetadataBands) {
                    if (numMetadataBands != 1) {
                        /*
                         * If the metadata declares excactly one band, we assume that it is aimed
                         * to applied for all ImageReader band. We need to apply this patch for
                         * now because of an inconsistency between Metadata and ImageReader in
                         * NetCDF files: the former associates NetCDF variables to bands, while
                         * the later associates NetCDF variables to image index.
                         *
                         * TODO: We need to fix this inconcistency after we reviewed
                         * GeographicMetadata.
                         */
                        warningOccurred("getRawImageType", indexOutOfBounds(sourceBand, 0, numMetadataBands));
                    }
                    if (numMetadataBands == 0) {
                        break; // We are sure that next bands will not be better.
                    }
                }
                final Band band = metadata.getBand(Math.min(sourceBand, numMetadataBands-1));
                final double[] nodataValues = band.getNoDataValues();
                final NumberRange range = band.getValidRange();
                double minimum, maximum;
                if (range != null) {
                    minimum = range.getMinimum();
                    maximum = range.getMaximum();
                    if (!isFloat) {
                        // If the metadata do not contain any information about the range,
                        // treat as if we use the maximal range allowed by the data type.
                        if (minimum == Double.NEGATIVE_INFINITY) minimum = floor;
                        if (maximum == Double.POSITIVE_INFINITY) maximum = ceil;
                    }
                    final double extent = maximum - minimum;
                    if (extent >= 0 && (isFloat || extent <= (ceil - floor))) {
                        allRanges = (allRanges != null) ? allRanges.union(range) : range;
                    } else {
                        // Use range.getMin/MaxValue() because they may be integers rather than doubles.
                        warningOccurred("getRawImageType", Errors.format(ErrorKeys.BAD_RANGE_$2,
                                range.getMinValue(), range.getMaxValue()));
                        continue;
                    }
                } else {
                    minimum = Double.NaN;
                    maximum = Double.NaN;
                }
                final int targetBand = (targetBands != null) ? targetBands[i] : i;
                /*
                 * For floating point types, replaces no-data values by NaN because the floating
                 * point numbers are typically used for geophysics data, so the raster is likely
                 * to be a "geophysics" view for GridCoverage2D. All other values are stored "as
                 * is" without any offset.
                 *
                 * For integer types, if the range of values from the source data file fits into
                 * the range of values allowed by the destination raster, we will use an identity
                 * converter. If the only required conversion is a shift from negative to positive
                 * values, creates an offset converter with no-data values collapsed to 0.
                 */
                final SampleConverter converter;
                if (isFloat) {
                    converter = SampleConverter.createPadValuesMask(nodataValues);
                } else {
                    final boolean isZeroValid = (minimum <= 0 && maximum >= 0);
                    boolean collapsePadValues = false;
                    if (nodataValues != null && nodataValues.length != 0) {
                        final double[] sorted = nodataValues.clone();
                        Arrays.sort(sorted);
                        double minFill = sorted[0];
                        double maxFill = minFill;
                        int indexMax = sorted.length;
                        while (--indexMax!=0 && Double.isNaN(maxFill = sorted[indexMax]));
                        assert minFill <= maxFill || Double.isNaN(minFill) : maxFill;
                        if (targetBand == visibleBand && maxFill > maximumFillValue) {
                            maximumFillValue = maxFill;
                        }
                        if (minFill < floor || maxFill > ceil) {
                            // At least one fill value is outside the range of acceptable values.
                            collapsePadValues = true;
                        } else if (minimum >= 0) {
                            /*
                             * Arbitrary optimization of memory usage:  if there is a "large" empty
                             * space between the range of valid values and a no-data value, then we
                             * may (at subclass implementors choice) collapse the no-data values to
                             * zero in order to avoid wasting the empty space.  Note that we do not
                             * perform this collapse if the valid range contains negative values
                             * because it would not save any memory. We do not check the no-data
                             * values between 0 and 'minimum' for the same reason.
                             */
                            int k = Arrays.binarySearch(sorted, maximum);
                            if (k >= 0) k++; // We want the first element greater than maximum.
                            else k = ~k; // Really ~ operator, not -
                            if (k <= indexMax) {
                                double unusedSpace = Math.max(sorted[k] - maximum - 1, 0);
                                while (++k <= indexMax) {
                                    final double delta = sorted[k] - sorted[k-1] - 1;
                                    if (delta > 0) {
                                        unusedSpace += delta;
                                    }
                                }
                                final int unused = (int) Math.min(Math.round(unusedSpace), Integer.MAX_VALUE);
                                collapsePadValues = collapseNoDataValues(isZeroValid, sorted, unused);
                                // We invoked 'collapseNoDataValues' inconditionnaly even if
                                // 'unused' is zero because the user may decide on the basis
                                // of other criterions, like 'isZeroValid'.
                            }
                        }
                    }
                    if (minimum < floor || maximum > ceil) {
                        // The range of valid values is outside the range allowed by raw data type.
                        converter = SampleConverter.createOffset(1 - minimum, nodataValues);
                    } else if (collapsePadValues) {
                        if (isZeroValid) {
                            // We need to collapse the no-data values to 0, but it causes a clash
                            // with the range of valid values. So we also shift the later.
                            converter = SampleConverter.createOffset(1 - minimum, nodataValues);
                        } else {
                            // We need to collapse the no-data values and there is no clash.
                            converter = SampleConverter.createPadValuesMask(nodataValues);
                        }
                    } else {
                        /*
                         * Do NOT take 'nodataValues' in account if there is no need to collapse
                         * them. This is not the converter's job to transform "packed" values to
                         * "geophysics" values. We just want them to fit in the IndexColorModel,
                         * and they already fit. So the identity converter is appropriate even
                         * in presence of pad values.
                         */
                        converter = SampleConverter.IDENTITY;
                    }
                }
                if (converters!=null && targetBand>=0 && targetBand<converters.length) {
                    converters[targetBand] = converter;
                }
                if (targetBand == visibleBand) {
                    visibleConverter = converter;
                    visibleRange = range;
                }
            }
        }
        /*
         * Creates a color palette suitable for the range of values in the visible band.
         * The case for floating points is the simpliest: we should not have any offset,
         * at most a replacement of no-data values. In the case of integer values, we
         * must make sure that the indexed color map is large enough for containing both
         * the highest data value and the highest no-data value.
         */
        if (visibleRange == null) {
            visibleRange = (allRanges != null) ? allRanges : new NumberRange(floor, ceil);
        }
        final PaletteFactory factory = PaletteFactory.getDefault();
        factory.setWarningLocale(locale);
        final Palette palette;
        if (isFloat) {
            assert visibleConverter.getOffset() == 0 : visibleConverter;
            palette = factory.getContinuousPalette(paletteName, (float) visibleRange.getMinimum(),
                    (float) visibleRange.getMaximum(), dataType, numBands, visibleBand);
        } else {
            final double offset  = visibleConverter.getOffset();
            final double minimum = visibleRange.getMinimum();
            final double maximum = visibleRange.getMaximum();
            long lower, upper;
            if (minimum == Double.NEGATIVE_INFINITY) {
                lower = floor;
            } else {
                lower = Math.round(minimum + offset);
                if (!visibleRange.isMinIncluded()) {
                    lower++; // Must be inclusive
                }
            }
            if (maximum == Double.POSITIVE_INFINITY) {
                upper = ceil;
            } else {
                upper = Math.round(maximum + offset);
                if (visibleRange.isMaxIncluded()) {
                    upper++; // Must be exclusive
                }
            }
            final long size = Math.max(upper, Math.round(maximumFillValue) + 1);
            /*
             * The target lower, upper and size parameters are usually in the range of SHORT
             * or USHORT data type. The Palette class will performs the necessary checks and
             * throws an exception if those variables are out of range.  However, because we
             * need to cast to int before passing the parameter values,  we restrict them to
             * the 'int' range as a safety in order to avoid results that accidently fall in
             * the SHORT or USHORT range.  Because Integer.MIN_VALUE or MAX_VALUE are out of
             * range,  it doesn't matter if those values are inaccurate since we will get an
             * exception anyway.
             */
            palette = factory.getPalette(paletteName,
                    (int) Math.max(lower, Integer.MIN_VALUE),
                    (int) Math.min(upper, Integer.MAX_VALUE),
                    (int) Math.min(size,  Integer.MAX_VALUE), numBands, visibleBand);
        }
        return palette.getImageTypeSpecifier();
    }

    /**
     * Returns the data type which most closely represents the "raw" internal data of the image.
     * It should be one of {@link DataBuffer} constants. The default {@code GeographicImageReader}
     * implementation works better with the following types:
     *
     * {@link DataBuffer#TYPE_BYTE   TYPE_BYTE},
     * {@link DataBuffer#TYPE_SHORT  TYPE_SHORT},
     * {@link DataBuffer#TYPE_USHORT TYPE_USHORT} and
     * {@link DataBuffer#TYPE_FLOAT  TYPE_FLOAT}.
     *
     * The default implementation returns {@link DataBuffer#TYPE_FLOAT TYPE_FLOAT} in every cases.
     * <p>
     * <h3>Handling of negative integer values</h3>
     * If the raw internal data contains negative values but this method still declares a unsigned
     * integer type ({@link DataBuffer#TYPE_BYTE TYPE_BYTE} or {@link DataBuffer#TYPE_USHORT TYPE_USHORT}),
     * then the values will be translated in order to fit in the range of strictly positive values.
     * For example if the raw internal data range from -23000 to +23000, then there is a choice:
     *
     * <ul>
     *   <li><p>If this method returns {@link DataBuffer#TYPE_SHORT}, then the data will be
     *       stored "as is" without transformation. However the {@linkplain IndexColorModel
     *       index color model} will have the maximal length allowed by 16 bits integers, with
     *       positive values in the [0 .. {@value java.lang.Short#MAX_VALUE}] range and negative
     *       values wrapped in the [32768 .. 65535] range in two's complement binary form. The
     *       results is a color model consuming 256 kilobytes in every cases. The space not used
     *       by the [-23000 .. +23000] range (in the above example) is lost.</p></li>
     *
     *   <li><p>If this method returns {@link DataBuffer#TYPE_USHORT}, then the data will be
     *       translated to the smallest strictly positive range that can holds the data
     *       ([1..46000] for the above example). Value 0 is reserved for missing data. The
     *       result is a smaller {@linkplain IndexColorModel index color model} than the
     *       one used by untranslated data.</p></li>
     * </ul>
     *
     * @param  imageIndex The index of the image to be queried.
     * @return The data type ({@link DataBuffer#TYPE_FLOAT} by default).
     * @throws IOException If an error occurs reading the format information from the input source.
     *
     * @see #getRawImageType(int, ImageReadParam, SampleConverter[])
     */
    protected int getRawDataType(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        return DataBuffer.TYPE_FLOAT;
    }

    /**
     * Returns {@code true} if the no-data values should be collapsed to 0 in order to save memory.
     * This method is invoked automatically by the {@link #getRawImageType(int, ImageReadParam,
     * SampleConverter[]) getRawImageType} method when it detected some unused space between the
     * {@linkplain Band#getValidRange range of valid values} and at least one
     * {@linkplain Band#getNoDataValues no-data value}.
     * <p>
     * The default implementation returns {@code false} in all cases, thus avoiding arbitrary
     * choice. Subclasses can override this method with some arbitrary threashold, as in the
     * example below:
     *
     * <blockquote><pre>
     * return unusedSpace >= 1024;
     * </pre></blockquote>
     *
     * @param isZeroValid
     *          {@code true} if 0 is a valid value. If this method returns {@code true} while
     *          {@code isZeroValid} is {@code true}, then the {@linkplain SampleConverter sample
     *          converter} to be returned by {@link #getRawImageType(int, ImageReadParam,
     *          SampleConverter[]) getRawImageType} will offset all valid values by 1.
     * @param nodataValues
     *          The {@linkplain Arrays#sort(double[]) sorted}
     *          {@linkplain Band#getNoDataValues no-data values} (never null and never empty).
     * @param unusedSpace
     *          The largest amount of unused space outside the range of valid values.
     */
    protected boolean collapseNoDataValues(final boolean  isZeroValid,
                                           final double[] nodataValues,
                                           final int      unusedSpace)
    {
        return false;
    }

    /**
     * Returns the buffered image to which decoded pixel data should be written. The image
     * is determined by inspecting the supplied parameters if it is non-null, as described
     * in the {@linkplain #getDestination(ImageReadParam,Iterator,int,int) super-class method}.
     * In the default implementation, the {@linkplain ImageTypeSpecifier image type specifier}
     * set is a singleton containing only the {@linkplain #getRawImageType(int,ImageReadParam,
     * SampleConverter[]) raw image type}.
     * <p>
     * Implementations of the {@link #read(int,ImageReadParam)} method should invoke this
     * method instead of {@link #getDestination(ImageReadParam,Iterator,int,int)}.
     *
     * @param  imageIndex The index of the image to be retrieved.
     * @param  parameters The parameter given to the {@code read} method.
     * @param  width      The true width of the image or tile begin decoded.
     * @param  height     The true width of the image or tile being decoded.
     * @param  converters If non-null, an array where to store the converters required
     *                    for converting decoded pixel data into stored pixel data.
     * @return The buffered image to which decoded pixel data should be written.
     *
     * @throws IOException If an error occurs reading the format information from the input source.
     *
     * @see #getRawImageType(int, ImageReadParam, SampleConverter[])
     */
    protected BufferedImage getDestination(final int imageIndex, final ImageReadParam parameters,
                            final int width, final int height, final SampleConverter[] converters)
            throws IOException
    {
        final ImageTypeSpecifier type = getRawImageType(imageIndex, parameters, converters);
        final Set<ImageTypeSpecifier> spi = Collections.singleton(type);
        return getDestination(parameters, spi.iterator(), width, height);
    }

    /**
     * Returns a default parameter object appropriate for this format. The default
     * implementation constructs and returns a new {@link GeographicImageReadParam}.
     *
     * @return An {@code ImageReadParam} object which may be used.
     *
     * @todo Replace the return type by {@link GeographicImageReadParam} when we will
     *       be allowed to compile for J2SE 1.5.
     */
    @Override
    public ImageReadParam getDefaultReadParam() {
        return new GeographicImageReadParam(this);
    }

    /**
     * Reads the image indexed by {@code imageIndex} using a default {@link ImageReadParam}.
     * This is a convenience method that calls <code>{@linkplain #read(int,ImageReadParam)
     * read}(imageIndex, {@linkplain #getDefaultReadParam})</code>.
     * <p>
     * The default Java implementation passed a {@code null} parameter. This implementation
     * passes the default parameter instead in order to improve consistency when a subclass
     * overrides {@link #getDefaultReadParam}.
     *
     * @param imageIndex the index of the image to be retrieved.
     * @return the desired portion of the image.
     *
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs during reading.
     */
    @Override
    public BufferedImage read(final int imageIndex) throws IOException {
        return read(imageIndex, getDefaultReadParam());
    }

    /**
     * Flips the source region vertically. This method should be invoked straight after
     * {@link #computeRegions computeRegions} when the image to be read will be flipped
     * vertically, for example when the {@linkplain java.awt.image.Raster raster} sample
     * values are filled in a "{@code for (y=ymax-1; y>=ymin; y--)}" loop instead of
     * "{@code for (y=ymin; y<ymax; y++)}".
     * <p>
     * This method should be invoked as in the example below:
     *
     * <blockquote><pre>
     * computeRegions(param, srcWidth, srcHeight, image, srcRegion, destRegion);
     * flipVertically(param, srcHeight, srcRegion);
     * </pre></blockquote>
     *
     * @param param     The {@code param}     argument given to {@code computeRegions}.
     * @param srcHeight The {@code srcHeight} argument given to {@code computeRegions}.
     * @param srcRegion The {@code srcRegion} argument given to {@code computeRegions}.
     */
    protected static void flipVertically(final ImageReadParam param, final int srcHeight,
                                         final Rectangle srcRegion)
    {
        final int spaceLeft = srcRegion.y;
        srcRegion.y = srcHeight - (srcRegion.y + srcRegion.height);
        /*
         * After the flip performed by the above line, we still have 'spaceLeft' pixels left for
         * a downward translation.  We usually don't need to care about if, except if the source
         * region is very close to the bottom of the source image,  in which case the correction
         * computed below may be greater than the space left.
         *
         * We are done if there is no vertical subsampling. But if there is subsampling, then we
         * need an adjustment. The flipping performed above must be computed as if the source
         * region had exactly the size needed for reading nothing more than the last line, i.e.
         * 'srcRegion.height' must be a multiple of 'sourceYSubsampling' plus 1. The "offset"
         * correction is computed below accordingly.
         */
        if (param != null) {
            int offset = (srcRegion.height - 1) % param.getSourceYSubsampling();
            srcRegion.y += offset;
            offset -= spaceLeft;
            if (offset > 0) {
                // Happen only if we are very close to image border and
                // the above translation bring us outside the image area.
                srcRegion.height -= offset;
            }
        }
    }

    /**
     * Invoked when a warning occured. The default implementation make the following choice:
     * <p>
     * <ul>
     *   <li>If at least one {@linkplain IIOReadWarningListener warning listener}
     *       has been {@linkplain #addIIOReadWarningListener specified}, then the
     *       {@link IIOReadWarningListener#warningOccurred warningOccurred} method is
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
     * Convenience method for logging a warning from the given method.
     */
    private void warningOccurred(final String method, final String message) {
        final LogRecord record = new LogRecord(Level.WARNING, message);
        record.setSourceClassName(GeographicImageReader.class.getName());
        record.setSourceMethodName(method);
        warningOccurred(record);
    }

    /**
     * To be overriden and made {@code protected} by {@link StreamImageReader} only.
     */
    void close() throws IOException {
        metadata = null;
    }
}
