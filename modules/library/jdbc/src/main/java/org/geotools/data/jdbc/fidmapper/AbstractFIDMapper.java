/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 * 26-may-2005 D. Adler Move returnFIDColumnsAsAttributes here
 *                      from subclasses.
 * 17-Jul-2006 D. Adler GEOT-728 Refactor FIDMapper classes
 */
package org.geotools.data.jdbc.fidmapper;

/**
 * Abstract implementation providing common methods that usually are coded the
 * same way in all fid mappers.
 *
 * @author wolf
 *
 * @source $URL$
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public abstract class AbstractFIDMapper implements FIDMapper {
    protected boolean[] autoIncrement = new boolean[1];
    protected int[] colDecimalDigits = new int[1];
    protected String[] colNames = new String[1];
    protected int[] colSizes = new int[1];
    protected int[] colTypes = new int[1];
    protected String tableName = null;
    protected String tableSchemaName = null;
    /** Set if table FID columns are to be returned as business attributes. */
    protected boolean returnFIDColumnsAsAttributes = false;
    
    /** 
     * Default constructor for AbstractFIDMapper
     *
     */
    public AbstractFIDMapper() {
    	
    }
    
    /** 
     * Set common values for all FID mappers.
     * 
     * @param tableSchemaName
     * @param tableName
     */
    public AbstractFIDMapper(String tableSchemaName, String tableName) {
    	this.tableSchemaName = tableSchemaName;
    	this.tableName = tableName;
    }
    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnCount()
     */
    public int getColumnCount() {
    	if (colNames[0] == null) return 0;
        else return colNames.length;
    }
    
    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnCount()
     */    
    protected int getColumnDecimalDigits() {
    	return getColumnDecimalDigits(0);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnDecimalDigits(int)
     */
    public int getColumnDecimalDigits(int colIndex) {
        if (colIndex > 0 && colIndex < getColumnCount()) {
            return colDecimalDigits[colIndex];
        } else {
            return 0;
        }
    }
    
    protected String getColumnName() {
    	return getColumnName(0);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnName(int)
     */
    public String getColumnName(int colIndex) {
        if (colIndex >= 0 && colIndex < getColumnCount()) {
            return colNames[colIndex];
        } else {
            return null;
        }
    }
    protected int getColumnSize() {
    	return getColumnSize(0);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnSize(int)
     */
    public int getColumnSize(int colIndex) {
        if (colIndex >= 0 && colIndex < getColumnCount()) {
            return colSizes[colIndex];
        } else {
            return -1;
        }
    }
    protected int getColumnType() {
    	return getColumnType(0);
    }
    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnType(int)
     */
    public int getColumnType(int colIndex) {
        if (colIndex >= 0 && colIndex < getColumnCount()) {
            return colTypes[colIndex];
        } else {
            throw new ArrayIndexOutOfBoundsException(colIndex);
        }
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#hasAutoIncrementColumns()
     */
    public boolean hasAutoIncrementColumns() {
        for (int i = 0; i < getColumnCount(); i++) {
            if (isAutoIncrement(i)) {
                return true;
            }
        }
        return false;
    }
    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#initSupportStructures()
     */
    public void initSupportStructures() {
        // nothing to do
    }
    protected boolean isAutoIncrement() {
    	return isAutoIncrement(0);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#isAutoIncrement(int)
     */
    public boolean isAutoIncrement(int colIndex) {
        if (colIndex >= 0 && colIndex < getColumnCount()) {
            return autoIncrement[colIndex];
        } else {
            return true;
        }
    }

    /**
     * Convenience since most FID mappers should be persistent, override on the
     * specific ones that aren't.
     *
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#isVolatile()
     */
    public boolean isVolatile() {
        return false;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#returnFIDColumnsAsAttributes()
     */
    public boolean returnFIDColumnsAsAttributes() {
        return returnFIDColumnsAsAttributes;
    }

    protected void setInfo(String colName, int colType, 
    		int colSize, int colDecimalDigits, boolean autoIncrement) {
    	this.colNames[0] = colName;
    	this.colTypes[0] = colType;
    	this.colSizes[0] = colSize;
    	this.colDecimalDigits[0] = colDecimalDigits;
    	this.autoIncrement[0] = autoIncrement;	
    }
    /**
     * Create a nice string representation of a FID Mapper
     *
     *
     * @return the String representation
     */
    public String toString() {
        String mapperName = getClass().toString();
        String colInfo = "";

        if (getColumnCount() > 0) {
            colInfo = getColumnName(0) + ":" + getColumnType(0) + ":"
                + getColumnSize(0) + ":" + getColumnDecimalDigits(0);
        }

        String s = mapperName + ":" + getColumnCount() + ":" + colInfo + ":"
            + returnFIDColumnsAsAttributes() + ":"
            + hasAutoIncrementColumns() + ":" + "";

        return s;
    }
    public String getTableName() {
    	return tableName;
    }
    public String getTableSchemaName() {
    	return tableSchemaName;
    }
}
