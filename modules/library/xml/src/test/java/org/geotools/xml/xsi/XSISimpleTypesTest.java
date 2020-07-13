/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.xsi;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.geotools.util.Converters;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.ElementValueGT;
import org.mockito.Mockito;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** Tests for {@link XSISimpleTypes}. */
public class XSISimpleTypesTest extends TestCase {

    public void testParseDate() throws Exception {
        SimpleType dateBinding = XSISimpleTypes.Date.getInstance();

        Element element = null;
        Attributes attrs = null;
        Map<?, ?> hints = null;
        ElementValue[] value;
        Date expected;
        String sval;
        Object actual;

        sval = "2012-02-14";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Date.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "2012-02-14Z";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Date.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "2011-10-24T10:53:24.200Z";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Date.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateBinding.getValue(element, value, attrs, hints);
        assertNull(actual);

        sval = "10:53:24.255+03:00";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        try {
            dateBinding.getValue(element, value, attrs, hints);
        } catch (SAXException e) {
            assertTrue(true);
        }
    }

    public void testParseDateTime() throws Exception {
        SimpleType dateTimeBinding = XSISimpleTypes.DateTime.getInstance();

        Element element = null;
        Attributes attrs = null;
        Map<?, ?> hints = null;
        ElementValue[] value;
        java.util.Date expected;
        String sval;
        Object actual;

        sval = "2012-02-14";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "2012-02-14Z";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "2011-10-24T10:53:24.200Z";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "2011-10-24T00:00:00.200+03:00";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNull(actual);

        sval = "10:53:24.255+03:00";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        try {
            dateTimeBinding.getValue(element, value, attrs, hints);
        } catch (SAXException e) {
            assertTrue(true);
        }
    }

    public void testParseTime() throws Exception {
        SimpleType timeBinding = XSISimpleTypes.Time.getInstance();

        Element element = null;
        Attributes attrs = null;
        Map<?, ?> hints = null;
        ElementValue[] value;
        java.util.Date expected;
        String sval;
        Object actual;

        sval = "10:53:24Z";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = timeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Time.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "10:53:24-03:00";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = timeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Time.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "10:53:24.255+03:00";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = timeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Time.class);
        assertEquals(
                expected.getClass().getName()
                        + "["
                        + expected
                        + "] : "
                        + actual.getClass().getName()
                        + "["
                        + actual
                        + "]",
                expected,
                actual);

        sval = "";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = timeBinding.getValue(element, value, attrs, hints);
        assertNull(actual);

        sval = "2012-02-14";
        value = new ElementValue[] {new ElementValueGT(null, sval)};
        try {
            timeBinding.getValue(element, value, attrs, hints);
        } catch (SAXException e) {
            assertTrue(true);
        }
    }

    public void testParseDuration() throws Exception {
        SimpleType durationBinding = XSISimpleTypes.Duration.getInstance();

        Element element = null;
        Map<?, ?> hints = null;
        Attributes attrs = null;
        ElementValue[] value;
        String sval = "";
        Object actual;

        value = new ElementValue[] {new ElementValueGT(null, sval)};
        actual = durationBinding.getValue(element, value, attrs, hints);
        assertNull(actual);
    }

    /**
     * Tests encoding of java.util.Date as {@link XSISimpleTypes.Date} and {@link
     * XSISimpleTypes.DateTime}
     */
    public void testDateEncode() throws Exception {
        // given: 2016-09-02, 12:00h in GMT
        SimpleType dateType = XSISimpleTypes.Date.getInstance();
        SimpleType dateTimeType = XSISimpleTypes.DateTime.getInstance();

        Element element = mock(Element.class);
        PrintHandler printer = mock(PrintHandler.class);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        java.util.Date date = calendar.getTime();

        // when: encoded as XSI Date
        dateType.encode(element, date, printer, new HashMap<>());
        // then: the result is expected as follows
        verify(printer).characters(eq("2016-09-02Z"));

        // when: encoded as XSI DateTime
        printer = Mockito.mock(PrintHandler.class);
        dateTimeType.encode(element, date, printer, new HashMap<>());
        // then: the result is expected as follows
        verify(printer).characters(eq("2016-09-02T12:00:00Z"));
    }
}
