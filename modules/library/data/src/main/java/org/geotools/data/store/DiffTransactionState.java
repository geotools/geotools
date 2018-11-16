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
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.util.factory.Hints;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/** Transaction state responsible for holding an in memory {@link Diff} of any modifications. */
public class DiffTransactionState implements Transaction.State {
    protected Diff diff;

    /** The transaction (ie session) associated with this state */
    protected Transaction transaction;

    /**
     * ContentState for this transaction used to hold information for FeatureReader implementations
     */
    protected ContentState state;

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
     * Transaction state responsible for holding an in memory {@link Diff}.
     *
     * @param state ContentState for the transaction
     */
    protected DiffTransactionState(ContentState state, Diff diff) {
        this.state = state;
        this.diff = diff;
    }

    /**
     * Access the in memory Diff.
     *
     * @return in memory diff.
     */
    public Diff getDiff() {
        return this.diff;
    }

    @Override

    /**
     * We are already holding onto our transaction from ContentState; however this method does check
     * that the transaction is correct.
     */
    public synchronized void setTransaction(Transaction transaction) {
        if (this.transaction != null && transaction == null) {
            // clear ContentEntry transaction to fix GEOT-3315
            state.getEntry().clearTransaction(this.transaction);
        }
        this.transaction = transaction;
    }

    @Override

    /**
     * Will apply differences to store.
     *
     * <p>The provided diff will be modified as the differences are applied, If the operations are
     * all successful diff will be empty at the end of this process.
     *
     * <p>diff can be used to represent the following operations:
     *
     * <ul>
     *   <li>fid|null: represents a fid being removed
     *   <li>fid|feature: where fid exists, represents feature modification
     *   <li>fid|feature: where fid does not exist, represents feature being modified
     * </ul>
     *
     * @param typeName typeName being updated
     * @param diff differences to apply to FeatureWriter
     * @throws IOException If the entire diff cannot be writen out
     * @t
     * @see org.geotools.data.Transaction.State#commit()
     */
    public synchronized void commit() throws IOException {
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
            // request a plain writer with no events, filtering or locking checks
            store = (ContentFeatureStore) dataStore.getFeatureSource(name, transaction);
            writer = store.getWriter(Filter.INCLUDE, ContentDataStore.WRITER_COMMIT);
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

                    if (update == Diff.NULL) {
                        writer.remove();

                    } else {
                        try {
                            feature.setAttributes(update.getAttributes());
                            writer.write();

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
                                String providedFid =
                                        (String) addedFeature.getUserData().get(Hints.PROVIDED_FID);
                                nextFeature.getUserData().put(Hints.PROVIDED_FID, providedFid);
                            } else {
                                nextFeature
                                        .getUserData()
                                        .put(Hints.PROVIDED_FID, addedFeature.getID());
                            }
                            // }
                            writer.write();

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

    @Override
    /** @see org.geotools.data.Transaction.State#rollback() */
    public synchronized void rollback() throws IOException {
        diff.clear(); // rollback differences
        state.fireBatchFeatureEvent(false);
    }

    @Override

    /** @see org.geotools.data.Transaction.State#addAuthorization(java.lang.String) */
    public synchronized void addAuthorization(String AuthID) throws IOException {
        // not required for TransactionStateDiff
    }

    /**
     * Provides a wrapper on the provided reader which gives a diff writer.
     *
     * @param contentFeatureStore ContentFeatureStore
     * @param reader FeatureReader requiring diff support
     * @return FeatureWriter with diff support
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> diffWriter(
            ContentFeatureStore contentFeatureStore,
            FeatureReader<SimpleFeatureType, SimpleFeature> reader) {

        return new DiffContentFeatureWriter(contentFeatureStore, diff, reader);
    }
}
