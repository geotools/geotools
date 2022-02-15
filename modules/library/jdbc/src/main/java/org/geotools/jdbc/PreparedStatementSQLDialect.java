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
package org.geotools.jdbc;

import static java.lang.String.format;
import static java.lang.reflect.Array.getLength;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * SQL dialect which uses prepared statements for database interaction.
 *
 * @author Justin Deoliveira, OpenGEO
 */
public abstract class PreparedStatementSQLDialect extends SQLDialect {

    protected PreparedStatementSQLDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    /**
     * Prepares the geometry value for a prepared statement.
     *
     * <p>This method should be overridden if the implementation needs to wrap the geometry
     * placeholder in the function. The default implementationg just appends the default
     * placeholder: '?'.
     *
     * @param gClass The geometry class.
     * @param srid The spatial reference system of the geometry, or -1 if unknown
     * @param dimension The dimensions (2,3,4) of the coordinates, or -1 if unknown
     * @param binding The class of the geometry.
     * @param sql The prepared sql statement buffer.
     */
    public void prepareGeometryValue(
            Class<? extends Geometry> gClass,
            int dimension,
            int srid,
            Class binding,
            StringBuffer sql) {
        sql.append("?");
    }

    /**
     * Prepares the geometry value for a prepared statement.
     *
     * <p>This method should be overridden if the implementation needs to wrap the geometry
     * placeholder in the function. The default implementationg just appends the default
     * placeholder: '?'.
     *
     * @param g The geometry value.
     * @param srid The spatial reference system of the geometry, or -1 if unknown
     * @param dimension The dimensions (2,3,4) of the coordinates, or -1 if unknown
     * @param binding The class of the geometry.
     * @param sql The prepared sql statement buffer.
     */
    public final void prepareGeometryValue(
            Geometry g, int dimension, int srid, Class binding, StringBuffer sql) {
        prepareGeometryValue(g == null ? null : g.getClass(), dimension, srid, binding, sql);
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
     *
     * @param g The geometry
     * @param srid the geometry native srid (should be forced into the encoded geometry)
     * @param binding the geometry type
     * @param ps the prepared statement
     * @param column the column index where the geometry is to be set
     */
    public abstract void setGeometryValue(
            Geometry g, int dimension, int srid, Class binding, PreparedStatement ps, int column)
            throws SQLException;

    /**
     * Sets a value in a prepared statement, for "basic types" (non-geometry).
     *
     * <p>Subclasses should override this method if they need to do something custom or they wish to
     * support non-standard types.
     *
     * @param value the value.
     * @param binding The class of the value.
     * @param ps The prepared statement.
     * @param column The column the value maps to.
     * @param cx The database connection.
     */
    public void setValue(
            Object value, Class binding, PreparedStatement ps, int column, Connection cx)
            throws SQLException {

        // get the sql type
        Integer sqlType = dataStore.getMapping(binding);

        // handle null case
        if (value == null) {
            ps.setNull(column, sqlType);
            return;
        }

        switch (sqlType) {
            case Types.VARCHAR:
                ps.setString(column, convert(value, String.class));
                break;
            case Types.BOOLEAN:
                ps.setBoolean(column, convert(value, Boolean.class));
                break;
            case Types.SMALLINT:
                ps.setShort(column, convert(value, Short.class));
                break;
            case Types.INTEGER:
                ps.setInt(column, convert(value, Integer.class));
                break;
            case Types.BIGINT:
                ps.setLong(column, convert(value, Long.class));
                break;
            case Types.REAL:
                ps.setFloat(column, convert(value, Float.class));
                break;
            case Types.DOUBLE:
                ps.setDouble(column, convert(value, Double.class));
                break;
            case Types.NUMERIC:
                ps.setBigDecimal(column, (BigDecimal) convert(value, BigDecimal.class));
                break;
            case Types.DATE:
                ps.setDate(column, convert(value, Date.class));
                break;
            case Types.TIME:
                ps.setTime(column, convert(value, Time.class));
                break;
            case Types.TIMESTAMP:
                ps.setTimestamp(column, convert(value, Timestamp.class));
                break;
            case Types.BLOB:
                ps.setBytes(column, convert(value, byte[].class));
                break;
            case Types.CLOB:
                String string = convert(value, String.class);
                ps.setCharacterStream(column, new StringReader(string), string.length());
                break;
            default:
                ps.setObject(column, value, Types.OTHER);
        }
    }

    /**
     * Sets a value in a prepared statement, for the specific case of {@link Array}
     *
     * <p>This method uses the standard SQL Array handling, subclasses can override to add special
     * behavior
     *
     * @param value the value.
     * @param att The full attribute description
     * @param ps The prepared statement.
     * @param i The column the value maps to.
     * @param cx The database connection.
     */
    public void setArrayValue(
            Object value, AttributeDescriptor att, PreparedStatement ps, int i, Connection cx)
            throws SQLException {
        if (value == null) {
            ps.setNull(i, Types.ARRAY);
        } else {
            String typeName = getArrayComponentTypeName(att);
            Class<?> componentType =
                    typeName != null
                            ? dataStore.getSqlTypeNameToClassMappings().get(typeName)
                            : String.class;
            Array array = convertToArray(value, typeName, componentType, cx);
            ps.setArray(i, array);
        }
    }

    /**
     * Given the full information about the attribute being transformed, figure out the native SQL
     * Type Name to use when creating a SQL Array object· The default implementation just scans
     * {@link JDBCDataStore#getSqlTypeNameToClassMappings()} backwards, and will fail in case there
     * are ambiguities. Subclasses can implement their own logic and eventually use information
     * contained in the attribute's {@link AttributeDescriptor#getUserData()}, stored at attribute
     * creation time.
     */
    protected String getArrayComponentTypeName(AttributeDescriptor att) throws SQLException {
        Map<String, Class<?>> mappings = dataStore.getSqlTypeNameToClassMappings();
        Class<?> componentType = att.getType().getBinding().getComponentType();
        List<String> sqlTypeNames =
                mappings.entrySet().stream()
                        .filter(e -> e.getValue().equals(componentType))
                        .map(e -> e.getKey())
                        .collect(Collectors.toList());
        if (sqlTypeNames.isEmpty()) {
            throw new SQLException("Failed to find a SQL type for " + componentType);
        } else if (sqlTypeNames.size() > 1) {
            throw new SQLException(
                    String.format(
                            "Found multiple SQL type candidates %s for the Java type %s",
                            sqlTypeNames, componentType.getName()));
        }
        return sqlTypeNames.get(0);
    }

    /**
     * Converts a given array value into a {@link Array}
     *
     * @param value The non null value to be converted
     * @param componentType The attribute binding (of array type)
     * @param connection The connection used to create an {@link Array}
     * @return The converted array
     */
    protected Array convertToArray(
            Object value, String componentTypeName, Class componentType, Connection connection)
            throws SQLException {
        int length = getLength(value);
        Object[] elements = new Object[length];
        for (int i = 0; i < elements.length; i++) {
            Object element = java.lang.reflect.Array.get(value, i);
            if (element == null) {
                elements[i] = null;
            } else {
                Object converted = convertArrayElement(element, componentType);
                elements[i] = converted;
            }
        }
        return connection.createArrayOf(componentTypeName, elements);
    }

    /**
     * Converts a given array element to the desired type, throws an exception in case conversion
     * failed
     *
     * @param value The value to be converted. Must be non null.
     * @param target The target class
     * @return The converted value
     * @throws SQLException In case the conversion failed.
     */
    protected Object convertArrayElement(Object value, Class<?> target) throws SQLException {
        Object converted = Converters.convert(value, target);
        if (converted == null) {
            String message =
                    format("Failed to convert array element %s to target type %s", value, target);
            throw new SQLException(message);
        }
        return converted;
    }

    /*
     * Helper method to convert a value.
     */
    protected <T> T convert(Object value, Class<T> binding) {
        if (value == null) {
            return null;
        }
        // convert the value if necessary
        if (!binding.isInstance(value)) {
            Object converted = Converters.convert(value, binding);
            if (converted != null) {
                value = converted;
            } else {
                dataStore
                        .getLogger()
                        .warning("Unable to convert " + value + " to " + binding.getName());
            }
        }
        return binding.cast(value);
    }

    public PreparedFilterToSQL createPreparedFilterToSQL() {
        PreparedFilterToSQL f2s = new PreparedFilterToSQL(this);
        f2s.setCapabilities(BASE_DBMS_CAPABILITIES);
        return f2s;
    }

    // callback methods
    /**
     * Callback invoked before a SELECT statement is executed against the database.
     *
     * <p>The callback is provided with both the statement being executed and the database
     * connection. Neither should be closed. Any statements created from the connection however in
     * this method should be closed.
     *
     * @param select The select statement being executed
     * @param cx The database connection
     * @param featureType The feature type the select is executing against.
     */
    public void onSelect(PreparedStatement select, Connection cx, SimpleFeatureType featureType)
            throws SQLException {}

    /**
     * Callback invoked before a DELETE statement is executed against the database.
     *
     * <p>The callback is provided with both the statement being executed and the database
     * connection. Neither should be closed. Any statements created from the connection however in
     * this method should be closed.
     *
     * @param delete The delete statement being executed
     * @param cx The database connection
     * @param featureType The feature type the delete is executing against.
     */
    public void onDelete(PreparedStatement delete, Connection cx, SimpleFeatureType featureType)
            throws SQLException {}

    /**
     * Callback invoked before an INSERT statement is executed against the database.
     *
     * <p>The callback is provided with both the statement being executed and the database
     * connection. Neither should be closed. Any statements created from the connection however in
     * this method should be closed.
     *
     * @param insert The delete statement being executed
     * @param cx The database connection
     * @param featureType The feature type the insert is executing against.
     */
    public void onInsert(PreparedStatement insert, Connection cx, SimpleFeatureType featureType)
            throws SQLException {}

    /**
     * Callback invoked before an UPDATE statement is executed against the database.
     *
     * <p>The callback is provided with both the statement being executed and the database
     * connection. Neither should be closed. Any statements created from the connection however in
     * this method should be closed.
     *
     * @param update The delete statement being executed
     * @param cx The database connection
     * @param featureType The feature type the update is executing against.
     */
    public void onUpdate(PreparedStatement update, Connection cx, SimpleFeatureType featureType)
            throws SQLException {}
}
