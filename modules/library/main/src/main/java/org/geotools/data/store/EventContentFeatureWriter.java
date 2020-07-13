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
import org.geotools.data.BatchFeatureEvent;
import org.geotools.data.DiffFeatureWriter;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * FeatureWriter wrapper that issues events modifications as required.
 *
 * <p>It is the responsibility of a FeatureStore to issue events to interested parties as content is
 * modified. The {@link ContentState} keeps track of the listeners, while {@link
 * EventContentFeatureWriter} is willing to fire the events as needed.
 *
 * <p>Event generation happens in two passes:
 *
 * <ul>
 *   <li>As features are modified events are sent out one at a time
 *   <li>When commit() or rollback() is called a "batch" event is sent out
 * </ul>
 *
 * The only trick is the comment() event contains our only indication of the final FeatureIDs
 * generated for new features. The {@link BatchFeatureEvent} maintains a map of BEFORE/AFTER values
 * allowing any interested party to update their seleciton.
 *
 * <p>Please note that if you are using {@link DiffFeatureWriter} it sends out events on its own.
 *
 * @author Jody Garnett (LISASoft)
 */
public class EventContentFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    ContentState state;

    SimpleFeature feature; // live value supplied by writer

    ContentFeatureStore store;

    FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

    /** EventContentFeatureWriter construction. */
    public EventContentFeatureWriter(
            ContentFeatureStore store, FeatureWriter<SimpleFeatureType, SimpleFeature> writer) {
        this.store = store;
        this.writer = writer;
        this.state = store.getState();

        @SuppressWarnings("PMD.CloseResource") // not to be closed, not generated
        Transaction t = state.getTransaction();
        if (t != Transaction.AUTO_COMMIT) {
            // auto commit does not issue batch events
            t.putState(this, new EventContentTransactionState());
        }
    }

    /**
     * Supplys FeatureTypeFrom reader
     *
     * @see org.geotools.data.FeatureWriter#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return writer.getFeatureType();
    }

    /**
     * Next Feature from reader or new content.
     *
     * @see org.geotools.data.FeatureWriter#next()
     */
    public SimpleFeature next() throws IOException {
        if (writer == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        feature = writer.next();
        return feature;
    }

    /** @see org.geotools.data.FeatureWriter#remove() */
    public void remove() throws IOException {
        if (writer == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        state.fireFeatureRemoved(store, feature);
        writer.remove();
    }

    /**
     * Writes out the current feature.
     *
     * @see org.geotools.data.FeatureWriter#write()
     */
    public void write() throws IOException {
        if (writer == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        writer.write();
        if (feature == null) {
            throw new IOException("No feature available to write");
        }
        if (writer.hasNext()) {
            // modify existing feature
            ReferencedEnvelope bounds = ReferencedEnvelope.reference(feature.getBounds());
            state.fireFeatureUpdated(store, feature, bounds);
            feature = null;
        } else {
            // modify a new feature - ie we are adding something
            state.fireFeatureAdded(store, feature);
        }
    }

    /**
     * Query for more content.
     *
     * @see org.geotools.data.FeatureWriter#hasNext()
     */
    public boolean hasNext() throws IOException {
        if (writer == null) {
            return false;
        }
        return writer.hasNext();
    }

    /**
     * Clean up resources associated with this writer.
     *
     * <p>Diff is not clear()ed as it is assumed that it belongs to a Transaction.State object and
     * may yet be written out.
     *
     * @see org.geotools.data.FeatureWriter#close()
     */
    public void close() throws IOException {
        @SuppressWarnings("PMD.CloseResource") // not to be closed here
        Transaction t = state.getTransaction();
        if (t != Transaction.AUTO_COMMIT) {
            t.removeState(this);
        }
        if (writer != null) {
            writer.close();
            writer = null;
        }
        feature = null;
    }
    /**
     * Used to detect commit() and rollback() in order to fire batch feature events.
     *
     * @author jody
     */
    class EventContentTransactionState implements Transaction.State {
        @Override
        public void commit() throws IOException {
            store.getState().fireBatchFeatureEvent(true);
        }

        @Override
        public void rollback() throws IOException {
            store.getState().fireBatchFeatureEvent(false);
        }

        @Override
        public void setTransaction(Transaction transaction) {
            // not needed to issue batch events (as they pretend to be from AUTO_COMMIT)
        }

        @Override
        public void addAuthorization(String AuthID) throws IOException {
            // functionality not restricted by lock authorisation
        }
    }
}
