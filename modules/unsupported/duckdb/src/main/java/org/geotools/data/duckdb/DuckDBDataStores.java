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
package org.geotools.data.duckdb;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.VirtualTable;

/**
 * Helpers for hardening DuckDB datastore instances.
 *
 * <p>The DuckDB hardened store must not allow virtual tables because a virtual table definition is arbitrary SQL text,
 * which bypasses the "no user-provided SQL" security contract enforced for this module.
 *
 * <p>GeoTools {@link JDBCDataStore} keeps virtual tables in a private field. To enforce this policy without changing
 * GeoTools JDBC core, this class replaces that private map via reflection with a blocking implementation.
 *
 * <p><strong>Operational note:</strong> this reflective replacement can be restricted in hardened runtimes:
 *
 * <ul>
 *   <li>JPMS strong encapsulation (module-path execution) without appropriate {@code opens}/{@code --add-opens}
 *   <li>security-policy environments denying reflective private-field access
 *   <li>native-image or similarly constrained reflection environments
 * </ul>
 *
 * <p>When that happens, datastore construction fails closed (throws), which is intentional: it is safer to reject the
 * datastore than to run with virtual-table hardening disabled.
 */
final class DuckDBDataStores {

    private static final String VIRTUAL_TABLES_FIELD = "virtualTables";

    private DuckDBDataStores() {}

    /**
     * Applies DuckDB hardening to a JDBC datastore instance.
     *
     * <p>This method replaces {@code JDBCDataStore#virtualTables} with a blocking map that rejects all mutating
     * operations, including map-view mutations.
     *
     * <p>The implementation uses reflection because JDBC core does not provide a public hook to replace this structure.
     * If reflective access is not available in the current runtime, this method throws {@link IllegalStateException}
     * and datastore creation must stop.
     *
     * @param store JDBC datastore instance to harden
     * @return the same datastore instance, after hardening
     * @throws IllegalStateException if virtual-table hardening cannot be installed
     */
    static JDBCDataStore guard(JDBCDataStore store) {
        try {
            if (store.getSQLDialect() != null) {
                store.getDBsqlTypesCache().putAll(store.getSqlTypeToSqlTypeNameOverrides());
            }
            Field field = JDBCDataStore.class.getDeclaredField(VIRTUAL_TABLES_FIELD);
            field.setAccessible(true);
            field.set(store, new BlockingVirtualTableMap());
            return store;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to disable DuckDB virtual tables", e);
        }
    }

    /**
     * Map implementation that denies all write paths used by virtual-table registration.
     *
     * <p>The map exposes read-only views for {@link #keySet()}, {@link #values()}, and {@link #entrySet()} to prevent
     * bypass via collection-view mutation methods such as {@code entrySet().clear()}.
     */
    static final class BlockingVirtualTableMap implements Map<String, VirtualTable> {

        private static final String MESSAGE = "DuckDB virtual tables are disabled by the GeoTools security wrapper";

        private final ConcurrentHashMap<String, VirtualTable> delegate = new ConcurrentHashMap<>();
        private final Map<String, VirtualTable> unmodifiableView = Collections.unmodifiableMap(delegate);

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return delegate.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return delegate.containsValue(value);
        }

        @Override
        public VirtualTable get(Object key) {
            return delegate.get(key);
        }

        @Override
        public VirtualTable put(String key, VirtualTable value) {
            throw new UnsupportedOperationException(MESSAGE);
        }

        @Override
        public VirtualTable remove(Object key) {
            throw new UnsupportedOperationException(MESSAGE);
        }

        @Override
        public void putAll(Map<? extends String, ? extends VirtualTable> m) {
            throw new UnsupportedOperationException(MESSAGE);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException(MESSAGE);
        }

        @Override
        public Set<String> keySet() {
            return unmodifiableView.keySet();
        }

        @Override
        public Collection<VirtualTable> values() {
            return unmodifiableView.values();
        }

        @Override
        public Set<Entry<String, VirtualTable>> entrySet() {
            return unmodifiableView.entrySet();
        }
    }
}
