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

import java.awt.Color;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.styling.SLD;
import org.geotools.styling.TextSymbolizerImpl;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class LabelStyleTypeBindingTestImpl extends KMLTestSupport {
    @Test
    public void testType() {
        assertEquals(TextSymbolizerImpl.class, binding(KML.LabelStyleType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.AFTER, binding(KML.LabelStyleType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        String xml = "<LabelStyle>" + "<color>ffff0000</color>" + "</LabelStyle>";

        buildDocument(xml);

        TextSymbolizerImpl text = (TextSymbolizerImpl) parse();
        assertEquals(Color.RED, SLD.color(text.getFill()));
    }
}
