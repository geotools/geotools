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
package org.geotools.data.vpf;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.readers.VPFGeometryFactory;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

/**
 * @author <a href="mailto:jim@lomasoft.com">James Gambale</a>
 * @source $URL$
 */
class ColumnSet {
    public String tableName;
    public VPFFile table;
    public List<String> colNames = null;

    // public String joinTableName;
    // public String joinTableKeyName;
    // public VPFFile joinTable;
    // public List<String> joinColNames = null;

    public boolean isGeometryTable = false;

    public VPFGeometryFactory geometryFactory;

    public Geometry geometry = null;

    public VPFColumn geometryColumn = null;

    public SimpleFeature currRow = null;

    public void setTable(String name, VPFFile table) {
        this.tableName = name;
        this.table = table;
        // this.tableKeyName = keyName;
        // this.tableKey = key;

        if (table != null) {
            this.colNames = new ArrayList<String>();

            int colCount = this.table.getColumnCount();

            for (int icol = 0; icol < colCount; icol++) {
                VPFColumn col = this.table.getColumn(icol);
                String colName = col.getName();
                this.colNames.add(colName);
            }
        }
    }

    /*
    public void setJoinTable(String name, VPFFile table, String keyName, VPFColumn key) {
        this.joinTableName = name;
        this.joinTable = table;
        this.joinTableKeyName = keyName;
        this.joinTableKey = key;

        if (table != null) {
            this.joinColNames = new ArrayList<String>();

            int colCount = this.joinTable.getColumnCount();

            for (int icol = 0; icol < colCount; icol++) {
                VPFColumn col = this.joinTable.getColumn(icol);
                String colName = col.getName();
                this.joinColNames.add(colName);
            }
        }
    }
    */

    public void setGeometry(
            boolean b, VPFGeometryFactory geometryFactory, VPFColumn geometryColumn) {
        this.isGeometryTable = b;
        this.geometryFactory = geometryFactory;
        this.geometryColumn = geometryColumn;
        if (geometryColumn != null) {
            this.colNames = new ArrayList<String>();
            String colName = geometryColumn.getName();
            this.colNames.add(colName);
        }
    }
}
