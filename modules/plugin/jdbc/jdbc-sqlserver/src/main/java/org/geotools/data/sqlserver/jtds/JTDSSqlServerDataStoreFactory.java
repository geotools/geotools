/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.sqlserver.jtds;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.Parameter;
import org.geotools.data.sqlserver.SQLServerDataStoreFactory;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;

public class JTDSSqlServerDataStoreFactory extends SQLServerDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "jtds-sqlserver",
                    Collections.singletonMap(Parameter.LEVEL, "program"));
    /* (non-Javadoc)
     * @see org.geotools.data.sqlserver.SQLServerDataStoreFactory#getDescription()
     */
    @Override
    public String getDescription() {
        return "Microsoft SQL Server (JTDS Driver)";
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new JTDSSQLServerDialect(dataStore);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.sqlserver.SQLServerDataStoreFactory#getDriverClassName()
     */
    @Override
    protected String getDriverClassName() {

        return "net.sourceforge.jtds.jdbc.Driver";
    }

    /* (non-Javadoc)
     * @see org.geotools.data.sqlserver.SQLServerDataStoreFactory#getDatabaseID()
     */
    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.sqlserver.SQLServerDataStoreFactory#getJDBCUrl(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        // jdbc:jtds:<server_type>://<server>[:<port>][/<database>][;<property>=<value>[;...]]
        String host = (String) HOST.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        String db = (String) DATABASE.lookUp(params);
        String instance = (String) INSTANCE.lookUp(params);

        String url = "jdbc:jtds:sqlserver://" + host;
        if (port != null) {
            url += ":" + port;
        }

        if (db != null) {
            url += "/" + db;
        }

        if (instance != null) {
            url += ";instance=" + instance;
        }

        Boolean intsec = (Boolean) INTSEC.lookUp(params);
        if (intsec != null && intsec.booleanValue()) {
            url = url + ";integratedSecurity=true";
        }

        return url;
    }
}
