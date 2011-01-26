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
package org.geotools.data.jdbc.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataAccessFactory.Param;

/**
 * A datasource factory using DBCP connection pool
 * 
 * @author Andrea Aime - TOPP
 * 
 *
 * @source $URL$
 */
public class DBCPDataSourceFactory extends AbstractDataSourceFactorySpi {

    public static final Param DSTYPE = new Param("dstype", String.class,
            "Must be DBCP", false);
    
    public static final Param USERNAME = new Param("username", String.class,
            "User name to login as", false);

    public static final Param PASSWORD = new Param("password", String.class,
            "Password used to login", false);

    public static final Param JDBC_URL = new Param("jdbcUrl", String.class,
            "The JDBC url (check the JDCB driver docs to find out its format)", true);

    public static final Param DRIVERCLASS = new Param("driverClassName", String.class,
            "The JDBC driver class name (check the JDCB driver docs to find out its name)", true);

    public static final Param MAXACTIVE = new Param("maxActive", Integer.class,
            "The maximum number of active connections in the pool", true);

    public static final Param MAXIDLE = new Param("maxIdle", Integer.class,
            "The maximum number of idle connections in the pool", true);

    private static final Param[] PARAMS = new Param[] { DSTYPE, DRIVERCLASS, JDBC_URL, USERNAME, PASSWORD,
            MAXACTIVE, MAXIDLE };

    public DataSource createDataSource(Map params) throws IOException {
        return createNewDataSource(params);
    }
    
    public boolean canProcess(Map params) {
        return super.canProcess(params) && "DBCP".equals(params.get("dstype"));
    }

    public DataSource createNewDataSource(Map params) throws IOException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName((String) DRIVERCLASS.lookUp(params));
        dataSource.setUrl((String) JDBC_URL.lookUp(params));
        dataSource.setUsername((String) USERNAME.lookUp(params));
        dataSource.setPassword((String) PASSWORD.lookUp(params));
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        dataSource.setMaxActive(((Integer) MAXACTIVE.lookUp(params)).intValue());
        dataSource.setMaxIdle(((Integer) MAXIDLE.lookUp(params)).intValue());

        // check the data source is properly setup by trying to gather a connection out of it
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataSourceException("Connection pool improperly set up: " + e.getMessage(), e);
        } finally {
            // close the connection at once
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                }
        }

        return dataSource;
    }

    public String getDescription() {
        return "A BDCP connection pool.";
    }

    public Param[] getParametersInfo() {
        return PARAMS;
    }

    public boolean isAvailable() {
        try {
            new BasicDataSource();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
