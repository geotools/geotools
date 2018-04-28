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
package org.geotools.ysld.parse;

import static org.geotools.ysld.TestUtils.rangeContains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Test;

public class RatioZoomContextTest {

    static final double EPSILON = 0.0000001;

    @Test
    public void testLevels() {

        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2);

        assertThat(ctxt.getScaleDenominator(0), Matchers.closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), Matchers.closeTo(5000000 / 2, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), Matchers.closeTo(5000000 / 4, EPSILON));
    }

    @Test
    public void testNonIntegerRatio() {

        RatioZoomContext ctxt = new RatioZoomContext(5000000, 1.5);

        assertThat(ctxt.getScaleDenominator(0), Matchers.closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), Matchers.closeTo(5000000 / 1.5, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), Matchers.closeTo(5000000 / (1.5 * 1.5), EPSILON));
    }

    @Test
    public void testNonZeroInitial() {

        RatioZoomContext ctxt = new RatioZoomContext(2, 5000000, 2);

        assertThat(ctxt.getScaleDenominator(0), Matchers.closeTo(5000000 * 4, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), Matchers.closeTo(5000000 * 2, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), Matchers.closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(3), Matchers.closeTo(5000000 / 2, EPSILON));
        assertThat(ctxt.getScaleDenominator(4), Matchers.closeTo(5000000 / 4, EPSILON));
    }

    @Test
    public void testSingletonRangeInitial() {

        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2);

        ScaleRange result = ctxt.getRange(0, 0);

        assertThat(result, rangeContains(5_000_000d));
        assertThat(result, not(rangeContains(5_000_000d * 2)));
        assertThat(result, not(rangeContains(5_000_000d / 2)));
    }

    @Test
    public void testSingletonRange() {

        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2);

        ScaleRange result = ctxt.getRange(2, 2);

        assertThat(result, rangeContains(5_000_000d / 4));
        assertThat(result, not(rangeContains(5_000_000d / 2)));
        assertThat(result, not(rangeContains(5_000_000d / 8)));
    }

    @Test
    public void testRange() {

        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2);

        ScaleRange result = ctxt.getRange(0, 2);

        assertThat(result, rangeContains(5_000_000d / 1));
        assertThat(result, rangeContains(5_000_000d / 2));
        assertThat(result, rangeContains(5_000_000d / 4));
        assertThat(result, not(rangeContains(5_000_000d * 2)));
        assertThat(result, not(rangeContains(5_000_000d / 8)));
    }
}
