/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;

/**
 * Encodes a filter into a SQL WHERE statement for the JDBC providers used with the optional RECNO field index.
 * 
 * @author Alvaro Huarte - Tracasa / ahuarte@tracasa.es
 */
public class RecnoFilterToSQL extends FilterToSQL 
{
    public RecnoFilterToSQL(FilterCapabilities filterCapabilities) 
    {
        this.filterCapabilities = filterCapabilities;
    }
    
    @Override
    protected FilterCapabilities createFilterCapabilities() 
    {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addAll(filterCapabilities);        
        return caps;
    }
    private FilterCapabilities filterCapabilities;
    
    static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    static {
        // Set DATE_FORMAT time zone to GMT, as Date's are always in GMT internaly. Otherwise we'll
        // get a local timezone encoding regardless of the actual Date value
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
    
    @Override
    protected void writeLiteral(Object literal) throws IOException 
    {
        if (literal instanceof Date)
        {
            out.write("'");
            
            if (literal instanceof java.sql.Date)
            {
                out.write(DATE_FORMAT.format(literal));
            }
            else
            {
                out.write(DATETIME_FORMAT.format(literal));
            }
            out.write("'");
        }
        else
        {
            super.writeLiteral(literal);
        }
    }
}
