/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;

public class CopyingHandler extends DefaultHandler {

    protected StringBuffer buffer;
    protected NamespaceSupport namespaceContext;
    protected int root = 0;

    public CopyingHandler() {
        this(null);
    }

    public CopyingHandler(NamespaceSupport namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (buffer == null) {
            buffer = new StringBuffer();
            root++;
        }

        buffer.append("<");
        if (qName.startsWith(":")) {
            buffer.append(localName);
        } else {
            buffer.append(qName);
        }
        Set<String> xmlnsAttributes = new HashSet<>();
        if (attributes.getLength() > 0) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String attributeName = attributes.getQName(i);
                if (attributeName.startsWith("xmlns")) {
                    xmlnsAttributes.add(attributeName);
                }
                buffer.append(" ")
                        .append(attributeName)
                        .append("=\"")
                        .append(attributes.getValue(i))
                        .append("\"");
            }
        }

        if (root > 0) {
            if (namespaceContext != null) {
                // dump out namespace context (mind, it may contain duplicates)
                Set<String> mappedPrefixes = new HashSet<>();
                for (Enumeration e = namespaceContext.getPrefixes(); e.hasMoreElements(); ) {
                    String prefix = (String) e.nextElement();
                    if (mappedPrefixes.contains(prefix)) {
                        continue;
                    }
                    if ("".equals(prefix)) {
                        if (xmlnsAttributes.contains("xmlns")) {
                            continue;
                        }
                        buffer.append(" xmlns");
                    } else {
                        if (xmlnsAttributes.contains("xmlns:" + prefix)) {
                            continue;
                        }
                        buffer.append(" xmlns:").append(prefix);
                    }
                    buffer.append("=\"").append(namespaceContext.getURI(prefix)).append("\"");
                    mappedPrefixes.add(prefix);
                }
            }
            root--;
        }
        buffer.append(">");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (buffer == null) {
            buffer = new StringBuffer();
            root++;
        }
        buffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (buffer != null) {
            buffer.append("</").append(qName).append(">");
        }
    }

    @Override
    public void endDocument() throws SAXException {
        buffer = null;
    }
}
