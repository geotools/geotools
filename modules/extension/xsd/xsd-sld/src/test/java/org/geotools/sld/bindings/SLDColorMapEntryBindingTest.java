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

import java.awt.Color;
import org.geotools.filter.Filters;
import org.geotools.styling.ColorMapEntry;


public class SLDColorMapEntryBindingTest extends SLDTestSupport {
    public void testType() {
        assertEquals(ColorMapEntry.class, new SLDColorMapEntryBinding(null, null).getType());
    }

    public void testNormal() throws Exception {
        document.appendChild(document.createElementNS(SLD.NAMESPACE, "ColorMapEntry"));

        document.getDocumentElement().setAttributeNS(SLD.NAMESPACE, "color", "#000000");
        document.getDocumentElement().setAttributeNS(SLD.NAMESPACE, "opacity", "1.2");
        document.getDocumentElement().setAttributeNS(SLD.NAMESPACE, "quantity", "3.4");
        document.getDocumentElement().setAttributeNS(SLD.NAMESPACE, "label", "someLabel");

        ColorMapEntry entry = (ColorMapEntry) parse();
        assertNotNull(entry);
        assertEquals(org.geotools.styling.SLD.color(entry.getColor()), Color.BLACK);
        assertEquals(Filters.asDouble(entry.getOpacity()), 1.2d, 0d);
        assertEquals(Filters.asDouble(entry.getQuantity()), 3.4d, 0d);
        assertEquals(entry.getLabel(), "someLabel");
    }
}
