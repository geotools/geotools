/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * DataStoreFactory for Informix database.
 *
 * @author George Dewar, Land Information New Zealand
 * @author Ines Falcao, Land Information New Zealand
 */
public class InformixDataStoreFactory extends JDBCDataStoreFactory {
    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "informix-sqli",
                    Collections.singletonMap(Parameter.LEVEL, "program"));
    /** Default port number for Informix */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 9088);

    public static final Param JDBC_URL =
            new Param(
                    "jdbcUrl",
                    String.class,
                    "The JDBC url (check the JDBC driver docs to find out its format)",
                    true);

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new InformixDialect(dataStore);
    }

    @Override
    public String getDisplayName() {
        return "Informix";
    }

    @Override
    protected String getDriverClassName() {
        return "com.informix.jdbc.IfxDriver";
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDescription() {
        return "Informix Database";
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT DBINFO('version','full') FROM systables WHERE tabid = 1";
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);

        parameters.remove(HOST.key);
        parameters.remove(PORT.key);
        parameters.remove(DATABASE.key);

        parameters.remove(SCHEMA.key);

        parameters.put(JDBC_URL.key, JDBC_URL);
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        // Get url from informix-sql.properties as jdbc url
        if (params.containsKey("url")) {
            return (String) params.get("url");
        }
        return (String) JDBC_URL.lookUp(params);
    }
}
