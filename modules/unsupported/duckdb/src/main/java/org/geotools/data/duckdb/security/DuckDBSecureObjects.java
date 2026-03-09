/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb.security;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;

/** Wraps JDBC objects so all SQL goes through a {@link DuckDBExecutionPolicy}. */
public final class DuckDBSecureObjects {

    private DuckDBSecureObjects() {}

    public static Connection wrapConnection(Connection delegate, DuckDBExecutionPolicy policy) {
        return (Connection) Proxy.newProxyInstance(
                DuckDBSecureObjects.class.getClassLoader(),
                new Class<?>[] {Connection.class},
                new ConnectionInvocationHandler(delegate, policy));
    }

    private static final class ConnectionInvocationHandler implements InvocationHandler {

        private final Connection delegate;
        private final DuckDBExecutionPolicy policy;

        private ConnectionInvocationHandler(Connection delegate, DuckDBExecutionPolicy policy) {
            this.delegate = delegate;
            this.policy = policy;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if ("unwrap".equals(name)) {
                return unwrap(proxy, (Class<?>) args[0], Connection.class);
            }
            if ("isWrapperFor".equals(name)) {
                return isWrapperFor((Class<?>) args[0], Connection.class);
            }
            if ("nativeSQL".equals(name) && args != null && args.length == 1) {
                policy.validateStatementSql((String) args[0]);
            }
            if ("prepareCall".equals(name)) {
                throw new SQLFeatureNotSupportedException(
                        "Callable statements are disabled by DuckDB execution policy '" + policy.getName() + "'");
            }
            if ("prepareStatement".equals(name) && args != null && args.length > 0 && args[0] instanceof String) {
                policy.validateStatementSql((String) args[0]);
                PreparedStatement prepared = (PreparedStatement) invokeDelegate(method, delegate, args);
                return wrapPreparedStatement(prepared, policy, (Connection) proxy);
            }
            if ("createStatement".equals(name)) {
                Statement statement = (Statement) invokeDelegate(method, delegate, args);
                return wrapStatement(statement, policy, (Connection) proxy);
            }
            return invokeDelegate(method, delegate, args);
        }
    }

    private static PreparedStatement wrapPreparedStatement(
            PreparedStatement delegate, DuckDBExecutionPolicy policy, Connection wrappedConnection) {
        return (PreparedStatement) Proxy.newProxyInstance(
                DuckDBSecureObjects.class.getClassLoader(),
                new Class<?>[] {PreparedStatement.class},
                new StatementInvocationHandler(delegate, policy, wrappedConnection));
    }

    private static Statement wrapStatement(
            Statement delegate, DuckDBExecutionPolicy policy, Connection wrappedConnection) {
        return (Statement) Proxy.newProxyInstance(
                DuckDBSecureObjects.class.getClassLoader(),
                new Class<?>[] {Statement.class},
                new StatementInvocationHandler(delegate, policy, wrappedConnection));
    }

    private static final class StatementInvocationHandler implements InvocationHandler {

        private final Statement delegate;
        private final DuckDBExecutionPolicy policy;
        private final Connection wrappedConnection;

        private StatementInvocationHandler(
                Statement delegate, DuckDBExecutionPolicy policy, Connection wrappedConnection) {
            this.delegate = delegate;
            this.policy = policy;
            this.wrappedConnection = wrappedConnection;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if ("unwrap".equals(name)) {
                Class<?> iface = (Class<?>) args[0];
                return unwrap(
                        proxy,
                        iface,
                        PreparedStatement.class.isInstance(proxy) ? PreparedStatement.class : Statement.class);
            }
            if ("isWrapperFor".equals(name)) {
                Class<?> iface = (Class<?>) args[0];
                return isWrapperFor(
                        iface, PreparedStatement.class.isInstance(proxy) ? PreparedStatement.class : Statement.class);
            }
            if ("getConnection".equals(name)) {
                return wrappedConnection;
            }
            if ("addBatch".equals(name) && args != null && args.length == 1 && args[0] instanceof String) {
                policy.validateStatementSql((String) args[0]);
            }
            if (name.startsWith("execute") && args != null && args.length > 0 && args[0] instanceof String) {
                policy.validateStatementSql((String) args[0]);
            }
            return invokeDelegate(method, delegate, args);
        }
    }

    private static Object unwrap(Object proxy, Class<?> iface, Class<?> apiType) throws SQLException {
        if (iface.isInstance(proxy) || iface == apiType || iface == Object.class) {
            return proxy;
        }
        throw new SQLException("Unwrapping to " + iface.getName() + " is disabled by the DuckDB execution policy");
    }

    private static boolean isWrapperFor(Class<?> iface, Class<?> apiType) {
        return iface == apiType || iface.isAssignableFrom(apiType);
    }

    private static Object invokeDelegate(Method method, Object delegate, Object[] args) throws Throwable {
        try {
            return method.invoke(delegate, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
