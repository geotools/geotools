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
package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.FactoryException;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class TeradataGISDialect extends BasicSQLDialect {
    private boolean looseBBOXEnabled;
    private boolean estimatedExtentsEnabled;

    protected TeradataGISDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

    public void setEstimatedExtentsEnabled(boolean estimatedExtentsEnabled) {
        this.estimatedExtentsEnabled = estimatedExtentsEnabled;
    }

    final static Map<String, Class<?>> TYPE_TO_CLASS = new HashMap<String, Class<?>>() {
        {
            put("GEOMETRY", Geometry.class);
            put("POINT", Point.class);
            put("LINESTRING", LineString.class);
            put("POLYGON", Polygon.class);
            ;
            put("MULTIPOINT", MultiPoint.class);
            put("MULTILINESTRING", MultiLineString.class);
            put("MULTIPOLYGON", MultiPolygon.class);
            put("GEOMETRYCOLLECTION", GeometryCollection.class);
            put("GEOSEQUENCE", Geometry.class);
        }
    };

    final static Map<Class<?>, String> CLASS_TO_TYPE = new HashMap<Class<?>, String>() {
        {
            put(Geometry.class, "GEOMETRY");
            put(Point.class, "POINT");
            put(LineString.class, "LINESTRING");
            put(Polygon.class, "POLYGON");
            put(MultiPoint.class, "MULTIPOINT");
            put(MultiLineString.class, "MULTILINESTRING");
            put(MultiPolygon.class, "MULTIPOLYGON");
            put(GeometryCollection.class, "GEOMETRYCOLLECTION");
        }
    };

    @Override
    public boolean includeTable(String schemaName, String tableName,
                                Connection cx) throws SQLException {
        if (tableName.equalsIgnoreCase("geometry_columns")) {
            return false;
        } else if (tableName.toLowerCase().startsWith("spatial_ref_sys")) {
            return false;
        } else if (tableName.equalsIgnoreCase("geography_columns")) {
            return false;
        }

        // others?
        return dataStore.getDatabaseSchema() == null || dataStore.getDatabaseSchema().equals(schemaName);
    }

    ThreadLocal<WKBAttributeIO> wkbReader = new ThreadLocal<WKBAttributeIO>();

    //    WKBAttributeIO reader;
    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
                                        ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        WKBAttributeIO reader = getWkbReader(factory);

        Geometry g = (Geometry) reader.read(rs, column);
        return g;
    }

    private WKBAttributeIO getWkbReader(GeometryFactory factory) {
        WKBAttributeIO reader = wkbReader.get();
        if (reader == null) {
            GeometryFactory geometryFactory = factory == null ? dataStore.getGeometryFactory() : factory;
            reader = new WKBAttributeIO(geometryFactory);
            wkbReader.set(reader);
        }
        return reader;
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, int srid,
                                     StringBuffer sql) {
        encodeColumnName(gatt.getLocalName(), sql);
        sql.append(".ST_AsBinary()");
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn,
                                       StringBuffer sql) {
        encodeColumnName(geometryColumn, sql);
        sql.append(".ST_Envelope().ST_AsBinary()");
    }

    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema, SimpleFeatureType featureType,
                                                       Connection cx) throws SQLException, IOException {
        return null;
/*        String tableName = featureType.getTypeName();

        Statement st = null;
        ResultSet rs = null;

        List<ReferencedEnvelope> result = new ArrayList<ReferencedEnvelope>();
        try {
            st = cx.createStatement();

            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    // use estimated extent (optimizer statistics)
                    StringBuffer sql = new StringBuffer();
                    sql.append("select SYSSPATIAL.AggGeomUnion(");
                    encodeColumnName(att.getName().getLocalPart(), sql);
                    sql.append(".ST_Envelope()) from ");
                    if(schema != null) {
                        encodeColumnName(tableName, sql);
                        sql.append(".");
                    }
                    encodeColumnName(tableName, sql);
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
            LOGGER.log(Level.WARNING, "Failed to use ST_Estimated_Extent, falling back on envelope aggregation", e);
            return null;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }
        return result;*/
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
                                           Connection cx) throws SQLException, IOException {
        Geometry envelope = getWkbReader(null).read(rs, column);
        if (envelope != null) {
            return envelope.getEnvelopeInternal();
        } else {
            // empty one
            return new Envelope();
        }
/*        try {

            if (envelope != null)
                return new WKTReader().read(envelope).getEnvelopeInternal();
            else
                // empty one
                return new Envelope();
        } catch (ParseException e) {
            throw (IOException) new IOException(
                    "Error occurred parsing the bounds WKT").initCause(e);
        }*/
        /*byte[] bytes = rs.getBytes(column);
        if (bytes != null && bytes.length > 0) {
            WKBAttributeIO reader = getWkbReader(null);
            return reader.wkb2Geometry(bytes).getEnvelopeInternal();
        }
        else
            // empty one
            return new Envelope();*/
    }

    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
        String typeName = columnMetaData.getString("TYPE_NAME");
        String gType = null;
        if ("SYSUDTLIB.ST_GEOMETRY".equalsIgnoreCase(typeName)) {
            gType = lookupGeometryType(columnMetaData, cx, "SYSSPATIAL.GEOMETRY_COLUMNS", "F_GEOMETRY_COLUMN");
        } else if ("SYSUDTLIB.ST_GEOGRAPHY".equalsIgnoreCase(typeName)) {
            gType = lookupGeometryType(columnMetaData, cx, "SYSSPATIAL.GEOGRAPHY_COLUMNS", "G_GEOGRAPHY_COLUMN");
        } else {
            return null;
        }

        // decode the type into
        if (gType == null) {
            // it's either a generic geography or geometry not registered in the medatata tables
            return Geometry.class;
        } else {
            Class<?> geometryClass = TYPE_TO_CLASS.get(gType.toUpperCase());
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
        Statement statement = null;
        ResultSet result = null;

        try {
            String sqlStatement = "SELECT \"GEOM_TYPE\" FROM " + gTableName + " WHERE "
                    + "F_TABLE_SCHEMA = '" + schemaName + "' "
                    + "AND F_TABLE_NAME = '" + tableName + "' "
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

        // first attempt, try with the geometry metadata
        Statement statement = null;
        ResultSet result = null;
        Integer srid = null;
        try {
            if (schemaName == null)
                schemaName = "public";

            String sqlStatement = "SELECT ref.AUTH_SRID FROM SYSSPATIAL.GEOMETRY_COLUMNS as col, SYSSPATIAL.spatial_ref_sys as ref WHERE "
                    + "col.F_TABLE_SCHEMA = '" + schemaName + "' "
                    + "AND col.F_TABLE_NAME = '" + tableName + "' "
                    + "AND col.F_GEOMETRY_COLUMN = '" + columnName + "' "
                    + "AND col.SRID = ref.SRID";

            LOGGER.log(Level.FINE, "Geometry srid check; {0} ", sqlStatement);
            statement = cx.createStatement();
            result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                srid = result.getInt(1);
            } else {
                dataStore.closeSafe(result);
                sqlStatement = "SELECT ref.AUTH_SRID FROM SYSSPATIAL.GEOMETRY_COLUMNS as col, SYSSPATIAL.spatial_ref_sys as ref WHERE "
                        + "col.F_TABLE_NAME = '" + tableName + "' "
                        + "AND col.F_GEOMETRY_COLUMN = '" + columnName + "' "
                        + "AND col.SRID = ref.SRID";
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
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);

        // jdbc metadata for geom columns reports DATA_TYPE=1111=Types.OTHER
        mappings.put(Geometry.class, Types.OTHER);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(
            Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put("ST_Geometry", Geometry.class);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(
            Map<Integer, String> overrides) {
        overrides.put(Types.VARCHAR, "VARCHAR");
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return "ST_Geometry";
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(column, sql);
        sql.append(" PRIMARY KEY not null generated always as identity (start with 0) integer");
    }

    public void encodeColumnType(String sqlTypeName, StringBuffer sql) {
        if (sqlTypeName.toUpperCase().startsWith("VARCHAR")) {
            sql.append(sqlTypeName);
            sql.append("casespecific");
        } else {
            sql.append(sqlTypeName);
        }
    }

    /**
     * Creates GEOMETRY_COLUMN registrations and spatial indexes for all
     * geometry columns
     */
    @Override
    public void postCreateTable(String schemaName,
                                SimpleFeatureType featureType, Connection cx) throws SQLException {
        schemaName = schemaName != null ? schemaName : "";
        String tableName = featureType.getName().getLocalPart();
        Statement st = null;
        try {
            st = cx.createStatement();

            // register all geometry columns in the database
            for (AttributeDescriptor att : featureType
                    .getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    GeometryDescriptor gd = (GeometryDescriptor) att;
                    int srid = -1;
                    try {
                        Integer result = CRS.lookupEpsgCode(gd.getCoordinateReferenceSystem(), true);
                        if (result != null) {
                            String sql = "select srid from SYSSPATIAL.spatial_ref_sys" +
                                    " where AUTH_SRID = " + result;
                            LOGGER.fine(sql);
                            ResultSet resultSet = st.executeQuery(sql);
                            resultSet.next();
                            srid = resultSet.getInt("srid");
                        }
                    } catch (FactoryException e) {
                        e.printStackTrace();
                    }

                    // grab the geometry type
                    String geomType = CLASS_TO_TYPE.get(gd.getType()
                            .getBinding());
                    if (geomType == null)
                        geomType = "GEOMETRY";

                    String sql = "INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', "
                            + "'" + schemaName + "', "
                            + "'" + tableName + "', "
                            + "'" + gd.getLocalName() + "', "
                            + "2, "
                            + srid + ", "
                            + "'" + geomType + ")";
                    LOGGER.fine(sql);
                    st.execute(sql);

                    // add the spatial index
/*                    sql =
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
                    st.execute(sql);*/
                }
                cx.commit();
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    public void encodeGeometryValue(Geometry value, int srid, StringBuffer sql)
            throws IOException {
        if (value == null) {
            sql.append("NULL");
        } else {
            sql.append("'" + value.toText() + "'");
        }
    }

    @Override
    public FilterToSQL createFilterToSQL() {
        TeradataFilterToSQL sql = new TeradataFilterToSQL(this);
        return sql;
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return false;
    }

    @Override
    public Object getLastAutoGeneratedValue(String schemaName, String tableName, String columnName, Connection cx) throws SQLException {
        Statement stmt = cx.createStatement();
        try {
            StringBuffer sql = new StringBuffer("SELECT TOP 1 ");
            encodeColumnName(columnName, sql);
            sql.append(" FROM ");
            if (schemaName != null) {
                encodeColumnName(schemaName, sql);
                sql.append(".");
            }
            encodeColumnName(tableName, sql);
            sql.append(" ORDER BY ");
            encodeColumnName(columnName, sql);
            sql.append(" DESC");

            ResultSet resultSet = stmt.executeQuery(sql.toString());
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new IllegalStateException("Unable to determine next value in autogenerated sequence.  SQL was: " + sql);
            }
        } finally {
            stmt.close();
        }
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return true;
    }
}
