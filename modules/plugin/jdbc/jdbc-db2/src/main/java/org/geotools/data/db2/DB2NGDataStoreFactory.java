/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.db2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.Parameter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.ByteOrderDataInStream;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBConstants;

/**
 * DataStoreFactory for DB2 database.
 *
 * @author Christian Mueller
 */
// temporary work around, the factory parameters map will be fixed separately
public class DB2NGDataStoreFactory extends JDBCDataStoreFactory {

    public static String GetCurrentSchema = "select current sqlid from sysibm.sysdummy1";
    public static String GetWKBZTypes =
            "select db2gse.st_asbinary(db2gse.st_point(1,2,3,0)) from sysibm.sysdummy1";
    public static String SelectGeometryColumns =
            "select * from db2gse.st_geometry_columns where 0 = 1";

    /** parameter for database type */
    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "db2",
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    /** enables using EnvelopesIntersect in bbox queries */
    public static final Param LOOSEBBOX =
            new Param(
                    "Loose bbox",
                    Boolean.class,
                    "Perform only primary filter on bbox",
                    false,
                    Boolean.TRUE);

    /** use selectivity clause for spatial predicates */
    public static final Param USE_SELECTIVITY =
            new Param(
                    "Use selectivity clause",
                    Boolean.class,
                    "Use selectivity clause for spatial queries",
                    false,
                    Boolean.TRUE);

    public static final String DriverClassName = "com.ibm.db2.jcc.DB2Driver";

    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return new DB2SQLDialectPrepared(dataStore, new DB2DialectInfo());
    }

    @Override
    public String getDisplayName() {
        return "DB2 NG";
    }

    @Override
    protected String getDriverClassName() {
        return DriverClassName;
    }

    @Override
    protected String getDatabaseID() {
        return (String) DBTYPE.sample;
    }

    @Override
    public String getDescription() {
        return "DB2 Database";
    }

    @Override
    protected String getValidationQuery() {
        return "select current date from sysibm.sysdummy1";
    }

    @Override
    protected boolean checkDBType(Map<String, ?> params) {
        if (super.checkDBType(params)) {
            return true;
        }

        // check also for "DB2" which is iold db type, but only when the old
        // factory is not on the classpath
        if (checkDBType(params, "DB2")) {
            try {
                Class.forName("org.geotools.data.db2.DB2DataStoreFactory");

                // old factory is around, let it handle the connection
                return false;
            } catch (ClassNotFoundException e) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected String getJDBCUrl(Map<String, ?> params) throws IOException {
        // jdbc url
        String host = null;
        Integer port = null;
        try {
            host = (String) HOST.lookUp(params);
            port = (Integer) PORT.lookUp(params);
        } catch (IOException ex) {
            // do nothing
        }

        String db = (String) DATABASE.lookUp(params);

        if (host == null && port == null && db != null) return "jdbc:" + getDatabaseID() + ":" + db;

        return super.getJDBCUrl(params);
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);
        parameters.put(DBTYPE.key, DBTYPE);
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(USE_SELECTIVITY.key, USE_SELECTIVITY);
    }

    @Override
    @SuppressWarnings("PMD.CheckResultSet")
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params)
            throws IOException {
        Connection con = null;
        try {
            con = dataStore.getDataSource().getConnection();
            DB2DialectInfo di =
                    ((DB2SQLDialectPrepared) dataStore.getSQLDialect()).getDb2DialectInfo();

            DB2SQLDialectPrepared dialect = (DB2SQLDialectPrepared) dataStore.getSQLDialect();
            Boolean loose = (Boolean) LOOSEBBOX.lookUp(params);
            dialect.setLooseBBOXEnabled(loose == null || Boolean.TRUE.equals(loose));

            Boolean selectivity = (Boolean) USE_SELECTIVITY.lookUp(params);
            dialect.setUseSelectivity(Boolean.TRUE.equals(selectivity));

            DatabaseMetaData md = con.getMetaData();
            di.setMajorVersion(md.getDatabaseMajorVersion());
            di.setMinorVersion(md.getDatabaseMinorVersion());
            di.setProductName(md.getDatabaseProductName());
            di.setProductVersion(md.getDatabaseProductVersion());

            try (PreparedStatement ps = con.prepareStatement(SelectGeometryColumns);
                    ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData rsmd = ps.getMetaData();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    if ("MIN_X".equals(rsmd.getColumnName(i + 1))) {
                        di.setSupportingPrecalculatedExtents(true);
                        break;
                    }
                }
            }

            if (dataStore.getDatabaseSchema() == null) {
                try (PreparedStatement ps = con.prepareStatement(GetCurrentSchema);
                        ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    dataStore.setDatabaseSchema(rs.getString(1));
                }
            }
            try (PreparedStatement ps = con.prepareStatement(GetWKBZTypes);
                    ResultSet rs = ps.executeQuery()) {
                rs.next();
                byte[] bytes = rs.getBytes(1);
                ByteOrderDataInStream dis = new ByteOrderDataInStream();
                dis.setInStream(new ByteArrayInStream(bytes));
                byte byteOrder = dis.readByte();
                // default is big endian
                if (byteOrder == WKBConstants.wkbNDR) dis.setOrder(ByteOrderValues.LITTLE_ENDIAN);

                int geometryType = dis.readInt();
                if (geometryType == 1001) di.setHasOGCWkbZTyps(true);
            }
        } catch (ParseException | SQLException e) {
            throw new IOException(e.getMessage());
        } finally {
            dataStore.closeSafe(con);
        }

        return dataStore;
    }
}
