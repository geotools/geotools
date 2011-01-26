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

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;


public class LatLonBoxTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(Envelope.class, binding(KML.LatLonBoxType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.LatLonBoxType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<LatLonBox>" + "<north>1</north>" + "<south>-1</south>" + "<east>1</east>"
            + "<west>-1</west>" + "</LatLonBox>";

        buildDocument(xml);

        Envelope box = (Envelope) parse();
        assertEquals(box.getMinX(), -1d, 0.1);
        assertEquals(box.getMaxX(), 1d, 0.1);
        assertEquals(box.getMinY(), -1d, 0.1);
        assertEquals(box.getMaxY(), 1d, 0.1);
    }
}
