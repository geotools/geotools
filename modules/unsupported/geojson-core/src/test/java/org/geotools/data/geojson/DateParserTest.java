package org.geotools.data.geojson;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DateParserTest {

    @Test
    public void testLongNumberString() {
        DateParser p = new DateParser();
        assertNull(p.parse("903425011324"));
    }
}
