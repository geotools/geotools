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

import javax.sql.PooledConnection;


/**
 * Provides a container for a PooledConnection so we can store extra information such as when it
 * was last used and whether it is currently in use.
 * 
 * <p>
 * This class should not be subclassed.
 * </p>
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author $Author: cholmesny $
 * @source $URL$
 * @version $Id$ Last Modified: $Date: 2003/09/22 18:54:39 $
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
final class ManagedPooledConnection {
    /** The timestamp the last use of the connection */
    long lastUsed;
    /** True if the connection is in use */
    boolean inUse = false;
    /** The actual connection we manage */
    PooledConnection pooledConn;

    /**
     * Creates a ManagedPooledConnection for a Pooled Connection.
     *
     * @param pooledConn The PooledConnection to manage.
     */
    ManagedPooledConnection(PooledConnection pooledConn) {
        this.pooledConn = pooledConn;
    }

    /**
     * Check whether this PooledConnection is still valid.
     * 
     * <p>
     * A Pooled Connection is valid if it is either in use or it is still returning valid
     * Connections.  This method may trigger ConnectionEvents, so if you dont want to receive an
     * event for this method you should remove any ConnectionEventListeners from the pooledConn
     * prior to calling.
     * </p>
     *
     * @return True if the connection is in use or is still returning valid logical Connections.
     */
    boolean isValid() {
        if (inUse) {
            return true;
        } else {
            try {
                Connection conn = pooledConn.getConnection();

                conn.close();

                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }
}
