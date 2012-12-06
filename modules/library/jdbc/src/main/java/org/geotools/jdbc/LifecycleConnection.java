/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

/**
 * Calls the {@link ConnectionLifecycleListener#onBorrow(Connection)} method on construction and
 * makes sure the {@link ConnectionLifecycleListener#onRelease(Connection)} is called when this
 * connection is closed (the assumption is that the wrapped connection is pooled, as such on "close"
 * it will actually be returned to the pool)
 * 
 * @author Andrea Aime - GeoSolutions
 */
class LifecycleConnection implements Connection {
    
    static final Logger LOGGER = Logging.getLogger(LifecycleConnection.class);

    Connection delegate;
    
    JDBCDataStore store;

    List<ConnectionLifecycleListener> listeners;

    public LifecycleConnection(JDBCDataStore store, Connection delegate, List<ConnectionLifecycleListener> listeners)
            throws SQLException {
        this.delegate = delegate;
        this.listeners = listeners;
        this.store = store;

        for (ConnectionLifecycleListener listener : listeners) {
            listener.onBorrow(store, delegate);
        }
    }
    
    public void commit() throws SQLException {
        for (ConnectionLifecycleListener listener : listeners) {
            listener.onCommit(store, delegate);
        }
        delegate.commit();
    }

    public void rollback() throws SQLException {
        delegate.rollback();
        for (ConnectionLifecycleListener listener : listeners) {
            listener.onRollback(store, delegate);
        }
    }

    public void close() throws SQLException {
        try {
            for (ConnectionLifecycleListener listener : listeners) {
                listener.onRelease(store, delegate);
            }
        } finally {
            delegate.close();
        }
    }
    
    public Statement createStatement() throws SQLException {
        return delegate.createStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return delegate.prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return delegate.prepareCall(sql);
    }

    public String nativeSQL(String sql) throws SQLException {
        return delegate.nativeSQL(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        delegate.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        return delegate.getAutoCommit();
    }

    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        delegate.setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException {
        return delegate.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        delegate.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        return delegate.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        delegate.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        return delegate.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return delegate.createStatement(resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return delegate.getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        delegate.setTypeMap(map);
    }

    public void setHoldability(int holdability) throws SQLException {
        delegate.setHoldability(holdability);
    }

    public int getHoldability() throws SQLException {
        return delegate.getHoldability();
    }

    public Savepoint setSavepoint() throws SQLException {
        return delegate.setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return delegate.setSavepoint(name);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        delegate.rollback(savepoint);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        delegate.releaseSavepoint(savepoint);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return delegate.prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return delegate.prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return delegate.prepareStatement(sql, columnNames);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    public Clob createClob() throws SQLException {
        return delegate.createClob();
    }

    public Blob createBlob() throws SQLException {
        return delegate.createBlob();
    }

    public NClob createNClob() throws SQLException {
        return delegate.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return delegate.createSQLXML();
    }

    public boolean isValid(int timeout) throws SQLException {
        return delegate.isValid(timeout);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        delegate.setClientInfo(name, value);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        delegate.setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException {
        return delegate.getClientInfo(name);
    }

    public Properties getClientInfo() throws SQLException {
        return delegate.getClientInfo();
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return delegate.createArrayOf(typeName, elements);
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return delegate.createStruct(typeName, attributes);
    }

    public void setSchema(String schema) throws SQLException {
        invokeMethodQuietly("setSchema", new Class[] {String.class}, new Object[] {schema}, null);
    }

    public String getSchema() throws SQLException {
        return (String) invokeMethodQuietly("getSchema", null, null, null);
    }

    public void abort(Executor executor) throws SQLException {
        invokeMethodQuietly("abort", new Class[] {Executor.class}, new Object[] {executor}, null);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        invokeMethodQuietly("setNetworkTimeout", new Class[] {Executor.class, int.class}, new Object[] {executor, milliseconds}, null);
    }

    public int getNetworkTimeout() throws SQLException {
        return (Integer) invokeMethodQuietly("getNetworkTimeout", null, null, 0);
    }

    private Object invokeMethodQuietly(String methodName, Class[] paramTypes, Object[] params, Object defaultValue) {
        try {
            if(paramTypes == null) {
                Method method = delegate.getClass().getDeclaredMethod(methodName);
                return method.invoke(delegate);
            } else {
                Method method = delegate.getClass().getDeclaredMethod(methodName, paramTypes);
                return method.invoke(delegate, params);
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINER, "Failed to invoke delegate method", e);
            return defaultValue;
        }
    }

}
