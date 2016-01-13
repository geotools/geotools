/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.impl;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;

import org.junit.Test;

/**
 * This class is used for testing the parsing capabilities of the {@link XsDateTimeFormat} class.
 * 
 * @author Nicola Lagomarsini GeoSolutions
 */
public class XsDateTimeFormatTest {

    @Test
    public void testDate() throws ParseException {
        // Create the parser
        XsDateTimeFormat format = new XsDateTimeFormat();
        // Set a date
        String time = "2014-10-12T09:00:00.000Z";
        // Parse the Date
        Object parseObject = format.parseObject(time);
        assertTrue(parseObject instanceof Calendar);

        // Get the date
        Calendar cal = (Calendar) parseObject;
        // Ensure the parsing is correct
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hours = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // check
        assertEquals(year, 2014);
        assertEquals(month, 9);
        assertEquals(day, 12);
        assertEquals(hours, 9);
        assertEquals(mins, 0);
        assertEquals(sec, 0);
        assertEquals(millis, 0);
    }

    @Test(expected = ParseException.class)
    public void testWrongDate() throws ParseException {
        // Create the parser
        XsDateTimeFormat format = new XsDateTimeFormat();
        // Set a date
        String time = "2014-10-12T09:00Z";
        // Parse the Date
        Object parseObject = format.parseObject(time);
    }

    @Test
    public void testWrongDateLenientNoSec() throws ParseException {
        // Create the parser
        XsDateTimeFormat format = new XsDateTimeFormat();
        // Set a date
        String time = "2014-10-12T09:00Z";
        // Parse the Date
        Object parseObject = format.parseObject(time, true);
        assertTrue(parseObject instanceof Calendar);

        // Get the date
        Calendar cal = (Calendar) parseObject;
        // Ensure the parsing is correct
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hours = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // check
        assertEquals(year, 2014);
        assertEquals(month, 9);
        assertEquals(day, 12);
        assertEquals(hours, 9);
        assertEquals(mins, 0);
        assertEquals(sec, 0);
        assertEquals(millis, 0);
    }

    @Test
    public void testWrongDateLenientNoMins() throws ParseException {
        // Create the parser
        XsDateTimeFormat format = new XsDateTimeFormat();
        // Set a date
        String time = "2014-10-12T09Z";
        // Parse the Date
        Object parseObject = format.parseObject(time, true);
        assertTrue(parseObject instanceof Calendar);

        // Get the date
        Calendar cal = (Calendar) parseObject;
        // Ensure the parsing is correct
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hours = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // check
        assertEquals(year, 2014);
        assertEquals(month, 9);
        assertEquals(day, 12);
        assertEquals(hours, 9);
        assertEquals(mins, 0);
        assertEquals(sec, 0);
        assertEquals(millis, 0);
    }

    @Test
    public void testWrongDateLenientIncomplete() throws ParseException {
        // Create the parser
        XsDateTimeFormat format = new XsDateTimeFormat();
        // Set a date
        String time = "2014-10-12T0Z";
        // Parse the Date
        Object parseObject = format.parseObject(time, true);
        assertTrue(parseObject instanceof Calendar);

        // Get the date
        Calendar cal = (Calendar) parseObject;
        // Ensure the parsing is correct
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hours = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // check
        assertEquals(year, 2014);
        assertEquals(month, 9);
        assertEquals(day, 12);
        assertEquals(hours, 0);
        assertEquals(mins, 0);
        assertEquals(sec, 0);
        assertEquals(millis, 0);
    }
    
    @Test
    public void testWrongDateLenientIncompleteMins() throws ParseException {
        // Create the parser
        XsDateTimeFormat format = new XsDateTimeFormat();
        // Set a date
        String time = "2014-10-12T05:0Z";
        // Parse the Date
        Object parseObject = format.parseObject(time, true);
        assertTrue(parseObject instanceof Calendar);

        // Get the date
        Calendar cal = (Calendar) parseObject;
        // Ensure the parsing is correct
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hours = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // check
        assertEquals(year, 2014);
        assertEquals(month, 9);
        assertEquals(day, 12);
        assertEquals(hours, 5);
        assertEquals(mins, 0);
        assertEquals(sec, 0);
        assertEquals(millis, 0);
    }
    
    @Test
    public void testExtendedFractionalSeconds() throws ParseException {
        XsDateTimeFormat format = new XsDateTimeFormat();
        // parse a timestamp with extended decimals
        String time = "2014-10-13T05:34:02.1109963Z";
        Object parseObject = format.parseObject(time, true);
        assertTrue(parseObject instanceof Calendar);

        // Get the date
        Calendar cal = (Calendar) parseObject;
        // Ensure the parsing is correct
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hours = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        // check
        assertEquals(year, 2014);
        assertEquals(month, 9);
        assertEquals(day, 13);
        assertEquals(hours, 5);
        assertEquals(mins, 34);
        assertEquals(sec, 02);
        assertEquals(millis, 111);
    }
   
}
