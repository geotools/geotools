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
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.tutorial.csv3.parse.CSVStrategy;

/**
 * Read-write access to CSV File.
 *
 * @author Jody Garnett (Boundless)
 * @author Ian Turton (Envitia)
 */
public class CSVFeatureStore extends ContentFeatureStore {
    private CSVStrategy csvStrategy;
    private CSVFileState csvFileState;

    public CSVFeatureStore(CSVStrategy csvStrategy, CSVFileState csvFileState, ContentEntry entry, Query query) {
        super(entry, query);

        this.csvStrategy = csvStrategy;
        this.csvFileState = csvFileState;
    }
    // header end
    // getWriter start
    //
    // CSVFeatureStore implementations
    //
    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(Query query, int flags)
            throws IOException {
        return new CSVFeatureWriter(this.csvFileState, this.csvStrategy, query);
    }
    // getWriter end

    // transaction start
    /**
     * Delegate used for FeatureSource methods (We do this because Java cannot inherit from both ContentFeatureStore and
     * CSVFeatureSource at the same time
     */
    CSVFeatureSource delegate = new CSVFeatureSource(entry, query) {
        @Override
        public void setTransaction(Transaction transaction) {
            super.setTransaction(transaction);
            CSVFeatureStore.this.setTransaction(transaction); // Keep these two implementations on the same transaction
        }
    };

    @Override
    public void setTransaction(Transaction transaction) {
        super.setTransaction(transaction);
        if (delegate.getTransaction() != transaction) {
            delegate.setTransaction(transaction);
        }
    }
    // transaction end

    // internal start
    //
    // Internal Delegate Methods
    // Implement FeatureSource methods using CSVFeatureSource implementation
    //
    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return delegate.buildFeatureType();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        return delegate.getBoundsInternal(query);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return delegate.getCountInternal(query);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        return delegate.getReaderInternal(query);
    }
    // internal end

    // public start
    //
    // Public Delegate Methods
    // Implement FeatureSource methods using CSVFeatureSource implementation
    //
    @Override
    public CSVDataStore getDataStore() {
        return delegate.getDataStore();
    }

    @Override
    public ContentEntry getEntry() {
        return delegate.getEntry();
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public ContentState getState() {
        return delegate.getState();
    }

    @Override
    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }
    // public start

}
