/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 * (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */

package org.geotools.appschema.filter.expression;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

/**
 * Test that {@link org.geotools.appschema.filter.expression.FormatDateTimezoneFunction} correctly
 * formats days in various time zones.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class FormatDateTimezoneFunctionTest {

    /** The time whose formatting is under test. */
    private static final String TIME = "1948-01-01T00:00:00Z";

    /**
     * {@link SimpleDateFormat} pattern for formatting; we will get just the day in ISO 8601 form.
     */
    private static final String PATTERN = "yyyy-MM-dd";

    /**
     * Expected ISO 8601 formatted day in UTC (zero offset) or time zones with positive offset (east
     * of meridian).
     */
    private static final String DAY = "1948-01-01";

    /** Expected ISO 8601 formatted day in time zones with negative offset (west of meridian). */
    private static final String PREVIOUS_DAY = "1947-12-31";

    private static final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    /**
     * Return {@link #DAY} formatted according to {@link #PATTERN} in a time zone.
     *
     * @param timezone in one of the formats supported by {@link TimeZone}
     * @return the formatted day
     */
    private String formatTimezone(String timezone) {
        return (String)
                ff.function(
                                FormatDateTimezoneFunction.NAME.getFunctionName(),
                                ff.literal(PATTERN),
                                ff.literal(TIME),
                                ff.literal(timezone))
                        .evaluate(null);
    }

    /** Test formatting in UTC time zone. */
    @Test
    public void utc() {
        Assert.assertEquals(DAY, formatTimezone("UTC"));
    }

    /** Test formatting in Canada/Mountain time zone. */
    @Test
    public void canadaMountain() {
        Assert.assertEquals(PREVIOUS_DAY, formatTimezone("Canada/Mountain"));
    }

    /** Test formatting in Australia/Perth time zone. */
    @Test
    public void australiaPerth() {
        Assert.assertEquals(DAY, formatTimezone("Australia/Perth"));
    }

    /** Test for helpful exception detail if wrong number of parameters. */
    @Test
    public void wrongNumberOfParameters() {
        try {
            ff.function(
                            FormatDateTimezoneFunction.NAME.getFunctionName(),
                            ff.literal(PATTERN),
                            ff.literal(TIME))
                    .evaluate(null);
            Assert.fail("Unexpected success");
        } catch (RuntimeException e) {
            Assert.assertEquals(
                    FormatDateTimezoneFunction.NAME.getFunctionName()
                            + ": wrong number of parameters (2 not 3)",
                    e.getMessage());
        }
    }

    /** Test for helpful exception detail if date is invalid. */
    @Test
    public void invalidDate() {
        try {
            ff.function(
                            FormatDateTimezoneFunction.NAME.getFunctionName(),
                            ff.literal(PATTERN),
                            ff.literal("not a valid time"),
                            ff.literal("UTC"))
                    .evaluate(null);
            Assert.fail("Unexpected success");
        } catch (RuntimeException e) {
            Assert.assertEquals(
                    FormatDateTimezoneFunction.NAME.getFunctionName()
                            + ": could not parse date: not a valid time",
                    e.getMessage());
        }
    }

    /** Test that a null pattern causes null to be returned. */
    @Test
    public void nullPattern() {
        Assert.assertNull(
                ff.function(
                                FormatDateTimezoneFunction.NAME.getFunctionName(),
                                ff.literal(null),
                                ff.literal(TIME),
                                ff.literal("UTC"))
                        .evaluate(null));
    }

    /** Test that a null date causes null to be returned. */
    @Test
    public void nullDate() {
        Assert.assertNull(
                ff.function(
                                FormatDateTimezoneFunction.NAME.getFunctionName(),
                                ff.literal(PATTERN),
                                ff.literal(null),
                                ff.literal("UTC"))
                        .evaluate(null));
    }

    /** Test that a null timezone causes null to be returned. */
    @Test
    public void nullTimezone() {
        Assert.assertNull(
                ff.function(
                                FormatDateTimezoneFunction.NAME.getFunctionName(),
                                ff.literal(PATTERN),
                                ff.literal(TIME),
                                ff.literal(null))
                        .evaluate(null));
    }
}
