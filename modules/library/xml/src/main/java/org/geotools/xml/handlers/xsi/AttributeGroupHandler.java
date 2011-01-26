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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.impl.AttributeGroupGT;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * AttributeGroupHandler purpose.
 * 
 * <p>
 * Represents an 'attributeGroup' element.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class AttributeGroupHandler extends XSIElementHandler {
    /** 'attributeGroup' */
    public final static String LOCALNAME = "attributeGroup";
    private static int offset = 0;
    private String id;
    private String name;
    private String ref;
    private AnyAttributeHandler anyAttribute;
    private List attrDecs;
    private int hashCodeOffset = getOffset();
    private AttributeGroup cache = null;

    /*
     * Helper method for hashCode
     */
    private static int getOffset() {
        return offset++;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()) * ((ref == null)
        ? 1 : ref.hashCode()) * ((name == null) ? 1 : name.hashCode()))
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
            // attribute
            if (AttributeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attrDecs == null) {
                    attrDecs = new LinkedList();
                }

                AttributeHandler ah = new AttributeHandler();
                attrDecs.add(ah);

                return ah;
            }

            // attributeGroup
            if (AttributeGroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attrDecs == null) {
                    attrDecs = new LinkedList();
                }

                AttributeGroupHandler ah = new AttributeGroupHandler();
                attrDecs.add(ah);

                return ah;
            }

            // anyAttribute
            if (AnyAttributeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                AnyAttributeHandler sth = new AnyAttributeHandler();

                if (anyAttribute == null) {
                    anyAttribute = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
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

        name = atts.getValue("", "name");

        if (name == null) {
            name = atts.getValue(namespaceURI, "name");
        }

        ref = atts.getValue("", "ref");

        if (ref == null) {
            ref = atts.getValue(namespaceURI, "ref");
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * returns the 'name' attribute
     *
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Reduces the memory imprint returning a smaller object
     * </p>
     *
     * @param parent
     *
     *
     * @throws SAXException
     */
    protected AttributeGroup compress(SchemaHandler parent)
        throws SAXException {
        if (cache != null) {
            return cache;
        }

        String anyAttributeNamespace = (anyAttribute == null) ? null
                                                              : anyAttribute
            .getNamespace();
        Attribute[] attributes = null;

        if (attrDecs != null) {
            Iterator i = attrDecs.iterator();
            HashSet h = new HashSet();

            while (i.hasNext()) {
                Object o = i.next();

                if (o instanceof AttributeHandler) {
                    h.add(((AttributeHandler) o).compress(parent));
                } else {
                    AttributeGroupHandler agh = (AttributeGroupHandler) o;
                    AttributeGroup ag = agh.compress(parent);

                    if ((ag != null) && (ag.getAttributes() != null)) {
                        Attribute[] aa = ag.getAttributes();

                        for (int j = 0; j < aa.length; j++)
                            h.add(aa[j]);
                    }
                }
            }

            attributes = (Attribute[]) h.toArray(new Attribute[h.size()]);
        }

        String name1 = this.name;

        if ((ref != null) && !"".equalsIgnoreCase(ref)) {
            AttributeGroup ag = parent.lookUpAttributeGroup(ref);

            if (ag == null) {
                throw new SAXException("AttributeGroup '" + ref
                    + "' was refered and not found");
            }

            name1 = ag.getName();

            if ((anyAttribute == null)
                    || "".equalsIgnoreCase(anyAttribute.getNamespace())) {
                anyAttributeNamespace = ag.getAnyAttributeNameSpace();
            }

            if (attributes != null) {
                throw new SAXException(
                    "Cannot have a ref and children for an AttributeGroup");
            }

            attributes = ag.getAttributes();
        }

        cache = new AttributeGroupGT(id, name1,
                parent.getTargetNamespace(), attributes, anyAttributeNamespace);

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
}
