package org.geotools.arcsde.session;

import java.io.IOException;

/**
 * Maintains <code>SeConnection</code>'s for a single set of connection properties (for instance: by
 * server, port, user and password) in a pool to recycle used connections.
 * <p>
 * The expected optional parameters that you can set up in the argument Map for createDataStore are:
 * <ul>
 * <li>pool.minConnections Integer, tells the minimum number of open connections the pool will
 * maintain opened</li>
 * <li>pool.maxConnections Integer, tells the maximum number of open connections the pool will
 * create and maintain opened</li>
 * <li>pool.timeOut Integer, tells how many milliseconds a calling thread is guaranteed to wait
 * before getConnection() throws an UnavailableArcSDEConnectionException</li>
 * </ul>
 * </p>
 * 
 * @author Gabriel Roldan
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/common/src/main/java/org/geotools
 *         /arcsde/session/ISessionPool.java $
 * @version $Id$
 */
public interface ISessionPool {

    /**
     * returns the number of actual connections held by this connection pool. In other words, the
     * sum of used and available connections, regardless
     * 
     */
    int getPoolSize();

    /**
     * closes all connections in this pool. The first call closes all SeConnections, further calls
     * have no effect.
     */
    void close();

    /**
     * Returns whether this pool is closed
     * 
     * @return
     */
    boolean isClosed();

    /**
     * Returns the number of idle connections
     */
    int getAvailableCount();

    /**
     * Number of active sessions.
     * 
     * @return Number of active session; used to monitor the live pool.
     */
    int getInUseCount();

    /**
     * Grab a session from the pool, this session is the responsibility of the calling code and must
     * be closed after use.
     * 
     * @param transactional
     *            whether the session is intended to be used on a transaction, so the pool may
     *            choose to reuse or not a connection.
     * @return A Session, when close() is called it will be recycled into the pool
     * @throws IOException
     *             If we could not get a connection
     * @throws UnavailableConnectionException
     *             If we are out of connections
     * @throws IllegalStateException
     *             If pool has been closed.
     */
    ISession getSession(final boolean transactional) throws IOException,
            UnavailableConnectionException;

    /**
     * Shortcut for {@code getSession(true)}
     * 
     * @see #getSession(boolean)
     */
    ISession getSession() throws IOException, UnavailableConnectionException;

    ArcSDEConnectionConfig getConfig();

}
