/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch.date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.junit.Test;

public class ElasticsearchDateConverterParseTest {

    static final Logger LOGGER = Logging.getLogger(ElasticsearchDateConverterParseTest.class);

    @Test
    public void testEpochSecond() {
        assertRoundTrip("1522332219", DateFormat.epoch_second);
        assertRoundTrip("0", DateFormat.epoch_second);
        assertRoundTrip("1", DateFormat.epoch_second);
    }

    @Test
    public void testEpochMillis() {
        assertRoundTrip("1522332219321", DateFormat.epoch_second);
        assertRoundTrip("0", DateFormat.epoch_second);
        assertRoundTrip("1", DateFormat.epoch_second);
    }

    @Test
    public void testBasicDate() {
        assertRoundTrip("20181126", DateFormat.basic_date);
    }

    @Test
    public void testBasicDateTime() {
        assertRoundTrip("20181126T121212.123Z", DateFormat.basic_date_time);
        assertRoundTrip("20181126T121212.123+10:00", DateFormat.basic_date_time);
        assertRoundTrip("20181126T121212.123-0800", DateFormat.basic_date_time);
    }

    @Test
    public void testBasicDateTimeNoMillis() {
        assertRoundTrip("20181126T121212Z", DateFormat.basic_date_time_no_millis);
        assertRoundTrip("20181126T121212+01:00", DateFormat.basic_date_time_no_millis);
        assertRoundTrip("20181126T121212+0100", DateFormat.basic_date_time_no_millis);
    }

    @Test
    public void testBasicOrdinalDate() {
        assertRoundTrip("2018363", DateFormat.basic_ordinal_date);
    }

    @Test
    public void testBasicOrdinalDateTime() {
        assertRoundTrip("2018363T121212.1Z", DateFormat.basic_ordinal_date_time);
        assertRoundTrip("2018363T121212.123Z", DateFormat.basic_ordinal_date_time);
        assertRoundTrip("2018363T121212.123456789Z", DateFormat.basic_ordinal_date_time);
        assertRoundTrip("2018363T121212.123+0100", DateFormat.basic_ordinal_date_time);
        assertRoundTrip("2018363T121212.123+01:00", DateFormat.basic_ordinal_date_time);
    }

    @Test
    public void testBasicOrdinateDateTimeNoMillis() {
        assertRoundTrip("2018363T121212Z", DateFormat.basic_ordinal_date_time_no_millis);
        assertRoundTrip("2018363T121212+0100", DateFormat.basic_ordinal_date_time_no_millis);
        assertRoundTrip("2018363T121212+01:00", DateFormat.basic_ordinal_date_time_no_millis);
    }

    @Test
    public void testBasicTime() {
        assertRoundTrip("121212.1Z", DateFormat.basic_time);
        assertRoundTrip("121212.123Z", DateFormat.basic_time);
        assertRoundTrip("121212.123456789Z", DateFormat.basic_time);
        assertRoundTrip("121212.1+0100", DateFormat.basic_time);
        assertRoundTrip("121212.123+0100", DateFormat.basic_time);
        assertRoundTrip("121212.123+01:00", DateFormat.basic_time);
    }

    @Test
    public void testDateOptionalTime() {
        assertRoundTrip("2018-05", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.1", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.123", DateFormat.date_optional_time);
        assertRoundTrip("2021-11-08T23:41:22.7860", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.123456789", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.123Z", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.123456789Z", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.1+0100", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.123+0100", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.1+01:00", DateFormat.date_optional_time);
        assertRoundTrip("2018-05-30T20:21:23.123+01:00", DateFormat.date_optional_time);
        assertRoundTrip("2018-12-1", DateFormat.date_optional_time);
        assertRoundTrip("2018-12-31T10:15:30", DateFormat.date_optional_time);
        assertRoundTrip("2018-12-31T10:15:3", DateFormat.date_optional_time);
        assertRoundTrip("2018-12-31T10:5:30", DateFormat.date_optional_time);
        assertRoundTrip("2018-12-31T1:15:30", DateFormat.date_optional_time);
    }

    @Test
    public void testParseManyFormats() {}

    @Test
    public void testHourMinuteSecondMillis() {
        assertRoundTrip("2018-12-31T12:12:12.1", DateFormat.date_hour_minute_second_millis);
        assertRoundTrip("2018-12-31T12:12:12.123", DateFormat.date_hour_minute_second_millis);
        assertRoundTrip("2018-12-31T12:12:12.1", DateFormat.date_hour_minute_second_millis);
    }

    @Test
    public void testDateHourMinuteSecondFraction() {
        assertRoundTrip("2018-12-31T12:12:12.1", DateFormat.date_hour_minute_second_fraction);
        assertRoundTrip("2018-12-31T12:12:12.123", DateFormat.date_hour_minute_second_fraction);
        assertRoundTrip(
                "2018-12-31T12:12:12.123456789", DateFormat.date_hour_minute_second_fraction);
    }

    @Test
    public void testDateHourMinuteSecond() {
        assertRoundTrip("2018-12-31T12:12:12", DateFormat.date_hour_minute_second);
        assertRoundTrip("2018-12-31T12:12:1", DateFormat.date_hour_minute_second);
    }

    @Test
    public void testDateHourMinute() {
        assertRoundTrip("2018-12-31T12:12", DateFormat.date_hour_minute);
        assertRoundTrip("2018-12-31T8:3", DateFormat.date_hour_minute);
    }

    @Test
    public void testDateHour() {
        assertRoundTrip("2018-12-31T12", DateFormat.date_hour);
        assertRoundTrip("2018-12-31T8", DateFormat.date_hour);
    }

    @Test
    public void testDate() {
        assertRoundTrip("2018-12-31", DateFormat.date);
        assertRoundTrip("18-5-6", DateFormat.date);
        assertRoundTrip("10000-5-6", DateFormat.date);
    }

    @Test
    public void testBasciWeekDateTimeNoMillis() {
        assertRoundTrip("2018W313T121212Z", DateFormat.basic_week_date_time_no_millis);
        assertRoundTrip("2018W313T121212+0100", DateFormat.basic_week_date_time_no_millis);
        assertRoundTrip("2018W313T121212+01:00", DateFormat.basic_week_date_time_no_millis);
    }

    @Test
    public void testBasicWeekDateTime() {
        assertRoundTrip("2018W313T121212.1Z", DateFormat.basic_week_date_time);
        assertRoundTrip("2018W313T121212.123Z", DateFormat.basic_week_date_time);
        assertRoundTrip("2018W313T121212.123456789Z", DateFormat.basic_week_date_time);
        assertRoundTrip("2018W313T121212.123+0100", DateFormat.basic_week_date_time);
        assertRoundTrip("2018W313T121212.123+01:00", DateFormat.basic_week_date_time);
    }

    @Test
    public void testBasicWeekDate() {
        assertRoundTrip("2018W313", DateFormat.basic_week_date);
        assertRoundTrip("1W313", DateFormat.basic_week_date);
        assertRoundTrip("18W313", DateFormat.basic_week_date);
    }

    @Test
    public void testBasicTTimeNoMillis() {
        assertRoundTrip("T121212Z", DateFormat.basic_t_time_no_millis);
        assertRoundTrip("T121212+0100", DateFormat.basic_t_time_no_millis);
        assertRoundTrip("T121212+01:00", DateFormat.basic_t_time_no_millis);
    }

    @Test
    public void testBasicTTime() {
        assertRoundTrip("T121212.1Z", DateFormat.basic_t_time);
        assertRoundTrip("T121212.123Z", DateFormat.basic_t_time);
        assertRoundTrip("T121212.123456789Z", DateFormat.basic_t_time);
        assertRoundTrip("T121212.1+0100", DateFormat.basic_t_time);
        assertRoundTrip("T121212.123+0100", DateFormat.basic_t_time);
        assertRoundTrip("T121212.123+01:00", DateFormat.basic_t_time);
    }

    @Test
    public void testDateTime() {
        assertRoundTrip("2018-12-31T10:15:30.1Z", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.123Z", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.123456789Z", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.1+0100", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.123+0100", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.123+01:00", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.1+01:00", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.11Z", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.11+0100", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:30.11+01:00", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:3.1Z", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:3.123Z", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:3.123456789Z", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:3.1+0100", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:3.123+0100", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:3.123+01:00", DateFormat.date_time);
        assertRoundTrip("2018-12-31T10:15:3.1+01:00", DateFormat.date_time);
    }

    @Test
    public void testDateTimeNoMillis() {
        assertRoundTrip("2018-12-31T10:15:30Z", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:15:30+0100", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:15:30+01:00", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:5:30Z", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:5:30+0100", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:5:30+01:00", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:15:3Z", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:15:3+0100", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T10:15:3+01:00", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T1:15:30Z", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T1:15:30+0100", DateFormat.date_time_no_millis);
        assertRoundTrip("2018-12-31T1:15:30+01:00", DateFormat.date_time_no_millis);
    }

    @Test
    public void testHourMinuteSecondFraction() {
        assertRoundTrip("12:12:12.123", DateFormat.hour_minute_second_fraction);
        assertRoundTrip("12:12:12.123456789", DateFormat.hour_minute_second_fraction);
        assertRoundTrip("12:12:12.1", DateFormat.hour_minute_second_fraction);
        assertRoundTrip("12:12:12.123", DateFormat.hour_minute_second_millis);
        assertRoundTrip("12:12:12.1", DateFormat.hour_minute_second_millis);
    }

    @Test
    public void testHourMinuteSecond() {
        assertRoundTrip("12:12:12", DateFormat.hour_minute_second);
        assertRoundTrip("12:12:01", DateFormat.hour_minute_second);
        assertRoundTrip("12:12:1", DateFormat.hour_minute_second);
    }

    @Test
    public void testHourMinute() {
        assertRoundTrip("12:12", DateFormat.hour_minute);
        assertRoundTrip("12:01", DateFormat.hour_minute);
        assertRoundTrip("12:1", DateFormat.hour_minute);
    }

    @Test
    public void testHour() {
        assertRoundTrip("12", DateFormat.hour);
        assertRoundTrip("01", DateFormat.hour);
        assertRoundTrip("1", DateFormat.hour);
    }

    @Test
    public void testOrdinalDate() {
        assertRoundTrip("2018-128", DateFormat.ordinal_date);
        assertRoundTrip("2018-1", DateFormat.ordinal_date);
    }

    @Test
    public void testOrdinalDateTime() {
        assertRoundTrip("2018-128T10:15:30.1Z", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-128T10:15:30.123Z", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-128T10:15:30.123456789Z", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-128T10:15:30.123+0100", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-128T10:15:30.123+01:00", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-1T10:15:30.1Z", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-1T10:15:30.123Z", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-1T10:15:30.123456789Z", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-1T10:15:30.123+0100", DateFormat.ordinal_date_time);
        assertRoundTrip("2018-1T10:15:30.123+01:00", DateFormat.ordinal_date_time);
    }

    @Test
    public void testOrdinalDateTimeNoMillis() {
        assertRoundTrip("2018-128T10:15:30Z", DateFormat.ordinal_date_time_no_millis);
        assertRoundTrip("2018-128T10:15:30+0100", DateFormat.ordinal_date_time_no_millis);
        assertRoundTrip("2018-128T10:15:30+01:00", DateFormat.ordinal_date_time_no_millis);
        assertRoundTrip("2018-1T10:15:30Z", DateFormat.ordinal_date_time_no_millis);
        assertRoundTrip("2018-1T10:15:30+0100", DateFormat.ordinal_date_time_no_millis);
        assertRoundTrip("2018-1T10:15:30+01:00", DateFormat.ordinal_date_time_no_millis);
    }

    @Test
    public void testTime() {
        assertRoundTrip("10:15:30.1Z", DateFormat.time);
        assertRoundTrip("10:15:30.123Z", DateFormat.time);
        assertRoundTrip("10:15:30.123456789Z", DateFormat.time);
        assertRoundTrip("10:15:30.123+0100", DateFormat.time);
        assertRoundTrip("10:15:30.123+01:00", DateFormat.time);
        assertRoundTrip("1:15:30.1Z", DateFormat.time);
        assertRoundTrip("1:15:30.123Z", DateFormat.time);
        assertRoundTrip("1:15:30.123+0100", DateFormat.time);
        assertRoundTrip("1:15:30.123+01:00", DateFormat.time);
        assertRoundTrip("10:1:30.1Z", DateFormat.time);
        assertRoundTrip("10:1:30.123Z", DateFormat.time);
        assertRoundTrip("10:1:30.123+0100", DateFormat.time);
        assertRoundTrip("10:1:30.123+01:00", DateFormat.time);
        assertRoundTrip("10:15:3.1Z", DateFormat.time);
        assertRoundTrip("10:15:3.123Z", DateFormat.time);
        assertRoundTrip("10:15:3.123+0100", DateFormat.time);
        assertRoundTrip("10:15:3.123+01:00", DateFormat.time);
    }

    @Test
    public void testTimeNoMillis() {
        assertRoundTrip("10:15:30Z", DateFormat.time_no_millis);
        assertRoundTrip("10:15:30+0100", DateFormat.time_no_millis);
        assertRoundTrip("10:15:30+01:00", DateFormat.time_no_millis);
        assertRoundTrip("01:15:30Z", DateFormat.time_no_millis);
        assertRoundTrip("01:15:30+0100", DateFormat.time_no_millis);
        assertRoundTrip("01:15:30+01:00", DateFormat.time_no_millis);
        assertRoundTrip("1:15:30Z", DateFormat.time_no_millis);
        assertRoundTrip("1:15:30+0100", DateFormat.time_no_millis);
        assertRoundTrip("1:15:30+01:00", DateFormat.time_no_millis);
        assertRoundTrip("10:5:30Z", DateFormat.time_no_millis);
        assertRoundTrip("10:5:30+0100", DateFormat.time_no_millis);
        assertRoundTrip("10:5:30+01:00", DateFormat.time_no_millis);
        assertRoundTrip("10:15:3Z", DateFormat.time_no_millis);
        assertRoundTrip("10:15:3+0100", DateFormat.time_no_millis);
        assertRoundTrip("10:15:3+01:00", DateFormat.time_no_millis);
    }

    @Test
    public void testTTime() {
        assertRoundTrip("T10:15:30.1Z", DateFormat.t_time);
        assertRoundTrip("T10:15:30.123Z", DateFormat.t_time);
        assertRoundTrip("T10:15:30.123456789Z", DateFormat.t_time);
        assertRoundTrip("T10:15:30.1+0100", DateFormat.t_time);
        assertRoundTrip("T10:15:30.123+0100", DateFormat.t_time);
        assertRoundTrip("T10:15:30.123+01:00", DateFormat.t_time);
        assertRoundTrip("T10:15:30.1+01:00", DateFormat.t_time);
        assertRoundTrip("T1:15:30.123Z", DateFormat.t_time);
        assertRoundTrip("T1:15:30.123+0100", DateFormat.t_time);
        assertRoundTrip("T1:15:30.123+01:00", DateFormat.t_time);
        assertRoundTrip("T10:1:30.123Z", DateFormat.t_time);
        assertRoundTrip("T10:1:30.123+0100", DateFormat.t_time);
        assertRoundTrip("T10:1:30.123+01:00", DateFormat.t_time);
        assertRoundTrip("T10:15:3.123Z", DateFormat.t_time);
        assertRoundTrip("T10:15:3.123+0100", DateFormat.t_time);
        assertRoundTrip("T10:15:3.123+01:00", DateFormat.t_time);
    }

    @Test
    public void testTTimeNoMillis() {
        assertRoundTrip("T10:15:30Z", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:15:30+0100", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:15:30+01:00", DateFormat.t_time_no_millis);
        assertRoundTrip("T1:15:30Z", DateFormat.t_time_no_millis);
        assertRoundTrip("T1:15:30+0100", DateFormat.t_time_no_millis);
        assertRoundTrip("T1:15:30+01:00", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:1:30Z", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:1:30+0100", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:1:30+01:00", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:15:3Z", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:15:3+0100", DateFormat.t_time_no_millis);
        assertRoundTrip("T10:15:3+01:00", DateFormat.t_time_no_millis);
    }

    @Test
    public void testWeekDate() {
        assertRoundTrip("2012-W48-6", DateFormat.week_date);
        assertRoundTrip("2012-W01-6", DateFormat.week_date);
        assertRoundTrip("2012-W1-6", DateFormat.week_date);
    }

    @Test
    public void testWeekDateTime() {
        assertRoundTrip("2012-W48-6T10:15:30.1Z", DateFormat.week_date_time);
        assertRoundTrip("2012-W48-6T10:15:30.123Z", DateFormat.week_date_time);
        assertRoundTrip("2012-W48-6T10:15:30.123456789Z", DateFormat.week_date_time);
        assertRoundTrip("2012-W48-6T10:15:30.1+0100", DateFormat.week_date_time);
        assertRoundTrip("2012-W48-6T10:15:30.123+0100", DateFormat.week_date_time);
        assertRoundTrip("2012-W48-6T10:15:30.1+01:00", DateFormat.week_date_time);
        assertRoundTrip("2012-W48-6T10:15:30.123+01:00", DateFormat.week_date_time);
        assertRoundTrip("2012-W1-6T10:15:30.1Z", DateFormat.week_date_time);
        assertRoundTrip("2012-W1-6T10:15:30.123Z", DateFormat.week_date_time);
        assertRoundTrip("2012-W1-6T10:15:30.1+0100", DateFormat.week_date_time);
        assertRoundTrip("2012-W1-6T10:15:30.123+0100", DateFormat.week_date_time);
        assertRoundTrip("2012-W1-6T10:15:30.1+01:00", DateFormat.week_date_time);
        assertRoundTrip("2012-W1-6T10:15:30.123+01:00", DateFormat.week_date_time);
    }

    @Test
    public void testWeekDateTimeNoMillis() {
        assertRoundTrip("2012-W48-6T10:15:30Z", DateFormat.week_date_time_no_millis);
        assertRoundTrip("2012-W48-6T10:15:30+0100", DateFormat.week_date_time_no_millis);
        assertRoundTrip("2012-W48-6T10:15:30+01:00", DateFormat.week_date_time_no_millis);
        assertRoundTrip("2012-W1-6T10:15:30Z", DateFormat.week_date_time_no_millis);
        assertRoundTrip("2012-W1-6T10:15:30+0100", DateFormat.week_date_time_no_millis);
        assertRoundTrip("2012-W1-6T10:15:30+01:00", DateFormat.week_date_time_no_millis);
    }

    @Test
    public void testYearMonth() {
        assertRoundTrip("2012-12", DateFormat.year_month);
        assertRoundTrip("1-1", DateFormat.year_month);
    }

    @Test
    public void testYear() {
        assertRoundTrip("2012", DateFormat.year);
        assertRoundTrip("1", DateFormat.year);
        assertRoundTrip("-2000", DateFormat.year);
    }

    @Test
    public void testYearMonthDay() {
        assertRoundTrip("2012-12-31", DateFormat.year_month_day);
        assertRoundTrip("1-12-31", DateFormat.year_month_day);
        assertRoundTrip("2012-1-31", DateFormat.year_month_day);
        assertRoundTrip("2012-12-1", DateFormat.year_month_day);
    }

    private void assertRoundTrip(String date, DateFormat dateFormat) {
        ElasticsearchDateConverter converter = ElasticsearchDateConverter.of(dateFormat);
        Date parsed = converter.parse(date);
        assertNotNull(parsed);
        String formatted = converter.format(parsed);
        LOGGER.info(date + " -> " + formatted);
        Date parsed2 = converter.parse(formatted);
        assertEquals(parsed, parsed2);
    }
}
