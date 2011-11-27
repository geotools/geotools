/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.Diff;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * This ContentState can be used by implementations wishing to make use of in memory transaction support.
 * <p>
 * This implementation makes use of two key parts:
 * <ul>
 * <li>A Diff object used to record changes made against the content (using the provided FeaatureWriter).
 * </ul>
 * <li>A Transaction.State registered with the Transaction for commit / rollback notification</li> </ul>
 * 
 * @author Jody Garnett (LISAsoft)
 * 
 * @source $URL$
 */
public class DiffContentState extends ContentState {

    class DiffTransactionState implements Transaction.State {
        @Override
        /**
         * We are already holding onto our transaction from
         * ContentState; however this method does check that
         * the transaction is correct.
         */
        public synchronized void setTransaction(Transaction transaction) {
            if (transaction != null) {
                // configure
                if (transaction != DiffContentState.this.tx) {
                    // not expected!
                }
            }
        }

        @Override
        /**
         * Will apply differences to store.
         * 
         * @see org.geotools.data.Transaction.State#commit()
         */
        public synchronized void commit() throws IOException {
            applyDiff();
        }

        @Override
        /**
         * @see org.geotools.data.Transaction.State#rollback()
         */
        public synchronized void rollback() throws IOException {
            diff.clear(); // rollback differences
            fireBatchFeatureEvent(false);
        }

        @Override
        /**
         * @see org.geotools.data.Transaction.State#addAuthorization(java.lang.String)
         */
        public synchronized void addAuthorization(String AuthID) throws IOException {
            // not required for TransactionStateDiff
        }
    }

    /**
     * Collections differences.
     * 
     * <p>
     * Differences are stored as a Map of Feature by fid, and are reset during a commit() or rollback().
     * </p>
     */
    Diff diff;

    DiffTransactionState callback;

    public DiffContentState(ContentEntry entry) {
        super(entry);
        callback = new DiffTransactionState();
        getTransaction().putState(this, callback);
    }

    public DiffContentState(ContentState state) {
        super(state);
        callback = new DiffTransactionState();
        getTransaction().putState(this, callback);
    }

    public synchronized Diff diff() {
        if (diff == null) {
            diff = new Diff();
        }
        return diff;
    }

    /**
     * Called by commit() to apply one set of diff
     * 
     * <p>
     * The provided <code> will be modified as the differences are applied,
     * If the operations are all successful diff will be empty at
     * the end of this process.
     * </p>
     * 
     * <p>
     * diff can be used to represent the following operations:
     * </p>
     * 
     * <ul>
     * <li>
     * fid|null: represents a fid being removed</li>
     * 
     * <li>
     * fid|feature: where fid exists, represents feature modification</li>
     * <li>
     * fid|feature: where fid does not exist, represents feature being modified</li>
     * </ul>
     * 
     * 
     * @param typeName typeName being updated
     * @param diff differences to apply to FeatureWriter
     * 
     * @throws IOException If the entire diff cannot be writen out
     * @throws DataSourceException If the entire diff cannot be writen out
     */
    void applyDiff() throws IOException {
        if (diff.isEmpty()) {
            return; // nothing to do
        }
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        ContentFeatureStore store;
        ContentEntry entry = DiffContentState.this.getEntry();
        Name name = entry.getName();
        ContentDataStore dataStore = entry.getDataStore();
        ContentFeatureSource source = (ContentFeatureSource) dataStore.getFeatureSource(name);
        if( source instanceof ContentFeatureStore ){
            store  = (ContentFeatureStore) dataStore.getFeatureSource(name);
            writer = store.getWriter(Filter.INCLUDE);
        }
        else {
            throw new UnsupportedOperationException("not writable");
        }
        SimpleFeature feature;
        SimpleFeature update;

        Throwable cause = null;
        try {
            while (writer.hasNext()) {
                feature = (SimpleFeature) writer.next();
                String fid = feature.getID();

                if (diff.modified2.containsKey(fid)) {
                    update = (SimpleFeature) diff.modified2.get(fid);

                    if (update == Diff.NULL) {
                        writer.remove();

                        // notify
                        ContentState state = store.getEntry().getState(getTransaction());
                        state.fireFeatureRemoved(store, feature);
                    } else {
                        try {
                            feature.setAttributes(update.getAttributes());
                            writer.write();

                            // notify
                            ReferencedEnvelope bounds = ReferencedEnvelope.reference(feature
                                    .getBounds());
                            fireFeatureUpdated(store, update, bounds);
                        } catch (IllegalAttributeException e) {
                            throw new DataSourceException("Could update " + fid, e);
                        }
                    }
                }
            }

            SimpleFeature addedFeature;
            SimpleFeature nextFeature;

            synchronized (diff) {
                for (String fid : diff.getAddedOrder()) {
                    addedFeature = diff.getAdded().get(fid);

                    nextFeature = (SimpleFeature) writer.next();

                    if (nextFeature == null) {
                        throw new DataSourceException("Could not add " + fid);
                    } else {
                        try {
                            nextFeature.setAttributes(addedFeature.getAttributes());
                            // if( Boolean.TRUE.equals( addedFeature.getUserData().get(Hints.USE_PROVIDED_FID)) ){
                            nextFeature.getUserData().put(Hints.USE_PROVIDED_FID, true);
                            if (addedFeature.getUserData().containsKey(Hints.PROVIDED_FID)) {
                                String providedFid = (String) addedFeature.getUserData().get(
                                        Hints.PROVIDED_FID);
                                nextFeature.getUserData().put(Hints.PROVIDED_FID, providedFid);
                            } else {
                                nextFeature.getUserData().put(Hints.PROVIDED_FID,
                                        addedFeature.getID());
                            }
                            // }
                            writer.write();

                            // notify
                            fireFeatureAdded(store, nextFeature);
                        } catch (IllegalAttributeException e) {
                            throw new DataSourceException("Could update " + fid, e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            cause = e;
            throw e;
        } catch (RuntimeException e) {
            cause = e;
            throw e;
        } finally {
            try {
                writer.close();
                fireBatchFeatureEvent(true);
                diff.clear();
            } catch (IOException e) {
                if (cause != null) {
                    e.initCause(cause);
                }
                throw e;
            } catch (RuntimeException e) {
                if (cause != null) {
                    e.initCause(cause);
                }
                throw e;
            }
        }
    }

    /**
     * Convience Method for a Transaction based FeatureReader.
     * 
     * <p>
     * Constructs a DiffFeatureReader that works against this Transaction.
     * </p>
     * 
     * @param typeName TypeName to aquire a Reader on
     * 
     * @return FeatureReader<SimpleFeatureType, SimpleFeature> the mask orgional contents with against the current Differences recorded by the
     *         Tansasction State
     * 
     * @throws IOException If typeName is not Manged by this Tansaction State
     */
    public synchronized FeatureReader<SimpleFeatureType, SimpleFeature> reader(
            ContentFeatureStore store, FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        return new DiffContentFeatureReader<SimpleFeatureType, SimpleFeature>(store, reader);
    }

    /**
     * Convience Method for a Transaction based FeatureWriter
     * 
     * <p>
     * Constructs a DiffFeatureWriter that works against this Transaction.
     * </p>
     * 
     * @param typeName Type Name to record differences against
     * @param filter
     * 
     * @return A FeatureWriter that records Differences against a FeatureReader
     * 
     * @throws IOException If a FeatureRader could not be constucted to record differences against
     */
    public synchronized FeatureWriter<SimpleFeatureType, SimpleFeature> writer(
            ContentFeatureStore store, Filter filter) throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getReader(filter);
        if (true) {
            // this seems to protect against implementations not implementing filtering?
            // can probably remove
            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, filter);
        }
        return new DiffContentFeatureWriter(store, reader, filter);
    }

}
