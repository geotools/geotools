package org.geotools.data.ingres;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.DataStore;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * 
 *
 * @source $URL$
 */
public class IngresDataStoreFactory extends JDBCDataStoreFactory {

    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "ingres");
    
    /** parameter for namespace of the datastore */
    public static final Param LOOSEBBOX = new Param("Loose bbox", Boolean.class, "Perform only primary filter on bbox", false, Boolean.TRUE);
    
    /** parameter for database port */
    public static final Param PORT = new Param("port", String.class, "Port", true, "II7");
    
    /** parameter for database schema */
    public static final Param SCHEMA = new Param("schema", String.class, "Schema", false, null);

    /**
     * Wheter a prepared statements based dialect should be used, or not
     */
    public static final Param PREPARED_STATEMENTS = new Param("preparedStatements", Boolean.class, "Use prepared statements", false, Boolean.FALSE);
    
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new IngresDialect(dataStore);
    }

    @Override
    protected String getDatabaseID() {
        return (String)DBTYPE.sample;
    }

    @Override
    protected String getDriverClassName() {
        return "com.ingres.jdbc.IngresDriver";
    }

    @Override
    protected String getValidationQuery() {
        return "select date('now')";
    }

    @Override
    protected void setupParameters(Map parameters) {    
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(SCHEMA.key, SCHEMA);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(PORT.key, PORT);
        
    }

    public String getDescription() {
        return "Ingres Database";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        String port = (String) PORT.lookUp(params);
        String database = (String) DATABASE.lookUp(params);
        
        StringBuilder url = new StringBuilder("jdbc:");
        url.append(getDatabaseID());
        url.append("://");
        url.append(host);
        if (port != null) {
            url.append(":").append(port);
        }
        if (database != null) {
            url.append("/").append(database);
        }
        url.append(";autocommit_mode=multi");
        
        return url.toString();
    }
    
    

}
