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
package org.geotools.xml.xsi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.naming.OperationNotSupportedException;

import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeValue;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.AttributeValueGT;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * <p>
 * This class represents the pre-defined simpletypes included within the XML
 * schema definition.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @see Schema
 *
 * @source $URL$
 */
public class XSISimpleTypes {
    private static Map m;

    /** DOCUMENT ME! */
    public static final URI NAMESPACE = makeURI(
            "http://www.w3.org/2001/XMLSchema");

    // convinience method to deal with the URISyntaxException
    private static URI makeURI(java.lang.String s) {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            // do nothing
            return null;
        }
    }

    /**
     * <p>
     * Searches for the requested SimpleType, if not found this method returns
     * null;
     * </p>
     *
     * @param type the element localName
     *
     * @return SimpleType
     */
    public static SimpleType find(java.lang.String type) {
        if (m == null) {
            load();
        }

        SimpleType r = (SimpleType) m.get(type);

        return r;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return SimpleType
     */
    public static SimpleType find(Class type) {
        // assuming strings and class values will not conflict
        if (m == null) {
            load();
        }

        SimpleType r = (SimpleType) m.get(type);

        return r;
    }

    /*
     * loads the mapping of names -> simpletypes on demand
     */
    private static void load() {
        m = new HashMap();
        m.put(Integer.getInstance().getName(), Integer.getInstance());
        m.put(Decimal.getInstance().getName(), Decimal.getInstance());
        m.put(NegativeInteger.getInstance().getName(),
            NegativeInteger.getInstance());
        m.put(PositiveInteger.getInstance().getName(),
            PositiveInteger.getInstance());
        m.put(NonNegativeInteger.getInstance().getName(),
            NonNegativeInteger.getInstance());
        m.put(NonPositiveInteger.getInstance().getName(),
            NonPositiveInteger.getInstance());
        m.put(Long.getInstance().getName(), Long.getInstance());
        m.put(Int.getInstance().getName(), Int.getInstance());
        m.put(Short.getInstance().getName(), Short.getInstance());
        m.put(Byte.getInstance().getName(), Byte.getInstance());
        m.put(UnsignedLong.getInstance().getName(), UnsignedLong.getInstance());
        m.put(UnsignedShort.getInstance().getName(), UnsignedShort.getInstance());
        m.put(UnsignedInt.getInstance().getName(), UnsignedInt.getInstance());
        m.put(UnsignedByte.getInstance().getName(), UnsignedByte.getInstance());
        m.put(Float.getInstance().getName(), Float.getInstance());
        m.put(Double.getInstance().getName(), Double.getInstance());

        m.put(Date.getInstance().getName(), Date.getInstance());
        m.put(DateTime.getInstance().getName(), DateTime.getInstance());
        m.put(Duration.getInstance().getName(), Duration.getInstance());
        m.put(gDay.getInstance().getName(), gDay.getInstance());
        m.put(gMonth.getInstance().getName(), gMonth.getInstance());
        m.put(gMonthDay.getInstance().getName(), gMonthDay.getInstance());
        m.put(gYear.getInstance().getName(), gYear.getInstance());
        m.put(gYearMonth.getInstance().getName(), gYearMonth.getInstance());
        m.put(Time.getInstance().getName(), Time.getInstance());

        m.put(ID.getInstance().getName(), ID.getInstance());
        m.put(IDREF.getInstance().getName(), IDREF.getInstance());
        m.put(IDREFS.getInstance().getName(), IDREFS.getInstance());
        m.put(ENTITY.getInstance().getName(), ENTITY.getInstance());
        m.put(ENTITIES.getInstance().getName(), ENTITIES.getInstance());
        m.put(NMTOKEN.getInstance().getName(), NMTOKEN.getInstance());
        m.put(NMTOKENS.getInstance().getName(), NMTOKENS.getInstance());
        m.put(NOTATION.getInstance().getName(), NOTATION.getInstance());

        m.put(String.getInstance().getName(), String.getInstance());
        m.put(NormalizedString.getInstance().getName(),
            NormalizedString.getInstance());
        m.put(Token.getInstance().getName(), Token.getInstance());
        m.put(QName.getInstance().getName(), QName.getInstance());
        m.put(Name.getInstance().getName(), Name.getInstance());
        m.put(NCName.getInstance().getName(), NCName.getInstance());
        m.put(Boolean.getInstance().getName(), Boolean.getInstance());
        m.put(AnyURI.getInstance().getName(), AnyURI.getInstance());
        m.put(Base64Binary.getInstance().getName(), Base64Binary.getInstance());
        m.put(HexBinary.getInstance().getName(), HexBinary.getInstance());
        m.put(Language.getInstance().getName(), Language.getInstance());

        m.put(Integer.getInstance().getInstanceType(), Integer.getInstance());
        m.put(Decimal.getInstance().getInstanceType(), Decimal.getInstance());
        m.put(NegativeInteger.getInstance().getInstanceType(),
            NegativeInteger.getInstance());
        m.put(PositiveInteger.getInstance().getInstanceType(),
            PositiveInteger.getInstance());
        m.put(NonNegativeInteger.getInstance().getInstanceType(),
            NonNegativeInteger.getInstance());
        m.put(NonPositiveInteger.getInstance().getInstanceType(),
            NonPositiveInteger.getInstance());
        m.put(Long.getInstance().getInstanceType(), Long.getInstance());
        m.put(Int.getInstance().getInstanceType(), Int.getInstance());
        m.put(Short.getInstance().getInstanceType(), Short.getInstance());
        m.put(Byte.getInstance().getInstanceType(), Byte.getInstance());
        m.put(UnsignedLong.getInstance().getInstanceType(),
            UnsignedLong.getInstance());
        m.put(UnsignedShort.getInstance().getInstanceType(),
            UnsignedShort.getInstance());
        m.put(UnsignedInt.getInstance().getInstanceType(),
            UnsignedInt.getInstance());
        m.put(UnsignedByte.getInstance().getInstanceType(),
            UnsignedByte.getInstance());
        m.put(Float.getInstance().getInstanceType(), Float.getInstance());
        m.put(Double.getInstance().getInstanceType(), Double.getInstance());

        m.put(Date.getInstance().getInstanceType(), Date.getInstance());
        m.put(DateTime.getInstance().getInstanceType(), DateTime.getInstance());
        m.put(Duration.getInstance().getInstanceType(), Duration.getInstance());
        m.put(gDay.getInstance().getInstanceType(), gDay.getInstance());
        m.put(gMonth.getInstance().getInstanceType(), gMonth.getInstance());
        m.put(gMonthDay.getInstance().getInstanceType(), gMonthDay.getInstance());
        m.put(gYear.getInstance().getInstanceType(), gYear.getInstance());
        m.put(gYearMonth.getInstance().getInstanceType(),
            gYearMonth.getInstance());
        m.put(Time.getInstance().getInstanceType(), Time.getInstance());

        m.put(ID.getInstance().getInstanceType(), ID.getInstance());
        m.put(IDREF.getInstance().getInstanceType(), IDREF.getInstance());
        m.put(IDREFS.getInstance().getInstanceType(), IDREFS.getInstance());
        m.put(ENTITY.getInstance().getInstanceType(), ENTITY.getInstance());
        m.put(ENTITIES.getInstance().getInstanceType(), ENTITIES.getInstance());
        m.put(NMTOKEN.getInstance().getInstanceType(), NMTOKEN.getInstance());
        m.put(NMTOKENS.getInstance().getInstanceType(), NMTOKENS.getInstance());
        m.put(NOTATION.getInstance().getInstanceType(), NOTATION.getInstance());

        m.put(String.getInstance().getInstanceType(), String.getInstance());
        m.put(NormalizedString.getInstance().getInstanceType(),
            NormalizedString.getInstance());
        m.put(Token.getInstance().getInstanceType(), Token.getInstance());
        m.put(QName.getInstance().getInstanceType(), QName.getInstance());
        m.put(Name.getInstance().getInstanceType(), Name.getInstance());
        m.put(NCName.getInstance().getInstanceType(), NCName.getInstance());
        m.put(Boolean.getInstance().getInstanceType(), Boolean.getInstance());
        m.put(AnyURI.getInstance().getInstanceType(), AnyURI.getInstance());
        m.put(Base64Binary.getInstance().getInstanceType(),
            Base64Binary.getInstance());
        m.put(HexBinary.getInstance().getInstanceType(), HexBinary.getInstance());
        m.put(Language.getInstance().getInstanceType(), Language.getInstance());
    }

    /**
     * <p>
     * A generic implementation of a SimpleType for use by the xsi Schema.
     * </p>
     *
     * @author dzwiers
     */
    protected static abstract class XSISimpleType implements SimpleType {
        /**
         * @param name 
         *  
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#canCreateAttributes(org.geotools.xml.schema.Attribute,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canCreateAttributes(Attribute attribute, Object value,
            Map hints) {
            // TODO ensure equals works here
            return (value != null)
            && value.getClass().equals(getInstanceType())
            && (attribute.getSimpleType() != null)
            && this.getClass().equals(attribute.getSimpleType().getClass());
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // TODO ensure equals works here
            return (value != null)
            && value.getClass().equals(getInstanceType())
            && (element.getType() != null)
            && this.getClass().equals(element.getType().getClass());
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#toAttribute(org.geotools.xml.schema.Attribute,
         *      java.lang.Object, java.util.Map)
         */
        public AttributeValue toAttribute(Attribute attribute, Object value,
            Map hints) {
            return new AttributeValueGT(attribute, value.toString());
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException{
            if (element == null) {
                output.startElement(getNamespace(), getName(), null);
                output.characters(value.toString());
                output.endElement(getNamespace(), getName());
            } else {
                output.startElement(element.getNamespace(), element.getName(),
                    null);
                output.characters(((value == null) ? "" : value.toString()));
                output.endElement(element.getNamespace(), element.getName());
            }
        }

        /**
         * <p>
         * This method is intended to return an instance of the implemented
         * type.
         * </p>
         *
         * @return SimpleType
         *
         * @throws RuntimeException when not overridden
         */
        public static SimpleType getInstance() {
            throw new RuntimeException("This method must be overwritten");
        }

        /**
         * @see org.geotools.xml.xsi.Type#getParent()
         */
        public Type getParent() {
            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getNamespace()
         */
        public URI getNamespace() {
            return NAMESPACE;
        }

        /**
         * @see org.geotools.xml.xsi.SimpleType#getFinal()
         */
        public int getFinal() {
            return SimpleType.NONE;
        }

        /**
         * @see org.geotools.xml.xsi.SimpleType#getId()
         */
        public java.lang.String getId() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getChildType()
         */
        public int getChildType() {
            return NONE; // this should be ok
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getParents()
         */
        public SimpleType[] getParents() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getFacets()
         */
        public Facet[] getFacets() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        public Element findChildElement(java.lang.String name) {
            return null;
        }
    }

    /**
     * <p>
     * XSI Schema Integer
     * </p>
     *
     * @author dzwiers
     */
    public static class Integer extends XSISimpleType {
        private static SimpleType instance = new Integer();

        /**
         * @see org.geotools.xml.xsi.Type#getLocalName()
         */
        public java.lang.String getName() {
            return "integer";
        }

        /**
         * @param element
         * @param value
         * @param attrsgetValue
         * @param hints
         * @throws SAXException
         * @see org.geotools.xml.xsi.Type#getValue(org.geotools.xml.xsi.Element,
         *      org.geotools.xml.xsi.ElementValue[], org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrsgetValue, Map hints){
            if ((value.length >= 1) && (value[0].getValue() != null)) {
                return new java.lang.Integer((java.lang.String) value[0]
                    .getValue());
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Integer.class;
        }

        /**
         * @see org.geotools.xml.schemas.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Decimal
     * </p>
     *
     * @author dzwiers
     */
    public static class Decimal extends XSISimpleType {
        private static SimpleType instance = new Decimal();

        /**
         * @see org.geotools.xml.schemas.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getLocalName()
         */
        public java.lang.String getName() {
            return "decimal";
        }

        /**
         * @param element
         * @param value
         * @param attrs
         * @param hints
         * @throws SAXException
         * @see org.geotools.xml.xsi.Type#getValue(org.geotools.xml.xsi.Element,
         *      org.geotools.xml.xsi.ElementValue[], org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            if ((value.length == 1) && (value[0].getValue() != null)) {
                return new java.lang.Double((java.lang.String) value[0]
                    .getValue());
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Double.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NegativeInteger
     * </p>
     *
     * @author dzwiers
     */
    public static class NegativeInteger extends XSISimpleType {
        private static SimpleType instance = new NegativeInteger();

        /**
         * @see org.geotools.xml.schemas.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getLocalName()
         */
        public java.lang.String getName() {
            return "negativeInteger";
        }

        /**
         * @see org.geotools.xml.xsi.Type#getValue(org.geotools.xml.xsi.Element,
         *      org.geotools.xml.xsi.ElementValue[], org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Integer i = new java.lang.Integer((java.lang.String) value[0]
                        .getValue());

                return (i.intValue() < 0) ? i : null;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Integer.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NonNegativeInteger
     * </p>
     *
     * @author dzwiers
     */
    public static class NonNegativeInteger extends XSISimpleType {
        private static SimpleType instance = new NonNegativeInteger();

        /**
         * @see org.geotools.xml.schemas.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getLocalName()
         */
        public java.lang.String getName() {
            return "nonNegativeInteger";
        }

        /**
         * 
         * TODO summary sentence for getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs
         * @param hints
         * @return Object
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Integer i = new java.lang.Integer((java.lang.String) value[0]
                        .getValue());

                return (i.intValue() >= 0) ? i : null;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Integer.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of PositiveInteger
     * </p>
     *
     * @author dzwiers
     */
    public static class PositiveInteger extends XSISimpleType {
        private static SimpleType instance = new PositiveInteger();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "positiveInteger";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Integer i = new java.lang.Integer((java.lang.String) value[0]
                        .getValue());

                return (i.intValue() > 0) ? i : null;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Integer.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NonPositiveInteger
     * </p>
     *
     * @author dzwiers
     */
    public static class NonPositiveInteger extends XSISimpleType {
        private static SimpleType instance = new NonPositiveInteger();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "nonPositiveInteger";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Integer i = new java.lang.Integer((java.lang.String) value[0]
                        .getValue());

                return (i.intValue() <= 0) ? i : null;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Integer.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Long
     * </p>
     *
     * @author dzwiers
     */
    public static class Long extends XSISimpleType {
        private static SimpleType instance = new Long();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "long";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Long i = new java.lang.Long((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Long.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Int
     * </p>
     *
     * @author dzwiers
     */
    public static class Int extends XSISimpleType {
        private static SimpleType instance = new Int();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "int";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Integer i = new java.lang.Integer((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Integer.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Short
     * </p>
     *
     * @author dzwiers
     */
    public static class Short extends XSISimpleType {
        private static SimpleType instance = new Short();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "short";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Short i = new java.lang.Short((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Short.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Byte
     * </p>
     *
     * @author dzwiers
     */
    public static class Byte extends XSISimpleType {
        private static SimpleType instance = new Byte();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "byte";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Byte i = new java.lang.Byte((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Byte.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of UnsignedLong
     * </p>
     *
     * @author dzwiers
     */
    public static class UnsignedLong extends XSISimpleType {
        private static SimpleType instance = new UnsignedLong();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "unsignedLong";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Long i = new java.lang.Long((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Long.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of UnsignedShort
     * </p>
     *
     * @author dzwiers
     */
    public static class UnsignedShort extends XSISimpleType {
        private static SimpleType instance = new UnsignedShort();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "unsignedShort";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Short i = new java.lang.Short((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Short.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of UnsignedInt
     * </p>
     *
     * @author dzwiers
     */
    public static class UnsignedInt extends XSISimpleType {
        private static SimpleType instance = new UnsignedInt();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "unsignedInt";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Integer i = new java.lang.Integer((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Integer.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of UnsignedByte
     * </p>
     *
     * @author dzwiers
     */
    public static class UnsignedByte extends XSISimpleType {
        private static SimpleType instance = new UnsignedByte();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "unsignedByte";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Byte i = new java.lang.Byte((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Byte.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Float
     * </p>
     *
     * @author dzwiers
     */
    public static class Float extends XSISimpleType {
        private static SimpleType instance = new Float();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "float";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Float i = new java.lang.Float((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Float.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Double
     * </p>
     *
     * @author dzwiers
     */
    public static class Double extends XSISimpleType {
        private static SimpleType instance = new Double();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "double";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.Double i = new java.lang.Double((java.lang.String) value[0]
                        .getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Double.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Date
     * </p>
     *
     * @author dzwiers
     */
    public static class Date extends XSISimpleType {
        private static SimpleType instance = new Date();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "date";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                java.lang.String[] values = svalue.split("\\-");

                if (values.length != 3) {
                    throw new SAXException("Could not parse the DateTime "
                        + value);
                }

                int year;
                int month;
                int day;
                year = java.lang.Integer.parseInt(values[0]);
                month = java.lang.Integer.parseInt(values[1]);
                day = java.lang.Integer.parseInt(values[2]);

                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(year, month, day);

                return c.getTime();
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of DateTime
     * </p>
     *
     * @author dzwiers
     */
    public static class DateTime extends XSISimpleType {
        private static SimpleType instance = new DateTime();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "dateTime";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                java.lang.String[] values = svalue.split("T");

                if (values.length != 2) {
                    throw new SAXException("Could not parse the DateTime "
                        + value);
                }

                java.lang.String date = values[0];
                java.lang.String time = values[1];

                values = date.split("\\-");

                if (values.length != 3) {
                    throw new SAXException("Could not parse the DateTime "
                        + value);
                }

                int year;
                int month;
                int day;
                year = java.lang.Integer.parseInt(values[0]);
                month = java.lang.Integer.parseInt(values[1]);
                day = java.lang.Integer.parseInt(values[2]);

                values = time.split("\\:", 3);

                if (values.length != 3) {
                    throw new SAXException("Could not parse the DateTime "
                        + value);
                }

                int hour;
                int minute;
                int second;
                hour = java.lang.Integer.parseInt(values[0]);
                minute = java.lang.Integer.parseInt(values[1]);

                int i = values[2].indexOf("-");
                Calendar c = null;

                if (i == -1) {
                    i = values[2].indexOf("+");
                }

                if (i == -1) {
                    if (values[2].endsWith("Z")) {
                        second = (int) java.lang.Float.parseFloat(values[2]
                                .substring(0, values[2].length() - 1));
                    } else {
                        second = (int) java.lang.Float.parseFloat(values[2]);
                    }

                    TimeZone tz = TimeZone.getTimeZone("GMT");
                    c = Calendar.getInstance(tz);
                    c.clear();
                } else {
                    second = (int) java.lang.Float.parseFloat(values[2]
                            .substring(0, i));

                    TimeZone tz = TimeZone.getTimeZone("GMT"
                            + values[2].substring(i));
                    c = Calendar.getInstance(tz);
                    c.clear();
                }

                c.set(year, month, day, hour, minute, second);

                java.util.Date r = c.getTime();

                return r;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Duration
     * </p>
     *
     * @author dzwiers
     */
    public static class Duration extends XSISimpleType {
        private static SimpleType instance = new Duration();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "duration";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                int year;
                int month;
                int day;
                int hour;
                int minute;
                int second;
                year = month = day = hour = minute = second = 0;

                int index = 0;
                java.lang.String svalue = (java.lang.String) value[0].getValue();

                if (svalue.indexOf("P") == 0) {
                    index++;
                } else {
                    throw new SAXException("Malformed Duration: " + svalue);
                }

                java.lang.String[] svalues = svalue.split("\\D");
                int sindex = 0;

                if (svalues == null) {
                    return null;
                }

                if ((svalue.length() < index)
                        && (svalue.charAt(1 + svalues[sindex].length()) == 'Y')
                        && (sindex < svalues.length)) {
                    year = java.lang.Integer.parseInt(svalues[sindex]);
                    index += (svalues[sindex].length() + 1);
                    sindex++;
                }

                if ((svalue.length() < index)
                        && (svalue.charAt(1 + svalues[sindex].length()) == 'M')
                        && (sindex < svalues.length)) {
                    month = java.lang.Integer.parseInt(svalues[sindex]);
                    index += (svalues[sindex].length() + 1);
                    sindex++;
                }

                if ((svalue.length() < index)
                        && (svalue.charAt(1 + svalues[sindex].length()) == 'D')
                        && (sindex < svalues.length)) {
                    day = java.lang.Integer.parseInt(svalues[sindex]);
                    index += (svalues[sindex].length() + 1);
                    sindex++;
                }

                index++; // T

                if ((svalue.length() < index)
                        && (svalue.charAt(1 + svalues[sindex].length()) == 'H')
                        && (sindex < svalues.length)) {
                    hour = java.lang.Integer.parseInt(svalues[sindex]);
                    index += (svalues[sindex].length() + 1);
                    sindex++;
                }

                if ((svalue.length() < index)
                        && (svalue.charAt(1 + svalues[sindex].length()) == 'M')
                        && (sindex < svalues.length)) {
                    minute = java.lang.Integer.parseInt(svalues[sindex]);
                    index += (svalues[0].length() + 1);
                    sindex++;
                }

                if ((svalue.length() < index)
                        && (svalue.charAt(1 + svalues[sindex].length()) == 'S')
                        && (sindex < svalues.length)) {
                    second = java.lang.Integer.parseInt(svalues[sindex]);
                    index += (svalues[sindex].length() + 1);
                    sindex++;
                }

                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(year, month, day, hour, minute, second);

                java.util.Date r = c.getTime();

                return r;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of gDay
     * </p>
     *
     * @author dzwiers
     */
    public static class gDay extends XSISimpleType {
        private static SimpleType instance = new gDay();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "gDay";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                svalue = svalue.split("\\D*")[0]; // get digits;

                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(Calendar.DAY_OF_MONTH, java.lang.Integer.parseInt(svalue));

                return c.getTime();
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of gMonth
     * </p>
     *
     * @author dzwiers
     */
    public static class gMonth extends XSISimpleType {
        private static SimpleType instance = new gMonth();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "gMonth";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                svalue = svalue.split("\\D*")[0]; // get digits;

                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(Calendar.MONTH, java.lang.Integer.parseInt(svalue));

                return c.getTime();
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of gMonthDay
     * </p>
     *
     * @author dzwiers
     */
    public static class gMonthDay extends XSISimpleType {
        private static SimpleType instance = new gMonthDay();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "gMonthDay";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                java.lang.String[] t = svalue.split("\\D*"); // get digits;

                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(Calendar.DAY_OF_MONTH, java.lang.Integer.parseInt(t[0]));
                c.set(Calendar.MONTH, java.lang.Integer.parseInt(t[1]));

                return c.getTime();
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of gYear
     * </p>
     *
     * @author dzwiers
     */
    public static class gYear extends XSISimpleType {
        private static SimpleType instance = new gYear();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "gYear";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                java.lang.String[] t = svalue.split("\\D*"); // get digits;

                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(Calendar.YEAR, java.lang.Integer.parseInt(t[0]));

                return c.getTime();
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of gYearMonth
     * </p>
     *
     * @author dzwiers
     */
    public static class gYearMonth extends XSISimpleType {
        private static SimpleType instance = new gYearMonth();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "gYearMonth";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                java.lang.String[] t = svalue.split("\\D*"); // get digits;

                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(Calendar.MONTH, java.lang.Integer.parseInt(t[1]));
                c.set(Calendar.YEAR, java.lang.Integer.parseInt(t[0]));

                return c.getTime();
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Time
     * </p>
     *
     * @author dzwiers
     */
    public static class Time extends XSISimpleType {
        private static SimpleType instance = new Time();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "time";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String svalue = (java.lang.String) value[0].getValue();
                java.lang.String[] values = svalue.split("\\:", 3);

                if (values.length != 3) {
                    throw new SAXException("Could not parse the DateTime "
                        + value);
                }

                int hour;
                int minute;
                int second;
                hour = java.lang.Integer.parseInt(values[0]);
                minute = java.lang.Integer.parseInt(values[1]);

                int i = values[2].indexOf("-");
                Calendar c = null;

                if (i == -1) {
                    i = values[2].indexOf("+");
                }

                if (i == -1) {
                    if (values[2].endsWith("Z")) {
                        second = (int) java.lang.Float.parseFloat(values[2]
                                .substring(0, values[2].length() - 1));
                    } else {
                        second = (int) java.lang.Float.parseFloat(values[2]);
                    }

                    TimeZone tz = TimeZone.getTimeZone("GMT");
                    c = Calendar.getInstance(tz);
                    c.clear();
                } else {
                    second = (int) java.lang.Float.parseFloat(values[2]
                            .substring(0, i));

                    TimeZone tz = TimeZone.getTimeZone("GMT"
                            + values[2].substring(i));
                    c = Calendar.getInstance(tz);
                    c.clear();
                }

                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, second);

                java.util.Date r = c.getTime();

                return r;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.util.Date.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of ID
     * </p>
     *
     * @author dzwiers
     */
    public static class ID extends XSISimpleType {
        private static SimpleType instance = new ID();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "ID";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = (java.lang.String) value[0].getValue();

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of IDREF
     * </p>
     *
     * @author dzwiers
     */
    public static class IDREF extends XSISimpleType {
        private static SimpleType instance = new IDREF();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "IDREF";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = (java.lang.String) value[0].getValue();

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of IDREFS
     * </p>
     *
     * @author dzwiers
     */
    public static class IDREFS extends XSISimpleType {
        private static SimpleType instance = new IDREFS();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "IDREFS";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String[] i = ((java.lang.String) value[0].getValue())
                    .split("\\s");

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String[].class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of ENTITY
     * </p>
     *
     * @author dzwiers
     */
    public static class ENTITY extends XSISimpleType {
        private static SimpleType instance = new ENTITY();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "ENTITY";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of ENTITIES
     * </p>
     *
     * @author dzwiers
     */
    public static class ENTITIES extends XSISimpleType {
        private static SimpleType instance = new ENTITIES();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "ENTITIES";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String[] i = ((java.lang.String) value[0].getValue())
                    .split("\\s");

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String[].class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NMTOKEN
     * </p>
     *
     * @author dzwiers
     */
    public static class NMTOKEN extends XSISimpleType {
        private static SimpleType instance = new NMTOKEN();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "NMTOKEN";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NMTOKENS
     * </p>
     *
     * @author dzwiers
     */
    public static class NMTOKENS extends XSISimpleType {
        private static SimpleType instance = new NMTOKENS();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "NMTOKENS";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String[] i = ((java.lang.String) value[0].getValue())
                    .split("\\s");

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String[].class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NOTATION
     * </p>
     *
     * @author dzwiers
     */
    public static class NOTATION extends XSISimpleType {
        private static SimpleType instance = new NOTATION();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "NOTATION";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of String
     * </p>
     *
     * @author dzwiers
     */
    public static class String extends XSISimpleType {
        private static SimpleType instance = new String();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "string";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NormalizedString
     * </p>
     *
     * @author dzwiers
     */
    public static class NormalizedString extends XSISimpleType {
        private static SimpleType instance = new NormalizedString();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "normalizedString";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue())
                    .replaceAll("\\s", " ");

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Token
     * </p>
     *
     * @author dzwiers
     */
    public static class Token extends XSISimpleType {
        private static SimpleType instance = new Token();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "token";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue())
                    .replaceAll("\\s", " ");

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of QName
     * </p>
     *
     * @author dzwiers
     */
    public static class QName extends XSISimpleType {
        private static SimpleType instance = new QName();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "QName";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Name
     * </p>
     *
     * @author dzwiers
     */
    public static class Name extends XSISimpleType {
        private static SimpleType instance = new Name();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "Name";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of NCNAME
     * </p>
     *
     * @author dzwiers
     */
    public static class NCName extends XSISimpleType {
        private static SimpleType instance = new NCName();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "NCName";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Boolean
     * </p>
     *
     * @author dzwiers
     */
    public static class Boolean extends XSISimpleType {
        private static SimpleType instance = new Boolean();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "boolean";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String booleanValue = (java.lang.String) value[0].getValue();
                java.lang.Boolean parsedValue = "1".equals(booleanValue) ? java.lang.Boolean.TRUE
                        : java.lang.Boolean.valueOf(booleanValue);
                return parsedValue;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.Boolean.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of AnyURI
     * </p>
     *
     * @author dzwiers
     */
    public static class AnyURI extends XSISimpleType {
        private static SimpleType instance = new AnyURI();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "anyURI";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                try {
                    URI i = new URI((java.lang.String) value[0].getValue());

                    return i;
                } catch (URISyntaxException e) {
                    throw new SAXException(e);
                }
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return URI.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Base64Binary
     * </p>
     *
     * @author dzwiers
     */
    public static class Base64Binary extends XSISimpleType {
        private static SimpleType instance = new Base64Binary();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "base64Binary";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of HexBinary
     * </p>
     *
     * @author dzwiers
     */
    public static class HexBinary extends XSISimpleType {
        private static SimpleType instance = new HexBinary();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "hexBinary";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                java.lang.String i = ((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return java.lang.String.class;
        }
    }

    /**
     * <p>
     * XSI Schema instance of Language
     * </p>
     *
     * @author dzwiers
     */
    public static class Language extends XSISimpleType {
        private static SimpleType instance = new Language();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public java.lang.String getName() {
            return "language";
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) {
            if ((value.length == 1) && (value[0].getValue() != null)) {
                Locale i = new Locale((java.lang.String) value[0].getValue());

                return i;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Locale.class;
        }
    }
    
    public static class AnyType extends XSISimpleType implements ComplexType{

        private static SimpleType instance = new AnyType();

        /**
         * @see schema.xsi.XSISimpleTypes.XSISimpleType#getInstance()
         */
        public static SimpleType getInstance() {
            return instance;
        }

        /**
         * TODO summary sentence for isAbstract ...
         * 
         * @see org.geotools.xml.schema.ComplexType#isAbstract()
         */
        public boolean isAbstract() {
            return false;
        }

        /**
         * TODO summary sentence for getAnyAttributeNameSpace ...
         * 
         * @see org.geotools.xml.schema.ComplexType#getAnyAttributeNameSpace()
         */
        public java.lang.String getAnyAttributeNameSpace() {
            return null;
        }

        /**
         * TODO summary sentence for getAttributes ...
         * 
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * TODO summary sentence for getBlock ...
         * 
         * @see org.geotools.xml.schema.ComplexType#getBlock()
         */
        public int getBlock() {
            return 0;
        }

        /**
         * TODO summary sentence for getChild ...
         * 
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * TODO summary sentence for getChildElements ...
         * 
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * TODO summary sentence for isMixed ...
         * 
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return false;
        }

        /**
         * TODO summary sentence for isDerived ...
         * 
         * @see org.geotools.xml.schema.ComplexType#isDerived()
         */
        public boolean isDerived() {
            return false;
        }

        /**
         * TODO summary sentence for cache ...
         * 
         * @see org.geotools.xml.schema.ComplexType#cache(org.geotools.xml.schema.Element, java.util.Map)
         * @param element
         * @param hints
         */
        public boolean cache( Element element, Map hints ) {
            return false;
        }

        /**
         * TODO summary sentence for getValue ...
         * 
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         * @param element
         * @param value
         * @param attrs
         * @param hints
         * @throws SAXException
         * @throws OperationNotSupportedException
         */
        public Object getValue( Element element, ElementValue[] value, Attributes attrs, Map hints ) throws OperationNotSupportedException {
            if(element == null || element.getType() == null || value == null)
                throw new OperationNotSupportedException();
            if(value.length == 1 && value[0].getElement() == null)
                return value[0].getValue();
            Object[] r = new Object[value.length];
            for(int i=0;i<r.length;i++)
                r[i] = value[i].getValue();

            return r;
        }

        /**
         * TODO summary sentence for getName ...
         * 
         * @see org.geotools.xml.schema.Type#getName()
         */
        public java.lang.String getName() {
            return "AnyType";
        }

        /**
         * TODO summary sentence for getInstanceType ...
         * 
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }
    }
}
