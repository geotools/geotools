/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import it.geosolutions.imageioimpl.plugins.cog.CogImageInputStreamSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogSourceSPIProvider;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.CogConfiguration;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * This class is responsible for walking through the target schema and check all the located
 * granules.
 *
 * <p>Its role is basically to simplify the construction of the mosaic by implementing a visitor
 * pattern for the files that we have to use for the index.
 *
 * @author Carlo Cancellieri - GeoSolutions SAS @TODO check the schema structure
 */
class ImageMosaicDatastoreWalker extends ImageMosaicWalker {

    private static final ImageInputStreamSpi COG_IMAGE_INPUT_STREAM_SPI =
            new CogImageInputStreamSpi();

    private static final ImageReaderSpi COG_IMAGE_READER_SPI = new CogImageReaderSpi();

    /** Default Logger * */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ImageMosaicDatastoreWalker.class);

    public ImageMosaicDatastoreWalker(
            ImageMosaicConfigHandler configHandler, ImageMosaicEventHandlers eventHandler) {
        super(configHandler, eventHandler);
    }

    /** run the walker on the store */
    public void run() {

        SimpleFeatureIterator it = null;
        GranuleCatalog catalog = null;
        try {

            configHandler.indexingPreamble();
            startTransaction();

            // start looking into catalog
            catalog = configHandler.getCatalog();
            String locationAttrName =
                    configHandler.getRunConfiguration().getParameter(Prop.LOCATION_ATTRIBUTE);
            String requestedTypeName =
                    configHandler.getRunConfiguration().getParameter(Prop.TYPENAME);
            String location =
                    configHandler.getRunConfiguration().getParameter(Prop.LOCATION_ATTRIBUTE);
            boolean isCog = configHandler.isCog();
            for (String typeName : catalog.getTypeNames()) {
                if (requestedTypeName != null && !requestedTypeName.equals(typeName)) {
                    continue;
                }

                if (!Utils.isValidMosaicSchema(catalog.getType(typeName), location)) {
                    LOGGER.log(Level.FINE, "Skipping invalid mosaic index table " + typeName);
                    continue;
                }

                // how many rows for this feature type?
                final Query query = new Query(typeName);
                int numFiles = catalog.getGranulesCount(query);
                if (numFiles <= 0) {
                    // empty table?
                    LOGGER.log(Level.FINE, "No rows in the typeName: " + typeName);
                    continue;
                }
                setNumFiles(numFiles);

                // cool, now let's walk over the features
                final SimpleFeatureCollection coll = catalog.getGranules(query);

                SimpleFeatureType schema = coll.getSchema();
                if (schema.getDescriptor(locationAttrName) == null) {
                    LOGGER.fine(
                            "Skipping feature type "
                                    + typeName
                                    + " as the location attribute "
                                    + locationAttrName
                                    + " is not part of the schema");
                    continue;
                } else if (schema.getGeometryDescriptor() == null) {
                    LOGGER.fine(
                            "Skipping feature type "
                                    + typeName
                                    + " as it does not have a footprint column");
                    continue;
                }

                // create an iterator
                it = coll.features();
                // TODO setup index name

                while (it.hasNext()) {
                    // get next element
                    final SimpleFeature feature = it.next();

                    Object locationAttrObj = feature.getAttribute(locationAttrName);
                    File file = null;
                    if (isCog) {
                        // Do not perform validations check.
                        // That will be made on handleFile
                        handleCogGranule((String) locationAttrObj);
                        continue;
                    } else if (locationAttrObj instanceof String) {
                        final String path = (String) locationAttrObj;
                        if (Boolean.getBoolean(
                                configHandler
                                        .getRunConfiguration()
                                        .getParameter(Prop.ABSOLUTE_PATH))) {
                            // absolute files
                            file = new File(path);
                            // check this is _really_ absolute
                            if (!checkFile(file)) {
                                file = null;
                            }
                        }
                        if (file == null) {
                            // relative files
                            file =
                                    new File(
                                            configHandler
                                                    .getRunConfiguration()
                                                    .getParameter(Prop.ROOT_MOSAIC_DIR),
                                            path);
                            // check this is _really_ relative
                            if (!(file.exists() && file.canRead() && file.isFile())) {
                                // let's try for absolute, despite what the config says
                                // absolute files
                                file = new File(path);
                                // check this is _really_ absolute
                                if (!(checkFile(file))) {
                                    file = null;
                                }
                            }
                        }

                        // final check
                        if (file == null) {
                            // SKIP and log
                            // empty table?
                            super.skipFile(path);
                            continue;
                        }

                    } else if (locationAttrObj instanceof File) {
                        file = (File) locationAttrObj;
                    } else {
                        eventHandler.fireException(
                                new IOException(
                                        "Location attribute type not recognized for column name: "
                                                + locationAttrName));
                        stop();
                        break;
                    }

                    // process this file
                    handleFile(file);
                }
            } // next table

            // close transaction
            // did we cancel?
            if (getStop()) {
                rollbackTransaction();
            } else {
                commitTransaction();
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failure occurred while collecting the granules", e);
            try {
                rollbackTransaction();
            } catch (IOException e1) {
                throw new IllegalStateException(e1);
            }
        } finally {
            // close read iterator
            if (it != null) {
                try {
                    it.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }
            // close transaction
            try {
                closeTransaction();
            } catch (Exception e) {
                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
                // notify listeners
                eventHandler.fireException(e);
            }

            // close indexing
            try {
                configHandler.indexingPostamble(!getStop());
            } catch (Exception e) {
                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
                // notify listeners
                eventHandler.fireException(e);
            }

            try {
                if (catalog != null) {
                    catalog.dispose();
                }
            } catch (RuntimeException e) {
                String message = "Failed to dispose harvesting catalog";
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
            }
        }
    }

    protected void handleCogGranule(final String granuleBeingProcessed) throws IOException {

        // increment counter
        fileIndex++;

        // replacing chars on input path
        String validFileName;
        String extension;
        File fileRepresentation = new File(granuleBeingProcessed);
        validFileName = granuleBeingProcessed;
        extension = FilenameUtils.getExtension(validFileName);
        validFileName = FilenameUtils.getName(validFileName);
        eventHandler.fireEvent(
                Level.INFO, "Now indexing file " + validFileName, ((fileIndex * 100.0) / numFiles));
        GridCoverage2DReader coverageReader = null;
        try {
            // STEP 1
            // Getting a coverage reader for this coverage.
            //
            final AbstractGridFormat format;
            final AbstractGridFormat cachedFormat = configHandler.getCachedFormat();

            CogConfiguration cogBean =
                    new CogConfiguration(configHandler.getRunConfiguration().getIndexer());
            SourceSPIProvider readerInputObject =
                    new CogSourceSPIProvider(
                            cogBean.createUri(granuleBeingProcessed),
                            COG_IMAGE_READER_SPI,
                            COG_IMAGE_INPUT_STREAM_SPI,
                            cogBean.getRangeReader());

            if (cachedFormat == null) {
                // When looking for formats which may parse this file, make sure to exclude the
                // ImageMosaicFormat as return
                format = GridFormatFinder.findFormat(readerInputObject, excludeMosaicHints);
            } else {
                if (cachedFormat.accepts(readerInputObject)) {
                    format = cachedFormat;
                } else {
                    format = GridFormatFinder.findFormat(readerInputObject, excludeMosaicHints);
                }
            }
            if ((format instanceof UnknownFormat) || format == null) {
                if (!logExcludes.contains(extension)) {
                    eventHandler.fireFileEvent(
                            Level.INFO,
                            fileRepresentation,
                            false,
                            "Skipped granule "
                                    + granuleBeingProcessed
                                    + ": File format is not supported.",
                            ((fileIndex * 99.0) / numFiles));
                }
                return;
            }

            final Hints configurationHints = configHandler.getRunConfiguration().getHints();
            coverageReader =
                    (GridCoverage2DReader) format.getReader(readerInputObject, configurationHints);

            // Setting of the ReaderSPI to use
            if (configHandler.getCachedReaderSPI() == null) {
                ImageInputStreamSpi inStreamSpi = readerInputObject.getStreamSpi();
                // Ensure that the ImageInputStreamSPI is available
                if (inStreamSpi == null) {
                    throw new IllegalArgumentException("no inputStreamSPI available!");
                }
                ImageInputStream inStream = null;
                try {
                    // Get the ImageInputStream from the SPI

                    inStream = ((CogSourceSPIProvider) readerInputObject).getStream();
                    // Throws an Exception if the ImageInputStream is not present
                    if (inStream == null) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(
                                    Level.WARNING,
                                    "Unable to open a stream on " + granuleBeingProcessed);
                        }
                        throw new IllegalArgumentException(
                                "Unable to get an input stream for the provided file granule"
                                        + granuleBeingProcessed);
                    }
                    // Selection of the ImageReaderSpi from the Stream
                    ImageReaderSpi spi = readerInputObject.getReaderSpi();
                    // Setting of the ImageReaderSpi to the ImageMosaicConfigHandler in order to set
                    // it inside the indexer properties
                    configHandler.setCachedReaderSPI(spi);
                } finally {
                    if (inStream != null) {
                        inStream.close();
                    }
                }
            }

            // Getting available coverageNames from the reader
            String[] coverageNames = coverageReader.getGridCoverageNames();

            for (String cvName : coverageNames) {
                try {

                    //  Assume that all the granules already put on the datastore are valid
                    configHandler.setCachedFormat(format);
                } catch (Exception e) {
                    LOGGER.log(
                            Level.FINE,
                            "Failure during potential granule evaluation, skipping it: "
                                    + granuleBeingProcessed,
                            e);
                }

                configHandler.updateConfiguration(
                        coverageReader,
                        cvName,
                        fileRepresentation,
                        fileIndex,
                        numFiles,
                        transaction);

                // fire event
                eventHandler.fireFileEvent(
                        Level.FINE,
                        fileRepresentation,
                        true,
                        "Done with file " + granuleBeingProcessed,
                        (((fileIndex + 1) * 99.0) / numFiles));
            }
        } catch (Exception e) {
            // we got an exception, we should stop the walk
            eventHandler.fireException(e);

            this.stop();
            return;
        } finally {

            try {
                if (coverageReader != null)
                    // release resources
                    coverageReader.dispose();
            } catch (Throwable e) {
                // ignore exception
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(Level.FINEST, e.getLocalizedMessage(), e);
            }
        }
    }
}
