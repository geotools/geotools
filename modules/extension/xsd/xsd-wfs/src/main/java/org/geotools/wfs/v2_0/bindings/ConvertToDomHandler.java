/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Parses XML into a namespace aware dom.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class ConvertToDomHandler extends DefaultHandler {

    Document doc;
    Node node;
    NamespaceSupport nsContext;

    public ConvertToDomHandler(Document doc, NamespaceSupport nsContext) {
        this.doc = doc;
        this.node = doc;
        this.nsContext = nsContext;
    }

    public Document getDocument() {
        return doc;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs)
            throws SAXException {
        // start a new namespace context and declare prefixes from this node
        nsContext.pushContext();

        // look for any namespace declarations
        for (int i = 0; i < attrs.getLength(); ++i) {
            String attQName = attrs.getQName(i);
            if (attQName.startsWith("xmlns")) {
                String prefix = attQName.length() > 5 ? attQName.substring(6) : "";
                nsContext.declarePrefix(prefix, attrs.getValue(i));
            }
        }

        uri = namespace(qName);

        // Create the element.
        Element e = doc.createElementNS(uri, qName);

        // Add each attribute.
        for (int i = 0; i < attrs.getLength(); ++i) {
            String attQName = attrs.getQName(i);
            if (attQName.startsWith("xmlns")) {
                continue;
            }

            String attUri = namespace(attQName);

            Attr a = doc.createAttributeNS(attUri, attQName);
            a.setValue(attrs.getValue(i));
            e.setAttributeNodeNS(a);
        }

        // Actually add it in the tree, and adjust the right place.
        node.appendChild(e);
        node = e;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        Text text = doc.createTextNode(str);
        node.appendChild(text);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        node = node.getParentNode();
        nsContext.popContext();
    }

    String namespace(String qName) {
        String[] split = qName.split(":");
        if (split.length > 1) {
            return nsContext.getURI(split[0]);
        }
        return nsContext.getURI("");
    }
}
