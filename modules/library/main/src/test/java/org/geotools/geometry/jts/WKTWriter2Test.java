/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

public class WKTWriter2Test {

    /** Draw a circle between the start and end point; or each group of three their after. */
    @Test
    public void circularString() throws Exception {
        testRoundTrip(
                "CIRCULARSTRING (220268.439465645 150415.359530563, 220227.333322076 150505.561285879, 220227.353105332 150406.434743975)");
        testRoundTrip(
                "CIRCULARSTRING (143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, "
                        + "145.96132309891922 -34.985671061528784, 149.57565307617188 -33.41153335571289, 149.41972407584802 -29.824672680573517, "
                        + "146.1209416055467 -30.19711586270431, 143.62025166838282 -30.037497356076827)");
        testRoundTrip(
                "CIRCULARSTRING (143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 143.62025166838282 -30.037497356076827)");
        testRoundTrip("CIRCULARSTRING EMPTY");
    }

    @Test
    public void compoundCurve() throws Exception {
        testRoundTrip(
                "COMPOUNDCURVE ((153.72942375 -27.2175704, 152.29285719 -29.23940482, 154.74034096 -30.51635287), "
                        + "CIRCULARSTRING (154.74034096 -30.51635287, 154.74034096 -30.51635287, 152.39926953 -32.16574411, 155.11278414 -34.08116619, 151.86720784 -35.62414508))");
        testRoundTrip(
                "COMPOUNDCURVE ((153.72942375 -27.2175704, 152.29285719 -29.23940482, 154.74034096 -30.51635287))");
        testRoundTrip(
                "COMPOUNDCURVE (CIRCULARSTRING (154.74034096 -30.51635287, 154.74034096 -30.51635287, 152.39926953 -32.16574411, "
                        + "155.11278414 -34.08116619, 151.86720784 -35.62414508))");
        testRoundTrip("COMPOUNDCURVE EMPTY");
    }

    @Test
    public void curvePolygon() throws Exception {
        testRoundTrip(
                "CURVEPOLYGON (CIRCULARSTRING (143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 143.62025166838282 -30.037497356076827))");
        testRoundTrip(
                "CURVEPOLYGON ((144.84399355252685 -31.26123924022086, 144.20551952601693 -32.27215644886158, 145.55230712890625 -33.49203872680664, "
                        + "147.97080993652344 -32.03618621826172, 146.38697244992585 -31.47406391572417, 144.84399355252685 -31.26123924022086))");
        testRoundTrip(
                "CURVEPOLYGON ("
                        + "CIRCULARSTRING (143.62025166838282 -30.037497356076827, 142.92857147299705 -32.75101196874403, 145.96132309891922 "
                        + "-34.985671061528784, 149.57565307617188 -33.41153335571289, 149.41972407584802 -29.824672680573517, 146.1209416055467 "
                        + "-30.19711586270431, 143.62025166838282 -30.037497356076827), "
                        + "(144.84399355252685 -31.26123924022086, 144.20551952601693 -32.27215644886158, 145.55230712890625 -33.49203872680664, "
                        + "147.97080993652344 -32.03618621826172, 146.38697244992585 -31.47406391572417, 144.84399355252685 -31.26123924022086))");
        testRoundTrip(
                "CURVEPOLYGON (COMPOUNDCURVE (CIRCULARSTRING (0 0, 2 0, 2 1, 2 3, 4 3), (4 3, 4 5, 1 4, 0 0)), CIRCULARSTRING (1.7 1, 1.4 0.4, 1.6 0.4, 1.6 0.5, 1.7 1))");
        testRoundTrip("CURVEPOLYGON EMPTY");
    }

    @Test
    public void multiCurve() throws Exception {
        testRoundTrip("MULTICURVE ((0 0, 5 5), CIRCULARSTRING (4 0, 4 4, 8 4))");
        testRoundTrip(
                "MULTICURVE ((100 100, 120 120), COMPOUNDCURVE (CIRCULARSTRING (0 0, 2 0, 2 1, 2 3, 4 3), (4 3, 4 5, 1 4, 0 0)))");
        testRoundTrip("MULTICURVE EMPTY");
    }

    @Test
    public void multiSurface() throws Exception {
        testRoundTrip(
                "MULTISURFACE (CURVEPOLYGON ("
                        + "COMPOUNDCURVE ((6 10, 10 1, 14 10), CIRCULARSTRING (14 10, 10 14, 6 10)), COMPOUNDCURVE ((13 10, 10 2, 7 10), CIRCULARSTRING (7 10, 10 13, 13 10))), "
                        + "CURVEPOLYGON (COMPOUNDCURVE ((106 110, 110 101, 114 110), CIRCULARSTRING (114 110, 110 114, 106 110))))");
        testRoundTrip("MULTISURFACE EMPTY");
    }

    private void testRoundTrip(String wkt, String expectedWkt) throws ParseException {
        WKTReader2 reader = new WKTReader2(0.2);
        Geometry geometry = reader.read(wkt);
        WKTWriter2 writer = new WKTWriter2();
        String wkt2 = writer.write(geometry);

        assertEquals(expectedWkt, wkt2);
    }

    private void testRoundTrip(String wkt) throws ParseException {
        testRoundTrip(wkt, wkt);
    }
}
