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
import org.geotools.xml.handlers.XMLTypeHelper;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Type;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

/**
 * ElementTypeHandler purpose.
 *
 * <p>Represtents an 'element' declaration.
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class ElementTypeHandler extends ElementGroupingHandler {
    /** 'element' */
    public static final String LOCALNAME = "element";

    /** UNBOUNDED */
    private static int offset = 0;

    private String id;
    private String name;
    private String type;
    private String ref = null;
    private String defaulT;
    private String fixed;
    private String substitutionGroup;
    private int maxOccurs = 1;
    private int minOccurs = 1;
    private int finaL;
    private int block;
    private boolean form;
    private boolean abstracT;
    private boolean nillable;
    private Object child;

    // private List constraints;
    private int hashCodeOffset = getOffset();
    private DefaultElement cache = null;

    /*
     * helper for hashCode()
     */
    private static int getOffset() {
        return offset++;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return LOCALNAME.hashCode()
                        * (id == null ? 1 : id.hashCode())
                        * (ref == null ? 1 : ref.hashCode())
                        * (name == null ? 1 : name.hashCode())
                + hashCodeOffset;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XSIElementHandler getHandler(String namespaceURI, String localName) throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // simpleType
            if (SimpleTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SimpleTypeHandler sth = new SimpleTypeHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(
                            "Extension may only have one 'simpleType' or 'complexType' declaration.");
                }

                return sth;
            }

            // complexType
            if (ComplexTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ComplexTypeHandler sth = new ComplexTypeHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(
                            "Extension may only have one 'simpleType' or 'complexType' declaration.");
                }

                return sth;
            }

            // TODO add constraint checking
            //            // ref
            //            if (UniqueHandler.LOCALNAME.equalsIgnoreCase(localName)) {
            //                UniqueHandler sth = new UniqueHandler();
            //                constraints.add(sth);
            //
            //                return sth;
            //            }
            //
            //            // key
            //            if (KeyHandler.LOCALNAME.equalsIgnoreCase(localName)) {
            //                KeyHandler sth = new KeyHandler();
            //                constraints.add(sth);
            //
            //                return sth;
            //            }
            //
            //            // key
            //            if (KeyrefHandler.LOCALNAME.equalsIgnoreCase(localName)) {
            //                KeyrefHandler sth = new KeyrefHandler();
            //                constraints.add(sth);
            //
            //                return sth;
            //            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, Attributes atts) throws SAXException {
        // abstract
        String abstracT1 = atts.getValue("", "abstract");

        if (abstracT1 == null) {
            abstracT1 = atts.getValue(namespaceURI, "abstract");
        }

        if (abstracT1 == null || "".equalsIgnoreCase(abstracT1)) {
            this.abstracT = false;
        } else {
            if ("true".equals(abstracT1)) {
                this.abstracT = true;
            } else if ("false".equals(abstracT1)) {
                this.abstracT = false;
            } else {
                throw new SAXException(String.format(
                        "Schema element declaration supports 'abstract' \"true\" or \"false\" only (abstract=\"%s\")",
                        abstracT1));
            }
        }

        // block
        String block1 = atts.getValue("", "block");

        if (block1 == null) {
            block1 = atts.getValue(namespaceURI, "block");
        }

        this.block = ComplexTypeHandler.findBlock(block1);

        // default
        defaulT = atts.getValue("", "default");

        if (defaulT == null) {
            defaulT = atts.getValue(namespaceURI, "default");
        }

        // final
        String finaL1 = atts.getValue("", "final");

        if (finaL1 == null) {
            finaL1 = atts.getValue(namespaceURI, "final");
        }

        this.finaL = ComplexTypeHandler.findFinal(finaL1);

        // fixed
        fixed = atts.getValue("", "fixed");

        if (fixed == null) {
            fixed = atts.getValue(namespaceURI, "fixed");
        }

        // form
        String form1 = atts.getValue("", "form");

        if (form1 == null) {
            form1 = atts.getValue(namespaceURI, "form");
        }

        this.form = "qualified".equalsIgnoreCase(form1);

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

        if (maxOccurs1 != null && !"".equalsIgnoreCase(maxOccurs1)) {
            if ("unbounded".equalsIgnoreCase(maxOccurs1)) {
                this.maxOccurs = ElementGrouping.UNBOUNDED;
            } else {
                this.maxOccurs = Integer.parseInt(maxOccurs1);
            }
        } else {
            this.maxOccurs = 1;
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

        // name
        name = atts.getValue("", "name");

        if (name == null) {
            name = atts.getValue(namespaceURI, "name");
        }

        // nillable
        String nillable1 = atts.getValue("", "nillable");

        if (nillable1 == null) {
            nillable1 = atts.getValue(namespaceURI, "nillable");
        }

        if (nillable1 == null || "".equalsIgnoreCase(nillable1)) {
            this.nillable = false;
        } else {
            this.nillable = Boolean.parseBoolean(nillable1);
        }

        // ref
        ref = atts.getValue("", "ref");

        if (ref == null) {
            ref = atts.getValue(namespaceURI, "ref");
        }

        // substitutionGroup
        substitutionGroup = atts.getValue("", "substitutionGroup");

        if (substitutionGroup == null) {
            substitutionGroup = atts.getValue(namespaceURI, "substitutionGroup");
        }

        // type
        type = atts.getValue("", "type");

        if (type == null) {
            type = atts.getValue(namespaceURI, "type");
        }

        if (ref != null && !ref.isEmpty()) {
            if (name != null && !name.isEmpty()) {
                throw new SAXException(String.format(
                        "Schema element declaration cannot have both "
                                + "'ref' and 'name' attributes (ref=\"%s\", name=\"%s\")",
                        ref, name));
            }
            if (type != null && !type.isEmpty()) {
                throw new SAXException(String.format(
                        "Schema element declaration cannot have both "
                                + "'ref' and 'type' attributes (ref=\"%s\", type=\"%s\")",
                        ref, type));
            }
            name = type = ref;
        }
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    @Override
    public String getLocalName() {
        return LOCALNAME;
    }

    /** returns the element name */
    public String getName() {
        return name;
    }

    /** @see org.geotools.xml.XSIHandlers.ElementGroupingHandler#compress(org.geotools.xml.XSIHandlers.SchemaHandler) */
    @Override
    protected ElementGrouping compress(SchemaHandler parent) throws SAXException {

        synchronized (this) {
            if (cache != null) return cache;
            cache = new DefaultElement();
        }

        cache.id = id;
        cache.name = name;
        cache.namespace = parent.getTargetNamespace();
        cache.defaulT = defaulT;
        cache.fixed = fixed;
        cache.block = block;
        cache.finaL = finaL;
        cache.abstracT = abstracT;
        cache.form = form;
        cache.nillable = nillable;
        cache.minOccurs = minOccurs;
        cache.maxOccurs = maxOccurs;

        if (substitutionGroup != null) {
            cache.substitutionGroup = parent.lookUpElement(substitutionGroup);
        }

        if (child == null) {
            cache.type = parent.lookUpType(type);
        } else if (child instanceof SimpleTypeHandler) {
            cache.type = ((SimpleTypeHandler) child).compress(parent);
        } else {
            cache.type = ((ComplexTypeHandler) child).compress(parent);
        }

        if (ref != null) {
            Element e = parent.lookUpElement(ref);

            if (e == null) {
                throw new SAXException("Element '" + ref + "' was referenced and not found");
            }

            cache.name = e.getName();
            cache.type = e.getType();

            if (defaulT == null || "".equalsIgnoreCase(defaulT)) {
                cache.defaulT = e.getDefault();
            }

            if (fixed == null || "".equalsIgnoreCase(fixed)) {
                cache.fixed = e.getFixed();
            }

            if (block == 0) {
                cache.block = e.getBlock();
            }

            if (finaL == 0) {
                cache.finaL = e.getFinal();
            }

            cache.minOccurs = minOccurs == 1 ? e.getMinOccurs() : minOccurs;
            cache.maxOccurs = maxOccurs == 1 ? e.getMaxOccurs() : maxOccurs;

            if (substitutionGroup != null) {
                cache.substitutionGroup = e.getSubstitutionGroup();
            }
        }

        //  TODO add constraint checking

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
     * Default implementation of an element
     *
     * @author dzwiers
     */
    private static class DefaultElement implements Element {
        int block;
        int finaL;
        boolean abstracT;
        boolean form;
        boolean nillable;
        int maxOccurs;
        int minOccurs;
        String name;
        String id;
        String defaulT;
        String fixed;
        URI namespace;
        Element substitutionGroup;
        Type type;

        /** @see org.geotools.xml.xsi.ElementGrouping#findChildElement(java.lang.String) */
        @Override
        public Element findChildElement(String name1) {
            if (this.name != null) {
                if (this.name.equalsIgnoreCase(name1)) {
                    return this;
                }
            }

            return type == null ? null : type.findChildElement(name1);
        }

        /** @see org.geotools.xml.xsi.Element#isAbstract() */
        @Override
        public boolean isAbstract() {
            return abstracT;
        }

        /** @see org.geotools.xml.xsi.Element#getBlock() */
        @Override
        public int getBlock() {
            return block;
        }

        /** @see org.geotools.xml.xsi.Element#getDefault() */
        @Override
        public String getDefault() {
            return defaulT;
        }

        /** @see org.geotools.xml.xsi.Element#getFinal() */
        @Override
        public int getFinal() {
            return finaL;
        }

        /** @see org.geotools.xml.xsi.Element#getFixed() */
        @Override
        public String getFixed() {
            return fixed;
        }

        /** @see org.geotools.xml.xsi.Element#isForm() */
        @Override
        public boolean isForm() {
            return form;
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

        /** @see org.geotools.xml.xsi.Element#getLocalName() */
        @Override
        public String getName() {
            return name;
        }

        /** @see org.geotools.xml.xsi.Element#isNillable() */
        @Override
        public boolean isNillable() {
            return nillable;
        }

        /** @see org.geotools.xml.xsi.Element#getSubstitutionGroup() */
        @Override
        public Element getSubstitutionGroup() {
            return substitutionGroup;
        }

        /** @see org.geotools.xml.xsi.Element#getBinding() */
        @Override
        public Type getType() {
            return type;
        }

        @Override
        public int getGrouping() {
            return ELEMENT;
        }

        /** @see org.geotools.xml.xsi.Element#getId() */
        @Override
        public String getId() {
            return id;
        }

        /** @see org.geotools.xml.schema.Element#getNamespace() */
        @Override
        public URI getNamespace() {
            return namespace;
        }

        @Override
        public Element findChildElement(String localName, URI namespaceURI) {
            if (this.name != null) {
                if (this.name.equalsIgnoreCase(localName) && getNamespace().equals(namespaceURI)) {
                    return this;
                }
            }

            return type == null ? null : XMLTypeHelper.findChildElement(type, localName, namespaceURI);
        }
    }
}
