/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.duckdb.DuckDBStruct;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
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
import org.locationtech.jts.io.InputStreamInStream;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * Base SQL Dialect for DuckDB-based datastores. Provides common DuckDB SQL functionality including spatial support.
 *
 * <p>This dialect implements the core SQL operations for DuckDB, with a focus on spatial functionality. It provides
 * implementations for:
 *
 * <ul>
 *   <li>Geometry handling (WKB encoding/decoding, spatial function mapping)
 *   <li>SQL type conversions for geometry classes
 *   <li>Filter and query translation
 *   <li>Extension management (spatial, etc.)
 *   <li>Optimized bounds calculations
 *   <li>Geometry simplification for rendering
 * </ul>
 *
 * <p>This base dialect is extended by format-specific dialects like {@code GeoParquetDialect} to provide specialized
 * functionality for different data formats while sharing the common DuckDB handling code.
 *
 * <p>DuckDB is particularly well-suited for analytical workloads and includes excellent built-in support for spatial
 * operations and columnar file formats like Parquet.
 */
public class DuckDBDialect extends BasicSQLDialect {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private boolean screenMapEnabled = true;

    private boolean simplifyEnabled = true;

    // private boolean topologyPreserved;

    protected DuckDBDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    public void setScreenMapEnabled(boolean screenMapEnabled) {
        this.screenMapEnabled = screenMapEnabled;
    }

    public void setSimplifyEnabled(boolean simplifyEnabled) {
        this.simplifyEnabled = simplifyEnabled;
    }

    //    public void setTopologyPreserved(boolean topologyPreserved) {
    //        this.topologyPreserved = topologyPreserved;
    //    }

    public List<String> getDatabaseInitSql() {
        return List.of("install spatial", "load spatial");
    }

    @Override
    public String getNameEscape() {
        return "\"";
    }

    @Override
    public String escapeName(String name) {
        return getNameEscape() + name + getNameEscape();
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx) throws SQLException {
        // exclude DuckDB system tables
        boolean systemTable = tableName.startsWith("pg_")
                || tableName.startsWith("sqlite_")
                || tableName.startsWith("information_schema");

        return !systemTable;
    }

    @Override
    protected void addSupportedHints(Set<Hints.Key> hints) {
        if (simplifyEnabled) {
            hints.add(Hints.GEOMETRY_SIMPLIFICATION);
            hints.add(Hints.GEOMETRY_GENERALIZATION);
        }
        hints.add(Hints.FEATURE_2D);
        if (screenMapEnabled) {
            hints.add(Hints.SCREENMAP);
        }
    }

    /**
     * It is called before the mappings registered in
     * {@link #registerSqlTypeToClassMappings(Map)} and
     * {@link #registerSqlTypeNameToClassMappings(Map) are used to determine the
     * mapping. Subclasses should implement as needed, the default implementation
     * returns {@code null}.
     */
    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx) throws SQLException {
        super.getMapping(columnMetaData, cx);
        String typeName = columnMetaData.getString("TYPE_NAME");

        // Check if it's a geometry column
        if ("GEOMETRY".equalsIgnoreCase(typeName)) {
            return Geometry.class;
        }

        return null;
    }

    /**
     * Appends a mapping of {@link Types#STRUCT} to {@code java.sql.Struct.class}
     *
     * <p>Support for struct is limited and results in GeoServer WMS and WFS to interpret it as a String literal. For
     * example, a "bbox" {@link Struct} attribute value with {@link Struct#getSQLTypeName() SQL type name} as
     * {@literal STRUCT(xmin FLOAT, xmax FLOAT, ymin FLOAT, ymax FLOAT)} will be encoded as
     * {@literal <bbox>{xmin=-63.005005, xmax=-63.004997, ymin=-40.81569, ymax=-40.81568}</bbox> } by virtue of
     * {@link DuckDBStruct#toString()}
     */
    @Override
    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        super.registerSqlTypeToClassMappings(mappings);
        mappings.put(Integer.valueOf(Types.STRUCT), java.sql.Struct.class);
    }

    /**
     * Maps {@link Geometry} types to {@link Types#OTHER} and {@link java.sql.Struct} to {@link Types#STRUCT}
     *
     * <p>Support for struct is limited and results in GeoServer WMS and WFS to interpret it as a String literal. For
     * example, a "bbox" {@link Struct} attribute value with {@link Struct#getSQLTypeName() SQL type name} as
     * {@literal STRUCT(xmin FLOAT, xmax FLOAT, ymin FLOAT, ymax FLOAT)} will be encoded as
     * {@literal <bbox>{xmin=-63.005005, xmax=-63.004997, ymin=-40.81569, ymax=-40.81568}</bbox> } by virtue of
     * {@link DuckDBStruct#toString()}
     */
    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);

        // DuckDB uses SQL/MM types for geometries
        mappings.put(Geometry.class, Types.OTHER);
        mappings.put(Point.class, Types.OTHER);
        mappings.put(LineString.class, Types.OTHER);
        mappings.put(Polygon.class, Types.OTHER);
        mappings.put(MultiPoint.class, Types.OTHER);
        mappings.put(MultiLineString.class, Types.OTHER);
        mappings.put(MultiPolygon.class, Types.OTHER);
        mappings.put(GeometryCollection.class, Types.OTHER);

        mappings.put(java.sql.Struct.class, Types.STRUCT);
    }

    @Override
    public void encodePostColumnCreateTable(AttributeDescriptor att, StringBuffer sql) {
        if (att instanceof GeometryDescriptor) {
            // Add proper type hint if it's a geometry
            Class<?> binding = att.getType().getBinding();
            if (isConcreteGeometry(binding)) {
                sql.append(" GEOMETRY");
            }
        }
    }

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        // Query to get the SRID of a geometry column
        String sql = String.format(
                "SELECT ST_SRID(%s) FROM %s WHERE %s IS NOT NULL LIMIT 1",
                escapeName(columnName), escapeName(tableName), escapeName(columnName));

        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql);
            try {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to retrieve SRID for geometry column", e);
        } finally {
            dataStore.closeSafe(st);
        }
        return null;
    }

    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        if (null == geometryDescriptor) {
            return List.of();
        }
        return List.of(optimizedBounds(featureType, cx));
    }

    protected ReferencedEnvelope optimizedBounds(SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {

        GeometryDescriptor geometryDescriptor = requireNonNull(featureType.getGeometryDescriptor());
        String tableName = featureType.getTypeName();
        String column = geometryDescriptor.getLocalName();
        String sql = String.format(
                "SELECT ST_AsWKB(ST_Extent_Agg(%s)::GEOMETRY)::BLOB FROM %s",
                escapeName(column), escapeName(tableName));

        try (PreparedStatement ps = cx.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (!rs.next())
                throw new RuntimeException("Could not compute optimized bounds, ST_Extent_Agg returned no record");
            Blob blob = rs.getBlob(1);
            Geometry fullBounds = parseWKB(blob);
            return new ReferencedEnvelope(
                    fullBounds.getEnvelopeInternal(), geometryDescriptor.getCoordinateReferenceSystem());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see #decodeGeometryEnvelope(ResultSet, int, Connection)
     */
    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        // Use ST_Envelope to get the envelope
        sql.append("ST_AsWKB(ST_Envelope(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append(")::GEOMETRY)::BLOB "); // mind the trailing whitespace, JDBCDataStore won't add it
    }

    /**
     * {@inheritDoc}
     *
     * @see #encodeGeometryEnvelope(String, String, StringBuffer)
     */
    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        // Extract envelope from result
        Geometry geom = parseWKB(GEOMETRY_FACTORY, rs.getBlob(column));
        if (geom == null) {
            return new Envelope();
        }
        return geom.getEnvelopeInternal();
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        encodeGeometryColumnInternal(gatt, prefix, hints, false, sql);
    }

    protected void encodeGeometryColumnInternal(
            GeometryDescriptor gatt, String prefix, Hints hints, boolean forceMulti, StringBuffer sql) {
        boolean force2D = hints != null
                && hints.containsKey(Hints.FEATURE_2D)
                && Boolean.TRUE.equals(hints.get(Hints.FEATURE_2D));

        String geometry = format("%s::GEOMETRY", encodeColumnName(prefix, gatt.getLocalName()));
        if (forceMulti) {
            geometry = format("ST_Multi(%s)", geometry);
        }
        if (force2D) {
            geometry = format("ST_Force2D(%s)", geometry);
        }
        String geomSql = format("ST_AsWKB(%s)::BLOB", geometry);
        sql.append(geomSql);
    }

    public String encodeColumnName(String prefix, String raw) {
        if (prefix != null) {
            return format("%s.%s", escapeName(prefix), escapeName(raw));
        }
        return escapeName(raw);
    }

    @Override
    public void encodeGeometryColumnGeneralized(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {

        if (distance == null) {
            encodeGeometryColumn(gatt, prefix, srid, null, sql);
        }

        String geometryColumn = gatt.getLocalName();
        sql.append("ST_AsWKB(ST_SimplifyPreserveTopology(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append("::GEOMETRY, " + distance + "))::BLOB");
    }

    @Override
    public void encodeGeometryColumnSimplified(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {

        if (simplifyEnabled && distance != null) {
            // NOTE: gotta use ST_SimplifyPreserveTopology always, ST_Simplify() results in too many empty/invalid
            // geometryies (as of duckdb 1.2)
            // if (topologyPreserved) {
            // force topology preserving simplification
            encodeGeometryColumnGeneralized(gatt, prefix, srid, sql, distance);
            //            } else {
            //                String geometryColumn = gatt.getLocalName();
            //                sql.append("ST_AsWKB(ST_Simplify(");
            //                encodeColumnName(null, geometryColumn, sql);
            //                sql.append("::GEOMETRY, " + distance + "))::BLOB");
            //            }
        } else { // fallback to no simplification
            encodeGeometryColumn(gatt, prefix, srid, null, sql);
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

        Blob blob = rs.getBlob(column);
        return parseWKB(factory, blob);
    }

    protected Geometry parseWKB(Blob blob) throws SQLException, IOException {
        return parseWKB(GEOMETRY_FACTORY, blob);
    }

    protected Geometry parseWKB(GeometryFactory factory, Blob blob) throws SQLException, IOException {
        if (blob == null) {
            return null;
        }

        try {
            InputStreamInStream inStream = new InputStreamInStream(blob.getBinaryStream());
            return new WKBReader(factory).read(inStream);
        } catch (ParseException e) {
            throw new IOException("Error parsing WKB geometry", e);
        }
    }

    @Override
    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql) throws IOException {
        if (value != null && !value.isEmpty()) {
            sql.append("ST_GeomFromText('");
            sql.append(new WKTWriter().write(value));
            sql.append("', ");
            sql.append(srid);
            sql.append(")");
        } else {
            sql.append("NULL");
        }
    }

    protected boolean isConcreteGeometry(Class<?> binding) {
        return Point.class.isAssignableFrom(binding)
                || LineString.class.isAssignableFrom(binding)
                || Polygon.class.isAssignableFrom(binding)
                || MultiPoint.class.isAssignableFrom(binding)
                || MultiLineString.class.isAssignableFrom(binding)
                || MultiPolygon.class.isAssignableFrom(binding);
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(null, column, sql);
        sql.append(" INTEGER PRIMARY KEY");
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return true;
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        if (limit >= 0 && limit < Integer.MAX_VALUE) {
            sql.append(" LIMIT ").append(limit);
            if (offset > 0) {
                sql.append(" OFFSET ").append(offset);
            }
        } else if (offset > 0) {
            // SQL standard says OFFSET requires LIMIT, PostgreSQL allows OFFSET without
            // LIMIT
            // DuckDB follows PostgreSQL approach
            sql.append(" OFFSET ").append(offset);
        }
    }

    @Override
    public FilterToSQL createFilterToSQL() {
        return new DuckDBFilterToSQL();
    }
}
