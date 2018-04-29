/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class DateRangeVisitorTest {

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    private static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private SimpleDateFormat df;

    private SimpleFeatureBuilder fb;

    @Before
    public void setup() {
        df = new SimpleDateFormat(UTC_PATTERN);
        df.setTimeZone(UTC_TIME_ZONE);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("start", Date.class);
        tb.add("end", Date.class);
        tb.setName("test");
        SimpleFeatureType featureType = tb.buildFeatureType();

        fb = new SimpleFeatureBuilder(featureType);
    }

    SimpleFeature buildRangeFeature(String from, String to) throws ParseException {
        fb.add(df.parse(from));
        fb.add(df.parse(to));
        return fb.buildFeature(null);
    }

    @Test
    public void testSingleRange() throws ParseException {
        DateRangeVisitor visitor = new DateRangeVisitor("start", "end");
        visitor.visit(buildRangeFeature("2008-10-31T00:00:00.000Z", "2008-11-01T00:00:00.000Z"));

        Set<String> range = visitor.getRange();
        assertEquals(1, range.size());
        assertEquals(
                "2008-10-31T00:00:00.000Z/2008-11-01T00:00:00.000Z/PT1S", range.iterator().next());
    }

    @Test
    public void testTwoRanges() throws ParseException {
        DateRangeVisitor visitor = new DateRangeVisitor("start", "end");
        visitor.visit(buildRangeFeature("2008-10-31T00:00:00.000Z", "2008-11-01T00:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-11-05T00:00:00.000Z", "2008-11-07T00:00:00.000Z"));

        List<String> range = new ArrayList<String>(visitor.getRange());
        assertEquals(2, range.size());
        assertEquals("2008-10-31T00:00:00.000Z/2008-11-01T00:00:00.000Z/PT1S", range.get(0));
        assertEquals("2008-11-05T00:00:00.000Z/2008-11-07T00:00:00.000Z/PT1S", range.get(1));
    }

    @Test
    public void testThreeRangesOverlapFirst() throws ParseException {
        DateRangeVisitor visitor = new DateRangeVisitor("start", "end");
        visitor.visit(buildRangeFeature("2008-10-28T00:00:00.000Z", "2008-10-31T12:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-10-31T00:00:00.000Z", "2008-11-01T00:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-11-05T00:00:00.000Z", "2008-11-07T00:00:00.000Z"));

        List<String> range = new ArrayList<String>(visitor.getRange());
        assertEquals(2, range.size());
        assertEquals("2008-10-28T00:00:00.000Z/2008-11-01T00:00:00.000Z/PT1S", range.get(0));
        assertEquals("2008-11-05T00:00:00.000Z/2008-11-07T00:00:00.000Z/PT1S", range.get(1));
    }

    @Test
    public void testThreeRangesOverlapLast() throws ParseException {
        DateRangeVisitor visitor = new DateRangeVisitor("start", "end");
        visitor.visit(buildRangeFeature("2008-10-31T00:00:00.000Z", "2008-11-01T00:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-11-05T00:00:00.000Z", "2008-11-07T00:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-11-06T00:00:00.000Z", "2008-11-09T12:00:00.000Z"));

        List<String> range = new ArrayList<String>(visitor.getRange());
        assertEquals(2, range.size());
        assertEquals("2008-10-31T00:00:00.000Z/2008-11-01T00:00:00.000Z/PT1S", range.get(0));
        assertEquals("2008-11-05T00:00:00.000Z/2008-11-09T12:00:00.000Z/PT1S", range.get(1));
    }

    @Test
    public void testFourRanges() throws ParseException {
        DateRangeVisitor visitor = new DateRangeVisitor("start", "end");
        visitor.visit(buildRangeFeature("2008-10-28T00:00:00.000Z", "2008-10-31T12:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-10-31T00:00:00.000Z", "2008-11-01T00:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-11-05T00:00:00.000Z", "2008-11-07T00:00:00.000Z"));
        visitor.visit(buildRangeFeature("2008-11-06T00:00:00.000Z", "2008-11-09T12:00:00.000Z"));

        List<String> range = new ArrayList<String>(visitor.getRange());
        assertEquals(2, range.size());
        assertEquals("2008-10-28T00:00:00.000Z/2008-11-01T00:00:00.000Z/PT1S", range.get(0));
        assertEquals("2008-11-05T00:00:00.000Z/2008-11-09T12:00:00.000Z/PT1S", range.get(1));
    }
}
