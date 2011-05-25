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
package org.geotools.xml.handlers.xsi;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.All;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * AllHandler purpose.
 * 
 * <p>
 * This represents an 'all' element in an xml schema.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class AllHandler extends ElementGroupingHandler {
    /** 'all' */
    public static final String LOCALNAME = "all";
    private String id;
    private int minOccurs;
    private int maxOccurs;
    private List elements;
    private DefaultAll cache = null;

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName){
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // annotation
            // element
            if (ElementTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (elements == null) {
                    elements = new LinkedList();
                }

                ElementTypeHandler eh = new ElementTypeHandler();
                elements.add(eh);

                return eh;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts){
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        String minOccurs1 = atts.getValue("", "minOccurs");

        if (minOccurs1 == null) {
            minOccurs1 = atts.getValue(namespaceURI, "minOccurs");
        }

        if (minOccurs1 == null || minOccurs1.length() == 0) {
        	this.minOccurs = 1;
        } else {
        	this.minOccurs = Integer.parseInt(minOccurs1);
        }

        String maxOccurs1 = atts.getValue("", "maxOccurs");

        if (maxOccurs1 == null) {
            maxOccurs1 = atts.getValue(namespaceURI, "maxOccurs");
        }
        if (maxOccurs1 == null || maxOccurs1.length() == 0) {
        	this.maxOccurs = 1;
        } else {
        	this.maxOccurs = Integer.parseInt(maxOccurs1);
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * @see org.geotools.xml.XSIHandlers.ElementGroupingHandler#compress(org.geotools.xml.XSIHandlers.SchemaHandler)
     */
    protected ElementGrouping compress(SchemaHandler parent)
        throws SAXException {

        synchronized(this){
            if (cache != null)
            	return cache;
            cache = new DefaultAll();
        }
            
        cache.id = id;
        cache.maxOccurs = maxOccurs;
        cache.minOccurs = minOccurs;

        if (elements != null) {
            cache.elements = new Element[elements.size()];

            for (int i = 0; i < cache.elements.length; i++){
                cache.elements[i] = (Element) ((ElementTypeHandler) elements.get(i))
                    .compress(parent);
            }
        }

        id = null;
        elements = null;

        return cache;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()))
        + (minOccurs * maxOccurs);
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return DEFAULT;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String namespaceURI, String localName){
        // do nothing
    }

    /**
     * <p>
     * Default implementation for when the parse is complete
     * </p>
     *
     * @author dzwiers
     *
     * @see All
     */
    private static class DefaultAll implements All {
        // file visible to avoid set* methods.
        Element[] elements;
        String id;
        int maxOccurs;
        int minOccurs;

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            if (elements == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++) {
                Element t = elements[i].findChildElement(name);

                if (t != null) { // found it

                    return t;
                }
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.All#getElements()
         */
        public Element[] getElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.xsi.All#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#getMaxOccurs()
         */
        public int getMaxOccurs() {
            return maxOccurs;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#getMinOccurs()
         */
        public int getMinOccurs() {
            return minOccurs;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#getGrouping()
         */
        public int getGrouping() {
            return ALL;
        }

		public Element findChildElement(String localName, URI namespaceURI) {
			if (elements == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++) {
                Element t = elements[i].findChildElement(localName, namespaceURI);

                if (t != null) { // found it

                    return t;
                }
            }

			return null;
		}
    }
}
