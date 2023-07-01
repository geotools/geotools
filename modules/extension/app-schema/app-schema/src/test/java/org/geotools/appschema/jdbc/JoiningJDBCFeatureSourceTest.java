/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.geotools.data.Transaction;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.data.oracle.OracleDialect;
import org.geotools.data.postgis.PostGISDialect;
import org.geotools.data.store.ContentEntry;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCState;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PrimaryKey;
import org.junit.Test;
import org.mockito.Mockito;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory2;

public class JoiningJDBCFeatureSourceTest {

    private FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testMultipleIds() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        SimpleFeatureType origType = builder.buildFeatureType();

        JoiningQuery query = new JoiningQuery();
        JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
        join.getIds().add("one");
        join.getIds().add("two");
        query.setQueryJoins(Collections.singletonList(join));

        JDBCDataStore mockStore = Mockito.mock(JDBCDataStore.class);
        ContentEntry mockEntry = Mockito.mock(ContentEntry.class);
        Mockito.when(mockStore.getPrimaryKey(origType))
                .thenReturn(new PrimaryKey(null, Collections.emptyList()));

        Mockito.when(mockStore.getSQLDialect()).thenReturn(new PostGISDialect(mockStore));
        Mockito.when(mockEntry.getDataStore()).thenReturn(mockStore);
        JoiningJDBCFeatureSource source =
                new JoiningJDBCFeatureSource(new JDBCFeatureSource(mockEntry, null));
        SimpleFeatureType type = source.getFeatureType(origType, query);
        assertNotNull(type);
        assertEquals("FOREIGN_ID_0_0", type.getDescriptor(0).getName().getLocalPart());
        assertEquals("FOREIGN_ID_0_1", type.getDescriptor(1).getName().getLocalPart());
    }

    @Test
    public void testCountQuery() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        builder.add("testAttr", String.class);
        SimpleFeatureType origType = builder.buildFeatureType();

        JoiningQuery query = new JoiningQuery();
        JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
        join.getIds().add("one");
        join.getIds().add("two");
        query.setQueryJoins(Collections.singletonList(join));
        query.setFilter(FF.equals(FF.property("testAttr"), FF.literal("testValue")));

        // create store
        JDBCDataStore store = new JDBCDataStore();
        OracleDialect dialect = new OracleDialect(store);
        store.setSQLDialect(dialect);
        // mock entry and state
        ContentEntry mockEntry = Mockito.mock(ContentEntry.class);
        Mockito.when(mockEntry.getDataStore()).thenReturn(store);
        JDBCState state = Mockito.mock(JDBCState.class);
        Mockito.when(mockEntry.getState(Mockito.any(Transaction.class))).thenReturn(state);
        Mockito.when(state.getPrimaryKey())
                .thenReturn(new PrimaryKey(null, Collections.emptyList()));

        JoiningJDBCFeatureSource source = Mockito.mock(JoiningJDBCFeatureSource.class);
        Mockito.when(source.getEntry()).thenReturn(mockEntry);
        Mockito.when(source.createFilterToSQL(origType))
                .thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.createFilterToSQL(origType, true))
                .thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.getDataStore()).thenReturn(store);

        // execute real createCountQuery method
        Set<String> columns = new HashSet<>(Arrays.asList("one", "two"));
        AtomicReference<PreparedFilterToSQL> toSQLRef = new AtomicReference<>();
        Mockito.doCallRealMethod()
                .when(source)
                .createCountQuery(dialect, origType, query, columns, toSQLRef);

        String sql = source.createCountQuery(dialect, origType, query, columns, toSQLRef);
        // assert that between table name and WHERE clause we have a space
        assertEquals(
                "SELECT COUNT(*) FROM (SELECT DISTINCT TEST.ONE, TEST.TWO FROM TEST WHERE testAttr = ?)DISTINCT_TABLE",
                sql);
    }
}
