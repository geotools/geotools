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
import java.net.URISyntaxException;
import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.Any;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * AnyHandler purpose.
 *
 * <p>Represents an 'any' element.
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class AnyHandler extends ElementGroupingHandler {
    /** 'any' */
    public static final String LOCALNAME = "any";

    /** strict */
    public static final int STRICT = 0;

    /** lax */
    public static final int LAX = 1;

    /** skip */
    public static final int SKIP = 2;

    private String id;
    private URI namespace;
    private int minOccurs;
    private int maxOccurs;

    //    private int processContents;
    private DefaultAny cache = null;

    /** @see java.lang.Object#hashCode() */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return LOCALNAME.hashCode() * (id == null ? 1 : id.hashCode()) + minOccurs * maxOccurs;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XSIElementHandler getHandler(String namespaceURI, String localName) {
        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, Attributes atts) throws SAXException {
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

        String namespace1 = atts.getValue("", "namespace");

        if (namespace1 == null) {
            namespace1 = atts.getValue(namespaceURI, "namespace");
        }

        try {
            if (namespace1 != null) {
                if (namespace1.toLowerCase().equals("##any")) {
                    this.namespace = Any.ALL;
                } else {
                    if (namespace1.toLowerCase().equals("##other")) {
                        // TODO improve this
                        this.namespace = Any.ALL;
                    } else {
                        if (namespace1.toLowerCase().equals("##targetNamespace")) {
                            try {
                                this.namespace = new URI(namespaceURI);
                            } catch (URISyntaxException e) {
                                logger.warning(e.toString());
                                this.namespace = new URI(namespace1);
                            }
                        } else {
                            this.namespace = new URI(namespace1);
                        }
                    }
                }
            }
        } catch (URISyntaxException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        }

        if (null == min || "".equalsIgnoreCase(min)) {
            minOccurs = 1;
        } else {
            minOccurs = Integer.parseInt(min);
        }

        if (null == max || "".equalsIgnoreCase(max)) {
            maxOccurs = 1;
        } else {
            if ("unbounded".equalsIgnoreCase(max)) {
                maxOccurs = ElementGrouping.UNBOUNDED;
            } else {
                maxOccurs = Integer.parseInt(max);
            }
        }
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    @Override
    public String getLocalName() {
        return LOCALNAME;
    }

    /** maps strings -> int constants for the 'process' attribute */
    public static int findProcess(String process) throws SAXException {
        if (process == null || "".equalsIgnoreCase(process)) {
            return STRICT;
        }

        if ("lax".equalsIgnoreCase(process)) {
            return LAX;
        }

        if ("skip".equalsIgnoreCase(process)) {
            return SKIP;
        }

        if ("strict".equalsIgnoreCase(process)) {
            return STRICT;
        }

        throw new SAXException("Unknown Process Type: '" + process + "'");
    }

    /** reverses the findProcess method, converting from integers to String for the process attribute. */
    public static String writeProcess(int process) {
        switch (process) {
            case LAX:
                return "lax";

            case SKIP:
                return "skip";

            case STRICT:
            default:
                return "strict";
        }
    }

    /** @see org.geotools.xml.XSIHandlers.ElementGroupingHandler#compress(org.geotools.xml.XSIHandlers.SchemaHandler) */
    @Override
    protected ElementGrouping compress(SchemaHandler parent) {
        synchronized (this) {
            if (cache != null) return cache;
            cache = new DefaultAny();
        }
        cache.id = id;
        cache.namespace = namespace;
        cache.minOccurs = minOccurs;
        cache.maxOccurs = maxOccurs;

        id = null;
        namespace = null;

        return cache;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandlerType() */
    @Override
    public int getHandlerType() {
        return DEFAULT;
    }

    /** @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String, java.lang.String) */
    @Override
    public void endElement(String namespaceURI, String localName) {
        // do nothing
    }

    /**
     * Any instance implementation
     *
     * @author dzwiers
     * @see Any
     */
    private static class DefaultAny implements Any {
        String id;
        URI namespace;
        int maxOccurs;
        int minOccurs;

        @Override
        public Element findChildElement(String name) {
            // TODO look up namespace Schema and do this correctly
            return null;
        }

        /** @see org.geotools.xml.xsi.Any#getId() */
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

        /** @see org.geotools.xml.xsi.Any#getNamespace() */
        @Override
        public URI getNamespace() {
            return namespace;
        }

        /** @see org.geotools.xml.xsi.ElementGrouping#getGrouping() */
        @Override
        public int getGrouping() {
            return ANY;
        }

        @Override
        public Element findChildElement(String localName, URI namespaceURI) {
            // TODO look up namespace Schema and do this correctly
            return null;
        }
    }
}
