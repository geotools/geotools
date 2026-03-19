/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final Pattern NEXTVAL_PATTERN = Pattern.compile("(?i)nextval\\('([^']+)'\\)");
    private static final String ST_SRID_CAPABILITY_SQL =
            "SELECT 1 FROM duckdb_functions() WHERE lower(function_name) = 'st_srid' LIMIT 1";

    private boolean screenMapEnabled = true;

    private boolean simplifyEnabled = true;

    // null means "not probed yet"; true/false are stable cached outcomes.
    private volatile Boolean stSridAvailable;

    // private boolean topologyPreserved;

    protected DuckDBDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    @Override
    public void encodeValue(Object value, Class type, StringBuffer sql) {
        if (value instanceof byte[] bytes) {
            sql.append("from_hex('");
            appendHex(bytes, sql);
            sql.append("')");
            return;
        }
        super.encodeValue(value, type, sql);
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
    public String[] getDesiredTablesType() {
        return new String[] {"TABLE", "BASE TABLE", "VIEW", "MATERIALIZED VIEW", "SYNONYM"};
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
        Class<?> parentMapping = getParentMapping(columnMetaData, cx);
        String typeName = columnMetaData.getString("TYPE_NAME");

        // Check if it's a geometry column
        if ("GEOMETRY".equalsIgnoreCase(typeName)) {
            return Geometry.class;
        }

        return parentMapping;
    }

    protected Class<?> getParentMapping(ResultSet columnMetaData, Connection cx) throws SQLException {
        return super.getMapping(columnMetaData, cx);
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
        mappings.put(Types.STRUCT, Struct.class);
        mappings.put(Types.TIMESTAMP_WITH_TIMEZONE, Timestamp.class);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        mappings.put("TIMESTAMP WITH TIME ZONE", Timestamp.class);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        overrides.put(Types.INTEGER, "INTEGER");
        overrides.put(Types.BIGINT, "BIGINT");
        overrides.put(Types.SMALLINT, "SMALLINT");
        overrides.put(Types.FLOAT, "FLOAT");
        overrides.put(Types.REAL, "REAL");
        overrides.put(Types.DOUBLE, "DOUBLE");
        overrides.put(Types.DECIMAL, "DECIMAL");
        overrides.put(Types.NUMERIC, "NUMERIC");
        overrides.put(Types.BOOLEAN, "BOOLEAN");
        overrides.put(Types.VARCHAR, "VARCHAR");
        overrides.put(Types.CHAR, "CHAR");
        overrides.put(Types.DATE, "DATE");
        overrides.put(Types.TIME, "TIME");
        overrides.put(Types.TIMESTAMP, "TIMESTAMP");
        overrides.put(Types.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP WITH TIME ZONE");
        overrides.put(Types.BLOB, "BLOB");
        overrides.put(Types.STRUCT, "STRUCT");
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

        mappings.put(Struct.class, Types.STRUCT);
    }

    @Override
    public Object convertValue(Object value, AttributeDescriptor ad) {
        if (value == null) {
            return null;
        }

        Class<?> binding = ad.getType().getBinding();
        if (binding == java.sql.Date.class && value instanceof LocalDate localDate) {
            return java.sql.Date.valueOf(localDate);
        }
        if (binding == Timestamp.class) {
            if (value instanceof LocalDateTime localDateTime) {
                return Timestamp.valueOf(localDateTime);
            }
            if (value instanceof OffsetDateTime offsetDateTime) {
                return Timestamp.from(offsetDateTime.toInstant());
            }
            if (value instanceof Instant instant) {
                return Timestamp.from(instant);
            }
        }
        if (binding == java.sql.Time.class && value instanceof LocalTime localTime) {
            return java.sql.Time.valueOf(localTime);
        }

        return super.convertValue(value, ad);
    }

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        if (!isStSridAvailable(cx)) {
            return null;
        }

        // Query to get the SRID of a geometry column
        String sql = "SELECT ST_SRID(%s) FROM %s WHERE %s IS NOT NULL LIMIT 1"
                .formatted(escapeName(columnName), escapeName(tableName), escapeName(columnName));

        try (Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return null;
    }

    private boolean isStSridAvailable(Connection cx) throws SQLException {
        Boolean cached = stSridAvailable;
        if (cached != null) {
            return cached;
        }

        synchronized (this) {
            cached = stSridAvailable;
            if (cached != null) {
                return cached;
            }

            boolean probed = probeStSridAvailability(cx);
            stSridAvailable = probed;
            return probed;
        }
    }

    private boolean probeStSridAvailability(Connection cx) throws SQLException {
        try (Statement statement = cx.createStatement();
                ResultSet rs = statement.executeQuery(ST_SRID_CAPABILITY_SQL)) {
            // PMD false-positive safe pattern: navigation result is checked and then consumed.
            boolean available = rs.next();
            if (available) {
                LOGGER.log(Level.FINE, "DuckDB spatial extension exposes ST_SRID for native GEOMETRY");
            } else {
                LOGGER.log(Level.FINE, "DuckDB spatial extension does not expose ST_SRID for native GEOMETRY");
            }
            return available;
        }
    }

    @Override
    public String getSequenceForColumn(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        String sql = "SELECT column_default FROM information_schema.columns WHERE table_name = ? AND column_name = ?";
        if (schemaName != null && !schemaName.isEmpty()) {
            sql += " AND table_schema = ?";
        }

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            if (schemaName != null && !schemaName.isEmpty()) {
                ps.setString(3, schemaName);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String columnDefault = rs.getString(1);
                if (columnDefault == null) {
                    return null;
                }

                Matcher matcher = NEXTVAL_PATTERN.matcher(columnDefault);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
                return null;
            }
        }
    }

    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx) throws SQLException {
        String sql = "SELECT " + encodeNextSequenceValue(schemaName, sequenceName);
        try (Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        }
    }

    @Override
    public String encodeNextSequenceValue(String schemaName, String sequenceName) {
        StringBuilder qualifiedSequence = new StringBuilder();
        if (schemaName != null && !schemaName.isEmpty()) {
            qualifiedSequence.append(escapeName(schemaName)).append('.');
        }
        qualifiedSequence.append(escapeName(sequenceName));

        // nextval expects a string literal containing the relation name.
        String escapedLiteral = qualifiedSequence.toString().replace("'", "''");
        return "nextval('" + escapedLiteral + "')";
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        if (type != null && type == Types.OTHER) {
            return "GEOMETRY";
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
        String sql = "SELECT ST_AsWKB(ST_Extent_Agg(%s)::GEOMETRY)::BLOB FROM %s"
                .formatted(escapeName(column), escapeName(tableName));

        try (PreparedStatement ps = cx.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (!rs.next())
                throw new RuntimeException("Could not compute optimized bounds, ST_Extent_Agg returned no record");
            Blob blob = rs.getBlob(1);
            Geometry fullBounds = parseWKB(blob);
            if (fullBounds == null) {
                // DuckDB ST_Extent_Agg returns NULL for empty tables (or all-NULL geometry columns).
                // Returning an empty envelope keeps the GeoTools bounds contract stable and avoids
                // null propagation/failures in callers expecting a non-null ReferencedEnvelope.
                return new ReferencedEnvelope(geometryDescriptor.getCoordinateReferenceSystem());
            }
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

        String geometry = "%s::GEOMETRY".formatted(encodeColumnName(prefix, gatt.getLocalName()));
        if (forceMulti) {
            geometry = "ST_Multi(%s)".formatted(geometry);
        }
        if (force2D) {
            geometry = "ST_Force2D(%s)".formatted(geometry);
        }
        String geomSql = "ST_AsWKB(%s)::BLOB".formatted(geometry);
        sql.append(geomSql);
    }

    public String encodeColumnName(String prefix, String raw) {
        if (prefix != null) {
            return "%s.%s".formatted(escapeName(prefix), escapeName(raw));
        }
        return escapeName(raw);
    }

    @Override
    public void encodeGeometryColumnGeneralized(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {

        if (distance == null) {
            encodeGeometryColumn(gatt, prefix, srid, null, sql);
            return;
        }

        String geometryColumn = gatt.getLocalName();
        sql.append("ST_AsWKB(ST_SimplifyPreserveTopology(");
        sql.append(encodeColumnName(prefix, geometryColumn));
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

    private static void appendHex(byte[] bytes, StringBuffer sql) {
        for (byte b : bytes) {
            sql.append(Character.forDigit((b >> 4) & 0xF, 16));
            sql.append(Character.forDigit(b & 0xF, 16));
        }
    }

    @Override
    public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql) throws IOException {
        // Use WKB encoding to avoid locale-specific decimal parsing issues in geometry literals
        if (value != null && !value.isEmpty()) {
            sql.append("ST_GeomFromHEXEWKB('");
            HexWKBEncoder.encode(value, sql);
            sql.append("')");
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
    public boolean isAggregatedSortSupported(String function) {
        return "distinct".equalsIgnoreCase(function);
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
