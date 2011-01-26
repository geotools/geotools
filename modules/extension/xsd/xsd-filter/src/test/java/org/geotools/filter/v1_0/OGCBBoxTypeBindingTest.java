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
package org.geotools.filter.v1_0;

import org.w3c.dom.Document;
import org.opengis.filter.spatial.BBOX;
import org.geotools.gml2.GML;
import org.geotools.xml.Binding;


public class OGCBBoxTypeBindingTest extends FilterTestSupport {
    public void testType() {
        assertEquals(BBOX.class, binding(OGC.BBOXType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.BBOXType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.bbox(document, document);

        BBOX box = (BBOX) parse();

        assertEquals("foo", box.getPropertyName());
        assertEquals(0, box.getMinX(), 0.0);
        assertEquals(0, box.getMinY(), 0.0);
        assertEquals(1, box.getMaxX(), 0.0);
        assertEquals(1, box.getMaxY(), 0.0);
        assertEquals("EPSG:4326", box.getSRS());
    }

    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.bbox(), OGC.BBOX);

        assertEquals(1,
            doc.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            doc.getElementsByTagNameNS(GML.NAMESPACE, GML.Box.getLocalPart()).getLength());
    }
}
