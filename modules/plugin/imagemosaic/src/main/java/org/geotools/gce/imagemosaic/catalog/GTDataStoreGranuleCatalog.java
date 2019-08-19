/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.oracle.OracleDatastoreWrapper;
import org.geotools.gce.imagemosaic.catalog.postgis.PostgisDatastoreWrapper;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;

public class GTDataStoreGranuleCatalog extends AbstractGTDataStoreGranuleCatalog {

    private DataStore tileIndexStore;

    private Set<String> validTypeNames;

    public GTDataStoreGranuleCatalog(
            Properties params, boolean create, DataStoreFactorySpi spi, Hints hints) {
        super(params, create, spi, hints);
    }

    protected void initTileIndexStore(
            final Properties params, final boolean create, final DataStoreFactorySpi spi)
            throws IOException, MalformedURLException {
        Utilities.ensureNonNull("spi", spi);

        // creating a store, this might imply creating it for an existing underlying store or
        // creating a brand new one
        Map<String, Serializable> dastastoreParams = Utils.filterDataStoreParams(params, spi);

        boolean isPostgis = Utils.isPostgisStore(spi);
        // H2 workadound
        if (Utils.isH2Store(spi)) {
            Utils.fixH2DatabaseLocation(dastastoreParams, parentLocation);
            Utils.fixH2MVCCParam(dastastoreParams);
        }
        if (isPostgis) {
            Utils.fixPostgisDBCreationParams(dastastoreParams);
        }

        if (!create) {
            this.tileIndexStore = spi.createDataStore(dastastoreParams);
        } else {
            // this works only with the shapefile datastore, not with the others
            // therefore I try to catch the error to try and use the method without *New*
            try {
                this.tileIndexStore = spi.createNewDataStore(dastastoreParams);
            } catch (UnsupportedOperationException e) {
                this.tileIndexStore = spi.createDataStore(dastastoreParams);
            }
        }

        if (isPostgis && wrapstore) {
            this.tileIndexStore =
                    new PostgisDatastoreWrapper(
                            getTileIndexStore(), FilenameUtils.getFullPath(parentLocation));
        } else if (Utils.isOracleStore(spi)) {
            this.tileIndexStore =
                    new OracleDatastoreWrapper(
                            getTileIndexStore(), FilenameUtils.getFullPath(parentLocation));
        }

        // this init must be here as it's getting called by the parent class constructor
        this.validTypeNames = new HashSet<String>();

        // is this a new store? If so we do not set any properties
        if (create) {
            return;
        }
        initializeTypeNames(params);
    }

    protected void handleInitializationException(Throwable t) {
        try {
            if (tileIndexStore != null) tileIndexStore.dispose();
        } catch (Throwable e1) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e1.getLocalizedMessage(), e1);
        } finally {
            this.tileIndexStore = null;
        }
    }

    @Override
    protected DataStore getTileIndexStore() {
        return tileIndexStore;
    }

    protected void disposeTileIndexStore() {
        try {
            if (tileIndexStore != null) {
                tileIndexStore.dispose();
            }
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        } finally {
            this.tileIndexStore = null;
        }
    }

    @Override
    protected Set<String> getValidTypeNames() {
        return this.validTypeNames;
    }
}
