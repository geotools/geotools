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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.ColumnMetadata;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.geotools.util.Version;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class PostGISDialect extends BasicSQLDialect {

    final static Map<String, Class> TYPE_TO_CLASS_MAP = new HashMap<String, Class>() {
        {
            put("GEOMETRY", Geometry.class);
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
    
    static final Version V_1_5_0 = new Version("1.5.0");

    public PostGISDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    boolean looseBBOXEnabled = false;

    boolean estimatedExtentsEnabled = false;
    
    Version version;

    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }
    
        
    public boolean isEstimatedExtentsEnabled() {
        return estimatedExtentsEnabled;
    }

    public void setEstimatedExtentsEnabled(boolean estimatedExtentsEnabled) {
        this.estimatedExtentsEnabled = estimatedExtentsEnabled;
    }

    @Override
    public boolean includeTable(String schemaName, String tableName,
            Connection cx) throws SQLException {
        if (tableName.equals("geometry_columns")) {
            return false;
        } else if (tableName.startsWith("spatial_ref_sys")) {
            return false;
        } else if (tableName.equals("geography_columns")) {
            return false;
        }

        // others?
        return true;
    }

    ThreadLocal<WKBAttributeIO> wkbReader = new ThreadLocal<WKBAttributeIO>();
//    WKBAttributeIO reader;
    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
            ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        WKBAttributeIO reader = wkbReader.get();
        if(reader == null) {
            reader = new WKBAttributeIO(factory);
            wkbReader.set(reader);
        }
        
        return (Geometry) reader.read(rs, column);
    }
    
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
            ResultSet rs, int column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        WKBAttributeIO reader = wkbReader.get();
        if(reader == null) {
            reader = new WKBAttributeIO(factory);
            wkbReader.set(reader);
        }
        
        return (Geometry) reader.read(rs, column);
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, int srid,
            StringBuffer sql) {
        
        boolean geography = "geography".equals(gatt.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME));
        
        sql.append("encode(");
        sql.append("ST_AsBinary(");
        if (!geography) {
            sql.append("ST_Force_2D(");
        }
        
        encodeColumnName(gatt.getLocalName(), sql);
        if (!geography) {
            sql.append(")");
        }
        sql.append("),'base64')");
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn,
            StringBuffer sql) {
        sql.append("ST_AsText(ST_Force_2D(Envelope(");
        sql.append("ST_Extent(\"" + geometryColumn + "\"::geometry))))");
    }
    
    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema, SimpleFeatureType featureType,
            Connection cx) throws SQLException, IOException {
        if (!estimatedExtentsEnabled)
            return null;

        String tableName = featureType.getTypeName();

        Statement st = null;
        ResultSet rs = null;

        List<ReferencedEnvelope> result = new ArrayList<ReferencedEnvelope>();
        Savepoint savePoint = null;
        try {
            st = cx.createStatement();
            if(!cx.getAutoCommit()) {
                savePoint = cx.setSavepoint();
            }

            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    // use estimated extent (optimizer statistics)
                    StringBuffer sql = new StringBuffer();
                    sql.append("select AsText(force_2d(Envelope(ST_Estimated_Extent('");
                    if(schema != null) {
                        sql.append(schema);
                        sql.append("', '");
                    }
                    sql.append(tableName);
                    sql.append("', '");
                    sql.append(att.getName().getLocalPart());
                    sql.append("'))))");
                    rs = st.executeQuery(sql.toString());

                    if (rs.next()) {
                        // decode the geometry
                        Envelope env = decodeGeometryEnvelope(rs, 1, cx);

                        // reproject and merge
                        if (!env.isNull()) {
                            CoordinateReferenceSystem crs = ((GeometryDescriptor) att)
                                    .getCoordinateReferenceSystem();
                            result.add(new ReferencedEnvelope(env, crs));
                        }
                    }
                    rs.close();
                }
            }
        } catch(SQLException e) {
            if(savePoint != null) {
                cx.rollback(savePoint);
            }
            LOGGER.log(Level.WARNING, "Failed to use ST_Estimated_Extent, falling back on envelope aggregation", e);
            return null;
        } finally {
            if(savePoint != null) {
                cx.releaseSavepoint(savePoint);
            }
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        } 
        return result;
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
            Connection cx) throws SQLException, IOException {
        try {
            String envelope = rs.getString(column);
            if (envelope != null)
                return new WKTReader().read(envelope).getEnvelopeInternal();
            else
                // empty one
                return new Envelope();
        } catch (ParseException e) {
            throw (IOException) new IOException(
                    "Error occurred parsing the bounds WKT").initCause(e);
        }
    }

    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
        
        String typeName = columnMetaData.getString("TYPE_NAME");
        String gType = null;
        if ("geometry".equalsIgnoreCase(typeName)) {
            gType = lookupGeometryType(columnMetaData, cx, "geometry_columns", "f_geometry_column");
        } else if ("geography".equalsIgnoreCase(typeName)) {
            gType = lookupGeometryType(columnMetaData, cx, "geography_columns", "f_geography_column");
        } else {
            return null;
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
                    + "F_TABLE_SCHEMA = '" + schemaName + "' " //
                    + "AND F_TABLE_NAME = '" + tableName + "' " //
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
    public void handleUserDefinedType(ResultSet columnMetaData, ColumnMetadata metadata,
            Connection cx) throws SQLException {

        String tableName = columnMetaData.getString("TABLE_NAME");
        String columnName = columnMetaData.getString("COLUMN_NAME");
        String schemaName = columnMetaData.getString("TABLE_SCHEM");
        
        String sql = "SELECT udt_name FROM information_schema.columns " + 
        " WHERE table_schema = '"+schemaName+"' " + 
        "   AND table_name = '"+tableName+"' " + 
        "   AND column_name = '"+columnName+"' ";
        LOGGER.fine(sql);
        
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql);
            try {
                if (rs.next()) {
                    metadata.setTypeName(rs.getString(1));
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
    
    @Override
    public Integer getGeometrySRID(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {

        // first attempt, try with the geometry metadata
        Statement statement = null;
        ResultSet result = null;
        Integer srid = null;
        try {
            if (schemaName == null)
                schemaName = "public";
            
            // try geography_columns
            if(supportsGeography(cx)) {
                try {
                    //first look for an entry in geography_columns, if there return 4326
                    String sqlStatement = "SELECT SRID FROM GEOGRAPHY_COLUMNS WHERE " //
                        + "F_TABLE_SCHEMA = '" + schemaName + "' " //
                        + "AND F_TABLE_NAME = '" + tableName + "' " //
                        + "AND F_GEOGRAPHY_COLUMN = '" + columnName + "'";
                    LOGGER.log(Level.FINE, "Geography srid check; {0} ", sqlStatement);
                    statement = cx.createStatement();
                    result = statement.executeQuery(sqlStatement);
        
                    if (result.next()) {
                        return 4326;
                    }
                } catch(SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to retrieve information about " 
                            + schemaName + "." + tableName + "."  + columnName 
                            + " from the geometry_columns table, checking geometry_columns instead", e);
                } finally {
                    dataStore.closeSafe(result);
                }
            }
            
            // try geometry_columns
            try {
                String sqlStatement = "SELECT SRID FROM GEOMETRY_COLUMNS WHERE " //
                        + "F_TABLE_SCHEMA = '" + schemaName + "' " //
                        + "AND F_TABLE_NAME = '" + tableName + "' " //
                        + "AND F_GEOMETRY_COLUMN = '" + columnName + "'";
    
                LOGGER.log(Level.FINE, "Geometry srid check; {0} ", sqlStatement);
                statement = cx.createStatement();
                result = statement.executeQuery(sqlStatement);
    
                if (result.next()) {
                    srid = result.getInt(1);
                }
            } catch(SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to retrieve information about " 
                        + schemaName + "." + tableName + "."  + columnName 
                        + " from the geometry_columns table, checking the first geometry instead", e);
            } finally {
                dataStore.closeSafe(result);
            }
            
            // fall back on inspection of the first geometry, assuming uniform srid (fair assumption
            // an unpredictable srid makes the table un-queriable)
            if(srid == null) {
                String sqlStatement = "SELECT SRID(\"" + columnName + "\") " +
                               "FROM \"" + schemaName + "\".\"" + tableName + "\" " +
                               "LIMIT 1";
                result = statement.executeQuery(sqlStatement);
                if (result.next()) {
                    srid = result.getInt(1);
                }
            }
        } finally {
            dataStore.closeSafe(result);
            dataStore.closeSafe(statement);
        }

        return srid;
    }

    @Override
    public String getSequenceForColumn(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {
        Statement st = cx.createStatement();
        try {
            // pg_get_serial_sequence oddity: table name needs to be
            // escaped with "", whilst column name, doesn't...
            String sql = "SELECT pg_get_serial_sequence('\"";
            if(schemaName != null && !"".equals(schemaName))
                sql += schemaName + "\".\"";
            sql += tableName + "\"', '" + columnName + "')";

            dataStore.getLogger().fine(sql);
            ResultSet rs = st.executeQuery(sql);
            try {
                if (rs.next()) {
                    return rs.getString(1);
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }

        return null;
    }

    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName,
            Connection cx) throws SQLException {
        Statement st = cx.createStatement();
        try {
            String sql = "SELECT nextval('" + sequenceName + "')";

            dataStore.getLogger().fine(sql);
            ResultSet rs = st.executeQuery(sql);
            try {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }

        return null;
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
            String sql = "SELECT lastval()";
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
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);

        // jdbc metadata for geom columns reports DATA_TYPE=1111=Types.OTHER
        mappings.put(Geometry.class, Types.OTHER);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(
            Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put("geometry", Geometry.class);
        mappings.put("text", String.class);
    }
    
    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(
            Map<Integer, String> overrides) {
        overrides.put(Types.VARCHAR, "VARCHAR");
        overrides.put(Types.BOOLEAN, "BOOL");
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return "geometry";
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(column, sql);
        sql.append(" SERIAL PRIMARY KEY");
    }

    /**
     * Creates GEOMETRY_COLUMN registrations and spatial indexes for all
     * geometry columns
     */
    @Override
    public void postCreateTable(String schemaName,
            SimpleFeatureType featureType, Connection cx) throws SQLException {
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

                    // register the geometry type, first remove and eventual
                    // leftover, then write out the real one
                    String sql = 
                    "DELETE FROM GEOMETRY_COLUMNS"
                            + " WHERE f_table_catalog=''" //
                            + " AND f_table_schema = '" + schemaName + "'" //
                            + " AND f_table_name = '" + tableName + "'" // 
                            + " AND f_geometry_column = '" + gd.getLocalName() + "'";
                    
                    LOGGER.fine( sql );
                    st.execute( sql );
                    
                    sql = "INSERT INTO GEOMETRY_COLUMNS VALUES (''," //
                            + "'" + schemaName + "'," //
                            + "'" + tableName + "'," //
                            + "'" + gd.getLocalName() + "'," //
                            + dimensions + "," //
                            + srid + "," //
                            + "'" + geomType + "')";
                    LOGGER.fine( sql );
                    st.execute( sql );

                    // add srid checks
                    if (srid > -1) {
                        sql = "ALTER TABLE " //
                                + "\"" + schemaName + "\"" // 
                                + "." //
                                + "\"" + tableName + "\"" //
                                + " ADD CONSTRAINT \"enforce_srid_" // 
                                + gd.getLocalName() + "\""// 
                                + " CHECK (SRID(" //
                                + "\"" + gd.getLocalName() + "\"" //
                                + ") = " + srid + ")";
                        LOGGER.fine( sql );
                        st.execute(sql);
                    }

                    // add dimension checks
                    sql = "ALTER TABLE " //
                            + "\"" + schemaName + "\"" // 
                            + "." //
                            + "\"" + tableName + "\"" //
                            + " ADD CONSTRAINT \"enforce_dims_" // 
                            + gd.getLocalName() + "\""// 
                            + " CHECK (ndims(\"" + gd.getLocalName() + "\")" //
                            + " = 2)";
                    LOGGER.fine(sql);
                    st.execute(sql);

                    // add geometry type checks
                    if (!geomType.equals("GEOMETRY")) {
                        sql = "ALTER TABLE " //
                                + "\"" + schemaName + "\"" // 
                                + "." //
                                + "\"" + tableName + "\"" //
                                + " ADD CONSTRAINT \"enforce_geotype_" //
                                + gd.getLocalName() + "\""//
                                + " CHECK (geometrytype(" //
                                + "\"" + gd.getLocalName() + "\"" //
                                + ") = '" + geomType + "'::text " + "OR \""
                                + gd.getLocalName() + "\"" //
                                + " IS NULL)";
                        LOGGER.fine(sql);
                        st.execute(sql);
                    }
                    
                    // add the spatial index
                    sql = 
                    "CREATE INDEX \"spatial_" + tableName // 
                            + "_" + gd.getLocalName().toLowerCase() + "\""// 
                            + " ON " //
                            + "\"" + schemaName + "\"" // 
                            + "." //
                            + "\"" + tableName + "\"" //
                            + " USING GIST (" //
                            + "\"" + gd.getLocalName() + "\"" //
                            + ")";
                    LOGGER.fine(sql);
                    st.execute(sql);
                }
            }
            cx.commit();
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    public void encodeGeometryValue(Geometry value, int srid, StringBuffer sql)
            throws IOException {
        if(value == null) {
            sql.append("NULL");
        } else {
            if (value instanceof LinearRing) {
                //postgis does not handle linear rings, convert to just a line string
                value = value.getFactory().createLineString(((LinearRing) value).getCoordinateSequence());
            }
            
            sql.append("GeomFromText('" + value.toText() + "', " + srid + ")");
        }
    }

    @Override
    public FilterToSQL createFilterToSQL() {
        PostgisFilterToSQL sql = new PostgisFilterToSQL(this);
        sql.setLooseBBOXEnabled(looseBBOXEnabled);
        return sql;
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
            sql.append(" OFFSET " + offset);
        }
    }
    
    @Override
    public void encodeValue(Object value, Class type, StringBuffer sql) {
        if(byte[].class.equals(type)) {
            // escape the into bytea representation
            StringBuffer sb = new StringBuffer();
            byte[] input = (byte[]) value;
            for (int i = 0; i < input.length; i++) {
                byte b = input[i];
                if(b == 0) {
                    sb.append("\\\\000");
                } else if(b == 39) {
                    sb.append("\\'");
                } else if(b == 92) {
                    sb.append("\\\\134'");
                } else if(b < 31 || b >= 127) {
                    sb.append("\\\\");
                    String octal = Integer.toOctalString(b);
                    if(octal.length() == 1) {
                        sb.append("00");
                    } else if(octal.length() == 2) {
                        sb.append("0");
                    }
                    sb.append(octal);
                } else {
                    sb.append((char) b);
                }
            }
            super.encodeValue(sb.toString(), String.class, sql);
        } else {
            super.encodeValue(value, type, sql);
        }
    }
    
    @Override
    public int getDefaultVarcharSize(){
        return -1;
    }

    /**
     * Returns the PostGIS version
     * @return
     */
    public Version getVersion(Connection conn) throws SQLException {
        if(version == null) {
            Statement st = null;
            ResultSet rs = null;
            try {
                st = conn.createStatement();
                rs = st.executeQuery("select PostGIS_Lib_Version()");
                if(rs.next()) {
                    version = new Version(rs.getString(1));
                } 
            } finally {
                dataStore.closeSafe(rs);
                dataStore.closeSafe(st);
            }
        }
        
        return version;
    }
    
    /**
     * Returns true if the PostGIS version is >= 1.5.0
     */
    boolean supportsGeography(Connection cx) throws SQLException {
        return getVersion(cx).compareTo(V_1_5_0) >= 0;
    }
    
    }
