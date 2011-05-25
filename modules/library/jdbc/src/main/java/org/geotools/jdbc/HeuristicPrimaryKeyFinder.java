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
package org.geotools.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

/**
 * Looks up the primary key using the {@link DatabaseMetaData} for the specified table, looking both
 * for primary keys and unique indexes. The sequence lookup is performed in conjuction with the sql
 * dialect
 * 
 * @author Andrea Aime - OpenGeo
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/jdbc/src/main/java/org/geotools/jdbc/HeuristicPrimaryKeyFinder.java $
 */
public class HeuristicPrimaryKeyFinder extends PrimaryKeyFinder {
    protected static final Logger LOGGER = Logging.getLogger(HeuristicPrimaryKeyFinder.class);

    @Override
    public PrimaryKey getPrimaryKey(JDBCDataStore store, String databaseSchema, String tableName,
            Connection cx) throws SQLException {
        DatabaseMetaData metaData = cx.getMetaData();
        LOGGER.log(Level.FINE, "Getting information about primary keys of {0}", tableName);
        ResultSet primaryKey = metaData.getPrimaryKeys(null, databaseSchema, tableName);

        try {
            /*
             * <LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
             * <LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
             * <LI><B>TABLE_NAME</B> String => table name <LI><B>COLUMN_NAME</B> String => column
             * name <LI><B>KEY_SEQ</B> short => sequence number within primary key
             * <LI><B>PK_NAME</B> String => primary key name (may be <code>null</code>)
             */
            PrimaryKey pkey = createPrimaryKey(store, primaryKey, metaData, databaseSchema,
                    tableName, cx);
            if (pkey == null) {
                // No known database supports unique indexes on views and this check
                // causes problems with Oracle, so we skip it
                if (!store.isView(metaData, databaseSchema, tableName)
                        && store.getVirtualTables().get(tableName) == null) {
                    // no primary key, check for a unique index
                    LOGGER.log(Level.FINE, "Getting information about unique indexes of {0}",
                            tableName);
                    ResultSet uniqueIndex = metaData.getIndexInfo(null, databaseSchema, tableName,
                            true, true);
                    try {
                        pkey = createPrimaryKey(store, uniqueIndex, metaData, databaseSchema,
                                tableName, cx);
                    } finally {
                        store.closeSafe(uniqueIndex);
                    }
                }
            }

            return pkey;
        } finally {
            store.closeSafe(primaryKey);
        }
    }

    /*
     * Creates a key from a primary key or unique index.
     */
    PrimaryKey createPrimaryKey(JDBCDataStore store, ResultSet index, DatabaseMetaData metaData,
            String databaseSchema, String tableName, Connection cx) throws SQLException {
        ArrayList<PrimaryKeyColumn> cols = new ArrayList();

        while (index.next()) {
            String columnName = index.getString("COLUMN_NAME");
            // work around. For some reason the first record returned is always 'empty'
            // this was tested on Oracle and Postgres databases
            if (columnName == null) {
                continue;
            }

            // look up the type ( should only be one row )
            ResultSet columns = metaData.getColumns(null, databaseSchema, tableName, columnName);
            columns.next();

            int binding = columns.getInt("DATA_TYPE");
            Class columnType = store.getMapping(binding);

            if (columnType == null) {
                LOGGER.warning("No class for sql type " + binding);
                columnType = Object.class;
            }

            // determine which type of primary key we have
            PrimaryKeyColumn col = null;

            // 1. Auto Incrementing?
            Statement st = cx.createStatement();

            try {
                // not actually going to get data
                st.setFetchSize(1);

                StringBuffer sql = new StringBuffer();
                sql.append("SELECT ");
                store.getSQLDialect().encodeColumnName(columnName, sql);
                sql.append(" FROM ");
                store.encodeTableName(tableName, sql, null);

                sql.append(" WHERE 0=1");

                LOGGER.log(Level.FINE, "Grabbing table pk metadata: {0}", sql);

                ResultSet rs = st.executeQuery(sql.toString());

                try {
                    if (rs.getMetaData().isAutoIncrement(1)) {
                        col = new AutoGeneratedPrimaryKeyColumn(columnName, columnType);
                    }
                } finally {
                    store.closeSafe(rs);
                }
            } finally {
                store.closeSafe(st);
            }

            // 2. Has a sequence?
            if (col == null) {
                try {
                    String sequenceName = store.getSQLDialect().getSequenceForColumn(
                            databaseSchema, tableName, columnName, cx);
                    if (sequenceName != null) {
                        col = new SequencedPrimaryKeyColumn(columnName, columnType, sequenceName);
                    }
                } catch (Exception e) {
                    // log the exception , and continue on
                    LOGGER.log(Level.WARNING, "Error occured determining sequence for "
                            + columnName + ", " + tableName, e);
                }
            }

            if (col == null) {
                col = new NonIncrementingPrimaryKeyColumn(columnName, columnType);
            }

            cols.add(col);
        }

        if (!cols.isEmpty()) {
            return new PrimaryKey(tableName, cols);
        }
        return null;
    }

}
