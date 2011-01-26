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
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;


public class SLDGraphicBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(Graphic.class, new SLDGraphicBinding(null).getType());
    }

    public void test() throws Exception {
        SLDMockData.graphic(document, document);

        Graphic graphic = (Graphic) parse();
        assertNotNull(graphic);

        assertEquals(graphic.getMarks().length, 1);

        Mark mark = graphic.getMarks()[0];

        Color c = org.geotools.styling.SLD.color(mark.getFill().getColor());
        assertEquals(Integer.parseInt("12", 16), c.getRed());
        assertEquals(Integer.parseInt("34", 16), c.getGreen());
        assertEquals(Integer.parseInt("56", 16), c.getBlue());

        assertEquals(1, Filters.asInt(graphic.getSize()));
        assertEquals(1, Filters.asInt(graphic.getOpacity()));
        assertEquals(90, Filters.asInt(graphic.getRotation()));
    }
}
