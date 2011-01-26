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
package org.geotools.xml;

import javax.xml.namespace.QName;

import org.geotools.xml.impl.ParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Parser delegate which which uses the xsd framework to parse.
 * <p>
 * This is used to support the parsing of dynamically imported schemas, ie schemas that are included
 * on the fly in an instance document but not referenced by the schema itself.
 * </p>
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 *
 * @source $URL$
 */
public class XSDParserDelegate implements ParserDelegate {

    ParserHandler handler;
    
    public XSDParserDelegate(Configuration configuration) {
        handler = new ParserHandler( configuration );
    }
    
    public boolean canHandle(QName elementName) {
        return handler.getConfiguration().getXSD().getNamespaceURI().equals( elementName.getNamespaceURI() );
    }
    
    public void setDocumentLocator(Locator locator) {
        handler.setDocumentLocator(locator);
    }
    
    public void startDocument() throws SAXException {
        handler.startDocument();
    }
    
    public void processingInstruction(String target, String data)
        throws SAXException {
        handler.processingInstruction(target, data);
    }
    
    public void skippedEntity(String name) throws SAXException {
        handler.skippedEntity(name);
    }

    public void startPrefixMapping(String prefix, String uri)
        throws SAXException {
        handler.startPrefixMapping(prefix, uri);
    }
    
    public void endPrefixMapping(String prefix) throws SAXException {
        handler.endPrefixMapping(prefix);
    }
    
    public void startElement(String uri, String localName, String name,
            Attributes atts) throws SAXException {
        handler.startElement(uri, localName, name, atts);
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        handler.characters(ch, start, length);
    }

    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        handler.ignorableWhitespace( ch, start, length );
    }

    public void endElement(String uri, String localName, String name)
            throws SAXException {
        handler.endElement(uri, localName, name);
    }

    public void endDocument() throws SAXException {
        handler.endDocument();
    }

    public Object getParsedObject() {
        return handler.getValue();
    }
}
