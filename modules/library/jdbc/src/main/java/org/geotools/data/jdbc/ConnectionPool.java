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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.DataSourceUtil;


/**
 * Provides a ConnectionPool that can be used by multiple data sources to get connections to a
 * single database.
 * 
 * <p>
 * This class should not be subclassed.
 * </p>
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author Chris Holmes
 *
 * @source $URL$
 * @version $Id$
 * @deprecated Use {@link DataSource}, {@link DataSourceUtil} and {@link DataSourceFinder} instead
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public final class ConnectionPool {
    /** A logger */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc");
    /** The default wait time for cleaning the Pool - defaults to 30 secs */
    private static final long DEFAULT_POOL_CLEANER_WAIT = 30000;
    /** A mutex for synchronizing */
    private Object mutex = new Object();
    /** The data source we get the pooled connections from */
    private ConnectionPoolDataSource cpDataSource;
    /** List of available connections */
    private LinkedList availableConnections = new LinkedList();
    /** This of connections that are in use */
    private LinkedList usedConnections = new LinkedList();
    /** Handles ConnectionEvents and manages the lists */
    private ConnectionListManager listManager = new ConnectionListManager();
    /** Cleans the list of dead connections */
    private ConnectionPoolCleaner poolCleaner;
    /** Indicates that this Connection Pool is closed and it should not
     *  return connections on calls to getConnection()
     */
    private boolean closed = false;

    /**
     * Creates a new Connection Pool using a ConnectionPoolDataSource.
     * 
     * <p>
     * This constructor will also spawn a thread for cleaning the connection pool every 30 seconds.
     * </p>
     *
     * @param cpDataSource The Connection Pool Data Source to get PooledConnections from.
     */
    public ConnectionPool(ConnectionPoolDataSource cpDataSource) {
        this.cpDataSource = cpDataSource;
        poolCleaner = new ConnectionPoolCleaner(DEFAULT_POOL_CLEANER_WAIT);

        Thread cleanerThread = new Thread(poolCleaner);
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    /**
     * Gets a Connection from the Connection Pool.
     * 
     * <p>
     * If no available connections exist a new connection will be created and added to the pool.
     * When the returned connection is closed it will be added to the connection pool for other
     * requests to this method.
     * </p>
     *
     * @return A Connection from the ConnectionPool.
     *
     * @throws SQLException If an error occurs getting the connection or if the 
     * connection pool has been closed by a previous call to close().
     */
    public Connection getConnection() throws SQLException {
        if (closed) {
            throw new SQLException("The ConnectionPool has been closed.");
        }
        
        Connection conn = null;

        synchronized (mutex) {
            if (availableConnections.size() > 0) {
                LOGGER.fine("Getting available connection.");

                ManagedPooledConnection mConn = 
                    (ManagedPooledConnection) availableConnections.removeFirst();

                conn = mConn.pooledConn.getConnection();

                mConn.lastUsed = System.currentTimeMillis();
                mConn.inUse = true;
                usedConnections.add(mConn);
            } else {
                LOGGER.fine("No available connections, creating a new one.");

                PooledConnection pConn = cpDataSource.getPooledConnection();

                conn = pConn.getConnection();

                pConn.addConnectionEventListener(listManager);

                ManagedPooledConnection mConn = new ManagedPooledConnection(pConn);

                mConn.inUse = true;
                mConn.lastUsed = System.currentTimeMillis();
                usedConnections.add(mConn);
            }
        }

        return conn;
    }

    /**
     * Helper method to get the ManagedPooledConnection for a given PooledConnection.
     *
     * @param conn The PooledConnection
     *
     * @return The ManagedPooledConnection that contains the PooledConnection.
     */
    private ManagedPooledConnection getInUseManagedPooledConnection(PooledConnection conn) {
        ManagedPooledConnection returnConn = null;

        for (Iterator iter = usedConnections.iterator(); iter.hasNext();) {
            ManagedPooledConnection mConn = (ManagedPooledConnection) iter.next();

            if (mConn.pooledConn == conn) {
                returnConn = mConn;
            }
        }

        return returnConn;
    }

    /** Closes all the PooledConnections in the the ConnectionPool.
     *  The current behaviour is to first close all the used connections,
     *  then close all the available connections.  This method will also set
     *  the state of the ConnectionPool to closed, caused any future calls
     *  to getConnection to throw an SQLException.
     */
    public void close() {
        if (closed)  {
            return;
        }
        synchronized (mutex) {
            int size = usedConnections.size();
            for (int i = 0; i < size; i++) {
                ManagedPooledConnection mPool = 
                    (ManagedPooledConnection) usedConnections.removeFirst();
                mPool.pooledConn.removeConnectionEventListener(listManager);
                try {
                    mPool.pooledConn.close();
                } catch (SQLException e) {
                    LOGGER.warning("Failed to close PooledConnection: " + e);
                }
            }
            
            size = availableConnections.size();
            for (int i = 0; i < size; i++) {
                ManagedPooledConnection mPool = 
                    (ManagedPooledConnection) availableConnections.removeFirst();
                mPool.pooledConn.removeConnectionEventListener(listManager);
                try {
                    mPool.pooledConn.close();
                } catch (SQLException e) {
                    LOGGER.warning("Failed to close PooledConnection: " + e);
                }
            }
            closed = true;
        }
    }

    /** Checks whether the ConnectionPool has been closed.
     * 
     * @return True if the connection pool is closed. If the Pool is closed
     * future calls to getConnection will throw an SQLException.
     */
    public boolean isClosed() {
        return closed;
    }
    
    /**
     * A ConnectionEventListener for managing the list of connections in the pool.
     *
     * @author Sean Geoghegan, Defence Science and Technology Organisation
     * @author Chris Holmes
     * @version $Id$
     */
    private class ConnectionListManager implements ConnectionEventListener {
        /**
         * This is called when a logical connection is closed.  The pooled connection is returned
         * to the list of available connections.
         *
         * @param event The Connection event.
         *
         * @see javax.sql.ConnectionEventListener#connectionClosed(javax.sql.ConnectionEvent)
         */
        public void connectionClosed(ConnectionEvent event) {
            LOGGER.fine("Connection closed - adding to available connections.");

            
            PooledConnection conn = (PooledConnection) event.getSource();
            synchronized (mutex) {
                ManagedPooledConnection mConn = getInUseManagedPooledConnection(conn);
    
                mConn.inUse = false;

                usedConnections.remove(mConn);
                availableConnections.addLast(mConn);
            }
        }

        /**
         * Called when an error occurs on the Connection. This removes the connection from the
         * pool.
         *
         * @param event The ConnectionEvent indicating an error.
         *
         * @see javax.sql.ConnectionEventListener#connectionErrorOccurred(javax.sql.ConnectionEvent)
         */
        public void connectionErrorOccurred(ConnectionEvent event) {
            PooledConnection conn = (PooledConnection) event.getSource();
            synchronized (mutex) {
                ManagedPooledConnection mConn = getInUseManagedPooledConnection(conn);
    
                conn.removeConnectionEventListener(this);
    
                try {
                    conn.close();
                } catch (SQLException e) {
                    // don't need to do anything here, just log it
                    LOGGER.log(Level.WARNING, "Error closing a connection", e);
                }

                usedConnections.remove(mConn);
            }
        }
    }

    /**
     * Runnable class that handles cleaning of invalid connections from the connection pool.  A
     * connection is removed from the pool when its isValid method return false.  The Constructor
     * for the ConnectionPoolCleaner has a parameter that defines how often the pool cleaner will
     * run.
     * 
     * <p>
     * The Pool Cleaner has come to clean ze pool.
     * </p>
     *
     * @author Sean Geoghegan, Defence Science and Technology Organisation
     * @author Chris Holmes
     * @version $Id$
     */
    private class ConnectionPoolCleaner implements Runnable {
        /** Time to wait between cleaning */
        private long waitTime;
        /** Run loop flag */
        private boolean active = true;

        /** Creates a new ConnectionPoolCleaner
         * 
         * @param waitTime The frequency of the cleaning.
         */
        ConnectionPoolCleaner(long waitTime) {
            this.waitTime = waitTime;
        }

        /** Stops the Pool cleaner.
         * 
         */
        void disable() {
            active = false;
        }

        /**
         * Executes the Connection Pool Cleaning.
         */
        public void run() {
            while (active) {
                synchronized (mutex) {
                    for (Iterator iter = availableConnections.iterator(); iter.hasNext();) {
                        ManagedPooledConnection conn = (ManagedPooledConnection) iter.next();

                        conn.pooledConn.removeConnectionEventListener(listManager);

                        if (!conn.isValid()) {
                            LOGGER.fine("Connection invalid, removing from pool");

                            try {
                                conn.pooledConn.close();
                            } catch (SQLException e) {
                                LOGGER.log(Level.WARNING, "Error closing dead connection", e);
                            }

                            iter.remove();
                        } else {
                            // Connection is fine, add the list manager again.
                            conn.pooledConn.addConnectionEventListener(listManager);
                        }
                    }
                }

                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "Interrupted exception when wait in Pool Cleaner", e);
                }
            }
        }
    }
}
