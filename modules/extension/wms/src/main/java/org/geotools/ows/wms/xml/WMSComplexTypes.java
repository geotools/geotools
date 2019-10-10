/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.naming.OperationNotSupportedException;
import org.geotools.data.ows.Service;
import org.geotools.metadata.iso.citation.AddressImpl;
import org.geotools.metadata.iso.citation.ContactImpl;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wms.WMS1_0_0;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WMSRequest;
import org.geotools.util.SimpleInternationalString;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.ChoiceGT;
import org.geotools.xml.schema.impl.FacetGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.xLink.XLinkSchema;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class WMSComplexTypes {
    static class OperationType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new OperationType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "Format", _FormatType.getInstance(), 1, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "DCPType", _DCPTypeType.getInstance(), 1, Integer.MAX_VALUE)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            org.geotools.data.ows.OperationType operationType = null;

            List formatStrings = new ArrayList();

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    Object[] stringValues = (Object[]) value[i].getValue();
                    for (int ii = 0; ii < stringValues.length; ii++) {
                        formatStrings.add((String) stringValues[ii]);
                    }
                }

                if (sameName(elems[1], value[i])) {
                    operationType = (org.geotools.data.ows.OperationType) value[i].getValue();
                }
            }
            if (operationType != null) {
                operationType.setFormats(new ArrayList(formatStrings));
            }

            return operationType;
        }

        public String getName() {
            return "OperationType";
        }

        public Class getInstanceType() {
            return org.geotools.data.ows.OperationType.class;
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _WMT_MS_CapabilitiesType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _WMT_MS_CapabilitiesType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Service", _ServiceType.getInstance()),
                    new WMSSchema.WMSElement("Capability", _CapabilityType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "version",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            "updateSequence", XSISimpleTypes.String.getInstance())
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            WMSCapabilities capabilities = null;
            Service service = null;

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    service = ((Service) value[i].getValue());
                }

                if (sameName(elems[1], value[i])) {
                    capabilities = (WMSCapabilities) value[i].getValue();
                }
            }

            capabilities.setVersion(attrs.getValue("", "version")); // $NON-NLS-1$//$NON-NLS-2$
            capabilities.setUpdateSequence(
                    attrs.getValue("", "updateSequence")); // $NON-NLS-1$//$NON-NLS-2$

            capabilities.setService(service);

            return capabilities;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WMT_MS_Capabilities";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return WMSCapabilities.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _WMS_CapabilitiesType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _WMS_CapabilitiesType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Service", _ServiceType.getInstance()),
                    new WMSSchema.WMSElement("Capability", _CapabilityType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "version",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            "updateSequence", XSISimpleTypes.String.getInstance())
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            WMSCapabilities capabilities = null;
            Service service = null;

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    service = ((Service) value[i].getValue());
                }

                if (sameName(elems[1], value[i])) {
                    capabilities = (WMSCapabilities) value[i].getValue();
                }
            }

            capabilities.setVersion(attrs.getValue("", "version")); // $NON-NLS-1$//$NON-NLS-2$
            capabilities.setUpdateSequence(
                    attrs.getValue("", "updateSequence")); // $NON-NLS-1$//$NON-NLS-2$

            capabilities.setService(service);

            return capabilities;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WMS_Capabilities";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return WMSCapabilities.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _FormatType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _FormatType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("GIF", _GIFType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("JPEG", _JPEGType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("PNG", _PNGType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("PPM", _PPMType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("TIFF", _TIFFType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GeoTIFF", _GeoTIFFType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("WebCGM", _WebCGMType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("SVG", _SVGType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("WMS_XML", _WMS_XMLType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GML.1", _GML_1Type.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GML.2", _GML_2Type.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GML.3", _GML_3Type.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("BMP", _BMPType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("WBMP", _WBMPType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("MIME", _MIMEType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("INIMAGE", _INIMAGEType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("BLANK", _BLANKType.getInstance(), 0, 1),
                };

        // private static Sequence seq = new SequenceGT(elems);
        private static Sequence seq =
                new SequenceGT(
                        new ElementGrouping[] {new ChoiceGT(null, 0, Integer.MAX_VALUE, elems)});

        // private static All seq = new AllGT(elems);

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            ArrayList strings = new ArrayList();

            for (int i = 0; i < value.length; i++) {
                // System.out.println("Strings adding: "+value[i].getValue());
                if (value[i].getValue() != null && ((String) value[i].getValue()).length() != 0) {
                    strings.add(value[i].getValue());
                }
            }

            return strings.toArray();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Format";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String[].class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        public boolean isMixed() {
            return true;
        }
    }

    protected static class _ServiceType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ServiceType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Name", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("Title", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("Abstract", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("KeywordList", _KeywordListType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance()),
                    new WMSSchema.WMSElement(
                            "ContactInformation", _ContactInformationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("Fees", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "AccessConstraints", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "LayerLimit", XSISimpleTypes.PositiveInteger.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "MaxWidth", XSISimpleTypes.PositiveInteger.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "MaxHeight", XSISimpleTypes.PositiveInteger.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("Keywords", _KeywordsType.getInstance(), 0, 1)
                };

        private static Sequence seq =
                new SequenceGT(
                        new ElementGrouping[] {
                            elems[0],
                            elems[1],
                            elems[2],
                            new ChoiceGT(null, 0, 1, new Element[] {elems[3], elems[11]}),
                            elems[4],
                            elems[5],
                            elems[6],
                            elems[7],
                            elems[8],
                            elems[9],
                            elems[10]
                        });

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            Service service = new Service();

            for (int i = 0; i < value.length; i++) {

                if (sameName(elems[0], value[i])) {
                    service.setName((String) value[i].getValue());
                }

                if (sameName(elems[1], value[i])) {
                    service.setTitle((String) value[i].getValue());
                }

                if (sameName(elems[2], value[i])) {
                    service.set_abstract((String) value[i].getValue());
                }

                if (sameName(elems[3], value[i]) || sameName(elems[11], value[i])) {
                    service.setKeywordList((String[]) value[i].getValue());
                }

                if (sameName(elems[4], value[i])) {
                    service.setOnlineResource((URL) value[i].getValue());
                }

                if (sameName(elems[5], value[i])) {
                    ResponsiblePartyImpl contactInfo = (ResponsiblePartyImpl) value[i].getValue();
                    service.setContactInformation(contactInfo);
                }

                // if (sameName(elems[6], value[i])) {
                // //TODO fees not implemented, ignoring
                // }

                // if (sameName(elems[7], value[i])) {
                // //TODO access constraints not implemented, ignoring
                // }

                if (sameName(elems[8], value[i])) {
                    service.setLayerLimit(((Integer) value[i].getValue()).intValue());
                }

                if (sameName(elems[9], value[i])) {
                    service.setMaxWidth(((Integer) value[i].getValue()).intValue());
                }

                if (sameName(elems[10], value[i])) {
                    service.setMaxHeight(((Integer) value[i].getValue()).intValue());
                }
            }

            return service;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Service";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Service.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _KeywordListType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _KeywordListType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "Keyword", _KeywordType.getInstance(), 0, Integer.MAX_VALUE)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            String[] keywords = new String[value.length];

            for (int i = 0; i < value.length; i++) {
                keywords[i] = (String) value[i].getValue();
            }

            return keywords;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "KeywordList";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String[].class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _KeywordType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _KeywordType();

        private static Attribute[] attributes = {
            new WMSSchema.WMSAttribute("vocabulary", XSISimpleTypes.String.getInstance())
        };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return value[value.length - 1].getValue();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Keyword";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        public boolean isMixed() {
            return true;
        }
    }

    protected static class _KeywordsType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _KeywordsType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            Object keywords = value[value.length - 1].getValue();
            if (keywords == null) {
                return null;
            }
            return ((String) keywords).split(" ");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Keywords";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String[].class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        public boolean isMixed() {
            return true;
        }
    }

    protected static class _ContactInformationType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ContactInformationType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "ContactPersonPrimary", _ContactPersonPrimaryType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "ContactPosition", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "ContactAddress", _ContactAddressType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "ContactVoiceTelephone", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "ContactFacsimileTelephone", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "ContactElectronicMailAddress",
                            XSISimpleTypes.String.getInstance(),
                            0,
                            1)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            ResponsiblePartyImpl contactPerson = null;
            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    contactPerson = (ResponsiblePartyImpl) value[i].getValue();
                }
            }
            if (contactPerson == null) {
                contactPerson = new ResponsiblePartyImpl();
            }

            TelephoneImpl telephone = null;
            AddressImpl address = null;
            ContactImpl contact = new ContactImpl();

            for (int i = 0; i < value.length; i++) {

                contactPerson.setContactInfo(contact);

                if (sameName(elems[1], value[i])) {
                    String positionName = (String) value[i].getValue();
                    contactPerson.setPositionName(new SimpleInternationalString(positionName));
                }

                if (sameName(elems[2], value[i])) {
                    address = (AddressImpl) value[i].getValue();
                }

                if (sameName(elems[3], value[i])) {
                    Collection voices = Collections.singleton(value[i].getValue());
                    if (telephone == null) {
                        telephone = new TelephoneImpl();
                    }
                    telephone.setVoices(voices);
                }

                if (sameName(elems[4], value[i])) {
                    Collection fax = Collections.singleton(value[i].getValue());
                    if (telephone == null) {
                        telephone = new TelephoneImpl();
                    }
                    telephone.setFacsimiles(fax);
                }

                contact.setPhone(telephone);
            }

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[5], value[i])) {
                    String email = (String) value[i].getValue();

                    if (address == null) {
                        address = new AddressImpl();
                    }
                    address.setElectronicMailAddresses(Collections.singleton(email));
                }
            }
            contact.setAddress(address);

            return contactPerson;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ContactInformation";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return ResponsiblePartyImpl.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _ContactPersonPrimaryType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ContactPersonPrimaryType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("ContactPerson", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement(
                            "ContactOrganization", XSISimpleTypes.String.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            ResponsiblePartyImpl responsibleParty = new ResponsiblePartyImpl();

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    String name = (String) value[i].getValue();
                    responsibleParty.setIndividualName(name);
                }

                if (sameName(elems[1], value[i])) {
                    String organization = (String) value[i].getValue();
                    responsibleParty.setOrganisationName(
                            new SimpleInternationalString(organization));
                }
            }

            return responsibleParty;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ContactPersonPrimary";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return ResponsiblePartyImpl.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _ContactAddressType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ContactAddressType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("AddressType", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("Address", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("City", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement(
                            "StateOrProvince", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("PostCode", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("Country", XSISimpleTypes.String.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            AddressImpl address = new AddressImpl();

            for (int i = 0; i < value.length; i++) {
                //				if (sameName(elems[0], value[i])) {
                //					String addressType = (String) value[0].getValue();
                //					//nothing to do with address Type - it does not fit into the GeoAPI citation
                // model
                //				}

                if (sameName(elems[1], value[i])) {
                    String address1 = (String) value[i].getValue();
                    address.setDeliveryPoints(Collections.singleton(address1));
                }

                if (sameName(elems[2], value[i])) {
                    String city = (String) value[i].getValue();
                    address.setCity(new SimpleInternationalString(city));
                }

                if (sameName(elems[3], value[i])) {
                    String state = (String) value[i].getValue();
                    address.setAdministrativeArea(new SimpleInternationalString(state));
                }

                if (sameName(elems[4], value[i])) {
                    String postalCode = (String) value[i].getValue();
                    address.setPostalCode(postalCode);
                }

                if (sameName(elems[5], value[i])) {
                    String country = (String) value[i].getValue();
                    address.setCountry(new SimpleInternationalString(country));
                }
            }

            return address;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ContactAddress";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return AddressImpl.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _CapabilityType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _CapabilityType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Request", _RequestType.getInstance()),
                    new WMSSchema.WMSElement("Exception", _ExceptionType.getInstance()),
                    new WMSSchema.WMSElement(
                            "VendorSpecificCapabilities",
                            _VendorSpecificCapabilitiesType.getInstance(),
                            0,
                            1),
                    new WMSSchema.WMSElement(
                            "UserDefinedSymbolization",
                            _UserDefinedSymbolizationType.getInstance(),
                            0,
                            1),
                    new WMSSchema.WMSElement(
                            "_ExtendedCapabilities",
                            __ExtendedCapabilitiesType.getInstance(),
                            0,
                            Integer.MAX_VALUE),
                    new WMSSchema.WMSElement("Layer", _LayerType.getInstance(), 0, 1)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            WMSCapabilities capabilities = new WMSCapabilities();

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    capabilities.setRequest((WMSRequest) value[i].getValue());
                }

                if (sameName(elems[1], value[i])) {
                    capabilities.setExceptions((String[]) value[i].getValue());
                }

                // if (sameName(elems[2], value[i])) {
                // TODO ExtendedCapabilities ignored
                // }
                // if (sameName(elems[3], value[i])) {
                // TODO VendorSpecificCapabilities ignored
                // }
                // if (sameName(elems[4], value[i])) {
                // TODO UserDefinedSymbolization ignored
                // }

                if (sameName(elems[5], value[i])) {

                    Layer layer = (Layer) value[i].getValue();

                    capabilities.setLayer(layer);
                }
            }

            return capabilities;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Capability";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return WMSCapabilities.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _VendorSpecificCapabilitiesType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new __ExtendedCapabilitiesType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "VendorSpecificCapabilities";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _UserDefinedSymbolizationType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new __ExtendedCapabilitiesType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "SupportedSLDVersion",
                            XSISimpleTypes.String.getInstance(),
                            0,
                            Integer.MAX_VALUE)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "SupportSLD",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "UserLayer",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "UserStyle",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "RemoteWFS",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "RemoteWCS",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false)
                };

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "UserDefinedSymbolization";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class __ExtendedCapabilitiesType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new __ExtendedCapabilitiesType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        public boolean isAbstract() {
            return true;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _RequestType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _RequestType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("GetCapabilities", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GetMap", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GetFeatureInfo", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("DescribeLayer", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GetLegendGraphic", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("GetStyles", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("PutStyles", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "_ExtendedOperation",
                            OperationType.getInstance(),
                            0,
                            Integer.MAX_VALUE),
                    new WMSSchema.WMSElement("Capabilities", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("Map", OperationType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("FeatureInfo", OperationType.getInstance(), 0, 1)
                };

        private static Sequence seq =
                new SequenceGT(
                        new ElementGrouping[] {
                            new ChoiceGT(
                                    new ElementGrouping[] {
                                        new SequenceGT(new ElementGrouping[] {elems[9], elems[8]}),
                                        new SequenceGT(new ElementGrouping[] {elems[0], elems[1]})
                                    }),
                            new ChoiceGT(null, 0, 1, new Element[] {elems[2], elems[10]}),
                            elems[3],
                            elems[4],
                            elems[5],
                            elems[6],
                            elems[7]
                        });

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            WMSRequest request = new WMSRequest();

            for (int i = 0; i < value.length; i++) {
                // System.out.println("OpType ValueName:"
                // +value[i].getElement().getName());

                if (sameName(elems[0], value[i]) || sameName(elems[8], value[i])) {
                    request.setGetCapabilities(
                            (org.geotools.data.ows.OperationType) value[i].getValue());
                }

                if (sameName(elems[1], value[i]) || sameName(elems[9], value[i])) {
                    request.setGetMap((org.geotools.data.ows.OperationType) value[i].getValue());
                }

                if (sameName(elems[2], value[i]) || sameName(elems[10], value[i])) {
                    request.setGetFeatureInfo(
                            (org.geotools.data.ows.OperationType) value[i].getValue());
                }

                if (sameName(elems[3], value[i])) {
                    request.setDescribeLayer(
                            (org.geotools.data.ows.OperationType) value[i].getValue());
                }

                if (sameName(elems[4], value[i])) {
                    request.setGetLegendGraphic(
                            (org.geotools.data.ows.OperationType) value[i].getValue());
                }

                if (sameName(elems[5], value[i])) {
                    request.setGetStyles((org.geotools.data.ows.OperationType) value[i].getValue());
                }

                if (sameName(elems[6], value[i])) {
                    request.setPutStyles((org.geotools.data.ows.OperationType) value[i].getValue());
                }

                // TODO extended operations here
            }

            return request;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Request";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return WMSRequest.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _DCPTypeType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _DCPTypeType();

        private static Element[] elems =
                new Element[] {new WMSSchema.WMSElement("HTTP", _HTTPType.getInstance())};

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return value[0].getValue();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "HTTP";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return org.geotools.data.ows.OperationType.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _HTTPType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _HTTPType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Get", _GetType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("Post", _PostType.getInstance(), 0, 1)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            org.geotools.data.ows.OperationType operationType =
                    new org.geotools.data.ows.OperationType();

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    operationType.setGet((URL) value[i].getValue());
                }

                if (sameName(elems[1], value[i])) {
                    operationType.setPost((URL) value[i].getValue());
                }
            }

            return operationType;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "HTTP";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return org.geotools.data.ows.OperationType.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _GetType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _GetType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "OnlineResource", _OnlineResourceType.getInstance(), 0, 1)
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attributes =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "onlineResource",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.OPTIONAL,
                            null,
                            null,
                            false)
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            try {
                return new URL(attrs.getValue("onlineResource"));
            } catch (MalformedURLException e) {
            }

            return (URL) value[0].getValue();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Get";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return URL.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _PostType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _PostType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Attribute[] attributes =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "onlineResource",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.OPTIONAL,
                            null,
                            null,
                            false)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            try {
                return new URL(attrs.getValue("onlineResource"));
            } catch (MalformedURLException e) {
            }

            return (URL) value[0].getValue();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Post";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return URL.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _ExceptionType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ExceptionType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "Format", _FormatType.getInstance(), 1, Integer.MAX_VALUE)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            String[] formatStrings = new String[value.length];

            for (int i = 0; i < value.length; i++) {
                Object[] stringValues = (Object[]) value[i].getValue();
                for (int ii = 0; ii < stringValues.length; ii++) {
                    formatStrings[i] = (String) stringValues[ii];
                }
            }

            return formatStrings;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Exception";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String[].class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _LayerType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _LayerType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "Name", XSISimpleTypes.String.getInstance(), 0, 1), // 0
                    new WMSSchema.WMSElement("Title", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("Abstract", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("KeywordList", _KeywordListType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "CRS", XSISimpleTypes.String.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "EX_GeographicBoundingBox",
                            _EX_GeographicBoundingBoxType.getInstance(),
                            0,
                            1), // 5
                    new WMSSchema.WMSElement(
                            "BoundingBox", _BoundingBoxType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "Dimension", _DimensionType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "Extent", _ExtentType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement("Attribution", _AttributionType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "AuthorityURL",
                            _AuthorityURLType.getInstance(),
                            0,
                            Integer.MAX_VALUE), // 10
                    new WMSSchema.WMSElement(
                            "Identifier", _IdentifierType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "MetadataURL", _MetadataURLType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "DataURL", _DataURLType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "FeatureListURL",
                            _FeatureListURLType.getInstance(),
                            0,
                            Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "Style", _StyleType.getInstance(), 0, Integer.MAX_VALUE), // 15
                    new WMSSchema.WMSElement(
                            "MinScaleDenominator", XSISimpleTypes.Double.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "MaxScaleDenominator", XSISimpleTypes.Double.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "Layer", _LayerType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "SRS", XSISimpleTypes.String.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "LatLonBoundingBox", _LatLonBoundingBoxType.getInstance(), 0, 1), // 20
                    new WMSSchema.WMSElement("ScaleHint", _ScaleHintType.getInstance(), 0, 1)
                };

        private static Sequence seq =
                new SequenceGT(
                        new ElementGrouping[] {
                            elems[0],
                            elems[1],
                            elems[2],
                            elems[3],
                            new ChoiceGT(
                                    null,
                                    0,
                                    Integer.MAX_VALUE,
                                    new Element[] {elems[4], elems[19]}),
                            new ChoiceGT(null, 0, 1, new Element[] {elems[5], elems[20]}),
                            elems[6],
                            elems[7],
                            elems[8],
                            elems[9],
                            elems[10],
                            elems[11],
                            elems[12],
                            elems[13],
                            elems[14],
                            elems[15],
                            elems[16],
                            elems[17],
                            elems[18],
                            elems[21]
                        });

        private static Attribute[] attributes =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "queryable",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.REQUIRED,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            "cascaded", XSISimpleTypes.NonNegativeInteger.getInstance()),
                    new WMSSchema.WMSAttribute(
                            null,
                            "opaque",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.REQUIRED,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "noSubSets",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.REQUIRED,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            "fixedWidth", XSISimpleTypes.NonNegativeInteger.getInstance()),
                    new WMSSchema.WMSAttribute(
                            "fixedHeight", XSISimpleTypes.NonNegativeInteger.getInstance())
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        public Attribute[] getAttributes() {
            return attributes;
        }

        public ElementGrouping getChild() {
            return seq;
        }

        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            List childLayers = new ArrayList();

            Layer layer = new Layer();

            Set crs = new TreeSet();
            HashMap boundingBoxes = new HashMap();
            Map<String, Dimension> dimensions = new HashMap<>();
            HashMap extents = new HashMap();
            List styles = new ArrayList();
            List metadataURLS = new ArrayList();

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    layer.setName((String) value[i].getValue());
                }

                if (sameName(elems[1], value[i])) {
                    layer.setTitle((String) value[i].getValue());
                }

                if (sameName(elems[2], value[i])) {
                    layer.set_abstract((String) value[i].getValue());
                }
                if (sameName(elems[3], value[i])) {
                    layer.setKeywords((String[]) value[i].getValue());
                }

                if (sameName(elems[4], value[i]) || sameName(elems[19], value[i])) {
                    String[] crss = ((String) value[i].getValue()).split(" ");
                    for (int j = 0; j < crss.length; j++) {
                        crs.add(crss[j].toUpperCase());
                    }
                }

                if (sameName(elems[5], value[i]) || sameName(elems[20], value[i])) {
                    layer.setLatLonBoundingBox((CRSEnvelope) value[i].getValue());
                }

                if (sameName(elems[6], value[i])) {
                    CRSEnvelope bbox = (CRSEnvelope) value[i].getValue();

                    boundingBoxes.put(bbox.getEPSGCode(), bbox);
                }

                if (sameName(elems[7], value[i])) {
                    Dimension dim = (Dimension) value[i].getValue();
                    dimensions.put(dim.getName(), dim);
                }
                if (sameName(elems[8], value[i])) {
                    Extent ext = (Extent) value[i].getValue();
                    extents.put(ext.getName(), ext);
                    // NOTE: dim might be null here, because at this point a sublayer without
                    // dimension tags may not yet have inherited parent dimensions.
                    // but if we have a dimension, we use it
                    Dimension dim = dimensions.get(ext.getName());
                    if (dim != null) {
                        dim.setExtent(ext);
                    }
                }
                if (sameName(elems[9], value[i])) {
                    layer.setAttribution((Attribution) value[i].getValue());
                }
                // if (sameName(elems[10], value[i])) {
                // //TODO authorityURL ignored
                // }
                // if (sameName(elems[11], value[i])) {
                // //TODO identifier ignored
                // }
                if (sameName(elems[12], value[i])) {
                    MetadataURL metadataUrl = (MetadataURL) value[i].getValue();
                    metadataURLS.add(metadataUrl);
                }
                // if (sameName(elems[13], value[i])) {
                // //TODO dataURL ignored
                // }
                // if (sameName(elems[14], value[i])) {
                // //TODO featureLIstURL ignored
                // }

                if (sameName(elems[15], value[i])) {
                    styles.add(value[i].getValue());
                }
                if (sameName(elems[16], value[i])) {
                    Double min = (Double) value[i].getValue();
                    layer.setScaleDenominatorMin(min.doubleValue());
                }
                if (sameName(elems[17], value[i])) {
                    Double max = (Double) value[i].getValue();
                    layer.setScaleDenominatorMax(max.doubleValue());
                }
                if (sameName(elems[18], value[i])) {
                    Layer childLayer = (Layer) value[i].getValue();
                    childLayer.setParent(layer);
                    childLayers.add(childLayer);
                }
                if (sameName(elems[21], value[i])) {
                    double[] scaleHint = (double[]) value[i].getValue();

                    layer.setScaleDenominatorMin(scaleHint[0]);
                    layer.setScaleDenominatorMax(scaleHint[1]);
                }
            }

            layer.setSrs(crs);
            layer.setBoundingBoxes(boundingBoxes);
            layer.setDimensions(dimensions);
            layer.setExtents(extents);
            layer.setStyles(styles);
            layer.setMetadataURL(metadataURLS);

            layer.setChildren((Layer[]) childLayers.toArray(new Layer[childLayers.size()]));

            // Attributes -- only do queryable for now

            // Do not set queryable unless it is explicitly specified
            String queryable = attrs.getValue("queryable");
            if (queryable != null) {
                if ("1".equals(queryable) || "true".equalsIgnoreCase(queryable)) {
                    layer.setQueryable(true);
                } else if ("0".equals(queryable) || "false".equalsIgnoreCase(queryable)) {
                    layer.setQueryable(false);
                }
            }
            String cascaded = attrs.getValue("cascaded");
            int cascadedValue = 0;
            if (cascaded != null) {
                try {
                    cascadedValue = Integer.parseInt(cascaded);
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
            layer.setCascaded(cascadedValue);
            return layer;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Layer";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Layer.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _EX_GeographicBoundingBoxType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance =
                new _EX_GeographicBoundingBoxType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("westBoundLongitude", LongitudeType.getInstance()),
                    new WMSSchema.WMSElement("eastBoundLongitude", LongitudeType.getInstance()),
                    new WMSSchema.WMSElement("southBoundLatitude", LatitudeType.getInstance()),
                    new WMSSchema.WMSElement("northBoundLatitude", LatitudeType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            CRSEnvelope bbox = new CRSEnvelope();

            for (int i = 0; i < value.length; i++) {
                if (sameName(elems[0], value[i])) {
                    bbox.setMinX(((Double) value[i].getValue()).doubleValue());
                }
                if (sameName(elems[1], value[i])) {
                    bbox.setMaxX(((Double) value[i].getValue()).doubleValue());
                }
                if (sameName(elems[2], value[i])) {
                    bbox.setMinY(((Double) value[i].getValue()).doubleValue());
                }
                if (sameName(elems[3], value[i])) {
                    bbox.setMaxY(((Double) value[i].getValue()).doubleValue());
                }
            }

            return bbox;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "EX_GeographicBoundingBox";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return CRSEnvelope.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _LatLonBoundingBoxType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _LatLonBoundingBoxType();

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "minx",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "miny",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "maxx",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "maxy",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            CRSEnvelope bbox = new CRSEnvelope();

            bbox.setMinX(Double.parseDouble(attrs.getValue("minx")));
            bbox.setMaxX(Double.parseDouble(attrs.getValue("maxx")));
            bbox.setMinY(Double.parseDouble(attrs.getValue("miny")));
            bbox.setMaxY(Double.parseDouble(attrs.getValue("maxy")));

            return bbox;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LatLonBoundingBox";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return CRSEnvelope.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _BoundingBoxType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _BoundingBoxType();

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "CRS",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.OPTIONAL,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "SRS",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.OPTIONAL,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "minx",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "miny",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "maxx",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "maxy",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Double.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute("resx", XSISimpleTypes.Double.getInstance()),
                    new WMSSchema.WMSAttribute("resy", XSISimpleTypes.Double.getInstance())
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            CRSEnvelope bbox = new CRSEnvelope();

            String crs = attrs.getValue("CRS");
            if (crs == null || crs.length() == 0) {
                crs = attrs.getValue("SRS");

                if (crs == null || crs.length() == 0) {
                    throw new SAXException("Bounding Box element contains no CRS/SRS attribute");
                }
            }

            bbox.setEPSGCode(crs.toUpperCase());
            bbox.setMinX(Double.parseDouble(attrs.getValue("minx")));
            bbox.setMaxX(Double.parseDouble(attrs.getValue("maxx")));
            bbox.setMinY(Double.parseDouble(attrs.getValue("miny")));
            bbox.setMaxY(Double.parseDouble(attrs.getValue("maxy")));

            return bbox;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "BoundingBox";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return CRSEnvelope.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _DimensionType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _DimensionType();

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "name",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "units",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute("unitSymbol", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSAttribute("default", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSAttribute("current", XSISimpleTypes.Boolean.getInstance()),
                    new WMSSchema.WMSAttribute(
                            null,
                            "multipleValues",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "nearestValue",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false)
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return true;
        }
        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            String name = attrs.getValue("name");
            if (name == null || name.length() == 0) {
                throw new SAXException("Dimension element contains no 'name' attribute");
            }

            String units = attrs.getValue("units");
            if (units == null || units.length() == 0) {
                throw new SAXException("Dimension element contains no 'units' attribute");
            }

            Dimension dim = new Dimension(name, units);
            Boolean current = Boolean.parseBoolean(attrs.getValue("current"));
            if (current != null) {
                dim.setCurrent(current);
            }

            String unitSymbol = attrs.getValue("unitSymbol");
            if (unitSymbol != null && unitSymbol.length() > 0) {
                dim.setUnitSymbol(unitSymbol);
            }

            // We delegate to _ExtentType to fetch properties and value since document structure
            // differs between WMS Spec. 1.1.1 and 1.3.0
            Extent ext = (Extent) _ExtentType.instance.getValue(element, value, attrs, hints);
            if (!ext.isEmpty()) {
                // only use extent if it actually aquired a value
                dim.setExtent(ext);
            }
            return dim;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Dimension";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Dimension.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _ExtentType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ExtentType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "name",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.OPTIONAL,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "default",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.OPTIONAL,
                            null,
                            null,
                            false),
                    new WMSSchema.WMSAttribute(
                            null,
                            "nearestValue",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.Boolean.getInstance(),
                            Attribute.OPTIONAL,
                            "0",
                            null,
                            false)
                };

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            String name = attrs.getValue("name");
            if (name == null || name.length() == 0) {
                throw new SAXException("Dimension element contains no 'name' attribute");
            }
            String defaultValue = attrs.getValue("default");
            boolean multipleValues = "1".equals(attrs.getValue("multipleValues"));
            boolean nearestValue = "1".equals(attrs.getValue("nearestValue"));

            String extractedValue = "";
            for (ElementValue elementValue : value) {
                if (elementValue.getValue() != null) {
                    extractedValue += elementValue.getValue();
                }
            }

            return new Extent(name, defaultValue, multipleValues, nearestValue, extractedValue);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Extent";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Extent.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        public boolean isMixed() {
            return true;
        }
    }

    protected static class _AttributionType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _AttributionType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Title", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "OnlineResource", _OnlineResourceType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("LogoURL", _LogoURLType.getInstance(), 0, 1)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            String title = null;
            URL onlineResource = null;
            LogoURL logoURL = null;

            int length = Math.min(elems.length, value.length);
            for (int i = 0; i < length; i++) {
                if (value[i].getValue() == null || value[i].getElement() == null) {
                    continue;
                }
                if (sameName(elems[0], value[i])) {
                    title = (String) value[i].getValue();
                }

                if (sameName(elems[1], value[i])) {
                    onlineResource = (URL) value[i].getValue();
                }

                if (sameName(elems[2], value[i])) {
                    logoURL = (LogoURL) value[i].getValue();
                }
            }

            return new Attribution(title, onlineResource, logoURL);
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Attribution";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Attribution.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            // TODO Auto-generated method stub
            return true;
        }
    }

    protected static class _LogoURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _LogoURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Format", _FormatType.getInstance()),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            "width", XSISimpleTypes.PositiveInteger.getInstance()),
                    new WMSSchema.WMSAttribute(
                            "height", XSISimpleTypes.PositiveInteger.getInstance())
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            String widthName = attrs.getValue("width");
            int width;
            if (widthName == null || widthName.length() == 0) {
                width = 0;
            } else {
                width = Integer.parseInt(widthName);
            }

            String heightName = attrs.getValue("height");
            int height;
            if (heightName == null || heightName.length() == 0) {
                height = 0;
            } else {
                height = Integer.parseInt(heightName);
            }

            int length = Math.min(elems.length, value.length);

            URL url = null;
            String format = null;

            for (int i = 0; i < length; i++) {
                if (value[i].getValue() == null || value[i].getElement() == null) {
                    continue;
                }
                if (sameName(elems[0], value[i])) {
                    Object[] formObj = (Object[]) value[0].getValue();
                    format =
                            (formObj == null || formObj.length == 0) ? null : (String) (formObj[0]);
                }

                if (sameName(elems[1], value[i])) {
                    url = (URL) value[1].getValue();
                }
            }

            return new LogoURL(format, url, width, height);
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LogoURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return LogoURL.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _MetadataURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _MetadataURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Format", _FormatType.getInstance()),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "type",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.NMTOKEN.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false)
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return true;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            String type = attrs.getValue("type").toString();
            URL url = (URL) value[1].getValue();
            String format = (String) (((Object[]) value[0].getValue())[0]);
            MetadataURL metadataURL = new MetadataURL(url, type, format);

            return metadataURL;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "MetadataURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _AuthorityURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _AuthorityURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "name",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.NMTOKEN.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false)
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "AuthorityURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            // TODO Auto-generated method stub
            return true;
        }
    }

    protected static class _IdentifierType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _IdentifierType();

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            null,
                            "authority",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.REQUIRED,
                            null,
                            null,
                            false)
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Identifier";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return true;
        }
    }

    protected static class _DataURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _DataURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Format", _FormatType.getInstance()),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "DataURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
        /*
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return true;
        }
    }

    protected static class _FeatureListURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _FeatureListURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Format", _FormatType.getInstance()),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "FeatureListURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _StyleType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _StyleType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Name", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("Title", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSElement("Abstract", XSISimpleTypes.String.getInstance(), 0, 1),
                    new WMSSchema.WMSElement(
                            "LegendURL", _LegendURLType.getInstance(), 0, Integer.MAX_VALUE),
                    new WMSSchema.WMSElement(
                            "StyleSheetURL", _StyleSheetURLType.getInstance(), 0, 1),
                    new WMSSchema.WMSElement("StyleURL", _StyleURLType.getInstance(), 0, 1)
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            StyleImpl style = new StyleImpl();
            List legendURLS = new ArrayList();

            for (int i = 0; i < value.length; i++) {

                if (sameName(elems[0], value[i])) {
                    String name = (String) value[i].getValue();
                    style.setName(name);
                }

                if (sameName(elems[1], value[i])) {
                    String title = (String) value[i].getValue();
                    style.setTitle(new SimpleInternationalString(title));
                }

                if (sameName(elems[2], value[i])) {
                    String _abstract = (String) value[i].getValue();
                    style.setAbstract(new SimpleInternationalString(_abstract));
                }

                if (sameName(elems[3], value[i])) {
                    legendURLS.add((String) value[i].getValue());
                }

                //                if (sameName(elems[4], value[i])) {
                //                    // TODO Implement StyleSheet URL
                //                }
                //
                //                if (sameName(elems[5], value[i])) {
                //                    // TODO implement StyleURL
                //                }
            }
            style.setLegendURLs(legendURLS);
            return style;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "Style";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _LegendURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _LegendURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Format", _FormatType.getInstance()),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute(
                            "width", XSISimpleTypes.PositiveInteger.getInstance()),
                    new WMSSchema.WMSAttribute(
                            "height", XSISimpleTypes.PositiveInteger.getInstance())
                };

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            String legendURL = value[1].getValue() == null ? null : value[1].getValue().toString();
            return legendURL;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "LegendURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
        /*
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return true;
        }
    }

    protected static class _StyleSheetURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _StyleSheetURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Format", _FormatType.getInstance()),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "StyleSheetURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _StyleURLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _StyleURLType();

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement("Format", _FormatType.getInstance()),
                    new WMSSchema.WMSElement("OnlineResource", _OnlineResourceType.getInstance())
                };

        private static Sequence seq = new SequenceGT(elems);

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return null;
            // throw new OperationNotSupportedException();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "StyleURL";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
        /*
         * @see org.geotools.xml.schema.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return true;
        }
    }

    protected static class _ScaleHintType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ScaleHintType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute("min", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSAttribute(
                            null,
                            "max",
                            WMSSchema.NAMESPACE,
                            XSISimpleTypes.String.getInstance(),
                            Attribute.OPTIONAL,
                            null,
                            null,
                            false)
                };

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            double[] scaleHint = new double[2];
            scaleHint[0] = Double.parseDouble(attrs.getValue("min"));
            scaleHint[1] = Double.parseDouble(attrs.getValue("max"));
            return scaleHint;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ScaleHint";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return double[].class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _OnlineResourceType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _OnlineResourceType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return XLinkSchema.SimpleLink.getInstance().getAttributes();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {

            if (value != null && value.length >= 1) {
                try {
                    return new URL((String) value[value.length - 1].getValue());
                } catch (MalformedURLException e1) {
                }
            }

            String href = attrs.getValue("", "href");
            href = href != null ? href : attrs.getValue(XLinkSchema.NAMESPACE.toString(), "href");
            try {
                return new URL(href);
            } catch (MalformedURLException e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                return null;
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "OnlineResource";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return URL.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        public boolean isMixed() {
            return true;
        }
    }

    protected static class _WMTException extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _WMTException();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute("version", XSISimpleTypes.String.getInstance())
                };

        public Attribute[] getAttributes() {
            return attrs;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return new ServiceException((String) value[value.length - 1].getValue(), null);
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WMTException";
        }

        public boolean isMixed() {
            return true;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return ServiceException.class;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            // TODO Auto-generated method stub

        }
    }

    protected static class _ServiceExceptionReport extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ServiceExceptionReport();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        private static Element[] elems =
                new Element[] {
                    new WMSSchema.WMSElement(
                            "ServiceException",
                            _ServiceException.getInstance(),
                            0,
                            Integer.MAX_VALUE)
                };

        private static Sequence seq = new SequenceGT(elems);

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute("version", XSISimpleTypes.String.getInstance())
                };

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return seq;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return elems;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            /*
             * ServiceExceptions with codes get bumped to the top of the list.
             */
            List codes = new ArrayList();
            List noCodes = new ArrayList();
            for (int i = 0; i < value.length; i++) {
                ServiceException exception = (ServiceException) value[i].getValue();
                if (exception.getCode() != null && exception.getCode().length() != 0) {
                    codes.add(exception);
                } else {
                    noCodes.add(exception);
                }
            }

            /*
             * Now chain them.
             */
            ServiceException firstException = null;
            ServiceException recentException = null;
            for (int i = 0; i < codes.size(); i++) {
                ServiceException exception = (ServiceException) codes.get(i);
                if (firstException == null) {
                    firstException = exception;
                    recentException = exception;
                } else {
                    recentException.setNext(exception);
                    recentException = exception;
                }
            }
            codes = null;
            for (int i = 0; i < noCodes.size(); i++) {
                ServiceException exception = (ServiceException) noCodes.get(i);
                if (firstException == null) {
                    firstException = exception;
                    recentException = exception;
                } else {
                    recentException.setNext(exception);
                    recentException = exception;
                }
            }
            noCodes = null;

            return firstException;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ServiceExceptionReport";
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return ServiceException.class;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // TODO Auto-generated method stub
            return false;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            // TODO Auto-generated method stub

        }
    }

    protected static class _ServiceException extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _ServiceException();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs =
                new Attribute[] {
                    new WMSSchema.WMSAttribute("code", XSISimpleTypes.String.getInstance()),
                    new WMSSchema.WMSAttribute("location", XSISimpleTypes.String.getInstance())
                };

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return attrs;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element, org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes, java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            String body = (String) value[value.length - 1].getValue();
            String code = attrs.getValue("code");
            String location = attrs.getValue("location");
            return new ServiceException(body, code, location);
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "ServiceException";
        }

        public boolean isMixed() {
            return true;
        }
        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return ServiceException.class;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element, java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // TODO Auto-generated method stub
            return false;
        }

        /* (non-Javadoc)
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element, java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            // TODO Auto-generated method stub

        }
    }

    protected static class _GIFType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _GIFType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("GIF");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GIF";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _JPEGType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _JPEGType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("JPEG");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "JPEG";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _PNGType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _PNGType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("PNG");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "PNG";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _PPMType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _PPMType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("PPM");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "PPM";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _TIFFType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _TIFFType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("TIFF");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "TIFF";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _GeoTIFFType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _GeoTIFFType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("GeoTIFF");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GeoTIFF";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _WebCGMType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _WebCGMType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("WebCGM");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WebCGM";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _SVGType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _SVGType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("SVG");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "SVG";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _WMS_XMLType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _WMS_XMLType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("WMS_XML");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WMS_XML";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _GML_1Type extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _GML_1Type();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("GML.1");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GML.1";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _GML_2Type extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _GML_2Type();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("GML.2");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GML.2";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _GML_3Type extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _GML_3Type();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("GML.3");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "GML.3";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _WBMPType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _WBMPType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("WBMP");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "WBMP";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _BMPType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _BMPType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("BMP");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "BMP";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _MIMEType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _MIMEType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("MIME");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "MIME";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _INIMAGEType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _INIMAGEType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("INIMAGE");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "INIMAGE";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _BLANKType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _BLANKType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("BLANK");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "BLANK";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    protected static class _CW_WKBType extends WMSSchema.WMSComplexType {
        private static final WMSSchema.WMSComplexType instance = new _CW_WKBType();

        public static WMSSchema.WMSComplexType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getAttributes()
         */
        public Attribute[] getAttributes() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.ComplexType#getChildElements()
         */
        public Element[] getChildElements() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return WMS1_0_0.toFormatMIME("CW_WKB");
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "CW_WKB";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return String.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class LongitudeType extends WMSSchema.WMSSimpleType {
        private static SimpleType instance = new LongitudeType();

        private static Facet[] facets =
                new Facet[] {
                    new FacetGT(Facet.MININCLUSIVE, "-180"), new FacetGT(Facet.MAXINCLUSIVE, "180")
                };

        public static SimpleType getInstance() {
            return instance;
        }

        public int getChildType() {
            return Schema.RESTRICTION;
        }

        public SimpleType[] getParents() {
            return null;
        }

        public Facet[] getFacets() {
            return facets;
        }

        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return Double.valueOf((String) value[0].getValue());
        }

        public String getName() {
            return "longitudeType";
        }

        public Class getInstanceType() {
            return Double.class;
        }

        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    static class LatitudeType extends WMSSchema.WMSSimpleType {
        private static SimpleType instance = new LatitudeType();

        private static Facet[] facets =
                new Facet[] {
                    new FacetGT(Facet.MININCLUSIVE, "-90"), new FacetGT(Facet.MAXINCLUSIVE, "90")
                };

        public static SimpleType getInstance() {
            return instance;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.SimpleType#getChildType()
         */
        public int getChildType() {
            return Schema.RESTRICTION;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.SimpleType#getParents()
         */
        public SimpleType[] getParents() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.SimpleType#getFacets()
         */
        public Facet[] getFacets() {
            return facets;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getValue(org.geotools.xml.schema.Element,
         *      org.geotools.xml.schema.ElementValue[], org.xml.sax.Attributes,
         *      java.util.Map)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            return Double.valueOf((String) value[0].getValue());
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getName()
         */
        public String getName() {
            return "latitudeType";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Double.class;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }
}
