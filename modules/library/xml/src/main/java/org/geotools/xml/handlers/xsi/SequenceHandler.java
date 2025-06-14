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
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Sequence;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * SequenceHandler purpose.
 *
 * <p>represents a sequence element
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class SequenceHandler extends ElementGroupingHandler {
    /** 'sequence' */
    public static final String LOCALNAME = "sequence";

    private String id;
    private int maxOccurs;
    private int minOccurs;
    private List<XSIElementHandler> children; // element, group, choice, sequence or any
    private DefaultSequence cache = null;

    /** @see java.lang.Object#hashCode() */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return LOCALNAME.hashCode() * (id == null ? 1 : id.hashCode()) + (children == null ? 2 : children.hashCode());
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XSIElementHandler getHandler(String namespaceURI, String localName) {
        logger.finest("Getting Handler for " + localName + " :: " + namespaceURI);

        // child types
        //
        // any
        if (AnyHandler.LOCALNAME.equalsIgnoreCase(localName)) {
            if (children == null) {
                children = new LinkedList<>();
            }

            AnyHandler ah = new AnyHandler();
            children.add(ah);

            return ah;
        }

        // choice
        if (ChoiceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
            if (children == null) {
                children = new LinkedList<>();
            }

            ChoiceHandler ah = new ChoiceHandler();
            children.add(ah);

            return ah;
        }

        // element
        if (ElementTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
            if (children == null) {
                children = new LinkedList<>();
            }

            ElementTypeHandler ah = new ElementTypeHandler();
            children.add(ah);

            return ah;
        }

        // group
        if (GroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
            if (children == null) {
                children = new LinkedList<>();
            }

            GroupHandler ah = new GroupHandler();
            children.add(ah);

            return ah;
        }

        // sequence
        if (LOCALNAME.equalsIgnoreCase(localName)) {
            if (children == null) {
                children = new LinkedList<>();
            }

            SequenceHandler ah = new SequenceHandler();
            children.add(ah);

            return ah;
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, Attributes atts) {
        // id
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        // maxOccurs
        String maxOccurs1 = atts.getValue("", "maxOccurs");

        if (maxOccurs1 == null) {
            maxOccurs1 = atts.getValue(namespaceURI, "maxOccurs");
        }

        // minOccurs
        String minOccurs1 = atts.getValue("", "minOccurs");

        if (minOccurs1 == null) {
            minOccurs1 = atts.getValue(namespaceURI, "minOccurs");
        }

        if (minOccurs1 != null && !"".equalsIgnoreCase(minOccurs1)) {
            this.minOccurs = Integer.parseInt(minOccurs1);
        } else {
            this.minOccurs = 1;
        }

        if (maxOccurs1 != null && !"".equalsIgnoreCase(maxOccurs1)) {
            if ("unbounded".equalsIgnoreCase(maxOccurs1)) {
                this.maxOccurs = ElementGrouping.UNBOUNDED;
            } else {
                this.maxOccurs = Integer.parseInt(maxOccurs1);
            }
        } else {
            this.maxOccurs = 1;
        }
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    @Override
    public String getLocalName() {
        return LOCALNAME;
    }

    /** @see org.geotools.xml.XSIHandlers.ElementGroupingHandler#compress(org.geotools.xml.XSIHandlers.SchemaHandler) */
    @Override
    protected ElementGrouping compress(SchemaHandler parent) throws SAXException {

        synchronized (this) {
            if (cache != null) return cache;
            cache = new DefaultSequence();
        }

        cache.id = id;
        cache.minOccurs = minOccurs;
        cache.maxOccurs = maxOccurs;

        logger.finest(id + " :: This Sequence has " + (children == null ? 0 : children.size()) + " children");

        if (children != null) {
            cache.children = new ElementGrouping[children.size()];

            // TODO compress sequences here
            // sequqnces can be inlined here.
            for (int i = 0; i < cache.children.length; i++)
                cache.children[i] = ((ElementGroupingHandler) children.get(i)).compress(parent);
        }

        children = null;
        id = null;

        return cache;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandlerType() */
    @Override
    public int getHandlerType() {
        return SEQUENCE;
    }

    /** @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String, java.lang.String) */
    @Override
    public void endElement(String namespaceURI, String localName) {
        // do nothing
    }

    /**
     * Default implementation of a sequence for a parsed xml sequence
     *
     * @author dzwiers
     * @see Sequence
     */
    private static class DefaultSequence implements Sequence {
        // file visible avoids set* methods
        ElementGrouping[] children;
        String id;
        int minOccurs;
        int maxOccurs;

        /** @see org.geotools.xml.xsi.ElementGrouping#findChildElement(java.lang.String) */
        @Override
        public Element findChildElement(String name) {
            if (children == null) {
                return null;
            }

            for (ElementGrouping child : children) {
                Element t = child.findChildElement(name);

                if (t != null) { // found it

                    return t;
                }
            }

            return null;
        }

        /** @see org.geotools.xml.xsi.Sequence#getChildren() */
        @Override
        public ElementGrouping[] getChildren() {
            return children;
        }

        /** @see org.geotools.xml.xsi.Sequence#getId() */
        @Override
        public String getId() {
            return id;
        }

        /** @see org.geotools.xml.xsi.ElementGrouping#getMaxOccurs() */
        @Override
        public int getMaxOccurs() {
            return maxOccurs;
        }

        /** @see org.geotools.xml.xsi.ElementGrouping#getMinOccurs() */
        @Override
        public int getMinOccurs() {
            return minOccurs;
        }

        /** @see org.geotools.xml.xsi.ElementGrouping#getGrouping() */
        @Override
        public int getGrouping() {
            return SEQUENCE;
        }

        @Override
        public Element findChildElement(String localName, URI namespaceURI) {
            if (children == null) {
                return null;
            }

            for (ElementGrouping child : children) {
                Element t = child.findChildElement(localName, namespaceURI);

                if (t != null) { // found it

                    return t;
                }
            }

            return null;
        }
    }
}
