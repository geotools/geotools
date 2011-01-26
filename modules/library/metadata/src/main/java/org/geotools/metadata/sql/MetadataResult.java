/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata.sql;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.geotools.util.logging.Logging;


/**
 * The result of a query for metadata attributes. This object {@linkplain PreparedStatement
 * prepares a statement} once for ever for a given table. When a particular record in this
 * table is fetched, the {@link ResultSet} is automatically constructed. If many attributes
 * are fetched consecutivly for the same record, then the same {@link ResultSet} is reused.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @todo Automatically close the ResultSet after some delay (e.g. 2 minutes).
 */
final class MetadataResult {
    /**
     * The table name, used for formatting error message.
     */
    private final String tableName;

    /**
     * The statements for a specific table.
     */
    private final PreparedStatement statement;

    /**
     * The results, or {@code null} if not yet determined.
     */
    private ResultSet results;

    /**
     * The identifier (usually the primary key) for current results.
     * If the record to fetch doesn't have the same identifier, then
     * the {@link #results} will need to be closed and reconstructed.
     */
    private String identifier;

    /**
     * Constructs a metadata result from the specified connection.
     *
     * @param  connection The connection to the database.
     * @param  query The SQL query. The first question mark will be replaced
     *               by the table name.
     * @param  tableName The table name.
     * @throws SQLException if the statement can't be created.
     */
    public MetadataResult(final Connection connection,
                          final String     query,
                          final String     tableName)
            throws SQLException
    {
        this.tableName = tableName;
        final int index = query.indexOf('?');
        if (index < 0) {
            // TODO: localize
            throw new SQLException("Invalid query");
        }
        final StringBuilder buffer = new StringBuilder(query);
        buffer.replace(index, index+1, tableName);
        statement = connection.prepareStatement(buffer.toString());
    }

    /**
     * Returns the result set for the given record.
     *
     * @param  identifier The object identifier, usually the primary key value.
     * @return The result set.
     * @throws SQLException if an SQL operation failed.
     */
    private ResultSet getResultSet(final String identifier) throws SQLException {
        if (results != null) {
            if (this.identifier.equals(identifier)) {
                return results;
            }
            if (results.next()) {
                // TODO: Localize
                Logging.getLogger(MetadataResult.class).warning("Duplicate identifier: "+identifier);
            }
            results.close();
            results = null; // In case the 'results = ...' below will fails.
        }
        this.identifier = identifier;
        statement.setString(1, identifier);
        results = statement.executeQuery();
        if (!results.next()) {
            results.close();
            results = null;
            throw new SQLException("Metadata not found: \""+identifier+"\" in table \""+tableName+'"');
            // TODO: localize
        }
        return results;
    }

    /**
     * Returns the attribute value in the given column for the given record.
     *
     * @param  identifier The object identifier, usually the primary key value.
     * @param  columnName The column name of the attribute to search.
     * @return The attribute value.
     * @throws SQLException if an SQL operation failed.
     */
    public Object getObject(final String identifier, final String columnName) throws SQLException {
        return getResultSet(identifier).getObject(columnName);
    }

    /**
     * Returns the attribute value in the given column for the given record.
     *
     * @param  identifier The object identifier, usually the primary key value.
     * @param  columnName The column name of the attribute to search.
     * @return The attribute value.
     * @throws SQLException if an SQL operation failed.
     */
    public Object getArray(final String identifier, final String columnName) throws SQLException {
        final Array array = getResultSet(identifier).getArray(columnName);
        return (array!=null) ? array.getArray() : null;
    }

    /**
     * Returns the attribute value in the given column for the given record.
     *
     * @param  identifier The object identifier, usually the primary key value.
     * @param  columnName The column name of the attribute to search.
     * @return The attribute value.
     * @throws SQLException if an SQL operation failed.
     */
    public int getInt(final String identifier, final String columnName) throws SQLException {
        return getResultSet(identifier).getInt(columnName);
    }

    /**
     * Returns the attribute value in the given column for the given record.
     *
     * @param  identifier The object identifier, usually the primary key value.
     * @param  columnName The column name of the attribute to search.
     * @return The attribute value.
     * @throws SQLException if an SQL operation failed.
     */
    public String getString(final String identifier, final String columnName) throws SQLException {
        return getResultSet(identifier).getString(columnName);
    }

    /**
     * Returns the string value in the first column of the given record.
     * This is used for fetching the name of a code list element.
     *
     * @param  code The object identifier, usually the primary key value.
     * @return The string value found in the first column.
     * @throws SQLException if an SQL operation failed.
     */
    public String getString(final String code) throws SQLException {
        return getResultSet(code).getString(1);
    }

    /**
     * Returns {@code true} if the last value returned by a {@code getFoo} method was null.
     */
    public boolean wasNull() throws SQLException {
        return results.wasNull();
    }

    /**
     * Close this statement and free all resources.
     * After this method has been invoked, this object can't be used anymore.
     *
     * @throws SQLException if an SQL operation failed.
     */
    public void close() throws SQLException {
        if (results != null) {
            results.close();
            results = null;
        }
        statement.close();
    }
}
