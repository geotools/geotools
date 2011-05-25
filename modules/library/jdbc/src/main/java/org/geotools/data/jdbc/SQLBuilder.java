/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.factory.Hints;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;

/** Provides an interface for SQL statement construction.
 * 
 *  <p>Currently just doing query building, but obviously this can be extended.
 * 
 *  @author Sean Geoghegan, Defence Science and Technology Organisation.
 *
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public interface SQLBuilder {

    /**
     * Hints supplied by the user.
     * <p>
     * The following hints are of interest when working with Features:
     * <ul>
     * <li>Hints.FEATURE_2D - indicates that only 2 axis are needed
     * <li>Hints.FEATURE_DETACHED - indicate feature can be updated without fear of harming the origional data
     * <li>etc...
     * </ul>
     * @param hints
     * @since 2.4.1
     */
    public void setHints( Hints hints );
    
    /**
     * Makes an SQL Select statement.  Constructs an SQL statement that will select the
     * features from the table based on the filter.  The default implementation creates
     * a select statement for the table with the name <tt>typeName</tt>, selecting all
     * the columns with the names in the <tt>attrTypes</tt> array using the filter
     * as a WHERE clause.  The default implementation ignores the maxFeature parameter
     * since this requires DB dependant SQL.  Subclasses can override this to provide 
     * the maxFeatures functionality specific to their DB.
     *
     * @param typeName  a String with the typeName used as the table to query
     * @param mapper    an FIDMapper
     * @param attrTypes the array of AttributeType elements for the select statement
     * @param filter    the filter to convert to a where statement
     * @param sortBy 
     * @param offset
     * @param limit
     *
     * @return a String representing an SQL statement
     *
     * @throws SQLEncoderException If an error occurs encoding the SQL
     * FIXME: This should change to a FilterToSQLException after SQLEncoder is dropped
     */
    public String buildSQLQuery(String typeName, FIDMapper mapper, 
            AttributeDescriptor[] attrTypes, Filter filter, SortBy[] sortBy, Integer offset, Integer limit) throws SQLEncoderException;

    /**
     * @deprecated use {@link #buildSQLQuery(String, FIDMapper, AttributeDescriptor[], Filter, SortBy[], Integer, Integer)}
     */
    public String buildSQLQuery(String typeName, FIDMapper mapper, 
            AttributeDescriptor[] attrTypes, Filter filter) throws SQLEncoderException;

    /**
     * Returns the Filter required for post processing.
     * <p>
     * The result will be null if no post processing is required.
     * </p>
     * <p>
     * This method is used by DefaultJDBCFeatureSource to see if
     * the a Query can be optimized
     * </p>
     * @param filter
     * @return Filter required for post processing, or <code>null</code>
     */
    public Filter getPostQueryFilter(Filter filter);
    
    public Filter getPreQueryFilter(Filter filter);
    
    /**
     * Produces the select information required.
     * <p>
     * The featureType, if known, is always requested.
     * </p>
     * <p>
     * sql: <code>featureID (,attributeColumn)*</code>
     * </p>
     * <p>
     * We may need to provide AttributeReaders with a hook
     * so they can request a wrapper function.
     * </p>
     * @param sql
     * @param mapper
     * @param attributes
     */
    public void sqlColumns( StringBuffer sql, FIDMapper mapper, AttributeDescriptor attributes[] );
    
    /**
     * Consutrcts FROM clause for featureType
     * <p>
     * sql: <code>FROM typeName</code>
     * </p>
     * @param sql
     * @param typeName
     */
    public void sqlFrom( StringBuffer sql, String typeName);
    
    /**
     * Constructs WHERE clause, if needed, for FILTER.
     * <p>
     * sql: <code>WHERE filter encoding</code>
     * </p>
     * FIXME: This should change to a FilterToSQLException after SQLEncoder is dropped
     */
    public void sqlWhere( StringBuffer sql, Filter preFilter ) throws SQLEncoderException;

    /**
     * Constructs ORDER BY clause.
     * <p>
     * sql: <code>ORDER BY &lt;property1&gt; [ASC|DESC], ....</code>
     * </p>
     * FIXME: This should change to a FilterToSQLException after SQLEncoder is dropped
     * @deprecated use {@link #sqlOrderBy(StringBuffer, FIDMapper, SortBy[])}
     */
    public void sqlOrderBy( StringBuffer sql, SortBy[] sortBy ) throws SQLEncoderException;
    
    /**
     * Lower level method allowing for the encoding of a single expession in sql
     */
    public void encode(StringBuffer sql, Expression expression) throws SQLEncoderException;

    /**
     * Lower level method allowing for the encoding of a single filter in sql
     */

    public void encode(StringBuffer sql, Filter filter) throws SQLEncoderException;
    /**
     * Constructs ORDER BY clause.
     * <p>
     * sql: <code>ORDER BY &lt;property1&gt; [ASC|DESC], ....</code>
     * </p>
     * 
     * @param sql buffer where the complete query is being built
     * @param mapper where to inferr the primary key fields from in case the sortBy list contains
     *            {@link SortBy#NATURAL_ORDER} or {@link SortBy#REVERSE_ORDER}
     * @param sortBy the order by criteria, possibly <code>null</code> 
     * FIXME: This should change to a FilterToSQLException after SQLEncoder is dropped
     */
    public void sqlOrderBy( StringBuffer sql, FIDMapper mapper, SortBy[] sortBy ) throws SQLEncoderException;
}
