/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.Diff;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.TransactionStateDiff;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Transaction state responsible for holding an in memory {@link Diff} of any modifications.
 */
class DiffTransactionState implements Transaction.State {
    Diff diff;
    
    /** The transaction (ie session) associated with this state */
    Transaction transaction;

    /**
     * ContentState for this transaction used to hold information for
     * FeatureReader implementations
     */
    ContentState state;

    /**
     * Transaction state responsible for holding an in memory {@link Diff}.
     * 
     * @param state ContentState for the transaction
     */
    public DiffTransactionState(ContentState state) {
        this.state = state;
        this.diff = new Diff();
    }
    /**
     * Access the in memory Diff.
     * @return in memory diff.
     */
    public Diff getDiff() {
        return this.diff;
    }

    @Override
    
    /**
     * We are already holding onto our transaction from
     * ContentState; however this method does check that
     * the transaction is correct.
     */
    public synchronized void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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
        state.fireBatchFeatureEvent(false);
    }

    @Override
    
    /**
     * @see org.geotools.data.Transaction.State#addAuthorization(java.lang.String)
     */
    public synchronized void addAuthorization(String AuthID) throws IOException {
        // not required for TransactionStateDiff
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
        ContentEntry entry = state.getEntry();
        Name name = entry.getName();
        ContentDataStore dataStore = entry.getDataStore();
        ContentFeatureSource source = (ContentFeatureSource) dataStore.getFeatureSource(name);
        if (source instanceof ContentFeatureStore) {
            store = (ContentFeatureStore) dataStore.getFeatureSource(name);
            writer = store.getWriter(Filter.INCLUDE);
        } else {
            throw new UnsupportedOperationException("not writable");
        }
        SimpleFeature feature;
        SimpleFeature update;

        Throwable cause = null;
        try {
            while (writer.hasNext()) {
                feature = (SimpleFeature) writer.next();
                String fid = feature.getID();

                if (diff.getModified().containsKey(fid)) {
                    update = (SimpleFeature) diff.getModified().get(fid);

                    if (update == TransactionStateDiff.NULL) {
                        writer.remove();

                        // notify
                        state.fireFeatureRemoved(store, feature);
                    } else {
                        try {
                            feature.setAttributes(update.getAttributes());
                            writer.write();

                            // notify
                            ReferencedEnvelope bounds = ReferencedEnvelope.reference(feature
                                    .getBounds());
                            state.fireFeatureUpdated(store, update, bounds);
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
                            // if( Boolean.TRUE.equals(
                            // addedFeature.getUserData().get(Hints.USE_PROVIDED_FID)) ){
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
                            state.fireFeatureAdded(store, nextFeature);
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
                state.fireBatchFeatureEvent(true);
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
}