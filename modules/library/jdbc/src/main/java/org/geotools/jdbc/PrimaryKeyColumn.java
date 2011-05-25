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


/**
 * Represents a column in a primary key.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 *
 *
 * @source $URL$
 */
public abstract class PrimaryKeyColumn {

    String name;
    
    Class type;
  
    protected PrimaryKeyColumn( String name, Class type ) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public Class getType() {
        return type;
    }
    
}
