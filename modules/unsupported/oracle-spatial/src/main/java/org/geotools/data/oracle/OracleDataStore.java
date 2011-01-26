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
 *
 *    Created on 16/10/2003
 */
package org.geotools.data.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.DefaultSQLBuilder;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.JDBCFeatureWriter;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.attributeio.SDOAttributeIO;
import org.geotools.data.oracle.referencing.OracleAuthorityFactory;
import org.geotools.data.oracle.sdo.GeometryConverter;
import org.geotools.data.oracle.sdo.TT;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.SchemaException;
import org.geotools.filter.SQLEncoder;
import org.geotools.filter.SQLEncoderException;
import org.geotools.filter.SQLEncoderOracle;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeodeticCRS;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;

/**
 * @author Sean Geoghegan, Defence Science and Technology Organisation.
 * @source $URL$
 */
public class OracleDataStore extends JDBCDataStore {
     private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.oracle");
     private OracleAuthorityFactory af;

    /**
     * @param connectionPool
     * @param config
     * @throws IOException
     */
    public OracleDataStore(DataSource dataSource, JDBCDataStoreConfig config) throws IOException {
        super(dataSource, config);
    }

    /**
     * @param connectionPool
     * @throws DataSourceException
     */
    public OracleDataStore(DataSource dataSource, String schemaName, Map fidGeneration) throws IOException {
        this(dataSource, schemaName, schemaName, fidGeneration);
    }
    
    /**
     * @param connectionPool
     * @param namespace
     * @throws DataSourceException
     */
    public OracleDataStore(DataSource dataSource, String namespace, String schemaName, Map fidGeneration) throws IOException {
        //Ok, this needs more investigation, since the config constructor being
        //used seems to ignoe the fid map stuff.  I don't quite understand it,
        //and I think it may get picked up later, or at least auto-generated
        //later - maybe this is for the user specified stuff that never got
        //implemented.  Point being this needs to be looked into, I'm just 
        //setting it like this to get things working. -ch
        this(dataSource, new JDBCDataStoreConfig(namespace, schemaName, null, fidGeneration));

    }

    
    /** Crops non feature type tables. 
     * There are alot of additional tables in a Oracle tablespace. This tries
     * to remove some of them.  If the schemaName is provided in the Constructor
     * then the job of narrowing down tables will be mush easier.  Otherwise
     * there are alot of Meta tables and SDO tables to cull.  This method tries
     * to remove as many as possible. 
     * 
     * @see org.geotools.data.jdbc.JDBCDataStore#allowTable(java.lang.String)
     */
    protected boolean allowTable(String tablename) {
	LOGGER.finer("checking table name: " + tablename);
		if (tablename.endsWith("$"))  {
            return false;
		} else if (tablename.startsWith("BIN$"))  { // Added to ignore some Oracle 10g tables
            return false;
        } else if (tablename.startsWith("XDB$"))  {
            return false;
        } else if (tablename.startsWith("DR$"))  {
            return false;
        } else if (tablename.startsWith("DEF$"))  {
            return false;
        } else if (tablename.startsWith("SDO_"))  {
            return false;
        } else if (tablename.startsWith("WM$"))  {
            return false;
        } else if (tablename.startsWith("WK$"))  {
            return false;
        } else if (tablename.startsWith("AW$"))  {
            return false;
        } else if (tablename.startsWith("AQ$"))  {
            return false;
	} else if (tablename.startsWith("APPLY$"))  {
            return false;
	} else if (tablename.startsWith("REPCAT$"))  {
            return false;
        } else if (tablename.startsWith("CWM$"))  {
            return false;
        } else if (tablename.startsWith("CWM2$"))  {
            return false;
        } else if (tablename.startsWith("EXF$"))  {
            return false;
        } else if (tablename.startsWith("DM$"))  {
            return false;
        } 
        LOGGER.finer("returning true for tablename: " + tablename);
        return true;
    }

    /**
     * Overrides the buildAttributeType method to check for SDO_GEOMETRY columns.
     * 
     * @see <a href="http://download-west.oracle.com/docs/cd/B14117_01/appdev.101/b10826.pdf">A doc from Oracle.</a>
     * 
     *  TODO: Determine the specific type of the geometry.
     */
    protected AttributeDescriptor buildAttributeType(ResultSet rs) throws IOException {
    	final int TABLE_NAME = 3;
        final int COLUMN_NAME = 4;
        final int TYPE_NAME = 6;
        final int IS_NULLABLE = 18; // "NO", "YES" or ""
        try {
			if (rs.getString(TYPE_NAME).equals("SDO_GEOMETRY")) {
			    String tableName = rs.getString(TABLE_NAME);
                String columnName = rs.getString(COLUMN_NAME);
                String isNullable = rs.getString( IS_NULLABLE );
                return getSDOGeometryAttribute(tableName, columnName, "YES".equals(isNullable) );
			} else  {
			    return super.buildAttributeType(rs);
			}
		} catch (SQLException e) {
			throw new DataSourceException("Sql error occurred", e);
		}
    }
   
    /**
     * Construct and SDO_GEOMETRY attribute.
     * 
     * @see org.geotools.data.jdbc.JDBCDataStore#buildAttributeType(java.sql.ResultSet) 
     * @param tableName
     * @param columnName
     * @param isNillable 
     */
    private AttributeDescriptor getSDOGeometryAttribute(String tableName, String columnName, boolean isNullable ) {
    	int srid = 0; // aka NULL
    	AttributeTypeBuilder build = new AttributeTypeBuilder();
        build.setNillable(isNullable);
        try {
            Class geomClass = determineGeometryClass(tableName, columnName);
            build.setBinding(geomClass); 
        } catch (Exception e) {
            LOGGER.warning("could not determin Geometry class");
        }
		try {
			srid = determineSRID( tableName, columnName );
			CoordinateReferenceSystem crs = null;
            try {
              crs = CRS.decode("EPSG:" + srid);
            } catch(Exception e) {
              crs = determineCRS( srid );
            }
            build.setCRS(crs);
    
		} catch (Exception e) {
			LOGGER.warning( "Could not map SRID "+srid+" to CRS:"+e );
		}
		return build.buildDescriptor(columnName);		
	}
    private Class determineGeometryClass(String tableName, String columnName) throws IOException {
        Connection conn = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            String sqlStatement = //
            "select meta.sdo_layer_gtype \n" + "from mdsys.ALL_SDO_INDEX_INFO info \n"
                    + " inner join mdsys.user_sdo_index_metadata meta \n"
                    + " on info.index_name = meta.sdo_index_name \n" //
                    + "where info.table_name = '" + tableName + "' \n" //
                    + "and info.column_name = '" + columnName + "'";
            String schema = config.getDatabaseSchemaName();
            if(schema != null && !"".equals(schema)) {
                sqlStatement += " and info.table_owner = '" + schema + "'";
            }
            conn = getConnection(Transaction.AUTO_COMMIT);
            LOGGER.finer("the sql statement for geometry type check is " + sqlStatement);
            statement = conn.createStatement();
            result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                String gType = result.getString(1);
                Class geometryClass = (Class) TT.GEOM_CLASSES.get(gType);
                if(geometryClass == null)
                    geometryClass = Geometry.class;

                return geometryClass;
            } else {
                return Geometry.class;
            }
        } catch (SQLException sqle) {
            String message = sqle.getMessage();
            LOGGER.fine("Could not determine geometry class due to an error_: "  + sqle);

            throw new DataSourceException(message, sqle);
        } finally {
            JDBCUtils.close(result);
            JDBCUtils.close(statement);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }

    }

    protected CoordinateReferenceSystem determineCRS(int srid) throws IOException {
        try {
            return getOracleAuthorityFactory().createCRS(srid);
        } catch(FactoryException e) {
            return null;
        }
    }
        
	/**
     * @see org.geotools.data.jdbc.JDBCDataStore#determineSRID(java.lang.String, java.lang.String)
     */
    protected int determineSRID(String tableName, String geometryColumnName) throws IOException {
        Connection conn = null;        
	 try {
            String sqlStatement = "SELECT SRID FROM MDSYS.ALL_SDO_GEOM_METADATA "
                + "WHERE TABLE_NAME='" + tableName + "' AND COLUMN_NAME='"
                + geometryColumnName + "'";
            String schema = config.getDatabaseSchemaName();
            if(schema != null && !"".equals(schema)) {
                sqlStatement += " and OWNER = '" + schema + "'";
            }
            conn = getConnection(Transaction.AUTO_COMMIT);
            LOGGER.finer("the sql statement for srid is " + sqlStatement);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                int retSrid = result.getInt("srid");
                JDBCUtils.close(statement);

                return retSrid;
            } else {
                String mesg = "No geometry column row for srid in table: "
                    + tableName + ", geometry column " + geometryColumnName +
                    ", be sure column is defined in USER_SDO_GEOM_METADATA";
                throw new DataSourceException(mesg);
            }
        } catch (SQLException sqle) {
            String message = sqle.getMessage();

            throw new DataSourceException(message, sqle);
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);            
        }        
    }
    
    private OracleAuthorityFactory getOracleAuthorityFactory() {
        if (af == null) {
            af = new OracleAuthorityFactory(dataSource);
        }

        return af;
    }
    
    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getSqlBuilder(java.lang.String)
     */
    public SQLBuilder getSqlBuilder(String typeName) throws IOException {
    	FeatureTypeInfo info = typeHandler.getFeatureTypeInfo(typeName);
        SQLEncoder encoder = new SQLEncoderOracle(info.getSRIDs());
        encoder.setFIDMapper(getFIDMapper(typeName));
        return new DefaultSQLBuilder(encoder, info.getSchema(), null);
    }
    
    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getGeometryAttributeIO(org.geotools.feature.AttributeType, org.geotools.data.jdbc.QueryData)
     */
    protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type, QueryData queryData) throws IOException {
	return new SDOAttributeIO(type, queryData);
    }
    
    /**
     * Returns a Oracle text based feature writer that just issues the sql
     * statements directly, as text.  Jody and Sean say things will go faster 
     * if we use updatable resultsets and all that jazz, but I can't get
     * those to work, and this does, so I'm going forth with it.
     *
     * @task TODO: Comment out this method and try out the default JDBC
     *             FeatureWriter - Jody thinks it will go faster.  It will
     *             need to be debugged, however, as it would not work.
     */ 
    protected JDBCFeatureWriter createFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> fReader,
        QueryData queryData) throws IOException {
        return new OracleFeatureWriter(fReader, queryData);
    }
    
    /**
     * Retrieve approx bounds of all Features.
     * <p>
     * This result is suitable for a quick map display, illustrating the data.
     * This value is often stored as metadata in databases such as oraclespatial.
     * </p>
     * @return null as a generic implementation is not provided.
     */

    public Envelope getEnvelope(String typeName) {
        try {
            return bounds(new DefaultQuery(typeName));
        } catch(IOException e) {
            LOGGER.log(Level.WARNING, "Could not compute feature type bounds", e);
            return null;
        }
    }
    
    /** This is used by helper classes to hammer sql back to the database */
    public boolean sql( Transaction t, String sql ) throws IOException, SQLException  {
    	Connection conn = getConnection( t );
    	Statement st = conn.createStatement();
    	LOGGER.info( sql );
    	return st.execute( sql );    	
    }
    
    public void createSchema(SimpleFeatureType featureType) throws IOException {
    	String tableName = featureType.getTypeName();
    	Transaction t = new DefaultTransaction("createSchema");
    	
    	//CoordinateReferenceSystem crs = featureType.getDefaultGeometry().getCoordinateSystem();
    	// TODO: lookup srid for crs
    	Envelope bounds = new Envelope();
		bounds.expandToInclude( -180, -90 );
		bounds.expandToInclude( 180, 90 );
		int srid = -1;
		
    	SQLEncoderOracle encoder = new SQLEncoderOracle("fid",srid); // should figure out from encoding
    	SqlStatementEncoder statements = new SqlStatementEncoder( encoder, tableName, "fid" );
    	
    	try {
			sql( t, "DROP TABLE "+tableName );
			sql( t, "DELETE FROM user_sdo_geom_metadata WHERE TABLE_NAME='"+tableName+"'");			
    	}
    	catch( SQLException ignore ){
    		// table probably did not exist
    	}
    	try{
    		sql( t, statements.makeCreateTableSQL( featureType ) );
    		
  
    		sql( t, statements.makeAddGeomMetadata( featureType, bounds, srid ) );
    		//sql( t, statements.makeCreateFidIndex() );
    		//sql( t, statements.makeCreateGeomIndex( featureType ) );
    		
    		t.commit();
    		
    	} catch (SQLException e) {
    		t.rollback();
    		throw (IOException) new IOException( e.getLocalizedMessage() ).initCause( e );
		}    	    	
    	finally {
    		t.close();
    	}
    }
    
    /**
     * Default implementation based on getFeatureReader and getFeatureWriter.
     *
     * <p>
     * We should be able to optimize this to only get the RowSet once
     * </p>
     *
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        if (!typeHandler.getFIDMapper(typeName).isVolatile()
                || allowWriteOnVolatileFIDs) {
            if (getLockingManager() != null) {
                // Use default JDBCFeatureLocking that delegates all locking
                // the getLockingManager
                //
                return new OracleFeatureLocking(this, getSchema(typeName));
            } else {
                // subclass should provide a FeatureLocking implementation
                // but for now we will simply forgo all locking
                return new OracleFeatureStore(this, getSchema(typeName));
            }
        } else {
            return new OracleFeatureSource(this, getSchema(typeName));
        }
    }
    
    /**
     * This is (unfortunately) a copy and paste from PostgisFeatureStore, I simply did not
     * know a better place to put this...
     * @param query
     * @return
     * @throws IOException
     */
    protected ReferencedEnvelope bounds(Query query) throws IOException {
        Filter filter = query.getFilter();

        if (filter == Filter.EXCLUDE) {
            return new ReferencedEnvelope(new Envelope(), query.getCoordinateSystem());
        }

        SimpleFeatureType schema = getSchema(query.getTypeName());
        SQLBuilder sqlBuilder = getSqlBuilder(schema.getTypeName());

        Filter postQueryFilter = sqlBuilder.getPostQueryFilter(query.getFilter());
        if (postQueryFilter != null && !postQueryFilter.equals(Filter.INCLUDE)) {
            // this would require postprocessing the filter
            // so we cannot optimize
            return null;
        }

        Connection conn = null;
        try {
            conn = getConnection(Transaction.AUTO_COMMIT);

            Envelope retEnv = new Envelope();
            Filter preFilter = sqlBuilder.getPreQueryFilter(query.getFilter());
            //AttributeType[] attributeTypes = schema.getAttributeTypes();
            SimpleFeatureType schemaNew = schema;
            if (!query.retrieveAllProperties()) {
                try {
                    schemaNew = DataUtilities.createSubType(schema, query.getPropertyNames());
                    if (schemaNew.getGeometryDescriptor() == null) // does the sub-schema have a
                                                                // geometry in it?
                    {
                        // uh-oh better get one!
                        if (schema.getGeometryDescriptor() != null) // does the entire schema have a
                                                                    // geometry in it?
                        {
                            // buff-up the sub-schema so it has the default geometry in it.
                            ArrayList al = new ArrayList(Arrays.asList(query.getPropertyNames()));
                            al.add(schema.getGeometryDescriptor().getName());
                            schemaNew = DataUtilities.createSubType(schema, (String[]) al
                                    .toArray(new String[1]));
                        }
                    }
                } catch (SchemaException e1) {
                    throw new DataSourceException("Could not create subtype", e1);
                }
            }
            // at this point, the query should have a geometry in it.
            // BUT, if there's no geometry in the table, then the query will not (obviously) have a
            // geometry in it.

            List<AttributeDescriptor> attributeTypes = schemaNew.getAttributeDescriptors();

            for (int j = 0, n = schemaNew.getAttributeCount(); j < n; j++) {
                if (Geometry.class.isAssignableFrom(attributeTypes.get(j).getType().getBinding())) // same as
                                                                                    // .isgeometry()
                                                                                    // - see new
                                                                                    // featuretype
                                                                                    // javadoc
                {
                    String attName = attributeTypes.get(j).getLocalName();
                    Envelope curEnv = getEnvelope(conn, schemaNew, attName, sqlBuilder, filter);

                    if (curEnv == null) {
                        return null;
                    }

                    retEnv.expandToInclude(curEnv);
                }
            }

            LOGGER.finer("returning bounds " + retEnv);

            if ((schemaNew != null) && (schemaNew.getGeometryDescriptor() != null))
                return new ReferencedEnvelope(retEnv, schemaNew.getGeometryDescriptor()
                        .getCoordinateReferenceSystem());
            if (query.getCoordinateSystem() != null)
                return new ReferencedEnvelope(retEnv, query.getCoordinateSystem());
            return new ReferencedEnvelope(retEnv, null);
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
            conn = null;
            throw new DataSourceException("Could not count " + query.getHandle(), sqlException);
        } catch (SQLEncoderException e) {
            // could not encode count
            // but at least we did not break the connection
            return null;
        } catch (ParseException parseE) {
            String message = "Could not read geometry: " + parseE.getMessage();

            return null;
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }
    
    protected Envelope getEnvelope(
        Connection conn,
        SimpleFeatureType schema,
        String geomName,
        SQLBuilder sqlBuilder,
        Filter filter)
        throws SQLException, SQLEncoderException, IOException, ParseException {
        
        StringBuffer sql = new StringBuffer();
        GeometryDescriptor gat = (GeometryDescriptor) schema.getDescriptor(geomName);
        // from the Oracle docs: "The SDO_TUNE.EXTENT_OF function has better performance than the
        // SDO_AGGR_MBR function if the data is non-geodetic and if a spatial index is defined
        // on the geometry column; however, the SDO_TUNE.EXTENT_OF function is limited to
        // two-dimensional geometries, whereas the SDO_AGGR_MBR function is not".
        // And also: "In addition, the SDO_TUNE.EXTENT_OF function computes the extent for all
        // geometries in a table; by contrast, the SDO_AGGR_MBR function can operate on
        // subsets of rows. The SDO_TUNE.EXTENT_OF function returns NULL if the data is inconsistent."
        // Long story short: under restrictive conditions SDO_TUNE.EXTENT_OF works, but we have
        // to be prepared to fall back on SDO_AGGR_MBR.
        List queries = new ArrayList();
        if(Filter.INCLUDE.equals(filter) && !(gat.getCoordinateReferenceSystem() instanceof GeodeticCRS)) {
            sql.append("SELECT SDO_TUNE.EXTENT_OF('").append(schema.getTypeName()).append("', '");
            sql.append(geomName).append("') from dual");
            queries.add(sql.toString());
            sql = new StringBuffer();
        } 
        sql.append("SELECT SDO_AGGR_MBR(").append(geomName).append(") ");
        sqlBuilder.sqlFrom(sql, schema.getTypeName());
        sqlBuilder.sqlWhere(sql, filter);
        queries.add(sql.toString());
      
        LOGGER.fine("SQL: " + sql);

        // loop over the (eventual) two sql statements, so that if the first does not provide
        // an answer, we fall back on the second
        Statement statement = null;
        ResultSet results = null;
        Envelope result = null;
        for (Iterator it = queries.iterator(); it.hasNext();) {
            String query = (String) it.next();
            try {
                statement = conn.createStatement();
                results = statement.executeQuery(query);
                
                results.next();
                
                Geometry geom = null;
                Object struct = results.getObject(1);
                UnWrapper unwrapper = DataSourceFinder.getUnWrapper(conn);
                OracleConnection oraConn = (OracleConnection) unwrapper.unwrap(conn);
                GeometryConverter converter = new GeometryConverter(oraConn, new GeometryFactory());
                geom = converter.asGeometry( (STRUCT) struct );
                
                // Oracle may return a point, a line or a polygon
                if(geom != null)
                    return geom.getEnvelopeInternal();
            } finally {
                JDBCUtils.close(results);
                JDBCUtils.close(statement);
            }
        }
        return result;
    }   
}
