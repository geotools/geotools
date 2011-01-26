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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.logging.Loggers;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeDBMSInfo;
import com.esri.sde.sdk.client.SeDelete;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeFilter;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeRasterColumn;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRelease;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeState;
import com.esri.sde.sdk.client.SeStreamOp;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.client.SeUpdate;
import com.esri.sde.sdk.geom.GeometryFactory;

/**
 * Default implementation of an {@link ISession}
 * <p>
 * As for the ESRI ArcSDE Java API v9.3.0, the {@link SeQuery#prepareQuery} and
 * {@link SeQuery#prepareQueryInfo} methods lead to a memory leak, with {@code SgCoordRef} and
 * {@link SeCoordinateReference} instances somehow tied to the {@link SeConnection}. To avoid Heap
 * Memory starvation, this {@link Session} will auto-close upon a fixed number of calls to those
 * {@code SeQuery} methods, so that the memory can be reclaimed by the garbage collector before it
 * becomes a real problem. When that happens, this Session will be marked closed and discarded from
 * the {@link SessionPool}, leaving room in the pool to create a new Session as needed.
 * </p>
 * <p>
 * Both the {@link #createAndExecuteQuery} and {@link #prepareQuery} methods will increment the
 * auto-close counter.
 * <p>
 * The default value for the auto-close threshold is {@code 500}. A different value can be specified
 * through the {@code "org.geotools.arcsde.session.AutoCloseThreshold"} System property. For
 * example, by running your application like
 * {@code java -Dorg.geotools.arcsde.session.AutoCloseThreshold=100 -cp... MyApp}
 * </p>
 * 
 * @author Gabriel Roldan
 * @author Jody Garnett
 * @version $Id$
 * @since 2.3.x
 */
class Session implements ISession {

    private static final Logger LOGGER = Loggers.getLogger("org.geotools.arcsde.session");

    /**
     * Threshold to be reached by {@link #autoCloseCounter} to automatically recycle (close) the
     * Session and its {@link SeConnection}
     */
    private static final int AUTO_CLOSE_COUNTER_THRESHOLD;
    static {
        Integer systemPropValue = Integer
                .getInteger("org.geotools.arcsde.session.AutoCloseThreshold");
        AUTO_CLOSE_COUNTER_THRESHOLD = systemPropValue == null ? 500 : systemPropValue.intValue();
        LOGGER.info("Session auto-close threshold set to " + AUTO_CLOSE_COUNTER_THRESHOLD);
    }

    /**
     * Counter incremented every time an operation that degrades the performance of the running
     * application is executed, in order to close the {@link SeConnection} when it reaches
     * {@link #AUTO_CLOSE_COUNTER_THRESHOLD}. See class' JavaDocs for more details
     * 
     * @see <a href="http://jira.codehaus.org/browse/GEOT-3227">GEOT-3227</a>
     * @see #prepareQuery(SeQueryInfo, SeFilter[], ArcSdeVersionHandler)
     */
    private int autoCloseCounter;

    /**
     * How many seconds must have elapsed since the last connection round trip to the server for
     * {@link #testServer()} to actually check the connection's validity
     */
    protected static final long TEST_SERVER_ROUNDTRIP_INTERVAL_SECONDS = 5;

    /** Actual SeConnection being protected */
    private final SeConnection connection;

    /**
     * SessionPool used to manage open connections (shared).
     */
    private final SessionPool pool;

    private final ArcSDEConnectionConfig config;

    /**
     * Used to assign unique ids to each new session
     */
    private static final AtomicInteger sessionCounter = new AtomicInteger();

    /**
     * Global unique id for this session
     */
    private final int sessionId;

    private boolean transactionInProgress;

    private boolean isPassivated;

    private Map<String, SeTable> cachedTables = new WeakHashMap<String, SeTable>();

    private Map<String, SeLayer> cachedLayers = new WeakHashMap<String, SeLayer>();

    /**
     * Keeps track of the number of references to this session (ie, how many times it has been
     * {@link #markActive() activated} so it's only actually {@link #dispose() disposed} when the
     * reference count gets down to zero.
     */
    private final AtomicInteger referenceCounter = new AtomicInteger();

    /**
     * Provides safe access to an SeConnection.
     * 
     * @param pool
     *            SessionPool used to manage SeConnection
     * @param config
     *            Used to set up a SeConnection
     * @throws SeException
     *             If we cannot connect
     */
    Session(final SessionPool pool, final ArcSDEConnectionConfig config) throws IOException {
        this.sessionId = sessionCounter.incrementAndGet();
        this.config = config;
        this.pool = pool;

        final CreateSeConnectionCommand connectionCommand;
        connectionCommand = new CreateSeConnectionCommand(config, sessionId);
        try {
            this.connection = issue(connectionCommand);
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException shouldntHappen) {
            throw shouldntHappen;
        }
    }

    /**
     * @see ISession#issue(org.geotools.arcsde.session.Command)
     */
    public synchronized <T> T issue(final Command<T> command) throws IOException {
        try {
            if (connection == null) {
                return command.execute(this, null);
            } else {
                return command.execute(this, connection);
            }
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
    }

    /**
     * @see ISession#testServer()
     */
    public final void testServer() throws IOException {
        /*
         * This method is called often (every time a session is to be returned from the pool) to
         * check if it's still valid. We can call getTimeSinceLastRT safely since it does not
         * require a server roundtrip and hence there's no risk of violating thread safety. So we do
         * it before issuing the command to avoid the perf penalty imposed by running the command if
         * not needed.
         */
        final long secondsSinceLastServerRoundTrip = this.connection.getTimeSinceLastRT();

        if (TEST_SERVER_ROUNDTRIP_INTERVAL_SECONDS < secondsSinceLastServerRoundTrip) {
            issue(Commands.TEST_SERVER);
        }
    }

    /**
     * @see ISession#isClosed()
     */
    public final boolean isClosed() {
        return this.connection.isClosed();
    }

    /**
     * Marks the connection as being active (i.e. its out of the pool and ready to be used).
     * <p>
     * Shall be called just before being returned from the connection pool
     * </p>
     * 
     * @see #isPassivated
     * @see #checkActive()
     */
    void markActive() {
        referenceCounter.incrementAndGet();
        this.isPassivated = false;
    }

    /**
     * Marks the connection as being inactive (i.e. laying on the connection pool)
     * <p>
     * Shall be callled just before sending it back to the pool
     * </p>
     * 
     * @see #markActive()
     * @see #isPassivated
     * @see #checkActive()
     */
    void markInactive() {
        if (referenceCounter.get() != 0) {
            throw new IllegalStateException("referenceCount = " + referenceCounter);
        }
        this.isPassivated = true;
    }

    /**
     * @see ISession#isPassivated()
     */
    public boolean isDisposed() {
        return isPassivated;
    }

    /**
     * Sanity check method called before every public operation delegates to the superclass.
     * 
     * @throws IllegalStateException
     *             if {@link #isDisposed() isPassivated() == true} as this is a serious workflow
     *             breackage.
     */
    private void checkActive() {
        if (isDisposed()) {
            throw new IllegalStateException("Unrecoverable error: " + toString()
                    + " is passivated, shall not be used!");
        }
    }

    /**
     * @see ISession#getLayer(java.lang.String)
     */
    public SeLayer getLayer(final String layerName) throws IOException {
        checkActive();
        if (!cachedLayers.containsKey(layerName)) {
            synchronized (cachedLayers) {
                if (!cachedLayers.containsKey(layerName)) {
                    SeTable table = getTable(layerName);
                    SeLayer layer = issue(new Commands.GetLayerCommand(table));
                    if (layer != null) {
                        cachedLayers.put(layerName, layer);
                    }
                }
            }
        }

        SeLayer seLayer = cachedLayers.get(layerName);
        if (seLayer == null) {
            throw new NoSuchElementException("Layer '" + layerName + "' not found");
        }
        return seLayer;

    }

    /**
     * @see ISession#getRasterColumn(java.lang.String)
     */
    public synchronized SeRasterColumn getRasterColumn(final String rasterName) throws IOException {
        throw new UnsupportedOperationException("Waiting for a proper implementation");
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getRasterColumns()
     */
    public List<String> getRasterColumns() throws IOException {
        checkActive();
        List<String> rasterNames = issue(Commands.GET_RASTER_COLUMN_NAMES);
        return rasterNames;
    }

    /**
     * @see ISession#getTable(java.lang.String)
     */
    public SeTable getTable(final String tableName) throws IOException {
        checkActive();
        if (!cachedTables.containsKey(tableName)) {
            synchronized (cachedTables) {
                if (!cachedTables.containsKey(tableName)) {
                    SeTable table = issue(new Commands.GetTableCommand(tableName));
                    cachedTables.put(tableName, table);
                }
            }
        }

        SeTable seTable = (SeTable) cachedTables.get(tableName);

        return seTable;
    }

    /**
     * @see ISession#startTransaction()
     */
    public void startTransaction() throws IOException {
        checkActive();
        issue(Commands.START_TRANSACTION);
        transactionInProgress = true;
    }

    /**
     * @see ISession#commitTransaction()
     */
    public void commitTransaction() throws IOException {
        checkActive();
        issue(Commands.COMMIT_TRANSACTION);
        transactionInProgress = false;
    }

    /**
     * @see ISession#isTransactionActive()
     */
    public boolean isTransactionActive() {
        checkActive();
        return transactionInProgress;
    }

    /**
     * @see ISession#rollbackTransaction()
     */
    public void rollbackTransaction() throws IOException {
        checkActive();
        try {
            issue(Commands.ROLLBACK_TRANSACTION);
        } finally {
            transactionInProgress = false;
        }
    }

    /**
     * @see ISession#dispose()
     */
    public void dispose() throws IllegalStateException {
        checkActive();
        final int refCount = referenceCounter.decrementAndGet();

        if (refCount > 0) {
            // ignore
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("---------> Ignoring disposal, ref count is still " + refCount
                        + " for " + this);
            }

            // System.err.println("---------> Ignoring disposal, ref count is still " + refCount
            // + " for " + this);
            return;
        }

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("  -> RefCount is " + refCount + ". Disposing " + this);
        }
        if (transactionInProgress) {
            throw new IllegalStateException(
                    "Transaction is in progress, should commit or rollback before closing");
        }
        if (autoCloseCounter >= AUTO_CLOSE_COUNTER_THRESHOLD) {
            LOGGER.warning("Auto-closing " + this
                    + " to avoid memory leak in ESRI Java API (see GEOT-3227)");
            this.destroy();
        }
        try {
            // System.err.println("---------> Disposing " + this + " on thread " +
            // Thread.currentThread().getName());
            this.pool.returnObject(this);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "Session[" + sessionId + "]";
    }

    /**
     * Actually closes the connection, called when the session is discarded from the pool
     */
    void destroy() {
        LOGGER.fine("Destroying connection " + toString());
        try {
            issue(Commands.CLOSE_CONNECTION);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "closing connection " + toString(), e);
        } finally {
            // taskExecutor.shutdown();
        }
    }

    /**
     * @see ISession#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return other == this;
    }

    /**
     * @see ISession#hashCode()
     */
    @Override
    public int hashCode() {
        return 17 ^ this.config.hashCode();
    }

    /**
     * @see ISession#getLayers()
     */
    public List<SeLayer> getLayers() throws IOException {
        return issue(Commands.GET_LAYERS);
    }

    /**
     * @see ISession#getUser()
     */
    public String getUser() throws IOException {
        return issue(Commands.GET_USER);
    }

    /**
     * @see ISession#getRelease()
     */
    public SeRelease getRelease() throws IOException {
        return issue(Commands.GET_RELEASE);
    }

    /**
     * @see ISession#getDatabaseName()
     */
    public String getDatabaseName() throws IOException {
        return issue(Commands.GET_DATABASENAME);
    }

    /**
     * @see ISession#getDBMSInfo()
     */
    public SeDBMSInfo getDBMSInfo() throws IOException {
        return issue(Commands.GET_DBMS_INFO);
    }

    /**
     * @see ISession#createSeRegistration(java.lang.String)
     */
    public SeRegistration createSeRegistration(final String typeName) throws IOException {
        return issue(new Commands.CreateSeRegistrationCommand(typeName));
    }

    /**
     * @see ISession#createSeTable(java.lang.String)
     */
    public SeTable createSeTable(final String qualifiedName) throws IOException {
        return issue(new Commands.CreateSeTableCommand(qualifiedName));
    }

    /**
     * @see ISession#createSeInsert()
     */
    public SeInsert createSeInsert() throws IOException {
        return issue(Commands.CREATE_SEINSERT);
    }

    /**
     * @see ISession#createSeUpdate()
     */
    public SeUpdate createSeUpdate() throws IOException {
        return issue(Commands.CREATE_SEUPDATE);
    }

    /**
     * @see ISession#createSeDelete()
     */
    public SeDelete createSeDelete() throws IOException {
        return issue(Commands.CREATE_SEDELETE);
    }

    /**
     * @see ISession#describe(java.lang.String)
     */
    public SeColumnDefinition[] describe(final String tableName) throws IOException {
        final SeTable table = getTable(tableName);
        return describe(table);
    }

    /**
     * @see ISession#describe(com.esri.sde.sdk.client.SeTable)
     */
    public SeColumnDefinition[] describe(final SeTable table) throws IOException {
        return issue(new Commands.DescribeTableCommand(table));
    }

    /**
     * @see ISession#fetch(com.esri.sde.sdk.client.SeQuery)
     */
    public SdeRow fetch(final SeQuery query) throws IOException {
        return fetch(query, new SdeRow((GeometryFactory) null));
    }

    public SdeRow fetch(final SeQuery query, final SdeRow currentRow) throws IOException {
        return issue(new Commands.FetchRowCommand(query, currentRow));
    }

    /**
     * @see ISession#close(com.esri.sde.sdk.client.SeState)
     */
    public void close(final SeState state) throws IOException {
        issue(new Commands.CloseStateCommand(state));
    }

    /**
     * @see ISession#close(com.esri.sde.sdk.client.SeStreamOp)
     */
    public void close(final SeStreamOp stream) throws IOException {
        issue(new Commands.CloseStreamCommand(stream));
    }

    /**
     * @see ISession#createState(com.esri.sde.sdk.client.SeObjectId)
     */
    public SeState createState(final SeObjectId stateId) throws IOException {
        return issue(new Commands.CreateSeStateCommand(stateId));
    }

    /**
     * @see ISession#createAndExecuteQuery(java.lang.String[],
     *      com.esri.sde.sdk.client.SeSqlConstruct)
     */
    public SeQuery createAndExecuteQuery(final String[] propertyNames, final SeSqlConstruct sql)
            throws IOException {
        this.autoCloseCounter++;
        return issue(new Commands.CreateAndExecuteQueryCommand(propertyNames, sql));
    }

    /**
     * Creates either a direct child state of parentStateId, or a sibling being an exact copy of
     * parentStatId if either the state can't be closed because its in use or parentStateId does not
     * belong to the current user.
     */
    public SeState createChildState(final long parentStateId) throws IOException {
        return issue(new Commands.CreateVersionStateCommand(parentStateId));
    }

    private static final class CreateSeConnectionCommand extends Command<SeConnection> {
        private final ArcSDEConnectionConfig config;

        private final int sessionId;

        /**
         * 
         * @param config
         * @param sessionId
         *            the session id the connection is to be created for. For exception reporting
         *            purposes only
         */
        private CreateSeConnectionCommand(final ArcSDEConnectionConfig config, final int sessionId) {
            this.config = config;
            this.sessionId = sessionId;
        }

        @Override
        public SeConnection execute(final ISession session, final SeConnection connection)
                throws SeException, IOException {
            final String serverName = config.getServerName();
            final String portNumber = config.getPortNumber();
            final String databaseName = config.getDatabaseName();
            final String userName = config.getUserName();
            final String userPassword = config.getPassword();

            NegativeArraySizeException cause = null;
            SeConnection conn = null;
            try {
                for (int i = 0; i < 3; i++) {
                    try {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine("Creating connection for session #" + sessionId + "(try "
                                    + (i + 1) + " of 3)");
                        }
                        conn = new SeConnection(serverName, portNumber, databaseName, userName,
                                userPassword);
                        break;
                    } catch (NegativeArraySizeException nase) {
                        LOGGER.warning("Strange failed ArcSDE connection error.  "
                                + "Trying again (try " + (i + 1) + " of 3). SessionId: "
                                + sessionId);
                        cause = nase;
                    }
                }
            } catch (SeException e) {
                throw new ArcSdeException("Can't create connection to " + serverName
                        + " for Session #" + sessionId, e);
            } catch (RuntimeException e) {
                throw (IOException) new IOException("Can't create connection to " + serverName
                        + " for Session #" + sessionId).initCause(e);
            }

            if (cause != null) {
                throw (IOException) new IOException("Couldn't create ArcSDE connection to "
                        + serverName + " for Session #" + sessionId
                        + " because of strange SDE internal exception. "
                        + " Tried 3 times, giving up.").initCause(cause);
            }
            return conn;
        }
    }

    /**
     * @see org.geotools.arcsde.session.ISession#prepareQuery(com.esri.sde.sdk.client.SeQueryInfo,
     *      com.esri.sde.sdk.client.SeFilter[], org.geotools.arcsde.versioning.ArcSdeVersionHandler)
     */
    public SeQuery prepareQuery(final SeQueryInfo qInfo, final SeFilter[] spatialConstraints,
            final ArcSdeVersionHandler version) throws IOException {
        this.autoCloseCounter++;
        return issue(new Commands.PrepareQueryCommand(qInfo, spatialConstraints, version));
    }
}
