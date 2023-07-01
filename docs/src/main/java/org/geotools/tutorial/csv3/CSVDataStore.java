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
import java.util.Collections;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.tutorial.csv3.parse.CSVStrategy;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;

public class CSVDataStore extends ContentDataStore implements FileDataStore {

    private final CSVStrategy csvStrategy;

    private final CSVFileState csvFileState;

    public CSVDataStore(CSVFileState csvFileState, CSVStrategy csvStrategy) {
        this.csvFileState = csvFileState;
        this.csvStrategy = csvStrategy;
    }

    // docs start getTypeName
    public Name getTypeName() {
        if (namespaceURI != null) {
            return new NameImpl(namespaceURI, csvFileState.getTypeName());
        } else {
            return new NameImpl(csvFileState.getTypeName());
        }
    }
    // docs end getTypeName

    // docs start dataStoreOperations
    @Override
    protected List<Name> createTypeNames() throws IOException {
        return Collections.singletonList(getTypeName());
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        if (csvFileState.getFile().canWrite()) {
            return new CSVFeatureStore(csvStrategy, csvFileState, entry, Query.ALL);
        } else {
            return new CSVFeatureSource(entry, Query.ALL);
        }
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return this.csvStrategy.getFeatureType();
    }

    @Override
    public void updateSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleFeatureSource getFeatureSource() throws IOException {
        return new CSVFeatureSource(this);
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException {
        return new CSVFeatureSource(this).getReader();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            Filter filter, Transaction transaction) throws IOException {
        return super.getFeatureWriter(this.csvFileState.getTypeName(), filter, transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
            throws IOException {
        return super.getFeatureWriter(this.csvFileState.getTypeName(), transaction);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            Transaction transaction) throws IOException {
        return super.getFeatureWriterAppend(this.csvFileState.getTypeName(), transaction);
    }

    public CSVStrategy getCSVStrategy() {
        return csvStrategy;
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        this.csvStrategy.createSchema(featureType);
    }
    // docs end dataStoreOperations
}
