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
package org.geotools.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;

/**
 * Responsible for flow control; issues commit and rollback on the managed connection.
 *
 * @source $URL$
 */
final class JDBCTransactionState implements State {
    /**
     * The datastore
     */
    JDBCDataStore dataStore;    
    /**
     * the current transaction
     */
    Transaction tx;
    /**
     * The current connection
     */
    Connection cx;
    /**
     * Whether the connection is internally managed, or externally provided (in the latter
     * case no attempt to commit, rollback or close will be done)
     */
    boolean external;

    public JDBCTransactionState(Connection cx, JDBCDataStore dataStore) {
        this(cx, dataStore, false);
    }
    
    public JDBCTransactionState(Connection cx, JDBCDataStore dataStore, boolean external) {
        this.cx = cx;
        this.dataStore = dataStore;
        this.external = external;
    }

    public void setTransaction(Transaction tx) {
        if ( tx != null && this.tx != null ) {
            throw new IllegalStateException( "New transaction set without " +
                "closing old transaction first.");
        }
            
        if ( tx == null ) {
            if ( cx != null && !external) {
                dataStore.closeSafe(cx);
            }  else {
                dataStore.getLogger().warning("Transaction is attempting to " +
                    "close an already closed connection");
            }
            cx = null;
        }
        
        this.tx = tx;
    }

    public void addAuthorization(String AuthID) throws IOException {
    }

    public void commit() throws IOException {
        if(!external) {        
            try {
                cx.commit();
            } catch (SQLException e) {
                String msg = "Error occured on commit";
                throw (IOException) new IOException(msg).initCause(e);
            }        
        }
    }

    public void rollback() throws IOException {
        if(!external) {
            try {
                cx.rollback();
            } catch (SQLException e) {
                String msg = "Error occured on rollback";
                throw (IOException) new IOException(msg).initCause(e);
            }
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        if ( !external && cx != null && !cx.isClosed()) {
            Logger.getLogger( "org.geotools.jdbc").severe("State finalized with open connection.");
            dataStore.closeSafe(cx);
        }
    }
}
