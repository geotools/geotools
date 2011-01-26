/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sql;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RsMd2DbMdResultSet implements ResultSet {
	private static final int TABLE_CAT = 1; //String => table catalog (may be null) 
	private static final int TABLE_SCHEM = 2; // String => table schema (may be null) 
	private static final int TABLE_NAME = 3; // String => table name 
	private static final int COLUMN_NAME = 4; // String => column name 
	private static final int DATA_TYPE = 5; // int => SQL type from java.sql.Types 
	private static final int TYPE_NAME = 6; // String => Data source dependent type name, for a UDT the type name is fully qualified 
	private static final int COLUMN_SIZE = 7; // int => column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision. 
	private static final int BUFFER_LENGTH = 8; // is not used. 
	private static final int DECIMAL_DIGITS = 9; // int => the number of fractional digits 
	private static final int NUM_PREC_RADIX = 10; // int => Radix (typically either 10 or 2) 
	private static final int NULLABLE = 11; // int => is NULL allowed. 
	 //columnNoNulls - might not allow NULL values 
	 //columnNullable - definitely allows NULL values 
	 //columnNullableUnknown - nullability unknown 
	 private static final int REMARKS  = 12; //String => comment describing column (may be null) 
	 private static final int COLUMN_DEF  = 13; //String => default value (may be null) 
	private static final int SQL_DATA_TYPE  = 14; //int => unused 
	private static final int SQL_DATETIME_SUB  = 15; //int => unused 
	private static final int CHAR_OCTET_LENGTH  = 16; //int => for char types the maximum number of bytes in the column 
	private static final int ORDINAL_POSITION  = 17; //int => index of column in table (starting at 1) 
	private static final int IS_NULLABLE  = 18; //String => "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows. 
	private static final int SCOPE_CATLOG  = 19; //String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) 
	private static final int SCOPE_SCHEMA  = 20; //String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) 
	private static final int SCOPE_TABLE  = 21; //String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF) 
	private static final int SOURCE_DATA_TYPE  = 22; //short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
			
	
	private List contents;
	private Object[] currentRow;
	private Iterator iterator;
	
	public RsMd2DbMdResultSet(ResultSetMetaData rsmd)throws SQLException {
		contents = new LinkedList();
		final int colCount = rsmd.getColumnCount();
		for(int i = 1; i <= colCount; i++){
			Object[] row = new Object[23]; //index 0 not used
			row[TABLE_CAT] = rsmd.getCatalogName(i);
			row[TABLE_SCHEM] = rsmd.getSchemaName(i);
			row[TABLE_NAME] = rsmd.getTableName(i);
			row[COLUMN_NAME] = rsmd.getColumnName(i);
			row[DATA_TYPE] = new Integer(rsmd.getColumnType(i));
			row[TYPE_NAME] = rsmd.getColumnTypeName(i);
			row[COLUMN_SIZE] = new Integer(rsmd.getColumnDisplaySize(i));
			row[DECIMAL_DIGITS] = new Integer( rsmd.getScale(i));
			row[NULLABLE] = new Integer(rsmd.isNullable(i));
			row[REMARKS] = rsmd.getColumnLabel(i);
			
			contents.add(row);
		}
		this.iterator = contents.iterator();
	}

    public String getString(int columnIndex) throws SQLException{
    	Object val = this.currentRow[columnIndex];
    	String retVal = val == null? null : String.valueOf(val);
    	return retVal;
    }

    public int getInt(int columnIndex) throws SQLException{
    	Object val = this.currentRow[columnIndex];
    	int  intVal = ((Number)val).intValue();
    	return intVal;
    }

    public boolean next() throws SQLException{
    	boolean hasNext = this.iterator.hasNext();
    	if(hasNext){
    		this.currentRow = (Object[])this.iterator.next();
    	}
    	return hasNext;
    }


    /**
     * Releases this <code>ResultSet</code> object's database and
     * JDBC resources immediately instead of waiting for
     * this to happen when it is automatically closed.
     *
     * <P><B>Note:</B> A <code>ResultSet</code> object
     * is automatically closed by the
     * <code>Statement</code> object that generated it when
     * that <code>Statement</code> object is closed,
     * re-executed, or is used to retrieve the next result from a
     * sequence of multiple results. A <code>ResultSet</code> object
     * is also automatically closed when it is garbage collected.  
     *
     * @exception SQLException if a database access error occurs
     */
    public void close() throws SQLException{
    	this.currentRow = null;
    	this.iterator = null;
    	this.contents = null;
    }


    ////////////////////////////////////////////////////////////////
    //  the rest of methods throw unsupported operation exception //
    ////////////////////////////////////////////////////////////////
    public boolean wasNull() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    
    public boolean getBoolean(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }

    public byte getByte(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public short getShort(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public long getLong(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public float getFloat(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public double getDouble(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public byte[] getBytes(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Date getDate(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Time getTime(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Timestamp getTimestamp(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.InputStream getAsciiStream(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.InputStream getBinaryStream(int columnIndex)
        throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public String getString(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean getBoolean(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public byte getByte(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public short getShort(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public int getInt(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public long getLong(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public float getFloat(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public double getDouble(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public byte[] getBytes(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Date getDate(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Time getTime(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Timestamp getTimestamp(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.InputStream getAsciiStream(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.InputStream getUnicodeStream(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.InputStream getBinaryStream(String columnName)
        throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public SQLWarning getWarnings() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void clearWarnings() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public String getCursorName() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public ResultSetMetaData getMetaData() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Object getObject(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Object getObject(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public int findColumn(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.Reader getCharacterStream(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.io.Reader getCharacterStream(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public BigDecimal getBigDecimal(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean isBeforeFirst() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean isAfterLast() throws SQLException{
    	throw new UnsupportedOperationException();
    }    
    public boolean isFirst() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean isLast() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void beforeFirst() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void afterLast() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean first() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean last() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public int getRow() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean absolute( int row ) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean relative( int rows ) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean previous() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void setFetchDirection(int direction) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public int getFetchDirection() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void setFetchSize(int rows) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public int getFetchSize() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public int getType() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public int getConcurrency() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean rowUpdated() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean rowInserted() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public boolean rowDeleted() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateNull(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }  
    public void updateBoolean(int columnIndex, boolean x) throws SQLException{
    	throw new UnsupportedOperationException();
    }    
    public void updateByte(int columnIndex, byte x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateShort(int columnIndex, short x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateInt(int columnIndex, int x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateLong(int columnIndex, long x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateFloat(int columnIndex, float x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateDouble(int columnIndex, double x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateString(int columnIndex, String x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBytes(int columnIndex, byte x[]) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateDate(int columnIndex, java.sql.Date x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateTime(int columnIndex, java.sql.Time x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateTimestamp(int columnIndex, java.sql.Timestamp x)
      throws SQLException {
    	throw new UnsupportedOperationException();
    }
    public void updateAsciiStream(int columnIndex, 
			   java.io.InputStream x, 
			   int length) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBinaryStream(int columnIndex, 
			    java.io.InputStream x,
			    int length) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateCharacterStream(int columnIndex,
			     java.io.Reader x,
			     int length) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateObject(int columnIndex, Object x, int scale)
      throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateObject(int columnIndex, Object x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateNull(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }  
    public void updateBoolean(String columnName, boolean x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateByte(String columnName, byte x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateShort(String columnName, short x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateInt(String columnName, int x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateLong(String columnName, long x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateFloat(String columnName, float x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateDouble(String columnName, double x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateString(String columnName, String x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBytes(String columnName, byte x[]) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateDate(String columnName, java.sql.Date x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateTime(String columnName, java.sql.Time x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateTimestamp(String columnName, java.sql.Timestamp x)
      throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateAsciiStream(String columnName, 
			   java.io.InputStream x, 
			   int length) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBinaryStream(String columnName, 
			    java.io.InputStream x,
			    int length) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateCharacterStream(String columnName,
			     java.io.Reader reader,
			     int length) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateObject(String columnName, Object x, int scale)
      throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateObject(String columnName, Object x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void insertRow() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateRow() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void deleteRow() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void refreshRow() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void cancelRowUpdates() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void moveToInsertRow() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void moveToCurrentRow() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Statement getStatement() throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Object getObject(int i, java.util.Map map)
	throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Ref getRef(int i) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Blob getBlob(int i) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Clob getClob(int i) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Array getArray(int i) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Object getObject(String colName, java.util.Map map)
	throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Ref getRef(String colName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Blob getBlob(String colName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Clob getClob(String colName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public Array getArray(String colName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Date getDate(int columnIndex, Calendar cal) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Date getDate(String columnName, Calendar cal) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Time getTime(int columnIndex, Calendar cal) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Time getTime(String columnName, Calendar cal) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Timestamp getTimestamp(int columnIndex, Calendar cal) 
      throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.sql.Timestamp getTimestamp(String columnName, Calendar cal)	
      throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.net.URL getURL(int columnIndex) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public java.net.URL getURL(String columnName) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateRef(int columnIndex, java.sql.Ref x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateRef(String columnName, java.sql.Ref x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBlob(int columnIndex, java.sql.Blob x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateBlob(String columnName, java.sql.Blob x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateClob(int columnIndex, java.sql.Clob x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateClob(String columnName, java.sql.Clob x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateArray(int columnIndex, java.sql.Array x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
    public void updateArray(String columnName, java.sql.Array x) throws SQLException{
    	throw new UnsupportedOperationException();
    }
}
