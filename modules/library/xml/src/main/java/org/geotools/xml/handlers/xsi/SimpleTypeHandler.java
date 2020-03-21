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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SimpleTypeGT;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

/**
 * SimpleTypeHandler purpose.
 *
 * <p>represents a simpleType element
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class SimpleTypeHandler extends XSIElementHandler {
    /** NONE */
    public static final int NONE = 0;

    /** ALL */
    public static final int ALL = 7;

    /** 'simpleType' */
    public static final String LOCALNAME = "simpleType";

    private static int offset = 0;
    private String id;
    private String name;
    private int finaL;
    private XSIElementHandler child; // one of List, Restriction or Union
    private int hashCodeOffset = getOffset();
    private SimpleType cache;

    /*
     * helper for hashCode()
     */
    private static int getOffset() {
        return offset++;
    }

    /** @see java.lang.Object#hashCode() */
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return (LOCALNAME.hashCode()
                        * ((id == null) ? 1 : id.hashCode())
                        * ((finaL == 0) ? 1 : finaL)
                        * ((name == null) ? 1 : name.hashCode()))
                + hashCodeOffset;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    public XSIElementHandler getHandler(String namespaceURI, String localName) throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // list
            if (ListHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ListHandler lh = new ListHandler();

                if (child == null) {
                    child = lh;
                } else {
                    throw new SAXNotRecognizedException(
                            getLocalName()
                                    + " may only have one '"
                                    + ListHandler.LOCALNAME
                                    + "' declaration.");
                }

                return lh;
            }

            // restriction
            if (RestrictionHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                RestrictionHandler lh = new RestrictionHandler();

                if (child == null) {
                    child = lh;
                } else {
                    throw new SAXNotRecognizedException(
                            getLocalName()
                                    + " may only have one '"
                                    + RestrictionHandler.LOCALNAME
                                    + "' declaration.");
                }

                return lh;
            }

            // union
            if (UnionHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                UnionHandler lh = new UnionHandler();

                if (child == null) {
                    child = lh;
                } else {
                    throw new SAXNotRecognizedException(
                            getLocalName()
                                    + " may only have one '"
                                    + UnionHandler.LOCALNAME
                                    + "' declaration.");
                }

                return lh;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String,
     *     org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName, Attributes atts) {
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        String finaL1 = atts.getValue("", "final");

        if (finaL1 == null) {
            finaL1 = atts.getValue(namespaceURI, "final");
        }

        this.finaL = findFinal(finaL1);

        name = atts.getValue("", "name");

        if (name == null) {
            name = atts.getValue(namespaceURI, "name");
        }
    }

    /** translates the final attribute to an integer mask */
    public static int findFinal(String finaL) {
        if ((finaL == null) || "".equalsIgnoreCase(finaL)) {
            return NONE;
        }

        String[] tokens = finaL.split("\\s");
        int r = 0;

        for (int i = 0; i < tokens.length; i++) {
            if ("#all".equalsIgnoreCase(tokens[i])) {
                r = ALL;
                i = tokens.length;
            } else {
                if ("union".equalsIgnoreCase(tokens[i])) {
                    r += UNION;
                } else {
                    if ("list".equalsIgnoreCase(tokens[i])) {
                        r += LIST;
                    } else {
                        if ("restriction".equalsIgnoreCase(tokens[i])) {
                            r += RESTRICTION;
                        }
                    }
                }
            }
        }

        return r;
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    public String getLocalName() {
        return LOCALNAME;
    }

    /** returns the simpletype's name */
    public String getName() {
        return name;
    }

    /** compacts the data resolving references. */
    protected SimpleType compress(SchemaHandler parent) {
        logger.info("Start compressing SimpleType " + getName());

        if (cache != null) {
            return cache;
        }

        Facet[] facets = null;

        if (child.getHandlerType() == SimpleType.RESTRICTION) {
            facets = getFacets((RestrictionHandler) child);
        }

        SimpleType[] simpleTypes = getSimpleTypes(child, parent);

        cache =
                new SimpleTypeGT(
                        id,
                        name,
                        parent.getTargetNamespace(),
                        child.getHandlerType(),
                        simpleTypes,
                        facets,
                        finaL);

        logger.info("End compressing SimpleType " + getName());
        id = null;
        child = null;

        return cache;
    }

    static SimpleType[] getSimpleTypes(XSIElementHandler child, SchemaHandler parent) {
        switch (child.getHandlerType()) {
            case RESTRICTION:
                return getSimpleTypes((RestrictionHandler) child, parent);

            case LIST:
                return getSimpleTypes((ListHandler) child, parent);

            case UNION:
                return getSimpleTypes((UnionHandler) child, parent);

            default:
                throw new RuntimeException(
                        "Should not be here ... child is one of the other three types.");
        }
    }

    static SimpleType[] getSimpleTypes(RestrictionHandler rest, SchemaHandler parent) {
        SimpleType[] children = new SimpleType[1];

        if (rest.getChild() != null) {
            children[0] = ((SimpleTypeHandler) rest.getChild()).compress(parent);
        } else {
            children[0] = parent.lookUpSimpleType(rest.getBase());
        }

        return children;
    }

    static SimpleType[] getSimpleTypes(ListHandler rest, SchemaHandler parent) {
        SimpleType[] children = new SimpleType[1];

        if (rest.getSimpleType() != null) {
            children[0] = (rest.getSimpleType()).compress(parent);
        } else {
            children[0] = parent.lookUpSimpleType(rest.getItemType());
        }

        return children;
    }

    static SimpleType[] getSimpleTypes(UnionHandler union, SchemaHandler parent) {
        List l = new LinkedList();

        if (union.getMemberTypes() != null) {
            String[] qNames = union.getMemberTypes().split("\\s");

            for (int i = 0; i < qNames.length; i++) l.add(parent.lookUpSimpleType(qNames[i]));
        }

        if (union.getSimpleTypes() != null) {
            Iterator i = union.getSimpleTypes().iterator();

            while (i.hasNext()) {
                l.add(((SimpleTypeHandler) i.next()).compress(parent));
            }
        }

        return (SimpleType[]) l.toArray(new SimpleType[l.size()]);
    }

    static Facet[] getFacets(RestrictionHandler rh) {
        List contraints = rh.getConstraints();

        if ((contraints == null) || (contraints.size() == 0)) {
            return null;
        }

        Facet[] facets = new Facet[contraints.size()];
        Iterator i = contraints.iterator();
        int index = 0;

        while (i.hasNext()) {
            FacetHandler fh = (FacetHandler) i.next();
            facets[index] = new FacetGT(fh.getType(), fh.getValue());
            index++;
        }

        return facets;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandlerType() */
    public int getHandlerType() {
        return SIMPLETYPE;
    }

    /** @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String, java.lang.String) */
    public void endElement(String namespaceURI, String localName) {
        // do nothing
    }
}
