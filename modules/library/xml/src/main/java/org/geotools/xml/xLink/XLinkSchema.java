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
package org.geotools.xml.xLink;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.AttributeValue;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.AttributeValueGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * <p>
 * This class is a hard-coded version of the XLink Schema. The results of
 * parsing the schema in through the parser and calling the resulting Schema
 * objects methods should be the same as calling these methods, except that
 * these methods should be faster.
 * </p>
 * 
 * <p>
 * This class consists of a series of internal private classes and an
 * implementation of the Schema interface which is public.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @see Schema
 *
 * @source $URL$
 */
public class XLinkSchema implements Schema {
    private static Schema instance = new XLinkSchema();

    // local list of attribute declarations
    private static final Attribute[] attributes = loadAttributes();

    // local list of attributeGroup declarations
    private static final AttributeGroup[] attributeGroups = loadAttributeGroups();

    /** The full xLink namespace */
    public static final URI NAMESPACE = makeURI("http://www.w3.org/1999/xlink");

    /**
     * @see org.geotools.xml.schema.Schema#getInstance()
     */
    public static Schema getInstance() {
        return instance;
    }

    /*
     * loads the list of attribute declarations for the XLink Schema
     */
    private static Attribute[] loadAttributes() {
        Attribute[] r = new Attribute[9];
        r[0] = Href.getInstance();
        r[1] = Role.getInstance();
        r[2] = Arcrole.getInstance();
        r[3] = Title.getInstance();
        r[4] = Show.getInstance();
        r[5] = Actuate.getInstance();
        r[6] = Label.getInstance();
        r[7] = From.getInstance();
        r[8] = To.getInstance();

        return r;
    }

    /*
     * loads the list of attributeGroup declarations for the XLink Schema
     */
    private static AttributeGroup[] loadAttributeGroups() {
        AttributeGroup[] r = new AttributeGroup[7];
        r[0] = SimpleLink.getInstance();
        r[1] = ExtendedLink.getInstance();
        r[2] = LocatorLink.getInstance();
        r[3] = ArcLink.getInstance();
        r[4] = ResourceLink.getInstance();
        r[5] = TitleLink.getInstance();
        r[6] = EmptyLink.getInstance();

        return r;
    }

    /**
     * @see schema.Schema#getAttributeGroups()
     */
    public AttributeGroup[] getAttributeGroups() {
        return attributeGroups;
    }

    /**
     * @see schema.Schema#getAttributeDescriptors()
     */
    public Attribute[] getAttributes() {
        return attributes;
    }

    /*
     * (non-Javadoc)
     *
     * @see schema.Schema#getBlockDefault()
     */
    public int getBlockDefault() {
        return NONE;
    }

    /**
     * @see schema.Schema#getComplexTypes()
     */
    public ComplexType[] getComplexTypes() {
        return new ComplexType[0];
    }

    /**
     * @see schema.Schema#getElements()
     */
    public Element[] getElements() {
        return new Element[0];
    }

    /**
     * @see schema.Schema#getFinalDefault()
     */
    public int getFinalDefault() {
        return NONE;
    }

    /**
     * @see schema.Schema#getGroups()
     */
    public Group[] getGroups() {
        return new Group[0];
    }

    /**
     * @see schema.Schema#getId()
     */
    public String getId() {
        return null;
    }

    /**
     * @see schema.Schema#getImports()
     */
    public Schema[] getImports() {
        return new Schema[0];
    }

    /**
     * @see schema.Schema#getSimpleTypes()
     */
    public SimpleType[] getSimpleTypes() {
        return new SimpleType[0];
    }

    /**
     * @see schema.Schema#getTargetNamespace()
     */
    public URI getTargetNamespace() {
        return NAMESPACE;
    }

    public URI getURI() {
        return NAMESPACE;
    }

    /**
     * @see schema.Schema#getVersion()
     */
    public String getVersion() {
        return "2.1.2";
    }

    /**
     * @see schema.Schema#includesURI(java.net.URI)
     */
    public boolean includesURI(URI uri) {
        //        if (uri.toString().toLowerCase().endsWith("xlinks.xsd")) {
        //            return true;
        //        }
        //
        //        return false;
        // this is a spec ... we never want the def modified.
        // TODO see if this affects printing
        return true;
    }

    // convinience method to deal with the URISyntaxException
    private static URI makeURI(String s) {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            // do nothing
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     */
    public String getPrefix() {
        return "xLink";
    }

    /**
     * @see schema.Schema#isAttributeFormDefault()
     */
    public boolean isAttributeFormDefault() {
        return false;
    }

    /**
     * @see schema.Schema#isElementFormDefault()
     */
    public boolean isElementFormDefault() {
        return false;
    }

    /**
     * ActuateSimpleType purpose.
     * 
     * <p>
     * A static representation of an Actuate type as defined in the XLink
     * Schema
     * </p>
     * 
     * <p></p>
     *
     * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
     * @author $Author:$ (last modification)
     * @version $Id$
     */
    private static class ActuateSimpleType implements SimpleType {
        // list of allowable enumeration values
        private static List lookUpTable = loadTable();

        /**
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            return null; // will never happen
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            //            return (lookUpTable.contains(value));
            return false; // it's an attribute
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints){
            // it's an attribute ... do nothing
        }

        public Class getInstanceType() {
            return String.class;
        }

        public org.geotools.xml.schema.Type getParent() {
            return null;
        }

        /**
         * @see schema.SimpleType#getNamespace()
         */
        public URI getNamespace() {
            return XLinkSchema.NAMESPACE;
        }

        /**
         * @see schema.SimpleType#getFinal()
         */
        public int getFinal() {
            return NONE;
        }

        /**
         * @see schema.SimpleType#getId()
         */
        public String getId() {
            return null;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public String getName() {
            return null;
        }

        private static List loadTable() {
            lookUpTable = new LinkedList();
            lookUpTable.add("onLoad");
            lookUpTable.add("onRequest");
            lookUpTable.add("other");
            lookUpTable.add("none");

            return lookUpTable;
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
            if ((value == null) || (value.length != 1) || (value[0] == null)) {
                return null;
            }

            if (value[0].getValue() instanceof String) {
                if (lookUpTable.contains(value[0].getValue())) {
                    return value[0].getValue();
                }

                throw new SAXException(
                    "The value speficified was not one of the expected values.");
            }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#toAttributes(org.geotools.xml.schema.Attribute,
         *      java.lang.Object, java.util.Map)
         */
        public AttributeValue toAttribute(Attribute attribute, Object value,
            Map hints) {
            if (canCreateAttributes(attribute, value, hints)) {
                return new AttributeValueGT(attribute, (String) value);
            }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#canCreateAttributes(org.geotools.xml.schema.Attribute,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canCreateAttributes(Attribute attribute, Object value,
            Map hints) {
            return (attribute.getName() != null)
            && attribute.getName().equals(Actuate.getInstance().getName())
            && lookUpTable.contains(value);
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getChildType()
         */
        public int getChildType() {
            return RESTRICTION;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getParents()
         */
        public SimpleType[] getParents() {
            return new SimpleType[] { XSISimpleTypes.String.getInstance() };
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getFacets()
         */
        public Facet[] getFacets() {
            return new Facet[] {
                new FacetGT(Facet.ENUMERATION, "onLoad"),
                new FacetGT(Facet.ENUMERATION, "onRequest"),
                new FacetGT(Facet.ENUMERATION, "other"),
                new FacetGT(Facet.ENUMERATION, "none"),
            };
        }
    }

    /**
     * ShowSimpleType purpose.
     * 
     * <p>
     * Represents the Show type in the XLink Schema
     * </p>
     * 
     * <p></p>
     *
     * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
     * @author $Author:$ (last modification)
     * @version $Id$
     */
    private static class ShowSimpleType implements SimpleType {
        // static enumeration list
        private static List lookUpTable = loadTable();

        /**
         * @see org.geotools.xml.schema.SimpleType#toAttributes(org.geotools.xml.schema.Attribute,
         *      java.lang.Object, java.util.Map)
         */
        public AttributeValue toAttribute(Attribute attribute, Object value,
            Map hints) {
            if (canCreateAttributes(attribute, value, hints)) {
                return new AttributeValueGT(attribute, (String) value);
            }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            return null; // will never happen
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#canCreateAttributes(org.geotools.xml.schema.Attribute,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canCreateAttributes(Attribute attribute, Object value,
            Map hints) {
            return (attribute.getName() != null)
            && attribute.getName().equals(Actuate.getInstance().getName())
            && lookUpTable.contains(value);
        }

        public Class getInstanceType() {
            return String.class;
        }

        public org.geotools.xml.schema.Type getParent() {
            return null;
        }

        /**
         * @see schema.SimpleType#getNamespace()
         */
        public URI getNamespace() {
            return XLinkSchema.NAMESPACE;
        }

        /**
         * @see schema.SimpleType#getFinal()
         */
        public int getFinal() {
            return NONE;
        }

        /**
         * @see schema.SimpleType#getId()
         */
        public String getId() {
            return null;
        }

        /**
         * @see schema.SimpleType#getLocalName()
         */
        public String getName() {
            return null;
        }

        private static List loadTable() {
            lookUpTable = new LinkedList();
            lookUpTable.add("new");
            lookUpTable.add("replace");
            lookUpTable.add("embed");
            lookUpTable.add("other");
            lookUpTable.add("none");

            return lookUpTable;
        }

        /**
         * @see schema.Type#getValue(java.lang.Object, org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints) throws SAXException {
            if ((value == null) || (value.length != 1) || (value[0] == null)) {
                return null;
            }

            if (value[0].getValue() instanceof String) {
                if (lookUpTable.contains(value[0].getValue())) {
                    return value[0].getValue();
                }

                throw new SAXException(
                    "The value speficified was not one of the expected values.");
            }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            //            return (lookUpTable.contains(value));
            return false; // it's an attribute
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints){
            // it's an attribute ... do nothing
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getChildType()
         */
        public int getChildType() {
            return RESTRICTION;
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getParents()
         */
        public SimpleType[] getParents() {
            return new SimpleType[] { XSISimpleTypes.String.getInstance() };
        }

        /**
         * @see org.geotools.xml.schema.SimpleType#getFacets()
         */
        public Facet[] getFacets() {
            return new Facet[] {
                new FacetGT(Facet.ENUMERATION, "new"),
                new FacetGT(Facet.ENUMERATION, "replace"),
                new FacetGT(Facet.ENUMERATION, "embed"),
                new FacetGT(Facet.ENUMERATION, "other"),
                new FacetGT(Facet.ENUMERATION, "none"),
            };
        }
    }

    /**
     * XLinkAttribute purpose.
     * 
     * <p>
     * Used to define some constant values for XLink attributes, such as
     * Namespace
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
     * @author $Author:$ (last modification)
     * @version $Id$
     */
    protected abstract static class XLinkAttribute implements Attribute {
        /**
         * @see schema.Attribute#getNameSpace()
         */
        public URI getNamespace() {
            return XLinkSchema.NAMESPACE;
        }

        /**
         * @see schema.Attribute#getDefault()
         */
        public String getDefault() {
            return null;
        }

        /**
         * @see schema.Attribute#isForm()
         */
        public boolean isForm() {
            return false;
        }

        /**
         * @see schema.Attribute#getId()
         */
        public String getId() {
            return null;
        }
    }

    /**
     * Href purpose.
     * 
     * <p>
     * Represents an Href Attribute in the XLink Schema
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
     * @author $Author:$ (last modification)
     * @version $Id$
     */
    public static class Href extends XLinkAttribute {
        // singleton instance
        static Attribute instance1;

        // internal storage for the attribute's use (required,optional ...)
        private int use = Attribute.OPTIONAL;

        /**
         * Href constructor.
         * 
         * <p>
         * Creates an instance of the Href Attribute of the XLink Schema
         * </p>
         *
         * @param use
         *
         * @see Attribute#getUse()
         */
        public Href(int use) {
            this.use = use;
        }

        /**
         * Href constructor
         * 
         * <p>
         * Creates an instance of the Href Attribute of the XLink Schema. Sets
         * the usage to Optional.
         * </p>
         */
        public Href() {
            use = Attribute.OPTIONAL;
        }

        /**
         * Returns a copy of the default instance.
         *
         * @return A default instance
         */
        public static Attribute getInstance() {
            if (instance1 == null) {
                instance1 = new Href();
            }

            return instance1;
        }

        /*
         * (non-Javadoc)
         *
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "href";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return use;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.AnyURI.getInstance();
        }
    }

    public static class Role extends XLinkAttribute {
        static Attribute instance1 = new Role();

        public static Attribute getInstance() {
            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "role";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.AnyURI.getInstance();
        }
    }

    /**
     * Arcrole represents an Arcrole attribute in the XLink Schema
     *
     * @author dzwiers
     */
    public static class Arcrole extends XLinkAttribute {
        // the instance singleton
        static Attribute instance1;

        // returns the default instance
        public static Attribute getInstance() {
            if (instance1 == null) {
                instance1 = new Arcrole();
            }

            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "arcrole";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.AnyURI.getInstance();
        }
    }

    /**
     * Title represents a Title attribute in the XLink Schema
     *
     * @author dzwiers
     */
    public static class Title extends XLinkAttribute {
        // the default singleton instance
        static Attribute instance1;

        /**
         * Returns the default singleton instance
         *
         */
        public static Attribute getInstance() {
            if (instance1 == null) {
                instance1 = new Title();
            }

            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "title";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.String.getInstance();
        }
    }

    /**
     * Show represents a Show attribute in the XLink Schema
     *
     * @author dzwiers
     */
    public static class Show extends XLinkAttribute {
        // the singleton instance
        static Attribute instance1;

        // the asociated simpletype
        private static SimpleType simpleType = new ShowSimpleType();

        /**
         * Returns the singleton instance
         *
         */
        public static Attribute getInstance() {
            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "show";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return simpleType;
        }
    }

    /**
     * Actuate represents an Actuate attribute in the XLink Schema
     *
     * @author dzwiers
     */
    public static class Actuate extends XLinkAttribute {
        // the singleton instance
        static Attribute instance1;

        // the simpletype for the attribute
        private static SimpleType simpleType = new ActuateSimpleType();

        /**
         * Return a singleton of an Actuate
         *
         */
        public static Attribute getInstance() {
            if (instance1 == null) {
                instance1 = new Actuate();
            }

            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "actuate";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return simpleType;
        }
    }

    /**
     * Label represents a Label attribute in the XLink Schema
     *
     * @author dzwiers
     */
    public static class Label extends XLinkAttribute {
        // the singleton instance
        static Attribute instance1;

        /**
         * Returns a singleton of Label
         *
         */
        public static Attribute getInstance() {
            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "label";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.String.getInstance();
        }
    }

    /**
     * From represents a From attribute in the XLink Schema
     *
     * @author dzwiers
     */
    public static class From extends XLinkAttribute {
        // the singleton instance
        static Attribute instance1;

        /**
         * Returns the singleton From instance
         *
         */
        public static Attribute getInstance() {
            if (instance1 == null) {
                instance1 = new From();
            }

            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "from";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.String.getInstance();
        }
    }

    /**
     * To represents a To attribute in the XLink Schema
     *
     * @author dzwiers
     */
    public static class To extends XLinkAttribute {
        // the static instance
        static Attribute instance1;

        /**
         * Returns the singleton To instance
         *
         */
        public static Attribute getInstance() {
            if (instance1 == null) {
                instance1 = new To();
            }

            return instance1;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return null;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "to";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.OPTIONAL;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.String.getInstance();
        }
    }

    /**
     * Type represents a Type attribute in the XLink Schema
     *
     * @author dzwiers
     */
    private static class Type extends XLinkAttribute {
        // the fixed value of this type
        private final String fixed;

        /**
         * Creates a Type attribute with the specified fixed value.
         *
         * @param fixed The fixed value.
         */
        public Type(String fixed) {
            this.fixed = fixed;
        }

        /**
         * @see schema.Attribute#getFixed()
         */
        public String getFixed() {
            return fixed;
        }

        /**
         * @see schema.Attribute#getLocalName()
         */
        public String getName() {
            return "type";
        }

        /**
         * @see schema.Attribute#getUse()
         */
        public int getUse() {
            return Attribute.REQUIRED;
        }

        /**
         * @see schema.Attribute#getSimpleType()
         */
        public SimpleType getSimpleType() {
            return XSISimpleTypes.String.getInstance();
        }
    }

    /**
     * XLinkAttributeGroup purpose.
     * 
     * <p>
     * Used to define some constant values for XLink attributeGroups, such as
     * Namespace
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
     * @author $Author:$ (last modification)
     * @version $Id$
     */
    protected abstract static class XLinkAttributeGroup
        implements AttributeGroup {
        /**
         * @see schema.AttributeGroup#getNameSpace()
         */
        public URI getNamespace() {
            return XLinkSchema.NAMESPACE;
        }

        /**
         * @see schema.AttributeGroup#getAnyAttributeNameSpace()
         */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /**
         * @see schema.AttributeGroup#getId()
         */
        public String getId() {
            return null;
        }
    }

    /**
     * SimpleLink represents a SimpleLink AttributeGroup in the XLink Schema
     *
     * @author dzwiers
     */
    public static class SimpleLink extends XLinkAttributeGroup {
        // the singleton instance
        private static AttributeGroup instance1;

        // the static attribute list
        private static final Attribute[] attributes1 = {
                Href.instance1, Role.instance1, Arcrole.instance1, Title.instance1,
                Show.instance1, Actuate.instance1,
            };

        /**
         * @see schema.AttributeGroup#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /**
         * @see schema.AttributeGroup#getLocalName()
         */
        public String getName() {
            return "simpleLink";
        }

        /**
         * Returns the singleton SimpleLink instance
         *
         */
        public static AttributeGroup getInstance() {
            if (instance1 == null) {
                instance1 = new SimpleLink();
            }

            return instance1;
        }
    }

    /**
     * ExtendedLink represents a ExtendedLink AttributeGroup in the XLink
     * Schema
     *
     * @author dzwiers
     */
    public static class ExtendedLink extends XLinkAttributeGroup {
        // the singleton instance
        private static AttributeGroup instance1;

        // the list of static attributes
        private static final Attribute[] attributes1 = {
                Role.instance1, Title.instance1,
            };

        /**
         * @see schema.AttributeGroup#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /**
         * @see schema.AttributeGroup#getLocalName()
         */
        public String getName() {
            return "extendedLink";
        }

        /**
         * Returns the singleton ExtendedLink instance
         *
         */
        public static AttributeGroup getInstance() {
            if (instance1 == null) {
                instance1 = new ExtendedLink();
            }

            return instance1;
        }
    }

    /**
     * LocatorLink represents a LocatorLink AttributeGroup in the XLink Schema
     *
     * @author dzwiers
     */
    public static class LocatorLink extends XLinkAttributeGroup {
        // the singleton instance
        private static AttributeGroup instance1;

        // the static attribute list
        private static final Attribute[] attributes1 = {
                new Type("extended"), new Href(Attribute.REQUIRED),
                Role.instance1, Title.instance1, Label.instance1
            };

        /**
         * @see schema.AttributeGroup#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /**
         * @see schema.AttributeGroup#getLocalName()
         */
        public String getName() {
            return "locatorLink";
        }

        /**
         * Returns the singleton LocatorLink instance
         *
         */
        public static AttributeGroup getInstance() {
            if (instance1 == null) {
                instance1 = new LocatorLink();
            }

            return instance1;
        }
    }

    /**
     * ArcLink represents a ArcLink AttributeGroup in the XLink Schema
     *
     * @author dzwiers
     */
    public static class ArcLink extends XLinkAttributeGroup {
        // the singleton instance
        private static AttributeGroup instance1;

        //	the static attribute list
        private static final Attribute[] attributes1 = {
                new Type("arc"), Arcrole.instance1, Title.instance1, Show.instance1,
                Actuate.instance1, From.instance1, To.instance1,
            };

        /**
         * @see schema.AttributeGroup#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /**
         * @see schema.AttributeGroup#getLocalName()
         */
        public String getName() {
            return "arcLink";
        }

        /**
         * Returns the singleton ArcLink instance
         *
         */
        public static AttributeGroup getInstance() {
            if (instance1 == null) {
                instance1 = new ArcLink();
            }

            return instance1;
        }
    }

    /**
     * ResourceLink represents a ResourceLink AttributeGroup in the XLink
     * Schema
     *
     * @author dzwiers
     */
    public static class ResourceLink extends XLinkAttributeGroup {
        // the singleton instance
        private static AttributeGroup instance1;

        // the static attribute list
        private static final Attribute[] attributes1 = {
                new Type("resource"), Role.instance1, Title.instance1,
                Label.instance1,
            };

        /**
         * @see schema.AttributeGroup#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /**
         * @see schema.AttributeGroup#getLocalName()
         */
        public String getName() {
            return "resourceLink";
        }

        /**
         * Returns the singleton ResourceLink instance
         *
         */
        public static AttributeGroup getInstance() {
            if (instance1 == null) {
                instance1 = new ResourceLink();
            }

            return instance1;
        }
    }

    /**
     * TitleLink represents a TitleLink AttributeGroup in the XLink Schema
     *
     * @author dzwiers
     */
    public static class TitleLink extends XLinkAttributeGroup {
        // the singleton instance
        private static AttributeGroup instance1;

        // the static attribute list
        static final Attribute[] attributes1 = { new Type("title"), };

        /**
         * @see schema.AttributeGroup#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /**
         * @see schema.AttributeGroup#getLocalName()
         */
        public String getName() {
            return "titleLink";
        }

        /**
         * Returns the singleton TitleLink instance
         *
         */
        public static AttributeGroup getInstance() {
            if (instance1 == null) {
                instance1 = new TitleLink();
            }

            return instance1;
        }
    }

    /**
     * EmptyLink represents a EmptyLink AttributeGroup in the XLink Schema
     *
     * @author dzwiers
     */
    public static class EmptyLink extends XLinkAttributeGroup {
        // the singleton instance
        private static AttributeGroup instance1;

        // the static attribute list
        static final Attribute[] attributes1 = { new Type("empty"), };

        /**
         * @see schema.AttributeGroup#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes1;
        }

        /**
         * @see schema.AttributeGroup#getLocalName()
         */
        public String getName() {
            return "emptyLink";
        }

        /**
         * Returns the singleton EmptyLink instance
         *
         */
        public static AttributeGroup getInstance() {
            if (instance1 == null) {
                instance1 = new EmptyLink();
            }

            return instance1;
        }
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
