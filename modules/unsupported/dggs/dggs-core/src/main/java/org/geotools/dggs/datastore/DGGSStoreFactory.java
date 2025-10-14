/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.datastore;

import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.Repository;
import org.geotools.api.feature.type.Name;
import org.geotools.dggs.DGGSFactoryFinder;
import org.geotools.dggs.DGGSInstance;
import org.geotools.feature.NameImpl;

/** Factory for {@link org.geotools.dggs.gstore.DGGSStore} */
// TODO: add a limit to the complexity of queries the store is willing to accept? And suggest the
// user to switch to a lower resolution instead. Though maybe this ought to be done at the
// service level, and propagated down to the store as a Hint?
public class DGGSStoreFactory implements DataStoreFactorySpi {

    /** parameter for database type */
    // TODO: find some better way to separate this from the DGGSGeometryStore?
    public static final Param DGGS_FACTORY_ID =
            new Param("dggs_id", String.class, "DGGS Factory identifier, e.g., H3 or rHEALPix", true, null);

    /** Logical store name (used by your store to resolve tables/collections/etc.) */
    public static final Param STORE_NAME = new Param(
            "storeName", String.class, "Logical store name for this repository (schema, namespace, etc.)", true, null);

    /** Repository instance to be used by the store. */
    public static final Param REPOSITORY = new Param(
            "repository",
            Repository.class,
            "Repository implementation used by the store (passed by reference)",
            true,
            null);

    public static final Param ZONE_ID_COLUMN_NAME = new Param(
            "zoneIdColumnName",
            String.class,
            "The column name identifying the attribute containing the DGGS zone identifiers",
            false,
            "zoneId");

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        return createDataStore(params);
    }

    @Override
    public DataStore createDataStore(Map<String, ?> params) throws IOException {
        // setup the DGGS instance
        String factoryId = (String) DGGS_FACTORY_ID.lookUp(params);
        DGGSInstance dggs = DGGSFactoryFinder.createInstance(factoryId, params);

        String storeName = (String) STORE_NAME.lookUp(params);
        Repository repository = (Repository) REPOSITORY.lookUp(params);
        String zoneIdAttribute = (String) ZONE_ID_COLUMN_NAME.lookUp(params);
        String[] storeNameParts = storeName.split(":");
        Name name = storeNameParts.length == 2
                ? new NameImpl(storeNameParts[0], storeNameParts[1])
                : new NameImpl(storeName);
        DataStore datastore = repository.dataStore(name);
        if (datastore == null) {
            throw new IOException("Could not find a DataStore named '" + storeName + "' in the provided repository.");
        }
        return new DGGSDataStore(dggs, datastore, zoneIdAttribute);
    }

    @Override
    public String getDisplayName() {
        return "DGGS Datastore";
    }

    @Override
    public String getDescription() {
        return "Builds a DGGS-backed store using a provided Repository and store name.";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {DGGS_FACTORY_ID, STORE_NAME, REPOSITORY, ZONE_ID_COLUMN_NAME};
    }

    @Override
    public boolean isAvailable() {
        return DGGSFactoryFinder.getExtensionFactories().findAny().isPresent();
    }
}
