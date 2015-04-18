/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xs.bindings;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.util.Converters;
import org.geotools.xml.ComplexBinding;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XSAnyTypeStrategyTest extends TestSchema {
    private ComplexBinding stratagy;

    protected void setUp() throws Exception {
        super.setUp();
        stratagy = (ComplexBinding) stratagy(XS.ANYTYPE);
    }
    
    public void testSetUp() {
        assertNotNull("found anyType", stratagy);
    }
    
    public void testXMLEncode() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element element = doc.createElement("root");
        doc.appendChild(element);
        stratagy.encode(Converters.convert("foo<bar>foo</bar>", DocumentFragment.class), doc, element);
        
        NodeList childNodes = element.getChildNodes();
        assertEquals(2, childNodes.getLength());
        assertEquals("#text", childNodes.item(0).getNodeName());
        assertEquals("foo", childNodes.item(0).getTextContent());
        assertEquals("bar", childNodes.item(1).getNodeName());
        assertEquals("foo", childNodes.item(1).getTextContent());
    }
    
    protected QName getQName() {
        return null;
    }
}
