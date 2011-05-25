/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
/*
 * 26-may-2005 D. Adler Added constructor with returnFIDColumnsAsAttributes.
 *                      Added accessors for ColumnInfo
 * 12-jul-2006 D. Adler GEOT-728 Refactor FIDMapper classes
 */
package org.geotools.data.jdbc.fidmapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * Default FID mapper that works with default FID mappers.
 * 
 * <p>
 * May also be used a base class for more specific and feature rich factories
 * </p>
 *
 * @author Andrea Aime
 *
 * @source $URL$
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class DefaultFIDMapperFactory implements FIDMapperFactory {
    /** The logger for the filter module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.jdbc");
    protected boolean returningTypedFIDMapper = true;

    /** Set if table FID columns are to be returned as business attributes. */
    protected boolean returnFIDColumnsAsAttributes = false;

    /**
     * Constructs a DefaultFIDMapperFactory which will not return FID columns
     * as business attributes.
     */
    public DefaultFIDMapperFactory() {
    }

    /**
     * Constructs a DefaultFIDMapperFactory with user specification of whether
     * to return FID columns as business attributes.
     *
     * @param returnFIDColumnsAsAttributes true if FID columns should be
     *        returned as business  attributes.
     */
    public DefaultFIDMapperFactory(boolean returnFIDColumnsAsAttributes) {
        this.returnFIDColumnsAsAttributes = returnFIDColumnsAsAttributes;
    }

    /**
     * Setter for the flag controlling wther a "typed" fid mapper is returned.
     * @param returningTypedFIDMapper
     */
    public void setReturningTypedFIDMapper(boolean returningTypedFIDMapper) {
		this.returningTypedFIDMapper = returningTypedFIDMapper;
	}
    
    /**
     * Getter for the flog controll wether a "typed" fid mapper should be returned.
     */
    public boolean isReturningTypedFIDMapper() {
		return returningTypedFIDMapper;
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
        ColumnInfo[] colInfos = getPkColumnInfo(catalog, schema, tableName,
                connection);
        FIDMapper mapper = null;

        if (colInfos.length == 0) {
            mapper = buildNoPKMapper(schema, tableName, connection);
        } else if (colInfos.length > 1) {
            mapper = buildMultiColumnFIDMapper(schema, tableName, connection,
                    colInfos);
        } else {
            ColumnInfo ci = colInfos[0];

            mapper = buildSingleColumnFidMapper(schema, tableName, connection,
                    ci);
        }

        if (mapper == null) {
            mapper = buildLastResortFidMapper(schema, tableName, connection,
                    colInfos);

            if (mapper == null) {
                String msg = "Cannot map primary key to a FID mapper, primary key columns are:\n"
                    + getColumnInfoList(colInfos); 
                LOGGER.log(Level.SEVERE, msg);
                throw new IOException(msg);
            }
        }

        if (returningTypedFIDMapper && (mapper != null)) {
            return new TypedFIDMapper(mapper, tableName);
        } else {
            return mapper;
        }
    }

    /**
     * Retuns a List of column infos, nice for logging the column infos
     * leveraging the complete toString() method provided by lists
     *
     * @param colInfos
     *
     */
    protected List getColumnInfoList(ColumnInfo[] colInfos) {
        ArrayList list = new ArrayList();

        for (int i = 0; i < colInfos.length; i++) {
            list.add(colInfos[i]);
        }

        return list;
    }

    /**
     * Builds a FidMapper when every other tentative of building one fails.
     * This method is used as a last resort fall back, use it if you can
     * provide a FIDMapper that works on every kind of table, but it's usually
     * suboptimal. The default behaviour is to return no FID mapper at all.
     *
     * @param schema
     * @param tableName
     * @param connection
     * @param colInfos
     *
     */
    protected FIDMapper buildLastResortFidMapper(String schema,
        String tableName, Connection connection, ColumnInfo[] colInfos) {
        return null;
    }

    /**
     * Builds a FID mapper based on a single column primary key. Default
     * version tries the auto-increment way, then a mapping on an {@link
     * MaxIncFIDMapper} type for numeric columns, and a plain {@link
     * BasicFIDMapper} of text based columns.
     *
     * @param schema
     * @param tableName
     * @param connection an open database connection.
     * @param ci the column information for the FID column.
     *
     * @return the appropriate FIDMapper.
     */
    protected FIDMapper buildSingleColumnFidMapper(String schema,
        String tableName, Connection connection, ColumnInfo ci) {
        if (ci.autoIncrement) {
            return new AutoIncrementFIDMapper(schema, tableName, ci.colName, ci.dataType);
        } else if (isIntegralType(ci.dataType)) {
            return new MaxIncFIDMapper(schema, tableName, ci.colName, ci.dataType,
                this.returnFIDColumnsAsAttributes);
        } else {
            return new BasicFIDMapper(ci.colName, ci.size,
                this.returnFIDColumnsAsAttributes);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param schema
     * @param tableName
     * @param connection
     *
     */
    protected FIDMapper buildNoPKMapper(String schema, String tableName,
        Connection connection) {
        FIDMapper mapper;
        mapper = new NullFIDMapper();

        return mapper;
    }

    /**
     * Builds a FID mapper for multi column public columns
     *
     * @param schema
     * @param tableName
     * @param connection
     * @param colInfos
     *
     */
    protected FIDMapper buildMultiColumnFIDMapper(String schema,
        String tableName, Connection connection, ColumnInfo[] colInfos) {
        String[] colNames = new String[colInfos.length];
        int[] colTypes = new int[colInfos.length];
        int[] colSizes = new int[colInfos.length];
        int[] colDecimalDigits = new int[colInfos.length];
        boolean[] autoIncrement = new boolean[colInfos.length];

        for (int i = 0; i < colInfos.length; i++) {
            ColumnInfo ci = colInfos[i];
            colNames[i] = ci.colName;
            colTypes[i] = ci.dataType;
            colSizes[i] = ci.size;
            colDecimalDigits[i] = ci.decimalDigits;
            autoIncrement[i] = ci.autoIncrement;
        }

        return new MultiColumnFIDMapper(schema, tableName, colNames, colTypes, colSizes,
            colDecimalDigits, autoIncrement);
    }

    protected ColumnInfo[] getPkColumnInfo(String catalog, String schema,
        String typeName, Connection conn)
        throws SchemaNotFoundException, DataSourceException {
        ResultSet tableInfo = null;
        ResultSet pkInfo = null;
        boolean pkMetadataFound = false;

        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();

            Map pkMap = new LinkedHashMap();
            pkInfo = dbMetaData.getPrimaryKeys(catalog, schema, typeName);
            pkMetadataFound = true;

            while (pkInfo.next()) {
                ColumnInfo ci = new ColumnInfo();
                ci.colName = pkInfo.getString("COLUMN_NAME");
                ci.keySeq = pkInfo.getInt("KEY_SEQ");
                pkMap.put(ci.colName, ci);
            }

            tableInfo = dbMetaData.getColumns(catalog, schema, typeName, "%");

            boolean tableInfoFound = false;

            while (tableInfo.next()) {
                tableInfoFound = true;

                String columnName = tableInfo.getString("COLUMN_NAME");
                ColumnInfo ci = (ColumnInfo) pkMap.get(columnName);

                if (ci != null) {
                    ci.dataType = tableInfo.getInt("DATA_TYPE");
                    ci.typeName = tableInfo.getString("TYPE_NAME");
                    ci.size = tableInfo.getInt("COLUMN_SIZE");
                    ci.decimalDigits = tableInfo.getInt("DECIMAL_DIGITS");
                    ci.autoIncrement = isAutoIncrement(catalog, schema,
                            typeName, conn, tableInfo, columnName, ci.dataType);
                }
            }

            if (!tableInfoFound) {
                throw new SchemaNotFoundException(typeName);
            }

            Collection columnInfos = pkMap.values();

            return (ColumnInfo[]) columnInfos.toArray(new ColumnInfo[columnInfos
                .size()]);
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
            conn = null; // prevent finally block from reclosing

            if (pkMetadataFound) {
                throw new DataSourceException(
                    "SQL Error building FeatureType for " + typeName + " "
                    + sqlException.getMessage(), sqlException);
            } else {
                throw new SchemaNotFoundException(typeName, sqlException);
            }
        } finally {
            JDBCUtils.close(tableInfo);
        }
    }

    /**
     * Returns true if the specified column is auto-increment. This method is
     * left protected so that specific datastore implementations can put their
     * own logic, should the default one be ineffective or have bad
     * performance.  NOTE: the postgis subclass will call this with the
     * columnname and table name pre-double-quoted! Other DB may have to do
     * the same - please check your DB's documentation.
     *
     * @param catalog
     * @param schema
     * @param tableName
     * @param conn
     * @param tableInfo
     * @param columnName
     * @param dataType
     *
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

        // ok, it's an integer. To know if it's an auto-increment let's have a look at resultset metadata
        // and try to force it to get just a single row for exploring the metadata
        boolean autoIncrement = false;
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.createStatement();
            statement.setFetchSize(1);
            String query = "SELECT " + columnName + " FROM ";
            if (schema != null) {
                query = query + schema + "."; //the schema will default to public if not specified
            }
            query = query + tableName + " WHERE 0=1"; //DJB: the "where 0=1" will optimize if you have a lot of dead tuples 
            rs = statement.executeQuery(query);

            // if the WHERE 0=1 give any data store problems, just remove it 
            // and put a comment here as to why it caused problems.
            java.sql.ResultSetMetaData rsInfo = rs.getMetaData();
            autoIncrement = rsInfo.isAutoIncrement(1);
        } finally {
            JDBCUtils.close(statement);
            JDBCUtils.close(rs);
        }

        return autoIncrement;
    }

    /**
     * Returns true if the dataType for the column can serveget as a primary key.
     * Note that this now returns true for a DECIMAL type, because oracle
     * Numbers are returned in jdbc as DECIMAL.  This may cause errors in very
     * rare cases somewhere down the line, but only if users do something
     * incredibly silly like defining a primary key with a double.
     *
     * @param dataType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isIntegralType(int dataType) {
        return (dataType == Types.BIGINT) || (dataType == Types.INTEGER)
        || (dataType == Types.NUMERIC) || (dataType == Types.SMALLINT)
        || (dataType == Types.TINYINT) || (dataType == Types.DECIMAL);
    }

    protected boolean isTextType(int dataType) {
        return (dataType == Types.VARCHAR) || (dataType == Types.CHAR)
        || (dataType == Types.CLOB);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapperFactory#getMapper(org.geotools.feature.FeatureType)
     */
    public FIDMapper getMapper(SimpleFeatureType featureType) {
        return new BasicFIDMapper("ID", 255, false);
    }

    /**
     * Simple class used as a struct to hold column informations used in this
     * factory
     *
     * @author Andrea Aime
     */
    protected class ColumnInfo implements Comparable {
        public String colName;
        public int dataType;
        public String typeName;
        public int size;
        public int decimalDigits;
        public boolean autoIncrement;
        public int keySeq;

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Object o) {
            return keySeq - ((ColumnInfo) o).keySeq;
        }

        public String toString() {
            return "ColumnInfo, name(" + colName + "), type(" + dataType
            + ") size(" + size + ") decimalDigits(" + decimalDigits
            + ") autoIncrement(" + autoIncrement + ")";
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the autoIncrement.
         */
        public boolean isAutoIncrement() {
            return autoIncrement;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the colName.
         */
        public String getColName() {
            return colName;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the dataType.
         */
        public int getDataType() {
            return dataType;
        }
        
        public String getTypeName() {
            return typeName;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the decimalDigits.
         */
        public int getDecimalDigits() {
            return decimalDigits;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the size.
         */
        public int getSize() {
            return size;
        }
    }
}
