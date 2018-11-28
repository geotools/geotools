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
package org.geotools.data.complex.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import org.apache.commons.digester.Digester;
import org.geotools.data.DataAccess;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.DataAccessMappingFeatureIterator;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.SourceDataStore;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * This interface allows data stores to take advantage of certain App-Schema extension points
 * allowing them to provided the necessary customizations. Is up to each store, based on the
 * context, to decide if it wants to contribute with anything.
 */
public interface CustomSourceDataStore {

    /**
     * Builds a data store based on the provided App-Schema data store configuration. If the
     * extension is NOT capable of building a data store based on the provided configuration NULL
     * should be returned.
     *
     * @param dataStoreConfig App-Schema data store configuration
     * @param appSchemaConfig App-Schema full configuration
     * @return a data store build based on the provided configuration or NULL
     */
    DataAccess<? extends FeatureType, ? extends Feature> buildDataStore(
            SourceDataStore dataStoreConfig, AppSchemaDataAccessDTO appSchemaConfig);

    /**
     * Allows a data store to provide its own configuration to the XML parser \ digester for data
     * sources.
     *
     * @param digester XML parser \ digester
     */
    void configXmlDigesterDataSources(Digester digester);

    /**
     * Allows a data store to provide its own configuration to the XML parser \ digester for
     * attributes mappings.
     *
     * @param digester XML parser \ digester
     */
    void configXmlDigesterAttributesMappings(Digester digester);

    /**
     * Allows a data store to build its own iterator that will be used by App-Schema core to
     * retrieve the necessary attributes from the data store to build the mapped feature types.
     * Stores can also if need change the current App-Schema query.
     *
     * @param store the data store from where App-Schema will retrieve the necessary values
     * @param featureTypeMapping the feature typ mapping that is being build by App-Schema core
     * @param query the query that will \ should be submitted to the data store
     * @param transaction the current transaction context
     * @return a custom iterator or NULL
     */
    DataAccessMappingFeatureIterator buildIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping featureTypeMapping,
            Query query,
            Transaction transaction);

    /**
     * Helper method that loads all the custom data stores extensions.
     *
     * @return the list of found custom data store extensions
     */
    static List<CustomSourceDataStore> loadExtensions() {
        ServiceLoader<CustomSourceDataStore> loader =
                ServiceLoader.load(CustomSourceDataStore.class);
        loader.reload();
        // get the custom data store extensions from the loader
        List<CustomSourceDataStore> extensions = new ArrayList<>();
        for (CustomSourceDataStore extension : loader) {
            extensions.add(extension);
        }
        return extensions;
    }
}
