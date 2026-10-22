/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.transform;

import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.eclipse.emf.ecore.xml.type.internal.DataValue.XMLChar.isValidNCName;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.TransformerHandler;
import org.w3c.dom.DOMException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/** Validates that element names do not contain invalid/illegal XML characters. */
public class QNameValidatingHandler implements TransformerHandler {

    private final ContentHandler original;

    public QNameValidatingHandler(final ContentHandler original) {
        this.original = original;
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        // org.geotools.xsd.Encoder tends to declare namespaces using this method
        if (!isEmpty(prefix) && !isValidNCName(prefix)) {
            throw new DOMException(
                    DOMException.INVALID_CHARACTER_ERR,
                    "INVALID_CHARACTER_ERR: An invalid or illegal XML character is specified.");
        }
        checkNamespaceUri(uri);
        original.startPrefixMapping(prefix, uri);
    }

    @Override
    public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts) throws SAXException {
        if (!checkName(localName, qName) || !checkAtts(atts)) {
            throw new DOMException(
                    DOMException.INVALID_CHARACTER_ERR,
                    "INVALID_CHARACTER_ERR: An invalid or illegal XML character is specified.");
        }
        original.startElement(namespaceURI, localName, qName, atts);
    }

    private static boolean checkName(final String localName, final String qName) {
        if (!isEmpty(localName) && !isValidNCName(localName)) {
            return false;
        } else if (!isEmpty(qName)) {
            // the validation is more lenient than the XML specification to allow existing
            // unit tests that pass qualified names with an empty prefix when the test
            // only uses local names (e.g., ':Name' instead of 'Name' or 'sld:Name')
            final String[] names = qName.split(":", 2);
            return (names[0].isEmpty() || isValidNCName(names[0])) && (names.length == 1 || isValidNCName(names[1]));
        }
        return !isEmpty(localName);
    }

    private static boolean checkAtts(final Attributes atts) {
        // org.geotools.xml.transform.TransformerBase tends to declare namespaces
        // directly in the element attributes
        if (atts != null && atts.getLength() > 0) {
            for (int i = 0; i < atts.getLength(); i++) {
                String localName = atts.getLocalName(i);
                final String qName = atts.getQName(i);
                // there are existing cases where attributes are set with the qualified
                // name as the local name
                localName = qName != null && qName.equals(localName) ? "" : localName;
                if (!checkName(localName, qName)) {
                    return false;
                } else if (XMLNS_ATTRIBUTE.equals(localName)
                        || XMLNS_ATTRIBUTE.equals(qName)
                        || qName != null && qName.startsWith(XMLNS_ATTRIBUTE + ':')) {
                    checkNamespaceUri(atts.getValue(i));
                }
            }
        }
        return true;
    }

    private static void checkNamespaceUri(final String uri) {
        // block the HTML namespace URI to prevent XSS
        if ("http://www.w3.org/1999/xhtml".equals(uri)) {
            throw new DOMException(DOMException.NAMESPACE_ERR, "NAMESPACE_ERR: The XHTML namespace is not allowed.");
        }
    }

    // All other methods just call the original handler.

    @Override
    public void characters(final char[] ch, final int start,final  int length) throws SAXException {
        original.characters(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        original.endDocument();
    }

    @Override
    public void endElement(final String namespaceURI, final String localName, final String qName) throws SAXException {
        original.endElement(namespaceURI, localName, qName);
    }

    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        original.endPrefixMapping(prefix);
    }

    @Override
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        original.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
        original.processingInstruction(target, data);
    }

    @Override
    public void setDocumentLocator(final Locator locator) {
        original.setDocumentLocator(locator);
    }

    @Override
    public void skippedEntity(final String name) throws SAXException {
        original.skippedEntity(name);
    }

    @Override
    public void startDocument() throws SAXException {
        original.startDocument();
    }

    @Override
    public void comment(final char[] ch, final int start, final int length) throws SAXException {
        if (original instanceof final LexicalHandler handler) {
            handler.comment(ch, start, length);
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        if (original instanceof final LexicalHandler handler) {
            handler.startCDATA();
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        if (original instanceof final LexicalHandler handler) {
            handler.endCDATA();
        }
    }

    @Override
    public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
        if (original instanceof final LexicalHandler handler) {
            handler.startDTD(name, publicId, systemId);
        }
    }

    @Override
    public void endDTD() throws SAXException {
        if (original instanceof final LexicalHandler handler) {
            handler.endDTD();
        }
    }

    @Override
    public void startEntity(final String name) throws SAXException {
        if (original instanceof final LexicalHandler handler) {
            handler.startEntity(name);
        }
    }

    @Override
    public void endEntity(String name) throws SAXException {
        if (original instanceof LexicalHandler handler) {
            handler.endEntity(name);
        }
    }

    @Override
    public void notationDecl(final String name, final String publicId, final String systemId) throws SAXException {
        if (original instanceof final DTDHandler handler) {
            handler.notationDecl(name, publicId, systemId);
        }
    }

    @Override
    public void unparsedEntityDecl(final String name, final String publicId, final String systemId, final String notationName)
            throws SAXException {
        if (original instanceof final DTDHandler handler) {
            handler.notationDecl(name, publicId, systemId);
        }
    }

    @Override
    public void setResult(final Result result) throws IllegalArgumentException {
        if (original instanceof final TransformerHandler handler) {
            handler.setResult(result);
        }
    }

    @Override
    public void setSystemId(final String systemID) {
        if (original instanceof final TransformerHandler handler) {
            handler.setSystemId(systemID);
        }
    }

    @Override
    public String getSystemId() {
        if (original instanceof final TransformerHandler handler) {
            return handler.getSystemId();
        }
        return null;
    }

    @Override
    public Transformer getTransformer() {
        if (original instanceof final TransformerHandler handler) {
            return handler.getTransformer();
        }
        return null;
    }
}
