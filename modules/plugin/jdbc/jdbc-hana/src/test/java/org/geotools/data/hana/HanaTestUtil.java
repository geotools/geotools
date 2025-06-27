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
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.geotools.data.hana.metadata.MetadataDdl;
import org.geotools.data.hana.metadata.Srs;

/** @author Stefan Uhrig, SAP SE */
public class HanaTestUtil {

    private static final String HANA_UUID = "8E468249703240F0ACDE78162124A62F";

    private static final String SEQUENCE_SUFFIX = "_SEQ_" + HANA_UUID;

    private static final String METADATA_TABLE_NAME = "METADATA_" + HANA_UUID;

    public static String getSequenceName(String tableName, String columnName) {
        return tableName + "_" + columnName + SEQUENCE_SUFFIX;
    }

    public static String nextSequenceValSql(String schemaName, String tableName, String columnName) {
        String sequenceName = getSequenceName(tableName, columnName);
        StringBuilder sb = new StringBuilder();
        encodeIdentifiers(sb, schemaName, sequenceName);
        sb.append(".NEXTVAL");
        return sb.toString();
    }

    public static StringBuilder encodeIdentifiers(StringBuilder sb, String... ids) {
        boolean first = true;
        for (String id : ids) {
            if (id == null) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append('.');
            }
            sb.append(HanaUtil.encodeIdentifier(id));
        }
        return sb;
    }

    public static void safeClose(ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        } catch (SQLException e) {
        }
    }

    public static void safeClose(Statement stmt) {
        if (stmt == null) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
        }
    }

    public HanaTestUtil(Connection conn, Properties fixture) {
        this.conn = conn;
        this.fixture = fixture;
    }

    private Connection conn;

    private Properties fixture;

    public boolean srsExists(int srid) throws SQLException {
        try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(*) FROM PUBLIC.ST_SPATIAL_REFERENCE_SYSTEMS WHERE SRS_ID = ?")) {
            ps.setInt(1, srid);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new AssertionError();
                }
                int count = rs.getInt(1);
                return count == 1;
            }
        }
    }

    public void createSrs(Srs srs) throws SQLException {
        if (srsExists(srs.getSrid())) {
            return;
        }
        String sql = MetadataDdl.getSrsDdl(srs);
        execute(sql);
    }

    public boolean schemaExists(String schemaName) throws SQLException {
        if (schemaName == null) {
            return true;
        }
        try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(*) FROM PUBLIC.SCHEMAS WHERE SCHEMA_NAME = ?")) {
            ps.setString(1, schemaName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new AssertionError();
                }
                int count = rs.getInt(1);
                return count == 1;
            }
        }
    }

    public void createSchema(String schemaName) throws SQLException {
        if (schemaExists(schemaName)) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE SCHEMA ");
        encodeIdentifiers(sql, schemaName);
        execute(sql.toString());
    }

    public String getTestSchema() {
        return fixture.getProperty("schema", "geotools");
    }

    public void createTestSchema() throws SQLException {
        createSchema(getTestSchema());
    }

    public String resolveSchema(String schemaName) throws SQLException {
        if (schemaName == null) {
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT CURRENT_SCHEMA FROM DUMMY")) {
                if (!rs.next()) {
                    throw new AssertionError();
                }
                schemaName = rs.getString(1);
            }
        }
        return schemaName;
    }

    public boolean tableExists(String schemaName, String tableName) throws SQLException {
        String sql = schemaName == null
                ? "SELECT COUNT(*) FROM PUBLIC.TABLES WHERE SCHEMA_NAME = CURRENT_SCHEMA AND TABLE_NAME = ?"
                : "SELECT COUNT(*) FROM PUBLIC.TABLES WHERE SCHEMA_NAME = ? AND TABLE_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (schemaName == null) {
                ps.setString(1, tableName);
            } else {
                ps.setString(1, schemaName);
                ps.setString(2, tableName);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new AssertionError();
                }
                int count = rs.getInt(1);
                return count == 1;
            }
        }
    }

    public void createRegisteredTable(String schemaName, String tableName, String[]... cols) throws SQLException {
        createTable(schemaName, tableName, cols);
        createSequencesForTable(schemaName, tableName);
    }

    public void createRegisteredTestTable(String tableName, String[]... cols) throws SQLException {
        createRegisteredTable(getTestSchema(), tableName, cols);
    }

    public void createTable(String schemaName, String tableName, String[]... cols) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE COLUMN TABLE ");
        encodeIdentifiers(sql, schemaName, tableName);
        sql.append(" (");
        for (int i = 0; i < cols.length; ++i) {
            String[] col = cols[i];
            if (col.length != 2) {
                throw new IllegalArgumentException();
            }
            if (i > 0) {
                sql.append(", ");
            }
            encodeIdentifiers(sql, col[0]);
            sql.append(' ');
            sql.append(col[1]);
        }
        sql.append(")");
        execute(sql.toString());
    }

    public void createTestTable(String tableName, String[]... cols) throws SQLException {
        createTable(getTestSchema(), tableName, cols);
    }

    public List<String> getPrimaryKeyColumnsOfTable(String schemaName, String tableName) throws SQLException {
        schemaName = resolveSchema(schemaName);
        return getPrimaryKeys(schemaName, tableName);
    }

    public void addPrimaryKey(String schemaName, String tableName, String... fields) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ");
        encodeIdentifiers(sql, schemaName, tableName);
        sql.append(" ADD PRIMARY KEY (");
        for (int i = 0; i < fields.length; ++i) {
            if (i > 0) {
                sql.append(", ");
            }
            encodeIdentifiers(sql, fields[i]);
        }
        sql.append(")");
        execute(sql.toString());
    }

    public void addUniqueIndex(String schemaName, String tableName, String indexName, String... fields)
            throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE UNIQUE INDEX ");
        encodeIdentifiers(sql, schemaName, indexName);
        sql.append(" ON ");
        encodeIdentifiers(sql, schemaName, tableName);
        sql.append(" (");
        for (int i = 0; i < fields.length; ++i) {
            if (i > 0) {
                sql.append(", ");
            }
            encodeIdentifiers(sql, fields[i]);
        }
        sql.append(")");
        execute(sql.toString());
    }

    public void createSequencesForTable(String schemaName, String tableName) throws SQLException {
        List<String> pkColumns = getPrimaryKeyColumnsOfTable(schemaName, tableName);
        for (String pkColumn : pkColumns) {
            String sequenceName = getSequenceName(tableName, pkColumn);
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE SEQUENCE ");
            encodeIdentifiers(sql, schemaName, sequenceName);
            sql.append(" MINVALUE 0");
            execute(sql.toString());
        }
    }

    public void dropTableCascade(String schemaName, String tableName) throws SQLException {
        dropMetadataOfTable(schemaName, tableName);
        dropSequencesOfTable(schemaName, tableName);
        dropTable(schemaName, tableName);
    }

    public void dropTestTableCascade(String tableName) throws SQLException {
        dropTableCascade(getTestSchema(), tableName);
    }

    public void dropTable(String schemaName, String tableName) throws SQLException {
        if (!tableExists(schemaName, tableName)) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("DROP TABLE ");
        encodeIdentifiers(sql, schemaName, tableName);
        execute(sql.toString());
    }

    public void dropMetadataOfTable(String schemaName, String tableName) throws SQLException {
        if (!tableExists(schemaName, METADATA_TABLE_NAME)) {
            return;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        encodeIdentifiers(sql, schemaName, METADATA_TABLE_NAME);
        sql.append(" WHERE TABLE_NAME = ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, tableName);
            ps.executeUpdate();
        }
    }

    public void dropSequencesOfTable(String schemaName, String tableName) throws SQLException {
        List<String> sequences = getSequencesOfTable(schemaName, tableName);
        for (String sequence : sequences) {
            dropSequence(schemaName, sequence);
        }
    }

    public List<String> getSequencesOfTable(String schemaName, String tableName) throws SQLException {
        List<String> pks = getPrimaryKeyColumnsOfTable(schemaName, tableName);
        ArrayList<String> ret = new ArrayList<>();
        for (String pk : pks) {
            ret.add(getSequenceName(tableName, pk));
        }
        return ret;
    }

    public boolean viewExists(String schemaName, String viewName) throws SQLException {
        String sql = schemaName == null
                ? "SELECT COUNT(*) FROM PUBLIC.VIEWS WHERE SCHEMA_NAME = CURRENT_SCHEMA AND VIEW_NAME = ?"
                : "SELECT COUNT(*) FROM PUBLIC.VIEWS WHERE SCHEMA_NAME = ? AND VIEW_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (schemaName == null) {
                ps.setString(1, viewName);
            } else {
                ps.setString(1, schemaName);
                ps.setString(2, viewName);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new AssertionError();
                }
                int count = rs.getInt(1);
                return count == 1;
            }
        }
    }

    public void createView(String schemaName, String viewName, String sourceTableName, String... selectedFields)
            throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE VIEW ");
        encodeIdentifiers(sql, schemaName, viewName);
        sql.append(" AS SELECT ");
        if (selectedFields.length == 0) {
            sql.append("*");
        } else {
            for (int i = 0; i < selectedFields.length; ++i) {
                if (i > 0) {
                    sql.append(", ");
                }
                encodeIdentifiers(sql, selectedFields[i]);
            }
        }
        sql.append(" FROM ");
        encodeIdentifiers(sql, schemaName, sourceTableName);
        execute(sql.toString());
    }

    public void createTestView(String viewName, String sourceTableName, String... selectedFields) throws SQLException {
        createView(getTestSchema(), viewName, sourceTableName, selectedFields);
    }

    public void dropView(String schemaName, String viewName) throws SQLException {
        if (!viewExists(schemaName, viewName)) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("DROP VIEW ");
        encodeIdentifiers(sql, schemaName, viewName);
        execute(sql.toString());
    }

    public void dropTestView(String viewName) throws SQLException {
        dropView(getTestSchema(), viewName);
    }

    public boolean sequenceExists(String schemaName, String sequenceName) throws SQLException {
        String sql = schemaName == null
                ? "SELECT COUNT(*) FROM PUBLIC.SEQUENCES WHERE SCHEMA_NAME = CURRENT_SCHEMA AND SEQUENCE_NAME = ?"
                : "SELECT COUNT(*) FROM PUBLIC.SEQUENCES WHERE SCHEMA_NAME = ? AND SEQUENCE_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (schemaName == null) {
                ps.setString(1, sequenceName);
            } else {
                ps.setString(1, schemaName);
                ps.setString(2, sequenceName);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new AssertionError();
                }
                int count = rs.getInt(1);
                return count == 1;
            }
        }
    }

    public void createSequence(String schemaName, String sequenceName, int startValue) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE SEQUENCE ");
        encodeIdentifiers(sql, schemaName, sequenceName);
        sql.append(" START WITH ");
        sql.append(startValue);
        execute(sql.toString());
    }

    public void skipSequenceValueFor(String schemaName, String tableName, String columnName) throws SQLException {
        execute("SELECT " + HanaTestUtil.nextSequenceValSql(schemaName, tableName, columnName) + " FROM DUMMY");
    }

    public void dropSequence(String schemaName, String sequenceName) throws SQLException {
        if (!sequenceExists(schemaName, sequenceName)) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("DROP SEQUENCE ");
        encodeIdentifiers(sql, schemaName, sequenceName);
        execute(sql.toString());
    }

    public void insertIntoTable(String schemaName, String tableName, Object... values) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        encodeIdentifiers(sql, schemaName, tableName);
        sql.append(" VALUES (");
        for (int i = 0; i < values.length; ++i) {
            if (i > 0) {
                sql.append(", ");
            }
            addObject(sql, values[i]);
        }
        sql.append(")");
        execute(sql.toString());
    }

    public void insertIntoTestTable(String tableName, Object... values) throws SQLException {
        insertIntoTable(getTestSchema(), tableName, values);
    }

    public void insertFieldsIntoTable(String schemaName, String tableName, Object... fieldsAndValues)
            throws SQLException {
        int numFields = fieldsAndValues.length / 2;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        encodeIdentifiers(sql, schemaName, tableName);
        sql.append(" (");
        for (int i = 0; i < numFields; ++i) {
            if (i > 0) {
                sql.append(", ");
            }
            String name = (String) fieldsAndValues[i];
            encodeIdentifiers(sql, name);
        }
        sql.append(") VALUES (");
        for (int i = numFields; i < fieldsAndValues.length; ++i) {
            if (i > numFields) {
                sql.append(", ");
            }
            addObject(sql, fieldsAndValues[i]);
        }
        sql.append(")");
        execute(sql.toString());
    }

    public Object nextSequenceValueForColumn(String schemaName, String tableName, String columnName) {
        String sequenceName = getSequenceName(tableName, columnName);
        return new NextSequenceValue(schemaName, sequenceName);
    }

    public Object nextTestSequenceValueForColumn(String tableName, String columnName) {
        String sequenceName = getSequenceName(tableName, columnName);
        return new NextSequenceValue(getTestSchema(), sequenceName);
    }

    public Object nextSequenceValue(String schemaName, String sequenceName) {
        return new NextSequenceValue(schemaName, sequenceName);
    }

    public Object geometry(String wkt, int srid) {
        return new Geometry(wkt, srid);
    }

    private void addObject(StringBuilder sql, Object object) {
        if (object == null) {
            sql.append("NULL");
        } else if (object instanceof String) {
            addString(sql, (String) object);
        } else if (object instanceof Integer) {
            sql.append(Integer.toString((Integer) object));
        } else if (object instanceof NextSequenceValue) {
            NextSequenceValue nsv = (NextSequenceValue) object;
            nsv.encode(sql);
        } else if (object instanceof Geometry) {
            Geometry g = (Geometry) object;
            g.encode(sql);
        } else if (object instanceof Double) {
            Double d = (Double) object;
            sql.append(Double.toString(d));
        } else if (object instanceof Boolean) {
            Boolean b = (Boolean) object;
            sql.append(b == true ? "TRUE" : "FALSE");
        } else if (object instanceof byte[]) {
            byte[] bin = (byte[]) object;
            addByteArray(sql, bin);
        } else {
            throw new RuntimeException("Unsupported object type " + object.getClass());
        }
    }

    private void addString(StringBuilder sql, String s) {
        sql.append(HanaUtil.toStringLiteral(s));
    }

    private static final char[] HEX_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String asHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    private void addByteArray(StringBuilder sql, byte[] bin) {
        sql.append("x'");
        for (byte b : bin) {
            sql.append(HEX_CHARS[(b & 0xF0) >>> 4]);
            sql.append(HEX_CHARS[b & 0x0F]);
        }
        sql.append("'");
    }

    private void execute(String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private List<String> getPrimaryKeys(String schemaName, String tableName) throws SQLException {
        DatabaseMetaData dbmd = conn.getMetaData();
        List<String> pkColumns = new ArrayList<>();
        try (ResultSet rs = dbmd.getPrimaryKeys(null, schemaName, tableName)) {
            while (rs.next()) {
                pkColumns.add(rs.getString(4));
            }
        }
        return pkColumns;
    }

    private static class NextSequenceValue {

        public NextSequenceValue(String schemaName, String sequenceName) {
            this.schemaName = schemaName;
            this.sequenceName = sequenceName;
        }

        private String schemaName;

        private String sequenceName;

        public void encode(StringBuilder sql) {
            encodeIdentifiers(sql, schemaName, sequenceName);
            sql.append(".NEXTVAL");
        }
    }

    private static class Geometry {

        public Geometry(String wkt, int srid) {
            this.wkt = wkt;
            this.srid = srid;
        }

        private String wkt;

        private int srid;

        public void encode(StringBuilder sql) {
            sql.append("ST_GeomFromText(");
            sql.append(HanaUtil.toStringLiteral(wkt));
            sql.append(", ");
            sql.append(Integer.toString(srid));
            sql.append(")");
        }
    }
}
