/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.MaxIncFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;


/**
 * Overrides DefaultFIDMapperFactory methods for DB2-specific handling.
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2FIDMapperFactory extends DefaultFIDMapperFactory {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.db2");
    private String databaseSchemaName = null;

    /**
     * Default constructor will cause FID columns to be returned as business
     * attributes.
     */
    public DB2FIDMapperFactory(String databaseSchemaName) {
        super(true);
        this.databaseSchemaName = databaseSchemaName;
    }

    /**
     * Constructs a DB2FIDMapperFactory with user specification of whether to
     * return FID columns as business attributes.
     *
     * @param returnFIDColumnsAsAttributes true if FID columns should be
     *        returned as business attributes.
     */
    public DB2FIDMapperFactory(boolean returnFIDColumnsAsAttributes) {
        super(returnFIDColumnsAsAttributes);
    }

    /**
     * Gets the appropriate FIDMapper for the specified table.
     *
     * @param catalog
     * @param schema
     * @param tableName
     * @param connection the active database connection to get table key
     *        information
     *
     * @return the appropriate FIDMapper for the specified table.
     *
     * @throws IOException if any error occurs.
     */
    public FIDMapper getMapper(String catalog, String schema, String tableName,
        Connection connection) throws IOException {
        FIDMapper fm = super.getMapper(catalog, schema, tableName, connection);
        LOGGER.fine(toString(fm));

        return fm;
    }

    /**
     * Determine whether this column is autoincrement.  An open connection to
     * the database must be provided.
     *
     * @param catalog not used
     * @param schema not used
     * @param tableName the table name
     * @param conn an open database connection
     * @param tableInfo not used
     * @param columnName the FID column name
     * @param dataType not used
     *
     * @return true if this is an autoincrement column
     *
     * @throws SQLException
     */
    protected boolean isAutoIncrement(String catalog, String schema,
        String tableName, Connection conn, ResultSet tableInfo,
        String columnName, int dataType) throws SQLException {
        // if it's not an integer type it can't be an auto increment type
        if (!isIntegralType(dataType)) {
            return false;
        }

        // It seems like there should be a better way to get this directly from the catalog
        boolean autoIncrement = false;
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.createStatement();

            String stmtString = "SELECT IDENTITY, GENERATED FROM SYSCAT.COLUMNS "
                + "WHERE TABSCHEMA = '" + tableInfo.getString("TABLE_SCHEM")
                + "' " + "AND TABNAME = '" + tableName + "' "
                + "AND COLNAME = '" + columnName + "'";

            rs = statement.executeQuery(stmtString);

            if (rs.next()) {
                if (rs.getString(1).equals("Y") && rs.getString(2).equals("A")) {
                    autoIncrement = true;
                }
            }
        } finally {
            JDBCUtils.close(statement);
            JDBCUtils.close(rs);
        }

        return autoIncrement;
    }

    /**
     * Returns a DB2NullFIDMapper when there is no primary key.
     *
     * @param schema ignored.
     * @param tableName ignored.
     * @param connection ignored.
     *
     * @return a DB2NullFIDMapper.
     */
    protected FIDMapper buildNoPKMapper(String schema, String tableName,
        Connection connection) {
        FIDMapper mapper;
        mapper = new DB2NullFIDMapper(schema, tableName);

        return mapper;
    }

    /**
     * Builds a FID mapper based on a single column primary key. Default
     * version tries the auto-increment way, then a mapping on an {@link
     * MaxIncFIDMapper} type for numeric columns, and a plain {@link
     * BasicFIDMapper} of text based columns.
     *
     * @param schema
     * @param tableName
     * @param connection not used
     * @param ci the column information
     *
     * @return a FIDMapper
     */
    protected FIDMapper buildSingleColumnFidMapper(String schema,
        String tableName, Connection connection, ColumnInfo ci) {

              if (ci.isAutoIncrement()) {
                  return new DB2AutoIncrementFIDMapper(databaseSchemaName, tableName, ci.getColName(), ci.getDataType());
              } else if (isIntegralType(ci.getDataType())) {
                  return new MaxIncFIDMapper(databaseSchemaName, tableName, ci.getColName(), ci.getDataType(), true);
              } else {
                  return new BasicFIDMapper(databaseSchemaName, tableName, ci.getColName(), ci.getSize(), true);
              }
    }

    /**
     * Create a nice string representation of a FID Mapper
     *
     * @param fm the FID Mapper
     *
     * @return the String representation
     */
    String toString(FIDMapper fm) {
        String mapperName = ((TypedFIDMapper) fm).getWrappedMapper().getClass()
                             .toString();
        String colInfo = "";

        if (fm.getColumnCount() > 0) {
            colInfo = fm.getColumnName(0) + ":" + fm.getColumnType(0) + ":"
                + fm.getColumnSize(0) + ":" + fm.getColumnDecimalDigits(0);
        }

        String s = mapperName + ":" + fm.getColumnCount() + ":" + colInfo + ":"
            + fm.returnFIDColumnsAsAttributes() + ":"
            + fm.hasAutoIncrementColumns() + ":" + "";

        return s;
    }
}
