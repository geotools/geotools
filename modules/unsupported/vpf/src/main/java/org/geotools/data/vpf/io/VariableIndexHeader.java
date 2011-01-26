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

import org.geotools.data.vpf.ifc.VPFHeader;


/**
 * VariableIndexHeader.java Created: Tue Mar 11 23:41:57 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 * @source $URL$
 * @version $Id$
 */
public class VariableIndexHeader implements VPFHeader {
    /**
     * Variable constant <code>VARIABLE_INDEX_HEADER_LENGTH</code> keeps value
     * of
     */
    public static final int VARIABLE_INDEX_HEADER_LENGTH = 8;

    /** Variable constant <code>VARIABLE_INDEX_ROW_SIZE</code> keeps value of */
    public static final int VARIABLE_INDEX_ROW_SIZE = 8;

    /** Variable variable <code>entriesNumber</code> keeps value of */
    private int entriesNumber = 0;

    /** Variable variable <code>vpfHeaderLen</code> keeps value of */
    private int vpfHeaderLen = 0;

    /**
     * Creates a new VariableIndexHeader object.
     *
     * @param entriesNumber DOCUMENT ME!
     * @param vpfHeaderLen DOCUMENT ME!
     */
    public VariableIndexHeader(int entriesNumber, int vpfHeaderLen) {
        this.entriesNumber = entriesNumber;
        this.vpfHeaderLen = vpfHeaderLen;
    }

    // VariableIndexHeader constructor

    /**
     * Returns particular <code>VPFHeader</code> length.
     *
     * @return an <code>int</code> value of header length.
     */
    public int getLength() {
        return VARIABLE_INDEX_HEADER_LENGTH;
    }

    /**
     * Method <code><code>getRecordSize</code></code> is used to return size in
     * bytes of records stored in this table. If table keeps variable length
     * records <code>-1</code> should be returned.
     *
     * @return an <code><code>int</code></code> value
     */
    public int getRecordSize() {
        return VARIABLE_INDEX_ROW_SIZE;
    }

    /**
     * Gets the value of <code>entriesNumber</code>
     *
     * @return the value of <code>entriesNumber</code>
     */
    public int getEntriesNumber() {
        return this.entriesNumber;
    }

    //   /**
    //    * Sets the value of entriesNumber
    //    *
    //    * @param argEntriesNumber Value to assign to this.entriesNumber
    //    */
    //   public void setEntriesNumber(int argEntriesNumber) {
    //     this.entriesNumber = argEntriesNumber;
    //   }

    /**
     * Gets the value of <code>vpfHeaderLen</code>
     *
     * @return the value of <code>vpfHeaderLen</code>
     */
    public int getVpfHeaderLen() {
        return this.vpfHeaderLen;
    }

    //   /**
    //    * Sets the value of vpfHeaderLen
    //    *
    //    * @param argVpfHeaderLen Value to assign to this.vpfHeaderLen
    //    */
    //   public void setVpfHeaderLen(int argVpfHeaderLen) {
    //     this.vpfHeaderLen = argVpfHeaderLen;
    //   }
}

// VariableIndexHeader
