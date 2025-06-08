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

import java.util.Map;
import org.geotools.data.vpf.ifc.VPFRow;

/**
 * TableRow.java Created: Thu Jan 02 23:58:39 2003
 *
 * @author <a href="mailto:kobit@users.fs.net">Artur Hefczyc</a>
 * @source $URL$
 * @version 1.0
 */
public class TableRow implements VPFRow {
    /** Describe variable <code>fieldsArr</code> here. */
    private RowField[] fieldsArr = null;

    /** Describe variable <code>fieldsMap</code> here. */
    private Map<String, RowField> fieldsMap = null;

    /**
     * Creates a new <code>TableRow</code> instance.
     *
     * @param fieldsArr a <code>RowField[]</code> value
     * @param fieldsMap a <code>HashMap</code> value
     */
    public TableRow(RowField[] fieldsArr, Map<String, RowField> fieldsMap) {
        this.fieldsArr = fieldsArr;
        this.fieldsMap = fieldsMap;
    }

    // TableRow constructor

    /**
     * Describe <code>toString</code> method here.
     *
     * @return a <code>String</code> value
     */
    @Override
    public String toString() {
        //     StringBuffer buff = new StringBuffer(" ["+getClass().getName());
        //     buff.append(" (fieldsMap=");
        //     if (fieldsMap == null)
        //     {
        //       buff.append("null)");
        //     } // end of if (columnDefs == null)
        //     else
        //     {
        //       Iterator it = fieldsMap.entrySet().iterator();
        //       while (it.hasNext())
        //       {
        //         Map.Entry entry = (Map.Entry)it.next();
        //         buff.append("\n"+
        //                     entry.getKey().toString()+"="+
        //                     entry.getValue().toString());
        //       } // end of while (it.hasNext())
        //       buff.append("\n)");
        //     } // end of if (columnDefs == null) else
        //     buff.append("]");
        StringBuffer buff = new StringBuffer();

        if (fieldsMap == null) {
            buff.append("null)");
        } else {
            for (RowField rowField : fieldsArr) {
                buff.append(rowField.toString() + ":");
            }

            buff.append(";");
        }

        return buff.toString();
    }

    /**
     * Describe <code>fieldsCount</code> method here.
     *
     * @return an <code>int</code> value
     */
    public int fieldsCount() {
        return fieldsArr.length;
    }

    /**
     * Describe <code>get</code> method here.
     *
     * @param name a <code>String</code> value
     * @return a <code>RowField</code> value
     */
    public RowField get(String name) {
        return fieldsMap.get(name);
    }

    /**
     * Describe <code>get</code> method here.
     *
     * @param idx an <code>int</code> value
     * @return a <code>RowField</code> value
     */
    public RowField get(int idx) {
        return fieldsArr[idx];
    }

    /**
     * Describe <code>equals</code> method here.
     *
     * @param obj an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TableRow)) {
            return false;
        }

        TableRow row = (TableRow) obj;

        if (fieldsArr == null && row.fieldsArr == null) {
            return true;
        }

        if (fieldsArr == null || row.fieldsArr == null) {
            return false;
        }

        if (fieldsArr.length != row.fieldsArr.length) {
            return false;
        }

        for (int i = 0; i < fieldsArr.length; i++) {
            if (!fieldsArr[i].equals(row.fieldsArr[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int code = 0;

        if (fieldsArr == null || fieldsArr.length == 0) {
            code = super.hashCode();
        } else {
            for (RowField rowField : fieldsArr) {
                code += rowField.hashCode();
            }
        }

        return code;
    }
}
