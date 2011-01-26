/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.FeatureTypeHandler;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.data.postgis.fidmapper.PostgisFIDMapperFactory;
import org.geotools.data.postgis.fidmapper.VersionedFIDMapperFactory;

/**
 * A postgis datastore subclass that provides extra accessors so that the versioned datastore can
 * work.<br>
 * This class is needed because this way all the JDBC datastore infrastructure can work without the
 * need to break its assumptions.<br>
 * For example, getSchema() usually returns a feature type that mimics the table structure, but for
 * versioned data we have to return one that does not have the versioning columns.
 * 
 * @author aaime
 * @since 2.4
 * 
 */
class WrappedPostgisDataStore extends PostgisDataStore {

    public WrappedPostgisDataStore(DataSource dataSource, JDBCDataStoreConfig config,
            int optimizeMode) throws IOException {
        super(dataSource, config, optimizeMode);

    }

    public WrappedPostgisDataStore(DataSource dataSource, String schema, String namespace,
            int optimizeMode) throws IOException {
        super(dataSource, schema, namespace, optimizeMode);

    }

    public WrappedPostgisDataStore(DataSource dataSource, String schema, String namespace)
            throws IOException {
        super(dataSource, schema, namespace);

    }

    public WrappedPostgisDataStore(DataSource dataSource, String namespace) throws IOException {
        super(dataSource, namespace);

    }

    public WrappedPostgisDataStore(DataSource dataSource) throws IOException {
        super(dataSource);

    }

    /**
     * Overridden to store a versioned jdbc transaction state instead
     */
    public Connection getConnection(Transaction transaction) throws IOException {
        if (transaction != Transaction.AUTO_COMMIT) {
            // we will need to save a JDBC connection is
            // transaction.putState( connectionPool, JDBCState )
            // throw new UnsupportedOperationException("Transactions not
            // supported yet");

            VersionedJdbcTransactionState state;

            state = getVersionedJdbcTransactionState(transaction);
            return state.getConnection();
        }

        try {
            return createConnection();
        } catch (SQLException sqle) {
            throw new DataSourceException("Connection failed:" + sqle, sqle);
        }
    }

    /**
     * Returns the versioned jdbc transaction state, eventually
     * 
     * @param transaction
     * @return
     * @throws IOException
     * @throws DataSourceException
     */
    protected VersionedJdbcTransactionState getVersionedJdbcTransactionState(Transaction transaction)
            throws IOException, DataSourceException {
        synchronized (transaction) {
            VersionedJdbcTransactionState state;
            state = (VersionedJdbcTransactionState) transaction.getState(this);

            if (state == null) {
                try {
                    Connection conn = createConnection();
                    conn.setAutoCommit(requireAutoCommit());
                    if (getTransactionIsolation() != Connection.TRANSACTION_NONE) {
                        // for us, NONE means use the default, which is
                        // usually READ_COMMITTED
                        conn.setTransactionIsolation(getTransactionIsolation());
                    }
                    state = new VersionedJdbcTransactionState(conn, this);
                    transaction.putState(this, state);
                } catch (SQLException eep) {
                    throw new DataSourceException("Connection failed:" + eep, eep);
                }
            }
            return state;
        }
    }

    protected FIDMapperFactory buildFIDMapperFactory(JDBCDataStoreConfig config) {
        return new VersionedFIDMapperFactory(new PostgisFIDMapperFactory(config));
    }

    public String[] propertyNames(Query query) throws IOException {
        return super.propertyNames(query);
    }

    public JDBCDataStoreConfig getConfig() {
        return config;
    }

    public FeatureTypeHandler getTypeHandler() {
        return typeHandler;
    }

}
