/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.xml.gml.FCBuffer.StopException;
import org.geotools.xml.handlers.ComplexElementHandler;
import org.geotools.xml.handlers.DocumentHandler;
import org.geotools.xml.handlers.ElementHandlerFactory;
import org.geotools.xml.handlers.IgnoreHandler;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is a schema content handler. Code here has been modified from code written by Ian Schneider.
 *
 * <p>This class contains one stack used to store part of the parse tree. The ElementHandlers found on the stack have
 * direct next handlers placed on the stack. So here's the warning, be careful to read how you may be affecting (or
 * forgetting to affect) the stack.
 *
 * <p>If a FlowHandler implementation is available in the hints, the handler will periodically check it to see if it
 * should stop parsing. See the FlowHandler interface.
 *
 * <p>This is an XML Schema driven parser and {@link #resolveEntity(String, String)} will ignore all dtd references. If
 * an {@link EntityResolver} is provided it will be used.
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @version $Id$
 * @see XMLElementHandler
 */
public class XMLSAXHandler extends DefaultHandler {
    /** the logger -- should be used for debugging (assuming there are bugs LOL) */
    protected static final Logger logger = org.geotools.util.logging.Logging.getLogger(XMLSAXHandler.class);

    protected static Level level = Level.FINE;

    // the stack of handlers
    private Stack<XMLElementHandler> handlers = new Stack<>();

    /**
     * Collects string chunks in {@link #characters(char[], int, int)} callback to be handled at the beggining of
     * {@link #endElement(String, String, String)}
     */
    private StringBuffer characters = new StringBuffer();

    /** entity resolver */
    EntityResolver entityResolver;

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    /**
     * Delegate to {@link #entityResolver} if available.
     *
     * <p>
     *
     * <p>Note this is an XMLSchema based parser, all attempts to resolved DTDs are rejected.
     *
     * @param publicId The public identifier, or null if none is available.
     * @param systemId The system identifier provided in the XML document.
     * @return The new input source, or null to require the default behavior.
     * @exception java.io.IOException If there is an error setting up the new input source.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        // avoid dtd files
        if (systemId != null && systemId.endsWith("dtd")) {
            return new InputSource(new StringReader(""));
        }
        if (entityResolver != null) {
            return entityResolver.resolveEntity(publicId, systemId);
        } else {
            return super.resolveEntity(publicId, systemId);
        }
    }
    // hints
    private Map<String, Object> hints;
    private ElementHandlerFactory ehf = new ElementHandlerFactory(logger);

    // used to store prefix -> targetNamespace mapping until which time as the
    // schema uri is availiable (on the next startElement Call).
    private Map<String, String> schemaProxy = new HashMap<>();

    // the base handler for the document
    private DocumentHandler document = null;

    // the Locator stores the current position in the parse
    // for end-user debug information
    private Locator locator;

    // the uri of the instance ducment, used to resolve relative URIs
    private URI instanceDocument;

    /**
     * This contructor is intended to create an XMLSAXHandler to be used when parsing an XML instance document. The
     * instance document's uri is also be provided, as this will allow the parser to resolve relative uri's.
     */
    public XMLSAXHandler(URI intendedDocument, Map<String, Object> hints) {
        instanceDocument = intendedDocument;
        init(hints);
        logger.setLevel(level);
    }

    /**
     * <p>
     * This contructor is intended to create an XMLSAXHandler to be used when
     * parsing an XML instance document. The instance document's uri is also
     * be provided, as this will allow the parser to resolve relative uri's.
     * </p>
     *
     * @param hints Hints as per {@link {@link XMLHandlerHints}
     */
    public XMLSAXHandler(Map<String, Object> hints) {
        init(hints);
        logger.setLevel(level);
    }

    protected void init(Map<String, Object> hints) {
        if (hints == null) {
            hints = new HashMap<>();
        }
        this.hints = hints;
        setEntityResolver(XMLHandlerHints.toEntityResolver(hints));
    }
    /**
     * Implementation of endDocument.
     *
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    @Override
    public void endDocument() {
        document = (DocumentHandler) handlers.pop();
    }

    /**
     * Implementation of startDocument.
     *
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    @Override
    public void startDocument() {
        try {
            document = new DocumentHandler(ehf);
            handlers.push(document);
        } catch (RuntimeException e) {
            logger.warning(e.toString());
            throw e;
        }
    }

    /**
     * Implementation of characters.
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);
    }

    /** Handles the string chunks collected in {@link #characters}. */
    private void handleCharacters() throws SAXException {
        if (characters.length() == 0) {
            return;
        }
        try {
            checkStatus();

            String text = characters.toString();
            characters.setLength(0);

            if (text != null && !"".equals(text)) {
                handlers.peek().characters(text);
            }
        } catch (SAXException e) {
            logger.warning(e.toString());
            throw e;
        }
    }

    private void checkStatus() throws StopException {
        if (this.hints != null && hints.get(XMLHandlerHints.FLOW_HANDLER_HINT) != null) {
            FlowHandler handler = (FlowHandler) hints.get(XMLHandlerHints.FLOW_HANDLER_HINT);
            if (handler.shouldStop(hints)) {
                throw new StopException();
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new StopException();
        }
    }

    /**
     * Implementation of endElement.
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        handleCharacters();
        logger.fine("END: " + qName);
        XMLElementHandler handler = null;
        try {

            handler = handlers.peek();
            URI uri = new URI(namespaceURI);
            handler.endElement(uri, localName, hints);
        } catch (Exception e) {
            processException(e);

            logger.warning(e.getMessage());
            logger.warning("Line " + locator.getLineNumber() + " Col " + locator.getColumnNumber());

            SAXException exception = new SAXException(
                    e.getMessage()
                            + " at Line "
                            + locator.getLineNumber()
                            + " Col "
                            + locator.getColumnNumber()
                            + " tag is: \n"
                            + qName,
                    e);
            exception.initCause(e);
            throw exception;
        } finally {
            handlers.pop(); // we must do this or leak memory
            if (handler != null && !handlers.isEmpty()) {
                XMLElementHandler parent = handlers.peek();
                if (parent instanceof ComplexElementHandler) {
                    ComplexElementHandler complexParent = (ComplexElementHandler) parent;
                    String typename = complexParent.getType().getClass().getName();
                    // TODO: HACK The required Type is not in this Module
                    if (typename.equals("org.geotools.xml.wfs.WFSBasicComplexTypes$FeatureCollectionType")) {
                        complexParent.removeElement(handler);
                    }
                }
            }
        }
    }

    private void processException(Exception e) {
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        StringBuffer msg = new StringBuffer(e.getLocalizedMessage());
        StackTraceElement[] trace = e.getStackTrace();

        for (StackTraceElement element : trace) {
            msg.append("    ");
            msg.append(element.toString());
            msg.append("\n");
        }
        logger.log(Level.SEVERE, msg.toString());
    }

    /**
     * Implementation of startElement.
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     *     org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        characters.setLength(0);

        checkStatus();

        if (!schemaProxy.isEmpty()) {
            logger.fine("ADDING NAMESPACES: " + schemaProxy.size());

            String t = atts.getValue("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation");

            if (t == null || "".equals(t)) {
                t = atts.getValue("", "schemaLocation");
            }

            if (!(t == null || "".equals(t))) {
                t = t.trim();
                String[] targ2uri = t.split("\\s+");

                if (targ2uri != null) {
                    if (targ2uri.length != 0 && targ2uri.length % 2 != 0)
                        throw new SAXException("Bad Schema location attribute: you must have an even number of terms");

                    for (int i = 0; i < targ2uri.length / 2; i++) {
                        String uri = targ2uri[i * 2 + 1];
                        String targ = targ2uri[i * 2];
                        String prefix = schemaProxy.get(targ);
                        URI targUri = null;

                        boolean set = false;
                        if (hints != null && hints.containsKey(XMLHandlerHints.NAMESPACE_MAPPING)) {

                            Map schemas = (Map) hints.get(XMLHandlerHints.NAMESPACE_MAPPING);

                            if (schemas.containsKey(targ)) {
                                ehf.startPrefixMapping(prefix, targ, (URI) schemas.get(targ));
                                set = true;
                                break;
                            }
                        }

                        if (!set) {
                            try {
                                targUri = instanceDocument == null ? new URI(uri) : instanceDocument.resolve(uri);
                            } catch (URISyntaxException e1) {
                                logger.warning(e1.toString());
                            }
                            ehf.startPrefixMapping(prefix, targ, targUri);
                        }
                        schemaProxy.remove(targ);
                    }
                }
            }

            if (!schemaProxy.isEmpty()) {
                Iterator it = schemaProxy.keySet().iterator();

                while (it.hasNext()) {
                    String targ = (String) it.next();
                    String prefix = schemaProxy.get(targ);
                    ehf.startPrefixMapping(prefix, targ);

                    it.remove();
                }
            }
        }

        logger.finest("Moving on to finding the element handler");

        try {
            XMLElementHandler parent = handlers.peek();
            logger.finest("Parent Node = " + parent.getClass().getName() + "  '" + parent.getName() + "'");

            //            logger.finest("Parent Node = "+parent.getClass().getName()+"
            // '"+parent.getName()+"' "+
            //                    (parent.getType()==null?"null":
            //                    ((((ComplexType)parent.getType()).getChild()==null)?"null":
            //                    ((((ComplexType)parent.getType()).getChild().getGrouping() ==
            // ElementGrouping.SEQUENCE)?
            //
            // ((((Sequence)((ComplexType)parent.getType()).getChild()).getChildren()==null)?0:
            //
            // ((Sequence)((ComplexType)parent.getType()).getChild()).getChildren().length)+"":"null"))));
            logger.finest("This Node = " + localName + " :: " + namespaceURI);
            URI uri = new URI(namespaceURI);
            XMLElementHandler eh = parent.getHandler(uri, localName, hints);

            if (eh == null) {
                eh = new IgnoreHandler();
            }

            logger.finest("This Node = " + eh.getClass().getName());

            handlers.push(eh);
            eh.startElement(new URI(namespaceURI), localName, atts);
        } catch (Exception e) {
            processException(e);

            logger.warning(e.toString());
            logger.warning("Line " + locator.getLineNumber() + " Col " + locator.getColumnNumber());

            SAXException exception = new SAXException(
                    e.getMessage()
                            + " at Line "
                            + locator.getLineNumber()
                            + " Col "
                            + locator.getColumnNumber()
                            + " tag is: \n"
                            + qName,
                    e);
            exception.initCause(e);
            throw exception;
        }
    }

    /** Used to set the logger level for all XMLSAXHandlers */
    public static void setLogLevel(Level l) {
        level = l;
        logger.setLevel(l);
        XMLElementHandler.setLogLevel(l);
    }

    /**
     * getDocument purpose.
     *
     * <p>Completes the post-processing phase, and returns the value from the parse ...
     *
     * @return Object parsed
     * @see DocumentHandler#getValue()
     */
    public Object getDocument() throws SAXException {
        return document.getValue();
    }

    /**
     * Implementation of error.
     *
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    @Override
    public void error(SAXParseException exception) {
        logger.severe("ERROR " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
    }

    /**
     * Implementation of fatalError.
     *
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        logger.severe("FATAL " + exception.getMessage());
        if (locator != null) {
            logger.severe("col " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
        }
        throw exception;
    }

    /**
     * Implementation of warning.
     *
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    @Override
    public void warning(SAXParseException exception) {
        logger.warning("WARN " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
    }

    /**
     * Stores the locator for future error reporting
     *
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    @Override
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }

    /** @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String) */
    @Override
    public void endPrefixMapping(String prefix) {
        // hard coded schemas should not be removed.  For example.  GML and WFS
        if (prefix.equals("gml") || prefix.equals("wfs")) return;
        ehf.endPrefixMapping(prefix);
    }

    /** @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String) */
    @Override
    public void startPrefixMapping(String prefix, String uri) {
        if ("http://www.w3.org/2001/XMLSchema-instance".equals(uri)) {
            return;
        }

        schemaProxy.put(uri, prefix);
    }
}
