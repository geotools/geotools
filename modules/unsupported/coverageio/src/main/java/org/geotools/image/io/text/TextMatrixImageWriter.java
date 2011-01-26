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
package org.geotools.image.io.text;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.media.jai.iterator.RectIter;

import org.geotools.factory.GeoTools;
import org.geotools.image.ImageDimension;
import org.geotools.resources.i18n.DescriptionKeys;
import org.geotools.resources.i18n.Descriptions;
import org.geotools.util.Utilities;


/**
 * An image encoder for matrix of floating-point numbers.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class TextMatrixImageWriter extends TextImageWriter {
    /**
     * Amount of spaces to put between columns.
     */
    private static final int SEPARATOR_WIDTH = 1;

    /**
     * Constructs a new image writer.
     *
     * @param provider the provider that is invoking this constructor, or {@code null} if none.
     */
    protected TextMatrixImageWriter(final ImageWriterSpi provider) {
        super(provider);
    }

    /**
     * Appends a complete image stream containing a single image.
     *
     * @param streamMetadata The stream metadata (ignored in default implementation).
     * @param image The image or raster to be written.
     * @param parameters The write parameters, or null if the whole image will be written.
     */
    public void write(final IIOMetadata streamMetadata, final IIOImage image,
                      final ImageWriteParam parameters) throws IOException
    {
        processImageStarted();
        final BufferedWriter   out = getWriter(parameters);
        final String lineSeparator = getLineSeparator(parameters);
        final NumberFormat  format = createNumberFormat(image, parameters);
        final FieldPosition    pos = getExpectedFractionPosition(format);
        final int    fractionWidth = pos.getEndIndex() - pos.getBeginIndex();
        final int            width = pos.getEndIndex() + SEPARATOR_WIDTH;
        final StringBuffer  buffer = new StringBuffer(width);
        final RectIter    iterator = createRectIter(image, parameters);
        final ImageDimension  size = computeSize(image, parameters);
        final float  progressScale = 100f / size.getNumSampleValues();
        int numSampleValues = 0, nextProgress = 0;
        if (!iterator.finishedBands()) do {
            if (!iterator.finishedLines()) do {
                if (numSampleValues >= nextProgress) {
                    // Informs about progress only every 32 lines.
                    processImageProgress(progressScale * numSampleValues);
                    nextProgress = numSampleValues + 2000; // Reports after every 2000 numbers.
                }
                if (!iterator.finishedPixels()) do {
                    buffer.setLength(0);
                    String n = format.format(iterator.getSampleDouble(), buffer, pos).toString();
                    final int fractionOffset = Math.max(0, fractionWidth - (pos.getEndIndex() - pos.getBeginIndex()));
                    out.write(Utilities.spaces(width - n.length() - fractionOffset));
                    out.write(n);
                    out.write(Utilities.spaces(fractionOffset));
                } while (!iterator.nextPixelDone());
                out.write(lineSeparator);
                numSampleValues += size.width;
                iterator.startPixels();
            } while (!iterator.nextLineDone());
            out.write(lineSeparator); // Separate bands by a blank line.
            iterator.startLines();
        } while (!iterator.nextBandDone());
        out.flush();
        processImageComplete();
    }




    /**
     * Service provider interface (SPI) for {@link TextMatrixImageWriter}s.
     *
     * @since 2.4
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux
     */
    public static class Spi extends TextImageWriter.Spi {
        /**
         * Constructs a default {@code TextMatrixImageWriter.Spi}. This constructor
         * provides the following defaults in addition to the defaults defined in the
         * {@linkplain TextImageWriter.Spi#Spi super-class constructor}:
         *
         * <ul>
         *   <li>{@link #names}           = {@code "matrix"}</li>
         *   <li>{@link #MIMETypes}       = {@code "text/x-matrix"}</li>
         *   <li>{@link #pluginClassName} = {@code "org.geotools.image.io.text.TextMatrixImageWriter"}</li>
         *   <li>{@link #vendorName}      = {@code "Geotools"}</li>
         * </ul>
         *
         * For efficienty reasons, the above fields are initialized to shared arrays. Subclasses
         * can assign new arrays, but should not modify the default array content.
         */
        public Spi() {
            names           = TextMatrixImageReader.Spi.NAMES;
            MIMETypes       = TextMatrixImageReader.Spi.MIME_TYPES;
            pluginClassName = "org.geotools.image.io.text.TextMatrixImageWriter";
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
         * Returns an instance of the {@code ImageWriter} implementation associated
         * with this service provider.
         *
         * @param  extension An optional extension object, which may be null.
         * @return An image writer instance.
         * @throws IOException if the attempt to instantiate the writer fails.
         */
        public ImageWriter createWriterInstance(final Object extension) throws IOException {
            return new TextMatrixImageWriter(this);
        }
    }
}
