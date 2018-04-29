/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.*;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.DataAccessMap;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.util.URLs;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * DataStoreFactory for ComplexDataStore.
 *
 * <p>NOTE: currently this one is not registered through the geotools datastore plugin mechanism.
 * Instead, we're directly using DataAccessFactory
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class AppSchemaDataAccessFactory implements DataAccessFactory {

    public static final String DBTYPE_STRING = "app-schema";

    public static final DataAccessFactory.Param DBTYPE =
            new DataAccessFactory.Param(
                    "dbtype",
                    String.class,
                    "Fixed value '" + DBTYPE_STRING + "'",
                    true,
                    DBTYPE_STRING,
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    public static final DataAccessFactory.Param URL =
            new DataAccessFactory.Param(
                    "url",
                    URL.class,
                    "URL to an application schema datastore XML configuration file",
                    true);

    public AppSchemaDataAccessFactory() {}

    public DataAccess<FeatureType, Feature> createDataStore(Map params) throws IOException {
        return createDataStore(params, false, new DataAccessMap());
    }

    public DataAccess<FeatureType, Feature> createDataStore(
            Map params, boolean hidden, DataAccessMap sourceDataStoreMap) throws IOException {
        Set<FeatureTypeMapping> mappings;
        AppSchemaDataAccess dataStore;

        URL configFileUrl = (URL) AppSchemaDataAccessFactory.URL.lookUp(params);
        XMLConfigDigester configReader = new XMLConfigDigester();
        AppSchemaDataAccessDTO config = configReader.parse(configFileUrl);

        // load related types that are mapped separately, and not visible on their own
        // this is when the related types are not feature types, so they don't appear
        // on getCapabilities, and getFeature also shouldn't return anything etc.
        List<String> includes = config.getIncludes();
        for (Iterator<String> it = includes.iterator(); it.hasNext(); ) {
            String parentLocation;
            parentLocation = URLs.urlToFile(configFileUrl).getParent();
            File includedConfig = new File(parentLocation, it.next());
            if (!includedConfig.exists()) {
                throw new RuntimeException(
                        "Please check that the includedTypes location is correct: \n '"
                                + includedConfig.getPath()
                                + "' doesn't exist!");
            }

            URL relatedConfigURL = URLs.fileToUrl(includedConfig);
            params.put("url", relatedConfigURL);
            // this will register the related data access, to enable feature chaining;
            // sourceDataStoreMap is passed on to keep track of the already created source data
            // stores
            // and avoid creating the same data store twice (this enables feature iterators sharing
            // the same transaction to re-use the connection instead of opening a new one for each
            // joined type)
            createDataStore(params, true, sourceDataStoreMap);
        }

        mappings = AppSchemaDataAccessConfigurator.buildMappings(config, sourceDataStoreMap);

        dataStore = new AppSchemaDataAccess(mappings, hidden);

        return dataStore;
    }

    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException();
    }

    public String getDisplayName() {
        return "Application Schema DataAccess";
    }

    public String getDescription() {
        return "Application Schema DataStore allows mapping of FeatureTypes to externally defined Output Schemas";
    }

    public DataStoreFactorySpi.Param[] getParametersInfo() {
        return new DataStoreFactorySpi.Param[] {
            AppSchemaDataAccessFactory.DBTYPE, AppSchemaDataAccessFactory.URL
        };
    }

    public boolean canProcess(Map params) {
        try {
            Object dbType = AppSchemaDataAccessFactory.DBTYPE.lookUp(params);
            Object configUrl = AppSchemaDataAccessFactory.URL.lookUp(params);
            return DBTYPE_STRING.equals(dbType) && configUrl != null;
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return false;
    }

    public boolean isAvailable() {
        return true;
    }

    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
