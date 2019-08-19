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

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.GML;
import org.geotools.referencing.CRS;
import org.geotools.xsd.Binding;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.w3c.dom.Document;

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

        assertEquals("foo", ((PropertyName) box.getExpression1()).getPropertyName());
        assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(0, 1, 0, 1, CRS.decode("EPSG:4326")),
                        box.getBounds(),
                        1e-6));
    }

    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.bbox(), OGC.BBOX);

        assertEquals(
                1,
                doc.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1, doc.getElementsByTagNameNS(GML.NAMESPACE, GML.Box.getLocalPart()).getLength());
    }
}
