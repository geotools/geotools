package org.geotools.gce.imagemosaic;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.data.DefaultTransaction;
import org.geotools.factory.Hints;
import org.geotools.util.Utilities;

/**
 * This class is responsible for walking through the files inside a directory (and its children directories) which respect a specified wildcard.
 * 
 * <p>
 * Its role is basically to simplify the construction of the mosaic by implementing a visitor pattern for the files that we have to use for the index.
 * 
 * <p>
 * It is based on the Commons IO {@link DirectoryWalker} class.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Carlo Cancellieri, GeoSolutions SAS
 * 
 */
abstract class ImageMosaicWalker implements Runnable {

    /** Default Logger * */
    final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicWalker.class);

    private DefaultTransaction transaction;

    /**
     * Proper way to stop a thread is not by calling Thread.stop() but by using a shared variable that can be checked in order to notify a terminating
     * condition.
     */
    private volatile boolean stop = false;

    protected final ImageMosaicConfigHandler configHandler;

    protected final Hints excludeMosaicHints = new Hints(Utils.EXCLUDE_MOSAIC, true);

    private AbstractGridFormat cachedFormat;

    /**
     * index of the file being processed
     */    
    private int fileIndex = 0;

    /** Number of files to process. */
    private int numFiles = 1;

    protected final ImageMosaicEventHandlers eventHandler;

    /**
     * @param updateFeatures if true update catalog with loaded granules
     * @param imageMosaicConfigHandler TODO
     */
    public ImageMosaicWalker(ImageMosaicConfigHandler configHandler,
            ImageMosaicEventHandlers eventHandler) {
        Utilities.ensureNonNull("config handler", configHandler);
        Utilities.ensureNonNull("event handler", eventHandler);
        
        this.configHandler = configHandler;
        this.eventHandler = eventHandler;

    }

    public boolean getStop() {
        return stop;
    }

    public void stop() {
        stop = true;
    }

    protected boolean checkFile(final File fileBeingProcessed) {
        if (!fileBeingProcessed.exists() || !fileBeingProcessed.canRead()
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
        if (!checkFile(fileBeingProcessed))
            return;

        // replacing chars on input path
        String validFileName;
        try {
            validFileName = fileBeingProcessed.getCanonicalPath();
            validFileName = FilenameUtils.normalize(validFileName);
        } catch (IOException e) {
            eventHandler.fireFileEvent(
                    Level.FINER,
                    fileBeingProcessed,
                    false,
                    "Exception occurred while processing file " + fileBeingProcessed + ": "
                            + e.getMessage(), ((fileIndex * 100.0) / numFiles));
            eventHandler.fireException(e);
            return;
        }
        validFileName = FilenameUtils.getName(validFileName);
        eventHandler.fireEvent(Level.INFO, "Now indexing file " + validFileName,
                ((fileIndex * 100.0) / numFiles));
        GridCoverage2DReader coverageReader = null;
        try {
            // STEP 1
            // Getting a coverage reader for this coverage.
            //
            final AbstractGridFormat format;

            if (cachedFormat == null) {
                // When looking for formats which may parse this file, make sure to exclude the ImageMosaicFormat as return
                format = (AbstractGridFormat) GridFormatFinder.findFormat(fileBeingProcessed,
                        excludeMosaicHints);
            } else {
                if (cachedFormat.accepts(fileBeingProcessed)) {
                    format = cachedFormat;
                } else {
                    format = new UnknownFormat();
                }
            }
            if ((format instanceof UnknownFormat) || format == null) {
                eventHandler.fireFileEvent(Level.INFO, fileBeingProcessed, false, "Skipped file "
                        + fileBeingProcessed + ": File format is not supported.",
                        ((fileIndex * 99.0) / numFiles));
                return;
            }
            cachedFormat = format;

            final Hints configurationHints = configHandler.getRunConfiguration().getHints();
            coverageReader = (GridCoverage2DReader) format.getReader(fileBeingProcessed,
                    configurationHints);

            // Getting available coverageNames from the reader
            String[] coverageNames = coverageReader.getGridCoverageNames();

            for (String cvName : coverageNames) {

                configHandler.updateConfiguration(coverageReader, cvName, fileBeingProcessed,
                        fileIndex, numFiles, transaction);

                // fire event
                eventHandler.fireFileEvent(Level.FINE, fileBeingProcessed, true, "Done with file "
                        + fileBeingProcessed, (((fileIndex + 1) * 99.0) / numFiles));

            }
        } catch (Exception e) {
            eventHandler.fireException(e);
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
    
    /**
     * Create a transaction for being used in this walker
     */
    public void startTransaction(){
        if(transaction!=null){
            throw new IllegalStateException("Transaction already open!");
        }
        this.transaction = new DefaultTransaction("MosaicCreationTransaction"+ System.nanoTime());
    }
    
    public void rollbackTransaction() throws IOException{
        transaction.rollback();
    }
    
    public void commitTransaction() throws IOException{
        transaction.commit();
    }
    
    public void closeTransaction(){
        transaction.close();
    }

    protected boolean checkStop() {
        if (getStop()) {
            eventHandler.fireEvent(Level.INFO, "Stopping requested at file  " + fileIndex
                    + " of " + numFiles + " files", ((fileIndex * 100.0) / numFiles));
            return false;
        }
        return true;
    }

    /**
     * @return the fileIndex
     */
    public int getFileIndex() {
        return fileIndex;
    }

    /**
     * @return the numFiles
     */
    public int getNumFiles() {
        return numFiles;
    }

    /**
     * @param fileIndex the fileIndex to set
     */
    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    /**
     * @param numFiles the numFiles to set
     */
    public void setNumFiles(int numFiles) {
        this.numFiles = numFiles;
    }

    /**
     * Warn this walker that we skip the provided path 
     * @param path the path to the file to skip
     * 
     */
    public void skipFile(String path) {
        LOGGER.log(Level.INFO,"Unable to use path: "+path+" - skipping it.");
        fileIndex++;
        
    }

}