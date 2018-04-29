/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 * 	  (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * 	  (c) 2012 - 2014 OpenPlans
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
package org.geotools.data.csv;

import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

@SuppressWarnings("unchecked")
public class CSVFeatureSource extends ContentFeatureSource {

    public CSVFeatureSource(CSVDataStore datastore) {
        this(datastore, Query.ALL);
    }

    public CSVFeatureSource(CSVDataStore datastore, Query query) {
        this(new ContentEntry(datastore, datastore.getTypeName()), query);
    }

    public CSVFeatureSource(ContentEntry entry) {
        this(entry, Query.ALL);
    }

    public CSVFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }

    public CSVDataStore getDataStore() {
        return (CSVDataStore) super.getDataStore();
    }

    // docs start getBoundsInternal
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(getSchema().getCoordinateReferenceSystem());
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReader(query);
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
    // docs end getBoundsInternal

    // docs start getCountInternal
    protected int getCountInternal(Query query) throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReaderInternal(query);
        int n = 0;
        try {
            for (n = 0; featureReader.hasNext(); n++) {
                featureReader.next();
            }
        } finally {
            featureReader.close();
        }
        return n;
    }
    // docs end getCountInternal

    // docs start getReaderInternal
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        CSVDataStore dataStore = getDataStore();
        return new CSVFeatureReader(dataStore.getCSVStrategy(), query);
    }
    // docs end getReaderInternal

    // docs start buildFeatureType
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return getDataStore().getSchema();
    }
    // docs end buildFeatureType
}
