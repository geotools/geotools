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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.api.filter.expression.PropertyName;
import org.junit.Test;
import org.w3c.dom.Document;

public class PropertyNameTypeBindingTest extends FilterTestSupport {
    @Test
    public void testParse() throws Exception {
        FilterMockData.propertyName(document, document);

        PropertyName propertyName = (PropertyName) parse();
        assertNotNull(propertyName);

        assertEquals("foo", propertyName.getPropertyName());
        assertNotNull(propertyName.getNamespaceContext());
    }

    @Test
    public void testParseWithPrefix() throws Exception {
        FilterMockData.propertyName("bar:foo", document, document);

        PropertyName propertyName = (PropertyName) parse();
        assertNotNull(propertyName);

        assertEquals("bar:foo", propertyName.getPropertyName());
        assertNotNull(propertyName.getNamespaceContext());
    }

    @Test
    public void testEncode() throws Exception {
        PropertyName propertyName = FilterMockData.propertyName("foo");
        Document dom = encode(propertyName, OGC.PropertyName);

        assertEquals("foo", dom.getDocumentElement().getFirstChild().getNodeValue());
    }
}
