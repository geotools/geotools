/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.wkb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/** @author Stefan Uhrig, SAP SE */
@RunWith(Parameterized.class)
public class HanaWKBTest {

    private static class Parameter {

        private int hexToBin(char c) {
            int ret = Character.digit(c, 16);
            if (ret == -1) {
                throw new IllegalArgumentException("Invalid hex character");
            }
            return ret;
        }

        private byte[] decodeHex(String s) {
            int l = s.length();
            if (l % 2 != 0) throw new IllegalArgumentException("Invalid hexstring length");
            byte[] ret = new byte[l / 2];
            for (int i = 0; i < l; i += 2) {
                int hi = hexToBin(s.charAt(i));
                int lo = hexToBin(s.charAt(i + 1));
                ret[i / 2] = (byte) (16 * hi + lo);
            }
            return ret;
        }

        public Parameter(String wkt, String hexwkb) {
            this.wkt = wkt;
            this.wkb = decodeHex(hexwkb);
        }

        private String wkt;

        private byte[] wkb;

        public String getWKT() {
            return wkt;
        }

        public byte[] getWKB() {
            return wkb;
        }
    }

    private static final Parameter[] PARAMETERS = {
        new Parameter("POINT(1 2)", "0101000000000000000000F03F0000000000000040"),
        new Parameter("LINESTRING EMPTY", "010200000000000000"),
        new Parameter(
                "LINESTRING(1 2, 3 4)",
                "010200000002000000000000000000F03F000000000000004000000000000008400000000000001040"),
        new Parameter("POLYGON EMPTY", "010300000000000000"),
        new Parameter(
                "POLYGON((0 0, 4 0, 4 4, 0 4, 0 0))",
                "010300000001000000050000000000000000000000000000000000000000000000000010400000000000000000000000000000104000000000000010400000000000000000000000000000104000000000000000000000000000000000"),
        new Parameter(
                "POLYGON((0 0, 4 0, 4 4, 0 4, 0 0),(1 1, 1 3, 3 3, 3 1, 1 1))",
                "01030000000200000005000000000000000000000000000000000000000000000000001040000000000000000000000000000010400000000000001040000000000000000000000000000010400000000000000000000000000000000005000000000000000000F03F000000000000F03F000000000000F03F0000000000000840000000000000084000000000000008400000000000000840000000000000F03F000000000000F03F000000000000F03F"),
        new Parameter(
                "POLYGON((0 0, 10 0, 10 10, 0 10, 0 0), (1 1, 1 3, 3 3, 3 1, 1 1), (7 7, 7 9, 9 9, 9 7, 7 7))",
                "01030000000300000005000000000000000000000000000000000000000000000000002440000000000000000000000000000024400000000000002440000000000000000000000000000024400000000000000000000000000000000005000000000000000000F03F000000000000F03F000000000000F03F0000000000000840000000000000084000000000000008400000000000000840000000000000F03F000000000000F03F000000000000F03F050000000000000000001C400000000000001C400000000000001C4000000000000022400000000000002240000000000000224000000000000022400000000000001C400000000000001C400000000000001C40"),
        new Parameter("MULTIPOINT EMPTY", "010400000000000000"),
        new Parameter(
                "MULTIPOINT((0 0),(1 1))",
                "0104000000020000000101000000000000000000000000000000000000000101000000000000000000F03F000000000000F03F"),
        new Parameter("MULTILINESTRING EMPTY", "010500000000000000"),
        new Parameter(
                "MULTILINESTRING((0 0, 1 1), EMPTY, (2 2, 3 3))",
                "01050000000300000001020000000200000000000000000000000000000000000000000000000000F03F000000000000F03F0102000000000000000102000000020000000000000000000040000000000000004000000000000008400000000000000840"),
        new Parameter("MULTIPOLYGON EMPTY", "010600000000000000"),
        new Parameter(
                "MULTIPOLYGON(((0 0, 4 0, 4 4, 0 4, 0 0)),EMPTY,((0 0, 4 0, 4 4, 0 4, 0 0),(1 1, 1 3, 3 3, 3 1, 1 1)))",
                "01060000000300000001030000000100000005000000000000000000000000000000000000000000000000001040000000000000000000000000000010400000000000001040000000000000000000000000000010400000000000000000000000000000000001030000000000000001030000000200000005000000000000000000000000000000000000000000000000001040000000000000000000000000000010400000000000001040000000000000000000000000000010400000000000000000000000000000000005000000000000000000F03F000000000000F03F000000000000F03F0000000000000840000000000000084000000000000008400000000000000840000000000000F03F000000000000F03F000000000000F03F"),
        new Parameter("GEOMETRYCOLLECTION EMPTY", "010700000000000000"),
        new Parameter(
                "GEOMETRYCOLLECTION(POINT(1 1), LINESTRING EMPTY, GEOMETRYCOLLECTION(GEOMETRYCOLLECTION EMPTY), POLYGON((0 0,1 0,1 1,0 0)))",
                "0107000000040000000101000000000000000000F03F000000000000F03F0102000000000000000107000000010000000107000000000000000103000000010000000400000000000000000000000000000000000000000000000000F03F0000000000000000000000000000F03F000000000000F03F00000000000000000000000000000000")
    };

    @Parameters
    public static Object[] data() {
        return PARAMETERS;
    }

    public HanaWKBTest(Parameter param) {
        this.param = param;
    }

    private Parameter param;

    @Test
    public void testWKBWriter() throws ParseException, HanaWKBWriterException {
        WKTReader reader = new WKTReader();
        Geometry geometry = reader.read(param.getWKT());
        byte[] actual = HanaWKBWriter.write(geometry, 2);
        Assert.assertArrayEquals(param.getWKB(), actual);
    }

    @Test
    public void testWKBParser() throws ParseException, HanaWKBParserException {
        WKTReader reader = new WKTReader();
        HanaWKBParser parser = new HanaWKBParser(new GeometryFactory());
        Geometry expected = reader.read(param.getWKT());
        Geometry actual = parser.parse(param.getWKB());
        Assert.assertEquals(0, actual.compareTo(expected));
    }
}
