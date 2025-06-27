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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.locationtech.jts.geom.Coordinate;
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
import org.locationtech.jts.io.ParseException;

public class DB2SQLDialect extends SQLDialect {

    private static Integer GEOMETRY = 9001;
    private static Integer GEOMETRYCOLL = 9002;
    private static Integer POINT = 9003;
    private static Integer MULTIPOINT = 9004;
    private static Integer LINESTRING = 9005;
    private static Integer MULTILINESTRING = 9006;
    private static Integer POLY = 9007;
    private static Integer MULTIPOLY = 9008;

    private static String POINT_STR = "\"DB2GSE\".\"ST_POINT\"";
    private static String LINESTRING_STR = "\"DB2GSE\".\"ST_LINESTRING\"";
    private static String POLY_STR = "\"DB2GSE\".\"ST_POLYGON\"";
    private static String MULTIPOINT_STR = "\"DB2GSE\".\"ST_MULTIPOINT\"";
    private static String MULTILINESTRING_STR = "\"DB2GSE\".\"ST_MULTILINESTRING\"";
    private static String MULTIPOLY_STR = "\"DB2GSE\".\"ST_MULTIPOLYGON\"";
    private static String GEOMETRY_STR = "\"DB2GSE\".\"ST_GEOMETRY\"";
    private static String GEOMETRYCOLL_STR = "\"DB2GSE\".\"ST_GEOMCOLLECTION\"";

    static String SELECTIVITY_CLAUSE = "SELECTIVITY 0.000001 ";

    private static String DEFAULT_SRS_NAME = "DEFAULT_SRS";
    private static Integer DEFAULT_SRS_ID = 0;

    private boolean looseBBOXEnabled;
    private boolean useSelectivity;

    private static String SELECT_SRSID_WITH_SCHEMA =
            "select SRS_ID from DB2GSE.ST_GEOMETRY_COLUMNS where TABLE_SCHEMA = ? and "
                    + "TABLE_NAME = ? and COLUMN_NAME = ?";

    private static String SELECT_SRSID_WITHOUT_SCHEMA =
            "select SRS_ID from DB2GSE.ST_GEOMETRY_COLUMNS where  " + "TABLE_NAME = ? and COLUMN_NAME = ?";
    private static String SELECT_CRS_WKT = "select definition,organization,organization_coordsys_id "
            + "from db2gse.st_coordinate_systems "
            + "where coordsys_name = (select coordsys_name from db2gse.st_spatial_reference_systems where srs_id=?)";
    private static String SELECT_SRS_NAME_FROM_ID =
            "select srs_name from db2gse.st_spatial_reference_systems where srs_id = ?";
    private String SELECT_SRS_NAME_FROM_ORG =
            "select srs_name,srs_id from db2gse.st_spatial_reference_systems where organization = ? and organization_coordsys_id=?";

    //    private static String SELECT_INCLUDE_WITH_SCHEMA ="select table_schema,table_name  from
    // db2gse.st_geometry_columns where table_schema = ? and table_name=?";
    //    private static String SELECT_INCLUDE="select table_schema,table_name  from
    // db2gse.st_geometry_columns where table_schema = current schema  and table_name=?";

    private static String SELECT_ROWNUMBER = "select * from sysibm.sysdummy1 where rownum = 1";
    private Boolean isRowNumberSupported = null;
    private static String SELECT_LIMITOFFSET = "select * from sysibm.sysdummy1 limit 0,1";
    private Boolean isLimitOffsetSupported = null;
    //    private static String SELECT_OLAP_ROWNUM=
    //            "select  * from (select  row_number() over () as rownum , a.* from  (select * from
    // sysibm.sysdummy1) a)   where rownum > 0 and rownum <= 1";
    //    private  Boolean isOLAPRowNumSupported=null;

    private static String ROWNUMBER_MESSAGE = "Using Oracle ROWNUM for paging support";

    private static String LIMITOFFSET_MESSAGE = "Using LIMIT OFFSET for paging support";

    //    private  static String OLAPROWNUM_MESSAGE=
    //            "Using ROW_NUMBER () OVER () for paging support";

    private String NOPAGESUPPORT_MESSAGE = "DB2 handles paged select statements inefficiently\n"
            + "Try to set MySql or Oracle compatibility mode\n"
            + "dbstop\n"
            + "db2set DB2_COMPATIBILITY_VECTOR=MYS\n"
            + "db2start\n";

    private DB2DialectInfo db2DialectInfo;
    private boolean functionEncodingEnabled;

    public DB2SQLDialect(JDBCDataStore dataStore, DB2DialectInfo info) {
        super(dataStore);
        db2DialectInfo = info;
    }

    public DB2DialectInfo getDb2DialectInfo() {
        return db2DialectInfo;
    }

    /* (non-Javadoc)
     * @see org.geotools.jdbc.SQLDialect#createCRS(int, java.sql.Connection)
     *
     * First, look up the wkt def for the srid, if not found return null
     * If we have a wkt def from db2, try to decode with CRS.parseWKT, on success return
     * the crs
     *
     * If we cannot parse the WKT def, use the organization and organization coordsys id to parse
     * with CRS.decode(), on success return the crs
     *
     * Otherwise, its time to give up and return null
     */
    @Override
    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException {
        String org = null, wkt = null;
        int orgid = 0;
        try (PreparedStatement ps = cx.prepareStatement(SELECT_CRS_WKT)) {
            ps.setInt(1, srid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    wkt = rs.getString(1);
                    org = rs.getString(2);
                    orgid = rs.getInt(3);
                }
            }
        }
        if (orgid != 0 && org != null) {
            try {
                CoordinateReferenceSystem crs = CRS.decode(org + ":" + orgid, true); // Force longitude first
                return crs;
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(
                            Level.WARNING, "Could not decode " + org + ":" + orgid + " using the geotools database", e);
            }
        }

        if (wkt != null) {
            try {

                CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
                return crs;
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING, "Could not decode db2 wkt definition for " + srid);
            }
        }
        return null;
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        super.encodePrimaryKey(column, sql);
        sql.append(" NOT NULL");
    }

    @Override
    public String getGeometryTypeName(Integer type) {

        if (GEOMETRY.equals(type)) return GEOMETRY_STR;
        if (GEOMETRYCOLL.equals(type)) return GEOMETRYCOLL_STR;
        if (POINT.equals(type)) return POINT_STR;
        if (MULTIPOINT.equals(type)) return MULTIPOINT_STR;
        if (LINESTRING.equals(type)) return LINESTRING_STR;
        if (MULTILINESTRING.equals(type)) return MULTILINESTRING_STR;
        if (POLY.equals(type)) return POLY_STR;
        if (MULTIPOLY.equals(type)) return MULTIPOLY_STR;
        return null;
    }

    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {

        Integer srid = null;
        PreparedStatement stmt = null;

        try {
            if (schemaName != null) {
                stmt = cx.prepareStatement(SELECT_SRSID_WITH_SCHEMA);
                stmt.setString(1, schemaName);
                stmt.setString(2, tableName);
                stmt.setString(3, columnName);
            } else {
                stmt = cx.prepareStatement(SELECT_SRSID_WITHOUT_SCHEMA);
                stmt.setString(1, tableName);
                stmt.setString(2, columnName);
            }

            ResultSet rs = null;
            try {
                rs = stmt.executeQuery();
                if (rs.next()) srid = (Integer) rs.getObject(1);
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(stmt);
        }
        return srid;
    }

    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        encodeGeometryColumn(gatt, prefix, sql);
    }

    public void encodeGeometryColumn(GeometryDescriptor gatt, String prefix, StringBuffer sql) {
        sql.append("db2gse.ST_AsBinary(");
        encodeColumnName(prefix, gatt.getLocalName(), sql);
        sql.append(")");
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {

        /* becomes slow on large data sets
        *
           sql.append("db2gse.ST_AsBinary(db2gse.ST_GetAggrResult(MAX(db2gse.ST_BuildMBRAggr(");
           encodeColumnName(null, geometryColumn, sql);
           sql.append("))))");

        */

        sql.append("min(db2gse.st_minx(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append("))");
        sql.append(",min(db2gse.st_miny(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append("))");
        sql.append(",max(db2gse.st_maxx(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append("))");
        sql.append(",max(db2gse.st_maxy(");
        encodeColumnName(null, geometryColumn, sql);
        sql.append("))");
    }

    /**
     * Controls whether keys are looked up post or pre insert.
     *
     * <p>TODO TODO TODO Due to DB2 implementation problems, ps.addBatch(), ps.executeBatch() doesn't work for
     * auto-generated identity key columns. This function should return 'true' to make the overall logic work with
     * auto-generated columns but this breaks the logic for sequence generated key columns so for now, return 'false'.
     *
     * <p>Part of the reason for the breakage is that if 'true', KeysFetcher will add to the INSERT statement NEXT VALUE
     * FOR seq_name but doesn't include the schema so an error occurs since the test setup specifies the sequence schema
     * name as 'geotools' which is different from the default which is the connection id. It isn't clear why the
     * 'geotools' schema isn't being specified. Even if this was corrected, the current DB2 JDBC support wouldn't
     * correctly get the inserted key values later.
     *
     * <p>Another problem is that if 'true', JDBCDataStore::insertPS executes: ps = cx.prepareStatement(sql,
     * keysFetcher.getColumnNames()); which causes a failure with ps.addBatch() because it defaults to uppercase for the
     * key column while the table was defined with a lowercase key column - this is a DB2 JDBC defect which is not
     * likely to be fixed.
     *
     * <p>It isn't really clear why this method is being called for sequence key columns. Fixing this probably requires
     * significant changes to the core JDBC logic which I'm not prepared to do. -- David Adler 04/18/2017
     *
     * <p>When a row is inserted into a table, and a key is automatically generated DB2 allows the generated key to be
     * retrieved after the insert. The DB2 dialect returns <code>true</code> to cause the lookup to occur after the
     * insert via {@link #getLastAutoGeneratedValue(String, String, String, Connection)}.
     *
     * <p>DB2 implements:
     *
     * <ul>
     *   <li>{@link #getLastAutoGeneratedValue(String, String, String, Connection)}
     * </ul>
     */
    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return false;
    }

    /**
     * Since DB2 V10.
     *
     * <p>Look in the system view "db2gse.st_geometry_columns" and check for min_x,min_y,max_x,max_y
     *
     * <p>If ALL geometry attributes have precalculated extents, return the list of the envelopes. If only one of them
     * has no precalculated extent, return null
     */
    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {

        if (getDb2DialectInfo().isSupportingPrecalculatedExtents() == false) return null;

        if (dataStore.getVirtualTables().get(featureType.getTypeName()) != null) return null;

        if (schema == null || "".equals(schema)) return null; // no db schema

        String tableName = featureType.getTypeName();

        Statement st = null;
        ResultSet rs = null;

        List<ReferencedEnvelope> result = new ArrayList<>();
        try {
            st = cx.createStatement();

            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {

                    StringBuffer sql = new StringBuffer();
                    sql.append("select min_x,min_y,max_x,max_y from db2gse.st_geometry_columns where  TABLE_SCHEMA='");
                    sql.append(schema).append("' AND TABLE_NAME='");
                    sql.append(tableName).append("' AND COLUMN_NAME='");
                    sql.append(att.getName().getLocalPart()).append("'");

                    LOGGER.log(Level.FINE, "Getting the full extent of the table using optimized search: {0}", sql);
                    rs = st.executeQuery(sql.toString());

                    if (rs.next()) {
                        Double min_x = rs.getDouble(1);
                        if (rs.wasNull()) return null;
                        Double min_y = rs.getDouble(2);
                        if (rs.wasNull()) return null;
                        Double max_x = rs.getDouble(3);
                        if (rs.wasNull()) return null;
                        Double max_y = rs.getDouble(4);
                        if (rs.wasNull()) return null;

                        Geometry geometry = new GeometryFactory().createPolygon(new Coordinate[] {
                            new Coordinate(min_x, min_y),
                            new Coordinate(min_x, max_y),
                            new Coordinate(max_x, max_y),
                            new Coordinate(max_x, min_y),
                            new Coordinate(min_x, min_y)
                        });

                        // Either a ReferencedEnvelope or ReferencedEnvelope3D will be generated
                        // here
                        ReferencedEnvelope env =
                                JTS.bounds(geometry, ((GeometryDescriptor) att).getCoordinateReferenceSystem());

                        // reproject and merge
                        if (env != null && !env.isNull()) result.add(env);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(
                    Level.WARNING,
                    "Failed to use extent from DB2GSE.ST_GEOMETRY_COLUMNS, falling back on envelope aggregation",
                    e);
            return null;
        } finally {
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }
        return result;
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
        //        byte[] wkb = rs.getBytes(column);
        //
        //        try {
        //            if (wkb!=null) {
        //                Geometry geom  =  new DB2WKBReader().read(wkb);
        //                return geom.getEnvelopeInternal();
        //            } else {
        //                return new Envelope();
        //            }
        //        } catch (ParseException e) {
        //            String msg = "Error decoding wkb for envelope";
        //            throw (IOException) new IOException(msg).initCause(e);
        //        }

        if (column % 4 != 1) return null;
        double minX = rs.getDouble(column);
        if (rs.wasNull()) return null;

        return new Envelope(minX, rs.getDouble(3), rs.getDouble(2), rs.getDouble(4));
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
        return decodeGeometryValueFromBytes(factory, bytes);
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
        byte[] bytes = rs.getBytes(column);
        return decodeGeometryValueFromBytes(factory, bytes);
    }

    private Geometry decodeGeometryValueFromBytes(GeometryFactory factory, byte[] bytes) throws IOException {
        if (bytes == null) return null;

        try {
            return new DB2WKBReader(factory).read(bytes);
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
        mappings.put(LinearRing.class, LINESTRING);
        mappings.put(Polygon.class, POLY);
        mappings.put(MultiPoint.class, MULTIPOINT);
        mappings.put(MultiLineString.class, MULTILINESTRING);
        mappings.put(MultiPolygon.class, MULTIPOLY);
        mappings.put(Geometry.class, GEOMETRY);
        mappings.put(GeometryCollection.class, GEOMETRYCOLL);
    }

    @Override
    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        super.registerSqlTypeToClassMappings(mappings);

        mappings.put(GEOMETRY, Geometry.class);
        mappings.put(GEOMETRYCOLL, GeometryCollection.class);
        mappings.put(POINT, Point.class);
        mappings.put(MULTIPOINT, MultiPoint.class);
        mappings.put(LINESTRING, LineString.class);
        mappings.put(MULTILINESTRING, MultiLineString.class);
        mappings.put(POLY, Polygon.class);
        mappings.put(MULTIPOLY, Polygon.class);
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put(POINT_STR, Point.class);
        mappings.put(LINESTRING_STR, LineString.class);
        mappings.put(POLY_STR, Polygon.class);
        mappings.put(MULTIPOINT_STR, MultiPoint.class);
        mappings.put(MULTILINESTRING_STR, MultiLineString.class);
        mappings.put(MULTIPOLY_STR, MultiPolygon.class);
        mappings.put(GEOMETRY_STR, Geometry.class);
        mappings.put(GEOMETRYCOLL_STR, GeometryCollection.class);
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx) throws SQLException {

        if (featureType.getGeometryDescriptor() == null) // table without geometry
        return;

        String tableName = featureType.getTypeName();
        String columnName = featureType.getGeometryDescriptor().getName().toString();

        for (AttributeDescriptor attr : featureType.getAttributeDescriptors()) {
            if (attr instanceof GeometryDescriptor) {
                GeometryDescriptor gDescr = (GeometryDescriptor) attr;
                String srsName = null;
                Integer srsId = (Integer) gDescr.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
                if (srsId != null) {
                    try (PreparedStatement ps1 = cx.prepareStatement(SELECT_SRS_NAME_FROM_ID)) {
                        ps1.setInt(1, srsId);
                        try (ResultSet rs = ps1.executeQuery()) {
                            if (rs.next()) srsName = rs.getString(1);
                        }
                    }
                }
                if (srsName == null && gDescr.getCoordinateReferenceSystem() != null) {
                    for (ReferenceIdentifier ident :
                            gDescr.getCoordinateReferenceSystem().getIdentifiers()) {
                        try (PreparedStatement ps1 = cx.prepareStatement(SELECT_SRS_NAME_FROM_ORG)) {
                            ps1.setString(1, ident.getCodeSpace());
                            ps1.setInt(2, Integer.valueOf(ident.getCode()));
                            try (ResultSet rs = ps1.executeQuery()) {
                                if (rs.next()) {
                                    srsName = rs.getString(1);
                                    srsId = rs.getInt(2);
                                }
                            }
                        }
                        if (srsName != null) break;
                    }
                }
                if (srsName == null) {
                    srsName = DEFAULT_SRS_NAME;
                    srsId = DEFAULT_SRS_ID;
                }
                DB2Util.executeRegister(schemaName, tableName, columnName, srsName, cx);
                gDescr.getUserData().put(JDBCDataStore.JDBC_NATIVE_SRID, srsId);
            }
        }
    }

    @Override
    public String getSequenceForColumn(String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {

        // TODO, hard stuff
        String sequenceName = tableName + "_" + columnName + "_SEQUENCE";
        StringBuffer sql = new StringBuffer("SELECT SEQNAME FROM SYSCAT.SEQUENCES WHERE ");

        if (schemaName != null) {
            sql.append("SEQSCHEMA ='");
            sql.append(schemaName);
            sql.append("' AND ");
        }
        sql.append("SEQNAME = '");
        sql.append(sequenceName);
        sql.append("'");

        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql.toString());
            try {
                if (rs.next()) {
                    return sequenceName;
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
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(encodeNextSequenceValue(schemaName, sequenceName));
        sql.append(" from sysibm.sysdummy1");
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery(sql.toString());
            try {
                if (!rs.next()) {
                    throw new SQLException("Could not get next value for sequence");
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
        StringBuffer sql = new StringBuffer("next value for ");
        if (schemaName != null) {
            encodeSchemaName(schemaName, sql);
            sql.append(".");
        }
        encodeTableName(sequenceName, sql);
        return sql.toString();
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx) throws SQLException {

        return true;

        //		PreparedStatement ps = null;
        //		if (schemaName!=null && schemaName.trim().length()>0) {
        //			ps = cx.prepareStatement(SELECT_INCLUDE_WITH_SCHEMA);
        //			ps.setString(1,schemaName);
        //			ps.setString(2,tableName);
        //		}	else {
        //			ps = cx.prepareStatement(SELECT_INCLUDE);
        //			ps.setString(1,tableName);
        //		}
        //
        //		ResultSet rs = ps.executeQuery();
        //		boolean isGeomTable = rs.next();
        //		rs.close();
        //		ps.close();
        //		return isGeomTable;
    }

    @Override
    public boolean isLimitOffsetSupported() {

        boolean firstCall =
                isLimitOffsetSupported == null && isRowNumberSupported == null; // && isOLAPRowNumSupported ==null;

        if (isLimitOffsetSupported == null) setIsLimitOffsetSupported();
        if (isLimitOffsetSupported) {
            return true;
        }

        if (isRowNumberSupported == null) setIsRowNumberSupported();
        if (isRowNumberSupported) {
            return true;
        }

        //        if (isOLAPRowNumSupported==null)
        //            setIsOLAPRowNumSupported();
        //        if (isOLAPRowNumSupported) {
        //            return true;
        //        }

        if (firstCall) {
            LOGGER.warning(NOPAGESUPPORT_MESSAGE);
        }

        return false;
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        // since 9.7.2, Limit and offset is supported

        if (Boolean.TRUE.equals(isLimitOffsetSupported)) {
            if (limit >= 0 && limit < Integer.MAX_VALUE) {
                if (offset > 0) sql.append(" LIMIT " + offset + ", " + limit);
                else sql.append(" LIMIT " + limit);
            } else if (offset > 0) {
                sql.append(" LIMIT " + offset + ", " + (Integer.MAX_VALUE - 7));
            }
            return; // end here, we are finished
        }

        // Since 9.5, Using the same code as in the OracleDialict. This method is only invoked if
        // DB2 is configured to be compatible to Oracle with
        // "db2set DB2_COMPATIBILITY_VECTOR=01"
        // enabling the rownum pseudo column

        if (Boolean.TRUE.equals(isRowNumberSupported)) {
            if (offset == 0) {
                sql.insert(0, "SELECT * FROM (");
                sql.append(") WHERE ROWNUM <= " + limit);
            } else {
                long max = limit == Integer.MAX_VALUE ? Long.MAX_VALUE : limit + offset;
                sql.insert(0, "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM ( ");
                sql.append(") A WHERE ROWNUM <= " + max + ")");
                sql.append("WHERE RNUM > " + offset);
            }
        }

        //        if (Boolean.TRUE.equals(isOLAPRowNumSupported)) {
        //            sql.insert(0,"select  * from (select   a.* from  (");
        //            sql.append(") a ,row_number() over () as rownum )   where ");
        //            if(offset == 0) {
        //                sql.append(" rownum <= " + limit);
        //            } else {
        //                long max = (limit == Integer.MAX_VALUE ? Long.MAX_VALUE : limit + offset);
        //                sql.append (" rownum > " + offset + " and rownum <= " + max);
        //            }
        //        }
    }

    private void setIsRowNumberSupported() {

        try (Connection con = dataStore.getDataSource().getConnection();
                PreparedStatement ps = con.prepareStatement(SELECT_ROWNUMBER);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) isRowNumberSupported = Boolean.TRUE;
            LOGGER.info(ROWNUMBER_MESSAGE);
        } catch (SQLException ex) {
            isRowNumberSupported = Boolean.FALSE;
        }
    }

    private void setIsLimitOffsetSupported() {

        try (Connection con = dataStore.getDataSource().getConnection();
                PreparedStatement ps = con.prepareStatement(SELECT_LIMITOFFSET);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) isLimitOffsetSupported = Boolean.TRUE;
            LOGGER.info(LIMITOFFSET_MESSAGE);
        } catch (SQLException ex) {
            isLimitOffsetSupported = Boolean.FALSE;
        }
    }

    //    private void setIsOLAPRowNumSupported() {
    //        Connection con = null;
    //        PreparedStatement ps = null;
    //        ResultSet rs = null;
    //
    //        try {
    //                con = dataStore.getDataSource().getConnection();
    //                ps = con.prepareStatement(SELECT_OLAP_ROWNUM);
    //                rs = ps.executeQuery();
    //                if (rs.next()) isOLAPRowNumSupported=Boolean.TRUE;
    //                LOGGER.info(OLAPROWNUM_MESSAGE);
    //        }
    //        catch (SQLException ex) {
    //                isOLAPRowNumSupported=Boolean.FALSE;
    //        }
    //        finally {
    //            try {if (rs!=null) rs.close(); } catch (SQLException ex1) {};
    //            try {if (ps!=null) ps.close();} catch (SQLException ex1) {};
    //            try {if (con!=null) con.close();} catch (SQLException ex1) {};
    //        }
    //    }

    @Override
    public void encodeGeometryColumnGeneralized(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {

        sql.append("db2gse.ST_AsBinary(db2gse.st_Generalize(");
        encodeColumnName(null, gatt.getLocalName(), sql);
        sql.append(",").append(distance);
        sql.append("))");
    }

    @Override
    protected void addSupportedHints(Set<Key> hints) {

        if (isGeomGeneralizationSupported()) {
            LOGGER.info("GEOMETRY_GENERALIZATION support: YES");
            hints.add(Hints.GEOMETRY_GENERALIZATION);
        } else {
            LOGGER.info("GEOMETRY_GENERALIZATION support: NO");
        }
    }

    private boolean isGeomGeneralizationSupported() {
        DB2DialectInfo info = getDb2DialectInfo();

        if (info.getProductVersion().startsWith("DSN")) return false; // I have no idea about the version on z/OS
        if (info.getProductName().startsWith("Informix")) return false;

        if (info.getProductVersion().startsWith("SQL") == false) return false; // insist on DB2 on windows and linux

        if (info.getMajorVersion() > 9) return true;
        if (info.getMajorVersion() < 9) return false;
        // major version 9
        if (info.getMinorVersion() > 7) return true;
        if (info.getMinorVersion() < 5) return false;

        // left 9.5 and 9.7, get FP number
        if (info.getProductVersion().length() < 8) return false;
        String fp = info.getProductVersion().substring(7);
        StringBuffer buff = new StringBuffer();

        for (int i = 0; i < fp.length(); i++) {
            if (Character.isDigit(fp.charAt(i))) buff.append(fp.charAt(i));
            else break;
        }
        if (buff.length() == 0) return false;

        int fpNumber = Integer.parseInt(buff.toString());
        if (info.getMinorVersion() == 5 && fpNumber >= 5) return true;
        if (info.getMinorVersion() == 7 && fpNumber >= 1) return true;
        return false;
    }

    public boolean isFunctionEncodingEnabled() {
        return functionEncodingEnabled;
    }

    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        this.functionEncodingEnabled = functionEncodingEnabled;
    }

    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

    public boolean isUseSelectivity() {
        return useSelectivity;
    }

    public void setUseSelectivity(boolean useSelectivity) {
        this.useSelectivity = useSelectivity;
    }

    @Override
    protected boolean supportsSchemaForIndex() {
        return true;
    }

    @Override
    public boolean canGroupOnGeometry() {
        // not supported
        // DB2 SQL Error: SQLCODE=-134, SQLSTATE=42907, SQLERRMC=geometry, DRIVER=4.29.24
        return false;
    }
}
