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
package org.geotools.arcsde.session;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeDBMSInfo;
import com.esri.sde.sdk.client.SeDelete;
import com.esri.sde.sdk.client.SeFilter;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeRasterColumn;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRelease;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeState;
import com.esri.sde.sdk.client.SeStreamOp;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.client.SeUpdate;
import java.io.IOException;
import java.util.List;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;

/**
 * Provides thread safe access to an SeConnection.
 *
 * <p>An {@code ISession} is a decorator around an SeConnection, ensuring the SeConnection is not
 * hit by two concurrent threads while at the same time participating in a {@link ISessionPool
 * connection pool} where more than one thread can reclaim usage of the {@link SeConnection}.
 *
 * <p>Each piece of code that needs access to an {@link SeConnection}, either directly or indirectly
 * (for example, by accessing an {@link SeStreamOp} like in {@code SeQuery.fetch()} or {@code new
 * SeLayer(connection)}, needs to do so inside the body of a {@link Command}, and issue the command
 * through {@link ISession#issue(Command)}.
 *
 * <p>
 *
 * @author Gabriel Roldan
 * @author Jody Garnett
 * @version $Id$
 * @since 2.5.x
 */
public interface ISession {

    /**
     * Executes the given command and returns its result.
     *
     * @param command the command to execute
     * @throws IOException if an exception occurs handling any ArcSDE resource while executing the
     *     command
     */
    public abstract <T> T issue(final Command<T> command) throws IOException;

    /**
     * Performs a session sanity check to avoid stale connections to be returned from the pool.
     *
     * @see {@link SeConnection#testServer(long)}
     */
    public void testServer() throws IOException;

    public abstract boolean isClosed();

    /**
     * Returns whether this connection is on the connection pool domain or not.
     *
     * @return <code>true</code> if this connection has beed returned to the pool and thus cannot be
     *     used, <code>false</code> if its safe to keep using it.
     */
    public abstract boolean isDisposed();

    public abstract SeLayer getLayer(final String layerName) throws IOException;

    public abstract SeRasterColumn getRasterColumn(final String rasterName) throws IOException;

    public abstract List<String> getRasterColumns() throws IOException;

    public abstract SeTable getTable(final String tableName) throws IOException;

    /**
     * Starts a transaction over the connection held by this Session
     *
     * <p>If this method succeeds, {@link #isTransactionActive()} will return true afterwards
     *
     * @see {@link #issueStartTransaction(Session)}
     */
    public abstract void startTransaction() throws IOException;

    /**
     * Commits the current transaction.
     *
     * <p>This method shall only be called from inside a command
     */
    public abstract void commitTransaction() throws IOException;

    /**
     * Returns whether a transaction is in progress over this connection
     *
     * <p>As for any other public method, this one can't be called if {@link #isDisposed()} is true.
     */
    public abstract boolean isTransactionActive();

    /**
     * Rolls back the current transaction
     *
     * <p>When this method returns it is guaranteed that {@link #isTransactionActive()} will return
     * false, regardless of the success of the rollback operation.
     */
    public abstract void rollbackTransaction() throws IOException;

    /**
     * Return to the pool (may not close the internal connection, depends on pool settings).
     *
     * @throws IllegalStateException if dispose() is called while a transaction is in progress
     */
    public abstract void dispose() throws IllegalStateException;

    /** Compares for reference equality */
    public abstract boolean equals(Object other);

    public abstract int hashCode();

    /**
     * Returns the live list of layers, not the cached ones, so it may pick up the differences in
     * the database.
     */
    public abstract List<SeLayer> getLayers() throws IOException;

    public abstract String getUser() throws IOException;

    public abstract SeRelease getRelease() throws IOException;

    public abstract String getDatabaseName() throws IOException;

    public abstract SeDBMSInfo getDBMSInfo() throws IOException;

    public abstract SeRegistration createSeRegistration(final String typeName) throws IOException;

    /**
     * Creates an SeTable named
     * <code>qualifiedName<code>; the layer does not need to exist on the server.
     *
     */
    public abstract SeTable createSeTable(final String qualifiedName) throws IOException;

    public abstract SeInsert createSeInsert() throws IOException;

    public abstract SeUpdate createSeUpdate() throws IOException;

    public abstract SeDelete createSeDelete() throws IOException;

    public abstract SeColumnDefinition[] describe(final String tableName) throws IOException;

    public abstract SeColumnDefinition[] describe(final SeTable table) throws IOException;

    /**
     * Issues a command that fetches a row from an already executed SeQuery and returns the {@link
     * SdeRow} object with its contents.
     *
     * <p>The point in returning an {@link SdeRow} instead of a plain {@link SeRow} is that the
     * former prefetches the row values and this can be freely used outside a {@link Command}.
     * Otherwise the SeRow should only be used inside a command as accessing its values implies
     * using the connection.
     */
    public abstract SdeRow fetch(final SeQuery query) throws IOException;

    public abstract SdeRow fetch(SeQuery seQuery, SdeRow currentRow) throws IOException;

    public abstract void close(final SeState state) throws IOException;

    public abstract void close(final SeStreamOp stream) throws IOException;

    public abstract SeState createState(final SeObjectId stateId) throws IOException;

    public abstract SeQuery createAndExecuteQuery(
            final String[] propertyNames, final SeSqlConstruct sql) throws IOException;

    public SeState createChildState(long parentStateId) throws IOException;

    public abstract SeQuery prepareQuery(
            final SeQueryInfo qInfo,
            final SeFilter[] spatialConstraints,
            final ArcSdeVersionHandler version)
            throws IOException;
}
