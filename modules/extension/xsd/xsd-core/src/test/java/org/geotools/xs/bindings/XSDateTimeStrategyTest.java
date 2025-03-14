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
package org.geotools.xs.bindings;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;
import org.geotools.xs.XSConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.SimpleBinding;
import org.geotools.xsd.XSD;
import org.junit.Test;
import org.w3c.dom.Document;

public class XSDateTimeStrategyTest extends TestSchema {

    @Override
    protected QName getQName() {
        return null;
    }

    public void checkParseEncode(
            final QName qname, final String toParse, final Date toEncode, final String expectedEncoding)
            throws Exception {

        SimpleBinding strategy = (SimpleBinding) stratagy(qname);
        Date parsed = (Date) strategy.parse(element(toParse, qname), toParse);
        assertEquals(toEncode.getClass(), parsed.getClass());

        assertEquals(parsed.getClass().getName(), toEncode, parsed);

        String encoded = strategy.encode(toEncode, null);
        assertEquals(expectedEncoding, encoded);
    }

    private Calendar calendar(Integer... values) {
        return calendar(TimeZone.getTimeZone("GMT"), values);
    }

    private Calendar calendar(TimeZone timeZone, Integer... values) {
        int[] fields = {
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH,
            Calendar.HOUR_OF_DAY,
            Calendar.MINUTE,
            Calendar.SECOND,
            Calendar.MILLISECOND
        };

        Calendar cal = Calendar.getInstance(timeZone);
        cal.clear();
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                cal.set(fields[i], values[i]);
            }
        }
        return cal;
    }

    private long timestamp(Integer... values) {
        return timestamp(TimeZone.getTimeZone("GMT"), values);
    }

    private long timestamp(TimeZone timeZone, Integer... values) {
        Calendar cal = calendar(timeZone, values);
        return cal.getTimeInMillis();
    }

    @Test
    public void testDate() throws Exception {

        java.sql.Date expected = new java.sql.Date(timestamp(2011, 9, 24));
        checkParseEncode(XS.DATE, "2011-10-24Z", expected, "2011-10-24Z");
    }

    @Test
    public void testTime() throws Exception {
        Integer nil = null;

        java.sql.Time expected = new java.sql.Time(timestamp(nil, nil, nil, 10, 53, 24));
        checkParseEncode(XS.TIME, "10:53:24Z", expected, "10:53:24Z");

        expected = new java.sql.Time(timestamp(TimeZone.getTimeZone("GMT-3:00"), nil, nil, nil, 10, 53, 24));
        checkParseEncode(XS.TIME, "10:53:24-03:00", expected, "13:53:24Z");

        expected = new java.sql.Time(timestamp(TimeZone.getTimeZone("GMT+3:00"), nil, nil, nil, 10, 53, 24, 255));
        checkParseEncode(XS.TIME, "10:53:24.255+03:00", expected, "07:53:24.255Z");
    }

    @Test
    public void testTimeStamp() throws Exception {

        java.sql.Timestamp expected = new java.sql.Timestamp(timestamp(2011, 9, 24, 10, 53, 24));
        checkParseEncode(XS.DATETIME, "2011-10-24T10:53:24Z", expected, "2011-10-24T10:53:24Z");

        expected = new java.sql.Timestamp(timestamp(2011, 9, 24, 10, 53, 24, 200));
        checkParseEncode(XS.DATETIME, "2011-10-24T10:53:24.200Z", expected, "2011-10-24T10:53:24.200Z");

        expected = new java.sql.Timestamp(timestamp(TimeZone.getTimeZone("GMT+3:00"), 2011, 9, 24, 0, 00, 00, 200));
        checkParseEncode(XS.DATETIME, "2011-10-24T00:00:00.200+03:00", expected, "2011-10-23T21:00:00.200Z");

        expected = new java.sql.Timestamp(timestamp(TimeZone.getTimeZone("GMT-3:00"), 2011, 9, 24, 0, 00, 00, 200));
        checkParseEncode(XS.DATETIME, "2011-10-24T00:00:00.200-03:00", expected, "2011-10-24T03:00:00.200Z");

        expected = new java.sql.Timestamp(timestamp(2011, 9, 24, 10, 53, 00));
        checkParseEncode(XS.DATETIME, "2011-10-24T10:53Z", expected, "2011-10-24T10:53:00Z");
    }

    @Test
    public void testEncodeCalendarDate() throws Exception {

        Calendar cal = calendar(2011, 9, 24);
        testEncodeCalendar(cal, TEST.DATE, "2011-10-24Z");

        cal = calendar(TimeZone.getTimeZone("GMT+14:00"), 2011, 9, 24);
        testEncodeCalendar(cal, TEST.DATE, "2011-10-23Z");
    }

    @Test
    public void testEncodeCalendarTime() throws Exception {

        Integer nil = null;

        Calendar cal = calendar(nil, nil, nil, 10, 53, 31);
        testEncodeCalendar(cal, TEST.TIME, "10:53:31Z");

        cal = calendar(nil, nil, nil, 10, 53, 31, 125);
        testEncodeCalendar(cal, TEST.TIME, "10:53:31.125Z");
    }

    @Test
    public void testEncodeCalendarDateTime() throws Exception {

        Calendar cal = calendar(2011, 9, 24, 10, 53, 31);
        testEncodeCalendar(cal, TEST.DATETIME, "2011-10-24T10:53:31Z");

        cal = calendar(2011, 9, 24, 10, 53, 31, 999);
        testEncodeCalendar(cal, TEST.DATETIME, "2011-10-24T10:53:31.999Z");
    }

    @Test
    public void testDirectlyEncodeCalendarDateTime() throws Exception {
        Calendar cal = calendar(2011, 9, 24, 10, 53, 31);
        String encoded = new XSDateTimeBinding().encode(cal, null);
        assertEquals("2011-10-24T10:53:31Z", encoded);

        cal = calendar(2011, 9, 24, 10, 53, 31, 999);
        encoded = new XSDateTimeBinding().encode(cal, null);
        assertEquals("2011-10-24T10:53:31.999Z", encoded);
    }

    /** GEOT-7072: Non-comformant WFS implementations tend to send empty elements (e.g. {@code <value></value>}) */
    @Test
    public void testParseEmptyStringAsNull() throws Exception {
        validateValues(XS.DATE, "", null);
        validateValues(XS.DATE, "\t", null);
        validateValues(XS.TIME, "", null);
        validateValues(XS.TIME, "\t", null);
        validateValues(XS.DATETIME, "", null);
        validateValues(XS.DATETIME, "\t", null);
    }

    public void validateValues(QName qname, String given, Object expected) throws Exception {
        SimpleBinding strategy = (SimpleBinding) stratagy(qname);
        Object result = strategy.parse(element(given, qname), given);
        super.validateValues(result, expected);
    }

    private void testEncodeCalendar(Calendar cal, QName qname, String expected) throws Exception {
        Encoder encoder = new Encoder(new TestConfiguration());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        encoder.encode(cal, qname, out);

        Document dom = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(out.toByteArray()));

        String encodedValue = dom.getDocumentElement().getTextContent();

        assertEquals(expected, encodedValue);
    }

    private static class TEST extends XSD {
        public static final String NAMESPACE = "http://localhost/xob/test";

        public static final QName DATE = new QName(NAMESPACE, "date");

        public static final QName TIME = new QName(NAMESPACE, "time");

        public static final QName DATETIME = new QName(NAMESPACE, "dateTime");

        @Override
        public String getNamespaceURI() {
            return NAMESPACE;
        }

        @Override
        public String getSchemaLocation() {
            return TestSchema.url.toExternalForm();
        }
    }

    private static class TestConfiguration extends Configuration {

        public TestConfiguration() {
            super(new TEST());
            addDependency(new XSConfiguration());
        }
    }
}
