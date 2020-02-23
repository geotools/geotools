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
package org.geotools.xml.schema.impl;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.naming.OperationNotSupportedException;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeValue;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.SimpleType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** @author dzwiers */
public class SimpleTypeGT implements SimpleType {
    // file visible to avoid set* methods
    private int finaL;
    private String id;
    private String name;
    private URI namespace;
    private SimpleType[] parents = null;
    private int type = 0;
    private Facet[] constraints;

    private SimpleTypeGT() {
        // should not be called
    }

    /** Creates a new SimpleTypeGT object. */
    public SimpleTypeGT(
            String id,
            String name,
            URI namespace,
            int type,
            SimpleType[] parents,
            Facet[] constraints,
            int finaL) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
        this.parents = parents;
        this.type = type;
        this.constraints = constraints;
        if (constraints != null) {
            for (int i = 0; i < constraints.length; i++)
                if (constraints[i] == null)
                    throw new NullPointerException(name + " constraint #" + i + " is null");
        }
    }

    /** @see org.geotools.xml.xsi.Type#getInstanceType() */
    public Class getInstanceType() {
        // if it's a union ... deal with it i guess
        return parents[0].getInstanceType();
    }

    /** @see org.geotools.xml.schema.Type#findChildElement(java.lang.String) */
    public Element findChildElement(String name1) {
        return null; // will never happen
    }

    /** @see org.geotools.xml.xsi.SimpleType#getFinal() */
    public int getFinal() {
        return finaL;
    }

    /** @see org.geotools.xml.xsi.SimpleType#getId() */
    public String getId() {
        return id;
    }

    /** @see org.geotools.xml.xsi.Type#getLocalName() */
    public String getName() {
        return name;
    }

    /** @see org.geotools.xml.xsi.Type#getLocalName() */
    public URI getNamespace() {
        return namespace;
    }

    /** @see org.geotools.xml.xsi.Type#getParent() */
    public SimpleType[] getParents() {
        return parents;
    }

    /**
     * This method ignores the attributes from the xml node
     *
     * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
     */
    public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
            throws OperationNotSupportedException, SAXException {
        if ((value == null) || (value.length != 1)) {
            throw new SAXException("can only have one text value ... and one is required");
        }

        if (type == UNION) {
            return getUnionValue(element, value[0], attrs, hints);
        }

        if (type == LIST) {
            return getListValue(element, value[0], attrs, hints);
        }

        return getRestValue(element, value[0], attrs, hints);
    }

    /*
     * Helper for getValue(Element,ElementValue[])
     */
    private Object getUnionValue(Element element, ElementValue value, Attributes attrs, Map hints)
            throws OperationNotSupportedException, SAXException {
        if (parents == null) {
            return null;
        }

        ElementValue[] valss = new ElementValue[1];
        valss[0] = value;

        for (int i = 0; i < parents.length; i++) {
            Object o = parents[0].getValue(element, valss, attrs, hints);

            if (o != null) {
                return o;
            }
        }

        return null;
    }

    /*
     * Helper for getValue(Element,ElementValue[])
     */
    private Object getListValue(Element element, ElementValue value, Attributes attrs, Map hints)
            throws OperationNotSupportedException, SAXException {
        if ((parents == null) || (parents[0] == null)) {
            return null;
        }

        String[] vals = ((String) value.getValue()).split("\\s");
        List l = new LinkedList();
        ElementValueGT[] valss = new ElementValueGT[1];

        for (int i = 0; i < vals.length; i++) {
            valss[0] = new ElementValueGT(value.getElement(), vals[i]);
            l.add(parents[0].getValue(element, valss, attrs, hints));
        }

        valss[0] = new ElementValueGT(value.getElement(), l);

        return valss[0];
    }

    /*
     * Helper for getValue(Element,ElementValue[])
     */
    private Object getRestValue(Element element, ElementValue value, Attributes attrs, Map hints)
            throws OperationNotSupportedException, SAXException {
        if ((parents == null) || (parents[0] == null)) {
            return null;
        }

        if (constraints == null) {
            return null;
        }

        if (constraints.length == 0) {
            ElementValue[] t = new ElementValue[1];
            t[0] = value;

            return parents[0].getValue(element, t, attrs, hints);
        }

        String val = (String) value.getValue();

        if (val != null && constraints[0].getFacetType() == Facet.ENUMERATION) {
            for (int i = 0; i < constraints.length; i++) {
                if (val.equalsIgnoreCase(constraints[i].getValue())) {
                    ElementValue[] t = new ElementValue[1];
                    t[0] = value;

                    return parents[0].getValue(element, t, attrs, hints);
                }
            }

            return null;
        }

        Number nval = null;
        Date dval = null;

        ElementValue[] t = new ElementValue[1];
        t[0] = value;

        Object o = parents[0].getValue(element, t, attrs, hints);

        if (o instanceof Number) {
            nval = (Number) o;
        }

        if (o instanceof Date) {
            dval = (Date) o;
        }

        // check each constraint
        for (int i = 0; i < constraints.length; i++) {
            switch (constraints[i].getFacetType()) {
                case Facet.ENUMERATION:
                    /*throw new SAXException(
                    "cannot have enumerations mixed with other facets.");*/
                    break;

                case Facet.FRACTIONDIGITS:
                    int decimals = val.length() - val.indexOf(".");
                    int maxDec = Integer.parseInt(constraints[i].getValue());

                    if (decimals > maxDec) {
                        throw new SAXException("Too many decimal places");
                    }

                    break;

                case Facet.LENGTH:
                    int maxLength = Integer.parseInt(constraints[i].getValue());

                    if (val.length() != maxLength) {
                        throw new SAXException("Too long places");
                    }

                    break;

                case Facet.MAXEXCLUSIVE:
                    if (nval != null) {
                        Double max = Double.valueOf(constraints[i].getValue());

                        if (nval.doubleValue() > max.doubleValue()) {
                            throw new SAXException("Too large a value");
                        }
                    }

                    if (dval != null) {
                        Date max;

                        try {
                            max = DateFormat.getDateTimeInstance().parse(constraints[i].getValue());
                        } catch (ParseException e) {
                            throw new SAXException(e);
                        }

                        if (dval.after(max)) {
                            throw new SAXException("Too large a value");
                        }
                    }

                    break;

                case Facet.MAXINCLUSIVE:
                    if (nval != null) {
                        Double max = Double.valueOf(constraints[i].getValue());

                        if (nval.doubleValue() >= max.doubleValue()) {
                            throw new SAXException("Too large a value");
                        }
                    }

                    if (dval != null) {
                        Date max;

                        try {
                            max = DateFormat.getDateTimeInstance().parse(constraints[i].getValue());
                        } catch (ParseException e) {
                            throw new SAXException(e);
                        }

                        if (dval.compareTo(max) > 0) {
                            throw new SAXException("Too large a value");
                        }
                    }

                case Facet.MAXLENGTH:
                    maxLength = Integer.parseInt(constraints[i].getValue());

                    if (val.length() > maxLength) {
                        throw new SAXException("Too long places");
                    }

                    break;

                case Facet.MINEXCLUSIVE:
                    if (nval != null) {
                        Double max = Double.valueOf(constraints[i].getValue());

                        if (nval.doubleValue() < max.doubleValue()) {
                            throw new SAXException("Too large a value");
                        }
                    }

                    if (dval != null) {
                        Date max;

                        try {
                            max = DateFormat.getDateTimeInstance().parse(constraints[i].getValue());
                        } catch (ParseException e) {
                            throw new SAXException(e);
                        }

                        if (dval.before(max)) {
                            throw new SAXException("Too large a value");
                        }
                    }

                case Facet.MININCLUSIVE:
                    if (nval != null) {
                        Double max = Double.valueOf(constraints[i].getValue());

                        if (nval.doubleValue() <= max.doubleValue()) {
                            throw new SAXException("Too large a value");
                        }
                    }

                    if (dval != null) {
                        Date max;

                        try {
                            max = DateFormat.getDateTimeInstance().parse(constraints[i].getValue());
                        } catch (ParseException e) {
                            throw new SAXException(e);
                        }

                        if (dval.compareTo(max) < 0) {
                            throw new SAXException("Too large a value");
                        }
                    }

                case Facet.MINLENGTH:
                    maxLength = Integer.parseInt(constraints[i].getValue());

                    if (val.length() < maxLength) {
                        throw new SAXException("Too short places");
                    }

                    break;

                case Facet.PATTERN:
                    if (val.split(constraints[i].getValue()).length != 0) {
                        throw new SAXException("Does not match pattern");
                    }

                    break;

                case Facet.TOTALDIGITS:
                    maxLength = Integer.parseInt(constraints[i].getValue()) + 1;

                    if (val.length() > maxLength) {
                        throw new SAXException("Too many digits");
                    }

                    break;
            }
        }

        return o;
    }

    /** @see org.geotools.xml.schema.SimpleType#getChildType() */
    public int getChildType() {
        return type;
    }

    /** @see org.geotools.xml.schema.SimpleType#getFacets() */
    public Facet[] getFacets() {
        return constraints;
    }

    /**
     * @see org.geotools.xml.schema.SimpleType#toAttribute(org.geotools.xml.schema.Attribute,
     *     java.lang.Object, java.util.Map)
     */
    public AttributeValue toAttribute(Attribute attribute, Object value, Map hints)
            throws OperationNotSupportedException {
        if (value == null) {
            return null;
        }

        if (type == UNION) {
            for (int i = 0; i < parents.length; i++) {
                // finds first that works
                // TODO check that 'equals' works here
                if (parents[i].equals(attribute.getSimpleType())
                        && parents[i].canCreateAttributes(attribute, value, hints)) {
                    return parents[i].toAttribute(attribute, value, hints);
                }
            }

            return parents[0].toAttribute(attribute, value, hints);
        }

        if (type == LIST) {
            List l = (List) value;
            Iterator i = l.iterator();
            String s = "";

            if (i.hasNext()) {
                Object t = parents[0].toAttribute(attribute, i.next(), hints).getValue();
                s = t.toString();

                while (i.hasNext()) {
                    t = parents[0].toAttribute(attribute, i.next(), hints).getValue();
                    s = s + " " + t.toString();
                }
            }

            return new AttributeValueGT(attribute, s);
        }

        return parents[0].toAttribute(attribute, value, hints);
    }

    /**
     * @see
     *     org.geotools.xml.schema.SimpleType#canCreateAttributes(org.geotools.xml.schema.Attribute,
     *     java.lang.Object, java.util.Map)
     */
    public boolean canCreateAttributes(Attribute attribute, Object value, Map hints) {
        if (value == null) {
            return false;
        }

        if (type == UNION) {
            for (int i = 0; i < parents.length; i++) {
                // finds first that works
                // TODO check that 'equals' works here
                if (parents[i].equals(attribute.getSimpleType())
                        && parents[i].canCreateAttributes(attribute, value, hints)) {
                    return true;
                }
            }

            return false;
        }

        if (type == LIST) {
            return parents[0].canCreateAttributes(attribute, value, hints);
        }

        return parents[0].canCreateAttributes(attribute, value, hints);
    }

    /**
     * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
     *     java.lang.Object, java.util.Map)
     */
    public boolean canEncode(Element element, Object value, Map hints) {
        if (value == null) {
            return false;
        }

        if (type == UNION) {
            for (int i = 0; i < parents.length; i++) {
                // finds first that works
                // TODO check that 'equals' works here
                if (parents[i].equals(element.getType())
                        && parents[i].canEncode(element, value, hints)) {
                    return true;
                }
            }

            return false;
        }

        if (type == LIST) {
            return parents[0].canEncode(element, value, hints);
        }

        return parents[0].canEncode(element, value, hints);
    }

    /**
     * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object,
     *     org.geotools.xml.PrintHandler, java.util.Map)
     */
    public void encode(Element element, Object value, PrintHandler output, Map hints)
            throws IOException, OperationNotSupportedException {
        if (value == null) {
            return;
        }

        if (type == UNION) {
            for (int i = 0; i < parents.length; i++) {
                // finds first that works
                // TODO check that 'equals' works here
                if (parents[i].equals(element.getType())
                        && parents[i].canEncode(element, value, hints)) {
                    parents[i].encode(element, value, output, hints);
                }

                return;
            }

            parents[0].encode(element, value, output, hints);

            return;
        }

        if (type == LIST) {
            List l = (List) value;
            Iterator i = l.iterator();
            String s = "";

            if (i.hasNext()) {
                Object t =
                        parents[0]
                                .toAttribute(
                                        new AttributeGT(
                                                null,
                                                null,
                                                namespace,
                                                parents[0],
                                                0,
                                                null,
                                                null,
                                                false),
                                        value,
                                        hints)
                                .getValue();
                s = t.toString();

                while (i.hasNext()) {
                    t =
                            parents[0]
                                    .toAttribute(
                                            new AttributeGT(
                                                    null,
                                                    null,
                                                    namespace,
                                                    parents[0],
                                                    0,
                                                    null,
                                                    null,
                                                    false),
                                            value,
                                            hints)
                                    .getValue();
                    s = s + " " + t.toString();
                }
            }

            parents[0].encode(element, s, output, hints);

            return;
        }

        parents[0].encode(element, value, output, hints);
    }
}
