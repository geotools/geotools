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

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.spi.ImageReaderSpi;
import javax.media.jai.ImageLayout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.DefaultProgressListener;
import org.geotools.feature.collection.AbstractFeatureVisitor;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.acceptors.DefaultGranuleAcceptorFactory;
import org.geotools.gce.imagemosaic.acceptors.GranuleAcceptor;
import org.geotools.gce.imagemosaic.acceptors.GranuleAcceptorFactorySPI;
import org.geotools.gce.imagemosaic.acceptors.GranuleAcceptorFactorySPIFinder;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogFactory;
import org.geotools.gce.imagemosaic.catalog.MultiLevelROIProviderMosaicFactory;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors.Collector;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.gce.imagemosaic.catalog.index.SchemasType;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.gce.imagemosaic.catalogbuilder.MosaicBeanBuilder;
import org.geotools.gce.imagemosaic.granulehandler.DefaultGranuleHandler;
import org.geotools.gce.imagemosaic.granulehandler.GranuleHandler;
import org.geotools.gce.imagemosaic.granulehandler.GranuleHandlerFactoryFinder;
import org.geotools.gce.imagemosaic.granulehandler.GranuleHandlerFactorySPI;
import org.geotools.gce.imagemosaic.granulehandler.GranuleHandlingException;
import org.geotools.gce.imagemosaic.namecollector.DefaultCoverageNameCollectorSPI;
import org.geotools.gce.imagemosaic.properties.CRSExtractor;
import org.geotools.gce.imagemosaic.properties.DefaultPropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * This class is in responsible for creating and managing the catalog and the configuration of the
 * mosaic
 *
 * @author Carlo Cancellieri - GeoSolutions SAS
 */
public class ImageMosaicConfigHandler {

    /** Default Logger * */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ImageMosaicConfigHandler.class);

    /*
     * Used to check if we can use memory mapped buffers safely. In case the OS cannot be detected, we act as if it was Windows and do not use memory
     * mapped buffers
     */
    private static final Boolean USE_MEMORY_MAPPED_BUFFERS =
            !System.getProperty("os.name", "Windows").contains("Windows");

    private List<PropertiesCollector> propertiesCollectors = null;

    private Map<String, MosaicConfigurationBean> configurations = new HashMap<>();

    /**
     * Proper way to stop a thread is not by calling Thread.stop() but by using a shared variable
     * that can be checked in order to notify a terminating condition.
     */
    private volatile boolean stop = false;

    protected GranuleCatalog catalog;

    private CatalogBuilderConfiguration runConfiguration;

    private ImageReaderSpi cachedReaderSPI;

    private ReferencedEnvelope imposedBBox;

    private ImageMosaicReader parentReader;

    private File indexerFile;

    private File parent;

    private ImageMosaicEventHandlers eventHandler;

    private boolean useExistingSchema;

    private List<GranuleAcceptor> granuleAcceptors = new ArrayList<>();

    private GranuleHandler granuleHandler = new DefaultGranuleHandler();

    private CoverageNameHandler coverageNameHandler =
            new CoverageNameHandler(new DefaultCoverageNameCollectorSPI());

    /** Default constructor */
    @SuppressFBWarnings("NP_NULL_PARAM_DEREF")
    public ImageMosaicConfigHandler(
            final CatalogBuilderConfiguration configuration,
            final ImageMosaicEventHandlers eventHandler) {
        Utilities.ensureNonNull("runConfiguration", configuration);
        Utilities.ensureNonNull("eventHandler", eventHandler);
        this.eventHandler = eventHandler;

        Indexer defaultIndexer = configuration.getIndexer();
        ParametersType params = null;
        String rootMosaicDir = null;
        if (defaultIndexer != null) {
            params = defaultIndexer.getParameters();
            rootMosaicDir = IndexerUtils.getParam(params, Prop.ROOT_MOSAIC_DIR);
        }

        Utilities.ensureNonNull("root location", rootMosaicDir);

        // look for and indexer.properties file
        parent = new File(rootMosaicDir);
        Indexer indexer = IndexerUtils.initializeIndexer(params, parent);
        if (indexer != null) {
            indexerFile = indexer.getIndexerFile();
        }

        Hints hints = configuration.getHints();
        String ancillaryFile = null;
        String datastoreFile = null;

        if (indexer != null) {
            // Overwrite default indexer only when indexer is available
            configuration.setIndexer(indexer);
            String auxiliaryFileParam =
                    IndexerUtils.getParameter(Utils.Prop.AUXILIARY_FILE, indexer);
            if (auxiliaryFileParam != null) {
                ancillaryFile = auxiliaryFileParam;
            }
            String datastoreFileParam =
                    IndexerUtils.getParameter(Utils.Prop.AUXILIARY_DATASTORE_FILE, indexer);
            if (datastoreFileParam != null) {
                datastoreFile = datastoreFileParam;
            }
            if (datastoreFileParam != null || auxiliaryFileParam != null) {
                setReader(hints, false);
            }
            if (IndexerUtils.getParameterAsBoolean(Utils.Prop.USE_EXISTING_SCHEMA, indexer)) {
                this.useExistingSchema = true;
            }
        }

        initializeGranuleAcceptors(indexer);
        initializeGranuleHandler(indexer);
        initializeCoverageNameHandler(indexer);

        updateConfigurationHints(
                configuration,
                hints,
                ancillaryFile,
                datastoreFile,
                IndexerUtils.getParam(params, Prop.ROOT_MOSAIC_DIR));

        // check config
        configuration.check();

        this.runConfiguration = new CatalogBuilderConfiguration(configuration);
    }

    /**
     * Initialize the list of granule collectors from the indexer.
     *
     * @param indexer the indexer configuration
     */
    private void initializeGranuleAcceptors(Indexer indexer) {
        // initialized granuleAcceptors with empty list

        // do we wnat/need something different
        if (indexer != null) {
            String granuleAcceptorsString =
                    IndexerUtils.getParameter(Prop.GRANULE_ACCEPTORS, indexer);
            if (granuleAcceptorsString != null && granuleAcceptorsString.length() > 0) {
                // parsing indicated granule acceptors
                Map<String, GranuleAcceptorFactorySPI> granuleAcceptorsMap =
                        GranuleAcceptorFactorySPIFinder.getGranuleAcceptorFactorySPI();
                Arrays.stream(granuleAcceptorsString.split(","))
                        .forEach(
                                (factoryImpl) -> {
                                    if (granuleAcceptorsMap.containsKey(factoryImpl)) {
                                        granuleAcceptors.addAll(
                                                granuleAcceptorsMap.get(factoryImpl).create());
                                    }
                                });
            }
        }

        // if we didn't find any granule acceptors in the index configuration, use the default
        if (granuleAcceptors.isEmpty()) {
            granuleAcceptors.addAll(new DefaultGranuleAcceptorFactory().create());
        }
    }

    private void initializeGranuleHandler(Indexer indexer) {

        // we initialized at construction time with the default handler

        // ok, do we need/want something different?
        if (indexer != null) {
            String granuleHandlerString = IndexerUtils.getParameter(Prop.GEOMETRY_HANDLER, indexer);
            if (granuleHandlerString != null && granuleHandlerString.length() > 0) {
                GranuleHandlerFactorySPI factory =
                        GranuleHandlerFactoryFinder.getGranuleHandlersSPI()
                                .get(granuleHandlerString);
                if (factory != null) {
                    granuleHandler = factory.create();
                }
            }
        }
    }

    private void initializeCoverageNameHandler(Indexer indexer) {
        // we initialized at construction time with the default handler
        // ok, do we need/want something different?
        if (indexer != null) {
            String coverageNameCollectorString =
                    IndexerUtils.getParameter(Prop.COVERAGE_NAME_COLLECTOR_SPI, indexer);
            if (coverageNameCollectorString != null && coverageNameCollectorString.length() > 0) {
                // Override default handling machinery
                coverageNameHandler = new CoverageNameHandler(coverageNameCollectorString);
            }
        }
    }

    /**
     * Create or load a GranuleCatalog on top of the provided configuration
     *
     * @param runConfiguration configuration to be used
     * @param create if true create a new catalog, otherwise it is loaded
     * @return a new GranuleCatalog built from the configuration
     */
    private GranuleCatalog createCatalog(
            CatalogBuilderConfiguration runConfiguration, boolean create) throws IOException {
        //
        // create the index
        //
        // do we have a datastore.properties file?
        final File parent = new File(runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR));

        GranuleCatalog catalog;

        // Consider checking that from the indexer if any
        final File datastoreProperties = new File(parent, "datastore.properties");
        // GranuleCatalog catalog = null;
        if (Utils.checkFileReadable(datastoreProperties)) {
            // read the properties file
            Properties properties = createGranuleCatalogProperties(datastoreProperties);
            // pass the typename from the indexer, if one is available
            String indexerTypeName = runConfiguration.getParameter(Prop.TYPENAME);
            if (indexerTypeName != null && properties.getProperty(Prop.TYPENAME) == null) {
                properties.put(Prop.TYPENAME, indexerTypeName);
            }
            catalog =
                    createGranuleCatalogFromDatastore(
                            parent,
                            properties,
                            create,
                            Boolean.parseBoolean(runConfiguration.getParameter(Prop.WRAP_STORE)),
                            runConfiguration.getHints());
        } else {

            // we do not have a datastore properties file therefore we continue with a shapefile
            // datastore
            final URL file =
                    URLs.fileToUrl(
                            new File(
                                    parent,
                                    runConfiguration.getParameter(Prop.INDEX_NAME) + ".shp"));
            final Properties params = new Properties();
            params.put(ShapefileDataStoreFactory.URLP.key, file);
            if (file.getProtocol().equalsIgnoreCase("file")) {
                params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
            }
            params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, USE_MEMORY_MAPPED_BUFFERS);
            params.put(ShapefileDataStoreFactory.DBFTIMEZONE.key, TimeZone.getTimeZone("UTC"));
            params.put(
                    Prop.LOCATION_ATTRIBUTE,
                    runConfiguration.getParameter(Prop.LOCATION_ATTRIBUTE));
            catalog =
                    GranuleCatalogFactory.createGranuleCatalog(
                            params, false, create, Utils.SHAPE_SPI, runConfiguration.getHints());
            MultiLevelROIProvider roi =
                    MultiLevelROIProviderMosaicFactory.createFootprintProvider(
                            parent, runConfiguration.getHints());
            catalog.setMultiScaleROIProvider(roi);
        }

        return catalog;
    }

    static Properties createGranuleCatalogProperties(File datastoreProperties) throws IOException {
        Properties properties =
                CoverageUtilities.loadPropertiesFromURL(URLs.fileToUrl(datastoreProperties));
        if (properties == null) {
            throw new IOException(
                    "Unable to load properties from:" + datastoreProperties.getAbsolutePath());
        }
        return properties;
    }

    static GranuleCatalog createGranuleCatalogFromDatastore(
            File parent, File datastoreProperties, boolean create, Hints hints) throws IOException {
        return createGranuleCatalogFromDatastore(parent, datastoreProperties, create, false, hints);
    }

    /** Create a granule catalog from a datastore properties file */
    private static GranuleCatalog createGranuleCatalogFromDatastore(
            File parent, File datastoreProperties, boolean create, boolean wraps, Hints hints)
            throws IOException {
        Utilities.ensureNonNull("datastoreProperties", datastoreProperties);
        Properties properties = createGranuleCatalogProperties(datastoreProperties);
        return createGranuleCatalogFromDatastore(parent, properties, create, wraps, hints);
    }

    private static GranuleCatalog createGranuleCatalogFromDatastore(
            File parent, Properties properties, boolean create, boolean wraps, Hints hints)
            throws IOException {
        GranuleCatalog catalog = null;
        // SPI
        final String SPIClass = properties.getProperty("SPI");
        DataStoreFactorySpi spi = null;
        try {
            if (SPIClass == null) {
                if (properties.get(Utils.Prop.STORE_NAME) == null) {
                    throw new IllegalArgumentException(
                            "Required property SPI is missing from configuration");
                }
            } else {
                // create a datastore as instructed
                spi =
                        (DataStoreFactorySpi)
                                Class.forName(SPIClass).getDeclaredConstructor().newInstance();
            }
            // set ParentLocation parameter since for embedded database like H2 we must change the
            // database
            // to incorporate the path where to write the db
            properties.put("ParentLocation", URLs.fileToUrl(parent).toExternalForm());
            if (wraps) {
                properties.put(Prop.WRAP_STORE, wraps);
            }

            catalog =
                    GranuleCatalogFactory.createGranuleCatalog(
                            properties, false, create, spi, hints);
            MultiLevelROIProvider rois =
                    MultiLevelROIProviderMosaicFactory.createFootprintProvider(parent);
            catalog.setMultiScaleROIProvider(rois);

        } catch (Exception e) {
            final IOException ioe = new IOException();
            throw (IOException) ioe.initCause(e);
        }
        return catalog;
    }

    /**
     * Create a {@link SimpleFeatureType} from the specified configuration.
     *
     * @return the schema for the mosaic
     */
    private SimpleFeatureType createSchema(
            CatalogBuilderConfiguration runConfiguration,
            String name,
            CoordinateReferenceSystem actualCRS) {
        SimpleFeatureType indexSchema = null;
        SchemaType schema = null;
        String schemaAttributes = null;
        Indexer indexer = runConfiguration.getIndexer();
        if (indexer != null) {
            SchemasType schemas = indexer.getSchemas();
            Coverage coverage = IndexerUtils.getCoverage(indexer, name);
            if (coverage != null) {
                schema = IndexerUtils.getSchema(indexer, coverage);
            }
            if (schema != null) {
                schemaAttributes = schema.getAttributes();
            } else if (schemas != null) {
                List<SchemaType> schemaList = schemas.getSchema();
                // CHECK THAT
                if (!schemaList.isEmpty()) {
                    schemaAttributes = schemaList.get(0).getAttributes();
                }
            }
        }
        if (schemaAttributes == null) {
            schemaAttributes = runConfiguration.getSchema(name);
        }
        if (schemaAttributes != null) {
            schemaAttributes = schemaAttributes.trim();
            // get the schema
            try {
                indexSchema = DataUtilities.createType(name, schemaAttributes);
                // override the crs in case the provided one was wrong or absent
                indexSchema =
                        DataUtilities.createSubType(
                                indexSchema, DataUtilities.attributeNames(indexSchema), actualCRS);
                if (actualCRS != null) {
                    Set<ReferenceIdentifier> identifiers = actualCRS.getIdentifiers();
                    if (identifiers == null || identifiers.isEmpty()) {
                        Integer code = CRS.lookupEpsgCode(actualCRS, true);
                        int nativeSrid = code == null ? 0 : code;
                        GeometryDescriptor geometryDescriptor = indexSchema.getGeometryDescriptor();
                        if (geometryDescriptor != null) {
                            Map<Object, Object> userData = geometryDescriptor.getUserData();
                            userData.put(JDBCDataStore.JDBC_NATIVE_SRID, nativeSrid);
                        }
                    }
                }

            } catch (Throwable e) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                indexSchema = null;
            }
        }

        if (indexSchema == null) {
            // Proceed with default Schema
            final SimpleFeatureTypeBuilder featureBuilder = new SimpleFeatureTypeBuilder();
            String typeName = runConfiguration.getParameter(Prop.TYPENAME);
            featureBuilder.setName(typeName != null ? typeName : name);
            featureBuilder.setNamespaceURI("http://www.geo-solutions.it/");
            featureBuilder.add(
                    runConfiguration.getParameter(Prop.LOCATION_ATTRIBUTE).trim(), String.class);
            featureBuilder.add("the_geom", Polygon.class, actualCRS);
            featureBuilder.setDefaultGeometry("the_geom");
            String timeAttribute = runConfiguration.getTimeAttribute();
            addAttributes(timeAttribute, featureBuilder, Date.class);
            indexSchema = featureBuilder.buildFeatureType();
        }
        return indexSchema;
    }

    /**
     * Add splitted attributes to the featureBuilder
     *
     * @param classType TODO: Remove that once reworking on the dimension stuff
     */
    private static void addAttributes(
            String attribute, SimpleFeatureTypeBuilder featureBuilder, Class classType) {
        if (attribute != null) {
            if (!attribute.contains(Utils.RANGE_SPLITTER_CHAR)) {
                featureBuilder.add(attribute, classType);
            } else {
                String[] ranges = attribute.split(Utils.RANGE_SPLITTER_CHAR);
                if (ranges.length != 2) {
                    throw new IllegalArgumentException(
                            "All ranges attribute need to be composed of a maximum of 2 elements:\n"
                                    + "As an instance (min;max) or (low;high) or (begin;end) , ...");
                } else {
                    featureBuilder.add(ranges[0], classType);
                    featureBuilder.add(ranges[1], classType);
                }
            }
        }
    }

    /**
     * Get a {@link GranuleSource} related to a specific coverageName from an inputReader and put
     * the related granules into a {@link GranuleStore} related to the same coverageName of the
     * mosaicReader.
     *
     * @param coverageName the name of the coverage to be managed
     * @param fileBeingProcessed the reference input file
     * @param inputReader the reader source of granules
     * @param mosaicReader the reader where to store source granules
     * @param configuration the configuration
     * @param envelope envelope of the granule being added
     * @param transaction transaction in progress
     * @param propertiesCollectors list of properties collectors to use
     */
    private void updateCatalog(
            final String coverageName,
            final File fileBeingProcessed,
            final GridCoverage2DReader inputReader,
            final ImageMosaicReader mosaicReader,
            final CatalogBuilderConfiguration configuration,
            final GeneralEnvelope envelope,
            final DefaultTransaction transaction,
            final List<PropertiesCollector> propertiesCollectors)
            throws IOException, GranuleHandlingException {

        // Retrieving the store and the destination schema
        final GranuleStore store = (GranuleStore) mosaicReader.getGranules(coverageName, false);
        if (store == null) {
            throw new IllegalArgumentException(
                    "No valid granule store has been found for: " + coverageName);
        }
        final SimpleFeatureType indexSchema = store.getSchema();
        final SimpleFeature feature =
                new ShapefileCompatibleFeature(DataUtilities.template(indexSchema));
        store.setTransaction(transaction);

        final ListFeatureCollection collection = new ListFeatureCollection(indexSchema);
        final String fileLocation = prepareLocation(configuration, fileBeingProcessed);
        final String locationAttribute = configuration.getParameter(Prop.LOCATION_ATTRIBUTE);
        MosaicConfigurationBean mosaicConfiguration = this.getConfigurations().get(coverageName);
        GranuleHandler geometryHandler = this.getGeometryHandler();

        // getting input granules
        if (inputReader instanceof StructuredGridCoverage2DReader) {
            //
            // Case A: input reader is a StructuredGridCoverage2DReader. We can get granules from a
            // source
            //
            handleStructuredGridCoverage(
                    ((StructuredGridCoverage2DReader) inputReader).getGranules(coverageName, true),
                    fileBeingProcessed,
                    inputReader,
                    propertiesCollectors,
                    indexSchema,
                    feature,
                    collection,
                    fileLocation,
                    locationAttribute,
                    mosaicConfiguration);

        } else {
            //
            // Case B: old style reader, proceed with classic way, using properties collectors
            //
            geometryHandler.handleGranule(
                    fileBeingProcessed,
                    inputReader,
                    feature,
                    indexSchema,
                    null,
                    null,
                    mosaicConfiguration);
            feature.setAttribute(locationAttribute, fileLocation);

            updateAttributesFromCollectors(
                    feature, fileBeingProcessed, inputReader, propertiesCollectors);
            collection.add(feature);
        }

        // drop all the granules associated to the same
        Filter filter =
                Utils.FF.equal(
                        Utils.FF.property(locationAttribute),
                        Utils.FF.literal(fileLocation),
                        !isCaseSensitiveFileSystem(fileBeingProcessed));
        store.removeGranules(filter);

        // Add the granules collection to the store
        store.addGranules(collection);
    }

    private void handleStructuredGridCoverage(
            GranuleSource granules,
            final File fileBeingProcessed,
            final GridCoverage2DReader inputReader,
            final List<PropertiesCollector> propertiesCollectors,
            final SimpleFeatureType indexSchema,
            SimpleFeature feature,
            final ListFeatureCollection collection,
            final String fileLocation,
            final String locationAttribute,
            final MosaicConfigurationBean mosaicConfiguration)
            throws IOException {

        // Getting granule source and its input granules
        final GranuleSource source = granules;
        final SimpleFeatureCollection originCollection = source.getGranules(null);
        final DefaultProgressListener listener = new DefaultProgressListener();

        // Getting attributes structure to be filled
        final Collection<Property> destProps = feature.getProperties();
        final Set<Name> destAttributes = new HashSet<>();
        for (Property prop : destProps) {
            destAttributes.add(prop.getName());
        }

        // Collecting granules
        final GranuleHandler geometryHandler = this.granuleHandler;
        originCollection.accepts(
                new AbstractFeatureVisitor() {
                    public void visit(Feature feature) {
                        if (feature instanceof SimpleFeature) {
                            // get the feature
                            final SimpleFeature sourceFeature = (SimpleFeature) feature;
                            final SimpleFeature destFeature = DataUtilities.template(indexSchema);
                            Collection<Property> props = sourceFeature.getProperties();
                            Name propName = null;
                            Object propValue = null;

                            // Assigning value to dest feature for matching attributes
                            for (Property prop : props) {
                                Name geometryName =
                                        sourceFeature
                                                .getFeatureType()
                                                .getGeometryDescriptor()
                                                .getName();
                                if (prop.getName().equals(geometryName)) {
                                    try {
                                        geometryHandler.handleGranule(
                                                fileBeingProcessed,
                                                (StructuredGridCoverage2DReader) inputReader,
                                                destFeature,
                                                destFeature.getFeatureType(),
                                                sourceFeature,
                                                sourceFeature.getFeatureType(),
                                                mosaicConfiguration);
                                    } catch (GranuleHandlingException e) {
                                        throw new RuntimeException(
                                                "Error handling structured coverage granule", e);
                                    }
                                } else {
                                    propName = prop.getName();
                                    propValue = prop.getValue();

                                    // Matching attributes are set
                                    if (destAttributes.contains(propName)) {
                                        destFeature.setAttribute(propName, propValue);
                                    }
                                }
                            }

                            // Set location
                            destFeature.setAttribute(locationAttribute, fileLocation);

                            // delegate remaining attributes set to properties collector
                            updateAttributesFromCollectors(
                                    destFeature,
                                    fileBeingProcessed,
                                    inputReader,
                                    propertiesCollectors);
                            collection.add(destFeature);

                            // check if something bad occurred
                            if (listener.isCanceled() || listener.hasExceptions()) {
                                if (listener.hasExceptions())
                                    throw new RuntimeException(listener.getExceptions().peek());
                                else
                                    throw new IllegalStateException(
                                            "Feature visitor has been canceled");
                            }
                        }
                    }
                },
                listener);
    }

    private GranuleHandler getGeometryHandler() {
        return this.granuleHandler;
    }

    /**
     * Checks if the file system is case sensitive or not using File.exists (the only method that
     * also works on OSX too according to
     * http://stackoverflow.com/questions/1288102/how-do-i-detect-whether-the-file-system-is-case-sensitive
     * )
     */
    private static boolean isCaseSensitiveFileSystem(File fileBeingProcessed) {
        File loCase =
                new File(
                        fileBeingProcessed.getParentFile(),
                        fileBeingProcessed.getName().toLowerCase());
        File upCase =
                new File(
                        fileBeingProcessed.getParentFile(),
                        fileBeingProcessed.getName().toUpperCase());
        return loCase.exists() && upCase.exists();
    }

    /** Update feature attributes through properties collector */
    private static void updateAttributesFromCollectors(
            final SimpleFeature feature,
            final File fileBeingProcessed,
            final GridCoverage2DReader inputReader,
            final List<PropertiesCollector> propertiesCollectors) {
        // collect and dump properties
        if (propertiesCollectors != null && propertiesCollectors.size() > 0)
            for (PropertiesCollector pc : propertiesCollectors) {
                pc.collect(fileBeingProcessed).collect(inputReader).setProperties(feature);
                pc.reset();
            }
    }

    /** Prepare the location on top of the configuration and file to be processed. */
    private static String prepareLocation(
            CatalogBuilderConfiguration runConfiguration, final File fileBeingProcessed)
            throws IOException {
        // absolute
        String pathType = runConfiguration.getParameter(Prop.PATH_TYPE);
        String absolutePath = runConfiguration.getParameter(Prop.ABSOLUTE_PATH);
        if (Boolean.valueOf(absolutePath) || PathType.ABSOLUTE.name().equals(pathType)) {
            return fileBeingProcessed.getAbsolutePath();
        }

        // relative (harvesting of PathType.URL is not supported)
        String targetPath = fileBeingProcessed.getCanonicalPath();
        String basePath = runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR);
        String relative =
                getRelativePath(
                        targetPath,
                        basePath,
                        File.separator); // TODO: Remove this replace after fixing the quote
        // escaping
        return relative;
    }

    /**
     * Get the relative path from one file to another, specifying the directory separator. If one of
     * the provided resources does not exist, it is assumed to be a file unless it ends with '/' or
     * '\'.
     *
     * @param targetPath targetPath is calculated to this file
     * @param basePath basePath is calculated from this file
     * @param pathSeparator directory separator. The platform default is not assumed so that we can
     *     test Unix behaviour when running on Windows (for example)
     */
    private static String getRelativePath(
            String targetPath, String basePath, String pathSeparator) {

        // Normalize the paths
        String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
        String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

        // Undo the changes to the separators made by normalization
        if (pathSeparator.equals("/")) {
            normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);

        } else if (pathSeparator.equals("\\")) {
            normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);

        } else {
            throw new IllegalArgumentException(
                    "Unrecognised dir separator '" + pathSeparator + "'");
        }

        String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
        String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        StringBuilder common = new StringBuilder();

        int commonIndex = 0;
        while (commonIndex < target.length
                && commonIndex < base.length
                && target[commonIndex].equals(base[commonIndex])) {
            common.append(target[commonIndex] + pathSeparator);
            commonIndex++;
        }

        if (commonIndex == 0) {
            // No single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized.
            throw new RuntimeException(
                    "No common path element found for '"
                            + normalizedTargetPath
                            + "' and '"
                            + normalizedBasePath
                            + "'");
        }

        // The number of directories we have to backtrack depends on whether the base is a file or a
        // dir
        // For example, the relative path from
        //
        // /foo/bar/baz/gg/ff to /foo/bar/baz
        //
        // ".." if ff is a file
        // "../.." if ff is a directory
        //
        // The following is a heuristic to figure out if the base refers to a file or dir. It's not
        // perfect, because
        // the resource referred to by this path may not actually exist, but it's the best I can do
        boolean baseIsFile = true;

        File baseResource = new File(normalizedBasePath);

        if (baseResource.exists()) {
            baseIsFile = baseResource.isFile();

        } else if (basePath.endsWith(pathSeparator)) {
            baseIsFile = false;
        }

        StringBuilder relative = new StringBuilder();

        if (base.length != commonIndex) {
            int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

            for (int i = 0; i < numDirsUp; i++) {
                relative.append(".." + pathSeparator);
            }
        }
        relative.append(normalizedTargetPath.substring(common.length()));
        return relative.toString();
    }

    /**
     * Make sure a proper type name is specified in the catalogBean, it will be used to create the
     * {@link GranuleCatalog}
     */
    private static void checkTypeName(URL sourceURL, MosaicConfigurationBean configuration)
            throws IOException {
        CatalogConfigurationBean catalogBean = configuration.getCatalogConfigurationBean();
        if (catalogBean.getTypeName() == null) {
            if (sourceURL.getPath().endsWith("shp")) {
                // In case we didn't find a typeName and we are dealing with a shape index,
                // we set the typeName as the shape name
                final File file = URLs.urlToFile(sourceURL);
                catalogBean.setTypeName(FilenameUtils.getBaseName(file.getCanonicalPath()));
            } else {
                // use the default "mosaic" name
                catalogBean.setTypeName("mosaic");
            }
        }
    }

    /** Create a {@link GranuleCatalog} on top of the provided Configuration */
    static GranuleCatalog createCatalog(
            final URL sourceURL, final MosaicConfigurationBean configuration, Hints hints)
            throws IOException {
        CatalogConfigurationBean catalogBean = configuration.getCatalogConfigurationBean();

        // Check the typeName
        checkTypeName(sourceURL, configuration);
        if (hints != null && hints.containsKey(Hints.MOSAIC_LOCATION_ATTRIBUTE)) {
            final String hintLocation = (String) hints.get(Hints.MOSAIC_LOCATION_ATTRIBUTE);
            if (!catalogBean.getLocationAttribute().equalsIgnoreCase(hintLocation)) {
                throw new DataSourceException("wrong location attribute");
            }
        }
        // Create the catalog
        GranuleCatalog catalog =
                GranuleCatalogFactory.createGranuleCatalog(sourceURL, catalogBean, null, hints);
        File parent = URLs.urlToFile(sourceURL).getParentFile();
        MultiLevelROIProvider rois =
                MultiLevelROIProviderMosaicFactory.createFootprintProvider(parent);
        catalog.setMultiScaleROIProvider(rois);

        return catalog;
    }

    private ImageMosaicReader getImageMosaicReader(Hints hints) {
        ImageMosaicReader imReader = null;
        if (hints != null && hints.containsKey(Utils.MOSAIC_READER)) {
            Object reader = hints.get(Utils.MOSAIC_READER);
            if (reader instanceof ImageMosaicReader) {
                if (getParentReader() == null) {
                    setParentReader((ImageMosaicReader) reader);
                }
                imReader = (ImageMosaicReader) reader;
            }
        }
        return imReader;
    }

    private void setReader(Hints hints, final boolean updateHints) {
        ImageMosaicReader reader = getImageMosaicReader(hints);
        if (reader != null && updateHints) {
            Hints readerHints = reader.getHints();
            readerHints.add(hints);
        }
    }

    private Hints updateRepositoryHints(CatalogBuilderConfiguration configuration, Hints hints) {
        ImageMosaicReader reader = getImageMosaicReader(hints);
        if (reader != null) {
            Hints readerHints = reader.getHints();
            if (readerHints != null && readerHints.containsKey(Hints.REPOSITORY)) {
                hints.add(new Hints(Hints.REPOSITORY, readerHints.get(Hints.REPOSITORY)));
            }
        }
        return hints;
    }

    private void updateConfigurationHints(
            final CatalogBuilderConfiguration configuration,
            Hints hints,
            final String ancillaryFile,
            final String datastoreFile,
            final String rootMosaicDir) {
        final boolean isAbsolutePath =
                Boolean.parseBoolean(configuration.getParameter(Prop.ABSOLUTE_PATH));
        hints =
                updateHints(
                        ancillaryFile,
                        isAbsolutePath,
                        rootMosaicDir,
                        configuration,
                        hints,
                        Utils.AUXILIARY_FILES_PATH);
        hints =
                updateHints(
                        datastoreFile,
                        isAbsolutePath,
                        rootMosaicDir,
                        configuration,
                        hints,
                        Utils.AUXILIARY_DATASTORE_PATH);
        hints = updateRepositoryHints(configuration, hints);
        setReader(hints, true);
    }

    private Hints updateHints(
            String filePath,
            boolean isAbsolutePath,
            String rootMosaicDir,
            CatalogBuilderConfiguration configuration,
            Hints hints,
            Key key) {
        String updatedFilePath = null;
        if (filePath != null) {
            if (isAbsolutePath && !filePath.startsWith(rootMosaicDir)) {
                updatedFilePath = rootMosaicDir + File.separatorChar + filePath;
            } else {
                updatedFilePath = filePath;
            }

            if (hints != null) {
                hints.put(key, updatedFilePath);
            } else {
                hints = new Hints(key, updatedFilePath);
                configuration.setHints(hints);
            }
            if (!isAbsolutePath) {
                hints.put(Utils.PARENT_DIR, rootMosaicDir);
            }
        }
        return hints;
    }

    /**
     * Perform proper clean up.
     *
     * <p>Make sure to call this method when you are not running the {@link
     * ImageMosaicConfigHandler} or bad things can happen. If it is running, please stop it first.
     */
    public void reset() {
        eventHandler.removeAllProcessingEventListeners();
        // clear stop
        stop = false;

        // fileIndex = 0;
        runConfiguration = null;

        this.catalog.dispose();
    }

    public boolean getStop() {
        return stop;
    }

    public void stop() {
        stop = true;
    }

    void indexingPreamble() throws IOException {

        this.catalog = buildCatalog();

        //
        // IMPOSED ENVELOPE
        //
        String bbox = runConfiguration.getParameter(Prop.ENVELOPE2D);
        try {
            this.imposedBBox = Utils.parseEnvelope(bbox);
        } catch (Exception e) {
            this.imposedBBox = null;
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, "Unable to parse imposed bbox", e);
        }

        //
        // load property collectors
        //
        loadPropertyCollectors();
    }

    protected GranuleCatalog buildCatalog() throws IOException {
        GranuleCatalog catalog = createCatalog(runConfiguration, !useExistingSchema);
        getParentReader().granuleCatalog = catalog;
        return catalog;
    }

    /** Load properties collectors from the configuration */
    private void loadPropertyCollectors() {
        // load property collectors
        Indexer indexer = runConfiguration.getIndexer();
        Collectors collectors = indexer.getCollectors();
        // check whether this indexer allows heterogeneous CRS, then we know we need the CRS
        // collector
        Boolean heterogeneousCRS =
                Boolean.valueOf(IndexerUtils.getParameter(Prop.HETEROGENEOUS_CRS, indexer));
        if (collectors == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No properties collector have been found");
            }

            if (heterogeneousCRS) {
                this.propertiesCollectors = Collections.singletonList(new CRSExtractor());
                for (MosaicConfigurationBean configuration : configurations.values()) {
                    configuration.getCatalogConfigurationBean().setHeterogeneousCRS(true);
                }
            }
            return;
        }
        List<Collector> collectorList = collectors.getCollector();

        // load the SPI set
        final Set<PropertiesCollectorSPI> pcSPIs =
                PropertiesCollectorFinder.getPropertiesCollectorSPI();

        // parse the string
        final List<PropertiesCollector> pcs = new ArrayList<>();
        boolean hasCRSCollector = false;
        for (Collector collector : collectorList) {
            PropertiesCollectorSPI selectedSPI = null;
            final String spiName = collector.getSpi();
            for (PropertiesCollectorSPI spi : pcSPIs) {
                if (spi.isAvailable() && spi.getName().equalsIgnoreCase(spiName)) {
                    selectedSPI = spi;
                    break;
                }
            }

            if (selectedSPI == null) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(
                            "Unable to find a PropertyCollector for this definition: " + spiName);
                }
                continue;
            }

            // property names
            String collectorValue = collector.getValue();
            String config = null;
            if (collectorValue != null) {
                if (!collectorValue.startsWith(DefaultPropertiesCollectorSPI.REGEX_PREFIX)) {
                    config = DefaultPropertiesCollectorSPI.REGEX_PREFIX + collector.getValue();
                } else {
                    config = collector.getValue();
                }
            }

            // create the PropertiesCollector
            final PropertiesCollector pc =
                    selectedSPI.create(config, Arrays.asList(collector.getMapped()));
            if (pc != null) {
                hasCRSCollector |= pc instanceof CRSExtractor;
                pcs.add(pc);
            } else {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Unable to create PropertyCollector");
                }
            }
        }

        if (heterogeneousCRS && !hasCRSCollector) {
            pcs.add(new CRSExtractor());
        }

        this.propertiesCollectors = pcs;
    }

    void indexingPostamble(final boolean success) throws IOException {
        // close shapefile elements
        if (success) {
            Indexer indexer = runConfiguration.getIndexer();
            boolean supportsEmpty = false;
            if (indexer != null) {
                supportsEmpty = IndexerUtils.getParameterAsBoolean(Prop.CAN_BE_EMPTY, indexer);
            }
            // complete initialization of mosaic configuration
            boolean haveConfigs = configurations != null && !configurations.isEmpty();
            if (haveConfigs || supportsEmpty) {

                // We did found some MosaicConfigurations
                Set<String> keys = configurations.keySet();
                int keySize = keys.size();
                if (haveConfigs || !supportsEmpty) {
                    final boolean useName = keySize > 1;
                    for (String key : keys) {
                        MosaicConfigurationBean mosaicConfiguration = configurations.get(key);
                        RasterManager manager = parentReader.getRasterManager(key);
                        manager.initialize(supportsEmpty);
                        // create sample image if the needed elements are available
                        createSampleImage(mosaicConfiguration, useName);
                        eventHandler.fireEvent(Level.INFO, "Creating final properties file ", 99.9);
                        createPropertiesFiles(mosaicConfiguration);
                    }
                }
                final String base = FilenameUtils.getName(parent.getAbsolutePath());
                // we create a root properties file if we have more than one coverage, or if the
                // one coverage does not have the default name
                if (supportsEmpty
                        || keySize > 1
                        || (keySize > 0 && !base.equals(keys.iterator().next()))) {
                    File mosaicFile = null;
                    File originFile = null;
                    if (indexerFile.getAbsolutePath().endsWith("xml")) {
                        mosaicFile =
                                new File(
                                        indexerFile
                                                .getAbsolutePath()
                                                .replace(
                                                        IndexerUtils.INDEXER_XML, (base + ".xml")));
                        originFile = indexerFile;
                    } else if (indexerFile.getAbsolutePath().endsWith("properties")) {
                        mosaicFile =
                                new File(
                                        indexerFile
                                                .getAbsolutePath()
                                                .replace(
                                                        IndexerUtils.INDEXER_PROPERTIES,
                                                        (base + ".properties")));
                        originFile = indexerFile;
                    } else {
                        final String source =
                                runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR)
                                        + File.separatorChar
                                        + configurations.get(keys.iterator().next()).getName()
                                        + ".properties";
                        mosaicFile =
                                new File(
                                        indexerFile
                                                .getAbsolutePath()
                                                .replace(
                                                        IndexerUtils.INDEXER_PROPERTIES,
                                                        (base + ".properties")));
                        originFile = new File(source);
                    }
                    if (!mosaicFile.exists()) {
                        FileUtils.copyFile(originFile, mosaicFile);
                    }
                }

                // processing information
                eventHandler.fireEvent(Level.FINE, "Done!!!", 100);

            } else {
                // processing information
                eventHandler.fireEvent(Level.FINE, "Nothing to process!!!", 100);
            }
        } else {
            // processing information
            eventHandler.fireEvent(Level.FINE, "Canceled!!!", 100);
        }
    }

    /** Store a sample image frmo which we can derive the default SM and CM */
    private void createSampleImage(
            final MosaicConfigurationBean mosaicConfiguration, final boolean useName) {
        // create a sample image to store SM and CM
        Utilities.ensureNonNull("mosaicConfiguration", mosaicConfiguration);
        String filePath = null;
        if (mosaicConfiguration.getSampleModel() != null
                && mosaicConfiguration.getColorModel() != null) {

            // sample image file
            // TODO: Consider revisit this using different name/folder
            final String baseName = runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR) + "/";
            filePath =
                    baseName
                            + (useName ? mosaicConfiguration.getName() : "")
                            + Utils.SAMPLE_IMAGE_NAME;
            try {
                Utils.storeSampleImage(
                        new File(filePath),
                        mosaicConfiguration.getSampleModel(),
                        mosaicConfiguration.getColorModel());
            } catch (IOException e) {
                eventHandler.fireEvent(Level.SEVERE, e.getLocalizedMessage(), 0);
            }
        }
    }

    /** Creates the final properties file. */
    private void createPropertiesFiles(MosaicConfigurationBean mosaicConfiguration) {

        //
        // FINAL STEP
        //
        // CREATING GENERAL INFO FILE
        //

        CatalogConfigurationBean catalogConfigurationBean =
                mosaicConfiguration.getCatalogConfigurationBean();

        // envelope
        final Properties properties = new Properties();
        properties.setProperty(
                Utils.Prop.PATH_TYPE, catalogConfigurationBean.getPathType().toString());
        properties.setProperty(
                Utils.Prop.LOCATION_ATTRIBUTE, catalogConfigurationBean.getLocationAttribute());

        // Time
        final String timeAttribute = mosaicConfiguration.getTimeAttribute();
        if (timeAttribute != null) {
            properties.setProperty(
                    Utils.Prop.TIME_ATTRIBUTE, mosaicConfiguration.getTimeAttribute());
        }

        // Elevation
        final String elevationAttribute = mosaicConfiguration.getElevationAttribute();
        if (elevationAttribute != null) {
            properties.setProperty(
                    Utils.Prop.ELEVATION_ATTRIBUTE, mosaicConfiguration.getElevationAttribute());
        }

        // CRS
        final String crsAttribute = mosaicConfiguration.getCRSAttribute();
        if (crsAttribute != null) {
            properties.setProperty(Utils.Prop.CRS_ATTRIBUTE, mosaicConfiguration.getCRSAttribute());
        }

        // Additional domains
        final String additionalDomainAttribute =
                mosaicConfiguration.getAdditionalDomainAttributes();
        if (additionalDomainAttribute != null) {
            properties.setProperty(
                    Utils.Prop.ADDITIONAL_DOMAIN_ATTRIBUTES,
                    mosaicConfiguration.getAdditionalDomainAttributes());
        }

        final int numberOfLevels = mosaicConfiguration.getLevelsNum();
        final double[][] resolutionLevels = mosaicConfiguration.getLevels();
        properties.setProperty(Utils.Prop.LEVELS_NUM, Integer.toString(numberOfLevels));
        final StringBuilder levels = new StringBuilder();
        for (int k = 0; k < numberOfLevels; k++) {
            levels.append(Double.toString(resolutionLevels[k][0]))
                    .append(",")
                    .append(Double.toString(resolutionLevels[k][1]));
            if (k < numberOfLevels - 1) {
                levels.append(" ");
            }
        }
        properties.setProperty(Utils.Prop.LEVELS, levels.toString());
        properties.setProperty(Utils.Prop.NAME, mosaicConfiguration.getName());
        String typeName = mosaicConfiguration.getCatalogConfigurationBean().getTypeName();
        if (typeName == null) {
            typeName = mosaicConfiguration.getName();
        }
        properties.setProperty(Utils.Prop.TYPENAME, typeName);
        properties.setProperty(
                Utils.Prop.EXP_RGB, Boolean.toString(mosaicConfiguration.isExpandToRGB()));
        properties.setProperty(
                Utils.Prop.CHECK_AUXILIARY_METADATA,
                Boolean.toString(mosaicConfiguration.isCheckAuxiliaryMetadata()));
        properties.setProperty(
                Utils.Prop.HETEROGENEOUS,
                Boolean.toString(catalogConfigurationBean.isHeterogeneous()));
        properties.setProperty(
                Utils.Prop.HETEROGENEOUS_CRS,
                Boolean.toString(catalogConfigurationBean.isHeterogeneousCRS()));

        boolean wrapStore = catalogConfigurationBean.isWrapStore();
        if (wrapStore) {
            // Avoid setting this property when false, since it's default
            properties.setProperty(Utils.Prop.WRAP_STORE, Boolean.toString(wrapStore));
        }

        if (cachedReaderSPI != null) {
            // suggested spi
            properties.setProperty(Utils.Prop.SUGGESTED_SPI, cachedReaderSPI.getClass().getName());
        }

        // write down imposed bbox
        if (imposedBBox != null) {
            properties.setProperty(
                    Utils.Prop.ENVELOPE2D,
                    imposedBBox.getMinX()
                            + ","
                            + imposedBBox.getMinY()
                            + " "
                            + imposedBBox.getMaxX()
                            + ","
                            + imposedBBox.getMaxY());
        }
        properties.setProperty(
                Utils.Prop.CACHING, Boolean.toString(catalogConfigurationBean.isCaching()));
        if (mosaicConfiguration.getAuxiliaryFilePath() != null) {
            properties.setProperty(
                    Utils.Prop.AUXILIARY_FILE, mosaicConfiguration.getAuxiliaryFilePath());
        }
        if (mosaicConfiguration.getAuxiliaryDatastorePath() != null) {
            properties.setProperty(
                    Utils.Prop.AUXILIARY_DATASTORE_FILE,
                    mosaicConfiguration.getAuxiliaryDatastorePath());
        }
        if (mosaicConfiguration.getCoverageNameCollectorSpi() != null) {
            properties.setProperty(
                    Utils.Prop.COVERAGE_NAME_COLLECTOR_SPI,
                    mosaicConfiguration.getCoverageNameCollectorSpi());
        }

        if (mosaicConfiguration.getCrs() != null) {
            properties.setProperty(Prop.MOSAIC_CRS, CRS.toSRS(mosaicConfiguration.getCrs()));
        }

        if (mosaicConfiguration.getNoData() != null) {
            properties.setProperty(Prop.NO_DATA, String.valueOf(mosaicConfiguration.getNoData()));
        }

        String filePath =
                runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR)
                        + "/"
                        // + runConfiguration.getIndexName() + ".properties"));
                        + mosaicConfiguration.getName()
                        + ".properties";
        try (OutputStream outStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            properties.store(outStream, "-Automagically created from GeoTools-");
        } catch (IOException e) {
            eventHandler.fireEvent(Level.SEVERE, e.getLocalizedMessage(), 0);
        }
    }

    /**
     * Check whether the specified coverage already exist in the reader. This allows to get the
     * rasterManager associated with that coverage instead of creating a new one.
     *
     * @param coverageName the name of the coverage to be searched
     * @return {@code true} in case that coverage already exists
     */
    protected boolean coverageExists(String coverageName) throws IOException {
        String[] coverages = getParentReader().getGridCoverageNames();
        for (String coverage : coverages) {
            if (coverage.equals(coverageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Use the passed coverageReader to create or update the all the needed configurations<br>
     * It not responsible of the passed coverageReader which should be disposed outside (in the
     * caller).
     */
    public void updateConfiguration(
            GridCoverage2DReader coverageReader,
            final String inputCoverageName,
            File fileBeingProcessed,
            int fileIndex,
            double numFiles,
            DefaultTransaction transaction)
            throws IOException, GranuleHandlingException, NoSuchAuthorityCodeException,
                    FactoryException, TransformException {

        final String targetCoverageName = getTargetCoverageName(coverageReader, inputCoverageName);

        final Indexer indexer = getRunConfiguration().getIndexer();

        // checking whether the coverage already exists
        final boolean coverageExists = coverageExists(targetCoverageName);
        MosaicConfigurationBean mosaicConfiguration = null;
        MosaicConfigurationBean currentConfigurationBean = null;
        RasterManager rasterManager = null;
        if (coverageExists) {
            // Get the manager for this coverage so it can be updated
            rasterManager = getParentReader().getRasterManager(targetCoverageName);
            mosaicConfiguration = rasterManager.getConfiguration();
            this.configurations.put(mosaicConfiguration.getName(), mosaicConfiguration);
        }

        // STEP 2
        // Collecting all Coverage properties to setup a MosaicConfigurationBean through
        // the builder
        final MosaicBeanBuilder configBuilder = new MosaicBeanBuilder();

        final GeneralEnvelope envelope = coverageReader.getOriginalEnvelope(inputCoverageName);
        final CoordinateReferenceSystem actualCRS =
                coverageReader.getCoordinateReferenceSystem(inputCoverageName);

        SampleModel sm = null;
        ColorModel cm = null;
        int numberOfLevels = 1;
        double[][] resolutionLevels = null;
        CatalogBuilderConfiguration catalogConfig;
        Boolean heterogeneousCRS =
                Boolean.valueOf(IndexerUtils.getParameter(Prop.HETEROGENEOUS_CRS, indexer));
        if (mosaicConfiguration == null) {
            catalogConfig = getRunConfiguration();
            // We don't have a configuration for this configuration

            // Get the type specifier for this image and the check that the
            // image has the correct sample model and color model.
            // If this is the first cycle of the loop we initialize everything.
            //
            ImageLayout layout = coverageReader.getImageLayout(inputCoverageName);
            cm = layout.getColorModel(null);
            sm = layout.getSampleModel(null);

            // at the first step we initialize everything that we will
            // reuse afterwards starting with color models, sample
            // models, crs, etc....

            configBuilder.setSampleModel(sm);
            configBuilder.setColorModel(cm);
            ColorModel defaultCM = cm;

            // Checking palette
            if (defaultCM instanceof IndexColorModel) {
                IndexColorModel icm = (IndexColorModel) defaultCM;
                byte[][] defaultPalette = Utils.extractPalette(icm);
                configBuilder.setPalette(defaultPalette);
            }

            // STEP 2.A
            // Preparing configuration
            String mosaicCrs = IndexerUtils.getParameter(Utils.Prop.MOSAIC_CRS, indexer);
            if (mosaicCrs != null) {
                configBuilder.setCrs(CRS.decode(mosaicCrs, true));
            } else {
                configBuilder.setCrs(actualCRS);
            }

            String noData = IndexerUtils.getParameter(Prop.NO_DATA, indexer);
            if (noData != null && !noData.isEmpty()) {
                try {
                    double noDataValue = Double.parseDouble(noData);
                    configBuilder.setNoData(noDataValue);
                } catch (NumberFormatException e) {
                    String error =
                            "Invalid NoData specification " + noData + ", was expecting a number";
                    LOGGER.log(Level.WARNING, error);
                    throw new RuntimeException(error, e);
                }
            }

            // get/compute the resolution levels
            resolutionLevels =
                    getResolutionLevels(coverageReader, inputCoverageName, configBuilder.getCrs());
            numberOfLevels = resolutionLevels.length;
            configBuilder.setLevels(resolutionLevels);
            configBuilder.setLevelsNum(numberOfLevels);
            configBuilder.setName(targetCoverageName);
            configBuilder.setTimeAttribute(
                    IndexerUtils.getAttribute(targetCoverageName, Utils.TIME_DOMAIN, indexer));
            configBuilder.setCrsAttribute(
                    IndexerUtils.getAttribute(targetCoverageName, Prop.CRS_ATTRIBUTE, indexer));
            configBuilder.setElevationAttribute(
                    IndexerUtils.getAttribute(targetCoverageName, Utils.ELEVATION_DOMAIN, indexer));
            configBuilder.setAdditionalDomainAttributes(
                    IndexerUtils.getAttribute(
                            targetCoverageName, Utils.ADDITIONAL_DOMAIN, indexer));

            final Hints runHints = getRunConfiguration().getHints();
            if (runHints != null) {
                if (runHints.containsKey(Utils.AUXILIARY_FILES_PATH)) {
                    String auxiliaryFilePath = (String) runHints.get(Utils.AUXILIARY_FILES_PATH);
                    if (auxiliaryFilePath != null && auxiliaryFilePath.trim().length() > 0) {
                        configBuilder.setAuxiliaryFilePath(auxiliaryFilePath);
                    }
                }
                if (runHints.containsKey(Utils.AUXILIARY_DATASTORE_PATH)) {
                    String auxiliaryDatastorePath =
                            (String) runHints.get(Utils.AUXILIARY_DATASTORE_PATH);
                    if (auxiliaryDatastorePath != null
                            && auxiliaryDatastorePath.trim().length() > 0) {
                        configBuilder.setAuxiliaryDatastorePath(auxiliaryDatastorePath);
                    }
                }
            }

            final CatalogConfigurationBean catalogConfigurationBean =
                    new CatalogConfigurationBean();
            catalogConfigurationBean.setCaching(
                    IndexerUtils.getParameterAsBoolean(Prop.CACHING, indexer));
            if (IndexerUtils.getParameterAsBoolean(Prop.ABSOLUTE_PATH, indexer)) {
                catalogConfigurationBean.setPathType(PathType.ABSOLUTE);
            } else {
                catalogConfigurationBean.setPathType(PathType.RELATIVE);
            }
            PathType pathType =
                    IndexerUtils.getParameterAsEnum(Prop.PATH_TYPE, PathType.class, indexer);
            if (pathType != null) {
                catalogConfigurationBean.setPathType(pathType);
            }

            catalogConfigurationBean.setLocationAttribute(
                    IndexerUtils.getParameter(Prop.LOCATION_ATTRIBUTE, indexer));
            catalogConfigurationBean.setWrapStore(
                    IndexerUtils.getParameterAsBoolean(Prop.WRAP_STORE, indexer));

            String configuredTypeName = IndexerUtils.getParameter(Prop.TYPENAME, indexer);
            if (configuredTypeName != null) {
                catalogConfigurationBean.setTypeName(configuredTypeName);
            } else {
                catalogConfigurationBean.setTypeName(targetCoverageName);
            }
            configBuilder.setCatalogConfigurationBean(catalogConfigurationBean);
            configBuilder.setCheckAuxiliaryMetadata(
                    IndexerUtils.getParameterAsBoolean(Prop.CHECK_AUXILIARY_METADATA, indexer));

            currentConfigurationBean = configBuilder.getMosaicConfigurationBean();
            if (heterogeneousCRS) {
                currentConfigurationBean.getCatalogConfigurationBean().setHeterogeneous(true);
                currentConfigurationBean.getCatalogConfigurationBean().setHeterogeneousCRS(true);
            }

            // Creating a rasterManager which will be initialized after populating the catalog
            getParentReader().addRasterManager(currentConfigurationBean, false);

            // Creating a granuleStore
            if (!useExistingSchema) {
                // creating the schema
                SimpleFeatureType indexSchema =
                        createSchema(
                                getRunConfiguration(),
                                currentConfigurationBean.getName(),
                                configBuilder.getCrs());
                getParentReader().createCoverage(targetCoverageName, indexSchema);
            }
            getConfigurations().put(currentConfigurationBean.getName(), currentConfigurationBean);

        } else {
            catalogConfig = new CatalogBuilderConfiguration();
            CatalogConfigurationBean bean = mosaicConfiguration.getCatalogConfigurationBean();
            catalogConfig.setParameter(Prop.LOCATION_ATTRIBUTE, (bean.getLocationAttribute()));
            catalogConfig.setParameter(
                    Prop.ABSOLUTE_PATH, Boolean.toString(bean.getPathType() == PathType.ABSOLUTE));
            catalogConfig.setParameter(Prop.PATH_TYPE, bean.getPathType().toString());
            catalogConfig.setParameter(
                    Prop.ROOT_MOSAIC_DIR /* setRootMosaicDirectory( */,
                    getRunConfiguration().getParameter(Prop.ROOT_MOSAIC_DIR));

            // We already have a Configuration for this coverage.
            // Check its properties are compatible with the existing coverage.

            CatalogConfigurationBean catalogConfigurationBean = bean;

            // make sure we pick the same resolution irrespective of order of harvest
            resolutionLevels =
                    getResolutionLevels(
                            coverageReader, inputCoverageName, mosaicConfiguration.getCrs());
            numberOfLevels = resolutionLevels.length;

            int originalNumberOfLevels = mosaicConfiguration.getLevelsNum();
            boolean needUpdate = false;
            if (Utils.homogeneousCheck(
                    Math.min(numberOfLevels, originalNumberOfLevels),
                    resolutionLevels,
                    mosaicConfiguration.getLevels())) {
                if (numberOfLevels != originalNumberOfLevels) {
                    catalogConfigurationBean.setHeterogeneous(true);
                    if (numberOfLevels > originalNumberOfLevels) {
                        needUpdate = true; // pick the one with highest number of levels
                    }
                }
            } else {
                catalogConfigurationBean.setHeterogeneous(true);
                if (isHigherResolution(resolutionLevels, mosaicConfiguration.getLevels())) {
                    needUpdate = true; // pick the one with the highest resolution
                }
            }

            // configuration need to be updated
            if (needUpdate) {
                mosaicConfiguration.setLevels(resolutionLevels);
                mosaicConfiguration.setLevelsNum(numberOfLevels);
                getConfigurations().put(mosaicConfiguration.getName(), mosaicConfiguration);
            }
        }
        // STEP 3
        if (!useExistingSchema) {
            // create and store features
            updateCatalog(
                    targetCoverageName,
                    fileBeingProcessed,
                    coverageReader,
                    getParentReader(),
                    catalogConfig,
                    envelope,
                    transaction,
                    getPropertiesCollectors());
        }
    }

    private double[][] getResolutionLevels(
            GridCoverage2DReader coverageReader,
            final String inputCoverageName,
            CoordinateReferenceSystem mosaicCRS)
            throws IOException, FactoryException, TransformException {
        double[][] resolutionLevels;
        resolutionLevels = coverageReader.getResolutionLevels(inputCoverageName);
        final CoordinateReferenceSystem readerCRS = coverageReader.getCoordinateReferenceSystem();
        if (mosaicCRS != null
                && readerCRS != null
                && !CRS.equalsIgnoreMetadata(mosaicCRS, readerCRS)) {
            resolutionLevels =
                    transformResolutionLevels(
                            resolutionLevels,
                            readerCRS,
                            mosaicCRS,
                            coverageReader.getOriginalEnvelope());
        }
        return resolutionLevels;
    }

    /** Transforms the given resolution levels from a start CRS to a target one. */
    private double[][] transformResolutionLevels(
            double[][] resolutionLevels,
            CoordinateReferenceSystem fromCRS,
            CoordinateReferenceSystem toCRS,
            GeneralEnvelope sourceEnvelope)
            throws FactoryException, TransformException {

        // prepare a set of points at middle of the envelope and their
        // corresponding offsets based on resolutions
        final int numLevels = resolutionLevels.length;
        double[] points = new double[numLevels * 8];
        double baseX = sourceEnvelope.getMedian(0);
        double baseY = sourceEnvelope.getMedian(1);
        for (int i = 0, j = 0; i < numLevels; i++) {
            // delta x point
            points[j++] = baseX;
            points[j++] = baseY;
            points[j++] = baseX + resolutionLevels[i][0];
            points[j++] = baseY;
            // delta y point
            points[j++] = baseX;
            points[j++] = baseY;
            points[j++] = baseX;
            points[j++] = baseY + resolutionLevels[i][1];
        }

        // transform to get offsets in the target CRS
        MathTransform mt = CRS.findMathTransform(fromCRS, toCRS);
        mt.transform(points, 0, points, 0, numLevels * 4);

        // compute back the offsets
        double[][] result = new double[numLevels][2];
        for (int i = 0; i < numLevels; i++) {
            result[i][0] = distance(points, i * 8);
            result[i][1] = distance(points, i * 8 + 4);
        }
        return result;
    }

    private double distance(double[] points, int base) {
        double dx = points[base + 2] - points[base];
        double dy = points[base + 3] - points[base + 1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Get the name of the target coverage for a given reader. For most input coverages, the target
     * coverage is simply the default coverage. For structured coverages the target coverage has the
     * same name as the input coverage.
     *
     * @param inputCoverageReader the coverage being added to the index
     * @param inputCoverageName the name of the input coverage
     * @return the target coverage name for the input coverage
     */
    public String getTargetCoverageName(
            GridCoverage2DReader inputCoverageReader, String inputCoverageName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Prop.INDEX_NAME, getRunConfiguration().getParameter(Prop.INDEX_NAME));
        map.put(Prop.INPUT_COVERAGE_NAME, inputCoverageName);
        return coverageNameHandler.getTargetCoverageName(inputCoverageReader, map);
    }

    private boolean isHigherResolution(double[][] a, double[][] b) {
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            for (int j = 0; j < Math.min(a[i].length, b[i].length); j++) {
                if (a[i][j] < b[i][j]) {
                    return true;
                } else if (a[i][j] > b[i][j]) {
                    return false;
                }
            }
        }
        return false;
    }

    public void dispose() {
        reset();
    }

    public Map<String, MosaicConfigurationBean> getConfigurations() {
        return configurations;
    }

    public GranuleCatalog getCatalog() {
        return catalog;
    }

    public CatalogBuilderConfiguration getRunConfiguration() {
        return runConfiguration;
    }

    public ImageMosaicReader getParentReader() {
        return parentReader;
    }

    public void setParentReader(ImageMosaicReader parentReader) {
        this.parentReader = parentReader;
    }

    public List<PropertiesCollector> getPropertiesCollectors() {
        return propertiesCollectors;
    }

    public boolean isUseExistingSchema() {
        return useExistingSchema;
    }

    public ImageReaderSpi getCachedReaderSPI() {
        return cachedReaderSPI;
    }

    public void setCachedReaderSPI(ImageReaderSpi cachedReaderSPI) {
        this.cachedReaderSPI = cachedReaderSPI;
    }

    public List<GranuleAcceptor> getGranuleAcceptors() {
        return granuleAcceptors;
    }

    public RasterManager getRasterManagerForTargetCoverage(String targetCoverageName) {
        return this.getParentReader().getRasterManager(targetCoverageName);
    }
}
