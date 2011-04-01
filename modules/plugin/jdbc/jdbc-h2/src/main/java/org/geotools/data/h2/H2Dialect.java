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
package org.geotools.data.h2;

import geodb.GeoDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.geometry.jts.Geometries;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Delegate for {@link H2DialectBasic} and {@link H2DialectPrepared} which implements
 * the common parts of the dialect api.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 * @source $URL$
 */
public class H2Dialect extends SQLDialect {
    
    public static String H2_SPATIAL_INDEX = "org.geotools.data.h2.spatialIndex";
    
    public H2Dialect( JDBCDataStore dataStore ) {
        super( dataStore );
    }
    
    public String getNameEscape() {
        return "\"";
    }

    
    @Override
    public void initializeConnection(Connection cx) throws SQLException {
        //spatialize the database
        GeoDB.InitGeoDB(cx);
    }
    
    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        if ("_GEODB".equals(tableName) || tableName.endsWith("_HATBOX")) {
            return false;
        }
        if ("GEOMETRY_COLUMNS".equalsIgnoreCase(tableName)) {
            return false;
        }

        return true;
    }
    
    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
        
        String typeName = columnMetaData.getString("TYPE_NAME");
        if ("BLOB".equalsIgnoreCase(typeName)) {
            String schemaName = columnMetaData.getString("TABLE_SCHEM");
            String tableName = columnMetaData.getString("TABLE_NAME");
            String columnName = columnMetaData.getString("COLUMN_NAME");
            
            //look up in geometry columns table
            StringBuffer sql = new StringBuffer("SELECT type FROM geometry_columns WHERE ");
            if (schemaName != null) {
                sql.append("f_table_schema = '").append(schemaName).append("'").append(" AND ");
            }
            sql.append("f_table_name = '").append(tableName).append("' AND ");
            sql.append("f_geometry_column = '").append(columnName).append("'");
            
            Statement st = cx.createStatement();
            try {
                LOGGER.fine(sql.toString());
                ResultSet rs = st.executeQuery(sql.toString());
                try {
                    if (rs.next()) {
                        String type = rs.getString(1);
                        Geometries g = Geometries.getForName(type);
                        if (g != null) {
                            return g.getBinding();
                        }
                        LOGGER.warning("Geometry type " + type + " not supported.");
                    }
                }
                finally {
                    dataStore.closeSafe(rs);
                }
            }
            finally {
                dataStore.closeSafe(st);
            }
        }
        
        //do a check for a column remark which marks this as a geometry
        // do this mostly for backwards compatability
        String remark = columnMetaData.getString( "REMARKS" );
        if ( remark != null ) {
            Geometries g = Geometries.getForName(remark);
            if (g != null) {
                return g.getBinding();
            }
        }
        
        return null;

    }
    
    @Override
    public void encodePostColumnCreateTable(AttributeDescriptor att,
            StringBuffer sql) {
        if ( att instanceof GeometryDescriptor ) {
            //try to narrow down the type with a comment
            Class binding = att.getType().getBinding();
            if (isConcreteGeometry(binding)) {
                sql.append( " COMMENT '").append( binding.getSimpleName().toUpperCase() )
                    .append( "'");
            }
        }
    }
    
    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);
        mappings.put(Geometry.class, Types.BLOB);
    }
    
    @Override
    public void postCreateTable(String schemaName,
            SimpleFeatureType featureType, Connection cx) throws SQLException {
        
        Statement st = cx.createStatement();
        String tableName = featureType.getTypeName();
        
        try {
            //post process the feature type and set up constraints based on geometry type
            for ( PropertyDescriptor ad : featureType.getDescriptors() ) {
                if ( ad instanceof GeometryDescriptor ) {
                    GeometryDescriptor gd = (GeometryDescriptor)ad;
                    Class binding = ad.getType().getBinding();
                    String propertyName = ad.getName().getLocalPart();
                  
                    //create a spatial index
                    int epsg = -1;
                    try {
                        CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
                        if (crs == null) {
                            continue;
                        }
                        
                        Integer code = CRS.lookupEpsgCode(crs, true); 
                        epsg = code != null ? code : -1;
                    } 
                    catch (FactoryException e) {
                        LOGGER.log(Level.FINER, "Unable to look epsg code", e);
                    }

                    StringBuffer sql = new StringBuffer();
                    sql.append("CALL AddGeometryColumn(");
                    if (schemaName != null) {
                        sql.append("'").append(schemaName).append("'");
                    }
                    else {
                        sql.append("NULL");
                    }
                    sql.append(", '").append(tableName).append("'");
                    sql.append(", '").append(gd.getLocalName()).append("'");
                    sql.append(", ").append(epsg);
                    sql.append(", '").append(Geometries.getForBinding(binding).getName()).append("'");
                    sql.append(", ").append(2); //TODO: dimension
                    sql.append(")");
                    
                    LOGGER.fine(sql.toString());
                    st.execute(sql.toString());
                    
                    if (epsg != -1) {
                        sql = new StringBuffer();
                        sql.append("CALL CreateSpatialIndex(");
                        if (schemaName == null) {
                            sql.append("NULL");
                        }
                        else {
                            sql.append("'").append(schemaName).append("'");
                        }
                       
                        sql.append(",'").append(tableName).append("'");
                        sql.append(",'").append(propertyName).append("'");
                        sql.append(",'").append(epsg).append("')");
                        
                        LOGGER.fine(sql.toString());
                        st.execute(sql.toString());
                    }
                }
            }
        }
        finally {
            dataStore.closeSafe( st );
        }
    }
    
    @Override
    public void postCreateFeatureType(SimpleFeatureType featureType, DatabaseMetaData metadata,
            String schemaName, Connection cx) throws SQLException {
        //figure out if the table has a spatial index and mark the feature type as so
        if (featureType.getGeometryDescriptor() == null) {
            return;
        }
        String idxTableName = featureType.getTypeName() + "_HATBOX";
        ResultSet rs = metadata.getTables(null, schemaName, idxTableName, new String[]{"TABLE"});
        try {
            if (rs.next()) {
                featureType.getGeometryDescriptor().getUserData().put(H2_SPATIAL_INDEX, idxTableName);
            }
        }
        finally {
            dataStore.closeSafe(rs);
        }
    }
    
    boolean isConcreteGeometry( Class binding ) {
        return Point.class.isAssignableFrom(binding) 
            || LineString.class.isAssignableFrom(binding)
            || Polygon.class.isAssignableFrom(binding) 
            || MultiPoint.class.isAssignableFrom( binding ) 
            || MultiLineString.class.isAssignableFrom(binding) 
            || MultiPolygon.class.isAssignableFrom( binding );
    }
    
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName,
        Connection cx) throws SQLException {
        
        //first try getting from table metadata
        int srid = GeoDB.GetSRID(cx, schemaName, tableName);
        if (srid > -1) {
            return srid;
        }
        
        //try grabbing directly from a geometry
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ST_SRID(");
        encodeColumnName(columnName, sql);
        sql.append(") ");
        sql.append("FROM ");

        if (schemaName != null) {
            encodeTableName(schemaName, sql);
            sql.append(".");
        }

        encodeSchemaName(tableName, sql);
        sql.append(" WHERE ");
        encodeColumnName(columnName, sql);
        sql.append(" is not null LIMIT 1");

        dataStore.getLogger().fine(sql.toString());
        Statement st = cx.createStatement();

        try {
            ResultSet rs = st.executeQuery(sql.toString());

            try {
                if (rs.next()) {
                    return new Integer(rs.getInt(1));
                } else {
                    //could not find o
           return null;
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        //TODO: change spatialdbbox to use envelope
        sql.append("ST_Envelope(");
        encodeColumnName(geometryColumn, sql);
        sql.append(")");
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
            Connection cx) throws SQLException, IOException {
        
        //TODO: change spatialdb in a box to return ReferencedEnvelope
        return (Envelope) rs.getObject(column);
    }

    public void encodeGeometryValue(Geometry value, int srid, StringBuffer sql)
        throws IOException {
        sql.append("ST_GeomFromText ('");
        sql.append(new WKTWriter().write(value));
        sql.append("',");
        sql.append(srid);
        sql.append(")");
    }
    
    

    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, String column,
        GeometryFactory factory, Connection cx ) throws IOException, SQLException {
        byte[] bytes = rs.getBytes(column);

        if (bytes == null) {
            return null;
        }

        try {
            return new WKBReader(factory).read(bytes);
        } catch (ParseException e) {
            throw (IOException) new IOException().initCause(e);
        }

        //return JTS.geometryFromBytes( bytes );
    }

    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(column, sql);
        sql.append(" int AUTO_INCREMENT(1) PRIMARY KEY");
    }

    @Override
    public String getSequenceForColumn(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {
        
        String sequenceName = tableName + "_" + columnName + "_SEQUENCE"; 
        
        //sequence names have to be upper case to select values from them
        sequenceName = sequenceName.toUpperCase();
        Statement st = cx.createStatement();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append( "SELECT * FROM INFORMATION_SCHEMA.SEQUENCES ");
            sql.append( "WHERE SEQUENCE_NAME = '").append( sequenceName ).append( "'" );
            
            dataStore.getLogger().fine( sql.toString() );
            ResultSet rs = st.executeQuery( sql.toString() );
            try {
                if ( rs.next() ) {
                    return sequenceName;
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        }
        finally {
            dataStore.closeSafe(st);
        }
        
        return null;
    }
    
    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName,
            Connection cx) throws SQLException {
        
        Statement st = cx.createStatement();
        try {
            String sql = "SELECT NEXTVAL('" + sequenceName + "')";
            dataStore.getLogger().fine( sql );
            ResultSet rs = st.executeQuery( sql );
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
    public Object getNextAutoGeneratedValue(String schemaName,
            String tableName, String columnName, Connection cx)
            throws SQLException {
        
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery("SELECT b.COLUMN_DEFAULT "
                    + " FROM INFORMATION_SCHEMA.INDEXES A, INFORMATION_SCHEMA.COLUMNS B "
                    + "WHERE a.TABLE_NAME = b.TABLE_NAME " + " AND a.COLUMN_NAME = b.COLUMN_NAME "
                    + " AND a.TABLE_NAME = '" + tableName + "' " + " AND a.COLUMN_NAME = '"
                    + columnName + "' " + " AND a.PRIMARY_KEY = TRUE");

            //figure out which sequence to query
            String sequence = null;

            try {
                //TODO: there has to be a better way to do this
                rs.next();

                String string = rs.getString(1);
                sequence = string.substring(string.indexOf("SYSTEM_SEQUENCE"), string.length() - 1);
            } finally {
                dataStore.closeSafe(rs);
            }

            try {
                if (schemaName != null) {
                    rs = st.executeQuery("SELECT CURRVAL('" + schemaName + "','" + sequence + "')");
                } else {
                    rs = st.executeQuery("SELECT CURRVAL('" + sequence + "')");
                }

                rs.next();

                int value = rs.getInt(1);

                return new Integer(value + 1);
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }
    
    @Override
    public boolean isLimitOffsetSupported() {
        return true;
    }
    
    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        if(limit >= 0 && limit < Integer.MAX_VALUE) {
            sql.append(" LIMIT " + limit);
            if(offset > 0) {
                sql.append(" OFFSET " + offset);
            }
        } else if(offset > 0) {
            // H2 pretends to have limit specified along with offset
            sql.append(" LIMIT " + Integer.MAX_VALUE);
            sql.append(" OFFSET " + offset);
        }
    }
}
