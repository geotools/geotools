/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.sql.Connection;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaPrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    private static final String METADATA_TABLE = "GT_PK_METADATA";

    private static final String SEQ_TABLE = "seqtable";

    private static final String PLAIN_TABLE = "plaintable";

    private static final String SEQUENCE = "pksequence";

    private static final String SINGLEPK_VIEW = "assignedsinglepk";

    private static final String MULTIPK_VIEW = "assignedmultipk";

    protected HanaPrimaryKeyFinderTestSetup() {
        super(new HanaTestSetup());
    }

    @Override
    protected void createMetadataTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);

            String[][] cols = {
                {"TABLE_SCHEMA", "VARCHAR(256)"},
                {"TABLE_NAME", "VARCHAR(256) NOT NULL"},
                {"PK_COLUMN", "VARCHAR(256) NOT NULL"},
                {"PK_COLUMN_IDX", "INT"},
                {"PK_POLICY", "VARCHAR(32)"},
                {"PK_SEQUENCE", "VARCHAR(256)"}
            };
            htu.createTable(null, METADATA_TABLE, cols);
        }
    }

    @Override
    protected void dropMetadataTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTableCascade(null, METADATA_TABLE);
        }
    }

    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);

            String[][] cols = {
                {"key", "INT PRIMARY KEY"},
                {"name", "VARCHAR(256)"},
                {"geom", "ST_Geometry(1000004326)"}
            };
            htu.createTable(null, SEQ_TABLE, cols);
            htu.createSequence(null, SEQUENCE, 1);

            htu.insertIntoTable(
                    null, SEQ_TABLE, htu.nextSequenceValue(null, SEQUENCE), "one", null);
            htu.insertIntoTable(
                    null, SEQ_TABLE, htu.nextSequenceValue(null, SEQUENCE), "two", null);
            htu.insertIntoTable(
                    null, SEQ_TABLE, htu.nextSequenceValue(null, SEQUENCE), "three", null);

            htu.insertIntoTable(
                    null, METADATA_TABLE, null, SEQ_TABLE, "key", 0, "sequence", SEQUENCE);
        }
    }

    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropSequence(null, SEQUENCE);
            htu.dropTable(null, SEQ_TABLE);
        }
    }

    @Override
    protected void createPlainTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);

            String[][] cols = {
                {"key1", "INT"},
                {"key2", "INT"},
                {"name", "VARCHAR(256)"},
                {"geom", "ST_Geometry(1000004326)"}
            };
            htu.createTable(null, PLAIN_TABLE, cols);

            htu.insertIntoTable(null, PLAIN_TABLE, 1, 2, "one", null);
            htu.insertIntoTable(null, PLAIN_TABLE, 2, 3, "two", null);
            htu.insertIntoTable(null, PLAIN_TABLE, 3, 4, "three", null);
        }
    }

    @Override
    protected void dropPlainTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTable(null, PLAIN_TABLE);
        }
    }

    @Override
    protected void createAssignedSinglePkView() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createView(null, SINGLEPK_VIEW, PLAIN_TABLE);
            htu.insertIntoTable(
                    null, METADATA_TABLE, null, SINGLEPK_VIEW, "key1", 0, "assigned", null);
        }
    }

    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropView(null, SINGLEPK_VIEW);
        }
    }

    @Override
    protected void createAssignedMultiPkView() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.createView(null, MULTIPK_VIEW, PLAIN_TABLE);

            htu.insertIntoTable(
                    null, METADATA_TABLE, null, MULTIPK_VIEW, "key1", 0, "assigned", null);
            htu.insertIntoTable(
                    null, METADATA_TABLE, null, MULTIPK_VIEW, "key2", 1, "assigned", null);
        }
    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropView(null, MULTIPK_VIEW);
        }
    }
}
