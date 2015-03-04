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
package org.geotools.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class XMLDocumentConverterFactoryTest extends TestCase {

    XMLDocumentConverterFactory factory;
    
    protected void setUp() throws Exception {
        factory = new XMLDocumentConverterFactory();
    }
    
    public void testDocumentToString() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element element = doc.createElement("root");
        element.setTextContent("text");
        doc.appendChild(element);
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root>text</root>", convert(doc, String.class));
    }
    
    public void testDocumentFragmentToString() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        DocumentFragment fragment = doc.createDocumentFragment();
        fragment.setTextContent("foo");
        Element element = doc.createElement("bar");
        element.setTextContent("foo");
        fragment.appendChild(element);
        
        assertEquals("foo<bar>foo</bar>", convert(fragment, String.class));
    }
    
    public void testStringToDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element element = doc.createElementNS(null, "root");
        element.setTextContent("text");
        doc.appendChild(element);
        
        assertTrue(doc.isEqualNode(convert("<root>text</root>", Document.class)));
    }
    
    public void testStringToDocumentFragment() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        DocumentFragment fragment = doc.createDocumentFragment();
        fragment.setTextContent("foo");
        Element element = doc.createElementNS(null, "bar");
        element.setTextContent("foo");
        fragment.appendChild(element);
        
        assertTrue(fragment.isEqualNode(convert("foo<bar>foo</bar>", DocumentFragment.class)));
    }
    
    <T> T convert( Object value , Class<T> target) throws Exception {
        return (T) factory.createConverter( value.getClass(), target, null ).convert( value, target );
    }
}
