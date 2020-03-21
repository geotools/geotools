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
package org.geotools.data.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.Datum;
import oracle.sql.STRUCT;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.sdo.GeometryConverter;
import org.geotools.data.oracle.sdo.SDOSqlDumper;
import org.geotools.data.oracle.sdo.TT;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.referencing.CRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.util.GenericName;

/**
 * Abstract dialect implementation for Oracle. Subclasses differ on the way used to parse and encode
 * the JTS geoemtries into Oracle MDSYS.SDO_GEOMETRY structures.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Andrea Aime, OpenGEO
 */
public class OracleDialect extends PreparedStatementSQLDialect {

    /**
     * Sentinel value used to mark that the unwrapper lookup happened already, and an unwrapper was
     * not found
     */
    UnWrapper UNWRAPPER_NOT_FOUND =
            new UnWrapper() {

                @Override
                public Statement unwrap(Statement statement) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Connection unwrap(Connection conn) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean canUnwrap(Statement st) {
                    return false;
                }

                @Override
                public boolean canUnwrap(Connection conn) {
                    return false;
                }
            };

    private static final int DEFAULT_AXIS_MAX = 10000000;

    private static final int DEFAULT_AXIS_MIN = -10000000;

    private static final Pattern AXIS_NAME_VALIDATOR = Pattern.compile("^[\\w]{1,30}");

    /** Marks a geometry column as geodetic */
    public static final String GEODETIC = "geodetic";

    /**
     * Map of <code>UnWrapper</code> objects keyed by the class of <code>Connection</code> it is an
     * unwrapper for. This avoids the overhead of searching the <code>DataSourceFinder</code>
     * service registry at each unwrap.
     */
    Map<Class<? extends Connection>, UnWrapper> uwMap =
            new HashMap<Class<? extends Connection>, UnWrapper>();

    private int nameLenghtLimit = 30;

    /**
     * A map from JTS Geometry type to Oracle geometry type. See Oracle Spatial documentation, Table
     * 2-1, Valid SDO_GTYPE values.
     */
    public static final Map<Class, String> CLASSES_TO_GEOM =
            Collections.unmodifiableMap(new GeomClasses());

    public void initVersion(Connection cx) {
        // try to figure out if longer names are supported by the database
        try {
            final int databaseMajorVersion = cx.getMetaData().getDatabaseMajorVersion();
            if (databaseMajorVersion >= 12) {
                nameLenghtLimit = 128;
            }
        } catch (SQLException e) {
            LOGGER.log(
                    Level.WARNING,
                    "Failed to determine database major version, "
                            + "will assume length cannot be longer than 30 chars",
                    e);
        }
    }

    static final class GeomClasses extends HashMap<Class, String> {
        private static final long serialVersionUID = -3359664692996608331L;

        public GeomClasses() {
            super();
            put(Point.class, "POINT");
            put(LineString.class, "LINE");
            put(LinearRing.class, "LINE");
            put(Polygon.class, "POLYGON");
            put(GeometryCollection.class, "COLLECTION");
            put(MultiPoint.class, "MULTIPOINT");
            put(MultiLineString.class, "MULTILINE");
            put(MultiPolygon.class, "MULTIPOLYGON");
        }
    }

    static final Map<String, Class> TYPES_TO_CLASSES =
            new HashMap<String, Class>() {
                {
                    put("CHAR", String.class);
                    put("NCHAR", String.class);
                    put("NVARCHAR", String.class);
                    put("NVARCHAR2", String.class);
                    put("DATE", java.sql.Date.class);
                    put("TIMESTAMP", java.sql.Timestamp.class);
                }
            };

    /** Whether to use only primary filters for BBOX filters */
    boolean looseBBOXEnabled = false;

    /** Whether to use estimated extents to build */
    boolean estimatedExtentsEnabled = false;

    /**
     * Stores srid and their nature, true if geodetic, false otherwise. Avoids repeated accesses to
     * the MDSYS.GEODETIC_SRIDS table
     */
    SoftValueHashMap<Integer, Boolean> geodeticCache = new SoftValueHashMap<Integer, Boolean>(20);

    /** Remembers whether the USER_SDO_* views could be accessed or not */
    Boolean canAccessUserViews;

    /** The direct geometry metadata table, if any */
    String geometryMetadataTable;

    /** Whether to use metadata tables to get bbox */
    boolean metadataBboxEnabled = false;

    public OracleDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    @Override
    public boolean isAggregatedSortSupported(String function) {
        return "distinct".equalsIgnoreCase(function);
    }

    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

    public boolean isEstimatedExtentsEnabled() {
        return estimatedExtentsEnabled;
    }

    public void setEstimatedExtentsEnabled(boolean estimatedExtenstEnabled) {
        this.estimatedExtentsEnabled = estimatedExtenstEnabled;
    }

    /**
     * Checks the user has permissions to read from the USER_SDO_INDEX_METADATA and
     * USER_SDO_GEOM_METADATA. The code can use this information to decide to access the
     * ALL_SDO_INDEX_METADATA and ALL_SOD_GEOM_METADATA views instead.
     */
    boolean canAccessUserViews(Connection cx) {
        if (canAccessUserViews == null) {
            Statement st = null;
            ResultSet rs = null;
            try {
                st = cx.createStatement();
                String sql = "SELECT * FROM MDSYS.USER_SDO_INDEX_METADATA WHERE ROWNUM < 2";
                LOGGER.log(Level.FINE, "Check user can access user metadata views: {0}", sql);
                rs = st.executeQuery(sql);
                dataStore.closeSafe(rs);

                sql = "SELECT * FROM MDSYS.USER_SDO_GEOM_METADATA WHERE ROWNUM < 2";
                LOGGER.log(Level.FINE, "Check user can access user metadata views: {0}", sql);
                LOGGER.log(Level.FINE, sql);
                rs = st.executeQuery(sql);
                dataStore.closeSafe(rs);

                canAccessUserViews = true;
            } catch (SQLException e) {
                canAccessUserViews = false;
            } finally {
                dataStore.closeSafe(st);
                dataStore.closeSafe(rs);
            }
        }
        return canAccessUserViews;
    }

    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx) throws SQLException {
        final int TABLE_NAME = 3;
        final int COLUMN_NAME = 4;
        final int TYPE_NAME = 6;
        String typeName = columnMetaData.getString(TYPE_NAME);
        if (typeName.equals("SDO_GEOMETRY")) {
            String tableName = columnMetaData.getString(TABLE_NAME);
            String columnName = columnMetaData.getString(COLUMN_NAME);
            String schema = dataStore.getDatabaseSchema();

            Class geometryClass = lookupGeometryOnMetadataTable(cx, tableName, columnName, schema);
            if (geometryClass == null) {
                lookupGeometryClassOnUserIndex(cx, tableName, columnName, schema);
            }
            if (geometryClass == null) {
                geometryClass = lookupGeometryClassOnAllIndex(cx, tableName, columnName, schema);
            }
            if (geometryClass == null) {
                geometryClass = Geometry.class;
            }

            return geometryClass;
        } else {
            // if we know, return non null value, otherwise returning
            // null will force the datatore to figure it out using
            // jdbc metadata
            return TYPES_TO_CLASSES.get(typeName);
        }
    }

    /** Tries to use the geometry metadata table, if available */
    private Class<?> lookupGeometryOnMetadataTable(
            Connection cx, String tableName, String columnName, String schema) throws SQLException {
        if (geometryMetadataTable == null) {
            return null;
        }

        List<String> parameters = new ArrayList<String>();

        // setup the sql to use for the ALL_SDO table
        String metadataTableStatement =
                "SELECT TYPE FROM "
                        + geometryMetadataTable
                        + " WHERE F_TABLE_NAME = ?"
                        + " AND F_GEOMETRY_COLUMN = ?";

        parameters.add(tableName);
        parameters.add(columnName);

        if (schema != null && !"".equals(schema)) {
            metadataTableStatement += " AND F_TABLE_SCHEMA = ?";
            parameters.add(schema);
        }

        return readGeometryClassFromStatement(cx, metadataTableStatement, parameters);
    }

    /** Looks up the geometry type on the "ALL_*" metadata views */
    private Class<?> lookupGeometryClassOnAllIndex(
            Connection cx, String tableName, String columnName, String schema) throws SQLException {
        List<String> parameters = new ArrayList<String>();

        // setup the sql to use for the ALL_SDO table
        String allSdoSqlStatement =
                "SELECT META.SDO_LAYER_GTYPE\n"
                        + "FROM ALL_INDEXES INFO\n"
                        + "INNER JOIN MDSYS.ALL_SDO_INDEX_METADATA META\n"
                        + "ON INFO.INDEX_NAME = META.SDO_INDEX_NAME\n"
                        + "WHERE INFO.TABLE_NAME = ?\n"
                        + "AND REPLACE(meta.sdo_column_name, '\"') = ?\n";

        parameters.add(tableName);
        parameters.add(columnName);

        if (schema != null && !"".equals(schema)) {
            allSdoSqlStatement += " AND INFO.TABLE_OWNER = ?";
            parameters.add(schema);
            allSdoSqlStatement += " AND META.SDO_INDEX_OWNER = ?";
            parameters.add(schema);
        }

        return readGeometryClassFromStatement(cx, allSdoSqlStatement, parameters);
    }

    /** Looks up the geometry type on the "USER_*" metadata views */
    private Class lookupGeometryClassOnUserIndex(
            Connection cx, String tableName, String columnName, String schema) throws SQLException {
        // we only try this if we are able to access the
        // user_sdo views
        if (!canAccessUserViews(cx)) {
            return null;
        }

        List<String> parameters = new ArrayList<String>();

        // setup the sql to use for the USER_SDO table
        String userSdoSqlStatement =
                "SELECT META.SDO_LAYER_GTYPE\n"
                        + "FROM ALL_INDEXES INFO\n"
                        + "INNER JOIN MDSYS.USER_SDO_INDEX_METADATA META\n"
                        + "ON INFO.INDEX_NAME = META.SDO_INDEX_NAME\n"
                        + "WHERE INFO.TABLE_NAME = ?\n"
                        + "AND REPLACE(meta.sdo_column_name, '\"') = ?\n";
        parameters.add(tableName);
        parameters.add(columnName);

        if (schema != null && !"".equals(schema)) {
            userSdoSqlStatement += " AND INFO.TABLE_OWNER = ?";
            parameters.add(schema);
        }

        return readGeometryClassFromStatement(cx, userSdoSqlStatement, parameters);
    }

    /**
     * Reads the geometry type from the first column returned by executing the specified SQL
     * statement
     */
    private Class readGeometryClassFromStatement(Connection cx, String sql, List<String> parameters)
            throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            LOGGER.log(
                    Level.FINE,
                    "Geometry type check; {0} [ parameters = {1} ]",
                    new Object[] {sql, parameters});
            st = cx.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                st.setString(i + 1, parameters.get(i));
            }
            rs = st.executeQuery();
            if (rs.next()) {
                String gType = rs.getString(1);
                Class geometryClass = (Class) TT.GEOM_CLASSES.get(gType);
                if (geometryClass == null) {
                    // if there was a record but it's not a recognized geometry type fall back on
                    // geometry for backwards compatibility, but at least log the info
                    LOGGER.fine(
                            "Unrecognized geometry type "
                                    + gType
                                    + " falling back on generic 'GEOMETRY'");
                    geometryClass = Geometry.class;
                }

                return geometryClass;
            }
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }

        return null;
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {

        if (tableName.endsWith("$")) {
            return false;
        } else if (tableName.startsWith("BIN$")) { // Added to ignore some Oracle 10g tables
            return false;
        } else if (tableName.startsWith("XDB$")) {
            return false;
        } else if (tableName.startsWith("DR$")) {
            return false;
        } else if (tableName.startsWith("DEF$")) {
            return false;
        } else if (tableName.startsWith("SDO_")) {
            return false;
        } else if (tableName.startsWith("WM$")) {
            return false;
        } else if (tableName.startsWith("WK$")) {
            return false;
        } else if (tableName.startsWith("AW$")) {
            return false;
        } else if (tableName.startsWith("AQ$")) {
            return false;
        } else if (tableName.startsWith("APPLY$")) {
            return false;
        } else if (tableName.startsWith("REPCAT$")) {
            return false;
        } else if (tableName.startsWith("CWM$")) {
            return false;
        } else if (tableName.startsWith("CWM2$")) {
            return false;
        } else if (tableName.startsWith("EXF$")) {
            return false;
        } else if (tableName.startsWith("DM$")) {
            return false;
        } else if (tableName.startsWith("MDXT_")
                && (tableName.endsWith("$_BKTS") || tableName.endsWith("$_MBR"))) {
            return false;
        }

        return true;
    }

    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put("SDO_GEOMETRY", Geometry.class);
        mappings.put("MDSYS.SDO_GEOMETRY", Geometry.class);
    }

    @Override
    public String getNameEscape() {
        return "";
    }

    @Override
    public void encodeColumnName(String prefix, String raw, StringBuffer sql) {
        if (prefix != null && !prefix.isEmpty()) {
            prefix = prefix.toUpperCase();
            if (prefix.length() > nameLenghtLimit) {
                prefix = prefix.substring(0, nameLenghtLimit);
            }
            sql.append(prefix).append(".");
        }

        raw = raw.toUpperCase();
        if (raw.length() > nameLenghtLimit) raw = raw.substring(0, nameLenghtLimit);
        // need to quote column names with spaces in
        if (raw.contains(" ")) {
            raw = "\"" + raw + "\"";
        }
        sql.append(raw);
    }

    @Override
    public void encodeTableName(String raw, StringBuffer sql) {
        raw = raw.toUpperCase();
        if (raw.length() > nameLenghtLimit) raw = raw.substring(0, nameLenghtLimit);
        // need to quote table names with spaces in
        if (raw.contains(" ")) {
            raw = "\"" + raw + "\"";
        }
        sql.append(raw);
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return "MDSYS.SDO_GEOMETRY";
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException, IOException {
        Geometry geom = readGeometry(rs, column, new GeometryFactory(), cx);
        return geom != null ? geom.getEnvelopeInternal() : null;
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

        // read the geometry
        Geometry geom = readGeometry(rs, column, factory, cx);
        return convertGeometry(geom, descriptor, factory);
    }

    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            int column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        // read the geometry
        Geometry geom = readGeometry(rs, column, factory, cx);
        return convertGeometry(geom, descriptor, factory);
    };

    Geometry convertGeometry(
            Geometry geom, GeometryDescriptor descriptor, GeometryFactory factory) {
        // if the geometry is null no need to convert it
        if (geom == null) {
            return null;
        }

        // grab the binding
        Class targetClazz = descriptor.getType().getBinding();

        // in Oracle you can have polygons in a column declared to be multipolygon, and so on...
        // so we better convert geometries, since our feature model is not so lenient
        if (targetClazz.equals(MultiPolygon.class) && geom instanceof Polygon) {
            return factory.createMultiPolygon(new Polygon[] {(Polygon) geom});
        } else if (targetClazz.equals(MultiPoint.class) && geom instanceof Point) {
            return factory.createMultiPoint(new Point[] {(Point) geom});
        } else if (targetClazz.equals(MultiLineString.class) && geom instanceof LineString) {
            return factory.createMultiLineString(new LineString[] {(LineString) geom});
        } else if (targetClazz.equals(GeometryCollection.class)) {
            return factory.createGeometryCollection(new Geometry[] {geom});
        }
        return geom;
    }

    Geometry readGeometry(ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        return readGeometry(rs.getObject(column), factory, cx);
    }

    Geometry readGeometry(ResultSet rs, int column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        return readGeometry(rs.getObject(column), factory, cx);
    }

    @SuppressWarnings("PMD.CloseResource") // the connection is managed by the caller
    Geometry readGeometry(Object struct, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        if (struct == null) {
            return null;
        }

        // unwrap the connection and create a converter
        OracleConnection ocx = unwrapConnection(cx);
        GeometryConverter converter =
                factory != null ? new GeometryConverter(ocx, factory) : new GeometryConverter(ocx);

        return converter.asGeometry((STRUCT) struct);
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // the connection and ps are managed by the caller
    public void setGeometryValue(
            Geometry g, int dimension, int srid, Class binding, PreparedStatement ps, int column)
            throws SQLException {

        // Handle the null geometry case.
        // Surprisingly, using setNull(column, Types.OTHER) does not work...
        if (g == null || g.isEmpty()) {
            ps.setNull(column, Types.STRUCT, "MDSYS.SDO_GEOMETRY");
            return;
        }

        OracleConnection ocx = unwrapConnection(ps.getConnection());

        GeometryConverter converter = new GeometryConverter(ocx);
        STRUCT s = converter.toSDO(g, srid);
        ps.setObject(column, s);

        if (LOGGER.isLoggable(Level.FINE)) {
            String sdo;
            try {
                // the dumper cannot translate all types of geometries
                sdo = SDOSqlDumper.toSDOGeom(g, srid);
            } catch (Exception e) {
                sdo =
                        "Could not translate this geometry into a SDO string, "
                                + "WKT representation is: "
                                + g;
            }
            LOGGER.fine("Setting parameter " + column + " as " + sdo);
        }
    }

    /** Obtains the native oracle connection object given a database connecetion. */
    @SuppressWarnings("PMD.CloseResource")
    OracleConnection unwrapConnection(Connection cx) throws SQLException {
        if (cx == null) {
            return null;
        }

        if (cx instanceof OracleConnection) {
            return (OracleConnection) cx;
        }

        try {
            // Unwrap the connection multiple levels as necessary to get at the underlying
            // OracleConnection. Maintain a map of UnWrappers to avoid searching
            // the registry every time we need to unwrap.
            Connection testCon = cx;
            Connection toUnwrap;
            do {
                UnWrapper unwrapper = uwMap.get(testCon.getClass());
                if (unwrapper == null) {
                    unwrapper = DataSourceFinder.getUnWrapper(testCon);
                    if (unwrapper == null) {
                        unwrapper = UNWRAPPER_NOT_FOUND;
                    }
                    uwMap.put(testCon.getClass(), unwrapper);
                }
                if (unwrapper == UNWRAPPER_NOT_FOUND) {
                    // give up and do Java 6 unwrap below
                    break;
                }
                toUnwrap = testCon;
                testCon = unwrapper.unwrap(testCon);
                if (testCon instanceof OracleConnection) {
                    return (OracleConnection) testCon;
                }
            } while (testCon != null && testCon != toUnwrap);

            if (cx instanceof Wrapper) {
                // try to use java 6 unwrapping
                try {
                    Wrapper w = cx;
                    if (w.isWrapperFor(OracleConnection.class)) {
                        return w.unwrap(OracleConnection.class);
                    }
                } catch (Throwable t) {
                    // not a mistake, old DBCP versions will throw an Error here, we need to catch
                    // it
                    LOGGER.log(
                            Level.FINER, "Failed to unwrap connection using java 6 facilities", t);
                }
            }
        } catch (IOException e) {
            throw (SQLException)
                    new SQLException("Could not obtain native oracle connection.").initCause(e);
        }

        throw new SQLException("Could not obtain native oracle connection for " + cx.getClass());
    }

    public FilterToSQL createFilterToSQL() {
        throw new UnsupportedOperationException("This dialect works with prepared statements only");
    }

    @Override
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        OracleFilterToSQL sql = new OracleFilterToSQL(this);
        sql.setLooseBBOXEnabled(looseBBOXEnabled);

        return sql;
    }

    @Override
    public Integer getGeometrySRID(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {

        Integer srid = lookupSRIDOnMetadataTable(schemaName, tableName, columnName, cx);
        if (srid == null) {
            srid = lookupSRIDFromUserViews(tableName, columnName, cx);
        }
        if (srid == null) {
            srid = lookupSRIDFromAllViews(schemaName, tableName, columnName, cx);
        }
        return srid;
    }

    /** Reads the SRID from the geometry metadata table, if available */
    private Integer lookupSRIDOnMetadataTable(
            String schema, String tableName, String columnName, Connection cx) throws SQLException {
        if (geometryMetadataTable == null) {
            return null;
        }

        List<String> parameters = new ArrayList<String>();

        // setup the sql to use for the ALL_SDO table
        String metadataTableStatement =
                "SELECT SRID FROM "
                        + geometryMetadataTable
                        + " WHERE F_TABLE_NAME = ?"
                        + " AND F_GEOMETRY_COLUMN = ?";

        parameters.add(tableName);
        parameters.add(columnName);

        if (schema != null && !"".equals(schema)) {
            metadataTableStatement += " AND F_TABLE_SCHEMA = ?";
            parameters.add(schema);
        }

        return readIntegerFromStatement(cx, metadataTableStatement, parameters);
    }

    /** Reads the SRID from the SDO_ALL* views */
    private Integer lookupSRIDFromAllViews(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        List<String> parameters = new ArrayList<String>();

        String allSdoSql =
                "SELECT SRID FROM MDSYS.ALL_SDO_GEOM_METADATA WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";

        parameters.add(tableName.toUpperCase());
        parameters.add(columnName.toUpperCase());

        if (schemaName != null) {
            allSdoSql += " AND OWNER=?";
            parameters.add(schemaName);
        }

        return readIntegerFromStatement(cx, allSdoSql, parameters);
    }

    /** Reads the SRID from the SDO_USER* views */
    private Integer lookupSRIDFromUserViews(String tableName, String columnName, Connection cx)
            throws SQLException {
        // we run this only if we can access the user views
        if (!canAccessUserViews(cx)) {
            return null;
        }

        List<String> parameters = new ArrayList<String>();

        String userSdoSql =
                "SELECT SRID FROM MDSYS.USER_SDO_GEOM_METADATA WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
        parameters.add(tableName.toUpperCase());
        parameters.add(columnName.toUpperCase());

        return readIntegerFromStatement(cx, userSdoSql, parameters);
    }

    private Integer readIntegerFromStatement(Connection cx, String sql, List<String> parameters)
            throws SQLException {
        PreparedStatement userSdoStatement = null;
        ResultSet userSdoResult = null;
        try {
            LOGGER.log(
                    Level.FINE,
                    "SRID check; {0} [ parameters = {1} ]",
                    new Object[] {sql, parameters});
            userSdoStatement = cx.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                userSdoStatement.setString(i + 1, parameters.get(i));
            }

            userSdoResult = userSdoStatement.executeQuery();
            if (userSdoResult.next()) {
                Object intValue = userSdoResult.getObject(1);
                if (intValue != null) {
                    return ((Number) intValue).intValue();
                }
            }
        } finally {
            dataStore.closeSafe(userSdoResult);
            dataStore.closeSafe(userSdoStatement);
        }

        return null;
    }

    @Override
    public int getGeometryDimension(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        Integer srid = lookupDimensionOnMetadataTable(schemaName, tableName, columnName, cx);
        if (srid == null) {
            srid = lookupDimensionFromUserViews(tableName, columnName, cx);
        }
        if (srid == null) {
            srid = lookupDimensionFromAllViews(schemaName, tableName, columnName, cx);
        }

        if (srid == null) {
            srid = 2;
        }

        return srid;
    }

    /** Reads the dimensionfrom the geometry metadata table, if available */
    private Integer lookupDimensionOnMetadataTable(
            String schema, String tableName, String columnName, Connection cx) throws SQLException {
        if (geometryMetadataTable == null) {
            return null;
        }

        List<String> parameters = new ArrayList<String>();

        // setup the sql to use for the ALL_SDO table
        String metadataTableStatement =
                "SELECT COORD_DIMENSION FROM "
                        + geometryMetadataTable
                        + " WHERE F_TABLE_NAME = ?"
                        + " AND F_GEOMETRY_COLUMN = ?";

        parameters.add(tableName);
        parameters.add(columnName);

        if (schema != null && !"".equals(schema)) {
            metadataTableStatement += " AND F_TABLE_SCHEMA = ?";
            parameters.add(schema);
        }

        return readIntegerFromStatement(cx, metadataTableStatement, parameters);
    }

    /** Reads the SRID from the SDO_ALL* views */
    private Integer lookupDimensionFromAllViews(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        List<String> parameters = new ArrayList<String>();

        String allSdoSql =
                "SELECT DIMINFO FROM MDSYS.ALL_SDO_GEOM_METADATA USGM, table(USGM.DIMINFO) "
                        + "WHERE TABLE_NAME = ? AND COLUMN_NAME= ?";

        parameters.add(tableName.toUpperCase());
        parameters.add(columnName.toUpperCase());

        if (schemaName != null) {
            allSdoSql += " AND OWNER = ?";
            parameters.add(schemaName);
        }

        return readIntegerFromStatement(cx, allSdoSql, parameters);
    }

    /** Reads the SRID from the SDO_USER* views */
    private Integer lookupDimensionFromUserViews(String tableName, String columnName, Connection cx)
            throws SQLException {
        // we run this only if we can access the user views
        if (!canAccessUserViews(cx)) {
            return null;
        }

        List<String> parameters = new ArrayList<String>();

        String userSdoSql =
                "SELECT COUNT(*) FROM MDSYS.USER_SDO_GEOM_METADATA USGM, table(USGM.DIMINFO)"
                        + " WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";

        parameters.add(tableName.toUpperCase());
        parameters.add(columnName.toUpperCase());

        return readIntegerFromStatement(cx, userSdoSql, parameters);
    }

    @Override
    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException {
        // if the official EPSG database has an answer, use that one
        CoordinateReferenceSystem crs = super.createCRS(srid, cx);
        if (crs != null) return crs;

        // otherwise try to decode the WKT, most of the time it's invalid, but
        // for new codes they learned the proper WKT syntax
        String sql = "SELECT WKTEXT FROM MDSYS.CS_SRS WHERE SRID = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = cx.prepareStatement(sql);
            st.setInt(1, srid);
            rs = st.executeQuery();
            if (rs.next()) {
                String wkt = rs.getString(1);
                if (wkt != null) {
                    try {
                        return CRS.parseWKT(wkt);
                    } catch (Exception e) {
                        if (LOGGER.isLoggable(Level.FINE))
                            LOGGER.log(Level.FINE, "Could not parse WKT " + wkt, e);
                        return null;
                    }
                }
            }
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }
        return null;
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        sql.append("SDO_AGGR_MBR(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append(")");
    }

    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(
            String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        if (dataStore.getVirtualTables().get(featureType.getTypeName()) != null) return null;

        // get the bounds very fast from SDO_GEOM_METADATA, if not use SDO_TUNE.EXTENT_OF
        if (metadataBboxEnabled) {
            String tableName = featureType.getTypeName();

            PreparedStatement st = null;
            ResultSet rs = null;
            String sql;

            List<ReferencedEnvelope> result = new ArrayList<ReferencedEnvelope>();
            Savepoint savePoint = null;
            try {
                if (!cx.getAutoCommit()) {
                    savePoint = cx.setSavepoint();
                }

                for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                    if (att instanceof GeometryDescriptor) {
                        String columnName = att.getName().getLocalPart();
                        // check if we can access the MDSYS.USER_SDO_GEOM_METADATA table
                        if (canAccessUserViews(cx)) {
                            sql =
                                    "SELECT DIMINFO FROM MDSYS.USER_SDO_GEOM_METADATA WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";

                            st = cx.prepareStatement(sql);
                            st.setString(1, tableName.toUpperCase());
                            st.setString(2, columnName.toUpperCase());
                            rs = st.executeQuery();

                            if (rs.next()) {
                                // decode the dimension info
                                Envelope env = decodeDiminfoEnvelope(rs, 1);

                                // reproject and merge
                                if (env != null && !env.isNull()) {
                                    CoordinateReferenceSystem crs =
                                            ((GeometryDescriptor) att)
                                                    .getCoordinateReferenceSystem();
                                    result.add(new ReferencedEnvelope(env, crs));
                                    rs.close();
                                    continue;
                                }
                            }
                            dataStore.closeSafe(rs);
                            dataStore.closeSafe(st);
                        }
                        // if we could not retrieve the envelope from USER_SDO_GEOM_METADATA,
                        // try from ALL_SDO_GEOM_METADATA
                        sql =
                                "SELECT DIMINFO FROM MDSYS.ALL_SDO_GEOM_METADATA WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
                        if (schema != null) {
                            sql += " AND OWNER = ?";
                        }
                        st = cx.prepareStatement(sql);
                        st.setString(1, tableName.toUpperCase());
                        st.setString(2, columnName.toUpperCase());

                        if (schema != null) {
                            st.setString(3, schema);
                        }

                        rs = st.executeQuery();

                        if (rs.next()) {
                            // decode the dimension info
                            Envelope env = decodeDiminfoEnvelope(rs, 1);

                            // reproject and merge
                            if (env != null && !env.isNull()) {
                                CoordinateReferenceSystem crs =
                                        ((GeometryDescriptor) att).getCoordinateReferenceSystem();
                                result.add(new ReferencedEnvelope(env, crs));
                            }
                        }

                        dataStore.closeSafe(rs);
                        dataStore.closeSafe(st);
                    }
                }
            } catch (SQLException e) {
                if (savePoint != null) {
                    cx.rollback(savePoint);
                }
                LOGGER.log(
                        Level.WARNING,
                        "Failed to use METADATA DIMINFO, falling back on SDO_TUNE.EXTENT_OF",
                        e);
                return getOptimizedBoundsSDO_TUNE(schema, featureType, cx);
            } finally {
                if (savePoint != null) {
                    cx.rollback(savePoint);
                }
                dataStore.closeSafe(rs);
                dataStore.closeSafe(st);
            }
            return result;
        }
        // could not retrieve bounds from SDO_GEOM_METADATA table or did not want to
        // falling back on SDO_TUNE.EXTENT_OF
        return getOptimizedBoundsSDO_TUNE(schema, featureType, cx);
    }

    public List<ReferencedEnvelope> getOptimizedBoundsSDO_TUNE(
            String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        if (!estimatedExtentsEnabled) return null;

        String tableName;
        if (schema != null && !"".equals(schema)) {
            tableName = schema + "." + featureType.getTypeName();
        } else {
            tableName = featureType.getTypeName();
        }

        Statement st = null;
        ResultSet rs = null;

        List<ReferencedEnvelope> result = new ArrayList<ReferencedEnvelope>();
        Savepoint savePoint = null;
        try {
            st = cx.createStatement();
            if (!cx.getAutoCommit()) {
                savePoint = cx.setSavepoint();
            }

            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    // use estimated extent (optimizer statistics)
                    StringBuffer sql = new StringBuffer();
                    sql.append("select SDO_TUNE.EXTENT_OF('");
                    sql.append(tableName);
                    sql.append("', '");
                    sql.append(att.getName().getLocalPart());
                    sql.append("') FROM DUAL");
                    LOGGER.log(
                            Level.FINE,
                            "Getting the full extent of the table using optimized search: {0}",
                            sql);
                    rs = st.executeQuery(sql.toString());

                    if (rs.next()) {
                        // decode the geometry
                        GeometryDescriptor descriptor = (GeometryDescriptor) att;
                        Geometry geometry = readGeometry(rs, 1, new GeometryFactory(), cx);

                        // Either a ReferencedEnvelope or ReferencedEnvelope3D will be generated
                        // here
                        ReferencedEnvelope env =
                                JTS.bounds(geometry, descriptor.getCoordinateReferenceSystem());

                        // reproject and merge
                        if (env != null && !env.isNull()) {
                            result.add(env);
                        }
                    }
                    rs.close();
                }
            }
        } catch (SQLException e) {
            if (savePoint != null) {
                cx.rollback(savePoint);
            }
            LOGGER.log(
                    Level.WARNING,
                    "Failed to use SDO_TUNE.EXTENT_OF, falling back on envelope aggregation",
                    e);
            return null;
        } finally {
            if (savePoint != null) {
                cx.rollback(savePoint);
            }
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }
        return result;
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        String tableName = featureType.getName().getLocalPart().toUpperCase();
        Statement st = null;
        try {
            st = cx.createStatement();

            // register all geometry columns in the database
            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    GeometryDescriptor geom = (GeometryDescriptor) att;

                    // guess a tolerance, very small value for geographic data, 10cm for non
                    // geographic data
                    // (is there a better way to guess it?), and an extent.
                    // This is a hack for the moment, we need to find a better way to guess the
                    // extents,
                    // but unfortunately there is no reliable way to get the extent of a CRS due to
                    // http://jira.codehaus.org/browse/GEOT-1578
                    double tolerance;
                    int dims;
                    double[] min;
                    double[] max;
                    String[] axisNames;
                    if (geom.getCoordinateReferenceSystem() != null) {
                        CoordinateSystem cs =
                                geom.getCoordinateReferenceSystem().getCoordinateSystem();
                        Object userDims = geom.getUserData().get(Hints.COORDINATE_DIMENSION);
                        if (userDims != null && ((Number) userDims).intValue() > 0) {
                            dims = ((Number) userDims).intValue();
                        } else {
                            dims = cs.getDimension();
                        }
                        min = new double[dims];
                        max = new double[dims];
                        axisNames = new String[dims];
                        double extent = Double.MAX_VALUE;
                        for (int i = 0; i < dims; i++) {
                            if (i < cs.getDimension()) {
                                CoordinateSystemAxis axis = cs.getAxis(i);
                                axisNames[i] = getCompatibleAxisName(axis, i);
                                min[i] =
                                        Double.isInfinite(axis.getMinimumValue())
                                                ? DEFAULT_AXIS_MIN
                                                : axis.getMinimumValue();
                                max[i] =
                                        Double.isInfinite(axis.getMaximumValue())
                                                ? DEFAULT_AXIS_MAX
                                                : axis.getMaximumValue();
                                if (max[i] - min[i] < extent) extent = max[i] - min[i];
                            } else {
                                min[i] = DEFAULT_AXIS_MIN;
                                max[i] = 10000000;
                            }
                        }
                        // 1/10M of the extent
                        tolerance = extent / 10000000;
                    } else {
                        // assume fake values for a 2d ref system
                        dims = 2;
                        axisNames = new String[2];
                        min = new double[2];
                        max = new double[2];
                        axisNames[0] = "X";
                        axisNames[1] = "Y";
                        min[0] = DEFAULT_AXIS_MIN;
                        min[1] = DEFAULT_AXIS_MIN;
                        max[0] = 10000000;
                        max[1] = 10000000;
                        tolerance = 0.01;
                    }

                    int srid = -1;
                    if (geom.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID) != null) {
                        srid = (Integer) geom.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
                    } else if (geom.getCoordinateReferenceSystem() != null) {
                        try {
                            Integer result =
                                    CRS.lookupEpsgCode(geom.getCoordinateReferenceSystem(), true);
                            if (result != null) srid = result;
                        } catch (Exception e) {
                            LOGGER.log(
                                    Level.FINE,
                                    "Error looking up the epsg code for metadata insertion, assuming -1",
                                    e);
                        }
                    }

                    // register the metadata
                    String geomColumnName = geom.getLocalName().toUpperCase();
                    String sql =
                            "INSERT INTO USER_SDO_GEOM_METADATA" //
                                    + "(TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)\n" //
                                    + "VALUES (\n" //
                                    + "'"
                                    + tableName
                                    + "',\n" //
                                    + "'"
                                    + geomColumnName
                                    + "',\n" //
                                    + "MDSYS.SDO_DIM_ARRAY(\n";
                    for (int i = 0; i < dims; i++) {
                        sql +=
                                "   MDSYS.SDO_DIM_ELEMENT('"
                                        + axisNames[i]
                                        + "', "
                                        + min[i]
                                        + ", "
                                        + max[i]
                                        + ", "
                                        + tolerance
                                        + ")";
                        if (i < dims - 1) sql += ", ";
                        sql += "\n";
                    }
                    sql =
                            sql
                                    + "),\n" //
                                    + (srid == -1 ? "NULL" : String.valueOf(srid))
                                    + ")";
                    LOGGER.log(Level.FINE, "Creating metadata with sql: {0}", sql);
                    st.execute(sql);

                    // figure out the index dimension -> for geodetic data 11G accepts only 2d
                    // index,
                    // even if the data is 3d
                    int idxDim = isGeodeticSrid(srid, cx) ? 2 : dims;

                    // create the spatial index (or we won't be able to run spatial predicates)
                    String type = CLASSES_TO_GEOM.get(geom.getType().getBinding());
                    String idxName = tableName + "_" + geomColumnName + "_IDX";
                    if (idxName.length() > nameLenghtLimit) {
                        idxName =
                                "IDX_"
                                        + UUID.randomUUID()
                                                .toString()
                                                .replace("-", "")
                                                .substring(0, 26);
                    }
                    sql =
                            "CREATE INDEX " //
                                    + idxName
                                    + " ON \"" //
                                    + tableName
                                    + "\"(\""
                                    + geomColumnName
                                    + "\")" //
                                    + " INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                                    + " PARAMETERS ('SDO_INDX_DIMS="
                                    + idxDim;
                    if (type != null) sql += " LAYER_GTYPE=\"" + type + "\"')";
                    else sql += "')";
                    LOGGER.log(Level.FINE, "Creating index with sql: {0}", sql);

                    st.execute(sql);
                }
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    private String getCompatibleAxisName(CoordinateSystemAxis axis, int dimensionIdx) {
        // try with one of the various ways this can be called
        String abbreviation = axis.getAbbreviation();
        if (AXIS_NAME_VALIDATOR.matcher(abbreviation).matches()) {
            return abbreviation;
        }
        String name = axis.getName().getCode();
        if (AXIS_NAME_VALIDATOR.matcher(name).matches()) {
            return name;
        }
        for (GenericName gn : axis.getAlias()) {
            String alias = gn.tip().toString();
            if (AXIS_NAME_VALIDATOR.matcher(alias).matches()) {
                return alias;
            }
        }
        // one last try
        if (CRS.equalsIgnoreMetadata(DefaultCoordinateSystemAxis.LONGITUDE, axis)) {
            return "Longitude";
        } else if (CRS.equalsIgnoreMetadata(DefaultCoordinateSystemAxis.LATITUDE, axis)) {
            return "Latitude";
        } else if (CRS.equalsIgnoreMetadata(DefaultCoordinateSystemAxis.ALTITUDE, axis)) {
            return "Altitude";
        }
        // ok, give up, let's use a name
        return "DIM_" + (dimensionIdx + 1);
    }

    @Override
    public String getSequenceForColumn(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        String sequenceName = (tableName + "_" + columnName + "_%").toUpperCase();
        PreparedStatement st = null;
        String sql;

        try {
            sql = "SELECT SEQUENCE_NAME FROM USER_SEQUENCES WHERE SEQUENCE_NAME like ?";
            st = cx.prepareStatement(sql);
            st.setString(1, sequenceName);

            // check the user owned sequences
            ResultSet rs = st.executeQuery();
            try {
                if (rs.next()) {
                    return rs.getString(1);
                }
            } finally {
                dataStore.closeSafe(rs);
                dataStore.closeSafe(st);
            }

            // that did not work, let's see if the sequence is available in someone else schema
            sql =
                    "SELECT SEQUENCE_NAME, SEQUENCE_OWNER FROM ALL_SEQUENCES WHERE SEQUENCE_NAME like ?";
            st = cx.prepareStatement(sql);
            st.setString(1, sequenceName);
            rs = st.executeQuery();
            try {
                if (rs.next()) {
                    String schema = rs.getString(2);
                    return schema + "." + rs.getString(1);
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
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx)
            throws SQLException {
        Statement st = cx.createStatement();
        try {
            ResultSet rs =
                    st.executeQuery(
                            "SELECT "
                                    + encodeNextSequenceValue(schemaName, sequenceName)
                                    + " FROM DUAL");
            try {
                if (!rs.next()) {
                    throw new SQLException("Could not find next sequence value");
                }
                return rs.getInt(1);
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    public String encodeNextSequenceValue(String schemaName, String sequenceName) {
        return sequenceName + ".NEXTVAL";
    }

    @Override
    public void postDropTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        PreparedStatement st = null;
        String tableName = featureType.getTypeName();

        try {
            // remove all the geometry metadata (no need for schema as we can only play against
            // the current user's table)
            String sql = "DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = ?";
            st = cx.prepareStatement(sql);
            st.setString(1, tableName);
            LOGGER.log(
                    Level.FINE,
                    "Post drop table: {0} [ TABLE_NAME = {1} ]",
                    new Object[] {sql, tableName});
            st.execute();
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return true;
    }

    /** Checks if the specified srid is geodetic or not */
    protected boolean isGeodeticSrid(Integer srid, Connection cx) {
        if (srid == null) return false;

        Boolean geodetic = geodeticCache.get(srid);

        if (geodetic == null) {
            synchronized (this) {
                geodetic = geodeticCache.get(srid);

                if (geodetic == null) {
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    boolean closeConnection = false;
                    try {
                        ps =
                                cx.prepareStatement(
                                        "SELECT COUNT(*) FROM MDSYS.GEODETIC_SRIDS WHERE SRID = ?");
                        ps.setInt(1, srid);
                        rs = ps.executeQuery();
                        rs.next();
                        geodetic = rs.getInt(1) > 0;
                        geodeticCache.put(srid, geodetic);
                    } catch (SQLException e) {
                        LOGGER.log(
                                Level.WARNING,
                                "Could not evaluate if the SRID " + srid + " is geodetic",
                                e);
                    } finally {
                        dataStore.closeSafe(rs);
                        dataStore.closeSafe(ps);
                        if (closeConnection) dataStore.closeSafe(cx);
                    }
                }
            }
        }

        return geodetic != null ? geodetic : false;
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return true;
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        // see http://progcookbook.blogspot.com/2006/02/using-rownum-properly-for-pagination.html
        // and http://www.oracle.com/technology/oramag/oracle/07-jan/o17asktom.html
        // to understand why we are going thru such hoops in order to get it working
        // The same techinique is used in Hibernate to support pagination

        if (offset == 0) {
            // top-n query: select * from (your_query) where rownum <= n;
            sql.insert(0, "SELECT * FROM (");
            sql.append(") WHERE ROWNUM <= " + limit);
        } else {
            // find results between N and M
            // select * from
            // ( select rownum rnum, a.*
            //    from (your_query) a
            //   where rownum <= :M )
            // where rnum >= :N;
            long max = (limit == Integer.MAX_VALUE ? Long.MAX_VALUE : limit + offset);
            sql.insert(0, "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM ( ");
            sql.append(") A WHERE ROWNUM <= " + max + ")");
            sql.append("WHERE RNUM > " + offset);
        }
    }

    @Override
    public void encodeTableAlias(String raw, StringBuffer sql) {
        sql.append(" ");
        encodeTableName(raw, sql);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        super.registerSqlTypeToSqlTypeNameOverrides(overrides);
        overrides.put(Types.REAL, "DOUBLE PRECISION");
        overrides.put(Types.DOUBLE, "DOUBLE PRECISION");
        overrides.put(Types.FLOAT, "FLOAT");
        // starting with Oracle 11 + recent JDBC drivers the DATE type does not have a mapping
        // anymore in the JDBC driver, manually register it instead
        overrides.put(Types.DATE, "DATE");
        // overriding default java.sql.Timestamp to Oracle DATE mapping
        overrides.put(Types.TIMESTAMP, "TIMESTAMP");
    }

    @Override
    public void postCreateAttribute(
            AttributeDescriptor att, String tableName, String schemaName, Connection cx)
            throws SQLException {
        super.postCreateAttribute(att, tableName, schemaName, cx);

        if (att instanceof GeometryDescriptor) {
            Integer srid = (Integer) att.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
            boolean geodetic = isGeodeticSrid(srid, cx);
            att.getUserData().put(GEODETIC, geodetic);
        }
    }

    /** The geometry metadata table in use, if any */
    public String getGeometryMetadataTable() {
        return geometryMetadataTable;
    }

    /** Sets the geometry metadata table */
    public void setGeometryMetadataTable(String geometryMetadataTable) {
        this.geometryMetadataTable = geometryMetadataTable;
    }

    /**
     * Sets the decision if the table MDSYS.USER_SDO_GEOM_METADATA can be used for index calculation
     */
    public void setMetadataBboxEnabled(boolean metadataBboxEnabled) {
        this.metadataBboxEnabled = metadataBboxEnabled;
    }

    /**
     * @param rs result set of the dimension info query
     * @param column column of the dimension info
     * @return the envelope out of the dimension info (assumption: x before y or longitude before
     *     latitude) or null, if no data is in the specified column
     * @throws SQLException if dimension info can not be parsed
     * @author Hendrik Peilke
     */
    private Envelope decodeDiminfoEnvelope(ResultSet rs, int column) throws SQLException {
        ARRAY returnArray = (ARRAY) rs.getObject(column);

        if (returnArray == null) {
            throw new SQLException("no data inside the specified column");
        }

        Datum data[] = returnArray.getOracleArray();

        if (data.length < 2) {
            throw new SQLException("too little dimension information found in sdo_geom_metadata");
        }

        Datum[] xInfo = ((STRUCT) data[0]).getOracleAttributes();
        Datum[] yInfo = ((STRUCT) data[1]).getOracleAttributes();
        Double minx = xInfo[1].doubleValue();
        Double maxx = xInfo[2].doubleValue();
        Double miny = yInfo[1].doubleValue();
        Double maxy = yInfo[2].doubleValue();

        return new Envelope(minx, maxx, miny, maxy);
    }

    public int getDefaultVarcharSize() {
        return 4000;
    }
}
