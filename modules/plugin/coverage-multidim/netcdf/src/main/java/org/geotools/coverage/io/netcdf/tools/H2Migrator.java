/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.coverage.io.catalog.DataStoreConfiguration;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.imageio.netcdf.AncillaryFileManager;
import org.geotools.jdbc.Index;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;

/** Converts NetCDF slice H2 indexes towards a centralized database approach */
public class H2Migrator {

    static final Logger LOGGER = Logging.getLogger(H2Migrate.class);
    public static final String NETCDF_DATASTORE_PROPERTIES = "netcdf_datastore.properties";

    private final H2MigrateConfiguration configuration;

    public H2Migrator(H2MigrateConfiguration configuration) {
        this.configuration = configuration;
    }

    public void migrate() throws Exception {
        ExecutorService executorService =
                Executors.newFixedThreadPool(configuration.getConcurrency());
        DataStore targetStore =
                H2MigrateConfiguration.getDataStore(configuration.getTargetStoreConfiguration());
        File logDirectory = configuration.getLogDirectory();
        try (LogWriter netcdfWriter = new LogWriter(new File(logDirectory, "migrated.txt"));
                LogWriter h2Writer = new LogWriter(new File(logDirectory, "h2.txt"))) {
            // just to force an actual connection and verify it works
            targetStore.getTypeNames();

            // collect file paths via reader (TODO: implement direct store alternative for EO case)
            LinkedHashSet<String> filePaths = new LinkedHashSet<>();
            final String[] coverages;
            if (configuration.getSourceStoreConfiguration() != null) {
                coverages = getFilesFromStore(filePaths);
            } else {
                coverages = getFilesFromReader(filePaths);
            }

            // run the migration file by file
            Map<String, Future> futures = new HashMap<>();
            for (String path : filePaths) {
                futures.put(
                        path,
                        executorService.submit(
                                () ->
                                        migrateNetcdf(
                                                path,
                                                coverages,
                                                targetStore,
                                                netcdfWriter,
                                                h2Writer)));
            }
            for (Map.Entry<String, Future> entry : futures.entrySet()) {
                try {
                    entry.getValue().get();
                } catch (Exception e) {
                    if (configuration.isFailureIgnored()) {
                        LOGGER.log(Level.WARNING, "Failed to migrate file: " + entry.getKey(), e);
                    } else {
                        throw new MigrationException(
                                "Failed to migrate file: " + entry.getKey(), e);
                    }
                }
            }

            // create the store properties and update the indexer
            updateMosaicConfiguration(coverages);

            LOGGER.info("Migration complete with success!");
        } finally {
            executorService.shutdown();
            if (targetStore != null) targetStore.dispose();
        }
    }

    private String[] getFilesFromStore(LinkedHashSet<String> filePaths)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException,
                    IOException, NoSuchMethodException, InvocationTargetException {
        DataStore sourceStore =
                H2MigrateConfiguration.getDataStore(configuration.getSourceStoreConfiguration());
        final String[] indexTables =
                configuration.getIndexTables() != null
                        ? configuration.getIndexTables()
                        : getCoverageNames();
        try {
            final List<String> typeNames = Arrays.asList(sourceStore.getTypeNames());
            for (String indexTable : indexTables) {
                if (!typeNames.contains(indexTable)) {
                    throw new MigrationException(
                            "Could not find source index table "
                                    + indexTable
                                    + ", the available ones are "
                                    + typeNames);
                }

                final List<String> coverageFiles = collectFilesFromTable(sourceStore, indexTable);
                filePaths.addAll(coverageFiles);
            }
        } finally {
            if (sourceStore != null) {
                sourceStore.dispose();
            }
        }

        return indexTables;
    }

    /**
     * Returns the coverage names provided by the user, or tries to guess them by looking at
     * property files in the mosaic directory
     */
    private String[] getCoverageNames() {
        String[] names = configuration.getCoverageNames();
        if (names == null) {
            final File mosaicDirectory = configuration.getMosaicDirectory();
            // simplifying assumption, the coverages are the ones that have a config file with
            // a sidecar sample image file
            names =
                    Arrays.stream(
                                    mosaicDirectory.list(
                                            (dir, name) -> {
                                                if (!name.endsWith(".properties")) {
                                                    return false;
                                                }
                                                String baseName = FilenameUtils.getBaseName(name);
                                                return new File(
                                                                        dir,
                                                                        baseName
                                                                                + Utils
                                                                                        .SAMPLE_IMAGE_NAME)
                                                                .exists()
                                                        || new File(
                                                                        dir,
                                                                        baseName
                                                                                + Utils
                                                                                        .SAMPLE_IMAGE_NAME_LEGACY)
                                                                .exists();
                                            }))
                            .map(f -> FilenameUtils.getBaseName(f))
                            .toArray(n -> new String[n]);
        }

        return names;
    }

    private List<String> collectFilesFromTable(DataStore sourceStore, String table)
            throws IOException {
        final SimpleFeatureSource featureSource = sourceStore.getFeatureSource(table);
        final Properties properties = getCoverageConfiguration(table);
        String locationAttribute =
                getProperty(
                        properties,
                        Utils.Prop.LOCATION_ATTRIBUTE,
                        Utils.DEFAULT_LOCATION_ATTRIBUTE);
        PathType pathType =
                PathType.RELATIVE.valueOf(
                        getProperty(properties, Utils.Prop.PATH_TYPE, PathType.ABSOLUTE.name()));

        // query the location attribute
        Query q = new Query(table);
        q.setPropertyNames(new String[] {locationAttribute});
        // extract unique values
        UniqueVisitor uniqueLocations = new UniqueVisitor(locationAttribute);
        featureSource.getFeatures(q).accepts(uniqueLocations, null);
        Set<String> locations = uniqueLocations.getUnique();
        // map via pathtype and return
        return locations
                .stream()
                .map(l -> pathType.resolvePath(configuration.getMosaicDirectory().getPath(), l))
                .map(url -> URLs.urlToFile(url).getAbsolutePath())
                .collect(Collectors.toList());
    }

    private String getProperty(Properties properties, String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private Properties getCoverageConfiguration(String coverage) {
        final File mosaicDirectory = configuration.getMosaicDirectory();
        final File coverageConfig = new File(mosaicDirectory, coverage + ".properties");
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(coverageConfig)) {
            properties.load(is);
        } catch (IOException e) {
            throw new MigrationException(
                    "Could not open the image mosaic configuration file " + coverageConfig, e);
        }
        return properties;
    }

    public String[] getFilesFromReader(LinkedHashSet<String> filePaths) throws IOException {
        ImageMosaicReader reader = null;
        try {
            reader = new ImageMosaicReader(configuration.getMosaicDirectory());
            final String[] coverages = reader.getGridCoverageNames();
            for (String coverage : coverages) {
                final List<String> coverageFiles = collectFilesFromTable(reader, coverage);
                filePaths.addAll(coverageFiles);
            }
            return coverages;
        } finally {
            if (reader != null) reader.dispose();
        }
    }

    private List<String> collectFilesFromTable(ImageMosaicReader reader, String coverage)
            throws IOException {
        // grab the necessary background info
        final String mosaicDirectoryPath = configuration.getMosaicDirectory().getAbsolutePath();
        final GranuleSource granuleSource = reader.getGranules(coverage, true);
        final MosaicConfigurationBean configuration =
                reader.getRasterManager(coverage).getConfiguration();
        final CatalogConfigurationBean catalogConfiguration =
                configuration.getCatalogConfigurationBean();
        final PathType pathType = catalogConfiguration.getPathType();
        if (pathType != PathType.ABSOLUTE && pathType != PathType.RELATIVE) {
            throw new MigrationException("Cannot perform migration with path type " + pathType);
        }
        // query the location attribute
        final String locationAttribute = catalogConfiguration.getLocationAttribute();
        Query q = new Query(coverage);
        q.setPropertyNames(new String[] {locationAttribute});
        // extract unique values
        UniqueVisitor uniqueLocations = new UniqueVisitor(locationAttribute);
        final SimpleFeatureCollection granules = granuleSource.getGranules(q);
        granules.accepts(uniqueLocations, null);
        Set<String> locations = uniqueLocations.getUnique();
        // map via pathtype and return
        return locations
                .stream()
                .map(l -> pathType.resolvePath(mosaicDirectoryPath, l))
                .map(url -> URLs.urlToFile(url).getAbsolutePath())
                .collect(Collectors.toList());
    }

    private void updateMosaicConfiguration(String[] coverageNames)
            throws JAXBException, IOException {
        // create the aux store config file
        File netcdfStore =
                new File(configuration.getMosaicDirectory(), NETCDF_DATASTORE_PROPERTIES);
        try (OutputStream os = new FileOutputStream(netcdfStore)) {
            if (configuration.getIndexStoreName() != null) {
                Properties properties = new Properties();
                properties.put(Utils.Prop.STORE_NAME, configuration.getIndexStoreName());
                properties.store(os, null);
            } else {
                configuration.getTargetStoreConfiguration().store(os, null);
            }
        }

        // read, update and rewrite the indexer file
        final File indexerFile = new File(configuration.getMosaicDirectory(), "indexer.xml");
        final Indexer indexer = Utils.unmarshal(indexerFile);
        final List<Parameter> parameters = indexer.getParameters().getParameter();
        Optional<Parameter> indexerParameter =
                getParameter(Utils.Prop.AUXILIARY_DATASTORE_FILE, parameters);
        if (indexerParameter.isPresent()) {
            indexerParameter.get().setValue(NETCDF_DATASTORE_PROPERTIES);
        } else {
            final Parameter param = new Parameter();
            param.setName(Utils.Prop.AUXILIARY_DATASTORE_FILE);
            param.setValue(NETCDF_DATASTORE_PROPERTIES);
            parameters.add(param);
        }
        Optional<Parameter> auxParameter = getParameter(Utils.Prop.AUXILIARY_FILE, parameters);
        if (!auxParameter.isPresent()) {
            final Parameter param = new Parameter();
            param.setName(Utils.Prop.AUXILIARY_FILE);
            param.setValue("_auxiliary.xml");
            parameters.add(param);
        }
        Utils.marshal(indexer, indexerFile);
        LOGGER.info("Indexer.xml updated with auxiliary data store!");

        // read, update and rewrite the coverage config files
        for (String coverageName : coverageNames) {
            File configFile =
                    new File(configuration.getMosaicDirectory(), coverageName + ".properties");
            Properties properties = new Properties();
            try (FileInputStream is = new FileInputStream(configFile)) {
                properties.load(is);
            }
            properties.put(Utils.Prop.AUXILIARY_DATASTORE_FILE, NETCDF_DATASTORE_PROPERTIES);
            if (properties.get(Utils.Prop.AUXILIARY_FILE) == null) {
                properties.put(Utils.Prop.AUXILIARY_FILE, "_auxiliary.xml");
            }
            try (FileOutputStream os = new FileOutputStream(configFile)) {
                properties.store(os, null);
            }
            LOGGER.info(configFile.getName() + " updated with auxiliary data store!");
        }
    }

    private Optional<Parameter> getParameter(String name, List<Parameter> parameters) {
        return parameters.stream().filter(p -> name.equalsIgnoreCase(p.getName())).findFirst();
    }

    Void migrateNetcdf(
            String path,
            String[] coverages,
            DataStore targetStore,
            LogWriter netcdfWriter,
            LogWriter h2Writer)
            throws IOException, JAXBException, NoSuchAlgorithmException {
        final AncillaryFileManager provider =
                new AncillaryFileManager(new File(path), null) {
                    @Override
                    protected void initIndexer() {
                        // do not init, we just need to return the info
                    }
                };
        final DataStoreConfiguration config = provider.getDatastoreConfiguration();
        if (!(config.getDatastoreSpi() instanceof H2DataStoreFactory)) {
            throw new MigrationException(
                    "The NetCDF index datastore is not a H2, but "
                            + config.getDatastoreSpi()
                            + ", the migration was not designed to handle that case");
        }
        final DataStore sourceDataStore =
                config.getDatastoreSpi().createDataStore(config.getParams());
        Transaction t = new DefaultTransaction();
        try {
            final Set<String> typeNames =
                    new HashSet<>(Arrays.asList(sourceDataStore.getTypeNames()));
            // shuffle coverages to avoid all threads accumulating on the same coverage
            List<String> shuffledCoverages = new ArrayList<>(Arrays.asList(coverages));
            Collections.shuffle(shuffledCoverages);
            for (String coverage : shuffledCoverages) {
                // in case of one coverage per file, not all files contain all coverages
                if (typeNames.contains(coverage)) {
                    LOGGER.info("Migrating " + path + ":" + coverage);
                    final SimpleFeatureSource source = sourceDataStore.getFeatureSource(coverage);
                    final SimpleFeatureStore store =
                            getTargetFeatureStore(source.getSchema(), targetStore, coverage);
                    SimpleFeatureCollection indexWithLocation =
                            new LocationFeatureCollection(
                                    source.getFeatures(), path, store.getSchema());
                    store.setTransaction(t);
                    store.addFeatures(indexWithLocation);
                    LOGGER.info("Migration for " + path + ":" + coverage + " succesfull");
                }
            }
            t.commit();
            netcdfWriter.addLines(path);
            h2Writer.addLines(collectH2Files(config));
        } finally {
            t.close();
            if (sourceDataStore != null) {
                sourceDataStore.dispose();
            }
        }

        // just to make this compatible with Callable<Void>
        return null;
    }

    private String[] collectH2Files(DataStoreConfiguration config) throws IOException {
        String database = (String) config.getParams().get("database");
        if (database.startsWith("file:")) {
            database = database.substring("file:".length());
        }
        final File auxDirectory = new File(database).getParentFile();
        final String databaseName = new File(database).getName();
        return Files.list(auxDirectory.toPath())
                .filter(p -> isH2DatabaseFile(databaseName, p))
                .map(p -> p.toAbsolutePath().toString())
                .toArray(n -> new String[n]);
    }

    private boolean isH2DatabaseFile(String databaseName, Path p) {
        Path pfn = p.getFileName();
        if (pfn == null) {
            return false;
        }
        final String fileName = pfn.toString();
        boolean result = fileName.startsWith(databaseName) && fileName.endsWith(".db");
        return result;
    }

    private SimpleFeatureStore getTargetFeatureStore(
            SimpleFeatureType sourceSchema, DataStore targetStore, String coverage)
            throws IOException {
        // do we have to create it?
        List<String> existingTypeNames = Arrays.asList(targetStore.getTypeNames());
        if (!existingTypeNames.contains(coverage)) {
            synchronized (coverage) {
                existingTypeNames = Arrays.asList(targetStore.getTypeNames());
                if (!existingTypeNames.contains(coverage)) {
                    final SimpleFeatureType schema = buildTargetSchema(sourceSchema);
                    targetStore.createSchema(schema);
                    // create indexes if we can
                    if (targetStore instanceof JDBCDataStore) {
                        ((JDBCDataStore) targetStore)
                                .createIndex(
                                        new Index(
                                                schema.getTypeName(),
                                                schema.getTypeName() + "_loc_idx",
                                                true,
                                                "location",
                                                CoverageSlicesCatalog.IMAGE_INDEX_ATTR));
                    }
                }
            }
        }
        // TODO: handle Oracle casing issue?

        return (SimpleFeatureStore) targetStore.getFeatureSource(coverage);
    }

    private SimpleFeatureType buildTargetSchema(SimpleFeatureType sourceSchema) {
        if (sourceSchema.getDescriptor("location") != null) {
            return sourceSchema;
        }
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(sourceSchema);
        tb.add("location", String.class);
        return tb.buildFeatureType();
    }
}
