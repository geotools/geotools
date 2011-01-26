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

/**
 * Delegate for {@link MySQLDialectBasic} and {@link MySQLDialectPrepared}
 * which implements the common part of the api. 
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 * @source $URL$
 */
public class MySQLDialect extends SQLDialect {
    /**
     * mysql spatial types
     */
    protected Integer POINT = new Integer(2001);
    protected Integer LINESTRING = new Integer(2002);
    protected Integer POLYGON = new Integer(2003);
    protected Integer MULTIPOINT = new Integer(2004);
    protected Integer MULTILINESTRING = new Integer(2005);
    protected Integer MULTIPOLYGON = new Integer(2006);
    protected Integer GEOMETRY = new Integer(2007);

    /**
     * the storage engine to use when creating tables, one of MyISAM, InnoDB
     */
    protected String storageEngine;
    
    
    public MySQLDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    public void setStorageEngine(String storageEngine) {
        this.storageEngine = storageEngine;
    }
    
    public String getStorageEngine() {
        return storageEngine;
    }
    
    public String getNameEscape() {
        return "";
    }

    public String getGeometryTypeName(Integer type) {
        if (POINT.equals(type)) {
            return "POINT";
        }

        if (MULTIPOINT.equals(type)) {
            return "MULTIPOINT";
        }

        if (LINESTRING.equals(type)) {
            return "LINESTRING";
        }

        if (MULTILINESTRING.equals(type)) {
            return "MULTILINESTRING";
        }

        if (POLYGON.equals(type)) {
            return "POLYGON";
        }

        if (MULTIPOLYGON.equals(type)) {
            return "MULTIPOLYGON";
        }

        if (GEOMETRY.equals(type)) {
            return "GEOMETRY";
        }

        return super.getGeometryTypeName(type);
    }

    public Integer getGeometrySRID(String schemaName, String tableName, String columnName,
        Connection cx) throws SQLException {
        
        //first check the geometry_columns table
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        encodeColumnName("srid", sql);
        sql.append(" FROM ");
        encodeTableName("geometry_columns", sql);
        sql.append(" WHERE ");
        
        encodeColumnName("f_table_schema", sql);
        
        if (schemaName != null) {
            sql.append( " = '").append(schemaName).append("'");
        }
        else {
            sql.append(" IS NULL");
        }
        sql.append(" AND ");
        
        encodeColumnName("f_table_name", sql);
        sql.append(" = '").append(tableName).append("' AND ");
        
        encodeColumnName("f_geometry_column", sql);
        sql.append(" = '").append(columnName).append("'");
        
        dataStore.getLogger().fine(sql.toString());
        
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql.toString());
            try {
                if (rs.next()) {
                    return new Integer(rs.getInt(1));
                }
            }
            finally {
                dataStore.closeSafe(rs);
            }
        }
        catch(SQLException e) {
            //geometry_columns does not exist
        }
        finally {
            dataStore.closeSafe(st);
        }
        
        //execute SELECT srid(<columnName>) FROM <tableName> LIMIT 1;
        sql = new StringBuffer();
        sql.append("SELECT srid(");
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

        st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql.toString());

            try {
                if (rs.next()) {
                    return new Integer(rs.getInt(1));
                } else {
                    //could not find out
                    return null;
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    public void encodeGeometryColumn(GeometryDescriptor gatt, int srid, StringBuffer sql) {
        sql.append("asWKB(");
        encodeColumnName(gatt.getLocalName(), sql);
        sql.append(")");
    }

    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        sql.append("asWKB(");
        sql.append("envelope(");
        encodeColumnName(geometryColumn, sql);
        sql.append("))");
    }

    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
                Connection cx) throws SQLException, IOException {
        //String wkb = rs.getString( column );
        byte[] wkb = rs.getBytes(column);

        try {
            //TODO: srid
            Polygon polygon = (Polygon) new WKBReader().read(wkb);

            return polygon.getEnvelopeInternal();
        } catch (ParseException e) {
            String msg = "Error decoding wkb for envelope";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, String name,
        GeometryFactory factory, Connection cx ) throws IOException, SQLException {
        byte[] bytes = rs.getBytes(name);
        if ( bytes == null ) {
            return null;
        }
        try {
            return new WKBReader(factory).read(bytes);
        } catch (ParseException e) {
            String msg = "Error decoding wkb";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);

        mappings.put(Point.class, POINT);
        mappings.put(LineString.class, LINESTRING);
        mappings.put(Polygon.class, POLYGON);
        mappings.put(MultiPoint.class, MULTIPOINT);
        mappings.put(MultiLineString.class, MULTILINESTRING);
        mappings.put(MultiPolygon.class, MULTIPOLYGON);
        mappings.put(Geometry.class, GEOMETRY);
    }

    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        super.registerSqlTypeToClassMappings(mappings);

        mappings.put(POINT, Point.class);
        mappings.put(LINESTRING, LineString.class);
        mappings.put(POLYGON, Polygon.class);
        mappings.put(MULTIPOINT, MultiPoint.class);
        mappings.put(MULTILINESTRING, MultiLineString.class);
        mappings.put(MULTIPOLYGON, MultiPolygon.class);
        mappings.put(GEOMETRY, Geometry.class);
    }

    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put("POINT", Point.class);
        mappings.put("LINESTRING", LineString.class);
        mappings.put("POLYGON", Polygon.class);
        mappings.put("MULTIPOINT", MultiPoint.class);
        mappings.put("MULTILINESTRING", MultiLineString.class);
        mappings.put("MULTIPOLYGON", MultiPolygon.class);
        mappings.put("GEOMETRY", Geometry.class);
        mappings.put("GEOMETRYCOLLETION", GeometryCollection.class);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(
            Map<Integer, String> overrides) {
        overrides.put( Types.BOOLEAN, "BOOL");
    }
    
    public void encodePostCreateTable(String tableName, StringBuffer sql) {
        //TODO: make this configurable
        sql.append("ENGINE="+storageEngine);
    }
    
    @Override
    public void encodePostColumnCreateTable(AttributeDescriptor att, StringBuffer sql) {
        //make geometry columns non null in order to be able to index them
        if (att instanceof GeometryDescriptor && !att.isNillable()) {
            sql.append( " NOT NULL");
        }
    }
    
    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        
        //create teh geometry_columns table if necessary
        DatabaseMetaData md = cx.getMetaData();
        ResultSet rs = md.getTables(null, schemaName, "geometry_columns", new String[]{"TABLE"});
        try {
            if (!rs.next()) {
                //create it
                Statement st = cx.createStatement();
                try {
                    StringBuffer sql = new StringBuffer("CREATE TABLE ");
                    encodeTableName("geometry_columns", sql);
                    sql.append("(");
                    encodeColumnName("f_table_schema", sql); sql.append(" varchar(255), ");
                    encodeColumnName("f_table_name", sql); sql.append(" varchar(255), ");
                    encodeColumnName("f_geometry_column", sql); sql.append(" varchar(255), ");
                    encodeColumnName("coord_dimension", sql); sql.append(" int, ");
                    encodeColumnName("srid", sql); sql.append(" int, ");
                    encodeColumnName("type", sql); sql.append(" varchar(32)");
                    sql.append(")");
                    
                    if (LOGGER.isLoggable(Level.FINE)) { LOGGER.fine(sql.toString()); }
                    st.execute(sql.toString());
                }
                finally {
                    dataStore.closeSafe(st);
                }
            }
        }
        finally {
            dataStore.closeSafe(rs);
        }
        
        //create spatial index for all geometry columns
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            if (!(ad instanceof GeometryDescriptor)) {
                continue;
            }
            GeometryDescriptor gd = (GeometryDescriptor) ad;
            
            if (!ad.isNillable()) {
                //can only index non null columns
                StringBuffer sql = new StringBuffer("ALTER TABLE ");
                encodeTableName(featureType.getTypeName(), sql);
                sql.append(" ADD SPATIAL INDEX (");
                encodeColumnName(gd.getLocalName(), sql);
                sql.append(")");
                
                LOGGER.fine( sql.toString() );
                Statement st = cx.createStatement();
                try {
                    st.execute(sql.toString());
                }
                finally {
                    dataStore.closeSafe(st);
                }
            }
            
            CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
            int srid = -1;
            if (crs != null) {
                Integer i = null;
                try {
                    i = CRS.lookupEpsgCode(crs, true);
                } catch (FactoryException e) {
                    LOGGER.log(Level.FINER, "Could not determine epsg code", e);
                }
                srid = i != null ? i : srid;
            }
            
            StringBuffer sql = new StringBuffer("INSERT INTO ");
            encodeTableName("geometry_columns", sql);
            sql.append(" VALUES (");
            sql.append(schemaName != null ? "'"+schemaName+"'" : "NULL").append(", ");
            sql.append("'").append(featureType.getTypeName()).append("', ");
            sql.append("'").append(ad.getLocalName()).append("', ");
            sql.append("2, ");
            sql.append(srid).append(", ");
            
            
            Geometries g = Geometries.getForBinding((Class<? extends Geometry>) gd.getType().getBinding());
            sql.append("'").append(g != null ? g.getName().toUpperCase() : "GEOMETRY").append("')");
            
            LOGGER.fine( sql.toString() );
            Statement st = cx.createStatement();
            try {
                st.execute(sql.toString());
            }
            finally {
                dataStore.closeSafe(st);
            }
        }
        
    }

    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(column, sql);
        sql.append(" int AUTO_INCREMENT PRIMARY KEY");
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return true;
    }
    
    @Override
    public Object getLastAutoGeneratedValue(String schemaName, String tableName, String columnName,
            Connection cx) throws SQLException {
        Statement st = cx.createStatement();
        try {
            String sql = "SELECT last_insert_id()";
            dataStore.getLogger().fine( sql);
            
            ResultSet rs = st.executeQuery( sql);
            try {
                if ( rs.next() ) {
                    return rs.getLong(1);
                }
            } 
            finally {
                dataStore.closeSafe(rs);
            }
        }
        finally {
            dataStore.closeSafe(st);
        }

        return null;
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return true;
    }
    
    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        if(limit >= 0 && limit < Integer.MAX_VALUE) {
            if(offset > 0)
                sql.append(" LIMIT " + offset + ", " + limit);
            else 
                sql.append(" LIMIT " + limit);
        } else if(offset > 0) {
            // MySql pretends to have limit specified along with offset
            sql.append(" LIMIT " + offset + ", " + Long.MAX_VALUE);
        }
    }

}
