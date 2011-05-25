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

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.jdbc.FilterToSQL;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

public abstract class BasicSQLDialect extends SQLDialect {

    protected BasicSQLDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    /**
     * Encodes a value in an sql statement.
     * <p>
     * Subclasses may wish to override or extend this method to handle specific
     * types. This default implementation does the following:
     * <ol>
     *   <li>The <tt>value</tt> is encoded via its {@link #toString()} representation.
     *   <li>If <tt>type</tt> is a character type (extends {@link CharSequence}),
     *   it is wrapped in single quotes (').
     * </ol>
     * </p>
     *
     */
    public void encodeValue(Object value, Class type, StringBuffer sql) {
        
        //turn the value into a literal and use FilterToSQL to encode it
        Literal literal = dataStore.getFilterFactory().literal( value );
        FilterToSQL filterToSQL = dataStore.createFilterToSQL(null);
        
        StringWriter w = new StringWriter();
        filterToSQL.setWriter(w);
        
        filterToSQL.visit(literal,type);
        
        sql.append( w.getBuffer().toString() );
//        if (CharSequence.class.isAssignableFrom(type)) {
//            sql.append("'").append(value).append("'");
//        } else {
//            sql.append(value);
//        }
    }
    
    /**
     * Encodes a geometry value in an sql statement.
     * <p>
     * An implementations should serialize <tt>value</tt> into some exchange
     * format which will then be transported to the underlying database. For
     * example, consider an implementation which converts a geometry into its
     * well known text representation:
     * <pre>
     *   <code>
     *   sql.append( "GeomFromText('" );
     *   sql.append( new WKTWriter().write( value ) );
     *   sql.append( ")" );
     *   </code>
     *  </pre>
     * </p>
     * <p>
     *  The <tt>srid</tt> parameter is the spatial reference system identifier
     *  of the geometry, or 0 if not known.
     * </p>
     */
    public abstract void encodeGeometryValue(Geometry value, int srid, StringBuffer sql)
        throws IOException;

    /**
     * Creates the filter encoder to be used by the datastore when encoding 
     * query predicates.
     * <p>
     * Sublcasses can override this method to return a subclass of {@link FilterToSQL}
     * if need be.
     * </p>
     */
    public FilterToSQL createFilterToSQL() {
        FilterToSQL f2s = new FilterToSQL();
        f2s.setCapabilities(BASE_DBMS_CAPABILITIES);
        return f2s;
    }

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
    public void onSelect(Statement select, Connection cx, SimpleFeatureType featureType) throws SQLException {
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
    public void onDelete(Statement delete, Connection cx, SimpleFeatureType featureType) throws SQLException {
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
    public void onInsert(Statement insert, Connection cx, SimpleFeatureType featureType) throws SQLException {
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
    public void onUpdate(Statement update, Connection cx, SimpleFeatureType featureType) throws SQLException {
    }
}
