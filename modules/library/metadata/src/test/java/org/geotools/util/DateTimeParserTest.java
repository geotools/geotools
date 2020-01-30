/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

/** @author Simone Giannecchini, GeoSolutions SAS */
public class DateTimeParserTest extends Assert {

    private static final DateTimeParser PARSER =
            new DateTimeParser(
                    -1,
                    DateTimeParser.FLAG_IS_LENIENT
                            | DateTimeParser.FLAG_GET_TIME_ON_CURRENT
                            | DateTimeParser.FLAG_GET_TIME_ON_NOW);

    @Test
    public void testParserOnCurrentTime() throws ParseException, InterruptedException {
        long now = System.currentTimeMillis();
        Thread.sleep(1000);
        final String timeInstant = "current";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertTrue(now < getTime(time, 0));
    }

    @Test
    public void testParserOnNullTime() throws ParseException {
        Collection time = PARSER.parse(null);
        assertTrue(time.isEmpty());
    }

    @Test
    public void testParserOnTimeInstantFormat1() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyy
        String timeInstant = "2011";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-01-01T00:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat2() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyyMM
        String timeInstant = "201110";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-01T00:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat3() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyy-MM
        String timeInstant = "2011-10";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-01T00:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat4() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyyMMdd
        String timeInstant = "20111010";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T00:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat5() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyy-MM-dd
        String timeInstant = "2011-10-10";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T00:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat6() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyyMMdd'T'HH
        String timeInstant = "20111010T10";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat7() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyy-MM-dd'T'HH
        String timeInstant = "2011-10-10T10";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat8() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HH'Z'
        String timeInstant = "20111010T10Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat9() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HH'Z'
        String timeInstant = "2011-10-10T10Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:00:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat10() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyyMMdd'T'HHmm
        String timeInstant = "20111010T1011";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat11() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        // test format yyyyMMdd'T'HH:mm
        String timeInstant = "20111010T10:11";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat12() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HHmm
        String timeInstant = "2011-10-10T1011";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat13() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HH:mm
        String timeInstant = "2011-10-10T10:11";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat14() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HHmm'Z'
        String timeInstant = "20111010T1011Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat15() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HH:mm'Z'
        String timeInstant = "20111010T10:11Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat16() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HHmm'Z'
        String timeInstant = "2011-10-10T1011Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat17() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HH:mm'Z'
        String timeInstant = "2011-10-10T10:11Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:00.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat18() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HHmmss
        String timeInstant = "20111010T101120";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat19() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HH:mm:ss
        String timeInstant = "20111010T10:11:20";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat20() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HHmmss
        String timeInstant = "2011-10-10T101120";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat21() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HH:mm:ss
        String timeInstant = "2011-10-10T10:11:20";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat22() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HHmmss'Z'
        String timeInstant = "20111010T101120Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat23() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HH:mm:ss'Z'
        String timeInstant = "20111010T10:11:20Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat24() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HHmmss'Z'
        String timeInstant = "2011-10-10T101120Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat25() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HH:mm:ss'Z'
        String timeInstant = "2011-10-10T10:11:20Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.000Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat26() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HHmmssSSS
        String timeInstant = "20111010T101120666";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat27() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HH:mm:ss.SSS
        String timeInstant = "20111010T10:11:20.666";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat28() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HHmmssSSS
        String timeInstant = "2011-10-10T101120666";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat29() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HH:mm:ss.SSS
        String timeInstant = "2011-10-10T10:11:20.666";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat30() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HHmmssSSS'Z'
        String timeInstant = "20111010T101120666Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat31() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyyMMdd'T'HH:mm:ss.SSS'Z'
        String timeInstant = "20111010T10:11:20.666Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat32() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HHmmssSSS'Z'
        String timeInstant = "2011-10-10T101120666Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimeInstantFormat33() throws ParseException {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // test format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        String timeInstant = "2011-10-10T10:11:20.666Z";
        Collection time = PARSER.parse(timeInstant);
        assertEquals(1, time.size());
        assertEquals("2011-10-10T10:11:20.666Z", df.format(getTime(time, 0)));
    }

    @Test
    public void testParserOnTimePeriod() throws ParseException {
        final String timeInterval = "2011-10-10T10:11:12.000Z/2011-10-10T14:11:12.000Z/PT1H";
        Collection time = PARSER.parse(timeInterval);
        assertEquals(5, time.size());
        assertEquals(1318241472000l, getTime(time, 0));
        assertEquals(1318241472000l + (3600 * 1000 * 4), getTime(time, time.size() - 1));
    }

    @Test
    public void testParserOnDayPeriod() throws ParseException {
        final String timeInterval = "2011-10-10T10:11:12.000Z/2011-10-14T10:11:12.000Z/P2D";
        Collection time = PARSER.parse(timeInterval);
        assertEquals(3, time.size());
        assertEquals(1318241472000l, getTime(time, 0));
        assertEquals(1318241472000l + (3600 * 1000 * 48), getTime(time, 1));
    }

    private static long getTime(Collection time, int i) {
        Object date = null;
        if (i <= 0) {
            date = time.stream().findFirst().get();
        } else {
            date = time.stream().skip(i).findFirst().get();
        }

        if (date != null && date instanceof Date) {
            return ((Date) date).getTime();
        }
        throw new IllegalArgumentException("time isn't a collection of Date");
    }
}
