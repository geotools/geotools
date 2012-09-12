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
package org.geotools.data.sqlserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Dialect implementation for Microsoft SQL Server.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 *
 * @source $URL$
 */
public class SQLServerDialect extends BasicSQLDialect {

    private static final int DEFAULT_AXIS_MAX = 10000000;
    private static final int DEFAULT_AXIS_MIN = -10000000;
    
    /**
     * The direct geometry metadata table
     * @param dataStore
     */
    private String geometryMetadataTable;
    
    final static Map<String, Class> TYPE_TO_CLASS_MAP = new HashMap<String, Class>() {
        {
            put("GEOMETRY", Geometry.class);
            put("GEOGRAPHY", Geometry.class);
            put("POINT", Point.class);
            put("POINTM", Point.class);
            put("LINESTRING", LineString.class);
            put("LINESTRINGM", LineString.class);
            put("POLYGON", Polygon.class);
            put("POLYGONM", Polygon.class);
            put("MULTIPOINT", MultiPoint.class);
            put("MULTIPOINTM", MultiPoint.class);
            put("MULTILINESTRING", MultiLineString.class);
            put("MULTILINESTRINGM", MultiLineString.class);
            put("MULTIPOLYGON", MultiPolygon.class);
            put("MULTIPOLYGONM", MultiPolygon.class);
            put("GEOMETRYCOLLECTION", GeometryCollection.class);
            put("GEOMETRYCOLLECTIONM", GeometryCollection.class);
            put("BYTEA", byte[].class);
        }
    };

    final static Map<Class, String> CLASS_TO_TYPE_MAP = new HashMap<Class, String>() {
        {
            put(Geometry.class, "GEOMETRY");
            put(Point.class, "POINT");
            put(LineString.class, "LINESTRING");
            put(Polygon.class, "POLYGON");
            put(MultiPoint.class, "MULTIPOINT");
            put(MultiLineString.class, "MULTILINESTRING");
            put(MultiPolygon.class, "MULTIPOLYGON");
            put(GeometryCollection.class, "GEOMETRYCOLLECTION");
            put(byte[].class, "BYTEA");
        }
    };
    
    

    public SQLServerDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }
    
    @Override
    public boolean includeTable(String schemaName, String tableName,
            Connection cx) throws SQLException {
        return !("INFORMATION_SCHEMA".equals( schemaName ) || "sys".equals( schemaName ) );
    }
    
    @Override
    public String getGeometryTypeName(Integer type) {
        return "geometry";
    }
    
    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);
        
        //override since sql server maps all date times to timestamp
        mappings.put( Date.class, Types.TIMESTAMP );
        mappings.put( Time.class, Types.TIMESTAMP );
    }
    
    @Override
    public void registerSqlTypeNameToClassMappings(
            Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        
        
        mappings.put( "geometry", Geometry.class );
        mappings.put( "uniqueidentifier", UUID.class );
    }
    
    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(
            Map<Integer, String> overrides) {
        super.registerSqlTypeToSqlTypeNameOverrides(overrides);
        
        //force varchar, if not it will default to nvarchar which won't support length restrictions
        overrides.put( Types.VARCHAR, "varchar");
    }
    
    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        
    	schemaName = schemaName != null ? schemaName : "public"; 
        String tableName = featureType.getName().getLocalPart();
        
        Statement st = null;
        try {
            st = cx.createStatement();

            // register all geometry columns in the database
            for (AttributeDescriptor att : featureType
                    .getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    GeometryDescriptor gd = (GeometryDescriptor) att;

                    // lookup or reverse engineer the srid
                    int srid = -1;
                    if (gd.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID) != null) {
                        srid = (Integer) gd.getUserData().get(
                                JDBCDataStore.JDBC_NATIVE_SRID);
                    } else if (gd.getCoordinateReferenceSystem() != null) {
                        try {
                            Integer result = CRS.lookupEpsgCode(gd
                                    .getCoordinateReferenceSystem(), true);
                            if (result != null)
                                srid = result;
                        } catch (Exception e) {
                            LOGGER.log(Level.FINE, "Error looking up the "
                                    + "epsg code for metadata "
                                    + "insertion, assuming -1", e);
                        }
                    }

                    // assume 2 dimensions, but ease future customisation
                    int dimensions = 2;

                    // grab the geometry type
                    String geomType = CLASS_TO_TYPE_MAP.get(gd.getType()
                            .getBinding());
                    if (geomType == null)
                        geomType = "GEOMETRY";

                    String sql = null;
                    
                    // register the geometry type, first remove and eventual
                    // leftover, then write out the real one
                    sql = 
                    "DELETE FROM GEOMETRY_COLUMNS"
                            //+ " WHERE f_table_catalog=''" //
                            + " WHERE f_table_schema = '" + schemaName + "'" //
                            + " AND f_table_name = '" + tableName + "'" // 
                            + " AND f_geometry_column = '" + gd.getLocalName() + "'";
                    
                    LOGGER.fine( sql );
                    st.execute( sql );
                    
                    sql = "INSERT INTO GEOMETRY_COLUMNS VALUES (" //
                            + "'" + schemaName + "'," //
                            + "'" + tableName + "'," //
                            + "'" + gd.getLocalName() + "'," //
                            + dimensions + "," //
                            + srid + "," //
                            + "'" + geomType + "')";
                    LOGGER.fine( sql );
                    st.execute( sql );
                    
                    
                    String bbox = null;
                  
                  //get the crs, and derive a bounds
                  //TODO: stop being lame and properly figure out the dimension and bounds, see 
                  // oracle dialect for the proper way to do it
                  if (gd.getCoordinateReferenceSystem() != null) { 
                      CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
                      CoordinateSystem cs = crs.getCoordinateSystem();
                      if (cs.getDimension() == 2) {
                          CoordinateSystemAxis a0 = cs.getAxis(0);
                          CoordinateSystemAxis a1 = cs.getAxis(1);
                          bbox = "(";
                          bbox += (Double.isInfinite(a0.getMinimumValue()) ? 
                              DEFAULT_AXIS_MIN : a0.getMinimumValue()) + ", ";
                          bbox += (Double.isInfinite(a1.getMinimumValue()) ?
                              DEFAULT_AXIS_MIN : a1.getMinimumValue()) + ", ";

                          bbox += (Double.isInfinite(a0.getMaximumValue()) ? 
                              DEFAULT_AXIS_MAX : a0.getMaximumValue()) + ", ";
                          bbox += Double.isInfinite(a1.getMaximumValue()) ?
                              DEFAULT_AXIS_MAX : a1.getMaximumValue();
                          bbox += ")";
                      }
                  }
                  
                  if (bbox == null) {
                      //no crs or could not figure out bounds
                      continue;
                  }
                    
                    // add the spatial index
					StringBuffer sqlBufffer = new StringBuffer("CREATE SPATIAL INDEX ");
					encodeTableName(featureType.getTypeName() + "_" + gd.getLocalName() + "_index", sqlBufffer);
					sqlBufffer.append(" ON ");
					encodeTableName(featureType.getTypeName(), sqlBufffer);
					sqlBufffer.append("(");
					encodeColumnName(null, gd.getLocalName(), sqlBufffer);
					sqlBufffer.append(")");
					sqlBufffer.append(" WITH ( BOUNDING_BOX = ").append(bbox).append(")");

					LOGGER.fine(sql.toString());
                }
            }
            if (!cx.getAutoCommit()) {
                cx.commit();
            }
         }
	     finally {
	    	 dataStore.closeSafe(st);
	     }
    	
//        Statement st = cx.createStatement();
//        try {
//            //create spatial indexes for all geometry columns
//            for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
//                if (ad instanceof GeometryDescriptor) {
//                    String bbox = null;
//                    GeometryDescriptor gd = (GeometryDescriptor) ad;
//                    
//                    //get the crs, and derive a bounds
//                    //TODO: stop being lame and properly figure out the dimension and bounds, see 
//                    // oracle dialect for the proper way to do it
//                    if (gd.getCoordinateReferenceSystem() != null) { 
//                        CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
//                        CoordinateSystem cs = crs.getCoordinateSystem();
//                        if (cs.getDimension() == 2) {
//                            CoordinateSystemAxis a0 = cs.getAxis(0);
//                            CoordinateSystemAxis a1 = cs.getAxis(1);
//                            bbox = "(";
//                            bbox += (Double.isInfinite(a0.getMinimumValue()) ? 
//                                DEFAULT_AXIS_MIN : a0.getMinimumValue()) + ", ";
//                            bbox += (Double.isInfinite(a1.getMinimumValue()) ?
//                                DEFAULT_AXIS_MIN : a1.getMinimumValue()) + ", ";
//
//                            bbox += (Double.isInfinite(a0.getMaximumValue()) ? 
//                                DEFAULT_AXIS_MAX : a0.getMaximumValue()) + ", ";
//                            bbox += Double.isInfinite(a1.getMaximumValue()) ?
//                                DEFAULT_AXIS_MAX : a1.getMaximumValue();
//                            bbox += ")";
//                        }
//                    }
//                    
//                    if (bbox == null) {
//                        //no crs or could not figure out bounds
//                        continue;
//                    }
//                    StringBuffer sql = new StringBuffer("CREATE SPATIAL INDEX ");
//                    encodeTableName(featureType.getTypeName()+"_"+gd.getLocalName()+"_index", sql);
//                    sql.append( " ON ");
//                    encodeTableName(featureType.getTypeName(), sql);
//                    sql.append("(");
//                    encodeColumnName(null, gd.getLocalName(), sql);
//                    sql.append(")");
//                    sql.append( " WITH ( BOUNDING_BOX = ").append(bbox).append(")");
//                    
//                    LOGGER.fine(sql.toString());
//                    st.execute(sql.toString());
//                }
//            }
//        }
//        finally {
//            dataStore.closeSafe(st);
//        }
    }
    
    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
    	
    	String gType = null;
    	String typeName = columnMetaData.getString("TYPE_NAME");
    	 if ("geometry".equalsIgnoreCase(typeName)) {
             gType = lookupGeometryType(columnMetaData, cx, "geometry_columns", "f_geometry_column");
    	 }
    	 
         // decode the type into
         if(gType == null) {
             // it's either a generic geography or geometry not registered in the medatata tables
             return Geometry.class;
         } else {
             Class geometryClass = (Class) TYPE_TO_CLASS_MAP.get(gType.toUpperCase());
             if (geometryClass == null) {
                 geometryClass = Geometry.class;
             }
     
             return geometryClass;
         }
    }
    
    String lookupGeometryType(ResultSet columnMetaData, Connection cx, String gTableName, 
            String gColumnName) throws SQLException {
        
        // grab the information we need to proceed
        String tableName = columnMetaData.getString("TABLE_NAME");
        String columnName = columnMetaData.getString("COLUMN_NAME");
        String schemaName = columnMetaData.getString("TABLE_SCHEM");

        // first attempt, try with the geometry metadata
        Connection conn = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            String sqlStatement = "SELECT TYPE FROM " + gTableName + " WHERE " //
//                    + "F_TABLE_SCHEMA = '" + schemaName + "' " //
                    /*+ "AND*/ + "F_TABLE_NAME = '" + tableName + "' " //
                    + "AND " + gColumnName + " = '" + columnName + "'";

            LOGGER.log(Level.FINE, "Geometry type check; {0} ", sqlStatement);
            statement = cx.createStatement();
            result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                return result.getString(1);
            }
        } finally {
            dataStore.closeSafe(result);
            dataStore.closeSafe(statement);
        }

        return null;
    }
    
    
    @Override
    public Integer getGeometrySRID(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {
        
        StringBuffer sql = new StringBuffer("SELECT TOP 1 ");
        encodeColumnName(null, columnName, sql);
        sql.append( ".STSrid");
        
        sql.append( " FROM ");
        encodeTableName(schemaName, tableName, sql, true);
        
        sql.append( " WHERE ");
        encodeColumnName(null, columnName, sql );
        sql.append( " IS NOT NULL");
        
        dataStore.getLogger().fine( sql.toString() );
        
        Statement st = cx.createStatement();
        try {
            
            ResultSet rs = st.executeQuery( sql.toString() );
            try {
                if ( rs.next() ) {
                    return rs.getInt( 1 );
                }
                // the default sql server srid
                return 0;
            }
            finally {
                dataStore.closeSafe( rs );
            }
        }
        finally {
            dataStore.closeSafe( st );
        }
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix,
            int srid, Hints hints, StringBuffer sql) {
        encodeColumnName( prefix, gatt.getLocalName(), sql );
        sql.append( ".STAsBinary()");
    }

    @Override
    public void encodeGeometryValue(Geometry value, int srid, StringBuffer sql)
            throws IOException {
        
        if ( value == null ) {
            sql.append( "NULL");
            return;
        }
        
        sql.append( "geometry::STGeomFromText('").append( value.toText() ).append( "',").append( srid ).append(")");
    }
    
    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
            ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
       byte[] bytes = rs.getBytes(column);
       if(bytes == null) {
           return null;
       }
       try {
           return new WKBReader(factory).read(bytes);
       } catch ( ParseException e ) {
           throw (IOException) new IOException().initCause( e );
       }
    }
    
    Geometry decodeGeometry( String s, GeometryFactory factory ) throws IOException {
        if ( s == null ) {
            return null;
        }
        if ( factory == null ) {
            factory = new GeometryFactory();
        }
        
        String[] split = s.split( ":" );
        
        String  srid = split[0];
        
        Geometry g = null;
        try {
            g = new WKTReader(factory).read( split[1] );
        }
        catch ( ParseException e ) {
            throw (IOException) new IOException().initCause( e );
        }
        
        CoordinateReferenceSystem crs;
        try {
            crs = CRS.decode( "EPSG:" + srid );
        } 
        catch (Exception e ) {
            throw (IOException) new IOException().initCause( e );
        }
        
        g.setUserData( crs );
        return g;
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn,
            StringBuffer sql) {
        sql.append( "CAST(");
        encodeColumnName( null, geometryColumn, sql );
        sql.append( ".STSrid as VARCHAR)");
        
        sql.append( " + ':' + " );
        
        encodeColumnName( null, geometryColumn, sql );
        sql.append( ".STEnvelope().ToString()");
    }
    
    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
            Connection cx) throws SQLException, IOException {
        String s = rs.getString( column );
        Geometry g = decodeGeometry( s, null );
        if ( g == null ) {
            return null;
        }
        
        return new ReferencedEnvelope( g.getEnvelopeInternal(), (CoordinateReferenceSystem) g.getUserData() );
    }
    
    @Override
    public Object getNextAutoGeneratedValue(String schemaName,
            String tableName, String columnName, Connection cx)
            throws SQLException {
        
        StringBuffer sql = new StringBuffer("SELECT");
        sql.append( " IDENT_CURRENT('");
        encodeTableName(schemaName, tableName, sql, false);
        sql.append("')");
        sql.append( " + ");
        sql.append( " IDENT_INCR('");
        encodeTableName(schemaName, tableName, sql, false);
        sql.append("')");
        
        dataStore.getLogger().fine( sql.toString() );
        
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery( sql.toString() );
            try {
                rs.next();
                return rs.getInt( 1 );
            }
            finally {
                dataStore.closeSafe( rs );
            }
        }
        finally {
            dataStore.closeSafe( st );
        }
         
    }
    
    @Override
    public FilterToSQL createFilterToSQL() {
        return new SQLServerFilterToSQL();
    }
    
    protected void encodeTableName(String schemaName, String tableName, StringBuffer sql, boolean escape) {
        if (schemaName != null) {
            if (escape) {
                encodeSchemaName(schemaName, sql);
            }
            else {
                sql.append(schemaName);
            }
            sql.append(".");
        }
        if (escape) {
            encodeTableName(tableName, sql);
        }
        else {
            sql.append(tableName);
        }
    }
    
    @Override
    public boolean isLimitOffsetSupported() {
        return true;
    }
    
    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        // if we have a nested query (used in sql views) we might have a inner order by,
        // check for the last closed )
        int lastClosed = sql.lastIndexOf(")");
        int orderByIndex = sql.lastIndexOf("ORDER BY");
        CharSequence orderBy;
        if(orderByIndex > 0 && orderByIndex > lastClosed) {
            // we'll move the order by into the ROW_NUMBER call
            orderBy = sql.subSequence(orderByIndex, sql.length());
            sql.delete(orderByIndex, orderByIndex + orderBy.length());
        } else {
            // ROW_NUMBER requires an order by clause, we need to feed it something
            orderBy = "ORDER BY CURRENT_TIMESTAMP";
        }
        
        // now insert the order by inside the select
        int fromStart = sql.indexOf("FROM");
        sql.insert(fromStart - 1, ", ROW_NUMBER() OVER (" + orderBy + ") AS _GT_ROW_NUMBER ");
        
        // and wrap inside a block that selects the portion we want
        sql.insert(0, "SELECT * FROM (");
        sql.append(") AS _GT_PAGING_SUBQUERY WHERE ");
        if(offset > 0) {
            sql.append("_GT_ROW_NUMBER > " + offset);
        }
        if(limit >= 0 && limit < Integer.MAX_VALUE) {
            int max = limit;
            if(offset > 0) {
                max += offset;
                sql.append(" AND ");
            }
            sql.append("_GT_ROW_NUMBER <= " + max);
        }
    }
    
    @Override
    public void encodeValue(Object value, Class type, StringBuffer sql) {
        if(byte[].class.equals(type)) {
            byte[] b = (byte[]) value;
            
            //encode as hex string
            sql.append("0x");
            for (int i=0; i < b.length; i++) {
                sql.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
            }
        } else {
            super.encodeValue(value, type, sql);
        }
    }
    
    /**
     * The geometry metadata table in use, if any
     * @return
     */
    public String getGeometryMetadataTable() {
        return geometryMetadataTable;
    }

    /**
     * Sets the geometry metadata table
     * @param geometryMetadataTable
     */
    public void setGeometryMetadataTable(String geometryMetadataTable) {
        this.geometryMetadataTable = geometryMetadataTable;
    }
    
}
