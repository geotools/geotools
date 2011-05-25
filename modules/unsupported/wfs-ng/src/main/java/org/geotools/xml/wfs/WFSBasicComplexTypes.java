/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.xml.wfs;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.data.Query;
import org.geotools.filter.Filter;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.filter.FilterSchema;
import org.geotools.xml.gml.GMLComplexTypes;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.AttributeGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.wfs.WFSSchema.WFSAttribute;
import org.geotools.xml.wfs.WFSSchema.WFSComplexType;
import org.geotools.xml.wfs.WFSSchema.WFSElement;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/xml/wfs/WFSBasicComplexTypes.java $
 */
public class WFSBasicComplexTypes {
    public final static String LOCK_KEY = "WFSBasicComplexTypes.LOCKID.KEY";

    /**
     * <p>
     * This class represents an GetFeatureTypeType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * GetFeatureTypeType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class GetFeatureType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new GetFeatureType();
        private static Element[] elems = new Element[] {
                new WFSElement("Query", QueryType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null),
            };
        private static Sequence seq = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "1.0.0";
                        }
                    }
                ,
                new WFSAttribute("service",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "WFS";
                        }
                    }
                ,
                new WFSAttribute("outputFormat",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "GML2";
                        }
                    }
                ,
                new WFSAttribute("maxFeatures",
                    XSISimpleTypes.PositiveInteger.getInstance(),
                    Attribute.OPTIONAL)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints){
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GetFeatureType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Query.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((element.getType() != null)
                    && getName().equals(element.getType().getName())) {
                return (value == null) || value instanceof Query;
            }

            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (canEncode(element, value, hints)) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[0].getName(), null, "string", attrs[0].getFixed());
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[1].getName(), null, "string", attrs[1].getFixed());
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[2].getName(), null, "string", attrs[2].getFixed());

                Query query = (Query) value;

                if ((query != null)
                        && (query.getMaxFeatures() != Query.DEFAULT_MAX)) {
                    attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                            attrs[3].getName(), null, "integer",
                        "" + query.getMaxFeatures());
                }

                output.startElement(element.getNamespace(), element.getName(),
                    attributes);

                if (elems[0].getType().canEncode(elems[0], value, hints)) {
                    elems[0].getType().encode(elems[0], value, output, hints);
                }

                output.endElement(element.getNamespace(), element.getName());
            } else {
                throw new OperationNotSupportedException(
                    "not a valid value/element for a DescribeFeatureTypeType.");
            }
        }
    }

    /**
     * <p>
     * This class represents an DescribeFeatureType within the WFS Schema. This
     * includes both the data and parsing functionality associated with a
     * DescribeFeatureType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class DescribeFeatureTypeType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new DescribeFeatureTypeType();
        private static Element[] elems = new Element[] {
                new WFSElement("TypeName", XSISimpleTypes.QName.getInstance(),
                    0, Integer.MAX_VALUE, false, null),
            };
        private static Sequence seq = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "1.0.0";
                        }
                    }
                ,
                new WFSAttribute("service",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "WFS";
                        }
                    }
                ,
                new WFSAttribute("outputFormat",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "XMLSCHEMA";
                        }
                    }
                ,
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints){
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "DescribeFeatureTypeType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((element.getType() != null)
                    && getName().equals(element.getType().getName())) {
                return value instanceof String[];
            }

            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (canEncode(element, value, hints)) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[0].getName(), null, "string", attrs[0].getFixed());
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[1].getName(), null, "string", attrs[1].getFixed());
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[2].getName(), null, "string", attrs[2].getFixed());

                output.startElement(element.getNamespace(), element.getName(),
                    attributes);

                String[] strs = (String[]) value;

                for (int i = 0; i < strs.length; i++)
                    XSISimpleTypes.QName.getInstance().encode(elems[0],
                        strs[i], output, hints);

                output.endElement(element.getNamespace(), element.getName());
            } else {
                throw new OperationNotSupportedException(
                    "not a valid value/element for a DescribeFeatureTypeType.");
            }
        }
    }

    /**
     * <p>
     * This class represents an GetCapabilitiesType within the WFS Schema. This
     * includes both the data and parsing functionality associated with a
     * GetCapabilitiesType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class GetCapabilitiesType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new GetCapabilitiesType();
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "1.0.0";
                        }
                    }
                ,
                new WFSAttribute("service",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED) {
                        public String getFixed() {
                            return "WFS";
                        }
                    }
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints){
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GetCapabilitiesType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return (element.getType() != null)
            && getName().equals(element.getType().getName()) && (value == null);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[0].getName(), null, "string", attrs[0].getFixed());
            attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                attrs[1].getName(), null, "string", attrs[1].getFixed());

            output.element(element.getNamespace(), element.getName(), attributes);
        }
    }

    static class QueryType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new QueryType();
        private static Element[] elems = new Element[] {
                
                // PropertyName -- used to limit attributes
                new WFSElement(FilterSchema.getInstance().getElements()[34], 0,
                    Integer.MAX_VALUE) {
                        public URI getNamespace() {
                            return FilterSchema.NAMESPACE;
                        }
                    }
                ,
                
                // Filter -- used to limit features
                new WFSElement(FilterSchema.getInstance().getElements()[2], 0,
                    Integer.MAX_VALUE) {
                        public URI getNamespace() {
                            return FilterSchema.NAMESPACE;
                        }
                    }
            };
        private static Sequence seq = new SequenceGT(elems);
        private static Attribute[] attrs = new Attribute[] {
                new WFSAttribute("handle", XSISimpleTypes.String.getInstance(),
                    Attribute.OPTIONAL),
                new WFSAttribute("typeName",
                    XSISimpleTypes.QName.getInstance(), Attribute.REQUIRED),
                new WFSAttribute("featureVersion",
                    XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs1, Map hints){
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "QueryType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Query.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((element.getType() != null)
                    && getName().equals(element.getType().getName())) {
                return (value == null) || value instanceof Query;
            }

            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if (canEncode(element, value, hints)) {
                Query query = (Query) value;

                AttributesImpl attributes = new AttributesImpl();

                if ((query.getHandle() != null)
                        && !"".equals(query.getHandle())) {
                    attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                        attrs[0].getName(), null, "string", query.getHandle());
                }

                // TODO this is a QName I think ... check it out
                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                    attrs[1].getName(), null, "string", query.getTypeName());

                try {
                    if ((query.getVersion() != null)
                            && !"".equals(query.getVersion())) {
                        attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
                            attrs[2].getName(), null, "string",
                            query.getVersion());
                    }
                } catch (Throwable t) {
                    // ...
                }

                output.startElement(element.getNamespace(), element.getName(),
                    attributes);

                String[] propNames = query.getPropertyNames();

                if (Query.ALL_NAMES != propNames) {
                    if (propNames != null) {
                        for (int i = 0; i < propNames.length; i++)
                            elems[0].getType().encode(elems[0], propNames[i],
                                output, hints);
                    }
                }

                if (Filter.INCLUDE != query.getFilter()) {
                    if ((query.getFilter() != null)
                            && elems[1].getType().canEncode(elems[1],
                                query.getFilter(), hints)) {
                        elems[1].getType().encode(elems[1], query.getFilter(),
                            output, hints);
                    }
                }

                output.endElement(element.getNamespace(), element.getName());
            } else {
                throw new OperationNotSupportedException(
                    "not a valid value/element for a DescribeFeatureTypeType.");
            }
        }
    }

    static class FeatureCollectionType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new FeatureCollectionType();

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return new Attribute[] {
                new AttributeGT(null, "lockId", WFSSchema.NAMESPACE,
                    XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL,
                    null, null, false),
            };
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return ((ComplexType) getParent()).getChild();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getParent()
         */
        public Type getParent() {
            return GMLComplexTypes.AbstractFeatureCollectionType.getInstance();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /**
         * @throws SAXException
         * @throws OperationNotSupportedException
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws OperationNotSupportedException, SAXException {
            String lock = null;
            lock = attrs.getValue("", "lockID");

            if ((lock == null) || "".equals(lock)) {
                lock = attrs.getValue(WFSSchema.NAMESPACE.toString(), "lockID");
            }

            if ((hints != null) && (lock != null) && (!"".equals(lock))) {
                hints.put(LOCK_KEY, lock);
            }

            return getParent().getValue(element, value, attrs, hints);
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FeatureCollectionType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return getParent().getInstanceType();
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return getParent().canEncode(element, value, hints);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            // TODO add the lockId attribute
            getParent().encode(element, value, output, hints);
        }
    }
}
