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

public class IngresDataStoreFactory extends JDBCDataStoreFactory {

    /** parameter for database type */
    public static final Param DBTYPE = new Param("dbtype", String.class, "Type", true, "ingres");
    
    /** parameter for namespace of the datastore */
    public static final Param LOOSEBBOX = new Param("Loose bbox", Boolean.class, "Perform only primary filter on bbox", false, Boolean.TRUE);
    
    /** parameter for database port */
    public static final Param PORT = new Param("port", Integer.class, "Port", true, 5432);
    
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

//    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
//        return null;
//    }
    
/*    public BasicDataSource createDataSource(Map params) throws IOException {
    	try {
			SCHEMA.parse((String) USER.lookUp(params));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return super.createDataSource(params);
    }*/

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

}
