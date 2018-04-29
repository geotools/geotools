/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General  License for more details.
 */

package org.geotools.data.db2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import junit.framework.Assert;

class DB2TestUtil {

    protected static boolean ISENABLED = false;
    protected static final int SRID = 1003;
    protected static final String SRSNAME = "WGS84_SRS_1003";
    //	protected final static int SRID = 0;
    //	protected final static String SRSNAME = "DEFAULT_SRS";

    protected static final String SCHEMA = "geotools";
    protected static final String SCHEMA_QUOTED = "\"" + SCHEMA + "\"";

    static boolean existsTable(String schemaName, String tableName, Connection con)
            throws SQLException {
        PreparedStatement ps =
                con.prepareStatement(
                        "SELECT TABNAME FROM SYSCAT.TABLES WHERE TABSCHEMA = ? AND TABNAME = ?");
        ps.setString(1, schemaName);
        ps.setString(2, tableName);
        boolean retval = false;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) retval = true;
        rs.close();
        ps.close();
        return retval;
    }

    static boolean existsTrigger(String schemaName, String triggerName, Connection con)
            throws SQLException {
        PreparedStatement ps =
                con.prepareStatement(
                        "SELECT TRIGNAME FROM SYSCAT.TRIGGERS WHERE TRIGSCHEMA = ? AND TRIGNAME = ?");
        ps.setString(1, schemaName);
        ps.setString(2, triggerName);
        boolean retval = false;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) retval = true;
        rs.close();
        ps.close();
        return retval;
    }

    static boolean existsSequence(String schemaName, String seqName, Connection con)
            throws SQLException {
        PreparedStatement ps =
                con.prepareStatement(
                        "select seqschema,seqname from syscat.sequences where seqschema = ? and seqname = ?");
        ps.setString(1, schemaName);
        ps.setString(2, seqName);
        boolean retval = false;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) retval = true;
        rs.close();
        ps.close();
        return retval;
    }

    static boolean existsIndex(String schemaName, String indexName, Connection con)
            throws SQLException {

        PreparedStatement ps =
                con.prepareStatement(
                        "SELECT INDNAME FROM SYSCAT.INDEXES WHERE INDSCHEMA = ? AND INDNAME = ?");
        ps.setString(1, schemaName);
        ps.setString(2, indexName);
        boolean retval = false;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) retval = true;
        rs.close();
        ps.close();
        return retval;
    }

    static String getCurrrentSchemaName(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("select current schema from sysibm.sysdummy1");
        ResultSet rs = ps.executeQuery();
        String result = null;
        if (rs.next()) result = rs.getString(1);
        rs.close();
        ps.close();
        return result;
    }

    static void dropTable(String schemaName, String tableName, Connection con) throws SQLException {
        if (existsTable(schemaName, tableName, con) == false) return;
        PreparedStatement ps =
                con.prepareStatement("drop table \"" + schemaName + "\".\"" + tableName + "\"");
        ps.execute();
        ps.close();
    }

    static void dropView(String schemaName, String tableName, Connection con) throws SQLException {
        if (existsTable(schemaName, tableName, con) == false) return;
        PreparedStatement ps =
                con.prepareStatement("drop view \"" + schemaName + "\".\"" + tableName + "\"");
        ps.execute();
        ps.close();
    }

    static void dropIndex(String schemaName, String indexName, Connection con) throws SQLException {
        if (existsIndex(schemaName, indexName, con) == false) return;
        PreparedStatement ps =
                con.prepareStatement("drop index \"" + schemaName + "\".\"" + indexName + "\"");
        ps.execute();
        ps.close();
    }

    static void dropTrigger(String schemaName, String triggerName, Connection con)
            throws SQLException {
        if (existsTrigger(schemaName, triggerName, con) == false) return;
        PreparedStatement ps =
                con.prepareStatement("drop trigger \"" + schemaName + "\".\"" + triggerName + "\"");
        ps.execute();
        ps.close();
    }

    static void dropSequence(String schemaName, String seqName, Connection con)
            throws SQLException {
        if (existsSequence(schemaName, seqName, con) == false) return;
        PreparedStatement ps =
                con.prepareStatement("drop sequence \"" + schemaName + "\".\"" + seqName + "\"");
        ps.execute();
        ps.close();
    }

    protected static void enableDB(Connection con) {
        if (ISENABLED) return;
        try {
            CallableStatement s = con.prepareCall(" {call db2gse.ST_enable_db(?,?,?) }");
            s.registerOutParameter(2, Types.INTEGER);
            s.registerOutParameter(3, Types.CHAR);
            s.setNull(1, Types.CHAR);
            s.executeUpdate();
            DB2TestSetup.LOGGER.log(Level.INFO, s.getInt(2) + "|" + s.getString(3));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            DB2TestSetup.LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        ISENABLED = true;
    }

    public static void executecCreateSRS(
            String srsName,
            int srsId,
            double xoffset,
            double xscale,
            double yoffset,
            double yscale,
            String crsName,
            Connection con)
            throws SQLException {
        String stmt = "call db2gse.ST_create_srs(?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        String s = "{" + stmt + "}";
        CallableStatement ps = con.prepareCall(s);
        ps.setString(1, quote(srsName));
        ps.setInt(2, srsId);
        ps.setDouble(3, xoffset);
        ps.setDouble(4, xscale);
        ps.setDouble(5, yoffset);
        ps.setDouble(6, yscale);
        ps.setDouble(7, 0.0);
        ps.setDouble(8, 1.0);
        ps.setDouble(9, 0.0);
        ps.setDouble(10, 1.0);
        ps.setString(11, quote(crsName));
        ps.setString(12, quote(crsName + " (created by geotools) "));
        ps.registerOutParameter(13, Types.INTEGER);
        ps.registerOutParameter(14, Types.CHAR);
        ps.executeUpdate();
        ps.close();
    }

    public static void executeDropSRS(String srsName, Connection con) throws SQLException {
        String stmt = "call db2gse.ST_drop_srs(?,?,?)";

        String s = "{" + stmt + "}";
        CallableStatement ps = con.prepareCall(s);
        ps.setString(1, quote(srsName));
        ps.registerOutParameter(2, Types.INTEGER);
        ps.registerOutParameter(3, Types.CHAR);
        ps.executeUpdate();
        ps.close();
    }

    private static String quote(String s) {
        return "\"" + s + "\"";
    }
}
