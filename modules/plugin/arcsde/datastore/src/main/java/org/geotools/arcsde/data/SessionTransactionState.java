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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.SessionWrapper;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;

/**
 * Store the transaction state needed for a <code>Session</code> instances.
 * <p>
 * This transaction state is used to hold the SeConnection needed for a Session.
 * </p>
 * 
 * @author Jake Fear
 * @author Gabriel Roldan
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/ArcTransactionState.java $
 * @version $Id$
 */
final class SessionTransactionState implements Transaction.State {
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(SessionTransactionState.class.getPackage().getName());

    /**
     * The session being managed, it will be held open until commit(), rollback() or close() is
     * called.
     */
    private TransactionSession session;

    /**
     * The transaction that is holding on to this Transaction.State
     */
    private Transaction transaction;

    /**
     * Creates a new ArcTransactionState object.
     * 
     * @param listenerManager
     * @param pool
     *            connection pool where to grab a connection and hold it while there's a transaction
     *            open (signaled by any use of {@link #getConnection()}
     */
    private SessionTransactionState(final ISession session) {
        if (!session.isTransactionActive()) {
            throw new IllegalArgumentException("session shall be in transactional mode");
        }
        this.session = new TransactionSession(session);
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
        final ISession session = this.session;

        final Command<Void> commitCommand = new Command<Void>() {
            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {

                try {
                    session.commitTransaction();
                    session.startTransaction();
                } catch (IOException se) {
                    LOGGER.log(Level.WARNING, se.getMessage(), se);
                    throw se;
                }
                return null;
            }
        };

        try {
            session.issue(commitCommand);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 
     */
    public void rollback() throws IOException {
        failIfClosed();
        final ISession session = this.session;
        try {
            session.issue(new Command<Void>() {
                @Override
                public Void execute(ISession session, SeConnection connection) throws SeException,
                        IOException {
                    session.rollbackTransaction();
                    // and keep editing
                    session.startTransaction();
                    return null;
                }
            });
        } catch (IOException se) {
            close();
            LOGGER.log(Level.WARNING, se.getMessage(), se);
            throw se;
        }
    }

    /**
     * @see State#addAuthorization(String)
     */
    public void addAuthorization(String authId) {
        // intentionally blank we are not making use of ArcSDE locking
    }

    /**
     * Transaction start/end.
     * <p>
     * If the provided transaction is non null we are being added to the Transaction. If the
     * provided transaction is null we are being shutdown.
     * </p>
     * 
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
            // this is a call to free resources (ugly, but that's what the API says)
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
        if (session == null) {
            throw new IllegalStateException("This transaction state has already been closed");
        }
    }

    /**
     * Releases resources and invalidates this state (signaled by setting the connection to null)
     */
    private void close() {
        if (session == null) {
            return;
        }
        // can't even try to use this state in any way from now on
        // may throw ISE if transaction is still in progress
        try {
            // release current transaction before returning the
            // connection to the pool
            try {
                session.rollbackTransaction();
                // connection.setConcurrency(SeConnection.SE_UNPROTECTED_POLICY);
            } catch (IOException e) {
                // TODO: this shouldn't happen, but if it does
                // we should somehow invalidate the connection?
                LOGGER.log(Level.SEVERE, "Unexpected exception at close(): " + e.getMessage(), e);
            }
            // now its safe to return it to the pool
            session.dispose();
        } catch (IllegalStateException workflowError) {
            // fail fast but put the connection in a healthy state first
            try {
                session.rollbackTransaction();
            } catch (IOException e) {
                // well, it's totally messed up, just log though
                LOGGER.log(Level.SEVERE, "rolling back connection " + session, e);
                session.dispose();
            }
            throw workflowError;
        } finally {
            session = null;
        }
    }

    /**
     * Used only within the package to provide access to a single connection on which this
     * transaction is being conducted.
     * 
     * @return the session tied to ths state's transaction
     */
    ISession getConnection() {
        failIfClosed();
        return session;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * Grab the SessionTransactionState (when not using AUTO_COMMIT).
     * <p>
     * As of GeoTools 2.5 we store the TransactionState using the connection pool as a key.
     * </p>
     * 
     * @return the SessionTransactionState stored in the transaction with
     *         <code>connectionPool</code> as key.
     */
    public static SessionTransactionState getState(final Transaction transaction,
            final ISessionPool pool) throws IOException {
        SessionTransactionState state;

        if (transaction == Transaction.AUTO_COMMIT) {
            LOGGER.log(Level.SEVERE,
                    "Should not request ArcTransactionState when using AUTO_COMMITback connection");
            return null;
        }

        synchronized (SessionTransactionState.class) {
            state = (SessionTransactionState) transaction.getState(pool);
            if (state == null) {
                // start a transaction
                ISession session;
                try {
                    session = pool.getSession(true);
                } catch (UnavailableConnectionException e) {
                    throw new RuntimeException(
                            "Can't create a transaction state, connection pool exhausted", e);
                }
                try {
                    session.startTransaction();
                } catch (IOException e) {
                    try {
                        session.rollbackTransaction();
                    } finally {
                        session.dispose();
                    }
                    throw new DataSourceException("Exception initiating transaction on " + session,
                            e);
                }
                state = new SessionTransactionState(session);
                transaction.putState(pool, state);
            }
        }
        return state;
    }

    /**
     * A session wrapper that does not disposes if a transaction is active.
     * <p>
     * This wrapper provides for client code to follow de acquire/use/dispose workflow without
     * worrying if it should or should not actually dispose the session depending on a transaction
     * being in progress or not.
     * </p>
     * 
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.5.x
     * @source $URL:
     *         http://svn.geotools.org/trunk/modules/plugin/arcsde/datastore/src/main/java/org/
     *         geotools/arcsde/pool/SessionTransactionState.java $
     */
    private static final class TransactionSession extends SessionWrapper {

        public TransactionSession(final ISession session) {
            super(session);
        }

        /**
         * Does not returns the session to the pool while a transaction is active.
         */
        @Override
        public void dispose() throws IllegalStateException {
            if (isTransactionActive()) {
                LOGGER.finer("Ignoring Session.close, transaction is active...");
            } else {
                wrapped.dispose();
            }
        }
    }

}
