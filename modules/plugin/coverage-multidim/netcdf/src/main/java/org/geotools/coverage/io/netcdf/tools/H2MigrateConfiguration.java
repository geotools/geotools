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
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.Converters;

/** Configuration for NetCDF sidecar H2 index to target store migration */
public class H2MigrateConfiguration {

    private String indexStoreName;

    /**
     * Builds a datastore from a property file configuration, using GeoTools default convention as
     * well as ImageMosaic own one
     */
    public static DataStore getDataStore(Properties configuration)
            throws IOException, ClassNotFoundException, IllegalAccessException,
                    InstantiationException, NoSuchMethodException, InvocationTargetException {
        // first try out the default GeoTools approach
        final DataStore dataStore = DataStoreFinder.getDataStore(configuration);
        if (dataStore == null) {
            // use ImageMosaic bizarre own way
            final String spiClass = (String) configuration.get("SPI");
            DataStoreFactorySpi spi =
                    (DataStoreFactorySpi)
                            Class.forName(spiClass).getDeclaredConstructor().newInstance();
            Map<String, Serializable> datastoreParams =
                    Utils.filterDataStoreParams(configuration, spi);
            return spi.createDataStore(datastoreParams);
        }

        return null;
    }

    public static class ConfigurationException extends RuntimeException {
        public ConfigurationException(String message) {
            super(message);
        }

        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private File mosaicDirectory;
    private boolean failureIgnored;
    private int concurrency;
    private File logDirectory;
    private Properties sourceStoreConfiguration;
    private String[] coverageNames;
    private Properties targetStoreConfiguration;
    private String[] indexTables;

    public void setMosaicPath(String mosaicPath) {
        this.mosaicDirectory = new File(mosaicPath);
        if (!mosaicDirectory.exists() && !mosaicDirectory.isDirectory()) {
            throw new ConfigurationException(
                    "Mosaic directory does not exist or is not a directory " + mosaicDirectory);
        }
    }

    public void setTargetStoreConfiguration(String configPath) {
        Properties properties = loadStoreProperties(configPath);

        this.targetStoreConfiguration = properties;
    }

    public void setSourceStoreConfiguration(String configPath) {
        Properties properties = loadStoreProperties(configPath);

        this.sourceStoreConfiguration = properties;
    }

    public Properties loadStoreProperties(String configPath) {
        File configFile = new File(configPath);
        if (!configFile.exists() || !configFile.isFile()) {
            throw new ConfigurationException(
                    "Target store configuration file does not exist: " + configPath);
        }
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(configFile)) {
            properties.load(is);
        } catch (IOException e) {
            throw new ConfigurationException(
                    "Could not load the store configuration from: " + configPath, e);
        }
        try {
            final DataStore dataStore = getDataStore(properties);
            if (dataStore == null) {
                throw new ConfigurationException(
                        "No datastore could be loaded with the given properties, make sure you have all the required jars for it in the classpath. Tried to use: "
                                + configPath);
            }
            dataStore.dispose();
        } catch (Exception e) {
            throw new ConfigurationException(
                    "Failed to load data store from configuration at "
                            + configPath
                            + ": "
                            + e.getMessage(),
                    e);
        }
        return properties;
    }

    public void setFailureIgnored(boolean failureIgnored) {
        this.failureIgnored = failureIgnored;
    }

    public boolean isFailureIgnored() {
        return failureIgnored;
    }

    public void setConcurrency(String concurrencySpec) {
        final Integer c = Converters.convert(concurrencySpec, Integer.class);
        if (c == null) {
            throw new ConfigurationException("Invalid concurrency level " + concurrencySpec);
        }
        setConcurrency(c);
    }

    public void setConcurrency(int concurrency) {
        if (concurrency < 1) {
            throw new ConfigurationException(
                    "Invalid concurrency level, must be a positive number " + concurrency);
        }
        this.concurrency = concurrency;
    }

    public void setLogDirectory(String logDirectoryPath) {
        this.logDirectory = new File(logDirectoryPath);
        if (!logDirectory.exists() && !logDirectory.isDirectory()) {
            throw new ConfigurationException(
                    "Invalid log file location, does not exist or it's not a directory "
                            + logDirectoryPath);
        }
    }

    public File getLogDirectory() {
        return logDirectory;
    }

    public void setMosaicDirectory(File mosaicDirectory) {
        this.mosaicDirectory = mosaicDirectory;
    }

    public File getMosaicDirectory() {
        return mosaicDirectory;
    }

    public Properties getTargetStoreConfiguration() {
        return targetStoreConfiguration;
    }

    public Properties getSourceStoreConfiguration() {
        return sourceStoreConfiguration;
    }

    public int getConcurrency() {
        return concurrency <= 0 ? 1 : concurrency;
    }

    public void setCoverageNames(String[] coverageNames) {
        this.coverageNames = coverageNames;
    }

    public String[] getCoverageNames() {
        return coverageNames;
    }

    public void setIndexStoreName(String indexStoreName) {
        this.indexStoreName = indexStoreName;
    }

    public String getIndexStoreName() {
        return indexStoreName;
    }

    public String[] getIndexTables() {
        return indexTables;
    }

    public void setIndexTables(String[] indexTables) {
        this.indexTables = indexTables;
    }
}
