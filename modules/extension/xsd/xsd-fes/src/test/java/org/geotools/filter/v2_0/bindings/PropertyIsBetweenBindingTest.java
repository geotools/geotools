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
        Element docElement = doc.getDocumentElement();
        assertEquals(FES.Filter.getLocalPart(), docElement.getLocalName());
        assertEquals(FES.Filter.getNamespaceURI(), docElement.getNamespaceURI());
        print(doc);

        Element between = getElementByQName(doc, FES.PropertyIsBetween);
        assertNotNull(between);
        NodeList children = between.getChildNodes();
        assertEquals(3, children.getLength());
        Node valueReference = children.item(0);
        assertEquals(FES.ValueReference.getLocalPart(), valueReference.getLocalName());
        assertEquals(FES.ValueReference.getNamespaceURI(), valueReference.getNamespaceURI());
        assertEquals("foo", valueReference.getTextContent());

        Node lowerBoundary = children.item(1);
        assertEquals("LowerBoundary", lowerBoundary.getLocalName());
        assertEquals(FES.NAMESPACE, lowerBoundary.getNamespaceURI());

        Node lowerLiteral = children.item(1).getFirstChild();
        assertEquals(FES.Literal.getLocalPart(), lowerLiteral.getLocalName());
        assertEquals(FES.Literal.getNamespaceURI(), lowerLiteral.getNamespaceURI());
        assertEquals("10", lowerLiteral.getTextContent());

        Node upperBoundary = children.item(2);
        assertEquals("UpperBoundary", upperBoundary.getLocalName());
        assertEquals(FES.NAMESPACE, upperBoundary.getNamespaceURI());

        Node upperLiteral = children.item(2).getFirstChild();
        assertEquals(FES.Literal.getLocalPart(), upperLiteral.getLocalName());
        assertEquals(FES.Literal.getNamespaceURI(), upperLiteral.getNamespaceURI());

        assertEquals("20", upperLiteral.getTextContent());
    }
}
