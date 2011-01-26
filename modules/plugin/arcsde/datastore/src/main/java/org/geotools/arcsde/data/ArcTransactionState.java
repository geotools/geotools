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
 *
 */
package org.geotools.arcsde.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.arcsde.versioning.TransactionVersionHandler;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.Transaction;
import org.geotools.util.logging.Logging;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeState;
import com.esri.sde.sdk.client.SeVersion;

/**
 * Store the transaction state for <code>ArcSDEFeatureWriter</code> instances.
 * 
 * @author Jake Fear
 * @author Gabriel Roldan
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/ArcTransactionState.java $
 * @version $Id$
 */
final class ArcTransactionState implements Transaction.State {
    private static final Logger LOGGER = Logging.getLogger(ArcTransactionState.class.getName());

    /**
     * ArcSDEDataStore we can use to look up a Session for our Transaction.
     * <p>
     * The ConnectionPool will hold this connection open for us until commit(), rollback() or
     * close() is called.
     */
    private ArcSDEDataStore dataStore;

    private Transaction transaction;

    private final FeatureListenerManager listenerManager;

    /**
     * Set of typename changed to fire changed events for at commit and rollback.
     */
    private final Set<String> typesChanged = new HashSet<String>();

    public SeState currentVersionState;

    public SeObjectId initialStateId;

    public SeVersion defaultVersion;

    private ArcSdeVersionHandler versionHandler = ArcSdeVersionHandler.NONVERSIONED_HANDLER;

    /**
     * Creates a new ArcTransactionState object.
     * 
     * @param listenerManager
     * @param arcSDEDataStore
     *            connection pool where to grab a connection and hold it while there's a transaction
     *            open (signaled by any use of {@link #getConnection()}
     */
    ArcTransactionState(ArcSDEDataStore dataStore, final FeatureListenerManager listenerManager) {
        this.dataStore = dataStore;
        this.listenerManager = listenerManager;
    }

    private void setupVersioningHandling(final String versionName) throws IOException {
        // create a versioned handler only if not already settled up, as this method
        // may be called for each layer inside a transaction
        if (versionHandler == ArcSdeVersionHandler.NONVERSIONED_HANDLER) {
            ISession session = getConnection();
            versionHandler = new TransactionVersionHandler(session, versionName);
        }
    }

    /**
     * @param versioName
     *            the name of the version to work against
     * @return
     * @throws IOException
     */
    public ArcSdeVersionHandler getVersionHandler(final boolean ftIsVersioned,
            final String versionName) throws IOException {
        if (ftIsVersioned) {
            setupVersioningHandling(versionName);
        }
        return versionHandler;
    }

    /**
     * Registers a feature change event over a feature type.
     * <p>
     * To be called by {@link TransactionFeatureWriter#write()} so this state can fire a changed
     * event at {@link #commit()} and {@link #rollback()}.
     * </p>
     * 
     * @param typeName
     *            the type name of the feature changed (inserted/removed/modified).
     */
    public void addChange(final String typeName) {
        typesChanged.add(typeName);
    }

    /**
     * Commits the transaction and returns the connection to the pool. A new one will be grabbed
     * when needed.
     * <p>
     * Preconditions:
     * <ul>
     * <li>{@link #setTransaction(Transaction)} already called with non <code>null</code> argument.
     * <li>
     * </ul>
     * </p>
     */
    public void commit() throws IOException {
        failIfClosed();
        final ISession session = this.getConnection();

        final Command<Void> commitCommand = new Command<Void>() {
            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {

                try {
                    if (currentVersionState != null) {
                        SeObjectId parentStateId = initialStateId;
                        // Change the version's state pointer to the last edit state.
                        defaultVersion.changeState(currentVersionState.getId());
                        // Trim the state tree.
                        currentVersionState.trimTree(parentStateId, currentVersionState.getId());
                    }
                } catch (SeException se) {
                    LOGGER.log(Level.WARNING, se.getMessage(), se);
                    close();
                    throw se;
                }
                return null;
            }
        };

        try {
            session.issue(commitCommand);
            fireChanges(true);
            versionHandler.commitEditState();
        } catch (IOException e) {
            versionHandler.rollbackEditState();
            throw e;
        }
    }

    /**
     * 
     */
    public void rollback() throws IOException {
        failIfClosed();
        try {
            versionHandler.rollbackEditState();
            // fire changes in the calling thread
            fireChanges(false);
        } catch (IOException se) {
            // release resources
            close();
            LOGGER.log(Level.WARNING, se.getMessage(), se);
            throw se;
        }
    }

    /**
     * Fires the per typename changes registered through {@link #addChange(String)} and clears the
     * changes cache.
     */
    private void fireChanges(final boolean commit) {
        for (String typeName : typesChanged) {
            listenerManager.fireChanged(typeName, transaction, commit);
        }
        typesChanged.clear();
    }

    /**
     * 
     */
    public void addAuthorization(String authId) {
        // intentionally blank
    }

    /**
     * @see Transaction.State#setTransaction(Transaction)
     * @param transaction
     *            transaction information, <code>null</code> signals this state lifecycle end.
     * @throws IllegalStateException
     *             if close() is called while a transaction is in progress
     */
    public void setTransaction(final Transaction transaction) {
        if (Transaction.AUTO_COMMIT.equals(transaction)) {
            throw new IllegalArgumentException("Cannot use Transaction.AUTO_COMMIT here");
        }
        if (transaction == null) {
            // this is a call to free resources (ugly, but that's what the API
            // says)
            close();
        } else if (this.transaction != null) {
            // assert this assumption
            throw new IllegalStateException(
                    "Once a transaction is set, it is "
                            + "illegal to call Transaction.State.setTransaction with anything other than null: "
                            + transaction);
        }

        this.transaction = transaction;
    }

    /**
     * If this state has been closed throws an unchecked exception as its clearly a broken workflow.
     * 
     * @throws IllegalStateException
     *             if the transaction state has been closed.
     */
    private void failIfClosed() throws IllegalStateException {
        if (dataStore == null) {
            throw new IllegalStateException("This transaction state has already been closed");
        }
    }

    /**
     * Releases resources and invalidates this state (signaled by setting the connection to null)
     */
    private void close() {
        if (dataStore == null) {
            return;
        }
        dataStore = null;
    }

    /**
     * Used only within the package to provide access to a single connection on which this
     * transaction is being conducted.
     * 
     * @return connection
     * @throws IOException
     */
    ISession getConnection() throws IOException {
        failIfClosed();
        // the pool is keeping track of connection according to transaction for us
        return dataStore.getSession(transaction);
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
