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
package org.geotools.xml.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.xml.SchemaFactory;
import org.geotools.xml.XMLElementHandler;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.xml.sax.SAXException;


/**
 * <p>
 * This class is used to create handlers for child elements based on the
 * currently defined namespaces. This class is called by the XMLSAXHandler  to
 * help act as a library of prefix -> Schema mappings.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @see org.geotools.xml.XMLSAXHandler
 * @see Schema
 *
 * @source $URL$
 */
public class ElementHandlerFactory {
    public static final String KEY = "org.geotools.xml.handlers.ElementHandlerFactory_KEY";
    private Logger logger;
    private Map targSchemas = new HashMap(); // maps prefix -->> Schema
    private Map prefixURIs = new HashMap(); // maps prefix -->> URI
    protected URI defaultNS = null;

    /**
     * Creates a new ElementHandlerFactory object.
     *
     * @param l Logger
     */
    public ElementHandlerFactory(Logger l) {
        logger = l;
    }

    /**
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix) {
        URI s = (URI) prefixURIs.remove(prefix);

        if (s != null) {
            targSchemas.remove(s);
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String prefix, String targ, URI uri)
        throws SAXException {
        logger.finest("Target == '" + targ + "'");
        logger.finest("URI == '" + uri + "'");

        try {
            URI tns = new URI(targ);
            Schema s = SchemaFactory.getInstance(tns, uri, logger.getLevel());

            if (s != null) {
                if ((prefix == null) || "".equalsIgnoreCase(prefix)) {
                    defaultNS = s.getTargetNamespace();
                }

                targSchemas.put(s.getTargetNamespace(), s);
                prefixURIs.put(prefix, tns); 
            }else{
                prefixURIs.put(prefix, tns); 
            }
        } catch (URISyntaxException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String prefix, String targ)
        throws SAXException {
        logger.finest("Target == '" + targ + "'");

        try {
            URI tns = new URI(targ);
            Schema s = SchemaFactory.getInstance(tns);

            if (s == null) {
                prefixURIs.put(prefix, tns); 
                return;
            }

            if ((prefix == null) || "".equalsIgnoreCase(prefix)) {
                defaultNS = s.getTargetNamespace();
            }

            targSchemas.put(s.getTargetNamespace(), s);
            prefixURIs.put(prefix, tns); 
        } catch (URISyntaxException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    protected void startPrefixMapping(String prefix, Schema targ){
        logger.finest("Target == '" + targ + "'");
        	if ((prefix == null) || "".equalsIgnoreCase(prefix)) {
            	defaultNS = targ.getTargetNamespace();
        	}
            targSchemas.put(targ.getTargetNamespace(), targ);
            prefixURIs.put(prefix, targ.getTargetNamespace()); 
    }

    /**
     * Creates an element handler for the element specified by name and
     * namespace. Will return null if a suitable handler is not found.
     *
     * @param namespaceURI
     * @param localName
     *
     *
     * @throws SAXException
     *
     * @see ElementHandlerFactory#createElementHandler(Element)
     */
    public XMLElementHandler createElementHandler(URI namespaceURI,
        String localName) throws SAXException {

        if (localName == null) {
            return null;
        }

        if (namespaceURI == null || "".equals(namespaceURI.toString())) {
            namespaceURI = defaultNS;
        }

        logger.finest("Trying to create an element handler for " + localName
            + " :: " + namespaceURI);

        Schema s = (Schema) targSchemas.get(namespaceURI);

        if (s == null) {
            logger.finest("Could not find Schema " + namespaceURI);

            return null;
        }

        logger.finest("Found Schema " + s.getTargetNamespace());

        Element[] eth = s.getElements();

        if (eth == null) {
            return null;
        }

        for (int i = 0; i < eth.length; i++) {
            String name = eth[i].getName();
			if (localName.equalsIgnoreCase(name) || name.equals(IgnoreHandler.NAME)) {
                return createElementHandler(eth[i]);
            }
        }

        //TODO search the nesting import stuff
        return null;
    }

    /**
     * Creates an element handler based on the element provided.
     *
     * @param eth Element
     *
     *
     * @throws SAXException
     */
    public XMLElementHandler createElementHandler(Element eth)
        throws SAXException {
        Type type = eth.getType();

        if (type == null) {
            throw new SAXException("Type not found for " + eth.getName() + " ");
        }
        
        if (type instanceof SimpleType) {
            return new SimpleElementHandler(eth);
        }
        if (type instanceof ComplexType) {
            return new ComplexElementHandler(this, eth);
        }


        return new IgnoreHandler(eth);
    }
    
    public URI getNamespace(String prefix){
        URI s = (URI)prefixURIs.get(prefix);
        return s;
    }
}
