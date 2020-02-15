/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.geotools.gce.image.WorldImageFormat;
import org.geotools.gce.imagemosaic.Utils.Prop;

/**
 * This class is in responsible for creating the index for a mosaic of images that we want to tie
 * together as a single coverage.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @author Carlo Cancellieri - GeoSolutions SAS
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ImageMosaicDirectoryWalker extends ImageMosaicWalker {

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
     */
    final class MosaicDirectoryWalker extends DirectoryWalker {

        private ImageMosaicWalker walker;

        @Override
        protected void handleCancelled(
                File startDirectory, Collection results, CancelException cancel)
                throws IOException {
            super.handleCancelled(startDirectory, results, cancel);
            // clean up objects and rollback transaction
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Stop requested when walking directory " + startDirectory);
            }
            super.handleEnd(results);
        }

        @Override
        protected boolean handleIsCancelled(final File file, final int depth, Collection results)
                throws IOException {

            //
            // Anyone has asked us to stop?
            //
            if (!checkStop()) {
                return true;
            }
            return false;
        }

        @Override
        protected void handleFile(
                final File fileBeingProcessed, final int depth, final Collection results)
                throws IOException {

            walker.handleFile(fileBeingProcessed);

            super.handleFile(fileBeingProcessed, depth, results);
        }

        public MosaicDirectoryWalker(
                final List<String> indexingDirectories,
                final FileFilter filter,
                ImageMosaicWalker walker)
                throws IOException {
            super(
                    filter,
                    Integer.MAX_VALUE); // runConfiguration.isRecursive()?Integer.MAX_VALUE:0);

            this.walker = walker;
            startTransaction();
            configHandler.indexingPreamble();

            try {
                // start walking directories
                for (String indexingDirectory : indexingDirectories) {
                    walk(new File(indexingDirectory), null);

                    // did we cancel?
                    if (getStop()) {
                        break;
                    }
                }
                // did we cancel?
                if (getStop()) {
                    rollbackTransaction();
                } else {
                    commitTransaction();
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failure occurred while collecting the granules", e);
                rollbackTransaction();
            } finally {
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
                    closeTransaction();
                } catch (Exception e) {
                    final String message = "Unable to close indexing" + e.getLocalizedMessage();
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, message, e);
                    }
                    // notify listeners
                    eventHandler.fireException(e);
                }
            }
        }
    }

    private IOFileFilter fileFilter;

    /** run the directory walker */
    public void run() {

        try {

            //
            // creating the file filters for scanning for files to check and index
            //
            final IOFileFilter finalFilter = createDefaultGranuleExclusionFilter();

            // TODO we might want to remove this in the future for performance
            int numFiles = 0;
            String harvestDirectory =
                    configHandler.getRunConfiguration().getParameter(Prop.HARVEST_DIRECTORY);
            String indexDirs =
                    configHandler.getRunConfiguration().getParameter(Prop.INDEXING_DIRECTORIES);
            if (harvestDirectory != null) {
                indexDirs = harvestDirectory;
            }
            String[] indexDirectories = indexDirs.split("\\s*,\\s*");
            for (String indexingDirectory : indexDirectories) {
                indexingDirectory = Utils.checkDirectory(indexingDirectory, false);
                final File directoryToScan = new File(indexingDirectory);
                final Collection files =
                        FileUtils.listFiles(
                                directoryToScan,
                                finalFilter,
                                Boolean.parseBoolean(
                                                configHandler
                                                        .getRunConfiguration()
                                                        .getParameter(Prop.RECURSIVE))
                                        ? TrueFileFilter.INSTANCE
                                        : FalseFileFilter.INSTANCE);
                numFiles += files.size();
            }
            //
            // walk over the files that have filtered out
            //
            if (numFiles > 0) {
                setNumFiles(numFiles);
                final List<String> indexingDirectories =
                        new ArrayList<String>(Arrays.asList(indexDirectories));
                new MosaicDirectoryWalker(indexingDirectories, finalFilter, this);

            } else {
                LOGGER.log(Level.INFO, "No files to process!");
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /** @return */
    private IOFileFilter createDefaultGranuleExclusionFilter() {
        final IOFileFilter specialWildCardFileFilter =
                new WildcardFileFilter(
                        configHandler.getRunConfiguration().getParameter(Prop.WILDCARD),
                        IOCase.INSENSITIVE);
        IOFileFilter dirFilter =
                FileFilterUtils.and(
                        FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter filesFilter =
                Utils.excludeFilters(
                        FileFilterUtils.makeSVNAware(
                                FileFilterUtils.makeFileOnly(
                                        FileFilterUtils.and(
                                                specialWildCardFileFilter,
                                                HiddenFileFilter.VISIBLE))),
                        FileFilterUtils.suffixFileFilter("shp"),
                        FileFilterUtils.suffixFileFilter("dbf"),
                        FileFilterUtils.suffixFileFilter("sbn"),
                        FileFilterUtils.suffixFileFilter("sbx"),
                        FileFilterUtils.suffixFileFilter("shx"),
                        FileFilterUtils.suffixFileFilter("qix"),
                        FileFilterUtils.suffixFileFilter("lyr"),
                        FileFilterUtils.suffixFileFilter("prj"),
                        FileFilterUtils.suffixFileFilter("ncx2"),
                        FileFilterUtils.suffixFileFilter("ncx3"),
                        FileFilterUtils.suffixFileFilter("gbx9"),
                        FileFilterUtils.suffixFileFilter("ncx"),
                        FileFilterUtils.nameFileFilter("error.txt"),
                        FileFilterUtils.nameFileFilter("error.txt.lck"),
                        FileFilterUtils.suffixFileFilter("properties"),
                        FileFilterUtils.suffixFileFilter("svn-base"));
        filesFilter =
                FileFilterUtils.or(
                        filesFilter, FileFilterUtils.nameFileFilter("indexer.properties"));

        // exclude common extensions
        Set<String> extensions = WorldImageFormat.getWorldExtension("png");
        for (String ext : extensions) {
            filesFilter =
                    FileFilterUtils.and(
                            filesFilter,
                            FileFilterUtils.notFileFilter(
                                    FileFilterUtils.suffixFileFilter(ext.substring(1))));
        }
        extensions = WorldImageFormat.getWorldExtension("gif");
        for (String ext : extensions) {
            filesFilter =
                    FileFilterUtils.and(
                            filesFilter,
                            FileFilterUtils.notFileFilter(
                                    FileFilterUtils.suffixFileFilter(ext.substring(1))));
        }
        extensions = WorldImageFormat.getWorldExtension("jpg");
        for (String ext : extensions) {
            filesFilter =
                    FileFilterUtils.and(
                            filesFilter,
                            FileFilterUtils.notFileFilter(
                                    FileFilterUtils.suffixFileFilter(ext.substring(1))));
        }
        extensions = WorldImageFormat.getWorldExtension("tiff");
        for (String ext : extensions) {
            filesFilter =
                    FileFilterUtils.and(
                            filesFilter,
                            FileFilterUtils.notFileFilter(
                                    FileFilterUtils.suffixFileFilter(ext.substring(1))));
        }
        extensions = WorldImageFormat.getWorldExtension("bmp");
        for (String ext : extensions) {
            filesFilter =
                    FileFilterUtils.and(
                            filesFilter,
                            FileFilterUtils.notFileFilter(
                                    FileFilterUtils.suffixFileFilter(ext.substring(1))));
        }

        // sdw
        filesFilter =
                FileFilterUtils.and(
                        filesFilter,
                        FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("sdw")),
                        FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("aux")),
                        FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("wld")),
                        FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter("svn")));

        if (this.fileFilter != null) {
            filesFilter = FileFilterUtils.and(this.fileFilter, filesFilter);
        }

        final IOFileFilter finalFilter = FileFilterUtils.or(dirFilter, filesFilter);
        return finalFilter;
    }

    /**
     * Default constructor.
     *
     * <p>Sets a filter that can reduce the file the mosaic walker will take into consideration (in
     * a more flexible way than the wildcards)
     */
    public ImageMosaicDirectoryWalker(
            ImageMosaicConfigHandler configHandler,
            ImageMosaicEventHandlers eventHandler,
            IOFileFilter filter) {
        super(configHandler, eventHandler);

        this.fileFilter = filter;
    }

    /** */
    public ImageMosaicDirectoryWalker(
            ImageMosaicConfigHandler catalogHandler, ImageMosaicEventHandlers eventHandler) {
        this(catalogHandler, eventHandler, null);
    }
}
