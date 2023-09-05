/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.oracle;

import java.io.IOException;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeatureType;

/**
 * Specific Oracle implementation for a {@link DataStoreWrapper} Oracle DB has a couple of
 * limitations: 1) All attributes and type names are UPPERCASE 2) attribute and type names can't be
 * longer than 30 chars
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class OracleDatastoreWrapper extends DataStoreWrapper {

    public OracleDatastoreWrapper(DataStore datastore, String location) {
        super(datastore, location);
    }

    @Override
    protected FeatureTypeMapper getFeatureTypeMapper(SimpleFeatureType featureType)
            throws Exception {
        return new OracleFeatureTypeMapper(featureType);
    }

    @Override
    protected SimpleFeatureSource transformFeatureStore(
            SimpleFeatureStore store, FeatureTypeMapper mapper) throws IOException {
        SimpleFeatureSource transformedSource = mapper.getSimpleFeatureSource();
        if (transformedSource != null) {
            return transformedSource;
        } else {
            transformedSource =
                    new OracleTransformFeatureStore(
                            store, mapper.getName(), mapper.getDefinitions(), datastore);
            ((OracleFeatureTypeMapper) mapper).setSimpleFeatureSource(transformedSource);
            return transformedSource;
        }
    }
}
