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
package org.geotools.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;

/**
 * A {@link ConnectionLifecycleListener} that executes custom SQL commands on connection grab and
 * release. The SQL commands can contain environment variable references, where the environment
 * variable reference contains a name and an eventual default value.
 *
 * Parsing rules are:
 * <ul>
 * <li>whatever is between <code>${</code> and <code>}</code> is considered a environment variable
 * reference in the form <code>${name,defaultvalue}, the default value being optional</li>
 * <li><code>$</code> and <code>}</code> can be used stand alone only escaped with <code>\</code>
 * (e.g. <code>\$</code> and <code>\}</code>)</li>
 * <li><code>\</code> can be used stand alone only escaped with another <code>\</code></li> (e.g.
 * <code>\\</code>)
 * <li>a environment variable name cannot contain a comma, which is used as the separator between the
 * environment variable name and its default value (first comma acts as a separator)</li>
 * <li>the default value is always interpreted as a string and expanded as such in the sql commands</li>
 * </ul>
 *
 * Examples of valid expressions:
 * <ul>
 * <li>"one two three \} \$ \\" (simple literal with special chars escaped)</li>
 * <li>"My name is ${name}" (a simple environment variable reference without a default value)</li>
 * <li>"My name is ${name,Joe}" (a simple environment variable reference with a default value)</li>
 * </ul>
 *
 * Examples of non valid expressions:
 * <ul>
 * <li>"bla ${myAttName" (unclosed expression section)</li>
 * <li>"bla } bla" (<code>}</code> is reserved, should have been escaped)</li>
 *
 *
 * @author Andrea Aime - GeoSolutions
 */
public class SessionCommandsListener implements ConnectionLifecycleListener {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private Expression sqlOnBorrow;

    private Expression sqlOnRelease;

    public SessionCommandsListener(String sqlOnBorrow, String sqlOnRelease) {
        this.sqlOnBorrow = expandEviromentVariables(sqlOnBorrow);
        this.sqlOnRelease = expandEviromentVariables(sqlOnRelease);
    }

    @Override
    public void onBorrow(JDBCDataStore store, Connection cx) throws SQLException {
        if (sqlOnBorrow != null) {
            String command = sqlOnBorrow.evaluate(null, String.class);
            if (!"".equals(command)) {
                Statement st = null;
                try {
                    st = cx.createStatement();
                    st.execute(command);
                } finally {
                    store.closeSafe(st);
                }
            }
        }
    }

    @Override
    public void onRelease(JDBCDataStore store, Connection cx) throws SQLException {
        if (sqlOnRelease != null) {
            String command = sqlOnRelease.evaluate(null, String.class);
            Statement st = null;
            try {
                st = cx.createStatement();
                st.execute(command);
            } finally {
                store.closeSafe(st);
            }
        }
    }

    @Override
    public void onCommit(JDBCDataStore store, Connection cx) {
        // nothing to do
    }

    @Override
    public void onRollback(JDBCDataStore store, Connection cx) {
        // nothing to do
    }

    /**
     * Parses the original sql command and returns a Expression that has all environment variable references expanded to
     * a {@link EnvFunction} call. This code is partially copied from gt-renderer ExpressionExtractor code, but
     * simplified to only have environment variable references instead of CQL to avoid creating a dependency cascading
     * issue (ExpressionExtractor would have to be moved to gt-cql and gt-jdbc made to depend on it.
     */
    Expression expandEviromentVariables(String sql) {
        if (sql == null || "".equals(sql)) {
            return null;
        }

        boolean inEnvVariable = false;
        List<Expression> expressions = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sql.length(); i++) {
            final char curr = sql.charAt(i);
            final boolean last = i == sql.length() - 1;
            final char next = last ? 0 : sql.charAt(i + 1);

            if (curr == '\\') {
                if (last) throw new IllegalArgumentException("Unescaped \\ at position " + (i + 1));

                if (next == '\\') sb.append('\\');
                else if (next == '$') sb.append('$');
                else if (next == '}') sb.append('}');
                else throw new IllegalArgumentException("Unescaped \\ at position " + (i + 1));

                // skip the next character
                i++;
            } else if (curr == '$') {
                if (last || next != '{') throw new IllegalArgumentException("Unescaped $ at position " + (i + 1));
                if (inEnvVariable)
                    throw new IllegalArgumentException("Already found a ${ sequence before the one at " + (i + 1));

                // if we extracted a literal in between two expressions, add it to the result
                if (sb.length() > 0) {
                    expressions.add(ff.literal(sb.toString()));
                    sb.setLength(0);
                }

                // mark the beginning and skip the next character
                inEnvVariable = true;
                i++;
            } else if (curr == '}' && inEnvVariable) {
                if (sb.length() == 0)
                    throw new IllegalArgumentException(
                            "Invalid empty environment variable reference ${} at " + (i - 1));

                String name = sb.toString();
                String defaultValue = null;
                int idx = name.indexOf(',');
                if (idx >= 0) {
                    if (idx == 0) {
                        throw new IllegalArgumentException("There is no variable name before "
                                + "the comma, the valid format is '${name,defaultValue}'");
                    } else if (idx < name.length() - 1) {
                        defaultValue = name.substring(idx + 1);
                        name = name.substring(0, idx);
                    }
                }
                Expression env;
                if (defaultValue != null) {
                    env = ff.function("env", ff.literal(name), ff.literal(defaultValue));
                } else {
                    env = ff.function("env", ff.literal(name));
                }
                expressions.add(env);
                sb.setLength(0);
                inEnvVariable = false;

            } else {
                sb.append(curr);
            }
        }

        // when done, if we are still in a environment variable reference it means it hasn't been
        // closed
        if (inEnvVariable) {
            throw new IllegalArgumentException("Unclosed environment variable reference '" + sb + "'");
        } else if (sb.length() > 0) {
            expressions.add(ff.literal(sb.toString()));
        }

        // now concatenate back all the references
        if (expressions == null || expressions.isEmpty())
            throw new IllegalArgumentException("The SQL command appears to be empty: " + sql);

        Expression result = expressions.get(0);
        for (int i = 1; i < expressions.size(); i++) {
            result = ff.function("strConcat", result, expressions.get(i));
        }

        return result;
    }
}
