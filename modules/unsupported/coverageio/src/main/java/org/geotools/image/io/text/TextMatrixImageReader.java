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
package org.geotools.image.io.text;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferFloat;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.metadata.IIOMetadata;

import org.geotools.io.LineFormat;
import org.geotools.factory.GeoTools;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Descriptions;
import org.geotools.resources.i18n.DescriptionKeys;
import org.geotools.image.io.metadata.GeographicMetadata;


/**
 * An image decoder for matrix of floating-point numbers. The default implementation creates
 * rasters of {@link DataBuffer#TYPE_FLOAT}. An easy way to change this type is to overwrite
 * the {@link #getRawDataType} method.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class TextMatrixImageReader extends TextImageReader {
    /**
     * The matrix data loaded by {@link #load} method.
     */
    private float[] data;

    /**
     * The image width. This number is valid only if {@link #data} is non-null.
     */
    private int width;

    /**
     * The image height. This number is valid only if {@link #completed} is true.
     */
    private int height;

    /**
     * The expected height, or 0 if unknow. This number
     * has no signification if {@link #data} is null.
     */
    private int expectedHeight;

    /**
     * {@code true} if {@link #data} contains all data, or {@code false}
     * if {@link #data} contains only the first line. This field has no
     * signification if {@link #data} is null.
     */
    private boolean completed;

    /**
     * Constructs a new image reader.
     *
     * @param provider the provider that is invoking this constructor, or {@code null} if none.
     */
    protected TextMatrixImageReader(final ImageReaderSpi provider) {
        super(provider);
    }

    /**
     * Load data. No subsampling is performed.
     *
     * @param  imageIndex the index of the image to be read.
     * @param  all {@code true} to read all data, or {@code false} to read only the first line.
     * @return {@code true} if reading has been aborted.
     * @throws IOException If an error occurs reading the width information from the input source.
     */
    private boolean load(final int imageIndex, final boolean all) throws IOException {
        clearAbortRequest();
        if (all) {
            processImageStarted(imageIndex);
        }
        float[] values = (data != null) ? new float[width] : null;
        int     offset = width*height;

        final BufferedReader input = getReader();
        final LineFormat    format = getLineFormat(imageIndex);
        final float       padValue = (float)getPadValue(imageIndex);
        String line; while ((line=input.readLine())!=null) {
            if (isComment(line)) {
                continue;
            }
            try {
                format.setLine(line);
                values = format.getValues(values);
                for (int i=values.length; --i>=0;) {
                    if (values[i] == padValue) {
                        values[i] = Float.NaN;
                    }
                }
            } catch (ParseException exception) {
                throw new IIOException(getPositionString(exception.getLocalizedMessage()), exception);
            }
            if (data == null) {
                data = new float[1024];
            }
            final int newOffset = offset + (width=values.length);
            if (newOffset > data.length) {
                data = XArray.resize(data, newOffset+Math.min(newOffset, 65536));
            }
            System.arraycopy(values, 0, data, offset, width);
            offset = newOffset;
            height++;
            /*
             * If only one line was requested, try to guess the expected height.
             */
            if (!all) {
                final long streamLength = getStreamLength(imageIndex, imageIndex+1);
                if (streamLength >= 0) {
                    expectedHeight = (int) (streamLength / (line.length() + 1));
                }
                break;
            }
            /*
             * Update progress.
             */
            if (height <= expectedHeight) {
                processImageProgress(height*100f/expectedHeight);
                if (abortRequested()) {
                    processReadAborted();
                    return true;
                }
            }
        }
        if ((completed = all) == true) {
            data = XArray.resize(data, offset);
            expectedHeight = height;
        }
        if (all) {
            processImageComplete();
        }
        return false;
    }

    /**
     * Returns the width in pixels of the given image within the input source.
     *
     * @param  imageIndex the index of the image to be queried.
     * @return Image width.
     * @throws IOException If an error occurs reading the width information
     *         from the input source.
     */
    public int getWidth(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        if (data == null) {
            load(imageIndex, false);
        }
        return width;
    }

    /**
     * Returns the height in pixels of the given image within the input source.
     * Calling this method may force loading of full image.
     *
     * @param  imageIndex the index of the image to be queried.
     * @return Image height.
     * @throws IOException If an error occurs reading the height information
     *         from the input source.
     */
    public int getHeight(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        if (data == null || !completed) {
            load(imageIndex, true);
        }
        return height;
    }

    /**
     * Returns metadata associated with the given image.
     * Calling this method may force loading of full image.
     *
     * @param  imageIndex The image index.
     * @return The metadata, or {@code null} if none.
     * @throws IOException If an error occurs reading the data information from the input source.
     */
    @Override
    public IIOMetadata getImageMetadata(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        if (!ignoreMetadata) {
            if (data == null || !completed) {
                load(imageIndex, true);
            }
            float minimum = Float.POSITIVE_INFINITY;
            float maximum = Float.NEGATIVE_INFINITY;
            for (int i=0; i<data.length; i++) {
                final float value = data[i];
                if (value < minimum) minimum = value;
                if (value > maximum) maximum = value;
            }
            if (minimum < maximum) {
                final GeographicMetadata metadata = new GeographicMetadata(this);
                metadata.getBand(0).setValidRange(minimum, maximum);
                return metadata;
            }
        }
        return null;
    }

    /**
     * Reads the image indexed by {@code imageIndex}.
     *
     * @param  imageIndex  The index of the image to be retrieved.
     * @param  param       Parameters used to control the reading process, or null.
     * @return The desired portion of the image.
     * @throws IOException if an input operation failed.
     */
    public BufferedImage read(final int imageIndex, final ImageReadParam param) throws IOException {
        /*
         * Parameters check.
         */
        final int numSrcBands = 1;
        final int numDstBands = 1;
        checkImageIndex(imageIndex);
        checkReadParamBandSettings(param, numSrcBands, numDstBands);
        /*
         * Extract user's parameters.
         */
        final int[]      sourceBands;
        final int[] destinationBands;
        final int sourceXSubsampling;
        final int sourceYSubsampling;
        final int subsamplingXOffset;
        final int subsamplingYOffset;
        final int destinationXOffset;
        final int destinationYOffset;
        if (param != null) {
            sourceBands        = param.getSourceBands();
            destinationBands   = param.getDestinationBands();
            final Point offset = param.getDestinationOffset();
            sourceXSubsampling = param.getSourceXSubsampling();
            sourceYSubsampling = param.getSourceYSubsampling();
            subsamplingXOffset = param.getSubsamplingXOffset();
            subsamplingYOffset = param.getSubsamplingYOffset();
            destinationXOffset = offset.x;
            destinationYOffset = offset.y;
        } else {
            sourceBands        = null;
            destinationBands   = null;
            sourceXSubsampling = 1;
            sourceYSubsampling = 1;
            subsamplingXOffset = 0;
            subsamplingYOffset = 0;
            destinationXOffset = 0;
            destinationYOffset = 0;
        }
        /*
         * Compute source region and check for possible optimization.
         */
        final Rectangle srcRegion = getSourceRegion(param, width, height);
        final boolean isDirect = sourceXSubsampling==1 && sourceYSubsampling==1    &&
        subsamplingXOffset==0 && subsamplingYOffset==0    &&
        destinationXOffset==0 && destinationYOffset==0    &&
        srcRegion.x       ==0 && srcRegion.width ==width  &&
        srcRegion.y       ==0 && srcRegion.height==height;
        /*
         * Read data if it was not already done.
         */
        if (data == null || !completed) {
            if (load(imageIndex, true)) {
                return null;
            }
        }
        /*
         * If a direct mapping is possible, perform it.
         */
        if (isDirect && (param==null || param.getDestination()==null)) {
            final ImageTypeSpecifier type = getRawImageType(imageIndex, param, null); // TODO: use SampleConverter
            final SampleModel       model = type.getSampleModel().createCompatibleSampleModel(width,height);
            final DataBuffer       buffer = new DataBufferFloat(data, data.length);
            final WritableRaster   raster = Raster.createWritableRaster(model, buffer, null);
            return new BufferedImage(type.getColorModel(), raster, false, null);
        }
        /*
         * Copy data into a new image.
         */
        final int              dstBand = 0;
        final BufferedImage      image = getDestination(imageIndex, param, width, height, null); // TODO
        final WritableRaster dstRaster = image.getRaster();
        final Rectangle      dstRegion = new Rectangle();
        computeRegions(param, width, height, image, srcRegion, dstRegion);
        final int dstXMin = dstRegion.x;
        final int dstYMin = dstRegion.y;
        final int dstXMax = dstRegion.width  + dstXMin;
        final int dstYMax = dstRegion.height + dstYMin;

        int srcY = srcRegion.y;
        for (int y=dstYMin; y<dstYMax; y++) {
            assert(srcY < srcRegion.y+srcRegion.height);
            int srcX = srcRegion.x;
            for (int x=dstXMin; x<dstXMax; x++) {
                assert(srcX < srcRegion.x+srcRegion.width);
                final float value = data[srcY*width+srcX];
                dstRaster.setSample(x, y, dstBand, value);
                srcX += sourceXSubsampling;
            }
            srcY += sourceYSubsampling;
        }
        return image;
    }

    /**
     * Closes the input stream and disposes the resources that was specific to that stream.
     */
    @Override
    public void close() throws IOException {
        completed      = false;
        data           = null;
        width          = 0;
        height         = 0;
        expectedHeight = 0;
        super.close();
    }




    /**
     * Service provider interface (SPI) for {@link TextMatrixImageReader}s. This SPI provides
     * the necessary implementation for creating default {@link TextMatrixImageReader} using
     * default locale and character set. Subclasses can set some fields at construction time
     * in order to tune the reader to a particular environment, e.g.:
     *
     * <blockquote><pre>
     * public final class MyCustomSpi extends TextMatrixImageReader.Spi {
     *     public MyCustomSpi() {
     *         {@link #names      names}      = new String[] {"myformat"};
     *         {@link #MIMETypes  MIMETypes}  = new String[] {"text/plain"};
     *         {@link #vendorName vendorName} = "Foo inc.";
     *         {@link #version    version}    = "1.0";
     *         {@link #locale     locale}     = Locale.US;
     *         {@link #charset    charset}    = Charset.forName("ISO-LATIN-1");
     *         {@link #padValue   padValue}   = 9999;
     *     }
     * }
     * </pre></blockquote>
     *
     * (Note: fields {@code vendorName} and {@code version} are only informatives).
     * There is no need to override any method in this example. However, developers
     * can gain more control by creating subclasses of {@link TextMatrixImageReader}
     * and {@code Spi}.
     *
     * @since 2.1
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static class Spi extends TextImageReader.Spi {
        /**
         * The format names for the default {@link TextMatrixImageReader} configuration.
         */
        static final String[] NAMES = {"matrix"};

        /**
         * The mime types for the default {@link TextMatrixImageReader} configuration.
         */
        static final String[] MIME_TYPES = {"application/matrix", "text/plain"};

        /**
         * Constructs a default {@code TextMatrixImageReader.Spi}. This constructor
         * provides the following defaults in addition to the defaults defined in the
         * {@linkplain TextImageReader.Spi#Spi super-class constructor}:
         *
         * <ul>
         *   <li>{@link #names}           = {@code "matrix"}</li>
         *   <li>{@link #MIMETypes}       = {@code "text/x-matrix"}</li>
         *   <li>{@link #pluginClassName} = {@code "org.geotools.image.io.text.TextMatrixImageReader"}</li>
         *   <li>{@link #vendorName}      = {@code "Geotools"}</li>
         * </ul>
         *
         * For efficienty reasons, the above fields are initialized to shared arrays. Subclasses
         * can assign new arrays, but should not modify the default array content.
         */
        public Spi() {
            names           = NAMES;
            MIMETypes       = MIME_TYPES;
            pluginClassName = "org.geotools.image.io.text.TextMatrixImageReader";
            vendorName      = "GeoTools";
            version         = GeoTools.getVersion().toString();
        }

        /**
         * Returns a brief, human-readable description of this service provider
         * and its associated implementation. The resulting string should be
         * localized for the supplied locale, if possible.
         *
         * @param  locale A Locale for which the return value should be localized.
         * @return A String containing a description of this service provider.
         */
        public String getDescription(final Locale locale) {
            return Descriptions.getResources(locale).getString(DescriptionKeys.CODEC_MATRIX);
        }

        /**
         * Returns an instance of the {@code ImageReader} implementation associated
         * with this service provider.
         *
         * @param  extension An optional extension object, which may be null.
         * @return An image reader instance.
         * @throws IOException if the attempt to instantiate the reader fails.
         */
        public ImageReader createReaderInstance(final Object extension) throws IOException {
            return new TextMatrixImageReader(this);
        }

        /**
         * Returns {@code true} if the specified row length is valid. The default implementation
         * returns {@code true} if the row seems "long", where "long" is arbitrary fixed to 10
         * columns. This is an arbitrary choice, which is why this method is not public. It may
         * be changed in any future Geotools version.
         */
        @Override
        boolean isValidColumnCount(final int count) {
            return count > 10;
        }
    }
}
