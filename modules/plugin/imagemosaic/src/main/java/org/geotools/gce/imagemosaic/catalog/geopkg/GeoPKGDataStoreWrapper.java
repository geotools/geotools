/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic.catalog.geopkg;

import java.io.IOException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.gce.imagemosaic.catalog.oracle.AbstractFeatureTypeMapper;
import org.geotools.gce.imagemosaic.catalog.oracle.DataStoreWrapper;
import org.geotools.gce.imagemosaic.catalog.oracle.FeatureTypeMapper;

/**
 * SQLite and thus GeoPackage cannot mix DDL and DML transactions, as DML always tries to grab a read lock on the
 * database, thus causing failures when trying to create tables (DDL) while other connections are performing DML
 * operations.
 *
 * <p>This wrapper thus forces all feature stores to operate in auto-commit mode, ignoring any attempt to set a
 * transaction.
 *
 * <p>This is currently necessary as haversting does exactly that, creating new featuretypes while files are being
 * ingested over a transaction. In the future we migth want to break the DataStore API and allow passing a Transaction
 * to createSchema, allowing to run all operations (DDL and DML) within the same transaction.
 */
public class GeoPKGDataStoreWrapper extends DataStoreWrapper {

    public GeoPKGDataStoreWrapper(DataStore datastore, String auxFolderPath) {
        super(datastore, auxFolderPath);
    }

    @Override
    protected FeatureTypeMapper getFeatureTypeMapper(SimpleFeatureType featureType) throws Exception {
        return new AbstractFeatureTypeMapper(featureType, 2000) {
            {
                remapFeatureType();
            }
        };
    }

    @Override
    protected SimpleFeatureSource transformFeatureStore(SimpleFeatureStore source, FeatureTypeMapper mapper)
            throws IOException {
        SimpleFeatureSource transformed = super.transformFeatureStore(source, mapper);
        if (transformed instanceof SimpleFeatureStore store) return new AutoCommitFeatureStore(store);
        return transformed;
    }
}
