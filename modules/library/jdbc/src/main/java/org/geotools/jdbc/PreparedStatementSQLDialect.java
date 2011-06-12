/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;


/**
 * SQL dialect which uses prepared statements for database interaction.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 * @source $URL$
 */
public abstract class PreparedStatementSQLDialect extends SQLDialect {

    protected PreparedStatementSQLDialect(JDBCDataStore dataStore) {
        super(dataStore);
        
    }
    
    /**
     * Prepares the geometry value for a prepared statement.
     * <p>
     * This method should be overridden if the implementation needs to 
     * wrap the geometry placeholder in the function. The default implementationg
     * just appends the default placeholder: '?'.
     * </p>
     * @param g The geometry value.
     * @param srid The spatial reference system of the geometry.
     * @param binding The class of the geometry.
     * @param sql The prepared sql statement buffer. 
     */
    public void prepareGeometryValue(Geometry g, int srid, Class binding, StringBuffer sql ) {
        sql.append( "?" );
    }
    
    /**
     * Prepares a function argument for a prepared statement.
     * 
     * @param clazz The mapped class of the argument.
     * @param sql The prepared sql statement buffer
     */
    public void prepareFunctionArgument(Class clazz, StringBuffer sql) {
        sql.append("?");
    }
    
    /**
     * Sets the geometry value into the prepared statement. 
     * @param g The geometry
     * @param srid the geometry native srid (should be forced into the encoded geometry)
     * @param binding the geometry type
     * @param ps the prepared statement
     * @param column the column index where the geometry is to be set
     * @throws SQLException
     */
    public abstract void setGeometryValue(Geometry g, int srid,
            Class binding, PreparedStatement ps, int column) throws SQLException;

    /**
     * Sets a value in a prepared statement, for "basic types" (non-geometry).
     * <p>
     * Subclasses should override this method if they need to do something custom or they 
     * wish to support non-standard types. 
     * </p>
     * @param value the value.
     * @param binding The class of the value.
     * @param ps The prepared statement.
     * @param column The column the value maps to.
     * @param cx The database connection.
     * @throws SQLException
     */
    public void setValue(Object value, Class binding, PreparedStatement ps,
            int column, Connection cx) throws SQLException {
        
        //get the sql type
        Integer sqlType = dataStore.getMapping( binding );
        
        //handl null case
        if ( value == null ) {
            ps.setNull( column, sqlType );
            return;
        }
        
        switch( sqlType ) {
            case Types.VARCHAR:
                ps.setString( column, (String) convert(value,String.class) );
                break;
            case Types.BOOLEAN:
                ps.setBoolean( column, (Boolean) convert(value,Boolean.class) );
                break;
            case Types.SMALLINT:
                ps.setShort( column, (Short) convert(value,Short.class) );
                break;
            case Types.INTEGER:
                ps.setInt( column, (Integer) convert(value,Integer.class) );
                break;
            case Types.BIGINT:
                ps.setLong( column, (Long) convert(value,Long.class) );
                break;
            case Types.REAL:
                ps.setFloat( column, (Float) convert(value,Float.class) );
                break;
            case Types.DOUBLE:
                ps.setDouble( column, (Double) convert(value,Double.class) );
                break;
            case Types.NUMERIC:
                ps.setBigDecimal( column, (BigDecimal) convert(value,BigDecimal.class) );
                break;
            case Types.DATE:
                ps.setDate( column, (Date) convert(value,Date.class) );
                break;
            case Types.TIME:
                ps.setTime( column, (Time) convert(value,Time.class) );
                break;
            case Types.TIMESTAMP:
                ps.setTimestamp( column, (Timestamp) convert(value,Timestamp.class) );
                break;
            case Types.BLOB:
                ps.setBytes(column, convert(value, byte[].class));
                break;
            case Types.CLOB:
                String string = convert(value, String.class);
                ps.setCharacterStream(column, new StringReader(string), string.length());
                break;
            default:
                ps.setObject( column, value );
        }
        
    }
    
    /*
     * Helper method to convert a value.
     */
    <T> T convert( Object value, Class<T> binding ) {
        if (value == null) {
            return (T) value;
        }
        //convert the value if necessary
        if ( ! binding.isInstance( value ) ) {
            Object converted = Converters.convert(value, binding);
            if ( converted != null ) {
                value = converted;
            }
            else {
                dataStore.getLogger().warning( "Unable to convert " + value + " to " + binding.getName() );
            }
        }
        return (T) value;
    }
    
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        PreparedFilterToSQL f2s = new PreparedFilterToSQL();
        f2s.setCapabilities(BASE_DBMS_CAPABILITIES);
        return f2s;
    }
    
    //callback methods
    /**
     * Callback invoked before a SELECT statement is executed against the database.
     * <p>
     * The callback is provided with both the statement being executed and the database connection.
     * Neither should be closed. Any statements created from the connection however in this method
     * should be closed.
     * </p>
     * @param select The select statement being executed
     * @param cx The database connection
     * @param featureType The feature type the select is executing against.
     * 
     * @throws SQLException
     */
    public void onSelect(PreparedStatement select, Connection cx, SimpleFeatureType featureType) 
        throws SQLException {
    }

    /**
     * Callback invoked before a DELETE statement is executed against the database.
     * <p>
     * The callback is provided with both the statement being executed and the database connection.
     * Neither should be closed. Any statements created from the connection however in this method
     * should be closed.
     * </p>
     * @param delete The delete statement being executed
     * @param cx The database connection
     * @param featureType The feature type the delete is executing against.
     * 
     * @throws SQLException
     */
    public void onDelete(PreparedStatement delete, Connection cx, SimpleFeatureType featureType) 
        throws SQLException {
    }

    /**
     * Callback invoked before an INSERT statement is executed against the database.
     * <p>
     * The callback is provided with both the statement being executed and the database connection.
     * Neither should be closed. Any statements created from the connection however in this method
     * should be closed.
     * </p>
     * @param insert The delete statement being executed
     * @param cx The database connection
     * @param featureType The feature type the insert is executing against.
     * 
     * @throws SQLException
     */
    public void onInsert(PreparedStatement insert, Connection cx, SimpleFeatureType featureType) 
        throws SQLException {
    }

    /**
     * Callback invoked before an UPDATE statement is executed against the database.
     * <p>
     * The callback is provided with both the statement being executed and the database connection.
     * Neither should be closed. Any statements created from the connection however in this method
     * should be closed.
     * </p>
     * @param update The delete statement being executed
     * @param cx The database connection
     * @param featureType The feature type the update is executing against.
     * 
     * @throws SQLException
     */
    public void onUpdate(PreparedStatement update, Connection cx, SimpleFeatureType featureType) throws SQLException {
    }
}
