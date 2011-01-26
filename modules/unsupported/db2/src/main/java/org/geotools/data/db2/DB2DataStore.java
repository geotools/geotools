/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.db2.filter.SQLEncoderDB2;
import org.geotools.data.jdbc.FeatureTypeHandler;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.JDBCFeatureWriter;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.AttributeTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * DB2 DataStore implementation.
 * 
 * <p>
 * Instances of this class should only be obtained via
 * DB2DataStoreFactory.createDataStore or DataStoreFinder.getDataStore.
 * </p>
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2DataStore extends JDBCDataStore implements DataStore{
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.db2");

    /**
     * List of all the DB2 geometry type names and the corresponding JTS class
     */
    private static final Map<String, Class<?>> DB2_GEOM_TYPE_MAPPING = new HashMap<String, Class<?>>();

    /**
     * Populate list of geometry classes supported by DB2
     */
    static {
        DB2_GEOM_TYPE_MAPPING.put("ST_POINT", Point.class);
        DB2_GEOM_TYPE_MAPPING.put("ST_LINESTRING", LineString.class);
        DB2_GEOM_TYPE_MAPPING.put("ST_POLYGON", Polygon.class);
        DB2_GEOM_TYPE_MAPPING.put("ST_MULTIPOINT", MultiPoint.class);
        DB2_GEOM_TYPE_MAPPING.put("ST_MULTILINESTRING", MultiLineString.class);
        DB2_GEOM_TYPE_MAPPING.put("ST_MULTIPOLYGON", MultiPolygon.class);
    }

    /** Reference to DB2 in-memory spatial catalog */
    private DB2SpatialCatalog catalog;

    /** The URL for this particular data store */
    private String dbURL = null;
    
    /** Time since catalog was last reloaded */
	private long lastTypeNameRequestTime  = 0;
	
    
    /**
     * The only supported constructor for a DB2DataStore. This constructor is
     * mainly intended to be called from DB2DataStoreFactory.
     *
     * @param connectionPool the initialized DB2 connection pool
     * @param config the JDBCDataStoreConfiguration
     * @param dbURL the database URL of the form
     *        <code>jdbc:db2://hostname:hostport/dbname </code>
     *
     * @throws IOException
     */
    public DB2DataStore(DataSource dataSource,
        JDBCDataStoreConfig config, String dbURL) throws IOException {
        super(dataSource, config);

        if (dataSource == null) {
            throw new IOException("DataSource pool is null");
        }

        this.dbURL = dbURL;

        // Get an instance of the DB2 spatial catalog for this database.
        // We need to provide a database connection in case the spatial catalog instance
        // does not already exist and we need to issue SQL against the database.
        Connection conn = this.getConnection(Transaction.AUTO_COMMIT);

        try {
            this.catalog = DB2SpatialCatalog.getInstance(dbURL,
                    config.getDatabaseSchemaName(), conn);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        } catch (SQLException e) {
            LOGGER.info("DB2SpatialCatalog create failed: " + e);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, e);
            throw new IOException("DB2SpatialCatalog create failed");
        }
    }
    public DataSource getDataSource() {
        return dataSource;
    }
    /**
     * Handles DB2-specific geometry types.  If it isn't one, just let the
     * parent method handle it.
     *
     * @param rs The ResultSet containing the result of a
     *        DatabaseMetaData.getColumns call.
     *
     * @return The AttributeType built from the ResultSet.
     *
     * @throws IOException If an error occurs processing the ResultSet.
     */
    protected AttributeDescriptor buildAttributeType(ResultSet rs)
        throws IOException {
        try {
        	String spatialTypePrefix = "\"DB2GSE\".\"ST_";

        	int dataType = rs.getInt("DATA_TYPE");
            String typeName = rs.getString("TYPE_NAME");
            String tableSchema = rs.getString("TABLE_SCHEM");
            String tableName = rs.getString("TABLE_NAME");
            String columnName = rs.getString("COLUMN_NAME");
            if (!typeName.startsWith(spatialTypePrefix)) {
                return super.buildAttributeType(rs);
            }
            
            String geomTypeName = this.catalog.getDB2GeometryTypeName(tableSchema,
                    tableName, columnName);

            // Look up the geometry type we just got back and create the
            // corresponding attribute type for the geometry class, if it was found.
            Class geomClass = (Class) DB2_GEOM_TYPE_MAPPING.get(geomTypeName);

            if (geomClass != null) {
                CoordinateReferenceSystem crs;

                try {
                    crs = this.catalog.getCRS(tableSchema, tableName, columnName);
                } catch (Exception e) {
                    throw new IOException("Exception: " + e.getMessage());
                }

                AttributeTypeBuilder atb = new AttributeTypeBuilder();
                atb.setName(columnName);
                atb.setBinding(geomClass);
                atb.setNillable(true);
                atb.setLength(0);
                atb.setDefaultValue(null);
                atb.setCRS(crs);
                
                GeometryDescriptor geometryAttribute = (GeometryDescriptor) atb
						.buildDescriptor(columnName);

                return geometryAttribute;
            }

            // It is some other unrecognized structured type - log what we found and return null
            LOGGER.fine("Type '" + geomTypeName + "' is not recognized");

            return null;
        } catch (SQLException e) {
            throw new IOException("SQL exception occurred: " + e.getMessage());
        }
    }

    /**
     * Creates a DB2-specific FIDMapperFactory.
     *
     * @param config not used.
     *
     * @return a DB2FIDMapperFactory
     */
    protected FIDMapperFactory buildFIDMapperFactory(JDBCDataStoreConfig config) {
        return new DB2FIDMapperFactory(config.getDatabaseSchemaName());
    }

    /**
     * Get the SRID associated with a geometry column.
     * 
     * <p>
     * The value returned is the EPSG coordinate system identifier, not the DB2
     * srs_id.
     * </p>
     *
     * @param tableName The name of the table to get the SRID for.
     * @param geometryColumnName The name of the geometry column within the
     *        table to get SRID for.
     *
     * @return The SRID for the geometry column  or -1.
     *
     * @throws IOException
     */
    protected int determineSRID(String tableName, String geometryColumnName)
        throws IOException {
        int srid = -1;

        // Not sure that this is actually the right value to use
        String tableSchema = this.config.getDatabaseSchemaName();
        srid = this.catalog.getCsId(tableSchema, tableName, geometryColumnName);
        LOGGER.fine(DB2SpatialCatalog.geomID(tableSchema, tableName,
                geometryColumnName) + " srid=" + srid);

        return srid;
    }

    /**
     * Gets the database URL.
     *
     * @return the database URL.
     */
    String getDbURL() {
        return this.dbURL;
    }

    /**
     * Create a DB2-specific FeatureTypeHandler.
     *
     * @param config a JDBCDataStoreConfig.
     *
     * @return a DB2FeatureTypeHandler.
     *
     * @throws IOException if the feature type handler could not be created.
     */
    protected FeatureTypeHandler getFeatureTypeHandler(
        JDBCDataStoreConfig config) throws IOException {
        return new DB2FeatureTypeHandler(this, buildFIDMapperFactory(config),
            config.getTypeHandlerTimeout());
    }

    /**
     * Gets the handler to convert a geometry database value to a JTS geometry.
     *
     * @param type not used.
     * @param queryData not used.
     *
     * @return AttributIO
     */
    protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type,
        QueryData queryData) {
        return new DB2WKBAttributeIO();
    }

    /**
     * Gets the instance of the DB2SpatialCatalog associated with this data
     * store.
     *
     * @return a DB2SpatialCatalog
     */
    DB2SpatialCatalog getSpatialCatalog() {
        return this.catalog;
    }

    /**
     * Gets the DB2-specific SQL builder object.
     *
     * @param typeName Name of the type to build the SQL for.
     *
     * @return DB2SQLBuilder
     *
     * @throws IOException
     */
    public SQLBuilder getSqlBuilder(String typeName) throws IOException {
        FeatureTypeInfo info = this.typeHandler.getFeatureTypeInfo(typeName);
        int srid = 0;
        SQLEncoderDB2 encoder = new SQLEncoderDB2();
        encoder.setSqlNameEscape("\"");
        FIDMapper mapper = getFIDMapper(typeName);
        encoder.setFIDMapper(mapper);

        if (info.getSchema().getGeometryDescriptor() != null) {
            String geom = info.getSchema().getGeometryDescriptor().getLocalName();
            srid = this.catalog.getSRID(getTableSchema(), typeName, geom);
        }

        encoder.setSRID(srid);

        // We should probably get the table schema name from the feature type
        // information - not sure that it exists there.
        return new DB2SQLBuilder((FilterToSQL) encoder, getTableSchema(), info.getSchema(), mapper);
    }

    /**
     * Gets the names of tables (types) that contain a spatial column.  Note
     * that there is still an issue concerning the ambiguity of spatial tables
     * that have the same table name but different table schema names.
     *
     * @return Array of type names as Strings.
     *
     * @throws IOException if the spatial catalog can not be accessed.
     */
    public String[] getTypeNames() throws IOException {

        long lastTime = lastTypeNameRequestTime;
        long now = System.currentTimeMillis();
        
        lastTypeNameRequestTime = now;
        
        if (lastTime < (now - config.getTypeHandlerTimeout())) {
        	refreshCatalog();
        }
        return getSpatialCatalog().getTypeNames();
    }
    
    
    /** 
     * Reloads the spatial catalog from the database.
     * <p>
     * This is useful in case anything changed after the datastore was initialized.
     * @throws IOException
     * @throws SQLException
     */
    public void refreshCatalog() throws IOException {
    	try {
    		Connection conn = getConnection(Transaction.AUTO_COMMIT);
			getSpatialCatalog().loadCatalog(conn,getTableSchema());
			lastTypeNameRequestTime = System.currentTimeMillis();
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
			LOGGER.fine("Refreshing spatial catalog");
		} catch (SQLException e) {
			throw(new IOException(e.getLocalizedMessage()));
		}
    }

    /**
     * Gets the table schema associated with this data store.
     * 
     * <p>
     * At some point this may change if multiple schemas are supported by a
     * data store.
     * </p>
     *
     * @return the schema name that will prefix table names.
     */
    public String getTableSchema() {
        return this.config.getDatabaseSchemaName();
    }

    /**
     * Gets a DB2-specific feature source.
     *
     * @param typeName
     *
     * @return a DB2Feature Source, Store or Locking
     *
     * @throws IOException if the feature source could not be created.
     *
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public SimpleFeatureSource getFeatureSource(String typeName)
        throws IOException {
        if (!this.typeHandler.getFIDMapper(typeName).isVolatile()
                || this.allowWriteOnVolatileFIDs) {
            if (getLockingManager() != null) {
                // Use default JDBCFeatureLocking that delegates all locking
                // the getLockingManager
                //
                return new DB2FeatureLocking(this, getSchema(typeName));
            } else {
                // subclass should provide a FeatureLocking implementation
                // but for now we will simply forgo all locking
                return new DB2FeatureStore(this, getSchema(typeName));
            }
        } else {
            return new DB2FeatureSource(this, getSchema(typeName));
        }
    }

    /**
     * Overrides the method in JDBCDataStore because it includes
     * PostGIS-specific handling to setAutoCommit(false) which causes problems
     * for DB2 because the transaction is still uncommitted when the
     * connection is closed.
     *
     * @param featureTypeInfo
     * @param tableName
     * @param sqlQuery The SQL query to execute.
     * @param transaction The Transaction is included here for handling
     *        transaction connections at a later stage.  It is not currently
     *        used.
     * @param forWrite
     *
     * @return The QueryData object that contains the resources for the query.
     *
     * @throws IOException
     * @throws DataSourceException If an error occurs performing the query.
     */
    protected QueryData executeQuery(FeatureTypeInfo featureTypeInfo,
        String tableName, String sqlQuery, Transaction transaction,
        boolean forWrite) throws IOException {
        LOGGER.fine("About to execute query: " + sqlQuery);

        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            conn = getConnection(transaction);
            statement = conn.createStatement(getResultSetType(forWrite),
                    getConcurrency(forWrite));

            statement.setFetchSize(200);

            int rsc1 = statement.getResultSetConcurrency();
            rs = statement.executeQuery(sqlQuery);

            int rsc2 = statement.getResultSetConcurrency();
            int c = rs.getConcurrency();
            int update = ResultSet.CONCUR_UPDATABLE;
            int read = ResultSet.CONCUR_READ_ONLY;

            return new QueryData(featureTypeInfo, this, conn, statement, rs,
                transaction);
        } catch (SQLException e) {
            // if an error occurred we close the resources
            String msg = "Error Performing SQL query: " + sqlQuery;
            LOGGER.log(Level.SEVERE, msg, e);
            JDBCUtils.close(rs);
            JDBCUtils.close(statement);
            JDBCUtils.close(conn, transaction, e);
            throw new DataSourceException(msg, e);
        }
    }

    /**
     * Overrides the method in JDBCDataStore so that a DB2FeatureWriter is
     * created.
     *
     * @param featureReader
     * @param queryData
     *
     * @return The DB2FeatureWriter
     *
     * @throws IOException
     *
     * @see org.geotools.data.jdbc.JDBCDataStore#createFeatureWriter(org.geotools.data.FeatureReader,
     *      org.geotools.data.jdbc.QueryData)
     */
    protected JDBCFeatureWriter createFeatureWriter(
         FeatureReader featureReader, QueryData queryData)
        throws IOException {
        String featureName = queryData.getFeatureType().getTypeName();

        return new DB2FeatureWriter(featureReader, queryData,
            (DB2SQLBuilder) getSqlBuilder(featureName));
    }
	/**
	 * This is a public entry point to the DataStore.
	 * 
	 * <p>
	 * We have given some though to changing this api to be based on query.
	 * </p>
	 * 
	 * <p>
	 * Currently the is is the only way to retype your features to different
	 * name spaces.
	 * </p>
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.data.DataStore#getFeatureReader(org.geotools.feature.FeatureType,
	 *      Filter, org.geotools.data.Transaction)
	 */
	public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(final SimpleFeatureType requestType,
			final Filter filter, final Transaction transaction)
			throws IOException {
		String typeName = requestType.getTypeName();
		SimpleFeatureType schemaType = getSchema(typeName);
        LOGGER.fine("requestType: " + requestType);
        LOGGER.fine("schemaType: " + schemaType);

		int compare = DataUtilities.compare(requestType, schemaType);

		Query query;

		if (compare == 0) {
			// they are the same type
			//
			query = new DefaultQuery(typeName, filter);
		} else if (compare == 1) {
			// featureType is a proper subset and will require reTyping
			//
			String[] names = attributeNames(requestType, filter);
			query = new DefaultQuery(typeName, filter, Query.DEFAULT_MAX,
					names, "getFeatureReader");
		} else {
			// featureType is not compatiable
			//
			throw new IOException("Type " + typeName + " does match request");
		}

		if ((filter == Filter.INCLUDE) || filter.equals(Filter.INCLUDE)) {
			return new EmptyFeatureReader(requestType);
		}

		FeatureReader<SimpleFeatureType, SimpleFeature> reader = getFeatureReader(query, transaction);

		if (compare == 1) {
			reader = new ReTypeFeatureReader(reader, requestType);
		}

		return reader;
	}
    
}
