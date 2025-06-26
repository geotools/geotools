/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.Geometries;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTReader;

/**
 * Delegate for {@link SingleStoreDialect} and {@link org.geotools.data.singlestore.SingleStoreDialectPrepared} which
 * implements the common part of the api.
 */
public class SingleStoreDialect extends SQLDialect {

    public SingleStoreDialect(JDBCDataStore dataStore) {
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
        return super.getGeometryTypeName(type);
    }

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        String sql = String.format(
                "SELECT COLUMN_NAME, DATA_TYPE " + "FROM INFORMATION_SCHEMA.COLUMNS "
                        + "WHERE TABLE_NAME = '%s' AND COLUMN_NAME = '%s'",
                tableName, columnName);

        dataStore.getLogger().fine(sql);

        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql);
            try {
                if (rs.next()) {
                    return 4326;
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } catch (SQLException e) {
            // geometry_columns does not exist
        } finally {
            dataStore.closeSafe(st);
        }
        return null;
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        encodeColumnName(null, geometryColumn, sql);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        try {
            Geometry geom = new WKTReader().read(rs.getString(column));
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
    public void encodePostColumnCreateTable(AttributeDescriptor att, StringBuffer sql) {
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
                    StringBuffer sqlb = new StringBuffer("CREATE TABLE ");
                    encodeTableName("geometry_columns", sqlb);
                    sqlb.append("(");
                    encodeColumnName(null, "f_table_schema", sqlb);
                    sqlb.append(" varchar(255), ");
                    encodeColumnName(null, "f_table_name", sqlb);
                    sqlb.append(" varchar(255), ");
                    encodeColumnName(null, "f_geometry_column", sqlb);
                    sqlb.append(" varchar(255), ");
                    encodeColumnName(null, "coord_dimension", sqlb);
                    sqlb.append(" int, ");
                    encodeColumnName(null, "srid", sqlb);
                    sqlb.append(" int, ");
                    encodeColumnName(null, "type", sqlb);
                    sqlb.append(" varchar(32)");
                    sqlb.append(")");

                    String sql = sqlb.toString();
                    LOGGER.fine(sql);
                    st.execute(sql);
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
            // SingleStore pretends to have limit specified along with offset
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
        // weirdness, index names are treated as strings...
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

    @Override
    public Filter[] splitFilter(Filter filter, SimpleFeatureType schema) {
        Filter[] prePost = super.splitFilter(filter, schema);
        Filter pre = prePost[0];
        Filter post = prePost[1];
        pre = (Filter) pre.accept(new SingleStoreLargeGeometryPreProcessor(), null);

        return new Filter[] {pre, post};
    }
}
