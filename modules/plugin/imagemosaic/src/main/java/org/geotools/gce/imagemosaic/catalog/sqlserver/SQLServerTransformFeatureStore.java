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
package org.geotools.gce.imagemosaic.catalog.sqlserver;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.transform.Definition;
import org.geotools.data.transform.TransformFeatureStore;

/**
 * A SQLServer transforming feature store, will transform on the fly all attempts to write so that
 * the underlying features are getting modified while exposing a different feature type to its
 * callers.
 */
public class SQLServerTransformFeatureStore extends TransformFeatureStore {

    DataStore datastore;

    public SQLServerTransformFeatureStore(
            SimpleFeatureStore store, Name name, List<Definition> definitions, DataStore datastore)
            throws IOException {
        super(store, name, definitions);
        this.datastore = datastore;
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return datastore;
    }
}
