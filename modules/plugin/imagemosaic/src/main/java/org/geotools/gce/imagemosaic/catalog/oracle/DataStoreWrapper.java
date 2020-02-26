/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.oracle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.transform.Definition;
import org.geotools.data.transform.TransformFactory;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.CRS;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A data store wrapper around a {@link DataStore} object.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS @TODO move that class on gt-transform once ready
 */
public abstract class DataStoreWrapper implements DataStore {

    protected static final String HIDDEN_FOLDER = ".mosaic";

    protected static final String NAME = "NAME";

    protected static final String MAPPEDNAME = "MAPPEDNAME";

    protected static final String SCHEMA = "SCHEMA";

    protected static final String COORDINATE_REFERENCE_SYSTEM = "CRS";

    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DataStoreWrapper.class);

    /** Auxiliary folder which contains properties file with mapping information */
    protected File auxiliaryFolder;

    /** The underlying datastore */
    protected final DataStore datastore;

    /** Mapping between typeNames and FeatureTypeMapper */
    protected final Map<Name, FeatureTypeMapper> mapping =
            new ConcurrentHashMap<Name, FeatureTypeMapper>();

    /** Quick access typeNames list */
    private List<String> typeNames = new ArrayList<String>();

    /** Base constructor */
    public DataStoreWrapper(DataStore datastore, String auxFolderPath) {
        this(datastore, auxFolderPath, HIDDEN_FOLDER);
    }

    /** Base constructor with custom hidden folder */
    public DataStoreWrapper(DataStore datastore, String auxFolderPath, String subFolderName) {
        this.datastore = datastore;
        initMapping(auxFolderPath + File.separatorChar + subFolderName);
    }

    /**
     * Initialize the mapping by creating proper {@link FeatureTypeMapper}s on top of the available
     * property files which contain mapping information.
     *
     * @param auxFolderPath the path of the folder containing mapping properties files
     */
    private void initMapping(String auxFolderPath) {
        URL url;
        try {
            url = new URL(auxFolderPath);
            File file = URLs.urlToFile(url);
            if (!file.exists()) {
                // Pre-create folder when missing
                file.mkdir();
            } else if (file.isDirectory() || file.canRead()) {
                loadMappers(file);
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "The specified config folder for datastore wrapping can't be read or isn't a directory: "
                                    + auxFolderPath);
                }
            }
            this.auxiliaryFolder = file;
        } catch (MalformedURLException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "The specified config folder for datastore wrapping is not valid: "
                                + auxFolderPath);
            }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.severe(
                        "Unable to initialize the wrapping mapping for this folder: "
                                + auxFolderPath);
            }
        }
    }

    /**
     * Load information from property files and initialize the related {@link FeatureTypeMapper}s
     */
    private void loadMappers(final File file) throws Exception {
        // TODO we should do a lazy load initialization
        final String[] files = file.list();
        final String parentPath = file.getAbsolutePath();

        // Loop over files
        if (files != null) {
            for (String element : files) {
                final Properties properties =
                        loadProperties(parentPath + File.separatorChar + element);
                final FeatureTypeMapper mapper = getFeatureTypeMapper(properties);
                final Name name = mapper.getName();
                mapping.put(name, mapper);
                typeNames.add(name.getLocalPart());
            }
        }
    }

    /** Utility method which load mapping properties from a propertiesFile. */
    private static Properties loadProperties(final String propertiesFile) {
        Properties properties = new Properties();
        File propertiesFileP = new File(propertiesFile);
        try (InputStream inStream = new BufferedInputStream(new FileInputStream(propertiesFileP))) {
            properties.load(inStream);
        } catch (FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Unable to store the mapping " + e.getLocalizedMessage());
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Unable to store the mapping " + e.getLocalizedMessage());
            }
        }
        return properties;
    }

    @Override
    public ServiceInfo getInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        if (featureType != null) {
            Name name = featureType.getName();

            // Initialize mapping
            try {
                // Get a mapper for that featureType
                final FeatureTypeMapper mapper = getFeatureTypeMapper(featureType);

                // Get the transformed featureType
                final SimpleFeatureType mappedFeatureType = mapper.getMappedFeatureType();
                String typeName = mappedFeatureType.getTypeName();
                boolean exists = false;
                try {
                    datastore.getSchema(typeName);
                    exists = true;
                } catch (IOException ioe) {

                }
                if (exists) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Schema already exists: " + typeName);
                    }
                } else {
                    datastore.createSchema(mappedFeatureType);
                }
                mapping.put(name, mapper);
                typeNames.add(name.getLocalPart());

                // Store the mapper
                storeMapper(mapper);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    /** Store the properties on disk */
    protected void storeProperties(Properties properties, String typeName) {
        final String propertiesPath =
                auxiliaryFolder.getAbsolutePath() + File.separatorChar + typeName + ".properties";
        try (OutputStream outStream =
                new BufferedOutputStream(new FileOutputStream(new File(propertiesPath)))) {
            properties.store(outStream, null);
        } catch (FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Unable to store the mapping " + e.getLocalizedMessage());
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Unable to store the mapping " + e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Name> getNames() throws IOException {
        return new ArrayList<Name>(mapping.keySet());
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        if (mapping.containsKey(name)) {
            FeatureTypeMapper mapper = mapping.get(name);
            if (mapper != null) {
                return mapper.getWrappedFeatureType();
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("No schema found for that name: " + name + "\nNo mappers available");
        }
        return null;
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return getSchema(new NameImpl(typeName));
    }

    @Override
    public void dispose() {
        datastore.dispose();
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        FeatureTypeMapper mapper = getMapper(typeName);
        if (mapper == null) {
            throw new IOException("No wrapper found for " + typeName);
        } else {
            String mappedName = mapper.getMappedName();
            datastore.removeSchema(mappedName);
        }
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        removeSchema(new NameImpl(typeName));
    }

    @Override
    public String[] getTypeNames() throws IOException {
        return typeNames != null
                ? (String[]) typeNames.toArray(new String[typeNames.size()])
                : null;
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        Utilities.ensureNonNull("typeName", typeName);
        return getFeatureSource(new NameImpl(typeName));
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        FeatureTypeMapper mapper = getMapper(typeName);
        if (mapper == null) {
            throw new IOException("No wrapper found for " + typeName);
        } else {
            SimpleFeatureStore source =
                    (SimpleFeatureStore) datastore.getFeatureSource(mapper.getMappedName());
            if (source == null) {
                throw new IOException("No feature source available for " + typeName);
            }
            return transformFeatureStore(source, mapper);
        }
    }

    protected SimpleFeatureSource transformFeatureStore(
            SimpleFeatureStore source, FeatureTypeMapper mapper) throws IOException {
        return TransformFactory.transform(source, mapper.getName(), mapper.getDefinitions());
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            Query query, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            String typeName, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public LockingManager getLockingManager() {
        return datastore.getLockingManager();
    }

    /** Return the mapper for the specified typeName */
    private FeatureTypeMapper getMapper(Name typeName) {
        FeatureTypeMapper mapper = null;
        Utilities.ensureNonNull("typeName", typeName);
        if (mapping.containsKey(typeName)) {
            mapper = mapping.get(typeName);
        }
        return mapper;
    }

    /** Store the {@link FeatureTypeMapper} instance */
    protected void storeMapper(FeatureTypeMapper mapper) {
        final Properties properties = new Properties();
        final String typeName = mapper.getName().toString();
        properties.setProperty(NAME, typeName);
        properties.setProperty(MAPPEDNAME, mapper.getMappedName().toString());
        final List<Definition> definitions = mapper.getDefinitions();
        final StringBuilder builder = new StringBuilder();

        // Populating schema
        for (Definition definition : definitions) {
            builder.append(definition.getName())
                    .append(":")
                    .append(definition.getBinding().getName())
                    .append(",");
        }
        String schema = builder.toString();
        schema = schema.substring(0, schema.length() - 1);
        properties.setProperty(SCHEMA, schema);
        properties.setProperty(
                COORDINATE_REFERENCE_SYSTEM, mapper.getCoordinateReferenceSystem().toWKT());

        // Storing properties
        storeProperties(properties, typeName);
    }

    /**
     * Return a specific {@link FeatureTypeMapper} by parsing mapping properties contained within
     * the specified {@link Properties} object
     */
    protected FeatureTypeMapper getFeatureTypeMapper(final Properties props) throws Exception {
        SimpleFeatureType indexSchema;
        // Creating schema
        indexSchema = DataUtilities.createType(props.getProperty(NAME), props.getProperty(SCHEMA));
        CoordinateReferenceSystem crs =
                CRS.parseWKT(props.getProperty(COORDINATE_REFERENCE_SYSTEM));
        indexSchema =
                DataUtilities.createSubType(
                        indexSchema, DataUtilities.attributeNames(indexSchema), crs);
        return getFeatureTypeMapper(indexSchema);
    }

    /** Return a specific {@link FeatureTypeMapper} instance on top of an input featureType */
    protected abstract FeatureTypeMapper getFeatureTypeMapper(final SimpleFeatureType featureType)
            throws Exception;
}
