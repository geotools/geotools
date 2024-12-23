/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
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
import org.locationtech.jts.io.WKTWriter;

/**
 * Informix database dialect based on basic (non-prepared) statements.
 *
 * @author George Dewar, Land Information New Zealand
 * @author Ines Falcao, Land Information New Zealand
 */
public class InformixDialect extends BasicSQLDialect {
    public InformixDialect(JDBCDataStore dataStore) {
        super(dataStore);
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
        return "";
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return "st_geometry";
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
        sql.append("SELECT st_srid(");
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
        sql.append("st_asbinary(");
        encodeColumnName(prefix, gatt.getLocalName(), sql);
        sql.append(")");
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        sql.append("ST_AsBinary(");
        sql.append("ST_Envelope(");

        encodeColumnName(null, geometryColumn, sql);
        sql.append("))");
    }

    @Override
    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql) {
        if (value != null) {
            String fromText = fromTextFunctionName(value);
            sql.append(fromText + "('");
            sql.append(new WKTWriter().write(value));
            if (srid == -1) {
                LOGGER.warning("SRID -1 is not supported, using 0");
                srid = 0;
            }
            sql.append("', ").append(srid).append(")");
        } else {
            sql.append("NULL");
        }
    }

    // https://www.cursor-distribution.de/aktuell.12.10.xC6/documentation/ids_spat_bookmap.pdf
    private String fromTextFunctionName(Geometry value) {
        if (value instanceof Point) {
            return "ST_PointFromText";
        } else if (value instanceof LineString) {
            return "ST_LineFromText";
        } else if (value instanceof Polygon) {
            return "ST_PolyFromText";
        } else if (value instanceof MultiPoint) {
            return "ST_MPointFromText";
        } else if (value instanceof MultiLineString) {
            return "ST_MLineFromText";
        } else if (value instanceof MultiPolygon) {
            return "ST_MPolyFromText";
        }
        return "ST_GeomFromText";
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        byte[] wkb = rs.getBytes(column);
        if (wkb == null) return null;

        try {
            Geometry geom = new WKBReader().read(wkb);
            return geom.getEnvelopeInternal();
        } catch (ParseException e) {
            String msg = "Error decoding wkb for envelope";
            throw new IOException(msg, e);
        }
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        byte[] bytes = rs.getBytes(column);
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
        mappings.put(Geometry.class, Types.OTHER);
    }

    /** Return the geometry type ID to be stored in the geometry_columns table for a particular class */
    private int getGeometryTypeNumber(String className) {
        if (className.equals(Point.class.getSimpleName())) {
            return 1;
        } else if (className.equals(LineString.class.getSimpleName())) {
            return 3;
        } else if (className.equals(Polygon.class.getSimpleName())) {
            return 5;
        } else if (className.equals(MultiPoint.class.getSimpleName())) {
            return 7;
        } else if (className.equals(MultiLineString.class.getSimpleName())) {
            return 9;
        } else if (className.equals(MultiPolygon.class.getSimpleName())) {
            return 11;
        } else if (className.equals(GeometryCollection.class.getSimpleName())) {
            return 6;
        }
        return 0; // Geometry
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put("st_point", Point.class);
        mappings.put("st_linestring", LineString.class);
        mappings.put("st_polygon", Polygon.class);
        mappings.put("st_multipoint", MultiPoint.class);
        mappings.put("st_multilinestring", MultiLineString.class);
        mappings.put("st_multipolygon", MultiPolygon.class);
        mappings.put("st_geometry", Geometry.class);
        mappings.put("st_geometrycollection", GeometryCollection.class);
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

        // create spatial index for all geometry columns
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            if (!(ad instanceof GeometryDescriptor)) {
                continue;
            }
            GeometryDescriptor gd = (GeometryDescriptor) ad;

            // TODO: Check this, syntax is probably wrong for Informix, is it not ever running?
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

            // This code does not seem to be needed by any of the tests, but it is present for all
            // the other datasource plugins so we have adapted it for Informix and retained it.
            StringBuffer sql = new StringBuffer("INSERT INTO geometry_columns ");
            sql.append(
                    "(f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, geometry_type)");
            sql.append(" VALUES (");
            sql.append("DBINFO('dbname'), ");
            // Per the IBM Informix Spatial Data User's Guide, the value of f_table_schema should be
            // the DB owner name.
            sql.append("'" + cx.getMetaData().getUserName() + "'").append(", ");
            sql.append("'").append(featureType.getTypeName()).append("', ");
            sql.append("'").append(ad.getLocalName()).append("', ");
            sql.append("2, ");
            sql.append(srid).append(", ");
            String typeName = ad.getType().getBinding().getSimpleName();
            int typeNumber = getGeometryTypeNumber(typeName);

            sql.append(typeNumber);
            sql.append(")");

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
        sql.append(" SERIAL PRIMARY KEY");
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
            String sql = "SELECT DBINFO( 'sqlca.sqlerrd1' ) FROM systables LIMIT 1";
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
        // Note: The syntax of LIMIT and SKIP in Informix is stricter inside a subquery than in
        // other contexts. SKIP must precede FIRST (no LIMIT) and these must be immediately after
        // SELECT.
        int selectIndex = sql.indexOf("SELECT");
        if (selectIndex < 0) {
            throw new IllegalArgumentException("Cannot apply limit to a query that does not include SELECT");
        }

        String limitSql = null;
        if (limit >= 0 && limit < Integer.MAX_VALUE) {
            if (offset > 0) limitSql = " SKIP " + offset + " FIRST " + limit;
            else limitSql = " FIRST " + limit;
        } else if (offset > 0) {
            limitSql = " SKIP " + offset;
        }

        sql.insert(selectIndex + "SELECT".length(), limitSql);
    }

    @Override
    public FilterToSQL createFilterToSQL() {
        return new InformixFilterToSQL();
    }

    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema, SimpleFeatureType featureType, Connection cx)
            throws IOException {
        String tableName = featureType.getTypeName();

        Statement st = null;
        ResultSet rs = null;

        List<ReferencedEnvelope> result = new ArrayList<>();
        try {
            st = cx.createStatement();

            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    StringBuffer sql = new StringBuffer();
                    sql.append("select ST_AsBinary(SE_BoundingBox('");
                    sql.append(tableName);
                    sql.append("', '");
                    sql.append(att.getName().getLocalPart());
                    sql.append("')) from systables where tabid = 1");
                    rs = st.executeQuery(sql.toString());

                    if (rs.next()) {
                        // decode the geometry
                        Envelope env = decodeGeometryEnvelope(rs, 1, cx);

                        // reproject and merge
                        if (!env.isNull()) {
                            CoordinateReferenceSystem flatCRS =
                                    CRS.getHorizontalCRS(featureType.getCoordinateReferenceSystem());
                            result.add(new ReferencedEnvelope(env, flatCRS));
                        }
                    }
                    rs.close();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to use SE_BoundingBox, falling back on envelope aggregation", e);
            return null;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }
        return result;
    }
}
