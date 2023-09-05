/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.csv3;

import java.io.IOException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;

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
        try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReader(query)) {
            while (featureReader.hasNext()) {
                SimpleFeature feature = featureReader.next();
                bounds.include(feature.getBounds());
            }
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
