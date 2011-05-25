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
package org.geotools.data.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.JDBCFeatureWriter;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.attributeio.WKBAttributeIO;
import org.geotools.data.jdbc.attributeio.WKTAttributeIO;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.filter.SQLEncoderMySQL;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * An implementation of the GeoTools Data Store API for the MySQL database platform.
 * The plan is to support traditional MySQL datatypes, as well as the new geometric
 * datatypes provided with MySQL 4.1 and higher.<br>
 * <br>
 * TODO:  MySQLDataStore is not yet tested for MySQL 4.1's geometric datatypes.<br>
 * <br>
 * Please see {@link org.geotools.data.jdbc.JDBCDataStore class JDBCDataStore} and
 * {@link org.geotools.data.DataStore interface DataStore} for DataStore usage details.
 * @author Gary Sheppard garysheppard@psu.edu
 * @author Andrea Aime aaime@users.sourceforge.net
 * @author Debasish Sahu debasish.sahu@rmsi.com
 *
 * @source $URL$
 */
public class MySQLDataStore extends JDBCDataStore {
    /** The logger for the mysql module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.mysql");
    
    
    /**
     * When true wkb encoding will be used to transfer geometries over the wire
     */
    protected boolean wkbEnabled;

    /**
     * Basic constructor for MySQLDataStore.
     * be done similar to the following:<br>
     * @param dataSource A source of connections for this datastore
     * @throws IOException if the database cannot be properly accessed
     * @see javax.sql.DataSource
     * @see org.geotools.data.mysql.MySQLConnectionFactory
     */
    public MySQLDataStore(DataSource dataSource) throws IOException {
        this(dataSource, null);
    }

    /**
     * Constructor for MySQLDataStore where the database schema name is provided.
     * @param dataSource A source of connections for this datastore
     * @param databaseSchemaName the database schema.  Can be null.  See the comments for the parameter schemaPattern in {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[]) DatabaseMetaData.getTables}, because databaseSchemaName behaves in the same way.
     * @throws IOException if the database cannot be properly accessed
     */
    public MySQLDataStore(DataSource dataSource, String databaseSchemaName)
        throws IOException {
        this(dataSource, databaseSchemaName, null);
    }

    /**
     * Constructor for MySQLDataStore where the database schema name is provided.
     * @param dataSource A source of connections for this datastore
     * @param databaseSchemaName the database schema.  Can be null.  See the comments for the parameter schemaPattern in {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[]) DatabaseMetaData.getTables}, because databaseSchemaName behaves in the same way.
     * @param namespace the namespace for this data store.  Can be null, in which case the namespace will simply be the schema name.
     * @throws IOException if the database cannot be properly accessed
     */
    public MySQLDataStore(DataSource dataSource, String databaseSchemaName, String namespace)
        throws IOException {
        super(dataSource,
            JDBCDataStoreConfig.createWithNameSpaceAndSchemaName(namespace, databaseSchemaName));
    }

    /**
     * A utility method for creating a MySQLDataStore from database connection parameters,
     * using the default port (3306) for MySQL.
     * @param host the host name or IP address of the database server
     * @param schema the name of the database instance
     * @param username the database username
     * @param password the password corresponding to <code>username</code>
     * @return a MySQLDataStore for the specified parameters
     */
    public static MySQLDataStore getInstance(String host, String schema, String username,
        String password) throws IOException, SQLException {
        return getInstance(host, 3306, schema, username, password);
    }

    /**
     * Utility method for creating a MySQLDataStore from database connection parameters.
     * @param host the host name or IP address of the database server
     * @param port the port number of the database
     * @param schema the name of the database instance
     * @param username the database username
     * @param password the password corresponding to <code>username</code>
     * @throws IOException if the MySQLDataStore cannot be created because the database cannot be properly accessed
     * @throws SQLException if a MySQL connection pool cannot be established
     */
    public static MySQLDataStore getInstance(String host, int port, String schema, String username,
        String password) throws IOException, SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + schema;
        String driver = "com.mysql.jdbc.Driver";

        return new MySQLDataStore(DataSourceUtil.buildDefaultDataSource(url, driver, username,
                password, "select version()"));
    }

    /**
     * Utility method for getting a FeatureWriter for modifying existing features,
     * using no feature filtering and auto-committing.  Not used for adding new
     * features.
     * @param typeName the feature type name (the table name)
     * @return a FeatureWriter for modifying existing features
     * @throws IOException if the database cannot be properly accessed
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName)
        throws IOException {
        return getFeatureWriter(typeName, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    }

    /**
     * Utility method for getting a FeatureWriter for adding new features, using
     * auto-committing.  Not used for modifying existing features.
     * @param typeName the feature type name (the table name)
     * @return a FeatureWriter for adding new features
     * @throws IOException if the database cannot be properly accessed
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName)
        throws IOException {
        return getFeatureWriterAppend(typeName, Transaction.AUTO_COMMIT);
    }

    /**
     * Constructs an AttributeType from a row in a ResultSet. The ResultSet
     * contains the information retrieved by a call to  getColumns() on the
     * DatabaseMetaData object.  This information  can be used to construct an
     * Attribute Type.
     *
     * <p>
     * In addition to standard SQL types, this method identifies MySQL 4.1's geometric
     * datatypes and creates attribute types accordingly.  This happens when the
     * datatype, identified by column 5 of the ResultSet parameter, is equal to
     * java.sql.Types.OTHER.  If a Types.OTHER ends up not being geometric, this
     * method simply calls the parent class's buildAttributeType method to do something
     * with it.
     * </p>
     *
     * <p>
     * Note: Overriding methods must never move the current row pointer in the
     * result set.
     * </p>
     *
     * @param rs The ResultSet containing the result of a
     *        DatabaseMetaData.getColumns call.
     *
     * @return The AttributeType built from the ResultSet.
     *
     * @throws SQLException If an error occurs processing the ResultSet.
     * @throws DataSourceException Provided for overriding classes to wrap
     *         exceptions caused by other operations they may perform to
     *         determine additional types.  This will only be thrown by the
     *         default implementation if a type is present that is not present
     *         in the TYPE_MAPPINGS.
     * NOTE: the srid of the features stored is used to build an CRS assuming 
     * that it an ESPG srid
     */
    protected AttributeDescriptor buildAttributeType(ResultSet rs)
        throws IOException {
	// these come from the Interface DatabaseMetaData in java.sql
	final int TABLE_NAME = 3;
        final int COLUMN_NAME = 4;
        final int DATA_TYPE = 5;
        final int TYPE_NAME = 6;

        try {
            int dataType = rs.getInt(DATA_TYPE);
            if (dataType == Types.OTHER || dataType == Types.BINARY) {
                //this is MySQL-specific; handle it
                String typeName = rs.getString(TYPE_NAME);
                AttributeTypeBuilder builder = new AttributeTypeBuilder();

				Class geom = getGeomClass(typeName);
                if ( geom == null) {
                    //nothing else we can do
                    return super.buildAttributeType(rs);
                }
				builder.setBinding(geom);

				//get CRS we are making the assumtion that the srid coresponds
				// to the EPSG srid.  Which may not be true.
				int srid = determineSRID(rs.getString(TABLE_NAME), rs.getString(COLUMN_NAME) );
				CoordinateReferenceSystem crs = null;
				try{
					crs = CRS.decode("EPSG:"+ srid);
				} catch (Exception e) {
					crs = null;
				}
				builder.setCRS(crs);

				// set some other stuff like name and return
                builder.setNillable(true);
                builder.setName(rs.getString(COLUMN_NAME));
                return builder.buildDescriptor(rs.getString(COLUMN_NAME));
            } else {
                return super.buildAttributeType(rs);
            }
        } catch (SQLException e) {
            throw new IOException("SQL exception occurred: " + e.getMessage());
        }
    }

    /** Map of mysql geometries to jts geometries */
    private static Map<String,Class> GEOM_TYPE_MAP = new HashMap<String,Class>();

    static {
        GEOM_TYPE_MAP.put("GEOMETRY", Geometry.class);
        GEOM_TYPE_MAP.put("POINT", Point.class);
        GEOM_TYPE_MAP.put("LINESTRING", LineString.class);
        GEOM_TYPE_MAP.put("POLYGON", Polygon.class);
        GEOM_TYPE_MAP.put("MULTIPOINT", MultiPoint.class);
        GEOM_TYPE_MAP.put("MULTILINESTRING", MultiLineString.class);
        GEOM_TYPE_MAP.put("MULTIPOLYGON", MultiPolygon.class);
        GEOM_TYPE_MAP.put("GEOMETRYCOLLECTION", GeometryCollection.class);
    }

 
	/** Returns the geomoty class associated with the string passed in or null
 	*/
    static public Class getGeomClass(String type)
    {
		return GEOM_TYPE_MAP.get(type.toUpperCase());
    }



    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getSqlBuilder(java.lang.String)
     */
    public SQLBuilder getSqlBuilder(String typeName) throws IOException {
        //SQLEncoder encoder = new SQLEncoderMySQL(); replace with this once
        //it is fully tested, the test cases work, but I don't have a live
        //mysql database. -ch
        //SQLEncoder encoder = new SQLEncoder();
        SQLEncoderMySQL encoder = new SQLEncoderMySQL();
        encoder.setFIDMapper(getFIDMapper(typeName));
        MySQLSQLBuilder builder = new MySQLSQLBuilder(encoder, getSchema(typeName));
        builder.setWKBEnabled(wkbEnabled);
        return builder;
    }

    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getGeometryAttributeIO(org.geotools.feature.AttributeType)
     */
    protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type, QueryData queryData) {
        if(wkbEnabled)
            return new WKBAttributeIO(queryData.getHints());
        else
            return new WKTAttributeIO();
    }

    protected JDBCFeatureWriter createFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> reader, QueryData queryData)
        throws IOException {
        LOGGER.fine("returning jdbc feature writer");

        return new MySQLFeatureWriter(reader, queryData);
    }
    
    private static final Set MYSQL_HINTS = Collections.unmodifiableSet(
            new HashSet(Arrays.asList(new Object[] {
            Hints.FEATURE_DETACHED, Hints.JTS_COORDINATE_SEQUENCE_FACTORY, 
            Hints.JTS_GEOMETRY_FACTORY})));

    @Override
    public Set getSupportedHints() {
        return MYSQL_HINTS;
    }
    
    @Override
    protected void setAutoCommit(boolean forWrite, Connection conn) throws SQLException {
        // override, do nothing
    }
    
    /**
     * Returns true if the WKB format is used to transfer geometries, false
     * otherwise
     *
     */
    public boolean isWKBEnabled() {
        return wkbEnabled;
    }

    /**
     * If turned on, WKB will be used to transfer geometry data instead of  WKT
     *
     * @param enabled
     */
    public void setWKBEnabled(boolean enabled) {
        wkbEnabled = enabled;
    }

    protected int determineSRID(String table, String column)
	    throws IOException{
    	Connection con = null;
  	try {

		// Gets a connection for the stored  JDBCDataStore and uses it to make a statament
		con = getConnection(Transaction.AUTO_COMMIT);
		Statement stmt = con.createStatement();
		ResultSet rslt =  stmt.executeQuery("SELECT SRID(" + column + ") FROM  " + table + " LIMIT 1;");
		if ( rslt.next() ) {
		   int srid = rslt.getInt(1);
		   JDBCUtils.close(stmt);
		   return srid;
		}
		throw new DataSourceException( "In table: " + table + ", geometry column: " + column + " was not able to return a valid srid.");
        } catch (SQLException sqle) {
            String message = sqle.getMessage();
            throw new DataSourceException(message, sqle);
        } finally {
            JDBCUtils.close(con, Transaction.AUTO_COMMIT, null);
        }
   }
}
