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
package org.geotools.feature;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.geotools.feature.type.DateUtil;

public class DateUtilTest extends TestCase {
    
    public void testJavaUtilDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2007, 3, 1, 1, 15);
        
        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        assertEquals("2007-04-01T01:15:00", dateTime);
        String date = DateUtil.serializeDate(time);
        assertEquals("2007-04-01", date);
    }
    
    public void testSqlDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2007, 3, 1, 1, 15);
        
        Date time = cal.getTime();
        java.sql.Date date = new java.sql.Date(time.getTime());
        String dateTime = DateUtil.serializeSqlDate(date);
        assertEquals("2007-04-01", dateTime);
    }
    
    public void testSqlTime() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2007, 3, 1, 1, 15);
        
        long lngTime = cal.getTime().getTime();
        java.sql.Time time = new java.sql.Time(lngTime);
        System.out.println(time);
        String t = DateUtil.serializeSqlTime(time);
        System.out.println(t);
        assertEquals("01:15:00", t);
    }
    
    
}
