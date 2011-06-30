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
package org.geotools.data.teradata;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DBCPDataSource;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.jdbc.CompositePrimaryKeyFinder;
import org.geotools.jdbc.HeuristicPrimaryKeyFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.MetadataTablePrimaryKeyFinder;
import org.geotools.jdbc.PrimaryKeyFinder;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.FeatureType;

public class TeradataDataStoreFactory extends JDBCDataStoreFactory {

    public static final Logger LOGGER = Logging.getLogger(TeradataDataStoreFactory.class);

    /**
     * parameter for database type
     */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "teradata");
    
    public static final Param LOBWORKAROUND = new Param("LOB Workaround",Boolean.class,
            "Disable LOB workaround", false, Boolean.FALSE);

    /**
     * enables using && in bbox queries
     */
    public static final Param LOOSEBBOX = new Param("Loose bbox", Boolean.class,
            "Perform only primary filter on bbox", false, Boolean.TRUE);

    /**
     * parameter for database port
     */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 1025);

    /**
     * teradata connection mode
     */
    public static final Param TMODE = new Param("tmode", String.class, "tmode", false, "ANSI");

    /**
     * charset to use when reading character data
     */
    public static final Param CHARSET = new Param("charset", String.class, "charset", false);

    /**
     * Tessellation lookup table
     */
    public static final Param TESSELLATION_TABLE = new Param("tessellationTable", String.class, 
        "Tessellation lookup table", false, "sysspatial.tessellation");

    /**
     * Flag controlling estimated bounds.
     */
    public static final Param ESTIMATED_BOUNDS = new Param("estimatedBounds", Boolean.class, 
        "Use estimated bounds from tessellation table", false, false);


    public static final Param APPLICATION = new Param(
        "application", String.class, "ApplicationName query band", false, "GeoTools");

    private static final PrimaryKeyFinder KEY_FINDER = new CompositePrimaryKeyFinder(
            new MetadataTablePrimaryKeyFinder(), new TeradataPrimaryKeyFinder(),
            new HeuristicPrimaryKeyFinder());

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new TeradataDialect(dataStore);
    }

    @Override
    public String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDisplayName() {
        return "Teradata";
    }

    public String getDescription() {
        return "Teradata Database";
    }

    @Override
    protected String getDriverClassName() {
        return "com.teradata.jdbc.TeraDriver";
    }

    @Override
    protected boolean checkDBType(Map params) {
        return checkDBType(params, "teradata");
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, final Map params)
            throws IOException {

        // setup loose bbox
        TeradataDialect dialect = (TeradataDialect) dataStore.getSQLDialect();
        
        Boolean lobWorkaround = (Boolean) LOBWORKAROUND.lookUp(params);
        dialect.setLobWorkaroundEnabled(lobWorkaround == null || !lobWorkaround);

        Boolean loose = (Boolean) LOOSEBBOX.lookUp(params);
        dialect.setLooseBBOXEnabled(loose == null || Boolean.TRUE.equals(loose));

        //primary key finder
        if (!params.containsKey(PK_METADATA_TABLE.key)) {
            dataStore.setPrimaryKeyFinder(KEY_FINDER);
        }

        //estimated bounds
        Boolean estimatedBounds = (Boolean) ESTIMATED_BOUNDS.lookUp(params);
        if (estimatedBounds != null && estimatedBounds.booleanValue()) {
            dialect.setEstimatedBounds(estimatedBounds);
        }
        
        if (params.containsKey(APPLICATION.key)) {
            dialect.setApplication((String) APPLICATION.lookUp(params));
        }

        //set schema to be same as user
        String username = null;
        if (params.containsKey(USER.key)) {
            username = (String) USER.lookUp(params);
        }
        else if (params.containsKey(DATASOURCE.key)) {
            DataSource dataSource = (DataSource) DATASOURCE.lookUp(params);
            if (dataSource instanceof BasicDataSource) {
                username = ((BasicDataSource)dataSource).getUsername();
            }
            else if (dataSource instanceof DBCPDataSource) {
                try {
                    username = ((BasicDataSource)((DBCPDataSource)dataSource)
                        .unwrap(DataSource.class)).getUsername();
                } catch (SQLException e) {
                    throw (IOException) new IOException().initCause(e);
                }
            }
        }
        if(params.containsKey(SCHEMA.key)) {
          dataStore.setDatabaseSchema((String) SCHEMA.lookUp(params));
        } else {
          dataStore.setDatabaseSchema(username);
        }
        return dataStore;
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(LOBWORKAROUND.key, LOBWORKAROUND);
        parameters.put(PORT.key, PORT);
        parameters.put(TESSELLATION_TABLE.key, TESSELLATION_TABLE);
        parameters.put(ESTIMATED_BOUNDS.key, ESTIMATED_BOUNDS);
        parameters.put(MAX_OPEN_PREPARED_STATEMENTS.key, MAX_OPEN_PREPARED_STATEMENTS);
        parameters.put(APPLICATION.key, APPLICATION);
    }

    @Override
    protected String getValidationQuery() {
        return "select current_timestamp;";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        String db = (String) DATABASE.lookUp(params);
        int port = (Integer) PORT.lookUp(params);
        String mode = (String) TMODE.lookUp(params);
        if (mode == null) {
            mode = TMODE.sample.toString();
        }
        
        String url = 
            "jdbc:teradata://" + host + "/DATABASE=" + db + ",PORT=" + port + ",TMODE=" + mode;
        
        //JD: only add charset if user specifically set it... it seems when CHARSET is set 
        // writing clob data does not work... need to investigate
        String charset = (String) CHARSET.lookUp(params);
        if (charset != null) {
            url += ",CHARSET=" + charset;
        }
        
        return url;
    }
}
