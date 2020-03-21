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
package org.geotools.data;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

/**
 * The controller for Transaction with FeatureStore.
 *
 * <p>Shapefiles, databases, etc. are safely modified with the assistance of this interface.
 * Transactions are also to provide authorization when working with locked features.
 *
 * <p>All operations are considered to be working against a Transaction. Transaction.AUTO_COMMIT is
 * used to represent an immidiate mode where requests are immidately commited.
 *
 * <p>For more information please see DataStore and FeatureStore.
 *
 * <p>Example Use:
 *
 * <pre><code>
 * Transaction t = new DefaultTransaction("handle");
 * t.putProperty( "hint", Integer.valueOf(7) );
 * try {
 *     SimpleFeatureStore road = (SimpleFeatureStore) store.getFeatureSource("road");
 *     FeatureStore river = (SimpleFeatureStore) store.getFeatureSource("river");
 *
 *     road.setTransaction( t );
 *     river.setTransaction( t );
 *
 *     t.addAuthorization( lockID );  // provide authoriztion
 *     road.removeFeatures( filter ); // operate against transaction
 *     river.removeFeature( filter ); // operate against transaction
 *
 *     t.commit(); // commit operations
 * }
 * catch (IOException io){
 *     t.rollback(); // cancel operations
 * }
 * finally {
 *     t.close(); // free resources
 * }
 * </code></pre>
 *
 * <p>Example code walkthrough (from the perspective of Transaction):
 *
 * <ol>
 *   <li>A new transaction is created (an instanceof DefaultTransaction with a handle)
 *   <li>A hint is provided using Transaction.putProperty( key, value )
 *   <li>Transaction is provided to two FeatureStores, this may result in Transaction.State
 *       instances being registered
 *       <ul>
 *         <li>DiffTransactionState (stored by DataStore): Used for in memory locking by many
 *             DataStore's (like ShapefileDataStore). Lazy creation as part of
 *             ContentDataStore.getFeatureSource(Name typeName, Transaction tx).
 *         <li>JDBCTransactionState (stored by ConnectionPool): Used to manage connection
 *             rollback/commit. Lazy creation as part of JDBCDataStore.getConnection(transaction).
 *         <li>InProcessLockingManager.FeatureLock (stored by LockingManger): Used for per
 *             transaction FeatureLocks, used to free locked features on Transaction
 *             commit/rollback.
 *       </ul>
 *       These instances of Transaction state may make use of any hint provided to
 *       Transaction.putProperty( key, value ) when they are connected with
 *       Transaction.State.setTransaction( transaction ).
 *   <li>t.addAuthorization(lockID) is called, each Transaction.State has its
 *       addAuthroization(String) callback invoked with the value of lockID
 *   <li>FeatureStore.removeFeatures methods are called on the two DataStores.
 *       <ul>
 *         <li>PostgisFeatureStore.removeFeatures(filter) handles operation without delegation.
 *         <li>Most removeFeature(filter) implementations use the implementation provided by
 *             ContentFeatureStore which delegates to FeatureWriter.
 *       </ul>
 *       Any of these operations may make use of the Transaction.putProperty( key, value ).
 *   <li>The transaction is committed, all of the Transaction.State methods have there
 *       Transaction.State.commit() methods called giving them a chance to applyDiff maps, or commit
 *       various connections.
 *   <li>The transaction is closed, all of the Transaction.State methods have there
 *       Transaction.State.setTransaction( null ) called, giving them a chance to clean up diffMaps,
 *       or return connections to the pool.
 * </ol>
 *
 * @author Jody Garnett
 * @author Chris Holmes, TOPP
 */
public interface Transaction extends Closeable {
    /**
     * Represents AUTO_COMMIT mode as opposed to operations with commit/rollback control under a
     * user-supplied transaction.
     */
    static final Transaction AUTO_COMMIT = new AutoCommitTransaction();

    //
    // External State
    //
    /**
     * Retrive a Transaction property held by this transaction.
     *
     * <p>This may be used to provide hints to DataStore implementations, it operates as a
     * blackboard for client, SimpleFeatureSource communication.
     */
    Object getProperty(Object key);

    /**
     * List of Authorizations IDs held by this transaction.
     *
     * <p>This list is reset by the next call to commit() or rollback().
     *
     * <p>Authorization IDs are used to provide FeatureLock support.
     *
     * @return List of Authorization IDs
     */
    Set<String> getAuthorizations();

    /**
     * Allows SimpleFeatureSource to squirel away information( and callbacks ) for later.
     *
     * <p>The most common example is a JDBC DataStore saving the required connection for later
     * operations.
     *
     * <pre><code>
     * ConnectionState implements State {
     *     public Connection conn;
     *     public addAuthorization() {}
     *     public commit(){ conn.commit(); }
     *     public rollback(){ conn.rollback(); }
     * }
     * </code></pre>
     *
     * <p>putState will call State.setTransaction( transaction ) to allow State a chance to
     * configure itself.
     *
     * @param key Key used to externalize State
     * @param state Externalized State
     */
    void putState(Object key, State state);

    /**
     * Allows FeatureSources to clean up information ( and callbacks ) they earlier provided.
     *
     * <p>Care should be taken when using shared State to not remove State required by another
     * FeatureSources.
     *
     * <p>removeState will call State.setTransaction( null ) to allow State a chance cleanup after
     * itself.
     *
     * @param key Key that was used to externalize State
     */
    void removeState(Object key);

    /**
     * Allows DataStores to squirel away information( and callbacks ) for later.
     *
     * <p>The most common example is a JDBC DataStore saving the required connection for later
     * operations.
     *
     * @return Current State externalized by key, or <code>null</code> if not found
     */
    State getState(Object key);

    //
    // Flow Control
    //

    /**
     * Makes all transactions made since the previous commit/rollback permanent.
     *
     * <p>FeatureSources will need to issue any changes notifications using a
     * FeatureEvent.FEATURES_CHANGED to all FeatureSources with the same typeName and a different
     * Transaction. FeatureSources with the same Transaction will of been notified of changes as the
     * FeaureWriter made them.
     *
     * @throws DataSourceException if there are any datasource errors.
     * @see #setAutoCommit(boolean)
     */
    void commit() throws IOException;

    /**
     * Undoes all transactions made since the last commit or rollback.
     *
     * <p>FeatureSources will need to issue any changes notifications using a
     * FeatureEvent.FEATURES_CHANGED. This will need to be issued to all FeatureSources with the
     * same typeName and Transaction.
     *
     * @throws DataSourceException if there are problems with the datasource.
     * @throws UnsupportedOperationException if the rollback method is not supported by this
     *     datasource.
     * @see #setAutoCommit(boolean)
     */
    void rollback() throws IOException;

    //
    // Locking Support
    //

    /**
     * Provides an Authorization ID for this Transaction.
     *
     * <p>All proceeding modifyFeatures,removeFeature, unLockFeatures, refreshLock and ReleaseLock
     * operations will make use of the provided authorization.
     *
     * <p>Authorization is only maintained until the this Transaction is commited or rolledback.
     *
     * <p>That is operations will only succeed if affected features either:
     *
     * <ul>
     *   <li>not locked
     *   <li>locked with the provided authID
     * </ul>
     *
     * <p>Authorization ID is provided as a String, rather than a FeatureLock, to account for across
     * process lock use.
     */
    void addAuthorization(String authID) throws IOException;

    /**
     * Provides a Transaction property for this Transasction.
     *
     * <p>All proceeding SimpleFeatureSource (for FeatureReader/Writer) operations may make use of
     * the provided property.
     */
    void putProperty(Object key, Object value) throws IOException;

    /**
     * Provides an opportunity for a Transaction to free an State it maintains.
     *
     * <p>This method should call State.setTransaction( null ) on all State it maintains.
     *
     * <p>It is hoped that FeatureStore implementations that have externalized their State with the
     * transaction take the opportunity to revert to Transction.AUTO_COMMIT.
     */
    void close() throws IOException;

    /**
     * DataStore implementations can use this interface to externalize the state they require to
     * implement Transaction Support.
     *
     * <p>The commit and rollback methods will be called as required. The intension is that several
     * DataStores can share common transaction state (example: Postgis DataStores sharing a
     * connection to the same database).
     *
     * @author jgarnett, Refractions Reasearch Inc.
     * @version CVS Version
     * @see org.geotools.data
     */
    public static interface State {
        /**
         * Provides configuration information for Transaction.State
         *
         * <p>setTransaction is called with non null <code>transaction</code> when Transaction.State
         * is <code>putState</code> into a Transaction. This tranasction will be used to determine
         * correct event notification.
         *
         * <p>setTransaction is called with <code>null</code> when removeState is called (usually
         * during Transaction.close() ).
         */
        void setTransaction(Transaction transaction);

        /** Call back used for Transaction.setAuthorization() */
        void addAuthorization(String AuthID) throws IOException;

        /** Call back used for Transaction.commit() */
        void commit() throws IOException;

        /** Call back used for Transaction.rollback() */
        void rollback() throws IOException;
    }
}
