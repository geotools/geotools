package org.geotools.data.teradata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.NullPrimaryKey;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTWriter;

public class TeradataDialect extends PreparedStatementSQLDialect {

    /**
     * sysspatial schema
     */
    final static String SYSSPATIAL = "sysspatial";
    
    /**
     * tessellation information table
     */
    final static String TESSELLATION = "tessellation";
    
    /**
     * geometry columns table
     */
    final static String GEOMETRY_COLUMNS = "geometry_columns";
    
    /**
     * spatial ref sys
     */
    final static String SPATIAL_REF_SYS = "spatial_ref_sys";
    
    /**
     * key for spatial index table
     */
    final static String SPATIAL_INDEX = "org.geotools.data.teradata.spatialIndex";
    
    final static Map<String, Class<?>> TYPE_TO_CLASS = new HashMap<String, Class<?>>() {
        {
            put("GEOMETRY", Geometry.class);
            put("POINT", Point.class);
            put("LINESTRING", LineString.class);
            put("POLYGON", Polygon.class);

            put("MULTIPOINT", MultiPoint.class);
            put("MULTILINESTRING", MultiLineString.class);
            put("MULTIPOLYGON", MultiPolygon.class);
            put("GEOMETRYCOLLECTION", GeometryCollection.class);
            put("GEOSEQUENCE", Geometry.class);
        }
    };

    final static Map<Class<?>, String> CLASS_TO_TYPE = new HashMap<Class<?>, String>() {
        {
            put(Geometry.class, "GEOMETRY");
            put(Point.class, "POINT");
            put(LineString.class, "LINESTRING");
            put(Polygon.class, "POLYGON");
            put(MultiPoint.class, "MULTIPOINT");
            put(MultiLineString.class, "MULTILINESTRING");
            put(MultiPolygon.class, "MULTIPOLYGON");
            put(GeometryCollection.class, "GEOMETRYCOLLECTION");
        }
    };

    /** loose bbox flag */
    boolean looseBBOXEnabled = false;
    
    /** estimated bounds */
    boolean estimatedBounds = false;

    public TeradataDialect(JDBCDataStore store) {
        super(store);
    }
    
    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }
    
    public void setEstimatedBounds(boolean estimatedBounds) {
        this.estimatedBounds = estimatedBounds;
    }
    
    public boolean isEstimatedBounds() {
        return estimatedBounds;
    }
    
    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        
        if (tableName.equalsIgnoreCase("geometry_columns")) {
            return false;
        } else if (tableName.toLowerCase().startsWith("spatial_ref_sys")) {
            return false;
        } else if (tableName.equalsIgnoreCase("geography_columns")) {
            return false;
        } else if (tableName.equalsIgnoreCase("tessellation")) {
            return false;
        } else if (tableName.endsWith("_idx")) {
            return false;
        }

        // others?
        return dataStore.getDatabaseSchema() == null
                || dataStore.getDatabaseSchema().equals(schemaName);
    }

    @Override
    public void setGeometryValue(Geometry g, int srid, Class binding,
            PreparedStatement ps, int column) throws SQLException {
        if (g != null) {
            if (g instanceof LinearRing ) {
                //teradata does not handle linear rings, convert to just a line string
                g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
            }
            
            //TODO: use WKB instead of WKT
            String wkt = new WKTWriter().write(g);
            if (wkt.length() > 64000) {
                ByteArrayInputStream bin = new ByteArrayInputStream(wkt.getBytes());
                ps.setAsciiStream(column, bin, bin.available());
            }
            else {
                ps.setString(column, wkt);
            }
            
        }
        else {
            ps.setNull(column, Types.OTHER, "Geometry");
        }
    }

    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, String column,
            GeometryFactory factory, Connection cx) throws IOException, SQLException {
        try {
            byte[] wkb = rs.getBytes(column + "_inline");
            if (wkb != null) {
                return new WKBReader(factory).read(wkb);
            }
        }
        catch(SQLException e) {
            //ok, just fall back to reading blob
        } 
        catch (ParseException e) {
            throw (IOException) new IOException("Error parsing well known binary").initCause(e);
        }
        
        return getWkbReader(factory).read(rs, column);
    }

    WKBAttributeIO getWkbReader(GeometryFactory factory) {
        factory = factory != null ? factory : dataStore.getGeometryFactory();
        return new WKBAttributeIO(factory);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
            Connection cx) throws SQLException, IOException {
        Geometry envelope = getWkbReader(null).read(rs, column);
        if (envelope != null) {
            return envelope.getEnvelopeInternal();
        } else {
            // empty one
            return new Envelope();
        }
    }

    @Override
    public void encodeColumnType(String sqlTypeName, StringBuffer sql) {
        if ("DECIMAL".equals(sqlTypeName)) {
            sql.append(sqlTypeName).append("(10,2)");
        }
        else {
            super.encodeColumnType(sqlTypeName, sql);
        }
    }
    
    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn,
            StringBuffer sql) {
        encodeColumnName(geometryColumn, sql);
        sql.append(".ST_Envelope().ST_AsBinary()");
    }
    
    @Override
    public void encodePostSelect(SimpleFeatureType featureType, StringBuffer sql) {
        //Since geometries are stored as LOB's in teradata reading a geometry value in TD requires
        // as second trip to the database. Since this is expensive we attempt to cast the geometry
        // into a 64K byte string which is the limit of what TD will include inline. For large 
        // geometries we will have to check and fall back onto readin the Blob, but for smaller 
        // geometries this will save us the second trip
        // see decodeGeometryValue()
       
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            if (att instanceof GeometryDescriptor) {
                sql.append(", CAST(");
                encodeColumnName(att.getLocalName(), sql);
                sql.append(".ST_AsBinary() AS VARBYTE(16000))");
                encodeColumnAlias(att.getLocalName() + "_inline", sql);
            }
        }
    }
    
    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema,
            SimpleFeatureType featureType, Connection cx) throws SQLException, IOException {
        if (!estimatedBounds) {
            return null;
        }
        
        List<TessellationInfo> tinfos = 
            lookupTessellationInfos(cx, schema, featureType.getTypeName(), null);
        if (tinfos.isEmpty()) {
            return null;
        }
        
        List<ReferencedEnvelope> envs = new ArrayList();
        for (TessellationInfo tinfo : tinfos) {
            GeometryDescriptor gatt = 
                (GeometryDescriptor) featureType.getDescriptor(tinfo.getColumName());
            
            ReferencedEnvelope env = new ReferencedEnvelope(gatt.getCoordinateReferenceSystem());
            env.init(tinfo.getUBounds());
            envs.add(env);
        }
        
        
        return envs;
    }
    
    public void encodePrimaryKey(String column, StringBuffer sql) {
       encodeColumnName(column, sql);
       sql.append(" PRIMARY KEY not null generated always as identity (start with 0) integer");
    }

    public Integer getGeometrySRID(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {

        StringBuffer sql = new StringBuffer("SELECT ref.AUTH_SRID FROM ");
        encodeTableName(SYSSPATIAL, SPATIAL_REF_SYS, sql);
        sql.append(" as ref, ");
        encodeTableName(SYSSPATIAL, GEOMETRY_COLUMNS, sql);
        sql.append(" as col ");
        sql.append(" WHERE col.F_TABLE_SCHEMA = ?");
        sql.append(" AND col.F_TABLE_NAME = ? ");
        sql.append(" AND col.F_GEOMETRY_COLUMN = ? ");
        sql.append(" AND col.SRID = ref.SRID");
        
        LOGGER.log(Level.FINE, String.format("%s; 1=%s, 2=%s, 3=%s", sql.toString(), schemaName,
            tableName, columnName));
        
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        try {
            ps.setString(1, schemaName);
            ps.setString(2, tableName);
            ps.setString(3, columnName);
            
            ResultSet rs = ps.executeQuery(); 
            try {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                else {
                    LOGGER.warning(String.format("No SRID entry for %s, %s, %s", schemaName, 
                        tableName, columnName));
                }
            }
            finally {
                dataStore.closeSafe(rs);
            }
        }
        finally {
            dataStore.closeSafe(ps);
        }


        return null;
    }

    public String getGeometryTypeName(Integer type) {
        return "ST_Geometry";
    }

    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
        String typeName = columnMetaData.getString("TYPE_NAME");
        String gType = null;
        if ("SYSUDTLIB.ST_GEOMETRY".equalsIgnoreCase(typeName)) {
            gType = lookupGeometryType(columnMetaData, cx);
        } 
        else {
            return null;
        }

        // decode the type into
        if (gType == null) {
            // it's either a generic geography or geometry not registered in the
            // medatata tables
            return Geometry.class;
        } else {
            Class<?> geometryClass = TYPE_TO_CLASS.get(gType.toUpperCase());
            if (geometryClass == null) {
                geometryClass = Geometry.class;
            }

            return geometryClass;
        }
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return true;
    }

    @Override
    public Object getLastAutoGeneratedValue(String schemaName, String tableName, String columnName,
            Connection cx) throws SQLException {

        StringBuffer sql = new StringBuffer("SELECT TOP 1 ");
        encodeColumnName(columnName, sql);
        
        sql.append(" FROM ");
        encodeTableName(schemaName, tableName, sql);
        
        sql.append(" ORDER BY ");
        encodeColumnName(columnName, sql);
        sql.append(" DESC");

        LOGGER.fine(sql.toString());
        
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        try {
            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            finally {
                dataStore.closeSafe(rs);
            }
        } 
        finally {
            dataStore.closeSafe(ps);
        }
        
        return null;
    }
    
    /**
     * Looks up geometry type of schema/table/column.
     */
    String lookupGeometryType(ResultSet columnMetaData, Connection cx) throws SQLException {

        // grab the information we need to proceed
        String schemaName = columnMetaData.getString("TABLE_SCHEM");
        String tableName = columnMetaData.getString("TABLE_NAME");
        String columnName = columnMetaData.getString("COLUMN_NAME");

        StringBuffer sql = new StringBuffer("SELECT GEOM_TYPE");
        sql.append(" FROM ");
        encodeTableName(SYSSPATIAL, GEOMETRY_COLUMNS, sql);
        
        sql.append("WHERE F_TABLE_SCHEMA = ? AND F_TABLE_NAME = ? AND F_GEOMETRY_COLUMN = ?");

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(String.format("%s; 1=%s, 2=%s, 3=%s", sql.toString(), schemaName,
                tableName, columnName));
        }

        
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        try {
            ps.setString(1, schemaName);
            ps.setString(2, tableName);
            ps.setString(3, columnName);

            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
            finally {
                dataStore.closeSafe(rs);
            }
        } 
        finally {
            dataStore.closeSafe(ps);
        }

        return null;
    }
    /**
     * Looks up tessellation info for the schema/table/geometry.
     */
    TessellationInfo lookupTessellationInfo(Connection cx, String schemaName, String tableName, 
        String columnName) throws SQLException {
        if (columnName == null) {
            throw new IllegalArgumentException("Column name must not be null");
        }
        List<TessellationInfo> tinfos = lookupTessellationInfos(cx, schemaName, tableName, columnName);
        return !tinfos.isEmpty() ? tinfos.get(0) : null;
    }
    
    /**
     * Looks up tessellation info for the schema/table.
     */
    List<TessellationInfo> lookupTessellationInfos(Connection cx, String schemaName, String tableName, 
        String columnName) throws SQLException {
        
        ResultSet tables = cx.getMetaData().getTables(
                null, "sysspatial", TESSELLATION, new String[]{"TABLE"});
        try {
            if (!tables.next()) {
                LOGGER.warning("sysspatial." + TESSELLATION + " does not exist. Unable to " +
                    " perform spatially index queries.");
                return Collections.EMPTY_LIST;
            }
        }
        finally {
            dataStore.closeSafe(tables);
        }
        
        List<TessellationInfo> tinfos = new ArrayList();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        encodeTableName(SYSSPATIAL, TESSELLATION, sql);
        
        sql.append(" WHERE ");
        
        encodeColumnName("F_TABLE_SCHEMA", sql);
        sql.append(" = ?").append(" AND ");
        
        encodeColumnName("F_TABLE_NAME", sql);
        sql.append(" = ?");
        
        if (columnName != null) {
            sql.append(" AND ");
            encodeColumnName("F_GEOMETRY_COLUMN", sql);
            sql.append(" = ?");
        }
        
        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        try {
            ps.setString(1, schemaName);
            ps.setString(2, tableName);
            if (columnName != null) {
                ps.setString(3, columnName);
            }
            
            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    TessellationInfo tinfo = new TessellationInfo();
                    tinfo.setUBounds(new Envelope(rs.getDouble("UxMin"), rs.getDouble("UxMax"), 
                        rs.getDouble("UyMin"), rs.getDouble("UyMax")));
                    tinfo.setNx(rs.getInt("NX"));
                    tinfo.setNy(rs.getInt("NY"));
                    tinfo.setLevels(rs.getInt("LEVELS"));
                    tinfo.setScale(rs.getDouble("SCALE"));
                    tinfo.setShift(rs.getInt("SHIFT"));
                    tinfo.setColumName(rs.getString("F_GEOMETRY_COLUMN"));
                    
                    tinfo.setSchemaName(schemaName);
                    tinfo.setTableName(tableName);
                    
                    //look up the spatial index table
                    tables = cx.getMetaData().getTables(
                        null, schemaName, tableName+"_"+columnName+"_idx", new String[]{"TABLE"});
                    try {
                        if (tables.next()) {
                            tinfo.setIndexTableName(tables.getString("TABLE_NAME"));
                        }
                    }
                    finally {
                        dataStore.closeSafe(tables);
                    }
                    
                    tinfos.add(tinfo);
                }
            }
            finally {
                dataStore.closeSafe(rs);
            }
        }
        finally {
            dataStore.closeSafe(ps);
        }
        
        return tinfos;
    }
    
    /**
     * Returns the database typename of the column by inspecting the metadata.
     */
    public String lookupSqlTypeName(Connection cx, String schemaName, String tableName, 
        String columnName) throws SQLException {

        ResultSet columns =  null;
        try {
            columns = cx.getMetaData().getColumns(null, schemaName, tableName, columnName);
            columns.next();
    
            return columns.getString("TYPE_NAME");
        } 
        finally {
            columns.close();
        }
    }
    
    void encodeTableName(String schemaName, String tableName, StringBuffer sql) {
        if (schemaName != null && !"".equals(schemaName.trim())) {
            encodeSchemaName(schemaName, sql);
            sql.append(".");
        }
        encodeTableName(tableName, sql);
    }
    
    @Override
    public void postCreateAttribute(AttributeDescriptor att, String tableName, String schemaName,
            Connection cx) throws SQLException {
        if (att instanceof GeometryDescriptor) {
            //look up tessellation info
            TessellationInfo tinfo = 
                lookupTessellationInfo(cx, schemaName, tableName, att.getLocalName());
            if (tinfo != null) {
                att.getUserData().put(TessellationInfo.KEY, tinfo);
            }
            else {
                LOGGER.fine(String.format("%s.%s.(%s) does not have tessellation entry.", schemaName, 
                    tableName, att.getLocalName()));
            }
        }
    }
    
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx) 
        throws SQLException {

        String tableName = featureType.getName().getLocalPart();

        // register all geometry columns in the database
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            if (att instanceof GeometryDescriptor) {
                GeometryDescriptor gd = (GeometryDescriptor) att;
                
                //figure out the native db srid
                int srid = 0;
                    
                Integer epsg = null;
                if (gd.getCoordinateReferenceSystem() != null) {
                    try {
                        epsg = CRS.lookupEpsgCode(gd.getCoordinateReferenceSystem(), true);
                    }
                    catch(Exception e) {
                        LOGGER.log(Level.WARNING, "Error looking up epsg code", e);
                    }
                }

                if (epsg != null) {
                    String sql = "SELECT SRID FROM SYSSPATIAL.spatial_ref_sys"
                            + " WHERE AUTH_SRID = ?";
                    LOGGER.log(Level.FINE, sql + ";{0}", epsg);
                    
                    PreparedStatement ps = cx.prepareStatement(sql);
                    try {
                        ps.setInt(1, epsg);
                        
                        ResultSet rs = ps.executeQuery();
                        try {
                            if (rs.next()) {
                                srid = rs.getInt("SRID");
                            }
                            else {
                                LOGGER.warning("EPSG Code " + epsg + " does not map to SRID");
                            }
                        }
                        finally {
                            dataStore.closeSafe(ps);
                        }
                    }
                    finally {
                        dataStore.closeSafe(ps);
                    }
                }
                
                // grab the geometry type
                String geomType = CLASS_TO_TYPE.get(gd.getType().getBinding());
                geomType = geomType != null ? geomType : "GEOMETRY";

                //insert into geometry columns table
                String sql = 
                    "INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, " +
                        "F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) " +
                    "VALUES (?, ?, ?, ?, 2, ?, ?)";
                LOGGER.log(Level.FINE, sql + ";{0},{1},{2},{3},{4},{5}",
                    new Object[]{"", schemaName, tableName, gd.getLocalName(), srid, geomType});

                PreparedStatement ps = cx.prepareStatement(sql);
                try {
                    ps.setString(1, "");
                    ps.setString(2, schemaName);
                    ps.setString(3, tableName);
                    ps.setString(4, gd.getLocalName());
                    ps.setInt(5, srid);
                    ps.setString(6, geomType);
                    
                    ps.execute();
                }
                finally {
                    dataStore.closeSafe(ps);
                }

                //create the spatial index table
                PrimaryKey pkey = dataStore.getPrimaryKeyFinder()
                    .getPrimaryKey(dataStore, schemaName, tableName, cx);
                if (!(pkey instanceof NullPrimaryKey)) {
                    String indexTableName = tableName + "_" + gd.getLocalName() + "_idx";
                    
                    StringBuffer sb = new StringBuffer("DROP TABLE ");
                    encodeTableName(schemaName, indexTableName, sb);
                    
                    sql = sb.toString();
                    LOGGER.fine(sql);
                    
                    try {
                        ps = cx.prepareStatement(sql);
                        ps.execute();
                    }
                    catch(SQLException e) {
                        //ignore
                    }
                    finally {
                        dataStore.closeSafe(ps);
                    }

                    sb = new StringBuffer("CREATE MULTISET TABLE ");
                    encodeTableName(schemaName, indexTableName, sb);
                    sb.append("( ");
                    
                    for (PrimaryKeyColumn col : pkey.getColumns()) {
                        encodeColumnName(col.getName(), sb);
                        
                        String typeName = lookupSqlTypeName(cx, schemaName, tableName, col.getName());
                        sb.append(" ").append(typeName).append(", ");
                    }
                    sb.append("cellid INTEGER) PRIMARY INDEX (cellid)");
                    sql = sb.toString();
                    LOGGER.fine(sql);

                    ps = cx.prepareStatement(sql);
                    try {
                        ps.execute();
                    }
                    finally {
                        dataStore.closeSafe(ps);
                    }
                    
                    //TODO: create triggers to keep spatial index in sync
                    /*
                    "CREATE TRIGGER \"{0}_{1}_mi\" AFTER INSERT ON {12}"
                    + "  REFERENCING NEW TABLE AS nt"
                    + "  FOR EACH STATEMENT"
                    + "  BEGIN ATOMIC"
                    + "  ("
                    + "    INSERT INTO {13} SELECT \"{2}\","
                    + "      sysspatial.tessellate_index("
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(), "
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(), "
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(), "
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(), "
                    + "      {3,number,0.0#}, {4,number,0.0#}, {5,number,0.0#}, {6,number,0.0#}, "
                    + "      {7,number,0}, {8,number,0}, {9,number,0}, {10,number,0.0#}, {11,number,0})"
                    // + ")"
                    + "    FROM nt WHERE \"{1}\" IS NOT NULL;"
                    + "  ) " + "END"
                    */
                    
                    /*"CREATE TRIGGER \"{0}_{1}_mu\" AFTER UPDATE OF \"{1}\" ON {12}"
                    + "  REFERENCING NEW AS nt"
                    + "  FOR EACH STATEMENT"
                    + "  BEGIN ATOMIC"
                    + "  ("
                    + "    DELETE FROM {13} WHERE id in (SELECT \"{2}\" from nt); "
                    + "    INSERT INTO {13} SELECT \"{2}\","
                    + "    sysspatial.tessellate_index("
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(), "
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(), "
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(), "
                    + "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(), "
                    + "      {3,number,0.0#}, {4,number,0.0#}, {5,number,0.0#}, {6,number,0.0#}, "
                    + "      {7,number,0}, {8,number,0}, {9,number,0}, {10,number,0.0#}, {11,number,0})"
                    + "    FROM nt WHERE \"{1}\" IS NOT NULL;" + "  ) "*/
                    
                    /*"CREATE TRIGGER \"{0}_{1}_md\" AFTER DELETE ON {2}"
                    + "  REFERENCING OLD TABLE AS ot"
                    + "  FOR EACH STATEMENT"
                    + "  BEGIN ATOMIC"
                    + "  ("
                    + "    DELETE FROM \"{0}_{1}_idx\" WHERE ID IN (SELECT \"{1}\" from ot);"
                    + "  )" + "END"*/
                }
                else {
                    LOGGER.warning("No primary key for " + schemaName + "." + tableName + ". Unable"
                       + " to create spatial index.");
                }
            }
            
            //cx.commit();
        }
    }

    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);

        // jdbc metadata for geom columns reports DATA_TYPE=1111=Types.OTHER
        mappings.put(Geometry.class, Types.OTHER);
    }

    public void registerSqlTypeNameToClassMappings(
            Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);

        mappings.put("ST_Geometry", Geometry.class);
    }
    
    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(
            Map<Integer, String> overrides) {
        overrides.put(Types.VARCHAR, "VARCHAR");
        overrides.put(Types.DOUBLE, "FLOAT");
        overrides.put(Types.NUMERIC, "DECIMAL");
    }

    @Override
    public boolean isLimitOffsetSupported() {
        return false;
    }
    
    @Override
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        return new TeradataFilterToSQL(this);
    }
}
