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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import org.junit.Test;

public class DuckDBExecutionPoliciesTest {

    @Test
    public void testPublicPolicyAllowsReadStatements() throws Exception {
        DuckDBExecutionPolicies.jdbcStorePublic().validateStatementSql("select 1");
        DuckDBExecutionPolicies.jdbcStorePublic().validateStatementSql("pragma show_tables");
    }

    @Test
    public void testPublicPolicyRejectsCommonTableExpressions() {
        assertRejected("with cte as (select 1) select * from cte");
        assertRejected("with x as (insert into t values (1)) select * from x");
    }

    @Test
    public void testPublicPolicyRejectsWritesAndViews() {
        assertRejected("insert into test values (1)");
        assertRejected("create table test(id integer)");
        assertRejected("create or replace view v as select 1");
    }

    @Test
    public void testPublicPolicyRejectsCommentsAndMultiStatements() {
        assertRejected("-- comment\nselect 1");
        assertRejected("select 1; select 2");
    }

    @Test
    public void testWritablePolicyAllowsManagedWriteStatementsButRejectsViews() throws Exception {
        DuckDBExecutionPolicies.jdbcStoreWritable().validateStatementSql("with cte as (select 1) select * from cte");
        DuckDBExecutionPolicies.jdbcStoreWritable().validateStatementSql("create table test(id integer)");
        DuckDBExecutionPolicies.jdbcStoreWritable().validateStatementSql("insert into test values (1)");
        DuckDBExecutionPolicies.jdbcStoreWritable().validateStatementSql("comment on column test.id is 'pk'");

        try {
            DuckDBExecutionPolicies.jdbcStoreWritable().validateStatementSql("create or replace view v as select 1");
            fail("Expected CREATE VIEW to be rejected");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("rejected"));
        }
    }

    @Test
    public void testWritablePolicyRejectsRuntimeLoadStatements() {
        try {
            DuckDBExecutionPolicies.jdbcStoreWritable().validateStatementSql("load spatial");
            fail("Expected LOAD to be rejected at runtime");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("rejected"));
        }
    }

    private void assertRejected(String sql) {
        try {
            DuckDBExecutionPolicies.jdbcStorePublic().validateStatementSql(sql);
            fail("Expected SQL to be rejected: " + sql);
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("rejected"));
        }
    }
}
