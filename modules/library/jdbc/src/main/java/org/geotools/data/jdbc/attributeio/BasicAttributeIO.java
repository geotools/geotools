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
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.data.DataSourceException;


/**
 * A basic attribute IO class, that will parse and write attributes
 * as objects (using getObject, updateObject). This works usually with
 * all primitives wrapped by an objects, and of course with every {@link Serializable}
 * object.
 *
 * @author wolf
 *
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class BasicAttributeIO implements AttributeIO {
    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Object read(ResultSet rs, int position) throws IOException {
        try {
            return rs.getObject(position);
        } catch (SQLException e) {
            throw new DataSourceException("Sql problem.", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.ResultSet,
     *      int, java.lang.Object)
     */
    public void write(ResultSet rs, int position, Object value)
        throws IOException {
        try {
            if (value == null) {
                rs.updateNull(position);
            } else {
                rs.updateObject(position, value);
            }
        } catch (Exception e) {
            throw new DataSourceException("Sql problem.", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.PreparedStatement, int, java.lang.Object)
     */
    public void write(PreparedStatement ps, int position, Object value) throws IOException {
        try {
            if (value == null) {
                ps.setNull(position, Types.OTHER);
            } else {
                ps.setObject(position, value);
            }
        } catch (Exception e) {
            throw new DataSourceException("Sql problem.", e);
        }
        
    }
    
    
}
