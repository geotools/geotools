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

import java.net.URI;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.xml.handlers.xsi.IgnoreHandler;
import org.geotools.xml.handlers.xsi.RootHandler;
import org.geotools.xml.schema.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is a schema handler. Code here has been modified from code written by Ian Schneider.
 *
 * <p>This class contains one stack used to store part of the parse tree. The ElementHandlers found
 * on the stack have direct next handlers placed on the stack. So here's the warning, be careful to
 * read how you may be affecting (or forgetting to affect) the stack.
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 * @see XSIElementHandler
 */
public class XSISAXHandler extends DefaultHandler {
    // the logger -- should be used for debugging (assuming there are bugs LOL)
    protected static final Logger logger =
            org.geotools.util.logging.Logging.getLogger(XSISAXHandler.class);

    // the stack of handers representing a portion of the parse tree
    private Stack handlers = new Stack();

    // The schema being used to parse into
    private Schema schema = null;

    // The root parsing element
    protected RootHandler rootHandler = null;

    // the Locator is used for end-user debugging
    private Locator locator;

    /**
     * Collects string chunks in {@link #characters(char[], int, int)} callback to be handled at the
     * beggining of {@link #endElement(String, String, String)}
     */
    private StringBuffer characters = new StringBuffer();

    // the schema uri being parsed. This is important to resolve relative uris
    //    private URI uri;

    /** should never be called */
    private XSISAXHandler() {
        /* not used*/
    }

    /** Stores the uri being parsed to help resolve relative uris within the document. */
    public XSISAXHandler(URI uri) {
        //        this.uri = uri;
        rootHandler = new RootHandler(uri);
    }

    /** @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String) */
    public void startPrefixMapping(String arg0, String arg1) {
        rootHandler.startPrefixMapping(arg0, arg1);
    }

    /**
     * Implementation of endDocument.
     *
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() {
        handlers.pop();
    }

    /**
     * Implementation of startDocument.
     *
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() {
        try {
            handlers.push(rootHandler);
        } catch (RuntimeException e) {
            logger.warning(e.toString());
            throw e;
        }
    }

    /**
     * Implementation of characters. push String
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);
    }

    /** Handles the string chunks collected in {@link #characters}. */
    private void handleCharacters() throws SAXException {
        final String text = characters.toString();
        characters.setLength(0);
        if (text.length() == 0) {
            return;
        }
        XSIElementHandler peek = null;
        try {
            if ((text != null) && !"".equals(text.trim())) {
                peek = (XSIElementHandler) handlers.peek();
                peek.characters(text);
            }
        } catch (SAXException e) {
            logger.warning(e.toString());
            throw e;
        }
    }

    /**
     * Implementation of endElement. push NS,Name
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String,
     *     java.lang.String)
     */
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        handleCharacters();
        logger.fine("END: " + qName);

        try {
            XSIElementHandler element = (XSIElementHandler) handlers.pop();
            element.endElement(namespaceURI, localName);
        } catch (SAXException e) {
            logger.warning(e.toString());
            throw e;
        }
    }

    /**
     * Implementation of startElement.
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String,
     *     java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        characters.setLength(0);
        logger.fine("START: " + qName);

        try {
            XSIElementHandler eh =
                    ((XSIElementHandler) handlers.peek()).getHandler(namespaceURI, localName);
            logger.finest(
                    "Parent Node = " + ((XSIElementHandler) handlers.peek()).getClass().getName());

            if (eh == null) {
                eh = new IgnoreHandler();
            }

            logger.finest("This Node = " + eh.getClass().getName());
            logger.finest("This Node = " + localName + " :: " + namespaceURI);

            handlers.push(eh);
            eh.startElement(namespaceURI, localName, atts);
        } catch (SAXException e) {
            logger.warning(e.toString());
            throw e;
        }
    }

    /** Sets the logging level for all the XSISAXHandlers. */
    public static void setLogLevel(Level l) {
        logger.setLevel(l);
        XSIElementHandler.setLogLevel(l);
    }

    /**
     * getSchema purpose.
     *
     * <p>This method should be called only after the parse has been completed. This method will
     * then return a compressed schema instance.
     *
     * @return schema
     */
    public Schema getSchema() throws SAXException {
        if (schema == null) {
            schema = rootHandler.getSchema();
        }

        return schema;
    }

    /**
     * Implementation of error.
     *
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException exception) {
        logger.severe("ERROR " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
    }

    /**
     * Implementation of fatalError.
     *
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        logger.severe("FATAL " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
        throw exception;
    }

    /**
     * Implementation of warning.
     *
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException exception) {
        logger.warning("WARN " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
    }

    /** @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator) */
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }
}
