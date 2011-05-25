/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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

import org.geotools.factory.FactoryRegistryException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Hold information about a DB2 geometry column and provide access.
 *
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2GeometryColumn {
    private String tableSchema;
    private String tableName;
    private String columnName;
    private String typeName;
    private Integer srsId;
    private DB2CoordinateSystem cs;

    /**
     * Constructs a DB2GeometryColumn from all relevant values.
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     * @param typeName
     * @param srsId
     * @param cs
     */
    public DB2GeometryColumn(String tableSchema, String tableName,
        String columnName, String typeName, Integer srsId,
        DB2CoordinateSystem cs) {
        super();
        this.tableSchema = tableSchema;
        this.tableName = tableName;
        this.columnName = columnName;
        this.typeName = typeName;
        this.srsId = srsId;
        this.cs = cs;
    }

    String getTableSchema() {
        return this.tableSchema;
    }

    String getTableName() {
        return this.tableName;
    }

    String getColumnName() {
        return this.columnName;
    }

    String getTypeName() {
        return this.typeName;
    }

    int getCsId() {
        return this.cs.getCsId();
    }

    /**
     * Gets the OpenGIS CoordinateReferenceSystem for this geometry column.
     *
     * @return a CoordinateReferenceSystem
     *
     * @throws FactoryRegistryException
     * @throws FactoryException
     */
    CoordinateReferenceSystem getCRS()
        throws FactoryRegistryException, FactoryException {
        return this.cs.getCRS();
    }

    Integer getSrsId() {
        return this.srsId;
    }

    /**
     * Returns the table schema, table name and column name.
     *
     * @return the table schema, table name and column name as a String.
     */
    public String toString() {
        return this.tableSchema + "." + this.tableName + "(" + this.columnName
        + "); srid=" + this.srsId;
    }
}
