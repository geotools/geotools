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

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.eclipse.emf.ecore.xml.type.internal.DataValue.XMLChar.isValidNCName;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.TransformerHandler;
import org.w3c.dom.DOMException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/** Validates that element names do not contain invalid/illegal XML characters. */
public class QNameValidatingHandler implements TransformerHandler {

    private final ContentHandler original;

    public QNameValidatingHandler(ContentHandler original) {
        this.original = original;
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
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
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (!checkName(localName, qName) || !checkAtts(atts)) {
            throw new DOMException(
                    DOMException.INVALID_CHARACTER_ERR,
                    "INVALID_CHARACTER_ERR: An invalid or illegal XML character is specified.");
        }
        original.startElement(namespaceURI, localName, qName, atts);
    }

    private static boolean checkName(String localName, String qName) {
        if (!isEmpty(localName) && !isValidNCName(localName)) {
            return false;
        } else if (!isEmpty(qName)) {
            // the validation is more lenient than the XML specification to allow existing
            // unit tests that pass qualified names with an empty prefix when the test
            // only uses local names (e.g., ':Name' instead of 'Name' or 'sld:Name')
            String[] names = qName.split(":", 2);
            return (names[0].isEmpty() || isValidNCName(names[0])) && (names.length == 1 || isValidNCName(names[1]));
        }
        return !isEmpty(localName);
    }

    private static boolean checkAtts(Attributes atts) {
        // org.geotools.xml.transform.TransformerBase tends to declare namespaces
        // directly in the element attributes
        if (atts != null && atts.getLength() > 0) {
            for (int i = 0; i < atts.getLength(); i++) {
                String localName = atts.getLocalName(i);
                String qName = atts.getQName(i);
                // there are existing cases where attributes are set with the qualified
                // name as the local name
                localName = qName != null && qName.equals(localName) ? "" : localName;
                if (!checkName(localName, qName)) {
                    return false;
                } else if ("xmlns".equals(localName)
                        || "xmlns".equals(qName)
                        || qName != null && qName.startsWith("xmlns:")) {
                    checkNamespaceUri(atts.getValue(i));
                }
            }
        }
        return true;
    }

    private static void checkNamespaceUri(String uri) {
        // block the HTML namespace URI to prevent XSS
        if ("http://www.w3.org/1999/xhtml".equals(uri)) {
            throw new DOMException(DOMException.NAMESPACE_ERR, "NAMESPACE_ERR: The XHTML namespace is not allowed.");
        }
    }

    // All other methods just call the original handler.

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        original.characters(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        original.endDocument();
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        original.endElement(namespaceURI, localName, qName);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        original.endPrefixMapping(prefix);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        original.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        original.processingInstruction(target, data);
    }

    @Override
    public void setDocumentLocator(org.xml.sax.Locator locator) {
        original.setDocumentLocator(locator);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        original.skippedEntity(name);
    }

    @Override
    public void startDocument() throws SAXException {
        original.startDocument();
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (original instanceof LexicalHandler) {
            ((LexicalHandler) original).comment(ch, start, length);
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        if (original instanceof LexicalHandler) {
            ((LexicalHandler) original).startCDATA();
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        if (original instanceof LexicalHandler) {
            ((LexicalHandler) original).endCDATA();
        }
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (original instanceof LexicalHandler) {
            ((LexicalHandler) original).startDTD(name, publicId, systemId);
        }
    }

    @Override
    public void endDTD() throws SAXException {
        if (original instanceof LexicalHandler) {
            ((LexicalHandler) original).endDTD();
        }
    }

    @Override
    public void startEntity(String name) throws SAXException {
        if (original instanceof LexicalHandler) {
            ((LexicalHandler) original).startEntity(name);
        }
    }

    @Override
    public void endEntity(String name) throws SAXException {
        if (original instanceof LexicalHandler) {
            ((LexicalHandler) original).endEntity(name);
        }
    }

    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        if (original instanceof DTDHandler) {
            ((DTDHandler) original).notationDecl(name, publicId, systemId);
        }
    }

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
            throws SAXException {
        if (original instanceof DTDHandler) {
            ((DTDHandler) original).notationDecl(name, publicId, systemId);
        }
    }

    @Override
    public void setResult(Result result) throws IllegalArgumentException {
        if (original instanceof TransformerHandler) {
            ((TransformerHandler) original).setResult(result);
        }
    }

    @Override
    public void setSystemId(String systemID) {
        if (original instanceof TransformerHandler) {
            ((TransformerHandler) original).setSystemId(systemID);
        }
    }

    @Override
    public String getSystemId() {
        if (original instanceof TransformerHandler) {
            return ((TransformerHandler) original).getSystemId();
        }
        return null;
    }

    @Override
    public Transformer getTransformer() {
        if (original instanceof TransformerHandler) {
            return ((TransformerHandler) original).getTransformer();
        }
        return null;
    }
}
