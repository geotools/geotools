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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Map;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.VirtualTable;
import org.junit.Test;

public class DuckDBDataStoresTest {

    @Test
    public void testVirtualTablesAreDisabled() throws Exception {
        JDBCDataStore store = DuckDBDataStores.guard(new JDBCDataStore());

        try {
            store.createVirtualTable(new VirtualTable("vt", "select 1"));
            fail("Expected virtual table creation to be rejected");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("virtual tables are disabled"));
        }
    }

    @Test
    public void testBlockingVirtualTableMapCollectionViewsAreUnmodifiable() throws Exception {
        DuckDBDataStores.BlockingVirtualTableMap map = new DuckDBDataStores.BlockingVirtualTableMap();
        seedMap(map, "seed", new VirtualTable("seed", "select 1"));

        assertUnsupported(() -> map.keySet().clear());
        assertUnsupported(() -> map.values().clear());
        assertUnsupported(() -> map.entrySet().clear());

        assertTrue(map.containsKey("seed"));
    }

    @SuppressWarnings("unchecked")
    private void seedMap(DuckDBDataStores.BlockingVirtualTableMap map, String key, VirtualTable value)
            throws ReflectiveOperationException {
        Field field = DuckDBDataStores.BlockingVirtualTableMap.class.getDeclaredField("delegate");
        field.setAccessible(true);
        ((Map<String, VirtualTable>) field.get(map)).put(key, value);
    }

    private void assertUnsupported(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // expected
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
