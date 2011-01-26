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
package org.geotools.filter.v1_1;

import org.w3c.dom.Document;
import org.opengis.filter.identity.GmlObjectId;
import org.geotools.gml3.GML;
import org.geotools.xml.Binding;


public class GmlObjectIdTypeBindingTest extends FilterTestSupport {
    public void testType() {
        assertEquals(GmlObjectId.class, binding(OGC.GmlObjectIdType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.GmlObjectIdType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.gmlObjectId(document, document);

        GmlObjectId id = (GmlObjectId) parse();

        assertEquals("foo", id.toString());
    }

    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.gmlObjectId(), OGC.GmlObjectId);
        assertEquals("foo", doc.getDocumentElement().getAttributeNS(GML.NAMESPACE, "id"));
    }
}
