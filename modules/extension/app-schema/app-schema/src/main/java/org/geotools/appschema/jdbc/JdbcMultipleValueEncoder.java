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

import java.io.IOException;
import java.io.Writer;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.config.JdbcMultipleValue;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;

/** Visitor that encode a JDBC multivalued mapping to SQL. */
public final class JdbcMultipleValueEncoder extends DuplicatingFilterVisitor {

    private final JDBCDataStore store;
    private final Writer output;
    private final FeatureTypeMapping typeMapping;
    private final SimpleFeatureType featureType;

    public JdbcMultipleValueEncoder(FeatureTypeMapping typeMapping, Writer output) {
        this.store = (JDBCDataStore) typeMapping.getSource().getDataStore();
        this.output = output;
        this.typeMapping = typeMapping;
        this.featureType = (SimpleFeatureType) typeMapping.getSource().getSchema();
    }

    @Override
    protected Expression visit(Expression expression, Object extraData) {
        if (!(expression instanceof JdbcMultipleValue)) {
            return super.visit(expression, extraData);
        }
        JdbcMultipleValue multipleValue = (JdbcMultipleValue) expression;
        FilterToSQL filterToSql = createFilterToSQL(multipleValue);
        filterToSql.setFieldEncoder(
                field -> {
                    StringBuffer sql = new StringBuffer();
                    store.dialect.encodeTableName(multipleValue.getId(), sql);
                    sql.append(".");
                    sql.append(field);
                    return sql.toString();
                });
        try {
            return new MultipleValueExpressionHolder(
                    output,
                    filterToSql.encodeToString(((JdbcMultipleValue) expression).getTargetValue()));
        } catch (FilterToSQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static final class MultipleValueExpressionHolder implements Expression {

        private final Writer output;
        private final String expression;

        private MultipleValueExpressionHolder(Writer output, String expression) {
            this.output = output;
            this.expression = expression;
        }

        @Override
        public Object evaluate(Object object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T evaluate(Object object, Class<T> context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            if (visitor instanceof FilterToSQL) {
                try {
                    output.write(expression);
                } catch (Exception exception) {
                    throw new RuntimeException(
                            String.format(
                                    "Error writing multiple value expression '%s' to output.",
                                    expression),
                            exception);
                }
            }
            return null;
        }
    }

    private FilterToSQL createFilterToSQL(JdbcMultipleValue multipleValue) {
        SimpleFeatureType ft;
        try {
            ft = store.getSchema(multipleValue.getTargetTable());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (store.getSQLDialect() instanceof PreparedStatementSQLDialect) {
            PreparedFilterToSQL preparedFilterToSQL = store.createPreparedFilterToSQL(ft);
            preparedFilterToSQL.setPrepareEnabled(false);
            return preparedFilterToSQL;
        }
        return store.createFilterToSQL(ft);
    }
}
