/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis.fidmapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.postgis.PostgisSQLBuilder;
import org.geotools.filter.SQLEncoderPostgis;


/**
 * Postgis specific FIDMapperFactory that uses the {@link org.geotools.data.postgis.fidmapper.OIDFidMapper OIDFidMapper}
 * to map tables with no primary keys or tables that have weird primary keys that cannot be mapped
 * in other ways.
 *
 * @author Andrea Aime
 *
 *
 * @source $URL$
 */
public class PostgisFIDMapperFactory extends DefaultFIDMapperFactory {
    JDBCDataStoreConfig config;

    public PostgisFIDMapperFactory(JDBCDataStoreConfig config) {
        this.config = config;
        //setReturningTypedFIDMapper( false );
    }

    protected FIDMapper buildNoPKMapper(String schema, String tableName,
        Connection connection) {
        //oid's supported before version 7
        if (getDatabaseMajorVersion(connection) <= 7) {
            return new OIDFidMapper();
        }

        //table could also have been created with OID flag on
        PostgisSQLBuilder sqlb = new PostgisSQLBuilder(new SQLEncoderPostgis(),
                config);
        String sql = "SELECT " + sqlb.encodeColumnName("oid") + " FROM "
            + sqlb.encodeTableName(tableName) + " LIMIT 1";
        Statement st = null;

        try {
            st = connection.createStatement();
            st.execute(sql);

            //if we get here oid is supported
            return new OIDFidMapper();
        } catch (SQLException e) {
            //ignore, fall back to parent
        } finally {
            try {
                st.close();
            } catch (SQLException ignore) {
            }
        }

        return super.buildNoPKMapper(schema, tableName, connection);
    }

    /**
     * Retrieves Postgresql database major version number. This is used to see
     * if OID are there or not.
     *
     * @param connection
     */
    private int getDatabaseMajorVersion(Connection connection) {
        int major;

        try {
            major = connection.getMetaData().getDatabaseMajorVersion();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,
                "Failed to retrieve Postgres "
                + "database version number, assuming 7. Error is: "
                + e.getMessage(), e);
            major = 7;
        }

        return major;
    }

    protected FIDMapper buildLastResortFidMapper(String schema,
        String tableName, Connection connection, ColumnInfo[] colInfos) {
        if (getDatabaseMajorVersion(connection) > 7) {
            throw new IllegalArgumentException(
                "Tables for postgis 8+ must have a primary key defined");
        }

        return new OIDFidMapper();
    }

    protected FIDMapper buildSingleColumnFidMapper(String schema,
        String tableName, Connection connection, ColumnInfo ci) {
        if (ci.isAutoIncrement()) {
            return new PostGISAutoIncrementFIDMapper(schema, tableName,
                ci.getColName(), ci.getDataType());
        } else if("uuid".equals(ci.getTypeName())) {
            return new UUIDFIDMapper(ci.getColName(), ci.dataType);
        }

        return super.buildSingleColumnFidMapper(schema, tableName, connection, ci);
    }

    /**
     *  see@DefaultFIDMapperFactory in main module (jdbc)
     *   This version pre-double quotes the column name and table name and passes it to the superclass's version.
     */
    protected boolean isAutoIncrement(String catalog, String schema,
        String tableName, Connection conn, ResultSet tableInfo,
        String columnName, int dataType) throws SQLException {
        String schemaName = null;

        if (schema != null) {
            schemaName = "\"" + schema + "\"";
        }

        return super.isAutoIncrement(catalog, schemaName,
            "\"" + tableName + "\"", conn, tableInfo, "\"" + columnName + "\"",
            dataType);
    }

    //
    //    /**
    //     * @see org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory#getMapper(org.geotools.feature.FeatureType)
    //     */
    //    public FIDMapper getMapper( FeatureType featureType ) {
    //        return new TypedFIDMapper(new OIDFidMapper(), featureType.getTypeName());
    //    }
    //    
    //    /**
    //     * @see org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory#getMapper(java.lang.String, java.lang.String, java.lang.String, java.sql.Connection)
    //     */
    //    public FIDMapper getMapper( String catalog, String schema, String tableName,
    //            Connection connection ) throws IOException {
    //        return new TypedFIDMapper(new OIDFidMapper(), tableName);
    //    }
}
