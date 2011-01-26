package org.geotools.data.ingres;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Blob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

//import net.sf.jsqlparser.schema.Column;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.hsqldb.Types;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKBReader;

public class IngresDialect extends PreparedStatementSQLDialect {

    final static Map<String, Class> TYPE_TO_CLASS_MAP = new HashMap<String, Class>() {
        {
        	put("GEOMETRY", Geometry.class);
        	put("POINT", Point.class);
        	put("LINESTRING", LineString.class);
        	put("POLYGON", Polygon.class);
            put("MULTIPOINT", MultiPoint.class);
            put("MULTILINESTRING", MultiLineString.class);
            put("MULTIPOLYGON", MultiPolygon.class);
            put("GEOMCOLLECTION", GeometryCollection.class);
        }
    };
    
    final static Map<Class, String> CLASS_TO_TYPE_MAP = new HashMap<Class, String>() {
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

    /** Whether to use only primary filters for BBOX filters */
    boolean looseBBOXEnabled = false;
	
    public IngresDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }
    
    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

/*    @Override
    public void encodeGeometryValue(Geometry value, int srid, StringBuffer sql) throws IOException {
        if(value == null) {
            sql.append("NULL");
        } else {
            if (value instanceof LinearRing) {
                //Ingres does not handle linear rings, convert to just a line string
                value = value.getFactory().createLineString(((LinearRing) value).getCoordinateSequence());
            }
            
            sql.append("GeometryFromText('" + value.toText() + "', " + srid + ")");
        }
    }*/

    ThreadLocal<WKBReader> wkbReader = new ThreadLocal<WKBReader>();
    public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs,
            String column, GeometryFactory factory, Connection cx ) throws IOException, SQLException {
        WKBReader reader = wkbReader.get();
        if(reader == null) {
            reader = new WKBReader(factory);
            wkbReader.set(reader);
        }
        try {
        	byte bytes[] = rs.getBytes(column);
        	
        	if(bytes == null) {
        		return null;
        	}
            Geometry geom = (Geometry) new WKBReader().read(bytes);
            return geom;
        }
        catch (Exception e) {
            throw new DataSourceException("An exception occurred while parsing WKB data", e);
        }
    }
    
    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException, IOException {
        try {
            String envelope = rs.getString(column);
            if (envelope != null) {
                return new WKTReader().read(envelope).getEnvelopeInternal();
            } else {
                // empty one
                return new Envelope();
            }
        } catch (ParseException e) {
            throw (IOException) new IOException(
                    "Error occurred parsing the bounds WKT").initCause(e);
        }
    }
    
    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
    	sql.append("AsText(Envelope(" + geometryColumn + "))");
    }
    
    @Override
    public void encodeGeometryColumn(GeometryDescriptor gatt, int srid,
            StringBuffer sql) {
    	sql.append(" AsBinary( ");
    	encodeColumnName(gatt.getLocalName(), sql);
    	sql.append(" ) ");
    }
    
    @Override
    public void prepareGeometryValue(Geometry g, int srid, Class binding,
            StringBuffer sql) {
    	sql.append("GeomFromWKB(?, " + srid + ")");
    }

    @Override
    public void setGeometryValue(Geometry g, int srid, Class binding,
            PreparedStatement ps, int column) throws SQLException {
        if (g != null) {
            if (g instanceof LinearRing ) {
                //postgis does not handle linear rings, convert to just a line string
                g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
            }
            
            byte[] bytes = new WKBWriter().write(g);
            ps.setBytes(column, bytes);
        } else {
            ps.setBytes(column, null);
        }
    }
      
    @Override
    public Integer getGeometrySRID(String schemaName, String tableName, String columnName,
        Connection cx) throws SQLException {
    	
        // first attempt, try with the geometry metadata
        Statement statement = null;
        ResultSet result = null;
        Integer srid = null;
        try {
            
            String sqlStatement = "SELECT SRID FROM GEOMETRY_COLUMNS WHERE ";
            if(schemaName == null) {
                sqlStatement += "F_TABLE_NAME = '" + tableName + "' ";            	
            }
            else {
                sqlStatement += "F_TABLE_SCHEMA = '" + schemaName + "' "
                	+ "AND F_TABLE_NAME = '" + tableName + "' ";
            }
            sqlStatement += "AND F_GEOMETRY_COLUMN = '" + columnName + "'";

            LOGGER.log(Level.FINE, "Geometry type check; {0} ", sqlStatement);
            statement = cx.createStatement();
            result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                srid = result.getInt(1);
            }
/*            dataStore.closeSafe(result);
            
            // if srid is null  then the srid is undefined in the GEOMETRY_COLUMNS table
            // This might not ever be necessary, since if the column is a geometry col, it will
            // exist in GEOMETRY_COLUMNS. Additionally will only work if there is at least one
            // non-null entry in the column
            if(srid == null) {
            	
            	if(schemaName == null) {
                    sqlStatement = "SELECT FIRST 1 SRID(" + columnName + ") " +
                    "FROM " + tableName;
            	}
            	else {
                    sqlStatement = "SELECT FIRST 1 SRID(" + columnName + ") " +
                    "FROM " + schemaName + "." + tableName;
            	}
                result = statement.executeQuery(sqlStatement);
                
                if (result.next()) {
                    srid = result.getInt(1);
                }
            }*/
        } finally {
            dataStore.closeSafe(result);
            dataStore.closeSafe(statement);
        }

        return srid;
    }
    
    
    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(column, sql);
        sql.append(" INT PRIMARY KEY");
    }

    /**Determines the class mapping for a particular column of a table.*/
    @Override
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
            throws SQLException {
    	final int SCHEMA_NAME = 2;
        final int TABLE_NAME = 3;
        final int COLUMN_NAME = 4;
        // grab the information we need to proceed
        String tableName = columnMetaData.getString(TABLE_NAME);
        String columnName = columnMetaData.getString(COLUMN_NAME);
        String schemaName = columnMetaData.getString(SCHEMA_NAME);
        String sqlStatement;
        
        Statement statement = null;
        ResultSet result = null;
        String gType = null;
        try {
        	if(schemaName != null) {
        		sqlStatement = "SELECT GEOMETRY_TYPE FROM GEOMETRY_COLUMNS WHERE " //
                    + "F_TABLE_SCHEMA = '" + schemaName + "' " //
                    + "AND F_TABLE_NAME = '" + tableName + "' " //
                    + "AND F_GEOMETRY_COLUMN = '" + columnName + "'";
        	} else {
        		sqlStatement = "SELECT GEOMETRY_TYPE FROM GEOMETRY_COLUMNS WHERE " //
                    + "F_TABLE_NAME = '" + tableName + "' " //
                    + "AND F_GEOMETRY_COLUMN = '" + columnName + "'";
        	}

            LOGGER.log(Level.FINE, "Geometry type check; {0} ", sqlStatement);
            statement = cx.createStatement();
            result = statement.executeQuery(sqlStatement);
       
            if (result.next()) {
                gType = result.getString(1);  
            }

        } finally {
            dataStore.closeSafe(result);
            dataStore.closeSafe(statement);
        }

        // decode the type into
        // Here gType may be null if database fail to give us an answer,
        // however if it did give an answer, make sure its without leading or trailing whitespaces.
        Class geometryClass = (Class) TYPE_TO_CLASS_MAP.get(gType==null? gType : gType.trim());

        return geometryClass;
    }
    
    @Override
    public void postCreateTable(String schemaName,
            SimpleFeatureType featureType, Connection cx) throws SQLException {
        String tableName = featureType.getName().getLocalPart();
        String sql;
        
        Statement st = null;
        try {
            st = cx.createStatement();

            // register all geometry columns in the database
            for (AttributeDescriptor att : featureType
                    .getAttributeDescriptors()) {
                if (att instanceof GeometryDescriptor) {
                    GeometryDescriptor gd = (GeometryDescriptor) att;
                    
                    //this class is already set right, continue
                    if(gd.getType().getBinding() == Geometry.class) {
                    	continue;
                    }

                    // lookup or reverse engineer the srid
                    int srid = -1;
                    if (gd.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID) != null) {
                        srid = (Integer) gd.getUserData().get(
                                JDBCDataStore.JDBC_NATIVE_SRID);
                    } else if (gd.getCoordinateReferenceSystem() != null) {
                        try {
                            Integer result = CRS.lookupEpsgCode(gd
                                    .getCoordinateReferenceSystem(), true);
                            if (result != null)
                                srid = result;
                        } catch (Exception e) {
                            LOGGER.log(Level.FINE, "Error looking up the "
                                    + "epsg code for metadata "
                                    + "insertion, assuming -1", e);
                        }
                    }

                    // grab the geometry type
                    String geomType = CLASS_TO_TYPE_MAP.get(gd.getType()
                            .getBinding());
                    if (geomType == null)
                        geomType = "GEOMETRY";

                    // alter the table to use the right type
                    if(schemaName != null) {
	                    sql = "ALTER TABLE \"" + schemaName + "\".\"" + tableName + "\" ALTER COLUMN \"" + gd.getLocalName() +
	                    	"\" " + geomType;
                    }
                    else {
	                    sql = "ALTER TABLE \"" + tableName + "\" ALTER COLUMN \"" + gd.getLocalName() +
	                    	"\" " + geomType;	
                    }
                    if(srid != -1) {
                    	sql += " SRID " + srid; 
                    }
                    st.execute( sql );
                }
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    //to register additional mappings if necessary
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        mappings.put("GEOMETRYCOLLECTION", GeometryCollection.class);
        mappings.put("GEOMETRY", Geometry.class);
    }
    
    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);
    }

    @Override
    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        super.registerSqlTypeToClassMappings(mappings);
        mappings.put(new Integer(Types.LONGVARBINARY), byte[].class);
        mappings.put(new Integer(Types.CLOB), String.class);
//        mappings.put(new Integer(Types.LONGVARCHAR), String.class);
    }
        	
    @Override
    public String getSequenceForColumn(String schemaName, String tableName,
            String columnName, Connection cx) throws SQLException {
        String sequenceName = (tableName + "_" + columnName + "_sequence").toLowerCase();
        Statement st = cx.createStatement();
        try {
            // check the user owned sequences
            String sql = "SELECT * FROM IISEQUENCE" +
                " WHERE SEQ_NAME = '" + sequenceName + "'";
            if(schemaName != null) {
                sql += " AND SEQ_OWNER = '" + schemaName + "'";
            }
            ResultSet rs = st.executeQuery(sql);
            try {
                if ( rs.next() ) {
                    return sequenceName; 
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
            String sql = "SELECT NEXT VALUE FOR " + sequenceName;

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
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        if (tableName.equals("geometry_columns")) {
            return false;
        } else if (tableName.startsWith("spatial_ref_sys")) {
            return false;
        } else if (tableName.equals("geography_columns")) {
            return false;
        }
       // others?
        return true;
    }
    
    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        return true;
    }

    // This will be a bit of a hack, since you cannot call current value on a sequence
    // if that sequence wasn't called with next value in the same session.
    @Override
    public Object getLastAutoGeneratedValue(String schemaName, String tableName, String columnName,
            Connection cx) throws SQLException {
        Statement st = cx.createStatement();
        try {
            String sql = "SELECT max(" + columnName +") FROM " + tableName;
            dataStore.getLogger().fine( sql);
            
            ResultSet rs = st.executeQuery( sql);
            try {
                if ( rs.next() ) {
                    return rs.getLong(1);
                }
            }
            finally {
                dataStore.closeSafe(rs);
            }
        }
        finally {
            dataStore.closeSafe(st);
        }

        return null;
    }

    @Override   
    public boolean isLimitOffsetSupported() {
    	return false;
    	//While Ingres does support OFFSET/LIMIT it is not
    	//supported in such a way that GeoTools make use of it
    }
    
    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
    	if(offset > 0) {
    		sql.append(" OFFSET " + offset);
    		if(limit > 0 && limit < Integer.MAX_VALUE) {
    			sql.append(" FETCH NEXT " + limit + " ROWS ONLY ");
    		}
    	} else if(limit > 0 && limit < Integer.MAX_VALUE) {
    		sql.append(" FETCH FIRST " + limit + " ROWS ONLY ");
    	}
    }
    
    @Override
    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException  {
        // if the official EPSG database has an answer, use that one
        CoordinateReferenceSystem crs = super.createCRS(srid, cx);
        if(crs != null)
            return crs;
        
        // otherwise try to use the Ingres spatial_ref_sys WKT
        String sql = "SELECT srtext FROM spatial_ref_sys WHERE srid = " + srid;
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
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        IngresFilterToSQL sql = new IngresFilterToSQL(this);
        sql.setLooseBBOXEnabled(looseBBOXEnabled);
        return sql;
    }
    
    @Override
    public String getGeometryTypeName(Integer type) {
        return "geometry";
    }
}
