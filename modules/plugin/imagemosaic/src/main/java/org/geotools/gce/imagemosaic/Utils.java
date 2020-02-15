/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Metadata;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Metadata.MDI;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.BorderExtender;
import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ROI;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import javax.media.jai.remote.SerializableRenderedImage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.ObjectFactory;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.gce.imagemosaic.granulecollector.ReprojectingSubmosaicProducerFactory;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.io.ImageIOExt;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.util.Converters;
import org.geotools.util.Range;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.TopologyException;
import org.locationtech.jts.operation.overlay.snap.GeometrySnapper;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Sparse utilities for the various mosaic classes. I use them to extract complex code from other
 * places.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 */
public class Utils {

    public static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    private static final String DATABASE_KEY = "database";

    private static final String MVCC_KEY = "MVCC";

    private static final double RESOLUTION_TOLERANCE_FACTOR = 1E-2;

    public static final Key EXCLUDE_MOSAIC = new Key(Boolean.class);

    /** Hints to use for avoiding to search for the imagemosaic format */
    public static final Hints EXCLUDE_MOSAIC_HINTS = new Hints(Utils.EXCLUDE_MOSAIC, true);

    public static final Key CHECK_AUXILIARY_METADATA = new Key(Boolean.class);

    public static final Key AUXILIARY_FILES_PATH = new Key(String.class);

    public static final Key AUXILIARY_DATASTORE_PATH = new Key(String.class);

    public static final Key PARENT_DIR = new Key(String.class);

    public static final Key MOSAIC_READER = new Key(ImageMosaicReader.class);

    public static final String RANGE_SPLITTER_CHAR = ";";

    private static JAXBContext CONTEXT = null;

    public static final String PAM_DATASET = "PamDataset";

    static final String DEFAULT = "default";

    public static final String PROPERTIES_SEPARATOR = ";";
    /** EHCache instance to cache histograms */
    private static Cache ehcache;

    /** RGB to GRAY coefficients (for Luminance computation) */
    public static final double RGB_TO_GRAY_MATRIX[][] = {{0.114, 0.587, 0.299, 0}};

    /**
     * Flag indicating whether to compute optimized crop ops (instead of standard mosaicking op)
     * when possible (As an instance when mosaicking a single granule)
     */
    static final boolean OPTIMIZE_CROP;

    static final double DEFAULT_LINESTRING_DECIMATION_SPAN = 0.8;

    static final int DEFAULT_COORDS_DECIMATION_THRESHOLD = 200;

    private static final int COORDS_DECIMATION_THRESHOLD;

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(Utils.class);

    static {
        final String prop = System.getProperty("org.geotools.imagemosaic.optimizecrop");
        if (prop != null && prop.equalsIgnoreCase("FALSE")) {
            OPTIMIZE_CROP = false;
        } else {
            OPTIMIZE_CROP = true;
        }

        try {
            CONTEXT = JAXBContext.newInstance("org.geotools.gce.imagemosaic.catalog.index");
        } catch (JAXBException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
        COORDS_DECIMATION_THRESHOLD =
                Integer.getInteger(
                        "org.geotools.gce.imagemosaic.decimationthreshold",
                        DEFAULT_COORDS_DECIMATION_THRESHOLD);
        CLEANUP_FILTER = initCleanUpFilter();
        MOSAIC_SUPPORT_FILES_FILTER = initMosaicSupportFilesFilter();
    }

    public static class Prop {
        public static final String LOCATION_ATTRIBUTE = "LocationAttribute";

        public static final String ENVELOPE2D = "Envelope2D";

        public static final String LEVELS_NUM = "LevelsNum";

        public static final String LEVELS = "Levels";

        public static final String SUGGESTED_SPI = "SuggestedSPI";

        public static final String EXP_RGB = "ExpandToRGB";

        public static final String ABSOLUTE_PATH = "AbsolutePath";

        public static final String AUXILIARY_FILE = "AuxiliaryFile";

        public static final String AUXILIARY_DATASTORE_FILE = "AuxiliaryDatastoreFile";

        public static final String NAME = "Name";

        public static final String INDEX_NAME = "Name";

        public static final String INPUT_COVERAGE_NAME = "InputCoverageName";

        public static final String FOOTPRINT_MANAGEMENT = "FootprintManagement";

        public static final String HETEROGENEOUS = "Heterogeneous";

        public static final String TIME_ATTRIBUTE = "TimeAttribute";

        public static final String ELEVATION_ATTRIBUTE = "ElevationAttribute";

        public static final String ADDITIONAL_DOMAIN_ATTRIBUTES = "AdditionalDomainAttributes";

        public static final String CRS_ATTRIBUTE = "CrsAttribute";

        public static final String RESOLUTION_ATTRIBUTE = "ResolutionAttribute";

        public static final String RESOLUTION_X_ATTRIBUTE = "ResolutionXAttribute";

        public static final String RESOLUTION_Y_ATTRIBUTE = "ResolutionYAttribute";

        public static final String SUGGESTED_IS_SPI = "SuggestedIsSPI";

        public static final String SUGGESTED_FORMAT = "SuggestedFormat";

        /**
         * Sets if the target schema should be used to locate granules (default is FALSE)<br>
         * {@value TRUE|FALSE}
         */
        public static final String USE_EXISTING_SCHEMA = "UseExistingSchema";

        public static final String TYPENAME = "TypeName";

        public static final String STORE_NAME = "StoreName";

        public static final String PATH_TYPE = "PathType";

        public static final String PARENT_LOCATION = "ParentLocation";

        public static final String ROOT_MOSAIC_DIR = "RootMosaicDirectory";

        public static final String INDEXING_DIRECTORIES = "IndexingDirectories";

        public static final String HARVEST_DIRECTORY = "HarvestingDirectory";

        public static final String CAN_BE_EMPTY = "CanBeEmpty";

        /** Sets if the reader should look for auxiliary metadata PAM files */
        public static final String CHECK_AUXILIARY_METADATA = "CheckAuxiliaryMetadata";

        // Indexer Properties specific properties
        public static final String RECURSIVE = "Recursive";

        public static final String WILDCARD = "Wildcard";

        public static final String SCHEMA = "Schema";

        public static final String RESOLUTION_LEVELS = "ResolutionLevels";

        public static final String PROPERTY_COLLECTORS = "PropertyCollectors";

        public static final String CACHING = "Caching";

        public static final String WRAP_STORE = "WrapStore";

        public static final String GRANULE_ACCEPTORS = "GranuleAcceptors";

        public static final String GEOMETRY_HANDLER = "GranuleHandler";

        public static final String COVERAGE_NAME_COLLECTOR_SPI = "CoverageNameCollectorSPI";

        public static final String MOSAIC_CRS = "MosaicCRS";

        public static final String HETEROGENEOUS_CRS = "HeterogeneousCRS";

        public static final String GRANULE_COLLECTOR_FACTORY = "GranuleCollectorFactory";

        /**
         * The NoData value used in case no granule is found, but the request falls inside the image
         * mosaic bounds
         */
        public static final String NO_DATA = "NoData";
    }

    /**
     * Extracts a bbox from a filter in case there is at least one.
     *
     * <p>I am simply looking for the BBOX filter but I am sure we could use other filters as well.
     * I will leave this as a todo for the moment.
     *
     * @author Simone Giannecchini, GeoSolutions SAS.
     * @todo TODO use other spatial filters as well
     */
    public static class BBOXFilterExtractor extends DefaultFilterVisitor {

        public ReferencedEnvelope getBBox() {
            return bbox;
        }

        private ReferencedEnvelope bbox;

        @Override
        public Object visit(BBOX filter, Object data) {
            final ReferencedEnvelope bbox = ReferencedEnvelope.reference(filter.getBounds());
            if (this.bbox != null) {
                this.bbox = (ReferencedEnvelope) this.bbox.intersection(bbox);
            } else {
                this.bbox = bbox;
            }
            return super.visit(filter, data);
        }
    }

    /**
     * Given a source object, allow to retrieve (when possible) the related url, the related file or
     * the original input source object itself.
     */
    public static class SourceGetter {
        private File file;
        private URL url;
        private Object source;

        public SourceGetter(Object inputSource) {
            source = inputSource;
            // if it is a URL or a String let's try to see if we can get a file to
            // check if we have to build the index
            if (source instanceof File) {
                file = (File) source;
                url = URLs.fileToUrl(file);
            } else if (source instanceof URL) {
                url = (URL) source;
                if (url.getProtocol().equals("file")) {
                    file = URLs.urlToFile(url);
                }
            } else if (source instanceof String) {
                // is it a File?
                final String tempSource = (String) source;
                File tempFile = new File(tempSource);
                if (!tempFile.exists()) {
                    // is it a URL
                    try {
                        url = new URL(tempSource);
                        file = URLs.urlToFile(url);
                        source = file;
                    } catch (MalformedURLException e) {
                        url = null;
                        source = null;
                    }
                } else {
                    url = URLs.fileToUrl(tempFile);

                    // so that we can do our magic here below
                    file = tempFile;
                }
            }
        }

        /** Return the File (if any) of the source object */
        public File getFile() {
            return file;
        }

        /** Return the URL (if any) of the source object */
        public URL getUrl() {
            return url;
        }

        /** Return the original source object */
        public Object getSource() {
            return source;
        }
    }

    /** Default wildcard for creating mosaics. */
    public static final String DEFAULT_WILCARD = "*.*";

    /** Default path behavior with respect to absolute paths. */
    public static final boolean DEFAULT_PATH_BEHAVIOR = false;

    /** Default behavior with respect to index caching. */
    private static final boolean DEFAULT_CACHING_BEHAVIOR = false;

    /**
     * Creates a mosaic for the provided input parameters.
     *
     * @param location path to the directory where to gather the elements for the mosaic.
     * @param indexName name to give to this mosaic
     * @param wildcard wildcard to use for walking through files. We are using commonsIO for this
     *     task
     * @param absolutePath tells the catalogue builder to use absolute paths.
     * @param hints hints to control reader instantiations
     * @return <code>true</code> if everything is right, <code>false</code>if something bad happens,
     *     in which case the reason should be logged to the logger.
     */
    static boolean createMosaic(
            final String location,
            final String indexName,
            final String wildcard,
            final boolean absolutePath,
            final Hints hints) {

        // create a mosaic index builder and set the relevant elements
        final CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
        // check if the indexer.properties is there
        configuration.setHints(
                hints); // retain hints as this may contain an instance of an ImageMosaicReader
        List<Parameter> parameterList = configuration.getIndexer().getParameters().getParameter();

        IndexerUtils.setParam(parameterList, Prop.ABSOLUTE_PATH, Boolean.toString(absolutePath));
        IndexerUtils.setParam(parameterList, Prop.ROOT_MOSAIC_DIR, location);
        IndexerUtils.setParam(parameterList, Prop.INDEX_NAME, indexName);
        IndexerUtils.setParam(parameterList, Prop.WILDCARD, wildcard);
        IndexerUtils.setParam(parameterList, Prop.INDEXING_DIRECTORIES, location);

        // create the builder
        // final ImageMosaicWalker catalogBuilder = new ImageMosaicWalker(configuration);
        final ImageMosaicEventHandlers eventHandler = new ImageMosaicEventHandlers();
        final ImageMosaicConfigHandler catalogHandler =
                new ImageMosaicConfigHandler(configuration, eventHandler);
        final ImageMosaicWalker walker;
        if (catalogHandler.isUseExistingSchema()) {
            // walks existing granules in the origin store
            walker = new ImageMosaicDatastoreWalker(catalogHandler, eventHandler);
        } else {
            // collects granules from the file system
            walker = new ImageMosaicDirectoryWalker(catalogHandler, eventHandler);
        }

        // this is going to help us with catching exceptions and logging them
        final Queue<Throwable> exceptions = new LinkedList<Throwable>();
        try {

            final ImageMosaicEventHandlers.ProcessingEventListener listener =
                    new ImageMosaicEventHandlers.ProcessingEventListener() {

                        @Override
                        public void exceptionOccurred(
                                ImageMosaicEventHandlers.ExceptionEvent event) {
                            final Throwable t = event.getException();
                            exceptions.add(t);
                            if (LOGGER.isLoggable(Level.SEVERE)) {
                                LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);
                            }
                        }

                        @Override
                        public void getNotification(
                                ImageMosaicEventHandlers.ProcessingEvent event) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.fine(event.getMessage());
                            }
                        }
                    };
            eventHandler.addProcessingEventListener(listener);
            walker.run();
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Unable to build mosaic", e);
            return false;
        } finally {
            catalogHandler.dispose();
        }

        // check that nothing bad happened
        if (exceptions.size() > 0) {
            return false;
        }
        return true;
    }

    // Make additional filters pluggable
    private static IOFileFilter initCleanUpFilter() {
        IOFileFilter filesFilter =
                FileFilterUtils.or(
                        FileFilterUtils.suffixFileFilter("properties"),
                        FileFilterUtils.suffixFileFilter("shp"),
                        FileFilterUtils.suffixFileFilter("dbf"),
                        FileFilterUtils.suffixFileFilter("sbn"),
                        FileFilterUtils.suffixFileFilter("sbx"),
                        FileFilterUtils.suffixFileFilter("shx"),
                        FileFilterUtils.suffixFileFilter("qix"),
                        FileFilterUtils.suffixFileFilter("lyr"),
                        FileFilterUtils.suffixFileFilter("prj"),
                        FileFilterUtils.suffixFileFilter("ncx"),
                        FileFilterUtils.suffixFileFilter("gbx9"),
                        FileFilterUtils.suffixFileFilter("ncx2"),
                        FileFilterUtils.suffixFileFilter("ncx3"),
                        FileFilterUtils.nameFileFilter("error.txt"),
                        FileFilterUtils.nameFileFilter("_metadata"),
                        FileFilterUtils.suffixFileFilter(Utils.SAMPLE_IMAGE_NAME),
                        FileFilterUtils.suffixFileFilter(Utils.SAMPLE_IMAGE_NAME_LEGACY),
                        FileFilterUtils.nameFileFilter("error.txt.lck"),
                        FileFilterUtils.suffixFileFilter("xml"),
                        FileFilterUtils.suffixFileFilter("db"));
        return filesFilter;
    }

    private static IOFileFilter initMosaicSupportFilesFilter() {
        IOFileFilter filesFilter =
                FileFilterUtils.or(
                        FileFilterUtils.suffixFileFilter("properties"),
                        FileFilterUtils.suffixFileFilter("shp"),
                        FileFilterUtils.suffixFileFilter("dbf"),
                        FileFilterUtils.suffixFileFilter("sbn"),
                        FileFilterUtils.suffixFileFilter("sbx"),
                        FileFilterUtils.suffixFileFilter("shx"),
                        FileFilterUtils.suffixFileFilter("qix"),
                        FileFilterUtils.suffixFileFilter("lyr"),
                        FileFilterUtils.suffixFileFilter("prj"),
                        FileFilterUtils.suffixFileFilter(Utils.SAMPLE_IMAGE_NAME),
                        FileFilterUtils.suffixFileFilter(Utils.SAMPLE_IMAGE_NAME_LEGACY),
                        FileFilterUtils.suffixFileFilter("db"));
        return filesFilter;
    }

    public static String getMessageFromException(Exception exception) {
        if (exception.getLocalizedMessage() != null) return exception.getLocalizedMessage();
        else return exception.getMessage();
    }

    static MosaicConfigurationBean loadMosaicProperties(final URL sourceURL) {
        return loadMosaicProperties(sourceURL, null);
    }

    private static MosaicConfigurationBean loadMosaicProperties(
            final URL sourceURL, final Set<String> ignorePropertiesSet) {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Trying to load properties file from URL:" + sourceURL);
        }

        // ret value
        final MosaicConfigurationBean retValue = new MosaicConfigurationBean();
        final CatalogConfigurationBean catalogConfigurationBean = new CatalogConfigurationBean();
        retValue.setCatalogConfigurationBean(catalogConfigurationBean);
        final boolean ignoreSome = ignorePropertiesSet != null && !ignorePropertiesSet.isEmpty();

        //
        // load the properties file
        //
        URL propsURL = sourceURL;
        if (!sourceURL.toExternalForm().endsWith(".properties")) {
            propsURL = URLs.changeUrlExt(sourceURL, "properties");
            if (propsURL.getProtocol().equals("file")) {
                final File sourceFile = URLs.urlToFile(propsURL);
                if (!sourceFile.exists()) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("properties file doesn't exist");
                    }
                    return null;
                }
            }
        }

        final Properties properties = CoverageUtilities.loadPropertiesFromURL(propsURL);
        if (properties == null) {
            if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("Unable to load mosaic properties file");
            return null;
        }

        String[] pairs = null;
        String pair[] = null;

        //
        // imposed bbox is optional
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ENVELOPE2D)) {
            String bboxString = properties.getProperty(Prop.ENVELOPE2D, null);
            if (bboxString != null) {
                bboxString = bboxString.trim();
                try {
                    ReferencedEnvelope bbox = parseEnvelope(bboxString);
                    if (bbox != null) retValue.setEnvelope(bbox);
                    else if (LOGGER.isLoggable(Level.INFO))
                        LOGGER.info("Cannot parse imposed bbox.");
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.INFO))
                        LOGGER.log(Level.INFO, "Cannot parse imposed bbox.", e);
                }
            }
        }

        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.AUXILIARY_FILE)) {
            retValue.setAuxiliaryFilePath(properties.getProperty(Prop.AUXILIARY_FILE));
        }
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.AUXILIARY_DATASTORE_FILE)) {
            retValue.setAuxiliaryDatastorePath(
                    properties.getProperty(Prop.AUXILIARY_DATASTORE_FILE));
        }

        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.CHECK_AUXILIARY_METADATA)) {
            final boolean checkAuxiliaryMetadata =
                    Boolean.valueOf(
                            properties.getProperty(Prop.CHECK_AUXILIARY_METADATA, "false").trim());
            retValue.setCheckAuxiliaryMetadata(checkAuxiliaryMetadata);
        }

        //
        // resolutions levels
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.LEVELS)) {
            int levelsNumber =
                    Integer.parseInt(properties.getProperty(Prop.LEVELS_NUM, "1").trim());
            retValue.setLevelsNum(levelsNumber);
            if (!properties.containsKey(Prop.LEVELS)) {
                if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("Required key Levels not found.");
                return null;
            }
            final String levels = properties.getProperty(Prop.LEVELS).trim();
            pairs = levels.split(" ");
            if (pairs.length != levelsNumber) {
                if (LOGGER.isLoggable(Level.INFO))
                    LOGGER.info(
                            "Levels number is different from the provided number of levels resoltion.");
                return null;
            }
            final double[][] resolutions = new double[levelsNumber][2];
            for (int i = 0; i < levelsNumber; i++) {
                pair = pairs[i].split(",");
                if (pair == null || pair.length != 2) {
                    if (LOGGER.isLoggable(Level.INFO))
                        LOGGER.info(
                                "OverviewLevel number is different from the provided number of levels resoltion.");
                    return null;
                }
                resolutions[i][0] = Double.parseDouble(pair[0]);
                resolutions[i][1] = Double.parseDouble(pair[1]);
            }
            retValue.setLevels(resolutions);
        }

        //
        // typename, is mandatory when we don't use shapeiles
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.TYPENAME)) {
            String typeName = properties.getProperty(Prop.TYPENAME, null);
            catalogConfigurationBean.setTypeName(typeName);
        }

        //
        // suggested spi is optional
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.SUGGESTED_SPI)) {
            if (properties.containsKey(Prop.SUGGESTED_SPI)) {
                final String suggestedSPI = properties.getProperty(Prop.SUGGESTED_SPI).trim();
                catalogConfigurationBean.setSuggestedSPI(suggestedSPI);
            }
        }

        //
        // suggested format is optional
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.SUGGESTED_FORMAT)) {
            if (properties.containsKey(Prop.SUGGESTED_FORMAT)) {
                final String suggestedFormat = properties.getProperty(Prop.SUGGESTED_FORMAT).trim();
                catalogConfigurationBean.setSuggestedFormat(suggestedFormat);
            }
        }

        //
        // suggested IS Spi is optional
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.SUGGESTED_IS_SPI)) {
            if (properties.containsKey(Prop.SUGGESTED_IS_SPI)) {
                final String suggestedIsSpi = properties.getProperty(Prop.SUGGESTED_IS_SPI).trim();
                catalogConfigurationBean.setSuggestedIsSPI(suggestedIsSpi);
            }
        }

        //
        // time attribute is optional
        //
        if (properties.containsKey(Prop.TIME_ATTRIBUTE)) {
            final String timeAttribute = properties.getProperty("TimeAttribute").trim();
            retValue.setTimeAttribute(timeAttribute);
        }

        //
        // elevation attribute is optional
        //
        if (properties.containsKey(Prop.ELEVATION_ATTRIBUTE)) {
            final String elevationAttribute =
                    properties.getProperty(Prop.ELEVATION_ATTRIBUTE).trim();
            retValue.setElevationAttribute(elevationAttribute);
        }

        //
        // crs attribute is optional
        //
        if (properties.containsKey(Prop.CRS_ATTRIBUTE)) {
            final String crsAttribute = properties.getProperty(Prop.CRS_ATTRIBUTE).trim();
            retValue.setCRSAttribute(crsAttribute);
        }

        //
        // resolution attributes are optional
        //
        if (properties.containsKey(Prop.RESOLUTION_ATTRIBUTE)) {
            final String attribute = properties.getProperty(Prop.RESOLUTION_ATTRIBUTE).trim();
            retValue.setResolutionAttribute(attribute);
        }
        if (properties.containsKey(Prop.RESOLUTION_X_ATTRIBUTE)) {
            final String attribute = properties.getProperty(Prop.RESOLUTION_X_ATTRIBUTE).trim();
            retValue.setResolutionXAttribute(attribute);
        }
        if (properties.containsKey(Prop.RESOLUTION_Y_ATTRIBUTE)) {
            final String attribute = properties.getProperty(Prop.RESOLUTION_Y_ATTRIBUTE).trim();
            retValue.setResolutionYAttribute(attribute);
        }

        //
        // additional domain attribute is optional
        //
        if (properties.containsKey(Prop.ADDITIONAL_DOMAIN_ATTRIBUTES)) {
            final String additionalDomainAttributes =
                    properties.getProperty(Prop.ADDITIONAL_DOMAIN_ATTRIBUTES).trim();
            retValue.setAdditionalDomainAttributes(additionalDomainAttributes);
        }

        //
        // caching
        //
        if (properties.containsKey(Prop.CACHING)) {
            String caching = properties.getProperty(Prop.CACHING).trim();
            try {
                catalogConfigurationBean.setCaching(Boolean.valueOf(caching));
            } catch (Throwable e) {
                catalogConfigurationBean.setCaching(
                        Boolean.valueOf(Utils.DEFAULT_CACHING_BEHAVIOR));
            }
        }

        //
        // name is not optional
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.NAME)) {
            if (!properties.containsKey(Prop.NAME)) {
                if (LOGGER.isLoggable(Level.SEVERE)) LOGGER.severe("Required key Name not found.");
                return null;
            }
            String coverageName = properties.getProperty(Prop.NAME).trim();
            retValue.setName(coverageName);
        }

        // need a color expansion?
        // this is a newly added property we have to be ready to the case where
        // we do not find it.
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.EXP_RGB)) {
            final boolean expandMe =
                    Boolean.valueOf(properties.getProperty(Prop.EXP_RGB, "false").trim());
            retValue.setExpandToRGB(expandMe);
        }

        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.WRAP_STORE)) {
            final boolean wrapStore =
                    Boolean.valueOf(properties.getProperty(Prop.WRAP_STORE, "false").trim());
            catalogConfigurationBean.setWrapStore(wrapStore);
        }

        //
        // Is heterogeneous granules mosaic
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.HETEROGENEOUS)) {
            final boolean heterogeneous =
                    Boolean.valueOf(properties.getProperty(Prop.HETEROGENEOUS, "false").trim());
            catalogConfigurationBean.setHeterogeneous(heterogeneous);
        }
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.HETEROGENEOUS_CRS)) {
            final boolean heterogeneousCRS =
                    Boolean.valueOf(properties.getProperty(Prop.HETEROGENEOUS_CRS, "false").trim());
            if (!catalogConfigurationBean.isHeterogeneous()) {
                catalogConfigurationBean.setHeterogeneous(heterogeneousCRS);
            }
            catalogConfigurationBean.setHeterogeneousCRS(heterogeneousCRS);
        }

        //
        // Absolute or relative path
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ABSOLUTE_PATH)) {
            final boolean absolutePath =
                    Boolean.valueOf(
                            properties
                                    .getProperty(
                                            Prop.ABSOLUTE_PATH,
                                            Boolean.toString(Utils.DEFAULT_PATH_BEHAVIOR))
                                    .trim());
            if (absolutePath) {
                catalogConfigurationBean.setPathType(PathType.ABSOLUTE);
            } else {
                catalogConfigurationBean.setPathType(PathType.RELATIVE);
            }
        }

        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.PATH_TYPE)) {
            final String pathType = properties.getProperty(Prop.PATH_TYPE);
            if (pathType != null) {
                catalogConfigurationBean.setPathType(PathType.valueOf(pathType.trim()));
            }
        }

        //
        // Footprint management
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.FOOTPRINT_MANAGEMENT)) {
            final boolean footprintManagement =
                    Boolean.valueOf(
                            properties.getProperty(Prop.FOOTPRINT_MANAGEMENT, "false").trim());
            retValue.setFootprintManagement(footprintManagement);
        }

        //
        // location
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.LOCATION_ATTRIBUTE)) {
            catalogConfigurationBean.setLocationAttribute(
                    properties
                            .getProperty(Prop.LOCATION_ATTRIBUTE, Utils.DEFAULT_LOCATION_ATTRIBUTE)
                            .trim());
        }

        //
        // CoverageNameCollectorSpi
        //
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.COVERAGE_NAME_COLLECTOR_SPI)) {
            String coverageNameCollectorSpi =
                    properties.getProperty(Prop.COVERAGE_NAME_COLLECTOR_SPI);
            if (coverageNameCollectorSpi != null
                    && ((coverageNameCollectorSpi = coverageNameCollectorSpi.trim()) != null)) {
                retValue.setCoverageNameCollectorSpi(coverageNameCollectorSpi);
            }
        }

        // target CRS
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.MOSAIC_CRS)) {
            String crsCode = properties.getProperty(Prop.MOSAIC_CRS);
            if (crsCode != null && !crsCode.isEmpty()) {
                try {
                    retValue.setCrs(decodeSrs(crsCode));
                } catch (FactoryException e) {
                    LOGGER.log(
                            Level.FINE,
                            "Unable to decode CRS of mosaic properties file. Configured CRS "
                                    + "code was: "
                                    + crsCode,
                            e);
                }
            }
        }

        // target NoData
        if (!ignoreSome || !ignorePropertiesSet.contains(Prop.NO_DATA)) {
            String noDataStr = properties.getProperty(Prop.NO_DATA);
            if (noDataStr != null && !noDataStr.isEmpty()) {
                try {
                    double noData = Double.parseDouble(noDataStr);
                    retValue.setNoData(noData);
                } catch (NumberFormatException e) {
                    LOGGER.log(
                            Level.FINE,
                            "Unable to decode NoData of mosaic properties file. Configured NoData "
                                    + "code was: "
                                    + noDataStr,
                            e);
                }
            }
        }

        // Also initialize the indexer here, since it will be needed later on.
        File mosaicParentFolder = URLs.urlToFile(sourceURL).getParentFile();
        Indexer indexer = loadIndexer(mosaicParentFolder);

        if (indexer != null) {
            retValue.setIndexer(indexer);
            String granuleCollectorFactorySPI =
                    IndexerUtils.getParameter(Prop.GRANULE_COLLECTOR_FACTORY, indexer);
            if (granuleCollectorFactorySPI == null || granuleCollectorFactorySPI.length() <= 0) {
                boolean isHeterogeneousCRS =
                        Boolean.parseBoolean(
                                IndexerUtils.getParameter(Prop.HETEROGENEOUS_CRS, indexer));
                if (isHeterogeneousCRS) {
                    // in this case we know we need the reprojecting collector anyway, let's use it
                    IndexerUtils.setParam(
                            indexer,
                            Prop.GRANULE_COLLECTOR_FACTORY,
                            ReprojectingSubmosaicProducerFactory.class.getName());
                }
            }
        }

        // return value
        return retValue;
    }

    private static CoordinateReferenceSystem decodeSrs(String property) throws FactoryException {
        return CRS.decode(property, true);
    }

    private static Indexer loadIndexer(File parentFolder) {
        Indexer defaultIndexer = IndexerUtils.createDefaultIndexer();
        Indexer configuredIndexer =
                IndexerUtils.initializeIndexer(defaultIndexer.getParameters(), parentFolder);
        return configuredIndexer;
    }

    /**
     * Parses a bbox in the form of MIX,MINY MAXX,MAXY
     *
     * @param bboxString the string to parse the bbox from
     * @return a {@link ReferencedEnvelope} with the parse bbox or null
     */
    public static ReferencedEnvelope parseEnvelope(final String bboxString) {
        if (bboxString == null || bboxString.length() == 0) return null;

        final String[] pairs = bboxString.split(" ");
        if (pairs != null && pairs.length == 2) {

            String[] pair1 = pairs[0].split(",");
            String[] pair2 = pairs[1].split(",");
            if (pair1 != null && pair1.length == 2 && pair2 != null && pair2.length == 2)
                return new ReferencedEnvelope(
                        Double.parseDouble(pair1[0]),
                        Double.parseDouble(pair2[0]),
                        Double.parseDouble(pair1[1]),
                        Double.parseDouble(pair2[1]),
                        null);
        }
        // something bad happened
        return null;
    }

    public static IOFileFilter excludeFilters(
            final IOFileFilter inputFilter, IOFileFilter... filters) {
        IOFileFilter retFilter = inputFilter;
        for (IOFileFilter filter : filters) {
            retFilter = FileFilterUtils.and(retFilter, FileFilterUtils.notFileFilter(filter));
        }
        return retFilter;
    }

    /**
     * Look for an {@link ImageReader} instance that is able to read the provided {@link
     * ImageInputStream}, which must be non null.
     *
     * <p>
     *
     * <p>In case no reader is found, <code>null</code> is returned.
     *
     * @param inStream an instance of {@link ImageInputStream} for which we need to find a suitable
     *     {@link ImageReader}.
     * @return a suitable instance of {@link ImageReader} or <code>null</code> if one cannot be
     *     found.
     */
    static ImageReader getReader(final ImageInputStream inStream) {
        Utilities.ensureNonNull("inStream", inStream);
        // get a reader
        inStream.mark();
        final Iterator<ImageReader> readersIt = ImageIO.getImageReaders(inStream);
        if (!readersIt.hasNext()) {
            return null;
        }
        return readersIt.next();
    }

    /**
     * Retrieves the dimensions of the {@link RenderedImage} at index <code>imageIndex</code> for
     * the provided {@link ImageReader} and {@link ImageInputStream}.
     *
     * <p>
     *
     * <p>Notice that none of the input parameters can be <code>null</code> or a {@link
     * NullPointerException} will be thrown. Morevoer the <code>imageIndex</code> cannot be negative
     * or an {@link IllegalArgumentException} will be thrown.
     *
     * @param imageIndex the index of the image to get the dimensions for.
     * @param reader the {@link ImageReader} to decode the image dimensions.
     * @return a {@link Rectangle} that contains the dimensions for the image at index <code>
     *     imageIndex</code>
     * @throws IOException in case the {@link ImageReader} or the {@link ImageInputStream} fail.
     */
    static Rectangle getDimension(final int imageIndex, final ImageReader reader)
            throws IOException {
        Utilities.ensureNonNull("reader", reader);
        if (imageIndex < 0)
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, imageIndex));
        return new Rectangle(0, 0, reader.getWidth(imageIndex), reader.getHeight(imageIndex));
    }

    /** Default priority for the underlying {@link Thread}. */
    public static final int DEFAULT_PRIORITY = Thread.NORM_PRIORITY;

    /** Default location attribute name. */
    public static final String DEFAULT_LOCATION_ATTRIBUTE = "location";

    public static final String DEFAULT_INDEX_NAME = "index";

    /**
     * Checks that a {@link File} is a real file, exists and is readable.
     *
     * @param file the {@link File} instance to check. Must not be null.
     * @return <code>true</code> in case the file is a real file, exists and is readable; <code>
     *     false </code> otherwise.
     */
    public static boolean checkFileReadable(final File file) {
        if (LOGGER.isLoggable(Level.FINE)) {
            final String message = getFileInfo(file);
            LOGGER.fine(message);
        }
        if (!file.exists() || !file.canRead() || !file.isFile()) return false;
        return true;
    }

    /**
     * Creates a human readable message that describe the provided {@link File} object in terms of
     * its properties.
     *
     * <p>
     *
     * <p>Useful for creating meaningful log messages.
     *
     * @param file the {@link File} object to create a descriptive message for
     * @return a {@link String} containing a descriptive message about the provided {@link File}.
     */
    public static String getFileInfo(final File file) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Checking file:")
                .append(FilenameUtils.getFullPath(file.getAbsolutePath()))
                .append("\n");
        builder.append("isHidden:").append(file.isHidden()).append("\n");
        builder.append("exists:").append(file.exists()).append("\n");
        builder.append("isFile").append(file.isFile()).append("\n");
        builder.append("canRead:").append(file.canRead()).append("\n");
        builder.append("canWrite").append(file.canWrite()).append("\n");
        builder.append("canExecute:").append(file.canExecute()).append("\n");
        builder.append("isAbsolute:").append(file.isAbsolute()).append("\n");
        builder.append("lastModified:").append(file.lastModified()).append("\n");
        builder.append("length:").append(file.length());
        final String message = builder.toString();
        return message;
    }

    /** */
    public static String checkDirectory(String testingDirectory, boolean writable)
            throws IllegalArgumentException {

        File inDir = new File(testingDirectory);
        boolean failure =
                !inDir.exists() || !inDir.isDirectory() || inDir.isHidden() || !inDir.canRead();
        if (writable) {
            failure |= !inDir.canWrite();
        }
        if (failure) {
            String message =
                    "Unable to create the mosaic\n"
                            + "location is:"
                            + testingDirectory
                            + "\n"
                            + "location exists:"
                            + inDir.exists()
                            + "\n"
                            + "location is a directory:"
                            + inDir.isDirectory()
                            + "\n"
                            + "location is writable:"
                            + inDir.canWrite()
                            + "\n"
                            + "location is readable:"
                            + inDir.canRead()
                            + "\n"
                            + "location is hidden:"
                            + inDir.isHidden()
                            + "\n";
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        try {
            testingDirectory = inDir.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        testingDirectory = FilenameUtils.normalize(testingDirectory);
        if (!testingDirectory.endsWith(File.separator))
            testingDirectory = testingDirectory + File.separator;
        // test to see if things are still good
        inDir = new File(testingDirectory);
        failure = !inDir.exists() || !inDir.isDirectory() || inDir.isHidden() || !inDir.canRead();
        if (writable) {
            failure |= !inDir.canWrite();
        }
        if (failure) {
            String message =
                    "Unable to create the mosaic\n"
                            + "location is:"
                            + testingDirectory
                            + "\n"
                            + "location exists:"
                            + inDir.exists()
                            + "\n"
                            + "location is a directory:"
                            + inDir.isDirectory()
                            + "\n"
                            + "location is writable:"
                            + inDir.canWrite()
                            + "\n"
                            + "location is readable:"
                            + inDir.canRead()
                            + "\n"
                            + "location is hidden:"
                            + inDir.isHidden()
                            + "\n";
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        return testingDirectory;
    }

    static boolean checkURLReadable(URL url) {
        try {
            url.openStream().close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static final DataStoreFactorySpi SHAPE_SPI = new ShapefileDataStoreFactory();

    static final String DIRECT_KAKADU_PLUGIN =
            "it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReader";

    public static final boolean DEFAULT_RECURSION_BEHAVIOR = true;

    /** */
    public static Map<String, Serializable> createDataStoreParamsFromPropertiesFile(
            final URL datastoreProperties) throws IOException {
        // read the properties file
        Properties properties = CoverageUtilities.loadPropertiesFromURL(datastoreProperties);
        if (properties == null) return null;

        // SPI
        final String SPIClass = properties.getProperty("SPI");
        try {
            // create a datastore as instructed
            final DataStoreFactorySpi spi =
                    (DataStoreFactorySpi)
                            Class.forName(SPIClass).getDeclaredConstructor().newInstance();
            return createDataStoreParamsFromPropertiesFile(properties, spi);
        } catch (Exception e) {
            final IOException ioe = new IOException();
            throw (IOException) ioe.initCause(e);
        }
    }

    /**
     * Store a sample image from which we can derive the default SM and CM
     *
     * @param sampleImageFile where we should store the image
     * @param defaultSM the {@link SampleModel} for the sample image.
     * @param defaultCM the {@link ColorModel} for the sample image.
     * @throws IOException in case something bad occurs during writing.
     */
    public static void storeSampleImage(
            final File sampleImageFile, final SampleModel defaultSM, final ColorModel defaultCM)
            throws IOException {

        SampleImage sampleImage = new SampleImage(defaultSM, defaultCM);

        // serialize it
        try (ObjectOutputStream ooStream =
                new ObjectOutputStream(
                        new BufferedOutputStream(new FileOutputStream(sampleImageFile)))) {
            ooStream.writeObject(sampleImage);
        }
    }

    /**
     * Load a sample image from which we can take the sample model and color model to be used to
     * fill holes in responses.
     *
     * @param sampleImageFile the path to sample image.
     * @return a sample image from which we can take the sample model and color model to be used to
     *     fill holes in responses.
     */
    public static RenderedImage loadSampleImage(final File sampleImageFile) {
        // serialize it
        InputStream inStream = null;
        ObjectInputStream oiStream = null;
        try {

            // do we have the sample image??
            if (Utils.checkFileReadable(sampleImageFile)) {
                inStream = new BufferedInputStream(new FileInputStream(sampleImageFile));
                oiStream = new ObjectInputStream(inStream);

                // load the image
                Object object = oiStream.readObject();
                if (object instanceof SampleImage) {
                    SampleImage si = (SampleImage) object;
                    return si.toBufferedImage();
                } else if (object instanceof SerializableRenderedImage) {
                    SerializableRenderedImage sri = (SerializableRenderedImage) object;
                    // SerializableRenderedImage is a finalization thread killer, try to replace
                    // it with SampleImage on disk instead
                    if (sampleImageFile.canWrite()) {
                        try {
                            storeSampleImage(
                                    sampleImageFile, sri.getSampleModel(), sri.getColorModel());
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(
                                        Level.WARNING,
                                        "Failed to upgrade the sample image to the new storage format",
                                        e);
                            }
                        }
                    }
                    // note, disposing the SerializableRenderedImage here is not done on purpose,
                    // as it will hang, timeout and fail, and then on finalize
                    // it will do it again, so there is really no point in doing that
                    return new SampleImage(sri.getSampleModel(), sri.getColorModel())
                            .toBufferedImage();
                } else {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("Unrecognized sample_image content: " + object);
                    }
                    return null;
                }

            } else {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.warning("Unable to find sample image for path " + sampleImageFile);
                return null;
            }
        } catch (FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } catch (ClassNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        } finally {
            try {
                if (inStream != null) inStream.close();
            } catch (Throwable e) {

                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
            try {
                if (oiStream != null) oiStream.close();
            } catch (Throwable e) {

                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
    }

    /** A transparent color for missing data. */
    static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    // final static Boolean IGNORE_FOOTPRINT = Boolean.getBoolean("org.geotools.footprint.ignore");

    public static final boolean DEFAULT_FOOTPRINT_MANAGEMENT = true;

    public static final boolean DEFAULT_CONFIGURATION_CACHING = false;

    public static Map<String, Serializable> createDataStoreParamsFromPropertiesFile(
            Properties properties, DataStoreFactorySpi spi) throws IOException {
        // get the params
        final Map<String, Serializable> params = new HashMap<String, Serializable>();
        final Param[] paramsInfo = spi.getParametersInfo();
        for (Param p : paramsInfo) {
            // search for this param and set the value if found
            if (properties.containsKey(p.key)) {
                params.put(
                        p.key,
                        (Serializable) Converters.convert(properties.getProperty(p.key), p.type));
            } else if (p.required && p.sample == null)
                throw new IOException("Required parameter missing: " + p.toString());
        }

        return params;
    }

    public static Map<String, Serializable> filterDataStoreParams(
            Properties properties, DataStoreFactorySpi spi) throws IOException {
        // get the params
        final Map<String, Serializable> params = new HashMap<String, Serializable>();
        final Param[] paramsInfo = spi.getParametersInfo();
        for (Param p : paramsInfo) {
            // search for this param and set the value if found
            if (properties.containsKey(p.key)) {
                params.put(p.key, (Serializable) Converters.convert(properties.get(p.key), p.type));
            } else if (p.required && p.sample == null)
                throw new IOException("Required parameter missing: " + p.toString());
        }

        return params;
    }

    static URL checkSource(Object source, Hints hints) {

        SourceGetter sourceGetter = new SourceGetter(source);
        URL sourceURL = sourceGetter.getUrl();
        File sourceFile = sourceGetter.getFile();

        //
        // Check source
        //

        // //
        //
        // at this point we have tried to convert the thing to a File as hard as
        // we could, let's see what we can do
        //
        // //
        if (sourceFile != null) {
            if (!sourceFile.isDirectory())
                // real file, can only be a shapefile at this stage or a
                // datastore.properties file
                sourceURL = URLs.fileToUrl(sourceFile);
            else {
                // it's a DIRECTORY, let's look for a possible properties files
                // that we want to load
                final String locationPath = sourceFile.getAbsolutePath();
                final String defaultIndexName = getDefaultIndexName(locationPath);
                boolean datastoreFound = false;
                boolean buildMosaic = false;

                //
                // do we have a datastore properties file? It will preempt on
                // the shapefile
                // TODO: Refactor these checks once we integrate datastore on indexer.xml
                //
                File dataStoreProperties = new File(locationPath, "datastore.properties");
                // File emptyFile = new File(locationPath,"empty");

                // this can be used to look for properties files that do NOT
                // define a datastore
                final File[] properties =
                        sourceFile.listFiles(
                                (FilenameFilter)
                                        FileFilterUtils.and(
                                                FileFilterUtils.notFileFilter(
                                                        FileFilterUtils.nameFileFilter(
                                                                "datastore.properties")),
                                                FileFilterUtils.makeFileOnly(
                                                        FileFilterUtils.suffixFileFilter(
                                                                ".properties"))));

                // do we have a valid datastore + mosaic properties pair?
                if (Utils.checkFileReadable(dataStoreProperties)) {
                    // we have a datastore.properties file
                    datastoreFound = true;

                    // check the first valid mosaic properties
                    boolean found = false;
                    for (File propFile : properties)
                        if (Utils.checkFileReadable(propFile)) {
                            // load it
                            if (null != Utils.loadMosaicProperties(URLs.fileToUrl(propFile))) {
                                found = true;
                                break;
                            }
                        }

                    // we did not find any good candidate for mosaic.properties
                    // file, this will signal it
                    if (!found) buildMosaic = true;

                } else {
                    // we did not find any good candidate for mosaic.properties
                    // file, this will signal it
                    buildMosaic = true;
                    datastoreFound = false;
                }

                //
                // now let's try with shapefile and properties couple
                //
                File shapeFile = null;
                if (!datastoreFound) {
                    for (File propFile : properties) {

                        // load properties
                        if (null == Utils.loadMosaicProperties(URLs.fileToUrl(propFile))) continue;

                        // look for a couple shapefile, mosaic properties file
                        shapeFile =
                                new File(
                                        locationPath,
                                        FilenameUtils.getBaseName(propFile.getName()) + ".shp");
                        if (!Utils.checkFileReadable(shapeFile)
                                && Utils.checkFileReadable(propFile)) buildMosaic = true;
                        else {
                            buildMosaic = false;
                            break;
                        }
                    }
                }

                // did we find anything? If no, we try to build a new mosaic
                if (buildMosaic) {
                    ////
                    //
                    // Creating a new mosaic
                    //
                    ////
                    // try to build a mosaic inside this directory and see what
                    // happens

                    // preliminar checks
                    final File mosaicDirectory = new File(locationPath);
                    if (!mosaicDirectory.exists()
                            || mosaicDirectory.isFile()
                            || !mosaicDirectory.canWrite()) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(
                                    Level.SEVERE,
                                    "Unable to create the mosaic, check the location:\n"
                                            + "location is:"
                                            + locationPath
                                            + "\n"
                                            + "location exists:"
                                            + mosaicDirectory.exists()
                                            + "\n"
                                            + "location is a directory:"
                                            + mosaicDirectory.isDirectory()
                                            + "\n"
                                            + "location is writable:"
                                            + mosaicDirectory.canWrite()
                                            + "\n"
                                            + "location is readable:"
                                            + mosaicDirectory.canRead()
                                            + "\n"
                                            + "location is hidden:"
                                            + mosaicDirectory.isHidden()
                                            + "\n");
                        }
                        return null;
                    }

                    // actual creation
                    createMosaic(
                            locationPath,
                            defaultIndexName,
                            DEFAULT_WILCARD,
                            DEFAULT_PATH_BEHAVIOR,
                            hints);

                    // check that the mosaic properties file was created
                    final File propertiesFile =
                            new File(locationPath, defaultIndexName + ".properties");
                    if (!Utils.checkFileReadable(propertiesFile)) {
                        // retrieve a null so that we shows that a problem occurred
                        if (!checkMosaicHasBeenInitialized(locationPath, defaultIndexName)) {
                            sourceURL = null;
                            return sourceURL;
                        }
                    }

                    // check that the shapefile was correctly created in case it
                    // was needed
                    sourceURL =
                            updateSourceURL(
                                    sourceURL,
                                    datastoreFound,
                                    locationPath,
                                    defaultIndexName /* , emptyFile */);

                } else
                    // now set the new source and proceed
                    sourceURL =
                            datastoreFound
                                    ? URLs.fileToUrl(dataStoreProperties)
                                    : URLs.fileToUrl(shapeFile);
            }
        }
        return sourceURL;
    }

    private static String getDefaultIndexName(final String locationPath) {
        String name = getIndexerProperty(locationPath, Utils.Prop.NAME);
        if (name != null) {
            return name;
        }

        return FilenameUtils.getName(locationPath);
    }

    public static String getIndexerProperty(String locationPath, String propertyName) {
        if (locationPath == null) {
            return null;
        }
        File file = new File(locationPath);
        if (file.isDirectory()) {
            File indexer = new File(file, IndexerUtils.INDEXER_PROPERTIES);
            if (indexer.exists()) {
                URL indexerUrl = URLs.fileToUrl(indexer);
                Properties config = CoverageUtilities.loadPropertiesFromURL(indexerUrl);
                if (config != null && config.get(Utils.Prop.NAME) != null) {
                    return (String) config.get(propertyName);
                }
            }
            indexer = new File(file, IndexerUtils.INDEXER_XML);
            String name = IndexerUtils.getParameter(Utils.Prop.NAME, indexer);
            if (name != null) {
                return name;
            }
        }

        return null;
    }

    /** Look for a proper sourceURL to be returned. */
    private static URL updateSourceURL(
            URL sourceURL,
            boolean datastoreFound,
            String locationPath,
            String defaultIndexName /*
                                    * , File emptyFile
                                    */) {
        if (!datastoreFound) {
            File shapeFile = new File(locationPath, defaultIndexName + ".shp");

            if (!Utils.checkFileReadable(shapeFile)) {
                // if (!Utils.checkFileReadable(emptyFile)) {
                sourceURL = null;
                // } else {
                // sourceURL = URLs.fileToUrl(emptyFile);
                // }
            } else {
                // now set the new source and proceed
                sourceURL = URLs.fileToUrl(shapeFile);
            }
        } else {
            File dataStoreProperties = new File(locationPath, "datastore.properties");

            // datastore.properties as the source
            if (!Utils.checkFileReadable(dataStoreProperties)) {
                sourceURL = null;
            } else {
                sourceURL = URLs.fileToUrl(dataStoreProperties);
            }
        }

        return sourceURL;
    }

    private static boolean checkMosaicHasBeenInitialized(
            String locationPath, String defaultIndexName) {
        File mosaicFile = new File(locationPath, defaultIndexName + ".xml");
        if (Utils.checkFileReadable(mosaicFile)) {
            return true;
        }
        mosaicFile = new File(locationPath, defaultIndexName + ".properties");
        if (Utils.checkFileReadable(mosaicFile)) {
            return true;
        }

        // Fallback on empty mosaic check on default indexers
        File indexFile = new File(locationPath, IndexerUtils.INDEXER_XML);
        if (Utils.checkFileReadable(indexFile)) {
            String canBeEmpty = IndexerUtils.getParameter(Prop.CAN_BE_EMPTY, indexFile);
            if (canBeEmpty != null) {
                if (Boolean.parseBoolean(canBeEmpty)) {
                    return true;
                }
            }
        }
        indexFile = new File(locationPath, IndexerUtils.INDEXER_PROPERTIES);
        if (Utils.checkFileReadable(indexFile)) {
            URL url = URLs.fileToUrl(indexFile);
            final Properties properties = CoverageUtilities.loadPropertiesFromURL(url);
            if (properties != null) {
                String canBeEmpty = properties.getProperty(Prop.CAN_BE_EMPTY, null);
                if (canBeEmpty != null) {
                    if (Boolean.parseBoolean(canBeEmpty)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static final double SAMEBBOX_THRESHOLD_FACTOR = 20;

    public static final boolean DEFAULT_COLOR_EXPANSION_BEHAVIOR = false;

    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    static final String DESCENDING_ORDER_IDENTIFIER = " D"; // SortOrder.DESCENDING.identifier();

    static final String ASCENDING_ORDER_IDENTIFIER = " A"; // SortOrder.ASCENDING.identifier();

    public static final String SCAN_FOR_TYPENAMES = "TypeNames";

    public static final String SAMPLE_IMAGE_NAME_LEGACY = "sample_image";
    public static final String SAMPLE_IMAGE_NAME = "sample_image.dat";

    public static final String BBOX = "BOUNDINGBOX";

    public static final String TIME_DOMAIN = "TIME";

    public static final String ELEVATION_DOMAIN = "ELEVATION";

    public static final String CRS_DOMAIN = "CRS";

    public static final String RESOLUTION_DOMAIN = "RESOLUTION";

    public static final String RESOLUTION_X_DOMAIN = "RESOLUTION_X";

    public static final String RESOLUTION_Y_DOMAIN = "RESOLUTION_Y";

    public static final String ADDITIONAL_DOMAIN = "ADDITIONAL";

    public static ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    static IOFileFilter CLEANUP_FILTER;

    static IOFileFilter MOSAIC_SUPPORT_FILES_FILTER;

    /**
     * Private constructor to initialize the ehCache instance. It can be configured through a Bean.
     */
    private Utils(Cache ehcache) {
        Utils.ehcache = ehcache;
    }

    /**
     * Setup a {@link Histogram} object by deserializing a file representing a serialized Histogram.
     *
     * @return the deserialized histogram.
     */
    public static Histogram getHistogram(final String file) {
        Utilities.ensureNonNull("file", file);
        Histogram histogram = null;

        // Firstly: check if the histogram have been already
        // deserialized and it is available in cache
        if (ehcache != null && ehcache.isKeyInCache(file)) {
            if (ehcache.isElementInMemory(file)) {
                final Element element = ehcache.get(file);
                if (element != null) {
                    final Object value = element.getObjectValue();
                    if (value != null && value instanceof Histogram) {
                        histogram = (Histogram) value;
                        return histogram;
                    }
                }
            }
        }

        // No histogram in cache. Deserializing...
        if (histogram == null) {
            try (ObjectInputStream objectStream =
                    new ObjectInputStream(new FileInputStream(file))) {
                histogram = (Histogram) objectStream.readObject();
                if (ehcache != null) {
                    ehcache.put(new Element(file, histogram));
                }
            } catch (FileNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } catch (ClassNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            }
        }
        return histogram;
    }

    /**
     * Check if the provided granule's footprint covers the same area of the granule's bbox.
     *
     * @param granuleFootprint the granule Footprint
     * @param granuleBBOX the granule bbox
     * @return {@code true} in case the footprint isn't covering the FULL granule's bbox.
     */
    static boolean areaIsDifferent(
            final Geometry granuleFootprint,
            final AffineTransform baseGridToWorld,
            final ReferencedEnvelope granuleBBOX) {

        // //
        //
        // First preliminar check:
        // check if the footprint's bbox corners are the same of the granule's bbox
        // (Using a threshold)
        //
        // //
        final Envelope envelope = granuleFootprint.getEnvelope().getEnvelopeInternal();
        double deltaMinX = Math.abs(envelope.getMinX() - granuleBBOX.getMinX());
        double deltaMinY = Math.abs(envelope.getMinY() - granuleBBOX.getMinY());
        double deltaMaxX = Math.abs(envelope.getMaxX() - granuleBBOX.getMaxX());
        double deltaMaxY = Math.abs(envelope.getMaxY() - granuleBBOX.getMaxY());
        final double resX = XAffineTransform.getScaleX0(baseGridToWorld);
        final double resY = XAffineTransform.getScaleY0(baseGridToWorld);
        final double toleranceX = resX / Utils.SAMEBBOX_THRESHOLD_FACTOR;
        final double toleranceY = resY / Utils.SAMEBBOX_THRESHOLD_FACTOR;

        // Taking note of the area of a single cell
        final double cellArea = resX * resY;

        if (deltaMinX > toleranceX
                || deltaMaxX > toleranceX
                || deltaMinY > toleranceY
                || deltaMaxY > toleranceY) {
            // delta exceed tolerance. Area is not the same
            return true;
        }

        // //
        //
        // Second check:
        // Here, the footprint's bbox and the granule's bbox are equal.
        // However this is not enough:
        // - suppose the footprint is a diamond
        // - Create a rectangle by circumscribing the diamond
        // - If this rectangle match with the granule's bbox, this doesn't imply
        // that the diamond covers the same area of the bbox.
        // Therefore, we need to compute the area and compare them.
        //
        // //
        final double footprintArea = granuleFootprint.getArea();
        // final double bboxArea = granuleBBOX.getArea();
        final double bboxArea = granuleBBOX.getHeight() * granuleBBOX.getWidth();

        // If 2 areas are different more than the cellArea, then they are not the same area
        if (Math.abs(footprintArea - bboxArea) > cellArea) return true;
        return false;
    }

    /**
     * Checks if the Shape equates to a Rectangle, if it does it performs a conversion, otherwise
     * returns null
     */
    static Rectangle toRectangle(Shape shape) {
        if (shape instanceof Rectangle) {
            return (Rectangle) shape;
        }

        if (shape == null) {
            return null;
        }

        // check if it's equivalent to a rectangle
        PathIterator iter = shape.getPathIterator(new AffineTransform());
        double[] coords = new double[2];

        // not enough points?
        if (iter.isDone()) {
            return null;
        }

        // get the first and init the data structures
        iter.next();
        int action = iter.currentSegment(coords);
        if (action != PathIterator.SEG_MOVETO && action != PathIterator.SEG_LINETO) {
            return null;
        }
        double minx = coords[0];
        double miny = coords[1];
        double maxx = minx;
        double maxy = miny;
        double prevx = minx;
        double prevy = miny;
        int i = 0;

        // at most 4 steps, if more it's not a strict rectangle
        for (; i < 4 && !iter.isDone(); i++) {
            iter.next();
            action = iter.currentSegment(coords);

            if (action == PathIterator.SEG_CLOSE) {
                break;
            }
            if (action != PathIterator.SEG_LINETO) {
                return null;
            }

            // check orthogonal step (x does not change and y does, or vice versa)
            double x = coords[0];
            double y = coords[1];
            if (!(prevx == x && prevy != y) && !(prevx != x && prevy == y)) {
                return null;
            }

            // update mins and maxes
            if (x < minx) {
                minx = x;
            } else if (x > maxx) {
                maxx = x;
            }
            if (y < miny) {
                miny = y;
            } else if (y > maxy) {
                maxy = y;
            }

            // keep track of prev step
            prevx = x;
            prevy = y;
        }

        // if more than 4 other points it's not a standard rectangle
        iter.next();
        if (!iter.isDone() || i != 3) {
            return null;
        }

        // turn it into a rectangle
        return new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny).getBounds();
    }

    public static ImageLayout getImageLayoutHint(RenderingHints renderHints) {
        if (renderHints == null || !renderHints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
            return null;
        } else {
            return (ImageLayout) renderHints.get(JAI.KEY_IMAGE_LAYOUT);
        }
    }

    public static TileCache getTileCacheHint(RenderingHints renderHints) {
        if (renderHints == null || !renderHints.containsKey(JAI.KEY_TILE_CACHE)) {
            return null;
        } else {
            return (TileCache) renderHints.get(JAI.KEY_TILE_CACHE);
        }
    }

    public static BorderExtender getBorderExtenderHint(RenderingHints renderHints) {
        if (renderHints == null || !renderHints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
            return null;
        } else {
            return (BorderExtender) renderHints.get(JAI.KEY_BORDER_EXTENDER);
        }
    }

    public static TileScheduler getTileSchedulerHint(RenderingHints renderHints) {
        if (renderHints == null || !renderHints.containsKey(JAI.KEY_TILE_SCHEDULER)) {
            return null;
        } else {
            return (TileScheduler) renderHints.get(JAI.KEY_TILE_SCHEDULER);
        }
    }

    public static Hints setupJAIHints(RenderingHints inputHints) {
        final Hints hints = new Hints();
        if (inputHints != null) {

            // TileCache
            TileCache tc = Utils.getTileCacheHint(inputHints);
            if (tc != null) {
                hints.add(new RenderingHints(JAI.KEY_TILE_CACHE, tc));
            }

            // TileScheduler
            TileScheduler tileScheduler = Utils.getTileSchedulerHint(inputHints);
            if (tileScheduler != null) {
                hints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, tileScheduler));
            }
        }
        return hints;
    }

    /** Create a Range of numbers from a couple of values. */
    public static Range<? extends Number> createRange(Object firstValue, Object secondValue) {
        Class<? extends Object> targetClass = firstValue.getClass();
        Class<? extends Object> target2Class = secondValue.getClass();
        if (targetClass != target2Class) {
            throw new IllegalArgumentException(
                    "The 2 values need to belong to the same class:\n"
                            + "firstClass = "
                            + targetClass.toString()
                            + "; secondClass = "
                            + targetClass.toString());
        }
        if (targetClass == Byte.class) {
            return new Range<Byte>(Byte.class, (Byte) firstValue, (Byte) secondValue);
        } else if (targetClass == Short.class) {
            return new Range<Short>(Short.class, (Short) firstValue, (Short) secondValue);
        } else if (targetClass == Integer.class) {
            return new Range<Integer>(Integer.class, (Integer) firstValue, (Integer) secondValue);
        } else if (targetClass == Long.class) {
            return new Range<Long>(Long.class, (Long) firstValue, (Long) secondValue);
        } else if (targetClass == Float.class) {
            return new Range<Float>(Float.class, (Float) firstValue, (Float) secondValue);
        } else if (targetClass == Double.class) {
            return new Range<Double>(Double.class, (Double) firstValue, (Double) secondValue);
        } else return null;
    }

    /** Simple minimal check which checks whether and indexer file exists */
    public static boolean minimalIndexCheck(Object source) {
        File sourceFile = null;
        URL sourceURL = null;
        if (source instanceof File) {
            sourceFile = (File) source;
        } else if (source instanceof URL) {
            sourceURL = (URL) source;
            if (sourceURL.getProtocol().equals("file")) {
                sourceFile = URLs.urlToFile(sourceURL);
            }
        } else if (source instanceof String) {
            // is it a File?
            final String tempSource = (String) source;
            File tempFile = new File(tempSource);
            if (!tempFile.exists()) {
                return false;
            } else {
                // so that we can do our magic here below
                sourceFile = tempFile;
            }
        }
        final File indexerProperties = new File(sourceFile, IndexerUtils.INDEXER_PROPERTIES);
        if (Utils.checkFileReadable(indexerProperties)) {
            return true;
        }
        final File indexerXML = new File(sourceFile, IndexerUtils.INDEXER_XML);
        if (Utils.checkFileReadable(indexerXML)) {
            return true;
        }
        return false;
    }

    /** Check whether 2 resolution levels sets are homogeneous (within a tolerance) */
    public static boolean homogeneousCheck(
            final int numberOfLevels, double[][] resolutionLevels, double[][] compareLevels) {
        for (int k = 0; k < numberOfLevels; k++) {
            if (Math.abs(resolutionLevels[k][0] - compareLevels[k][0])
                            > RESOLUTION_TOLERANCE_FACTOR * compareLevels[k][0]
                    || Math.abs(resolutionLevels[k][1] - compareLevels[k][1])
                            > RESOLUTION_TOLERANCE_FACTOR * compareLevels[k][1]) {
                return false;
            }
        }
        return true;
    }

    /** Unmarshal the file and return and Indexer object. */
    public static Indexer unmarshal(File indexerFile) throws JAXBException {
        Unmarshaller unmarshaller = null;
        Indexer indexer = null;
        if (indexerFile != null) {
            unmarshaller = CONTEXT.createUnmarshaller();
            indexer = (Indexer) unmarshaller.unmarshal(indexerFile);
        }
        return indexer;
    }

    /** Marshals the Indexer object to the specified file */
    public static void marshal(Indexer indexer, File indexerFile) throws JAXBException {
        Marshaller marshaller = CONTEXT.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(indexer, indexerFile);
    }

    /**
     * This method checks the {@link ColorModel} of the current image with the one of the first
     * image in order to check if they are compatible or not in order to perform a mosaic operation.
     *
     * <p>
     *
     * <p>It is worth to point out that we also check if, in case we have two index color model
     * image, we also try to suggest whether or not we should do a color expansion.
     *
     * @return a boolean asking to skip this feature.
     */
    public static boolean checkColorModels(
            ColorModel defaultCM, byte[][] defaultPalette, ColorModel actualCM) {

        // check the number of color components
        final int defNumComponents = defaultCM.getNumColorComponents();
        int actualNumComponents = actualCM.getNumColorComponents();
        int colorComponentsDifference = Math.abs(defNumComponents - actualNumComponents);

        if (colorComponentsDifference != 0) {
            if ((defNumComponents == 1 && defaultCM instanceof ComponentColorModel)
                    || (actualNumComponents == 1 && actualCM instanceof ComponentColorModel)) {
                // gray expansion can be performed
                return false;
            }
        } else {
            return false;
        }

        //
        // if we get here this means that the two color models where completely
        // different, hence skip this feature.
        //
        return true;
    }

    /*
     * Checks if the provided factory spi builds a H2 store
     */
    public static boolean isH2Store(DataStoreFactorySpi spi) {
        String spiName = spi == null ? null : spi.getClass().getName();
        return "org.geotools.data.h2.H2DataStoreFactory".equals(spiName)
                || "org.geotools.data.h2.H2JNDIDataStoreFactory".equals(spiName);
    }

    public static void fixH2DatabaseLocation(
            Map<String, Serializable> params, String parentLocation) throws MalformedURLException {
        if (params.containsKey(DATABASE_KEY)) {
            String dbname = (String) params.get(DATABASE_KEY);
            // H2 database URLs must not be percent-encoded: see GEOT-4262.
            params.put(
                    DATABASE_KEY,
                    "file:"
                            + (new File(URLs.urlToFile(new URL(parentLocation)), dbname))
                                    .getPath());
        }
    }

    /** Checks if the provided factory spi builds a Oracle store */
    public static boolean isOracleStore(DataStoreFactorySpi spi) {
        String spiName = spi == null ? null : spi.getClass().getName();
        return "org.geotools.data.oracle.OracleNGOCIDataStoreFactory".equals(spiName)
                || "org.geotools.data.oracle.OracleNGJNDIDataStoreFactory".equals(spiName)
                || "org.geotools.data.oracle.OracleNGDataStoreFactory".equals(spiName);
    }

    /** Checks if the provided factory spi builds a Postgis store */
    public static boolean isPostgisStore(DataStoreFactorySpi spi) {
        String spiName = spi == null ? null : spi.getClass().getName();
        return "org.geotools.data.postgis.PostgisNGJNDIDataStoreFactory".equals(spiName)
                || "org.geotools.data.postgis.PostgisNGDataStoreFactory".equals(spiName);
    }

    /** Merge statistics across datasets. */
    public static PAMDataset mergePamDatasets(PAMDataset[] pamDatasets) {
        PAMDataset merged = pamDatasets[0];
        if (pamDatasets.length > 1) {
            merged = initRasterBands(pamDatasets[0]);
            if (merged != null) {
                for (PAMDataset pamDataset : pamDatasets) {
                    updatePamDatasets(pamDataset, merged);
                }
            }
        }
        return merged;
    }

    /**
     * Merge basic statistics on destination {@link PAMDataset} {@link PAMRasterBand}s need to have
     * same size. No checks are performed here
     */
    private static void updatePamDatasets(PAMDataset inputPamDataset, PAMDataset outputPamDataset) {
        List<PAMRasterBand> inputRasterBands = inputPamDataset.getPAMRasterBand();
        List<PAMRasterBand> outputRasterBands = outputPamDataset.getPAMRasterBand();
        for (int i = 0; i < inputRasterBands.size(); i++) {
            updateRasterBand(inputRasterBands.get(i), outputRasterBands.get(i));
        }
    }

    /**
     * Merge basic statistics on {@link PAMRasterBand} by updating min/max Other statistics still
     * need some work. {@link MDI}s need to have same size. No checks are performed here
     */
    private static void updateRasterBand(
            PAMRasterBand inputPamRasterBand, PAMRasterBand outputPamRasterBand) {
        List<MDI> mdiInputs = inputPamRasterBand.getMetadata().getMDI();
        List<MDI> mdiOutputs = outputPamRasterBand.getMetadata().getMDI();
        for (int i = 0; i < mdiInputs.size(); i++) {
            MDI mdiInput = mdiInputs.get(i);
            MDI mdiOutput = mdiOutputs.get(i);
            updateMDI(mdiInput, mdiOutput);
        }
    }

    /**
     * Update min and max for mdiOutput. Other statistics need better management. For the moment we
     * simply returns the min between them
     */
    private static void updateMDI(MDI mdiInput, MDI mdiOutput) {
        Double current = Double.parseDouble(mdiInput.getValue());
        Object value = mdiOutput.getValue();
        if (value != null) {
            Double output = Double.parseDouble((String) value);
            if (mdiInput.getKey().toUpperCase().endsWith("_MAXIMUM")) {
                if (current < output) {
                    current = output;
                }
            } else {
                if (output < current) {
                    current = output;
                }
            }
        }
        mdiOutput.setValue(Double.toString(current));
    }

    /**
     * Initialize a list of {@link PAMRasterBand}s having same size of the sample {@link PAMDataset}
     * and same metadata names.
     */
    private static PAMDataset initRasterBands(PAMDataset samplePam) {
        PAMDataset merged = null;
        if (samplePam != null) {
            merged = new PAMDataset();
            final List<PAMRasterBand> samplePamRasterBands = samplePam.getPAMRasterBand();
            final int numBands = samplePamRasterBands.size();
            List<PAMRasterBand> pamRasterBands = merged.getPAMRasterBand();
            PAMRasterBand sampleBand = samplePamRasterBands.get(0);
            List<MDI> sampleMetadata = sampleBand.getMetadata().getMDI();
            for (int i = 0; i < numBands; i++) {
                final PAMRasterBand band = new PAMRasterBand();
                final Metadata metadata = new Metadata();
                List<MDI> mdiList = metadata.getMDI();
                for (MDI mdi : sampleMetadata) {
                    MDI addedMdi = new MDI();
                    addedMdi.setKey(mdi.getKey());
                    mdiList.add(addedMdi);
                }
                band.setMetadata(metadata);
                pamRasterBands.add(band);
            }
        }
        return merged;
    }

    public static IOFileFilter getCleanupFilter() {
        return CLEANUP_FILTER;
    }

    public static void fixH2MVCCParam(Map<String, Serializable> params) {
        if (params != null) {
            // H2 database URLs must not be percent-encoded: see GEOT-4262.
            params.put(MVCC_KEY, true);
        }
    }

    public static void fixPostgisDBCreationParams(Map<String, Serializable> datastoreParams) {
        datastoreParams.put("create database", true);
    }

    public static ImageReaderSpi getReaderSpiFromStream(
            ImageReaderSpi suggestedSPI, ImageInputStream inStream) throws IOException {
        ImageReaderSpi readerSPI = null;
        // get a reader and try to cache the suggested SPI first
        inStream.mark();
        if (suggestedSPI != null && suggestedSPI.canDecodeInput(inStream)) {
            readerSPI = suggestedSPI;
            inStream.reset();
        } else {
            inStream.mark();
            ImageReader reader = ImageIOExt.getImageioReader(inStream);
            if (reader != null) readerSPI = reader.getOriginatingProvider();
            inStream.reset();
        }
        return readerSPI;
    }

    public static ImageInputStreamSpi getInputStreamSPIFromURL(URL granuleUrl) throws IOException {

        ImageInputStreamSpi streamSPI = ImageIOExt.getImageInputStreamSPI(granuleUrl, true);
        if (streamSPI == null) {
            final File file = URLs.urlToFile(granuleUrl);
            if (file != null) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, Utils.getFileInfo(file));
                }
            }
            throw new IllegalArgumentException(
                    "Unable to get an input stream for the provided granule "
                            + granuleUrl.toString());
        }
        return streamSPI;
    }

    /** Extract the palette from an {@link IndexColorModel}. */
    public static byte[][] extractPalette(IndexColorModel indexColorModel) {
        Utilities.ensureNonNull("indexColorModel", indexColorModel);
        byte[][] palette = new byte[3][indexColorModel.getMapSize()];
        int numBands = indexColorModel.getNumColorComponents();

        indexColorModel.getReds(palette[0]);
        indexColorModel.getGreens(palette[0]);
        indexColorModel.getBlues(palette[0]);
        if (numBands == 4) {
            indexColorModel.getAlphas(palette[0]);
        }
        return palette;
    }

    /** Returns true if the type is usable as a mosaic index */
    public static boolean isValidMosaicSchema(
            SimpleFeatureType schema, String locationAttributeName) {
        // does it have a geometry?
        if (schema == null || schema.getGeometryDescriptor() == null) {
            return false;
        }

        // does it have the location property?
        AttributeDescriptor location = schema.getDescriptor(locationAttributeName);
        return location != null
                && CharSequence.class.isAssignableFrom(location.getType().getBinding());
    }

    public static ReferencedEnvelope reprojectEnvelope(
            ReferencedEnvelope sourceEnvelope,
            CoordinateReferenceSystem targetCRS,
            ReferencedEnvelope targetReferenceEnvelope)
            throws FactoryException, TransformException {
        Geometry reprojected =
                Utils.reprojectEnvelopeToGeometry(
                        sourceEnvelope, targetCRS, targetReferenceEnvelope);
        if (reprojected == null) {
            return new ReferencedEnvelope(targetCRS);
        } else {
            if (reprojected.getNumGeometries() > 1) {
                return new ReferencedEnvelope(
                        reprojected.getGeometryN(0).getEnvelopeInternal(), targetCRS);
            } else {
                return new ReferencedEnvelope(reprojected.getEnvelopeInternal(), targetCRS);
            }
        }
    }

    /**
     * Reprojects an envelope using the {@link ProjectionHandler} machinery. The output can be a
     * multipolygon in case of wrapping, which might or might not be what you want, act accordingly
     */
    public static Geometry reprojectEnvelopeToGeometry(
            ReferencedEnvelope sourceEnvelope,
            CoordinateReferenceSystem targetCRS,
            ReferencedEnvelope targetReferenceEnvelope)
            throws FactoryException, TransformException {
        ProjectionHandler handler;
        CoordinateReferenceSystem sourceCRS = sourceEnvelope.getCoordinateReferenceSystem();
        if (targetReferenceEnvelope == null) {
            targetReferenceEnvelope = ReferencedEnvelope.reference(getCRSEnvelope(targetCRS));
        }
        if (targetReferenceEnvelope != null) {
            handler = ProjectionHandlerFinder.getHandler(targetReferenceEnvelope, sourceCRS, true);
        } else {
            // cannot handle wrapping if we do not have a reference envelope, but
            // cutting/adapting will still work
            ReferencedEnvelope reference = new ReferencedEnvelope(targetCRS);
            handler = ProjectionHandlerFinder.getHandler(reference, sourceCRS, false);
        }

        if (handler != null) {
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
            Geometry footprint = JTS.toGeometry(sourceEnvelope);
            Geometry preProcessed = handler.preProcess(footprint);
            if (preProcessed == null) {
                return null;
            }
            Geometry transformed = JTS.transform(preProcessed, transform);
            // this might generate multipolygons for data crossing the dataline, which
            // is actually what we want
            Geometry postProcessed = handler.postProcess(transform.inverse(), transformed);
            if (postProcessed == null) {
                return null;
            }
            return postProcessed;
        } else {
            sourceEnvelope = new ReferencedEnvelope(CRS.transform(sourceEnvelope, targetCRS));
        }

        return JTS.toGeometry(sourceEnvelope);
    }

    private static org.opengis.geometry.Envelope getCRSEnvelope(CoordinateReferenceSystem targetCRS)
            throws FactoryException, NoSuchAuthorityCodeException {
        if (targetCRS.getDomainOfValidity() == null) {
            Integer code = CRS.lookupEpsgCode(targetCRS, true);
            if (code != null) {
                CRS.decode("EPSG:" + code, CRS.getAxisOrder(targetCRS) != AxisOrder.NORTH_EAST);
            }
        }
        org.opengis.geometry.Envelope envelope = CRS.getEnvelope(targetCRS);
        return envelope;
    }

    /** Returns the File associated to the rootFolder if not absolute */
    public static File getFile(String strValue, File rootFolder) throws IOException {
        File file = new File(strValue);
        if (!file.isAbsolute()) {
            file = new File(rootFolder, strValue);
        }
        return file;
    }

    /** Decimate a geometry (reducing the number of vertices) for incoming buffering */
    public static Geometry decimate(Geometry geometry) {
        Coordinate[] coordinates = geometry.getCoordinates();
        if (coordinates.length <= Utils.COORDS_DECIMATION_THRESHOLD) {
            return geometry;
        }

        Geometry g2 = LiteCoordinateSequence.cloneGeometry(geometry, 2);
        Decimator decimator =
                new Decimator(
                        DEFAULT_LINESTRING_DECIMATION_SPAN, DEFAULT_LINESTRING_DECIMATION_SPAN);
        decimator.decimate(g2);
        g2.geometryChanged();
        return g2;
    }

    /**
     * Intersects a ROI with a ROI geometry, with fallback on GeometrySnapper if a TopologyException
     * occurs
     */
    public static ROI roiIntersect(ROI roi, ROIGeometry roiGeometry, RenderingHints hints) {
        try {
            roi = roi.intersect(roiGeometry);
        } catch (TopologyException tpe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "Topology Exception occurred while producing the intersecting ROI"
                                + "\nTrying with a GeometrySnapper approach. "
                                + tpe.getLocalizedMessage());
            }

            Geometry g = roiGeometry.getAsGeometry();
            boolean isValid = g.isValid();
            // Try intersection with a GeometrySnapper approach
            if (!isValid) {
                double tol = GeometrySnapper.computeOverlaySnapTolerance(g);
                g = new GeometrySnapper(g).snapTo(g, tol).buffer(0);
                roiGeometry = new ROIGeometry(g, setupJAIHints(hints));
                roi = roi.intersect(roiGeometry);
            } else if (roi instanceof ROIGeometry) {
                g = ((ROIGeometry) roi).getAsGeometry();
                isValid = g.isValid();
                if (!isValid) {
                    double tol = GeometrySnapper.computeOverlaySnapTolerance(g);
                    g = new GeometrySnapper(g).snapTo(g, tol).buffer(0);
                    roi = new ROIGeometry(g, setupJAIHints(hints));
                    roi = roi.intersect(roiGeometry);
                }
            }
        }
        return roi;
    }
}
