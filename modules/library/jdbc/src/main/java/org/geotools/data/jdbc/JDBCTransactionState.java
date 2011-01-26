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
package org.geotools.data.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;

/**
 * Holds a JDBC Connectino for JDBCDataStore.
 * <p>
 * An alternative would be to hold the connection pool in the Transaction
 * State and only construct a connection when setTransaction is called.
 * </p>
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class JDBCTransactionState implements State {
    private Connection connection;
    
    public JDBCTransactionState( Connection connection ) throws IOException{
        this.connection = connection;            
    }
    public JDBCTransactionState( DataSource pool) throws IOException{
        try {
            connection = pool.getConnection();
            connection.setAutoCommit( false );                                    
        } catch (SQLException e) {
            connection = null;
            // This has not harmed the Transaction yet
            // so we don't need to force a rollback :-)
            throw new DataSourceException("Transaction could not acquire connection", e );
        }
    }
    /**
     * Retrieve connection for JDBC operation.
     * <p>
     * This connection may be used to issue JDBC operations against
     * this transaction.
     * </p>
     * <p>
     * Please do not use:
     * </p>
     * <ul>
     * <li>setAutoCommit()</li>
     * <li>commit()</li>
     * <li>rollback()</li>
     * </ul>
     */
    public Connection getConnection(){
        // We could make a wrapper to prevent the above
        // restricted operations
        return connection;
    }
    /**
     * Closes internal connection returns it to the ConnectionPool.
     * 
     * @see org.geotools.data.Transaction.State#setTransaction(org.geotools.data.Transaction)
     * @param transaction
     */
    public void setTransaction(Transaction transaction) {
        if( transaction == null){
            if( connection != null){
                try {
                    // return to pool
                    connection.close();
                } catch (SQLException e) {
                    // does this need to fail quiet?
                    // (at least it won't mess up the transaction)
                }            
                connection = null;
            }
            else {
                // connection was already closed !
                // We will be quiet on this one to
                // allow finally blocks a bit a of leaway                                    
            }
        }
    }

    /**
     * Not used by JDBCTransactionState
     * <p>
     * Postgis will need to override this method to use strong transaction
     * support.
     * </p>
     * @see org.geotools.data.Transaction.State#addAuthorization(java.lang.String)
     * @param AuthID
     * @throws IOException
     */
    public void addAuthorization(String AuthID) throws IOException {
        // not needed (yet)
    }

    /**
     * Commit the maintained state. 
     * <p>
     * JDBCTransactionState offers native support for this operation
     * </p>
     * @see org.geotools.data.Transaction.State#commit()
     * @throws IOException
     */
    public void commit() throws IOException {
        try {
            connection.commit();
        } catch (SQLException e) {
            // TODO: a rock and a hard place
            //
            // This is a horrible situation.
            // I assume the commit failed meaning that this connection was 
            // transaction was rolled back.
            // This is the worse though as we are calling this in a
            // loop as we commit all other Transaction.State!
            // 
            // That is we should rollback, but we cannot! (As we may have already
            // commited someone else)
            // We are absolutely going to have an inconsistent state here
            // and there is nothing I can do about it :-(
            //
            // This would be a design flaw, if there was anything we
            // could do about it
            throw new DataSourceException( "Transaction commit", e );
        }
    }

    /**
     * Rollback state of Transacstion.
     * <p>
     * JDBCTransactionState offers native support for this operation
     * </p>
     * @see org.geotools.data.Transaction.State#rollback()
     * @throws IOException
     */
    public void rollback() throws IOException {
        try {
            // TODO: does conneciton rollback clear warnings?
            connection.rollback();            
        } catch (SQLException e) {
            // TODO: between a rock and a rock
            // I am not sure how a rollback can fail, but we managed it
            // since the correct response is to rollback the Transaciton
            // we will continue on
            throw new DataSourceException( "Transaction rollback", e );
        }
    }
}
