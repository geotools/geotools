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
import java.util.LinkedHashMap;
import java.util.Map;
import org.geotools.api.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * A JDBC data store factory for connecting to SAP HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaDataStoreFactory extends JDBCDataStoreFactory {

    private static final String DATABASE_ID = "hana";

    public static final Param DBTYPE = new Param(
            "dbtype", String.class, "Type", true, DATABASE_ID, Collections.singletonMap(Parameter.LEVEL, "program"));

    public static final Param PORT =
            new Param("port", Integer.class, "Port to connect to. If omitted, you have to specify an instance.", false);

    public static final Param INSTANCE =
            new Param("instance", Integer.class, "Instance Number. Leave empty if you have specified a port.", false);

    public static final Param DATABASE = new Param(
            "database",
            String.class,
            "Database. Leave empty if you have specified a port or if you want to connect in single database mode. "
                    + "Use SYSTEMDB for the system database. ",
            false);

    public static final Param USE_SSL = new Param("use ssl", Boolean.class, "Use SSL", false);

    /** Enables direct encoding of selected filter functions in sql */
    public static final Param ENCODE_FUNCTIONS = new Param(
            "encode functions",
            Boolean.class,
            "Set to true to have a set of filter functions be translated directly in SQL. "
                    + "Due to differences in the type systems the result might not be the same as evaluating "
                    + "them in memory, including the SQL failing with errors while the in memory version works fine. "
                    + "However this allows to push more of the filter into the database, increasing performance.",
            false,
            Boolean.FALSE,
            Collections.singletonMap(Param.LEVEL, "advanced"));

    /** Prevents simplification by the database */
    public static final Param DISABLE_SIMPLIFY = new Param(
            "disable simplification",
            Boolean.class,
            "Certain operations like map rendering can request geometry simplification from the database. "
                    + "Setting this option to true will prevent geometry simplifcation by the database.",
            false,
            Boolean.FALSE);

    public static final Param SELECT_HINTS = new Param(
            "SELECT Hints",
            String.class,
            "Comma-separated list of hints that will be applied to SELECT queries, e.g. ESTIMATION_SAMPLES(0), NO_HASH_JOIN",
            false,
            null,
            Collections.singletonMap(Parameter.IS_LARGE_TEXT, Boolean.TRUE));

    /** parameter that enables estimated extents instead of exact ones */
    public static final Param ESTIMATED_EXTENTS = new Param(
            "Estimated extents",
            Boolean.class,
            "Use cached data to quickly get an estimate of the data bounds",
            false,
            Boolean.FALSE);

    private static final String DESCRIPTION = "SAP HANA";

    private static final String DRIVER_CLASS_NAME = "com.sap.db.jdbc.Driver";

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected String getDatabaseID() {
        return DATABASE_ID;
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

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        LinkedHashMap<String, Object> parentParams = new LinkedHashMap<>();
        super.setupParameters(parentParams);

        // Replace dbtype because the program level annotation is missing
        parentParams.put(DBTYPE.key, DBTYPE);

        // Replace port parameter as it is not required for HANA
        parentParams.put(PORT.key, PORT);

        // Replace database parameter to add additional documentation
        parentParams.put(DATABASE.key, DATABASE);

        // Insert additional parameters at the proper place
        for (Map.Entry<String, Object> param : parentParams.entrySet()) {
            parameters.put(param.getKey(), param.getValue());
            if (PORT.key.equals(param.getKey())) {
                parameters.put(INSTANCE.key, INSTANCE);
            }
            if (DATABASE.key.equals(param.getKey())) {
                parameters.put(USE_SSL.key, USE_SSL);
            }
            if (EXPOSE_PK.key.equals(param.getKey())) {
                parameters.put(ENCODE_FUNCTIONS.key, ENCODE_FUNCTIONS);
                parameters.put(DISABLE_SIMPLIFY.key, DISABLE_SIMPLIFY);
                parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
                parameters.put(SELECT_HINTS.key, SELECT_HINTS);
            }
        }
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        String host = (String) HOST.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        Integer instance = (Integer) INSTANCE.lookUp(params);
        String database = (String) DATABASE.lookUp(params);
        Boolean useSsl = (Boolean) USE_SSL.lookUp(params);

        HashMap<String, String> options = new HashMap<>();
        if (useSsl != null && useSsl == true) {
            options.put("encrypt", "true");
        }
        if (port != null && port != 0) {
            return HanaConnectionParameters.forPort(host, port, options).buildUrl();
        }
        if (instance == null) {
            throw new IOException("Either a port or an instance number must be given in the connection properties");
        }
        if (database != null && !database.isEmpty()) {
            return HanaConnectionParameters.forMultiContainer(host, instance, database, options)
                    .buildUrl();
        }
        return HanaConnectionParameters.forSingleContainer(host, instance, options)
                .buildUrl();
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        HanaDialect dialect = (HanaDialect) dataStore.getSQLDialect();
        Boolean encodeFunctions = (Boolean) ENCODE_FUNCTIONS.lookUp(params);
        dialect.setFunctionEncodingEnabled(encodeFunctions != null && encodeFunctions);
        Boolean disableSimplify = (Boolean) DISABLE_SIMPLIFY.lookUp(params);
        dialect.setSimplifyDisabled(disableSimplify != null && disableSimplify);
        String selectHints = (String) SELECT_HINTS.lookUp(params);
        dialect.setSelectHints(selectHints);
        Boolean estimated = (Boolean) ESTIMATED_EXTENTS.lookUp(params);
        dialect.setEstimatedExtentsEnabled(estimated != null && estimated);
        return dataStore;
    }
}
