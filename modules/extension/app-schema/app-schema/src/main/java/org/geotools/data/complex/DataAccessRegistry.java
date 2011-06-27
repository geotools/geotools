/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.Repository;
import org.geotools.util.InterpolationProperties;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * A registry that stores data access instances per application. This allows feature sources from
 * different data accesses to be accessed globally.
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @author Niels Charlier (Curtin University Of Technology)
 *
 */
public class DataAccessRegistry implements Repository {

    private static final long serialVersionUID = -373404928035022963L;
    
    /**
     * Singleton instance
     */
    protected static DataAccessRegistry theRegistry = null;
    
    /**
     * Properties for interpolation / configuration settings
     */
    protected InterpolationProperties properties = null;
    
    /**
     * Data Access Resources
     */
    protected List<DataAccess<FeatureType, Feature>> registry = new ArrayList<DataAccess<FeatureType, Feature>>();
    
    /**
     * Sole constructor
     */
    protected DataAccessRegistry() {
    }

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
     * @param featureTypeName
     * @return feature source
     * @throws IOException
     */
    public synchronized FeatureSource<FeatureType, Feature> featureSource(Name name) throws IOException {
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
    
    public synchronized DataAccess<FeatureType, Feature> access(Name name) {
        try {
            return featureSource(name).getDataStore();
        } catch (IOException e) {
            return null;
        }
    }

    public DataStore dataStore(Name name) {
        throw new UnsupportedOperationException("Simple feature DataStores not supported by app-schema registry.");
    }

    public List<DataStore> getDataStores() {
        throw new UnsupportedOperationException("Simple feature DataStores not supported by app-schema registry.");
    }
    
    /**
     * Registers a data access
     * 
     * @param dataAccess
     *            Data access to be registered
     */
    public synchronized void registerAccess(DataAccess<FeatureType, Feature> dataAccess) {
        registry.add(dataAccess);
    }
    
    /**
     * Unregister a data access. This is important especially at the end of test cases, so that the
     * mappings contained in the data access do not conflict with mappings of the same type used in
     * other tests.
     * 
     * @param dataAccess
     *            Data access to be unregistered
     */
    public synchronized void unregisterAccess(DataAccess<FeatureType, Feature> dataAccess) {
        registry.remove(dataAccess);
    }
    
    /**
     * Dispose and unregister all data accesses in the registry. This is may be needed to prevent unit tests
     * from conflicting with data accesses with the same type name registered for other tests.
     */
    public synchronized void disposeAndUnregisterAll() {
        List<DataAccess<FeatureType, Feature>> copyRegistry = new ArrayList<DataAccess<FeatureType, Feature>>(registry);
        for (DataAccess<FeatureType, Feature> da : copyRegistry) {
            da.dispose();
        }
        registry.clear();
    }
    
    /**
     * Return true if a type name is mapped in one of the registered data accesses. If
     * the type mapping has mappingName, then it will be the key that is matched in the search. If
     * it doesn't, then it will match the targetElementName.
     * 
     * @param featureTypeName
     *            Feature type name
     * @return
     * @throws IOException
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
     * @param featureTypeName
     *            Feature type name
     * @return
     * @throws IOException
     */
    public synchronized boolean hasAppSchemaAccessName(Name name) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess
                    && (((AppSchemaDataAccess) dataAccess).hasName(name) || ((AppSchemaDataAccess) dataAccess)
                            .hasElement(name))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get a feature type mapping from a registered app-schema data access. Please note that this is
     * only possible for app-schema data access instances.
     * 
     * @param featureTypeName
     * @return feature type mapping
     * @throws IOException
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
                    return ((AppSchemaDataAccess) dataAccess).getMappingByElement(name);
                }
            }
        }
        throwDataSourceException(name);

        return null;
    }
    
    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses as
     * targetElementName, regardless whether or not mappingName exists.
     * 
     * @param featureTypeName
     * @return
     * @throws IOException
     */
    public synchronized boolean hasAppSchemaTargetElement(Name name) throws IOException {
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess
                    && Arrays.asList(((AppSchemaDataAccess) dataAccess).getTypeNames()).contains(name)) {
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
    
    /**
     * Clean-up properties, mainly used for cleaning up after tests
     */
    public synchronized void clearProperties() {
        properties = null;
    }
    
    
    //-------------------------------------------------------------------------------------
    // Static short-cut methods for convenience and backward compatibility
    //-------------------------------------------------------------------------------------
    
    /**
     * Get a feature source for built features with supplied feature type name.
     * 
     * @param featureTypeName
     * @return feature source
     * @throws IOException
     */
    public static FeatureSource<FeatureType, Feature> getFeatureSource(Name featureTypeName) throws IOException {
        return getInstance().featureSource(featureTypeName);
    }

    public static DataAccess<FeatureType, Feature> getDataAccess(Name featureTypeName) throws IOException {
        return getInstance().featureSource(featureTypeName).getDataStore();
    }

    /**
     * Registers a data access
     * 
     * @param dataAccess
     *            Data access to be registered
     */
    public static void register(DataAccess<FeatureType, Feature> dataAccess) {
        getInstance().registerAccess(dataAccess);
    }

    /**
     * Unregister a data access. This is important especially at the end of test cases, so that the
     * mappings contained in the data access do not conflict with mappings of the same type used in
     * other tests. * Does not dispose *
     * This method should not be called directly, instead use dispose method from DataAccess
     * 
     * @param dataAccess
     *            Data access to be unregistered
     */
    public static void unregister(DataAccess<FeatureType, Feature> dataAccess) {
        getInstance().unregisterAccess(dataAccess);
    }

    /**
     * Unregister * and dispose * all data accesses in the registry. This is may be needed to prevent unit tests
     * from conflicting with data accesses with the same type name registered for other tests.
     */
    public static void unregisterAndDisposeAll() {
        getInstance().disposeAndUnregisterAll();
    }
    
    /**
     * Unregister * and dispose * all data accesses in the registry. This is may be needed to prevent unit tests
     * from conflicting with data accesses with the same type name registered for other tests.
     * 
     * @Deprecated use unregisterAndDisposeAll
     */
    @Deprecated
    public static void unregisterAll() {
        getInstance().disposeAndUnregisterAll();
    }
      
    /**
     * Return true if a type name is mapped in one of the registered data accesses. If
     * the type mapping has mappingName, then it will be the key that is matched in the search. If
     * it doesn't, then it will match the targetElementName.
     * 
     * @param featureTypeName
     *            Feature type name
     * @return
     * @throws IOException
     */
    public static boolean hasName(Name featureTypeName) throws IOException {
        return getInstance().hasAccessName(featureTypeName);
    }

    
    //---------------------------------------------------------------------------------------
    // helper methods
    //---------------------------------------------------------------------------------------
    
    /**
     * Throws data source exception if mapping is not found.
     * 
     * @param featureTypeName
     *            Name of feature type
     * @throws IOException
     */
    protected void throwDataSourceException(Name featureTypeName) throws IOException {
        List<Name> typeNames = new ArrayList<Name>();
        for (Iterator<DataAccess<FeatureType, Feature>> dataAccessIterator = registry.iterator(); dataAccessIterator.hasNext();) {
            typeNames.addAll(dataAccessIterator.next().getNames());
        }
        throw new DataSourceException("Feature type " + featureTypeName + " not found."
                + " Has the data access been registered in DataAccessRegistry?" + " Available: "
                + typeNames.toString());
    }
      

}
