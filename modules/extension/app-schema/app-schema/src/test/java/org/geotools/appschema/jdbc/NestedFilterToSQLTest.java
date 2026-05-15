/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertTrue;

import java.awt.RenderingHints;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainLink;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainedAttributeDescriptor;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.joining.JoiningNestedAttributeMapping;
import org.geotools.data.oracle.OracleDialect;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCState;
import org.geotools.jdbc.NonIncrementingPrimaryKeyColumn;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Test;
import org.xml.sax.helpers.NamespaceSupport;

public class NestedFilterToSQLTest extends AppSchemaTestSupport {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testEncodeChainedAttributeUsesSchemaQualifiedTables() throws Exception {
        org.geotools.data.complex.AppSchemaDataAccessRegistry.getAppSchemaProperties()
                .setProperty(AppSchemaDataAccessConfigurator.PROPERTY_CROSS_SCHEMA_JOINING, "true");
        try {
            JDBCDataStore store = new JDBCDataStore();
            store.setSQLDialect(new OracleDialect(store));

            SimpleFeatureType parentType = simpleType("parent_table", "parent_fk");
            SimpleFeatureType nestedType = simpleType("nested_table", "id");

            registerPrimaryKey(store, parentType, "parent_fk");
            registerPrimaryKey(store, nestedType, "id");

            FeatureTypeMapping parentMapping = mapping(store, parentType, "parent_schema");
            FeatureTypeMapping nestedMapping = mapping(store, nestedType, "nested_schema");

            AttributeMapping nestedIdMapping = new AttributeMapping(Expression.NIL, FF.property("id"), null);
            JoiningNestedAttributeMapping nestedAttribute =
                    new TestJoiningNestedAttributeMapping(FF.property("parent_fk"), nestedMapping, nestedIdMapping);

            FeatureChainedAttributeDescriptor descriptor = newDescriptor(parentMapping, nestedMapping, nestedAttribute);

            NestedFilterToSQL nested = new NestedFilterToSQL(parentMapping, new FilterToSQL());
            StringWriter out = new StringWriter();
            nested.setWriter(out);

            Method encodeChained = NestedFilterToSQL.class.getDeclaredMethod(
                    "encodeChainedAttribute", Filter.class, String.class, FeatureChainedAttributeDescriptor.class);
            encodeChained.setAccessible(true);
            encodeChained.invoke(nested, Filter.INCLUDE, "unused", descriptor);

            String sql = out.toString().toUpperCase(Locale.ROOT);
            assertTrue(sql.contains("EXISTS ("));
            assertTrue(sql.contains("FROM NESTED_SCHEMA.NESTED_TABLE"));
            // The correlated predicate points to the outer query table identifier.
            assertTrue(sql.contains("WHERE PARENT_TABLE"));
        } finally {
            org.geotools.data.complex.AppSchemaDataAccessRegistry.clearAppSchemaProperties();
        }
    }

    private static void registerPrimaryKey(JDBCDataStore store, SimpleFeatureType schema, String keyName)
            throws Exception {
        ContentEntry entry = new ContentEntry(store, schema.getName());
        JDBCState state = (JDBCState) entry.getState(Transaction.AUTO_COMMIT);
        state.setPrimaryKey(new PrimaryKey(
                schema.getTypeName(),
                Collections.singletonList(new NonIncrementingPrimaryKeyColumn(keyName, Integer.class))));
        getEntries(store).put(schema.getName(), entry);
    }

    @SuppressWarnings("unchecked")
    private static Map<Name, ContentEntry> getEntries(JDBCDataStore store) throws Exception {
        Field entries = ContentDataStore.class.getDeclaredField("entries");
        entries.setAccessible(true);
        return (Map<Name, ContentEntry>) entries.get(store);
    }

    private static SimpleFeatureType simpleType(String typeName, String column) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(typeName);
        builder.add(column, Integer.class);
        return builder.buildFeatureType();
    }

    private static FeatureTypeMapping mapping(JDBCDataStore store, SimpleFeatureType schema, String sourceSchema) {
        FeatureSource<FeatureType, Feature> source = new StaticFeatureSource(store, schema);

        FeatureTypeMapping mapping =
                new FeatureTypeMapping(source, null, Collections.emptyList(), new NamespaceSupport());
        mapping.setSourceDatabaseSchema(sourceSchema);
        return mapping;
    }

    private static FeatureChainedAttributeDescriptor newDescriptor(
            FeatureTypeMapping parentMapping,
            FeatureTypeMapping nestedMapping,
            JoiningNestedAttributeMapping nestedAttribute)
            throws Exception {
        Constructor<FeatureChainedAttributeDescriptor> descriptorCtor =
                FeatureChainedAttributeDescriptor.class.getDeclaredConstructor();
        descriptorCtor.setAccessible(true);
        FeatureChainedAttributeDescriptor descriptor = descriptorCtor.newInstance();

        Constructor<FeatureChainLink> linkWithNestedCtor =
                FeatureChainLink.class.getDeclaredConstructor(FeatureTypeMapping.class, NestedAttributeMapping.class);
        linkWithNestedCtor.setAccessible(true);
        FeatureChainLink root = linkWithNestedCtor.newInstance(parentMapping, nestedAttribute);

        Constructor<FeatureChainLink> plainLinkCtor =
                FeatureChainLink.class.getDeclaredConstructor(FeatureTypeMapping.class);
        plainLinkCtor.setAccessible(true);
        FeatureChainLink leaf = plainLinkCtor.newInstance(nestedMapping);

        descriptor.addLink(root);
        descriptor.addLink(leaf);
        return descriptor;
    }

    private static class TestJoiningNestedAttributeMapping extends JoiningNestedAttributeMapping {

        private final FeatureTypeMapping nestedMapping;
        private final AttributeMapping nestedIdMapping;

        TestJoiningNestedAttributeMapping(
                Expression sourceExpression, FeatureTypeMapping nestedMapping, AttributeMapping nestedIdMapping)
                throws IOException {
            super(
                    Expression.NIL,
                    sourceExpression,
                    null,
                    false,
                    Collections.emptyMap(),
                    FF.literal("nested"),
                    null,
                    new NamespaceSupport());
            this.nestedMapping = nestedMapping;
            this.nestedIdMapping = nestedIdMapping;
        }

        @Override
        public FeatureTypeMapping getFeatureTypeMapping(Feature feature) {
            return nestedMapping;
        }

        @Override
        public AttributeMapping getMapping(FeatureTypeMapping featureTypeMapping) {
            return nestedIdMapping;
        }
    }

    private static class StaticFeatureSource implements FeatureSource<FeatureType, Feature> {

        private final JDBCDataStore store;
        private final SimpleFeatureType schema;

        StaticFeatureSource(JDBCDataStore store, SimpleFeatureType schema) {
            this.store = store;
            this.schema = schema;
        }

        @Override
        public Name getName() {
            return schema.getName();
        }

        @Override
        public ResourceInfo getInfo() {
            throw unsupported();
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataAccess<FeatureType, Feature> getDataStore() {
            return (DataAccess<FeatureType, Feature>) (DataAccess<?, ?>) store;
        }

        @Override
        public QueryCapabilities getQueryCapabilities() {
            throw unsupported();
        }

        @Override
        public void addFeatureListener(FeatureListener listener) {
            throw unsupported();
        }

        @Override
        public void removeFeatureListener(FeatureListener listener) {
            throw unsupported();
        }

        @Override
        public FeatureCollection<FeatureType, Feature> getFeatures(org.geotools.api.filter.Filter filter) {
            throw unsupported();
        }

        @Override
        public FeatureCollection<FeatureType, Feature> getFeatures(Query query) {
            throw unsupported();
        }

        @Override
        public FeatureCollection<FeatureType, Feature> getFeatures() {
            throw unsupported();
        }

        @Override
        public FeatureType getSchema() {
            return schema;
        }

        @Override
        public ReferencedEnvelope getBounds() {
            throw unsupported();
        }

        @Override
        public ReferencedEnvelope getBounds(Query query) {
            throw unsupported();
        }

        @Override
        public int getCount(Query query) {
            throw unsupported();
        }

        @Override
        public Set<RenderingHints.Key> getSupportedHints() {
            return Collections.emptySet();
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Not required by this test");
        }
    }
}
