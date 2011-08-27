/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.aggregate;

import java.awt.RenderingHints.Key;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.Repository;
import org.geotools.util.KVP;

public class AggregatingDataStoreFactory extends AbstractDataStoreFactory {

    public static final Param REPOSITORY_PARAM = new Param("repository", Repository.class,
            "The repository that will provide the store intances", true, null, new KVP(Param.LEVEL,
                    "advanced"));

    public static final Param STORES_PARAM = new Param("stores", String[].class,
            "List of data stores to connect to", false, null, new KVP(Param.ELEMENT, String.class));

    public static final Param TOLERATE_CONNECTION_FAILURE = new Param("tolerate connection failure",
            Boolean.class, "Is failure to connect to a store tolerated", false, Boolean.TRUE);

    public static final Param NAMESPACE = new Param("namespace", String.class, "Namespace prefix",
            false);

    public static final Param PARALLELISM = new Param("parallelism", Integer.class,
            "Number of allowed concurrent queries on the delegate stores (unlimited by default)",
            false, new Integer(-1));
    
    public static final Param CONFIGURATION = new Param("configuration", URL.class,
            "Location of the aggregated type configuration file",
            false, null);
    
    public static final Param CONFIGURATION_XML = new Param("configuration xml", String.class,
            "The aggregated type configuration, as a xml document in a string",
            false, null);

    public String getDisplayName() {
        return "Aggregating data store";
    }

    public String getDescription() {
        return "Aggregates homologous feature types from separate data stores";
    }

    public Param[] getParametersInfo() {
        return new Param[] { REPOSITORY_PARAM, NAMESPACE, CONFIGURATION,
                TOLERATE_CONNECTION_FAILURE, PARALLELISM };
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return null;
    }

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        Repository repository = lookup(REPOSITORY_PARAM, params, Repository.class);
        String[] stores = lookup(STORES_PARAM, params, String[].class);
        boolean tolerant = lookup(TOLERATE_CONNECTION_FAILURE, params, Boolean.class);
        String namespace = lookup(NAMESPACE, params, String.class);
        ExecutorService executor;
        int parallelism = lookup(PARALLELISM, params, Integer.class);
        if(parallelism <= 0) {
            executor = Executors.newCachedThreadPool();
        } else {
            executor = Executors.newFixedThreadPool(parallelism);
        }
        List<AggregateTypeConfiguration> configs = null;
        URL configuration = lookup(CONFIGURATION, params, URL.class);
        if(configuration != null) {
            configs = new AggregateTypeParser().parseConfigurations(configuration.openStream());
        } 
        String configurationXml = lookup(CONFIGURATION_XML, params, String.class);
        if(configurationXml != null && !"".equals(configurationXml.trim())) {
            configs = new AggregateTypeParser().parseConfigurations(new ByteArrayInputStream(configurationXml.getBytes()));
        }

        AggregatingDataStore store = new AggregatingDataStore(repository, executor);
        store.setNamespaceURI(namespace);
        store.setTolerant(tolerant);
        if(stores != null) {
            store.autoConfigureStores(Arrays.asList(stores));
        }
        if(configs != null) {
            for (AggregateTypeConfiguration config : configs) {
                store.addType(config);
            }
        }

        return store;
    }
    
    public List<AggregateTypeConfiguration> parseConfiguration(String xml) throws IOException {
        if(xml != null && !"".equals(xml.trim())) {
            return new AggregateTypeParser().parseConfigurations(new ByteArrayInputStream(xml.getBytes()));
        } else {
            return null;
        }
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return createDataStore(params);
    }

    /**
     * Looks up a parameter, if not found it returns the default value, assuming there is one, or
     * null otherwise
     * 
     * @param <T>
     * @param param
     * @param params
     * @param target
     * @return
     * @throws IOException
     */
    <T> T lookup(Param param, Map<String, Serializable> params, Class<T> target) throws IOException {
        T result = (T) param.lookUp(params);
        if (result == null) {
            return (T) param.getDefaultValue();
        } else {
            return result;
        }

    }

}
