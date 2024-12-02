/*
 * Copyright (C) 2024 B3Partners B.V.
 *
 * SPDX-License-Identifier: MIT
 */
package org.geotools.data.sqlserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.util.List;
import org.geotools.api.data.Transaction;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

public class SQLServerDialectOnlineTest extends JDBCTestSupport {

    private final String testTypeName = "multiGeom";

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SQLServerDialectTestSetup();
    }

    @Test
    public void testOptimizedBounds() throws Exception {
        ((SQLServerDialect) dialect).setEstimatedExtentsEnabled(true);

        final ReferencedEnvelope expectedBounds =
                new ReferencedEnvelope(
                        132782,
                        132975,
                        457575,
                        457666,
                        dataStore.getSchema(testTypeName).getCoordinateReferenceSystem());
        final ReferencedEnvelope expectedBounds2 =
                new ReferencedEnvelope(
                        132400,
                        132600,
                        457400,
                        457600,
                        dataStore.getSchema(testTypeName).getCoordinateReferenceSystem());

        try (Connection cx = dataStore.getConnection(Transaction.AUTO_COMMIT)) {
            List<ReferencedEnvelope> bounds =
                    dialect.getOptimizedBounds(null, dataStore.getSchema(testTypeName), cx);

            assertEquals(
                    "Expected a bounds record for each geometry column in the table",
                    3,
                    bounds.size());
            assertEquals("", expectedBounds, bounds.get(0));
            assertEquals("", expectedBounds2, bounds.get(1));
            assertNull("Expecting null bounds for geom_noindex", bounds.get(2));
        }
    }

    @Test
    public void testOptimizedBoundsNoEstimatedExtents() throws Exception {
        ((SQLServerDialect) dialect).setEstimatedExtentsEnabled(false);

        try (Connection cx = dataStore.getConnection(Transaction.AUTO_COMMIT)) {
            List<ReferencedEnvelope> bounds =
                    dialect.getOptimizedBounds(null, dataStore.getSchema(testTypeName), cx);
            assertNull("Expecting no bounds with estimatedExtentsEnabled set to false", bounds);
        }
    }
}
