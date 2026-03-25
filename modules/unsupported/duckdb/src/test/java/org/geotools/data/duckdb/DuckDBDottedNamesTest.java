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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.jdbc.JDBCDataStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Regression test for datastore access to tables whose names contain dots. */
public class DuckDBDottedNamesTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder(new File("target"));

    @Test
    public void testBboxFilterOnDottedTableName() throws Exception {
        String tableName = "roads.with.dot";
        JDBCDataStore store =
                DuckDBTestUtils.createStore(tmp.newFolder("dotted-db").toPath().resolve("dotted.duckdb"), false);

        try {
            DuckDBTestUtils.runSetupSql(
                    store,
                    "CREATE TABLE \"" + tableName + "\" (id INTEGER PRIMARY KEY, geometry GEOMETRY, name VARCHAR)",
                    "INSERT INTO \"" + tableName + "\" VALUES (1, ST_GeomFromText('POINT (0 0)'), 'seed')");

            assertTrue(Arrays.asList(store.getTypeNames()).contains(tableName));

            SimpleFeatureSource source = store.getFeatureSource(tableName);
            assertNotNull(source);

            FilterFactory ff = store.getFilterFactory();
            Filter bbox = ff.bbox("geometry", -1, -1, 1, 1, null);
            SimpleFeatureCollection features = source.getFeatures(bbox);
            assertEquals(1, features.size());
        } finally {
            store.dispose();
        }
    }
}
