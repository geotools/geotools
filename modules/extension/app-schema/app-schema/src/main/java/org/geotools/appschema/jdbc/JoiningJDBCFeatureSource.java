/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.JdbcMultipleValue;
import org.geotools.data.complex.config.MultipleValue;
import org.geotools.data.complex.filter.MultipleValueExtractor;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.data.joining.JoiningQuery.QueryJoin;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureReader;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JoinInfo.JoinQualifier;
import org.geotools.jdbc.JoinPropertyName;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.jdbc.PrimaryKeyFIDValidator;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * This is where the magic happens. The Joining JDBC Feature Source is a "hacking" class rather than a proper subclass.
 * It provides functionality for executing 'joining queries'. Because only simple features can be returned by the
 * existing geotools database logic, these joins on the db are not used for linking the actual features together, but
 * only for putting the simple features in a certain order; an order that will allow us to do the actual feature
 * chaining faster when we are building the complex features, because the right features will already be lined up in the
 * right order.
 *
 * @author Niels Charlier (Curtin University of Technology)
 */
public class JoiningJDBCFeatureSource extends JDBCFeatureSource {

    private static final Logger LOGGER = Logging.getLogger(JoiningJDBCFeatureSource.class);

    private static final String TEMP_FILTER_ALIAS = "temp_alias_used_for_filter";
    public static final String FOREIGN_ID = "FOREIGN_ID";
    // attribute to indicate primary key column, so it can be retrieved from the feature type
    public static final String PRIMARY_KEY = "PARENT_TABLE_PKEY";

    private static final String COUNT_TABLE_ALIAS = "COUNT_TABLE";
    private static final String DISTINCT_TABLE_ALIAS = "DISTINCT_TABLE";

    public JoiningJDBCFeatureSource(JDBCFeatureSource featureSource) throws IOException {
        super(featureSource);
    }

    public JoiningJDBCFeatureSource(JDBCFeatureStore featureStore) throws IOException {
        super(featureStore.delegate);
    }

    /** Field Encoder for converting Filters/Expressions to SQL, will encode table name with field. */
    public static class JoiningFieldEncoder implements FilterToSQL.FieldEncoder {

        private String tableName;
        private JDBCDataStore store;

        public JoiningFieldEncoder(String tableName, JDBCDataStore store) {
            this.tableName = tableName;
            this.store = store;
        }

        /**
         * Encodes a field name in SQL, prefixing it with the table name (but not the schema).
         *
         * <p>Mostly useful when aliases are used.
         */
        @Override
        public String encode(String s) {
            StringBuffer buf = new StringBuffer();
            store.dialect.encodeTableName(tableName, buf);
            buf.append(".");
            buf.append(s);
            return buf.toString();
        }
    }

    /**
     * Encoding a geometry column with respect to hints Supported Hints are provided by
     * {@link SQLDialect#addSupportedHints(Set)}
     *
     * @param hints , may be null
     */
    protected void encodeGeometryColumn(GeometryDescriptor gatt, String typeName, StringBuffer sql, Hints hints)
            throws SQLException {

        StringBuffer temp = new StringBuffer();
        getDataStore().encodeGeometryColumn(gatt, temp, hints);

        StringBuffer originalColumnName = new StringBuffer();
        getDataStore().dialect.encodeColumnName(null, gatt.getLocalName(), originalColumnName);

        StringBuffer replaceColumnName = new StringBuffer();
        encodeColumnName(gatt.getLocalName(), typeName, replaceColumnName, hints);

        sql.append(temp.toString().replaceAll(originalColumnName.toString(), replaceColumnName.toString()));
    }

    /** Create order by field for specific table name */
    protected void sort(
            JoiningQuery query,
            String typeName,
            String schemaName,
            String alias,
            SortBy[] sort,
            Set<String> orderByFields,
            StringBuffer sql)
            throws IOException, SQLException {
        for (SortBy sortBy : sort) {
            if (SortBy.NATURAL_ORDER.equals(sortBy) || SortBy.REVERSE_ORDER.equals(sortBy)) {
                throw new IOException("Cannot do natural order in joining queries");
            } else {
                StringBuffer mySql = new StringBuffer();
                if (alias != null) {
                    encodeColumnName2(sortBy.getPropertyName().getPropertyName(), alias, mySql, null);
                } else {
                    encodeColumnName(sortBy.getPropertyName().getPropertyName(), typeName, schemaName, mySql, null);
                }
                if (!mySql.toString().isEmpty() && orderByFields.add(mySql.toString())) {
                    // if it's not already in ORDER BY (because you can't have duplicate column
                    // names in order by)
                    // add it to the query buffer
                    if (orderByFields.size() > 1) {
                        sql.append(", ");
                    }
                    sql.append(mySql);

                    if (sortBy.getSortOrder() == SortOrder.DESCENDING) {
                        sql.append(" DESC");
                    } else {
                        sql.append(" ASC");
                    }
                }
            }
        }
        // GEOT-4554: sort by PK if idExpression is not there
        if (sort.length == 0) {
            PrimaryKey joinKey = null;
            SimpleFeatureType joinFeatureType = resolveFeatureType(query, typeName, schemaName);
            try {
                JDBCDataStore store = resolveDataStore(query, typeName, schemaName);
                joinKey = store.getPrimaryKey(joinFeatureType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (PrimaryKeyColumn col : joinKey.getColumns()) {
                StringBuffer mySql = new StringBuffer();
                if (alias != null) {
                    encodeColumnName2(col.getName(), alias, mySql, null);
                } else {
                    encodeColumnName(col.getName(), typeName, schemaName, mySql, null);
                }
                if (!mySql.toString().isEmpty() && orderByFields.add(mySql.toString())) {
                    // if it's not already in ORDER BY (because you can't have duplicate column
                    // names in order by)
                    // add it to the query buffer
                    // this is what's used in AppSchemaDataAccess
                    if (orderByFields.size() > 1) {
                        sql.append(", ");
                    }
                    sql.append(mySql);
                    // this is what's used in AppSchemaDataAccess
                    sql.append(" ASC");
                }
            }
        }
    }

    protected void addMultiValuedSort(
            JoiningQuery query,
            String tableName,
            Set<String> orderByFields,
            JoiningQuery.QueryJoin join,
            StringBuffer sql)
            throws IOException, FilterToSQLException, SQLException {

        StringBuffer field2 = new StringBuffer();
        String joinSchema = resolveSchemaForJoin(query, join);
        JDBCDataStore joinStore = resolveDataStore(query, join.getJoiningTypeName(), joinSchema);
        joinSchema = effectiveSchema(joinStore, joinSchema);
        encodeColumnName(join.getForeignKeyName().toString(), join.getJoiningTypeName(), joinSchema, field2, null);

        StringBuffer field1 = new StringBuffer();
        String tableSchema = resolveSchemaForTypeName(query, tableName);
        JDBCDataStore tableStore = resolveDataStore(query, tableName, tableSchema);
        tableSchema = effectiveSchema(tableStore, tableSchema);
        encodeColumnName(join.getJoiningKeyName().toString(), tableName, tableSchema, field1, null);

        if (orderByFields.add(field1.toString()) && orderByFields.add(field2.toString())) {
            // check that they don't already exists in ORDER BY because duplicate column names
            // aren't allowed
            if (sql.length() > 0) {
                sql.append(", ");
            }
            sql.append(" CASE WHEN ");
            sql.append(field2);
            sql.append(" = ");
            sql.append(field1);
            sql.append(" THEN 0 ELSE 1 END ASC");
        }
    }

    /** Creates ORDER BY for joining query, based on all the sortby's that are specified per joining table */
    protected void sort(JoiningQuery query, StringBuffer sql, String[] aliases, Set<String> pkColumnNames)
            throws IOException, SQLException, FilterToSQLException {
        Set<String> orderByFields = new LinkedHashSet<>();
        StringBuffer joinOrders = new StringBuffer();
        for (int j = query.getQueryJoins() == null ? -1 : query.getQueryJoins().size() - 1; j >= -1; j--) {
            JoiningQuery.QueryJoin join = j < 0 ? null : query.getQueryJoins().get(j);
            SortBy[] sort = j < 0 ? query.getSortBy() : join.getSortBy();

            if (sort != null) {
                if (j < 0) {
                    String rootSchema = resolveSchemaForTypeName(query, query.getTypeName());
                    JDBCDataStore rootStore = resolveDataStore(query, query.getTypeName(), rootSchema);
                    rootSchema = effectiveSchema(rootStore, rootSchema);
                    sort(query, query.getTypeName(), rootSchema, null, sort, orderByFields, joinOrders);

                    if (query.getQueryJoins() != null && !query.getQueryJoins().isEmpty()) {
                        addMultiValuedSort(
                                query,
                                query.getTypeName(),
                                orderByFields,
                                query.getQueryJoins().get(0),
                                joinOrders);
                    }

                    if (joinOrders.length() > 0) {
                        sql.append(" ORDER BY ");
                        sql.append(joinOrders);
                    }

                    if (!pkColumnNames.isEmpty()) {
                        for (String pk : pkColumnNames) {

                            StringBuffer pkSql = new StringBuffer();
                            encodeColumnName(pk, query.getTypeName(), rootSchema, pkSql, null);

                            if (!pkSql.toString().isEmpty() && orderByFields.add(pkSql.toString())) {
                                sql.append(", ");
                                sql.append(pkSql);
                            }
                        }
                    }
                } else {
                    String joinSchema = resolveSchemaForJoin(query, join);
                    JDBCDataStore joinStore = resolveDataStore(query, join.getJoiningTypeName(), joinSchema);
                    joinSchema = effectiveSchema(joinStore, joinSchema);
                    if (aliases != null && aliases[j] != null) {
                        sort(query, join.getJoiningTypeName(), joinSchema, aliases[j], sort, orderByFields, joinOrders);
                    } else {
                        sort(query, join.getJoiningTypeName(), joinSchema, null, sort, orderByFields, joinOrders);
                    }
                    if (query.getQueryJoins().size() > j + 1) {
                        addMultiValuedSort(
                                query,
                                join.getJoiningTypeName(),
                                orderByFields,
                                query.getQueryJoins().get(j + 1),
                                joinOrders);
                    }
                }
            }
        }
    }

    /** Encode column name with table name included. */
    public void encodeColumnName(String colName, String typeName, StringBuffer sql, Hints hints) throws SQLException {

        encodeColumnName(colName, typeName, null, sql, hints);
    }

    /** Encode column name with table name included and optional schema override. */
    public void encodeColumnName(String colName, String typeName, String schemaName, StringBuffer sql, Hints hints)
            throws SQLException {
        encodeTableName(typeName, schemaName, sql, hints);
        sql.append(".");
        getDataStore().dialect.encodeColumnName(null, colName, sql);
    }

    /** Encode column name with table name included, but do not include schema name (for aliases) */
    public void encodeColumnName2(String colName, String typeName, StringBuffer sql, Hints hints) throws SQLException {

        getDataStore().dialect.encodeTableName(typeName, sql);
        sql.append(".");
        getDataStore().dialect.encodeColumnName(null, colName, sql);
    }

    private void encodeTableName(String typeName, String schemaName, StringBuffer sql, Hints hints)
            throws SQLException {
        if (schemaName == null) {
            getDataStore().encodeTableName(typeName, sql, hints);
            return;
        }
        SQLDialect dialect = getDataStore().getSQLDialect();
        dialect.encodeSchemaName(schemaName, sql);
        sql.append(".");
        dialect.encodeTableName(typeName, sql);
    }

    private void encodeAliasedTableName(String typeName, String schemaName, StringBuffer sql, Hints hints, String alias)
            throws SQLException {
        if (schemaName == null) {
            getDataStore().encodeAliasedTableName(typeName, sql, hints, alias);
            return;
        }
        SQLDialect dialect = getDataStore().getSQLDialect();
        dialect.encodeSchemaName(schemaName, sql);
        sql.append(".");
        dialect.encodeTableName(typeName, sql);
        if (alias != null) {
            dialect.encodeTableAlias(alias, sql);
        }
    }

    private String resolveSchemaForTypeName(JoiningQuery query, String typeName) {
        if (!AppSchemaDataAccessConfigurator.isCrossSchemaJoiningEnabled() || query == null || typeName == null) {
            return null;
        }
        FeatureTypeMapping rootMapping = query.getRootMapping();
        if (matchesTypeName(rootMapping, typeName)) {
            return rootMapping.getSourceDatabaseSchema();
        }
        for (QueryJoin join : query.getQueryJoins()) {
            if (!typeName.equals(join.getJoiningTypeName())) {
                continue;
            }
            FeatureTypeMapping joinRoot = join.getRootMapping();
            if (matchesTypeName(joinRoot, typeName)) {
                if (joinRoot.getSourceDatabaseSchema() != null) {
                    return joinRoot.getSourceDatabaseSchema();
                }
            }
            if (join.getJoiningTypeSchema() != null) {
                return join.getJoiningTypeSchema();
            }
        }
        return null;
    }

    private String resolveSchemaForJoin(JoiningQuery query, QueryJoin join) {
        if (!AppSchemaDataAccessConfigurator.isCrossSchemaJoiningEnabled() || join == null) {
            return null;
        }
        FeatureTypeMapping mapping = join.getRootMapping();
        if (mapping != null
                && mapping.getSourceDatabaseSchema() != null
                && matchesTypeName(mapping, join.getJoiningTypeName())) {
            return mapping.getSourceDatabaseSchema();
        }
        if (join.getJoiningTypeSchema() != null) {
            return join.getJoiningTypeSchema();
        }
        return resolveSchemaForTypeName(query, join.getJoiningTypeName());
    }

    private FeatureTypeMapping resolveMappingForTypeName(JoiningQuery query, String typeName, String schema) {
        if (query == null || typeName == null) {
            return null;
        }
        FeatureTypeMapping rootMapping = query.getRootMapping();
        if (matchesTypeName(rootMapping, typeName) && schemaMatches(rootMapping, schema)) {
            return rootMapping;
        }
        for (QueryJoin join : query.getQueryJoins()) {
            FeatureTypeMapping mapping = join.getRootMapping();
            if (matchesTypeName(mapping, typeName) && schemaMatches(mapping, schema)) {
                return mapping;
            }
        }
        return null;
    }

    private FeatureTypeMapping resolveFilterRootMapping(JoiningQuery query, String typeName, String schema) {
        // For nested/chained queries, prefer the mapping attached to the query join.
        // The query root mapping may point to the nested target (same source table, different
        // target type), which cannot encode root-level nested filters correctly.
        FeatureTypeMapping mapping = resolveJoinMappingForTypeName(query, typeName, schema);
        if (mapping == null && schema != null) {
            mapping = resolveJoinMappingForTypeName(query, typeName, null);
        }
        if (mapping != null) {
            return mapping;
        }

        mapping = resolveMappingForTypeName(query, typeName, schema);
        if (mapping == null && schema != null) {
            mapping = resolveMappingForTypeName(query, typeName, null);
        }
        if (mapping != null) {
            return mapping;
        }
        return query != null ? query.getRootMapping() : null;
    }

    private FeatureTypeMapping resolveJoinMappingForTypeName(JoiningQuery query, String typeName, String schema) {
        if (query == null || query.getQueryJoins() == null) {
            return null;
        }
        for (QueryJoin join : query.getQueryJoins()) {
            FeatureTypeMapping mapping = join.getRootMapping();
            if (matchesTypeName(mapping, typeName) && schemaMatches(mapping, schema)) {
                return mapping;
            }
        }
        return null;
    }

    private boolean schemaMatches(FeatureTypeMapping mapping, String schema) {
        if (schema == null) {
            return true;
        }
        if (mapping == null) {
            return false;
        }
        String mappingSchema = mapping.getSourceDatabaseSchema();
        if (mappingSchema != null) {
            return schema.equalsIgnoreCase(mappingSchema);
        }
        JDBCDataStore mappingStore = getMappingDataStore(mapping);
        String defaultSchema = mappingStore != null ? mappingStore.getDatabaseSchema() : null;
        return defaultSchema != null && schema.equalsIgnoreCase(defaultSchema);
    }

    private JDBCDataStore getMappingDataStore(FeatureTypeMapping mapping) {
        if (mapping == null || mapping.getSource() == null) {
            return null;
        }
        if (mapping.getSource() instanceof JDBCFeatureSource) {
            return ((JDBCFeatureSource) mapping.getSource()).getDataStore();
        }
        if (mapping.getSource() instanceof JDBCFeatureStore) {
            return ((JDBCFeatureStore) mapping.getSource()).getDataStore();
        }
        return null;
    }

    private boolean matchesTypeName(FeatureTypeMapping mapping, String typeName) {
        if (mapping == null
                || mapping.getSource() == null
                || mapping.getSource().getSchema() == null) {
            return false;
        }
        return typeName.equals(mapping.getSource().getSchema().getName().getLocalPart());
    }

    private SimpleFeatureType resolveFeatureType(JoiningQuery query, String typeName, String schema)
            throws IOException {
        FeatureTypeMapping mapping = resolveMappingForTypeName(query, typeName, schema);
        if (mapping == null && schema != null) {
            mapping = resolveMappingForTypeName(query, typeName, null);
        }
        if (mapping != null
                && mapping.getSource() != null
                && mapping.getSource().getSchema() instanceof SimpleFeatureType) {
            return (SimpleFeatureType) mapping.getSource().getSchema();
        }
        return resolveDataStore(query, typeName, schema).getSchema(typeName);
    }

    private JDBCDataStore resolveDataStore(JoiningQuery query, String typeName, String schema) {
        FeatureTypeMapping mapping = resolveMappingForTypeName(query, typeName, schema);
        if (mapping == null && schema != null) {
            mapping = resolveMappingForTypeName(query, typeName, null);
        }
        if (mapping != null && mapping.getSource() instanceof JDBCFeatureSource) {
            return ((JDBCFeatureSource) mapping.getSource()).getDataStore();
        }
        if (mapping != null && mapping.getSource() instanceof JDBCFeatureStore) {
            return ((JDBCFeatureStore) mapping.getSource()).getDataStore();
        }
        return getDataStore();
    }

    private String normalizeSchema(JDBCDataStore store, String schema) {
        if (schema == null || store == null) {
            return schema;
        }
        String defaultSchema = store.getDatabaseSchema();
        if (defaultSchema != null && defaultSchema.equalsIgnoreCase(schema)) {
            return null;
        }
        return schema;
    }

    /**
     * Computes schema to be used in SQL serialization for a table owned by {@code owningStore}.
     *
     * <p>When joining tables coming from a different source store than this feature source, a null schema would cause
     * table encoding to fallback to this datastore default schema. In that case we force the owning store default
     * schema to keep table qualification correct.
     */
    private String effectiveSchema(JDBCDataStore owningStore, String schema) {
        String normalized = normalizeSchema(owningStore, schema);
        if (normalized != null || owningStore == null || owningStore == getDataStore()) {
            return normalized;
        }
        String owningDefault = owningStore.getDatabaseSchema();
        String currentDefault = getDataStore().getDatabaseSchema();
        if (owningDefault != null && (currentDefault == null || !owningDefault.equalsIgnoreCase(currentDefault))) {
            return owningDefault;
        }
        return null;
    }

    /** Create the filter to SQL converter. */
    protected FilterToSQL createFilterToSQL(SimpleFeatureType ft) {
        return createFilterToSQL(ft, true);
    }

    /** Create the filter to SQL converter. */
    protected FilterToSQL createFilterToSQL(SimpleFeatureType ft, boolean usePreparedStatementParameters) {
        return createFilterToSQL(getDataStore(), ft, usePreparedStatementParameters);
    }

    protected FilterToSQL createFilterToSQL(
            JDBCDataStore store, SimpleFeatureType ft, boolean usePreparedStatementParameters) {
        if (store.getSQLDialect() instanceof PreparedStatementSQLDialect) {
            PreparedFilterToSQL pfsql = store.createPreparedFilterToSQL(ft);
            pfsql.setPrepareEnabled(usePreparedStatementParameters);
            return pfsql;
        } else {
            return store.createFilterToSQL(ft);
        }
    }

    protected FilterToSQL createFilterToSQL(
            SimpleFeatureType ft, boolean usePreparedStatementParameters, String schemaName) {
        return createFilterToSQL(getDataStore(), ft, usePreparedStatementParameters, schemaName);
    }

    protected FilterToSQL createFilterToSQL(
            JDBCDataStore store, SimpleFeatureType ft, boolean usePreparedStatementParameters, String schemaName) {
        FilterToSQL toSQL = createFilterToSQL(store, ft, usePreparedStatementParameters);
        if (schemaName != null) {
            toSQL.setDatabaseSchema(schemaName);
        }
        return toSQL;
    }

    protected static String createAlias(String typeName, Set<String> tableNames) {
        String alias = typeName;
        if (typeName.length() > 20) {
            typeName = typeName.substring(0, 20);
        }
        int index = 0;
        // if the typeName doesn't already exist, it won't create alias
        // if alias already exists, create a new one with new index
        while (tableNames.contains(alias)) {
            alias = typeName + "_" + ++index;
        }
        return alias;
    }

    /**
     * Generates a 'SELECT p1, p2, ... FROM ... WHERE ...' prepared statement.
     *
     * @param featureType the feature type that the query must return (may contain less attributes than the native one)
     * @param query the query to be run. The type name and property will be ignored, as they are supposed to have been
     *     already embedded into the provided feature type
     * @param toSQLref atomic reference to a FilterToSQL able to works with PreparedStament parameters
     */
    public String selectSQL(
            SimpleFeatureType featureType, JoiningQuery query, AtomicReference<PreparedFilterToSQL> toSQLref)
            throws SQLException, IOException, FilterToSQLException {
        return selectSQL(featureType, query, toSQLref, false);
    }

    /**
     * Generates a 'SELECT p1, p2, ... FROM ... WHERE ...' prepared statement.
     *
     * @param featureType the feature type that the query must return (may contain less attributes than the native one)
     * @param query the query to be run. The type name and property will be ignored, as they are supposed to have been
     *     already embedded into the provided feature type
     * @param toSQLref atomic reference to a FilterToSQL able to works with PreparedStament parameters
     * @param isCount avoid the encoding of unnecessary SQL pieces when a count query is needed
     */
    public String selectSQL(
            SimpleFeatureType featureType,
            JoiningQuery query,
            AtomicReference<PreparedFilterToSQL> toSQLref,
            boolean isCount)
            throws IOException, SQLException, FilterToSQLException {
        sanitizeJoinIds(query);

        // first we create from clause, for aliases

        StringBuffer joinClause = new StringBuffer();

        // joining
        Set<String> tableNames = new HashSet<>();

        String lastTypeName = featureType.getTypeName();
        String lastTypeSchema = resolveSchemaForTypeName(query, lastTypeName);
        String curTypeName = lastTypeName;

        String[] aliases = null;

        tableNames.add(lastTypeName);

        String alias = null;

        if (query.getQueryJoins() != null) {

            aliases = new String[query.getQueryJoins().size()];

            for (int i = 0; i < query.getQueryJoins().size(); i++) {
                JoiningQuery.QueryJoin join = query.getQueryJoins().get(i);

                joinClause.append(" INNER JOIN ");

                JDBCDataStore lastStore = resolveDataStore(query, lastTypeName, lastTypeSchema);
                String normalizedLastTypeSchema = effectiveSchema(lastStore, lastTypeSchema);
                FilterToSQL toSQL1 = createFilterToSQL(
                        lastStore,
                        resolveFeatureType(query, lastTypeName, lastTypeSchema),
                        toSQLref != null,
                        normalizedLastTypeSchema);
                String joinSchema = resolveSchemaForJoin(query, join);
                JDBCDataStore joinStore = resolveDataStore(query, join.getJoiningTypeName(), joinSchema);
                String normalizedJoinSchema = effectiveSchema(joinStore, joinSchema);
                FilterToSQL toSQL2 = createFilterToSQL(
                        joinStore,
                        resolveFeatureType(query, join.getJoiningTypeName(), joinSchema),
                        toSQLref != null,
                        normalizedJoinSchema);

                if (tableNames.contains(join.getJoiningTypeName())) {
                    alias = createAlias(join.getJoiningTypeName(), tableNames);

                    aliases[i] = alias;

                    encodeTableName(join.getJoiningTypeName(), normalizedJoinSchema, joinClause, query.getHints());
                    joinClause.append(" ");
                    getDataStore().dialect.encodeTableName(alias, joinClause);
                    joinClause.append(" ON ( ");

                    toSQL2.setFieldEncoder(new JoiningFieldEncoder(alias, getDataStore()));
                    joinClause.append(toSQL2.encodeToString(join.getForeignKeyName()));

                } else {
                    aliases[i] = null;
                    encodeTableName(join.getJoiningTypeName(), normalizedJoinSchema, joinClause, query.getHints());
                    joinClause.append(" ON ( ");
                    toSQL2.setFieldEncoder(new JoiningFieldEncoder(join.getJoiningTypeName(), getDataStore()));
                    joinClause.append(toSQL2.encodeToString(join.getForeignKeyName()));
                }

                joinClause.append(" = ");
                String fromTypeName = curTypeName;
                toSQL1.setFieldEncoder(new JoiningFieldEncoder(fromTypeName, getDataStore()));
                joinClause.append(toSQL1.encodeToString(join.getJoiningKeyName()));
                joinClause.append(") ");
                lastTypeName = join.getJoiningTypeName();
                lastTypeSchema = normalizedJoinSchema;
                curTypeName = aliases[i] == null ? lastTypeName : aliases[i];

                tableNames.add(curTypeName);
            }
        }

        // begin sql
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        String featureTypeSchema = resolveSchemaForTypeName(query, featureType.getTypeName());
        JDBCDataStore featureStore = resolveDataStore(query, featureType.getTypeName(), featureTypeSchema);
        featureTypeSchema = effectiveSchema(featureStore, featureTypeSchema);

        // primary key
        Set<String> pkColumnNames = getAllPrimaryKeys(featureStore, featureType);
        for (String colName : pkColumnNames) {
            encodeColumnName(colName, featureType.getTypeName(), featureTypeSchema, sql, query.getHints());
            sql.append(",");
        }
        Set<String> lastPkColumnNames = pkColumnNames;

        // other columns
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            // skip the eventually exposed pk column values
            String columnName = att.getLocalName();
            if (pkColumnNames.contains(columnName)) continue;

            if (att instanceof GeometryDescriptor descriptor) {
                // encode as geometry
                encodeGeometryColumn(descriptor, featureType.getTypeName(), sql, query.getHints());

                // alias it to be the name of the original geometry
                getDataStore().dialect.encodeColumnAlias(columnName, sql);
            } else {
                encodeColumnName(columnName, featureType.getTypeName(), featureTypeSchema, sql, query.getHints());
            }

            sql.append(",");
        }

        if (query.getQueryJoins() != null && !query.getQueryJoins().isEmpty()) {
            for (int i = 0; i < query.getQueryJoins().size(); i++) {
                List<String> ids = query.getQueryJoins().get(i).getIds();
                for (int j = 0; j < ids.size(); j++) {
                    if (aliases[i] != null) {
                        getDataStore()
                                .dialect
                                .encodeColumnName(
                                        aliases[i],
                                        query.getQueryJoins().get(i).getIds().get(j),
                                        sql);
                    } else {
                        QueryJoin queryJoin = query.getQueryJoins().get(i);
                        String joinSchema = resolveSchemaForJoin(query, queryJoin);
                        JDBCDataStore joinStore = resolveDataStore(query, queryJoin.getJoiningTypeName(), joinSchema);
                        String normalizedJoinSchema = effectiveSchema(joinStore, joinSchema);
                        encodeColumnName(
                                queryJoin.getIds().get(j),
                                queryJoin.getJoiningTypeName(),
                                normalizedJoinSchema,
                                sql,
                                query.getHints());
                    }
                    sql.append(" ").append(FOREIGN_ID + "_" + i + "_" + j).append(",");
                }
                // GEOT-4554: handle PK as default idExpression
                if (ids.isEmpty()) {
                    PrimaryKey joinKey = null;
                    QueryJoin queryJoin = query.getQueryJoins().get(i);
                    String joinTypeName = queryJoin.getJoiningTypeName();
                    String joinSchema = resolveSchemaForJoin(query, queryJoin);
                    SimpleFeatureType joinFeatureType = resolveFeatureType(query, joinTypeName, joinSchema);
                    JDBCDataStore store = resolveDataStore(query, joinTypeName, joinSchema);
                    String normalizedJoinSchema = effectiveSchema(store, joinSchema);

                    try {
                        joinKey = store.getPrimaryKey(joinFeatureType);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (!joinKey.getColumns().isEmpty()) {
                        lastPkColumnNames.clear();
                    }
                    int j = 0;
                    for (PrimaryKeyColumn col : joinKey.getColumns()) {
                        if (aliases[i] != null) {
                            getDataStore().dialect.encodeColumnName(aliases[i], col.getName(), sql);
                        } else {
                            encodeColumnName(col.getName(), joinTypeName, normalizedJoinSchema, sql, query.getHints());
                        }
                        query.getQueryJoins().get(i).addId(col.getName());
                        sql.append(" ").append(FOREIGN_ID + "_" + i + "_" + j).append(",");
                        j++;
                        lastPkColumnNames.add(col.getName());
                    }
                }
            }
        }
        if (!query.hasIdColumn() && !pkColumnNames.isEmpty()) {
            int pkIndex = 0;
            for (String pk : pkColumnNames) {
                encodeColumnName(pk, featureType.getTypeName(), featureTypeSchema, sql, query.getHints());
                sql.append(" ").append(PRIMARY_KEY).append("_").append(pkIndex).append(",");
                pkIndex++;
            }
        }

        sql.setLength(sql.length() - 1);

        sql.append(" FROM ");

        encodeTableName(featureType.getTypeName(), featureTypeSchema, sql, query.getHints());

        // filtering
        FilterToSQL toSQL = null;
        Filter filter = query.getFilter();

        sql.append(joinClause);

        boolean isRootFeature =
                query.getQueryJoins() == null || query.getQueryJoins().isEmpty();

        boolean pagingApplied = false;

        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // encode filter
            try {
                SortBy[] lastSortBy = null;
                // leave it as null if it's asking for a subset, since we don't want to join to get
                // other rows of same id
                // since we don't want a full feature, but a subset only
                if (!query.isSubset()) {
                    // grab the full feature type, as we might be encoding a filter
                    // that uses attributes that aren't returned in the results
                    lastSortBy = isRootFeature
                            ? query.getSortBy()
                            : query.getQueryJoins() == null
                                            || query.getQueryJoins().isEmpty()
                                    ? query.getSortBy()
                                    : query.getQueryJoins()
                                            .get(query.getQueryJoins().size() - 1)
                                            .getSortBy();
                }
                String lastTableName = isRootFeature
                        ? query.getTypeName()
                        : query.getQueryJoins()
                                .get(query.getQueryJoins().size() - 1)
                                .getJoiningTypeName();
                String lastTableAlias = isRootFeature
                        ? query.getTypeName()
                        : aliases[query.getQueryJoins().size() - 1] == null
                                ? lastTableName
                                : aliases[query.getQueryJoins().size() - 1];

                String lastTableSchema = isRootFeature
                        ? resolveSchemaForTypeName(query, lastTableName)
                        : resolveSchemaForJoin(
                                query,
                                query.getQueryJoins().get(query.getQueryJoins().size() - 1));
                FeatureTypeMapping lastTableMapping = resolveFilterRootMapping(query, lastTableName, lastTableSchema);
                JDBCDataStore lastStore = resolveDataStore(query, lastTableName, lastTableSchema);
                lastTableSchema = effectiveSchema(lastStore, lastTableSchema);
                if (lastTableMapping == null) {
                    lastTableMapping = resolveFilterRootMapping(query, lastTableName, lastTableSchema);
                }
                toSQL = createFilterToSQL(
                        lastStore,
                        resolveFeatureType(query, lastTableName, lastTableSchema),
                        toSQLref != null,
                        lastTableSchema);

                // apply paging to the root feature if applicable
                Collection<String> ids = new ArrayList<>();

                if (!isCount)
                    pagingApplied = applyPaging(
                            query,
                            isRootFeature,
                            sql,
                            featureType,
                            pkColumnNames,
                            tableNames,
                            toSQL,
                            filter,
                            lastTableMapping,
                            ids,
                            aliases);

                if (lastSortBy != null && (lastSortBy.length > 0 || !lastPkColumnNames.isEmpty())) {
                    buildFilter(
                            query,
                            sql,
                            lastPkColumnNames,
                            toSQL,
                            filter,
                            lastSortBy,
                            lastTableName,
                            lastTableSchema,
                            lastTableAlias,
                            lastTableMapping,
                            ids,
                            curTypeName);
                } else if (!pagingApplied) {
                    if (NestedFilterToSQL.isNestedFilter(filter)) {
                        toSQL.setFieldEncoder(new JoiningFieldEncoder(curTypeName, getDataStore()));
                        sql.append(" WHERE ").append(createNestedFilter(filter, lastTableMapping, toSQL));
                    } else {
                        sql.append(" ").append(toSQL.encodeToString(filter));
                    }
                }
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        } else if (!isCount) {
            pagingApplied = applyPaging(
                    query,
                    isRootFeature,
                    sql,
                    featureType,
                    pkColumnNames,
                    tableNames,
                    null,
                    null,
                    resolveFilterRootMapping(
                            query,
                            featureType.getTypeName(),
                            resolveSchemaForTypeName(query, featureType.getTypeName())),
                    null,
                    aliases);
        }

        // sorting
        if (!isCount) {
            sort(query, sql, aliases, pkColumnNames);

            // finally encode limit/offset, if not already done in the INNER JOIN
            if (!pagingApplied) {
                getDataStore().applyLimitOffset(sql, query);
            }
        }

        if (toSQLref != null && toSQL instanceof PreparedFilterToSQL qL) {
            toSQLref.set(qL);
        }

        return sql.toString();
    }

    private void sanitizeJoinIds(JoiningQuery query) throws IOException {
        if (query == null || query.getQueryJoins() == null) {
            return;
        }
        for (QueryJoin join : query.getQueryJoins()) {
            if (join.getIds().isEmpty()) {
                continue;
            }
            String joinTypeName = join.getJoiningTypeName();
            String joinSchema = resolveSchemaForJoin(query, join);
            SimpleFeatureType joinFeatureType = resolveFeatureType(query, joinTypeName, joinSchema);
            if (joinFeatureType == null) {
                continue;
            }
            List<String> validIds = new ArrayList<>();
            for (String id : join.getIds()) {
                if (hasAttribute(joinFeatureType, id)) {
                    validIds.add(id);
                }
            }
            if (validIds.size() != join.getIds().size()) {
                join.getIds().clear();
                join.getIds().addAll(validIds);
            }
        }
    }

    private boolean hasAttribute(SimpleFeatureType featureType, String attributeName) {
        if (featureType == null || attributeName == null) {
            return false;
        }
        if (featureType.getDescriptor(attributeName) != null) {
            return true;
        }
        for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
            if (attributeName.equalsIgnoreCase(descriptor.getLocalName())) {
                return true;
            }
        }
        return false;
    }

    private boolean applyPaging(
            JoiningQuery query,
            boolean isRootFeature,
            StringBuffer sql,
            SimpleFeatureType featureType,
            Set<String> pkColumnNames,
            Set<String> tableNames,
            FilterToSQL toSQL,
            Filter filter,
            FeatureTypeMapping filterRootMapping,
            Collection<String> ids,
            String[] aliases)
            throws IOException, SQLException, FilterToSQLException {
        boolean pagingApplied = false;

        if (isRootFeature && query.isDenormalised()) {
            // apply inner join for paging to root feature
            // if not denormalised, it will apply the maxFeatures and offset in the query
            // directly later
            pagingApplied = applyPaging(
                    query,
                    sql,
                    pkColumnNames,
                    featureType.getTypeName(),
                    featureType.getTypeName(),
                    tableNames,
                    toSQL,
                    filter,
                    filterRootMapping,
                    ids);
        } else if (!isRootFeature) {
            // also we always need to apply paging for the last queryJoin since it is the
            // join to
            // the root feature type (where the original paging parameters come from)

            int lastJoinIndex = query.getQueryJoins().size() - 1;
            QueryJoin lastJoin = query.getQueryJoins().get(lastJoinIndex);
            String lastTableName = query.getQueryJoins().get(lastJoinIndex).getJoiningTypeName();
            String lastTableSchema = resolveSchemaForJoin(query, lastJoin);
            String lastTableAlias = aliases[lastJoinIndex] == null ? lastTableName : aliases[lastJoinIndex];
            // For nested joins, fallback IDs must come from the joined parent table (last join),
            // not from the current feature type PK columns.
            Set<String> lastJoinPkColumnNames = Collections.emptySet();
            if (lastJoin.getIds().isEmpty()) {
                JDBCDataStore lastJoinStore = resolveDataStore(query, lastTableName, lastTableSchema);
                lastJoinPkColumnNames =
                        getAllPrimaryKeys(lastJoinStore, resolveFeatureType(query, lastTableName, lastTableSchema));
            }
            FeatureTypeMapping lastTableMapping = resolveFilterRootMapping(query, lastTableName, lastTableSchema);
            pagingApplied = applyPaging(
                    lastJoin,
                    sql,
                    lastJoinPkColumnNames,
                    lastTableName,
                    lastTableAlias,
                    tableNames,
                    toSQL,
                    filter,
                    lastTableMapping,
                    ids);
        }
        return pagingApplied;
    }

    private void buildFilter(
            JoiningQuery query,
            StringBuffer sql,
            Set<String> lastPkColumnNames,
            FilterToSQL toSQL,
            Filter filter,
            SortBy[] lastSortBy,
            String lastTableName,
            String lastTableSchema,
            String lastTableAlias,
            FeatureTypeMapping filterRootMapping,
            Collection<String> ids,
            String curTypeName)
            throws SQLException, FilterToSQLException, IOException {
        // we will use another join for the filter
        // assuming that the last sort by specifies the ID of the parent feature
        // this way we will ensure that if the table is denormalized, that all rows
        // with the same ID are included (for multi-valued features)

        StringBuffer sortBySQL = new StringBuffer();
        sortBySQL.append(" INNER JOIN ( SELECT DISTINCT ");
        boolean hasSortBy = false;
        boolean isMultiSort = lastSortBy.length > 1 && ids.isEmpty();
        hasSortBy = isMultiSort
                ? buildFiterBasedOnPk(
                        query,
                        toSQL,
                        filter,
                        filterRootMapping,
                        lastSortBy,
                        lastTableName,
                        lastTableSchema,
                        lastTableAlias,
                        lastPkColumnNames,
                        sortBySQL,
                        hasSortBy)
                : buildFiterBasedOnSortBy(
                        query,
                        toSQL,
                        filter,
                        filterRootMapping,
                        lastSortBy,
                        lastTableName,
                        lastTableSchema,
                        lastTableAlias,
                        ids,
                        sortBySQL,
                        hasSortBy);

        if (lastSortBy.length == 0) {
            // GEOT-4554: if ID expression is not specified, use PK
            JDBCDataStore lastTableStore = resolveDataStore(query, lastTableName, lastTableSchema);
            Set<String> lastTablePk =
                    getAllPrimaryKeys(lastTableStore, resolveFeatureType(query, lastTableName, lastTableSchema));
            if (!lastTablePk.isEmpty()) {
                int i = 0;
                for (String pk : lastTablePk) {
                    getDataStore().dialect.encodeColumnName(null, pk, sortBySQL);
                    if (i < lastTablePk.size() - 1) {
                        sortBySQL.append(", ");
                    }
                    i++;
                }
                sortBySQL.append(" FROM ");
                if (!lastTableAlias.equals(lastTableName)) {
                    encodeAliasedTableName(lastTableName, lastTableSchema, sortBySQL, query.getHints(), lastTableAlias);
                } else {
                    encodeTableName(lastTableName, lastTableSchema, sortBySQL, query.getHints());
                }
                String sqlFilter;
                if (NestedFilterToSQL.isNestedFilter(filter)) {
                    toSQL.setFieldEncoder(new JoiningFieldEncoder(curTypeName, getDataStore()));
                    sqlFilter =
                            createNestedFilter(filter, filterRootMapping, toSQL).toString();
                } else {
                    sqlFilter = toSQL.encodeToString(filter);
                }
                if (sqlFilter != null && !sqlFilter.isBlank()) {
                    if (NestedFilterToSQL.isNestedFilter(filter)) {
                        sortBySQL.append(" WHERE ");
                    } else {
                        sortBySQL.append(" ");
                    }
                    sortBySQL.append(sqlFilter);
                }
                sortBySQL.append(" ) ");
                getDataStore().dialect.encodeTableName(TEMP_FILTER_ALIAS, sortBySQL);
                sortBySQL.append(" ON ( ");
                i = 0;
                for (String pk : lastTablePk) {
                    encodeColumnName2(pk, lastTableAlias, sortBySQL, null);
                    sortBySQL.append(" = ");
                    encodeColumnName2(pk, TEMP_FILTER_ALIAS, sortBySQL, null);
                    if (i < lastTablePk.size() - 1) {
                        sortBySQL.append(" AND ");
                    }
                    i++;
                }
                hasSortBy = true;
            }
        }
        if (hasSortBy) {
            if (sortBySQL.toString().endsWith(" AND ")) {
                sql.append(sortBySQL.substring(0, sortBySQL.length() - 5)).append(" ) ");
            } else {
                sql.append(sortBySQL).append(" ) ");
            }
        }
    }

    private boolean buildFiterBasedOnSortBy(
            JoiningQuery query,
            FilterToSQL toSQL,
            Filter filter,
            FeatureTypeMapping filterRootMapping,
            SortBy[] lastSortBy,
            String lastTableName,
            String lastTableSchema,
            String lastTableAlias,
            Collection<String> ids,
            StringBuffer sortBySQL,
            boolean hasSortBy)
            throws SQLException, FilterToSQLException {
        for (int i = 0; i < lastSortBy.length; i++) {
            if (!ids.contains(lastSortBy[i].getPropertyName().toString())) {
                hasSortBy = processSortByKey(
                        query,
                        filterRootMapping,
                        toSQL,
                        filter,
                        lastSortBy,
                        lastTableName,
                        lastTableSchema,
                        lastTableAlias,
                        sortBySQL,
                        i);
            }
        }
        return hasSortBy;
    }

    /**
     * Alternative builder used for generating join on clause on presence of multiple sortBy expressions unsupported by
     * normal builder.
     */
    private boolean buildFiterBasedOnPk(
            JoiningQuery query,
            FilterToSQL toSQL,
            Filter filter,
            FeatureTypeMapping filterRootMapping,
            SortBy[] lastSortBy,
            String lastTableName,
            String lastTableSchema,
            String lastTableAlias,
            Set<String> lastPkColumnNames,
            StringBuffer sortBySQL,
            boolean hasSortBy)
            throws SQLException, FilterToSQLException {
        for (int i = lastSortBy.length - lastPkColumnNames.size(); i < lastSortBy.length; i++) {
            hasSortBy = processSortByKey(
                    query,
                    filterRootMapping,
                    toSQL,
                    filter,
                    lastSortBy,
                    lastTableName,
                    lastTableSchema,
                    lastTableAlias,
                    sortBySQL,
                    i);
        }
        return hasSortBy;
    }

    private boolean processSortByKey(
            JoiningQuery query,
            FeatureTypeMapping filterRootMapping,
            FilterToSQL toSQL,
            Filter filter,
            SortBy[] lastSortBy,
            String lastTableName,
            String lastTableSchema,
            String lastTableAlias,
            StringBuffer sortBySQL,
            int i)
            throws SQLException, FilterToSQLException {
        // skip if inner join is already done in paging
        getDataStore()
                .dialect
                .encodeColumnName(lastTableName, lastSortBy[i].getPropertyName().getPropertyName(), sortBySQL);
        sortBySQL.append(" FROM ");
        encodeTableName(lastTableName, lastTableSchema, sortBySQL, query.getHints());
        // perform a left join with multi values tables of the root feature type
        JDBCDataStore mappingStore = getMappingDataStore(filterRootMapping);
        if (mappingStore == null) {
            mappingStore = getDataStore();
        }
        encodeMultipleValueJoin(filterRootMapping, lastTableName, mappingStore, sortBySQL);
        if (NestedFilterToSQL.isNestedFilter(filter)) {
            // if it's postgis and replacement is enabled use UNION
            boolean replaceOrWithUnion = isPostgisDialect() && isOrUnionReplacementEnabled();
            // get current select clause
            String selectClause = sortBySQL.toString().replace(" INNER JOIN ( ", "");
            // use a FilterToSql without prepared statement parameters
            SimpleFeatureType featureType = toSQL.getFeatureType();
            JDBCDataStore lastStore = resolveDataStore(query, lastTableName, lastTableSchema);
            FilterToSQL toSQL2 = createFilterToSQL(lastStore, featureType, false);
            String sqlFilter = createNestedFilter(filter, filterRootMapping, toSQL2, selectClause, replaceOrWithUnion)
                    .toString();
            if (sqlFilter != null && !sqlFilter.isBlank()) {
                sortBySQL.append(" WHERE ").append(sqlFilter);
            }
        } else {
            String sqlFilter = toSQL.encodeToString(filter);
            if (sqlFilter != null && !sqlFilter.isBlank()) {
                sortBySQL.append(" ").append(sqlFilter);
            }
        }
        sortBySQL.append(" ) ");
        getDataStore().dialect.encodeTableName(TEMP_FILTER_ALIAS, sortBySQL);
        sortBySQL.append(" ON ( ");
        encodeColumnName2(lastSortBy[i].getPropertyName().getPropertyName(), lastTableAlias, sortBySQL, null);
        sortBySQL.append(" = ");
        encodeColumnName2(lastSortBy[i].getPropertyName().getPropertyName(), TEMP_FILTER_ALIAS, sortBySQL, null);
        if (i < lastSortBy.length - 1) {
            sortBySQL.append(" AND ");
        }
        boolean hasSortBy = true;
        return hasSortBy;
    }

    private void encodeMultipleValueJoin(
            FeatureTypeMapping rootMapping, String rootTableName, JDBCDataStore store, StringBuffer sql) {
        if (rootMapping == null) {
            return;
        }
        for (AttributeMapping attributeMapping : rootMapping.getAttributeMappings()) {
            if (!(attributeMapping.getMultipleValue() instanceof JdbcMultipleValue)) {
                continue;
            }
            JdbcMultipleValue multipleValue = (JdbcMultipleValue) attributeMapping.getMultipleValue();
            sql.append(" LEFT JOIN ");
            String alias = String.valueOf(multipleValue.getId());
            try {
                store.encodeAliasedTableName(multipleValue.getTargetTable(), sql, null, alias);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sql.append(" ON ");
            store.dialect.encodeColumnName(null, rootTableName, sql);
            sql.append(".");
            store.dialect.encodeColumnName(null, multipleValue.getSourceColumn(), sql);
            sql.append(" = ");
            store.dialect.encodeTableName(alias, sql);
            sql.append(".");
            store.dialect.encodeColumnName(null, multipleValue.getTargetColumn(), sql);
            sql.append(" ");
        }
    }

    private Object createNestedFilter(Filter filter, FeatureTypeMapping rootMapping, FilterToSQL filterToSQL)
            throws FilterToSQLException {
        if (rootMapping == null) {
            return filterToSQL.encodeToString(filter);
        }
        NestedFilterToSQL nested = new NestedFilterToSQL(rootMapping, filterToSQL);
        nested.setInline(true);
        return nested.encodeToString(filter);
    }

    private Object createNestedFilter(
            Filter filter,
            FeatureTypeMapping rootMapping,
            FilterToSQL filterToSQL,
            String selectClause,
            boolean replaceOrWithUnion)
            throws FilterToSQLException {
        if (rootMapping == null) {
            return filterToSQL.encodeToString(filter);
        }
        NestedFilterToSQL nested = new NestedFilterToSQL(rootMapping, filterToSQL);
        nested.setInline(true);
        nested.setSelectClause(selectClause);
        nested.setReplaceOrWithUnion(replaceOrWithUnion);
        return nested.encodeToString(filter);
    }

    /**
     * Apply paging. It's pretty straight forward except when the view is denormalised, because we don't know how many
     * rows would represent 1 feature.
     */
    private boolean applyPaging(
            JoiningQuery query,
            StringBuffer sql,
            Set<String> pkColumnNames,
            String typeName,
            String alias,
            Set<String> tableNames,
            FilterToSQL filterToSQL,
            Filter filter,
            FeatureTypeMapping filterRootMapping,
            Collection<String> allIds)
            throws SQLException, FilterToSQLException, IOException {
        Collection<String> ids = Collections.emptyList();
        String typeSchema = resolveSchemaForTypeName(query, typeName);
        JDBCDataStore typeStore = resolveDataStore(query, typeName, typeSchema);
        typeSchema = effectiveSchema(typeStore, typeSchema);
        if (filterToSQL != null && typeSchema != null) {
            filterToSQL.setDatabaseSchema(typeSchema);
        }
        if (getDataStore().dialect.isLimitOffsetSupported()) {
            int startIndex = query.getStartIndex() == null ? 0 : query.getStartIndex();
            int maxFeatures = query.getMaxFeatures();
            // Default maxfeatures is usually 1000000 unless changed in the config
            if (startIndex > 0 || maxFeatures < 1000000) {
                // handle denormalised grouping by id, or if unspecified, PK
                if (!query.getIds().isEmpty()) {
                    ids = query.getIds();
                } else {
                    ids = pkColumnNames;
                }
                for (String id : ids) {
                    sql.append(" INNER JOIN (");
                    StringBuffer topIds = new StringBuffer();
                    topIds.append("SELECT DISTINCT ");

                    StringBuffer idSQL = new StringBuffer();
                    encodeColumnName(id, typeName, typeSchema, idSQL, query.getHints());
                    topIds.append(idSQL);

                    // apply SORTBY
                    SortBy[] sort = query.getSortBy();
                    Set<String> orderByFields = new LinkedHashSet<>();
                    StringBuffer sortSQL = new StringBuffer();
                    if (sort != null) {
                        sort(query, typeName, typeSchema, null, sort, orderByFields, sortSQL);
                    }
                    if (!orderByFields.contains(idSQL.toString())) {
                        // check for duplicate
                        sortSQL.append(idSQL);
                    }
                    // make sure everything in ORDER BY is also in SELECT
                    for (String orderBy : orderByFields) {
                        if (!idSQL.toString().equals(orderBy)) {
                            topIds.append(", ").append(orderBy);
                        }
                    }
                    // Ensure ORDER BY is never empty to avoid invalid SQL in dialects requiring
                    // at least one ORDER BY expression when paging/sorting is applied.
                    // This can happen when sort expressions resolve only to the same id column.
                    if (sortSQL.length() == 0) {
                        sortSQL.append(idSQL);
                    }
                    topIds.append(" FROM ");
                    encodeTableName(typeName, typeSchema, topIds, query.getHints());
                    // perform a left join with multi values tables of the root feature type
                    JDBCDataStore mappingStore = getMappingDataStore(filterRootMapping);
                    if (mappingStore == null) {
                        mappingStore = getDataStore();
                    }
                    encodeMultipleValueJoin(filterRootMapping, typeName, mappingStore, topIds);
                    // apply filter
                    if (filter != null) {
                        if (NestedFilterToSQL.isNestedFilter(filter)) {
                            filterToSQL.setFieldEncoder(new JoiningFieldEncoder(typeName, getDataStore()));
                            String sqlFilter = createNestedFilter(filter, filterRootMapping, filterToSQL)
                                    .toString();
                            if (sqlFilter != null && !sqlFilter.isBlank()) {
                                topIds.append(" WHERE ").append(sqlFilter);
                            }
                        } else {
                            String sqlFilter = filterToSQL.encodeToString(filter);
                            if (sqlFilter != null && !sqlFilter.isBlank()) {
                                topIds.append(" ").append(sqlFilter);
                            }
                        }
                    }
                    topIds.append(" ORDER BY ");
                    topIds.append(sortSQL);
                    // apply TOP using limit offset
                    getDataStore().dialect.applyLimitOffset(topIds, maxFeatures, startIndex);
                    sql.append(topIds);
                    sql.append(") ");
                    String newAlias = createAlias(typeName, tableNames);
                    tableNames.add(newAlias);
                    getDataStore().dialect.encodeTableName(newAlias, sql);
                    sql.append(" ON (");
                    getDataStore().dialect.encodeColumnName(alias, id, sql);
                    sql.append(" = ");
                    getDataStore().dialect.encodeColumnName(newAlias, id, sql);
                    sql.append(" ) ");
                }
            }
        }
        if (!ids.isEmpty()) {
            if (allIds != null) {
                allIds.addAll(ids);
            }
            return true;
        }
        return false;
    }

    /**
     * Generates a 'SELECT p1, p2, ... FROM ... WHERE ...' prepared statement.
     *
     * @param featureType the feature type that the query must return (may contain less attributes than the native one)
     * @param query the query to be run. The type name and property will be ignored, as they are supposed to have been
     *     already embedded into the provided feature type
     * @param cx The database connection to be used to create the prepared statement
     */
    protected PreparedStatement selectSQLPS(SimpleFeatureType featureType, JoiningQuery query, Connection cx)
            throws SQLException, IOException, FilterToSQLException {

        AtomicReference<PreparedFilterToSQL> toSQLref = new AtomicReference<>();
        String sql = selectSQL(featureType, query, toSQLref);

        LOGGER.fine(sql);
        PreparedStatement ps = cx.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(getDataStore().fetchSize);

        if (toSQLref.get() != null) {
            getDataStore().setPreparedFilterValues(ps, toSQLref.get(), 0, cx);
        }

        return ps;
    }

    @Override
    protected Filter[] splitFilter(Filter original) {
        Filter[] split = new Filter[2];
        if (original != null) {
            // create a filter splitter
            PostPreProcessFilterSplittingVisitor splitter =
                    new PostPreProcessFilterSplittingVisitor(getDataStore().getFilterCapabilities(), null, null);
            original.accept(splitter, null);

            split[0] = splitter.getFilterPre();
            split[1] = splitter.getFilterPost();
        }

        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor() {

            @Override
            public Object visit(PropertyName propertyName, Object extraData) {
                // preserve the JoinPropertyName
                if (propertyName instanceof JoinPropertyName joinPropertyName) {
                    return new JoinPropertyName(
                            joinPropertyName.getFeatureType(),
                            joinPropertyName.getAlias(),
                            joinPropertyName.getPropertyName());
                }
                return super.visit(propertyName, extraData);
            }
        };
        visitor.setFIDValidator(new PrimaryKeyFIDValidator(this));
        split[0] = (Filter) split[0].accept(visitor, null);
        split[1] = (Filter) split[1].accept(visitor, null);

        return split;
    }

    protected SimpleFeatureType getFeatureType(SimpleFeatureType origType, JoiningQuery query) throws IOException {
        sanitizeJoinIds(query);
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.init(origType);

        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        if (query.getQueryJoins() != null) {
            for (int i = 0; i < query.getQueryJoins().size(); i++) {
                if (query.getQueryJoins().get(i).getIds().isEmpty()) {
                    // GEOT-4554: handle PK as default idExpression
                    PrimaryKey joinKey = null;
                    QueryJoin queryJoin = query.getQueryJoins().get(i);
                    String joinTypeName = queryJoin.getJoiningTypeName();
                    String joinSchema = resolveSchemaForJoin(query, queryJoin);
                    SimpleFeatureType joinFeatureType = resolveFeatureType(query, joinTypeName, joinSchema);

                    try {
                        JDBCDataStore store = resolveDataStore(query, joinTypeName, joinSchema);
                        joinKey = store.getPrimaryKey(joinFeatureType);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    int j = 0;
                    for (PrimaryKeyColumn col : joinKey.getColumns()) {
                        query.getQueryJoins().get(i).addId(col.getName());
                        ab.setBinding(String.class);
                        builder.add(ab.buildDescriptor(new NameImpl(FOREIGN_ID) + "_" + i + "_" + j, ab.buildType()));
                        j++;
                    }
                } else {
                    for (int j = 0; j < query.getQueryJoins().get(i).getIds().size(); j++) {
                        ab.setBinding(String.class);
                        builder.add(ab.buildDescriptor(new NameImpl(FOREIGN_ID) + "_" + i + "_" + j, ab.buildType()));
                    }
                }
            }
        }
        if (!query.hasIdColumn()) {
            // add primary key for the case where idExpression is not specified
            PrimaryKey key = null;

            try {
                key = getDataStore().getPrimaryKey(origType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (int j = 0; j < key.getColumns().size(); j++) {
                ab.setBinding(String.class);
                builder.add(ab.buildDescriptor(PRIMARY_KEY + "_" + j, ab.buildType()));
            }
        }

        return builder.buildFeatureType();
    }

    @SuppressWarnings("PMD.CloseResource") // PMD complains about cx, which is closed by the reader
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getJoiningReaderInternal(JoiningQuery query)
            throws IOException {
        // split the filter
        Filter[] split = splitFilter(query.getFilter());
        Filter preFilter = split[0];
        Filter postFilter = split[1];

        if (postFilter != null && postFilter != Filter.INCLUDE) {
            throw new IllegalArgumentException("Postfilters not allowed in Joining Queries");
        }

        // Build the feature type returned by this query. Also build an eventual extra feature type
        // containing the attributes we might need in order to evaluate the post filter
        SimpleFeatureType querySchema;
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            querySchema = getSchema();
        } else {
            querySchema = SimpleFeatureTypeBuilder.retype(getSchema(), query.getPropertyNames());
        }
        // rebuild and add primary key column if there's no idExpression pointing to a database
        // column
        // this is so we can retrieve the PK later to use for feature chaining grouping
        SimpleFeatureType fullSchema =
                query.hasIdColumn() && query.getQueryJoins() == null ? querySchema : getFeatureType(querySchema, query);

        // rebuild a new query with the same params, but just the pre-filter.
        // this must happen after getFeatureType, as that path may sanitize join ids
        JoiningQuery preQuery = new JoiningQuery(query);
        preQuery.setFilter(preFilter);
        preQuery.setRootMapping(query.getRootMapping());

        // create the reader
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        // grab connection
        JDBCDataStore store = getDataStore();
        Connection cx = store.getConnection(getState().getTransaction());
        try {
            // this allows PostGIS to page the results and respect the fetch size
            if (getState().getTransaction() == Transaction.AUTO_COMMIT) {
                cx.setAutoCommit(false);
            }

            SQLDialect dialect = store.getSQLDialect();
            if (dialect instanceof PreparedStatementSQLDialect) {
                PreparedStatement ps = selectSQLPS(querySchema, preQuery, cx);
                reader = new JDBCFeatureReader(ps, cx, this, fullSchema, query);
            } else {
                // build up a statement for the content
                String sql = selectSQL(querySchema, preQuery, null);
                store.getLogger().fine(sql);
                reader = new JDBCFeatureReader(sql, cx, this, fullSchema, query);
            }
        } catch (Exception e) {
            // close the connection
            store.closeSafe(cx);
            // safely rethrow
            throw (IOException) new IOException().initCause(e);
        }

        return reader;
    }

    /** Gets a feature reader for a JDBC multivalued mapping. */
    @SuppressWarnings("PMD.CloseResource") // PMD complains about cx, which is closed by the reader
    public FeatureReader<SimpleFeatureType, SimpleFeature> getJoiningReaderInternal(
            JdbcMultipleValue mv, JoiningQuery query) throws IOException {
        // split the filter
        Filter[] split = splitFilter(query.getFilter());
        Filter preFilter = split[0];
        Filter postFilter = split[1];

        if (postFilter != null && postFilter != Filter.INCLUDE) {
            throw new IllegalArgumentException("Postfilters not allowed in Joining Queries");
        }

        // rebuild a new query with the same params, but just the pre-filter
        JoiningQuery preQuery = new JoiningQuery(query);
        preQuery.setFilter(preFilter);
        preQuery.setRootMapping(query.getRootMapping());

        // Build the feature type returned by this query. Also build an eventual extra feature type
        // containing the attributes we might need in order to evaluate the post filter
        SimpleFeatureType querySchema;
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            querySchema = getSchema();
        } else {
            querySchema = SimpleFeatureTypeBuilder.retype(getSchema(), query.getPropertyNames());
        }

        // grab connection
        JDBCDataStore store = getDataStore();

        // create the reader
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        Connection cx = store.getConnection(getState().getTransaction());
        try {
            // this allows PostGIS to page the results and respect the fetch size
            if (getState().getTransaction() == Transaction.AUTO_COMMIT) {
                cx.setAutoCommit(false);
            }

            // build up a statement for the content
            String sql = selectSQL(querySchema, preQuery, null);
            store.getLogger().fine(sql);

            StringBuffer finalSql = new StringBuffer();
            finalSql.append("SELECT ");
            SimpleFeatureType featureType = store.getSchema(mv.getTargetTable());
            PrimaryKey primaryKeys = store.getPrimaryKey(featureType);
            List<String> pkNames = new ArrayList<>();
            for (PrimaryKeyColumn primaryKey : primaryKeys.getColumns()) {
                String pkName = primaryKey.getName();
                pkNames.add(pkName);
                encodeColumnName(finalSql, mv.getTargetTable(), pkName);
                finalSql.append(", ");
            }
            for (String property : mv.getProperties()) {
                if (!pkNames.contains(property)) {
                    encodeColumnName(finalSql, mv.getTargetTable(), property);
                    finalSql.append(", ");
                }
            }
            finalSql.delete(finalSql.length() - 2, finalSql.length());
            // encode value expression
            FilterToSQL cfToSql = createFilterToSQL(store, store.getSchema(mv.getTargetTable()), true);
            cfToSql.setFieldEncoder(field -> {
                StringBuffer fieldSql = new StringBuffer();
                store.dialect.encodeTableName(mv.getTargetTable(), fieldSql);
                fieldSql.append(".");
                fieldSql.append(field);
                return fieldSql.toString();
            });
            finalSql.append(" FROM ");
            store.encodeTableName(mv.getTargetTable(), finalSql, null);
            finalSql.append(" INNER JOIN (").append(sql).append(") ");
            store.dialect.encodeTableName("mv", finalSql);
            finalSql.append(" ON ");
            store.dialect.encodeTableName("mv", finalSql);
            finalSql.append(".");
            store.dialect.encodeColumnName(null, mv.getSourceColumn(), finalSql);
            finalSql.append(" = ");
            store.encodeTableName(mv.getTargetTable(), finalSql, null);
            finalSql.append(".");
            store.dialect.encodeColumnName(null, mv.getTargetColumn(), finalSql);
            SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
            b.setName(new NameImpl(null, mv.getTargetTable()));
            b.add("id", Object.class);
            b.add("value", Object.class);
            JoiningQuery jdbcQuery = new JoiningQuery(query);
            jdbcQuery.setPropertyNames(mv.getProperties());
            SimpleFeatureType featrueType =
                    SimpleFeatureTypeBuilder.retype(store.getSchema(mv.getTargetTable()), jdbcQuery.getPropertyNames());
            reader = new JDBCFeatureReader(finalSql.toString(), cx, this, featrueType, jdbcQuery);
        } catch (Exception e) {
            // close the connection
            store.closeSafe(cx);
            // safely rethrow
            throw (IOException) new IOException().initCause(e);
        }

        return reader;
    }

    /** Helper method that encode a column name. */
    private void encodeColumnName(StringBuffer sql, String tableName, String columnName) {
        JDBCDataStore dataStore = getDataStore();
        SQLDialect dialect = dataStore.getSQLDialect();
        String schema = dataStore.getDatabaseSchema();
        if (schema != null) {
            dialect.encodeSchemaName(schema, sql);
            sql.append(".");
        }
        dialect.encodeColumnName(tableName, columnName, sql);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        if (query instanceof JoiningQuery joiningQuery) {
            return getJoiningReaderInternal(joiningQuery);
        } else {
            return super.getReaderInternal(query);
        }
    }

    @Override
    protected Query resolvePropertyNames(Query query) {
        if (query instanceof JoiningQuery) {
            return query;
        } else {
            return super.resolvePropertyNames(query);
        }
    }

    @Override
    protected Query joinQuery(Query query) {
        if (this.query == null) {
            return query;
        } else if (query instanceof JoiningQuery joiningQuery) {
            JoiningQuery jQuery = new JoiningQuery(super.joinQuery(query));
            for (String id : joiningQuery.getIds()) {
                jQuery.addId(id);
            }
            jQuery.setQueryJoins(((JoiningQuery) query).getQueryJoins());
            jQuery.setRootMapping(((JoiningQuery) query).getRootMapping());
            return jQuery;
        } else {
            return super.joinQuery(query);
        }
    }

    protected Filter aliasFilter(Filter filter, SimpleFeatureType featureType, String alias) {
        JoinQualifier jq = new JoinQualifier(featureType, alias);
        Filter resultFilter = (Filter) filter.accept(jq, null);
        return resultFilter;
    }

    protected boolean isPostgisDialect() {
        String dialectClassName = getDataStore().getSQLDialect().getClass().getName();
        if ("org.geotools.data.postgis.PostGISDialect".equals(dialectClassName)
                || "org.geotools.data.postgis.PostGISPSDialect".equals(dialectClassName)) {
            return true;
        }
        return false;
    }

    protected boolean isOrUnionReplacementEnabled() {
        return AppSchemaDataAccessConfigurator.isOrUnionReplacementEnabled();
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        // the primaryKey from the wrapped JDBCSource
        Set<String> idColumnNames = getIdColumnNames(getSchema());
        JoiningQuery jQuery = (JoiningQuery) query;

        // Checks if the filter is based on root property
        // and we can go with the root JdbcSource to count
        // or we need a Join query.
        // Uses a MultipleValueExtractor to detect
        // if there are MultipleValues
        MultipleValueExtractor extractor = new MultipleValueExtractor();
        FeatureTypeMapping rootMapping = jQuery.getRootMapping();
        if (rootMapping != null && rootMapping.getFeatureIdExpression() != null) {
            rootMapping.getFeatureIdExpression().accept(extractor, null);
        } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Root mapping id expression is not available; count query will fallback to datastore keys");
        }
        // the id from the app-schema configuration
        Set<String> idMapping = new HashSet<>();
        Collections.addAll(idMapping, extractor.getAttributeNames());
        boolean idsColumnEquals = idColumnNames.equals(idMapping);
        Filter filter = jQuery.getFilter() == null ? Filter.INCLUDE : jQuery.getFilter();
        filter.accept(extractor, null);

        // check if the filter is nested, if not we might delegate to the root JDBCFeatureSource
        boolean isNestedFilter = isJoinRequired(filter, extractor.getMultipleValues(), extractor.getPropertyNameSet());

        // ids are equals and the filter doesn't have nested filters
        // we can delegate the count to the underlying JDBCFeatureSource
        if (idsColumnEquals && !isNestedFilter) return super.getCountInternal(query);

        // not equals the count should be over the actually used
        // field for the ComplexFeature's id
        if (!idsColumnEquals && !idMapping.isEmpty()) idColumnNames = idMapping;

        // Build the feature type returned by this query. Also build an eventual extra feature type
        // containing the attributes we might need in order to evaluate the post filter
        SimpleFeatureType querySchema = getSchema();

        // grab connection
        JDBCDataStore store = getDataStore();
        Connection cx = store.getConnection(getState().getTransaction());
        Statement st = null;
        ResultSet rs = null;
        try {

            SQLDialect dialect = store.getSQLDialect();

            if (dialect instanceof PreparedStatementSQLDialect) {
                AtomicReference<PreparedFilterToSQL> toSQLref = new AtomicReference<>();
                String sql = !isNestedFilter
                        ? createCountQuery(dialect, querySchema, jQuery, idColumnNames, toSQLref)
                        : createJoiningCountQuery(dialect, querySchema, jQuery, idColumnNames, toSQLref);
                st = cx.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                st.setFetchSize(getDataStore().fetchSize);
                if (toSQLref.get() != null && toSQLref.get().getLiteralValues() != null) {
                    getDataStore().setPreparedFilterValues((PreparedStatement) st, toSQLref.get(), 0, cx);
                }
                rs = ((PreparedStatement) st).executeQuery();
            } else {
                String sql = !isNestedFilter
                        ? createCountQuery(dialect, querySchema, jQuery, idColumnNames, null)
                        : createJoiningCountQuery(dialect, querySchema, jQuery, idColumnNames, null);
                st = cx.createStatement();
                rs = st.executeQuery(sql);
            }
            int result = 0;
            if (rs.next()) {
                Object value = rs.getObject(1);
                result = Converters.convert(value, Integer.class);
            }
            int maxFeatures = query.getMaxFeatures();
            if (maxFeatures > 0 && result > maxFeatures) result = query.getMaxFeatures();
            return result;
        } catch (Exception e) {
            // safely rethrow
            throw (IOException) new IOException().initCause(e);
        } finally {
            store.closeSafe(rs);
            store.closeSafe(st);
            store.closeSafe(cx);
        }
    }

    String createCountQuery(
            SQLDialect dialect,
            SimpleFeatureType querySchema,
            JoiningQuery query,
            Set<String> idColumnNames,
            AtomicReference<PreparedFilterToSQL> toSQLRef)
            throws FilterToSQLException, SQLException {
        Filter queryFilter = query.getFilter() == null ? Filter.INCLUDE : query.getFilter();
        if (idColumnNames == null || idColumnNames.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No id columns available for count query; using non-distinct fallback strategy");
            }
            return createCountQueryWithoutIdColumns(querySchema, query, toSQLRef);
        }

        StringBuffer countSQL = new StringBuffer("SELECT COUNT(*) FROM (SELECT DISTINCT ");
        String querySchemaName = resolveSchemaForTypeName(query, querySchema.getTypeName());
        JDBCDataStore queryStore = resolveDataStore(query, querySchema.getTypeName(), querySchemaName);
        querySchemaName = effectiveSchema(queryStore, querySchemaName);
        boolean first = true;
        for (String idColumnName : idColumnNames) {
            if (!first) {
                countSQL.append(", ");
            }
            encodeColumnName(idColumnName, querySchema.getTypeName(), querySchemaName, countSQL, query.getHints());
            first = false;
        }
        countSQL.append(" FROM ");
        encodeTableName(querySchema.getTypeName(), querySchemaName, countSQL, query.getHints());
        if (!queryFilter.equals(Filter.INCLUDE)) {
            countSQL.append(" ");
            FilterToSQL toSql = createFilterToSQL(queryStore, querySchema, true, querySchemaName);
            countSQL.append(toSql.encodeToString(queryFilter));
            capturePreparedFilterToSql(toSQLRef, toSql);
        }
        countSQL.append(")");
        dialect.encodeTableName(DISTINCT_TABLE_ALIAS, countSQL);
        String countQuery = countSQL.toString();
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine(countQuery);
        return countQuery;
    }

    private String createJoiningCountQuery(
            SQLDialect dialect,
            SimpleFeatureType querySchema,
            JoiningQuery query,
            Set<String> idColumnNames,
            AtomicReference<PreparedFilterToSQL> toSQLRef)
            throws IOException, SQLException, FilterToSQLException {
        if (idColumnNames == null || idColumnNames.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No id columns available for joining count query; using DISTINCT * fallback strategy");
            }
            return createJoiningCountQueryWithoutIdColumns(dialect, querySchema, query, toSQLRef);
        }

        StringBuffer countSQL = new StringBuffer("SELECT COUNT(*) FROM (SELECT DISTINCT ");
        boolean first = true;
        for (String idColumnName : idColumnNames) {
            if (!first) {
                countSQL.append(", ");
            }
            dialect.encodeColumnName(COUNT_TABLE_ALIAS, idColumnName, countSQL);
            first = false;
        }
        countSQL.append(" FROM (");
        String sql = selectSQL(querySchema, query, toSQLRef, true);
        countSQL.append(sql).append(") ");
        dialect.encodeTableName(COUNT_TABLE_ALIAS, countSQL);
        countSQL.append(") ");
        dialect.encodeTableName(DISTINCT_TABLE_ALIAS, countSQL);
        String countQuery = countSQL.toString();
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine(countQuery);
        return countQuery;
    }

    /**
     * Fallback for count generation when no id column can be resolved.
     *
     * <p>This prevents invalid SQL like {@code SELECT DISTINCT FROM ...}, which triggers a syntax error across SQL
     * dialects.
     */
    private String createCountQueryWithoutIdColumns(
            SimpleFeatureType querySchema, JoiningQuery query, AtomicReference<PreparedFilterToSQL> toSQLRef)
            throws FilterToSQLException, SQLException {
        Filter queryFilter = query.getFilter() == null ? Filter.INCLUDE : query.getFilter();
        StringBuffer countSQL = new StringBuffer("SELECT COUNT(*) FROM ");
        String querySchemaName = resolveSchemaForTypeName(query, querySchema.getTypeName());
        JDBCDataStore queryStore = resolveDataStore(query, querySchema.getTypeName(), querySchemaName);
        querySchemaName = effectiveSchema(queryStore, querySchemaName);
        encodeTableName(querySchema.getTypeName(), querySchemaName, countSQL, query.getHints());
        if (!queryFilter.equals(Filter.INCLUDE)) {
            countSQL.append(" ");
            FilterToSQL toSql = createFilterToSQL(queryStore, querySchema, true, querySchemaName);
            countSQL.append(toSql.encodeToString(queryFilter));
            capturePreparedFilterToSql(toSQLRef, toSql);
        }
        String countQuery = countSQL.toString();
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine(countQuery);
        return countQuery;
    }

    /**
     * Captures the prepared filter encoder when prepared statements are active.
     *
     * <p>This helper avoids repeating the same cast/assignment block in multiple SQL-building branches.
     */
    private void capturePreparedFilterToSql(AtomicReference<PreparedFilterToSQL> toSQLRef, FilterToSQL toSql) {
        if (toSQLRef != null && toSql instanceof PreparedFilterToSQL) {
            toSQLRef.set((PreparedFilterToSQL) toSql);
        }
    }

    /**
     * Joining fallback for count generation when no id column can be resolved.
     *
     * <p>We use {@code DISTINCT *} over the joining subquery to preserve de-duplication without emitting an empty
     * DISTINCT projection.
     */
    private String createJoiningCountQueryWithoutIdColumns(
            SQLDialect dialect,
            SimpleFeatureType querySchema,
            JoiningQuery query,
            AtomicReference<PreparedFilterToSQL> toSQLRef)
            throws IOException, SQLException, FilterToSQLException {
        StringBuffer countSQL = new StringBuffer("SELECT COUNT(*) FROM (SELECT DISTINCT * FROM (");
        String sql = selectSQL(querySchema, query, toSQLRef, true);
        countSQL.append(sql).append(") ");
        dialect.encodeTableName(COUNT_TABLE_ALIAS, countSQL);
        countSQL.append(") ");
        dialect.encodeTableName(DISTINCT_TABLE_ALIAS, countSQL);
        String countQuery = countSQL.toString();
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine(countQuery);
        return countQuery;
    }

    private Set<String> getIdColumnNames(SimpleFeatureType featureType) throws IOException {
        Set<String> columnNames = new HashSet<>();
        for (PrimaryKeyColumn column : getDataStore().getPrimaryKey(featureType).getColumns()) {
            columnNames.add(column.getName());
        }
        return columnNames;
    }

    private Set<String> getAllPrimaryKeys(JDBCDataStore store, SimpleFeatureType featureType) {
        PrimaryKey key = null;

        try {
            key = store.getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> pkColumnNames = new HashSet<>();
        for (PrimaryKeyColumn col : key.getColumns()) {
            pkColumnNames.add(col.getName());
        }
        return pkColumnNames;
    }

    // check if the filter requires a query with joins by looking at the presence of nested
    // PropertyNames
    // or of JDBCMultipleValues or of JoinPorpertyName
    private boolean isJoinRequired(
            Filter filter, List<MultipleValue> multipleValues, Set<PropertyName> propertyNameSet) {
        return NestedFilterToSQL.isNestedFilter(filter)
                || !multipleValues.isEmpty()
                || propertyNameSet.stream().anyMatch(p -> p instanceof JoinPropertyName);
    }
}
