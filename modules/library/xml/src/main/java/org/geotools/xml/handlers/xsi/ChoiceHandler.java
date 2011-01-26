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
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * ChoiceHandler purpose.
 * 
 * <p>
 * represtents a 'choice' element
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class ChoiceHandler extends ElementGroupingHandler {
    /** 'choice' */
    public final static String LOCALNAME = "choice";
    private String id;
    private int minOccurs;
    private int maxOccurs;
    private List children; // element, group, choice, sequence, any
    private DefaultChoice cache = null;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()))
        + (minOccurs * maxOccurs);
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName){
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // group
            if (GroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (children == null) {
                    children = new LinkedList();
                }

                GroupHandler gh = new GroupHandler();
                children.add(gh);

                return gh;
            }

            // choice
            if (LOCALNAME.equalsIgnoreCase(localName)) {
                if (children == null) {
                    children = new LinkedList();
                }

                GroupHandler gh = new GroupHandler();
                children.add(gh);

                return gh;
            }

            // sequence
            if (SequenceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (children == null) {
                    children = new LinkedList();
                }

                SequenceHandler gh = new SequenceHandler();
                children.add(gh);

                return gh;
            }

            // any
            if (AnyHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (children == null) {
                    children = new LinkedList();
                }

                AnyHandler gh = new AnyHandler();
                children.add(gh);

                return gh;
            }

            // element
            if (ElementTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (children == null) {
                    children = new LinkedList();
                }

                ElementTypeHandler gh = new ElementTypeHandler();
                children.add(gh);

                return gh;
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

        String min = atts.getValue("", "minOccurs");

        if (min == null) {
            min = atts.getValue(namespaceURI, "minOccurs");
        }

        String max = atts.getValue("", "maxOccurs");

        if (max == null) {
            max = atts.getValue(namespaceURI, "maxOccurs");
        }

        minOccurs = ((min == null) || "".equalsIgnoreCase(min)) ? 1
            : Integer.parseInt(min);
        maxOccurs = ((max == null) || "".equalsIgnoreCase(max)) ? 1
            : ("unbounded".equalsIgnoreCase(max) ? ElementGrouping.UNBOUNDED : Integer.parseInt(max));
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
            cache = new DefaultChoice();
        }
        
        cache.id = id;
        cache.maxOccurs = maxOccurs;
        cache.minOccurs = minOccurs;

        if (children != null) {
            cache.children = new ElementGrouping[children.size()];

            // TODO compress choices here
            // remove child choices and make the options as peers
            for (int i = 0; i < cache.children.length; i++)
                cache.children[i] = ((ElementGroupingHandler) children.get(i))
                    .compress(parent);
        }

        id = null;
        children = null;

        return cache;
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
        // does nothing
    }

    /**
     * <p>
     * Implementation of a Choice
     * </p>
     *
     * @author dzwiers
     *
     * @see Choice
     */
    private static class DefaultChoice implements Choice {
        // file visible to avoid set* methods
        ElementGrouping[] children;
        String id;
        int maxOccurs;
        int minOccurs;

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            if (children == null) {
                return null;
            }

            for (int i = 0; i < children.length; i++) {
                Element t = children[i].findChildElement(name);

                if (t != null) { // found it

                    return t;
                }
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Choice#getChildren()
         */
        public ElementGrouping[] getChildren() {
            return children;
        }

        /**
         * @see org.geotools.xml.xsi.Choice#getId()
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
            return CHOICE;
        }

		public Element findChildElement(String localName, URI namespaceURI) {
			if (children == null) {
                return null;
            }

            for (int i = 0; i < children.length; i++) {
                Element t = children[i].findChildElement(localName, namespaceURI);

                if (t != null) { // found it

                    return t;
                }
            }

            return null;
		}
    }
}
