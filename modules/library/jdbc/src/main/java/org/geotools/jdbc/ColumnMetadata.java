/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

/**
 * Metadata about a table column used to carry information through the type mapping process.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/jdbc/src/main/java/org/geotools/jdbc/ColumnMetadata.java $
 */
public class ColumnMetadata {

    /** The column java type, if known */
    Class binding;
    /** The column name */
    String name;
    /** The native type name */
    String typeName;
    /** The native sql type */
    int sqlType;
    /** Is the column accepting null values? */
    boolean nullable;
    /** The native srid */
    Integer srid;
    
    public Class getBinding() {
        return binding;
    }
    
    public void setBinding(Class binding) {
        this.binding = binding;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public int getSqlType() {
        return sqlType;
    }
    
    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    public Integer getSrid() {
        return srid;
    }
    
    public void setSrid(Integer srid) {
        this.srid = srid;
    }
}
