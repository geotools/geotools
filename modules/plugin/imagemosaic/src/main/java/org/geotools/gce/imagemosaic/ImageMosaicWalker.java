/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.data.DefaultTransaction;
import org.geotools.gce.imagemosaic.acceptors.GranuleAcceptor;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;

/**
 * This class is responsible for walking through the files inside a directory (and its children
 * directories) which respect a specified wildcard.
 *
 * <p>Its role is basically to simplify the construction of the mosaic by implementing a visitor
 * pattern for the files that we have to use for the index.
 *
 * <p>It is based on the Commons IO {@link DirectoryWalker} class.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Carlo Cancellieri, GeoSolutions SAS
 */
abstract class ImageMosaicWalker implements Runnable {

    /** Default Logger * */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ImageMosaicWalker.class);

    private List<GranuleAcceptor> granuleAcceptors;

    private DefaultTransaction transaction;

    private static Set<String> logExcludes = new HashSet<String>();

    static {
        logExcludes.add("xml");
        logExcludes.add("properties");
    }

    /**
     * Proper way to stop a thread is not by calling Thread.stop() but by using a shared variable
     * that can be checked in order to notify a terminating condition.
     */
    private volatile boolean stop = false;

    protected final ImageMosaicConfigHandler configHandler;

    protected final Hints excludeMosaicHints = new Hints(Utils.EXCLUDE_MOSAIC, true);

    private AbstractGridFormat cachedFormat;

    /** index of the file being processed */
    private int fileIndex = 0;

    /** Number of files to process. */
    private int numFiles = 1;

    protected final ImageMosaicEventHandlers eventHandler;

    /**
     * @param updateFeatures if true update catalog with loaded granules
     * @param imageMosaicConfigHandler configuration handler being used
     * @param granuleAcceptors list of acceptors to deterrmine granule inclusion
     */
    public ImageMosaicWalker(
            ImageMosaicConfigHandler configHandler, ImageMosaicEventHandlers eventHandler) {
        Utilities.ensureNonNull("config handler", configHandler);
        Utilities.ensureNonNull("event handler", eventHandler);
        this.configHandler = configHandler;
        this.eventHandler = eventHandler;
        this.granuleAcceptors = configHandler.getGranuleAcceptors();
    }

    public boolean getStop() {
        return stop;
    }

    public void stop() {
        stop = true;
    }

    protected boolean checkFile(final File fileBeingProcessed) {
        if (!fileBeingProcessed.exists()
                || !fileBeingProcessed.canRead()
                || !fileBeingProcessed.isFile()) {
            return false;
        }
        return true;
    }

    protected void handleFile(final File fileBeingProcessed) throws IOException {

        // increment counter
        fileIndex++;

        //
        // Check that this file is actually good to go
        //
        if (!checkFile(fileBeingProcessed)) return;

        // replacing chars on input path
        String validFileName;
        String extension;
        try {
            validFileName = fileBeingProcessed.getCanonicalPath();
            validFileName = FilenameUtils.normalize(validFileName);
            extension = FilenameUtils.getExtension(validFileName);
        } catch (IOException e) {
            eventHandler.fireFileEvent(
                    Level.FINER,
                    fileBeingProcessed,
                    false,
                    "Exception occurred while processing file "
                            + fileBeingProcessed
                            + ": "
                            + e.getMessage(),
                    ((fileIndex * 100.0) / numFiles));
            eventHandler.fireException(e);
            return;
        }
        validFileName = FilenameUtils.getName(validFileName);
        eventHandler.fireEvent(
                Level.INFO, "Now indexing file " + validFileName, ((fileIndex * 100.0) / numFiles));
        GridCoverage2DReader coverageReader = null;
        try {
            // STEP 1
            // Getting a coverage reader for this coverage.
            //
            final AbstractGridFormat format;
            if (cachedFormat == null) {
                // When looking for formats which may parse this file, make sure to exclude the
                // ImageMosaicFormat as return
                format = GridFormatFinder.findFormat(fileBeingProcessed, excludeMosaicHints);
            } else {
                if (cachedFormat.accepts(fileBeingProcessed)) {
                    format = cachedFormat;
                } else {
                    format = GridFormatFinder.findFormat(fileBeingProcessed, excludeMosaicHints);
                }
            }
            if ((format instanceof UnknownFormat) || format == null) {
                if (!logExcludes.contains(extension)) {
                    eventHandler.fireFileEvent(
                            Level.INFO,
                            fileBeingProcessed,
                            false,
                            "Skipped file "
                                    + fileBeingProcessed
                                    + ": File format is not supported.",
                            ((fileIndex * 99.0) / numFiles));
                }
                return;
            }

            final Hints configurationHints = configHandler.getRunConfiguration().getHints();
            coverageReader =
                    (GridCoverage2DReader) format.getReader(fileBeingProcessed, configurationHints);

            // Setting of the ReaderSPI to use
            if (configHandler.getCachedReaderSPI() == null) {
                // Get the URL associated to the file
                URL granuleUrl = URLs.fileToUrl(fileBeingProcessed);
                // Get the ImageInputStreamSPI associated to the URL
                ImageInputStreamSpi inStreamSpi = Utils.getInputStreamSPIFromURL(granuleUrl);
                // Ensure that the ImageInputStreamSPI is available
                if (inStreamSpi == null) {
                    throw new IllegalArgumentException("no inputStreamSPI available!");
                }
                ImageInputStream inStream = null;
                try {
                    // Get the ImageInputStream from the SPI
                    inStream =
                            inStreamSpi.createInputStreamInstance(
                                    granuleUrl, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
                    // Throws an Exception if the ImageInputStream is not present
                    if (inStream == null) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, Utils.getFileInfo(fileBeingProcessed));
                        }
                        throw new IllegalArgumentException(
                                "Unable to get an input stream for the provided file "
                                        + granuleUrl.toString());
                    }
                    // Selection of the ImageReaderSpi from the Stream
                    ImageReaderSpi spi = Utils.getReaderSpiFromStream(null, inStream);
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
                boolean shouldAccept = true;
                try {
                    for (GranuleAcceptor acceptor : this.configHandler.getGranuleAcceptors()) {
                        if (!acceptor.accepts(
                                coverageReader, cvName, fileBeingProcessed, configHandler)) {
                            shouldAccept = false;
                            eventHandler.fireFileEvent(
                                    Level.FINE,
                                    fileBeingProcessed,
                                    true,
                                    "Granule acceptor  "
                                            + acceptor.getClass().getName()
                                            + " rejected the granule being processed"
                                            + fileBeingProcessed,
                                    ((fileIndex + 1) * 99.0) / numFiles);
                            break;
                        }
                    }
                    // store the format only if we can accept this file, not before
                    cachedFormat = format;
                } catch (Exception e) {
                    LOGGER.log(
                            Level.FINE,
                            "Failure during potential granule evaluation, skipping it: "
                                    + fileBeingProcessed,
                            e);
                    shouldAccept = false;
                }

                if (shouldAccept) {
                    configHandler.updateConfiguration(
                            coverageReader,
                            cvName,
                            fileBeingProcessed,
                            fileIndex,
                            numFiles,
                            transaction);
                }

                // fire event
                eventHandler.fireFileEvent(
                        Level.FINE,
                        fileBeingProcessed,
                        true,
                        "Done with file " + fileBeingProcessed,
                        (((fileIndex + 1) * 99.0) / numFiles));
            }
        } catch (Exception e) {
            // we got an exception, we should stop the walk
            eventHandler.fireException(e);

            this.stop();
            return;
        } finally {
            //
            // STEP 5
            //
            // release resources
            //
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

    /** Create a transaction for being used in this walker */
    public void startTransaction() {
        if (transaction != null) {
            throw new IllegalStateException("Transaction already open!");
        }
        this.transaction = new DefaultTransaction("MosaicCreationTransaction" + System.nanoTime());
    }

    public void rollbackTransaction() throws IOException {
        transaction.rollback();
    }

    public void commitTransaction() throws IOException {
        transaction.commit();
    }

    public void closeTransaction() {
        transaction.close();
    }

    protected boolean checkStop() {
        if (getStop()) {
            eventHandler.fireEvent(
                    Level.INFO,
                    "Stopping requested at file  " + fileIndex + " of " + numFiles + " files",
                    ((fileIndex * 100.0) / numFiles));
            return false;
        }
        return true;
    }

    /** @return the fileIndex */
    public int getFileIndex() {
        return fileIndex;
    }

    /** @return the numFiles */
    public int getNumFiles() {
        return numFiles;
    }

    /** @param fileIndex the fileIndex to set */
    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    /** @param numFiles the numFiles to set */
    public void setNumFiles(int numFiles) {
        this.numFiles = numFiles;
    }

    /**
     * Warn this walker that we skip the provided path
     *
     * @param path the path to the file to skip
     */
    public void skipFile(String path) {
        LOGGER.log(Level.INFO, "Unable to use path: " + path + " - skipping it.");
        fileIndex++;
    }
}
