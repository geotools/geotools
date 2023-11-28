/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyIsBetweenBindingTest extends FESTestSupport {

    @Test
    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.propertyIsBetween(), FES.Filter);
        assertEquals("fes:Filter", doc.getDocumentElement().getNodeName());
        print(doc);

        Element between = getElementByQName(doc, FES.PropertyIsBetween);
        assertNotNull(between);
        NodeList children = between.getChildNodes();
        assertEquals(3, children.getLength());
        Node valueReference = children.item(0);
        assertEquals("fes:ValueReference", valueReference.getNodeName());
        assertEquals("foo", valueReference.getTextContent());

        assertEquals("fes:LowerBoundary", children.item(1).getNodeName());
        Node lowerLiteral = children.item(1).getFirstChild();
        assertEquals("fes:Literal", lowerLiteral.getNodeName());
        assertEquals("10", lowerLiteral.getTextContent());

        assertEquals("fes:UpperBoundary", children.item(2).getNodeName());
        Node upperLiteral = children.item(2).getFirstChild();
        assertEquals("fes:Literal", upperLiteral.getNodeName());
        assertEquals("20", upperLiteral.getTextContent());
    }
}
