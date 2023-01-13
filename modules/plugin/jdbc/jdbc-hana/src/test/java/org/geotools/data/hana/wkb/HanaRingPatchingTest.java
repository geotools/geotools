/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/** @author Stefan Uhrig, SAP SE */
public class HanaRingPatchingTest {

    @Test
    public void testRingPatching() throws ParseException, HanaWKBParserException {
        WKTReader reader = new WKTReader();
        Geometry expected = reader.read("POLYGON((1 2, 1 2, 1 2, 1 2))");

        // POLYGON((1 2, 1 2)) in little-endian WKB
        byte[] wkb = {
            0x01,
            0x03,
            0x00,
            0x00,
            0x00,
            0x01,
            0x00,
            0x00,
            0x00,
            0x02,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            (byte) 0xF0,
            0x3F,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x40,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            (byte) 0xF0,
            0x3F,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x40
        };
        HanaWKBParser parser = new HanaWKBParser(new GeometryFactory());
        Geometry actual = parser.parse(wkb);
        Assert.assertEquals(0, actual.compareTo(expected));
    }
}
