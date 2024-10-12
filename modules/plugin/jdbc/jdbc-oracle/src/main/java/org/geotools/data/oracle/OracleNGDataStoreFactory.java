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
package org.geotools.data.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.api.data.Parameter;
import org.geotools.api.data.Transaction;
import org.geotools.data.jdbc.datasource.DBCPDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.logging.Logging;

/**
 * Oracle data store factory.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Andrea Aime, OpenGEO
 * @author Hendrik Peilke
 */
public class OracleNGDataStoreFactory extends JDBCDataStoreFactory {
    private static final String JDBC_PATH = "jdbc:oracle:thin:@";

    /** parameter for database type */
    public static final Param DBTYPE = new Param(
            "dbtype", String.class, "Type", true, "oracle", Collections.singletonMap(Parameter.LEVEL, "program"));

    /** parameter for database port */
    public static final Param PORT = new Param("port", Integer.class, "Port", false, 1521);

    /** parameter for database host */
    public static final Param HOST = new Param("host", String.class, "Host", false, "localhost");

    /** parameter for database instance */
    public static final Param DATABASE = new Param("database", String.class, "Database", true);

    /** parameter that enables estimated extends instead of exact ones */
    public static final Param ESTIMATED_EXTENTS = new Param(
            "Estimated extends",
            Boolean.class,
            "Use the spatial index information to quickly get an estimate of the data bounds",
            false,
            Boolean.TRUE);

    /** parameter for namespace of the datastore */
    public static final Param LOOSEBBOX =
            new Param("Loose bbox", Boolean.class, "Perform only primary filter on bbox", false, Boolean.TRUE);

    /** Metadata table providing information about primary keys * */
    public static final Param GEOMETRY_METADATA_TABLE = new Param(
            "Geometry metadata table",
            String.class,
            "The optional table containing geometry metadata (geometry type and srid). Can be expressed as 'schema.name' or just 'name'",
            false);

    /**
     * parameter for getting bbox from MDSYS.USER_SDO_GEOM_METADATA or MDSYS.ALL_SDO_GEOM_METADATA
     * table
     */
    public static final Param METADATA_BBOX = new Param(
            "Metadata bbox",
            Boolean.class,
            "Get data bounds quickly from MDSYS.USER_SDO_GEOM_METADATA or MDSYS.ALL_SDO_GEOM_METADATA table",
            false,
            Boolean.FALSE);

    /** parameter for specify the login timeout. */
    public static final Param LOGIN_TIMEOUT = new Param(
            "Login Timeout (s)",
            Integer.class,
            "Specifies the timeout for opening an Oracle JDBC connection (seconds)",
            false);

    /** Specifies the socket connection timeout to the database. */
    public static final Param CONNECTION_TIMEOUT = new Param(
            "Socket connection timeout (ms)",
            Integer.class,
            "Specifies the timeout when connecting a socket to the database listener (milliseconds)",
            false);

    /** Specifies the timeout when negotiating a session with the database. */
    public static final Param OUTBOUND_CONNECTION_TIMEOUT = new Param(
            "Outbound connection timeout (ms)",
            Integer.class,
            "Specifies the timeout when negotiating a session with the database listener (milliseconds)",
            false);

    /** Specifies whether REMARKS metadata will be returned. */
    public static final Param GET_REMARKS = new Param(
            "Get remarks", Boolean.class, "Indicates whether REMARKS are fetched from database", false, Boolean.FALSE);

    static final String LOGIN_TIMEOUT_NAME = "oracle.jdbc.loginTimeout";

    static final String CONN_TIMEOUT_NAME = "oracle.net.CONNECT_TIMEOUT";

    static final String OUTBOUND_TIMEOUT_NAME = "oracle.net.OUTBOUND_CONNECT_TIMEOUT";

    static final Logger LOGGER = Logging.getLogger(OracleNGDataStoreFactory.class);

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new OracleDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDisplayName() {
        return "Oracle NG";
    }

    @Override
    public String getDescription() {
        return "Oracle Database";
    }

    @Override
    protected String getDriverClassName() {
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    protected boolean checkDBType(Map<String, ?> params) {
        if (super.checkDBType(params)) {
            try {
                // check for old factory
                Class.forName("org.geotools.data.oracle.OracleDataStoreFactory");

                // it is around, let it handle this connection
                return false;
            } catch (ClassNotFoundException e) {
                // old factory not around, pick up the connection
                return true;
            }
        } else {
            // check for the old id
            return checkDBType(params, "Oracle");
        }
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {

        // make the schema uppercase if it's not already
        if (dataStore.getDatabaseSchema() != null) {
            dataStore.setDatabaseSchema(dataStore.getDatabaseSchema().toUpperCase());
        }

        // setup loose bbox
        OracleDialect dialect = (OracleDialect) dataStore.getSQLDialect();
        Boolean loose = (Boolean) LOOSEBBOX.lookUp(params);
        dialect.setLooseBBOXEnabled(loose == null || Boolean.TRUE.equals(loose));

        // check the estimated extents
        Boolean estimated = (Boolean) ESTIMATED_EXTENTS.lookUp(params);
        dialect.setEstimatedExtentsEnabled(estimated == null || Boolean.TRUE.equals(estimated));

        // check the geometry metadata table
        String metadataTable = (String) GEOMETRY_METADATA_TABLE.lookUp(params);
        dialect.setGeometryMetadataTable(metadataTable);

        // check the metadata bbox option
        Boolean metadateBbox = (Boolean) METADATA_BBOX.lookUp(params);
        dialect.setMetadataBboxEnabled(Boolean.TRUE.equals(metadateBbox));

        // check the get column remarks option
        Boolean getColumnRemarks = (Boolean) GET_REMARKS.lookUp(params);
        dialect.setGetColumnRemarksEnabled(Boolean.TRUE.equals(getColumnRemarks));

        DataSource source = getDataSource(dataStore);
        if (source instanceof BasicDataSource) {
            Integer loginTimeout = (Integer) LOGIN_TIMEOUT.lookUp(params);
            Integer connectionTimeout = (Integer) CONNECTION_TIMEOUT.lookUp(params);
            Integer outboundConnTimeout = (Integer) OUTBOUND_CONNECTION_TIMEOUT.lookUp(params);
            BasicDataSource basicSource = (BasicDataSource) source;
            if (loginTimeout != null) basicSource.addConnectionProperty(LOGIN_TIMEOUT_NAME, loginTimeout.toString());
            if (connectionTimeout != null)
                basicSource.addConnectionProperty(CONN_TIMEOUT_NAME, connectionTimeout.toString());
            if (outboundConnTimeout != null)
                basicSource.addConnectionProperty(OUTBOUND_TIMEOUT_NAME, outboundConnTimeout.toString());
        }

        if (dataStore.getFetchSize() <= 0) {
            // Oracle is dead slow with the fetch size at 0, let's have a sane default
            dataStore.setFetchSize(200);
        }

        Connection cx = dataStore.getConnection(Transaction.AUTO_COMMIT);
        try {
            dialect.unwrapConnection(cx);
            dialect.initVersion(cx);
        } catch (SQLException e) {
            throw new IOException(
                    "Unable to obtain Oracle Connection require for SDO Geometry access)."
                            + "Check connection pool implementation to unsure unwrap is available",
                    e);
        } finally {
            dataStore.closeSafe(cx);
        }
        return dataStore;
    }

    private DataSource getDataSource(JDBCDataStore dataStore) {
        DataSource source = dataStore.getDataSource();
        if (source instanceof DBCPDataSource) {
            try {
                source = source.unwrap(BasicDataSource.class);
            } catch (SQLException e) {
                LOGGER.log(
                        Level.SEVERE,
                        "Error while unwrapping the DataSource before setting connection timeout properties. ",
                        e);
            }
        }
        return source;
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        String db = (String) DATABASE.lookUp(params);
        String host = (String) HOST.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);

        if (db.startsWith("(") || db.startsWith("ldap://"))
            // for Oracle LDAP:
            // ldap://[host]/[db],cn=OracleContext,dc=[oracle_ldap_context]
            // for Oracle RAC
            // (DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=[host])(PORT=[port, often
            // 1521]))(LOAD_BALANCE=YES)(CONNECT_DATA=(SERVICE_NAME=[oracle_service_name])))
            return JDBC_PATH + db;
        else if (db.startsWith("/") && host != null && port != null) return JDBC_PATH + "//" + host + ":" + port + db;
        else if (host != null && port != null) return JDBC_PATH + host + ":" + port + ":" + db;
        else
            throw new IOException(
                    "Unable to properly compose the JDBC URL string, some parameters as host and port may be null !");
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        // NOTE: when adding parameters here remember to add them to OracleNGOCIDataStoreFactory and
        // OracleNGJNDIDataStoreFactory

        super.setupParameters(parameters);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
        parameters.put(MAX_OPEN_PREPARED_STATEMENTS.key, MAX_OPEN_PREPARED_STATEMENTS);
        parameters.put(PORT.key, PORT);
        parameters.put(HOST.key, HOST);
        parameters.put(DATABASE.key, DATABASE);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(GEOMETRY_METADATA_TABLE.key, GEOMETRY_METADATA_TABLE);
        parameters.put(METADATA_BBOX.key, METADATA_BBOX);
        parameters.put(LOGIN_TIMEOUT.key, LOGIN_TIMEOUT);
        parameters.put(CONNECTION_TIMEOUT.key, CONNECTION_TIMEOUT);
        parameters.put(OUTBOUND_CONNECTION_TIMEOUT.key, OUTBOUND_CONNECTION_TIMEOUT);
        parameters.put(GET_REMARKS.key, GET_REMARKS);
    }

    @Override
    protected String getValidationQuery() {
        return "select 1 from dual";
    }
}
