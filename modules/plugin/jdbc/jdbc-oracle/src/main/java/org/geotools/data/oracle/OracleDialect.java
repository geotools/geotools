/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.sdo.GeometryConverter;
import org.geotools.data.oracle.sdo.SDOSqlDumper;
import org.geotools.data.oracle.sdo.TT;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.referencing.CRS;
import org.geotools.util.SoftValueHashMap;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;

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

/**
 * 
 * Abstract dialect implementation for Oracle. Subclasses differ on the way used to parse and encode
 * the JTS geoemtries into Oracle MDSYS.SDO_GEOMETRY structures.
 * 
 * @author Justin Deoliveira, OpenGEO
 * @author Andrea Aime, OpenGEO
 *
 * @source $URL$
 */
public class OracleDialect extends PreparedStatementSQLDialect {
    
    private static final int DEFAULT_AXIS_MAX = 1000000;

    private static final int DEFAULT_AXIS_MIN = -10000000;
    
    /**
     * Marks a geometry column as geodetic
     */
    public static final String GEODETIC = "geodetic";
    
    UnWrapper uw;

    /**
     * A map from JTS Geometry type to Oracle geometry type. See Oracle Spatial documentation,
     * Table 2-1, Valid SDO_GTYPE values.
     */
    public static final Map<Class, String> CLASSES_TO_GEOM = Collections.unmodifiableMap(new GeomClasses());
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
    
    static final Map<String, Class> TYPES_TO_CLASSES = new HashMap<String, Class>() {
    	{
    		put("CHAR", String.class);
    		put("NCHAR", String.class);
    		put("NVARCHAR", String.class);
    		put("NVARCHAR2", String.class);
    	}
    };
    
    /**
     * Whether to use only primary filters for BBOX filters 
     */
    boolean looseBBOXEnabled = false;
    
    /**
     * Whether to use estimated extents to build
     */
    boolean estimatedExtentsEnabled = false;
    
    /**
     * Stores srid and their nature, true if geodetic, false otherwise. Avoids repeated
     * accesses to the MDSYS.GEODETIC_SRIDS table
     */
    SoftValueHashMap<Integer, Boolean> geodeticCache = new SoftValueHashMap<Integer, Boolean>(20);
    
    /**
     * Remembers whether the USER_SDO_* views could be accessed or not
     */
    Boolean canAccessUserViews;

    public OracleDialect(JDBCDataStore dataStore) {
        super(dataStore);
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
     * @param cx
     * @return
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

                    if (canAccessUserViews(cx)) {
                        //we only try this block if we are able to access the
                        //user_sdo views

                        //setup the sql to use for the USER_SDO table
                        String userSdoSqlStatement = "SELECT META.SDO_LAYER_GTYPE\n" +
                            "FROM ALL_INDEXES INFO\n" +
                            "INNER JOIN MDSYS.USER_SDO_INDEX_METADATA META\n" +
                            "ON INFO.INDEX_NAME = META.SDO_INDEX_NAME\n" +
                            "WHERE INFO.TABLE_NAME = '" + tableName + "'\n" +
                            "AND REPLACE(meta.sdo_column_name, '\"') = '" + columnName + "'\n";

                        if(schema != null && !"".equals(schema)) {
                            userSdoSqlStatement += " AND INFO.TABLE_OWNER = '" + schema + "'";
                        }

                        Statement userSdoStatement = null;
                        ResultSet userSdoResult = null;
                        try {
                            userSdoStatement = cx.createStatement();
                            LOGGER.log(Level.FINE, "Geometry type check; {0} ", userSdoSqlStatement);
                            userSdoResult = userSdoStatement.executeQuery(userSdoSqlStatement);
                            if (userSdoResult.next()) {
                                String gType = userSdoResult.getString(1);
                                Class geometryClass = (Class) TT.GEOM_CLASSES.get(gType);
                                if(geometryClass == null)
                                    geometryClass = Geometry.class;

                                return geometryClass;
                            }
                        } finally {
                            dataStore.closeSafe(userSdoResult);
                            dataStore.closeSafe(userSdoStatement);
                        }
                    }

                    Statement allSdoStatement = null;
                    ResultSet allSdoResult = null;
                    try {
                        //setup the sql to use for the ALL_SDO table
                        String allSdoSqlStatement = "SELECT META.SDO_LAYER_GTYPE\n" +
                            "FROM ALL_INDEXES INFO\n" +
                            "INNER JOIN MDSYS.ALL_SDO_INDEX_METADATA META\n" +
                            "ON INFO.INDEX_NAME = META.SDO_INDEX_NAME\n" +
                            "WHERE INFO.TABLE_NAME = '" + tableName + "'\n" +
                            "AND REPLACE(meta.sdo_column_name, '\"') = '" + columnName + "'\n";

                        if(schema != null && !"".equals(schema)) {
                            allSdoSqlStatement += " AND INFO.TABLE_OWNER = '" + schema + "'";
                            allSdoSqlStatement += " AND META.SDO_INDEX_OWNER = '" + schema + "'";
                        }

                        allSdoStatement = cx.createStatement();
                        LOGGER.log(Level.FINE, "Geometry type check; {0} ", allSdoSqlStatement);
                        allSdoResult = allSdoStatement.executeQuery(allSdoSqlStatement);
                        if (allSdoResult.next()) {
                            String gType = allSdoResult.getString(1);
                            Class geometryClass = (Class) TT.GEOM_CLASSES.get(gType);
                            if(geometryClass == null)
                                geometryClass = Geometry.class;

                            return geometryClass;
                        } else {
                            //must default at this point, this is as far as we can go
                            return Geometry.class;
                        }

                    } finally {
                        dataStore.closeSafe(allSdoResult);
                        dataStore.closeSafe(allSdoStatement);
                    }

		} else {
			// if we know, return non null value, otherwise returning
			// null will force the datatore to figure it out using 
			// jdbc metadata
			return TYPES_TO_CLASSES.get(typeName);
		}
    }

    
    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        
        if (tableName.endsWith("$"))  {
            return false;
        } else if (tableName.startsWith("BIN$"))  { // Added to ignore some Oracle 10g tables
            return false;
        } else if (tableName.startsWith("XDB$"))  {
            return false;
        } else if (tableName.startsWith("DR$"))  {
            return false;
        } else if (tableName.startsWith("DEF$"))  {
            return false;
        } else if (tableName.startsWith("SDO_"))  {
            return false;
        } else if (tableName.startsWith("WM$"))  {
            return false;
        } else if (tableName.startsWith("WK$"))  {
            return false;
        } else if (tableName.startsWith("AW$"))  {
            return false;
        } else if (tableName.startsWith("AQ$"))  {
            return false;
        } else if (tableName.startsWith("APPLY$"))  {
            return false;
        } else if (tableName.startsWith("REPCAT$"))  {
            return false;
        } else if (tableName.startsWith("CWM$"))  {
            return false;
        } else if (tableName.startsWith("CWM2$"))  {
            return false;
        } else if (tableName.startsWith("EXF$"))  {
            return false;
        } else if (tableName.startsWith("DM$"))  {
            return false;
        } 
        
        return true;
    }
    
    public void registerSqlTypeNameToClassMappings(
            Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        
        mappings.put( "SDO_GEOMETRY", Geometry.class );
        mappings.put( "MDSYS.SDO_GEOMETRY", Geometry.class );
    }
    
    @Override
    public String getNameEscape() {
        return "";
    }
    
    @Override
    public void encodeColumnName(String raw, StringBuffer sql) {
        raw = raw.toUpperCase();
        if(raw.length() > 30)
            raw = raw.substring(0, 30);
        sql.append(raw);
    }
    
    @Override
    public void encodeTableName(String raw, StringBuffer sql) {
        raw = raw.toUpperCase();
        if(raw.length() > 30)
            raw = raw.substring(0, 30);
        sql.append(raw);
    }
    
    @Override
    public String getGeometryTypeName(Integer type) {
        return "MDSYS.SDO_GEOMETRY";
    }
    
    
    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx )
            throws SQLException, IOException {
        Geometry geom = readGeometry(rs, column, new GeometryFactory(), cx);
        return geom != null ? geom.getEnvelopeInternal() : null;
    }

    @Override
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
            ResultSet rs, String column, GeometryFactory factory, Connection cx )
            throws IOException, SQLException {
        
        // read the geometry
        Geometry geom = readGeometry( rs, column, factory, cx );
        return convertGeometry(geom, descriptor, factory);
    }
    
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, int column, GeometryFactory factory, Connection cx) throws IOException ,SQLException {
        // read the geometry
        Geometry geom = readGeometry( rs, column, factory, cx );
        return convertGeometry(geom, descriptor, factory);
    };
    
    Geometry convertGeometry(Geometry geom, GeometryDescriptor descriptor, GeometryFactory factory) {
        //if the geometry is null no need to convert it
        if(geom == null) {
            return null;
        }
        
        // grab the binding
        Class targetClazz = descriptor.getType().getBinding();
        
        // in Oracle you can have polygons in a column declared to be multipolygon, and so on...
        // so we better convert geometries, since our feature model is not so lenient
        if(targetClazz.equals(MultiPolygon.class) && geom instanceof Polygon){
            return factory.createMultiPolygon(new Polygon[] {(Polygon) geom});
        }
        else if(targetClazz.equals(MultiPoint.class) && geom instanceof Point) {
            return factory.createMultiPoint(new Point[] {(Point) geom});
        }
        else if(targetClazz.equals(MultiLineString.class) && geom instanceof LineString) {
            return factory.createMultiLineString(new LineString[] {(LineString) geom});
        }
        else if(targetClazz.equals(GeometryCollection.class)) {
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

    Geometry readGeometry(Object struct, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        if (struct == null) {
            return null;
        }

        // unwrap the connection and create a converter
        OracleConnection ocx = unwrapConnection(cx);
        GeometryConverter converter = factory != null ? new GeometryConverter(ocx, factory)
                : new GeometryConverter(ocx);

        return converter.asGeometry((STRUCT) struct);
    }

    @Override
    public void setGeometryValue(Geometry g, int srid, Class binding, PreparedStatement ps,
            int column) throws SQLException {

        // Handle the null geometry case.
        // Surprisingly, using setNull(column, Types.OTHER) does not work...
        if (g == null) {
            ps.setNull(column, Types.STRUCT, "MDSYS.SDO_GEOMETRY");
            return;
        }

        OracleConnection ocx = unwrapConnection(ps.getConnection());

        GeometryConverter converter = new GeometryConverter(ocx);
        STRUCT s = converter.toSDO(g, srid);
        ps.setObject(column, s);

        if (LOGGER.isLoggable(Level.FINE)) {
            String sdo = SDOSqlDumper.toSDOGeom(g, srid);
            LOGGER.fine("Setting parameter " + column + " as " + sdo);
        }
    }
    
    /**
     * Obtains the native oracle connection object given a database connecetion.
     *
     */
    OracleConnection unwrapConnection( Connection cx ) throws SQLException {
        if(cx == null)
            return null;
        
        if ( cx instanceof OracleConnection ) {
            return (OracleConnection) cx;
        }
        
        try {
            if(uw == null)
                uw = DataSourceFinder.getUnWrapper( cx );
            if ( uw != null ) {
                Connection uwcx = uw.unwrap( cx );
                if ( uwcx != null && uwcx instanceof OracleConnection ) {
                    return (OracleConnection) uwcx;
                }
            }
        } catch(IOException e) {
            throw (SQLException) new SQLException(
                    "Could not obtain native oracle connection.").initCause(e);
        }
        
        throw new SQLException( "Could not obtain native oracle connection for " + cx.getClass());
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
    public Integer getGeometrySRID(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {
        
        if (canAccessUserViews(cx)) {
            StringBuffer userSdoSql = new StringBuffer("SELECT SRID FROM MDSYS.USER_SDO_GEOM_METADATA WHERE ");
            userSdoSql.append( "TABLE_NAME='").append( tableName.toUpperCase() ).append("' AND ");
            userSdoSql.append( "COLUMN_NAME='").append( columnName.toUpperCase() ).append( "'");

            Statement userSdoStatement = null;
            ResultSet userSdoResult = null;
            try {
                userSdoStatement = cx.createStatement();
                LOGGER.log(Level.FINE, "SRID check; {0} ", userSdoSql.toString());
                userSdoResult = userSdoStatement.executeQuery(userSdoSql.toString());
                if (userSdoResult.next()) {
                    Object srid = userSdoResult.getObject( 1 );
                    if ( srid != null ) {
                        //return the SRID number if it was found in the USER_SDO
                        return ((Number) srid).intValue();
                    }
                }
            } finally {
                dataStore.closeSafe(userSdoResult);
                dataStore.closeSafe(userSdoStatement);
            }
        }

        StringBuffer allSdoSql = new StringBuffer("SELECT SRID FROM MDSYS.ALL_SDO_GEOM_METADATA WHERE ");
        allSdoSql.append( "TABLE_NAME='").append( tableName.toUpperCase() ).append("' AND ");
        allSdoSql.append( "COLUMN_NAME='").append( columnName.toUpperCase() ).append( "'");
        if(schemaName != null)
            allSdoSql.append(" AND OWNER='" + schemaName + "'");

        Statement allSdoStatement = null;
        ResultSet allSdoResult = null;
        try {
            allSdoStatement = cx.createStatement();
            LOGGER.log(Level.FINE, "SRID check; {0} ", allSdoSql.toString());
            allSdoResult = allSdoStatement.executeQuery(allSdoSql.toString());
            if (allSdoResult.next()) {
                Object srid = allSdoResult.getObject( 1 );
                if ( srid == null ) {
                    return null;
                }
                //return the SRID number if it was found in the ALL_SDO
                return ((Number) srid).intValue();
            } else {
                return null;
            }
        } finally {
            dataStore.closeSafe(allSdoResult);
            dataStore.closeSafe(allSdoStatement);
        }
    }
    
    @Override
    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException  {
        // if the official EPSG database has an answer, use that one
        CoordinateReferenceSystem crs = super.createCRS(srid, cx);
        if(crs != null)
            return crs;
        
        // otherwise try to decode the WKT, most of the time it's invalid, but
        // for new codes they learned the proper WKT syntax
        String sql = "SELECT WKTEXT FROM MDSYS.CS_SRS WHERE SRID = " + srid;
        Statement st = null;
        ResultSet rs = null; 
        try {
            st = cx.createStatement();
            rs = st.executeQuery( sql.toString() );
            if ( rs.next() ) {
                String wkt = rs.getString(1);
                if ( wkt != null ) {
                    try {
                        return CRS.parseWKT(wkt);
                    } catch(Exception e) {
                        if(LOGGER.isLoggable(Level.FINE))
                            LOGGER.log(Level.FINE, "Could not parse WKT " + wkt, e);
                        return null;
                    }
                }
            }
        } finally {
            dataStore.closeSafe( rs );
            dataStore.closeSafe( st );
        }
        return null;
    }
    
    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        sql.append( "SDO_AGGR_MBR(");
        encodeColumnName(geometryColumn, sql);
        sql.append( ")");
    }
    
    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(String schema, SimpleFeatureType featureType,
            Connection cx) throws SQLException, IOException {
        if (!estimatedExtentsEnabled || dataStore.getVirtualTables().get(featureType.getTypeName()) != null)
            return null;

        String tableName = featureType.getTypeName();
        
        Statement st = null;
        ResultSet rs = null;

        List<ReferencedEnvelope> result = new ArrayList<ReferencedEnvelope>();
        Savepoint savePoint = null;
        try {
            st = cx.createStatement();
            if(!cx.getAutoCommit()) {
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
                    rs = st.executeQuery(sql.toString());

                    if (rs.next()) {
                        // decode the geometry
                        Envelope env = decodeGeometryEnvelope(rs, 1, cx);

                        // reproject and merge
                        if (env != null && !env.isNull()) {
                            CoordinateReferenceSystem crs = ((GeometryDescriptor) att)
                                    .getCoordinateReferenceSystem();
                            result.add(new ReferencedEnvelope(env, crs));
                        }
                    }
                    rs.close();
                }
            }
        } catch(SQLException e) {
            if(savePoint != null) {
                cx.rollback(savePoint);
            }
            LOGGER.log(Level.WARNING, "Failed to use SDO_TUNE.EXTENT_OF, falling back on envelope aggregation", e);
            return null;
        } finally {
            if(savePoint != null) {
                cx.rollback(savePoint);
            }
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);
        }
        return result;
    }

    @Override
    public void postCreateTable(String schemaName,
            SimpleFeatureType featureType, Connection cx) throws SQLException {
        String tableName = featureType.getName().getLocalPart().toUpperCase();
        Statement st = null;
        try  {
            st = cx.createStatement();
            
            // register all geometry columns in the database
            for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                if(att instanceof GeometryDescriptor) {
                    GeometryDescriptor geom = (GeometryDescriptor) att;
                    
                    // guess a tolerance, very small value for geographic data, 10cm for non geographic data
                    // (is there a better way to guess it?), and an extent.
                    // This is a hack for the moment, we need to find a better way to guess the extents,
                    // but unfortunately there is no reliable way to get the extent of a CRS due to
                    // http://jira.codehaus.org/browse/GEOT-1578
                    double tolerance;
                    int dims;
                    double[] min;
                    double[] max;
                    String[] axisNames;
                    if(geom.getCoordinateReferenceSystem() != null) {
                        CoordinateSystem cs = geom.getCoordinateReferenceSystem().getCoordinateSystem();
                        if(geom.getUserData().get(Hints.COORDINATE_DIMENSION) != null) {
                            dims = ((Number) geom.getUserData().get(Hints.COORDINATE_DIMENSION)).intValue();
                        } else {
                            dims = cs.getDimension();
                        }
                        min = new double[dims];
                        max = new double[dims];
                        axisNames = new String[dims];
                        double extent = Double.MAX_VALUE;
                        for (int i = 0; i < dims; i++) {
                            if(i < cs.getDimension()) {
                                CoordinateSystemAxis axis = cs.getAxis(i);
                                axisNames[i] = axis.getAbbreviation();
                                min[i] = Double.isInfinite(axis.getMinimumValue()) ? DEFAULT_AXIS_MIN : axis.getMinimumValue();
                                max[i] = Double.isInfinite(axis.getMaximumValue()) ? DEFAULT_AXIS_MAX : axis.getMaximumValue();
                                if(max[i] - min[i] < extent)
                                    extent = max[i] - min[i];
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
                        axisNames[0] = "X"; axisNames[1] = "Y";
                        min[0] = DEFAULT_AXIS_MIN;
                        min[1] = DEFAULT_AXIS_MIN;
                        max[0]= 10000000;
                        max[1] = 10000000;
                        tolerance = 0.01;
                    }
    
                    int srid = -1;
                    if(geom.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID) != null) {
                        srid = (Integer) geom.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
                    } else if(geom.getCoordinateReferenceSystem() != null) {
                        try {
                            Integer result = CRS.lookupEpsgCode(geom.getCoordinateReferenceSystem(), true);
                            if(result != null)
                                srid = result;
                        } catch(Exception e) {
                            LOGGER.log(Level.FINE, "Error looking up the epsg code for metadata insertion, assuming -1", e);
                        }
                    }
                    
                    // register the metadata
                    String geomColumnName = geom.getLocalName().toUpperCase();
                    String sql = "INSERT INTO USER_SDO_GEOM_METADATA" //        
                                 + "(TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)\n" //
                                 + "VALUES (\n" //
                                 + "'" + tableName + "',\n" //
                                 + "'" + geomColumnName + "',\n" //
                                 + "MDSYS.SDO_DIM_ARRAY(\n";
                    for (int i = 0; i < dims; i++) {
                        sql += "   MDSYS.SDO_DIM_ELEMENT('" + axisNames[i] + "', "+ min[i] + ", " + max[i] +", " + tolerance + ")";
                        if(i < dims - 1)
                            sql += ", ";
                        sql += "\n";
                    }
                    sql = sql +  "),\n" //
                        + (srid == -1 ? "NULL" : String.valueOf(srid)) + ")";
                    LOGGER.log(Level.FINE, "Creating metadata with sql: {0}", sql);
                    st.execute(sql);
                    
                    // figure out the index dimension -> for geodetic data 11G accepts only 2d index,
                    // even if the data is 3d
                    int idxDim = isGeodeticSrid(srid, cx) ? 2 : dims;
                    
                    // create the spatial index (or we won't be able to run spatial predicates)
                    String type = CLASSES_TO_GEOM.get(geom.getType().getBinding());
                    String idxName = tableName +  "_" + geomColumnName + "_IDX";
                    sql = "CREATE INDEX " //
                        + idxName + " ON \"" //
                        + tableName + "\"(\"" + geomColumnName + "\")" //
                        + " INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=" + idxDim;
                    if(type != null)
                        sql += " LAYER_GTYPE=\"" + type + "\"')";
                    else
                        sql += "')"; 
                    LOGGER.log(Level.FINE, "Creating index with sql: {0}", sql);
                    
                    st.execute(sql);
                }
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }
    
    @Override
    public String getSequenceForColumn(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {
        String sequenceName = (tableName + "_" + columnName + "_SEQUENCE").toUpperCase();
        Statement st = cx.createStatement();
        try {
            // check the user owned sequences
            ResultSet rs = st.executeQuery( "SELECT * FROM USER_SEQUENCES" +
                " WHERE SEQUENCE_NAME = '" + sequenceName + "'");
            try {
                if ( rs.next() ) {
                    return sequenceName; 
                }    
            } finally {
                dataStore.closeSafe( rs );
            }
            dataStore.closeSafe( rs );
            
            // that did not work, let's see if the sequence is available in someone else schema
            rs = st.executeQuery( "SELECT * FROM ALL_SEQUENCES" +
                    " WHERE SEQUENCE_NAME = '" + sequenceName + "'");
            try {
                if ( rs.next() ) {
                    String schema = rs.getString("OWNER");
                    return schema + "." + sequenceName;
                }    
            } finally {
                dataStore.closeSafe( rs );
            }
            
            
        } finally {
            dataStore.closeSafe( st );
        }
        
        return null;
    }
    
    @Override
    public Object getNextSequenceValue(String schemaName, String sequenceName,
            Connection cx) throws SQLException {
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery( "SELECT " + sequenceName + ".NEXTVAL FROM DUAL");
            try {
                rs.next();
                return rs.getInt( 1 );
            }
            finally {
                dataStore.closeSafe( rs );
            }
        }
        finally {
            dataStore.closeSafe( st );
        }
        
    }

    /**
     * Checks if the specified srid is geodetic or not
     * @throws SQLException
     */
    protected boolean isGeodeticSrid(Integer srid, Connection cx) {
        if (srid == null)
            return false;
        
        Boolean geodetic = geodeticCache.get(srid); 
        
        if(geodetic == null) { 
            synchronized (this) {
                geodetic = geodeticCache.get(srid);
                
                if(geodetic == null) {
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    boolean closeConnection = false;
                    try {
                        ps = cx.prepareStatement("SELECT COUNT(*) FROM MDSYS.GEODETIC_SRIDS WHERE SRID = ?");
                        ps.setInt(1, srid);
                        rs = ps.executeQuery();
                        rs.next();
                        geodetic = rs.getInt(1) > 0;
                        geodeticCache.put(srid, geodetic);
                    } catch(SQLException e) {
                        LOGGER.log(Level.WARNING, "Could not evaluate if the SRID " + srid + " is geodetic", e);
                    } finally {
                        dataStore.closeSafe(rs);
                        dataStore.closeSafe(ps);
                        if(closeConnection)
                            dataStore.closeSafe(cx);
                    }
                }
            }
        }
        
        return geodetic;
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
        
        if(offset == 0) {
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
    public void registerSqlTypeToSqlTypeNameOverrides(
    		Map<Integer, String> overrides) {
    	super.registerSqlTypeToSqlTypeNameOverrides(overrides);
    	overrides.put(Types.REAL, "DOUBLE PRECISION");
    	overrides.put(Types.DOUBLE, "DOUBLE PRECISION");
    	overrides.put(Types.FLOAT, "FLOAT");
    }
    
    @Override
    public void postCreateAttribute(AttributeDescriptor att, String tableName,
            String schemaName, Connection cx) throws SQLException {
        super.postCreateAttribute(att, tableName, schemaName, cx);
        
        if(att instanceof GeometryDescriptor) {
            Integer srid = (Integer) att.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
            boolean geodetic = isGeodeticSrid(srid, cx);
            att.getUserData().put(GEODETIC, geodetic);
        }
    }

}
