/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import it.geosolutions.imageio.maskband.DatasetLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider.SpiHelper;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;

/** Default implementaion of {@link GranuleAccessProvider} */
class DefaultGranuleAccessProvider implements GranuleAccessProvider, GranuleDescriptorModifier {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DefaultGranuleAccessProvider.class);

    protected ImageInputStreamSpi imageInputStreamSpi;
    protected AbstractGridFormat suggestedFormat;
    protected AbstractGridFormat format;
    protected ImageReaderSpi imageReaderSpi;
    protected AbstractGridCoverage2DReader gcReader;
    protected MaskOverviewProvider ovrProvider;
    protected SpiHelper spiHelper;
    protected Object input;
    protected URL inputURL;
    protected Hints hints;
    protected boolean skipExternalOverviews;

    public DefaultGranuleAccessProvider(Hints hints) {
        this.hints = hints;
        if (hints != null && !hints.isEmpty()) {
            if (hints.containsKey(SUGGESTED_FORMAT)) {
                this.suggestedFormat = (AbstractGridFormat) hints.get(SUGGESTED_FORMAT);
            }
            if (hints.containsKey(SUGGESTED_READER_SPI)) {
                this.imageReaderSpi = (ImageReaderSpi) hints.get(SUGGESTED_READER_SPI);
            }
            if (hints.containsKey(SUGGESTED_STREAM_SPI)) {
                this.imageInputStreamSpi = (ImageInputStreamSpi) hints.get(SUGGESTED_STREAM_SPI);
            }
            if (hints.containsKey(Hints.SKIP_EXTERNAL_OVERVIEWS)) {
                this.skipExternalOverviews = (Boolean) hints.get(Hints.SKIP_EXTERNAL_OVERVIEWS);
            }
        }
    }

    @Override
    public void setGranuleInput(Object input) throws IOException {
        if (input == null || !(input instanceof URL)) {
            throw new IllegalArgumentException(
                    "Only URL type is supported by this provider: " + input);
        }
        this.input = input;
        this.inputURL = (URL) input;
    }

    public URL getInputURL() {
        return this.inputURL;
    }

    @Override
    public AbstractGridFormat getFormat() throws IOException {
        if (format == null) {
            if (suggestedFormat != null && suggestedFormat.accepts(input, hints)) {
                format = suggestedFormat;
            } else {
                format = GridFormatFinder.findFormat(input, hints);
            }
        }
        if (format == null) {
            throw new IOException("Unable to find a format for the specified input: " + input);
        }
        return format;
    }

    @Override
    public MaskOverviewProvider getMaskOverviewsProvider() throws IOException {
        if (ovrProvider == null) {
            AbstractGridCoverage2DReader reader = getGridCoverageReader();
            DatasetLayout layout = reader.getDatasetLayout();
            spiHelper = new SpiHelper(inputURL, imageReaderSpi, imageInputStreamSpi);
            ovrProvider =
                    new MaskOverviewProvider(layout, inputURL, spiHelper, skipExternalOverviews);
        }
        if (ovrProvider == null) {
            throw new IOException(
                    "Unable to find a MaskOverviewProvider for the specified input: " + inputURL);
        }
        return ovrProvider;
    }

    @Override
    public AbstractGridCoverage2DReader getGridCoverageReader(/*Hints hints*/ ) throws IOException {
        if (gcReader == null) {
            gcReader = getFormat().getReader(input, hints);
        }
        if (gcReader == null) {
            throw new IOException("Unable to get a reader for the specified input: " + input);
        }
        return gcReader;
    }

    @Override
    public ImageInputStreamSpi getInputStreamSpi() throws IOException {
        return getMaskOverviewsProvider().getInputStreamSpi();
    }

    @Override
    public ImageReaderSpi getImageReaderSpi() throws IOException {
        return getMaskOverviewsProvider().getImageReaderSpi();
    }

    @Override
    public ImageInputStream getImageInputStream() throws IOException {
        ImageInputStreamSpi streamSpi = getInputStreamSpi();
        ImageInputStream inStream =
                streamSpi.createInputStreamInstance(
                        inputURL, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
        if (inStream == null) {
            final File file = URLs.urlToFile(inputURL);
            if (file != null) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, Utils.getFileInfo(file));
                }
            }
            throw new IllegalArgumentException(
                    "Unable to get an input stream for the provided file " + inputURL.toString());
        }
        return inStream;
    }

    @Override
    public ImageReader getImageReader() throws IOException {
        ImageReaderSpi imageReaderSpi = getImageReaderSpi();
        if (imageReaderSpi == null) {
            throw new IllegalArgumentException(
                    "No ReaderSPI has been found for input: " + inputURL.toString());
        }
        ImageReader imageReader = imageReaderSpi.createReaderInstance();
        if (imageReader == null)
            throw new IllegalArgumentException(
                    "Unable to get an ImageReader for the provided file " + inputURL.toString());

        return imageReader;
    }

    public static ImageReaderSpi createImageReaderSpiInstance(String spiClass) {
        ImageReaderSpi spi = null;
        if (spiClass != null) {
            try {
                final Class<?> clazz = Class.forName(spiClass);
                Object spiInstance = clazz.getDeclaredConstructor().newInstance();
                if (spiInstance instanceof ImageReaderSpi) spi = (ImageReaderSpi) spiInstance;
                else spi = null;
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                spi = null;
            }
        }
        return spi;
    }

    public static ImageInputStreamSpi createImageInputStreamSpiInstance(String spiClass) {
        ImageInputStreamSpi spi = null;
        if (spiClass != null) {
            try {
                final Class<?> clazz = Class.forName(spiClass);
                Object spiInstance = clazz.getDeclaredConstructor().newInstance();
                if (spiInstance instanceof ImageInputStreamSpi)
                    spi = (ImageInputStreamSpi) spiInstance;
                else spi = null;
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                spi = null;
            }
        }
        return spi;
    }

    public static AbstractGridFormat createFormatInstance(String formatClass) {
        AbstractGridFormat format = null;
        if (formatClass != null) {
            try {
                final Class<?> clazz = Class.forName(formatClass);
                Object formatInstance = clazz.getDeclaredConstructor().newInstance();
                if (formatInstance instanceof AbstractGridFormat)
                    format = (AbstractGridFormat) formatInstance;
                else format = null;
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                format = null;
            }
        }
        return format;
    }

    @Override
    public void update(GranuleDescriptor granuleDescriptor, Hints hints) {
        if (!spiHelper.isMultidim()) {
            granuleDescriptor.setGranuleEnvelope(gcReader.getOriginalEnvelope());
        }
    }

    @Override
    public GranuleAccessProvider copyProviders() {
        DefaultGranuleAccessProvider provider = new DefaultGranuleAccessProvider(hints);
        return provider;
    }
}
