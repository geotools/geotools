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

import it.geosolutions.imageio.core.SourceSPIProvider;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.util.factory.Hints;

/**
 * An {@link ImageMosaicElementConsumer} which handles a provided {@link SimpleFeature} by leveraging on the {@link URL}
 * associated to the feature.
 */
public class ImageMosaicURLFeatureConsumer implements ImageMosaicElementConsumer<SimpleFeature> {

    /** The Underlying {@link ImageMosaicURLConsumer} instance, doing the actual handling */
    private ImageMosaicURLConsumer imageMosaicURLConsumer;

    public ImageMosaicURLFeatureConsumer(ImageMosaicURLConsumer imageMosaicURLConsumer) {
        this.imageMosaicURLConsumer = imageMosaicURLConsumer;
    }

    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicURLFeatureConsumer.class);

    @Override
    public boolean checkElement(SimpleFeature feature, ImageMosaicWalker walker) {
        // Current implementation Does nothing
        return true;
    }

    @Override
    public void handleElement(SimpleFeature feature, ImageMosaicWalker walker) throws IOException {
        ImageMosaicConfigHandler configHandler = walker.getConfigHandler();
        CatalogBuilderConfiguration runConfiguration = configHandler.getRunConfiguration();
        String locationAttrName = runConfiguration.getParameter(Utils.Prop.LOCATION_ATTRIBUTE);
        String location = (String) Utils.getAttribute(feature, locationAttrName);
        if (location == null) {
            throw new IllegalArgumentException("Could not find the location using attribute name: " + locationAttrName);
        }
        imageMosaicURLConsumer.handleElement(new URL(location), walker);
    }

    static class ImageMosaicURLConsumer implements ImageMosaicElementConsumer<URL> {

        private SourceSPIProviderFactory sourceSPIProvider;

        public ImageMosaicURLConsumer(SourceSPIProviderFactory sourceSPIProvider) {
            this.sourceSPIProvider = sourceSPIProvider;
        }

        @Override
        public boolean checkElement(URL url, ImageMosaicWalker walker) {
            // For the moment, we don't do any type of checks on URL
            return true;
        }

        @Override
        public void handleElement(URL url, ImageMosaicWalker walker) throws IOException {

            // increment counter
            int elementIndex = walker.getElementIndex() + 1;
            walker.setElementIndex(elementIndex);

            int numElements = walker.getNumElements();
            ImageMosaicEventHandlers eventHandler = walker.getEventHandler();
            ImageMosaicConfigHandler configHandler = walker.getConfigHandler();

            eventHandler.fireEvent(Level.INFO, "Now indexing url " + url, elementIndex * 100.0 / numElements);
            GridCoverage2DReader coverageReader = null;
            try {
                // STEP 1
                // Getting a coverage reader for this coverage.
                //
                final AbstractGridFormat format;
                final AbstractGridFormat cachedFormat = configHandler.getCachedFormat();

                SourceSPIProvider readerInputObject = sourceSPIProvider.getSourceSPIProvider(url);
                if (cachedFormat == null) {
                    // When looking for formats which may parse this file, make sure to exclude the
                    // ImageMosaicFormat as return
                    format = GridFormatFinder.findFormat(readerInputObject, Utils.EXCLUDE_MOSAIC_HINTS);
                } else {
                    if (cachedFormat.accepts(readerInputObject)) {
                        format = cachedFormat;
                    } else {
                        format = GridFormatFinder.findFormat(readerInputObject, Utils.EXCLUDE_MOSAIC_HINTS);
                    }
                }
                if (format instanceof UnknownFormat || format == null) {

                    eventHandler.fireUrlEvent(
                            Level.INFO,
                            url,
                            false,
                            "Skipped granule " + url + ": format is not supported.",
                            elementIndex * 99.0 / numElements);

                    return;
                }

                final Hints configurationHints =
                        configHandler.getRunConfiguration().getHints();
                coverageReader = format.getReader(readerInputObject, configurationHints);

                // Setting of the ReaderSPI to use
                if (configHandler.getCachedReaderSPI() == null) {
                    ImageInputStreamSpi inStreamSpi = readerInputObject.getStreamSpi();
                    // Ensure that the ImageInputStreamSPI is available
                    if (inStreamSpi == null) {
                        throw new IllegalArgumentException("no inputStreamSPI available!");
                    }
                    try (ImageInputStream inStream = readerInputObject.getStream()) {
                        // Get the ImageInputStream from the SPI

                        // Throws an Exception if the ImageInputStream is not present
                        if (inStream == null) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to open a stream on " + url);
                            }
                            throw new IllegalArgumentException(
                                    "Unable to get an input stream for the provided file granule" + url);
                        }
                        // Selection of the ImageReaderSpi from the Stream
                        ImageReaderSpi spi = readerInputObject.getReaderSpi();
                        // Setting of the ImageReaderSpi to the ImageMosaicConfigHandler in order to
                        // set
                        // it inside the indexer properties
                        configHandler.setCachedReaderSPI(spi);
                    }
                }

                // Getting available coverageNames from the reader
                String[] coverageNames = coverageReader.getGridCoverageNames();

                for (String cvName : coverageNames) {
                    try {

                        //  Assume that all the granules already put on the datastore are valid
                        configHandler.setCachedFormat(format);
                    } catch (Exception e) {
                        LOGGER.log(Level.FINE, "Failure during potential granule evaluation, skipping it: " + url, e);
                    }

                    ImageMosaicSourceElement element = new ImageMosaicSourceElement.URLElement(url);
                    configHandler.updateConfiguration(
                            coverageReader,
                            cvName,
                            new ImageMosaicSourceElement.URLElement(url),
                            elementIndex,
                            numElements,
                            walker.getTransaction());

                    // fire event
                    element.fireHarvestingEvent(eventHandler, elementIndex, numElements, "Done with granule " + url);
                }
            } catch (Exception e) {
                // we got an exception, we should stop the walk
                eventHandler.fireException(e);
                walker.stop();
            } finally {

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
