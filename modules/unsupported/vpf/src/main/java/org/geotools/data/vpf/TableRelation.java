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

/**
 * @author <a href="mailto:jim@lomasoft.com">James Gambale</a>
 * @source $URL$
 */
class TableRelation {
    public String tableName;
    public String tableKeyName;
    public ColumnSet colSet;
    public VPFColumn tableKey;

    public String joinTableName;
    public String joinTableKeyName;
    public ColumnSet joinColSet;
    public VPFColumn joinTableKey;

    public void setTable(String name, ColumnSet colSet, String keyName, VPFColumn key) {
        this.tableName = name;
        this.tableKeyName = keyName;
        this.colSet = colSet;
        this.tableKey = key;
    }

    public void setJoinTable(String name, ColumnSet colSet, String keyName, VPFColumn key) {
        this.joinTableName = name;
        this.joinTableKeyName = keyName;
        this.joinColSet = colSet;
        this.joinTableKey = key;
    }
}
