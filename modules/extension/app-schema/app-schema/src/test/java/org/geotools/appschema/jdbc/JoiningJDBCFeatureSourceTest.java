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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.data.oracle.OracleDialect;
import org.geotools.data.postgis.PostGISDialect;
import org.geotools.data.store.ContentEntry;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCState;
import org.geotools.jdbc.NonIncrementingPrimaryKeyColumn;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PrimaryKey;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.helpers.NamespaceSupport;

public class JoiningJDBCFeatureSourceTest {

    private FilterFactory FF = CommonFactoryFinder.getFilterFactory();

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
        Mockito.when(mockStore.getPrimaryKey(origType)).thenReturn(new PrimaryKey(null, Collections.emptyList()));

        Mockito.when(mockStore.getSQLDialect()).thenReturn(new PostGISDialect(mockStore));
        Mockito.when(mockEntry.getDataStore()).thenReturn(mockStore);
        JoiningJDBCFeatureSource source = new JoiningJDBCFeatureSource(new JDBCFeatureSource(mockEntry, null));
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
        Mockito.when(state.getPrimaryKey()).thenReturn(new PrimaryKey(null, Collections.emptyList()));

        JoiningJDBCFeatureSource source = Mockito.mock(JoiningJDBCFeatureSource.class);
        Mockito.when(source.getEntry()).thenReturn(mockEntry);
        Mockito.when(source.createFilterToSQL(origType)).thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.createFilterToSQL(origType, true)).thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.createFilterToSQL(origType, true, null)).thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.createFilterToSQL(
                        Mockito.any(JDBCDataStore.class),
                        Mockito.any(SimpleFeatureType.class),
                        Mockito.anyBoolean(),
                        Mockito.nullable(String.class)))
                .thenAnswer(invocation -> {
                    PreparedFilterToSQL toSQL = new PreparedFilterToSQL(dialect);
                    String schema = invocation.getArgument(3);
                    if (schema != null) {
                        toSQL.setDatabaseSchema(schema);
                    }
                    return toSQL;
                });
        Mockito.when(source.getDataStore()).thenReturn(store);

        // execute real createCountQuery method
        Set<String> columns = new HashSet<>(Arrays.asList("one", "two"));
        AtomicReference<PreparedFilterToSQL> toSQLRef = new AtomicReference<>();
        Mockito.doCallRealMethod().when(source).createCountQuery(dialect, origType, query, columns, toSQLRef);

        String sql = source.createCountQuery(dialect, origType, query, columns, toSQLRef);
        // assert that between table name and WHERE clause we have a space
        assertTrue(sql.contains(" FROM TEST WHERE "));
    }

    @Test
    public void testCountQueryWithoutIdColumns() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        builder.add("testAttr", String.class);
        SimpleFeatureType origType = builder.buildFeatureType();

        JoiningQuery query = new JoiningQuery();
        query.setFilter(FF.equals(FF.property("testAttr"), FF.literal("testValue")));

        JDBCDataStore store = new JDBCDataStore();
        OracleDialect dialect = new OracleDialect(store);
        store.setSQLDialect(dialect);

        ContentEntry mockEntry = Mockito.mock(ContentEntry.class);
        Mockito.when(mockEntry.getDataStore()).thenReturn(store);
        JDBCState state = Mockito.mock(JDBCState.class);
        Mockito.when(mockEntry.getState(Mockito.any(Transaction.class))).thenReturn(state);
        Mockito.when(state.getPrimaryKey()).thenReturn(new PrimaryKey(null, Collections.emptyList()));

        JoiningJDBCFeatureSource source = Mockito.mock(JoiningJDBCFeatureSource.class);
        Mockito.when(source.getEntry()).thenReturn(mockEntry);
        Mockito.when(source.createFilterToSQL(origType)).thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.createFilterToSQL(origType, true)).thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.createFilterToSQL(origType, true, null)).thenReturn(new PreparedFilterToSQL(dialect));
        Mockito.when(source.createFilterToSQL(
                        Mockito.any(JDBCDataStore.class),
                        Mockito.any(SimpleFeatureType.class),
                        Mockito.anyBoolean(),
                        Mockito.nullable(String.class)))
                .thenAnswer(invocation -> {
                    PreparedFilterToSQL toSQL = new PreparedFilterToSQL(dialect);
                    String schema = invocation.getArgument(3);
                    if (schema != null) {
                        toSQL.setDatabaseSchema(schema);
                    }
                    return toSQL;
                });
        Mockito.when(source.getDataStore()).thenReturn(store);

        AtomicReference<PreparedFilterToSQL> toSQLRef = new AtomicReference<>();
        Mockito.doCallRealMethod()
                .when(source)
                .createCountQuery(dialect, origType, query, Collections.emptySet(), toSQLRef);

        String sql = source.createCountQuery(dialect, origType, query, Collections.emptySet(), toSQLRef);
        String normalized = sql.toUpperCase(Locale.ROOT);

        assertTrue(normalized.startsWith("SELECT COUNT(*) FROM TEST"));
        assertTrue(normalized.contains(" WHERE "));
        assertFalse(normalized.contains("SELECT COUNT(*) FROM (SELECT DISTINCT"));
    }

    @Test
    public void testCountQueryHandlesNullFilter() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        builder.add("testAttr", String.class);
        SimpleFeatureType origType = builder.buildFeatureType();

        JoiningQuery query = new JoiningQuery();

        JDBCDataStore store = new JDBCDataStore();
        OracleDialect dialect = new OracleDialect(store);
        store.setSQLDialect(dialect);

        ContentEntry mockEntry = Mockito.mock(ContentEntry.class);
        Mockito.when(mockEntry.getDataStore()).thenReturn(store);
        JDBCState state = Mockito.mock(JDBCState.class);
        Mockito.when(mockEntry.getState(Mockito.any(Transaction.class))).thenReturn(state);
        Mockito.when(state.getPrimaryKey()).thenReturn(new PrimaryKey(null, Collections.emptyList()));

        JoiningJDBCFeatureSource source = Mockito.mock(JoiningJDBCFeatureSource.class);
        Mockito.when(source.getEntry()).thenReturn(mockEntry);
        Mockito.when(source.getDataStore()).thenReturn(store);

        AtomicReference<PreparedFilterToSQL> toSQLRef = new AtomicReference<>();
        Mockito.doCallRealMethod()
                .when(source)
                .createCountQuery(dialect, origType, query, Collections.emptySet(), toSQLRef);

        String sql = source.createCountQuery(dialect, origType, query, Collections.emptySet(), toSQLRef);
        String normalized = sql.toUpperCase(Locale.ROOT);

        assertTrue(normalized.startsWith("SELECT COUNT(*) FROM TEST"));
        assertFalse(normalized.contains(" WHERE "));
    }

    @Test
    public void testJoiningCountQueryWithoutIdColumns() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("test");
        builder.add("testAttr", String.class);
        SimpleFeatureType origType = builder.buildFeatureType();

        JoiningQuery query = new JoiningQuery();
        query.setFilter(org.geotools.api.filter.Filter.INCLUDE);

        JDBCDataStore store = new JDBCDataStore();
        OracleDialect dialect = new OracleDialect(store);
        store.setSQLDialect(dialect);

        JoiningJDBCFeatureSource source = Mockito.mock(JoiningJDBCFeatureSource.class);
        Mockito.when(source.selectSQL(origType, query, null, true)).thenReturn("SELECT TEST.TESTATTR FROM TEST");

        Method createJoiningCountQuery = JoiningJDBCFeatureSource.class.getDeclaredMethod(
                "createJoiningCountQuery",
                org.geotools.jdbc.SQLDialect.class,
                SimpleFeatureType.class,
                JoiningQuery.class,
                Set.class,
                AtomicReference.class);
        createJoiningCountQuery.setAccessible(true);

        @SuppressWarnings("unchecked")
        String sql =
                (String) createJoiningCountQuery.invoke(source, dialect, origType, query, Collections.emptySet(), null);
        String normalized = sql.toUpperCase(Locale.ROOT);

        assertTrue(
                normalized.contains("SELECT COUNT(*) FROM (SELECT DISTINCT * FROM (SELECT TEST.TESTATTR FROM TEST)"));
    }

    @Test
    public void testSelectSQLCrossSchemaJoin() throws Exception {
        AppSchemaDataAccessRegistry.getAppSchemaProperties()
                .setProperty(AppSchemaDataAccessConfigurator.PROPERTY_CROSS_SCHEMA_JOINING, "true");
        try {
            SimpleFeatureTypeBuilder rootBuilder = new SimpleFeatureTypeBuilder();
            rootBuilder.setName("child_table");
            rootBuilder.add("child_id", Integer.class);
            rootBuilder.add("parent_fk", Integer.class);
            SimpleFeatureType rootType = rootBuilder.buildFeatureType();

            SimpleFeatureTypeBuilder joinBuilder = new SimpleFeatureTypeBuilder();
            joinBuilder.setName("parent_table");
            joinBuilder.add("id", Integer.class);
            SimpleFeatureType joinType = joinBuilder.buildFeatureType();

            @SuppressWarnings("unchecked")
            FeatureSource<FeatureType, Feature> rootSource = Mockito.mock(FeatureSource.class);
            Mockito.when(rootSource.getSchema()).thenReturn(rootType);
            FeatureTypeMapping rootMapping =
                    new FeatureTypeMapping(rootSource, null, Collections.emptyList(), new NamespaceSupport());
            rootMapping.setSourceDatabaseSchema("child_schema");

            @SuppressWarnings("unchecked")
            FeatureSource<FeatureType, Feature> joinSource = Mockito.mock(FeatureSource.class);
            Mockito.when(joinSource.getSchema()).thenReturn(joinType);
            FeatureTypeMapping joinMapping =
                    new FeatureTypeMapping(joinSource, null, Collections.emptyList(), new NamespaceSupport());
            joinMapping.setSourceDatabaseSchema("parent_schema");

            JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
            join.setJoiningTypeName("parent_table");
            join.setJoiningTypeSchema("parent_schema");
            join.setRootMapping(joinMapping);
            join.setForeignKeyName(FF.property("id"));
            join.setJoiningKeyName(FF.property("parent_fk"));
            join.addId("id");

            JoiningQuery query = new JoiningQuery();
            query.setTypeName("child_table");
            query.setRootMapping(rootMapping);
            query.setFilter(org.geotools.api.filter.Filter.INCLUDE);
            query.setQueryJoins(Collections.singletonList(join));

            JDBCDataStore store = Mockito.spy(new JDBCDataStore());
            OracleDialect dialect = new OracleDialect(store);
            store.setSQLDialect(dialect);
            Mockito.doReturn(new PrimaryKey(
                            null,
                            Collections.singletonList(new NonIncrementingPrimaryKeyColumn("child_id", Integer.class))))
                    .when(store)
                    .getPrimaryKey(rootType);

            JoiningJDBCFeatureSource source = Mockito.mock(JoiningJDBCFeatureSource.class);
            Mockito.when(source.getDataStore()).thenReturn(store);
            Mockito.when(source.createFilterToSQL(Mockito.any(SimpleFeatureType.class), Mockito.anyBoolean()))
                    .thenAnswer(invocation -> {
                        PreparedFilterToSQL toSQL = new PreparedFilterToSQL(dialect);
                        toSQL.setPrepareEnabled((Boolean) invocation.getArgument(1));
                        return toSQL;
                    });
            Mockito.when(source.createFilterToSQL(
                            Mockito.any(JDBCDataStore.class),
                            Mockito.any(SimpleFeatureType.class),
                            Mockito.anyBoolean()))
                    .thenAnswer(invocation -> {
                        PreparedFilterToSQL toSQL = new PreparedFilterToSQL(dialect);
                        toSQL.setPrepareEnabled((Boolean) invocation.getArgument(2));
                        return toSQL;
                    });
            Mockito.when(source.createFilterToSQL(
                            Mockito.any(SimpleFeatureType.class), Mockito.anyBoolean(), Mockito.nullable(String.class)))
                    .thenAnswer(invocation -> {
                        PreparedFilterToSQL toSQL = new PreparedFilterToSQL(dialect);
                        toSQL.setPrepareEnabled((Boolean) invocation.getArgument(1));
                        String schema = invocation.getArgument(2);
                        if (schema != null) {
                            toSQL.setDatabaseSchema(schema);
                        }
                        return toSQL;
                    });
            Mockito.when(source.createFilterToSQL(
                            Mockito.any(JDBCDataStore.class),
                            Mockito.any(SimpleFeatureType.class),
                            Mockito.anyBoolean(),
                            Mockito.nullable(String.class)))
                    .thenAnswer(invocation -> {
                        PreparedFilterToSQL toSQL = new PreparedFilterToSQL(dialect);
                        toSQL.setPrepareEnabled((Boolean) invocation.getArgument(2));
                        String schema = invocation.getArgument(3);
                        if (schema != null) {
                            toSQL.setDatabaseSchema(schema);
                        }
                        return toSQL;
                    });
            Mockito.doCallRealMethod().when(source).selectSQL(rootType, query, null);
            Mockito.doCallRealMethod().when(source).selectSQL(rootType, query, null, false);
            String sql = source.selectSQL(rootType, query, null);
            String normalized = sql.toUpperCase(Locale.ROOT);

            assertTrue(normalized.contains(" FROM CHILD_SCHEMA.CHILD_TABLE "));
            assertTrue(normalized.contains(" INNER JOIN PARENT_SCHEMA.PARENT_TABLE "));
        } finally {
            AppSchemaDataAccessRegistry.clearAppSchemaProperties();
        }
    }
}
