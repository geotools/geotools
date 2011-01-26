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

import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Group;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * GroupHandler purpose.
 * 
 * <p>
 * Representa a 'group' element
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class GroupHandler extends ElementGroupingHandler {
    /** 'group' */
    public final static String LOCALNAME = "group";
    private static int offset = 0;
    private String id;
    private String name;
    private String ref = null;
    private int maxOccurs = 1;
    private int minOccurs = 1;
    private ElementGroupingHandler child; // one of 'all', 'choice', or 'sequence'
    private int hashCodeOffset = getOffset();
    private DefaultGroup cache = null;

    /*
     * helper for hashCode()
     */
    private static int getOffset() {
        return offset++;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (LOCALNAME.hashCode() * ((name == null) ? 1 : name.hashCode()))
        + hashCodeOffset;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName)
        throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // all
            if (AllHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                AllHandler sth = new AllHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child.");
                }

                return sth;
            }

            // choice
            if (ChoiceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ChoiceHandler sth = new ChoiceHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child.");
                }

                return sth;
            }

            // sequence
            if (SequenceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SequenceHandler sth = new SequenceHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child.");
                }

                return sth;
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

        String max = atts.getValue("", "maxOccurs");

        if (max == null) {
            max = atts.getValue(namespaceURI, "maxOccurs");
        }

        String min = atts.getValue("", "minOccurs");

        if (min == null) {
            min = atts.getValue(namespaceURI, "minOccurs");
        }

        name = atts.getValue("", "name");

        if (name == null || "".equals(name)) {
            name = atts.getValue(namespaceURI, "name");
        }

        ref = atts.getValue("", "ref");

        if (ref == null || "".equals(ref)) {
            ref = atts.getValue(namespaceURI, "ref"); // mutally exclusive with
        }

        // name ...
        if ((min != null) && !"".equalsIgnoreCase(min)) {
            minOccurs = Integer.parseInt(min);
        }

        if ((max != null) && !"".equalsIgnoreCase(max)) {
            if ("unbounded".equalsIgnoreCase(max)) {
                maxOccurs = ElementGrouping.UNBOUNDED;
            } else {
                maxOccurs = Integer.parseInt(max);
            }
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * <p>
     * returns the group's name
     * </p>
     *
     */
    public String getName() {
        return name;
    }

    /**
     * @see org.geotools.xml.XSIHandlers.ElementGroupingHandler#compress(org.geotools.xml.XSIHandlers.SchemaHandler)
     */
    protected ElementGrouping compress(SchemaHandler parent)
        throws SAXException {

        synchronized(this){
            if (cache != null)
            	return cache;
            cache = new DefaultGroup();
        }

        cache.id = this.id;
        cache.name = this.name;
        cache.namespace = parent.getTargetNamespace();
        cache.min = this.minOccurs;
        cache.max = this.maxOccurs;
        cache.child = (this.child == null) ? null
              : this.child.compress(parent); // deal with all/choice/sequnce
        if (ref != null) {
            Group g = parent.lookUpGroup(ref);
            if (g != null) {
                if ((id == null) || "".equalsIgnoreCase(id)) {
                    id = g.getId();
                }

                cache.min = g.getMinOccurs();
                cache.max = g.getMaxOccurs();
                cache.name = g.getName();
                cache.namespace = g.getNamespace();
                
                cache.child = (g.getChild() == null) ? cache.child : g.getChild();
            }
        }

        child = null;

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
        // do nothing
    }
    protected static class DefaultGroup implements Group{
        protected ElementGrouping child;
        protected String id;
        protected int min,max;
        protected String name;
        protected URI namespace;
        /**
         * TODO summary sentence for getChild ...
         * 
         * @see org.geotools.xml.schema.Group#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * TODO summary sentence for getId ...
         * 
         * @see org.geotools.xml.schema.Group#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * TODO summary sentence for getMaxOccurs ...
         * 
         * @see org.geotools.xml.schema.Group#getMaxOccurs()
         */
        public int getMaxOccurs() {
            return max;
        }

        /**
         * TODO summary sentence for getMinOccurs ...
         * 
         * @see org.geotools.xml.schema.Group#getMinOccurs()
         */
        public int getMinOccurs() {
            return min;
        }

        /**
         * TODO summary sentence for getName ...
         * 
         * @see org.geotools.xml.schema.Group#getName()
         */
        public String getName() {
            return name;
        }

        /**
         * TODO summary sentence for getNamespace ...
         * 
         * @see org.geotools.xml.schema.Group#getNamespace()
         */
        public URI getNamespace() {
            return namespace;
        }

        /**
         * TODO summary sentence for getGrouping ...
         * 
         * @see org.geotools.xml.schema.ElementGrouping#getGrouping()
         */
        public int getGrouping() {
            return GROUP;
        }

        /**
         * TODO summary sentence for findChildElement ...
         * 
         * @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String)
         * @param arg1
         */
        public Element findChildElement( String arg1 ) {
            return child==null?null:child.findChildElement(arg1);
        }

		public Element findChildElement(String localName, URI namespaceURI) {
			 return child==null?null:child.findChildElement(localName, namespaceURI);
		}
        
    }
}
