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

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/** Factory for the SQL execution policies used by DuckDB-backed datastores. */
public final class DuckDBExecutionPolicies {

    private static final Set<String> INIT_PREFIXES = Set.of("INSTALL", "LOAD");

    /*
     * Public (read_only=true) stores intentionally disallow WITH because DuckDB supports writable CTE bodies
     * (for example WITH ... INSERT ...), which would bypass a plain prefix allow-list.
     */
    private static final Set<String> PUBLIC_READ_PREFIXES =
            Set.of("SELECT", "VALUES", "EXPLAIN", "SHOW", "DESCRIBE", "PRAGMA");

    private static final Set<String> TRUSTED_READ_PREFIXES = union(PUBLIC_READ_PREFIXES, Set.of("WITH"));

    private static final Set<String> WRITE_PREFIXES = Set.of(
            "CREATE TABLE",
            "CREATE INDEX",
            "CREATE SEQUENCE",
            "ALTER TABLE",
            "DROP TABLE",
            "DROP INDEX",
            "DROP SEQUENCE",
            "INSERT",
            "UPDATE",
            "DELETE",
            "COMMENT ON");

    private static final Set<String> GEOPARQUET_PREFIXES =
            Set.of("CREATE OR REPLACE VIEW", "DROP VIEW IF EXISTS", "CREATE OR REPLACE SECRET");

    private static final Set<String> TEST_SETUP_PREFIXES =
            Set.of("CREATE VIEW", "CREATE OR REPLACE VIEW", "DROP VIEW", "CREATE UNIQUE INDEX");

    private DuckDBExecutionPolicies() {}

    /** Policy used by the public DuckDB datastore: read-only SQL, no user-managed session scripts, no DDL/DML. */
    public static DuckDBExecutionPolicy jdbcStorePublic() {
        return new PrefixBasedPolicy("jdbc-store-public", INIT_PREFIXES, PUBLIC_READ_PREFIXES);
    }

    /** Policy used by the writable DuckDB datastore: GeoTools-managed read/write SQL, still with no session scripts. */
    public static DuckDBExecutionPolicy jdbcStoreWritable() {
        return new PrefixBasedPolicy(
                "jdbc-store-writable", INIT_PREFIXES, union(TRUSTED_READ_PREFIXES, WRITE_PREFIXES));
    }

    /** Policy used by GeoParquet for trusted internal extension bootstrap and managed view/secret creation. */
    public static DuckDBExecutionPolicy geoparquetInternal() {
        return new PrefixBasedPolicy(
                "geoparquet-internal", INIT_PREFIXES, union(TRUSTED_READ_PREFIXES, GEOPARQUET_PREFIXES));
    }

    /**
     * Policy used by JDBC online-test setup SQL. It relaxes writable policy with view DDL needed by fixture creation.
     *
     * <p>This policy is intended for setup/teardown statements only, not for runtime datastore operations.
     */
    public static DuckDBExecutionPolicy jdbcTestSetup() {
        return new PrefixBasedPolicy(
                "jdbc-test-setup",
                INIT_PREFIXES,
                union(union(TRUSTED_READ_PREFIXES, WRITE_PREFIXES), TEST_SETUP_PREFIXES));
    }

    private static Set<String> union(Set<String> left, Set<String> right) {
        LinkedHashSet<String> values = new LinkedHashSet<>(left);
        values.addAll(right);
        return Set.copyOf(values);
    }

    private static final class PrefixBasedPolicy implements DuckDBExecutionPolicy {

        private final String name;
        private final Set<String> initPrefixes;
        private final Set<String> statementPrefixes;

        private PrefixBasedPolicy(String name, Set<String> initPrefixes, Set<String> statementPrefixes) {
            this.name = name;
            this.initPrefixes = initPrefixes;
            this.statementPrefixes = statementPrefixes;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void validateDatabaseInitSql(String sql) throws SQLException {
            validate(sql, initPrefixes, "database init");
        }

        @Override
        public void validateStatementSql(String sql) throws SQLException {
            validate(sql, statementPrefixes, "statement");
        }

        private void validate(String sql, Set<String> allowedPrefixes, String context) throws SQLException {
            String normalized = normalize(sql);
            for (String prefix : allowedPrefixes) {
                if (normalized.startsWith(prefix)) {
                    return;
                }
            }
            throw new SQLException("DuckDB execution policy '" + name + "' rejected " + context + " starting with "
                    + summarize(normalized));
        }
    }

    static String normalize(String sql) throws SQLException {
        if (sql == null) {
            throw new SQLException("DuckDB execution policy received null SQL");
        }

        String normalized = sql.trim();
        if (normalized.isEmpty()) {
            throw new SQLException("DuckDB execution policy received empty SQL");
        }
        if (normalized.startsWith("--") || normalized.startsWith("/*")) {
            throw new SQLException("DuckDB execution policy rejected SQL comments");
        }
        if (normalized.endsWith(";")) {
            normalized = normalized.substring(0, normalized.length() - 1).trim();
        }
        if (normalized.indexOf(';') >= 0) {
            throw new SQLException("DuckDB execution policy rejected multi-statement SQL");
        }
        return normalized.toUpperCase(Locale.ROOT);
    }

    private static String summarize(String normalizedSql) {
        String[] tokens = normalizedSql.split("\\s+");
        StringBuilder summary = new StringBuilder();
        int count = Math.min(tokens.length, 4);
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                summary.append(' ');
            }
            summary.append(tokens[i]);
        }
        return summary.length() == 0 ? "<empty>" : "'" + summary + "'";
    }
}
