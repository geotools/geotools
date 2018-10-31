/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.db2;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class DB2SQLDialectBasic extends BasicSQLDialect {

    private DB2SQLDialect delegate = null;

    public DB2SQLDialectBasic(JDBCDataStore dataStore, DB2DialectInfo info) {
        super(dataStore);
        delegate = new DB2SQLDialect(dataStore, info);
    }

    /* (non-Javadoc)
     * @see org.geotools.jdbc.SQLDialect#createCRS(int, java.sql.Connection)
     *
     */
    @Override
    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException {
        return delegate.createCRS(srid, cx);
    }

    @Override
    public FilterToSQL createFilterToSQL() {
        DB2FilterToSQL filter = new DB2FilterToSQL((Writer) null);
        filter.setFunctionEncodingEnabled(isFunctionEncodingEnabled());
        filter.setLooseBBOXEnabled(delegate.isLooseBBOXEnabled());
        if (delegate.isUseSelectivity())
            filter.setSelectivityClause(DB2SQLDialect.SELECTIVITY_CLAUSE);
        return filter;
    }

    public boolean isLooseBBOXEnabled() {
        return delegate.isLooseBBOXEnabled();
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        delegate.setLooseBBOXEnabled(looseBBOXEnabled);
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        delegate.encodePrimaryKey(column, sql);
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return delegate.getGeometryTypeName(type);
    }

    @Override
    public Integer getGeometrySRID(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {

        return delegate.getGeometrySRID(schemaName, tableName, columnName, cx);
    }

    public void encodeGeometryColumn(GeometryDescriptor gatt, StringBuffer sql) {
        delegate.encodeGeometryColumn(gatt, null, sql);
    }

    @Override
    public void encodeGeometryColumn(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql) {
        delegate.encodeGeometryColumn(gatt, prefix, srid, sql);
    }

    @Override
    public void encodeGeometryColumn(
            GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        delegate.encodeGeometryColumn(gatt, prefix, srid, hints, sql);
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        delegate.encodeGeometryEnvelope(tableName, geometryColumn, sql);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException, IOException {

        return delegate.decodeGeometryEnvelope(rs, column, cx);
    }

    @Override
    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql)
            throws IOException {
        DB2Util.encodeGeometryValue(value, srid, sql);
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
        return delegate.decodeGeometryValue(descriptor, rs, name, factory, cx, hints);
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            int column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        return delegate.decodeGeometryValue(descriptor, rs, column, factory, cx, new Hints());
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
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        delegate.postCreateTable(schemaName, featureType, cx);
    }

    @Override
    public String getSequenceForColumn(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getSequenceForColumn(schemaName, tableName, columnName, cx);
    }

    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx)
            throws SQLException {
        return delegate.getNextSequenceValue(schemaName, sequenceName, cx);
    }

    @Override
    public String encodeNextSequenceValue(String schemaName, String sequenceName) {
        return delegate.encodeNextSequenceValue(schemaName, sequenceName);
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return delegate.lookupGeneratedValuesPostInsert();
    }

    @Override
    public Object getLastAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return delegate.getLastAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    public boolean isLimitOffsetSupported() {
        return delegate.isLimitOffsetSupported();
    }

    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        delegate.applyLimitOffset(sql, limit, offset);
    }

    @Override
    public void encodeGeometryColumnGeneralized(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        delegate.encodeGeometryColumnGeneralized(gatt, prefix, srid, sql, distance);
    }

    @Override
    protected void addSupportedHints(Set<Key> hints) {
        delegate.addSupportedHints(hints);
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        return delegate.includeTable(schemaName, tableName, cx);
    }

    public DB2DialectInfo getDb2DialectInfo() {
        return delegate.getDb2DialectInfo();
    }

    public boolean isFunctionEncodingEnabled() {
        return delegate.isFunctionEncodingEnabled();
    }

    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        delegate.setFunctionEncodingEnabled(functionEncodingEnabled);
    }

    public List<ReferencedEnvelope> getOptimizedBounds(
            String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        return delegate.getOptimizedBounds(schema, featureType, cx);
    }

    public boolean isUseSelectivity() {
        return delegate.isUseSelectivity();
    }

    public void setUseSelectivity(boolean useSelectivity) {
        delegate.setUseSelectivity(useSelectivity);
    }

    @Override
    protected boolean supportsSchemaForIndex() {
        return delegate.supportsSchemaForIndex();
    }
}
