/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.Repository;
import org.geotools.util.KVP;

/** Factory for {@link VectorMosaicStore} instances. */
public class VectorMosaicStoreFactory implements DataStoreFactorySpi {

    private static final String DISPLAY_NAME = "Vector Mosaic Data Store";
    private static final String DESCRIPTION = "Creates Vector Mosaic from multiple sources";
    public static final Param REPOSITORY_PARAM = new Param(
            "repository",
            Repository.class,
            "The repository that will provide the store intances",
            true,
            null,
            new KVP(Param.LEVEL, "advanced"));
    public static final Param NAMESPACE = new Param("namespace", String.class, "Namespace prefix", false);

    public static final Param DELEGATE_STORE_NAME = new Param(
            "delegateStoreName",
            String.class,
            "The name of the delegate store.  "
                    + "The delegate store must point to vector features with the same attributes/schema.",
            true);
    public static final Param PREFERRED_DATASTORE_SPI = new Param(
            "preferredDataStoreSPI",
            String.class,
            "The preferred DataStoreSPI to test connection parameters against. "
                    + "If not specified, the first DataStoreSPI found that accepts connection parameters will be used. "
                    + "Default is org.geotools.data.shapefile.ShapefileDataStoreFactory",
            false);

    public static final Param CONNECTION_PARAMETER_KEY = new Param(
            "connectionParameterKey",
            String.class,
            "The key to use for the connection parameter.  Default is 'url'",
            false);

    @Override
    public DataStore createDataStore(Map<String, ?> params) throws IOException {
        Repository repository = lookup(REPOSITORY_PARAM, params, Repository.class);
        String namespace = lookup(NAMESPACE, params, String.class);
        String delegateStoreName = lookup(DELEGATE_STORE_NAME, params, String.class);
        String preferredDataStoreSPI = lookup(PREFERRED_DATASTORE_SPI, params, String.class);
        String connectionParameterKey = lookup(CONNECTION_PARAMETER_KEY, params, String.class);
        VectorMosaicStore vectorMosaicStore = new VectorMosaicStore(delegateStoreName, repository);
        vectorMosaicStore.setNamespaceURI(namespace);
        if (preferredDataStoreSPI != null) {
            vectorMosaicStore.setPreferredSPI(preferredDataStoreSPI);
        }
        if (connectionParameterKey != null) {
            vectorMosaicStore.setConnectionParameterKey(connectionParameterKey);
        }
        return vectorMosaicStore;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {
            REPOSITORY_PARAM, NAMESPACE, DELEGATE_STORE_NAME, CONNECTION_PARAMETER_KEY, PREFERRED_DATASTORE_SPI
        };
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private <T> T lookup(Param param, Map<String, ?> params, Class<T> target) throws IOException {
        T result = (T) param.lookUp(params);
        if (result == null) {
            return (T) param.getDefaultValue();
        } else {
            return result;
        }
    }
}
