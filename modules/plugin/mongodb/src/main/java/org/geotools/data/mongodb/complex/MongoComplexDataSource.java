/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb.complex;

import org.apache.commons.digester.Digester;
import org.geotools.data.DataAccess;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.DataAccessMappingFeatureIterator;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.SourceDataStore;
import org.geotools.data.complex.spi.CustomSourceDataStore;
import org.geotools.data.mongodb.MongoDataStore;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Class that builds an App-Schema iterator ready to be used with MongoDB. Data coming from MongoDB
 * is assumed to be always normalized, when using MongoDB with App-Schema we cannot chain multiple
 * collections together.
 */
public final class MongoComplexDataSource implements CustomSourceDataStore {

    @Override
    public DataAccess<? extends FeatureType, ? extends Feature> buildDataStore(
            SourceDataStore dataStoreConfig, AppSchemaDataAccessDTO appSchemaConfig) {
        // nothing to do
        return null;
    }

    @Override
    public void configXmlDigesterDataSources(Digester digester) {
        // nothing to do
    }

    @Override
    public void configXmlDigesterAttributesMappings(Digester digester) {
        // nothing to do
    }

    @Override
    public DataAccessMappingFeatureIterator buildIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping featureTypeMapping,
            Query query,
            Transaction transaction) {
        // let's se if this is a feature type coming from MongoDB
        if (!(featureTypeMapping.getSource() != null
                && featureTypeMapping.getSource().getDataStore() instanceof MongoDataStore)) {
            // not a MongoDB feature type mapping
            return null;
        }
        try {
            // we consider that data coming from MongoDB is always normalized
            return new DataAccessMappingFeatureIterator(
                    store, featureTypeMapping, query, false, true);
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Error creating App-Schema iterator for MongoDB data store.", exception);
        }
    }
}
