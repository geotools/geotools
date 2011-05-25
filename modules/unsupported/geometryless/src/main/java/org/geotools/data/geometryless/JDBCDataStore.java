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
package org.geotools.data.geometryless;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.sf.jsqlparser.statement.select.SelectBody;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.FeatureTypeHandler;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.JDBCFeatureWriter;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.attributeio.WKTAttributeIO;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.NullFIDMapper;
import org.geotools.data.sql.BypassSqlFeatureTypeHandler;
import org.geotools.data.sql.RsMd2DbMdResultSet;
import org.geotools.data.sql.SqlDataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.UnaliasSQLEncoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

/**
 * An implementation of the GeoTools Data Store API for a generic non-spatial
 * database platform. The plan is to support traditional jdbc datatypes, and
 * support geometry held within such types (eg, x,y columns, or possibly WKT
 * strings)<br>
 * <br>
 * Please see {@link org.geotools.data.jdbc.JDBCDataStore class JDBCDataStore}
 * and {@link org.geotools.data.DataStore interface DataStore} for DataStore
 * usage details.
 * 
 * @author Rob Atkinson rob@socialchange.net.au
 *
 * @source $URL$
 */

public class JDBCDataStore extends org.geotools.data.jdbc.JDBCDataStore implements SqlDataStore {
    /** The logger for the mysql module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

    /**
     * Basic constructor for JDBCDataStore. Requires creation of a
     * {@link org.geotools.data.jdbc.ConnectionPool ConnectionPool}, which
     * could be done similar to the following:<br>
     * <br>
     * <code>MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory("mysqldb.geotools.org", "3306", "myCoolSchema");</code><br>
     * <code>ConnectionPool connectionPool = connectionFactory.getConnectionPool("omcnoleg", "myTrickyPassword123");</code><br>
     * <code>DataStore dataStore = new JDBCDataStore(connectionPool);</code><br>
     * 
     * @param connectionPool
     *            a MySQL
     *            {@link org.geotools.data.jdbc.ConnectionPool ConnectionPool}
     * @throws IOException
     *             if the database cannot be properly accessed
     * @see org.geotools.data.jdbc.ConnectionPool
     * @see org.geotools.data.mysql.MySQLConnectionFactory
     */
    public JDBCDataStore(DataSource connectionPool) throws IOException {
        super(connectionPool, new JDBCDataStoreConfig());
    }

    /** <code>DEFAULT_NAMESPACE</code> field */
    public static String DEFAULT_NAMESPACE = "http://geotools.org/jdbc";

    /**
     * Constructor for JDBCDataStore where the database schema name is provided.
     * 
     * @param connectionPool
     *            a MySQL
     *            {@link org.geotools.data.jdbc.ConnectionPool ConnectionPool}
     * @param databaseSchemaName
     *            the database schema. Can be null. See the comments for the
     *            parameter schemaPattern in
     *            {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[]) DatabaseMetaData.getTables},
     *            because databaseSchemaName behaves in the same way.
     * @throws IOException
     *             if the database cannot be properly accessed
     */
    public JDBCDataStore(DataSource connectionPool, String databaseSchemaName)
            throws IOException {
        this(connectionPool, databaseSchemaName, DEFAULT_NAMESPACE);
    }

    /**
     * Constructor for JDBCDataStore where the database schema name is provided.
     * 
     * @param connectionPool
     *            a MySQL
     *            {@link org.geotools.data.jdbc.ConnectionPool ConnectionPool}
     * @param databaseSchemaName
     *            the database schema. Can be null. See the comments for the
     *            parameter schemaPattern in
     *            {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[]) DatabaseMetaData.getTables},
     *            because databaseSchemaName behaves in the same way.
     * @param namespace
     *            the namespace for this data store. Can be null, in which case
     *            the namespace will simply be the schema name.
     * @throws IOException
     *             if the database cannot be properly accessed
     */
    public JDBCDataStore(DataSource connectionPool, String databaseSchemaName, String namespace)
            throws IOException {
        super(connectionPool, JDBCDataStoreConfig.createWithNameSpaceAndSchemaName(namespace,
                databaseSchemaName));
    }

    /**
     * A utility method for creating a JDBCDataStore from database connection
     * parameters, using the default port (3306) for MySQL.
     * 
     * @param host
     *            the host name or IP address of the database server
     * @param schema
     *            the name of the database instance
     * @param username
     *            the database username
     * @param password
     *            the password corresponding to <code>username</code>
     * @return a JDBCDataStore for the specified parameters
     * 
     * public static JDBCDataStore getInstance( String host, String schema,
     * String username, String password) throws IOException, SQLException {
     * return getInstance(host, 3306, schema, username, password); }
     */
    /**
     * Utility method for creating a JDBCDataStore from database connection
     * parameters.
     * 
     * @param host
     *            the host name or IP address of the database server
     * @param port
     *            the port number of the database
     * @param schema
     *            the name of the database instance
     * @param username
     *            the database username
     * @param password
     *            the password corresponding to <code>username</code>
     * @throws IOException
     *             if the JDBCDataStore cannot be created because the database
     *             cannot be properly accessed
     * @throws SQLException
     *             if a MySQL connection pool cannot be established
     * 
     * public static JDBCDataStore getInstance( String host, int port, String
     * schema, String username, String password) throws IOException,
     * SQLException { return new JDBCDataStore( new MySQLConnectionFactory(host,
     * port, schema).getConnectionPool(username, password)); }
     */
    /**
     * Utility method for getting a FeatureWriter for modifying existing
     * features, using no feature filtering and auto-committing. Not used for
     * adding new features.
     * 
     * @param typeName
     *            the feature type name (the table name)
     * @return a FeatureWriter for modifying existing features
     * @throws IOException
     *             if the database cannot be properly accessed
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName) throws IOException {
        return getFeatureWriter(typeName, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    }

    /**
     * Utility method for getting a FeatureWriter for adding new features, using
     * auto-committing. Not used for modifying existing features.
     * 
     * @param typeName
     *            the feature type name (the table name)
     * @return a FeatureWriter for adding new features
     * @throws IOException
     *             if the database cannot be properly accessed
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName) throws IOException {
        return getFeatureWriterAppend(typeName, Transaction.AUTO_COMMIT);
    }

    /**
     * Constructs an AttributeType from a row in a ResultSet. The ResultSet
     * contains the information retrieved by a call to getColumns() on the
     * DatabaseMetaData object. This information can be used to construct an
     * Attribute Type.
     * 
     * <p>
     * In addition to standard SQL types, this method identifies MySQL 4.1's
     * geometric datatypes and creates attribute types accordingly. This happens
     * when the datatype, identified by column 5 of the ResultSet parameter, is
     * equal to java.sql.Types.OTHER. If a Types.OTHER ends up not being
     * geometric, this method simply calls the parent class's buildAttributeType
     * method to do something with it.
     * </p>
     * 
     * <p>
     * Note: Overriding methods must never move the current row pointer in the
     * result set.
     * </p>
     * 
     * @param rs
     *            The ResultSet containing the result of a
     *            DatabaseMetaData.getColumns call.
     * 
     * @return The AttributeType built from the ResultSet.
     * 
     * @throws SQLException
     *             If an error occurs processing the ResultSet.
     * @throws DataSourceException
     *             Provided for overriding classes to wrap exceptions caused by
     *             other operations they may perform to determine additional
     *             types. This will only be thrown by the default implementation
     *             if a type is present that is not present in the
     *             TYPE_MAPPINGS.
     */
    protected AttributeDescriptor buildAttributeType(ResultSet rs) throws IOException {
        final int COLUMN_NAME = 4;
        final int DATA_TYPE = 5;
        final int TYPE_NAME = 6;

        try {
            int dataType = rs.getInt(DATA_TYPE);
            LOGGER.fine("dataType: " + dataType + " " + rs.getString(TYPE_NAME) + " "
                    + rs.getString(COLUMN_NAME));

            if (dataType == Types.OTHER) {
                // this is MySQL-specific; handle it
                // String typeName = rs.getString(TYPE_NAME);
                // String typeNameLower = typeName.toLowerCase();
                return super.buildAttributeType(rs);
            } else {
                return super.buildAttributeType(rs);
            }
        } catch (SQLException e) {
            throw new IOException("SQL exception occurred: " + e.getMessage());
        }
    }

    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getGeometryAttributeIO(org.geotools.feature.AttributeType)
     */
    protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type, QueryData queryData) {
        return new WKTAttributeIO();
    }

    protected JDBCFeatureWriter createFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> reader, QueryData queryData)
            throws IOException {
        LOGGER.fine("returning jdbc feature writer");

        return new GeometrylessFeatureWriter(reader, queryData);
    }

    /*
     * public SQLBuilder getSqlBuilder2(String typeName) throws IOException {
     * FilterToSQL encoder = new FilterToSQL();
     * encoder.setFIDMapper(getFIDMapper(typeName)); return new
     * GeometrylessSQLBuilder(encoder); }
     */

    /**
     * Returns a SQLBuilder for the requested FeatureType.
     * <p>
     * If the requested FeatureType corresponds to an in-process view (a view
     * specified through {@linkplain #registerView(String, String)}, the
     * returned SQLBuilder takes care of it.
     * </p>
     */
    public SQLBuilder getSqlBuilder(String typeName) throws IOException {
        BypassSqlFeatureTypeHandler ftHandler = (BypassSqlFeatureTypeHandler) super.typeHandler;
        FilterToSQL encoder = new UnaliasSQLEncoder();
        SimpleFeatureType schema = getSchema( typeName );
		encoder.setFeatureType( schema );
        encoder.setFIDMapper(getFIDMapper(typeName));
        SQLBuilder sqlBuilder;
        /*
         * if (ftHandler.isView(typeName)) { sqlBuilder = new
         * GeometrylessSQLBuilder(encoder, ftHandler); } else {
         */
        sqlBuilder = new GeometrylessSQLBuilder(encoder);
        /*
         * }
         */
        return sqlBuilder;
    }

    // /////////////////////////////

    /**
     * Overrides <code>JDBC1DataStore.getFeatureTypeHandler</code> to return
     * an in-process view aware one, a {@linkplain BypassSqlFeatureTypeHandler}
     * 
     * @param config
     * @return a {@linkplain BypassSqlFeatureTypeHandler} that maintains a
     *         registry of user defined SQL queries exposed as read-only
     *         FeatureTypes
     */
    protected FeatureTypeHandler getFeatureTypeHandler(
            JDBCDataStoreConfig config) throws IOException {
        return new BypassSqlFeatureTypeHandler(this,
                buildFIDMapperFactory(config), config.getTypeHandlerTimeout());
    }
    
    /**
     * I'm adding this method right now just to get the class compiling. By now
     * it just delegates to the old {@link #registerView(String, String)} with
     * the <code>toString()</code> value of the select body. In the short
     * term, the delegation shuold be inversed so the datastore uses the object
     * model representing the query instead of parsing the string query "by
     * hand".
     */
    public void registerView(final String typeName, final SelectBody select) throws IOException {
        String sqlQuery = select.toString();
        registerView(typeName, sqlQuery);
    }

    /**
     * Creates an in-process data view against one or more actual FeatureTypes
     * of this DataStore, which will be advertised as <code>typeName</code>
     * 
     * @param typeName
     *            the name of the view's FeatureType.
     * @param sqlQuery
     *            a full SQL query which will act as the view definition.
     * @throws IOException
     * @throws IllegalArgumentException
     *             if <code>typeName</code> already exists as one of this
     *             datastore's feature types, regardless of type name case.
     */
    public void registerView(final String typeName, final String sqlQuery) throws IOException {
        if (typeName == null || sqlQuery == null) {
            throw new NullPointerException(typeName + "=" + sqlQuery);
        }
        String[] existingTypeNames = getTypeNames();
        for (int i = 0; i < existingTypeNames.length; i++) {
            if (typeName.equalsIgnoreCase(existingTypeNames[i])) {
                throw new IllegalArgumentException(typeName + " already exists: "
                        + existingTypeNames[i]);
            }
        }
        LOGGER.fine("registering view " + typeName + " as " + sqlQuery);
        Connection conn = getConnection(Transaction.AUTO_COMMIT);
        ResultSetMetaData rsmd;
        try {
            Statement st = conn.createStatement();
            st.setMaxRows(1);
            
            ResultSet rs = st.executeQuery(sqlQuery);
            rsmd = rs.getMetaData();

            // TODO: set a more appropiate fid mapper
            FIDMapper fidMapper = new NullFIDMapper();
            SimpleFeatureType viewType = buildSchema(typeName, rsmd);
            BypassSqlFeatureTypeHandler th = (BypassSqlFeatureTypeHandler) typeHandler;
            th.registerView(viewType, sqlQuery, fidMapper);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "executing query " + sqlQuery, e);
            throw new DataSourceException("executing " + sqlQuery, e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    /**
     * Creates a FeatureType from a <code>java.sql.ResultSetMetaData</code>
     * object, obtained from the execution of a SQL query configured as the
     * source of an in-process view.
     * 
     * @param typeName
     * @param rsmd
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private SimpleFeatureType buildSchema(String typeName, ResultSetMetaData rsmd) throws IOException,
            SQLException {
        SimpleFeatureType viewType;

        final int NAME_COLUMN = 4;
        final int TYPE_NAME = 6;

        ResultSet tableInfo = null;
        FIDMapper mapper = new NullFIDMapper();
        // the mapping from resultset medatada to the
        // resultset schema expected by JDBCDataStore
        tableInfo = new RsMd2DbMdResultSet(rsmd);

        try {
            List attributeDescriptors = new ArrayList();

            boolean tableInfoFound = false;

            while (tableInfo.next()) {
                tableInfoFound = true;
                try {
                    String columnName = tableInfo.getString(NAME_COLUMN);

                    if (!mapper.returnFIDColumnsAsAttributes()) {
                        boolean isPresent = false;

                        for (int i = 0; i < mapper.getColumnCount(); i++) {
                            if (columnName.equalsIgnoreCase(mapper.getColumnName(i))) {
                                isPresent = true;
                                break;
                            }
                        }
                        if (isPresent) {
                            continue;
                        }
                    }

                    // AttributeDescriptor attribute =
                    // buildAttributeDescriptor(tableInfo);
                    AttributeDescriptor attribute = buildAttributeType(tableInfo);

                    if (attribute != null) {
                        attributeDescriptors.add(attribute);
                    } else {
                        LOGGER.finest("Unknown SQL Type: (may be consumed) "
                                + tableInfo.getString(TYPE_NAME));
                    }
                } catch (DataSourceException dse) {
                    String msg = "Error building attribute type. The column will be ignored";
                    LOGGER.log(Level.WARNING, msg, dse);
                }
            }

            if (!tableInfoFound) {
                throw new SchemaNotFoundException(typeName);

            }

            URI namespace = getNameSpace();
            String ns = null;
            if (namespace != null) {
                ns = namespace.toString();
            }

            // AttributeName ftName = new AttributeName(ns, typeName);
            // viewType = (FeatureType)tf.createFeatureType(ftName, schema,
            // null);

            AttributeDescriptor[] types = (AttributeDescriptor[]) attributeDescriptors
                    .toArray(new AttributeDescriptor[0]);
            
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setName( typeName );
            tb.setNamespaceURI( namespace );
            tb.addAll( types );
            
            viewType = tb.buildFeatureType();
            return viewType;

        } catch (Exception sqlException) {
            throw new DataSourceException("SQL Error building FeatureType for " + typeName + " "
                    + sqlException.getMessage(), sqlException);
        } finally {
            JDBCUtils.close(tableInfo);
        }
    }

}
