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
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * SingleStore database dialect based on basic (non-prepared) statements.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Nikolaos Pringouris <nprigour@gmail.com> added support for SingleStore versions 5.6 (and above)
 */
public class SingleStoreDialectBasic extends BasicSQLDialect {

    SingleStoreDialect delegate;

    public SingleStoreDialectBasic(JDBCDataStore dataStore) {
        this(dataStore, false);
    }

    public SingleStoreDialectBasic(JDBCDataStore dataStore, boolean usePreciseSpatialOps) {
        super(dataStore);
        delegate = new SingleStoreDialect(dataStore);
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx) throws SQLException {
        return delegate.includeTable(schemaName, tableName, cx);
    }

    @Override
    public Class<?> getMapping(String sqlTypeName) {
        if (sqlTypeName.equalsIgnoreCase("geography")) {
            return Geometry.class;
        }

        if (sqlTypeName.equalsIgnoreCase("geographypoint")) {
            return Point.class;
        }
        return super.getMapping(sqlTypeName);
    }

    @Override
    public String getNameEscape() {
        return delegate.getNameEscape();
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return delegate.getGeometryTypeName(type);
    }

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getGeometrySRID(schemaName, tableName, columnName, cx);
    }

    @Override
    public void encodeColumnName(String prefix, String raw, StringBuffer sql) {
        delegate.encodeColumnName(prefix, raw, sql);
    }

    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql) {
        delegate.encodeColumnName(prefix, gatt.getLocalName(), sql);
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        delegate.encodeGeometryColumn(gatt, prefix, srid, hints, sql);
    }

    @Override
    public void encodeColumnType(String sqlTypeName, StringBuffer sql) {
        delegate.encodeColumnType(sqlTypeName, sql);
    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        delegate.registerClassToSqlMappings(mappings);
    }

    @Override
    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        delegate.registerSqlTypeToClassMappings(mappings);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        delegate.registerSqlTypeNameToClassMappings(mappings);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        delegate.registerSqlTypeToSqlTypeNameOverrides(overrides);
    }

    @Override
    public void encodePostColumnCreateTable(AttributeDescriptor att, StringBuffer sql) {
        delegate.encodePostColumnCreateTable(att, sql);
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        delegate.postCreateTable(schemaName, featureType, cx);
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        delegate.encodePrimaryKey(column, sql);
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return delegate.lookupGeneratedValuesPostInsert();
    }

    @Override
    public Object getNextAutoGeneratedValue(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getNextAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    @Override
    public Object getLastAutoGeneratedValue(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getLastAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    @Override
    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql) throws IOException {
        if (value != null) {
            if (value instanceof Point) {
                sql.append(String.format("'%s'", value));
            } else if (value instanceof LineString) {
                sql.append(String.format("'%s'", value));
            } else if (value instanceof Polygon) {
                sql.append(String.format("'%s'", value));
            } else {
                throw new IOException(
                        "Unsupported geometry type: " + value.getClass().getSimpleName());
            }
        } else {
            sql.append("NULL");
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
            WKTReader wktReader = new WKTReader(factory);
            Geometry geometry = wktReader.read(new String(bytes, StandardCharsets.UTF_8));
            return geometry;
        } catch (ParseException e) {
            String msg = "Error decoding wkb";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        delegate.encodeGeometryEnvelope(tableName, geometryColumn, sql);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        return delegate.decodeGeometryEnvelope(rs, column, cx);
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return delegate.isLimitOffsetSupported();
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        delegate.applyLimitOffset(sql, limit, offset);
    }

    @Override
    public FilterToSQL createFilterToSQL() {
        SingleStoreFilterToSQL fts = new SingleStoreFilterToSQL();
        return fts;
    }

    @Override
    public void dropIndex(Connection cx, SimpleFeatureType schema, String databaseSchema, String indexName)
            throws SQLException {
        delegate.dropIndex(cx, schema, databaseSchema, indexName);
    }

    @Override
    public boolean canGroupOnGeometry() {
        return delegate.canGroupOnGeometry();
    }

    @Override
    public Filter[] splitFilter(Filter filter, SimpleFeatureType schema) {
        return delegate.splitFilter(filter, schema);
    }
}
