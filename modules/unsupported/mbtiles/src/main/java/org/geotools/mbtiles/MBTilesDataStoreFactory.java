package org.geotools.mbtiles;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;
import org.sqlite.SQLiteConfig;

public class MBTilesDataStoreFactory extends JDBCDataStoreFactory {

    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "mbtiles",
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    /** optional user parameter */
    public static final Param USER =
            new Param(
                    JDBCDataStoreFactory.USER.key,
                    JDBCDataStoreFactory.USER.type,
                    JDBCDataStoreFactory.USER.description,
                    false,
                    JDBCDataStoreFactory.USER.sample);

    @Override
    protected String getDatabaseID() {
        return "mbtiles";
    }

    @Override
    public String getDescription() {
        return "MbTiles";
    }

    @Override
    protected String getDriverClassName() {
        return "org.sqlite.JDBC";
    }

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new MBTilesDialect(dataStore);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String db = (String) DATABASE.lookUp(params);
        return "jdbc:sqlite:" + db;
    }

    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);

        // remove unneccessary parameters
        parameters.remove(HOST.key);
        parameters.remove(PORT.key);
        parameters.remove(SCHEMA.key);

        // remove user and password temporarily in order to make username optional
        parameters.remove(JDBCDataStoreFactory.USER.key);
        parameters.put(USER.key, USER);

        // add user
        // add additional parameters
        parameters.put(DBTYPE.key, DBTYPE);
    }

    @Override
    public BasicDataSource createDataSource(Map params) throws IOException {
        // create a datasource
        BasicDataSource dataSource = new BasicDataSource();

        // driver
        dataSource.setDriverClassName(getDriverClassName());

        // url
        dataSource.setUrl(getJDBCUrl(params));

        // dataSource.setMaxActive(1);
        // dataSource.setMinIdle(1);

        // dataSource.setTestOnBorrow(true);
        // dataSource.setValidationQuery(getValidationQuery());
        addConnectionProperties(dataSource);

        return dataSource;
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        dataStore.setDatabaseSchema(null);
        return dataStore;
    }

    static void addConnectionProperties(BasicDataSource dataSource) {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableLoadExtension(true);
        // config.enableSpatiaLite(true);

        for (Map.Entry e : config.toProperties().entrySet()) {
            dataSource.addConnectionProperty((String) e.getKey(), (String) e.getValue());
        }
    }
}
