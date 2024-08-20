/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class WellKnownZoomContextFinderTest {

    final double[] WGS84_SCALE_DENOMS = {
        559_082_263.9508929d,
        279_541_132.0143589d,
        139_770_566.00717944d,
        69_885_283.00358972d,
        34_942_641.50179486d,
        17_471_320.75089743d,
        8_735_660.375448715d,
        4_367_830.1877243575d,
        2_183_915.0938621787d,
        1_091_957.5469310894d,
        545_978.7734655447d,
        272_989.38673277234d,
        136_494.69336638617d,
        68_247.34668319309d,
        34_123.67334159654d,
        17_061.83667079827d,
        8_530.918335399136d,
        4_265.459167699568d,
        2_132.729583849784d,
        1_066.364791924892d,
        533.182395962446d
    };

    @Test
    public void testWGS84Scales() throws Exception {
        ZoomContext context = WellKnownZoomContextFinder.getInstance().get("DEFAULT");

        for (int i = 0; i < WGS84_SCALE_DENOMS.length; i++) {
            assertThat(
                    context.getScaleDenominator(i), mCloseTo(WGS84_SCALE_DENOMS[i], 0.00000001d));
        }
    }

    Matcher<Double> mCloseTo(final double value, final double epsilon) {
        return new BaseMatcher<Double>() {

            @Override
            public boolean matches(Object arg0) {
                return Math.abs(value / (Double) arg0 - 1) <= epsilon;
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("divided by ")
                        .appendValue(value)
                        .appendText(" within ")
                        .appendValue(epsilon)
                        .appendText(" of 1.");
            }
        };
    }
}
