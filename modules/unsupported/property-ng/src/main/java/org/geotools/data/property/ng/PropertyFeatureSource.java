/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property.ng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 * 
 * @source $URL$
 */
public class PropertyFeatureSource extends ContentFeatureSource {
    public PropertyFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }
    @Override
    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities(){
            public boolean isUseProvidedFIDSupported() {
                return true;
            }
        };
    }
    
    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope bounds = new ReferencedEnvelope(getSchema()
                .getCoordinateReferenceSystem());

        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReaderInternal(query);
        try {
            while (featureReader.hasNext()) {
                SimpleFeature feature = featureReader.next();
                bounds.include(feature.getBounds());
            }
        } finally {
            featureReader.close();
        }
        return bounds;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        int count = 0;
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReaderInternal(query);
        try {
            while (featureReader.hasNext()) {
                featureReader.next();
                count++;
            }
        } finally {
            featureReader.close();
        }
        return count;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        String typeName = getEntry().getTypeName();
        String namespace = getEntry().getName().getNamespaceURI();
        
        String typeSpec = property("_");
        try {
            return DataUtilities.createType(namespace, typeName, typeSpec);
        } catch (SchemaException e) {
            e.printStackTrace();
            throw new DataSourceException(typeName + " schema not available", e);
        }
    }

    private String property(String key) throws IOException {
        PropertyDataStore dataStore = (PropertyDataStore) getEntry().getDataStore();
        File file = dataStore.file;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.startsWith(key + "=")) {
                    return line.substring(key.length() + 1);
                }
            }
        } finally {
            reader.close();
        }
        return null;
    }


    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        PropertyDataStore dataStore = (PropertyDataStore) getEntry().getDataStore();
        return new PropertyFeatureReader(dataStore.getNamespaceURI(),dataStore.file);
    }
}