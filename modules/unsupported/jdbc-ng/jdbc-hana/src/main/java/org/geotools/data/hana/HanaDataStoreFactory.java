/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * A JDBC data store factory for connecting to SAP HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaDataStoreFactory extends JDBCDataStoreFactory {

    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "hana",
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    public static final Param PORT = new Param("port", Integer.class, "Port", false);

    public static final Param INSTANCE =
            new Param("instance", Integer.class, "Instance Number", false);

    public static final Param USE_SSL = new Param("use ssl", Boolean.class, "Use SSL", false);

    /** Enables direct encoding of selected filter functions in sql */
    public static final Param ENCODE_FUNCTIONS =
            new Param(
                    "encode functions",
                    Boolean.class,
                    "Set to true to have a set of filter functions be translated directly in SQL. "
                            + "Due to differences in the type systems the result might not be the same as evaluating "
                            + "them in memory, including the SQL failing with errors while the in memory version works fine. "
                            + "However this allows to push more of the filter into the database, increasing performance.",
                    false,
                    Boolean.FALSE,
                    Collections.singletonMap(Param.LEVEL, "advanced"));

    private static final String DESCRIPTION = "SAP HANA";

    private static final String DRIVER_CLASS_NAME = "com.sap.db.jdbc.Driver";

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    protected String getDriverClassName() {
        return DRIVER_CLASS_NAME;
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new HanaDialect(dataStore);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1 FROM DUMMY";
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);

        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(PORT.key, PORT);
        parameters.put(INSTANCE.key, INSTANCE);
        parameters.put(USE_SSL.key, USE_SSL);
        parameters.put(ENCODE_FUNCTIONS.key, ENCODE_FUNCTIONS);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        Integer instance = (Integer) INSTANCE.lookUp(params);
        String database = (String) DATABASE.lookUp(params);
        Boolean useSsl = (Boolean) USE_SSL.lookUp(params);

        HashMap<String, String> options = new HashMap<String, String>();
        if ((useSsl != null) && (useSsl == true)) {
            options.put("encrypt", "true");
        }
        if ((port != null) && (port != 0)) {
            return HanaConnectionParameters.forPort(host, port, options).buildUrl();
        }
        if (instance == null) {
            throw new IOException(
                    "Either a port or an instance number must be given in the connection properties");
        }
        if ((database != null) && (!database.isEmpty())) {
            return HanaConnectionParameters.forMultiContainer(host, instance, database, options)
                    .buildUrl();
        }
        return HanaConnectionParameters.forSingleContainer(host, instance, options).buildUrl();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        HanaDialect dialect = (HanaDialect) dataStore.getSQLDialect();
        Boolean encodeFunctions = (Boolean) ENCODE_FUNCTIONS.lookUp(params);
        dialect.setFunctionEncodingEnabled((encodeFunctions != null) && encodeFunctions);
        return dataStore;
    }
}
