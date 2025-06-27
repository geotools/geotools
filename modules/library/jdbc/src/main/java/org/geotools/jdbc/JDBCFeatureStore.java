/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.geotools.api.data.FeatureEvent;
import org.geotools.api.data.FeatureEvent.Type;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.FilteringFeatureWriter;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;

/**
 * FeatureStore implementation for jdbc based relational database tables.
 *
 * <p>All read only methods are delegated to {@link JDBCFeatureSource}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public final class JDBCFeatureStore extends ContentFeatureStore {

    /**
     * jdbc feature source to delegate to, we do this b/c we can't inherit from both ContentFeatureStore and
     * JDBCFeatureSource at the same time
     */
    public JDBCFeatureSource delegate;

    /**
     * Creates the new feature store.
     *
     * @param entry The datastore entry.
     * @param query The defining query.
     */
    @SuppressWarnings("unchecked")
    public JDBCFeatureStore(ContentEntry entry, Query query) throws IOException {
        super(entry, query);

        delegate = new JDBCFeatureSource(entry, query) {
            @Override
            public void setTransaction(Transaction transaction) {
                super.setTransaction(transaction);

                // keep this feature store in sync
                JDBCFeatureStore.this.setTransaction(transaction);
            }
        };

        Set<Hints.Key> jdbcHints = new HashSet<>();

        jdbcHints.addAll((Set<Hints.Key>) (Set<?>) delegate.getSupportedHints());
        getDataStore().getSQLDialect().addSupportedHints(jdbcHints);
        hints = Collections.unmodifiableSet(jdbcHints);
    }

    /** We handle events internally */
    @Override
    protected boolean canEvent() {
        return true;
    }

    @Override
    public JDBCDataStore getDataStore() {
        return delegate.getDataStore();
    }

    public JDBCFeatureSource getFeatureSource() {
        return delegate;
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
    public JDBCState getState() {
        return delegate.getState();
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
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

    public PrimaryKey getPrimaryKey() {
        return delegate.getPrimaryKey();
    }

    /**
     * Sets the flag which will expose columns which compose a tables identifying or primary key, through feature type
     * attributes.
     *
     * <p>Note: setting this flag which affect all feature sources created from or working against the current
     * transaction.
     */
    public void setExposePrimaryKeyColumns(boolean exposePrimaryKeyColumns) {
        delegate.setExposePrimaryKeyColumns(exposePrimaryKeyColumns);
    }

    /**
     * The flag which will expose columns which compose a tables identifying or primary key, through feature type
     * attributes.
     */
    public boolean isExposePrimaryKeyColumns() {
        return delegate.isExposePrimaryKeyColumns();
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
    protected boolean canFilter(Query query) {
        return delegate.canFilter(query);
    }

    @Override
    protected boolean canSort(Query query) {
        return delegate.canSort(query);
    }

    @Override
    protected boolean canRetype(Query query) {
        return delegate.canRetype(query);
    }

    @Override
    protected boolean canLimit(Query query) {
        return delegate.canLimit(query);
    }

    @Override
    protected boolean canOffset(Query query) {
        return delegate.canOffset(query);
    }

    @Override
    protected boolean canTransact() {
        return delegate.canTransact();
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        return delegate.getReaderInternal(query);
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return delegate.handleVisitor(query, visitor);
    }

    //  /**
    //  * This method operates by delegating to the
    //  * {@link JDBCFeatureCollection#update(AttributeDescriptor[], Object[])}
    //  * method provided by the feature collection resulting from
    //  * {@link #filtered(ContentState, Filter)}.
    //  *
    //  * @see FeatureStore#modifyFeatures(AttributeDescriptor[], Object[], Filter)
    //  */
    // public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
    //     throws IOException {
    //     if (filter == null) {
    //         String msg = "Must specify a filter, must not be null.";
    //         throw new IllegalArgumentException(msg);
    //     }
    //
    //     JDBCFeatureCollection features = (JDBCFeatureCollection) filtered(getState(), filter);
    //     features.update(type, value);
    // }

    @Override
    @SuppressWarnings("PMD.CloseResource") // the cx is passed to the reader which will close it
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(Query query, int flags)
            throws IOException {

        if (flags == 0) {
            throw new IllegalArgumentException("no write flags set");
        }

        // get connection from current state
        Connection cx = getDataStore().getConnection(getState());

        Filter postFilter;
        // check for update only case
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        try {
            // check for insert only
            if ((flags | WRITER_ADD) == WRITER_ADD) {
                Query queryNone = new Query(query);
                queryNone.setFilter(Filter.EXCLUDE);
                if (getDataStore().getSQLDialect() instanceof PreparedStatementSQLDialect) {
                    PreparedStatement ps = getDataStore().selectSQLPS(getSchema(), queryNone, cx);
                    return new JDBCInsertFeatureWriter(ps, cx, delegate, query);
                } else {
                    // build up a statement for the content, inserting only so we dont want
                    // the query to return any data ==> Filter.EXCLUDE
                    String sql = getDataStore().selectSQL(getSchema(), queryNone);
                    getDataStore().getLogger().fine(sql);

                    return new JDBCInsertFeatureWriter(sql, cx, delegate, query);
                }
            }

            // split the filter
            Filter[] split = delegate.splitFilter(query.getFilter(), query.getHints());
            Filter preFilter = split[0];
            postFilter = split[1];

            // build up a statement for the content
            Query preQuery = new Query(query);
            preQuery.setFilter(preFilter);
            if (getDataStore().getSQLDialect() instanceof PreparedStatementSQLDialect) {
                PreparedStatement ps = getDataStore().selectSQLPS(getSchema(), preQuery, cx);
                if ((flags | WRITER_UPDATE) == WRITER_UPDATE) {
                    writer = new JDBCUpdateFeatureWriter(ps, cx, delegate, query);
                } else {
                    // update insert case
                    writer = new JDBCUpdateInsertFeatureWriter(ps, cx, delegate, query.getPropertyNames(), query);
                }
            } else {
                String sql = getDataStore().selectSQL(getSchema(), preQuery);
                getDataStore().getLogger().fine(sql);

                if ((flags | WRITER_UPDATE) == WRITER_UPDATE) {
                    writer = new JDBCUpdateFeatureWriter(sql, cx, delegate, query);
                } else {
                    // update insert case
                    writer = new JDBCUpdateInsertFeatureWriter(sql, cx, delegate, query);
                }
            }
        } catch (Throwable e) { // NOSONAR
            // close the connection
            getDataStore().closeSafe(cx);
            // safely rethrow
            if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw (IOException) new IOException().initCause(e);
            }
        }

        // check for post filter and wrap accordingly
        if (postFilter != null && postFilter != Filter.INCLUDE) {
            writer = new FilteringFeatureWriter(writer, postFilter);
        }
        return writer;
    }

    @Override
    public void modifyFeatures(Name[] names, Object[] values, Filter filter) throws IOException {

        // we cannot trust attribute definitions coming from outside, they might not
        // have the same metadata the inner ones have. Let's remap them
        AttributeDescriptor[] innerTypes = new AttributeDescriptor[names.length];
        for (int i = 0; i < names.length; i++) {
            innerTypes[i] = getSchema().getDescriptor(names[i].getLocalPart());
            if (innerTypes[i] == null)
                throw new IllegalArgumentException("Unknown attribute " + names[i].getLocalPart());
        }

        // split the filter
        Filter[] splitted = delegate.splitFilter(filter, this.query != null ? this.query.getHints() : null);
        Filter preFilter = splitted[0];
        Filter postFilter = splitted[1];

        if (postFilter != null && !Filter.INCLUDE.equals(postFilter)) {
            // we don't have a fast way to perform this update, let's do it the
            // feature by feature way then
            super.modifyFeatures(names, values, filter);
        } else {
            // let's grab the connection
            Connection cx = null;
            @SuppressWarnings("PMD.CloseResource") // transaction closing managed elsewhere
            Transaction tx = getState().getTransaction();
            try {
                cx = getDataStore().getConnection(tx);

                // we want to support a "batch" update, but we need to be weary of locks
                SimpleFeatureType featureType = getSchema();
                try {
                    getDataStore().ensureAuthorization(featureType, preFilter, getTransaction(), cx);
                } catch (SQLException e) {
                    throw (IOException) new IOException().initCause(e);
                }
                ContentState state = getEntry().getState(transaction);
                ReferencedEnvelope bounds =
                        ReferencedEnvelope.create(getSchema().getCoordinateReferenceSystem());
                if (state.hasListener()) {
                    // gather bounds before modification
                    ReferencedEnvelope before = getBounds(new Query(getSchema().getTypeName(), preFilter));
                    if (before != null && !before.isEmpty()) {
                        bounds = before;
                    }
                }
                try {
                    getDataStore().update(getSchema(), innerTypes, values, preFilter, cx);
                } catch (SQLException e) {
                    throw (IOException) new IOException(e.getMessage()).initCause(e);
                }

                if (state.hasListener()) {
                    // gather any updated bounds due to a geometry modification
                    for (Object value : values) {
                        if (value instanceof Geometry) {
                            Geometry geometry = (Geometry) value;
                            bounds.expandToInclude(geometry.getEnvelopeInternal());
                        }
                    }
                    // issue notificaiton
                    FeatureEvent event = new FeatureEvent(this, Type.CHANGED, bounds, preFilter);
                    state.fireFeatureEvent(event);
                }
            } finally {
                if (tx == null || tx == Transaction.AUTO_COMMIT) {
                    getDataStore().closeSafe(cx);
                }
            }
        }
    }

    @Override
    public void removeFeatures(Filter filter) throws IOException {
        Filter[] splitted = delegate.splitFilter(filter, this.query != null ? this.query.getHints() : null);
        Filter preFilter = splitted[0];
        Filter postFilter = splitted[1];

        if (postFilter != null && !Filter.INCLUDE.equals(postFilter)) {
            // we don't have a fast way to perform this delete, let's do it the
            // feature by feature way then
            super.removeFeatures(filter);
        } else {
            // let's grab the connection
            @SuppressWarnings("PMD.CloseResource") // transaction closing managed elsewhere
            Transaction tx = getState().getTransaction();
            Connection cx = null;

            try {
                cx = getDataStore().getConnection(tx);

                // we want to support a "batch" delete, but we need to be weary of locks
                SimpleFeatureType featureType = getSchema();
                try {
                    getDataStore().ensureAuthorization(featureType, preFilter, getTransaction(), cx);
                } catch (SQLException e) {
                    throw (IOException) new IOException().initCause(e);
                }
                ContentState state = getEntry().getState(transaction);
                ReferencedEnvelope bounds =
                        ReferencedEnvelope.create(getSchema().getCoordinateReferenceSystem());
                if (state.hasListener()) {
                    // gather bounds before modification
                    ReferencedEnvelope before = getBounds(new Query(getSchema().getTypeName(), preFilter));
                    if (before != null && !before.isEmpty()) {
                        bounds = before;
                    }
                }
                getDataStore().delete(featureType, preFilter, cx);
                if (state.hasListener()) {
                    // issue notification
                    FeatureEvent event = new FeatureEvent(this, Type.REMOVED, bounds, preFilter);
                    state.fireFeatureEvent(event);
                }
            } finally {
                if (tx == null || tx == Transaction.AUTO_COMMIT) {
                    getDataStore().closeSafe(cx);
                }
            }
        }
    }
}
