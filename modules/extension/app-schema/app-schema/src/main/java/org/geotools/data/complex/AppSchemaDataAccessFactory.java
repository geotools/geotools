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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.data.*;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.DataAccessMap;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.util.logging.Logging;
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
 * @since 2.4
 */
public class AppSchemaDataAccessFactory implements DataAccessFactory {

    private static final Logger LOGGER = Logging.getLogger(AppSchemaDataAccessFactory.class);

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
            params.put("url", buildIncludeUrl(configFileUrl, it.next()));
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

    /**
     * Helper method that builds the URL that should be used to retrieve an included type. If the
     * the include is already a valid URL then it is used has is, otherwise an URL will be used
     * using the parent URL.
     */
    private String buildIncludeUrl(URL parentUrl, String include) {
        // first check if the include is already an URL
        String includeLowerCase = include.toLowerCase();
        if (includeLowerCase.startsWith("http:") || includeLowerCase.startsWith("file:")) {
            // we already have an URL, return it has is
            return include;
        }
        // we need to build an URL using the parent URL as a basis
        String url = parentUrl.toString();
        int index = url.lastIndexOf("/");
        if (index <= 0) {
            // we can't handle this situation let's raise an exception
            throw new RuntimeException(
                    String.format(
                            "Can't build include types '%s' URL using parent '%s' URL.",
                            include, url));
        }
        // build the include types URL
        url = url.substring(0, index + 1) + include;
        LOGGER.fine(
                String.format("Using URL '%s' to retrieve include types with '%s'.", url, include));
        return url;
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
