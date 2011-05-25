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
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * This is a schema content handler. Code here has been modified from code
 * written by Ian Schneider.
 * 
 * <p>
 * This class contains one stack used to store part of the parse tree. The
 * ElementHandlers found on the stack have direct next handlers placed on the
 * stack. So here's the warning, be careful to read how you may be affecting
 * (or forgetting to affect) the stack.
 * </p>
 * 
 * <p>
 * If a FlowHandler implementation is available in the hints, the handler will
 * periodically check it to see if it should stop parsing. See the FlowHandler
 * interface.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 *
 * @source $URL$
 * @version $Id$
 *
 * @see XMLElementHandler
 */
public class XMLSAXHandler extends DefaultHandler {
    /**
     * the logger -- should be used for debugging (assuming there are bugs LOL)
     */
    protected final static Logger logger = org.geotools.util.logging.Logging.getLogger(
            "net.refractions.xml.sax");
    protected static Level level = Level.FINE;

    // the stack of handlers
    private Stack handlers = new Stack();

    /** Collects string chunks in {@link #characters(char[], int, int)} 
     * callback to be handled at the beggining of {@link #endElement(String, String, String)}
     */
    private StringBuffer characters = new StringBuffer();
    
    /**
     * 
     * TODO summary sentence for resolveEntity ...
     * 
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     * @param pubId
     * @param sysId
     * @return InputSource
     * @throws SAXException
     */
    public InputSource resolveEntity( String pubId, String sysId ) throws SAXException {
        // avoid dtd files
		if(sysId != null && sysId.endsWith("dtd")){
		    return new InputSource(new StringReader(""));
		}
		try{
            if (false) {
                /*
                 * HACK: This dead code exists only in order to make J2SE 1.4 compiler happy.
                 * This hack is needed because there is a slight API change between J2SE 1.4
                 * and 1.5: the 'resolveEntity()' method didn't declared IOException in its
                 * throw clause in J2SE 1.4. Compare the two following links:
                 *
                 * http://java.sun.com/j2se/1.5/docs/api/org/xml/sax/helpers/DefaultHandler.html#resolveEntity(java.lang.String,%20java.lang.String)
                 * http://java.sun.com/j2se/1.4/docs/api/org/xml/sax/helpers/DefaultHandler.html#resolveEntity(java.lang.String,%20java.lang.String)
                 */
                throw new IOException();
            }
            return super.resolveEntity(pubId,sysId);
		}catch(IOException e){
			SAXException se = new SAXException(e.getLocalizedMessage());
			se.initCause(e);
			throw se;
		}
    }
    // hints
    private Map hints;
    private ElementHandlerFactory ehf = new ElementHandlerFactory(logger);

    // used to store prefix -> targetNamespace mapping until which time as the
    // schema uri is availiable (on the next startElement Call).
    private Map schemaProxy = new HashMap();

    // the base handler for the document
    private DocumentHandler document = null;

    // the Locator stores the current position in the parse
    // for end-user debug information
    private Locator locator;

    // the uri of the instance ducment, used to resolve relative URIs
    private URI instanceDocument;

    /**
     * <p>
     * This contructor is intended to create an XMLSAXHandler to be used when
     * parsing an XML instance document. The instance document's uri is also
     * be provided, as this will allow the parser to resolve relative uri's.
     * </p>
     *
     * @param intendedDocument
     * @param hints DOCUMENT ME!
     */
    public XMLSAXHandler(URI intendedDocument, Map hints) {
        instanceDocument = intendedDocument;
        this.hints = hints;
        logger.setLevel(level);
    }

    /**
     * <p>
     * This contructor is intended to create an XMLSAXHandler to be used when
     * parsing an XML instance document. The instance document's uri is also
     * be provided, as this will allow the parser to resolve relative uri's.
     * </p>
     *
     * @param hints DOCUMENT ME!
     */
    public XMLSAXHandler(Map hints) {
        this.hints = hints;
        logger.setLevel(level);
    }

    /**
     * Implementation of endDocument.
     *
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument(){
        document = ((DocumentHandler) handlers.pop());
    }

    /**
     * Implementation of startDocument.
     *
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument(){
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
     * @param ch
     * @param start
     * @param length
     *
     * @throws SAXException
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
    	characters.append(ch, start, length);
    }

    /**
     * Handles the string chunks collected in {@link #characters}.
     */
    private void handleCharacters() throws SAXException{
    	if(characters.length() == 0){
    		return;
    	}
        try {
        	checkStatus();
        	
            String text = characters.toString();
            characters.setLength(0);

            if ((text != null) && !"".equals(text)) {
                ((XMLElementHandler) handlers.peek()).characters(text);
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
     * @param namespaceURI
     * @param localName
     * @param qName
     *
     * @throws SAXException
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String namespaceURI, String localName, String qName)
        throws SAXException {
    	handleCharacters();
        logger.fine("END: " + qName);
        XMLElementHandler handler = null;
        try {
        	
            handler = (XMLElementHandler) handlers.peek();
            URI uri = new URI(namespaceURI);
            handler.endElement(uri, localName, hints);            
        } catch (Exception e) {
            processException(e);

            logger.warning(e.getMessage());
            logger.warning("Line " + locator.getLineNumber() + " Col "
                    + locator.getColumnNumber());

            SAXException exception = new SAXException(e.getMessage()+" at Line " + locator.getLineNumber() + " Col "
                    + locator.getColumnNumber()+" tag is: \n"+qName, e);
            exception.initCause(e);
            throw exception;
        }
        finally {
            handlers.pop(); // we must do this or leak memory
            if (handler != null && !handlers.isEmpty()){
	        	XMLElementHandler parent = ((XMLElementHandler)handlers.peek());
	        	if (parent instanceof ComplexElementHandler){
	        	    ComplexElementHandler complexParent = (ComplexElementHandler)parent;
		        	String typename = complexParent.getType().getClass().getName();
		        	// TODO: HACK The required Type is not in this Module
		        	if(typename.equals("org.geotools.xml.wfs.WFSBasicComplexTypes$FeatureCollectionType")){
		        		complexParent.removeElement(handler);
		        	}
	        	}
        	}
        }
    }

    private void processException( Exception e ) {
        if( e instanceof RuntimeException )
            throw (RuntimeException) e;
        StringBuffer msg=new StringBuffer(e.getLocalizedMessage());
        StackTraceElement[] trace = e.getStackTrace();
        
        for( int i = 0; i < trace.length; i++ ) {
            StackTraceElement element = trace[i];
            msg.append("    ");
            msg.append(element.toString());
            msg.append("\n");
        }
        logger.log(Level.SEVERE, msg.toString());
    }

    /**
     * Implementation of startElement.
     *
     * @param namespaceURI
     * @param localName
     * @param qName
     * @param atts
     *
     * @throws SAXException
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        String qName, Attributes atts) throws SAXException {
    	characters.setLength(0);
    	
    	checkStatus();

        if (schemaProxy.size() != 0) {
        	logger.fine("ADDING NAMESPACES: " + schemaProxy.size());

            String t = atts.getValue("http://www.w3.org/2001/XMLSchema-instance",
                    "schemaLocation");

            if ((t == null) || "".equals(t)) {
                t = atts.getValue("", "schemaLocation");
            }

            if (!((t == null) || "".equals(t))) {
                String[] targ2uri = t.split("\\s+");
                
                if (targ2uri != null) {
                    if(targ2uri.length !=0 && targ2uri.length%2!=0)
                        throw new SAXException("Bad Schema location attribute: you must have an even number of terms");
                    
                    for (int i = 0; i < (targ2uri.length / 2); i++) {
                        String uri = targ2uri[(i * 2) + 1];
                        String targ = targ2uri[i * 2];
                        String prefix = (String) schemaProxy.get(targ);
                        URI targUri = null;


                        boolean set = false;
                        if (hints!=null && hints.containsKey(XMLHandlerHints.NAMESPACE_MAPPING)){

                            Map schemas=(Map) hints.get(XMLHandlerHints.NAMESPACE_MAPPING);
                            
                            if( schemas.containsKey(targ) ){
                                ehf.startPrefixMapping(prefix, targ, (URI) schemas.get(targ));
                                set=true;
                                break;
                            }
                        }
                        
                        if(!set){
                            try {
                                targUri = (instanceDocument == null) ? new URI(uri)
                                : instanceDocument
                                .resolve(uri);
                            } catch (URISyntaxException e1) {
                                logger.warning(e1.toString());
                            }
                            ehf.startPrefixMapping(prefix, targ, targUri);
                        }
                        schemaProxy.remove(targ);
                    }
                }
            }

            if (schemaProxy.size() != 0) {
                Iterator it = schemaProxy.keySet().iterator();

                while (it.hasNext()) {
                    String targ = (String) it.next();
                    String prefix = (String) schemaProxy.get(targ);
                    ehf.startPrefixMapping(prefix, targ);

                    it.remove();
                }
            }
        }

        logger.finest("Moving on to finding the element handler");

        try {
            XMLElementHandler parent = ((XMLElementHandler) handlers.peek());
            logger.finest("Parent Node = " + parent.getClass().getName()
                + "  '" + parent.getName() + "'");

            //            logger.finest("Parent Node = "+parent.getClass().getName()+"
            // '"+parent.getName()+"' "+
            //                    (parent.getType()==null?"null":
            //                    ((((ComplexType)parent.getType()).getChild()==null)?"null":
            //                    ((((ComplexType)parent.getType()).getChild().getGrouping() ==
            // ElementGrouping.SEQUENCE)?
            //                            ((((Sequence)((ComplexType)parent.getType()).getChild()).getChildren()==null)?0:
            //                                ((Sequence)((ComplexType)parent.getType()).getChild()).getChildren().length)+"":"null"))));
            logger.finest("This Node = " + localName + " :: " + namespaceURI);
            URI uri = new URI(namespaceURI);
            XMLElementHandler eh = parent.getHandler(uri,
                    localName, hints);

            if (eh == null) {
                eh = new IgnoreHandler();
            }

            logger.finest("This Node = " + eh.getClass().getName());

            handlers.push(eh);
            eh.startElement(new URI(namespaceURI), localName, atts);
        } catch (Exception e) {
            processException(e);

            logger.warning(e.toString());
            logger.warning("Line " + locator.getLineNumber() + " Col "
                + locator.getColumnNumber());

            SAXException exception = new SAXException(e.getMessage()+" at Line " + locator.getLineNumber() + " Col "
                    + locator.getColumnNumber()+" tag is: \n"+qName, e);
            exception.initCause(e);
            throw exception;        
        }
    }

    /**
     * <p>
     * Used to set the logger level for all XMLSAXHandlers
     * </p>
     *
     * @param l
     */
    public static void setLogLevel(Level l) {
        level = l;
        logger.setLevel(l);
        XMLElementHandler.setLogLevel(l);
    }

    /**
     * getDocument purpose.
     * 
     * <p>
     * Completes the post-processing phase, and returns the value from the
     * parse ...
     * </p>
     *
     * @return Object parsed
     *
     * @throws SAXException
     *
     * @see DocumentHandler#getValue()
     */
    public Object getDocument() throws SAXException {
        return document.getValue();
    }

    /**
     * Implementation of error.
     *
     * @param exception
     *
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException exception){
        logger.severe("ERROR " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line "
            + locator.getLineNumber());
    }

    /**
     * Implementation of fatalError.
     *
     * @param exception
     *
     * @throws SAXException
     *
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException exception)
        throws SAXException {
        logger.severe("FATAL " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line "
            + locator.getLineNumber());
        throw exception;
    }

    /**
     * Implementation of warning.
     *
     * @param exception
     *
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException exception){
        logger.warning("WARN " + exception.getMessage());
        logger.severe("col " + locator.getColumnNumber() + ", line "
            + locator.getLineNumber());
    }

    /**
     * Stores the locator for future error reporting
     *
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }

    /**
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix){
    	// hard coded schemas should not be removed.  For example.  GML and WFS
    	if( prefix.equals("gml") || prefix.equals("wfs"))
    		return;
        ehf.endPrefixMapping(prefix);
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri){
        if ("http://www.w3.org/2001/XMLSchema-instance".equals(uri)) {
            return;
        }

        schemaProxy.put(uri, prefix);
    }
}
