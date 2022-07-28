package org.geotools.data.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateParserTest {

    static TimeZone defaultTimeZone;

    @BeforeClass
    public static void setupTimeZone() {
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterClass
    public static void clearTimeZone() {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void testLongNumberString() {
        DateParser p = new DateParser();
        assertNull(p.parse("903425011324"));
    }

    @Test
    public void testParseISODateNanoseconds() {
        DateParser p = new DateParser();
        Date d = p.parse("2022-03-27T11:01:55.956478Z");
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        assertEquals(2022, cal.get(Calendar.YEAR));
        assertEquals(2, cal.get(Calendar.MONTH)); // zero based
        assertEquals(27, cal.get(Calendar.DAY_OF_MONTH)); // one based
        assertEquals(11, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(01, cal.get(Calendar.MINUTE));
    }
}
