/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.utils.imagemosaic;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.console.CommandLine;
import org.geotools.console.Option;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder.ExceptionEvent;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder.ProcessingEvent;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.utils.progress.ProcessingEventListener;

/**
 * This class is in responsible for creating the index for a mosaic of images
 * that we want to tie together as a single coverage.
 * 
 * <p>
 * To get instructions on how to run the tool just run it without any argument
 * and a nice and clean help message will be printed to the command line.
 * 
 * <p>
 * Anyway an example of a suitable list of argumentBuilder can be seen here
 * below:
 * 
 * <p>
 * -s H:\\work\\data\\merano_aime -w *.tif -name merano -abs
 * <p>
 * where:
 * <ol>
 * <li>-s H:\\work\\data\\merano_aime is the source directory</li>
 * <li>-w *.tif is he wildcard for the files to process</li>
 * <li>-name merano sets the name for the output shape</li>
 * <li>-abs asks the tool to use absolute paths instead of relative</li>
 * </ol>
 * 
 * 
 * <p>
 * It is worth to point out that this tool comes as a command line tool but it
 * has been built with GUI in mind . It has the capability to register
 * {@link ProcessingEventListener} object that receive notifications about what
 * is going on. Moreover it delegates all the computations to an external
 * thread, hence we can stop the tool in the middle of processing with no so
 * many concerns (hopefully :-) ).
 * <p>
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 * @author Blaz Repnik
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/coveragetools/src/main/java/org/geotools/utils/imagemosaic/CommandLineCatalogBuilderRunner.java $
 * @version 0.3
 * 
 */
public class CommandLineCatalogBuilderRunner extends CommandLine {
    /**
     * Logger.
     */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CommandLineCatalogBuilderRunner.class.toString());
    
    private final static String INDEXER_PROPERTIES = "indexer.properties";    
    static class Prop {
        final static String LOCATION_ATTRIBUTE = "LocationAttribute";
        final static String ENVELOPE2D = "Envelope2D";
        final static String LEVELS_NUM = "LevelsNum";
        final static String LEVELS = "Levels";
        final static String SUGGESTED_SPI = "SuggestedSPI";
        final static String EXP_RGB = "ExpandToRGB";
        final static String ABSOLUTE_PATH = "AbsolutePath";
        final static String NAME = "Name";
        final static String FOOTPRINT_MANAGEMENT = "FootprintManagement";
        final static String HETEROGENEOUS = "Heterogeneous";
        static final String TIME_ATTRIBUTE = "TimeAttribute";
        static final String ELEVATION_ATTRIBUTE = "ElevationAttribute";
        final static String CACHING= "Caching";
        
        //Indexer Properties
        static final String ABSOLUTE = "Absolute";
        static final String RECURSIVE = "Recursive";
        static final String WILDCARD = "Wildcard";
        static final String SCHEMA = "Schema";
        static final String RESOLUTION_LEVELS = "ResolutionLevels";
        static final String PROPERTY_COLLECTORS = "PropertyCollectors";
    }
    
    @Option(description = "This index must use absolute or relative path", mandatory = false, name = "absolute")
    private Boolean absolute;

    @Option(description = "This index can use caching or not", mandatory = false, name = "caching")
    private Boolean caching = Utils.DEFAULT_CONFIGURATION_CACHING;

    @Option(description = "Directories where to look for file to index", mandatory = true, name = "indexingDirectories")
    private String indexingDirectoriesString;

    @Option(description = "This index must handle footprint", mandatory = false, name = "footprintManagement")
    private Boolean footprintManagement;

    /**
     * Index file name. Default is index.
     */
    @Option(description = "Name to use for the index of this mosaic", mandatory = false, name = "index")
    private String indexName;

    @Option(description = "Root directory where to place the index file", mandatory = true, name = "rootDirectory")
    private String rootMosaicDirectory;

    @Option(description = "Wildcard to use for building the index of this mosaic", mandatory = false, name = "wildcard")
    private String wildcardString = Utils.DEFAULT_WILCARD;

    @Option(description = "Default location attribute for this index", mandatory = false, name = "locationAttribute")
    private String locationAttribute = Utils.DEFAULT_LOCATION_ATTRIBUTE;

    public CommandLineCatalogBuilderRunner(String[] args) {
        super(args);
        if (this.absolute == null) {
            this.absolute = Utils.DEFAULT_PATH_BEHAVIOR;
        }
        if (this.caching == null) {
            this.caching = Utils.DEFAULT_CONFIGURATION_CACHING;
        }
        if (this.footprintManagement == null) {
            this.footprintManagement = Utils.DEFAULT_FOOTPRINT_MANAGEMENT;
        }
        if (this.indexName == null)
            this.indexName = Utils.DEFAULT_INDEX_NAME;
    }

    public static void main(String args[]) {
        
//            // rebuild formats at each parse, date formats are not thread safe
//            SimpleDateFormat format = new SimpleDateFormat("yyyy");
//            format.setTimeZone(TimeZone.getTimeZone("GMT"));
//            
//            /* We do not use the standard method DateFormat.parse(String), because if the parsing
//             * stops before the end of the string, the remaining characters are just ignored and
//             * no exception is thrown. So we have to ensure that the whole string is correct for
//             * the format.
//             */
//            ParsePosition pos = new ParsePosition(0);
//            Date time = format.parse("1963Z", pos);
//
//            System.out.println(time.toString());
//            System.out.println(time.toGMTString());
        
        final CommandLineCatalogBuilderRunner runner = new CommandLineCatalogBuilderRunner(args);
        // prepare the configuration
        final CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
        configuration.setAbsolute(runner.absolute);
        configuration.setIndexName(runner.indexName);
        configuration.setFootprintManagement(runner.footprintManagement);
        configuration.setCaching(runner.caching);
        configuration.setRootMosaicDirectory(runner.rootMosaicDirectory);
        configuration.setWildcard(runner.wildcardString);
        configuration.setLocationAttribute(runner.locationAttribute);

        final String directories = runner.indexingDirectoriesString;
        final String[] dirs_ = directories.split(",");
        final List<String> dirs = new ArrayList<String>();
        for (String dir : dirs_)
            dirs.add(dir);
        configuration.setIndexingDirectories(dirs);
        
        // prepare and run the index builder
        final CatalogBuilder builder = new CatalogBuilder(configuration);
     // this is going to help us with catching exceptions and logging them
        final Queue<Throwable> exceptions = new LinkedList<Throwable>();
        try {

                final CatalogBuilder.ProcessingEventListener listener = new CatalogBuilder.ProcessingEventListener() {

                        @Override
                        public void exceptionOccurred(ExceptionEvent event) {
                                final Throwable t = event.getException();
                                exceptions.add(t);
                                if (LOGGER.isLoggable(Level.SEVERE))
                                        LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);

                        }

                        @Override
                        public void getNotification(ProcessingEvent event) {
                                if (LOGGER.isLoggable(Level.FINE))
                                        LOGGER.fine(event.getMessage());

                        }

                };
                builder.addProcessingEventListener(listener);
                builder.run();
        } catch (Throwable e) {
                LOGGER.log(Level.SEVERE, "Unable to build mosaic", e);
        } finally {
            builder.dispose();
        }        

    }

}
