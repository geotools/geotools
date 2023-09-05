/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Repository;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.appschema.util.InterpolationProperties;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * A registry that stores data access instances per application. This allows feature sources from
 * different data accesses to be accessed globally.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @author Niels Charlier (Curtin University Of Technology)
 */
public class DataAccessRegistry implements Repository {

    private static final long serialVersionUID = -373404928035022963L;

    private static final Logger LOGGER = Logging.getLogger(DataAccessRegistry.class);

    /** Singleton instance */
    protected static volatile DataAccessRegistry theRegistry = null;

    /** Properties for interpolation / configuration settings */
    protected InterpolationProperties properties = null;

    /** Data Access Resources */
    protected List<DataAccess<FeatureType, Feature>> registry = new ArrayList<>();

    /** Sole constructor */
    protected DataAccessRegistry() {}

    /**
     * Public method to get singleton instance to registry.
     *
     * @return An instance of this class
     */
    public static DataAccessRegistry getInstance() {
        if (theRegistry == null) {
            theRegistry = new DataAccessRegistry();
        }
        return theRegistry;
    }

    /**
     * Get a feature source for built features with supplied feature type name.
     *
     * @return feature source
     */
    public synchronized FeatureSource<FeatureType, Feature> featureSource(Name name)
            throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess.getNames().contains(name)) {
                if (dataAccess instanceof AppSchemaDataAccess) {
                    return ((AppSchemaDataAccess) dataAccess).getFeatureSourceByName(name);
                } else {
                    return dataAccess.getFeatureSource(name);
                }
            }
        }
        throwDataSourceException(name);

        return null;
    }

    @Override
    public synchronized DataAccess<FeatureType, Feature> access(Name name) {
        try {
            return featureSource(name).getDataStore();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public DataStore dataStore(Name name) {
        throw new UnsupportedOperationException(
                "Simple feature DataStores not supported by app-schema registry.");
    }

    @Override
    public List<DataStore> getDataStores() {
        throw new UnsupportedOperationException(
                "Simple feature DataStores not supported by app-schema registry.");
    }

    /**
     * Registers a data access
     *
     * @param dataAccess Data access to be registered
     */
    public synchronized void registerAccess(DataAccess<FeatureType, Feature> dataAccess) {
        registry.add(dataAccess);
    }

    /**
     * Unregister a data access. This is important especially at the end of test cases, so that the
     * mappings contained in the data access do not conflict with mappings of the same type used in
     * other tests.
     *
     * @param dataAccess Data access to be unregistered
     */
    public synchronized void unregisterAccess(DataAccess<FeatureType, Feature> dataAccess) {
        registry.remove(dataAccess);

        if (dataAccess instanceof AppSchemaDataAccess) {
            AppSchemaDataAccess asda = (AppSchemaDataAccess) dataAccess;
            // NOTE: this code assumes hidden data accesses are never removed directly by the user,
            // only by the automatic disposal algorithm, so no need to run it again
            if (!asda.hidden && asda.url != null) {
                try {
                    disposeHiddenDataAccessInstances(asda.url);
                } catch (IOException e) {
                    LOGGER.log(
                            Level.SEVERE,
                            "Exception occurred disposing unused data access instances",
                            e);
                }
            }
        }
    }

    // utility method to clear up hidden app-schema data accesses (i.e. configured via a separate
    // mapping file, specified in the <includedTypes>
    // directive of some top-level app-schema data access) that are no longer needed (i.e. they are
    // not referenced by any top-level data access).
    private void disposeHiddenDataAccessInstances(URL url) throws IOException {
        List<DataAccess<FeatureType, Feature>> copyRegistry = new ArrayList<>(registry);
        for (DataAccess<FeatureType, Feature> da : copyRegistry) {
            if (da instanceof AppSchemaDataAccess) {
                AppSchemaDataAccess asda = (AppSchemaDataAccess) da;
                if (asda.hidden && asda.parentUrl != null && asda.parentUrl.equals(url)) {
                    asda.dispose();
                }
            }
        }
    }

    /**
     * Dispose and unregister all data accesses in the registry. This is may be needed to prevent
     * unit tests from conflicting with data accesses with the same type name registered for other
     * tests.
     */
    public synchronized void disposeAndUnregisterAll() {
        List<DataAccess<FeatureType, Feature>> copyRegistry = new ArrayList<>(registry);
        for (DataAccess<FeatureType, Feature> da : copyRegistry) {
            da.dispose();
        }
        registry.clear();
    }

    /**
     * Return true if a type name is mapped in one of the registered data accesses. If the type
     * mapping has mappingName, then it will be the key that is matched in the search. If it
     * doesn't, then it will match the targetElementName.
     *
     * @param name Feature type name
     */
    public synchronized boolean hasAccessName(Name name) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess.getNames().contains(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses. If
     * the type mapping has mappingName, then it will be the key that is matched in the search. If
     * it doesn't, then it will match the targetElementName.
     *
     * @param name Feature type name
     */
    public synchronized boolean hasAppSchemaAccessName(Name name) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess
                    && (((AppSchemaDataAccess) dataAccess).hasName(name)
                            || ((AppSchemaDataAccess) dataAccess).hasElement(name))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a feature type mapping from a registered app-schema data access. Please note that this is
     * only possible for app-schema data access instances.
     *
     * @return feature type mapping
     */
    public synchronized FeatureTypeMapping mappingByName(Name name) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess) {
                if (((AppSchemaDataAccess) dataAccess).hasName(name)) {
                    return ((AppSchemaDataAccess) dataAccess).getMappingByName(name);
                }
            }
        }
        throwDataSourceException(name);

        return null;
    }

    public synchronized FeatureTypeMapping mappingByElement(Name name) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess) {
                if (((AppSchemaDataAccess) dataAccess).hasElement(name)) {
                    return ((AppSchemaDataAccess) dataAccess).getMappingByNameOrElement(name);
                }
            }
        }
        throwDataSourceException(name);

        return null;
    }

    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses as
     * targetElementName, regardless whether or not mappingName exists.
     */
    public synchronized boolean hasAppSchemaTargetElement(Name name) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess
                    && Arrays.asList(((AppSchemaDataAccess) dataAccess).getTypeNames())
                            .contains(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get properties
     *
     * @return properties
     */
    public synchronized InterpolationProperties getProperties() {
        if (properties == null) {
            properties = new InterpolationProperties(AppSchemaDataAccessFactory.DBTYPE_STRING);
        }
        return properties;
    }

    /** Clean-up properties, mainly used for cleaning up after tests */
    public synchronized void clearProperties() {
        properties = null;
    }

    // -------------------------------------------------------------------------------------
    // Static short-cut methods for convenience and backward compatibility
    // -------------------------------------------------------------------------------------

    /**
     * Get a feature source for built features with supplied feature type name.
     *
     * @return feature source
     */
    public static FeatureSource<FeatureType, Feature> getFeatureSource(Name featureTypeName)
            throws IOException {
        return getInstance().featureSource(featureTypeName);
    }

    public static DataAccess<FeatureType, Feature> getDataAccess(Name featureTypeName)
            throws IOException {
        return getInstance().featureSource(featureTypeName).getDataStore();
    }

    /**
     * Registers a data access
     *
     * @param dataAccess Data access to be registered
     */
    public static void register(DataAccess<FeatureType, Feature> dataAccess) {
        getInstance().registerAccess(dataAccess);
    }

    /**
     * Unregister a data access. This is important especially at the end of test cases, so that the
     * mappings contained in the data access do not conflict with mappings of the same type used in
     * other tests. * Does not dispose * This method should not be called directly, instead use
     * dispose method from DataAccess
     *
     * @param dataAccess Data access to be unregistered
     */
    public static void unregister(DataAccess<FeatureType, Feature> dataAccess) {
        getInstance().unregisterAccess(dataAccess);
    }

    /**
     * Unregister * and dispose * all data accesses in the registry. This is may be needed to
     * prevent unit tests from conflicting with data accesses with the same type name registered for
     * other tests.
     */
    public static void unregisterAndDisposeAll() {
        getInstance().disposeAndUnregisterAll();
    }

    /**
     * Return true if a type name is mapped in one of the registered data accesses. If the type
     * mapping has mappingName, then it will be the key that is matched in the search. If it
     * doesn't, then it will match the targetElementName.
     *
     * @param featureTypeName Feature type name
     */
    public static boolean hasName(Name featureTypeName) throws IOException {
        return getInstance().hasAccessName(featureTypeName);
    }

    // ---------------------------------------------------------------------------------------
    // helper methods
    // ---------------------------------------------------------------------------------------

    /**
     * Throws data source exception if mapping is not found.
     *
     * @param featureTypeName Name of feature type
     */
    protected void throwDataSourceException(Name featureTypeName) throws IOException {
        List<Name> typeNames = new ArrayList<>();
        for (DataAccess<FeatureType, Feature> featureTypeFeatureDataAccess : registry) {
            typeNames.addAll(featureTypeFeatureDataAccess.getNames());
        }
        throw new DataSourceException(
                "Feature type "
                        + featureTypeName
                        + " not found."
                        + " Has the data access been registered in DataAccessRegistry?"
                        + " Available: "
                        + typeNames.toString());
    }

    public Feature findFeature(FeatureId id, Hints hints) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            if (dataAccess instanceof AppSchemaDataAccess) {
                Feature feature = ((AppSchemaDataAccess) dataAccess).findFeature(id, hints);
                if (feature != null) {
                    return feature;
                }
            }
        }
        return null;
    }
}
