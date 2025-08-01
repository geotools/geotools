/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

public class ParseTimeFunctionTest {

    public static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testOrdinaryISODurationNotations() {
        assertionForParsedInstance("PT30M", Duration.ofMinutes(30));

        assertionForParsedInstance("PT12H", Duration.ofHours(12));

        assertionForParsedInstance("P4D", Duration.ofDays(4));

        // For following tests we are providing precision in days, since number of days in month/year can be different
        // Month can have 4 days of difference (from 28 to 31 days).
        assertionForParseLocalDateTIme("P1M", Period.ofMonths(1), 4);

        // Year can have 1 day of difference (365 to 366 days)
        assertionForParseLocalDateTIme("P1Y", Period.ofYears(1), 2);
    }

    @Test
    public void testTimestamps() {
        assertionForTimestamp("2025-07-14T12:30:15.000Z", LocalDateTime.of(2025, 07, 14, 12, 30, 15));

        assertionForTimestamp("20250714T12:30:15.000Z", LocalDateTime.of(2025, 07, 14, 12, 30, 15));

        assertionForTimestamp("2025-07-10T10:30", LocalDateTime.of(2025, 07, 10, 10, 30));

        assertionForTimestamp("20250710T10:30", LocalDateTime.of(2025, 07, 10, 10, 30));
    }

    private static void assertionForTimestamp(String expression, LocalDateTime localDateTime) {
        Function parseTime = FF.function("parseTime", FF.literal(expression));
        Date date = (Date) parseTime.evaluate(null);

        assertEquals(localDateTime, date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());
    }

    private static void assertionForParsedInstance(String expression, TemporalAmount timeInThePast) {
        Function parseTime = FF.function("parseTime", FF.literal(expression));
        Date date = (Date) parseTime.evaluate(null);

        Instant generatedDate = Instant.now().minus(timeInThePast).truncatedTo(ChronoUnit.SECONDS);

        Instant evaluatedTime = date.toInstant().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(generatedDate, evaluatedTime);
    }

    private static void assertionForParseLocalDateTIme(
            String expression, TemporalAmount timeInThePast, int daysPrecision) {
        Function parseTime = FF.function("parseTime", FF.literal(expression));
        Date date = (Date) parseTime.evaluate(null);

        LocalDateTime generatedDate = LocalDateTime.now().minus(timeInThePast);

        LocalDateTime evaluatedTime = date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        assertTrue(ChronoUnit.DAYS.between(generatedDate, evaluatedTime) < daysPrecision);
    }
}
