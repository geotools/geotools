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
package org.geotools.data.wms.xml;

import java.io.IOException;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.xml.PrintHandler;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.impl.AttributeGT;
import org.geotools.xml.schema.impl.SequenceGT;
import org.geotools.xml.styling.sldSchema;
import org.xml.sax.helpers.AttributesImpl;

public class ogcComplexTypes {

    protected static class VendorType extends ogcComplexType {
        private static ComplexType instance = new VendorType();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = null;
        private static Element[] elems = null;
        private static ElementGrouping child = new SequenceGT(null);

        private VendorType() {
            super("VendorType", child, attrs, elems, null, false, false);
        }
    }
    protected static class _Size extends ogcComplexType {
        private static ComplexType instance = new _Size();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new ogcElement("Width", org.geotools.xml.xsi.XSISimpleTypes.PositiveInteger
                        .getInstance()/* simpleType name is positiveInteger */, null, 1, 1),
                new ogcElement("Height", org.geotools.xml.xsi.XSISimpleTypes.PositiveInteger
                        .getInstance()/* simpleType name is positiveInteger */, null, 1, 1)};

        private static ElementGrouping child = new SequenceGT(null, new ElementGrouping[]{
                new ogcElement("Width", org.geotools.xml.xsi.XSISimpleTypes.PositiveInteger
                        .getInstance()/* simpleType name is positiveInteger */, null, 1, 1),
                new ogcElement("Height", org.geotools.xml.xsi.XSISimpleTypes.PositiveInteger
                        .getInstance()/* simpleType name is positiveInteger */, null, 1, 1)}, 1, 1);

        private _Size() {
            super(null, child, attrs, elems, null, false, false);
        }
    }
    protected static class _Output extends ogcComplexType {
        private static ComplexType instance = new _Output();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new ogcElement("Format", ogcSimpleTypes.FormatType.getInstance(), null, 1, 1),
                new ogcElement("Transparent", org.geotools.xml.xsi.XSISimpleTypes.Boolean
                        .getInstance(), null, 0, 1),
                new ogcElement("BGcolor", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        null, 0, 1),
                new ogcElement("Size", ogcComplexTypes._Size.getInstance(), null, 1, 1)};

        private static ElementGrouping child = new SequenceGT(elems);

        private _Output() {
            super(null, child, attrs, elems, null, false, false);
        }
    }
    protected static class _GetMap extends ogcComplexType {
        private static ComplexType instance = new _GetMap();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = new Attribute[]{
                new AttributeGT(null, "version", OGCSchema.NAMESPACE,
                        org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        Attribute.REQUIRED, null, null, false),
                new AttributeGT(null, "service", OGCSchema.NAMESPACE, ogcSimpleTypes.OWSType
                        .getInstance(), Attribute.REQUIRED, ogcSimpleTypes.OWSType.getInstance()
                        .getFacets()[0].getValue(), ogcSimpleTypes.OWSType.getInstance()
                        .getFacets()[0].getValue(), false)};

        private static Element[] elems = new Element[]{
                new ogcElement("StyledLayerDescriptor", new sldSchema().getSLDType(), null, 1, 1),
                new ogcElement("BoundingBox", org.geotools.xml.gml.GMLComplexTypes.BoxType
                        .getInstance(), null, 1, 1),
                new ogcElement("Output", ogcComplexTypes._Output.getInstance(), null, 1, 1),
                new ogcElement("Exceptions", ogcSimpleTypes.ExceptionsType.getInstance(), null, 0,
                        1),
                new ogcElement("Vendor", ogcComplexTypes.VendorType.getInstance(), null, 0, 1)};

        private static ElementGrouping child = new SequenceGT(elems);

        private _GetMap() {
            super(null, child, attrs, elems, null, false, false);
        }
    }
    protected static class _FeatureInfoSize extends ogcComplexType {
        private static ComplexType instance = new _FeatureInfoSize();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{new ogcElement("FeatureCount", null, null,
                1, 1)};

        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{new ogcElement("FeatureCount", null, null, 1, 1)}, 1, 1);

        private _FeatureInfoSize() {
            super(null, child, attrs, elems, null, false, false);
        }
    }
    protected static class _FeatureInfoOutput extends ogcComplexType {
        private static ComplexType instance = new _FeatureInfoOutput();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = null;
        private static Element[] elems = new Element[]{
                new ogcElement("Format", org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        null, 1, 1),
                new ogcElement("Size", ogcComplexTypes._FeatureInfoSize.getInstance(), null, 1, 1)};

        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{
                        new ogcElement("Format", org.geotools.xml.xsi.XSISimpleTypes.String
                                .getInstance(), null, 1, 1),
                        new ogcElement("Size", ogcComplexTypes._FeatureInfoSize.getInstance(),
                                null, 1, 1)}, 1, 1);

        private _FeatureInfoOutput() {
            super(null, child, attrs, elems, null, false, false);
        }
    }
    protected static class _GetFeatureInfo extends ogcComplexType {
        private static ComplexType instance = new _GetFeatureInfo();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = new Attribute[]{
                new AttributeGT(null, "version", OGCSchema.NAMESPACE,
                        org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        Attribute.REQUIRED, null, null, false),
                new AttributeGT(null, "service", OGCSchema.NAMESPACE, ogcSimpleTypes.OWSType
                        .getInstance(), Attribute.REQUIRED, ogcSimpleTypes.OWSType.getInstance()
                        .getFacets()[0].getValue(), ogcSimpleTypes.OWSType.getInstance()
                        .getFacets()[0].getValue(), false)};

        private static Element[] elems = new Element[]{
                new ogcElement("ogc:GetMap", null, null, 1, 1),
                new ogcElement("QueryLayer", org.geotools.xml.xsi.XSISimpleTypes.String
                        .getInstance()/* simpleType name is string */, null, 1, 2147483647),
                new ogcElement("X", org.geotools.xml.xsi.XSISimpleTypes.NonNegativeInteger
                        .getInstance()/* simpleType name is nonNegativeInteger */, null, 1, 1),
                new ogcElement("Y", org.geotools.xml.xsi.XSISimpleTypes.NonNegativeInteger
                        .getInstance()/* simpleType name is nonNegativeInteger */, null, 1, 1),
                new ogcElement("Output", ogcComplexTypes._FeatureInfoOutput.getInstance(), null, 1,
                        1),
                new ogcElement("Exceptions", org.geotools.xml.xsi.XSISimpleTypes.String
                        .getInstance()/* simpleType name is string */, null, 0, 1),
                new ogcElement("Vendor", null, null, 0, 1)};

        private static ElementGrouping child = new SequenceGT(elems);

        private _GetFeatureInfo() {
            super(null, child, attrs, elems, null, false, false);
        }
    }
    protected static class _GetCapabilities extends ogcComplexType {
        private static ComplexType instance = new _GetCapabilities();
        public static ComplexType getInstance() {
            return instance;
        }

        private static Attribute[] attrs = new Attribute[]{
                new AttributeGT(null, "service", OGCSchema.NAMESPACE, ogcSimpleTypes.OWSType.getInstance(), Attribute.REQUIRED, 
                        null, null, false),
                new AttributeGT(null, "version", OGCSchema.NAMESPACE, org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        Attribute.REQUIRED, null, null, false),
                new AttributeGT(null, "updateSequence", OGCSchema.NAMESPACE, 
                        org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                        Attribute.OPTIONAL, null, null, false)};

        private static Element[] elems = new Element[]{new ogcElement("Section", ogcSimpleTypes.CapabilitiesSectionType.getInstance(), null, 0, 1)};

        private static ElementGrouping child = new SequenceGT(null,
                new ElementGrouping[]{new ogcElement("Section", ogcSimpleTypes.CapabilitiesSectionType.getInstance(), null, 0, 1)}, 1, 1);

        private _GetCapabilities() {
            super(null, child, attrs, elems, null, false, false);
        }
        
        public boolean canEncode( Element element, Object value, Map hints ) {
            if (element.getType() != null && getName().equals(element.getType().getName())) {
                for (int i = 0; i < ogcSimpleTypes.CapabilitiesSectionType.getInstance().getFacets().length; i++) {
                    Facet facet = ogcSimpleTypes.CapabilitiesSectionType.getInstance().getFacets()[i];
                    if (facet.getValue().equals(value)) {
                        return true;
                    }
                }
                
                if (value == null || value == "") { //$NON-NLS-1$
                    return true;
                }
            }
            return super.canEncode(element, value, hints);
        }
        public void encode( Element element, Object value, PrintHandler output, Map hints )
                throws OperationNotSupportedException {
            
            if (canEncode(element, value, hints)) {
                AttributesImpl attributes = new AttributesImpl();
//                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
//                    attrs[0].getName(), null, "string", attrs[0].getFixed());
//                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
//                    attrs[1].getName(), null, "string", attrs[1].getFixed());
//                attributes.addAttribute(WFSSchema.NAMESPACE.toString(),
//                    attrs[2].getName(), null, "string", attrs[2].getFixed());
                
                
                try {
                    output.startElement(element.getNamespace(), element.getName(), attributes);
                    
                    output.endElement(element.getNamespace(), element.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } else {
                throw new UnsupportedOperationException();
            }
            
        }
        public Class getInstanceType() {
            return String.class;
        }
        
        public String getName() {
            return "GetCapabilities";
        }
    }
}
