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
package org.geotools.kml.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.SLD;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class PolyStyleTypeBindingTestImpl extends KMLTestSupport {
    @Test
    public void testType() {
        assertEquals(PolygonSymbolizerImpl.class, binding(KML.PolyStyleType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.AFTER, binding(KML.PolyStyleType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        String xml =
                "<PolyStyle>"
                        + "<color>ffff0000</color>"
                        + "<outline>1</outline>"
                        + "<fill>1</fill>"
                        + "</PolyStyle>";

        buildDocument(xml);

        PolygonSymbolizerImpl poly = (PolygonSymbolizerImpl) parse();
        assertEquals(Color.RED, SLD.color(poly.getFill()));
        assertEquals(1, SLD.width(poly.getStroke()));

        xml =
                "<PolyStyle>"
                        + "<color>ffff0000</color>"
                        + "<outline>0</outline>"
                        + "<fill>0</fill>"
                        + "</PolyStyle>";

        buildDocument(xml);
        poly = (PolygonSymbolizerImpl) parse();
        assertNull(poly.getFill());
        assertNull(poly.getStroke());

        xml = "<PolyStyle>" + "</PolyStyle>";

        buildDocument(xml);
        poly = (PolygonSymbolizerImpl) parse();
        assertEquals(Color.WHITE, SLD.color(poly.getFill()));
        assertEquals(1, SLD.width(poly.getStroke()));
    }
}
