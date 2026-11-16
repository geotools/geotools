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
package org.geotools.xsd;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.util.factory.GeoTools;
import org.geotools.xsd.impl.ParserHandler;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.TypeInfo;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import static javax.xml.XMLConstants.NULL_NS_URI;
import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE;
import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE_NS_URI;

/**
 * Parses a DOM (Document Object Model) using the geotools xml binding system.
 *
 * <p>This parser should be used if the source XML being parsed has already been parsed into a {@link Document}. If not
 * use one of {@link Parser} or {@link Parser}.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>, Evolved Binary
 * @author Justin Deoliveira, The Open Planning Project
 */
public class DOMParser {

    private static final String XMLNS_ATTRIBUTE_PREFIX = XMLNS_ATTRIBUTE + ":";

    private final Configuration configuration;
    private final Document document;
    private final EntityResolver entityResolver;

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param document An XML document.
     */
    public DOMParser(final Configuration configuration, final Document document) {
        this(configuration, document, null);
    }

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param document An XML document.
     * @param entityResolver An entity resolver to use when parsing the document, or {@code null} to use the default
     *     one.
     */
    public DOMParser(final Configuration configuration, final Document document, final EntityResolver entityResolver) {
        this.configuration = configuration;
        this.document = document;
        this.entityResolver = entityResolver;
    }

    /**
     * Parses the supplied DOM returning a single object representing the result of the parse.
     *
     * @return The object representation of the root element of the document.
     */
    public Object parse() throws IOException, SAXException, ParserConfigurationException {
        // "Parser" traverses Document issuing SAX events (fake as document has already been parsed)
        final Parser fake = new Parser(configuration);
        if (entityResolver != null) {
            fake.setEntityResolver(entityResolver);
        } else {
            fake.setEntityResolver(GeoTools.getEntityResolver());
        }

        // Create the handler to receive the SAX events
        final ParserHandler handler = fake.getParserHandler();

        // walk the DOM and emit SAX events
        nodeToSax(document, handler);

        return handler.getValue();
    }

    private void nodeToSax(final Node node, final ParserHandler handler) throws SAXException {
        if (node == null) {
            return;
        }

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                final Element element = (Element) node;

                // handle Attributes of the Element
                Deque<String> prefixMappings = null;
                final AttributesImpl attributes = new AttributesImpl();
                final NamedNodeMap elementAttributes = element.getAttributes();
                for (int i = 0; i < elementAttributes.getLength(); i++) {
                    final Attr elementAttr = (Attr) elementAttributes.item(i);
                    if (isNamespaceDeclaration(elementAttr)) {
                        // namespace declaration
                        final String xmlnsPrefix = attrXmlNsPrefix(elementAttr);
                        final String xmlnsUri = elementAttr.getValue();
                        handler.startPrefixMapping(xmlnsPrefix, xmlnsUri);
                        if (prefixMappings == null) {
                            prefixMappings = new ArrayDeque<>();
                        }
                        prefixMappings.addFirst(xmlnsPrefix);

                    } else {
                        // normal attribute
                        String elementAttrType = null;
                        final TypeInfo elementAttrTypeInfo = elementAttr.getSchemaTypeInfo();
                        if (elementAttrTypeInfo != null) {
                            final String elementAttrSchemaTypeName = elementAttrTypeInfo.getTypeName();
                            if (elementAttrSchemaTypeName != null) {
                                elementAttrType = elementAttrSchemaTypeName;
                            }
                        }
                        if (elementAttrType == null) {
                            elementAttrType = "CDATA";
                        }
                        attributes.addAttribute(
                                uri(elementAttr),
                                localName(elementAttr),
                                elementAttr.getNodeName(),
                                elementAttrType,
                                elementAttr.getValue());
                    }
                }

                final String elementUri = uri(element);
                final String elementLocalName = localName(element);

                handler.startElement(elementUri, elementLocalName, element.getNodeName(), attributes);
                final Node elementFirstChild = node.getFirstChild();
                nodeToSax(elementFirstChild, handler);
                handler.endElement(elementUri, elementLocalName, element.getNodeName());

                if (prefixMappings != null) {
                    String prefixMapping = null;
                    while ((prefixMapping = prefixMappings.pollFirst()) != null) {
                        handler.endPrefixMapping(prefixMapping);
                    }
                }

                break;

            case Node.TEXT_NODE:
                final char[] text = ((CharacterData) node).getData().toCharArray();
                handler.characters(text, 0, text.length);
                break;

            case Node.CDATA_SECTION_NODE:
                handler.startCDATA();
                final char[] cdata = ((CharacterData) node).getData().toCharArray();
                handler.characters(cdata, 0, cdata.length);
                handler.endCDATA();
                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                final ProcessingInstruction processingInstruction = (ProcessingInstruction) node;
                handler.processingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
                break;

            case Node.COMMENT_NODE:
                final char[] comment = ((CharacterData) node).getData().toCharArray();
                handler.comment(comment, 0, comment.length);
                break;

            case Node.DOCUMENT_NODE:
                handler.startDocument();
                final Node documentFirstChild = node.getFirstChild();
                nodeToSax(documentFirstChild, handler);
                handler.endDocument();
                break;

            case Node.DOCUMENT_TYPE_NODE:
                final DocumentType documentType = (DocumentType) node;
                handler.startDTD(documentType.getName(), documentType.getPublicId(), documentType.getSystemId());
                handler.endDTD();
                break;

            case Node.DOCUMENT_FRAGMENT_NODE:
                handler.startDocument();
                final Node documentFragmentFirstChild = node.getFirstChild();
                nodeToSax(documentFragmentFirstChild, handler);
                handler.endDocument();
                break;

            case Node.NOTATION_NODE:
                final Notation notation = (Notation) node;
                handler.notationDecl(notation.getNodeName(), notation.getPublicId(), notation.getSystemId());
                break;

            default:
                throw new SAXException("Unsupported node type: " + node.getNodeType());
        }

        final Node nextSibling = node.getNextSibling();
        nodeToSax(nextSibling, handler);
    }

    private static boolean isNamespaceDeclaration(final Attr attr) {
        return XMLNS_ATTRIBUTE_NS_URI.equals(attr.getNamespaceURI())
                || XMLNS_ATTRIBUTE.equals(attr.getName())
                || attr.getName().startsWith(XMLNS_ATTRIBUTE_PREFIX);
    }

    private static String uri(final Node node) {
        String uri = node.getNamespaceURI();
        if (uri == null) {
            uri = NULL_NS_URI;
        }
        return uri;
    }

    private static String localName(final Node node) {
        String localName = node.getLocalName();
        if (localName == null) {
            localName = node.getNodeName();
        }
        return localName;
    }

    private static String attrXmlNsPrefix(final Attr attr) {
        if (XMLNS_ATTRIBUTE.equals(attr.getName())) {
            return NULL_NS_URI;
        } else {
            if (attr.getLocalName() != null) {
                return attr.getLocalName();
            } else {
                return attr.getName().substring(6);
            }
        }
    }
}
