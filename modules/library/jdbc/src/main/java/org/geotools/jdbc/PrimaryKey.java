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

import java.util.List;


/**
 * Primary key of a table.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 * @source $URL$
 */
public class PrimaryKey {
    /**
     * The columns making up the primary key.
     */
    List<PrimaryKeyColumn> columns;
    
    /**
     * Table name
     */
    String tableName;
   
    public PrimaryKey( String tableName, List<PrimaryKeyColumn> columns ) {
        this.tableName = tableName;
        this.columns = columns;
    }
    
    public List<PrimaryKeyColumn> getColumns() {
        return columns;
    }
    
    
    public String getTableName() {
        return tableName;
    }
}
