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
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.junit.Test;

public class SLDColorMapImplBindingTest extends SLDTestSupport {
    @Test
    public void testType() {
        assertEquals(ColorMap.class, new SLDColorMapBinding(null).getType());
    }

    @Test
    public void testNormal() throws Exception {
        SLDMockData.colorMap(document, document);

        ColorMap colorMap = (ColorMap) parse();
        assertNotNull(colorMap);

        ColorMapEntry[] entries = colorMap.getColorMapEntries();

        assertNotNull(entries);
        assertEquals(2, entries.length);
        assertEquals(org.geotools.styling.SLD.color(entries[0].getColor()), Color.BLACK);
        assertEquals(org.geotools.styling.SLD.color(entries[1].getColor()), Color.WHITE);
    }
}
