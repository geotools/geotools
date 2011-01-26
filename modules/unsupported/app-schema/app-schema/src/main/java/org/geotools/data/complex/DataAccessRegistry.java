/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureSource;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * A registry that stores data access instances per application. This allows feature sources from
 * different data accesses to be accessed globally.
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 * @source $URL$
 */
public class DataAccessRegistry extends ArrayList<DataAccess<FeatureType, Feature>> {
    /**
     * 
     */
    private static final long serialVersionUID = 1999179819283985526L;

    protected static DataAccessRegistry registry = null;

    /**
     * Sole constructor
     */
    protected DataAccessRegistry() {
    }

    /**
     * Public method to create a new registry.
     * 
     * @return An instance of this class
     */
    private static DataAccessRegistry newInstance() {
        return new DataAccessRegistry();
    }

    /**
     * Registers a data access
     * 
     * @param dataAccess
     *            Data access to be registered
     */
    public static synchronized void register(DataAccess<FeatureType, Feature> dataAccess) {
        if (registry == null) {
            registry = DataAccessRegistry.newInstance();
        }
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
    public static synchronized void unregister(DataAccess<FeatureType, Feature> dataAccess) {
        if (registry == null) {
            throw new NullPointerException(
                    "This data access was never registered in DataAccessRegistry!: "
                            + dataAccess.getClass());
        }
        registry.remove(dataAccess);
    }

    /**
     * Unregister all data accesses in the registry. This is may be needed to prevent unit tests
     * from conflicting with data accesses with the same type name registered for other tests.
     */
    public static synchronized void unregisterAll() {
        if (registry != null) {
            registry.clear();
        }
    }
    
    public static boolean hasName(Name featureTypeName) throws IOException {
        if (registry == null) {
            // nothing's been registered, but it's OK, return false
            return false;
        }
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess.getNames().contains(featureTypeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a feature source for built features with supplied feature type name.
     * 
     * @param featureTypeName
     * @return feature source
     * @throws IOException
     */
    public static FeatureSource<FeatureType, Feature> getFeatureSource(Name featureTypeName)
            throws IOException {
        if (registry == null) {
            throw new UnsupportedOperationException(
                    "This method is only for registered data access. "
                            + "You need to register the data access that has mapping for: "
                            + featureTypeName.toString());
        }
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess.getNames().contains(featureTypeName)) {
                if (dataAccess instanceof AppSchemaDataAccess) {
                    return ((AppSchemaDataAccess) dataAccess)
                            .getFeatureSourceByName(featureTypeName);
                } else {
                    return dataAccess.getFeatureSource(featureTypeName);
                }
            }
        }
        throwDataSourceException(featureTypeName);

        return null;
    }

    public static DataAccess<FeatureType, Feature> getDataAccess(Name featureTypeName)
            throws IOException {
        return getFeatureSource(featureTypeName).getDataStore();
    }

    /**
     * Throws data source exception if mapping is not found.
     * 
     * @param featureTypeName
     *            Name of feature type
     * @throws IOException
     */
    protected static void throwDataSourceException(Name featureTypeName) throws IOException {
        List<Name> typeNames = new ArrayList<Name>();
        for (Iterator<DataAccess<FeatureType, Feature>> dataAccessIterator = registry.iterator(); dataAccessIterator
                .hasNext();) {
            typeNames.addAll(dataAccessIterator.next().getNames());
        }
        throw new DataSourceException("Feature type " + featureTypeName + " not found."
                + " Has the data access been registered in DataAccessRegistry?" + " Available: "
                + typeNames.toString());
    }

}
