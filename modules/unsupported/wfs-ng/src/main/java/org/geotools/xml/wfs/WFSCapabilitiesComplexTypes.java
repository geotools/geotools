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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.data.ows.FeatureSetDescription;
import org.geotools.data.ows.OperationType;
import org.geotools.data.ows.Service;
import org.geotools.data.ows.WFSCapabilities;
import org.geotools.filter.FilterCapabilities;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.filter.FilterSchema;
import org.geotools.xml.filter.FilterComplexTypes.Filter_CapabilitiesType;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.ChoiceGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.schema.impl.SimpleTypeGT;
import org.geotools.xml.wfs.WFSSchema.WFSAttribute;
import org.geotools.xml.wfs.WFSSchema.WFSComplexType;
import org.geotools.xml.wfs.WFSSchema.WFSElement;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

import com.vividsolutions.jts.geom.Envelope;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author Norman Barker www.comsine.com
 * @author dzwiers
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/main/java/org/geotools/xml/wfs/WFSCapabilitiesComplexTypes.java $
 */
public class WFSCapabilitiesComplexTypes {
    /**
     * <p>
     * This class represents an SchemaDescriptionLanguageType within the WFS
     * Schema.  This includes both the data and parsing functionality
     * associated with a SchemaDescriptionLanguageType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class SchemaDescriptionLanguageType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new SchemaDescriptionLanguageType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("XMLSCHEMA", EmptyType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements) {
                /**
                 * @see schema.Choice#getMaxOccurs()
                 */
                public int getMaxOccurs() {
                    return Integer.MAX_VALUE;
                }
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAnyAttributeNameSpace()
         */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            // 
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "SchemaDescriptionLanguageType";
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "SchemaDescriptionLanguage had an erro while parsing -- missing input");
            }

            if (value.length < 1) {
                throw new SAXException(
                    "too few child elements for SchemaDescriptionLanguage");
            }

            boolean found = false;
            List results = new LinkedList();

            for (int i = 0; i < value.length; i++) {
                if (value[i].getElement() != null) {
                    if ("XMLSCHEMA".equals(value[i].getElement().getName())) {
                        found = true;
                    }

                    if ((value[i].getElement().getName()) != null) {
                        results.add(value[i].getElement().getName());
                    }
                }
            }

            if (!found) {
                throw new SAXException(
                    "XMLSCHEMA is a required child element, which was not found");
            }

            return (results.size() == 0) ? null : results;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException(
                "Method not completed yet.");
        }
    }

    /**
     * <p>
     * This class represents an EmptyType within the WFS Schema.  This includes
     * both the data and parsing functionality associated with a EmptyType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class EmptyType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new EmptyType();

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints){
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException(
                "Method not completed yet.");
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
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
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "EmptyType";
        }
    }

    /**
     * <p>
     * This class represents an ResultFormatType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * ResultFormatType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class ResultFormatType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new ResultFormatType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("GML2", EmptyType.getInstance(), 1, 1, false,
                    null),
                new WFSElement("GML2-GZIP", EmptyType.getInstance(), 0, 1,
                    false, null),
                new WFSElement("GML2-ZIP", EmptyType.getInstance(), 0, 1,
                    false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements) {
                /**
                 * @see schema.Sequence#getMaxOccurs()
                 */
                public int getMaxOccurs() {
                    return Integer.MAX_VALUE;
                }
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Invalid inputs for parsing a GetCapabilitiesType");
            }

            boolean validation = true;
            if(hints != null && hints.containsKey(DocumentFactory.VALIDATION_HINT)){
                Boolean t = (Boolean)hints.get(DocumentFactory.VALIDATION_HINT);
                if(t!=null)
                    validation = t.booleanValue();
            }
            
            if (validation && value.length < 1) {
                throw new SAXException(
                    "Invalid number of inputs for parsing a GetCapabilitiesType");
            }

            List l = new LinkedList();

            for (int i = 0; i < value.length; i++) {
                if (value[i].getElement().getName() != null) {
                    l.add(value[i].getElement().getName());
                }
            }

            return l;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return List.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException(
                "Method not completed yet.");
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ResultFormatType";
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }
    }

    /**
     * <p>
     * This class represents an OperationsType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * OperationsType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class OperationsType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new OperationsType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("Insert", EmptyType.getInstance(), 1, 1, false,
                    null),
                new WFSElement("Update", EmptyType.getInstance(), 1, 1, false,
                    null),
                new WFSElement("Delete", EmptyType.getInstance(), 1, 1, false,
                    null),
                new WFSElement("Query", EmptyType.getInstance(), 1, 1, false,
                    null),
                new WFSElement("Lock", EmptyType.getInstance(), 1, 1, false,
                    null),
            };

        // static sequence
        private static final ChoiceGT seq = new ChoiceGT(elements) {
                /**
                 * @see schema.Choice#getMaxOccurs()
                 */
                public int getMaxOccurs() {
                    return Integer.MAX_VALUE;
                }
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException("missing inputs");
            }

            if (value.length < 1) {
                throw new SAXException("Too few children");
            }

            int t = 0;

            for (int i = 0; i < value.length; i++) {
                t = t
                    | FeatureSetDescription.findOperation(value[i].getElement()
                                                                  .getName());
            }

            return new Integer(t);
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException(
                "Method not completed yet.");
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "OperationsType";
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }
    }

    /**
     * <p>
     * This class represents an MetadataURLType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * MetadataURLType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class MetadataURLType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new MetadataURLType();
        private static Attribute[] attributes = {
                new WFSAttribute("type",
                    new SimpleTypeGT(null, null, WFSSchema.NAMESPACE,
                        SimpleType.RESTRICTION,
                        new SimpleType[] { XSISimpleTypes.NMTOKEN.getInstance() },
                        new Facet[] {
                            new FacetGT(Facet.ENUMERATION, "TC211"),
                            new FacetGT(Facet.ENUMERATION, "FGDC")
                        }, SimpleType.NONE), Attribute.REQUIRED),
                new WFSAttribute("format",
                    new SimpleTypeGT(null, null, WFSSchema.NAMESPACE,
                        SimpleType.RESTRICTION,
                        new SimpleType[] { XSISimpleTypes.NMTOKEN.getInstance() },
                        new Facet[] {
                            new FacetGT(Facet.ENUMERATION, "XML"),
                            new FacetGT(Facet.ENUMERATION, "SGML"),
                            new FacetGT(Facet.ENUMERATION, "TXT")
                        }, SimpleType.NONE), Attribute.REQUIRED)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            // SimpleContent
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
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Invalid parameters passed into MetadataURL");
            }

            if (value.length != 1) {
                throw new SAXException(
                    "Should only have one child value -- the String value of inline text");
            }

            //            MetadataURL mdurl = new MetadataURL();
            //            String t = "";
            //
            //            t = attrs.getValue("", "type");
            //
            //            if ((t == null) || "".equals(t)) {
            //                t = attrs.getValue(WFSSchema.NAMESPACE, "type");
            //            }
            //
            //            mdurl.setType(MetadataURL.parseType(t));
            //            t = attrs.getValue("", "format");
            //
            //            if ((t == null) || "".equals(t)) {
            //                t = attrs.getValue(WFSSchema.NAMESPACE, "format");
            //            }
            //
            //            mdurl.setFormat(t);
            //
            //            t = (String) value[0].getValue();
            //
            //            URL url = null;
            //
            //            try {
            //                url = new URL(t);
            //            } catch (MalformedURLException e) {
            //                throw new SAXException(e);
            //            }
            //
            //            mdurl.setOnlineResource(url);
            //            return mdurl;
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "MetadataURLType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            //            return MetadataURL.class;
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
        /**
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return true;
        }
    }

    /**
     * <p>
     * This class represents an LatLonBoundingBoxType within the WFS Schema.
     * This includes both the data and parsing functionality associated with a
     * LatLonBoundingBoxType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class LatLongBoundingBoxType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new LatLongBoundingBoxType();

        // static element list
        private static Attribute[] attributes = {
                new WFSAttribute("minx", XSISimpleTypes.String.getInstance(),
                        Attribute.REQUIRED),
                new WFSAttribute("miny", XSISimpleTypes.String.getInstance(),
                        Attribute.REQUIRED),
                new WFSAttribute("maxx", XSISimpleTypes.String.getInstance(),
                        Attribute.REQUIRED),
                new WFSAttribute("maxy", XSISimpleTypes.String.getInstance(),
                        Attribute.REQUIRED)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
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
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            double minx;
            double miny;
            double maxx;
            double maxy;
            minx = miny = maxx = maxy = 0;

            if ((element == null) || (attrs == null)) {
                throw new SAXException(
                    "Invalid parameters for LatLongBoundingBoxType");
            }

            String t = "";

            t = attrs.getValue("", "minx");

            if ((t == null) || "".equals(t)) {
                t = attrs.getValue(WFSSchema.NAMESPACE.toString(), "minx");
            }

            minx = Double.parseDouble(t);

            t = attrs.getValue("", "maxx");

            if ((t == null) || "".equals(t)) {
                t = attrs.getValue(WFSSchema.NAMESPACE.toString(), "maxx");
            }

            maxx = Double.parseDouble(t);

            t = attrs.getValue("", "miny");

            if ((t == null) || "".equals(t)) {
                t = attrs.getValue(WFSSchema.NAMESPACE.toString(), "miny");
            }

            miny = Double.parseDouble(t);

            t = attrs.getValue("", "maxy");

            if ((t == null) || "".equals(t)) {
                t = attrs.getValue(WFSSchema.NAMESPACE.toString(), "maxy");
            }

            maxy = Double.parseDouble(t);

            return new Envelope(minx, maxx, miny, maxy);
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LatLonBoundingBoxType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Envelope.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an PostType within the WFS Schema.  This includes
     * both the data and parsing functionality associated with a PostType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class PostType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new PostType();

        /**
         * @see org.geotools.xml.schema.ComplexType#findChildElement(java.lang.String)
         */

        // static list of attributes
        private static Attribute[] attributes = {
                new WFSAttribute("onlineResource",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
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
            Attributes attrs, Map hints){
            String s = attrs.getValue("", "onlineResource");

            if ((s == null) || "".equals(s)) {
                s = attrs.getValue(WFSSchema.NAMESPACE.toString(),
                        "onlineResource");
            }

            try {
                return new URL(s);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "PostType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return URL.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an HTTPType within the WFS Schema.  This includes
     * both the data and parsing functionality associated with a HTTPType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class HTTPType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new HTTPType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("Get", GetType.getInstance(), 0,
                    Integer.MAX_VALUE, false, null),
                new WFSElement("Post", PostType.getInstance(), 0,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final ChoiceGT seq = new ChoiceGT(elements) {
                /**
                 * @see schema.Choice#getMaxOccurs()
                 */
                public int getMaxOccurs() {
                    return Integer.MAX_VALUE;
                }
            };

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "HTTPType";
        }

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
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
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Error occured in HTTPType: both an element and value param is required");
            }

            if (value.length < 1) {
                throw new SAXException(
                    "Must have atleast one http type defined.");
            }

            OperationType c = new OperationType();

            for (int i = 0; i < value.length; i++) {
                if ((value[i].getElement() != null)
                        && value[i].getElement().getName().equals(elements[0]
                            .getName())) {
                    // get
                    c.setGet((URL) value[i].getValue());
                }

                if ((value[i].getElement() != null)
                        && value[i].getElement().getName().equals(elements[1]
                            .getName())) {
                    // post
                    c.setPost((URL) value[i].getValue());
                }
            }

            return c;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return OperationType.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an GetType within the WFS Schema.  This includes
     * both the data and parsing functionality associated with a GetType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class GetType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new GetType();

        // static list of attributes
        private static Attribute[] attributes = {
                new WFSAttribute("onlineResource",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED)
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
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
            Attributes attrs, Map hints){
            String s = attrs.getValue("", "onlineResource");

            if ((s == null) || "".equals(s)) {
                s = attrs.getValue(WFSSchema.NAMESPACE.toString(),
                        "onlineResource");
            }

            try {
                return new URL(s);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GetType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return URL.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an FeatureTypeType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * FeatureTypeType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class FeatureTypeType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new FeatureTypeType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("Name", XSISimpleTypes.QName.getInstance(), 1,
                    1, false, null),
                new WFSElement("Title", XSISimpleTypes.String.getInstance(), 0,
                    1, false, null),
                new WFSElement("Abstract", XSISimpleTypes.String.getInstance(),
                    0, 1, false, null),
                new WFSElement("Keywords", XSISimpleTypes.String.getInstance(),
                    0, 1, false, null),
                new WFSElement("SRS", XSISimpleTypes.String.getInstance(), 1,
                    1, false, null),
                new WFSElement("Operations", OperationsType.getInstance(), 0,
                    1, false, null),
                new WFSElement("LatLongBoundingBox",
                    LatLongBoundingBoxType.getInstance(), 0, Integer.MAX_VALUE,
                    false, null),
                new WFSElement("MetadataURL", MetadataURLType.getInstance(), 0,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FeatureTypeType";
        }

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
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
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException("Missing params for FeatureTypeType");
            }

            boolean validation = true;
            if(hints != null && hints.containsKey(DocumentFactory.VALIDATION_HINT)){
                Boolean t = (Boolean)hints.get(DocumentFactory.VALIDATION_HINT);
                if(t!=null)
                    validation = t.booleanValue();
            }
            
            if (validation && value.length < 2) {
                throw new SAXException(
                    "Missing child element for FeatureTypeType");
            }

            FeatureSetDescription fsd = new FeatureSetDescription();
            List llbb = new LinkedList();

            for (int i = 0; i < value.length; i++) {
                if (value[i].getElement() == null) {
                    throw new SAXException(
                        "Internal error -- a value object representing a child element was missing an element declaration");
                }

                if (elements[0].getName().equals(value[i].getElement().getName())) {
                    // Name
                    fsd.setName((String) value[i].getValue());
                    if(fsd.getName()!=null){
                        int j = fsd.getName().indexOf(":");
                        if(j>0){
                            // we have a ns prefix
                            String prefix = fsd.getName().substring(0,j);
                        }
                    }
                } else {
                    if (elements[1].getName().equals(value[i].getElement()
                                                                 .getName())) {
                        // Title
                        fsd.setTitle((String) value[i].getValue());
                    } else {
                        if (elements[2].getName().equals(value[i].getElement()
                                                                     .getName())) {
                            // Abstract
                            String t = (String) value[i].getValue();
                            t = "NONE".equals(t) ? "" : t;
                            fsd.setAbstract(t);
                        } else {
                            if (elements[3].getName().equals(value[i].getElement()
                                                                         .getName())) {
                                // Keywords
                                String t = (String) value[i].getValue();
                                t = (t == null) ? "" : t;
                                fsd.setKeywords(Arrays.asList(t.split(" ")));
                            } else {
                                if (elements[4].getName().equals(value[i].getElement()
                                                                             .getName())) {
                                    // SRS
                                    fsd.setSRS((String) value[i].getValue());
                                } else {
                                    if (elements[5].getName().equals(value[i].getElement()
                                                                                 .getName())) {
                                        // Operations
                                        fsd.setOperations(((Integer) value[i]
                                            .getValue()).intValue());
                                    } else {
                                        if (elements[6].getName().equals(value[i].getElement()
                                                                                     .getName())) {
                                            // LatLongBoundingBox
                                            llbb.add(value[i].getValue());
                                        } else {
                                            if (elements[7].getName().equals(value[i].getElement()
                                                                                         .getName())) {
                                                // MetadataURL
                                                //                                                mdurl.add((MetadataURL) value[i]
                                                //                                                    .getValue());
                                            } else {
                                                // error
                                                throw new SAXException(
                                                    "Unknown child element within a FeatureTypeType: "
                                                    + ((value[i].getElement()
                                                                .getName() == null)
                                                    ? "null"
                                                    : value[i].getElement()
                                                              .getName()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (llbb.size() > 0) {
                Envelope e = (Envelope) llbb.get(0);

                for (int i = 1; i < llbb.size(); i++)
                    e.expandToInclude((Envelope) llbb.get(i));

                fsd.setLatLongBoundingBox(e);
            }
            if ((fsd.getName() == null) || (fsd.getSRS() == null)) {
                throw new SAXException(
                    "Missing child element for FeatureTypeType");
            }

            return fsd;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return FeatureSetDescription.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an DCPTypeType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * DCPTypeType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class DCPTypeType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new DCPTypeType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("HTTP", HTTPType.getInstance(), 1, 1, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "DCPTypeType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            Element e = value[0].getElement();

            if ((e == null) || (value == null)) {
                throw new SAXException(
                    "Internal error, ElementValues require an associated Element.");
            }

            if (value.length != 1) {
                throw new SAXException("Wrong number of elements for DCPType");
            }

            return (OperationType) value[0].getValue();
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
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
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return OperationType.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an LockFeatureTypeType within the WFS Schema. This
     * includes both the data and parsing functionality associated with a
     * LockFeatureTypeType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class LockFeatureTypeType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new LockFeatureTypeType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("DCPType", DCPTypeType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Invalid inputs for parsing a GetCapabilitiesType");
            }

            if (value.length < 1) {
                throw new SAXException(
                    "Invalid number of inputs for parsing a GetCapabilitiesType");
            }

            OperationType[] c = new OperationType[value.length];

            for (int i = 0; i < value.length; i++) {
                c[i] = (OperationType) value[i].getValue();

                //                c[i].setType(OperationType.LOCK_FEATURE);
            }

            return c;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LockFeatureTypeType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return OperationType[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an RequestType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * RequestType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class RequestType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new RequestType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("GetCapabilities",
                    GetCapabilitiesType.getInstance(), 1, 1, false, null),
                new WFSElement("DescribeFeatureType",
                    DescribeFeatureTypeType.getInstance(), 1, 1, false, null),
                new WFSElement("Transaction", TransactionType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null),
                new WFSElement("GetFeature", GetFeatureTypeType.getInstance(),
                    1, 1, false, null),
                new WFSElement("GetFeatureWithLock",
                    GetFeatureTypeType.getInstance(), 1, 1, false, null),
                new WFSElement("LockFeature",
                    LockFeatureTypeType.getInstance(), 1, 1, false, null)
            };

        // static sequence
        private static final ChoiceGT seq = new ChoiceGT(elements) {
                /**
                 * @see schema.Choice#getMaxOccurs()
                 */
                public int getMaxOccurs() {
                    return Integer.MAX_VALUE;
                }
            };

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if (element == null) {
                throw new SAXException(
                    "Element cannot be null -- we should know what we are parsing");
            }

            if ((value == null) || (value.length == 0)) {
                throw new SAXException(
                    "We need atleast one value to parse a wfs:RequestType");
            }

            OperationType[] result = new OperationType[6];

            for (int i = 0; i < value.length; i++) {
                OperationType[] t = (OperationType[]) value[i].getValue();

                if (t != null) {
                    if (elements[0].getName().equals(value[i].getElement()
                                                                 .getName())) {
                        if (result[0] == null) {
                            result[0] = new OperationType();
                        }

                        // merge them
                        for (int j = 0; j < t.length; j++) {
                            if ((result[0].getGet() == null)
                                    && (t[j].getGet() != null)) {
                                result[0].setGet(t[j].getGet());
                            }

                            if ((result[0].getPost() == null)
                                    && (t[j].getPost() != null)) {
                                result[0].setPost(t[j].getPost());
                            }
                        }
                    } else {
                        if (elements[1].getName().equals(value[i].getElement()
                                                                     .getName())) {
                            if (result[1] == null) {
                                result[1] = new OperationType();
                            }

                            // merge them
                            for (int j = 0; j < t.length; j++) {
                                if ((result[1].getGet() == null)
                                        && (t[j].getGet() != null)) {
                                    result[1].setGet(t[j].getGet());
                                }

                                if ((result[1].getPost() == null)
                                        && (t[j].getPost() != null)) {
                                    result[1].setPost(t[j].getPost());
                                }

                                if (t[j].getFormats() != null) {
                                    if (result[1].getFormats() == null) {
                                        result[1].setFormats(t[j].getFormats());
                                    } else {
                                        List st = result[0].getFormats();

                                        if (t[j].getFormats() == null) {
                                            t[j].setFormats(st);
                                        } else if (st != null) {
                                            t[j].getFormats().addAll(st);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (elements[2].getName().equals(value[i].getElement()
                                                                         .getName())) {
                                if (result[2] == null) {
                                    result[2] = new OperationType();
                                }

                                // merge them
                                for (int j = 0; j < t.length; j++) {
                                    if ((result[2].getGet() == null)
                                            && (t[j].getGet() != null)) {
                                        result[2].setGet(t[j].getGet());
                                    }

                                    if ((result[2].getPost() == null)
                                            && (t[j].getPost() != null)) {
                                        result[2].setPost(t[j].getPost());
                                    }
                                }
                            } else {
                                if (elements[3].getName().equals(value[i].getElement()
                                                                             .getName())) {
                                    if (result[3] == null) {
                                        result[3] = new OperationType();
                                    }

                                    // merge them
                                    for (int j = 0; j < t.length; j++) {
                                        if ((result[3].getGet() == null)
                                                && (t[j].getGet() != null)) {
                                            result[3].setGet(t[j].getGet());
                                        }

                                        if ((result[3].getPost() == null)
                                                && (t[j].getPost() != null)) {
                                            result[3].setPost(t[j].getPost());
                                        }

                                        if (t[j].getFormats() != null) {
                                            if (result[3].getFormats() == null) {
                                                result[3].setFormats(t[j]
                                                    .getFormats());
                                            } else {
                                                List st = (List) result[0]
                                                    .getFormats();

                                                if (t[j].getFormats() == null) {
                                                    t[j].setFormats(st);
                                                } else if (st != null) {
                                                    ((List) t[j].getFormats())
                                                    .addAll(st);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (elements[4].getName().equals(value[i].getElement()
                                                                                 .getName())) {
                                        if (result[4] == null) {
                                            result[4] = new OperationType();
                                        }

                                        // merge them
                                        for (int j = 0; j < t.length; j++) {
                                            if ((result[4].getGet() == null)
                                                    && (t[j].getGet() != null)) {
                                                result[4].setGet(t[j].getGet());
                                            }

                                            if ((result[4].getPost() == null)
                                                    && (t[j].getPost() != null)) {
                                                result[4].setPost(t[j].getPost());
                                            }

                                            if (t[j].getFormats() != null) {
                                                if (result[4].getFormats() == null) {
                                                    result[4].setFormats(t[j]
                                                        .getFormats());
                                                } else {
                                                    List st = (List) result[0]
                                                        .getFormats();

                                                    if (t[j].getFormats() == null) {
                                                        t[j].setFormats(st);
                                                    } else if (st != null) {
                                                        ((List) t[j].getFormats())
                                                        .addAll(st);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (elements[5].getName().equals(value[i].getElement()
                                                                                     .getName())) {
                                            if (result[5] == null) {
                                                result[5] = new OperationType();
                                            }

                                            // merge them
                                            for (int j = 0; j < t.length;
                                                    j++) {
                                                if ((result[5].getGet() == null)
                                                        && (t[j].getGet() != null)) {
                                                    result[5].setGet(t[j]
                                                        .getGet());
                                                }

                                                if ((result[5].getPost() == null)
                                                        && (t[j].getPost() != null)) {
                                                    result[5].setPost(t[j]
                                                        .getPost());
                                                }
                                            }
                                        } else {
                                            // error
                                            throw new SAXException(
                                                "Unknown child element "
                                                + value[i].getElement().getName()
                                                + " found in RequestType");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return result;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "RequestType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return OperationType[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an ServiceType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * ServiceType .
     * </p>
     *
     * @see WFSComplexType
     */
    static class ServiceType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new ServiceType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("Name", XSISimpleTypes.String.getInstance(), 1,
                    1, false, null),
                new WFSElement("Title", XSISimpleTypes.String.getInstance(), 1,
                    1, false, null),
                new WFSElement("Abstract", XSISimpleTypes.String.getInstance(),
                    0, 1, false, null),
                new WFSElement("Keywords", XSISimpleTypes.String.getInstance(),
                    0, 1, false, null),
                new WFSElement("OnlineResource",
                    XSISimpleTypes.AnyURI.getInstance(), 1, 1, false, null),
                new WFSElement("Fees", XSISimpleTypes.String.getInstance(), 0,
                    1, false, null),
                new WFSElement("AccessConstraints",
                    XSISimpleTypes.String.getInstance(), 0, 1, false, null)
            };

        // static choice
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Service.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ServiceType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            Element e = value[0].getElement();

            if (e == null) {
                throw new SAXException(
                    "Internal error, ElementValues require an associated Element.");
            }

            Service service = new Service();

            for (int i = 0; i < value.length; i++) {
                if (elements[0].getName().equals(value[i].getElement().getName())) {
                    service.setName((String) value[i].getValue());
                }

                if (elements[1].getName().equals(value[i].getElement().getName())) {
                    service.setTitle((String) value[i].getValue());
                }

                if (elements[2].getName().equals(value[i].getElement().getName())) {
                    service.set_abstract((String) value[i].getValue());
                }

                if (elements[3].getName().equals(value[i].getElement().getName())) {
                    service.setKeywordList(((String) value[i].getValue()).split(
                            " "));
                }

                if (elements[4].getName().equals(value[i].getElement().getName())) {
                    try {
                        service.setOnlineResource(((URI) value[i].getValue())
                            .toURL());
                    } catch (MalformedURLException e1) {
                        throw new SAXException(e1);
                    }
                }

                //                if (elements[5].getName().equals(value[i].getElement().getName())) {
                //                    service.setFees((String) value[i].getValue());
                //                }
                //                if (elements[6].getName().equals(value[i].getElement().getName())) {
                //                    service.setAccessConstraints((String) value[i].getValue());
                //                }
            }

            // check the required elements 
            if ((service.getName() == null) || (service.getTitle() == null)
                    || ((service.getOnlineResource()) == null)) {
                throw new SAXException(
                    "Required Service Elements are missing, check"
                    + " for the existence of Name, Title , or OnlineResource elements.");
            }

            return service;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
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
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an FeatureTypeListType within the WFS Schema. This
     * includes both the data and parsing functionality associated with a
     * FeatureTypeListType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class FeatureTypeListType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new FeatureTypeListType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("Operations", OperationsType.getInstance(), 0,
                    1, false, null),
                new WFSElement("FeatureType", FeatureTypeType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
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
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "A parameter for FeatureTypeList is missing");
            }

            if (value.length < 1) {
                throw new SAXException("Missing a child element");
            }

            int operations = FeatureSetDescription.NO_OPERATION;
            List fts = new LinkedList();

            for (int i = 0; i < value.length; i++) {
                if (elements[0].getName().equals(value[i].getElement().getName())) {
                    // operation
                    operations = operations
                        | ((Integer) value[i].getValue()).intValue();
                } else {
                    if (elements[1].getName().equals(value[i].getElement()
                                                                 .getName())) {
                        // featureType
                        fts.add(value[i].getValue());
                    } else {
                        // error
                        throw new SAXException("An error occured here");
                    }
                }
            }

            //            FeatureSetDescription[] fsd = new FeatureSetDescription[fts.size()];
            //
            //            for (int i = 0; i < fsd.length; i++) {
            //                fsd[i] = (FeatureSetDescription) fts.get(i);
            //
            //                if (fsd[i].getOperations() == FeatureSetDescription.NO_OPERATION) {
            //                    fsd[i].setOperations(operations);
            //                }
            //            }
            return fts;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FeatureTypeListType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return FeatureSetDescription[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an CapabilityType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * CapabilityType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class CapabilityType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new CapabilityType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("Request", RequestType.getInstance(), 1, 1,
                    false, null),
                new WFSElement("VendorSpecificCapabilities",
                    XSISimpleTypes.String.getInstance(), 0, 1, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }
            return seq.findChildElement(name);
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Object[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "CapabilityType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            Element e = value[0].getElement();

            if (e == null) {
                throw new SAXException(
                    "Internal error, ElementValues require an associated Element.");
            }

            Object[] capab = new Object[2];

            for (int i = 0; i < value.length; i++) {
                if (elements[0].getName().equals(value[i].getElement().getName())) {
                    capab[1] = (OperationType[]) value[i].getValue();
                }

                if (elements[1].getName().equals(value[i].getElement().getName())) {
                    capab[0] = (String) value[i].getValue();
                }
            }

            // check the required elements 
            if (capab[1] == null) {
                throw new SAXException(
                    "Required Capability Element is missing, check"
                    + " for the existence of the Request Element.");
            }

            return capab;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an WFS_CapabilitiesType within the WFS Schema.
     * This includes both the data and parsing functionality associated  with
     * an WFS_CapabilitiesType .
     * </p>
     *
     * @see WFSComplexType
     */
    static class WFS_CapabilitiesType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new WFS_CapabilitiesType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("Service", ServiceType.getInstance(), 1, 1,
                    false, null),
                new WFSElement("Capability", CapabilityType.getInstance(), 1,
                    1, false, null),
                new WFSElement("FeatureTypeList",
                    FeatureTypeListType.getInstance(), 1, 1, false, null),

                new WFSElement("Filter_Capabilities",
                    Filter_CapabilitiesType.getInstance(), 1, 1, false, null) {
                        /**
                         * @see schema.Element#getNamespace()
                         */
                        public URI getNamespace() {
                            return FilterSchema.NAMESPACE;
                        }
                    }
            };
        private static final Attribute[] attributes = {
                new WFSAttribute("version",
                    XSISimpleTypes.String.getInstance(), Attribute.REQUIRED,
                    "1.0.0"),
                new WFSAttribute("updateSequence",
                    XSISimpleTypes.NonNegativeInteger.getInstance(),
                    Attribute.REQUIRED, "0")
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
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
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WFS_CapabilitiesType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return WFSCapabilities.class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if (element != null) {
                if (element.getType() != this) {
                    if (!(element.getType() instanceof ComplexType)) {
                        ComplexType t = (ComplexType) element.getType();

                        while ((t != null) && (t != this))
                            t = (t.getParent() instanceof ComplexType)
                                ? (ComplexType) t.getParent() : null;

                        if (t == null) {
                            throw new SAXNotSupportedException(
                                "The specified element was not a declared as a WFS_Capabilities element, or derived element");
                        }
                    } else {
                        // error -- cannot encode
                        throw new SAXNotSupportedException(
                            "The specified element was not a declared as a WFS_Capabilities element, or derived element");
                    }
                }
            }

            boolean validation = true;
            if(hints != null && hints.containsKey(DocumentFactory.VALIDATION_HINT)){
                Boolean t = (Boolean)hints.get(DocumentFactory.VALIDATION_HINT);
                if(t!=null)
                    validation = t.booleanValue();
            }
            
            if (validation && ((value == null) || (value.length != 4))) {
                throw new SAXException(
                    "The WFS Capabilites document has the wrong number of children");
            }

            WFSCapabilities result = new WFSCapabilities();

            for (int i = 0; i < 4 && i < value.length; i++) {
                if (elements[0].getName().equals(value[i].getElement().getName())) {
                    // service
                    result.setService((Service) value[i].getValue());
                } else {
                    if (elements[1].getName().equals(value[i].getElement()
                                                                 .getName())) {
                        // capability
                        Object[] temp = (Object[]) value[i].getValue();

                        if (temp.length != 2) {
                            throw new SAXException(
                                "The WFS Capabilites document has an invalid capability child");
                        }

                        result.setVendorSpecificCapabilities((String) temp[0]);

                        OperationType[] tmp = (OperationType[]) temp[1];

                        if (tmp != null) {
                            result.setGetCapabilities(tmp[0]);
                            result.setDescribeFeatureType(tmp[1]);
                            result.setTransaction(tmp[2]);
                            result.setGetFeature(tmp[3]);
                            result.setGetFeatureWithLock(tmp[4]);
                            result.setLockFeature(tmp[5]);
                        }
                    } else {
                        if (elements[2].getName().equals(value[i].getElement()
                                                                     .getName())) {
                            // FeatureTypeList
                            result.setFeatureTypes((List) value[i].getValue());
                        } else {
                            if (elements[3].getName().equals(value[i].getElement()
                                                                         .getName())) {
                                // Filter_Capabilities
                                result.setFilterCapabilities((FilterCapabilities) value[i]
                                    .getValue());
                            } else {
                                if(validation){
                                    // error
                                    throw new SAXException("The element "
                                    + ((value[i].getElement() == null) ? "null"
                                    : value[i].getElement().getName())
                                    + " was not found as a valid element ...");
                                }
                            }
                        }
                    }
                }
            }

            return result;
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

        // static element list
        private static final Element[] elements = {
                new WFSElement("DCPType", DCPTypeType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        /*
         * part of the singleton pattern
         *
         * @see WFSComplexType#getInstance()
         */
        static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Invalid inputs for parsing a GetCapabilitiesType");
            }

            if (value.length < 1) {
                throw new SAXException(
                    "Invalid number of inputs for parsing a GetCapabilitiesType");
            }

            List l = new LinkedList();

            for (int i = 0; i < value.length; i++) {
                OperationType t = (OperationType) value[i].getValue();
                l.add(t);
            }

            return l.toArray(new OperationType[(l.size())]);
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
            return OperationType[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
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

        // static element list
        private static final Element[] elements = {
                new WFSElement("SchemaDescriptionLanguage",
                    SchemaDescriptionLanguageType.getInstance(), 1, 1, false,
                    null),
                new WFSElement("DCPType", DCPTypeType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
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
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Invalid inputs for parsing a GetCapabilitiesType");
            }

            if (value.length < 2) {
                throw new SAXException(
                    "Invalid number of inputs for parsing a GetCapabilitiesType");
            }

            List l = new LinkedList();
            List sdl = null;

            for (int i = 0; i < value.length; i++) {
                if ((sdl == null) && (value[i].getElement() != null)
                        && "SchemaDescriptionLanguage".equals(
                            value[i].getElement().getName())) {
                    sdl = (List) value[i].getValue();
                } else {
                    OperationType t = (OperationType) value[i].getValue();
                    l.add(t);
                }
            }

            OperationType[] ot = new OperationType[l.size()];

            for (int i = 0; i < ot.length; i++) {
                ot[i] = (OperationType) l.get(i);
                ot[i].setFormats(sdl);
            }

            return ot;
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
            return OperationType[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an TransactionType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * TransactionType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class TransactionType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new TransactionType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("DCPType", DCPTypeType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Invalid inputs for parsing a GetCapabilitiesType");
            }

            if (value.length < 1) {
                throw new SAXException(
                    "Invalid number of inputs for parsing a GetCapabilitiesType");
            }

            List l = new LinkedList();

            for (int i = 0; i < value.length; i++) {
                OperationType t = (OperationType) value[i].getValue();
                l.add(t);
            }

            return l.toArray(new OperationType[l.size()]);
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "TransactionType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return OperationType[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }

    /**
     * <p>
     * This class represents an GetFeatureTypeType within the WFS Schema.  This
     * includes both the data and parsing functionality associated with a
     * GetFeatureTypeType.
     * </p>
     *
     * @see WFSComplexType
     */
    static class GetFeatureTypeType extends WFSComplexType {
        // singleton instance
        private static final WFSComplexType instance = new GetFeatureTypeType();

        // static element list
        private static final Element[] elements = {
                new WFSElement("ResultFormat", ResultFormatType.getInstance(),
                    1, 1, false, null),
                new WFSElement("DCPType", DCPTypeType.getInstance(), 1,
                    Integer.MAX_VALUE, false, null)
            };

        // static sequence
        private static final SequenceGT seq = new SequenceGT(elements);

        public static WFSComplexType getInstance() {
            return instance;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[],
         *      org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value,
            Attributes attrs, Map hints)
            throws SAXException, SAXNotSupportedException {
            if ((element == null) || (value == null)) {
                throw new SAXException(
                    "Invalid inputs for parsing a GetCapabilitiesType");
            }

            if (value.length < 1) {
                throw new SAXException(
                    "Invalid number of inputs for parsing a GetCapabilitiesType");
            }

            List l = new LinkedList();
            List sdl = null;

            for (int i = 0; i < value.length; i++) {
                if ((sdl == null) && (value[i].getElement() != null)
                        && elements[0].getName().equals(value[i].getElement()
                                                                    .getName())) {
                    sdl = (List) value[i].getValue();
                } else {
                    OperationType t = (OperationType) value[i].getValue();
                    l.add(t);
                }
            }

            OperationType[] ot = new OperationType[l.size()];

            for (int i = 0; i < ot.length; i++) {
                ot[i] = (OperationType) l.get(i);
                ot[i].setFormats(sdl);
            }

            return ot;
        }

        /**
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GetFeatureTypeType";
        }

        /**
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return OperationType[].class;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // 
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws OperationNotSupportedException {
            // 
            throw new OperationNotSupportedException();
        }
    }
}
