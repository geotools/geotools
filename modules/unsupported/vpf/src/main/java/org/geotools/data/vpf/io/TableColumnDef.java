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
package org.geotools.data.vpf.io;

import org.geotools.data.vpf.ifc.DataTypesDefinition;
import org.geotools.data.vpf.util.DataUtils;


/**
 * This class contains definition of VPF standard table column definition
 * according to specification found in: "Interface Standard for Vector Product
 * Format." Objects of this type are immutable. Created: Thu Jan 02 23:11:27
 * 2003
 *
 * @author <a href="mailto:kobit@users.fs.net">Artur Hefczyc</a>
 *
 * @source $URL$
 * @version 1.0
 */
public class TableColumnDef implements DataTypesDefinition {
    /** Describe variable <code>name</code> here. */
    private String name = null;

    /** Describe variable <code>type</code> here. */
    private char type = CHAR_NULL_VALUE;

    /** Describe variable <code>elementsNumber</code> here. */
    private int elementsNumber = 0;

    /** Describe variable <code>keyType</code> here. */
    private char keyType = CHAR_NULL_VALUE;

    /** Describe variable <code>colDesc</code> here. */
    private String colDesc = null;

    /** Describe variable <code>valDescTableName</code> here. */
    private String valDescTableName = null;

    /** Describe variable <code>thematicIdx</code> here. */
    private String thematicIdx = null;

    /** Describe variable <code>narrTable</code> here. */
    private String narrTable = null;

    /**
     * Creates a new <code>TableColumnDef</code> instance.
     *
     * @param name a <code>String</code> value
     * @param type a <code>char</code> value
     * @param elementsNumber an <code>int</code> value
     * @param keyType a <code>char</code> value
     * @param colDesc a <code>String</code> value
     * @param valDescTableName a <code>String</code> value
     * @param thematicIdx a <code>String</code> value
     * @param narrTable a <code>String</code> value
     */
    public TableColumnDef(String name, char type, int elementsNumber, 
                          char keyType, String colDesc, String valDescTableName, 
                          String thematicIdx, String narrTable) {
        this.name = name;
        this.type = type;
        this.elementsNumber = elementsNumber;
        this.keyType = keyType;
        this.colDesc = colDesc;
        this.valDescTableName = valDescTableName;
        this.thematicIdx = thematicIdx;
        this.narrTable = narrTable;
    }

    /**
     * Describe <code>toString</code> method here.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        String emptyStr = "";
        StringBuffer buff = new StringBuffer();
        StringBuffer sb = null;
        sb = new StringBuffer(emptyStr + name);
        sb.setLength(16);
        buff.append(sb);
        sb = new StringBuffer(emptyStr + type);
        sb.setLength(5);
        buff.append(sb);
        sb = new StringBuffer(emptyStr + elementsNumber);
        sb.setLength(5);
        buff.append(sb);
        sb = new StringBuffer(emptyStr + keyType);
        sb.setLength(4);
        buff.append(sb);
        sb = new StringBuffer(emptyStr + colDesc);
        sb.setLength(55);
        buff.append(sb);
        sb = new StringBuffer(emptyStr + valDescTableName);
        sb.setLength(5);
        buff.append(sb);
        sb = new StringBuffer(emptyStr + thematicIdx);
        sb.setLength(5);
        buff.append(sb);
        sb = new StringBuffer(emptyStr + narrTable);
        sb.setLength(5);
        buff.append(sb);

        return buff.toString();
    }

    /**
     * Gets the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value of type
     *
     * @return the value of type
     */
    public char getType() {
        return this.type;
    }

    /**
     * Gets the value of elementsNumber
     *
     * @return the value of elementsNumber
     */
    public int getElementsNumber() {
        return this.elementsNumber;
    }

    /**
     * Gets the value of keyType
     *
     * @return the value of keyType
     */
    public char getKeyType() {
        return this.keyType;
    }

    /**
     * Gets the value of colDesc
     *
     * @return the value of colDesc
     */
    public String getColDesc() {
        return this.colDesc;
    }

    /**
     * Gets the value of valDescTableName
     *
     * @return the value of valDescTableName
     */
    public String getValDescTableName() {
        return this.valDescTableName;
    }

    /**
     * Gets the value of thematicIdx
     *
     * @return the value of thematicIdx
     */
    public String getThematicIdx() {
        return this.thematicIdx;
    }

    /**
     * Gets the value of narrTable
     *
     * @return the value of narrTable
     */
    public String getNarrTable() {
        return this.narrTable;
    }

    /**
     * Describe <code>getColumnSize</code> method here.
     *
     * @return an <code>int</code> value
     */
    public int getColumnSize() {
        return DataUtils.getDataTypeSize(type) * elementsNumber;
    }

    /**
     * Describe <code>isNumeric</code> method here.
     *
     * @return a <code>boolean</code> value
     */
    public boolean isNumeric() {
        return DataUtils.isNumeric(type);
    }
}

// TableColumnDef
