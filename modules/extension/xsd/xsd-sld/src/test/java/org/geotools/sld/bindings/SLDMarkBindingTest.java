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
import org.geotools.styling.Mark;


public class SLDMarkBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(Mark.class, new SLDMarkBinding(null, null).getType());
    }

    public void testWithoutStroke() throws Exception {
        SLDMockData.mark(document, document);

        Mark mark = (Mark) parse();
        assertNotNull(mark);

        assertNotNull(mark.getFill());

        Color c = org.geotools.styling.SLD.color(mark.getFill().getColor());

        assertEquals(Integer.parseInt("12", 16), c.getRed());
        assertEquals(Integer.parseInt("34", 16), c.getGreen());
        assertEquals(Integer.parseInt("56", 16), c.getBlue());

        assertNotNull(mark.getWellKnownName());
        assertEquals(Filters.asString(mark.getWellKnownName()), "wellKnownName");
    }
}
