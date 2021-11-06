/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 Open Source Geospatial Foundation (OSGeo)
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

    @Override
    public CSVDataStore getDataStore() {
        return (CSVDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(getSchema().getCoordinateReferenceSystem());
        try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReader(query)) {
            while (featureReader.hasNext()) {
                SimpleFeature feature = featureReader.next();
                bounds.include(feature.getBounds());
            }
        }
        return bounds;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                getReaderInternal(query)) {
            int n = 0;
            while (featureReader.hasNext()) {
                featureReader.next();
                n++;
            }
            return n;
        }
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        CSVDataStore dataStore = getDataStore();
        return new CSVFeatureReader(dataStore.getCSVStrategy(), query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return getDataStore().getSchema();
    }
}
