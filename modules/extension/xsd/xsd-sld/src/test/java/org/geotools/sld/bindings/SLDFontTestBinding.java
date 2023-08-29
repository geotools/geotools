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

import org.geotools.api.style.Font;
import org.geotools.filter.Filters;
import org.junit.Test;

public class SLDFontTestBinding extends SLDTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(Font.class, new SLDFontBinding(null, null).getType());
    }

    @Test
    public void test() throws Exception {
        SLDMockData.font(document, document);

        Font font = (Font) parse();
        assertNotNull(font);
        assertEquals("Arial", Filters.asString(font.getFamily().get(0)));
        assertEquals("normal", Filters.asString(font.getStyle()));
        assertEquals("normal", Filters.asString(font.getWeight()));
        assertEquals(14, Filters.asInt(font.getSize()));
    }

    @Test
    public void testDefaultFont() throws Exception {
        SLDMockData.element(SLD.FONT, document, document);
        Font font = (Font) parse();
        assertNotNull(font);
        assertEquals("Serif", Filters.asString(font.getFamily().get(0)));
        assertEquals("normal", Filters.asString(font.getStyle()));
        assertEquals("normal", Filters.asString(font.getWeight()));
        assertEquals(10, Filters.asInt(font.getSize()));
    }
}
