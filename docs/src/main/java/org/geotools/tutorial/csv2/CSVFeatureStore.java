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
package org.geotools.tutorial.csv2;

import java.io.IOException;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Read-write access to CSV File.
 *
 * @author Jody Garnett (Boundless)
 * @author Ian Turton (Astun Technology)
 */
public class CSVFeatureStore extends ContentFeatureStore {
    public CSVFeatureStore(ContentEntry entry, Query query) {
        super(entry, query);
    }
    // header end
    // getWriter start
    //
    // CSVFeatureStore implementations
    //
    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(
            Query query, int flags) throws IOException {
        return new CSVFeatureWriter(getState(), query);
    }
    // getWriter end

    // transaction start
    /**
     * Delegate used for FeatureSource methods (We do this because Java cannot inherit from both
     * ContentFeatureStore and CSVFeatureSource at the same time
     */
    CSVFeatureSource delegate =
            new CSVFeatureSource(entry, query) {
                @Override
                public void setTransaction(Transaction transaction) {
                    super.setTransaction(transaction);
                    CSVFeatureStore.this.setTransaction(
                            transaction); // Keep these two implementations on the same transaction
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
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return delegate.getReaderInternal(query);
    }
    // internal end

    // visitor start
    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }
    // visitor end

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

    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    public ContentState getState() {
        return delegate.getState();
    }

    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    public Name getName() {
        return delegate.getName();
    }

    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }
    // public end

}
