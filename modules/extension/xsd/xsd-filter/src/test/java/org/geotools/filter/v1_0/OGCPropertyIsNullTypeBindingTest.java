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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.api.filter.PropertyIsNull;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OGCPropertyIsNullTypeBindingTest extends FilterTestSupport {

    @Test
    public void testType() {
        assertEquals(PropertyIsNull.class, binding(OGC.PropertyIsNullType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.PropertyIsNullType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        FilterMockData.propertyisNull(document, document);

        PropertyIsNull isNull = (PropertyIsNull) parse();

        assertNotNull(isNull.getExpression());
    }

    @Test
    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.propertyIsNull(), OGC.PropertyIsNull);
        Element e = getElementByQName(doc, OGC.PropertyName);
        assertEquals("foo", e.getFirstChild().getNodeValue());
    }

    @Test
    public void testEncodeAsFilter() throws Exception {
        Document doc = encode(FilterMockData.propertyIsNull(), OGC.Filter);
        Element e = getElementByQName(doc, OGC.PropertyName);
        assertEquals("foo", e.getFirstChild().getNodeValue());
    }
}
