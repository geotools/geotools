/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.jdbc;

import java.io.StringWriter;
import java.io.Writer;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/** Wraps a filter to SQL encoder allowing us to write directly to the output stream. */
public final class WrappedFilterToSql {

    private final StringWriter out = new StringWriter();

    private final FeatureTypeMapping featureMapping;
    private final JDBCDataStore store;
    private final FilterToSQL filterToSql;

    public WrappedFilterToSql(FeatureTypeMapping featureMapping, FilterToSQL filterToSql) {
        this.featureMapping = featureMapping;
        this.store = (JDBCDataStore) featureMapping.getSource().getDataStore();
        this.filterToSql = filterToSql;
        filterToSql.setWriter(out);
    }

    public void encode(Filter filter) throws FilterToSQLException {
        filterToSql.encode(encodeJdbcMultipleValues(filter, out));
    }

    public String encodeToString(Filter filter) throws FilterToSQLException {
        StringWriter out = new StringWriter();
        filterToSql.setWriter(out);
        filterToSql.encode(encodeJdbcMultipleValues(filter, out));
        return out.toString();
    }

    public void encode(Expression expression) throws FilterToSQLException {
        filterToSql.encode(encodeJdbcMultipleValues(expression, out));
    }

    public String encodeToString(Expression expression) throws FilterToSQLException {
        return filterToSql.encodeToString(encodeJdbcMultipleValues(expression, out));
    }

    public void setSqlNameEscape(String escape) {
        filterToSql.setSqlNameEscape(escape);
    }

    public void setFieldEncoder(FilterToSQL.FieldEncoder fieldEncoder) {
        filterToSql.setFieldEncoder(fieldEncoder);
    }

    private Filter encodeJdbcMultipleValues(Filter filter, Writer out) {
        JdbcMultipleValueEncoder encoder = new JdbcMultipleValueEncoder(featureMapping, out);
        return (Filter) filter.accept(encoder, null);
    }

    private Expression encodeJdbcMultipleValues(Expression expression, Writer out) {
        JdbcMultipleValueEncoder encoder = new JdbcMultipleValueEncoder(featureMapping, out);
        return (Expression) expression.accept(encoder, null);
    }
}
