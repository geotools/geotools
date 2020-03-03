/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.data.BatchFeatureEvent;
import org.geotools.data.Diff;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureEvent.Type;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

/**
 * The state of an entry in a DataStore, maintained on a per-transaction basis. For information
 * maintained on a typeName basis see {@link ContentEntry}.
 *
 * <h3>Data Cache Synchronization Required</h2>
 *
 * <p>The default ContentState implementation maintains cached values on a per transaction basis:
 *
 * <ul>
 *   <li>feature type ({@link #getFeatureType()}
 *   <li>number of features ({@link #getCount()}
 *   <li>spatial extent ({@link #getBounds()}
 * </ul>
 *
 * Other types of state depend on the data format and must be handled by a subclass.
 *
 * <p>This class is a "data object" used to store values and is not thread safe. It is up to clients
 * of this class to ensure that values are set in a thread-safe / synchronized manner. For example:
 *
 * <pre>
 *   <code>
 *   ContentState state = ...;
 *
 *   //get the count
 *   int count = state.getCount();
 *   if ( count == -1 ) {
 *     synchronized ( state ) {
 *       count = calculateCount();
 *       state.setCount( count );
 *     }
 *   }</code></pre>
 *
 * <h3>Event Notification</h3>
 *
 * The list of listeners interested in event notification as features are modified and edited is
 * stored as part of ContentState. Each notification is considered to be broadcast from a specific
 * FeatureSource. Since several ContentFeatureStores can be on the same transaction (and thus share
 * a ContentState) the fire methods listed here require you pass in the FeatureSource making the
 * change; this requires that your individual FeatureWriters keep a pointer to the
 * ContentFeatureStore which created them.
 *
 * <p>You may also make use of {@link ContentFeatureSource#canEvent} value of {@code false} allowing
 * the base ContentFeatureStore class to take responsibility for sending event notifications.
 *
 * <h3>Transaction Independence</h3>
 *
 * <p>The default ContentState implementation also supports the handling of {@link
 * ContentFeatureSource#canTransaction} value of {@code false}. The implementation asks ContentState
 * to store a {@link Diff} which is used to record any modifications made until commit is called.
 *
 * <p>Internally a {@link Transaction.State} is used to notify the implementation of {{@link
 * Transaction#commit} and {@link Transaction#rollback()}.
 *
 * <h3>Extension</h3>
 *
 * This class may be extended if your implementaiton wishes to capture additional information per
 * transaction. A database implementation using a JDBCContentState to store a JDBC Connection
 * remains a good example.
 *
 * <p>Subclasses may extend (not override) the following methods:
 *
 * <ul>
 *   <li>{@link #flush()} - remember to call super.flush()
 *   <li>{@link #close()} - remember to call super.close()
 * </ul>
 *
 * Subclasses should also override {@link #copy()} to ensure any additional state they are keeping
 * is correctly accounted for.
 *
 * @author Jody Garnett (LISASoft)
 * @author Justin Deoliveira, The Open Planning Project
 * @version 8.0
 * @since 2.6
 */
public class ContentState {

    /** Transaction the state works from. */
    protected Transaction tx;

    /** entry maintaining the state */
    protected ContentEntry entry;

    // CACHE
    /** cached feature type */
    protected SimpleFeatureType featureType;

    /** cached number of features */
    protected int count = -1;

    /** cached bounds of features */
    protected ReferencedEnvelope bounds;

    // EVENT NOTIFICATION SUPPORT
    /**
     * Even used for batch notification; used to collect the bounds and feature ids generated over
     * the course of a transaction.
     */
    protected BatchFeatureEvent batchFeatureEvent;

    /** observers */
    protected List<FeatureListener> listeners =
            Collections.synchronizedList(new ArrayList<FeatureListener>());

    // TRANSACTION SUPPORT
    /**
     * Callback used to issue batch feature events when commit/rollback issued on the transaction.
     */
    protected DiffTransactionState transactionState = new DiffTransactionState(this);

    /**
     * Creates a new state.
     *
     * @param entry The entry for the state.
     */
    public ContentState(ContentEntry entry) {
        this.entry = entry;
    }

    /**
     * Creates a new state from a previous one.
     *
     * <p>All state from the specified <tt>state</tt> is copied. Therefore subclasses extending this
     * constructor should clone all mutable objects.
     *
     * @param state The existing state.
     */
    protected ContentState(ContentState state) {
        this(state.getEntry());

        featureType = state.featureType;
        count = state.count;
        bounds = state.bounds == null ? null : ReferencedEnvelope.reference(state.bounds);
        batchFeatureEvent = null;
    }

    /** The entry which maintains the state. */
    public ContentEntry getEntry() {
        return entry;
    }

    /** The transaction associated with the state. */
    public Transaction getTransaction() {
        return tx;
    }

    /** Sets the transaction associated with the state. */
    public void setTransaction(Transaction tx) {
        this.tx = tx;
        if (tx != Transaction.AUTO_COMMIT) {
            tx.putState(this.entry, transactionState);
        }
    }

    /** The cached feature type. */
    public final SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /** Sets the cached feature type. */
    public final void setFeatureType(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    /** The cached number of features. */
    public final int getCount() {
        return count;
    }

    /** Sets the cached number of features. */
    public final void setCount(int count) {
        this.count = count;
    }

    /** The cached spatial extent. */
    public final ReferencedEnvelope getBounds() {
        return bounds;
    }

    /** Sets the cached spatial extent. */
    public final void setBounds(ReferencedEnvelope bounds) {
        this.bounds = bounds;
    }

    /**
     * Adds a listener for collection events.
     *
     * @param listener The listener to add
     */
    public final void addListener(FeatureListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener for collection events.
     *
     * @param listener The listener to remove
     */
    public final void removeListener(FeatureListener listener) {
        listeners.remove(listener);
    }

    public BatchFeatureEvent getBatchFeatureEvent() {
        return batchFeatureEvent;
    }

    /** Used to quickly test if any listeners are available. */
    public final boolean hasListener() {
        if (!listeners.isEmpty()) {
            return true;
        }
        if (this.tx == Transaction.AUTO_COMMIT && this.entry.state.size() > 1) {
            // We are the auto commit state; and there is at least one other thread to notify
            return true;
        }
        return false;
    }
    /**
     * Creates a FeatureEvent indicating that the provided feature has been changed.
     *
     * <p>This method is provided to simplify event notification for implementors by constructing a
     * FeatureEvent internally (and then only if listeners are interested). You may find it easier
     * to construct your own FeatureEvent using {{@link #hasListener()} to check if you need to fire
     * events at all.
     *
     * @param source FeatureSource responsible for the change
     * @param feature The updated feature
     * @param before the bounds of the feature before the change
     */
    public void fireFeatureUpdated(
            FeatureSource<?, ?> source, Feature feature, ReferencedEnvelope before) {
        if (feature == null) {
            return; // nothing changed
        }
        if (listeners.isEmpty() && tx != Transaction.AUTO_COMMIT) return; // nobody is listenting

        Filter filter = idFilter(feature);
        ReferencedEnvelope bounds = ReferencedEnvelope.reference(feature.getBounds());
        if (bounds != null) {
            bounds.expandToInclude(before);
        }

        FeatureEvent event = new FeatureEvent(source, Type.CHANGED, bounds, filter);
        fireFeatureEvent(event);
    }

    /** Used to issue a Type.ADDED FeatureEvent indicating a new feature being created */
    public final void fireFeatureAdded(FeatureSource<?, ?> source, Feature feature) {
        if (listeners.isEmpty() && tx != Transaction.AUTO_COMMIT) return;

        Filter filter = idFilter(feature);
        ReferencedEnvelope bounds = ReferencedEnvelope.reference(feature.getBounds());

        FeatureEvent event = new FeatureEvent(source, Type.ADDED, bounds, filter);

        fireFeatureEvent(event);
    }

    public void fireFeatureRemoved(FeatureSource<?, ?> source, Feature feature) {
        if (listeners.isEmpty() && tx != Transaction.AUTO_COMMIT) return;

        Filter filter = idFilter(feature);
        ReferencedEnvelope bounds = ReferencedEnvelope.reference(feature.getBounds());

        FeatureEvent event = new FeatureEvent(source, Type.REMOVED, bounds, filter);

        fireFeatureEvent(event);
    }

    /** Helper method or building fid filters. */
    Filter idFilter(Feature feature) {
        FilterFactory ff = this.entry.dataStore.getFilterFactory();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(feature.getIdentifier());
        return ff.id(fids);
    }

    /**
     * Used to issue a single FeatureEvent.
     *
     * <p>If this content state is used for Transaction.AUTO_COMMIT the notification will be passed
     * to all interested parties.
     *
     * <p>If not this event will be recored as part of a BatchFeatureEvent that will to be issued
     * using issueBatchFeatureEvent()
     */
    public final void fireFeatureEvent(FeatureEvent event) {
        if (this.tx == Transaction.AUTO_COMMIT) {
            this.entry.notifiyFeatureEvent(this, event);
        } else {
            // we are not in auto-commit mode so we need to batch
            // up the changes for when the commit goes out
            if (batchFeatureEvent == null) {
                batchFeatureEvent = new BatchFeatureEvent(event.getFeatureSource());
            }
            batchFeatureEvent.add(event);
        }
        if (listeners.isEmpty()) {
            return;
        }
        for (FeatureListener listener : listeners) {
            try {
                listener.changed(event);
            } catch (Throwable t) {
                this.entry.dataStore.LOGGER.log(
                        Level.WARNING, "Problem issuing batch feature event " + event, t);
            }
        }
    }

    /**
     * Notifies all waiting listeners that a commit has been issued; this notification is also sent
     * to our
     */
    public final void fireBatchFeatureEvent(boolean isCommit) {
        if (batchFeatureEvent == null) {
            return;
        }
        if (isCommit) {
            batchFeatureEvent.setType(Type.COMMIT);

            // This state already knows about the changes, let others know a modifications was made
            this.entry.notifiyFeatureEvent(this, batchFeatureEvent);
        } else {
            batchFeatureEvent.setType(Type.ROLLBACK);

            // Notify this state about the rollback, other transactions see no change
            for (FeatureListener listener : listeners) {
                try {
                    listener.changed(batchFeatureEvent);
                } catch (Throwable t) {
                    this.entry.dataStore.LOGGER.log(
                            Level.WARNING,
                            "Problem issuing batch feature event " + batchFeatureEvent,
                            t);
                }
            }
        }

        batchFeatureEvent = null;
    }

    /**
     * Clears cached state.
     *
     * <p>This method does not affect any non-cached state. This method may be extended by
     * subclasses, but not overiden.
     */
    public void flush() {
        featureType = null;
        count = -1;
        bounds = null;
    }

    /**
     * Clears all state.
     *
     * <p>Any resources that the state holds onto (like a database connection) should be closed or
     * disposes when this method is called. This method may be extended by subclasses, but not
     * overiden.
     */
    public void close() {
        featureType = null;
        if (listeners != null) {
            listeners.clear();
            listeners = null;
        }
    }

    /**
     * Copies the state.
     *
     * <p>Subclasses should override this method. Any mutable state objects should be cloned.
     *
     * @return A copy of the state.
     */
    public ContentState copy() {
        return new ContentState(this);
    }
}
