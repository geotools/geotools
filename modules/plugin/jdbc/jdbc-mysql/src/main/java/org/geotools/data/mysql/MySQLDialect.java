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
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.Geometries;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

/**
 * Delegate for {@link MySQLDialectBasic} and {@link MySQLDialectPrepared} which implements the common part of the api.
 *
 * @author Justin Deoliveira, OpenGEO
 */
public class MySQLDialect extends SQLDialect {
    /** mysql spatial types */
    protected Integer POINT = Integer.valueOf(2001);

    protected Integer LINESTRING = Integer.valueOf(2002);
    protected Integer POLYGON = Integer.valueOf(2003);
    protected Integer MULTIPOINT = Integer.valueOf(2004);
    protected Integer MULTILINESTRING = Integer.valueOf(2005);
    protected Integer MULTIPOLYGON = Integer.valueOf(2006);
    protected Integer GEOMETRY = Integer.valueOf(2007);

    /** the storage engine to use when creating tables, one of MyISAM, InnoDB */
    protected String storageEngine;

    /**
     * flag that indicates that precise spatial operation should be used (should apply to MySQL versions 5.6 and above)
     */
    protected boolean usePreciseSpatialOps;

    /** flag indicating we are using MySQL v8.0 or higher. */
    protected boolean isMySqlVersion80OrAbove;

    public MySQLDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    public void setStorageEngine(String storageEngine) {
        this.storageEngine = storageEngine;
    }

    public String getStorageEngine() {
        return storageEngine;
    }

    public void setUsePreciseSpatialOps(boolean usePreciseSpatialOps) {
        this.usePreciseSpatialOps = usePreciseSpatialOps;
    }

    public boolean getUsePreciseSpatialOps() {
        return usePreciseSpatialOps;
    }

    public boolean isMySqlVersion80OrAbove() {
        return this.isMySqlVersion80OrAbove;
    }

    public void setMySqlVersion80OrAbove(boolean mySqlVersion80OrAbove) {
        this.isMySqlVersion80OrAbove = mySqlVersion80OrAbove;
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx) throws SQLException {
        if ("geometry_columns".equalsIgnoreCase(tableName)) {
            return false;
        }
        return super.includeTable(schemaName, tableName, cx);
    }

    @Override
    public String getNameEscape() {
        return usePreciseSpatialOps ? "`" : "";
    }

    @Override
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

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {

        // first check the geometry_columns table
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        encodeColumnName(null, "srid", sql);
        sql.append(" FROM ");
        encodeTableName("geometry_columns", sql);
        sql.append(" WHERE ");

        encodeColumnName(null, "f_table_schema", sql);

        if (schemaName != null) {
            sql.append(" = '").append(schemaName).append("'");
        } else {
            sql.append(" IS NULL");
        }
        sql.append(" AND ");

        encodeColumnName(null, "f_table_name", sql);
        sql.append(" = '").append(tableName).append("' AND ");

        encodeColumnName(null, "f_geometry_column", sql);
        sql.append(" = '").append(columnName).append("'");

        dataStore.getLogger().fine(sql.toString());

        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql.toString());
            try {
                if (rs.next()) {
                    return Integer.valueOf(rs.getInt(1));
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } catch (SQLException e) {
            // geometry_columns does not exist
        } finally {
            dataStore.closeSafe(st);
        }

        // execute SELECT srid(<columnName>) FROM <tableName> LIMIT 1;
        sql = new StringBuffer();
        if (this.usePreciseSpatialOps) {
            sql.append("SELECT ST_SRID(");
        } else {
            sql.append("SELECT srid(");
        }
        encodeColumnName(null, columnName, sql);
        sql.append(") ");
        sql.append("FROM ");

        if (schemaName != null) {
            encodeTableName(schemaName, sql);
            sql.append(".");
        }

        encodeSchemaName(tableName, sql);
        sql.append(" WHERE ");
        encodeColumnName(null, columnName, sql);
        sql.append(" is not null LIMIT 1");

        dataStore.getLogger().fine(sql.toString());

        st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql.toString());

            try {
                if (rs.next()) {
                    return Integer.valueOf(rs.getInt(1));
                } else {
                    // could not find out
                    return null;
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        if (this.usePreciseSpatialOps) {
            sql.append("ST_asWKB(");
        } else {
            sql.append("asWKB(");
        }
        encodeColumnName(prefix, gatt.getLocalName(), sql);
        sql.append(")");
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        if (this.usePreciseSpatialOps) {
            if (this.isMySqlVersion80OrAbove) {
                // since mysql 8 fails to execute st_envelope on geography we need to
                // work around that casting to srid:0 and back. Example:
                //
                //            SELECT ST_asWkB(
                //                -- restore srid
                //                ST_srid(
                //                    -- get envelope
                //                    ST_Envelope(
                //                            -- cast to cartesian/srid 0
                //                            ST_srid(geom, 0)
                //                    ),
                //                    ST_SRID(geom)
                //                )
                //            ) FROM `road`
                sql.append("ST_asWKB(ST_SRID(ST_Envelope(ST_SRID(");
                encodeColumnName(null, geometryColumn, sql);
                sql.append(",0)),ST_SRID(");
                encodeColumnName(null, geometryColumn, sql);
                sql.append(")");
            } else {
                // 5.6/5.7 has a different syntax for ST_SRID so we can't use the above
                // also it doesn't care about projections
                sql.append("ST_asWKB(ST_Envelope(");
                encodeColumnName(null, geometryColumn, sql);
            }
        } else {
            sql.append("asWKB(");
            sql.append("envelope(");
            encodeColumnName(null, geometryColumn, sql);
        }
        sql.append("))");
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        byte[] wkb = rs.getBytes(column);

        try {
            /**
             * As of MySQL 5.7.6, if the argument is a point or a vertical or horizontal line segment, ST_Envelope()
             * returns the point or the line segment as its MBR rather than returning an invalid polygon therefore we
             * must override behavior and check for a geometry and not a polygon
             */
            // TODO: srid
            Geometry geom = new WKBReader().read(wkb);

            return geom.getEnvelopeInternal();
        } catch (ParseException e) {
            String msg = "Error decoding wkb for envelope";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String name,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        byte[] bytes = rs.getBytes(name);
        if (bytes == null) {
            return null;
        }
        try {
            return new WKBReader(factory).read(bytes);
        } catch (ParseException e) {
            String msg = "Error decoding wkb";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    @Override
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

    @Override
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

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put("POINT", Point.class);
        mappings.put("LINESTRING", LineString.class);
        mappings.put("POLYGON", Polygon.class);
        mappings.put("MULTIPOINT", MultiPoint.class);
        mappings.put("MULTILINESTRING", MultiLineString.class);
        mappings.put("MULTIPOLYGON", MultiPolygon.class);
        mappings.put("GEOMETRY", Geometry.class);
        mappings.put("GEOMETRYCOLLECTION", GeometryCollection.class);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        overrides.put(Types.BOOLEAN, "BOOL");
        overrides.put(Types.CLOB, "TEXT");
    }

    @Override
    public void encodePostCreateTable(String tableName, StringBuffer sql) {
        // TODO: make this configurable
        sql.append("ENGINE=" + storageEngine);
    }

    @Override
    public void encodePostColumnCreateTable(AttributeDescriptor att, StringBuffer sql) {
        // make geometry columns non null in order to be able to index them
        if (att instanceof GeometryDescriptor && !att.isNillable()) {
            if (!sql.toString().trim().endsWith(" NOT NULL")) {
                sql.append(" NOT NULL");
            }
        }
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {

        // create the geometry_columns table if necessary
        DatabaseMetaData md = cx.getMetaData();
        ResultSet rs = md.getTables(
                null,
                dataStore.escapeNamePattern(md, schemaName),
                dataStore.escapeNamePattern(md, "geometry_columns"),
                new String[] {"TABLE"});
        try {
            if (!rs.next()) {
                // create it
                Statement st = cx.createStatement();
                try {
                    StringBuffer sql = new StringBuffer("CREATE TABLE ");
                    encodeTableName("geometry_columns", sql);
                    sql.append("(");
                    encodeColumnName(null, "f_table_schema", sql);
                    sql.append(" varchar(255), ");
                    encodeColumnName(null, "f_table_name", sql);
                    sql.append(" varchar(255), ");
                    encodeColumnName(null, "f_geometry_column", sql);
                    sql.append(" varchar(255), ");
                    encodeColumnName(null, "coord_dimension", sql);
                    sql.append(" int, ");
                    encodeColumnName(null, "srid", sql);
                    sql.append(" int, ");
                    encodeColumnName(null, "type", sql);
                    sql.append(" varchar(32)");
                    sql.append(")");

                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(sql.toString());
                    }
                    st.execute(sql.toString());
                } finally {
                    dataStore.closeSafe(st);
                }
            }
        } finally {
            dataStore.closeSafe(rs);
        }

        // create spatial index for all geometry columns
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            if (!(ad instanceof GeometryDescriptor)) {
                continue;
            }
            GeometryDescriptor gd = (GeometryDescriptor) ad;

            if (!ad.isNillable()) {
                // can only index non null columns
                StringBuffer sql = new StringBuffer("ALTER TABLE ");
                encodeTableName(featureType.getTypeName(), sql);
                sql.append(" ADD SPATIAL INDEX (");
                encodeColumnName(null, gd.getLocalName(), sql);
                sql.append(")");

                LOGGER.fine(sql.toString());
                Statement st = cx.createStatement();
                try {
                    st.execute(sql.toString());
                } finally {
                    dataStore.closeSafe(st);
                }
            }

            CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
            int srid = 0;
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
            sql.append(" (");
            encodeColumnName(null, "f_table_schema", sql);
            sql.append(", ");
            encodeColumnName(null, "f_table_name", sql);
            sql.append(", ");
            encodeColumnName(null, "f_geometry_column", sql);
            sql.append(", ");
            encodeColumnName(null, "coord_dimension", sql);
            sql.append(", ");
            encodeColumnName(null, "srid", sql);
            sql.append(", ");
            encodeColumnName(null, "type", sql);
            sql.append(") ");
            sql.append(" VALUES (");
            sql.append(schemaName != null ? "'" + schemaName + "'" : "NULL").append(", ");
            sql.append("'").append(featureType.getTypeName()).append("', ");
            sql.append("'").append(ad.getLocalName()).append("', ");
            sql.append("2, ");
            sql.append(srid).append(", ");

            @SuppressWarnings("unchecked")
            Class<? extends Geometry> gc =
                    (Class<? extends Geometry>) gd.getType().getBinding();
            Geometries g = Geometries.getForBinding(gc);
            sql.append("'")
                    .append(g != null ? g.getName().toUpperCase() : "GEOMETRY")
                    .append("')");

            LOGGER.fine(sql.toString());
            Statement st = cx.createStatement();
            try {
                st.execute(sql.toString());
            } finally {
                dataStore.closeSafe(st);
            }
        }
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(null, column, sql);
        sql.append(" int AUTO_INCREMENT PRIMARY KEY");
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return true;
    }

    @Override
    public Object getLastAutoGeneratedValue(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        Statement st = cx.createStatement();
        try {
            String sql = "SELECT last_insert_id()";
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
    public boolean isLimitOffsetSupported() {
        return true;
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        if (limit >= 0 && limit < Integer.MAX_VALUE) {
            if (offset > 0) sql.append(" LIMIT " + offset + ", " + limit);
            else sql.append(" LIMIT " + limit);
        } else if (offset > 0) {
            // MySql pretends to have limit specified along with offset
            sql.append(" LIMIT " + offset + ", " + Long.MAX_VALUE);
        }
    }

    @Override
    public void dropIndex(Connection cx, SimpleFeatureType schema, String databaseSchema, String indexName)
            throws SQLException {
        StringBuffer sql = new StringBuffer();
        String escape = getNameEscape();
        sql.append("DROP INDEX ");
        if (databaseSchema != null) {
            encodeSchemaName(databaseSchema, sql);
            sql.append(".");
        }
        // weirdness, index naems are treated as strings...
        sql.append(escape).append(indexName).append(escape);
        sql.append(" on ");
        if (databaseSchema != null) {
            encodeSchemaName(databaseSchema, sql);
            sql.append(".");
        }
        encodeTableName(schema.getTypeName(), sql);

        Statement st = null;
        try {
            st = cx.createStatement();
            st.execute(sql.toString());
            if (!cx.getAutoCommit()) {
                cx.commit();
            }
        } finally {
            dataStore.closeSafe(st);
            dataStore.closeSafe(cx);
        }
    }

    @Override
    public boolean canGroupOnGeometry() {
        return true;
    }
}
