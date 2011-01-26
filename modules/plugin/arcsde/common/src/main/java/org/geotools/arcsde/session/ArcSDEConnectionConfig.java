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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of ArcSDE database connection parameters.
 * 
 * @author Gabriel Roldan
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/common/src/main/java/org/geotools
 *         /arcsde/session/ArcSDEConnectionConfig.java $
 * @version $Id$
 */
public class ArcSDEConnectionConfig {
    /** ArcSDE server parameter name */
    public static final String SERVER_NAME_PARAM_NAME = "server";

    /** ArcSDE server port parameter name */
    public static final String PORT_NUMBER_PARAM_NAME = "port";

    /** ArcSDE databse name parameter name */
    public static final String INSTANCE_NAME_PARAM_NAME = "instance";

    /** ArcSDE database user name parameter name */
    public static final String USER_NAME_PARAM_NAME = "user";

    /** ArcSDE database user password parameter name */
    public static final String PASSWORD_PARAM_NAME = "password";

    public static final String MIN_CONNECTIONS_PARAM_NAME = "pool.minConnections";

    public static final String MAX_CONNECTIONS_PARAM_NAME = "pool.maxConnections";

    public static final String CONNECTION_TIMEOUT_PARAM_NAME = "pool.timeOut";

    /** name or IP of the ArcSDE server to connect to */
    private String serverName;

    /** port number where the ArcSDE instance listens for connections */
    private String portNumber;

    /** name of the ArcSDE database to connect to */
    private String databaseName;

    /** database user name to connect as */
    private String userName;

    /** database user password */
    private String password;

    /** minimum number of connection held in reserve, often 0 */
    private Integer minConnections = null;

    /** maximum number of connections */
    private Integer maxConnections = null;

    /** time to hold onto an idle connection before cleaning it up */
    private Integer connTimeOut = null;

    public ArcSDEConnectionConfig() throws IllegalArgumentException {

    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMinConnections() {
        return minConnections;
    }

    public void setMinConnections(Integer minConnections) {
        this.minConnections = minConnections;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    public Integer getConnTimeOut() {
        return connTimeOut;
    }

    public void setConnTimeOut(Integer connTimeOut) {
        this.connTimeOut = connTimeOut;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[server=").append(serverName);
        sb.append(", port=").append(portNumber);
        sb.append(", database=").append(databaseName);
        sb.append(", user=").append(userName);
        sb.append(", minConnections=").append(minConnections);
        sb.append(", maxConnections=").append(maxConnections);
        sb.append(", timeout=").append(connTimeOut);
        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ArcSDEConnectionConfig)) {
            return false;
        }

        ArcSDEConnectionConfig c = (ArcSDEConnectionConfig) o;
        boolean equals = equals(serverName, c.serverName) && equals(portNumber, c.portNumber)
                && equals(databaseName, c.databaseName) && equals(userName, c.userName)
                && equals(password, c.password) && equals(minConnections, c.minConnections)
                && equals(maxConnections, c.maxConnections) && equals(connTimeOut, c.connTimeOut);

        return equals;
    }

    private static boolean equals(Object o1, Object o2) {
        return o1 == null ? (o2 == null) : o1.equals(o2);
    }

    @Override
    public int hashCode() {
        int prime = 17;
        int hash = prime
                * (hash(serverName) + hash(portNumber) + hash(databaseName) + hash(userName)
                        + hash(password) + hash(minConnections) + hash(maxConnections) + hash(connTimeOut));
        return hash;
    }

    private static int hash(Object o) {
        return o == null ? 1 : String.valueOf(o).hashCode();
    }

    public static ArcSDEConnectionConfig fromMap(final Map<String, Serializable> map) {
        ArcSDEConnectionConfig config = new ArcSDEConnectionConfig();
        config.setDatabaseName(String.valueOf(map.get(INSTANCE_NAME_PARAM_NAME)));
        config.setPassword(String.valueOf(map.get(PASSWORD_PARAM_NAME)));
        config.setPortNumber(String.valueOf(map.get(PORT_NUMBER_PARAM_NAME)));
        config.setServerName(String.valueOf(map.get(SERVER_NAME_PARAM_NAME)));
        config.setUserName(String.valueOf(map.get(USER_NAME_PARAM_NAME)));

        config.setConnTimeOut(Integer.valueOf(String.valueOf(map.get(CONNECTION_TIMEOUT_PARAM_NAME))));
        config.setMaxConnections(Integer.valueOf(String.valueOf(map.get(MAX_CONNECTIONS_PARAM_NAME))));
        config.setMinConnections(Integer.valueOf(String.valueOf(map.get(MIN_CONNECTIONS_PARAM_NAME))));

        return config;
    }

    public Map<String, Serializable> toMap() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(INSTANCE_NAME_PARAM_NAME, getDatabaseName());
        params.put(PASSWORD_PARAM_NAME, getPassword());
        params.put(PORT_NUMBER_PARAM_NAME, getPortNumber());
        params.put(SERVER_NAME_PARAM_NAME, getServerName());
        params.put(USER_NAME_PARAM_NAME, getUserName());
        params.put(CONNECTION_TIMEOUT_PARAM_NAME, getConnTimeOut());
        params.put(MAX_CONNECTIONS_PARAM_NAME, getMaxConnections());
        params.put(MIN_CONNECTIONS_PARAM_NAME, getMinConnections());
        return params;
    }
}
