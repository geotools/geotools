/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.Parameter;
import org.geotools.data.jdbc.datasource.DBCPDataSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.FactoryRegistryException;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Abstract implementation of DataStoreFactory for jdbc datastores.
 *
 * <p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public abstract class JDBCDataStoreFactory implements DataStoreFactorySpi {

    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    null,
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    /** parameter for database host */
    public static final Param HOST = new Param("host", String.class, "Host", true, "localhost");

    /** parameter for database port */
    public static final Param PORT = new Param("port", Integer.class, "Port", true);

    /** parameter for database instance */
    public static final Param DATABASE = new Param("database", String.class, "Database", false);

    /** parameter for database schema */
    public static final Param SCHEMA = new Param("schema", String.class, "Schema", false);

    /** parameter for database user */
    public static final Param USER = new Param("user", String.class, "user name to login as");

    /** parameter for database password */
    public static final Param PASSWD =
            new Param(
                    "passwd",
                    String.class,
                    new SimpleInternationalString("password used to login"),
                    false,
                    null,
                    Collections.singletonMap(Parameter.IS_PASSWORD, Boolean.TRUE));

    /** parameter for namespace of the datastore */
    public static final Param NAMESPACE =
            new Param("namespace", String.class, "Namespace prefix", false);

    /** parameter for data source */
    public static final Param DATASOURCE =
            new Param("Data Source", DataSource.class, "Data Source", false);

    /** Maximum number of connections in the connection pool */
    public static final Param MAXCONN =
            new Param(
                    "max connections",
                    Integer.class,
                    "maximum number of open connections",
                    false,
                    Integer.valueOf(10));

    /** Minimum number of connections in the connection pool */
    public static final Param MINCONN =
            new Param(
                    "min connections",
                    Integer.class,
                    "minimum number of pooled connection",
                    false,
                    Integer.valueOf(1));

    /** If connections should be validated before using them */
    public static final Param VALIDATECONN =
            new Param(
                    "validate connections",
                    Boolean.class,
                    "check connection is alive before using it",
                    false,
                    Boolean.TRUE);

    /** Number of records read */
    public static final Param FETCHSIZE =
            new Param(
                    "fetch size",
                    Integer.class,
                    "number of records read with each iteraction with the dbms",
                    false,
                    1000);

    /**
     * If different from one, the JDBCInsertFeatureWriter will buffer the features and insert them
     * in batches
     */
    public static final Param BATCH_INSERT_SIZE =
            new Param(
                    "Batch insert size",
                    Integer.class,
                    "Number of records inserted in the same batch (default, 1). For optimal performance, set to 100.",
                    false,
                    1);

    /** Maximum amount of time the pool will wait when trying to grab a new connection * */
    public static final Param MAXWAIT =
            new Param(
                    "Connection timeout",
                    Integer.class,
                    "number of seconds the connection pool will wait before timing out attempting to get a new connection (default, 20 seconds)",
                    false,
                    20);

    /** If IDLE connections should be validated before using them * */
    public static final Param TEST_WHILE_IDLE =
            new Param(
                    "Test while idle",
                    Boolean.class,
                    "Periodically test the connections are still valid also while idle in the pool",
                    false,
                    true);

    /** Idle object evictor periodicity * */
    public static final Param TIME_BETWEEN_EVICTOR_RUNS =
            new Param(
                    "Evictor run periodicity",
                    Integer.class,
                    "number of seconds between idle object evitor runs (default, 300 seconds)",
                    false,
                    300);

    /** Min time for a connection to be idle in order to be evicted * */
    public static final Param MIN_EVICTABLE_TIME =
            new Param(
                    "Max connection idle time",
                    Integer.class,
                    "number of seconds a connection needs to stay idle for the evictor to consider closing it",
                    false,
                    300);

    /** Number of connections checked during a single evictor run * */
    public static final Param EVICTOR_TESTS_PER_RUN =
            new Param(
                    "Evictor tests per run",
                    Integer.class,
                    "number of connections checked by the idle connection evictor for each of its runs (defaults to 3)",
                    false,
                    3);

    /** Metadata table providing information about primary keys * */
    public static final Param PK_METADATA_TABLE =
            new Param(
                    "Primary key metadata table",
                    String.class,
                    "The optional table containing primary key structure and sequence associations. Can be expressed as 'schema.name' or just 'name'",
                    false);

    /**
     * Number of prepared statements cached per connection (this param is exposed only by factories
     * supporting prepared statements *
     */
    public static final Param MAX_OPEN_PREPARED_STATEMENTS =
            new Param(
                    "Max open prepared statements",
                    Integer.class,
                    "Maximum number of prepared statements kept open and cached for each connection in the pool. "
                            + "Set to 0 to have unbounded caching, to -1 to disable caching",
                    false,
                    50);

    /** expose primary key columns as attributes */
    public static final Param EXPOSE_PK =
            new Param(
                    "Expose primary keys",
                    Boolean.class,
                    "Expose primary key columns as " + "attributes of the feature type",
                    false,
                    false);

    /** SQL executed when the session begins */
    public static final Param SQL_ON_BORROW =
            new Param(
                    "Session startup SQL",
                    String.class,
                    "SQL statement executed when the connection is grabbed from the pool",
                    false,
                    null,
                    Collections.singletonMap(Parameter.IS_LARGE_TEXT, Boolean.TRUE));

    /** SQL executed when the session ends */
    public static final Param SQL_ON_RELEASE =
            new Param(
                    "Session close-up SQL",
                    String.class,
                    "SQL statement executed when the connection is released to the pool",
                    false,
                    null,
                    Collections.singletonMap(Parameter.IS_LARGE_TEXT, Boolean.TRUE));

    /** JDBC callback factory */
    public static final Param CALLBACK_FACTORY =
            new Param(
                    "Callback factory",
                    String.class,
                    "Name of JDBCReaderCallbackFactory to enable on the data store",
                    false);

    /** system property name used to set callback factory */
    static final String CALLBACK_PROPERTY = "gt2.jdbc.callback";

    /** registry for callback plugins */
    static final FactoryRegistry CALLBACK_REGISTRY = new FactoryCreator(JDBCCallbackFactory.class);

    @Override
    public String getDisplayName() {
        return getDescription();
    }

    /**
     * Default implementation verifies the Map against the Param information.
     *
     * <p>It will ensure that:
     *
     * <ul>
     *   <li>params is not null
     *   <li>Everything is of the correct type (or upcovertable to the correct type without Error)
     *   <li>Required Parameters are present
     * </ul>
     *
     * @return true if params is in agreement with getParametersInfo and checkDBType
     */
    public boolean canProcess(Map params) {
        if (!DataUtilities.canProcess(params, getParametersInfo())) {
            return false;
        }
        return checkDBType(params);
    }

    protected boolean checkDBType(Map params) {
        return checkDBType(params, getDatabaseID());
    }

    protected final boolean checkDBType(Map params, String dbtype) {
        String type;

        try {
            type = (String) DBTYPE.lookUp(params);

            if (dbtype.equals(type)) {
                return true;
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public final JDBCDataStore createDataStore(Map params) throws IOException {
        JDBCDataStore dataStore = new JDBCDataStore();

        // dialect
        final SQLDialect dialect = createSQLDialect(dataStore, params);
        dataStore.setSQLDialect(dialect);

        // datasource
        // check if the DATASOURCE parameter was supplied, it takes precendence
        DataSource ds = (DataSource) DATASOURCE.lookUp(params);
        if (ds != null) {
            dataStore.setDataSource(ds);
        } else {
            dataStore.setDataSource(createDataSource(params, dialect));
        }

        // fetch size
        Integer fetchSize = (Integer) FETCHSIZE.lookUp(params);
        if (fetchSize != null && fetchSize > 0) dataStore.setFetchSize(fetchSize);
        else dataStore.setFetchSize((Integer) FETCHSIZE.sample);

        Integer batchInsertSize = (Integer) BATCH_INSERT_SIZE.lookUp(params);
        if (batchInsertSize != null && batchInsertSize > 0) {
            dataStore.setBatchInsertSize(batchInsertSize);
        }

        // namespace
        String namespace = (String) NAMESPACE.lookUp(params);

        if (namespace != null) {
            dataStore.setNamespaceURI(namespace);
        }

        // database schema
        String schema = (String) SCHEMA.lookUp(params);

        if (schema != null) {
            dataStore.setDatabaseSchema(schema);
        }

        // primary key finder lookup table location, if any
        String metadataTable = (String) PK_METADATA_TABLE.lookUp(params);
        if (metadataTable != null) {
            MetadataTablePrimaryKeyFinder tableFinder = new MetadataTablePrimaryKeyFinder();
            if (metadataTable.contains(".")) {
                String[] parts = metadataTable.split("\\.");
                if (parts.length > 2)
                    throw new IllegalArgumentException(
                            "The primary key metadata table format "
                                    + "is either 'name' or 'schema.name'");
                tableFinder.setTableSchema(parts[0]);
                tableFinder.setTableName(parts[1]);
            } else {
                tableFinder.setTableName(metadataTable);
            }
            dataStore.setPrimaryKeyFinder(
                    new CompositePrimaryKeyFinder(tableFinder, new HeuristicPrimaryKeyFinder()));
        }

        // expose primary keys
        Boolean exposePk = (Boolean) EXPOSE_PK.lookUp(params);
        if (exposePk != null) {
            dataStore.setExposePrimaryKeyColumns(exposePk);
        }

        // session startup and teardown
        String sqlOnBorrow = (String) SQL_ON_BORROW.lookUp(params);
        String sqlOnRelease = (String) SQL_ON_RELEASE.lookUp(params);
        if (sqlOnBorrow != null || sqlOnRelease != null) {
            SessionCommandsListener listener =
                    new SessionCommandsListener(sqlOnBorrow, sqlOnRelease);
            dataStore.getConnectionLifecycleListeners().add(listener);
        }

        // callback factory
        String callbackFactory = (String) CALLBACK_FACTORY.lookUp(params);
        if (callbackFactory == null) {
            // look for system property
            callbackFactory = System.getProperty(CALLBACK_PROPERTY);
        }
        dataStore.setCallbackFactory(
                callbackFactory != null
                        ? findCallbackFactory(callbackFactory)
                        : JDBCCallbackFactory.NULL);

        // factories
        dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        dataStore.setGeometryFactory(new GeometryFactory());
        dataStore.setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        dataStore.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
        dataStore.setDataStoreFactory(this);

        // call subclass hook and return
        JDBCDataStore result = createDataStoreInternal(dataStore, params);
        if (result.getDataSource() == null) {
            throw new IOException("JDBC Connection not available with provided parameters");
        }
        return result;
    }

    /**
     * Subclass hook to do additional initialization of a newly created datastore.
     *
     * <p>Typically subclasses will want to override this method in the case where they provide
     * additional datastore parameters, those should be processed here.
     *
     * <p>This method is provided with an instance of the datastore. In some cases subclasses may
     * wish to create a new instance of the datastore, for instance in order to wrap the original
     * instance. This is supported but the new datastore must be returned from this method. If not
     * is such the case this method should still return the original passed in.
     *
     * @param dataStore The newly created datastore.
     * @param params THe datastore parameters.
     */
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        return dataStore;
    }

    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException();
    }

    public final Param[] getParametersInfo() {
        LinkedHashMap map = new LinkedHashMap();
        setupParameters(map);

        return (Param[]) map.values().toArray(new Param[map.size()]);
    }

    /**
     * Sets up the database connection parameters.
     *
     * <p>Subclasses may extend, but should not override. This implementation registers the
     * following parameters.
     *
     * <ul>
     *   <li>{@link #HOST}
     *   <li>{@link #PORT}
     *   <li>{@link #DATABASE}
     *   <li>{@link #SCHEMA}
     *   <li>{@link #USER}
     *   <li>{@link #PASSWD}
     * </ul>
     *
     * Subclass implementation may remove any parameters from the map, or may overrwrite any
     * parameters in the map.
     *
     * @param parameters Map of {@link Param} objects.
     */
    protected void setupParameters(Map parameters) {
        // remember: when adding a new parameter here that is not connection related,
        // add it to the JDBCJNDIDataStoreFactory class
        parameters.put(
                DBTYPE.key,
                new Param(
                        DBTYPE.key,
                        DBTYPE.type,
                        DBTYPE.description,
                        DBTYPE.required,
                        getDatabaseID()));
        parameters.put(HOST.key, HOST);
        parameters.put(PORT.key, PORT);
        parameters.put(DATABASE.key, DATABASE);
        parameters.put(SCHEMA.key, SCHEMA);
        parameters.put(USER.key, USER);
        parameters.put(PASSWD.key, PASSWD);
        parameters.put(NAMESPACE.key, NAMESPACE);
        parameters.put(EXPOSE_PK.key, EXPOSE_PK);
        parameters.put(MAXCONN.key, MAXCONN);
        parameters.put(MINCONN.key, MINCONN);
        parameters.put(FETCHSIZE.key, FETCHSIZE);
        parameters.put(BATCH_INSERT_SIZE.key, BATCH_INSERT_SIZE);
        parameters.put(MAXWAIT.key, MAXWAIT);
        if (getValidationQuery() != null) parameters.put(VALIDATECONN.key, VALIDATECONN);
        parameters.put(TEST_WHILE_IDLE.key, TEST_WHILE_IDLE);
        parameters.put(TIME_BETWEEN_EVICTOR_RUNS.key, TIME_BETWEEN_EVICTOR_RUNS);
        parameters.put(MIN_EVICTABLE_TIME.key, MIN_EVICTABLE_TIME);
        parameters.put(EVICTOR_TESTS_PER_RUN.key, EVICTOR_TESTS_PER_RUN);
        parameters.put(PK_METADATA_TABLE.key, PK_METADATA_TABLE);
        parameters.put(SQL_ON_BORROW.key, SQL_ON_BORROW);
        parameters.put(SQL_ON_RELEASE.key, SQL_ON_RELEASE);
        parameters.put(CALLBACK_FACTORY.key, CALLBACK_FACTORY);
    }

    /**
     * Determines if the datastore is available.
     *
     * <p>Subclasses may with to override or extend this method. This implementation checks whether
     * the jdbc driver class (provided by {@link #getDriverClassName()} can be loaded.
     */
    public boolean isAvailable() {
        try {
            Class.forName(getDriverClassName());

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns the implementation hints for the datastore.
     *
     * <p>Subclasses may override, this implementation returns <code>null</code>.
     */
    public Map<java.awt.RenderingHints.Key, ?> getImplementationHints() {
        return null;
    }

    /**
     * Returns a string to identify the type of the database.
     *
     * <p>Example: 'postgis'.
     */
    protected abstract String getDatabaseID();

    /**
     * Returns the fully qualified class name of the jdbc driver.
     *
     * <p>For example: org.postgresql.Driver
     */
    protected abstract String getDriverClassName();

    /**
     * Creates the dialect that the datastore uses for communication with the underlying database.
     *
     * @param dataStore The datastore.
     * @param params The connection parameters
     */
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore, Map params) {
        return createSQLDialect(dataStore);
    }

    /**
     * Creates the dialect that the datastore uses for communication with the underlying database.
     *
     * @param dataStore The datastore.
     */
    protected abstract SQLDialect createSQLDialect(JDBCDataStore dataStore);

    /**
     * Creates the datasource for the data store.
     *
     * <p>This method creates a {@link BasicDataSource} instance and populates it as follows:
     *
     * <ul>
     *   <li>poolPreparedStatements -> false
     *   <li>driverClassName -> {@link #getDriverClassName()}
     *   <li>url -> 'jdbc:&lt;{@link #getDatabaseID()}>://&lt;{@link #HOST}>/&lt;{@link #DATABASE}>'
     *   <li>username -> &lt;{@link #USER}>
     *   <li>password -> &lt;{@link #PASSWD}>
     * </ul>
     *
     * If different behaviour is needed, this method should be extended or overridden.
     */
    protected DataSource createDataSource(Map params, SQLDialect dialect) throws IOException {
        BasicDataSource dataSource = createDataSource(params);

        // some default data source behaviour
        if (dialect instanceof PreparedStatementSQLDialect) {
            dataSource.setPoolPreparedStatements(true);

            // check if the dialect exposes the max prepared statements param
            Map<String, Serializable> testMap = new HashMap<String, Serializable>();
            setupParameters(testMap);
            if (testMap.containsKey(MAX_OPEN_PREPARED_STATEMENTS.key)) {
                Integer maxPreparedStatements =
                        (Integer) MAX_OPEN_PREPARED_STATEMENTS.lookUp(params);
                // limit prepared statements
                if (maxPreparedStatements != null && maxPreparedStatements > 0)
                    dataSource.setMaxOpenPreparedStatements(maxPreparedStatements);
                // disable statement caching fully if necessary
                if (maxPreparedStatements != null && maxPreparedStatements < 0)
                    dataSource.setPoolPreparedStatements(false);
            }
        }

        return new DBCPDataSource(dataSource);
    }

    /**
     * DataSource access allowing SQL use: intended to allow client code to query available schemas.
     *
     * <p>This DataSource is the clients responsibility to close() when they are finished using it.
     *
     * @param params Map of connection parameter.
     * @return DataSource for SQL use
     */
    public BasicDataSource createDataSource(Map params) throws IOException {
        // create a datasource
        BasicDataSource dataSource = new BasicDataSource();

        // driver
        dataSource.setDriverClassName(getDriverClassName());

        // url
        dataSource.setUrl(getJDBCUrl(params));

        // username (on embedded dbs it can be optional)
        String user = (String) USER.lookUp(params);
        if (user != null) {
            dataSource.setUsername(user);
        }

        // password
        String passwd = (String) PASSWD.lookUp(params);
        if (passwd != null) {
            dataSource.setPassword(passwd);
        }

        // max wait
        Integer maxWait = (Integer) MAXWAIT.lookUp(params);
        if (maxWait != null && maxWait != -1) {
            dataSource.setMaxWait(maxWait * 1000);
        }

        // connection pooling options
        Integer minConn = (Integer) MINCONN.lookUp(params);
        if (minConn != null) {
            dataSource.setMinIdle(minConn);
        }

        Integer maxConn = (Integer) MAXCONN.lookUp(params);
        if (maxConn != null) {
            dataSource.setMaxActive(maxConn);
        }

        Boolean validate = (Boolean) VALIDATECONN.lookUp(params);
        if (validate != null && validate && getValidationQuery() != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(getValidationQuery());
        }

        Boolean testWhileIdle = (Boolean) TEST_WHILE_IDLE.lookUp(params);
        if (testWhileIdle != null) {
            dataSource.setTestWhileIdle(testWhileIdle);
        }

        Integer timeBetweenEvictorRuns = (Integer) TIME_BETWEEN_EVICTOR_RUNS.lookUp(params);
        if (timeBetweenEvictorRuns != null && timeBetweenEvictorRuns > 0) {
            dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictorRuns * 1000l);
        }

        Integer minEvictableTime = (Integer) MIN_EVICTABLE_TIME.lookUp(params);
        if (minEvictableTime != null) {
            dataSource.setMinEvictableIdleTimeMillis(minEvictableTime * 1000l);
        }

        Integer evictorTestsPerRun = (Integer) EVICTOR_TESTS_PER_RUN.lookUp(params);
        if (evictorTestsPerRun != null) {
            dataSource.setNumTestsPerEvictionRun(evictorTestsPerRun);
        }

        // some datastores might need this
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        return dataSource;
    }

    /**
     * Override this to return a good validation query (a very quick one, such as one that asks the
     * database what time is it) or return null if the factory does not support validation.
     */
    protected abstract String getValidationQuery();

    /**
     * Builds up the JDBC url in a jdbc:<database>://<host>:<port>/<dbname> Override if you need a
     * different setup
     */
    protected String getJDBCUrl(Map params) throws IOException {
        // jdbc url
        String host = (String) HOST.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        String db = (String) DATABASE.lookUp(params);

        String url = "jdbc:" + getDatabaseID() + "://" + host;
        if (port != null) {
            url += ":" + port;
        }

        if (db != null) {
            url += "/" + db;
        }
        return url;
    }

    /**
     * Looks up the callback factory with the specified name.
     *
     * <p>Will return {@link JDBCCallbackFactory#NULL} if the factory with the specified name can't
     * be found.
     */
    static JDBCCallbackFactory findCallbackFactory(String factoryName) {
        if (factoryName != null) {
            try {
                Predicate<JDBCCallbackFactory> sameName =
                        f -> factoryName.equalsIgnoreCase(f.getName());
                return CALLBACK_REGISTRY.getFactory(
                        JDBCCallbackFactory.class, sameName, null, null);
            } catch (FactoryRegistryException e) {
                // pass through
            }
        }
        return JDBCCallbackFactory.NULL;
    }
}
