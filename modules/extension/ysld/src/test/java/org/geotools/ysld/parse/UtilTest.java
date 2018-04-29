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

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import org.junit.Test;
import org.opengis.filter.expression.Literal;

public class UtilTest {
    @Test
    public void testColor() {
        Factory factory = new Factory();

        assertEquals(new Color(0xFF0000), ((Literal) Util.color("FF0000", factory)).getValue());
        assertEquals(new Color(0x00FF00), ((Literal) Util.color("00ff00", factory)).getValue());
        assertEquals(new Color(0x0000FF), ((Literal) Util.color("#0000FF", factory)).getValue());
        assertEquals(new Color(0xFFFF00), ((Literal) Util.color("0xFFFF00", factory)).getValue());
        assertEquals(
                new Color(0x00FFFF),
                ((Literal) Util.color("rgb(0, 255, 255)", factory)).getValue());
        assertEquals(new Color(0xFF00FF), ((Literal) Util.color("f0f", factory)).getValue());
    }
}
