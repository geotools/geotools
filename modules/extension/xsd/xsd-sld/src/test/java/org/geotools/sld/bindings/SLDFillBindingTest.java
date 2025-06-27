/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import org.geotools.api.style.Fill;
import org.junit.Test;

public class SLDFillBindingTest extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(Fill.class, new SLDFillBinding(null, null).getType());
    }

    @Test
    public void test() throws Exception {
        SLDMockData.fill(document, document);

        Fill fill = (Fill) parse();
        assertNotNull(fill);
        assertEquals(org.geotools.styling.SLD.opacity(fill), 1, 0d);

        Color c = org.geotools.styling.SLD.color(fill.getColor());
        assertEquals(c.getRed(), integer("12"));
        assertEquals(c.getGreen(), integer("34"));
        assertEquals(c.getBlue(), integer("56"));
    }

    public int integer(String hex) {
        int integer = 0;

        for (int i = 0; i < hex.length(); i++) {
            int k = Integer.parseInt(hex.charAt(hex.length() - i - 1) + "");
            integer += k * Math.pow(16, i);
        }

        return integer;
    }
}
