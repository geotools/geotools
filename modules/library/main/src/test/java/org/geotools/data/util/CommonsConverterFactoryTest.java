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
package org.geotools.data.util;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommonsConverterFactoryTest {

    CommonsConverterFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new CommonsConverterFactory();
        // Some locales are not compatible with the date formats tested below
        Locale.setDefault(new Locale("en", "US"));
    }

    @Test
    public void testStringNumberConversion() throws Exception {
        // test with integers
        Assert.assertEquals(12, convert("12", Integer.class));
        Assert.assertNull(convert("12.0", Integer.class));
        Assert.assertNull(convert("12.5", Integer.class));
        Assert.assertNull(convert(Long.MAX_VALUE + "", Integer.class));

        // test with longs
        Assert.assertEquals(Long.MAX_VALUE, convert(Long.MAX_VALUE + "", Long.class));
        Assert.assertNull(convert("1e100", Long.class));
        Assert.assertNull(convert("12.5", Long.class));

        // test with doubles
        Assert.assertEquals((double) Long.MAX_VALUE, convert(Long.MAX_VALUE + "", Double.class));
        Assert.assertEquals(1e100, convert("1e100", Double.class));
        Assert.assertEquals(12.5, convert("12.5", Double.class));

        BigDecimal d = new BigDecimal(12345);
        d = d.divide(new BigDecimal(100));
        Assert.assertEquals(d, convert("123.45", BigDecimal.class));
    }

    @Test
    public void testDateConversion() throws Exception {
        Assert.assertEquals(TimeZone.getTimeZone("UTC"), convert("UTC", TimeZone.class));
        Assert.assertNull(convert("foobar", TimeZone.class));
        Assert.assertNull(
                factory.createConverter(String.class, TimeZone.class, null).convert(null, TimeZone.class));
    }

    @Test
    public void testTimeZoneConversion() throws Exception {
        Assert.assertEquals(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse("2011-08-02T00:00:00.000Z"),
                factory.createConverter(String.class, Date.class, null)
                        .convert("2011-08-02T00:00:00.000Z", Date.class));
        Assert.assertEquals(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ssa").parse("2011-08-02 00:00:00AM"),
                factory.createConverter(String.class, Date.class, null).convert("2011-08-02 00:00:00AM", Date.class));
        Assert.assertNull(
                factory.createConverter(String.class, Date.class, null).convert("2011-08-02", Date.class));
    }

    Object convert(Object source, Class<?> target) throws Exception {
        return factory.createConverter(source.getClass(), target, null).convert(source, target);
    }

    @Test
    public void testDataUrlConversion() throws Exception {
        String url = "data:,YQo=";
        Assert.assertNotNull(convert(url, URL.class));
    }
}
