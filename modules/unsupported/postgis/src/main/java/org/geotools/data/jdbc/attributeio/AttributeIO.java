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
package org.geotools.data.jdbc.attributeio;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Attribute reader/writer. Classes implementing this interface know
 * how to parse and encode Feature attributes into resultset fields
 *
 * @author wolf
 *
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public interface AttributeIO {
    /**
     * Reads a feature attribute out of a ResultSet
     *
     * @param rs - the resultset to be read
     * @param position - the position of the attribute in the resultset
     *
     * @return The parsed attribute
     *
     * @throws IOException - if some exception occurs while reading the attribute
     */
    public Object read(ResultSet rs, int position) throws IOException;

    /**
     * Writes a feature attribute into a ResultSet
     *
     * @param rs - the result set to be modified
     * @param position - the position in which the attribute will inserted into the result set  
     * @param value - the attribute that will be written into the resultset
     *
     * @throws IOException - if some exception occurs while writing the attribute
     */
    public void write(ResultSet rs, int position, Object value)
        throws IOException;
    
    /**
     * Writes a feature attribute into a PreparedStatement
     *
     * @param ps - the result set to be modified
     * @param position - the position in which the attribute will inserted into the result set  
     * @param value - the attribute that will be written into the resultset
     *
     * @throws IOException - if some exception occurs while writing the attribute
     */
    public void write(PreparedStatement ps, int position, Object value)
        throws IOException;
}
