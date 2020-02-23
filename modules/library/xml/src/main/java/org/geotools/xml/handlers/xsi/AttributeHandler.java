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

import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.AttributeGT;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

/**
 * AttributeHandler purpose.
 *
 * <p>Represents an 'attribute' element
 *
 * <p>Example Use:
 *
 * <pre><code>
 *
 *  AttributeHandler x = new AttributeHandler(...);
 *
 * </code></pre>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class AttributeHandler extends XSIElementHandler {
    /** 'attribute' */
    public static final String LOCALNAME = "attribute";

    /** OPTIONAL */
    public static final int OPTIONAL = 0;

    /** PROHIBITED */
    public static final int PROHIBITED = 1;

    /** REQUIRED */
    public static final int REQUIRED = 2;

    private static int offset = 0;
    private String id;
    private String name;
    private String type;
    private String ref;
    private String def;
    private String fixed;
    private int use;
    private SimpleTypeHandler simpleType;
    private int hashCodeOffset = getOffset();
    private Attribute cache = null;

    /*
     * hashCode() helper
     */
    private static int getOffset() {
        return offset++;
    }

    /** @see java.lang.Object#hashCode() */
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return (LOCALNAME.hashCode()
                        * ((id == null) ? 1 : id.hashCode())
                        * ((type == null) ? 1 : type.hashCode())
                        * ((name == null) ? 1 : name.hashCode()))
                + hashCodeOffset;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    public XSIElementHandler getHandler(String namespaceURI, String localName) throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // simpleType
            if (SimpleTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SimpleTypeHandler sth = new SimpleTypeHandler();

                if (simpleType == null) {
                    simpleType = sth;
                } else {
                    throw new SAXNotRecognizedException(
                            "Extension may only have one 'simpleType' or 'complexType' declaration.");
                }

                return sth;
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

        String name1 = atts.getValue("", "name");

        if (name1 == null) {
            name1 = atts.getValue(namespaceURI, "name");
        }

        String ref1 = atts.getValue("", "ref");

        if (ref1 == null) {
            ref1 = atts.getValue(namespaceURI, "ref");
        }

        String type1 = atts.getValue("", "type");

        if (type1 == null) {
            type1 = atts.getValue(namespaceURI, "type");
        }

        this.name = name1;
        this.type = type1;
        this.ref = ref1;

        def = atts.getValue("", "default");

        if (def == null) {
            def = atts.getValue(namespaceURI, "default");
        }

        fixed = atts.getValue("", "fixed");

        if (fixed == null) {
            fixed = atts.getValue(namespaceURI, "fixed");
        }

        // form -- Ignore
        String use1 = atts.getValue("", "use");

        if (use1 == null) {
            use1 = atts.getValue(namespaceURI, "use");
        }

        this.use = findUse(use1);
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    public String getLocalName() {
        return LOCALNAME;
    }

    /** Convert the 'use' attribute to an int mask */
    public static int findUse(String use) {
        if ("optional".equalsIgnoreCase(use)) {
            return OPTIONAL;
        }

        if ("prohibited".equalsIgnoreCase(use)) {
            return PROHIBITED;
        }

        if ("required".equalsIgnoreCase(use)) {
            return REQUIRED;
        }

        return -1;
    }

    /** converts an int mask representing use to the string representation */
    public static String writeUse(int use) {
        switch (use) {
            case OPTIONAL:
                return "optional";

            case PROHIBITED:
                return "prohibited";

            case REQUIRED:
                return "required";

            default:
                return null;
        }
    }

    /** Returns the attribute name */
    public String getName() {
        return name;
    }

    /** creates a smaller simpler version */
    protected Attribute compress(SchemaHandler parent) throws SAXException {
        if (cache != null) {
            return cache;
        }

        // a.form = form; TODO add form support?
        SimpleType st = null;
        String name1 = this.name;
        String def1 = this.def;
        String fixed1 = this.fixed;
        int use1 = this.use;

        if (simpleType != null) {
            st = simpleType.compress(parent);
        } else {
            if ((ref != null) && !"".equalsIgnoreCase(ref)) {
                Attribute refA = parent.lookUpAttribute(ref);

                if (refA == null) {
                    throw new SAXException("Attribute '" + ref + "' was refered and not found");
                }

                st = refA.getSimpleType();
                name1 = refA.getName();
                use1 = use1 | refA.getUse();

                if ((def1 == null) || "".equalsIgnoreCase(def1)) {
                    def1 = refA.getDefault();
                }

                if ((fixed1 == null) || "".equalsIgnoreCase(fixed1)) {
                    fixed1 = refA.getFixed();
                }
            } else if ((type != null) && (!"".equalsIgnoreCase(type))) {
                // 	look it up --- find it
                st = parent.lookUpSimpleType(type);
            }
        }

        cache =
                new AttributeGT(
                        id, name1, parent.getTargetNamespace(), st, use1, def1, fixed1, false);

        id = type = ref = null;

        return cache;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandlerType() */
    public int getHandlerType() {
        return DEFAULT;
    }

    /** @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String, java.lang.String) */
    public void endElement(String namespaceURI, String localName) {
        // do nothing
    }
}
