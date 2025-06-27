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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.gce.imagemosaic.acceptors.GranuleAcceptor;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;

/**
 * An {@link ImageMosaicElementConsumer} which handles a provided {@link SimpleFeature} by leveraging on the
 * {@link File} associated to the feature.
 */
public class ImageMosaicFileFeatureConsumer implements ImageMosaicElementConsumer<SimpleFeature> {

    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicFileConsumer.class);

    /** The Underlying {@link ImageMosaicFileConsumer} instance, doing the actual handling */
    private ImageMosaicFileConsumer imageMosaicFileConsumer;

    public ImageMosaicFileFeatureConsumer() {
        this.imageMosaicFileConsumer = new ImageMosaicFileConsumer();
    }

    @Override
    public boolean checkElement(SimpleFeature feature, ImageMosaicWalker provider) {
        ImageMosaicConfigHandler configHandler = provider.getConfigHandler();
        CatalogBuilderConfiguration runConfiguration = configHandler.getRunConfiguration();
        String locationAttrName = runConfiguration.getParameter(Utils.Prop.LOCATION_ATTRIBUTE);
        Object locationAttrObj = Utils.getAttribute(feature, locationAttrName);
        File file = null;
        if (locationAttrObj instanceof String) {
            final String path = (String) locationAttrObj;
            boolean isAbsolute = Boolean.getBoolean(runConfiguration.getParameter(Utils.Prop.ABSOLUTE_PATH));

            file = isAbsolute
                    ? new File(path)
                    : new File(runConfiguration.getParameter(Utils.Prop.ROOT_MOSAIC_DIR), path);
        } else if (locationAttrObj instanceof File) {
            file = (File) locationAttrObj;
        }
        return imageMosaicFileConsumer.checkElement(file, provider);
    }

    @Override
    public void handleElement(SimpleFeature feature, ImageMosaicWalker provider) throws IOException {
        ImageMosaicConfigHandler configHandler = provider.getConfigHandler();
        CatalogBuilderConfiguration runConfiguration = configHandler.getRunConfiguration();
        String locationAttrName = runConfiguration.getParameter(Utils.Prop.LOCATION_ATTRIBUTE);
        Object locationAttrObj = Utils.getAttribute(feature, locationAttrName);
        File file = null;
        if (locationAttrObj instanceof String) {
            final String path = (String) locationAttrObj;
            if (Boolean.getBoolean(runConfiguration.getParameter(Utils.Prop.ABSOLUTE_PATH))) {
                // absolute files
                file = new File(path);
                // check this is _really_ absolute
                if (!imageMosaicFileConsumer.checkElement(file, provider)) {
                    file = null;
                }
            }
            if (file == null) {
                // relative files
                file = new File(runConfiguration.getParameter(Utils.Prop.ROOT_MOSAIC_DIR), path);
                // check this is _really_ relative
                if (!(file.exists() && file.canRead() && file.isFile())) {
                    // let's try for absolute, despite what the config says
                    // absolute files
                    file = new File(path);
                    // check this is _really_ absolute
                    if (!imageMosaicFileConsumer.checkElement(file, provider)) {
                        file = null;
                    }
                }
            }

            // final check
            if (file == null) {
                // SKIP and log
                // empty table?
                provider.skip(path);
                return;
            }

        } else if (locationAttrObj instanceof File) {
            file = (File) locationAttrObj;
        } else {
            provider.getEventHandler()
                    .fireException(new IOException(
                            "Location attribute type not recognized for column name: " + locationAttrName));
            provider.stop();
            return;
        }

        // process this file
        imageMosaicFileConsumer.handleElement(file, provider);
    }

    static class ImageMosaicFileConsumer implements ImageMosaicElementConsumer<File> {

        @Override
        public boolean checkElement(File file, ImageMosaicWalker provider) {
            if (file == null || !file.exists() || !file.canRead() || !file.isFile()) {
                return false;
            }
            return true;
        }

        @Override
        public void handleElement(File file, ImageMosaicWalker provider) throws IOException {

            // increment counter
            int elementIndex = provider.getElementIndex() + 1;
            provider.setElementIndex(elementIndex);

            int numElements = provider.getNumElements();
            ImageMosaicEventHandlers eventHandler = provider.getEventHandler();
            ImageMosaicConfigHandler configHandler = provider.getConfigHandler();

            // Check that this file is actually good to go
            if (!checkElement(file, provider)) return;

            String validFileName;
            String extension;
            try {
                validFileName = file.getCanonicalPath();
                validFileName = FilenameUtils.normalize(validFileName);
                extension = FilenameUtils.getExtension(validFileName);
            } catch (IOException e) {
                eventHandler.fireFileEvent(
                        Level.FINER,
                        file,
                        false,
                        "Exception occurred while processing file " + file + ": " + e.getMessage(),
                        elementIndex * 100.0 / numElements);
                eventHandler.fireException(e);
                return;
            }
            validFileName = FilenameUtils.getName(validFileName);
            eventHandler.fireEvent(
                    Level.INFO, "Now indexing file " + validFileName, elementIndex * 100.0 / numElements);
            GridCoverage2DReader coverageReader = null;
            try {
                // Getting a coverage reader for this coverage.
                final AbstractGridFormat format;
                final AbstractGridFormat cachedFormat = configHandler.getCachedFormat();
                if (cachedFormat == null) {
                    // When looking for formats which may parse this file, make sure to exclude the
                    // ImageMosaicFormat as return
                    format = GridFormatFinder.findFormat(file, Utils.EXCLUDE_MOSAIC_HINTS);
                } else {
                    if (cachedFormat.accepts(file)) {
                        format = cachedFormat;
                    } else {
                        format = GridFormatFinder.findFormat(file, Utils.EXCLUDE_MOSAIC_HINTS);
                    }
                }
                if (format instanceof UnknownFormat || format == null) {
                    if (!Utils.LOG_EXCLUDES.contains(extension)) {
                        eventHandler.fireFileEvent(
                                Level.INFO,
                                file,
                                false,
                                "Skipped file " + file + ": File format is not supported.",
                                elementIndex * 99.0 / numElements);
                    }
                    return;
                }

                final Hints configurationHints =
                        configHandler.getRunConfiguration().getHints();
                coverageReader = format.getReader(file, configurationHints);

                // Setting of the ReaderSPI to use
                if (configHandler.getCachedReaderSPI() == null) {
                    // Get the URL associated to the file
                    URL granuleUrl = URLs.fileToUrl(file);
                    // Get the ImageInputStreamSPI associated to the URL
                    ImageInputStreamSpi inStreamSpi = Utils.getInputStreamSPIFromURL(granuleUrl);
                    // Ensure that the ImageInputStreamSPI is available
                    if (inStreamSpi == null) {
                        throw new IllegalArgumentException("no inputStreamSPI available!");
                    }
                    try (ImageInputStream inStream = inStreamSpi.createInputStreamInstance(
                            granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory())) {
                        // Get the ImageInputStream from the SPI
                        // Throws an Exception if the ImageInputStream is not present
                        if (inStream == null) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, Utils.getFileInfo(file));
                            }
                            throw new IllegalArgumentException(
                                    "Unable to get an input stream for the provided file " + granuleUrl.toString());
                        }
                        // Selection of the ImageReaderSpi from the Stream
                        ImageReaderSpi spi = Utils.getReaderSpiFromStream(null, inStream);
                        configHandler.setCachedReaderSPI(spi);
                    }
                }

                // Getting available coverageNames from the reader
                String[] coverageNames = coverageReader.getGridCoverageNames();

                for (String cvName : coverageNames) {
                    ImageMosaicSourceElement element = new ImageMosaicSourceElement.FileElement(file);
                    boolean shouldAccept = true;
                    try {
                        for (GranuleAcceptor acceptor : configHandler.getGranuleAcceptors()) {
                            if (!acceptor.accepts(coverageReader, cvName, file, configHandler)) {
                                shouldAccept = false;
                                String message = "Granule acceptor  "
                                        + acceptor.getClass().getName()
                                        + " rejected the granule being processed"
                                        + file;
                                element.fireHarvestingEvent(eventHandler, elementIndex, numElements, message);
                                break;
                            }
                        }
                        // store the format only if we can accept this file, not before
                        configHandler.setCachedFormat(format);
                    } catch (Exception e) {
                        LOGGER.log(Level.FINE, "Failure during potential granule evaluation, skipping it: " + file, e);
                        shouldAccept = false;
                    }

                    if (shouldAccept) {
                        configHandler.updateConfiguration(
                                coverageReader, cvName, element, elementIndex, numElements, provider.getTransaction());
                    }

                    element.fireHarvestingEvent(eventHandler, elementIndex, numElements, "Done with file " + file);
                }
            } catch (Exception e) {
                // we got an exception, we should stop the walk
                eventHandler.fireException(e);

                provider.stop();
            } finally {
                // STEP 5
                // release resources
                try {
                    if (coverageReader != null)
                        // release resources
                        coverageReader.dispose();
                } catch (Throwable e) {
                    // ignore exception
                    if (LOGGER.isLoggable(Level.FINEST)) LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
                }
            }
        }
    }
}
