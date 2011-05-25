/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.DataSourceException;

/**
 * Utility methods to build a default connection pool
 * 
 * @author Andrea Aime - TOPP
 * 
 *
 *
 * @source $URL$
 */
public class DataSourceUtil {
    private DataSourceUtil() {
        // singleton
    }
    
    /**
     * Builds up a default DBCP DataSource that easy to use connection factories
     * can use to setup a connection pool.
     * 
     * @param url
     *            the jdbc url
     * @param driverName
     *            the jdbc driver full qualified class name
     * @param username
     * @param password
     * @param validationQuery
     *            the validation query to be used for connection liveliness on
     *            borrow, or null, if no check is to be performed
     * @return
     * @throws DataSourceException
     */
    public static ManageableDataSource buildDefaultDataSource(String url,
            String driverName, String username, String password,
            String validationQuery) throws DataSourceException {
        return buildDefaultDataSource(url, driverName, username, password, 10, 1, validationQuery, false, 0);
    }

    /**
     * Builds up a default DBCP DataSource that easy to use connection factories
     * can use to setup a connection pool.
     * 
     * @param url
     *            the jdbc url
     * @param driverName
     *            the jdbc driver full qualified class name
     * @param username
     * @param password
     * @param maxActive maximum number of concurrent connections in the pool
     * @param minIdle minimum number of concurrent connections in the pool
     * @param validationQuery
     *            the validation query to be used for connection liveliness on
     *            borrow, or null, if no check is to be performed
     * @param cachePreparedStatements
     *            wheter to cache prepared statements or not
     * @return
     * @throws DataSourceException
     */
    public static ManageableDataSource buildDefaultDataSource(String url,
            String driverName, String username, String password,
            int maxActive, int minIdle, String validationQuery, boolean cachePreparedStatements, int removeAbandonedTimeout)
            throws DataSourceException {
        // basics
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setAccessToUnderlyingConnectionAllowed(true);

        // pool size
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        
        // pool eviction settings
        dataSource.setMinEvictableIdleTimeMillis(1000 * 20);
        dataSource.setTimeBetweenEvictionRunsMillis(1000 * 10);

        // connection validation
        if (validationQuery != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }

        // prepared statement cache
        if (cachePreparedStatements) {
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxOpenPreparedStatements(10);
        }
        
        // remove abandoned connections (I know it's deprecated, but we do want
        // something shaving off lost connections. Let's give them 5 minutes of 
        // continuous usage
        if(removeAbandonedTimeout > 0) {
            dataSource.setRemoveAbandoned(true);
            dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
            dataSource.setLogAbandoned(true);
        }

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (Exception e) {
            throw new DataSourceException("Connection test failed ",  e);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                }
        }

        return new DBCPDataSource(dataSource);
    }
}
