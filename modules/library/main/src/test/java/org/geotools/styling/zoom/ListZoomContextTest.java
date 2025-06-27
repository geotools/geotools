/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.styling.zoom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class ListZoomContextTest {

    static final double EPSILON = 0.0000001;

    @Test
    public void testLevels() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d));

        assertThat(ctxt.getScaleDenominator(0), closeTo(5000000, EPSILON));
    }

    @Test
    public void testNonZeroInitial() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d), 2);

        assertThat(ctxt.getScaleDenominator(2), closeTo(5000000, EPSILON));
    }

    @Test
    public void testBeforeList() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d));

        assertThat(ctxt.getScaleDenominator(-1), is(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testAfterList() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d));

        assertThat(ctxt.getScaleDenominator(1), is(0d));
    }

    @Test
    public void testMultiple() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d));

        assertThat(ctxt.getScaleDenominator(-1), is(Double.POSITIVE_INFINITY));
        assertThat(ctxt.getScaleDenominator(0), closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), closeTo(2000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), closeTo(1000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(3), is(0d));
    }

    @Test
    public void testSingletonRange() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d));

        ScaleRange result = ctxt.getRange(1, 1);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(10_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(2_000_000d));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }

    @Test
    public void testSingletonRangeFirst() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d));

        ScaleRange result = ctxt.getRange(0, 0);

        // assertThat(result, rangeContains(1/EPSILON));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(5_000_000d));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(2_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }

    @Test
    public void testSingletonRangeLast() {

        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d));

        ScaleRange result = ctxt.getRange(2, 2);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(2_000_000d)));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(1_000_000d));
        // assertThat(result, rangeContains(EPSILON));
    }

    @Test
    public void testRange() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(1, 3);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(2_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(1_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(500_000d));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(200_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(100_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }

    @Test
    public void testRangeFirst() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(0, 3);

        // assertThat(result, (rangeContains(1/EPSILON)));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(5_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(2_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(1_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(500_000d));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(200_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(100_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }

    @Test
    public void testRangeLast() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(3, 5);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(2_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1_000_000d)));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(500_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(200_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(100_000d));
        // assertThat(result, (rangeContains(EPSILON)));

    }

    @Test
    public void testRangeOpenStart() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(null, 3);

        MatcherAssert.assertThat(result, TestUtils.rangeContains(1 / EPSILON));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(5_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(2_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(1_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(500_000d));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(200_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(100_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }

    @Test
    public void testRangeOpenEnd() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(3, null);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(2_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1_000_000d)));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(500_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(200_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(100_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(EPSILON));
    }

    @Test
    public void testRangePastStart() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(-1, 3);

        MatcherAssert.assertThat(result, TestUtils.rangeContains(1 / EPSILON));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(5_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(2_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(1_000_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(500_000d));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(200_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(100_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }

    @Test
    public void testRangePastEnd() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(3, 6);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(2_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1_000_000d)));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(500_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(200_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(100_000d));
        MatcherAssert.assertThat(result, TestUtils.rangeContains(EPSILON));
    }

    @Test
    public void testRangeEntirelyPastEnd() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(7, 8);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(2_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(500_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(200_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(100_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }

    @Test
    public void testRangeEntirelyPastStart() {

        ListZoomContext ctxt =
                new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d));

        ScaleRange result = ctxt.getRange(-3, -2);

        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1 / EPSILON)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(5_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(2_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(1_000_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(500_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(200_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(100_000d)));
        MatcherAssert.assertThat(result, Matchers.not(TestUtils.rangeContains(EPSILON)));
    }
}
