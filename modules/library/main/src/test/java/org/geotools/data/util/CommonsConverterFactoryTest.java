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
import junit.framework.TestCase;

public class CommonsConverterFactoryTest extends TestCase {

    CommonsConverterFactory factory;

    protected void setUp() throws Exception {
        factory = new CommonsConverterFactory();
        // Some locales are not compatible with the date formats tested below
        Locale.setDefault(new Locale("en", "US"));
    }

    public void testStringNumberConversion() throws Exception {
        // test with integers
        assertEquals(12, convert("12", Integer.class));
        assertEquals(null, convert("12.0", Integer.class));
        assertEquals(null, convert("12.5", Integer.class));
        assertEquals(null, convert(Long.MAX_VALUE + "", Integer.class));

        // test with longs
        assertEquals(Long.MAX_VALUE, convert(Long.MAX_VALUE + "", Long.class));
        assertEquals(null, convert("1e100", Long.class));
        assertEquals(null, convert("12.5", Long.class));

        // test with doubles
        assertEquals((double) Long.MAX_VALUE, convert(Long.MAX_VALUE + "", Double.class));
        assertEquals(1e100, convert("1e100", Double.class));
        assertEquals(12.5, convert("12.5", Double.class));

        BigDecimal d = new BigDecimal(12345);
        d = d.divide(new BigDecimal(100));
        assertEquals(d, convert("123.45", BigDecimal.class));
    }

    public void testDateConversion() throws Exception {
        assertEquals(TimeZone.getTimeZone("UTC"), convert("UTC", TimeZone.class));
        assertNull(convert("foobar", TimeZone.class));
        assertNull(
                factory.createConverter(String.class, TimeZone.class, null)
                        .convert(null, TimeZone.class));
    }

    public void testTimeZoneConversion() throws Exception {
        assertEquals(
                (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"))
                        .parse("2011-08-02T00:00:00.000Z"),
                factory.createConverter(String.class, Date.class, null)
                        .convert("2011-08-02T00:00:00.000Z", Date.class));
        assertEquals(
                (new SimpleDateFormat("yyyy-MM-dd HH:mm:ssa")).parse("2011-08-02 00:00:00AM"),
                factory.createConverter(String.class, Date.class, null)
                        .convert("2011-08-02 00:00:00AM", Date.class));
        assertNull(
                factory.createConverter(String.class, Date.class, null)
                        .convert("2011-08-02", Date.class));
    }

    Object convert(Object source, Class target) throws Exception {
        return factory.createConverter(source.getClass(), target, null).convert(source, target);
    }

    public void testDataUrlConversion() throws Exception {
        String url = "data:,YQo=";
        assertNotNull(convert(url, URL.class));
    }
}
