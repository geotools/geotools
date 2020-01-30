/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureEvent.Type;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

@SuppressWarnings("unchecked")
class WFSFeatureStore extends ContentFeatureStore {

    private WFSFeatureSource delegate;

    public WFSFeatureStore(WFSFeatureSource source) {
        super(source.getEntry(), null);
        this.delegate = source;
    }

    @Override
    public boolean canReproject() {
        return delegate.canReproject();
    }

    /**
     * @return {@code false}, only in-process feature locking so far.
     * @see org.geotools.data.store.ContentFeatureSource#canLock()
     */
    @Override
    public boolean canLock() {
        return false; //
    }

    @Override
    protected boolean canEvent() {
        return true;
    }

    @Override
    public WFSDataStore getDataStore() {
        return delegate.getDataStore();
    }

    @Override
    public ContentEntry getEntry() {
        return delegate.getEntry();
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

    @Override
    public WFSContentState getState() {
        return (WFSContentState) delegate.getState();
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return delegate.buildFeatureType();
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        return delegate.getCount(query);
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        return delegate.getBoundsInternal(query);
    }

    @Override
    protected boolean canFilter() {
        return delegate.canFilter();
    }

    @Override
    protected boolean canSort() {
        return delegate.canSort();
    }

    @Override
    protected boolean canRetype() {
        return delegate.canRetype();
    }

    @Override
    protected boolean canLimit() {
        return delegate.canLimit();
    }

    @Override
    protected boolean canOffset() {
        return delegate.canOffset();
    }

    @Override
    protected boolean canTransact() {
        return true;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return delegate.getReaderInternal(query);
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }

    @Override
    public void setTransaction(Transaction transaction) {
        // JD: note, we need to set both super and delegate transactions.
        super.setTransaction(transaction);

        // JD: this guard ensures that a recursive loop will not form
        if (delegate.getTransaction() != transaction) {
            delegate.setTransaction(transaction);
        }
    }

    @Override
    protected WFSFeatureWriter getWriterInternal(Query query, final int flags) throws IOException {

        query = joinQuery(query);
        query = resolvePropertyNames(query);

        final boolean autoCommit;
        WFSLocalTransactionState localState;
        if (Transaction.AUTO_COMMIT.equals(getTransaction())) {
            localState = getState().getLocalTransactionState();
            autoCommit = true;
        } else {
            autoCommit = false;
            State state = transaction.getState(getEntry());
            localState = (WFSLocalTransactionState) state;
        }

        return new WFSFeatureWriter(this, localState, getReader(query), autoCommit);
    }

    @Override
    public void modifyFeatures(Name[] properties, Object[] values, Filter filter)
            throws IOException {
        if (filter == null) {
            throw new IllegalArgumentException("filter is null");
        }

        filter = resolvePropertyNames(filter);

        {
            QName typeName = getDataStore().getRemoteTypeName(getName());
            WFSClient wfsClient = getDataStore().getWfsClient();
            Filter[] splitFilters = wfsClient.splitFilters(typeName, filter);
            Filter unsupported = splitFilters[1];

            if (!Filter.INCLUDE.equals(unsupported)) {
                // Filter not fully supported, lets modify one by one
                super.modifyFeatures(properties, values, filter);
                return;
            }
        }

        // Filter fully supported, lets batch modify
        final ContentState contentState = getState();

        ReferencedEnvelope affectedBounds = new ReferencedEnvelope(getInfo().getCRS());
        if (contentState.hasListener()) {
            // gather bounds before modification
            ReferencedEnvelope before = getBounds(new Query(getSchema().getTypeName(), filter));
            if (before != null && !before.isEmpty()) {
                affectedBounds = before;
            }
        }
        @SuppressWarnings("PMD.CloseResource") // not managed here
        final Transaction transaction = getTransaction();

        try (FeatureReader<SimpleFeatureType, SimpleFeature> oldFeatures = getReader(filter)) {
            if (Transaction.AUTO_COMMIT.equals(transaction)) {
                // we're in auto commit. Do a batch update and commit right away
                WFSLocalTransactionState localState = getState().getLocalTransactionState();
                WFSRemoteTransactionState committingState =
                        new WFSRemoteTransactionState(getDataStore());
                committingState.watch(localState.getState());

                WFSDiff diff = localState.getDiff();

                ReferencedEnvelope bounds;
                bounds = diff.batchModify(properties, values, filter, oldFeatures, contentState);
                affectedBounds.expandToInclude(bounds);
                committingState.commit();

            } else {
                // we're in a transaction, record to local state and wait for commit to be called
                WFSLocalTransactionState localState;
                localState = (WFSLocalTransactionState) transaction.getState(getEntry());
                WFSDiff diff = localState.getDiff();

                ReferencedEnvelope bounds;
                bounds = diff.batchModify(properties, values, filter, oldFeatures, contentState);
                affectedBounds.expandToInclude(bounds);
            }

            if (contentState.hasListener()) {
                // issue notificaiton
                FeatureEvent event = new FeatureEvent(this, Type.CHANGED, affectedBounds, filter);
                contentState.fireFeatureEvent(event);
            }
        }
    }
}
