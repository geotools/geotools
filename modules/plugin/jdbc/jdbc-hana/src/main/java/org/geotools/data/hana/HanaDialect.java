/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.SingleCRS;
import org.geotools.data.hana.wkb.HanaWKBParser;
import org.geotools.data.hana.wkb.HanaWKBParserException;
import org.geotools.data.hana.wkb.HanaWKBWriter;
import org.geotools.data.hana.wkb.HanaWKBWriterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
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

/**
 * A prepared statement SQL dialect for SAP HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaDialect extends PreparedStatementSQLDialect {

    /** UUID suffix for auto-generated identifiers for avoiding name clashes */
    private static final String HANA_UUID = "8E468249703240F0ACDE78162124A62F";

    private static final String SEQUENCE_SUFFIX = "_SEQ_" + HANA_UUID;

    private static final String METADATA_TABLE_NAME = "METADATA_" + HANA_UUID;

    private static final Map<String, Class<?>> TYPE_NAME_TO_CLASS = new HashMap<>();

    private static final int GEOMETRY_TYPE_CODE = 29812;

    static {
        TYPE_NAME_TO_CLASS.put("TINYINT", Short.class);
        TYPE_NAME_TO_CLASS.put("SMALLINT", Short.class);
        TYPE_NAME_TO_CLASS.put("INTEGER", Integer.class);
        TYPE_NAME_TO_CLASS.put("BIGINT", Long.class);
        TYPE_NAME_TO_CLASS.put("DECIMAL", BigDecimal.class);
        TYPE_NAME_TO_CLASS.put("REAL", Float.class);
        TYPE_NAME_TO_CLASS.put("DOUBLE", Double.class);
        TYPE_NAME_TO_CLASS.put("CHAR", String.class);
        TYPE_NAME_TO_CLASS.put("VARCHAR", String.class);
        TYPE_NAME_TO_CLASS.put("BINARY", byte[].class);
        TYPE_NAME_TO_CLASS.put("VARBINARY", byte[].class);
        TYPE_NAME_TO_CLASS.put("DATE", Date.class);
        TYPE_NAME_TO_CLASS.put("TIME", Time.class);
        TYPE_NAME_TO_CLASS.put("TIMESTAMP", Timestamp.class);
        TYPE_NAME_TO_CLASS.put("CLOB", String.class);
        TYPE_NAME_TO_CLASS.put("BLOB", byte[].class);
        TYPE_NAME_TO_CLASS.put("NCHAR", String.class);
        TYPE_NAME_TO_CLASS.put("NVARCHAR", String.class);
        TYPE_NAME_TO_CLASS.put("ALPHANUM", String.class);
        TYPE_NAME_TO_CLASS.put("NCLOB", String.class);
        TYPE_NAME_TO_CLASS.put("SMALLDECIMAL", BigDecimal.class);
        TYPE_NAME_TO_CLASS.put("TEXT", String.class);
        TYPE_NAME_TO_CLASS.put("BINTEXT", byte[].class);
        TYPE_NAME_TO_CLASS.put("SHORTTEXT", String.class);
        TYPE_NAME_TO_CLASS.put("SECONDDATE", Timestamp.class);
        TYPE_NAME_TO_CLASS.put("ST_POINT", Point.class);
        TYPE_NAME_TO_CLASS.put("ST_GEOMETRY", Geometry.class);
        TYPE_NAME_TO_CLASS.put("BOOLEAN", Boolean.class);
    }

    private static final Map<Integer, Class<?>> SQL_TYPE_TO_CLASS = new HashMap<>();

    static {
        SQL_TYPE_TO_CLASS.put(-4, byte[].class); // BLOB
        SQL_TYPE_TO_CLASS.put(-8, String.class); // NCHAR
        SQL_TYPE_TO_CLASS.put(-10, String.class); // NCLOB
        SQL_TYPE_TO_CLASS.put(3000, BigDecimal.class); // SMALLDECIMAL
        SQL_TYPE_TO_CLASS.put(GEOMETRY_TYPE_CODE, Geometry.class); // ST_GEOMETRY
    }

    private static final Map<Class<?>, Integer> CLASS_TO_SQL_TYPE = new HashMap<>();

    static {
        CLASS_TO_SQL_TYPE.put(Geometry.class, GEOMETRY_TYPE_CODE);
        CLASS_TO_SQL_TYPE.put(Point.class, GEOMETRY_TYPE_CODE);
        CLASS_TO_SQL_TYPE.put(LineString.class, GEOMETRY_TYPE_CODE);
        CLASS_TO_SQL_TYPE.put(Polygon.class, GEOMETRY_TYPE_CODE);
        CLASS_TO_SQL_TYPE.put(MultiPoint.class, GEOMETRY_TYPE_CODE);
        CLASS_TO_SQL_TYPE.put(MultiLineString.class, GEOMETRY_TYPE_CODE);
        CLASS_TO_SQL_TYPE.put(MultiPolygon.class, GEOMETRY_TYPE_CODE);
        CLASS_TO_SQL_TYPE.put(GeometryCollection.class, GEOMETRY_TYPE_CODE);
    }

    private static final Map<Integer, String> SQL_TYPE_TO_SQL_TYPE_NAME = new HashMap<>();

    static {
        SQL_TYPE_TO_SQL_TYPE_NAME.put(Types.CLOB, "CLOB");
    }

    private static final String GEOMETRY_TYPE_NAME = "ST_Geometry";

    public HanaDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    private boolean functionEncodingEnabled;

    private boolean simplifyDisabled;

    private String selectHints;

    private HanaVersion hanaVersion;

    private HanaCloudVersion cloudVersion;

    private SchemaCache currentSchemaCache = new SchemaCache();

    private boolean estimatedExtentsEnabled = false;

    public boolean isEstimatedExtentsEnabled() {
        return estimatedExtentsEnabled;
    }

    public void setEstimatedExtentsEnabled(boolean estimatedExtentsEnabled) {
        this.estimatedExtentsEnabled = estimatedExtentsEnabled;
    }

    public void setFunctionEncodingEnabled(boolean enabled) {
        functionEncodingEnabled = enabled;
    }

    public void setSimplifyDisabled(boolean disabled) {
        simplifyDisabled = disabled;
    }

    public void setSelectHints(String selectHints) {
        this.selectHints = selectHints;
    }

    @Override
    public void initializeConnection(Connection cx) throws SQLException {
        super.initializeConnection(cx);
        if (hanaVersion == null) {
            hanaVersion = new HanaVersion(cx.getMetaData().getDatabaseProductVersion());
            if (hanaVersion.getVersion() == 1 && hanaVersion.getRevision() < 120) {
                throw new SQLException("Only HANA 2 and HANA 1 SPS 12 and later are supported");
            }

            cloudVersion = queryCloudVersion(cx);
        }
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx) throws SQLException {
        if (METADATA_TABLE_NAME.equals(tableName)) {
            return false;
        }
        if (dataStore.getDatabaseSchema() != null) {
            return true;
        }
        // The data store will only store the table name in its type name list. If the database
        // schema is null, we must only return the tables that
        // are in the current schema.
        String currentSchema = currentSchemaCache.getSchema(cx);
        return currentSchema.equals(schemaName);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        mappings.putAll(TYPE_NAME_TO_CLASS);
    }

    @Override
    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        super.registerSqlTypeToClassMappings(mappings);
        mappings.putAll(SQL_TYPE_TO_CLASS);
    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);
        mappings.putAll(CLASS_TO_SQL_TYPE);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        super.registerSqlTypeToSqlTypeNameOverrides(overrides);
        overrides.putAll(SQL_TYPE_TO_SQL_TYPE_NAME);
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return GEOMETRY_TYPE_NAME;
    }

    private Integer getGeometrySRIDFromView(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (schemaName != null) {
                ps = cx.prepareStatement(
                        "SELECT SRS_ID FROM PUBLIC.ST_GEOMETRY_COLUMNS WHERE SCHEMA_NAME = ? AND TABLE_NAME = ? AND COLUMN_NAME = ?");
                ps.setString(1, schemaName);
                ps.setString(2, tableName);
                ps.setString(3, columnName);
            } else {
                ps = cx.prepareStatement(
                        "SELECT SRS_ID FROM PUBLIC.ST_GEOMETRY_COLUMNS WHERE SCHEMA_NAME = CURRENT_SCHEMA AND TABLE_NAME = ? AND COLUMN_NAME = ?");
                ps.setString(1, tableName);
                ps.setString(2, columnName);
            }
            rs = ps.executeQuery();
            if (!rs.next()) return null;
            int srid = rs.getInt(1);
            if (rs.wasNull()) return null;
            if (rs.next()) return null;
            return srid;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(ps);
        }
    }

    private Integer getGeometrySRIDViaSelect(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        // Try the first non-NULL geometry
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        encodeIdentifiers(sql, columnName);
        sql.append(".ST_SRID() FROM ");
        encodeIdentifiers(sql, schemaName, tableName);
        sql.append(" WHERE ");
        encodeIdentifiers(sql, columnName);
        sql.append(" IS NOT NULL LIMIT 1");

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = cx.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return null;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(ps);
        }
    }

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        Integer srid = getGeometrySRIDFromView(schemaName, tableName, columnName, cx);
        if (srid == null) {
            srid = getGeometrySRIDViaSelect(schemaName, tableName, columnName, cx);
        }
        return srid;
    }

    @Override
    public int getGeometryDimension(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        // Try the metadata table first
        if (tableExists(schemaName, METADATA_TABLE_NAME, cx)) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT DIMENSION FROM ");
            encodeIdentifiers(sql, schemaName, METADATA_TABLE_NAME);
            sql.append(" WHERE TABLE_NAME = ? AND COLUMN_NAME = ?");

            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = cx.prepareStatement(sql.toString());
                ps.setString(1, tableName);
                ps.setString(2, columnName);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } finally {
                dataStore.closeSafe(rs);
                dataStore.closeSafe(ps);
            }
        }

        // Try the first non-NULL geometry
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        encodeIdentifiers(sql, columnName);
        sql.append(".ST_CoordDim() FROM ");
        encodeIdentifiers(sql, schemaName, tableName);
        sql.append(" WHERE ");
        encodeIdentifiers(sql, columnName);
        sql.append(" IS NOT NULL LIMIT 1");

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = cx.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(ps);
        }

        // Fallback
        return 2;
    }

    @Override
    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = cx.prepareStatement(
                    "SELECT ORGANIZATION, ORGANIZATION_COORDSYS_ID, DEFINITION FROM PUBLIC.ST_SPATIAL_REFERENCE_SYSTEMS WHERE SRS_ID = ?");
            ps.setInt(1, srid);
            rs = ps.executeQuery();
            if (!rs.next()) return null;

            String org = rs.getString(1);
            int orgId = rs.getInt(2);
            String wkt = rs.getString(3);
            if (org != null) {
                try {
                    return CRS.decode(org + ":" + orgId);
                } catch (Exception e) {
                    LOGGER.log(
                            Level.WARNING, "Could not decode " + org + ":" + orgId + " using the geotools database", e);
                }
            }

            if (wkt != null) {
                try {
                    return CRS.parseWKT(wkt);
                } catch (FactoryException e) {
                    LOGGER.log(Level.WARNING, "Could not decode wkt definition for " + srid, e);
                }
            }

            return null;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(ps);
        }
    }

    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        if (!isEstimatedExtentsEnabled()) return super.getOptimizedBounds(schema, featureType, cx);

        if (!isExtentEstimationAvailable()) {
            LOGGER.log(
                    Level.WARNING,
                    "Could not use fast extent estimation. This feature is available starting with HANA Cloud QRC1/2024 and HANA 2 SPS 08.");
            return null;
        }

        String tableName = featureType.getTypeName();
        if (dataStore.getVirtualTables().get(tableName) != null) return null;

        List<ReferencedEnvelope> result = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        schema = resolveSchema(schema, cx);
        try {
            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    StringBuffer sql = new StringBuffer();
                    sql.append(
                            "SELECT MIN_X, MAX_X, MIN_Y, MAX_Y FROM SYS.M_ST_GEOMETRY_COLUMNS WHERE SCHEMA_NAME=? AND TABLE_NAME=? AND COLUMN_NAME=?");

                    ps = cx.prepareStatement(sql.toString());
                    ps.setString(1, schema);
                    ps.setString(2, tableName);
                    ps.setString(3, att.getName().getLocalPart());

                    rs = ps.executeQuery();
                    if (rs.next()) {
                        double xMin = rs.getDouble(1);
                        double xMax = rs.getDouble(2);
                        double yMin = rs.getDouble(3);
                        double yMax = rs.getDouble(4);

                        // Check if row for column is available and data is present
                        if (rs.wasNull()) return null;

                        result.add(new ReferencedEnvelope(
                                xMin,
                                xMax,
                                yMin,
                                yMax,
                                CRS.getHorizontalCRS(featureType.getCoordinateReferenceSystem())));
                    }

                    rs.close();
                    ps.close();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Fast Extent Estimation failed!", e);
            return null;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(ps);
        }

        if (result.isEmpty()) return null;
        return result;
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        sql.append("MIN(");
        encodeIdentifiers(sql, geometryColumn);
        sql.append(".ST_XMin())");

        sql.append(" || ':' || ");
        sql.append("MIN(");
        encodeIdentifiers(sql, geometryColumn);
        sql.append(".ST_YMin())");

        sql.append(" || ':' || ");
        sql.append("MAX(");
        encodeIdentifiers(sql, geometryColumn);
        sql.append(".ST_XMax())");

        sql.append(" || ':' || ");
        sql.append("MAX(");
        encodeIdentifiers(sql, geometryColumn);
        sql.append(".ST_YMax())");
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        String senvelope = rs.getString(column);
        if (senvelope == null) return new Envelope();
        String[] comps = senvelope.split(":");
        assert comps.length == 4;
        double x1 = Double.parseDouble(comps[0]);
        double y1 = Double.parseDouble(comps[1]);
        double x2 = Double.parseDouble(comps[2]);
        double y2 = Double.parseDouble(comps[3]);
        return new Envelope(x1, x2, y1, y2);
    }

    @Override
    public void encodeGeometryColumn(
            GeometryDescriptor gatt, String prefix, int srid, org.geotools.util.factory.Hints hints, StringBuffer sql) {
        encodeColumnName(prefix, gatt.getLocalName(), sql);
        sql.append(".ST_AsBinary()");
    }

    @Override
    public void encodeGeometryColumnSimplified(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        encodeColumnName(prefix, gatt.getLocalName(), sql);
        if (distance != null
                && distance >= 0.0
                && !simplifyDisabled
                && isPlanarCRS(gatt.getCoordinateReferenceSystem())) {
            sql.append(".ST_Simplify(");
            sql.append(distance.toString());
            sql.append(")");
        }
        sql.append(".ST_AsBinary()");
    }

    private boolean isPlanarCRS(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return false;
        }
        CoordinateReferenceSystem hcrs = CRS.getHorizontalCRS(crs);
        if (hcrs == null) {
            return false;
        }
        return !(hcrs instanceof GeographicCRS);
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String column,
            GeometryFactory factory,
            Connection cx,
            org.geotools.util.factory.Hints hints)
            throws IOException, SQLException {
        try {
            return parseWkb(rs.getBytes(column), factory);
        } catch (HanaWKBParserException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            int column,
            GeometryFactory factory,
            Connection cx,
            org.geotools.util.factory.Hints hints)
            throws IOException, SQLException {
        try {
            return parseWkb(rs.getBytes(column), factory);
        } catch (HanaWKBParserException e) {
            throw new IOException(e);
        }
    }

    private Geometry parseWkb(byte[] wkb, GeometryFactory factory) throws HanaWKBParserException {
        if (wkb == null) return null;
        HanaWKBParser parser = new HanaWKBParser(factory);
        return parser.parse(wkb);
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(null, column, sql);
        sql.append(" INTEGER PRIMARY KEY");
    }

    @Override
    public void encodeCreateTable(StringBuffer sql) {
        sql.append("CREATE COLUMN TABLE ");
    }

    @Override
    public void encodePostColumnCreateTable(AttributeDescriptor att, StringBuffer sql) {
        if (att instanceof GeometryDescriptor) {
            // HANA expects the SRID right after the type, e.g. ST_Geometry(4326).
            // Unfortunately, the data store might already have inserted column constraints like
            // "NOT NULL". Therefore, we have to find the type string and insert the SRID right
            // behind it.
            GeometryDescriptor gd = (GeometryDescriptor) att;
            Integer srid = (Integer) gd.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
            if (srid == null) {
                srid = getSridFromCRS(gd);
            }
            if (srid == null) {
                return;
            }

            int index = sql.lastIndexOf(GEOMETRY_TYPE_NAME);
            if (index < 0) {
                throw new AssertionError();
            }
            String sridConstraint = "(" + Integer.toString(srid) + ")";
            sql.insert(index + GEOMETRY_TYPE_NAME.length(), sridConstraint);
        }
    }

    private Integer getSridFromCRS(GeometryDescriptor gd) {
        if (gd.getCoordinateReferenceSystem() == null) {
            return null;
        }
        try {
            CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
            Integer srid = CRS.lookupEpsgCode(crs, true);

            // Unlike other databases, HANA does not distinguish between a geometry and a geography
            // datatype. Instead, HANA carries out its computation on the surface of an ellipsoid
            // of revolution if the CRS is geographic. However, lots of software expects that
            // computations are carried out on a Euclidean plane. Therefore, by convention "flat"
            // versions of geographics CRS' are created with an SRID offset of 10^9. In GeoTools
            // the default seems to be to use the "flat" version. We adapt the SRID here.
            if (srid != null) {
                SingleCRS hcrs = CRS.getHorizontalCRS(crs);
                if (hcrs instanceof GeographicCRS) {
                    srid += 1000000000;
                }
            }
            return srid;
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Error looking up the epsg code for metadata insertion", e);
        }
        return null;
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        registerMetadata(schemaName, featureType, cx);
        createSequences(schemaName, featureType, cx);
    }

    private void registerMetadata(String schemaName, SimpleFeatureType featureType, Connection cx) throws SQLException {
        String tableName = featureType.getName().getLocalPart();
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            if (!(att instanceof GeometryDescriptor)) {
                continue;
            }
            GeometryDescriptor gd = (GeometryDescriptor) att;
            registerMetadata(schemaName, tableName, gd, cx);
        }
    }

    private void registerMetadata(String schemaName, String tableName, GeometryDescriptor gd, Connection cx)
            throws SQLException {
        // HANA accepts 2-, 3- and 4-dimensional geometries in each geometry column.
        // Therefore, we store the information about the dimension in an extra metadata table.
        int dimensions = 2;
        Integer dimHint = (Integer) gd.getUserData().get(org.geotools.util.factory.Hints.COORDINATE_DIMENSION);
        if (dimHint != null) {
            dimensions = dimHint;
        }
        if (!tableExists(schemaName, METADATA_TABLE_NAME, cx)) {
            createMetadataTable(schemaName, cx);
        }
        String columnName = gd.getLocalName();

        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        encodeIdentifiers(sql, schemaName, METADATA_TABLE_NAME);
        sql.append(" VALUES(?, ?, ?)");

        PreparedStatement ps = cx.prepareStatement(sql.toString());
        try {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            ps.setInt(3, dimensions);
            ps.executeUpdate();
        } finally {
            dataStore.closeSafe(ps);
        }
    }

    private void createMetadataTable(String schemaName, Connection cx) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE COLUMN TABLE ");
        encodeIdentifiers(sql, schemaName, METADATA_TABLE_NAME);
        sql.append(
                " (TABLE_NAME NVARCHAR(256), COLUMN_NAME NVARCHAR(256), DIMENSION INT, PRIMARY KEY (TABLE_NAME, COLUMN_NAME))");
        Statement stmt = cx.createStatement();
        try {
            stmt.execute(sql.toString());
        } finally {
            dataStore.closeSafe(stmt);
        }
    }

    private void createSequences(String schemaName, SimpleFeatureType featureType, Connection cx) throws SQLException {
        schemaName = resolveSchema(schemaName, cx);
        String tableName = featureType.getTypeName();
        List<String> pkColumns = getPrimaryKeys(schemaName, tableName, cx);

        for (String pkColumn : pkColumns) {
            String sequenceName = getSequenceName(tableName, pkColumn);
            StringBuffer sql = new StringBuffer();
            sql.append("CREATE SEQUENCE ");
            encodeIdentifiers(sql, schemaName, sequenceName);
            sql.append(" MINVALUE 0");
            Statement stmt = null;
            try {
                stmt = cx.createStatement();
                stmt.execute(sql.toString());
            } finally {
                dataStore.closeSafe(stmt);
            }
        }
    }

    @Override
    public void preDropTable(String schemaName, SimpleFeatureType featureType, Connection cx) throws SQLException {
        schemaName = resolveSchema(schemaName, cx);
        String tableName = featureType.getTypeName();
        List<String> pkColumns = getPrimaryKeys(schemaName, tableName, cx);

        for (String pkColumn : pkColumns) {
            String sequenceName = getSequenceName(tableName, pkColumn);
            StringBuffer sql = new StringBuffer();
            sql.append("DROP SEQUENCE ");
            encodeIdentifiers(sql, schemaName, sequenceName);
            Statement stmt = null;
            try {
                stmt = cx.createStatement();
                stmt.execute(sql.toString());
            } finally {
                dataStore.closeSafe(stmt);
            }
        }
    }

    @Override
    public void postDropTable(String schemaName, SimpleFeatureType featureType, Connection cx) throws SQLException {
        if (!tableExists(schemaName, METADATA_TABLE_NAME, cx)) {
            return;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
        encodeIdentifiers(sql, schemaName, METADATA_TABLE_NAME);
        sql.append(" WHERE TABLE_NAME = ?");

        PreparedStatement ps = cx.prepareStatement(sql.toString());
        try {
            ps.setString(1, featureType.getTypeName());
            ps.executeUpdate();
        } finally {
            dataStore.closeSafe(ps);
        }
    }

    private List<String> getPrimaryKeys(String schemaName, String tableName, Connection cx) throws SQLException {
        DatabaseMetaData dbmd = cx.getMetaData();

        List<String> pkColumns = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = dbmd.getPrimaryKeys(null, schemaName, tableName);
            while (rs.next()) {
                pkColumns.add(rs.getString(4));
            }
        } finally {
            dataStore.closeSafe(rs);
        }
        return pkColumns;
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return false;
    }

    @Override
    public Object getNextAutoGeneratedValue(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        String sequenceName = getSequenceForColumn(schemaName, tableName, columnName, cx);
        return getNextSequenceValue(schemaName, sequenceName, cx);
    }

    @Override
    public String getSequenceForColumn(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        String sequenceName = getSequenceName(tableName, columnName);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (schemaName == null) {
                ps = cx.prepareStatement(
                        "SELECT COUNT(*) FROM PUBLIC.SEQUENCES WHERE SCHEMA_NAME = CURRENT_SCHEMA AND SEQUENCE_NAME = ?");
                ps.setString(1, sequenceName);
            } else {
                ps = cx.prepareStatement(
                        "SELECT COUNT(*) FROM PUBLIC.SEQUENCES WHERE SCHEMA_NAME = ? AND SEQUENCE_NAME = ?");
                ps.setString(1, schemaName);
                ps.setString(2, sequenceName);
            }
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new AssertionError();
            }
            int count = rs.getInt(1);
            return count == 1 ? sequenceName : null;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(ps);
        }
    }

    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx) throws SQLException {
        String sql = "SELECT " + encodeNextSequenceValue(schemaName, sequenceName) + " FROM DUMMY";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = cx.createStatement();
            rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                throw new AssertionError();
            }
            return rs.getInt(1);
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(stmt);
        }
    }

    @Override
    public String encodeNextSequenceValue(String schemaName, String sequenceName) {
        StringBuffer sql = new StringBuffer();
        encodeIdentifiers(sql, schemaName, sequenceName);
        sql.append(".NEXTVAL");
        return sql.toString();
    }

    private String getSequenceName(String tableName, String columnName) {
        return tableName + "_" + columnName + SEQUENCE_SUFFIX;
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return true;
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        sql.append(" LIMIT ");
        sql.append(limit);
        sql.append(" OFFSET ");
        sql.append(offset);
    }

    @Override
    protected void addSupportedHints(Set<org.geotools.util.factory.Hints.Key> hints) {
        if (!simplifyDisabled) {
            if (hanaVersion.getVersion() > 2 || hanaVersion.getVersion() == 2 && hanaVersion.getRevision() >= 40) {
                hints.add(Hints.GEOMETRY_SIMPLIFICATION);
            }
        }
    }

    @Override
    public int getDefaultVarcharSize() {
        return 255;
    }

    @Override
    protected boolean supportsSchemaForIndex() {
        return true;
    }

    @Override
    public void handleSelectHints(StringBuffer sql, SimpleFeatureType featureType, Query query) {
        if (selectHints == null || selectHints.trim().isEmpty()) {
            return;
        }
        sql.append(" WITH HINT( ");
        sql.append(selectHints);
        sql.append(" )");
    }

    @Override
    public boolean applyHintsOnVirtualTables() {
        return true;
    }

    @Override
    public String[] getDesiredTablesType() {
        return new String[] {"TABLE", "OLAP VIEW", "JOIN VIEW", "VIEW", "CALC VIEW", "SYNONYM"};
    }

    @Override
    public void prepareGeometryValue(
            Class<? extends Geometry> gClass, int dimension, int srid, Class binding, StringBuffer sql) {
        sql.append("ST_GeomFromWKB(?, ");
        sql.append(srid);
        sql.append(")");
    }

    @Override
    public void setGeometryValue(Geometry g, int dimension, int srid, Class binding, PreparedStatement ps, int column)
            throws SQLException {
        if (g != null) {
            dimension = Math.min(dimension, HanaDimensionFinder.findDimension(g));
            try {
                byte[] wkb = HanaWKBWriter.write(g, dimension);
                ps.setBytes(column, wkb);
            } catch (HanaWKBWriterException e) {
                throw new SQLException(e);
            }
        } else {
            ps.setNull(column, Types.BLOB);
        }
    }

    @Override
    public void setValue(
            Object value, Class<?> binding, AttributeDescriptor att, PreparedStatement ps, int column, Connection cx)
            throws SQLException {
        if (value == null) {
            super.setValue(value, binding, att, ps, column, cx);
            return;
        }
        Integer sqlType = dataStore.getMapping(binding, att);
        switch (sqlType) {
            case Types.TIME:
                // HANA cannot deal with time instances where the date part is before 1970.
                // We re-create the time with a proper date part.
                Time time = Time.valueOf(convert(value, Time.class).toString());
                ps.setTime(column, time);
                break;
            default:
                super.setValue(value, binding, att, ps, column, cx);
        }
    }

    @Override
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        return new HanaFilterToSQL(this, functionEncodingEnabled, hanaVersion);
    }

    private String resolveSchema(String schemaName, Connection cx) throws SQLException, AssertionError {
        if (schemaName == null) {
            schemaName = getCurrentSchema(cx);
        }
        return schemaName;
    }

    private String getCurrentSchema(Connection cx) throws SQLException {
        String schemaName;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = cx.createStatement();
            rs = stmt.executeQuery("SELECT CURRENT_SCHEMA FROM DUMMY");
            if (!rs.next()) {
                throw new AssertionError();
            }
            schemaName = rs.getString(1);
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(stmt);
        }
        return schemaName;
    }

    private boolean tableExists(String schemaName, String tableName, Connection cx) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (schemaName == null) {
                ps = cx.prepareStatement(
                        "SELECT COUNT(*) FROM PUBLIC.TABLES WHERE SCHEMA_NAME = CURRENT_SCHEMA AND TABLE_NAME = ?");
                ps.setString(1, tableName);
            } else {
                ps = cx.prepareStatement("SELECT COUNT(*) FROM PUBLIC.TABLES WHERE SCHEMA_NAME = ? AND TABLE_NAME = ?");
                ps.setString(1, schemaName);
                ps.setString(2, tableName);
            }
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new AssertionError();
            }
            int count = rs.getInt(1);
            return count == 1;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(ps);
        }
    }

    private void encodeIdentifiers(StringBuffer sb, String... ids) {
        boolean first = true;
        for (String id : ids) {
            if (id == null) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append('.');
            }
            sb.append(HanaUtil.encodeIdentifier(id));
        }
    }

    private HanaCloudVersion queryCloudVersion(Connection cx) throws SQLException {
        if (hanaVersion.getVersion() < 4) return HanaCloudVersion.INVALID_VERSION;

        Statement st = null;
        ResultSet rs = null;
        try {
            st = cx.createStatement();
            rs = st.executeQuery("SELECT CLOUD_VERSION FROM SYS.M_DATABASE");

            if (!rs.next()) throw new RuntimeException("Unable to determine HANA Cloud Version.");

            String cloudVersion = rs.getString(1);
            return new HanaCloudVersion(cloudVersion);
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }
    }

    private boolean isExtentEstimationAvailable() {
        switch (hanaVersion.getVersion()) {
            case 2:
                return hanaVersion.compareTo(new HanaVersion(2, 0, 80, 0)) >= 0;
            case 4:
                return cloudVersion.compareTo(new HanaCloudVersion(2024, 2, 0)) >= 0;
        }

        return false;
    }

    private class SchemaCache {

        public SchemaCache() {}

        private Connection cx;

        private String currentSchema;

        public String getSchema(Connection cx) throws SQLException {
            if (cx == this.cx) {
                return currentSchema;
            }
            currentSchema = getCurrentSchema(cx);
            this.cx = cx;
            return currentSchema;
        }
    }
}
