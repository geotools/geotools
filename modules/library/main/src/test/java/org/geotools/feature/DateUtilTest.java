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
import org.geotools.feature.type.DateUtil;
import org.junit.Assert;
import org.junit.Test;

public class DateUtilTest {

    @Test
    public void testJavaUtilDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2007, 3, 1, 1, 15);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2007-04-01T01:15:00", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2007-04-01", date);
    }

    @Test
    public void testGEOT5353_1() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2100, 1, 28, 12, 43);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2100-02-28T12:43:00", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2100-02-28", date);
    }

    @Test
    public void testGEOT5353_2() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2100, 1, 28, 12, 00);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2100-02-28T12:00:00", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2100-02-28", date);
    }

    @Test
    public void testGEOT5353_3() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2100, 1, 28, 23, 59, 59);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2100-02-28T23:59:59", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2100-02-28", date);
    }

    @Test
    public void testGEOT5353_4() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2100, 2, 1, 00, 00, 00);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2100-03-01T00:00:00", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2100-03-01", date);
    }

    @Test
    public void testGEOT5353_5() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2019, 2, 1, 00, 00, 00);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2019-03-01T00:00:00", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2019-03-01", date);
    }

    @Test
    public void testGEOT5353_6() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2019, 1, 28, 12, 00);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2019-02-28T12:00:00", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2019-02-28", date);
    }

    @Test
    public void testGEOT5353_7() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2096, 1, 28, 23, 59, 59);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2096-02-28T23:59:59", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2096-02-28", date);
    }

    @Test
    public void testGEOT5353_8() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2104, 1, 28, 23, 59, 59);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2104-02-28T23:59:59", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2104-02-28", date);
    }

    @Test
    public void testGEOT5353_9() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2400, 1, 28, 23, 59, 59);

        Date time = cal.getTime();
        String dateTime = DateUtil.serializeDateTime(time);
        Assert.assertEquals("2400-02-28T23:59:59", dateTime);
        String date = DateUtil.serializeDate(time);
        Assert.assertEquals("2400-02-28", date);
    }

    @Test
    public void testSqlDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2007, 3, 1, 1, 15);

        Date time = cal.getTime();
        java.sql.Date date = new java.sql.Date(time.getTime());
        String dateTime = DateUtil.serializeSqlDate(date);
        Assert.assertEquals("2007-04-01", dateTime);
    }

    @Test
    public void testSqlTime() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2007, 3, 1, 1, 15);

        long lngTime = cal.getTime().getTime();
        java.sql.Time time = new java.sql.Time(lngTime);
        // System.out.println(time);
        String t = DateUtil.serializeSqlTime(time);
        // System.out.println(t);
        Assert.assertEquals("01:15:00", t);
    }
}
