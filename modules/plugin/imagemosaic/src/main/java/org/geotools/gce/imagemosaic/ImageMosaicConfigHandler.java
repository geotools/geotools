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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;
import javax.media.jai.ImageLayout;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.factory.Hints;
import org.geotools.factory.Hints.Key;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.index.DomainType;
import org.geotools.gce.imagemosaic.catalog.index.DomainsType;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors.Collector;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.gce.imagemosaic.catalog.index.SchemasType;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.gce.imagemosaic.catalogbuilder.MosaicBeanBuilder;
import org.geotools.gce.imagemosaic.namecollector.DefaultCoverageNameCollectorSPI;
import org.geotools.gce.imagemosaic.properties.DefaultPropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class is in responsible for creating and managing the catalog and the configuration of the mosaic
 * 
 * @author Carlo Cancellieri - GeoSolutions SAS
 * 
 * @source $URL$
 */
public class ImageMosaicConfigHandler {

    /** Default Logger * */
    final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ImageMosaicConfigHandler.class);

    private List<PropertiesCollector> propertiesCollectors = null;

    private Map<String, MosaicConfigurationBean> configurations = new HashMap<String, MosaicConfigurationBean>();

    /**
     * Proper way to stop a thread is not by calling Thread.stop() but by using a shared variable that can be checked in order to notify a terminating
     * condition.
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

    private CoverageNameHandler coverageNameHandler = new CoverageNameHandler(new DefaultCoverageNameCollectorSPI());

    /**
     * Default constructor
     * 
     * @throws @throws IllegalArgumentException
     */
    public ImageMosaicConfigHandler(final CatalogBuilderConfiguration configuration,
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
            IndexerUtils.getParameterAsBoolean(Prop.USE_EXISTING_SCHEMA, defaultIndexer);
        }

        Utilities.ensureNonNull("root location", rootMosaicDir);

        // look for and indexer.properties file
        parent = new File(rootMosaicDir);
        indexerFile = new File(parent, Utils.INDEXER_XML);
        Indexer indexer = null;

        Hints hints = configuration.getHints();
        String ancillaryFile = null;
        String datastoreFile = null;
        if (Utils.checkFileReadable(indexerFile)) {
            try {
                indexer = Utils.unmarshal(indexerFile);
                if (indexer != null) {
                    copyDefaultParams(params, indexer);
                }
            } catch (JAXBException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        } else {
            // Backward compatible with old indexing
            indexerFile = new File(parent, Utils.INDEXER_PROPERTIES);
            if (Utils.checkFileReadable(indexerFile)) {
                // load it and parse it
                final Properties props = CoverageUtilities
                        .loadPropertiesFromURL(DataUtilities.fileToURL(indexerFile));
                indexer = createIndexer(props, params);
            }
        }
        if (indexer != null) {
            // Overwrite default indexer only when indexer is available
            configuration.setIndexer(indexer);
            String auxiliaryFileParam = IndexerUtils.getParameter(Utils.Prop.AUXILIARY_FILE,
                    indexer);
            if (auxiliaryFileParam != null) {
                ancillaryFile = auxiliaryFileParam;
            }
            String datastoreFileParam = IndexerUtils
                    .getParameter(Utils.Prop.AUXILIARY_DATASTORE_FILE, indexer);
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

        initializeCoverageNameHandler(indexer);

        updateConfigurationHints(configuration, hints, ancillaryFile, datastoreFile,
                IndexerUtils.getParam(params, Prop.ROOT_MOSAIC_DIR));

        // check config
        configuration.check();

        this.runConfiguration = new CatalogBuilderConfiguration(configuration);
    }

    private void initializeCoverageNameHandler(Indexer indexer) {
        // we initialized at construction time with the default handler
        // ok, do we need/want something different?
        if (indexer != null) {
            String coverageNameCollectorString = IndexerUtils.getParameter(Prop.COVERAGE_NAME_COLLECTOR_SPI, indexer);
            if (coverageNameCollectorString != null && coverageNameCollectorString.length() > 0) {
                //Override default handling machinery
                coverageNameHandler = new CoverageNameHandler(coverageNameCollectorString);
            }
        }
    }

    private void setReader(Hints hints, final boolean updateHints) {
        if (hints != null && hints.containsKey(Utils.MOSAIC_READER)) {
            Object reader = hints.get(Utils.MOSAIC_READER);
            if (reader instanceof ImageMosaicReader) {
                if (getParentReader() == null) {
                    setParentReader((ImageMosaicReader) reader);
                }
                if (updateHints) {
                    Hints readerHints = getParentReader().getHints();
                    readerHints.add(hints);
                }
            }
        }
    }

    private void updateConfigurationHints(final CatalogBuilderConfiguration configuration,
            Hints hints, final String ancillaryFile, final String datastoreFile,
            final String rootMosaicDir) {
        final boolean isAbsolutePath = Boolean
                .parseBoolean(configuration.getParameter(Prop.ABSOLUTE_PATH));
        hints = updateHints(ancillaryFile, isAbsolutePath, rootMosaicDir, configuration, hints,
                Utils.AUXILIARY_FILES_PATH);
        hints = updateHints(datastoreFile, isAbsolutePath, rootMosaicDir, configuration, hints,
                Utils.AUXILIARY_DATASTORE_PATH);
        setReader(hints, true);
    }

    private Hints updateHints(String filePath, boolean isAbsolutePath, String rootMosaicDir,
            CatalogBuilderConfiguration configuration, Hints hints, Key key) {
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
     * Setup default params to the indexer.
     * 
     * @param params
     * @param indexer
     */
    private void copyDefaultParams(ParametersType params, Indexer indexer) {
        if (params != null) {
            List<Parameter> defaultParamList = params.getParameter();
            if (defaultParamList != null && !defaultParamList.isEmpty()) {
                ParametersType parameters = indexer.getParameters();
                if (parameters == null) {
                    parameters = Utils.OBJECT_FACTORY.createParametersType();
                    indexer.setParameters(parameters);
                }
                List<Parameter> parameterList = parameters.getParameter();
                for (Parameter defaultParameter : defaultParamList) {
                    final String defaultParameterName = defaultParameter.getName();
                    if (IndexerUtils.getParameter(defaultParameterName, indexer) == null) {
                        IndexerUtils.setParam(parameterList, defaultParameterName,
                                defaultParameter.getValue());
                    }
                }
            }
        }
    }

    /**
     * Perform proper clean up.
     * 
     * <p>
     * Make sure to call this method when you are not running the {@link ImageMosaicConfigHandler} or bad things can happen. If it is running, please
     * stop it first.
     */
    public void reset() {
        eventHandler.removeAllProcessingEventListeners();
        // clear stop
        stop = false;

        // fileIndex = 0;
        runConfiguration = null;

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
        GranuleCatalog catalog = CatalogManager.createCatalog(runConfiguration, !useExistingSchema);
        getParentReader().granuleCatalog = catalog;
        return catalog;
    }

    /**
     * Load properties collectors from the configuration
     */
    private void loadPropertyCollectors() {
        // load property collectors
        Indexer indexer = runConfiguration.getIndexer();
        Collectors collectors = indexer.getCollectors();
        if (collectors == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No properties collector have been found");
            }
            return;
        }
        List<Collector> collectorList = collectors.getCollector();

        // load the SPI set
        final Set<PropertiesCollectorSPI> pcSPIs = PropertiesCollectorFinder
                .getPropertiesCollectorSPI();

        // parse the string
        final List<PropertiesCollector> pcs = new ArrayList<PropertiesCollector>();
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
            final String config;
            if (!collectorValue.startsWith(DefaultPropertiesCollectorSPI.REGEX_PREFIX)) {
                config = DefaultPropertiesCollectorSPI.REGEX_PREFIX + collector.getValue();
            } else {
                config = collector.getValue();
            }

            // create the PropertiesCollector
            final PropertiesCollector pc = selectedSPI.create(config,
                    Arrays.asList(collector.getMapped()));
            if (pc != null) {
                pcs.add(pc);
            } else {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Unable to create PropertyCollector");
                }
            }
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
                if (supportsEmpty || keySize > 1
                        || (keySize > 0 && !base.equals(keys.iterator().next()))) {
                    File mosaicFile = null;
                    File originFile = null;
                    if (indexerFile.getAbsolutePath().endsWith("xml")) {
                        mosaicFile = new File(indexerFile.getAbsolutePath()
                                .replace(Utils.INDEXER_XML, (base + ".xml")));
                        originFile = indexerFile;
                    } else if (indexerFile.getAbsolutePath().endsWith("properties")) {
                        mosaicFile = new File(indexerFile.getAbsolutePath()
                                .replace(Utils.INDEXER_PROPERTIES, (base + ".properties")));
                        originFile = indexerFile;
                    } else {
                        final String source = runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR)
                                + File.separatorChar
                                + configurations.get(keys.iterator().next()).getName()
                                + ".properties";
                        mosaicFile = new File(indexerFile.getAbsolutePath()
                                .replace(Utils.INDEXER_PROPERTIES, (base + ".properties")));
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

    /**
     * Store a sample image frmo which we can derive the default SM and CM
     */
    private void createSampleImage(final MosaicConfigurationBean mosaicConfiguration,
            final boolean useName) {
        // create a sample image to store SM and CM
        Utilities.ensureNonNull("mosaicConfiguration", mosaicConfiguration);
        String filePath = null;
        if (mosaicConfiguration.getSampleModel() != null
                && mosaicConfiguration.getColorModel() != null) {

            // sample image file
            // TODO: Consider revisit this using different name/folder
            final String baseName = runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR) + "/";
            filePath = baseName + (useName ? mosaicConfiguration.getName() : "")
                    + Utils.SAMPLE_IMAGE_NAME;
            try {
                Utils.storeSampleImage(new File(filePath), mosaicConfiguration.getSampleModel(),
                        mosaicConfiguration.getColorModel());
            } catch (IOException e) {
                eventHandler.fireEvent(Level.SEVERE, e.getLocalizedMessage(), 0);
            }
        }
    }

    private Indexer createIndexer(Properties props, ParametersType params) {
        // Initializing Indexer objects
        Indexer indexer = Utils.OBJECT_FACTORY.createIndexer();
        indexer.setParameters(
                params != null ? params : Utils.OBJECT_FACTORY.createParametersType());
        Coverages coverages = Utils.OBJECT_FACTORY.createIndexerCoverages();
        indexer.setCoverages(coverages);
        List<Coverage> coverageList = coverages.getCoverage();

        Coverage coverage = Utils.OBJECT_FACTORY.createIndexerCoveragesCoverage();
        coverageList.add(coverage);

        indexer.setParameters(params);
        List<Parameter> parameters = params.getParameter();

        // name
        if (props.containsKey(Prop.NAME)) {
            IndexerUtils.setParam(parameters, props, Prop.NAME);
            coverage.setName(props.getProperty(Prop.NAME));
        }

        // type name
        if (props.containsKey(Prop.TYPENAME)) {
            IndexerUtils.setParam(parameters, props, Prop.TYPENAME);
            coverage.setName(props.getProperty(Prop.TYPENAME));
        }

        // absolute
        if (props.containsKey(Prop.ABSOLUTE_PATH))
            IndexerUtils.setParam(parameters, props, Prop.ABSOLUTE_PATH);

        // recursive
        if (props.containsKey(Prop.RECURSIVE))
            IndexerUtils.setParam(parameters, props, Prop.RECURSIVE);

        // wildcard
        if (props.containsKey(Prop.WILDCARD))
            IndexerUtils.setParam(parameters, props, Prop.WILDCARD);

        if (props.containsKey(Prop.COVERAGE_NAME_COLLECTOR_SPI)) {
            IndexerUtils.setParam(parameters, props, Prop.COVERAGE_NAME_COLLECTOR_SPI);
        }

        // schema
        if (props.containsKey(Prop.SCHEMA)) {
            SchemasType schemas = Utils.OBJECT_FACTORY.createSchemasType();
            SchemaType schema = Utils.OBJECT_FACTORY.createSchemaType();
            indexer.setSchemas(schemas);
            schemas.getSchema().add(schema);
            schema.setAttributes(props.getProperty(Prop.SCHEMA));
            schema.setName(IndexerUtils.getParameter(Prop.INDEX_NAME, indexer));
        }

        DomainsType domains = coverage.getDomains();
        List<DomainType> domainList = null;
        // time attr
        if (props.containsKey(Prop.TIME_ATTRIBUTE)) {
            if (domains == null) {
                domains = Utils.OBJECT_FACTORY.createDomainsType();
                coverage.setDomains(domains);
                domainList = domains.getDomain();
            }
            DomainType domain = Utils.OBJECT_FACTORY.createDomainType();
            domain.setName(Utils.TIME_DOMAIN.toLowerCase());
            IndexerUtils.setAttributes(domain, props.getProperty(Prop.TIME_ATTRIBUTE));
            domainList.add(domain);
        }

        // elevation attr
        if (props.containsKey(Prop.ELEVATION_ATTRIBUTE)) {
            if (domains == null) {
                domains = Utils.OBJECT_FACTORY.createDomainsType();
                coverage.setDomains(domains);
                domainList = domains.getDomain();
            }
            DomainType domain = Utils.OBJECT_FACTORY.createDomainType();
            domain.setName(Utils.ELEVATION_DOMAIN.toLowerCase());
            IndexerUtils.setAttributes(domain, props.getProperty(Prop.ELEVATION_ATTRIBUTE));
            domainList.add(domain);
        }

        // Additional domain attr
        if (props.containsKey(Prop.ADDITIONAL_DOMAIN_ATTRIBUTES)) {
            if (domains == null) {
                domains = Utils.OBJECT_FACTORY.createDomainsType();
                coverage.setDomains(domains);
                domainList = domains.getDomain();
            }
            String attributes = props.getProperty(Prop.ADDITIONAL_DOMAIN_ATTRIBUTES);
            IndexerUtils.parseAdditionalDomains(attributes, domainList);
        }

        // imposed BBOX
        if (props.containsKey(Prop.ENVELOPE2D))
            IndexerUtils.setParam(parameters, props, Prop.ENVELOPE2D);

        // imposed Pyramid Layout
        if (props.containsKey(Prop.RESOLUTION_LEVELS))
            IndexerUtils.setParam(parameters, props, Prop.RESOLUTION_LEVELS);

        // collectors
        if (props.containsKey(Prop.PROPERTY_COLLECTORS)) {
            IndexerUtils.setPropertyCollectors(indexer,
                    props.getProperty(Prop.PROPERTY_COLLECTORS));
        }

        if (props.containsKey(Prop.CACHING))
            IndexerUtils.setParam(parameters, props, Prop.CACHING);

        if (props.containsKey(Prop.ROOT_MOSAIC_DIR)) {
            // Overriding root mosaic directory
            IndexerUtils.setParam(parameters, props, Prop.ROOT_MOSAIC_DIR);
        }

        if (props.containsKey(Prop.INDEXING_DIRECTORIES)) {
            IndexerUtils.setParam(parameters, props, Prop.INDEXING_DIRECTORIES);
        }
        if (props.containsKey(Prop.AUXILIARY_FILE)) {
            IndexerUtils.setParam(parameters, props, Prop.AUXILIARY_FILE);
        }
        if (props.containsKey(Prop.AUXILIARY_DATASTORE_FILE)) {
            IndexerUtils.setParam(parameters, props, Prop.AUXILIARY_DATASTORE_FILE);
        }
        if (props.containsKey(Prop.CAN_BE_EMPTY)) {
            IndexerUtils.setParam(parameters, props, Prop.CAN_BE_EMPTY);
        }
        if (props.containsKey(Prop.WRAP_STORE)) {
            IndexerUtils.setParam(parameters, props, Prop.WRAP_STORE);
        }
        if (props.containsKey(Prop.USE_EXISTING_SCHEMA)) {
            IndexerUtils.setParam(parameters, props, Prop.USE_EXISTING_SCHEMA);
        }
        if (props.containsKey(Prop.CHECK_AUXILIARY_METADATA)) {
            IndexerUtils.setParam(parameters, props, Prop.CHECK_AUXILIARY_METADATA);
        }
        return indexer;
    }

    /**
     * Creates the final properties file.
     */
    private void createPropertiesFiles(MosaicConfigurationBean mosaicConfiguration) {

        //
        // FINAL STEP
        //
        // CREATING GENERAL INFO FILE
        //

        CatalogConfigurationBean catalogConfigurationBean = mosaicConfiguration
                .getCatalogConfigurationBean();

        // envelope
        final Properties properties = new Properties();
        properties.setProperty(Utils.Prop.ABSOLUTE_PATH,
                Boolean.toString(catalogConfigurationBean.isAbsolutePath()));
        properties.setProperty(Utils.Prop.LOCATION_ATTRIBUTE,
                catalogConfigurationBean.getLocationAttribute());

        // Time
        final String timeAttribute = mosaicConfiguration.getTimeAttribute();
        if (timeAttribute != null) {
            properties.setProperty(Utils.Prop.TIME_ATTRIBUTE,
                    mosaicConfiguration.getTimeAttribute());
        }

        // Elevation
        final String elevationAttribute = mosaicConfiguration.getElevationAttribute();
        if (elevationAttribute != null) {
            properties.setProperty(Utils.Prop.ELEVATION_ATTRIBUTE,
                    mosaicConfiguration.getElevationAttribute());
        }

        // Additional domains
        final String additionalDomainAttribute = mosaicConfiguration
                .getAdditionalDomainAttributes();
        if (additionalDomainAttribute != null) {
            properties.setProperty(Utils.Prop.ADDITIONAL_DOMAIN_ATTRIBUTES,
                    mosaicConfiguration.getAdditionalDomainAttributes());
        }

        final int numberOfLevels = mosaicConfiguration.getLevelsNum();
        final double[][] resolutionLevels = mosaicConfiguration.getLevels();
        properties.setProperty(Utils.Prop.LEVELS_NUM, Integer.toString(numberOfLevels));
        final StringBuilder levels = new StringBuilder();
        for (int k = 0; k < numberOfLevels; k++) {
            levels.append(Double.toString(resolutionLevels[k][0])).append(",")
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
        properties.setProperty(Utils.Prop.EXP_RGB,
                Boolean.toString(mosaicConfiguration.isExpandToRGB()));
        properties.setProperty(Utils.Prop.CHECK_AUXILIARY_METADATA,
                Boolean.toString(mosaicConfiguration.isCheckAuxiliaryMetadata()));
        properties.setProperty(Utils.Prop.HETEROGENEOUS,
                Boolean.toString(catalogConfigurationBean.isHeterogeneous()));
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
            properties.setProperty(Utils.Prop.ENVELOPE2D,
                    imposedBBox.getMinX() + "," + imposedBBox.getMinY() + " "
                            + imposedBBox.getMaxX() + "," + imposedBBox.getMaxY());
        }
        properties.setProperty(Utils.Prop.CACHING,
                Boolean.toString(catalogConfigurationBean.isCaching()));
        if (mosaicConfiguration.getAuxiliaryFilePath() != null) {
            properties.setProperty(Utils.Prop.AUXILIARY_FILE,
                    mosaicConfiguration.getAuxiliaryFilePath());
        }
        if (mosaicConfiguration.getAuxiliaryDatastorePath() != null) {
            properties.setProperty(Utils.Prop.AUXILIARY_DATASTORE_FILE,
                    mosaicConfiguration.getAuxiliaryDatastorePath());
        }
        if (mosaicConfiguration.getCoverageNameCollectorSpi() != null){
            properties.setProperty(Utils.Prop.COVERAGE_NAME_COLLECTOR_SPI, 
                    mosaicConfiguration.getCoverageNameCollectorSpi());
        }

        OutputStream outStream = null;
        String filePath = runConfiguration.getParameter(Prop.ROOT_MOSAIC_DIR) + "/"
        // + runConfiguration.getIndexName() + ".properties"));
                + mosaicConfiguration.getName() + ".properties";
        try {
            outStream = new BufferedOutputStream(new FileOutputStream(filePath));
            properties.store(outStream, "-Automagically created from GeoTools-");
        } catch (FileNotFoundException e) {
            eventHandler.fireEvent(Level.SEVERE, e.getLocalizedMessage(), 0);
        } catch (IOException e) {
            eventHandler.fireEvent(Level.SEVERE, e.getLocalizedMessage(), 0);
        } finally {
            if (outStream != null) {
                IOUtils.closeQuietly(outStream);
            }
        }
    }

    /**
     * Check whether the specified coverage already exist in the reader. This allows to get the rasterManager associated with that coverage instead of
     * creating a new one.
     * 
     * @param coverageName the name of the coverage to be searched
     * @return {@code true} in case that coverage already exists
     * @throws IOException
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
     * Use the passed coverageReader to create or update the all the needed configurations<br/>
     * It not responsible of the passed coverageReader which should be disposed outside (in the caller).
     * 
     * @param coverageReader
     * @param inputCoverageName
     * @param fileBeingProcessed
     * @param fileIndex
     * @param numFiles
     * @param transaction
     * @throws IOException
     */
    public void updateConfiguration(GridCoverage2DReader coverageReader,
            final String inputCoverageName, File fileBeingProcessed, int fileIndex, double numFiles,
            DefaultTransaction transaction) throws IOException {

        final String coverageName = getTargetCoverageName(coverageReader, inputCoverageName);

        final Indexer indexer = getRunConfiguration().getIndexer();

        // checking whether the coverage already exists
        final boolean coverageExists = coverageExists(coverageName);
        MosaicConfigurationBean mosaicConfiguration = null;
        MosaicConfigurationBean currentConfigurationBean = null;
        RasterManager rasterManager = null;
        if (coverageExists) {

            // Get the manager for this coverage so it can be updated
            rasterManager = getParentReader().getRasterManager(coverageName);
            mosaicConfiguration = rasterManager.getConfiguration();
        }

        // STEP 2
        // Collecting all Coverage properties to setup a MosaicConfigurationBean through
        // the builder
        final MosaicBeanBuilder configBuilder = new MosaicBeanBuilder();

        final GeneralEnvelope envelope = coverageReader.getOriginalEnvelope(inputCoverageName);
        final CoordinateReferenceSystem actualCRS = coverageReader
                .getCoordinateReferenceSystem(inputCoverageName);

        SampleModel sm = null;
        ColorModel cm = null;
        int numberOfLevels = 1;
        double[][] resolutionLevels = null;
        CatalogBuilderConfiguration catalogConfig;
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
            numberOfLevels = coverageReader.getNumOverviews(inputCoverageName) + 1;
            resolutionLevels = coverageReader.getResolutionLevels(inputCoverageName);

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
            configBuilder.setCrs(actualCRS);
            configBuilder.setLevels(resolutionLevels);
            configBuilder.setLevelsNum(numberOfLevels);
            configBuilder.setName(coverageName);
            configBuilder.setTimeAttribute(
                    IndexerUtils.getAttribute(coverageName, Utils.TIME_DOMAIN, indexer));
            configBuilder.setElevationAttribute(
                    IndexerUtils.getAttribute(coverageName, Utils.ELEVATION_DOMAIN, indexer));
            configBuilder.setAdditionalDomainAttributes(
                    IndexerUtils.getAttribute(coverageName, Utils.ADDITIONAL_DOMAIN, indexer));

            final Hints runHints = getRunConfiguration().getHints();
            if (runHints != null) {
                if (runHints.containsKey(Utils.AUXILIARY_FILES_PATH)) {
                    String auxiliaryFilePath = (String) runHints.get(Utils.AUXILIARY_FILES_PATH);
                    if (auxiliaryFilePath != null && auxiliaryFilePath.trim().length() > 0) {
                        configBuilder.setAuxiliaryFilePath(auxiliaryFilePath);
                    }
                }
                if (runHints.containsKey(Utils.AUXILIARY_DATASTORE_PATH)) {
                    String auxiliaryDatastorePath = (String) runHints
                            .get(Utils.AUXILIARY_DATASTORE_PATH);
                    if (auxiliaryDatastorePath != null
                            && auxiliaryDatastorePath.trim().length() > 0) {
                        configBuilder.setAuxiliaryDatastorePath(auxiliaryDatastorePath);
                    }
                }
            }

            final CatalogConfigurationBean catalogConfigurationBean = new CatalogConfigurationBean();
            catalogConfigurationBean
                    .setCaching(IndexerUtils.getParameterAsBoolean(Prop.CACHING, indexer));
            catalogConfigurationBean.setAbsolutePath(
                    IndexerUtils.getParameterAsBoolean(Prop.ABSOLUTE_PATH, indexer));

            catalogConfigurationBean.setLocationAttribute(
                    IndexerUtils.getParameter(Prop.LOCATION_ATTRIBUTE, indexer));
            catalogConfigurationBean
                    .setWrapStore(IndexerUtils.getParameterAsBoolean(Prop.WRAP_STORE, indexer));

            String configuredTypeName = IndexerUtils.getParameter(Prop.TYPENAME, indexer);
            if (configuredTypeName != null) {
                catalogConfigurationBean.setTypeName(configuredTypeName);
            } else {
                catalogConfigurationBean.setTypeName(coverageName);
            }
            configBuilder.setCatalogConfigurationBean(catalogConfigurationBean);
            configBuilder.setCheckAuxiliaryMetadata(
                    IndexerUtils.getParameterAsBoolean(Prop.CHECK_AUXILIARY_METADATA, indexer));

            currentConfigurationBean = configBuilder.getMosaicConfigurationBean();

            // Creating a rasterManager which will be initialized after populating the catalog
            rasterManager = getParentReader().addRasterManager(currentConfigurationBean, false);

            // Creating a granuleStore
            if (!useExistingSchema) {
                // creating the schema
                SimpleFeatureType indexSchema = CatalogManager.createSchema(getRunConfiguration(),
                        currentConfigurationBean.getName(), actualCRS);
                getParentReader().createCoverage(coverageName, indexSchema);
                // } else {
                // rasterManager.typeName = coverageName;
            }
            getConfigurations().put(currentConfigurationBean.getName(), currentConfigurationBean);

        } else {
            catalogConfig = new CatalogBuilderConfiguration();
            CatalogConfigurationBean bean = mosaicConfiguration.getCatalogConfigurationBean();
            catalogConfig.setParameter(Prop.LOCATION_ATTRIBUTE, (bean.getLocationAttribute()));
            catalogConfig.setParameter(Prop.ABSOLUTE_PATH, Boolean.toString(bean.isAbsolutePath()));
            catalogConfig.setParameter(Prop.ROOT_MOSAIC_DIR/* setRootMosaicDirectory( */,
                    getRunConfiguration().getParameter(Prop.ROOT_MOSAIC_DIR));

            // We already have a Configuration for this coverage.
            // Check its properties are compatible with the existing coverage.

            CatalogConfigurationBean catalogConfigurationBean = bean;

            // make sure we pick the same resolution irrespective of order of harvest
            numberOfLevels = coverageReader.getNumOverviews(inputCoverageName) + 1;
            resolutionLevels = coverageReader.getResolutionLevels(inputCoverageName);

            int originalNumberOfLevels = mosaicConfiguration.getLevelsNum();
            boolean needUpdate = false;
            if (Utils.homogeneousCheck(Math.min(numberOfLevels, originalNumberOfLevels),
                    resolutionLevels, mosaicConfiguration.getLevels())) {
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

            ImageLayout layout = coverageReader.getImageLayout(inputCoverageName);
            cm = layout.getColorModel(null);
            sm = layout.getSampleModel(null);

            // comparing ColorModel
            // comparing SampeModel
            // comparing CRSs
            ColorModel actualCM = cm;
            CoordinateReferenceSystem expectedCRS;
            if (mosaicConfiguration.getCrs() != null) {
                expectedCRS = mosaicConfiguration.getCrs();
            } else {
                expectedCRS = rasterManager.spatialDomainManager.coverageCRS;
            }
            if (!(CRS.equalsIgnoreMetadata(expectedCRS, actualCRS))) {
                // if ((fileIndex > 0 ? !(CRS.equalsIgnoreMetadata(defaultCRS, actualCRS)) : false)) {
                eventHandler.fireFileEvent(Level.INFO, fileBeingProcessed, false,
                        "Skipping image " + fileBeingProcessed + " because CRSs do not match.",
                        (((fileIndex + 1) * 99.0) / numFiles));
                return;
            }

            byte[][] palette = mosaicConfiguration.getPalette();
            ColorModel colorModel = mosaicConfiguration.getColorModel();
            if (colorModel == null) {
                colorModel = rasterManager.defaultCM;
            }
            if (palette == null) {
                palette = rasterManager.defaultPalette;
            }
            if (Utils.checkColorModels(colorModel, palette, actualCM)) {
                eventHandler.fireFileEvent(Level.INFO, fileBeingProcessed, false,
                        "Skipping image " + fileBeingProcessed
                                + " because color models do not match.",
                        (((fileIndex + 1) * 99.0) / numFiles));
                return;
            }

        }
        // STEP 3
        if (!useExistingSchema) {
            // create and store features
            CatalogManager.updateCatalog(coverageName, fileBeingProcessed, coverageReader,
                    getParentReader(), catalogConfig, envelope, transaction,
                    getPropertiesCollectors());
        }
    }

    /**
     * Get the name of the target coverage for a given reader. For most input coverages, the target coverage is simply the default coverage. For
     * structured coverages the target coverage has the same name as the input coverage.
     * 
     * @param inputCoverageReader the coverage being added to the index
     * @param inputCoverageName the name of the input coverage
     * @return the target coverage name for the input coverage
     */
    public String getTargetCoverageName(GridCoverage2DReader inputCoverageReader,
            String inputCoverageName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Prop.INDEX_NAME, getRunConfiguration().getParameter(Prop.INDEX_NAME));
        map.put(Prop.INPUT_COVERAGE_NAME, inputCoverageName);
        return coverageNameHandler.getTargetCoverageName(inputCoverageReader, map);
    }

    private boolean isHigherResolution(double[][] a, double[][] b) {
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            for (int j = 0; i < Math.min(a[i].length, b[i].length); i++) {
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
}
