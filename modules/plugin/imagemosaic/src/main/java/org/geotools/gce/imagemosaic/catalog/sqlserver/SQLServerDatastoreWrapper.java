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
import java.util.Properties;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.gce.imagemosaic.catalog.DataStoreWrapper;
import org.geotools.gce.imagemosaic.catalog.FeatureTypeMapper;
import org.opengis.feature.simple.SimpleFeatureType;

/** Specific SQLServer implementation for a {@link DataStoreWrapper} */
public class SQLServerDatastoreWrapper extends DataStoreWrapper {

    public SQLServerDatastoreWrapper(DataStore datastore, String location) {
        super(datastore, location);
    }

    /**
     * Return a specific {@link FeatureTypeMapper} by parsing mapping properties contained within
     * the specified {@link Properties} object
     */
    protected FeatureTypeMapper getFeatureTypeMapper(final Properties props) throws Exception {
        FeatureTypeMapper mapper = super.getFeatureTypeMapper(props);
        return mapper;
    }

    @Override
    protected FeatureTypeMapper getFeatureTypeMapper(SimpleFeatureType featureType)
            throws Exception {
        return new SQLServerTypeMapper(featureType);
    }

    @Override
    protected SimpleFeatureSource transformFeatureStore(
            SimpleFeatureStore store, FeatureTypeMapper mapper) throws IOException {
        SimpleFeatureSource transformedSource = mapper.getSimpleFeatureSource();
        if (transformedSource != null) {
            return transformedSource;
        } else {
            transformedSource =
                    (SimpleFeatureSource)
                            new SQLServerTransformFeatureStore(
                                    store, mapper.getName(), mapper.getDefinitions(), datastore);
            ((SQLServerTypeMapper) mapper).setSimpleFeatureSource(transformedSource);
            return transformedSource;
        }
    }
}
